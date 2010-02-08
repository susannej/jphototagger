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
import de.elmar_baumann.jpt.controller.actions.ControllerActionExecutor;
import de.elmar_baumann.jpt.data.Program;
import de.elmar_baumann.jpt.database.DatabasePrograms;
import de.elmar_baumann.jpt.event.DatabaseProgramsEvent;
import de.elmar_baumann.jpt.event.DatabaseProgramsEvent.Type;
import de.elmar_baumann.jpt.event.listener.DatabaseProgramsListener;
import de.elmar_baumann.jpt.event.listener.ProgramActionListener;
import de.elmar_baumann.jpt.resource.Bundle;
import de.elmar_baumann.jpt.resource.GUI;
import de.elmar_baumann.lib.dialog.Dialog;
import javax.swing.JProgressBar;

/**
 * Non modal dialog for actions: {@link de.elmar_baumann.jpt.data.Program}
 * where {@link de.elmar_baumann.jpt.data.Program#isAction()} is true.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 */
public final class ActionsDialog extends Dialog implements DatabaseProgramsListener {

    public static final  ActionsDialog INSTANCE         = new ActionsDialog();
    private static final long          serialVersionUID = -2671488119703014515L;

    private ActionsDialog() {
        super(GUI.INSTANCE.getAppFrame(), false, UserSettings.INSTANCE.getSettings(), null);
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        setHelpPages();
        DatabasePrograms.INSTANCE.addListener(this);
    }

    private void setHelpPages() {
        setHelpContentsUrl(Bundle.getString("Help.Url.Contents"));
        setHelpPageUrl(Bundle.getString("Help.Url.ActionsDialog"));
    }

    public JProgressBar getProgressBar(ControllerActionExecutor executor) {
        return panelActions.getProgressBar(executor);
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            panelActions.setButtonsEnabled();
        }
        super.setVisible(visible);
    }

    public synchronized void addActionListener(ProgramActionListener l) {
        panelActions.addListener(l);
    }

    @Override
    public void actionPerformed(DatabaseProgramsEvent evt) {
        Type type = evt.getType();
        if ((type.equals(Type.PROGRAM_INSERTED) ||
            type.equals(Type.PROGRAM_UPDATED)) && isVisible()) {
            toFront();
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

        panelActions = new de.elmar_baumann.jpt.view.panels.ActionsPanel();

        setTitle(Bundle.getString("ActionsDialog.title"));
        setAlwaysOnTop(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelActions, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelActions, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
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
                ActionsDialog dialog = new ActionsDialog();
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
    private de.elmar_baumann.jpt.view.panels.ActionsPanel panelActions;
    // End of variables declaration//GEN-END:variables
}
