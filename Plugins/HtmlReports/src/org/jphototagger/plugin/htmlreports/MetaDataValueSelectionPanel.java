package org.jphototagger.plugin.htmlreports;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.openide.util.Lookup;

import org.jphototagger.domain.metadata.MetaDataValue;
import org.jphototagger.domain.metadata.MetaDataValueProvider;
import org.jphototagger.lib.api.PositionProviderAscendingComparator;
import org.jphototagger.lib.util.Bundle;

/**
 * @author Elmar Baumann
 */
public class MetaDataValueSelectionPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    private final ComboBoxModel comboBoxModel = new MetaDataValuesComboBoxModel();
    private final ListCellRenderer listCellRenderer = new MetaDataValuesListCellRenderer();

    public MetaDataValueSelectionPanel() {
        initComponents();
    }

    private void addMetaDataValueSelectionPanel() {
        firePropertyChange("add", null, null);
    }

    private void removeMetaDataValueSelectionPanel() {
        firePropertyChange("remove", null, null);
    }

    private void valueChanged() {
        Object selectedItem = comboBoxModel.getSelectedItem();
        firePropertyChange("metaDataValue", null, selectedItem);
        if (MetaDataValuesComboBoxModel.NONE.equals(selectedItem)) {
            firePropertyChange("remove", null, null);
        }
    }

    MetaDataValue getSelectedMetaDataValue() {
        Object selectedItem = comboBoxModel.getSelectedItem();
        return selectedItem instanceof MetaDataValue
                ? (MetaDataValue) selectedItem
                : null;
    }

    void enableRemove() {
        buttonRemove.setEnabled(true);
    }

    void select(MetaDataValue metaDataValue) {
        metaDataValuesComboBox.setSelectedItem(metaDataValue);
    }

    void selectNone() {
        metaDataValuesComboBox.setSelectedItem(MetaDataValuesComboBoxModel.NONE);
    }

    private static class MetaDataValuesListCellRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof MetaDataValue) {
                MetaDataValue metaDataValue = (MetaDataValue) value;
                label.setIcon(metaDataValue.getCategoryIcon());
                label.setText(metaDataValue.getDescription());
            }

            return label;
        }
    }

    private static class MetaDataValuesComboBoxModel extends DefaultComboBoxModel {

        private static final long serialVersionUID = 1L;
        private static final Collection<? extends MetaDataValueProvider> META_DATA_VALUE_PROVIDERS;
        private static final String NONE = Bundle.getString(MetaDataValuesComboBoxModel.class, "MetaDataValuesComboBoxModel.NoneElement");

        static {
            List<MetaDataValueProvider> sortedMetaDataValueProviders =
                    new ArrayList<MetaDataValueProvider>(Lookup.getDefault().lookupAll(MetaDataValueProvider.class));
            Collections.sort(sortedMetaDataValueProviders, PositionProviderAscendingComparator.INSTANCE);
            META_DATA_VALUE_PROVIDERS = sortedMetaDataValueProviders;
        }

        private MetaDataValuesComboBoxModel() {
            addElements();
        }

        private void addElements() {
            addElement(NONE);
            for (MetaDataValueProvider metaDataValueProvider : META_DATA_VALUE_PROVIDERS) {
                Collection<MetaDataValue> providedMetaDataValues = metaDataValueProvider.getProvidedValues();
                for (MetaDataValue providedMetaDataValue : providedMetaDataValues) {
                    addElement(providedMetaDataValue);
                }
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        metaDataValuesComboBox = new javax.swing.JComboBox();
        buttonAdd = new javax.swing.JButton();
        buttonRemove = new javax.swing.JButton();

        setName("Form"); // NOI18N
        setLayout(new java.awt.GridBagLayout());

        metaDataValuesComboBox.setModel(comboBoxModel);
        metaDataValuesComboBox.setName("metaDataValuesComboBox"); // NOI18N
        metaDataValuesComboBox.setRenderer(listCellRenderer);
        metaDataValuesComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                metaDataValuesComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        add(metaDataValuesComboBox, gridBagConstraints);

        buttonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jphototagger/plugin/htmlreports/add.png"))); // NOI18N
        buttonAdd.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonAdd.setName("buttonAdd"); // NOI18N
        buttonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(buttonAdd, gridBagConstraints);

        buttonRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jphototagger/plugin/htmlreports/remove.png"))); // NOI18N
        buttonRemove.setEnabled(false);
        buttonRemove.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonRemove.setName("buttonRemove"); // NOI18N
        buttonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(buttonRemove, gridBagConstraints);
    }//GEN-END:initComponents

    private void buttonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddActionPerformed
        addMetaDataValueSelectionPanel();
    }//GEN-LAST:event_buttonAddActionPerformed

    private void buttonRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRemoveActionPerformed
        removeMetaDataValueSelectionPanel();
    }//GEN-LAST:event_buttonRemoveActionPerformed

    private void metaDataValuesComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_metaDataValuesComboBoxActionPerformed
        valueChanged();
    }//GEN-LAST:event_metaDataValuesComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonRemove;
    private javax.swing.JComboBox metaDataValuesComboBox;
    // End of variables declaration//GEN-END:variables
}