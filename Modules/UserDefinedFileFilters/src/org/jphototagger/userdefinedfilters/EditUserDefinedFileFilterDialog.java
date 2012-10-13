package org.jphototagger.userdefinedfilters;

import java.awt.Container;
import javax.swing.DefaultComboBoxModel;
import org.jphototagger.domain.filefilter.UserDefinedFileFilter;
import org.jphototagger.domain.repository.UserDefinedFileFiltersRepository;
import org.jphototagger.lib.beansbinding.MaxLengthValidator;
import org.jphototagger.lib.swing.Dialog;
import org.jphototagger.lib.swing.MessageDisplayer;
import org.jphototagger.lib.swing.util.ComponentUtil;
import org.jphototagger.lib.swing.util.MnemonicUtil;
import org.jphototagger.lib.util.Bundle;
import org.openide.util.Lookup;

/**
 * @author Elmar Baumann
 */
public class EditUserDefinedFileFilterDialog extends Dialog {

    private static final long serialVersionUID = 1L;
    private boolean accepted;
    private final UserDefinedFileFilter udf = new UserDefinedFileFilter();
    private final UserDefinedFileFiltersRepository repo = Lookup.getDefault().lookup(UserDefinedFileFiltersRepository.class);

    public EditUserDefinedFileFilterDialog() {
        super(ComponentUtil.findFrameWithIcon(), true);
        initComponents();
        postInitComponents();
    }

    public EditUserDefinedFileFilterDialog(UserDefinedFileFilter filter) {
        super(ComponentUtil.findFrameWithIcon(), true);
        if (filter == null) {
            throw new NullPointerException("filter == null");
        }

        udf.set(filter);
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        setHelpPage();
        MnemonicUtil.setMnemonics((Container) this);
    }

    private void setHelpPage() {
        setHelpPageUrl(Bundle.getString(EditUserDefinedFileFilterDialog.class, "EditUserDefinedFileFilterDialog.HelpPage"));
    }

    public UserDefinedFileFilter getFilter() {
        return new UserDefinedFileFilter(filter);
    }

    public boolean isAccepted() {
        return accepted;
    }

    private void handleOkButtonClicked() {
        if (filter.isValid()) {
            if (checkName(textFieldName.getText())) {
                accepted = true;
                setVisible(false);
            }
        } else {
            String message = Bundle.getString(EditUserDefinedFileFilterDialog.class, "EditUserDefinedFileFilterDialog.Error.Valid");
            MessageDisplayer.error(this, message);
        }
    }

    private boolean checkName(String name) {
        if (repo.existsUserDefinedFileFilter(name)) {
            String message = Bundle.getString(EditUserDefinedFileFilterDialog.class, "EditUserDefinedFileFilterDialog.Error.NameExists", name);
            MessageDisplayer.error(this, message);
            return false;
        }

        return true;
    }

    private static class ComboBoxModel extends DefaultComboBoxModel<Object> {

        private static final long serialVersionUID = 1L;

        private ComboBoxModel() {
            addElements();
        }

        private void addElements() {
            for (UserDefinedFileFilter.Type type : UserDefinedFileFilter.Type.values()) {
                addElement(type);
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void initComponents() {//GEN-BEGIN:initComponents
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        filter = udf;
        labelName = new javax.swing.JLabel();
        textFieldName = new javax.swing.JTextField();
        comboBoxType = new javax.swing.JComboBox<>();
        textFieldExpression = new javax.swing.JTextField();
        checkBoxNot = new javax.swing.JCheckBox();
        buttonCancel = new javax.swing.JButton();
        buttonOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/userdefinedfilters/Bundle"); // NOI18N
        setTitle(bundle.getString("EditUserDefinedFileFilterDialog.title")); // NOI18N
        setName("Form"); // NOI18N

        labelName.setLabelFor(textFieldName);
        labelName.setText(bundle.getString("EditUserDefinedFileFilterDialog.labelName.text")); // NOI18N
        labelName.setName("labelName"); // NOI18N

        textFieldName.setColumns(45);
        textFieldName.setName("textFieldName"); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, filter, org.jdesktop.beansbinding.ELProperty.create("${name}"), textFieldName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setValidator(new MaxLengthValidator(45));
        bindingGroup.addBinding(binding);

        comboBoxType.setModel(new ComboBoxModel());
        comboBoxType.setName("comboBoxType"); // NOI18N
        comboBoxType.setRenderer(new UserDefinedFileFiltersListCellRenderer());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, filter, org.jdesktop.beansbinding.ELProperty.create("${type}"), comboBoxType, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        textFieldExpression.setName("textFieldExpression"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, filter, org.jdesktop.beansbinding.ELProperty.create("${expression}"), textFieldExpression, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setValidator(new MaxLengthValidator(128));
        bindingGroup.addBinding(binding);

        checkBoxNot.setText(bundle.getString("EditUserDefinedFileFilterDialog.checkBoxNot.text")); // NOI18N
        checkBoxNot.setName("checkBoxNot"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, filter, org.jdesktop.beansbinding.ELProperty.create("${isNot}"), checkBoxNot, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        binding.setSourceNullValue(false);
        binding.setSourceUnreadableValue(false);
        bindingGroup.addBinding(binding);

        buttonCancel.setText(bundle.getString("EditUserDefinedFileFilterDialog.buttonCancel.text")); // NOI18N
        buttonCancel.setName("buttonCancel"); // NOI18N
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        buttonOk.setText(bundle.getString("EditUserDefinedFileFilterDialog.buttonOk.text")); // NOI18N
        buttonOk.setName("buttonOk"); // NOI18N
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(comboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textFieldExpression, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(checkBoxNot)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 293, Short.MAX_VALUE)
                        .addComponent(buttonCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonOk))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelName)
                    .addComponent(textFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldExpression, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBoxNot))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonOk)
                            .addComponent(buttonCancel))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bindingGroup.bind();

        pack();
    }//GEN-END:initComponents

    private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
        handleOkButtonClicked();
    }//GEN-LAST:event_buttonOkActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        accepted = false;
        setVisible(false);
    }//GEN-LAST:event_buttonCancelActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                EditUserDefinedFileFilterDialog dialog = new EditUserDefinedFileFilterDialog();
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
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonOk;
    private javax.swing.JCheckBox checkBoxNot;
    private javax.swing.JComboBox<Object> comboBoxType;
    private org.jphototagger.domain.filefilter.UserDefinedFileFilter filter;
    private javax.swing.JLabel labelName;
    private javax.swing.JTextField textFieldExpression;
    private javax.swing.JTextField textFieldName;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
