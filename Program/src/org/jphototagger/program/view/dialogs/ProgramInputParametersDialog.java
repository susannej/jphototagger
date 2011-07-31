package org.jphototagger.program.view.dialogs;

import java.awt.Container;

import org.jphototagger.lib.componentutil.MnemonicUtil;
import org.jphototagger.lib.dialog.Dialog;
import org.jphototagger.program.UserSettings;
import org.jphototagger.program.resource.GUI;

/**
 *
 * @author Elmar Baumann
 */
public final class ProgramInputParametersDialog extends Dialog {
    private static final long serialVersionUID = 7211489360676712179L;
    private boolean accepted = false;

    public ProgramInputParametersDialog() {
        super(GUI.getAppFrame(), true, UserSettings.INSTANCE.getSettings(), null);
        initComponents();
        MnemonicUtil.setMnemonics((Container) this);
    }

    public void setProgram(String program) {
        if (program == null) {
            throw new NullPointerException("program == null");
        }

        labelContextProgram.setText(program);
    }

    public void setFilename(String filename) {
        if (filename == null) {
            throw new NullPointerException("filename == null");
        }

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
        if (visible) {
            UserSettings.INSTANCE.getSettings().applySettings(this, UserSettings.SET_TABBED_PANE_SETTINGS);
        } else {
            UserSettings.INSTANCE.getSettings().set(this, UserSettings.SET_TABBED_PANE_SETTINGS);
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

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")

    private void initComponents() {//GEN-BEGIN:initComponents

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
        buttonCancel = new javax.swing.JButton();
        buttonOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/program/view/dialogs/Bundle"); // NOI18N
        setTitle(bundle.getString("ProgramInputParametersDialog.title")); // NOI18N
        setName("Form"); // NOI18N

        panelContext.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ProgramInputParametersDialog.panelContext.border.title"))); // NOI18N
        panelContext.setName("panelContext"); // NOI18N

        labelContextProgramPrompt.setText(bundle.getString("ProgramInputParametersDialog.labelContextProgramPrompt.text")); // NOI18N
        labelContextProgramPrompt.setName("labelContextProgramPrompt"); // NOI18N

        labelContextProgram.setName("labelContextProgram"); // NOI18N

        labelContextFilePrompt.setText(bundle.getString("ProgramInputParametersDialog.labelContextFilePrompt.text")); // NOI18N
        labelContextFilePrompt.setName("labelContextFilePrompt"); // NOI18N

        labelContextFile.setName("labelContextFile"); // NOI18N

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
                    .addComponent(labelContextFile, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                    .addComponent(labelContextProgram, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE))
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

        labelPrompt.setLabelFor(textAreaParameter);
        labelPrompt.setText(bundle.getString("ProgramInputParametersDialog.labelPrompt.text")); // NOI18N
        labelPrompt.setName("labelPrompt"); // NOI18N

        scrollPaneAreaParameter.setName("scrollPaneAreaParameter"); // NOI18N

        textAreaParameter.setColumns(20);
        textAreaParameter.setRows(2);
        textAreaParameter.setName("textAreaParameter"); // NOI18N
        scrollPaneAreaParameter.setViewportView(textAreaParameter);

        buttonGroup.add(radioButtonParametersBeforeFilename);
        radioButtonParametersBeforeFilename.setSelected(true);
        radioButtonParametersBeforeFilename.setText(bundle.getString("ProgramInputParametersDialog.radioButtonParametersBeforeFilename.text")); // NOI18N
        radioButtonParametersBeforeFilename.setName("radioButtonParametersBeforeFilename"); // NOI18N

        buttonGroup.add(radioButtonParametersAfterFilename);
        radioButtonParametersAfterFilename.setText(bundle.getString("ProgramInputParametersDialog.radioButtonParametersAfterFilename.text")); // NOI18N
        radioButtonParametersAfterFilename.setName("radioButtonParametersAfterFilename"); // NOI18N

        buttonCancel.setText(bundle.getString("ProgramInputParametersDialog.buttonCancel.text")); // NOI18N
        buttonCancel.setName("buttonCancel"); // NOI18N
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        buttonOk.setText(bundle.getString("ProgramInputParametersDialog.buttonOk.text")); // NOI18N
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
                    .addComponent(radioButtonParametersBeforeFilename)
                    .addComponent(panelContext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelPrompt)
                    .addComponent(scrollPaneAreaParameter, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                    .addComponent(radioButtonParametersAfterFilename)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(buttonCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonOk)))
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
                .addComponent(scrollPaneAreaParameter, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioButtonParametersBeforeFilename)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioButtonParametersAfterFilename)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonOk)
                    .addComponent(buttonCancel))
                .addContainerGap())
        );

        pack();
    }//GEN-END:initComponents

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
                ProgramInputParametersDialog dialog =
                    new ProgramInputParametersDialog();

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
