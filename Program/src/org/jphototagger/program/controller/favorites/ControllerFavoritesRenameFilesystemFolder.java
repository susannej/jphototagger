/*
 * @(#)ControllerFavoritesRenameFilesystemFolder.java    Created on 2009-06-19
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package org.jphototagger.program.controller.favorites;

import org.jphototagger.lib.io.TreeFileSystemDirectories;
import org.jphototagger.program.data.Favorite;
import org.jphototagger.program.factory.ControllerFactory;
import org.jphototagger.program.factory.ModelFactory;
import org.jphototagger.program.io.FileSystemDirectories;
import org.jphototagger.program.model.TreeModelFavorites;
import org.jphototagger.program.view.popupmenus.PopupMenuFavorites;
import org.jphototagger.program.view.ViewUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.EventQueue;

import java.io.File;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Listens to {@link PopupMenuFavorites#getItemRenameFilesystemFolder()} and
 * renames a directory in the file system when the action fires.
 *
 * Also listens to the {@link JTree}'s key events and renames the selected
 * file system directory if the keys <code>Strg+R</code> or <code>F2</code> were
 * pressed.
 *
 * @author  Elmar Baumann
 */
public final class ControllerFavoritesRenameFilesystemFolder
        implements ActionListener, KeyListener {
    public ControllerFavoritesRenameFilesystemFolder() {
        listen();
    }

    private void listen() {
        PopupMenuFavorites.INSTANCE.getItemRenameFilesystemFolder()
            .addActionListener(this);
        ViewUtil.getFavoritesTree().addKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent evt) {
        JTree tree = ViewUtil.getFavoritesTree();

        if (isRename(evt) &&!tree.isSelectionEmpty()) {
            Object node = tree.getSelectionPath().getLastPathComponent();

            if (node instanceof DefaultMutableTreeNode) {
                renameDirectory((DefaultMutableTreeNode) node);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                renameDirectory(
                    TreeFileSystemDirectories.getNodeOfLastPathComponent(
                        PopupMenuFavorites.INSTANCE.getTreePath()));
            }
        });
    }

    private boolean isRename(KeyEvent evt) {
        return evt.getKeyCode() == KeyEvent.VK_F2;
    }

    private void renameDirectory(DefaultMutableTreeNode node) {
        File dir = getFile(node);

        if (dir != null) {
            File newDir = FileSystemDirectories.rename(dir);

            if (newDir != null) {
                TreeModelFavorites model =
                    ModelFactory.INSTANCE.getModel(TreeModelFavorites.class);

                node.setUserObject(newDir);
                TreeFileSystemDirectories.updateInTreeModel(model, node);
                ControllerFactory.INSTANCE.getController(
                    ControllerRefreshFavorites.class).refresh();
            }
        }
    }

    private File getFile(DefaultMutableTreeNode node) {
        Object userObject = node.getUserObject();

        if (userObject instanceof File) {
            return (File) userObject;
        } else if (userObject instanceof Favorite) {
            return ((Favorite) userObject).getDirectory();
        }

        return null;
    }

    @Override
    public void keyTyped(KeyEvent evt) {

        // ignore
    }

    @Override
    public void keyReleased(KeyEvent evt) {

        // ignore
    }
}
