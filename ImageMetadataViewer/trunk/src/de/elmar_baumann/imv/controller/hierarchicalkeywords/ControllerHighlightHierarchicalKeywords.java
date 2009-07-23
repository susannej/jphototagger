package de.elmar_baumann.imv.controller.hierarchicalkeywords;

import de.elmar_baumann.imv.database.DatabaseImageFiles;
import de.elmar_baumann.imv.event.listener.ThumbnailsPanelListener;
import de.elmar_baumann.imv.image.metadata.xmp.XmpMetadata;
import de.elmar_baumann.imv.resource.GUI;
import de.elmar_baumann.imv.view.dialogs.HierarchicalKeywordsDialog;
import de.elmar_baumann.imv.view.panels.ImageFileThumbnailsPanel;
import de.elmar_baumann.imv.view.renderer.TreeCellRendererHierarchicalKeywords;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

/**
 * Listens to
 *
 * @author  Elmar Baumann <ebaumann@feitsch.de>
 * @version 2009/07/23
 */
public final class ControllerHighlightHierarchicalKeywords
        implements ThumbnailsPanelListener {

    private final ImageFileThumbnailsPanel panel =
            GUI.INSTANCE.getAppPanel().getPanelThumbnails();
    private final DatabaseImageFiles db = DatabaseImageFiles.INSTANCE;
    private final JTree treeAppPanel =
            GUI.INSTANCE.getAppPanel().getTreeHierarchicalKeywords();
    private final JTree treeDialog =
            HierarchicalKeywordsDialog.INSTANCE.getPanel().getTree();

    public ControllerHighlightHierarchicalKeywords() {
        listen();
    }

    private void listen() {
        panel.addThumbnailsPanelListener(this);
    }

    @Override
    public void thumbnailsSelectionChanged() {
        removeKeywords();
        if (panel.getSelectionCount() == 1) {
            List<File> selFile = panel.getSelectedFiles();
            assert selFile.size() == 1;
            if (selFile.size() == 1 && hasSidecarFile(selFile)) {
                Collection<String> keywords =
                        db.getDcSubjectsOfFile(selFile.get(0).getAbsolutePath());
                setKeywords(treeAppPanel, keywords);
                setKeywords(treeDialog, keywords);
            }
        }
    }

    private void setKeywords(JTree tree, Collection<String> keywords) {
        TreeCellRenderer r = tree.getCellRenderer();
        assert r instanceof TreeCellRendererHierarchicalKeywords :
                "Renderer is not an instance of " +
                TreeCellRendererHierarchicalKeywords.class + " but of " + r;
        if (r instanceof TreeCellRendererHierarchicalKeywords) {
            ((TreeCellRendererHierarchicalKeywords) r).setKeywords(keywords);
            tree.repaint();
        }
    }

    private void removeKeywords() {
        setKeywords(treeAppPanel, new ArrayList<String>());
        setKeywords(treeDialog, new ArrayList<String>());
    }

    private boolean hasSidecarFile(List<File> selFile) {
        assert selFile.size() == 1 :
                "Size < 1: " + selFile.size() + " - " + selFile;
        return XmpMetadata.hasImageASidecarFile(selFile.get(0).getAbsolutePath());
    }

    @Override
    public void thumbnailsChanged() {
        // ignore
    }
}
