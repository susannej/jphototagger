package de.elmar_baumann.lib.componentutil;

import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Werkzeuge für Trees.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>, Tobias Stening <info@swts.net>
 * @version 2008-10-05
 */
public class TreeUtil {

    /**
     * Liefert, ob der Mauszeiger über einem selektierten Item steht.
     * 
     * @param  e  Mausereigenis
     * @return true, falls der Zeiger über einem selektierten Item steht
     */
    public static boolean isSelectedItemPosition(MouseEvent e) {
        if (e.getSource() instanceof JTree) {
            JTree tree = (JTree) e.getSource();
            TreePath path = tree.getPathForLocation(e.getX(), e.getY());
            if (path != null && tree.getSelectionPath() != null) {
                Object selected = tree.getSelectionPath().getLastPathComponent();
                Object clicked = path.getLastPathComponent();
                if (selected != null && clicked != null) {
                    return selected.equals(clicked);
                }
            }
        }
        return false;
    }

    /**
     * Liefert, ob der Mauszeiger über dem Wurzelitem steht.
     * 
     * @param  e  Mausereigenis
     * @return true, falls der Zeiger über dem Wurzelitem steht
     */
    public static boolean isRootItemPosition(MouseEvent e) {
        if (e.getSource() instanceof JTree) {
            JTree tree = (JTree) e.getSource();
            TreePath path = tree.getPathForLocation(e.getX(), e.getY());
            if (path != null) {
                Object root = tree.getModel().getRoot();
                Object clicked = path.getLastPathComponent();
                if (root != null && clicked != null) {
                    return root.equals(clicked);
                }
            }
        }
        return false;
    }

    /**
     * Deselektiert alle Treeitems von mehreren Trees.
     * 
     * @param trees Trees
     */
    public static void clearSelection(List<JTree> trees) {
        for (JTree tree : trees) {
            if (tree.getSelectionCount() > 0) {
                tree.clearSelection();
            }
        }
    }

    /**
     * Liefert aus einem Model den Pfad anhand eines Strings, der diesen spezifiziert.
     * 
     * @param treeModel     Model
     * @param pathString    String mit Pfad
     * @param pathSeparator Separator zwischen den einzelnen Pfadbestandteilen
     * @return              Pfad oder null, wenn nicht gefunden
     */
    public static TreePath getTreePath(TreeModel treeModel, String pathString,
        String pathSeparator) {
        StringTokenizer tokenizer = new StringTokenizer(pathString,
            pathSeparator);
        int tokenCount = tokenizer.countTokens();
        int tokenNumber = 1;
        int tokenFoundCount = 0;
        Object[] path = new Object[tokenCount > 0 ? tokenCount : 1];
        if (tokenCount > 0) {
            path[0] = treeModel.getRoot();
            tokenizer.nextToken();
            Object currentElement = treeModel.getRoot();
            Object childElement = null;
            boolean appended = true;

            while (appended && tokenNumber < tokenCount) {
                int childCount = treeModel.getChildCount(currentElement);
                String pathToken = tokenizer.nextToken();
                boolean found = false;
                appended = false;
                for (int index = 0; index < childCount && !found; index++) {
                    childElement = treeModel.getChild(currentElement, index);
                    found = childElement.toString().equals(pathToken);
                    if (found) {
                        path[tokenNumber] = childElement;
                        currentElement = childElement;
                        appended = true;
                        tokenFoundCount++;
                    }
                }
                tokenNumber++;
            }
        }
        return tokenCount > 0 && tokenCount - 1 == tokenFoundCount
            ? new TreePath(path)
            : null;
    }

    /**
     * Öffnet den Tree auch, wenn das letzte Element eines Pfads ein Blatt ist.
     * 
     * @param tree Tree
     * @param path Pfad
     */
    public static void expandPath(JTree tree, TreePath path) {
        TreePath expandPath = path;
        if (tree.getModel().isLeaf(path.getLastPathComponent())) {
            expandPath = path.getParentPath();
        }
        tree.expandPath(expandPath);
    }

    /**
     * Expands a path step by step. First the path with not parents will be
     * expanded, then the child path of this, then the child path of the
     * child path until the last path component is reached.
     * 
     * @param tree tree
     * @param path path to expand in <code>tree</code>
     */
    public static void expandPathCascade(JTree tree, TreePath path) {
        Stack<TreePath> stack = new Stack<TreePath>();
        TreePath parent = path;
        while (parent != null) {
            stack.push(parent);
            parent = parent.getParentPath();
        }
        while (!stack.isEmpty()) {
            tree.expandPath(stack.pop());
        }
    }

    /**
     * Returns the tree path of a file, each path component is a parent
     * (child) file of a file.
     * 
     * @param  file   file
     * @param  model  model when the root is not a file, else null
     * @return path
     */
    public static TreePath getTreePath(File file, TreeModel model) {
        Stack<Object> stack = new Stack<Object>();
        while (file != null) {
            stack.push(file);
            file = file.getParentFile();
        }
        List<Object> list = new ArrayList<Object>(stack.size() + 1);
        if (model != null) {
            list.add(model.getRoot());
        }
        while (!stack.isEmpty()) {
            list.add(stack.pop());
        }
        return new TreePath(list.toArray());
    }
}
