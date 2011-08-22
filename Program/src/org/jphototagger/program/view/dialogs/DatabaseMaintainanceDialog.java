package org.jphototagger.program.view.dialogs;

import org.jphototagger.api.core.Storage;
import org.jphototagger.lib.componentutil.TabbedPaneUtil;
import org.jphototagger.lib.dialog.Dialog;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.program.UserSettings;
import org.jphototagger.lib.dialog.MessageDisplayer;
import org.jphototagger.program.resource.GUI;
import org.openide.util.Lookup;

/**
 * Modaler Dialog zur Wartung der Thumbnaildatenbank.
 *
 * @author Elmar Baumann
 */
public final class DatabaseMaintainanceDialog extends Dialog {
    private static final long serialVersionUID = -6775385212305459197L;
    public static final DatabaseMaintainanceDialog INSTANCE = new DatabaseMaintainanceDialog();

    private DatabaseMaintainanceDialog() {
        super(GUI.getAppFrame(), false);
        initComponents();
        setHelpPage();
        TabbedPaneUtil.setMnemonics(tabbedPane);
    }

    private void setHelpPage() {
        // Has to be localized!
        setHelpContentsUrl("/org/jphototagger/program/resource/doc/de/contents.xml");
        setHelpPageUrl("maintain_database.html");
    }

    @Override
    public void setVisible(boolean visible) {
        Storage storage = Lookup.getDefault().lookup(Storage.class);

        if (visible) {
            storage.applyComponentSettings(this, UserSettings.SET_TABBED_PANE_SETTINGS);
        } else {
            storage.setComponent(this, UserSettings.SET_TABBED_PANE_SETTINGS);
        }

        panelMaintainance.getsVisible(visible);
        panelCount.listenToDatabaseChanges(visible);
        super.setVisible(visible);
    }

    private void close() {
        if (panelMaintainance.canClose()) {
            Storage storage = Lookup.getDefault().lookup(Storage.class);

            storage.setComponent(this, UserSettings.SET_TABBED_PANE_SETTINGS);
            setVisible(false);
        } else {
            String message = Bundle.getString(DatabaseMaintainanceDialog.class, "DatabaseMaintainanceDialog.Error.WaitBeforeClose");
            MessageDisplayer.error(this, message);
        }
    }

    @Override
    protected void escape() {
        close();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")

    private void initComponents() {//GEN-BEGIN:initComponents

        tabbedPane = new javax.swing.JTabbedPane();
        panelCount = new org.jphototagger.program.view.panels.DatabaseInfoCountPanel();
        panelMaintainance = new org.jphototagger.program.view.panels.DatabaseMaintainancePanel();
        panelDatabaseUpdate = new org.jphototagger.program.view.panels.DatabaseUpdatePanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/program/view/dialogs/Bundle"); // NOI18N
        setTitle(bundle.getString("DatabaseMaintainanceDialog.title")); // NOI18N
        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tabbedPane.setName("tabbedPane"); // NOI18N

        panelCount.setName("panelCount"); // NOI18N
        tabbedPane.addTab(bundle.getString("DatabaseMaintainanceDialog.panelCount.TabConstraints.tabTitle"), panelCount); // NOI18N

        panelMaintainance.setName("panelMaintainance"); // NOI18N
        tabbedPane.addTab(bundle.getString("DatabaseMaintainanceDialog.panelMaintainance.TabConstraints.tabTitle"), panelMaintainance); // NOI18N

        panelDatabaseUpdate.setName("panelDatabaseUpdate"); // NOI18N
        tabbedPane.addTab(bundle.getString("DatabaseMaintainanceDialog.panelDatabaseUpdate.TabConstraints.tabTitle"), panelDatabaseUpdate); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane)
                .addContainerGap())
        );

        pack();
    }//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        close();
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                DatabaseMaintainanceDialog dialog =
                    new DatabaseMaintainanceDialog();

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
    private org.jphototagger.program.view.panels.DatabaseInfoCountPanel panelCount;
    private org.jphototagger.program.view.panels.DatabaseUpdatePanel panelDatabaseUpdate;
    private org.jphototagger.program.view.panels.DatabaseMaintainancePanel panelMaintainance;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
}
