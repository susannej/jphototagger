package org.jphototagger.program.controller.imagecollection;

import org.jphototagger.lib.componentutil.ListUtil;
import org.jphototagger.program.app.AppLogger;
import org.jphototagger.program.factory.ModelFactory;
import org.jphototagger.program.model.ListModelImageCollections;
import org.jphototagger.program.view.popupmenus.PopupMenuImageCollections;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.EventQueue;

import javax.swing.JList;
import org.jphototagger.program.helper.ImageCollectionsHelper;
import org.jphototagger.program.resource.GUI;

/**
 * Kontrolliert Aktion: Lösche Bildsammlung, ausgelöst von
 * {@link org.jphototagger.program.view.popupmenus.PopupMenuImageCollections}.
 *
 * Also listens to the {@link JList}'s key events and deletes the selected image
 * collection when the keys <code>Ctrl+N</code> were pressed.
 *
 * @author Elmar Baumann
 */
public final class ControllerDeleteImageCollection
        implements ActionListener, KeyListener {
    public ControllerDeleteImageCollection() {
        listen();
    }

    private void listen() {
        PopupMenuImageCollections.INSTANCE.getItemDelete().addActionListener(
            this);
        GUI.getImageCollectionsList().addKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent evt) {
        JList list = GUI.getImageCollectionsList();

        if ((evt.getKeyCode() == KeyEvent.VK_DELETE)
                &&!list.isSelectionEmpty()) {
            Object value = list.getSelectedValue();

            if (value instanceof String) {
                deleteCollection((String) value);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        deleteCollection(
            ListUtil.getItemString(
                GUI.getImageCollectionsList(),
                PopupMenuImageCollections.INSTANCE.getItemIndex()));
    }

    private void deleteCollection(final String collectionName) {
        if (!ListModelImageCollections.checkIsNotSpecialCollection(
                collectionName,
                "ControllerDeleteImageCollection.Error.SpecialCollection")) {
            return;
        }

        if (collectionName != null) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (ImageCollectionsHelper.deleteImageCollection(
                            collectionName)) {
                        ModelFactory.INSTANCE.getModel(
                            ListModelImageCollections.class).removeElement(
                            collectionName);
                    }
                }
            });
        } else {
            AppLogger.logWarning(
                ControllerDeleteImageCollection.class,
                "ControllerDeleteImageCollection.Error.CollectionNameIsNull");
        }
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
