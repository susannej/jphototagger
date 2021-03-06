package org.jphototagger.program.module.thumbnails;

import javax.swing.SpinnerNumberModel;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.jphototagger.api.preferences.Preferences;
import org.jphototagger.api.preferences.PreferencesChangedEvent;
import org.jphototagger.api.storage.Persistence;
import org.jphototagger.image.thumbnail.ThumbnailDefaults;
import org.jphototagger.lib.swing.util.MnemonicUtil;
import org.openide.util.Lookup;

/**
 * @author Elmar Baumann
 */
public class ThumbnailDimensionsSettingsPanel extends javax.swing.JPanel implements Persistence {

    private static final long serialVersionUID = 1L;
    private boolean listenToMaxThumbnailWidthChanges = true;

    public ThumbnailDimensionsSettingsPanel() {
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        MnemonicUtil.setMnemonics(this);
        AnnotationProcessor.process(this);
    }

    private void handleStateChangedSpinnerMaxThumbnailWidth() {
        setMaxThumbnailWidth((Integer) spinnerMaxThumbnailWidth.getValue());
    }

    private void setMaxThumbnailWidth(int width) {
        Preferences preferences = Lookup.getDefault().lookup(Preferences.class);

        preferences.setInt(Preferences.KEY_MAX_THUMBNAIL_WIDTH, width);
    }

    @Override
    public void restore() {
        spinnerMaxThumbnailWidth.setValue(getMaxThumbnailWidth());
    }

    private int getMaxThumbnailWidth() {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
        int width = prefs.getInt(Preferences.KEY_MAX_THUMBNAIL_WIDTH);

        return (width != Integer.MIN_VALUE)
                ? width
                : ThumbnailDefaults.DEFAULT_THUMBNAIL_WIDTH;
    }

    @Override
    public void persist() {
        // ignore
    }

    @EventSubscriber(eventClass = PreferencesChangedEvent.class)
    public void preferencesChanged(PreferencesChangedEvent evt) {
        if (Preferences.KEY_MAX_THUMBNAIL_WIDTH.equals(evt.getKey())) {
            listenToMaxThumbnailWidthChanges = false;
            spinnerMaxThumbnailWidth.setValue((Integer) evt.getNewValue());
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
        labelUpdateAllThumbnails = new javax.swing.JLabel();

        setName("Form"); // NOI18N
        setLayout(new java.awt.GridBagLayout());

        labelMaxThumbnailWidth.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/program/module/thumbnails/Bundle"); // NOI18N
        labelMaxThumbnailWidth.setText(bundle.getString("ThumbnailDimensionsSettingsPanel.labelMaxThumbnailWidth.text")); // NOI18N
        labelMaxThumbnailWidth.setName("labelMaxThumbnailWidth"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        add(labelMaxThumbnailWidth, gridBagConstraints);

        spinnerMaxThumbnailWidth.setModel(new SpinnerNumberModel(ThumbnailDefaults.DEFAULT_THUMBNAIL_WIDTH, ThumbnailDefaults.MIN_THUMBNAIL_WIDTH, ThumbnailDefaults.MAX_THUMBNAIL_WIDTH, 50));
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

        labelUpdateAllThumbnails.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelUpdateAllThumbnails.setText(bundle.getString("ThumbnailDimensionsSettingsPanel.labelUpdateAllThumbnails.text")); // NOI18N
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelMaxThumbnailWidth;
    private javax.swing.JLabel labelUpdateAllThumbnails;
    private javax.swing.JSpinner spinnerMaxThumbnailWidth;
    // End of variables declaration//GEN-END:variables
}
