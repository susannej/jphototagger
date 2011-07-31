package org.jphototagger.program.view.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SpinnerNumberModel;

import org.jphototagger.domain.event.UserSettingsEvent;
import org.jphototagger.domain.event.UserSettingsEvent.Type;
import org.jphototagger.domain.event.listener.UserSettingsListener;
import org.jphototagger.lib.componentutil.MnemonicUtil;
import org.jphototagger.program.UserSettings;
import org.jphototagger.program.helper.UpdateAllThumbnails;
import org.jphototagger.program.types.Persistence;

/**
 *
 *
 * @author Elmar Baumann
 */
public class SettingsThumbnailDimensionsPanel extends javax.swing.JPanel implements ActionListener, Persistence, UserSettingsListener {
    private static final long serialVersionUID = -6857947357604949567L;
    private transient UpdateAllThumbnails thumbnailsUpdater;
    private boolean listenToMaxThumbnailWidthChanges = true;

    public SettingsThumbnailDimensionsPanel() {
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        MnemonicUtil.setMnemonics(this);
        UserSettings.INSTANCE.addUserSettingsListener(this);
    }

    private void handleStateChangedSpinnerMaxThumbnailWidth() {
        UserSettings.INSTANCE.setMaxThumbnailWidth((Integer) spinnerMaxThumbnailWidth.getValue());
    }

    private void updateAllThumbnails() {
        synchronized (this) {
            buttonUpdateAllThumbnails.setEnabled(false);
            thumbnailsUpdater = new UpdateAllThumbnails();
            thumbnailsUpdater.addActionListener(this);

            Thread thread = new Thread(thumbnailsUpdater, "JPhotoTagger: Updating all thumbnails");

            thread.start();
        }
    }

    @Override
    public void readProperties() {
        spinnerMaxThumbnailWidth.setValue(UserSettings.INSTANCE.getMaxThumbnailWidth());
    }

    @Override
    public void writeProperties() {
        // ignore
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        synchronized (this) {
            if (evt.getSource() == thumbnailsUpdater) {
                buttonUpdateAllThumbnails.setEnabled(true);
            }
        }
    }

    @Override
    public void applySettings(UserSettingsEvent evt) {
        Type type = evt.getType();
        boolean maxThumbnailWidthChanged = type.equals(UserSettingsEvent.Type.MAX_THUMBNAIL_WIDTH);

        if (maxThumbnailWidthChanged) {
            listenToMaxThumbnailWidthChanges = false;
            spinnerMaxThumbnailWidth.setValue(UserSettings.INSTANCE.getMaxThumbnailWidth());
            listenToMaxThumbnailWidthChanges = true;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        labelMaxThumbnailWidth = new javax.swing.JLabel();
        spinnerMaxThumbnailWidth = new javax.swing.JSpinner();
        buttonUpdateAllThumbnails = new javax.swing.JButton();
        labelUpdateAllThumbnails = new javax.swing.JLabel();

        setName("Form"); // NOI18N
        setLayout(new java.awt.GridBagLayout());

        labelMaxThumbnailWidth.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/program/view/panels/Bundle"); // NOI18N
        labelMaxThumbnailWidth.setText(bundle.getString("SettingsThumbnailDimensionsPanel.labelMaxThumbnailWidth.text")); // NOI18N
        labelMaxThumbnailWidth.setName("labelMaxThumbnailWidth"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        add(labelMaxThumbnailWidth, gridBagConstraints);

        spinnerMaxThumbnailWidth.setModel(new SpinnerNumberModel(UserSettings.DEFAULT_THUMBNAIL_WIDTH, UserSettings.MIN_THUMBNAIL_WIDTH, UserSettings.MAX_THUMBNAIL_WIDTH, 50));
        spinnerMaxThumbnailWidth.setName("spinnerMaxThumbnailWidth"); // NOI18N
        spinnerMaxThumbnailWidth.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerMaxThumbnailWidthStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        add(spinnerMaxThumbnailWidth, gridBagConstraints);

        buttonUpdateAllThumbnails.setText(bundle.getString("SettingsThumbnailDimensionsPanel.buttonUpdateAllThumbnails.text")); // NOI18N
        buttonUpdateAllThumbnails.setName("buttonUpdateAllThumbnails"); // NOI18N
        buttonUpdateAllThumbnails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUpdateAllThumbnailsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 6);
        add(buttonUpdateAllThumbnails, gridBagConstraints);

        labelUpdateAllThumbnails.setForeground(new java.awt.Color(0, 0, 255));
        labelUpdateAllThumbnails.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelUpdateAllThumbnails.setText(bundle.getString("SettingsThumbnailDimensionsPanel.labelUpdateAllThumbnails.text")); // NOI18N
        labelUpdateAllThumbnails.setName("labelUpdateAllThumbnails"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 6, 6, 6);
        add(labelUpdateAllThumbnails, gridBagConstraints);
    }//GEN-END:initComponents

    private void spinnerMaxThumbnailWidthStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerMaxThumbnailWidthStateChanged
        handleStateChangedSpinnerMaxThumbnailWidth();
}//GEN-LAST:event_spinnerMaxThumbnailWidthStateChanged

    private void buttonUpdateAllThumbnailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpdateAllThumbnailsActionPerformed
        if (listenToMaxThumbnailWidthChanges) {
            updateAllThumbnails();
        }
}//GEN-LAST:event_buttonUpdateAllThumbnailsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonUpdateAllThumbnails;
    private javax.swing.JLabel labelMaxThumbnailWidth;
    private javax.swing.JLabel labelUpdateAllThumbnails;
    private javax.swing.JSpinner spinnerMaxThumbnailWidth;
    // End of variables declaration//GEN-END:variables
}
