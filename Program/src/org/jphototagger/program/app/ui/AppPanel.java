package org.jphototagger.program.app.ui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.TreeSelectionModel;
import org.bushe.swing.event.EventBus;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTree;
import org.jphototagger.api.messages.MessageType;
import org.jphototagger.api.preferences.Preferences;
import org.jphototagger.api.windows.MainWindowComponent;
import org.jphototagger.api.windows.MainWindowComponentProvider;
import org.jphototagger.api.windows.StatusLineElementProvider;
import org.jphototagger.api.windows.TabInEditWindowDisplayedEvent;
import org.jphototagger.api.windows.TabInSelectionWindowDisplayedEvent;
import org.jphototagger.domain.metadata.search.SearchComponent;
import org.jphototagger.domain.thumbnails.MainWindowThumbnailsComponent;
import org.jphototagger.lib.api.LayerUtil;
import org.jphototagger.lib.api.PositionProviderAscendingComparator;
import org.jphototagger.lib.awt.EventQueueUtil;
import org.jphototagger.lib.swing.KeyEventUtil;
import org.jphototagger.lib.swing.MessageLabel;
import org.jphototagger.lib.swing.TreeExpandCollapseAllAction;
import org.jphototagger.lib.swing.util.MnemonicUtil;
import org.jphototagger.lib.swingx.ListTextFilter;
import org.jphototagger.lib.swingx.SearchInJxListAction;
import org.jphototagger.lib.swingx.SearchInJxTreeAction;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.program.module.keywords.KeywordsPanel;
import org.jphototagger.program.resource.GUI;
import org.openide.util.Lookup;

/**
 * @author Elmar Baumann, Tobias Stening
 */
public final class AppPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    private static final String KEY_DIVIDER_LOCATION_MAIN = "AppPanel.DividerLocationMain";
    private static final String KEY_DIVIDER_LOCATION_THUMBNAILS = "AppPanel.DividerLocationThumbnails";
    private static final int DEFAULT_DIVIDER_LOCATION_MAIN = 100;
    private static final int DEFAULT_DIVIDER_LOCATION_THUMBNAILS = 200;
    private transient MessageLabel messageLabel;
    private final List<JTree> selectionTrees = new ArrayList<>();
    private final List<JXList> selectionLists = new ArrayList<>();
    private ListTextFilter listSelKeywordsTextFilter;
    private ListTextFilter listImageCollectionsTextFilter;
    private ListTextFilter listSavedSearchesTextFilter;

    private static final Logger LOGGER = Logger.getLogger(AppPanel.class.getName());

    public AppPanel() {
        init();
    }

    private void init() {
        initComponents();
        messageLabel = new MessageLabel(labelStatusbarText);
        GUI.setAppPanel(this);
        lookupWindows();
        lookupStatusLineElements();
        setTreesSingleSelection();
        initCollections();
        setMnemonics();
        initListTextFilters();
        tabbedPaneMetadata.addChangeListener(tabbedPaneEditChangeListener);
        tabbedPaneSelection.addChangeListener(tabbedPaneSelectionChangeListener);
    }

    private void setMnemonics() {

        // Do not set mnemonics to left panel because it can trigger edit actions!
        MnemonicUtil.setMnemonics((Container) panelSearch);
    }

    private void initListTextFilters() {
        listSelKeywordsTextFilter = new ListTextFilter(listSelKeywords);
        listImageCollectionsTextFilter = new ListTextFilter(listImageCollections);
        listSavedSearchesTextFilter = new ListTextFilter(listSavedSearches);

        listSelKeywordsTextFilter.filterOnDocumentChanges(textFieldListSelKeywordsFilter.getDocument());
        listSavedSearchesTextFilter.filterOnDocumentChanges(textFieldListSavedSearchesFilter.getDocument());
        listImageCollectionsTextFilter.filterOnDocumentChanges(textFieldListImageCollectionsFilter.getDocument());
    }

    private void initCollections() {
        initSelectionTreesCollection();
        initSelectionListsCollection();
    }

    private void initSelectionTreesCollection() {
        selectionTrees.add(treeDirectories);
        selectionTrees.add(treeFavorites);
        selectionTrees.add(treeMiscMetadata);
        selectionTrees.add(treeTimeline);
        selectionTrees.add(treeSelKeywords);
    }

    private void initSelectionListsCollection() {
        selectionLists.add(listImageCollections);
        selectionLists.add(listSelKeywords);
        selectionLists.add(listSavedSearches);
    }

    private int getDividerLocationMain() {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
        int location = prefs.getInt(KEY_DIVIDER_LOCATION_MAIN);

        return (location >= 0)
               ? location
               : DEFAULT_DIVIDER_LOCATION_MAIN;
    }

    private void setTreesSingleSelection() {
        setSingleSelection(treeDirectories);
        setSingleSelection(treeFavorites);
        setSingleSelection(treeMiscMetadata);
        setSingleSelection(treeTimeline);
        setSingleSelection(treeSelKeywords);
    }

    private void setSingleSelection(JTree tree) {
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    public enum SelectAlso { NOTHING_ELSE, SEL_KEYWORDS_TAB }

    public void displaySelKeywordsTree(SelectAlso select) {
        if (select == null) {
            throw new NullPointerException("select == null");
        }

        if (select.equals(SelectAlso.SEL_KEYWORDS_TAB)) {
            tabbedPaneSelection.setSelectedComponent(panelSelKeywords);
        }

        displaySelKeywordsCard("keywordsTree");
    }

    public void displaySelKeywordsList(SelectAlso select) {
        if (select == null) {
            throw new NullPointerException("select == null");
        }

        if (select.equals(SelectAlso.SEL_KEYWORDS_TAB)) {
            tabbedPaneSelection.setSelectedComponent(panelSelKeywords);
        }

        displaySelKeywordsCard("flatKeywords");
    }

    private void displaySelKeywordsCard(String name) {
        CardLayout cl = (CardLayout) (panelSelKeywords.getLayout());

        cl.show(panelSelKeywords, name);
    }

    private int getDividerLocationThumbnails() {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
        int location = prefs.getInt(KEY_DIVIDER_LOCATION_THUMBNAILS);

        return (location >= 0)
               ? location
               : DEFAULT_DIVIDER_LOCATION_THUMBNAILS;
    }

    void setStatusbarText(String text, MessageType type, final long milliseconds) {
        if (milliseconds > 0) {
            messageLabel.showMessage(text, type, milliseconds);
        } else {
            labelStatusbarText.setText(text);
        }
        LOGGER.log(Level.FINE, text);
    }

    public JTabbedPane getTabbedPaneMetadata() {
        return tabbedPaneMetadata;
    }

    public boolean isSelectionComponentSelected(Component component) {
        Component selectedComponent = tabbedPaneSelection.getSelectedComponent();
        return component == selectedComponent;
    }

    public boolean isEditComponentSelected(Component component) {
        Component selectedComponent = tabbedPaneMetadata.getSelectedComponent();
        return component == selectedComponent;
    }

    public JTabbedPane getTabbedPaneSelection() {
        return tabbedPaneSelection;
    }

    public Component getTabEditKeywords() {
        return panelEditKeywords;
    }

    public Component getTabSelectionKeywords() {
        return panelSelKeywords;
    }

    public Component getCardSelKeywordsList() {
        return panelSelKeywordsList;
    }

    public Component getCardSelKeywordsTree() {
        return panelSelKeywordsTree;
    }

    public Component getTabSelectionDirectories() {
        return panelDirectories;
    }

    public Component getTabSelectionFavoriteDirectories() {
        return panelFavorites;
    }

    public Component getTabSelectionImageCollections() {
        return panelImageCollections;
    }

    public Component getTabSelectionTimeline() {
        return panelTimeline;
    }

    public Component getTabSelectionMiscMetadata() {
        return panelMiscMetadata;
    }

    public JPanel getTabSelectionSavedSearches() {
        return panelSavedSearches;
    }

    public JTree getTreeTimeline() {
        return treeTimeline;
    }

    public JTree getTreeMiscMetadata() {
        return treeMiscMetadata;
    }

    public JTree getTreeEditKeywords() {
        return panelEditKeywords.getTree();
    }

    public JTree getTreeSelKeywords() {
        return treeSelKeywords;
    }

    public List<JTree> getSelectionTrees() {
        return Collections.unmodifiableList(selectionTrees);
    }

    public List<JXList> getSelectionLists() {
        return Collections.unmodifiableList(selectionLists);
    }

    public KeywordsPanel getPanelEditKeywords() {
        return panelEditKeywords;
    }

    public JXList getListImageCollections() {
        return listImageCollections;
    }

    public JXList getListSavedSearches() {
        return listSavedSearches;
    }

    public JTree getTreeDirectories() {
        return treeDirectories;
    }

    public JTree getTreeFavorites() {
        return treeFavorites;
    }

    public JXList getListSelKeywords() {
        return listSelKeywords;
    }

    public JXList getListEditKeywords() {
        return panelEditKeywords.getList();
    }

    public JToggleButton getToggleButtonSelKeywords() {
        return toggleButtonExpandAllNodesSelKeywords;
    }

    public JRadioButton getRadioButtonSelKeywordsMultipleSelAll() {
        return radioButtonSelKeywordsMultipleSelAll;
    }

    public JRadioButton getRadioButtonSelKeywordsMultipleSelOne() {
        return radioButtonSelKeywordsMultipleSelOne;
    }

    public JSplitPane getSplitPaneMain() {
        return splitPaneMain;
    }

    public JSplitPane getSplitPaneThumbnailsMetadata() {
        return splitPaneThumbnailsMetadata;
    }

    private void lookupWindows() {
        EventQueueUtil.invokeInDispatchThread(new Runnable() {

            @Override
            public void run() {
                lookupSearchComponent();
                lookupMainWindowThumbnailsComponent();
                Collection<? extends MainWindowComponentProvider> providers = Lookup.getDefault().lookupAll(MainWindowComponentProvider.class);
                List<MainWindowComponent> selectionsComponents = new ArrayList<>();
                List<MainWindowComponent> editComponents = new ArrayList<>();
                for (MainWindowComponentProvider provider : providers) {
                    editComponents.addAll(provider.getMainWindowEditComponents());
                    selectionsComponents.addAll(provider.getMainWindowSelectionComponents());
                }
                instertIntoEditTabbedPane(editComponents);
                insertIntoSelectionTabbedPane(selectionsComponents);
            }
        });
    }

    private void lookupSearchComponent() {
        SearchComponent searchComponent = Lookup.getDefault().lookup(SearchComponent.class);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelSearch.add(searchComponent.getSearchComponent(), gbc);
        GUI.getAppFrame().addGotoMenuItem(searchComponent.getSelectSearchComponentAction(), 0, false);
    }

    private void lookupMainWindowThumbnailsComponent() {
        MainWindowThumbnailsComponent tComponent = Lookup.getDefault().lookup(MainWindowThumbnailsComponent.class);
        Component component = tComponent.getThumbnailsComponent();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        thumbnailPanelComponent.add(component, gbc);
        SelectThumbnailsPanelAction selectThumbnailsPanelAction =
                new SelectThumbnailsPanelAction(tComponent.getThumbnailsDisplayingComponent());
        GUI.getAppFrame().addGotoMenuItem(selectThumbnailsPanelAction, 2, false);

    }

    private static class SelectThumbnailsPanelAction extends AbstractAction {

        private static final long serialVersionUID = 1L;
        private final Component thumbnailsPanel;

        private SelectThumbnailsPanelAction(Component thumbnailsPanel) {
            super(Bundle.getString(AppPanel.class, "SelectThumbnailsPanelAction.Name"));
            this.thumbnailsPanel = thumbnailsPanel;
            putValue(ACCELERATOR_KEY, KeyEventUtil.getKeyStrokeMenuShortcut(KeyEvent.VK_0));
            putValue(SMALL_ICON, AppLookAndFeel.getIcon("icon_thumbnails.png"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            thumbnailsPanel.requestFocusInWindow();
        }
    }

    private void instertIntoEditTabbedPane(List<MainWindowComponent> mainWindowComponents) {
        Collections.sort(mainWindowComponents, PositionProviderAscendingComparator.INSTANCE);
        LayerUtil.logWarningIfNotUniquePositions(mainWindowComponents);
        for (MainWindowComponent component : mainWindowComponents) {
            dockIntoTabbedPane(component, tabbedPaneMetadata);
            SelectTabAction selectTabAction = createSelectTabAction(component, tabbedPaneMetadata);
            GUI.getAppFrame().addGotoMenuItemForEditWindow(selectTabAction);
        }
    }

    private void insertIntoSelectionTabbedPane(List<MainWindowComponent> mainWindowComponents) {
        Collections.sort(mainWindowComponents, PositionProviderAscendingComparator.INSTANCE);
        LayerUtil.logWarningIfNotUniquePositions(mainWindowComponents);
        for (MainWindowComponent component : mainWindowComponents) {
            dockIntoTabbedPane(component, tabbedPaneSelection);
            SelectTabAction selectTabAction = createSelectTabAction(component, tabbedPaneSelection);
            GUI.getAppFrame().addGotoMenuItemForSelectionWindow(selectTabAction);
        }
    }

    private SelectTabAction createSelectTabAction(MainWindowComponent mainWindowComponent, JTabbedPane tabbedPane) {
        SelectTabAction action = new SelectTabAction(tabbedPane, mainWindowComponent.getComponent());
        action.putValue(Action.NAME, mainWindowComponent.getTitle());
        action.putValue(Action.SMALL_ICON, mainWindowComponent.getSmallIcon());
        KeyStroke keyStroke = mainWindowComponent.getOptionalSelectionAccelaratorKey();
        if (keyStroke != null) {
            action.putValue(Action.ACCELERATOR_KEY, keyStroke);
        }
        return action;
    }

    private static class SelectTabAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
        private final JTabbedPane tabbedPane;
        private final Component component;

        private SelectTabAction(JTabbedPane tabbedPane, Component component) {
            this.tabbedPane = tabbedPane;
            this.component = component;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Component selectedComponent = tabbedPane.getSelectedComponent();
            if (component != selectedComponent) {
                tabbedPane.setSelectedComponent(component);
            } else {
                EventBus.publish(new TabInEditWindowDisplayedEvent(this, component));
            }
        }
    }

    private void dockIntoTabbedPane(final MainWindowComponent appWindow, final JTabbedPane tabbedPane) {
        EventQueueUtil.invokeInDispatchThread(new Runnable() {

            @Override
            public void run() {
                int tabCount = tabbedPane.getTabCount();
                int appWindowPosition = appWindow.getPosition();
                int tabIndex = appWindowPosition < 0 || appWindowPosition > tabCount ? tabCount : appWindowPosition;
                Component component = appWindow.getComponent();
                Icon icon = appWindow.getSmallIcon();
                String tip = appWindow.getTooltipText();
                String title = appWindow.getTitle();

                tabbedPane.insertTab(title, icon, component, tip, tabIndex);
            }
        });
    }

    private void lookupStatusLineElements() {
        List<StatusLineElementProvider> providers = new ArrayList<>(
                Lookup.getDefault().lookupAll(StatusLineElementProvider.class));
        Collections.sort(providers, PositionProviderAscendingComparator.INSTANCE);
        boolean isFirst = true;
        for (StatusLineElementProvider provider : providers) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(0, isFirst ? 0 : 10, 0, 0);
            isFirst = false;
            statusLineElementsPanel.add(provider.getStatusLineElement(), gbc);
        }
    }

    private ChangeListener tabbedPaneEditChangeListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            Component selectedComponent = tabbedPaneMetadata.getSelectedComponent();
            EventBus.publish(new TabInEditWindowDisplayedEvent(tabbedPaneMetadata, selectedComponent));
        }
    };

    private ChangeListener tabbedPaneSelectionChangeListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            Component selectedComponent = tabbedPaneSelection.getSelectedComponent();
            EventBus.publish(new TabInSelectionWindowDisplayedEvent(tabbedPaneSelection, selectedComponent));
        }
    };

    @SuppressWarnings("serial")

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */

    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroupKeywordsMultipleSel = new javax.swing.ButtonGroup();
        splitPaneMain = new javax.swing.JSplitPane();
        splitPaneMain.setDividerLocation(getDividerLocationMain());
        panelSelection = new javax.swing.JPanel();
        panelSearch = new javax.swing.JPanel();
        tabbedPaneSelection = new javax.swing.JTabbedPane();
        panelDirectories = new javax.swing.JPanel();
        scrollPaneDirectories = new javax.swing.JScrollPane();
        treeDirectories = new JXTree();
        treeDirectories.setShowsRootHandles(true);
        buttonSearchInDirectories = new javax.swing.JButton();
        panelSavedSearches = new javax.swing.JPanel();
        panelListSavedSearchesFilter = new javax.swing.JPanel();
        labelListSavedSearchesFilter = new javax.swing.JLabel();
        textFieldListSavedSearchesFilter = new javax.swing.JTextField();
        scrollPaneSavedSearches = new javax.swing.JScrollPane();
        listSavedSearches = new JXList();
        buttonSearchInSavedSearches = new javax.swing.JButton();
        panelImageCollections = new javax.swing.JPanel();
        panelListImageCollectionsFilter = new javax.swing.JPanel();
        labelListImageCollectionsFilter = new javax.swing.JLabel();
        textFieldListImageCollectionsFilter = new javax.swing.JTextField();
        scrollPaneImageCollections = new javax.swing.JScrollPane();
        listImageCollections = new JXList();
        buttonSearchInImageCollections = new javax.swing.JButton();
        panelFavorites = new javax.swing.JPanel();
        scrollPaneFavorites = new javax.swing.JScrollPane();
        treeFavorites = new JXTree();
        treeFavorites.setTransferHandler(new org.jphototagger.program.module.directories.DirectoryTreeTransferHandler());
        treeFavorites.setShowsRootHandles(true);
        buttonSearchInTreeFavorites = new javax.swing.JButton();
        panelSelKeywords = new javax.swing.JPanel();
        panelSelKeywordsTree = new javax.swing.JPanel();
        scrollPaneSelKeywordsTree = new javax.swing.JScrollPane();
        treeSelKeywords = new JXTree();
        treeSelKeywords.setTransferHandler(new org.jphototagger.program.module.keywords.tree.KeywordsTreeTransferHandler());
        treeSelKeywords.setShowsRootHandles(true);
        buttonDisplaySelKeywordsList = new javax.swing.JButton();
        toggleButtonExpandAllNodesSelKeywords = new javax.swing.JToggleButton();
        buttonSearchInTreeSelKeywords = new javax.swing.JButton();
        panelSelKeywordsList = new javax.swing.JPanel();
        panelListSelKeywordsFilter = new javax.swing.JPanel();
        labelListSelKeywordsFilter = new javax.swing.JLabel();
        textFieldListSelKeywordsFilter = new javax.swing.JTextField();
        scrollPaneSelKeywordsList = new javax.swing.JScrollPane();
        listSelKeywords = new JXList();
        listSelKeywords.setTransferHandler(new org.jphototagger.program.module.keywords.list.KeywordsListTransferHandler());
        panelSelKeywordsListMultipleSelection = new javax.swing.JPanel();
        radioButtonSelKeywordsMultipleSelAll = new javax.swing.JRadioButton();
        radioButtonSelKeywordsMultipleSelOne = new javax.swing.JRadioButton();
        buttonDisplaySelKeywordsTree = new javax.swing.JButton();
        buttonSearchInListSelKeywords = new javax.swing.JButton();
        panelTimeline = new javax.swing.JPanel();
        scrollPaneTimeline = new javax.swing.JScrollPane();
        treeTimeline = new JXTree();
        treeTimeline.setShowsRootHandles(true);
        toggleButtonExpandCollapseTreeTimeline = new javax.swing.JToggleButton();
        buttonSearchInTreeTimeline = new javax.swing.JButton();
        panelMiscMetadata = new javax.swing.JPanel();
        scrollPaneMiscMetadata = new javax.swing.JScrollPane();
        treeMiscMetadata = new JXTree();
        treeMiscMetadata.setTransferHandler(new org.jphototagger.program.module.miscmetadata.MiscMetadataTreeTransferHandler());
        treeMiscMetadata.setShowsRootHandles(true);
        toggleButtonExpandCollapseTreeMiscMetadata = new javax.swing.JToggleButton();
        buttonSearchInTreeMiscMetadata = new javax.swing.JButton();
        panelThumbnailsMetadata = new javax.swing.JPanel();
        splitPaneThumbnailsMetadata = new javax.swing.JSplitPane();
        splitPaneThumbnailsMetadata.setDividerLocation(getDividerLocationThumbnails());
        thumbnailPanelComponent = new javax.swing.JPanel();
        panelMetadata = new javax.swing.JPanel();
        tabbedPaneMetadata = new javax.swing.JTabbedPane();
        panelEditKeywords = new org.jphototagger.program.module.keywords.KeywordsPanel();
        panelStatusbar = new javax.swing.JPanel();
        labelStatusbarText = new javax.swing.JLabel();
        statusLineElementsPanel = new javax.swing.JPanel();

        setName("Form"); // NOI18N
        setLayout(new java.awt.GridBagLayout());

        splitPaneMain.setDividerSize(6);
        splitPaneMain.setName("splitPaneMain"); // NOI18N
        splitPaneMain.setOneTouchExpandable(true);

        panelSelection.setName("panelSelection"); // NOI18N
        panelSelection.setLayout(new java.awt.GridBagLayout());

        panelSearch.setName("panelSearch"); // NOI18N
        panelSearch.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        panelSelection.add(panelSearch, gridBagConstraints);

        tabbedPaneSelection.setName("tabbedPaneSelection"); // NOI18N

        panelDirectories.setName("panelDirectories"); // NOI18N
        panelDirectories.setLayout(new java.awt.GridBagLayout());

        scrollPaneDirectories.setName("scrollPaneDirectories"); // NOI18N

        treeDirectories.setModel(org.jphototagger.lib.swing.WaitTreeModel.INSTANCE);
        treeDirectories.setCellRenderer(new org.jphototagger.lib.swing.AllSystemDirectoriesTreeCellRenderer());
        treeDirectories.setDragEnabled(true);
        treeDirectories.setName("treeDirectories"); // NOI18N
        treeDirectories.setRootVisible(false);
        scrollPaneDirectories.setViewportView(treeDirectories);
        treeDirectories.setTransferHandler(new org.jphototagger.program.module.directories.DirectoryTreeTransferHandler());
        treeDirectories.setShowsRootHandles(true);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelDirectories.add(scrollPaneDirectories, gridBagConstraints);

        buttonSearchInDirectories.setAction(new SearchInJxTreeAction((JXTree)treeDirectories));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/program/app/ui/Bundle"); // NOI18N
        buttonSearchInDirectories.setText(bundle.getString("AppPanel.buttonSearchInDirectories.text")); // NOI18N
        buttonSearchInDirectories.setMargin(new java.awt.Insets(1, 1, 1, 1));
        buttonSearchInDirectories.setName("buttonSearchInDirectories"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panelDirectories.add(buttonSearchInDirectories, gridBagConstraints);

        tabbedPaneSelection.addTab(bundle.getString("AppPanel.panelDirectories.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/org/jphototagger/program/resource/icons/icon_folder.png")), panelDirectories); // NOI18N

        panelSavedSearches.setName("panelSavedSearches"); // NOI18N
        panelSavedSearches.setLayout(new java.awt.GridBagLayout());

        panelListSavedSearchesFilter.setName("panelListSavedSearchesFilter"); // NOI18N
        panelListSavedSearchesFilter.setLayout(new java.awt.GridBagLayout());

        labelListSavedSearchesFilter.setLabelFor(textFieldListSavedSearchesFilter);
        labelListSavedSearchesFilter.setText(bundle.getString("AppPanel.labelListSavedSearchesFilter.text")); // NOI18N
        labelListSavedSearchesFilter.setName("labelListSavedSearchesFilter"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelListSavedSearchesFilter.add(labelListSavedSearchesFilter, gridBagConstraints);

        textFieldListSavedSearchesFilter.setName("textFieldListSavedSearchesFilter"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelListSavedSearchesFilter.add(textFieldListSavedSearchesFilter, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelSavedSearches.add(panelListSavedSearchesFilter, gridBagConstraints);

        scrollPaneSavedSearches.setName("scrollPaneSavedSearches"); // NOI18N

        listSavedSearches.setModel(org.jphototagger.lib.swing.WaitListModel.INSTANCE);
        listSavedSearches.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listSavedSearches.setCellRenderer(new org.jphototagger.program.module.search.SavedSearchesListCellRenderer());
        listSavedSearches.setName("listSavedSearches"); // NOI18N
        scrollPaneSavedSearches.setViewportView(listSavedSearches);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelSavedSearches.add(scrollPaneSavedSearches, gridBagConstraints);

        buttonSearchInSavedSearches.setAction(new SearchInJxListAction(listSavedSearches));
        buttonSearchInSavedSearches.setText(bundle.getString("AppPanel.buttonSearchInSavedSearches.text")); // NOI18N
        buttonSearchInSavedSearches.setMargin(new java.awt.Insets(1, 1, 1, 1));
        buttonSearchInSavedSearches.setName("buttonSearchInSavedSearches"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panelSavedSearches.add(buttonSearchInSavedSearches, gridBagConstraints);

        tabbedPaneSelection.addTab(bundle.getString("AppPanel.panelSavedSearches.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/org/jphototagger/program/resource/icons/icon_search.png")), panelSavedSearches); // NOI18N

        panelImageCollections.setName("panelImageCollections"); // NOI18N
        panelImageCollections.setLayout(new java.awt.GridBagLayout());

        panelListImageCollectionsFilter.setName("panelListImageCollectionsFilter"); // NOI18N
        panelListImageCollectionsFilter.setLayout(new java.awt.GridBagLayout());

        labelListImageCollectionsFilter.setLabelFor(textFieldListImageCollectionsFilter);
        labelListImageCollectionsFilter.setText(bundle.getString("AppPanel.labelListImageCollectionsFilter.text")); // NOI18N
        labelListImageCollectionsFilter.setName("labelListImageCollectionsFilter"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelListImageCollectionsFilter.add(labelListImageCollectionsFilter, gridBagConstraints);

        textFieldListImageCollectionsFilter.setName("textFieldListImageCollectionsFilter"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelListImageCollectionsFilter.add(textFieldListImageCollectionsFilter, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelImageCollections.add(panelListImageCollectionsFilter, gridBagConstraints);

        scrollPaneImageCollections.setName("scrollPaneImageCollections"); // NOI18N

        listImageCollections.setModel(org.jphototagger.lib.swing.WaitListModel.INSTANCE);
        listImageCollections.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listImageCollections.setCellRenderer(new org.jphototagger.program.module.imagecollections.ImageCollectionsListCellRenderer());
        listImageCollections.setDragEnabled(true);
        listImageCollections.setName("listImageCollections"); // NOI18N
        scrollPaneImageCollections.setViewportView(listImageCollections);
        listImageCollections.setTransferHandler(new org.jphototagger.program.module.imagecollections.ImageCollectionsListTransferHandler());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelImageCollections.add(scrollPaneImageCollections, gridBagConstraints);

        buttonSearchInImageCollections.setAction(new SearchInJxListAction(listImageCollections));
        buttonSearchInImageCollections.setText(bundle.getString("AppPanel.buttonSearchInImageCollections.text")); // NOI18N
        buttonSearchInImageCollections.setMargin(new java.awt.Insets(1, 1, 1, 1));
        buttonSearchInImageCollections.setName("buttonSearchInImageCollections"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panelImageCollections.add(buttonSearchInImageCollections, gridBagConstraints);

        tabbedPaneSelection.addTab(bundle.getString("AppPanel.panelImageCollections.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/org/jphototagger/program/resource/icons/icon_imagecollection.png")), panelImageCollections); // NOI18N

        panelFavorites.setName("panelFavorites"); // NOI18N
        panelFavorites.setLayout(new java.awt.GridBagLayout());

        scrollPaneFavorites.setName("scrollPaneFavorites"); // NOI18N

        treeFavorites.setModel(org.jphototagger.lib.swing.WaitTreeModel.INSTANCE);
        treeFavorites.setCellRenderer(new org.jphototagger.program.module.favorites.FavoritesTreeCellRenderer());
        treeFavorites.setDragEnabled(true);
        treeFavorites.setName("treeFavorites"); // NOI18N
        treeFavorites.setRootVisible(false);
        scrollPaneFavorites.setViewportView(treeFavorites);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelFavorites.add(scrollPaneFavorites, gridBagConstraints);

        buttonSearchInTreeFavorites.setAction(new SearchInJxTreeAction((JXTree)treeFavorites));
        buttonSearchInTreeFavorites.setText(bundle.getString("AppPanel.buttonSearchInTreeFavorites.text")); // NOI18N
        buttonSearchInTreeFavorites.setMargin(new java.awt.Insets(1, 1, 1, 1));
        buttonSearchInTreeFavorites.setName("buttonSearchInTreeFavorites"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panelFavorites.add(buttonSearchInTreeFavorites, gridBagConstraints);

        tabbedPaneSelection.addTab(bundle.getString("AppPanel.panelFavorites.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/org/jphototagger/program/resource/icons/icon_favorite.png")), panelFavorites); // NOI18N

        panelSelKeywords.setName("panelSelKeywords"); // NOI18N
        panelSelKeywords.setLayout(new java.awt.CardLayout());

        panelSelKeywordsTree.setName("panelSelKeywordsTree"); // NOI18N
        panelSelKeywordsTree.setLayout(new java.awt.GridBagLayout());

        scrollPaneSelKeywordsTree.setName("scrollPaneSelKeywordsTree"); // NOI18N

        treeSelKeywords.setModel(org.jphototagger.lib.swing.WaitTreeModel.INSTANCE);
        treeSelKeywords.setCellRenderer(new org.jphototagger.program.module.keywords.tree.KeywordsTreeCellRenderer());
        treeSelKeywords.setName("treeSelKeywords"); // NOI18N
        treeSelKeywords.setRootVisible(false);
        scrollPaneSelKeywordsTree.setViewportView(treeSelKeywords);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelSelKeywordsTree.add(scrollPaneSelKeywordsTree, gridBagConstraints);

        buttonDisplaySelKeywordsList.setText(bundle.getString("AppPanel.buttonDisplaySelKeywordsList.text")); // NOI18N
        buttonDisplaySelKeywordsList.setMargin(new java.awt.Insets(1, 1, 1, 1));
        buttonDisplaySelKeywordsList.setName("buttonDisplaySelKeywordsList"); // NOI18N
        buttonDisplaySelKeywordsList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDisplaySelKeywordsListActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 1, 0);
        panelSelKeywordsTree.add(buttonDisplaySelKeywordsList, gridBagConstraints);

        toggleButtonExpandAllNodesSelKeywords.setText(bundle.getString("AppPanel.toggleButtonExpandAllNodesSelKeywords.text")); // NOI18N
        toggleButtonExpandAllNodesSelKeywords.setMargin(new java.awt.Insets(1, 1, 1, 1));
        toggleButtonExpandAllNodesSelKeywords.setName("toggleButtonExpandAllNodesSelKeywords"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 1, 0);
        panelSelKeywordsTree.add(toggleButtonExpandAllNodesSelKeywords, gridBagConstraints);

        buttonSearchInTreeSelKeywords.setAction(new SearchInJxTreeAction((JXTree)treeSelKeywords));
        buttonSearchInTreeSelKeywords.setText(bundle.getString("AppPanel.buttonSearchInTreeSelKeywords.text")); // NOI18N
        buttonSearchInTreeSelKeywords.setMargin(new java.awt.Insets(1, 1, 1, 1));
        buttonSearchInTreeSelKeywords.setName("buttonSearchInTreeSelKeywords"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 1, 0);
        panelSelKeywordsTree.add(buttonSearchInTreeSelKeywords, gridBagConstraints);

        panelSelKeywords.add(panelSelKeywordsTree, "keywordsTree");

        panelSelKeywordsList.setName("panelSelKeywordsList"); // NOI18N
        panelSelKeywordsList.setLayout(new java.awt.GridBagLayout());

        panelListSelKeywordsFilter.setName("panelListSelKeywordsFilter"); // NOI18N
        panelListSelKeywordsFilter.setLayout(new java.awt.GridBagLayout());

        labelListSelKeywordsFilter.setLabelFor(textFieldListSelKeywordsFilter);
        labelListSelKeywordsFilter.setText(bundle.getString("AppPanel.labelListSelKeywordsFilter.text")); // NOI18N
        labelListSelKeywordsFilter.setName("labelListSelKeywordsFilter"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelListSelKeywordsFilter.add(labelListSelKeywordsFilter, gridBagConstraints);

        textFieldListSelKeywordsFilter.setName("textFieldListSelKeywordsFilter"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelListSelKeywordsFilter.add(textFieldListSelKeywordsFilter, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panelSelKeywordsList.add(panelListSelKeywordsFilter, gridBagConstraints);

        scrollPaneSelKeywordsList.setName("scrollPaneSelKeywordsList"); // NOI18N

        listSelKeywords.setModel(org.jphototagger.lib.swing.WaitListModel.INSTANCE);
        listSelKeywords.setCellRenderer(new org.jphototagger.program.module.keywords.list.KeywordsListCellRenderer());
        listSelKeywords.setDragEnabled(true);
        listSelKeywords.setName("listSelKeywords"); // NOI18N
        scrollPaneSelKeywordsList.setViewportView(listSelKeywords);
        listSelKeywords.setTransferHandler(new org.jphototagger.program.module.keywords.list.KeywordsListTransferHandler());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelSelKeywordsList.add(scrollPaneSelKeywordsList, gridBagConstraints);

        panelSelKeywordsListMultipleSelection.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("AppPanel.panelSelKeywordsListMultipleSelection.border.title"))); // NOI18N
        panelSelKeywordsListMultipleSelection.setName("panelSelKeywordsListMultipleSelection"); // NOI18N
        panelSelKeywordsListMultipleSelection.setLayout(new java.awt.GridBagLayout());

        buttonGroupKeywordsMultipleSel.add(radioButtonSelKeywordsMultipleSelAll);
        radioButtonSelKeywordsMultipleSelAll.setText(bundle.getString("AppPanel.radioButtonSelKeywordsMultipleSelAll.text")); // NOI18N
        radioButtonSelKeywordsMultipleSelAll.setName("radioButtonSelKeywordsMultipleSelAll"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panelSelKeywordsListMultipleSelection.add(radioButtonSelKeywordsMultipleSelAll, gridBagConstraints);

        buttonGroupKeywordsMultipleSel.add(radioButtonSelKeywordsMultipleSelOne);
        radioButtonSelKeywordsMultipleSelOne.setText(bundle.getString("AppPanel.radioButtonSelKeywordsMultipleSelOne.text")); // NOI18N
        radioButtonSelKeywordsMultipleSelOne.setName("radioButtonSelKeywordsMultipleSelOne"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        panelSelKeywordsListMultipleSelection.add(radioButtonSelKeywordsMultipleSelOne, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panelSelKeywordsList.add(panelSelKeywordsListMultipleSelection, gridBagConstraints);

        buttonDisplaySelKeywordsTree.setText(bundle.getString("AppPanel.buttonDisplaySelKeywordsTree.text")); // NOI18N
        buttonDisplaySelKeywordsTree.setMargin(new java.awt.Insets(1, 1, 1, 1));
        buttonDisplaySelKeywordsTree.setName("buttonDisplaySelKeywordsTree"); // NOI18N
        buttonDisplaySelKeywordsTree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDisplaySelKeywordsTreeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panelSelKeywordsList.add(buttonDisplaySelKeywordsTree, gridBagConstraints);

        buttonSearchInListSelKeywords.setAction(new SearchInJxListAction(listSelKeywords));
        buttonSearchInListSelKeywords.setText(bundle.getString("AppPanel.buttonSearchInListSelKeywords.text")); // NOI18N
        buttonSearchInListSelKeywords.setMargin(new java.awt.Insets(1, 1, 1, 1));
        buttonSearchInListSelKeywords.setName("buttonSearchInListSelKeywords"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panelSelKeywordsList.add(buttonSearchInListSelKeywords, gridBagConstraints);

        panelSelKeywords.add(panelSelKeywordsList, "flatKeywords");

        tabbedPaneSelection.addTab(bundle.getString("AppPanel.panelSelKeywords.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/org/jphototagger/program/resource/icons/icon_keyword.png")), panelSelKeywords); // NOI18N

        panelTimeline.setName("panelTimeline"); // NOI18N
        panelTimeline.setLayout(new java.awt.GridBagLayout());

        scrollPaneTimeline.setName("scrollPaneTimeline"); // NOI18N

        treeTimeline.setModel(org.jphototagger.lib.swing.WaitTreeModel.INSTANCE);
        treeTimeline.setCellRenderer(new org.jphototagger.program.module.timeline.TimelineTreeCellRenderer());
        treeTimeline.setName("treeTimeline"); // NOI18N
        treeTimeline.setRootVisible(false);
        scrollPaneTimeline.setViewportView(treeTimeline);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelTimeline.add(scrollPaneTimeline, gridBagConstraints);

        toggleButtonExpandCollapseTreeTimeline.setAction(new TreeExpandCollapseAllAction(toggleButtonExpandCollapseTreeTimeline, treeTimeline));
        toggleButtonExpandCollapseTreeTimeline.setText(TreeExpandCollapseAllAction.NOT_SELECTED_TEXT);
        toggleButtonExpandCollapseTreeTimeline.setMargin(new java.awt.Insets(1, 1, 1, 1));
        toggleButtonExpandCollapseTreeTimeline.setName("toggleButtonExpandCollapseTreeTimeline"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 0);
        panelTimeline.add(toggleButtonExpandCollapseTreeTimeline, gridBagConstraints);

        buttonSearchInTreeTimeline.setAction(new SearchInJxTreeAction((JXTree)treeTimeline));
        buttonSearchInTreeTimeline.setText(bundle.getString("AppPanel.buttonSearchInTreeTimeline.text")); // NOI18N
        buttonSearchInTreeTimeline.setMargin(new java.awt.Insets(1, 1, 1, 1));
        buttonSearchInTreeTimeline.setName("buttonSearchInTreeTimeline"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 0);
        panelTimeline.add(buttonSearchInTreeTimeline, gridBagConstraints);

        tabbedPaneSelection.addTab(bundle.getString("AppPanel.panelTimeline.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/org/jphototagger/program/resource/icons/icon_timeline.png")), panelTimeline); // NOI18N

        panelMiscMetadata.setName("panelMiscMetadata"); // NOI18N
        panelMiscMetadata.setLayout(new java.awt.GridBagLayout());

        scrollPaneMiscMetadata.setName("scrollPaneMiscMetadata"); // NOI18N

        treeMiscMetadata.setModel(org.jphototagger.lib.swing.WaitTreeModel.INSTANCE);
        treeMiscMetadata.setCellRenderer(new org.jphototagger.program.module.miscmetadata.MiscMetadataTreeCellRenderer());
        treeMiscMetadata.setDragEnabled(true);
        treeMiscMetadata.setName("treeMiscMetadata"); // NOI18N
        treeMiscMetadata.setRootVisible(false);
        scrollPaneMiscMetadata.setViewportView(treeMiscMetadata);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelMiscMetadata.add(scrollPaneMiscMetadata, gridBagConstraints);

        toggleButtonExpandCollapseTreeMiscMetadata.setAction(new TreeExpandCollapseAllAction(toggleButtonExpandCollapseTreeMiscMetadata, treeMiscMetadata));
        toggleButtonExpandCollapseTreeMiscMetadata.setText(TreeExpandCollapseAllAction.NOT_SELECTED_TEXT);
        toggleButtonExpandCollapseTreeMiscMetadata.setMargin(new java.awt.Insets(1, 1, 1, 1));
        toggleButtonExpandCollapseTreeMiscMetadata.setName("toggleButtonExpandCollapseTreeMiscMetadata"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 0);
        panelMiscMetadata.add(toggleButtonExpandCollapseTreeMiscMetadata, gridBagConstraints);

        buttonSearchInTreeMiscMetadata.setAction(new SearchInJxTreeAction((JXTree)treeMiscMetadata));
        buttonSearchInTreeMiscMetadata.setText(bundle.getString("AppPanel.buttonSearchInTreeMiscMetadata.text")); // NOI18N
        buttonSearchInTreeMiscMetadata.setMargin(new java.awt.Insets(1, 1, 1, 1));
        buttonSearchInTreeMiscMetadata.setName("buttonSearchInTreeMiscMetadata"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 0);
        panelMiscMetadata.add(buttonSearchInTreeMiscMetadata, gridBagConstraints);

        tabbedPaneSelection.addTab(bundle.getString("AppPanel.panelMiscMetadata.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/org/jphototagger/program/resource/icons/icon_misc_metadata.png")), panelMiscMetadata); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        panelSelection.add(tabbedPaneSelection, gridBagConstraints);

        splitPaneMain.setLeftComponent(panelSelection);

        panelThumbnailsMetadata.setName("panelThumbnailsMetadata"); // NOI18N

        splitPaneThumbnailsMetadata.setDividerSize(6);
        splitPaneThumbnailsMetadata.setResizeWeight(1.0);
        splitPaneThumbnailsMetadata.setName("splitPaneThumbnailsMetadata"); // NOI18N
        splitPaneThumbnailsMetadata.setOneTouchExpandable(true);

        thumbnailPanelComponent.setMinimumSize(new java.awt.Dimension(180, 0));
        thumbnailPanelComponent.setName("thumbnailPanelComponent"); // NOI18N
        thumbnailPanelComponent.setLayout(new java.awt.GridBagLayout());
        splitPaneThumbnailsMetadata.setLeftComponent(thumbnailPanelComponent);

        panelMetadata.setName("panelMetadata"); // NOI18N
        panelMetadata.setLayout(new java.awt.GridBagLayout());

        tabbedPaneMetadata.setName("tabbedPaneMetadata"); // NOI18N
        tabbedPaneMetadata.setOpaque(true);

        panelEditKeywords.setName("panelEditKeywords"); // NOI18N
        tabbedPaneMetadata.addTab(bundle.getString("AppPanel.panelEditKeywords.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/org/jphototagger/program/resource/icons/icon_keyword.png")), panelEditKeywords); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelMetadata.add(tabbedPaneMetadata, gridBagConstraints);

        splitPaneThumbnailsMetadata.setRightComponent(panelMetadata);

        javax.swing.GroupLayout panelThumbnailsMetadataLayout = new javax.swing.GroupLayout(panelThumbnailsMetadata);
        panelThumbnailsMetadata.setLayout(panelThumbnailsMetadataLayout);
        panelThumbnailsMetadataLayout.setHorizontalGroup(
            panelThumbnailsMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 557, Short.MAX_VALUE)
            .addGroup(panelThumbnailsMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(splitPaneThumbnailsMetadata, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE))
        );
        panelThumbnailsMetadataLayout.setVerticalGroup(
            panelThumbnailsMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 487, Short.MAX_VALUE)
            .addGroup(panelThumbnailsMetadataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(splitPaneThumbnailsMetadata, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE))
        );

        splitPaneMain.setRightComponent(panelThumbnailsMetadata);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(splitPaneMain, gridBagConstraints);

        panelStatusbar.setName("panelStatusbar"); // NOI18N
        panelStatusbar.setLayout(new java.awt.GridBagLayout());

        labelStatusbarText.setName("labelStatusbarText"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panelStatusbar.add(labelStatusbarText, gridBagConstraints);

        statusLineElementsPanel.setName("statusLineElementsPanel"); // NOI18N
        statusLineElementsPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panelStatusbar.add(statusLineElementsPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 3, 5);
        add(panelStatusbar, gridBagConstraints);
    }//GEN-END:initComponents

    private void buttonDisplaySelKeywordsListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDisplaySelKeywordsListActionPerformed
        displaySelKeywordsCard("flatKeywords");
    }//GEN-LAST:event_buttonDisplaySelKeywordsListActionPerformed

    private void buttonDisplaySelKeywordsTreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDisplaySelKeywordsTreeActionPerformed
        displaySelKeywordsCard("keywordsTree");
    }//GEN-LAST:event_buttonDisplaySelKeywordsTreeActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonDisplaySelKeywordsList;
    private javax.swing.JButton buttonDisplaySelKeywordsTree;
    private javax.swing.ButtonGroup buttonGroupKeywordsMultipleSel;
    private javax.swing.JButton buttonSearchInDirectories;
    private javax.swing.JButton buttonSearchInImageCollections;
    private javax.swing.JButton buttonSearchInListSelKeywords;
    private javax.swing.JButton buttonSearchInSavedSearches;
    private javax.swing.JButton buttonSearchInTreeFavorites;
    private javax.swing.JButton buttonSearchInTreeMiscMetadata;
    private javax.swing.JButton buttonSearchInTreeSelKeywords;
    private javax.swing.JButton buttonSearchInTreeTimeline;
    private javax.swing.JLabel labelListImageCollectionsFilter;
    private javax.swing.JLabel labelListSavedSearchesFilter;
    private javax.swing.JLabel labelListSelKeywordsFilter;
    private javax.swing.JLabel labelStatusbarText;
    private org.jdesktop.swingx.JXList listImageCollections;
    private org.jdesktop.swingx.JXList listSavedSearches;
    private org.jdesktop.swingx.JXList listSelKeywords;
    private javax.swing.JPanel panelDirectories;
    private org.jphototagger.program.module.keywords.KeywordsPanel panelEditKeywords;
    private javax.swing.JPanel panelFavorites;
    private javax.swing.JPanel panelImageCollections;
    private javax.swing.JPanel panelListImageCollectionsFilter;
    private javax.swing.JPanel panelListSavedSearchesFilter;
    private javax.swing.JPanel panelListSelKeywordsFilter;
    private javax.swing.JPanel panelMetadata;
    private javax.swing.JPanel panelMiscMetadata;
    private javax.swing.JPanel panelSavedSearches;
    private javax.swing.JPanel panelSearch;
    private javax.swing.JPanel panelSelKeywords;
    private javax.swing.JPanel panelSelKeywordsList;
    private javax.swing.JPanel panelSelKeywordsListMultipleSelection;
    private javax.swing.JPanel panelSelKeywordsTree;
    private javax.swing.JPanel panelSelection;
    private javax.swing.JPanel panelStatusbar;
    private javax.swing.JPanel panelThumbnailsMetadata;
    private javax.swing.JPanel panelTimeline;
    private javax.swing.JRadioButton radioButtonSelKeywordsMultipleSelAll;
    private javax.swing.JRadioButton radioButtonSelKeywordsMultipleSelOne;
    private javax.swing.JScrollPane scrollPaneDirectories;
    private javax.swing.JScrollPane scrollPaneFavorites;
    private javax.swing.JScrollPane scrollPaneImageCollections;
    private javax.swing.JScrollPane scrollPaneMiscMetadata;
    private javax.swing.JScrollPane scrollPaneSavedSearches;
    private javax.swing.JScrollPane scrollPaneSelKeywordsList;
    private javax.swing.JScrollPane scrollPaneSelKeywordsTree;
    private javax.swing.JScrollPane scrollPaneTimeline;
    private javax.swing.JSplitPane splitPaneMain;
    private javax.swing.JSplitPane splitPaneThumbnailsMetadata;
    private javax.swing.JPanel statusLineElementsPanel;
    private javax.swing.JTabbedPane tabbedPaneMetadata;
    private javax.swing.JTabbedPane tabbedPaneSelection;
    private javax.swing.JTextField textFieldListImageCollectionsFilter;
    private javax.swing.JTextField textFieldListSavedSearchesFilter;
    private javax.swing.JTextField textFieldListSelKeywordsFilter;
    private javax.swing.JPanel thumbnailPanelComponent;
    private javax.swing.JToggleButton toggleButtonExpandAllNodesSelKeywords;
    private javax.swing.JToggleButton toggleButtonExpandCollapseTreeMiscMetadata;
    private javax.swing.JToggleButton toggleButtonExpandCollapseTreeTimeline;
    private javax.swing.JTree treeDirectories;
    private javax.swing.JTree treeFavorites;
    private javax.swing.JTree treeMiscMetadata;
    private javax.swing.JTree treeSelKeywords;
    private javax.swing.JTree treeTimeline;
    // End of variables declaration//GEN-END:variables
}
