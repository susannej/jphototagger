package org.jphototagger.program.module.search;

import javax.swing.SwingUtilities;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.jphototagger.lib.api.LookAndFeelChangedEvent;
import org.jphototagger.lib.swing.Dialog;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.program.resource.GUI;

/**
 * Nicht modaler Dialog für eine erweiterte Suche.
 *
 * @author Elmar Baumann
 */
public final class AdvancedSearchDialog extends Dialog implements NameListener {

    public static final AdvancedSearchDialog INSTANCE = new AdvancedSearchDialog(false);
    private static final long serialVersionUID = 1L;

    private AdvancedSearchDialog(boolean modal) {
        super(GUI.getAppFrame(), modal);
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        panel.addNameListener(this);
        AnnotationProcessor.process(this);
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            panel.restore();
            setSearchName(panel.getSearchName());
        }
        super.setVisible(visible);
    }

    private void beforeWindowClosing() {
        panel.persist();
        panel.willDispose();
        setVisible(false);
    }

    public AdvancedSearchPanel getPanel() {
        return panel;
    }

    @Override
    protected void showHelp() {
        showHelp(Bundle.getString(AdvancedSearchDialog.class, "AdvancedSearchDialog.HelpPage"));
    }

    @Override
    protected void escape() {
        beforeWindowClosing();
    }

    public AdvancedSearchPanel getAdvancedSearchPanel() {
        return panel;
    }

    @Override
    public void nameChanged(String name) {
        if (name == null) {
            throw new NullPointerException("name == null");
        }
        setSearchName(name);
    }

    private void setSearchName(String name) {
        String delimiter =  ": ";
        setTitle(Bundle.getString(AdvancedSearchDialog.class, "AdvancedSearchDialog.TitlePrefix") + delimiter + name);
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

        panel = new org.jphototagger.program.module.search.AdvancedSearchPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/program/module/search/Bundle"); // NOI18N
        setTitle(bundle.getString("AdvancedSearchDialog.title")); // NOI18N
        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        panel.setName("panel"); // NOI18N

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
    }//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        beforeWindowClosing();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jphototagger.program.module.search.AdvancedSearchPanel panel;
    // End of variables declaration//GEN-END:variables
}
