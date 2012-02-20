package org.jphototagger.program.module.filesystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openide.util.Lookup;

import org.jphototagger.domain.repository.ImageFilesRepository;
import org.jphototagger.program.module.thumbnails.ThumbnailsPopupMenu;
import org.jphototagger.program.resource.GUI;

/**
 * @author Elmar Baumann
 */
public final class MoveFilesController implements ActionListener {

    private static final Logger LOGGER = Logger.getLogger(MoveFilesController.class.getName());
    private final ImageFilesRepository repo = Lookup.getDefault().lookup(ImageFilesRepository.class);

    public MoveFilesController() {
        listen();
    }

    private void listen() {
        ThumbnailsPopupMenu.INSTANCE.getItemFileSystemMoveFiles().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        moveSelectedFiles();
    }

    private void moveSelectedFiles() {
        List<File> selFiles = GUI.getSelectedImageFiles();
        if (!selFiles.isEmpty()) {
            MoveToDirectoryDialog dlg = new MoveToDirectoryDialog();
            dlg.setSourceFiles(selFiles);
            dlg.setVisible(true);
        } else {
            LOGGER.log(Level.WARNING, "Moving images: No images selected!");
        }
    }

    public void moveFilesWithoutConfirm(List<File> srcFiles, File targetDir) {
        if (srcFiles == null) {
            throw new NullPointerException("srcFiles == null");
        }
        if (targetDir == null) {
            throw new NullPointerException("targetDir == null");
        }
        if (!srcFiles.isEmpty() && targetDir.isDirectory()) {
            MoveToDirectoryDialog dlg = new MoveToDirectoryDialog();
            dlg.setSourceFiles(srcFiles);
            dlg.setTargetDirectory(targetDir);
            dlg.setVisible(true);
        }
    }
}
