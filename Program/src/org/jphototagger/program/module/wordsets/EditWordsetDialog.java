package org.jphototagger.program.module.wordsets;

import org.jphototagger.domain.wordsets.Wordset;
import org.jphototagger.lib.swing.Dialog;
import org.jphototagger.lib.swing.MessageDisplayer;
import org.jphototagger.lib.swing.util.ComponentUtil;
import org.jphototagger.lib.util.Bundle;

/**
 * @author Elmar Baumann
 */
public class EditWordsetDialog extends Dialog {

    private static final long serialVersionUID = 1L;
    private final Wordset wordset;
    private boolean selectNameTextfield;

    public EditWordsetDialog() {
        this(new Wordset(Bundle.getString(EditWordsetDialog.class, "EditWordset.DefaultWordsetName")));
        selectNameTextfield = true;
    }

    public EditWordsetDialog(Wordset wordset) {
        super(ComponentUtil.findFrameWithIcon(), true);
        this.wordset = wordset;
        initComponents();
    }

    private void checkDirty() {
        if (panelEditWordset.isDirty() && MessageDisplayer.confirmYesNo(this,
                Bundle.getString(EditWordsetDialog.class, "EditWordsetDialog.Confirm.Save"))) {
            panelEditWordset.saveWordset();
        }
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            panelEditWordset.enableAutocomplete();
            if (selectNameTextfield) {
                panelEditWordset.selectNameTextField();
            } else {
                panelEditWordset.selectWordTextField();
            }
        }
        super.setVisible(visible);
    }

    @Override
    protected void escape() {
        checkDirty();
        super.escape();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panelEditWordset = new EditWordsetPanel(wordset);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/program/module/wordsets/Bundle"); // NOI18N
        setTitle(bundle.getString("EditWordsetDialog.title")); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panelEditWordset.setPreferredSize(new java.awt.Dimension(300, 200));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(panelEditWordset, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        checkDirty();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jphototagger.program.module.wordsets.EditWordsetPanel panelEditWordset;
    // End of variables declaration//GEN-END:variables
}
