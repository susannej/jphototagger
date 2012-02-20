package org.jphototagger.program.module.filesystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Collections;
import java.util.List;

import org.bushe.swing.event.annotation.AnnotationProcessor;

import org.jphototagger.program.module.thumbnails.ThumbnailsPopupMenu;
import org.jphototagger.program.resource.GUI;

/**
 * @author Elmar Baumann
 */
public final class RenameFilesController implements ActionListener, KeyListener {

    public RenameFilesController() {
        listen();
    }

    private void listen() {
        GUI.getThumbnailsPanel().addKeyListener(this);
        AnnotationProcessor.process(this);
        ThumbnailsPopupMenu.INSTANCE.getItemFileSystemRenameFiles().addActionListener(this);
    }

    @Override
    public void keyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            renameSelectedFiles();
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(ThumbnailsPopupMenu.INSTANCE.getItemFileSystemRenameFiles())) {
            renameSelectedFiles();
        }
    }

    private void renameSelectedFiles() {
        List<File> selFiles = GUI.getSelectedImageFiles();
        if (selFiles.size() > 0) {
            RenameDialog dlg = new RenameDialog();
            Collections.sort(selFiles);
            dlg.setImageFiles(selFiles);
            dlg.selectRenameViaTemplatesTab(GUI.getThumbnailsPanel().getOriginOfDisplayedThumbnails().isInSameFileSystemDirectory());
            dlg.setVisible(true);
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
