package de.elmar_baumann.imv.view.dialogs;

import de.elmar_baumann.imv.AppSettings;
import de.elmar_baumann.imv.resource.Bundle;
import de.elmar_baumann.lib.dialog.Dialog;
import de.elmar_baumann.lib.persistence.PersistentAppSizes;
import de.elmar_baumann.lib.persistence.PersistentSettings;
import de.elmar_baumann.lib.persistence.PersistentSettingsHints;

/**
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 */
public final class ProgramInputParametersDialog extends Dialog {

    private boolean accepted = false;

    public ProgramInputParametersDialog() {
        super((java.awt.Frame) null, true);
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        setIconImages(AppSettings.getAppIcons());
        registerKeyStrokes();
    }

    public void setProgram(String program) {
        labelContextProgram.setText(program);
    }

    public void setFilename(String filename) {
        labelContextFile.setText(filename);
    }
    
    public boolean isAccepted() {
        return accepted;
    }

    public String getParameters() {
        return textAreaParameter.getText();
    }

    public boolean isParametersBeforeFilename() {
        return radioButtonParametersBeforeFilename.isSelected();
    }

    @Override
    public void setVisible(boolean visible) {
        PersistentSettingsHints hints = new PersistentSettingsHints();
        if (visible) {
            PersistentAppSizes.getSizeAndLocation(this);
            PersistentSettings.getInstance().getComponent(this, hints);
        } else {
            PersistentAppSizes.setSizeAndLocation(this);
            PersistentSettings.getInstance().setComponent(this, hints);
        }
        super.setVisible(visible);
    }

    @Override
    protected void escape() {
        cancel();
    }

    private void cancel() {
        accepted = false;
        setVisible(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        panelContext = new javax.swing.JPanel();
        labelContextProgramPrompt = new javax.swing.JLabel();
        labelContextProgram = new javax.swing.JLabel();
        labelContextFilePrompt = new javax.swing.JLabel();
        labelContextFile = new javax.swing.JLabel();
        labelPrompt = new javax.swing.JLabel();
        scrollPaneAreaParameter = new javax.swing.JScrollPane();
        textAreaParameter = new javax.swing.JTextArea();
        radioButtonParametersBeforeFilename = new javax.swing.JRadioButton();
        radioButtonParametersAfterFilename = new javax.swing.JRadioButton();
        buttonOk = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Bundle.getString("ProgramInputParametersDialog.title")); // NOI18N

        panelContext.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Bundle.getString("ProgramInputParametersDialog.panelContext.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12))); // NOI18N

        labelContextProgramPrompt.setText(Bundle.getString("ProgramInputParametersDialog.labelContextProgramPrompt.text")); // NOI18N

        labelContextProgram.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        labelContextProgram.setText(Bundle.getString("ProgramInputParametersDialog.labelContextProgram.text")); // NOI18N

        labelContextFilePrompt.setText(Bundle.getString("ProgramInputParametersDialog.labelContextFilePrompt.text")); // NOI18N

        labelContextFile.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        labelContextFile.setText(Bundle.getString("ProgramInputParametersDialog.labelContextFile.text")); // NOI18N

        javax.swing.GroupLayout panelContextLayout = new javax.swing.GroupLayout(panelContext);
        panelContext.setLayout(panelContextLayout);
        panelContextLayout.setHorizontalGroup(
            panelContextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContextLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelContextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelContextProgramPrompt)
                    .addComponent(labelContextFilePrompt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelContextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelContextFile, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                    .addComponent(labelContextProgram, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelContextLayout.setVerticalGroup(
            panelContextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContextLayout.createSequentialGroup()
                .addGroup(panelContextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelContextProgram)
                    .addComponent(labelContextProgramPrompt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelContextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelContextFile)
                    .addComponent(labelContextFilePrompt))
                .addContainerGap())
        );

        panelContextLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {labelContextProgram, labelContextProgramPrompt});

        panelContextLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {labelContextFile, labelContextFilePrompt});

        labelPrompt.setText(Bundle.getString("ProgramInputParametersDialog.labelPrompt.text")); // NOI18N

        textAreaParameter.setColumns(20);
        textAreaParameter.setRows(2);
        scrollPaneAreaParameter.setViewportView(textAreaParameter);

        buttonGroup.add(radioButtonParametersBeforeFilename);
        radioButtonParametersBeforeFilename.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        radioButtonParametersBeforeFilename.setMnemonic('v');
        radioButtonParametersBeforeFilename.setSelected(true);
        radioButtonParametersBeforeFilename.setText(Bundle.getString("ProgramInputParametersDialog.radioButtonParametersBeforeFilename.text")); // NOI18N

        buttonGroup.add(radioButtonParametersAfterFilename);
        radioButtonParametersAfterFilename.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        radioButtonParametersAfterFilename.setMnemonic('n');
        radioButtonParametersAfterFilename.setText(Bundle.getString("ProgramInputParametersDialog.radioButtonParametersAfterFilename.text")); // NOI18N

        buttonOk.setFont(new java.awt.Font("Dialog", 0, 12));
        buttonOk.setMnemonic('o');
        buttonOk.setText(Bundle.getString("ProgramInputParametersDialog.buttonOk.text")); // NOI18N
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });

        buttonCancel.setFont(new java.awt.Font("Dialog", 0, 12));
        buttonCancel.setMnemonic('a');
        buttonCancel.setText(Bundle.getString("ProgramInputParametersDialog.buttonCancel.text")); // NOI18N
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(radioButtonParametersAfterFilename)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                        .addComponent(buttonCancel))
                    .addComponent(radioButtonParametersBeforeFilename)
                    .addComponent(buttonOk, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelContext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelPrompt)
                    .addComponent(scrollPaneAreaParameter, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {buttonCancel, buttonOk});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelContext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelPrompt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneAreaParameter, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(radioButtonParametersBeforeFilename)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radioButtonParametersAfterFilename))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonOk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonCancel)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
    accepted = true;
    setVisible(false);
}//GEN-LAST:event_buttonOkActionPerformed

private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
    cancel();
}//GEN-LAST:event_buttonCancelActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ProgramInputParametersDialog dialog = new ProgramInputParametersDialog();
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JButton buttonOk;
    private javax.swing.JLabel labelContextFile;
    private javax.swing.JLabel labelContextFilePrompt;
    private javax.swing.JLabel labelContextProgram;
    private javax.swing.JLabel labelContextProgramPrompt;
    private javax.swing.JLabel labelPrompt;
    private javax.swing.JPanel panelContext;
    private javax.swing.JRadioButton radioButtonParametersAfterFilename;
    private javax.swing.JRadioButton radioButtonParametersBeforeFilename;
    private javax.swing.JScrollPane scrollPaneAreaParameter;
    private javax.swing.JTextArea textAreaParameter;
    // End of variables declaration//GEN-END:variables
}
