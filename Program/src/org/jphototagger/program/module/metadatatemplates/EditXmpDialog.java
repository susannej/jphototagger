package org.jphototagger.program.module.metadatatemplates;

import org.jphototagger.domain.metadata.xmp.Xmp;
import org.jphototagger.domain.templates.MetadataTemplate;
import org.jphototagger.lib.swing.Dialog;
import org.jphototagger.lib.swing.util.ComponentUtil;
import org.jphototagger.lib.swing.util.MnemonicUtil;
import org.jphototagger.program.app.ui.AppLookAndFeel;

/**
 * @author Elmar Baumann
 */
public class EditXmpDialog extends Dialog {

    private static final long serialVersionUID = 1L;
    private final Xmp xmp;
    private boolean accepted;

    public EditXmpDialog() {
        this(new Xmp());
    }

    public EditXmpDialog(Xmp xmp) {
        super(ComponentUtil.findFrameWithIcon(), true);
        if (xmp == null) {
            throw new NullPointerException("xmp == null");
        }
        this.xmp = xmp;
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        panelEditXmp.setXmp(xmp);
        setButtonInsertTemplateEnabled();
        buttonEditMetadataTemplates.setIcon(AppLookAndFeel.getIcon("icon_add.png")); // NOI18N
        MnemonicUtil.setMnemonics(this);
    }

    private void setButtonInsertTemplateEnabled() {
        buttonInsertTemplate.setEnabled(comboBoxTemplates.getSelectedIndex() >= 0);
    }

    public boolean isAccepted() {
        return accepted;
    }

    public Xmp getXmp() {
        return xmp;
    }

    private void insertTemplate() {
        Object selectedItem = comboBoxTemplates.getSelectedItem();
        if (selectedItem instanceof MetadataTemplate) {
            xmp.setMetaDataTemplate((MetadataTemplate) selectedItem);
            panelEditXmp.setXmp(xmp);
        }
    }

    private void acceptInputAndDispose() {
        accepted = true;
        if (panelEditXmp.isDirty()) {
            panelEditXmp.setInputToXmp();
        }
        dispose();
    }

    private void cancelAndDispose() {
        accepted = false;
        dispose();
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            panelEditXmp.requestFocusInWindow();
        }
        super.setVisible(visible);
    }

    private void editMetadataTemplates() {
        EditMetaDataTemplateDialog dialog = new EditMetaDataTemplateDialog();
        MetadataTemplate t = new MetadataTemplate();
        dialog.setTemplate(t);
        ComponentUtil.show(dialog);
        toFront();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panelContent = new javax.swing.JPanel();
        panelTemplates = new javax.swing.JPanel();
        labelTemplates = new javax.swing.JLabel();
        comboBoxTemplates = new javax.swing.JComboBox<>();
        buttonEditMetadataTemplates = new javax.swing.JButton();
        buttonInsertTemplate = new javax.swing.JButton();
        scrollPanePanelEditXmp = new javax.swing.JScrollPane();
        panelEditXmp = new org.jphototagger.program.module.metadatatemplates.EditXmpPanel();
        panelSubmitButtons = new javax.swing.JPanel();
        buttonCancel = new javax.swing.JButton();
        buttonOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/program/module/metadatatemplates/Bundle"); // NOI18N
        setTitle(bundle.getString("EditXmpDialog.title")); // NOI18N
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panelContent.setPreferredSize(new java.awt.Dimension(500, 450));
        panelContent.setLayout(new java.awt.GridBagLayout());

        panelTemplates.setLayout(new java.awt.GridBagLayout());

        labelTemplates.setLabelFor(comboBoxTemplates);
        labelTemplates.setText(bundle.getString("EditXmpDialog.labelTemplates.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelTemplates.add(labelTemplates, gridBagConstraints);

        comboBoxTemplates.setModel(new MetadataTemplatesComboBoxModel());
        comboBoxTemplates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxTemplatesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelTemplates.add(comboBoxTemplates, gridBagConstraints);

        buttonEditMetadataTemplates.setToolTipText(bundle.getString("EditXmpDialog.buttonEditMetadataTemplates.toolTipText")); // NOI18N
        buttonEditMetadataTemplates.setMargin(new java.awt.Insets(2, 2, 2, 2));
        buttonEditMetadataTemplates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEditMetadataTemplatesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelTemplates.add(buttonEditMetadataTemplates, gridBagConstraints);

        buttonInsertTemplate.setText(bundle.getString("EditXmpDialog.buttonInsertTemplate.text")); // NOI18N
        buttonInsertTemplate.setEnabled(false);
        buttonInsertTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonInsertTemplateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelTemplates.add(buttonInsertTemplate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        panelContent.add(panelTemplates, gridBagConstraints);

        scrollPanePanelEditXmp.setViewportView(panelEditXmp);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panelContent.add(scrollPanePanelEditXmp, gridBagConstraints);

        panelSubmitButtons.setLayout(new java.awt.GridBagLayout());

        buttonCancel.setText(bundle.getString("EditXmpDialog.buttonCancel.text")); // NOI18N
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelSubmitButtons.add(buttonCancel, gridBagConstraints);

        buttonOk.setText(bundle.getString("EditXmpDialog.buttonOk.text")); // NOI18N
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelSubmitButtons.add(buttonOk, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panelContent.add(panelSubmitButtons, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(panelContent, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    private void buttonInsertTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonInsertTemplateActionPerformed
        insertTemplate();
    }//GEN-LAST:event_buttonInsertTemplateActionPerformed

    private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
        acceptInputAndDispose();
    }//GEN-LAST:event_buttonOkActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        cancelAndDispose();
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void comboBoxTemplatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxTemplatesActionPerformed
        setButtonInsertTemplateEnabled();
    }//GEN-LAST:event_comboBoxTemplatesActionPerformed

    private void buttonEditMetadataTemplatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEditMetadataTemplatesActionPerformed
        editMetadataTemplates();
    }//GEN-LAST:event_buttonEditMetadataTemplatesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonEditMetadataTemplates;
    private javax.swing.JButton buttonInsertTemplate;
    private javax.swing.JButton buttonOk;
    private javax.swing.JComboBox<Object> comboBoxTemplates;
    private javax.swing.JLabel labelTemplates;
    private javax.swing.JPanel panelContent;
    private org.jphototagger.program.module.metadatatemplates.EditXmpPanel panelEditXmp;
    private javax.swing.JPanel panelSubmitButtons;
    private javax.swing.JPanel panelTemplates;
    private javax.swing.JScrollPane scrollPanePanelEditXmp;
    // End of variables declaration//GEN-END:variables
}
