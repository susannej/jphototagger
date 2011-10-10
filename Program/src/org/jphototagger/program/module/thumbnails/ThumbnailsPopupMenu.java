package org.jphototagger.program.module.thumbnails;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JPopupMenu.Separator;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import org.openide.util.Lookup;

import org.jphototagger.api.plugin.fileprocessor.FileProcessorPlugin;
import org.jphototagger.domain.programs.Program;
import org.jphototagger.domain.programs.ProgramType;
import org.jphototagger.domain.repository.ProgramsRepository;
import org.jphototagger.domain.repository.event.programs.ProgramDeletedEvent;
import org.jphototagger.domain.repository.event.programs.ProgramInsertedEvent;
import org.jphototagger.domain.repository.event.programs.ProgramUpdatedEvent;
import org.jphototagger.lib.event.util.KeyEventUtil;
import org.jphototagger.lib.swing.IconUtil;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.program.app.ui.AppLookAndFeel;
import org.jphototagger.program.factory.FileProcessorPluginManager;
import org.jphototagger.program.module.actions.ActionsHelper;
import org.jphototagger.program.module.programs.AddProgramController;
import org.jphototagger.program.plugins.PluginAction;

/**
 * Popup menu of the thumbnails panel.
 *
 * @author Elmar Baumann, Tobias Stening
 */
public final class ThumbnailsPopupMenu extends JPopupMenu {

    public static final ThumbnailsPopupMenu INSTANCE = new ThumbnailsPopupMenu();
    private static final long serialVersionUID = 1415777088897583494L;
    private static final AddProgramController ADD_PROGRAM_ACTION = new AddProgramController();
    private final JMenu menuRefresh = new JMenu(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.MenuRefresh"));
    private final JMenu menuPrograms = new JMenu(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.MenuOtherOpenImageApps"));
    private final JMenu menuMetadata = new JMenu(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.MenuMetadata"));
    private final JMenu menuImageCollection = new JMenu(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.MenuImageCollection"));
    private final JMenu menuRotateThumbnail = new JMenu(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.MenuRotateThumbnail"));
    private final JMenu menuRating = new JMenu(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.menuRating"));
    private final JMenu menuPlugins = new JMenu(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.MenuPlugins"));
    private final JMenu menuSelection = new JMenu(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.MenuSelection"));
    private final JMenu menuFsOps = new JMenu(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.MenuFileSystemOps"));
    private final JMenu menuActions = ActionsHelper.actionsAsMenu();
    private final JMenuItem itemUpdateThumbnail = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Action.UpdateThumbnail"));
    private final JMenuItem itemUpdateMetadata = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Action.UpdateMetadata"));
    private final JMenuItem itemSelectNothing = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.ItemSelectNothing"));
    private final JMenuItem itemSelectAll = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.ItemSelectAll"));
    private final JMenuItem itemRotateThumbnail90 = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Action.Rotate.90"), AppLookAndFeel.getIcon("icon_rotate_90.png"));
    private final JMenuItem itemRotateThumbnail270 = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Action.Rotate.270"), AppLookAndFeel.getIcon("icon_rotate_270.png"));
    private final JMenuItem itemRotateThumbnai180 = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Action.Rotate.180"), AppLookAndFeel.getIcon("icon_rotate_180.png"));
    private final JMenuItem itemReject = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Action.Reject"), AppLookAndFeel.getIcon("icon_rejected.png"));
    private final JMenuItem itemRefresh = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Action.Refresh"), AppLookAndFeel.ICON_REFRESH);
    private final JMenuItem itemRating5 = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Rating5"), AppLookAndFeel.getIcon("icon_xmp_rating_5.png"));
    private final JMenuItem itemRating4 = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Rating4"), AppLookAndFeel.getIcon("icon_xmp_rating_4.png"));
    private final JMenuItem itemRating3 = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Rating3"), AppLookAndFeel.getIcon("icon_xmp_rating_3.png"));
    private final JMenuItem itemRating2 = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Rating2"), AppLookAndFeel.getIcon("icon_xmp_rating_2.png"));
    private final JMenuItem itemRating1 = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Rating1"), AppLookAndFeel.getIcon("icon_xmp_rating_1.png"));
    private final JMenuItem itemRating0 = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Rating0"), AppLookAndFeel.getIcon("icon_xmp_rating_remove.png"));
    private final JMenuItem itemPick = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Action.Pick"), AppLookAndFeel.getIcon("icon_picked.png"));
    private final JMenuItem itemPasteMetadata = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.ItemPasteMetadata"), AppLookAndFeel.ICON_PASTE);
    private final JMenuItem itemPasteFromClipboard = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.ItemPasteFromClipboard"), AppLookAndFeel.ICON_PASTE);
    private final JMenuItem itemOpenFilesWithStandardApp = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Action.OpenFiles"));
    private final JMenuItem itemIptcToXmp = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Action.IptcToXmp"));
    private final JMenuItem itemFileSystemRenameFiles = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Action.FileSystemRename"));
    private final JMenuItem itemFileSystemMoveFiles = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Action.FileSystemMove"));
    private final JMenuItem itemFileSystemDeleteFiles = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Action.FileSystemDeleteFiles"), AppLookAndFeel.ICON_DELETE);
    private final JMenuItem itemFileSystemCopyToDirectory = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Action.FileSystemCopyToDirectory"), AppLookAndFeel.ICON_COPY);
    private final JMenuItem itemExifToXmp = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Action.ExifToXmp"));
    private final JMenuItem itemDeleteImageFromRepository = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Action.DeleteImageFromRepository"));
    private final JMenuItem itemDeleteFromImageCollection = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Action.DeleteFromImageCollection"), AppLookAndFeel.getIcon("icon_imagecollection_remove_from.png"));
    private final JMenuItem itemCutToClipboard = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.ItemCutToClipboard"), AppLookAndFeel.ICON_CUT);
    private final JMenuItem itemCreateImageCollection = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Action.CreateImageCollection"), AppLookAndFeel.getIcon("icon_imagecollection.png"));
    private final JMenuItem itemCopyToClipboard = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.ItemCopyToClipboard"), AppLookAndFeel.ICON_COPY);
    private final JMenuItem itemCopyMetadata = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.ItemCopyMetadata"), AppLookAndFeel.ICON_COPY);
    private final JMenuItem itemAddToImageCollection = new JMenuItem(Bundle.getString(ThumbnailsPopupMenu.class, "ThumbnailsPopupMenu.DisplayName.Action.AddToImageCollection"), AppLookAndFeel.getIcon("icon_imagecollection_add_to.png"));
    // End menu items
    private final List<ActionListener> actionListenersOpenFilesWithOtherApp = new ArrayList<ActionListener>();
    private final Map<JMenuItem, Program> programOfMenuItem = new HashMap<JMenuItem, Program>();
    private final Map<JMenuItem, Long> RATING_OF_ITEM = new HashMap<JMenuItem, Long>();
    private final Map<JMenuItem, FileProcessorPlugin> FILE_PROCESSOR_PLUGIN_OF_ITEM = new HashMap<JMenuItem, FileProcessorPlugin>();
    private final Map<JMenuItem, Action> ACTION_OF_ITEM = new HashMap<JMenuItem, Action>();

    private ThumbnailsPopupMenu() {
        init();
    }

    private void initRatingOfItem() {
        RATING_OF_ITEM.put(itemRating0, Long.valueOf(0));
        RATING_OF_ITEM.put(itemRating1, Long.valueOf(1));
        RATING_OF_ITEM.put(itemRating2, Long.valueOf(2));
        RATING_OF_ITEM.put(itemRating3, Long.valueOf(3));
        RATING_OF_ITEM.put(itemRating4, Long.valueOf(4));
        RATING_OF_ITEM.put(itemRating5, Long.valueOf(5));
    }

    private void addItems() {
        menuRefresh.add(itemUpdateThumbnail);
        menuRefresh.add(itemUpdateMetadata);
        menuRefresh.add(itemDeleteImageFromRepository);
        menuRefresh.add(itemRefresh);
        add(menuRefresh);
        menuRotateThumbnail.add(itemRotateThumbnail90);
        menuRotateThumbnail.add(itemRotateThumbnai180);
        menuRotateThumbnail.add(itemRotateThumbnail270);
        add(menuRotateThumbnail);
        add(new Separator());
        add(itemOpenFilesWithStandardApp);
        add(menuPrograms);
        add(menuActions);
        addPluginItems();
        add(new Separator());
        menuRating.add(itemRating0);
        menuRating.add(itemRating1);
        menuRating.add(itemRating2);
        menuRating.add(itemRating3);
        menuRating.add(itemRating4);
        menuRating.add(itemRating5);
        add(menuRating);
        menuSelection.add(itemPick);
        menuSelection.add(itemReject);
        add(menuSelection);
        menuImageCollection.add(itemCreateImageCollection);
        menuImageCollection.add(itemAddToImageCollection);
        menuImageCollection.add(itemDeleteFromImageCollection);
        add(menuImageCollection);
        menuMetadata.add(itemCopyMetadata);
        menuMetadata.add(itemPasteMetadata);
        menuMetadata.add(new Separator());
        menuMetadata.add(itemIptcToXmp);
        menuMetadata.add(itemExifToXmp);
        add(menuMetadata);
        itemPasteMetadata.setEnabled(false);
        menuFsOps.add(itemCopyToClipboard);
        menuFsOps.add(itemCutToClipboard);
        menuFsOps.add(itemPasteFromClipboard);
        menuFsOps.add(new Separator());
        menuFsOps.add(itemFileSystemCopyToDirectory);
        menuFsOps.add(new Separator());
        menuFsOps.add(itemFileSystemRenameFiles);
        menuFsOps.add(itemFileSystemMoveFiles);
        menuFsOps.add(itemFileSystemDeleteFiles);
        menuFsOps.add(new Separator());
        menuFsOps.add(itemSelectAll);
        menuFsOps.add(itemSelectNothing);
        add(menuFsOps);
    }

    private void addPluginItems() {
        if (!FileProcessorPluginManager.INSTANCE.hasEnabledPlugins()) {
            return;
        }

        add(menuPlugins);

        for (FileProcessorPlugin plugin : FileProcessorPluginManager.INSTANCE.getEnabledPlugins()) {
            if (plugin.isAvailable()) {
                addItemsOf(plugin);
            }
        }
    }

    private void addItemsOf(FileProcessorPlugin plugin) {
        PluginAction<FileProcessorPlugin> pluginAction = new PluginAction<FileProcessorPlugin>(plugin);
        JMenuItem pluginItem = new JMenuItem(pluginAction);

        ACTION_OF_ITEM.put(pluginItem, pluginAction);
        FILE_PROCESSOR_PLUGIN_OF_ITEM.put(pluginItem, plugin);

        menuPlugins.add(pluginItem);
    }

    public void setOtherPrograms() {
        menuPrograms.removeAll();
        programOfMenuItem.clear();

        ProgramsRepository repo = Lookup.getDefault().lookup(ProgramsRepository.class);
        List<Program> programs = repo.findAllPrograms(ProgramType.PROGRAM);

        if (!programs.isEmpty()) {
            for (Program program : programs) {
                String alias = program.getAlias();
                JMenuItem item = new JMenuItem(alias);

                for (ActionListener listener : actionListenersOpenFilesWithOtherApp) {
                    item.addActionListener(listener);
                }

                menuPrograms.add(item);

                if (program.getFile().exists()) {
                    item.setIcon(IconUtil.getSystemIcon(program.getFile()));
                }

                programOfMenuItem.put(item, program);
            }
        }

        menuPrograms.add(ADD_PROGRAM_ACTION);
    }

    public Action getActionOfItem(JMenuItem item) {
        return ACTION_OF_ITEM.get(item);
    }

    @EventSubscriber(eventClass = ProgramDeletedEvent.class)
    public void programDeleted(final ProgramDeletedEvent evt) {
        updatePrograms(evt.getProgram());
    }

    @EventSubscriber(eventClass = ProgramInsertedEvent.class)
    public void programInserted(final ProgramInsertedEvent evt) {
        updatePrograms(evt.getProgram());
    }

    @EventSubscriber(eventClass = ProgramUpdatedEvent.class)
    public void programUpdated(final ProgramUpdatedEvent evt) {
        updatePrograms(evt.getProgram());
    }

    private void updatePrograms(Program updatedProgram) {
        if (!updatedProgram.isAction()) {
            setOtherPrograms();
        }
    }

    public JMenuItem getItemAddToImageCollection() {
        return itemAddToImageCollection;
    }

    public JMenuItem getItemFileSystemCopyToDirectory() {
        return itemFileSystemCopyToDirectory;
    }

    public JMenuItem getItemCreateImageCollection() {
        return itemCreateImageCollection;
    }

    public JMenuItem getItemDeleteFromImageCollection() {
        return itemDeleteFromImageCollection;
    }

    public JMenuItem getItemDeleteImageFromRepository() {
        return itemDeleteImageFromRepository;
    }

    public JMenuItem getItemFileSystemDeleteFiles() {
        return itemFileSystemDeleteFiles;
    }

    public JMenuItem getItemFileSystemMoveFiles() {
        return itemFileSystemMoveFiles;
    }

    public JMenuItem getItemFileSystemRenameFiles() {
        return itemFileSystemRenameFiles;
    }

    public JMenuItem getItemOpenFilesWithStandardApp() {
        return itemOpenFilesWithStandardApp;
    }

    public JMenuItem getItemRotateThumbnai180() {
        return itemRotateThumbnai180;
    }

    public JMenuItem getItemRotateThumbnail270() {
        return itemRotateThumbnail270;
    }

    public JMenuItem getItemRotateThumbnail90() {
        return itemRotateThumbnail90;
    }

    public JMenuItem getItemUpdateMetadata() {
        return itemUpdateMetadata;
    }

    public JMenuItem getItemUpdateThumbnail() {
        return itemUpdateThumbnail;
    }

    public JMenuItem getItemRefresh() {
        return itemRefresh;
    }

    public JMenuItem getItemIptcToXmp() {
        return itemIptcToXmp;
    }

    public JMenuItem getItemExifToXmp() {
        return itemExifToXmp;
    }

    public JMenuItem getItemPick() {
        return itemPick;
    }

    public JMenuItem getItemCopyMetadata() {
        return itemCopyMetadata;
    }

    public JMenuItem getItemPasteMetadata() {
        return itemPasteMetadata;
    }

    public JMenuItem getItemCopyToClipboard() {
        return itemCopyToClipboard;
    }

    public JMenuItem getItemPasteFromClipboard() {
        return itemPasteFromClipboard;
    }

    public JMenuItem getItemCutToClipboard() {
        return itemCutToClipboard;
    }

    public JMenuItem getItemReject() {
        return itemReject;
    }

    public JMenuItem getItemRating0() {
        return itemRating0;
    }

    public JMenuItem getItemRating1() {
        return itemRating1;
    }

    public JMenuItem getItemRating2() {
        return itemRating2;
    }

    public JMenuItem getItemRating3() {
        return itemRating3;
    }

    public JMenuItem getItemRating4() {
        return itemRating4;
    }

    public JMenuItem getItemRating5() {
        return itemRating5;
    }

    public JMenuItem getItemSelectAll() {
        return itemSelectAll;
    }

    public JMenuItem getItemSelectNothing() {
        return itemSelectNothing;
    }

    public JMenu getMenuRating() {
        return menuRating;
    }

    public JMenu getMenuPlugins() {
        return menuPlugins;
    }

    public JMenu getMenuFsOps() {
        return menuFsOps;
    }

    public JMenu getMenuImageCollection() {
        return menuImageCollection;
    }

    public JMenu getMenuMetadata() {
        return menuMetadata;
    }

    public JMenu getMenuPrograms() {
        return menuPrograms;
    }

    public JMenu getMenuActions() {
        return menuActions;
    }

    public JMenu getMenuRefresh() {
        return menuRefresh;
    }

    public JMenu getMenuRotateThumbnail() {
        return menuRotateThumbnail;
    }

    public JMenu getMenuSelection() {
        return menuSelection;
    }

    public Long getRatingOfItem(JMenuItem item) {
        return RATING_OF_ITEM.get(item);
    }

    public Set<JMenuItem> getFileProcessorPluginMenuItems() {
        return FILE_PROCESSOR_PLUGIN_OF_ITEM.keySet();
    }

    public FileProcessorPlugin getFileProcessorPluginOfItem(JMenuItem item) {
        return FILE_PROCESSOR_PLUGIN_OF_ITEM.get(item);
    }

    public synchronized void addActionListenerOpenFilesWithOtherApp(ActionListener listener) {
        actionListenersOpenFilesWithOtherApp.add(listener);
        setOtherPrograms();
    }

    public Program getProgram(Object source) {
        if (source instanceof JMenuItem) {
            return programOfMenuItem.get((JMenuItem) source);
        }

        return null;
    }

    private void init() {
        initRatingOfItem();
        addItems();
        itemDeleteFromImageCollection.setEnabled(false);
        setAccelerators();
        AnnotationProcessor.process(this);
    }

    private void setAccelerators() {
        itemDeleteFromImageCollection.setAccelerator(KeyEventUtil.getKeyStroke(KeyEvent.VK_DELETE));
        itemFileSystemDeleteFiles.setAccelerator(KeyEventUtil.getKeyStroke(KeyEvent.VK_DELETE));
        itemFileSystemRenameFiles.setAccelerator(KeyEventUtil.getKeyStroke(KeyEvent.VK_F2));
        itemRefresh.setAccelerator(KeyEventUtil.getKeyStroke(KeyEvent.VK_F5));
        itemCopyToClipboard.setAccelerator(KeyEventUtil.getKeyStrokeMenuShortcut(KeyEvent.VK_C));
        itemCutToClipboard.setAccelerator(KeyEventUtil.getKeyStrokeMenuShortcut(KeyEvent.VK_X));
        itemPasteFromClipboard.setAccelerator(KeyEventUtil.getKeyStrokeMenuShortcut(KeyEvent.VK_V));
        itemCopyMetadata.setAccelerator(KeyEventUtil.getKeyStrokeMenuShortcutWithShiftDown(KeyEvent.VK_C));
        itemPasteMetadata.setAccelerator(KeyEventUtil.getKeyStrokeMenuShortcutWithShiftDown(KeyEvent.VK_V));
        itemPick.setAccelerator(KeyEventUtil.getKeyStroke(KeyEvent.VK_P));
        itemReject.setAccelerator(KeyEventUtil.getKeyStroke(KeyEvent.VK_R));
        itemRating0.setAccelerator(KeyEventUtil.getKeyStroke(KeyEvent.VK_0));
        itemRating1.setAccelerator(KeyEventUtil.getKeyStroke(KeyEvent.VK_1));
        itemRating2.setAccelerator(KeyEventUtil.getKeyStroke(KeyEvent.VK_2));
        itemRating3.setAccelerator(KeyEventUtil.getKeyStroke(KeyEvent.VK_3));
        itemRating4.setAccelerator(KeyEventUtil.getKeyStroke(KeyEvent.VK_4));
        itemRating5.setAccelerator(KeyEventUtil.getKeyStroke(KeyEvent.VK_5));
        itemSelectAll.setAccelerator(KeyEventUtil.getKeyStrokeMenuShortcut(KeyEvent.VK_A));
    }
}