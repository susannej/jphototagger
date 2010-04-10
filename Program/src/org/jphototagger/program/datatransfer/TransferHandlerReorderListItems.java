/*
 * @(#)TransferHandlerReorderListItems.java    Created on 2010-04-10
 *
 * Copyright (C) 2009-2010 by the JPhotoTagger developer team.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package org.jphototagger.program.datatransfer;

import org.jphototagger.lib.datatransfer.TransferableObject;
import org.jphototagger.program.app.AppLogger;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

/**
 * Reorders in a list with a {@link DefaultListModel} dragged and dropped items
 * from the drag location to the drop location.
 *
 * @author Elmar Baumann
 */
public final class TransferHandlerReorderListItems extends TransferHandler {
    private static final long       serialVersionUID = -5420770026801383911L;
    private static final DataFlavor INDICES_FLAVOR = new DataFlavor(LIST.class,
                                                         null);
    private final JList list;

    /**
     * Usage in DataFlavor in order that importData() will be called
     */
    private final class LIST {}

    private final class IndexInfo {
        private final JList source;
        private final int[] selIndices;

        public IndexInfo(JList source, int[] selIndices) {
            this.source     = source;
            this.selIndices = selIndices;
        }
    }


    public TransferHandlerReorderListItems(JList list) {
        if (list == null) {
            throw new NullPointerException("list == null");
        }

        boolean modelOk = list.getModel() instanceof DefaultListModel;

        if (!modelOk) {
            throw new IllegalArgumentException("Not a DefaultListModel: "
                                               + list.getModel());
        }

        this.list = list;
    }

    @Override
    public boolean canImport(TransferSupport support) {
        JList sourceList = getSourceList(support);

        return support.isDrop() && (list == sourceList)
               && support.isDataFlavorSupported(INDICES_FLAVOR);
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        JList sourceList = (JList) c;
        int[] selIndices = sourceList.getSelectedIndices();

        if (selIndices.length < 1) {
            return null;
        }

        return new TransferableObject(new IndexInfo(sourceList, selIndices),
                                      INDICES_FLAVOR);
    }

    @Override
    public boolean importData(TransferSupport support) {
        IndexInfo indexInfo = null;

        try {
            Object td =
                support.getTransferable().getTransferData(INDICES_FLAVOR);

            if (td instanceof IndexInfo) {
                indexInfo = (IndexInfo) td;
            }
        } catch (Exception ex) {
            AppLogger.logSevere(getClass(), ex);
        }

        if (indexInfo != null) {
            DropLocation dl = (JList.DropLocation) support.getDropLocation();
            int          dropIndex = list.locationToIndex(dl.getDropPoint());

            if (dropIndex >= 0) {
                reorder(dropIndex, indexInfo.selIndices);

                return true;
            }
        }

        return false;
    }

    private void reorder(int dropIndex, int[] selIndices) {
        if (selIndices.length < 1) {
            return;
        }

        Arrays.sort(selIndices);

        DefaultListModel model     = (DefaultListModel) list.getModel();
        List<Object>     selValues = new ArrayList<Object>(selIndices.length);

        for (int index : selIndices) {
            try {
                selValues.add(model.get(index));
            } catch (Exception e) {
                AppLogger.logSevere(getClass(), e);
            }
        }

        for (Object selValue : selValues) {
            model.removeElement(selValue);
        }

        int insertIndex = (dropIndex >= list.getModel().getSize())
                          ? list.getModel().getSize()
                          : dropIndex;

        for (Object selValue : selValues) {
            model.insertElementAt(selValue, insertIndex++);
        }
    }

    @Override
    public int getSourceActions(JComponent c) {
        return (c == list)
               ? TransferHandler.MOVE
               : TransferHandler.NONE;
    }

    private JList getSourceList(TransferSupport support) {
        try {
            Object td =
                support.getTransferable().getTransferData(INDICES_FLAVOR);

            if (td instanceof IndexInfo) {
                return ((IndexInfo) td).source;
            }
        } catch (Exception ex) {
            AppLogger.logSevere(getClass(), ex);
        }

        return null;
    }
}
