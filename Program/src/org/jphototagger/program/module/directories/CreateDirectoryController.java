package org.jphototagger.program.module.directories;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jphototagger.lib.awt.EventQueueUtil;
import org.jphototagger.lib.swing.AllSystemDirectoriesTreeModel;
import org.jphototagger.lib.swing.KeyEventUtil;
import org.jphototagger.program.factory.ControllerFactory;
import org.jphototagger.program.factory.ModelFactory;
import org.jphototagger.program.module.favorites.AddFilesystemFolderToFavoritesController;

/**
 * Listens to {@code DirectoriesPopupMenu#getItemCreateDirectory()} and
 * creates a directory when the action fires.
 *
 * Also listens to the {@code JTree}'s key events and creates a new directory
 * into the selected directory when the keys <code>Ctrl+N</code> was pressed.
 *
 * @author Elmar Baumann
 */
public final class CreateDirectoryController extends DirectoryController {

    public CreateDirectoryController() {
        listenToActionsOf(DirectoriesPopupMenu.INSTANCE.getItemCreateDirectory());
    }

    @Override
    protected boolean myKey(KeyEvent evt) {
        if (evt == null) {
            throw new NullPointerException("evt == null");
        }

        return KeyEventUtil.isMenuShortcut(evt, KeyEvent.VK_N);
    }

    @Override
    protected boolean myAction(ActionEvent evt) {
        if (evt == null) {
            throw new NullPointerException("evt == null");
        }

        return evt.getSource() == DirectoriesPopupMenu.INSTANCE.getItemCreateDirectory();
    }

    @Override
    protected void action(final DefaultMutableTreeNode node) {
        if (node == null) {
            throw new NullPointerException("node == null");
        }

        EventQueueUtil.invokeInDispatchThread(new Runnable() {

            @Override
            public void run() {
                File dir = ModelFactory.INSTANCE.getModel(AllSystemDirectoriesTreeModel.class).createDirectoryIn(node);

                if (dir != null) {
                    AddFilesystemFolderToFavoritesController ctrl =
                            ControllerFactory.INSTANCE.getController(AddFilesystemFolderToFavoritesController.class);

                    if (ctrl != null) {
                        ctrl.confirmMoveSelFilesInto(dir);
                    }
                }
            }
        });
    }
}
