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
package de.elmar_baumann.jpt.view.dialogs;

import de.elmar_baumann.jpt.UserSettings;
import de.elmar_baumann.jpt.data.SavedSearch;
import de.elmar_baumann.jpt.event.SearchEvent;
import de.elmar_baumann.jpt.event.listener.SearchListener;
import de.elmar_baumann.jpt.resource.JptBundle;
import de.elmar_baumann.jpt.resource.GUI;
import de.elmar_baumann.jpt.view.panels.AdvancedSearchPanel;
import de.elmar_baumann.lib.dialog.Dialog;

/**
 * Nicht modaler Dialog für eine erweiterte Suche.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>, Tobias Stening <info@swts.net>
 * @version 2008-10-05
 */
public final class AdvancedSearchDialog extends Dialog implements SearchListener {

    public static final  AdvancedSearchDialog INSTANCE         = new AdvancedSearchDialog(false);
    private static final long                 serialVersionUID = -7381253840654600441L;

    private AdvancedSearchDialog(boolean modal) {
        super(GUI.INSTANCE.getAppFrame(), modal, UserSettings.INSTANCE.getSettings(), null);
        initComponents();
        setHelpContentsUrl(JptBundle.INSTANCE.getString("Help.Url.Contents"));
    }

    public void setSavedSearch(SavedSearch savedSearch) {
        panel.setSavedSearch(savedSearch);
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            panel.readProperties();
        }
        super.setVisible(visible);
    }

    private void beforeWindowClosing() {
        panel.writeProperties();
        panel.willDispose();
        setVisible(false);
    }

    @Override
    protected void help() {
        help(JptBundle.INSTANCE.getString("Help.Url.AdvancedSearchDialog"));
    }

    @Override
    protected void escape() {
        beforeWindowClosing();
    }

    public AdvancedSearchPanel getAdvancedSearchPanel() {
        return panel;
    }

    @Override
    public void actionPerformed(SearchEvent evt) {
        if (evt.getType().equals(SearchEvent.Type.NAME_CHANGED)) {
            String name = evt.getSearchName();
            String separator = name.isEmpty()
                               ? ""
                               : ": ";
            setTitle(JptBundle.INSTANCE.getString("AdvancedSearchDialog.TitlePrefix") + separator + name);
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

        panel = new de.elmar_baumann.jpt.view.panels.AdvancedSearchPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(JptBundle.INSTANCE.getString("AdvancedSearchDialog.title")); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    beforeWindowClosing();
}//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                AdvancedSearchDialog dialog = new AdvancedSearchDialog(true);
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
    private de.elmar_baumann.jpt.view.panels.AdvancedSearchPanel panel;
    // End of variables declaration//GEN-END:variables
}
