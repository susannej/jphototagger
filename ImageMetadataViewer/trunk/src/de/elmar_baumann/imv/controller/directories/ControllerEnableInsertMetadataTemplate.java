package de.elmar_baumann.imv.controller.directories;

import de.elmar_baumann.imv.resource.Panels;
import de.elmar_baumann.imv.view.panels.AppPanel;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 * Aktiviert den Einfügen-Button für Daten eines Metadaten-Templates
 * beim Selektieren eines Verzeichnisses.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/09/22
 */
public final class ControllerEnableInsertMetadataTemplate implements TreeSelectionListener {

    private final AppPanel appPanel = Panels.getInstance().getAppPanel();
    private final JTree treeDirectories = appPanel.getTreeDirectories();
    private final JButton buttonMetadataTemplateInsert = appPanel.getButtonMetadataTemplateInsert();

    public ControllerEnableInsertMetadataTemplate() {
        listen();
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if (e.isAddedPath()) {
            setEnabledButtonInsert();
        }
    }

    private void listen() {
        treeDirectories.addTreeSelectionListener(this);
    }

    private void setEnabledButtonInsert() {
        if (treeDirectories.getSelectionPath().getLastPathComponent()  instanceof File) {

            String directoryName = ((File) 
                treeDirectories.getSelectionPath().getLastPathComponent()).getAbsolutePath();
            File directory = new File(directoryName);
            
            buttonMetadataTemplateInsert.setEnabled(
                directory.isDirectory() && directory.canWrite());
        }
    }
}
