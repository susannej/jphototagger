package org.jphototagger.program.module.actions;

import javax.swing.SwingUtilities;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.jphototagger.domain.repository.event.programs.ProgramInsertedEvent;
import org.jphototagger.domain.repository.event.programs.ProgramUpdatedEvent;
import org.jphototagger.lib.api.LookAndFeelChangedEvent;
import org.jphototagger.lib.swing.Dialog;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.program.resource.GUI;

/**
 * Non modal dialog for actions: {@code org.jphototagger.program.data.Program}
 * where {@code org.jphototagger.program.data.Program#isAction()} is true.
 *
 * @author Elmar Baumann
 */
public final class ActionsDialog extends Dialog {

    private static final long serialVersionUID = 1L;
    public static final ActionsDialog INSTANCE = new ActionsDialog();

    private ActionsDialog() {
        super(GUI.getAppFrame(), false);
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        setHelpPage();
        AnnotationProcessor.process(this);
    }

    private void setHelpPage() {
        setHelpPageUrl(Bundle.getString(ActionsDialog.class, "ActionsDialog.HelpPage"));
    }

    public ActionsPanel getPanelActions() {
        return panelActions;
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            panelActions.setEnabled();
        }
        super.setVisible(visible);
    }

    private void toFrontIfVisible() {
        if (isVisible()) {
            toFront();
        }
    }

    @EventSubscriber(eventClass = ProgramInsertedEvent.class)
    public void programInserted(final ProgramInsertedEvent evt) {
        toFrontIfVisible();
    }

    @EventSubscriber(eventClass = ProgramUpdatedEvent.class)
    public void programUpdated(final ProgramUpdatedEvent evt) {
        toFrontIfVisible();
    }

    @EventSubscriber(eventClass = LookAndFeelChangedEvent.class)
    public void lookAndFeelChanged(LookAndFeelChangedEvent evt) {
        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        panelActions = new org.jphototagger.program.module.actions.ActionsPanel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/program/module/actions/Bundle"); // NOI18N
        setTitle(bundle.getString("ActionsDialog.title")); // NOI18N
        setAlwaysOnTop(true);
        setName("Form"); // NOI18N

        panelActions.setName("panelActions"); // NOI18N

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
    }//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jphototagger.program.module.actions.ActionsPanel panelActions;
    // End of variables declaration//GEN-END:variables
}
