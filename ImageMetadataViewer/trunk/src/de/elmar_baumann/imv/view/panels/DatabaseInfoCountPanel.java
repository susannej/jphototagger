package de.elmar_baumann.imv.view.panels;

import de.elmar_baumann.imv.UserSettings;
import de.elmar_baumann.imv.database.DatabaseStatistics;
import de.elmar_baumann.imv.event.listener.TotalRecordCountListener;
import de.elmar_baumann.imv.model.TableModelDatabaseInfo;
import de.elmar_baumann.imv.resource.Bundle;
import de.elmar_baumann.imv.view.renderer.TableCellRendererDatabaseInfoColumns;

/**
 * Dislplays the database record count total and of specific columns.
 * 
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/11/08
 */
public final class DatabaseInfoCountPanel extends javax.swing.JPanel {

    private final TotalRecordCountListener listenerTotalRecordCount = new TotalRecordCountListener();
    private TableModelDatabaseInfo modelDatabaseInfo;

    /** Creates new form DatabaseInfoCountPanel */
    public DatabaseInfoCountPanel() {
        initComponents();
        table.setDefaultRenderer(Object.class, new TableCellRendererDatabaseInfoColumns());
        setLabelFilename();
    }

    public void listenToDatabaseChanges(boolean listen) {
        if (listen) {
            listenerTotalRecordCount.addLabel(labelTotalRecordCount);
            setModelDatabaseInfo();
        } else {
            listenerTotalRecordCount.removeLabel(labelTotalRecordCount);
        }
        listenerTotalRecordCount.setListenToDatabase(listen);
        if (modelDatabaseInfo != null) {
            modelDatabaseInfo.setListenToDatabase(listen);
        }
    }

    private void setInitTotalRecordCount() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                labelTotalRecordCount.setText(Long.toString(
                        DatabaseStatistics.INSTANCE.getTotalRecordCount()));
            }
        });
        thread.setName("DatabaseInfoCountPanel#setInitTotalRecordCount"); // NOI18N
        thread.start();
    }

    private void setLabelFilename() {
        labelFilename.setText(Bundle.getString(
                "DatabaseInfoCountPanel.labelFilename.Filename",
                UserSettings.INSTANCE.getDatabaseFileName()));
    }

    private void setModelDatabaseInfo() {
        if (modelDatabaseInfo == null) {
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    modelDatabaseInfo = new TableModelDatabaseInfo();
                    table.setModel(modelDatabaseInfo);
                    modelDatabaseInfo.update();
                    setInitTotalRecordCount();
                }
            });
            thread.setName("DatabaseInfoCountPanel#setModelDatabaseInfo"); // NOI18N
            thread.start();
        } else {
            setInitTotalRecordCount();
            modelDatabaseInfo.setListenToDatabase(true);
            modelDatabaseInfo.update();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelTable = new javax.swing.JLabel();
        scrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        labelPromptTotalRecordCount = new javax.swing.JLabel();
        labelTotalRecordCount = new javax.swing.JLabel();
        labelFilename = new javax.swing.JLabel();

        labelTable.setText(Bundle.getString("DatabaseInfoCountPanel.labelTable.text")); // NOI18N

        scrollPane.setViewportView(table);

        labelPromptTotalRecordCount.setText(Bundle.getString("DatabaseInfoCountPanel.labelPromptTotalRecordCount.text")); // NOI18N

        labelFilename.setText(Bundle.getString("DatabaseInfoCountPanel.labelFilename.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scrollPane, 0, 216, Short.MAX_VALUE)
                            .addComponent(labelTable, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelPromptTotalRecordCount)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelTotalRecordCount, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelFilename, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(175, 175, 175))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTable)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelPromptTotalRecordCount, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelTotalRecordCount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelFilename)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {labelPromptTotalRecordCount, labelTotalRecordCount});

    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelFilename;
    private javax.swing.JLabel labelPromptTotalRecordCount;
    private javax.swing.JLabel labelTable;
    private javax.swing.JLabel labelTotalRecordCount;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
