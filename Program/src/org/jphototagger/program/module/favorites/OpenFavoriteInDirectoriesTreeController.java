package org.jphototagger.program.module.favorites;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.jphototagger.domain.favorites.Favorite;
import org.jphototagger.lib.awt.EventQueueUtil;
import org.jphototagger.lib.swing.AllSystemDirectoriesTreeModel;
import org.jphototagger.lib.swing.KeyEventUtil;
import org.jphototagger.program.app.ui.AppPanel;
import org.jphototagger.program.factory.ModelFactory;
import org.jphototagger.program.resource.GUI;

/**
 * Listens to the {@code FavoritesPopupMenu} and opens the
 * selected favorite directory in the folder panel when the special menu item
 * was clicked.
 *
 * Also listens to the {@code JTree}'s key events and opens the selected folder
 * in the directorie's tree if the keys <code>Ctrl+O</code> were pressed.
 *
 * @author Elmar Baumann
 */
public final class OpenFavoriteInDirectoriesTreeController implements ActionListener, KeyListener {

    public OpenFavoriteInDirectoriesTreeController() {
        listen();
    }

    private void listen() {
        FavoritesPopupMenu.INSTANCE.getItemOpenInFolders().addActionListener(this);
        GUI.getFavoritesTree().addKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent evt) {
        if (KeyEventUtil.isMenuShortcut(evt, KeyEvent.VK_O) && !GUI.getFavoritesTree().isSelectionEmpty()) {
            selectDirectory();
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (GUI.getFavoritesTree().getSelectionCount() >= 0) {
            selectDirectory();
        }
    }

    private void selectDirectory() {
        EventQueueUtil.invokeInDispatchThread(new Runnable() {

            @Override
            public void run() {
                TreePath path = FavoritesPopupMenu.INSTANCE.getTreePath();

                if (path != null) {
                    File dir = getDir((DefaultMutableTreeNode) path.getLastPathComponent());

                    if ((dir != null) && dir.isDirectory()) {
                        expandTreeToDir(dir);
                    }
                }
            }

            private File getDir(DefaultMutableTreeNode node) {
                Object userObject = node.getUserObject();

                if (userObject instanceof File) {
                    return (File) userObject;
                } else if (userObject instanceof Favorite) {
                    Favorite favoriteDirectory = (Favorite) userObject;

                    return favoriteDirectory.getDirectory();
                }

                return null;
            }

            private void expandTreeToDir(File dir) {
                AppPanel appPanel = GUI.getAppPanel();
                JTabbedPane tabbedPaneSelection = appPanel.getTabbedPaneSelection();
                Component tabTreeDirectories = appPanel.getTabSelectionDirectories();

                GUI.getFavoritesTree().clearSelection();
                tabbedPaneSelection.setSelectedComponent(tabTreeDirectories);
                ModelFactory.INSTANCE.getModel(AllSystemDirectoriesTreeModel.class).expandToFile(dir, true);
            }
        });
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
