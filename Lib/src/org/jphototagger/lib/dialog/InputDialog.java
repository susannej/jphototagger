/*
 * @(#)InputDialog.java    Created on 
 *
 * Copyright (C) 2009-2010 by the JPhotoTagger developer team.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package org.jphototagger.lib.dialog;

import org.jphototagger.lib.componentutil.ComponentUtil;
import org.jphototagger.lib.util.Settings;

import java.awt.event.KeyEvent;

import java.util.Properties;

import javax.swing.JDialog;

/**
 * Modal text input dialog writing it's location to a properties object on demand.
 *
 * @author  Elmar Baumann
 */
public class InputDialog extends Dialog {
    private static final long  serialVersionUID = -4217215186067129031L;
    private transient Settings settings;
    private String             propertyKey;
    private boolean            accepted;

    public InputDialog() {
        super(ComponentUtil.getFrameWithIcon(), true);
        initComponents();
    }

    public InputDialog(JDialog owner) {
        super(owner, true);
        initComponents();
    }

    public InputDialog(String info, String input) {
        super(ComponentUtil.getFrameWithIcon(), true);
        initComponents();
        labelPrompt.setText(info);
        textFieldInput.setText(input);
    }

    public InputDialog(JDialog owner, String info, String input) {
        super(owner, true);
        initComponents();
        labelPrompt.setText(info);
        textFieldInput.setText(input);
    }

    public InputDialog(String info, String input, Properties properties,
                       String propertyKey) {
        super(ComponentUtil.getFrameWithIcon(), true);
        initComponents();
        labelPrompt.setText(info);
        textFieldInput.setText(input);
        setProperties(properties, propertyKey);
    }

    public InputDialog(JDialog owner, String info, String input,
                       Properties properties, String propertyKey) {
        super(owner, true);
        initComponents();
        labelPrompt.setText(info);
        textFieldInput.setText(input);
        setProperties(properties, propertyKey);
    }

    /**
     * Sets the info text ("prompt").
     *
     * @param info info text
     */
    public void setInfo(String info) {
        if (info == null) {
            throw new NullPointerException("info == null");
        }

        labelPrompt.setText(info);
    }

    /**
     * Sets the input to a specific string.
     *
     * @param input input
     */
    public void setInput(String input) {
        if (input == null) {
            throw new NullPointerException("input == null");
        }

        textFieldInput.setText(input);
    }

    /**
     * Returns whether the dialog was closed with <strong>OK</strong> and the
     * user input shall be used.
     *
     * @return true if closed with OK
     */
    public boolean isAccepted() {
        return accepted;
    }

    /**
     * Returns the user input.
     *
     * @return input
     */
    public String getInput() {
        return textFieldInput.getText();
    }

    /**
     * Sets the properties to put size and location.
     *
     * @param properties  properties
     * @param propertyKey property key
     */
    public void setProperties(Properties properties, String propertyKey) {
        if (properties == null) {
            throw new NullPointerException("properties == null");
        }

        if (propertyKey == null) {
            throw new NullPointerException("propertyKey == null");
        }

        settings         = new Settings(properties);
        this.propertyKey = propertyKey;
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            readProperties();
        } else {
            writeProperties();
        }

        super.setVisible(visible);
    }

    private void readProperties() {
        if ((settings != null) && (propertyKey != null)) {
            settings.applySize(this, propertyKey);
            settings.applyLocation(this, propertyKey);
        }

        if (getLocation().x <= 0) {
            ComponentUtil.centerScreen(this);
        }

        textFieldInput.selectAll();
        textFieldInput.requestFocusInWindow();
    }

    private void writeProperties() {
        if ((settings != null) && (propertyKey != null)) {
            settings.setSize(this, propertyKey);
            settings.setLocation(this, propertyKey);
        }
    }

    @Override
    protected void escape() {
        accepted = false;
        setVisible(false);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        labelPrompt    = new javax.swing.JLabel();
        textFieldInput = new javax.swing.JTextField();
        buttonCancel   = new javax.swing.JButton();
        buttonOk       = new javax.swing.JButton();
        setDefaultCloseOperation(
            javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        java.util.ResourceBundle bundle =
            java.util.ResourceBundle.getBundle(
                "org/jphototagger/lib/resource/properties/Bundle");    // NOI18N

        setTitle(bundle.getString("InputDialog.title"));    // NOI18N
        labelPrompt.setText(bundle.getString("InputDialog.labelPrompt.text"));    // NOI18N
        textFieldInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFieldInputKeyPressed(evt);
            }
        });
        buttonCancel.setMnemonic('a');
        buttonCancel.setText(bundle.getString("InputDialog.buttonCancel.text"));    // NOI18N
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        buttonOk.setMnemonic('o');
        buttonOk.setText(bundle.getString("InputDialog.buttonOk.text"));    // NOI18N
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout =
            new javax.swing.GroupLayout(getContentPane());

        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup().addContainerGap()
                .addGroup(layout
                    .createParallelGroup(javax.swing.GroupLayout.Alignment
                        .LEADING)
                            .addComponent(textFieldInput, javax.swing
                                .GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                                    .addComponent(labelPrompt)
                                    .addGroup(javax.swing.GroupLayout.Alignment
                                        .TRAILING, layout
                                        .createSequentialGroup()
                                        .addComponent(buttonCancel)
                                        .addPreferredGap(javax.swing.LayoutStyle
                                            .ComponentPlacement.RELATED)
                                                .addComponent(buttonOk)))
                                                    .addContainerGap()));
        layout.setVerticalGroup(
            layout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addContainerGap().addComponent(
                    labelPrompt).addPreferredGap(
                    javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                    textFieldInput, javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
                        javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                        layout.createParallelGroup(
                            javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
                            buttonOk).addComponent(
                            buttonCancel)).addContainerGap(
                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)));
        pack();
    }    // </editor-fold>//GEN-END:initComponents

    private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
        accepted = true;
        setVisible(false);
    }//GEN-LAST:event_buttonOkActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        accepted = false;
        setVisible(false);
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void textFieldInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldInputKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            accepted = true;
            setVisible(false);
        }
    }//GEN-LAST:event_textFieldInputKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                InputDialog dialog = new InputDialog();

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
    private javax.swing.JButton    buttonCancel;
    private javax.swing.JButton    buttonOk;
    private javax.swing.JLabel     labelPrompt;
    private javax.swing.JTextField textFieldInput;

    // End of variables declaration//GEN-END:variables
}
