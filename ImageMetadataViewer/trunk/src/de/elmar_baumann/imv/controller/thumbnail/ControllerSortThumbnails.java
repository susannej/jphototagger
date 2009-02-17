package de.elmar_baumann.imv.controller.thumbnail;

import de.elmar_baumann.imv.io.FileSort;
import de.elmar_baumann.imv.resource.Panels;
import de.elmar_baumann.imv.view.frames.AppFrame;
import de.elmar_baumann.imv.view.panels.ImageFileThumbnailsPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButtonMenuItem;

/**
 * 
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/10/14
 */
public final class ControllerSortThumbnails implements ActionListener {

    private final ImageFileThumbnailsPanel thumbnailsPanel = Panels.getInstance().getAppPanel().getPanelThumbnails();
    private final AppFrame appFrame = Panels.getInstance().getAppFrame();

    public ControllerSortThumbnails() {
        listen();
        appFrame.getMenuItemOfSort(thumbnailsPanel.getSort()).setSelected(true);
    }

    private void listen() {
        listenToActionSources();
    }

    private void listenToActionSources() {
        for (FileSort sort : FileSort.values()) {
            appFrame.getMenuItemOfSort(sort).addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        sortThumbnails(e);
    }

    private void sortThumbnails(ActionEvent e) {
        JRadioButtonMenuItem item = (JRadioButtonMenuItem) e.getSource();
        FileSort sort = appFrame.getSortOfMenuItem(item);
        setSelectedMenuItems(sort);
        thumbnailsPanel.setSort(sort);
        thumbnailsPanel.sort();
    }

    private void setSelectedMenuItems(FileSort sort) {
        for (FileSort sortType : FileSort.values()) {
            appFrame.getMenuItemOfSort(sortType).setSelected(sortType.equals(sort));
        }
    }
}
