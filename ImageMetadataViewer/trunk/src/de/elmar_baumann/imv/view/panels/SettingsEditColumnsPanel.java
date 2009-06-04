package de.elmar_baumann.imv.view.panels;

import de.elmar_baumann.imv.UserSettings;
import de.elmar_baumann.imv.database.metadata.Column;
import de.elmar_baumann.imv.database.metadata.ColumnUtil;
import de.elmar_baumann.imv.database.metadata.selections.EditColumns;
import de.elmar_baumann.imv.event.ListenerProvider;
import de.elmar_baumann.imv.event.UserSettingsChangeEvent;
import de.elmar_baumann.imv.model.ListModelSelectedColumns;
import de.elmar_baumann.imv.resource.Bundle;
import de.elmar_baumann.imv.types.Persistence;
import de.elmar_baumann.lib.component.CheckList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ListSelectionModel;

/**
 * Panel to (de-) select columns to show in the {@link EditMetadataPanelsArray}.
 * 
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/11/01
 */
public final class SettingsEditColumnsPanel extends javax.swing.JPanel
    implements ActionListener, Persistence {

    private final ListenerProvider listenerProvider = ListenerProvider.INSTANCE;
    CheckList list;

    /** Creates new form SettingsEditColumnsPanel */
    public SettingsEditColumnsPanel() {
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        list = new CheckList();
        list.setModel(new ListModelSelectedColumns(
            new ArrayList<Column>(EditColumns.getColumns())));
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.addActionListener(this);
        scrollPane.setViewportView(list);
    }

    @Override
    public void readProperties() {
        list.setSelectedItemsWithText(
            ColumnUtil.getDescriptionsOfColumns(
            UserSettings.INSTANCE.getEditColumns()), true);
    }

    @Override
    public void writeProperties() {
    }

    private UserSettingsChangeEvent getUserSettingsChangeEvent() {
        UserSettingsChangeEvent evt = new UserSettingsChangeEvent(
            UserSettingsChangeEvent.Type.EDIT_COLUMNS, this);
        evt.setEditColumns(ColumnUtil.getSelectedColumns(list));
        return evt;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        listenerProvider.notifyUserSettingsChangeListener(getUserSettingsChangeEvent());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelPrompt = new javax.swing.JLabel();
        scrollPane = new javax.swing.JScrollPane();

        labelPrompt.setText(Bundle.getString("SettingsEditColumnsPanel.labelPrompt.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelPrompt)
                    .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelPrompt, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelPrompt;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
}
