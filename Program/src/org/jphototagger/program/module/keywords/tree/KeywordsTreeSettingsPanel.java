package org.jphototagger.program.module.keywords.tree;

import java.awt.Component;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import org.jphototagger.api.preferences.Preferences;
import org.jphototagger.api.windows.OptionPageProvider;
import org.jphototagger.lib.util.Bundle;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Elmar Baumann
 */
@ServiceProvider(service = OptionPageProvider.class)
public class KeywordsTreeSettingsPanel extends javax.swing.JPanel implements OptionPageProvider {

    private static final long serialVersionUID = 1L;

    public KeywordsTreeSettingsPanel() {
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        applyPersistedBoolean(KeywordsTreePreferencesKeys.KEY_AUTO_INSERT_UNKNOWN_KEYWORDS, true, checkBoxAutoInsertUnknownKeywords);
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public String getTitle() {
        return Bundle.getString(KeywordsTreeSettingsPanel.class, "KeywordsTreeSettingsPanel.Title");
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public boolean isMiscOptionPage() {
        return true;
    }

    @Override
    public int getPosition() {
        return 200;
    }

    private void persistBoolean(String key, boolean value) {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
        prefs.setBoolean(key, value);
    }

    private void applyPersistedBoolean(String key, boolean def, AbstractButton button) {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
        button.setSelected(prefs.containsKey(key)
                ? prefs.getBoolean(key)
                : def);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        checkBoxAutoInsertUnknownKeywords = new javax.swing.JCheckBox();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/program/module/keywords/tree/Bundle"); // NOI18N
        checkBoxAutoInsertUnknownKeywords.setText(bundle.getString("KeywordsTreeSettingsPanel.checkBoxAutoInsertUnknownKeywords.text")); // NOI18N
        checkBoxAutoInsertUnknownKeywords.setToolTipText(bundle.getString("KeywordsTreeSettingsPanel.checkBoxAutoInsertUnknownKeywords.toolTipText")); // NOI18N
        checkBoxAutoInsertUnknownKeywords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAutoInsertUnknownKeywordsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(checkBoxAutoInsertUnknownKeywords)
                .addContainerGap(161, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(checkBoxAutoInsertUnknownKeywords)
                .addContainerGap(146, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void checkBoxAutoInsertUnknownKeywordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAutoInsertUnknownKeywordsActionPerformed
        persistBoolean(KeywordsTreePreferencesKeys.KEY_AUTO_INSERT_UNKNOWN_KEYWORDS, checkBoxAutoInsertUnknownKeywords.isSelected());
    }//GEN-LAST:event_checkBoxAutoInsertUnknownKeywordsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkBoxAutoInsertUnknownKeywords;
    // End of variables declaration//GEN-END:variables
}
