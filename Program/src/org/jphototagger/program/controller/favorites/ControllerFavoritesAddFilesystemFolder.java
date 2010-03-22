/*
 * @(#)ControllerFavoritesAddFilesystemFolder.java    Created on 2009-06-30
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

package org.jphototagger.program.controller.favorites;

import org.jphototagger.program.factory.ModelFactory;
import org.jphototagger.program.model.TreeModelFavorites;
import org.jphototagger.program.resource.GUI;
import org.jphototagger.program.view.popupmenus.PopupMenuFavorites;
import org.jphototagger.lib.event.util.KeyEventUtil;
import org.jphototagger.lib.io.TreeFileSystemDirectories;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * Listens to {@link PopupMenuFavorites#getItemAddFilesystemFolder()} and
 * creates a directory into the file system when the action fires.
 *
 * Also listens to the {@link JTree}'s key events and inserts a new directory
 * into the selected file system directory if the keys <code>Ctrl+N</code> were
 * pressed.
 *
 * @author  Elmar Baumann
 */
public final class ControllerFavoritesAddFilesystemFolder
        implements ActionListener, KeyListener {
    private final PopupMenuFavorites popup = PopupMenuFavorites.INSTANCE;
    private final JTree              tree  =
        GUI.INSTANCE.getAppPanel().getTreeFavorites();

    public ControllerFavoritesAddFilesystemFolder() {
        listen();
    }

    private void listen() {
        popup.getItemAddFilesystemFolder().addActionListener(this);
        tree.addKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (KeyEventUtil.isControl(e, KeyEvent.VK_N)
                &&!tree.isSelectionEmpty()) {
            Object node = tree.getSelectionPath().getLastPathComponent();

            if (node instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode pathNode = (DefaultMutableTreeNode) node;

                createDirectory(new TreePath(pathNode.getPath()));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        createDirectory(popup.getTreePath());
    }

    private void createDirectory(TreePath path) {
        TreeModelFavorites model =
            ModelFactory.INSTANCE.getModel(TreeModelFavorites.class);

        model.createNewDirectory(
            TreeFileSystemDirectories.getNodeOfLastPathComponent(path));
    }

    @Override
    public void keyTyped(KeyEvent e) {

        // ignore
    }

    @Override
    public void keyReleased(KeyEvent e) {

        // ignore
    }
}