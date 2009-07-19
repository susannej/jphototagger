package de.elmar_baumann.imv.view.dialogs;

import de.elmar_baumann.imv.UserSettings;
import de.elmar_baumann.imv.app.AppIcons;
import de.elmar_baumann.imv.app.MessageDisplayer;
import de.elmar_baumann.imv.resource.Bundle;
import de.elmar_baumann.imv.view.panels.FileEditorPanel;
import de.elmar_baumann.lib.dialog.Dialog;

/**
 * Dialog with a {@link de.elmar_baumann.imv.view.panels.FileEditorPanel}.
 * Closing is disabled as long as the file editor runs.
 * 
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2009-05-22
 */
public class FileEditorDialog extends Dialog {

    public FileEditorDialog(java.awt.Frame parent) {
        super(parent, false);
        initComponents();
        postInitComponents();
    }

    public FileEditorPanel getFileEditorPanel() {
        return panelFileEditor;
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            setTitle(panelFileEditor.getTitle());
            readProperties();
        } else {
            writeProperties();
            disposeIfNotRunning();
        }
        super.setVisible(visible);
    }

    private void disposeIfNotRunning() {
        if (panelFileEditor.isRunning()) {
            MessageDisplayer.error("FileEditorDialog.Error.Running"); // NOI18N
        } else {
            dispose();
        }
    }

    private void postInitComponents() {
        setIconImages(AppIcons.getAppIcons());
        setHelpContentsUrl(Bundle.getString("Help.Url.Contents")); // NOI18N
        registerKeyStrokes();
    }

    private void readProperties() {
        panelFileEditor.readProperties();
        UserSettings.INSTANCE.getSettings().getSizeAndLocation(this);
    }

    private void writeProperties() {
        panelFileEditor.writeProperties();
        UserSettings.INSTANCE.getSettings().setSizeAndLocation(this);
        UserSettings.INSTANCE.writeToFile();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelFileEditor = new de.elmar_baumann.imv.view.panels.FileEditorPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFileEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFileEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                FileEditorDialog dialog = new FileEditorDialog(
                        new javax.swing.JFrame());
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
    private de.elmar_baumann.imv.view.panels.FileEditorPanel panelFileEditor;
    // End of variables declaration//GEN-END:variables
}
