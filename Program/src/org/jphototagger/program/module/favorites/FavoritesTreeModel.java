package org.jphototagger.program.module.favorites;

import java.awt.Cursor;
import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.jphototagger.api.applifecycle.AppWillExitEvent;
import org.jphototagger.api.preferences.Preferences;
import org.jphototagger.domain.favorites.Favorite;
import org.jphototagger.domain.repository.FavoritesRepository;
import org.jphototagger.domain.repository.event.favorites.FavoriteDeletedEvent;
import org.jphototagger.domain.repository.event.favorites.FavoriteInsertedEvent;
import org.jphototagger.domain.repository.event.favorites.FavoriteUpdatedEvent;
import org.jphototagger.lib.awt.EventQueueUtil;
import org.jphototagger.lib.io.FileUtil;
import org.jphototagger.lib.io.TreeFileSystemDirectories;
import org.jphototagger.lib.io.filefilter.DirectoryFilter;
import org.jphototagger.lib.swing.MessageDisplayer;
import org.jphototagger.lib.swing.SortedChildrenTreeNode;
import org.jphototagger.lib.swing.util.ComponentUtil;
import org.jphototagger.lib.swing.util.TreeUtil;
import org.jphototagger.lib.util.Bundle;
import org.openide.util.Lookup;

/*
 * See org.jphototagger.lib.swing.AllSystemDirectoriesTreeModel for speed improvements
 */

/**
 * Elements are {@code DefaultMutableTreeNode}s with File or Favorite instances as user objects.
 *
 * @author Elmar Baumann
 */
public final class FavoritesTreeModel extends DefaultTreeModel implements TreeWillExpandListener {

    private static final long serialVersionUID = 1L;
    private static final String KEY_SELECTED_DIR = "FavoritesTreeModel.SelDir";
    private static final String KEY_SELECTED_FAV_NAME = "FavoritesTreeModel.SelFavDir";
    private static final long UPDATE_INTERVAL_MILLISECONDS = 2000;
    private static final Logger LOGGER = Logger.getLogger(FavoritesTreeModel.class.getName());
    private static final Object DUMMY = new Object();
    private final DirectoryFilter directoryFilter = new DirectoryFilter(getDirFilterOptionShowHiddenFiles());
    private final Object monitor = new Object();
    private volatile boolean listenToRepo = true;
    private final DefaultMutableTreeNode rootNode;
    private final JTree tree;
    private final FavoritesRepository repo = Lookup.getDefault().lookup(FavoritesRepository.class);
    private final ScheduledExecutorService updateScheduler;
    private volatile boolean autoupdate;

    public FavoritesTreeModel(JTree tree) {
        super(new DefaultMutableTreeNode(Bundle.getString(FavoritesTreeModel.class, "FavoritesTreeModel.Root.DisplayName")));
        if (tree == null) {
            throw new NullPointerException("tree == null");
        }
        this.tree = tree;
        rootNode = (DefaultMutableTreeNode) getRoot();
        addFavorites();
        listen();
        updateScheduler = Executors.newSingleThreadScheduledExecutor(threadFactory);
    }

    private void listen() {
        tree.addTreeWillExpandListener(this);
        AnnotationProcessor.process(this);
    }

    public void insert(Favorite favorite) {
        if (favorite == null) {
            throw new NullPointerException("favorite == null");
        }
        synchronized (monitor) {
            listenToRepo = false;
            favorite.setIndex(getNextNewFavoriteIndex());
            if (!existsFavorite(favorite)) {
                if (repo.saveOrUpdateFavorite(favorite)) {
                    addFavorite(favorite);
                } else {
                    errorMessage(favorite.getName(), Bundle.getString(FavoritesTreeModel.class, "FavoritesTreeModel.Error.ParamInsert"));
                }
            }
            listenToRepo = true;
        }
    }

    public void delete(Favorite favorite) {
        if (favorite == null) {
            throw new NullPointerException("favorite == null");
        }
        synchronized (monitor) {
            listenToRepo = false;
            DefaultMutableTreeNode favNode = findNode(favorite);
            if ((favNode != null) && repo.deleteFavorite(favorite.getName())) {
                removeNodeFromParent(favNode);
                resetFavoriteIndices();
            } else {
                errorMessage(favorite.getName(), Bundle.getString(FavoritesTreeModel.class, "FavoritesTreeModel.Error.ParamDelete"));
            }
            listenToRepo = true;
        }
    }

    private void deleteFavorite(Favorite favorite) {
        DefaultMutableTreeNode favNode = findNode(favorite);
        if (favNode != null) {
            removeNodeFromParent(favNode);
            resetFavoriteIndices();
        }
    }

    @SuppressWarnings("unchecked")
    private void resetFavoriteIndices() {
        int newIndex = 0;
        for (Enumeration<DefaultMutableTreeNode> children = rootNode.children(); children.hasMoreElements();) {
            Object userObject = children.nextElement().getUserObject();
            if (userObject instanceof Favorite) {
                Favorite fav = (Favorite) userObject;
                fav.setIndex(newIndex);
                newIndex++;
                repo.updateFavorite(fav);
            }
        }
    }

    private void updateNodes(DefaultMutableTreeNode nodeOfFavorite, Favorite newFavorite) {
        removeAllChildren(nodeOfFavorite);
        Object userObject = nodeOfFavorite.getUserObject();
        if (userObject instanceof Favorite) {
            Favorite favorite = (Favorite) userObject;
            favorite.setDirectory(newFavorite.getDirectory());
            favorite.setName(newFavorite.getName());
        }
        addChildren(nodeOfFavorite);
        nodeChanged(nodeOfFavorite);
    }

    @SuppressWarnings("unchecked")
    private void removeAllChildren(DefaultMutableTreeNode node) {
        List<DefaultMutableTreeNode> children = new ArrayList<>(node.getChildCount());
        for (Enumeration<DefaultMutableTreeNode> e = node.children(); e.hasMoreElements();) {
            children.add(e.nextElement());
        }
        for (DefaultMutableTreeNode child : children) {
            removeNodeFromParent(child);    // notifies listeners
        }
    }

    public void moveUpFavorite(Favorite favorite) {
        if (favorite == null) {
            throw new NullPointerException("favorite == null");
        }
        synchronized (monitor) {
            DefaultMutableTreeNode nodeToMoveUp = findNode(favorite);
            if (nodeToMoveUp != null) {
                int indexNodeToMoveUp = rootNode.getIndex(nodeToMoveUp);
                boolean isFirstNode = indexNodeToMoveUp == 0;
                if (!isFirstNode) {
                    DefaultMutableTreeNode prevNode = (DefaultMutableTreeNode) rootNode.getChildAt(indexNodeToMoveUp
                            - 1);
                    if ((prevNode != null)
                            && updateFavorite(nodeToMoveUp.getUserObject(), indexNodeToMoveUp - 1)
                            && updateFavorite(prevNode.getUserObject(), indexNodeToMoveUp)) {
                        removeNodeFromParent(prevNode);
                        insertNodeInto(prevNode, rootNode, indexNodeToMoveUp);
                    }
                }
            }
        }
    }

    public void moveDownFavorite(Favorite favorite) {
        if (favorite == null) {
            throw new NullPointerException("favorite == null");
        }
        synchronized (monitor) {
            DefaultMutableTreeNode nodeToMoveDown = findNode(favorite);
            if (nodeToMoveDown != null) {
                int indexNodeToMoveDown = rootNode.getIndex(nodeToMoveDown);
                boolean isLastNode = indexNodeToMoveDown == rootNode.getChildCount() - 1;
                if (!isLastNode) {
                    DefaultMutableTreeNode nextNode = (DefaultMutableTreeNode) rootNode.getChildAt(indexNodeToMoveDown + 1);
                    if ((nextNode != null)
                            && updateFavorite(nodeToMoveDown.getUserObject(), indexNodeToMoveDown + 1)
                            && updateFavorite(nextNode.getUserObject(), indexNodeToMoveDown)) {
                        removeNodeFromParent(nextNode);
                        insertNodeInto(nextNode, rootNode, indexNodeToMoveDown);
                    }
                }
            }
        }
    }

    private boolean updateFavorite(Object userObject, int newIndex) {
        if (userObject instanceof Favorite) {
            Favorite favorite = (Favorite) userObject;
            favorite.setIndex(newIndex);
            return repo.updateFavorite(favorite);
        }
        return false;
    }

    private void addFavorites() {
        List<Favorite> directories = repo.findAllFavorites();
        for (Favorite directory : directories) {
            if (directory.getDirectory().isDirectory()) {
                addFavorite(directory);
            } else {
                errorMessageAddDirectory(directory);
            }
        }
    }

    private void addFavorite(Favorite favorite) {
        DefaultMutableTreeNode dirNode = findNode(favorite);
        if (dirNode == null) {
            DefaultMutableTreeNode node = new SortedChildrenTreeNode(favorite);
            insertNodeInto(node, rootNode, rootNode.getChildCount());
            addDummy(node);
            tree.expandPath(new TreePath(rootNode.getPath()));
        }
    }

    /**
     * Adds to a parent node not existing children where the user object is a directory if the user object of the node
     * is a directory or a favorite directory (wich refers to a directory). The children are child directories of the
     * directory (user object).
     *
     * @param parentNode parent note which gets the new children
     */
    private void addChildren(DefaultMutableTreeNode parentNode) {
        Object userObject = parentNode.getUserObject();
        File dir = (userObject instanceof File)
                ? (File) userObject
                : (userObject instanceof Favorite)
                ? ((Favorite) userObject).getDirectory()
                : null;
        if (!FileUtil.isReadableDirectory(dir)) {
            return;
        }
        LOGGER.log(Level.FINEST, "Reading subdirectories of ''{0}''...", dir);
        File[] subdirs = dir.listFiles(directoryFilter);
        LOGGER.log(Level.FINEST, "Subdirectories of ''{0}'' have been read", dir);
        removeDummy(parentNode, false);
        if (subdirs == null) {
            return;
        }
        List<File> nodeChildrenDirs = getChildDirectories(parentNode);
        for (File subdir : subdirs) {
            if (!nodeChildrenDirs.contains(subdir) && FileUtil.isReadableDirectory(subdir)) {
                DefaultMutableTreeNode childNode = addChildDirectory(parentNode, subdir);
                addDummy(childNode);
            }
        }
    }

    private static List<File> getChildDirectories(DefaultMutableTreeNode node) {
        int childCount = node.getChildCount();
        List<File> childFiles = new ArrayList<>(childCount);
        for (int i = 0; i < childCount; i++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
            Object childNodeUserObject = childNode.getUserObject();
            if (childNodeUserObject instanceof File) {
                childFiles.add((File) childNodeUserObject);
            } else if (childNodeUserObject instanceof Favorite) {
                Favorite favorite = (Favorite) childNodeUserObject;
                childFiles.add(favorite.getDirectory());
            }
        }
        return childFiles;
    }

    private DefaultMutableTreeNode addChildDirectory(DefaultMutableTreeNode parentNode, File directory) {
        LOGGER.log(Level.FINEST, "Adding subdirectory ''{0}'' to node ''{1}''", new Object[]{directory, parentNode});
        DefaultMutableTreeNode newChildNode = new SortedChildrenTreeNode(directory);
        parentNode.add(newChildNode);
        int newChildIndex = parentNode.getIndex(newChildNode);
        fireTreeNodesInserted(this, parentNode.getPath(), new int[]{newChildIndex}, new Object[]{newChildNode});
        LOGGER.log(Level.FINEST, "Subdirectory ''{0}'' has been added to node ''{1}''", new Object[]{directory, parentNode});
        return newChildNode;
    }

    private void addDummy(DefaultMutableTreeNode parentNode) {
        File dir = getDirectory(parentNode);
        if (parentNode.isLeaf() && FileUtil.containsReadableDirectory(dir)) {
             // Must be created dynamically, because parent will be set and must be different (no static tree node)
            SortedChildrenTreeNode dummy = new SortedChildrenTreeNode(DUMMY);
            parentNode.add(dummy);
            fireTreeNodesInserted(this, parentNode.getPath(), new int[]{0}, new Object[]{dummy});
        }
    }

    private void removeDummy(DefaultMutableTreeNode parentNode, boolean onlyIfEmptyDir) {
        if (containsDummy(parentNode)) {
            TreeNode dummyChild = parentNode.getChildAt(0);
            boolean remove = true;
            if (onlyIfEmptyDir) {
                Object userObject = parentNode.getUserObject();
                if (userObject instanceof Favorite) {
                    Favorite favorite = (Favorite) userObject;
                    remove = !FileUtil.containsReadableDirectory(favorite.getDirectory());
                }
                if (userObject instanceof File) {
                    remove = !FileUtil.containsReadableDirectory((File) userObject);
                }
            }
            if (remove) {
                parentNode.remove(0);
                fireTreeNodesRemoved(this, parentNode.getPath(), new int[]{0}, new Object[]{dummyChild});
            }
        }
    }

    private boolean containsDummy(DefaultMutableTreeNode node) { // Quick & Dirty, but faster; relies on proper insertions/removes
        return node.getChildCount() == 1
                && ((DefaultMutableTreeNode)node.getChildAt(0)).getUserObject() == DUMMY;
    }

    private DirectoryFilter.Option getDirFilterOptionShowHiddenFiles() {
        return isAcceptHiddenDirectories()
                ? DirectoryFilter.Option.ACCEPT_HIDDEN_FILES
                : DirectoryFilter.Option.NO_OPTION;
    }

    private boolean isAcceptHiddenDirectories() {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);

        return prefs.containsKey(Preferences.KEY_ACCEPT_HIDDEN_DIRECTORIES)
                ? prefs.getBoolean(Preferences.KEY_ACCEPT_HIDDEN_DIRECTORIES)
                : false;
    }

    /**
     * Removes from a node child nodes with files as user objects if the file does not exist.
     *
     * @param parentNode parent node
     * @return count of removed nodes
     */
    private int removeChildrenWithNotExistingFiles(DefaultMutableTreeNode parentNode) {
        int childCount = parentNode.getChildCount();
        List<DefaultMutableTreeNode> nodesToRemove = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) parentNode.getChildAt(i);
            if (containsDummy(child)) {
                removeDummy(child, true);
            }
            Object userObject = child.getUserObject();
            File file = null;
            if (userObject instanceof File) {
                file = (File) userObject;
            } else if (userObject instanceof Favorite) {
                file = ((Favorite) userObject).getDirectory();
            }
            if ((file != null) && !file.exists()) {
                nodesToRemove.add(child);
            }
        }
        for (DefaultMutableTreeNode childNodeToRemove : nodesToRemove) {
            Object userObject = childNodeToRemove.getUserObject();
            if (userObject instanceof Favorite) {
                repo.deleteFavorite(((Favorite) userObject).getName());
            }
            removeNodeFromParent(childNodeToRemove);
        }
        return nodesToRemove.size();
    }

    // ROOT.getChildCount() is valid now, but if later there are other user
    // objects than Favorite in nodes below the root, this will not
    // work
    @SuppressWarnings("unchecked")
    private synchronized int getNextNewFavoriteIndex() {
        int index = 0;
        for (Enumeration<DefaultMutableTreeNode> children = rootNode.children(); children.hasMoreElements();) {
            Object userObject = children.nextElement().getUserObject();
            if (userObject instanceof Favorite) {
                index++;
            }
        }
        return index;
    }

    @SuppressWarnings("unchecked")
    private DefaultMutableTreeNode findNode(Favorite favorite) {
        for (Enumeration<DefaultMutableTreeNode> children = rootNode.children(); children.hasMoreElements();) {
            DefaultMutableTreeNode child = children.nextElement();
            if (favorite.equals(child.getUserObject())) {
                return child;
            }
        }
        return null;
    }

    private boolean existsFavorite(Favorite favorite) {
        return findNode(favorite) != null;
    }

    /**
     * Creates a new directory as child of a node. Let's the user input the new name and inserts the new created
     * directory.
     *
     * @param parentNode parent node. If null, nothing will be done.
     * @return new created directory or null if not created
     */
    public File createNewDirectory(DefaultMutableTreeNode parentNode) {
        File dirOfParentNode = (parentNode == null)
                ? null
                : getDirectory(parentNode);
        if (dirOfParentNode != null) {
            File newDir = TreeFileSystemDirectories.createDirectoryIn(dirOfParentNode);
            if (newDir != null) {
                SortedChildrenTreeNode newDirNode = new SortedChildrenTreeNode(newDir);
                parentNode.add(newDirNode);
                int childIndex = parentNode.getIndex(newDirNode);
                fireTreeNodesInserted(this, parentNode.getPath(), new int[]{childIndex}, new Object[]{newDirNode});
                return newDir;
            }
        }
        return null;
    }

    private File getDirectory(DefaultMutableTreeNode node) {
        Object userObject = node.getUserObject();
        return (userObject instanceof Favorite)
                ? ((Favorite) userObject).getDirectory()
                : (userObject instanceof File)
                ? (File) userObject
                : null;
    }

    private Stack<File> getFilePathToNode(DefaultMutableTreeNode node, File file) {
        if (node == null) {
            return null;
        }
        Object userObject = node.getUserObject();
        File nodeFile = (userObject instanceof File)
                ? (File) userObject
                : (userObject instanceof Favorite)
                ? ((Favorite) userObject).getDirectory()
                : null;
        if (nodeFile != null) {
            Stack<File> filePath = FileUtil.getPathFromRoot(file);
            File filePathTop = filePath.peek();
            while (!filePath.isEmpty() && !nodeFile.equals(filePathTop)) {
                filePathTop = filePath.pop();
            }
            return filePath;
        }
        return null;
    }

    /**
     * Expands the tree to a specific file.
     *
     * @param favoriteName favorite containing this file
     * @param file file
     * @param select if true the file node will be selected
     */
    private void expandToFile(String favoriteName, File file, boolean select) {
        DefaultMutableTreeNode node = findFavorite(favoriteName);
        if (node == null) {
            return;
        }
        Stack<File> filePathToFavorite = getFilePathToNode(node, file);
        if (filePathToFavorite == null) {
            return;
        }
        tree.expandPath(new TreePath(node.getPath()));
        while ((node != null) && !filePathToFavorite.isEmpty()) {
            node = TreeUtil.findChildNodeWithFile(node, filePathToFavorite.pop());
            if (node != null && filePathToFavorite.size() > 0) {
                tree.expandPath(new TreePath(node.getPath()));
            }
        }
        if (node != null) {
            TreePath nodePath = new TreePath(node.getPath());
            tree.scrollPathToVisible(nodePath);
            if (select) {
                tree.setSelectionPath(nodePath);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private DefaultMutableTreeNode findFavorite(String name) {
        for (Enumeration<DefaultMutableTreeNode> children = rootNode.children(); children.hasMoreElements();) {
            DefaultMutableTreeNode childNode = children.nextElement();
            Object userObject = childNode.getUserObject();
            if (userObject instanceof Favorite) {
                Favorite fav = (Favorite) userObject;
                if (name.equals(fav.getName())) {
                    return childNode;
                }
            }
        }
        return null;
    }

    public void readFromProperties() {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
        String favname = prefs.getString(KEY_SELECTED_FAV_NAME);
        String dirname = prefs.getString(KEY_SELECTED_DIR);
        if ((favname != null) && (dirname != null) && !favname.trim().isEmpty() && !dirname.trim().isEmpty()) {
            expandToFile(favname.trim(), new File(dirname.trim()), true);
        } else if (favname != null) {
            DefaultMutableTreeNode fav = findFavorite(favname.trim());
            if (fav != null) {
                TreePath path = new TreePath(fav.getPath());
                tree.setSelectionPath(path);
                tree.scrollPathToVisible(path);
            }
        }
    }

    private void writeToProperties() {
        if (tree.getSelectionCount() > 0) {
            TreePath path = tree.getSelectionPath();
            Object o = path.getLastPathComponent();
            if (o instanceof DefaultMutableTreeNode) {
                String favname = null;
                String dirname = null;
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) o;
                Object userObject = node.getUserObject();
                if (userObject instanceof Favorite) {
                    favname = ((Favorite) userObject).getName();
                } else if (userObject instanceof File) {
                    File file = ((File) userObject);
                    dirname = file.getAbsolutePath();
                    Favorite favDir = getParentFavDir(node);
                    if (favDir != null) {
                        favname = favDir.getName();
                    }
                }
                Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
                if (dirname == null) {
                    prefs.removeKey(KEY_SELECTED_DIR);
                } else {
                    prefs.setString(KEY_SELECTED_DIR, dirname);
                }
                if (favname == null) {
                    prefs.removeKey(KEY_SELECTED_FAV_NAME);
                } else {
                    prefs.setString(KEY_SELECTED_FAV_NAME, favname);
                }
            }
        } else {
            Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
            prefs.removeKey(KEY_SELECTED_DIR);
            prefs.removeKey(KEY_SELECTED_FAV_NAME);
        }
    }

    private Favorite getParentFavDir(DefaultMutableTreeNode childNode) {
        TreeNode parentNode = childNode.getParent();
        while ((parentNode instanceof DefaultMutableTreeNode) && !parentNode.equals(rootNode)) {
            Object userObject = ((DefaultMutableTreeNode) parentNode).getUserObject();
            if (userObject instanceof Favorite) {
                return (Favorite) userObject;
            }
            parentNode = parentNode.getParent();
        }
        return null;
    }

    /**
     * Updates this model: Adds nodes for new files, deletes nodes with not existing files.
     */
    public void update() {
        Cursor treeCursor = tree.getCursor();
        Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        tree.setCursor(waitCursor);
        for (DefaultMutableTreeNode node : getTreeRowNodes()) {
            TreeNode parent = node.getParent();
            removeChildrenWithNotExistingFiles((DefaultMutableTreeNode) parent);
            addChildren((DefaultMutableTreeNode) parent);
            for (Enumeration<?> e = parent.children(); e.hasMoreElements();) {
                Object sibling = e.nextElement();
                addDummy((DefaultMutableTreeNode) sibling);
                }
            }
        tree.setCursor(treeCursor);
    }

    private void updateFavorite(Favorite oldFavorite, Favorite updatedFavorite) {
        DefaultMutableTreeNode nodeOfFavorite = findNode(oldFavorite);
        if (nodeOfFavorite != null) {
            updateNodes(nodeOfFavorite, updatedFavorite);
        }
    }

    @EventSubscriber(eventClass = FavoriteInsertedEvent.class)
    public void favoriteInserted(final FavoriteInsertedEvent evt) {
        if (listenToRepo) {
            addFavorite(evt.getFavorite());
        }
    }

    @EventSubscriber(eventClass = FavoriteDeletedEvent.class)
    public void favoriteDeleted(final FavoriteDeletedEvent evt) {
        if (listenToRepo) {
            deleteFavorite(evt.getFavorite());
        }
    }

    @EventSubscriber(eventClass = FavoriteUpdatedEvent.class)
    public void favoriteUpdated(final FavoriteUpdatedEvent evt) {
        if (listenToRepo) {
            updateFavorite(evt.getOldFavorite(), evt.getUpdatedFavorite());
        }
    }

    private List<DefaultMutableTreeNode> getTreeRowNodes() {
        int rows = tree.getRowCount();
        List<DefaultMutableTreeNode> nodes = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            nodes.add((DefaultMutableTreeNode) tree.getPathForRow(i).getLastPathComponent());
        }
        return nodes;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        if (event == null) {
            throw new NullPointerException("event == null");
        }
        Cursor treeCursor = tree.getCursor();
        Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        tree.setCursor(waitCursor);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
        LOGGER.log(Level.FINEST, "Node ''{0}'' has been expanded, adding children...", node);
            addChildren(node);
        tree.setCursor(treeCursor);
    }

    @Override
    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
        // ignore
    }

    @EventSubscriber(eventClass = AppWillExitEvent.class)
    public void appWillExit(AppWillExitEvent evt) {
        writeToProperties();
    }

    private void errorMessage(String favoriteName, String cause) {
        String message = Bundle.getString(FavoritesTreeModel.class, "FavoritesTreeModel.Error.Template", favoriteName, cause);
        MessageDisplayer.error(null, message);
    }

    private void errorMessageAddDirectory(final Favorite favorite) {
        EventQueueUtil.invokeInDispatchThread(new Runnable() {
            @Override
            public void run() {
                String message = Bundle.getString(FavoritesTreeModel.class, "FavoritesTreeModel.Confirm.RemoveNotExistingFavorite",
                        favorite.getDirectory(), favorite.getName());
                if (MessageDisplayer.confirmYesNo(ComponentUtil.findFrameWithIcon(), message)) {
                    repo.deleteFavorite(favorite.getName());
                }
            }
        });
    }

    /**
     * Scans external filesystem for changes. Default: false.
     */
    public void startAutoUpdate() {
        synchronized (updateScheduler) {
            if (!autoupdate) {
                updateScheduler.scheduleWithFixedDelay(updater, 0, UPDATE_INTERVAL_MILLISECONDS, TimeUnit.MILLISECONDS);
                autoupdate = true;
            }
        }
    }

    /**
     * Stops scan for external filesystem for changes if started.
     */
    public void stopAutoUpdate() {
        synchronized (updateScheduler) {
            if (autoupdate) {
                updateScheduler.shutdown();
                autoupdate = false;
            }
        }
    }

    // Java 7 WatchService API does not work, see comment in org.jphototagger.lib.swing.AllSystemDirectoriesTreeModel
    private final Runnable updater = new Runnable() {

        private final AtomicInteger taskCount = new AtomicInteger(0);

        @Override
        public void run() {
            if (taskCount.intValue() > 0) {
                return;
            }
            for (DefaultMutableTreeNode node : getTreeRowNodes()) {
                updateRemoveDummy(node);
                Object userObject = node.getUserObject();
                File dir = null;
                if (userObject instanceof File) {
                    dir = (File) userObject;
                } else if (userObject instanceof Favorite) {
                    Favorite favorite = (Favorite) userObject;
                    dir = favorite.getDirectory();
                }
                if (dir != null) {
                    if (!dir.exists()) {
                        remove(node);
                    } else {
                        addNewChildren(node);
                    }
                }
            }
        }

        private void updateRemoveDummy(final DefaultMutableTreeNode node) {
            taskCount.incrementAndGet();
            EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    removeDummy(node, true);
                    taskCount.decrementAndGet();
                }
            });
        }

        private void remove(final DefaultMutableTreeNode node) {
            taskCount.incrementAndGet();
            EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    removeNodeFromParent(node);
                    Object userObject = node.getUserObject();
                    if (userObject instanceof Favorite) {
                        Favorite favorite = (Favorite) userObject;
                        synchronized (monitor) {
                            boolean prevListenToRepo = listenToRepo;
                            listenToRepo = false;
                            repo.deleteFavorite(favorite.getName());
                            listenToRepo = prevListenToRepo;
                        }
                    }
                    taskCount.decrementAndGet();
                }
            });
        }

        private void addNewChildren(final DefaultMutableTreeNode node) {
            taskCount.incrementAndGet();
            Object userObject = node.getUserObject();
            File dir = userObject instanceof File
                    ? (File) node.getUserObject()
                    : userObject instanceof Favorite
                    ? ((Favorite) userObject).getDirectory()
                    : null;
            if (dir == null) {
                taskCount.decrementAndGet();
                return;
            }
            if (!TreeUtil.isNodeExpanded(tree, node)) {
                addDummy(node);
                taskCount.decrementAndGet();
                return;
            }
            final List<File> childDirectories = getChildDirectories(node);
            final List<File> directories = FileUtil.listFiles(dir, directoryFilter);
            EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    for (File directory : directories) {
                        if (!childDirectories.contains(directory) && FileUtil.isReadableDirectory(directory)) {
                            DefaultMutableTreeNode child = addChildDirectory(node, directory);
                            addDummy(child);
                        }
                    }
                    taskCount.decrementAndGet();
                }
            });
        }
    };

    private final ThreadFactory threadFactory = new ThreadFactory() {

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("JPhotoTagger: Favorite Directories Tree Update");
            return thread;
        }
    };
}
