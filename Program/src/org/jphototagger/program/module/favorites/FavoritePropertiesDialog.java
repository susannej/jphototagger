package org.jphototagger.program.module.favorites;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import org.jphototagger.api.preferences.Preferences;
import org.jphototagger.domain.DomainPreferencesKeys;
import org.jphototagger.domain.favorites.Favorite;
import org.jphototagger.domain.repository.FavoritesRepository;
import org.jphototagger.lib.swing.Dialog;
import org.jphototagger.lib.swing.DirectoryChooser;
import org.jphototagger.lib.swing.DirectoryChooser.Option;
import org.jphototagger.lib.swing.MessageDisplayer;
import org.jphototagger.lib.swing.SelectRootFilesPanel;
import org.jphototagger.lib.swing.util.MnemonicUtil;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.lib.util.ObjectUtil;
import org.jphototagger.lib.util.StringUtil;
import org.jphototagger.program.resource.GUI;
import org.openide.util.Lookup;


/**
 * Changes the properties of a {@code org.jphototagger.program.data.Favorite}.
 *
 * @author Elmar Baumann
 */
public final class FavoritePropertiesDialog extends Dialog {

    private static final String KEY_LAST_DIRECTORY = "org.jphototagger.program.view.dialogs.FavoriteDirectoryPropertiesDialog.LastDirectory";
    private static final long serialVersionUID = 1L;
    private final FavoritesRepository repo = Lookup.getDefault().lookup(FavoritesRepository.class);
    private String oldName;
    private File dir = null;
    private boolean accepted;

    public FavoritePropertiesDialog() {
        super(GUI.getAppFrame(), true);
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        setHelpPage();
        MnemonicUtil.setMnemonics((Container) this);
    }

    private void setHelpPage() {
        setHelpPageUrl(Bundle.getString(FavoritePropertiesDialog.class, "FavoritePropertiesDialog.HelpPage"));
    }

    private void chooseDirectory() {
        Option showHiddenDirs = getDirChooserOptionShowHiddenDirs();
        List<File> hideRootFiles = SelectRootFilesPanel.readPersistentRootFiles(DomainPreferencesKeys.KEY_UI_DIRECTORIES_TAB_HIDE_ROOT_FILES);
        DirectoryChooser dlg = new DirectoryChooser(GUI.getAppFrame(), getDirectoryFromSettings(), hideRootFiles, showHiddenDirs);
        dlg.setPreferencesKey("FavoritePropertiesDialog.DirChooser");
        dlg.setVisible(true);
        toFront();
        if (dlg.isAccepted()) {
            setDirectory(dlg.getSelectedDirectories().get(0));
        }
        setOkEnabled();
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

    public boolean isEqualsTo(Favorite favorite) {
        if (favorite == null) {
            throw new NullPointerException("favorite == null");
        }
        if (!valuesOk()) {
            return false;
        }
        String favoriteName = favorite.getName();
        String componentName = getName();
        boolean componentNameIsFavoriteName = componentName.equalsIgnoreCase(favoriteName);
        File favoriteDirectory = favorite.getDirectory();
        return ObjectUtil.equals(dir, favoriteDirectory) && componentNameIsFavoriteName;
    }

    /**
     *
     * @param name name (alias)
     */
    public void setFavoriteName(String name) {
        if (name == null) {
            throw new NullPointerException("name == null");
        }
        String favoriteName = name.trim();
        textFieldFavoriteName.setText(favoriteName);
        oldName = favoriteName;
        textFieldFavoriteName.selectAll();
    }

    public void setDirectory(File dir) {
        if (dir == null) {
            throw new NullPointerException("dir == null");
        }
        this.dir = dir;
        String dirPathName = dir.getAbsolutePath();
        labelDirectoryname.setText(dirPathName);

        if (getFavoriteName().isEmpty()) {
            textFieldFavoriteName.setText(dir.getName());
        }
    }

    public void setEnabledButtonChooseDirectory(boolean enabled) {
        buttonChooseDirectory.setEnabled(enabled);
    }

    /**
     *
     * @return Name (Alias)
     */
    public String getFavoriteName() {
        return textFieldFavoriteName.getText().trim();
    }

    /**
     * @return null if not set (choosen). Not null, if {@link #isAccepted()} returns true.
     */
    public File getDirectory() {
        return dir;
    }

    /**
     * @return true, if closed with Ok
     */
    public boolean isAccepted() {
        return accepted;
    }

    private void exitIfOk() {
        if (checkValuesOk()) {
            String favoriteName = getFavoriteName();
            boolean nameChanged = oldName == null || (oldName != null && !oldName.equals(favoriteName));
            if (nameChanged && repo.existsFavorite(favoriteName)) {
                String message = Bundle.getString(FavoritePropertiesDialog.class, "FavoritePropertiesDialog.Error.FavoriteExists", favoriteName);
                MessageDisplayer.error(this, message);
            } else {
                accepted = true;
                setVisible(false);
            }
        }
    }

    private boolean checkValuesOk() {
        if (!valuesOk()) {
            String message = Bundle.getString(FavoritePropertiesDialog.class, "FavoritePropertiesDialog.Error.InvalidInput");
            MessageDisplayer.error(this, message);
            return false;
        }
        return true;
    }

    private boolean valuesOk() {
        return dir != null && dir.isDirectory() && StringUtil.hasContent(getFavoriteName());
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            setOkEnabled();
        } else {
            persistDirectory();
        }
        super.setVisible(visible);
    }

    private File getDirectoryFromSettings() {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
        if (prefs.containsKey(KEY_LAST_DIRECTORY)) {
            String persistedDir = prefs.getString(KEY_LAST_DIRECTORY);
            if (StringUtil.hasContent(persistedDir)) {
                File pd = new File(persistedDir);
                if (pd.isDirectory()) {
                    return pd;
                }
            }
        }
        return new File(System.getProperty("user.home"));
    }

    private void setOkEnabled() {
        buttonOk.setEnabled(valuesOk());
    }

    private void persistDirectory() {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
        prefs.setString(KEY_LAST_DIRECTORY, dir.getAbsolutePath());
    }

    @Override
    protected void escape() {
        accepted = false;
        setVisible(false);
    }

    private void handleKeyPressed(KeyEvent evt) {
        if (!buttonOk.isEnabled()) {
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            exitIfOk();
        }
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        labelPromptFavoriteName = new javax.swing.JLabel();
        textFieldFavoriteName = new javax.swing.JTextField();
        buttonChooseDirectory = new javax.swing.JButton();
        labelDirectoryname = new javax.swing.JLabel();
        buttonCancel = new javax.swing.JButton();
        buttonOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/program/module/favorites/Bundle"); // NOI18N
        setTitle(bundle.getString("FavoritePropertiesDialog.title")); // NOI18N
        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        labelPromptFavoriteName.setLabelFor(textFieldFavoriteName);
        labelPromptFavoriteName.setText(bundle.getString("FavoritePropertiesDialog.labelPromptFavoriteName.text")); // NOI18N
        labelPromptFavoriteName.setName("labelPromptFavoriteName"); // NOI18N

        textFieldFavoriteName.setName("textFieldFavoriteName"); // NOI18N
        textFieldFavoriteName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFieldFavoriteNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textFieldFavoriteNameKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textFieldFavoriteNameKeyTyped(evt);
            }
        });

        buttonChooseDirectory.setText(bundle.getString("FavoritePropertiesDialog.buttonChooseDirectory.text_1")); // NOI18N
        buttonChooseDirectory.setName("buttonChooseDirectory"); // NOI18N
        buttonChooseDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonChooseDirectoryActionPerformed(evt);
            }
        });

        labelDirectoryname.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        labelDirectoryname.setName("labelDirectoryname"); // NOI18N

        buttonCancel.setText(bundle.getString("FavoritePropertiesDialog.buttonCancel.text")); // NOI18N
        buttonCancel.setName("buttonCancel"); // NOI18N
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        buttonOk.setText(bundle.getString("FavoritePropertiesDialog.buttonOk.text")); // NOI18N
        buttonOk.setEnabled(false);
        buttonOk.setName("buttonOk"); // NOI18N
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelDirectoryname, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                    .addComponent(textFieldFavoriteName, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                    .addComponent(labelPromptFavoriteName)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(buttonCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonOk))
                    .addComponent(buttonChooseDirectory, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(labelPromptFavoriteName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldFavoriteName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonChooseDirectory)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelDirectoryname, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonOk)
                    .addComponent(buttonCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {labelDirectoryname, textFieldFavoriteName});

        pack();
    }//GEN-END:initComponents

    private void buttonChooseDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonChooseDirectoryActionPerformed
        chooseDirectory();
    }//GEN-LAST:event_buttonChooseDirectoryActionPerformed

    private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
        exitIfOk();
    }//GEN-LAST:event_buttonOkActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        escape();
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void textFieldFavoriteNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldFavoriteNameKeyPressed
        handleKeyPressed(evt);
    }//GEN-LAST:event_textFieldFavoriteNameKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        escape();
    }//GEN-LAST:event_formWindowClosing

    private void textFieldFavoriteNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldFavoriteNameKeyTyped
        setOkEnabled();
    }//GEN-LAST:event_textFieldFavoriteNameKeyTyped

    private void textFieldFavoriteNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldFavoriteNameKeyReleased
        setOkEnabled();
    }//GEN-LAST:event_textFieldFavoriteNameKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonChooseDirectory;
    private javax.swing.JButton buttonOk;
    private javax.swing.JLabel labelDirectoryname;
    private javax.swing.JLabel labelPromptFavoriteName;
    private javax.swing.JTextField textFieldFavoriteName;
    // End of variables declaration//GEN-END:variables
}
