package de.elmar_baumann.imagemetadataviewer.view.panels;

import de.elmar_baumann.imagemetadataviewer.model.ComboBoxModelMetaDataEditTemplates;
import de.elmar_baumann.imagemetadataviewer.resource.Bundle;

/**
 * Panel for action sources (buttons) related to edit metadata.
 * 
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 */
public class MetaDataEditActionsPanel extends javax.swing.JPanel {

    /** Creates new form MetaDataEditActionsPanel */
    public MetaDataEditActionsPanel() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelGroupMetadataEdit = new javax.swing.JPanel();
        labelMetadataInfoEditable = new javax.swing.JLabel();
        buttonSaveMetadata = new javax.swing.JButton();
        buttonMetaDataTemplateCreate = new javax.swing.JButton();
        panelGroupMetadataTemplates = new javax.swing.JPanel();
        comboBoxMetaDataTemplates = new javax.swing.JComboBox();
        buttonMetaDataTemplateRename = new javax.swing.JButton();
        buttonMetaDataTemplateDelete = new javax.swing.JButton();
        buttonMetaDataTemplateInsert = new javax.swing.JButton();
        buttonMetaDataTemplateUpdate = new javax.swing.JButton();

        panelGroupMetadataEdit.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Daten bearbeiten", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 11))); // NOI18N

        labelMetadataInfoEditable.setFont(new java.awt.Font("Dialog", 0, 12));
        labelMetadataInfoEditable.setText(Bundle.getString("MetaDataEditActionsPanel.labelMetadataInfoEditable.text")); // NOI18N

        buttonSaveMetadata.setFont(new java.awt.Font("Dialog", 0, 12));
        buttonSaveMetadata.setMnemonic('s');
        buttonSaveMetadata.setText(Bundle.getString("MetaDataEditActionsPanel.buttonSaveMetadata.text")); // NOI18N
        buttonSaveMetadata.setEnabled(false);

        buttonMetaDataTemplateCreate.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        buttonMetaDataTemplateCreate.setMnemonic('v');
        buttonMetaDataTemplateCreate.setText(Bundle.getString("MetaDataEditActionsPanel.buttonMetaDataTemplateCreate.text")); // NOI18N

        javax.swing.GroupLayout panelGroupMetadataEditLayout = new javax.swing.GroupLayout(panelGroupMetadataEdit);
        panelGroupMetadataEdit.setLayout(panelGroupMetadataEditLayout);
        panelGroupMetadataEditLayout.setHorizontalGroup(
            panelGroupMetadataEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGroupMetadataEditLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGroupMetadataEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelMetadataInfoEditable, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                    .addGroup(panelGroupMetadataEditLayout.createSequentialGroup()
                        .addComponent(buttonMetaDataTemplateCreate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonSaveMetadata, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelGroupMetadataEditLayout.setVerticalGroup(
            panelGroupMetadataEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGroupMetadataEditLayout.createSequentialGroup()
                .addComponent(labelMetadataInfoEditable)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelGroupMetadataEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonMetaDataTemplateCreate)
                    .addComponent(buttonSaveMetadata, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelGroupMetadataTemplates.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datenvorlagen", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 11))); // NOI18N

        comboBoxMetaDataTemplates.setModel(new ComboBoxModelMetaDataEditTemplates());

        buttonMetaDataTemplateRename.setFont(new java.awt.Font("Dialog", 0, 12));
        buttonMetaDataTemplateRename.setMnemonic('m');
        buttonMetaDataTemplateRename.setText(Bundle.getString("MetaDataEditActionsPanel.buttonMetaDataTemplateRename.text")); // NOI18N
        buttonMetaDataTemplateRename.setEnabled(false);

        buttonMetaDataTemplateDelete.setFont(new java.awt.Font("Dialog", 0, 12));
        buttonMetaDataTemplateDelete.setMnemonic('\u00f6');
        buttonMetaDataTemplateDelete.setText(Bundle.getString("MetaDataEditActionsPanel.buttonMetaDataTemplateDelete.text")); // NOI18N
        buttonMetaDataTemplateDelete.setEnabled(false);

        buttonMetaDataTemplateInsert.setFont(new java.awt.Font("Dialog", 0, 12));
        buttonMetaDataTemplateInsert.setMnemonic('e');
        buttonMetaDataTemplateInsert.setText(Bundle.getString("MetaDataEditActionsPanel.buttonMetaDataTemplateInsert.text")); // NOI18N
        buttonMetaDataTemplateInsert.setEnabled(false);

        buttonMetaDataTemplateUpdate.setFont(new java.awt.Font("Dialog", 0, 12));
        buttonMetaDataTemplateUpdate.setMnemonic('a');
        buttonMetaDataTemplateUpdate.setText(Bundle.getString("MetaDataEditActionsPanel.buttonMetaDataTemplateUpdate.text")); // NOI18N
        buttonMetaDataTemplateUpdate.setEnabled(false);

        javax.swing.GroupLayout panelGroupMetadataTemplatesLayout = new javax.swing.GroupLayout(panelGroupMetadataTemplates);
        panelGroupMetadataTemplates.setLayout(panelGroupMetadataTemplatesLayout);
        panelGroupMetadataTemplatesLayout.setHorizontalGroup(
            panelGroupMetadataTemplatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGroupMetadataTemplatesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGroupMetadataTemplatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboBoxMetaDataTemplates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelGroupMetadataTemplatesLayout.createSequentialGroup()
                        .addComponent(buttonMetaDataTemplateUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonMetaDataTemplateInsert))
                    .addGroup(panelGroupMetadataTemplatesLayout.createSequentialGroup()
                        .addComponent(buttonMetaDataTemplateDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonMetaDataTemplateRename)))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        panelGroupMetadataTemplatesLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {buttonMetaDataTemplateDelete, buttonMetaDataTemplateInsert, buttonMetaDataTemplateRename, buttonMetaDataTemplateUpdate});

        panelGroupMetadataTemplatesLayout.setVerticalGroup(
            panelGroupMetadataTemplatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGroupMetadataTemplatesLayout.createSequentialGroup()
                .addComponent(comboBoxMetaDataTemplates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGroupMetadataTemplatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonMetaDataTemplateUpdate)
                    .addComponent(buttonMetaDataTemplateInsert))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGroupMetadataTemplatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonMetaDataTemplateDelete)
                    .addComponent(buttonMetaDataTemplateRename))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelGroupMetadataTemplatesLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {buttonMetaDataTemplateDelete, buttonMetaDataTemplateInsert, buttonMetaDataTemplateRename, buttonMetaDataTemplateUpdate});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelGroupMetadataTemplates, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelGroupMetadataEdit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelGroupMetadataEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelGroupMetadataTemplates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton buttonMetaDataTemplateCreate;
    public javax.swing.JButton buttonMetaDataTemplateDelete;
    public javax.swing.JButton buttonMetaDataTemplateInsert;
    public javax.swing.JButton buttonMetaDataTemplateRename;
    public javax.swing.JButton buttonMetaDataTemplateUpdate;
    public javax.swing.JButton buttonSaveMetadata;
    public javax.swing.JComboBox comboBoxMetaDataTemplates;
    public javax.swing.JLabel labelMetadataInfoEditable;
    private javax.swing.JPanel panelGroupMetadataEdit;
    private javax.swing.JPanel panelGroupMetadataTemplates;
    // End of variables declaration//GEN-END:variables

}
