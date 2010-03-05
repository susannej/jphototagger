/*
 * JPhotoTagger tags and finds images fast.
 * Copyright (C) 2009-2010 by the JPhotoTagger developer team.
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package de.elmar_baumann.jpt.view.panels;

import de.elmar_baumann.jpt.UserSettings;
import de.elmar_baumann.jpt.database.DatabaseStatistics;
import de.elmar_baumann.jpt.event.listener.impl.DatabaseTotalRecordCountListener;
import de.elmar_baumann.jpt.model.TableModelDatabaseInfo;
import de.elmar_baumann.jpt.resource.JptBundle;
import de.elmar_baumann.jpt.types.Filename;
import de.elmar_baumann.jpt.view.renderer.TableCellRendererDatabaseInfoColumns;

/**
 * Dislplays the database record count total and of specific columns.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008-11-08
 */
public final class DatabaseInfoCountPanel extends javax.swing.JPanel {

    private static final    long                             serialVersionUID         = -8537559082830438692L;
    private final transient DatabaseTotalRecordCountListener listenerTotalRecordCount = new DatabaseTotalRecordCountListener();
    private                 TableModelDatabaseInfo           modelDatabaseInfo;
    private volatile        boolean                          listenToDbChanges;

    public DatabaseInfoCountPanel() {
        initComponents();
        table.setDefaultRenderer(Object.class, new TableCellRendererDatabaseInfoColumns());
        setLabelFilename();
    }

    public void listenToDatabaseChanges(boolean listen) {
        listenToDbChanges = listen;
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
        thread.setName("Database info setting initial total record count @ " + getClass().getSimpleName());
        thread.start();
    }

    private void setLabelFilename() {
        labelFilename.setText(JptBundle.INSTANCE.getString("DatabaseInfoCountPanel.labelFilename.Filename", UserSettings.INSTANCE.getDatabaseFileName(Filename.FULL_PATH)));
    }

    private void setModelDatabaseInfo() {
        if (modelDatabaseInfo == null) {
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    modelDatabaseInfo = new TableModelDatabaseInfo();
                    modelDatabaseInfo.setListenToDatabase(listenToDbChanges);
                    table.setModel(modelDatabaseInfo);
                    modelDatabaseInfo.update();
                    setInitTotalRecordCount();
                }
            });
            thread.setName("Database info creating table model database info @ " + getClass().getSimpleName());
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

        labelTable.setText(JptBundle.INSTANCE.getString("DatabaseInfoCountPanel.labelTable.text"));

        scrollPane.setViewportView(table);

        labelPromptTotalRecordCount.setText(JptBundle.INSTANCE.getString("DatabaseInfoCountPanel.labelPromptTotalRecordCount.text"));

        labelFilename.setText(JptBundle.INSTANCE.getString("DatabaseInfoCountPanel.labelFilename.text"));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(labelTable, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelPromptTotalRecordCount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelTotalRecordCount, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
                    .addComponent(labelFilename, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTable)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelPromptTotalRecordCount)
                    .addComponent(labelTotalRecordCount, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelFilename, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
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
