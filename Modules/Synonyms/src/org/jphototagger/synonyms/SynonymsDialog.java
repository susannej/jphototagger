package org.jphototagger.synonyms;

import org.jphototagger.lib.swing.Dialog;
import org.jphototagger.lib.swing.util.ComponentUtil;
import org.jphototagger.lib.util.Bundle;

/**
 * @author Elmar Baumann
 */
public class SynonymsDialog extends Dialog {
    private static final long serialVersionUID = 1L;
    public static final SynonymsDialog INSTANCE = new SynonymsDialog();

    private SynonymsDialog() {
        super(ComponentUtil.findFrameWithIcon(), false);
        setPreferencesKey("SynonymsDialog");
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        setHelpPage();
    }

    private void setHelpPage() {
        setHelpPageUrl(Bundle.getString(SynonymsDialog.class, "SynonymsDialog.HelpPage"));
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")

    private void initComponents() {//GEN-BEGIN:initComponents

        synonymsPanel1 = new org.jphototagger.synonyms.SynonymsPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/synonyms/Bundle"); // NOI18N
        setTitle(bundle.getString("SynonymsDialog.title")); // NOI18N
        setName("Form"); // NOI18N

        synonymsPanel1.setName("synonymsPanel1"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(synonymsPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(synonymsPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                SynonymsDialog dialog = new SynonymsDialog();

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
    private org.jphototagger.synonyms.SynonymsPanel synonymsPanel1;
    // End of variables declaration//GEN-END:variables
}
