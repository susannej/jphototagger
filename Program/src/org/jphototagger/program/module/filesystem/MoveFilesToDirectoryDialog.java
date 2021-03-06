package org.jphototagger.program.module.filesystem;

import java.awt.Container;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.jphototagger.api.file.CopyMoveFilesOptions;
import org.jphototagger.api.file.event.FileMovedEvent;
import org.jphototagger.api.preferences.Preferences;
import org.jphototagger.api.progress.ProgressEvent;
import org.jphototagger.api.progress.ProgressListener;
import org.jphototagger.domain.DomainPreferencesKeys;
import org.jphototagger.domain.event.listener.ProgressListenerSupport;
import org.jphototagger.domain.metadata.xmp.XmpSidecarFileResolver;
import org.jphototagger.lib.api.LookAndFeelChangedEvent;
import org.jphototagger.lib.awt.EventQueueUtil;
import org.jphototagger.lib.io.FileUtil;
import org.jphototagger.lib.io.SourceTargetFile;
import org.jphototagger.lib.swing.Dialog;
import org.jphototagger.lib.swing.DirectoryChooser;
import org.jphototagger.lib.swing.MessageDisplayer;
import org.jphototagger.lib.swing.SelectRootFilesPanel;
import org.jphototagger.lib.swing.util.MnemonicUtil;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.program.resource.GUI;
import org.jphototagger.program.settings.AppPreferencesKeys;
import org.openide.util.Lookup;

/**
 * @author Elmar Baumann
 */
public final class MoveFilesToDirectoryDialog extends Dialog implements ProgressListener {

    private static final long serialVersionUID = 1L;
    private static final String KEY_TARGET_DIRECTORY = "org.jphototagger.program.view.dialogs.MoveFilesToDirectoryDialog.TargetDirectory";
    private final List<File> movedFiles = new ArrayList<>();
    private final transient ProgressListenerSupport pListenerSupport = new ProgressListenerSupport();
    private final XmpSidecarFileResolver xmpSidecarFileResolver = Lookup.getDefault().lookup(XmpSidecarFileResolver.class);
    private transient FileSystemMove fileSystemMove;
    private boolean runs = false;
    private boolean cancel = false;
    private List<File> sourceFiles;
    private File targetDirectory = new File("");
    private boolean moveIfVisible = false;

    public MoveFilesToDirectoryDialog() {
        super(GUI.getAppFrame(), false);
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        setHelpPage();
        MnemonicUtil.setMnemonics((Container) this);
        AnnotationProcessor.process(this);
    }

    private void setHelpPage() {
        setHelpPageUrl(Bundle.getString(MoveFilesToDirectoryDialog.class, "MoveFilesToDirectoryDialog.HelpPage"));
    }

    public void addProgressListener(ProgressListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener == null");
        }

        pListenerSupport.add(listener);
    }

    private void checkClosing() {
        if (runs) {
            String message = Bundle.getString(MoveFilesToDirectoryDialog.class, "MoveFilesToDirectoryDialog.Error.CancelBeforeClose");
            MessageDisplayer.error(this, message);
        } else {
            setVisible(false);
        }
    }

    private void addXmpFiles() {
        List<File> xmpFiles = new ArrayList<>();
        for (File sourceFile : sourceFiles) {
            File xmpFile = xmpSidecarFileResolver.getXmpSidecarFileOrNullIfNotExists(sourceFile);
            if (xmpFile != null) {
                xmpFiles.add(xmpFile);
            }
        }
        sourceFiles.addAll(xmpFiles);
    }

    private void reset() {
        runs = false;
        cancel = false;
        movedFiles.clear();
    }

    private void start() {
        reset();
        CopyMoveFilesOptions copyMoveFilesOptions = getCopyMoveFilesOptions();
        boolean renameIfTargetFileExists = copyMoveFilesOptions.equals(CopyMoveFilesOptions.RENAME_SOURCE_FILE_IF_TARGET_FILE_EXISTS);
        fileSystemMove = new FileSystemMove(sourceFiles, targetDirectory, renameIfTargetFileExists);
        fileSystemMove.addProgressListener(this);
        Thread thread = new Thread(fileSystemMove, getMoveThreadName());
        thread.start();
        runs = true;
    }

    private CopyMoveFilesOptions getCopyMoveFilesOptions() {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
        return prefs.containsKey(AppPreferencesKeys.KEY_FILE_SYSTEM_OPERATIONS_OPTIONS_COPY_MOVE_FILES)
                ? CopyMoveFilesOptions.parseInteger(prefs.getInt(AppPreferencesKeys.KEY_FILE_SYSTEM_OPERATIONS_OPTIONS_COPY_MOVE_FILES))
                : CopyMoveFilesOptions.CONFIRM_OVERWRITE;
    }

    private String getMoveThreadName() {
        return "JPhotoTagger: Moving files to directory " + targetDirectory.getAbsolutePath();
    }

    private void cancel() {
        cancel = true;
    }

    private void chooseTargetDirectory() {
        List<File> hideRootFiles = SelectRootFilesPanel.readPersistentRootFiles(DomainPreferencesKeys.KEY_UI_DIRECTORIES_TAB_HIDE_ROOT_FILES);
        DirectoryChooser dlg = new DirectoryChooser(GUI.getAppFrame(), targetDirectory, hideRootFiles, getDirChooserOptionShowHiddenDirs());
        dlg.setPreferencesKey("MoveToDirectoriesDialog.DirChooser");
        dlg.setVisible(true);
        toFront();
        if (dlg.isAccepted()) {
            List<File> files = dlg.getSelectedDirectories();
            if (files.size() > 0) {
                targetDirectory = files.get(0);
                if (targetDirectory.canWrite()) {
                    labelDirectoryName.setText(targetDirectory.getAbsolutePath());
                    setIconToLabelTargetDirectory();
                    buttonStart.setEnabled(true);
                } else {
                    String message = Bundle.getString(MoveFilesToDirectoryDialog.class, "MoveFilesToDirectoryDialog.TargetDirNotWritable", targetDirectory);
                    MessageDisplayer.error(this, message);
                }
            }
        } else {
            File dir = new File(labelDirectoryName.getText().trim());
            buttonStart.setEnabled(FileUtil.isWritableDirectory(dir));
        }
    }

    private DirectoryChooser.Option getDirChooserOptionShowHiddenDirs() {
        return isAcceptHiddenDirectories()
                ? DirectoryChooser.Option.DISPLAY_HIDDEN_DIRECTORIES
                : DirectoryChooser.Option.NO_OPTION;
    }

    private boolean isAcceptHiddenDirectories() {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
        return prefs.containsKey(Preferences.KEY_ACCEPT_HIDDEN_DIRECTORIES)
                ? prefs.getBoolean(Preferences.KEY_ACCEPT_HIDDEN_DIRECTORIES)
                : false;
    }

    private void setIconToLabelTargetDirectory() {
        File dir = new File(labelDirectoryName.getText());
        if (dir.isDirectory()) {
            labelDirectoryName.setIcon(FileSystemView.getFileSystemView().getSystemIcon(dir));
        }
    }

    public void setSourceFiles(List<File> sourceFiles) {
        if (sourceFiles == null) {
            throw new NullPointerException("sourceFiles == null");
        }
        this.sourceFiles = new ArrayList<>(sourceFiles);
        addXmpFiles();
        Collections.sort(this.sourceFiles);
    }

    /**
     * Sets the target directory. If it exists, move will done after calling
     * {@code #setVisible(boolean)} with <code>true</code> as argument whitout
     * user interaction.
     *
     * @param directory  target directory
     */
    public void setTargetDirectory(File directory) {
        if (directory == null) {
            throw new NullPointerException("directory == null");
        }
        if (directory.exists()) {
            targetDirectory = directory;
            buttonStart.setEnabled(false);
            buttonChooseDirectory.setEnabled(false);
            moveIfVisible = true;
        }
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            if (moveIfVisible) {
                start();
            } else {
                setTargetDirectory();
            }
        } else {
            targetDirectoryToSettings();
        }

        super.setVisible(visible);
    }

    private void setTargetDirectory() {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
        targetDirectory = new File(prefs.getString(KEY_TARGET_DIRECTORY));
        if (targetDirectory.exists()) {
            labelDirectoryName.setText(targetDirectory.getAbsolutePath());
            setIconToLabelTargetDirectory();
            buttonStart.setEnabled(true);
        }
    }

    private void targetDirectoryToSettings() {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
        prefs.setString(KEY_TARGET_DIRECTORY, targetDirectory.getAbsolutePath());
    }

    private void checkCancel(ProgressEvent evt) {
        if (cancel) {
            evt.setCancel(true);
        }
    }

    @Override
    public void progressStarted(final ProgressEvent evt) {
        EventQueueUtil.invokeInDispatchThread(new Runnable() {
            @Override
            public void run() {
                buttonStart.setEnabled(false);
                buttonCancel.setEnabled(true);
                progressBar.setMinimum(evt.getMinimum());
                progressBar.setMaximum(evt.getMaximum());
                progressBar.setValue(evt.getValue());
                checkCancel(evt);
                pListenerSupport.notifyStarted(evt);
            }
        });
    }

    @Override
    public void progressPerformed(final ProgressEvent evt) {
        EventQueueUtil.invokeInDispatchThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setValue(evt.getValue());
                Object info = evt.getInfo();
                if (info instanceof SourceTargetFile) {
                    SourceTargetFile sourceTargetFile = (SourceTargetFile) info;
                    String filename = sourceTargetFile.getSourceFile().getAbsolutePath();
                    labelCurrentFilename.setText(filename);
                }
                checkCancel(evt);
                pListenerSupport.notifyPerformed(evt);
            }
        });
    }

    @Override
    public void progressEnded(final ProgressEvent evt) {
        EventQueueUtil.invokeInDispatchThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setValue(evt.getValue());
                    buttonCancel.setEnabled(true);
                    buttonStart.setEnabled(true);
                    runs = false;
                    fileSystemMove = null;
                    GUI.getThumbnailsPanel().removeFiles(movedFiles);
                    removeMovedFiles();
                    pListenerSupport.notifyEnded(evt);
                    setVisible(false);
                }
        });
    }

    private void removeMovedFiles() {
        for (File movedFile : movedFiles) {
            sourceFiles.remove(movedFile);
        }
        buttonStart.setEnabled(sourceFiles.size() > 0);
    }

    @Override
    protected void escape() {
        checkClosing();
    }


    @EventSubscriber(eventClass = FileMovedEvent.class)
    public void fileMoved(FileMovedEvent evt) {
        File sourceFile = evt.getSourceFile();
        movedFiles.add(sourceFile);
    }

    @EventSubscriber(eventClass = LookAndFeelChangedEvent.class)
    public void lookAndFeelChanged(LookAndFeelChangedEvent evt) {
        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        labelInfo = new javax.swing.JLabel();
        buttonChooseDirectory = new javax.swing.JButton();
        labelDirectoryName = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        labelInfoCurrentFilename = new javax.swing.JLabel();
        labelCurrentFilename = new javax.swing.JLabel();
        labelInfoIsThread = new javax.swing.JLabel();
        buttonCancel = new javax.swing.JButton();
        buttonStart = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/program/module/filesystem/Bundle"); // NOI18N
        setTitle(bundle.getString("MoveFilesToDirectoryDialog.title")); // NOI18N
        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        labelInfo.setText(bundle.getString("MoveFilesToDirectoryDialog.labelInfo.text")); // NOI18N
        labelInfo.setName("labelInfo"); // NOI18N

        buttonChooseDirectory.setText(bundle.getString("MoveFilesToDirectoryDialog.buttonChooseDirectory.text")); // NOI18N
        buttonChooseDirectory.setName("buttonChooseDirectory"); // NOI18N
        buttonChooseDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonChooseDirectoryActionPerformed(evt);
            }
        });

        labelDirectoryName.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        labelDirectoryName.setName("labelDirectoryName"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        labelInfoCurrentFilename.setText(bundle.getString("MoveFilesToDirectoryDialog.labelInfoCurrentFilename.text")); // NOI18N
        labelInfoCurrentFilename.setName("labelInfoCurrentFilename"); // NOI18N

        labelCurrentFilename.setForeground(new java.awt.Color(0, 0, 255));
        labelCurrentFilename.setName("labelCurrentFilename"); // NOI18N

        labelInfoIsThread.setForeground(new java.awt.Color(0, 0, 255));
        labelInfoIsThread.setText(bundle.getString("MoveFilesToDirectoryDialog.labelInfoIsThread.text")); // NOI18N
        labelInfoIsThread.setName("labelInfoIsThread"); // NOI18N

        buttonCancel.setText(bundle.getString("MoveFilesToDirectoryDialog.buttonCancel.text")); // NOI18N
        buttonCancel.setEnabled(false);
        buttonCancel.setName("buttonCancel"); // NOI18N
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        buttonStart.setText(bundle.getString("MoveFilesToDirectoryDialog.buttonStart.text")); // NOI18N
        buttonStart.setEnabled(false);
        buttonStart.setName("buttonStart"); // NOI18N
        buttonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStartActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(labelInfo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 132, Short.MAX_VALUE)
                        .addComponent(buttonChooseDirectory))
                    .addComponent(labelDirectoryName, javax.swing.GroupLayout.DEFAULT_SIZE, 622, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 622, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(labelInfoIsThread)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 132, Short.MAX_VALUE)
                        .addComponent(buttonCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonStart))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(labelInfoCurrentFilename)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelCurrentFilename, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buttonChooseDirectory)
                    .addComponent(labelInfo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelDirectoryName, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCurrentFilename)
                    .addComponent(labelInfoCurrentFilename))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonStart)
                    .addComponent(buttonCancel)
                    .addComponent(labelInfoIsThread))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {labelCurrentFilename, labelInfo});

        pack();
    }//GEN-END:initComponents

    private void buttonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStartActionPerformed
        start();
    }//GEN-LAST:event_buttonStartActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        cancel();
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void buttonChooseDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonChooseDirectoryActionPerformed
        chooseTargetDirectory();
    }//GEN-LAST:event_buttonChooseDirectoryActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        checkClosing();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonChooseDirectory;
    private javax.swing.JButton buttonStart;
    private javax.swing.JLabel labelCurrentFilename;
    private javax.swing.JLabel labelDirectoryName;
    private javax.swing.JLabel labelInfo;
    private javax.swing.JLabel labelInfoCurrentFilename;
    private javax.swing.JLabel labelInfoIsThread;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}
