package de.elmar_baumann.imv.view.panels;

import de.elmar_baumann.imv.UserSettings;
import de.elmar_baumann.imv.event.ListenerProvider;
import de.elmar_baumann.imv.event.UserSettingsChangeEvent;
import de.elmar_baumann.imv.model.ComboBoxModelThreadPriority;
import de.elmar_baumann.imv.resource.Bundle;
import de.elmar_baumann.imv.types.Persistence;

/**
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/11/02
 */
public final class SettingsPerformancePanel extends javax.swing.JPanel
        implements Persistence {

    private final ListenerProvider listenerProvider = ListenerProvider.INSTANCE;

    /** Creates new form SettingsPerformancePanel */
    public SettingsPerformancePanel() {
        initComponents();
    }

    private void handleActionPerformedCheckBoxIsAutocompleteDisabled() {
        UserSettingsChangeEvent evt = new UserSettingsChangeEvent(
                UserSettingsChangeEvent.Type.IS_USE_AUTOCOMPLETE, this);
        evt.setAutocomplete(!checkBoxIsAutocompleteDisabled.isSelected());
        notifyChangeListener(evt);
    }

    private void handleActionComboBoxThreadPriorityAction() {
        UserSettingsChangeEvent evt = new UserSettingsChangeEvent(
                UserSettingsChangeEvent.Type.THREAD_PRIORITY, this);
        evt.setThreadPriority(getThreadPriority());
        notifyChangeListener(evt);
    }

    private int getThreadPriority() {
        return ((ComboBoxModelThreadPriority) comboBoxThreadPriority.getModel()).getSelectedPriority();
    }

    private void handleMaximumSecondsToTerminateExternalProgramsStateChanged() {
        UserSettingsChangeEvent evt = new UserSettingsChangeEvent(
                UserSettingsChangeEvent.Type.MAX_SECONDS_TO_TERMINATE_EXTERNAL_PROGRAMS, this);
        evt.setMaxSecondsToTerminateExternalPrograms(
                (Integer) spinnerMaximumSecondsToTerminateExternalPrograms.getModel().getValue());
        notifyChangeListener(evt);
    }

    private void handleScanForEmbeddedXmpActionPerformed() {
        UserSettingsChangeEvent evt = new UserSettingsChangeEvent(
                UserSettingsChangeEvent.Type.SCAN_FOR_EMBEDDED_XMP, this);
        evt.setScanForEmbeddedXmp(checkBoxScanForEmbeddedXmp.isSelected());
        notifyChangeListener(evt);
    }

    private synchronized void notifyChangeListener(UserSettingsChangeEvent evt) {
        listenerProvider.notifyUserSettingsChangeListener(evt);
    }

    @Override
    public void readProperties() {
        UserSettings settings = UserSettings.INSTANCE;
        ComboBoxModelThreadPriority modelThreadPriority =
                (ComboBoxModelThreadPriority) comboBoxThreadPriority.getModel();
        modelThreadPriority.setSelectedItem(modelThreadPriority.getItemOfPriority(
                settings.getThreadPriority()));

        checkBoxIsAutocompleteDisabled.setSelected(!settings.isUseAutocomplete());
        checkBoxScanForEmbeddedXmp.setSelected(settings.isScanForEmbeddedXmp());
        spinnerMaximumSecondsToTerminateExternalPrograms.getModel().setValue(settings.getMaxSecondsToTerminateExternalPrograms());
    }

    @Override
    public void writeProperties() {
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelProcessingTime = new javax.swing.JPanel();
        labelThreadPriority = new javax.swing.JLabel();
        comboBoxThreadPriority = new javax.swing.JComboBox();
        labelInfoThreadPriority = new javax.swing.JLabel();
        labelMaximumSecondsToTerminateExternalPrograms = new javax.swing.JLabel();
        spinnerMaximumSecondsToTerminateExternalPrograms = new javax.swing.JSpinner();
        checkBoxScanForEmbeddedXmp = new javax.swing.JCheckBox();
        panelAccelerateStart = new javax.swing.JPanel();
        checkBoxIsAutocompleteDisabled = new javax.swing.JCheckBox();
        labelsAutocompleteDisabled = new javax.swing.JLabel();

        panelProcessingTime.setBorder(javax.swing.BorderFactory.createTitledBorder(Bundle.getString("SettingsPerformancePanel.panelProcessingTime.border.title"))); // NOI18N

        labelThreadPriority.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelThreadPriority.setText(Bundle.getString("SettingsPerformancePanel.labelThreadPriority.text")); // NOI18N

        comboBoxThreadPriority.setModel(new de.elmar_baumann.imv.model.ComboBoxModelThreadPriority());
        comboBoxThreadPriority.setEditor(null);
        comboBoxThreadPriority.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxThreadPriorityActionPerformed(evt);
            }
        });

        labelInfoThreadPriority.setForeground(new java.awt.Color(0, 0, 255));
        labelInfoThreadPriority.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelInfoThreadPriority.setText(Bundle.getString("SettingsPerformancePanel.labelInfoThreadPriority.text")); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("de/elmar_baumann/imv/resource/properties/Bundle"); // NOI18N
        labelMaximumSecondsToTerminateExternalPrograms.setText(bundle.getString("SettingsPerformancePanel.labelMaximumSecondsToTerminateExternalPrograms.text")); // NOI18N

        spinnerMaximumSecondsToTerminateExternalPrograms.setModel(new javax.swing.SpinnerNumberModel(60, 10, 600, 1));
        spinnerMaximumSecondsToTerminateExternalPrograms.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerMaximumSecondsToTerminateExternalProgramsStateChanged(evt);
            }
        });

        checkBoxScanForEmbeddedXmp.setText(Bundle.getString("SettingsPerformancePanel.checkBoxScanForEmbeddedXmp.text")); // NOI18N
        checkBoxScanForEmbeddedXmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxScanForEmbeddedXmpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelProcessingTimeLayout = new javax.swing.GroupLayout(panelProcessingTime);
        panelProcessingTime.setLayout(panelProcessingTimeLayout);
        panelProcessingTimeLayout.setHorizontalGroup(
            panelProcessingTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProcessingTimeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelProcessingTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelProcessingTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(labelInfoThreadPriority, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelProcessingTimeLayout.createSequentialGroup()
                            .addComponent(labelThreadPriority)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(comboBoxThreadPriority, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelProcessingTimeLayout.createSequentialGroup()
                        .addComponent(labelMaximumSecondsToTerminateExternalPrograms)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spinnerMaximumSecondsToTerminateExternalPrograms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(checkBoxScanForEmbeddedXmp))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelProcessingTimeLayout.setVerticalGroup(
            panelProcessingTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProcessingTimeLayout.createSequentialGroup()
                .addGroup(panelProcessingTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelThreadPriority, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxThreadPriority, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelInfoThreadPriority)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelProcessingTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelMaximumSecondsToTerminateExternalPrograms)
                    .addComponent(spinnerMaximumSecondsToTerminateExternalPrograms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkBoxScanForEmbeddedXmp)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelAccelerateStart.setBorder(javax.swing.BorderFactory.createTitledBorder(Bundle.getString("SettingsPerformancePanel.panelAccelerateStart.border.title"))); // NOI18N

        checkBoxIsAutocompleteDisabled.setText(Bundle.getString("SettingsPerformancePanel.checkBoxIsAutocompleteDisabled.text")); // NOI18N
        checkBoxIsAutocompleteDisabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxIsAutocompleteDisabledActionPerformed(evt);
            }
        });

        labelsAutocompleteDisabled.setForeground(new java.awt.Color(0, 0, 255));
        labelsAutocompleteDisabled.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelsAutocompleteDisabled.setText(Bundle.getString("SettingsPerformancePanel.labelsAutocompleteDisabled.text")); // NOI18N

        javax.swing.GroupLayout panelAccelerateStartLayout = new javax.swing.GroupLayout(panelAccelerateStart);
        panelAccelerateStart.setLayout(panelAccelerateStartLayout);
        panelAccelerateStartLayout.setHorizontalGroup(
            panelAccelerateStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccelerateStartLayout.createSequentialGroup()
                .addGroup(panelAccelerateStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(checkBoxIsAutocompleteDisabled)
                    .addGroup(panelAccelerateStartLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(labelsAutocompleteDisabled)))
                .addContainerGap())
        );
        panelAccelerateStartLayout.setVerticalGroup(
            panelAccelerateStartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccelerateStartLayout.createSequentialGroup()
                .addComponent(checkBoxIsAutocompleteDisabled)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelsAutocompleteDisabled)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelProcessingTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelAccelerateStart, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelProcessingTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAccelerateStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void checkBoxIsAutocompleteDisabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxIsAutocompleteDisabledActionPerformed
    handleActionPerformedCheckBoxIsAutocompleteDisabled();
}//GEN-LAST:event_checkBoxIsAutocompleteDisabledActionPerformed

private void comboBoxThreadPriorityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxThreadPriorityActionPerformed
    handleActionComboBoxThreadPriorityAction();
}//GEN-LAST:event_comboBoxThreadPriorityActionPerformed

private void spinnerMaximumSecondsToTerminateExternalProgramsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerMaximumSecondsToTerminateExternalProgramsStateChanged
    handleMaximumSecondsToTerminateExternalProgramsStateChanged();
}//GEN-LAST:event_spinnerMaximumSecondsToTerminateExternalProgramsStateChanged

private void checkBoxScanForEmbeddedXmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxScanForEmbeddedXmpActionPerformed
    handleScanForEmbeddedXmpActionPerformed();
}//GEN-LAST:event_checkBoxScanForEmbeddedXmpActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkBoxIsAutocompleteDisabled;
    private javax.swing.JCheckBox checkBoxScanForEmbeddedXmp;
    private javax.swing.JComboBox comboBoxThreadPriority;
    private javax.swing.JLabel labelInfoThreadPriority;
    private javax.swing.JLabel labelMaximumSecondsToTerminateExternalPrograms;
    private javax.swing.JLabel labelThreadPriority;
    private javax.swing.JLabel labelsAutocompleteDisabled;
    private javax.swing.JPanel panelAccelerateStart;
    private javax.swing.JPanel panelProcessingTime;
    private javax.swing.JSpinner spinnerMaximumSecondsToTerminateExternalPrograms;
    // End of variables declaration//GEN-END:variables
}
