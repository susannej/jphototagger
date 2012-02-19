package org.jphototagger.program.plugins;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.openide.util.Lookup;

import org.jphototagger.api.plugin.Plugin;
import org.jphototagger.api.preferences.Preferences;
import org.jphototagger.api.storage.Persistence;
import org.jphototagger.lib.help.HelpPageProvider;
import org.jphototagger.lib.swing.util.MnemonicUtil;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.program.factory.FileProcessorPluginManager;
import org.jphototagger.program.factory.PluginManager;

/**
 * Dynamically adds panels of plugins ({@code AbstractFileProcessorPlugin#getSettingsComponent()}).
 *
 * @author Elmar Baumann
 */
public class PluginsSettingsPanel extends javax.swing.JPanel implements Persistence, HelpPageProvider {

    private static final long serialVersionUID = 1L;
    private static final String KEY_TABBED_PANE = "SettingsPluginsPanel.TabbedPane";

    public PluginsSettingsPanel() {
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        addPlugins(FileProcessorPluginManager.INSTANCE);
        panelExcludeCheckboxes.add(new JPanel(), getGbcAfterLastCheckBox());    // ensures checkboxes vertically top and not centered
        MnemonicUtil.setMnemonics((Container) this);
    }

    private <T extends Plugin> void addPlugins(PluginManager<T> pluginManager) {
        for (Plugin plugin : pluginManager.getEnabledPlugins()) {
            if (plugin.isAvailable()) {
                addPluginSettingsComponent(plugin);
            }
        }

        for (T plugin : pluginManager.getAllPlugins()) {
            if (plugin.isAvailable()) {
                addPluginEnableCheckBox(pluginManager, plugin);
            }
        }
    }

    private void addPluginSettingsComponent(Plugin plugin) {
        Component component = plugin.getSettingsComponent();

        if (component != null) {
            tabbedPane.add(plugin.getDisplayName(), component);
        }
    }

    private <T extends Plugin> void addPluginEnableCheckBox(PluginManager<T> pluginManager, T plugin) {
        ActionExcludePlugin<T> actionExcludePlugin = new ActionExcludePlugin<T>(pluginManager, plugin);
        JCheckBox checkBox = new JCheckBox(actionExcludePlugin);

        checkBox.setSelected(pluginManager.isEnabled(plugin));
        panelExcludeCheckboxes.add(checkBox, getGbcCheckBox());
    }

    private GridBagConstraints getGbcCheckBox() {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = GridBagConstraints.REMAINDER;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;

        return gbc;
    }

    private GridBagConstraints getGbcAfterLastCheckBox() {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = GridBagConstraints.REMAINDER;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        return gbc;
    }

    @Override
    public void restore() {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);

        prefs.applyTabbedPaneSettings(KEY_TABBED_PANE, tabbedPane, null);
    }

    @Override
    public void persist() {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);

        prefs.setTabbedPane(KEY_TABBED_PANE, tabbedPane, null);
    }

    @Override
    public String getHelpPageUrl() {
        return Bundle.getString(PluginsSettingsPanel.class, "PluginsSettingsPanel.HelpPage");
    }

private static class ActionExcludePlugin<T extends Plugin> extends AbstractAction {
        private static final long serialVersionUID = 1L;
        private transient final T plugin;
        private transient final PluginManager<T> pluginManager;

        ActionExcludePlugin(PluginManager<T> pluginManager, T plugin) {
            this.pluginManager = pluginManager;
            this.plugin = plugin;
            putValue(Action.NAME, plugin.getDisplayName());
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JCheckBox cb = (JCheckBox) evt.getSource();

            pluginManager.setEnabled(plugin, cb.isSelected());
        }
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")

    private void initComponents() {//GEN-BEGIN:initComponents

        buttonGroup = new javax.swing.ButtonGroup();
        tabbedPane = new javax.swing.JTabbedPane();
        panelExclude = new javax.swing.JPanel();
        labelInfoExclude = new javax.swing.JLabel();
        scrollPaneExclude = new javax.swing.JScrollPane();
        panelExcludeCheckboxes = new javax.swing.JPanel();

        setName("Form"); // NOI18N

        tabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setName("tabbedPane"); // NOI18N

        panelExclude.setName("panelExclude"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/program/plugins/Bundle"); // NOI18N
        labelInfoExclude.setText(bundle.getString("PluginsSettingsPanel.labelInfoExclude.text")); // NOI18N
        labelInfoExclude.setName("labelInfoExclude"); // NOI18N

        scrollPaneExclude.setAlignmentX(0.0F);
        scrollPaneExclude.setAlignmentY(0.0F);
        scrollPaneExclude.setName("scrollPaneExclude"); // NOI18N

        panelExcludeCheckboxes.setName("panelExcludeCheckboxes"); // NOI18N
        panelExcludeCheckboxes.setLayout(new java.awt.GridBagLayout());
        scrollPaneExclude.setViewportView(panelExcludeCheckboxes);

        javax.swing.GroupLayout panelExcludeLayout = new javax.swing.GroupLayout(panelExclude);
        panelExclude.setLayout(panelExcludeLayout);
        panelExcludeLayout.setHorizontalGroup(
            panelExcludeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelExcludeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelExcludeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPaneExclude, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
                    .addComponent(labelInfoExclude))
                .addContainerGap())
        );
        panelExcludeLayout.setVerticalGroup(
            panelExcludeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelExcludeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelInfoExclude)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneExclude, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab(bundle.getString("PluginsSettingsPanel.panelExclude.TabConstraints.tabTitle"), panelExclude); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
        );
    }//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JLabel labelInfoExclude;
    private javax.swing.JPanel panelExclude;
    private javax.swing.JPanel panelExcludeCheckboxes;
    private javax.swing.JScrollPane scrollPaneExclude;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
}
