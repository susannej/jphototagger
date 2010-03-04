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
package de.elmar_baumann.jpt.view.dialogs;

import de.elmar_baumann.jpt.UserSettings;
import de.elmar_baumann.jpt.resource.GUI;
import de.elmar_baumann.jpt.resource.JptBundle;
import de.elmar_baumann.jpt.view.panels.ExportImportPanel;
import de.elmar_baumann.lib.dialog.Dialog;

/**
 *
 * @author Elmar Baumann <eb@elmar-baumann.de>
 */
public class ExportImportDialog extends Dialog {

    private static final long                      serialVersionUID = 8937656035473070405L;
    private final        ExportImportPanel.Context context;

    public ExportImportDialog(ExportImportPanel.Context context) {
        super(GUI.INSTANCE.getAppFrame(), UserSettings.INSTANCE.getSettings(), "ExportImportDialog");
        this.context = context;
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        setTitle(context.equals(ExportImportPanel.Context.EXPORT)
                ? JptBundle.INSTANCE.getString("ExportImportDialog.Title.Export")
                : JptBundle.INSTANCE.getString("ExportImportDialog.Title.Import"));

        setHelpContentsUrl(JptBundle.INSTANCE.getString("Help.Url.Contents"));
        setHelpPageUrl(context.equals(ExportImportPanel.Context.EXPORT)
                ? JptBundle.INSTANCE.getString("Help.Url.ExportImportDialog.Export")
                : JptBundle.INSTANCE.getString("Help.Url.ExportImportDialog.Import")
                );
        panelExportImport.setContext(context);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelExportImport = new de.elmar_baumann.jpt.view.panels.ExportImportPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelExportImport, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelExportImport, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ExportImportDialog dialog = new ExportImportDialog(ExportImportPanel.Context.EXPORT);
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
    private de.elmar_baumann.jpt.view.panels.ExportImportPanel panelExportImport;
    // End of variables declaration//GEN-END:variables

}