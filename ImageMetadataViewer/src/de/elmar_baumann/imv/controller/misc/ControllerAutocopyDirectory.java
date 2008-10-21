package de.elmar_baumann.imv.controller.misc;

import de.elmar_baumann.imv.AppSettings;
import de.elmar_baumann.imv.UserSettings;
import de.elmar_baumann.imv.controller.Controller;
import de.elmar_baumann.imv.io.ImageFilteredDirectory;
import de.elmar_baumann.imv.resource.Bundle;
import de.elmar_baumann.imv.resource.Panels;
import de.elmar_baumann.imv.view.dialogs.CopyToDirectoryDialog;
import de.elmar_baumann.imv.view.dialogs.UserSettingsDialog;
import de.elmar_baumann.lib.io.FileUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * 
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/10/19
 */
public class ControllerAutocopyDirectory extends Controller implements ActionListener {

    public ControllerAutocopyDirectory() {
        Panels.getInstance().getAppFrame().getMenuItemAutocopyDirectory().addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (isControl()) {
            File dir = UserSettings.getInstance().getAutocopyDirectory();
            if (dir == null && settingsMessage()) {
                UserSettingsDialog dialog = UserSettingsDialog.getInstance();
                dialog.selectTab(UserSettingsDialog.Tab.Misc);
                if (dialog.isVisible()) {
                    dialog.toFront();
                } else {
                    dialog.setVisible(true);
                }
            } else {
                copy(dir);
            }
        }
    }

    synchronized private void copy(File srcDir) {
        List<File> directories = new ArrayList<File>();
        directories.add(srcDir);
        directories.addAll(FileUtil.getAllSubDirectories(srcDir, false));
        List<File> files = ImageFilteredDirectory.getImageFilesOfDirectories(directories);
        if (files.size() > 0) {
            CopyToDirectoryDialog dialog = new CopyToDirectoryDialog();
            dialog.setSourceFiles(files);
            dialog.setVisible(true);
        } else {
            messageNoFilesFound();
        }
    }

    private void messageNoFilesFound() {
        JOptionPane.showMessageDialog(
            null,
            Bundle.getString("ControllerAutocopyDirectory.InformationMessage.NoFilesFound"),
            Bundle.getString("ControllerAutocopyDirectory.InformationMessage.NoFilesFound.Title"),
            JOptionPane.INFORMATION_MESSAGE,
            AppSettings.getMediumAppIcon());
    }

    private boolean settingsMessage() {
        return JOptionPane.showConfirmDialog(
            null,
            Bundle.getString("ControllerAutocopyDirectory.ConfirmMessage.DefineDirectory"),
            Bundle.getString("ControllerAutocopyDirectory.ConfirmMessage.DefineDirectory.Title"),
            JOptionPane.ERROR_MESSAGE,
            JOptionPane.YES_NO_OPTION,
            AppSettings.getMediumAppIcon()) == JOptionPane.YES_OPTION;
    }
}
