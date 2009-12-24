/*
 * JPhotoTagger tags and finds images fast
 * Copyright (C) 2009 by the developer team, resp. Elmar Baumann<eb@elmar-baumann.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package de.elmar_baumann.jpt.view.panels;

import de.elmar_baumann.jpt.UserSettings;
import de.elmar_baumann.jpt.event.listener.impl.ListenerProvider;
import de.elmar_baumann.jpt.event.UserSettingsChangeEvent;
import de.elmar_baumann.jpt.resource.Bundle;
import de.elmar_baumann.jpt.types.Persistence;

/**
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008-11-02
 */
public final class SettingsPerformancePanel extends javax.swing.JPanel
        implements Persistence {

    private final ListenerProvider listenerProvider = ListenerProvider.INSTANCE;

    /** Creates new form SettingsPerformancePanel */
    public SettingsPerformancePanel() {
        initComponents();
    }

    private void handleMaximumSecondsToTerminateExternalProgramsStateChanged() {
        UserSettingsChangeEvent evt =
                new UserSettingsChangeEvent(
                UserSettingsChangeEvent.Type.MAX_SECONDS_TO_TERMINATE_EXTERNAL_PROGRAMS,
                this);
        evt.setMaxSecondsToTerminateExternalPrograms(
                (Integer) spinnerMaximumSecondsToTerminateExternalPrograms.
                getModel().getValue());
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
        checkBoxScanForEmbeddedXmp.setSelected(settings.isScanForEmbeddedXmp());
        spinnerMaximumSecondsToTerminateExternalPrograms.getModel().setValue(settings.
                getMaxSecondsToTerminateExternalPrograms());
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
        labelMaximumSecondsToTerminateExternalPrograms = new javax.swing.JLabel();
        spinnerMaximumSecondsToTerminateExternalPrograms = new javax.swing.JSpinner();
        checkBoxScanForEmbeddedXmp = new javax.swing.JCheckBox();

        panelProcessingTime.setBorder(javax.swing.BorderFactory.createTitledBorder(Bundle.getString("SettingsPerformancePanel.panelProcessingTime.border.title"))); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("de/elmar_baumann/jpt/resource/properties/Bundle"); // NOI18N
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelProcessingTimeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelProcessingTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(checkBoxScanForEmbeddedXmp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                    .addGroup(panelProcessingTimeLayout.createSequentialGroup()
                        .addComponent(labelMaximumSecondsToTerminateExternalPrograms, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerMaximumSecondsToTerminateExternalPrograms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12))
        );
        panelProcessingTimeLayout.setVerticalGroup(
            panelProcessingTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProcessingTimeLayout.createSequentialGroup()
                .addGroup(panelProcessingTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(labelMaximumSecondsToTerminateExternalPrograms, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinnerMaximumSecondsToTerminateExternalPrograms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkBoxScanForEmbeddedXmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelProcessingTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelProcessingTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void spinnerMaximumSecondsToTerminateExternalProgramsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerMaximumSecondsToTerminateExternalProgramsStateChanged
    handleMaximumSecondsToTerminateExternalProgramsStateChanged();
}//GEN-LAST:event_spinnerMaximumSecondsToTerminateExternalProgramsStateChanged

private void checkBoxScanForEmbeddedXmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxScanForEmbeddedXmpActionPerformed
    handleScanForEmbeddedXmpActionPerformed();
}//GEN-LAST:event_checkBoxScanForEmbeddedXmpActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkBoxScanForEmbeddedXmp;
    private javax.swing.JLabel labelMaximumSecondsToTerminateExternalPrograms;
    private javax.swing.JPanel panelProcessingTime;
    private javax.swing.JSpinner spinnerMaximumSecondsToTerminateExternalPrograms;
    // End of variables declaration//GEN-END:variables
}
