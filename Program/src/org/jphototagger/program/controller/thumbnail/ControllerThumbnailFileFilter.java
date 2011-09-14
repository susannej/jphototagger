package org.jphototagger.program.controller.thumbnail;

import javax.swing.ComboBoxModel;
import javax.swing.JMenuItem;
import javax.swing.event.ListDataEvent;
import org.jphototagger.domain.filefilter.UserDefinedFileFilter;
import org.jphototagger.program.model.ComboBoxModelFileFilters;
import org.jphototagger.program.resource.GUI;
import org.jphototagger.program.view.dialogs.UserDefinedFileFilterDialog;
import org.jphototagger.program.view.panels.ThumbnailsPanel;
import org.jphototagger.program.view.WaitDisplay;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileFilter;
import javax.swing.JComboBox;
import javax.swing.event.ListDataListener;
import org.jphototagger.api.core.Storage;
import org.openide.util.Lookup;

/**
 *
 *
 * @author Elmar Baumann
 */
public final class ControllerThumbnailFileFilter implements ActionListener, ItemListener, ListDataListener {

    private final JComboBox fileFilterComboBox = getFileFilterComboBox();

    public ControllerThumbnailFileFilter() {
        listen();
    }

    private void listen() {
        ComboBoxModel fileFilterComboBoxModel = fileFilterComboBox.getModel();
        JMenuItem menuItemUserDefinedFileFilter = GUI.getAppFrame().getMenuItemUserDefinedFileFilter();

        fileFilterComboBox.addItemListener(this);
        fileFilterComboBoxModel.addListDataListener(this);
        menuItemUserDefinedFileFilter.addActionListener(this);
    }

    private JComboBox getFileFilterComboBox() {
        return GUI.getAppPanel().getComboBoxFileFilters();
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        new UserDefinedFileFilterDialog().setVisible(true);
    }

    @Override
    public void itemStateChanged(ItemEvent evt) {
        setItem(evt.getItem());
    }

    private void setItem(Object item) {
        ThumbnailsPanel tnPanel = GUI.getThumbnailsPanel();

        WaitDisplay.show();

        if (item instanceof FileFilter) {
            tnPanel.setFileFilter((FileFilter) item);
        } else if (item instanceof UserDefinedFileFilter) {
            tnPanel.setFileFilter(((UserDefinedFileFilter) item).getFileFilter());
        }

        writeSettings();
        WaitDisplay.hide();
    }

    private void writeSettings() {
        Storage storage = Lookup.getDefault().lookup(Storage.class);

        storage.setInt(ComboBoxModelFileFilters.SETTINGS_KEY_SEL_INDEX, getFileFilterComboBox().getSelectedIndex());
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
        int index0 = e.getIndex0();
        int index1 = e.getIndex1();

        if (index0 != index1 || index0 < 0) {
            return;
        }

        int selectedIndex = fileFilterComboBox.getSelectedIndex();

        if (selectedIndex == index0) {
            Object selectedItem = fileFilterComboBox.getSelectedItem();
            if (selectedItem != null) {
                setItem(selectedItem);
            }
        }
    }

    @Override
    public void intervalAdded(ListDataEvent e) {
        // ignore
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
        // ignore
    }
}
