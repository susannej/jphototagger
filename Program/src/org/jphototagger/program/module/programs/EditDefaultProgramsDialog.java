package org.jphototagger.program.module.programs;

import org.jphototagger.lib.swing.Dialog;
import org.jphototagger.lib.swing.util.ComponentUtil;

/**
 * @author Elmar Baumann
 */
public class EditDefaultProgramsDialog extends Dialog {

    private static final long serialVersionUID = 1L;

    public EditDefaultProgramsDialog() {
        super(ComponentUtil.findFrameWithIcon(), true);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        panelEditDefaultPrograms = new org.jphototagger.program.module.programs.EditDefaultProgramsPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/program/module/programs/Bundle"); // NOI18N
        setTitle(bundle.getString("EditDefaultProgramsDialog.title")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelEditDefaultPrograms, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelEditDefaultPrograms, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jphototagger.program.module.programs.EditDefaultProgramsPanel panelEditDefaultPrograms;
    // End of variables declaration//GEN-END:variables
}
