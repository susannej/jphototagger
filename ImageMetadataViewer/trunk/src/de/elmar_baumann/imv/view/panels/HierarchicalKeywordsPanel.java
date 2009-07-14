package de.elmar_baumann.imv.view.panels;

import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

/**
 * A tree for hierarchical keywords.
 * 
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2009/07/10
 */
public class HierarchicalKeywordsPanel extends javax.swing.JPanel {

    /** Creates new form HierarchicalKeywordsPanel */
    public HierarchicalKeywordsPanel() {
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        tree.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    public JTree getTree() {
        return tree;
    }

    public JMenuItem getMenuItemAdd() {
        return menuItemAdd;
    }

    public JMenuItem getMenuItemRemove() {
        return menuItemRemove;
    }

    public JMenuItem getMenuItemRename() {
        return menuItemRename;
    }

    public JMenuItem getMenuItemToggleReal() {
        return menuItemToggleReal;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupMenu = new javax.swing.JPopupMenu();
        menuItemAdd = new javax.swing.JMenuItem();
        menuItemRename = new javax.swing.JMenuItem();
        menuItemRemove = new javax.swing.JMenuItem();
        menuItemToggleReal = new javax.swing.JMenuItem();
        scrollPane = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();

        menuItemAdd.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        menuItemAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_add.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("de/elmar_baumann/imv/resource/properties/Bundle"); // NOI18N
        menuItemAdd.setText(bundle.getString("HierarchicalKeywordsPanel.menuItemAdd.text")); // NOI18N
        popupMenu.add(menuItemAdd);

        menuItemRename.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        menuItemRename.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_rename.png"))); // NOI18N
        menuItemRename.setText(bundle.getString("HierarchicalKeywordsPanel.menuItemRename.text")); // NOI18N
        popupMenu.add(menuItemRename);

        menuItemRemove.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        menuItemRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/elmar_baumann/imv/resource/icons/icon_remove.png"))); // NOI18N
        menuItemRemove.setText(bundle.getString("HierarchicalKeywordsPanel.menuItemRemove.text")); // NOI18N
        popupMenu.add(menuItemRemove);

        menuItemToggleReal.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        menuItemToggleReal.setText(bundle.getString("HierarchicalKeywordsPanel.menuItemToggleReal.text")); // NOI18N
        popupMenu.add(menuItemToggleReal);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        tree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        tree.setCellRenderer(new de.elmar_baumann.imv.view.renderer.TreeCellRendererHierarchicalKeywords());
        tree.setComponentPopupMenu(popupMenu);
        tree.setDragEnabled(true);
        scrollPane.setViewportView(tree);
        tree.setTransferHandler(new de.elmar_baumann.imv.datatransfer.TransferHandlerTreeHierarchicalKeywords());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem menuItemAdd;
    private javax.swing.JMenuItem menuItemRemove;
    private javax.swing.JMenuItem menuItemRename;
    private javax.swing.JMenuItem menuItemToggleReal;
    private javax.swing.JPopupMenu popupMenu;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTree tree;
    // End of variables declaration//GEN-END:variables
}
