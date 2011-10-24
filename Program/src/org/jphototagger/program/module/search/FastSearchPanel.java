package org.jphototagger.program.module.search;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

import org.jphototagger.api.preferences.Preferences;
import org.jphototagger.api.preferences.PreferencesChangedEvent;
import org.jphototagger.api.windows.MainWindowManager;
import org.jphototagger.api.windows.WaitDisplayer;
import org.jphototagger.domain.DomainPreferencesKeys;
import org.jphototagger.domain.metadata.MetaDataValue;
import org.jphototagger.domain.metadata.search.SearchComponent;
import org.jphototagger.domain.metadata.selections.AutoCompleteDataOfMetaDataValue;
import org.jphototagger.domain.metadata.selections.FastSearchMetaDataValues;
import org.jphototagger.domain.metadata.xmp.Xmp;
import org.jphototagger.domain.metadata.xmp.XmpDcSubjectsSubjectMetaDataValue;
import org.jphototagger.domain.repository.FindRepository;
import org.jphototagger.domain.repository.ImageFilesRepository;
import org.jphototagger.domain.repository.event.xmp.XmpDeletedEvent;
import org.jphototagger.domain.repository.event.xmp.XmpInsertedEvent;
import org.jphototagger.domain.repository.event.xmp.XmpUpdatedEvent;
import org.jphototagger.domain.thumbnails.OriginOfDisplayedThumbnails;
import org.jphototagger.domain.thumbnails.ThumbnailsDisplayer;
import org.jphototagger.domain.thumbnails.event.ThumbnailsChangedEvent;
import org.jphototagger.domain.thumbnails.event.ThumbnailsPanelRefreshEvent;
import org.jphototagger.lib.awt.EventQueueUtil;
import org.jphototagger.lib.swing.IconUtil;
import org.jphototagger.lib.swing.ImageTextArea;
import org.jphototagger.lib.swing.KeyEventUtil;
import org.jphototagger.lib.swing.util.Autocomplete;
import org.jphototagger.lib.swing.util.ComponentUtil;
import org.jphototagger.lib.swing.util.ListUtil;
import org.jphototagger.lib.swing.util.MnemonicUtil;
import org.jphototagger.lib.swing.util.TreeUtil;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.program.app.ui.AppLookAndFeel;
import org.jphototagger.program.misc.AutocompleteUtil;
import org.jphototagger.program.resource.GUI;
import org.jphototagger.program.settings.AppPreferencesKeys;

/**
 * @author Elmar Baumann
 */
@ServiceProvider(service = SearchComponent.class)
public class FastSearchPanel extends javax.swing.JPanel implements ActionListener, SearchComponent {

    private static final long serialVersionUID = 1L;
    private static final String DELIMITER_SEARCH_WORDS = ";";
    private final Autocomplete autocomplete;
    private boolean isAutocomplete;
    private final FindRepository findRepo = Lookup.getDefault().lookup(FindRepository.class);
    private final SelectSearchTextAreaAction selectSearchTextAreaAction = new SelectSearchTextAreaAction();

    public FastSearchPanel() {
        initComponents();
        if (getPersistedAutocomplete()) {
            autocomplete = new Autocomplete(isAutocompleteFastSearchIgnoreCase());
            autocomplete.setTransferFocusForward(false);
        } else {
            autocomplete = null;
            isAutocomplete = false;
        }
        postInitComponents();
    }

    private void postInitComponents() {
        setAutocomplete(true);
        MnemonicUtil.setMnemonics(this);
        toggleDisplaySearchButton();
        listen();
    }

    private boolean isAutocompleteFastSearchIgnoreCase() {
        Preferences storage = Lookup.getDefault().lookup(Preferences.class);

        return storage.containsKey(DomainPreferencesKeys.KEY_AUTOCOMPLETE_FAST_SEARCH_IGNORE_CASE)
                ? storage.getBoolean(DomainPreferencesKeys.KEY_AUTOCOMPLETE_FAST_SEARCH_IGNORE_CASE)
                : false;
    }

    private boolean getPersistedAutocomplete() {
        Preferences storage = Lookup.getDefault().lookup(Preferences.class);

        return storage.containsKey(DomainPreferencesKeys.KEY_ENABLE_AUTOCOMPLETE)
                ? storage.getBoolean(DomainPreferencesKeys.KEY_ENABLE_AUTOCOMPLETE)
                : true;
    }

    private void listen() {
        AnnotationProcessor.process(this);
        searchTextArea.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    search();
                }
            }
        });
        searchButton.addActionListener(this);
        fastSearchComboBox.addActionListener(this);
        AnnotationProcessor.process(this);
    }

    public void setAutocomplete(boolean ac) {
        if (autocomplete == null) {
            return;
        }

        isAutocomplete = ac;

        if (ac) {
            decorateTextFieldSearch();
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (isAutocomplete && (evt.getSource() == fastSearchComboBox) && (fastSearchComboBox.getSelectedIndex() >= 0)) {
            decorateTextFieldSearch();
        } else if (evt.getSource() == searchButton) {
            search();
        }
    }

    private void decorateTextFieldSearch() {
        if (autocomplete == null || !getPersistedAutocomplete()) {
            return;
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                autocomplete.decorate(searchTextArea, isSearchAllDefinedMetaDataValues()
                        ? AutoCompleteDataOfMetaDataValue.INSTANCE.getFastSearchData().get()
                        : AutoCompleteDataOfMetaDataValue.INSTANCE.get(getSearchMetaDataValue()).get(), true);
            }
        }, "JPhotoTagger: Updating autocomplete for search text field").start();
    }

    private void clearSelection() {
        TreeUtil.clearSelection(GUI.getAppPanel().getSelectionTrees());
        ListUtil.clearSelection(GUI.getAppPanel().getSelectionLists());
    }

    private void search() {
        search(searchTextArea.getText());
    }

    private void search(final String searchText) {
        EventQueueUtil.invokeInDispatchThread(new Runnable() {

            private final ImageFilesRepository imageFileRepo = Lookup.getDefault().lookup(ImageFilesRepository.class);

            @Override
            public void run() {
                String userInput = searchText.trim();

                if (!userInput.isEmpty()) {
                    WaitDisplayer waitDisplayer = Lookup.getDefault().lookup(WaitDisplayer.class);
                    waitDisplayer.show();
                    clearSelection();

                    List<File> imageFiles = searchFiles(userInput);

                    if (imageFiles != null) {
                        setTitle(userInput);
                        ThumbnailsDisplayer tDisplayer = Lookup.getDefault().lookup(ThumbnailsDisplayer.class);
                        tDisplayer.displayFiles(imageFiles, OriginOfDisplayedThumbnails.FILES_FOUND_BY_FAST_SEARCH);
                    }
                    waitDisplayer.hide();
                }
            }

            private void setTitle(String userInput) {
                String title = Bundle.getString(FastSearchPanel.class, "FastSearchController.AppFrame.Title.FastSearch", userInput);
                MainWindowManager mainWindowManager = Lookup.getDefault().lookup(MainWindowManager.class);
                mainWindowManager.setMainWindowTitle(title);
            }

            private List<File> searchFiles(String userInput) {
                if (isSearchAllDefinedMetaDataValues()) {
                    return findRepo.findImageFilesLikeOr(FastSearchMetaDataValues.get(), userInput);
                } else {
                    List<String> searchWords = getSearchWords(userInput);
                    MetaDataValue searchValue = getSearchMetaDataValue();

                    if (searchValue == null) {
                        return null;
                    }

                    boolean isKeywordSearch = searchValue.equals(XmpDcSubjectsSubjectMetaDataValue.INSTANCE);

                    if (searchWords.size() == 1) {
                        if (isKeywordSearch) {
                            return new ArrayList<File>(imageFileRepo.findImageFilesContainingDcSubject(searchWords.get(0), true));
                        } else {
                            return findRepo.findImageFilesLikeOr(Arrays.asList(searchValue), userInput);
                        }
                    } else if (searchWords.size() > 1) {
                        if (isKeywordSearch) {
                            return new ArrayList<File>(imageFileRepo.findImageFilesContainingAllDcSubjects(searchWords));
                        } else {
                            return new ArrayList<File>(imageFileRepo.findImageFilesContainingAllWordsInMetaDataValue(searchWords, searchValue));
                        }
                    } else {
                        return null;
                    }
                }
            }
        });
    }

    private List<String> getSearchWords(String userInput) {
        List<String> words = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(userInput, DELIMITER_SEARCH_WORDS);

        while (st.hasMoreTokens()) {
            words.add(st.nextToken().trim());
        }

        return words;
    }

    private MetaDataValue getSearchMetaDataValue() {
        assert !isSearchAllDefinedMetaDataValues() : "More than one search value!";

        if (isSearchAllDefinedMetaDataValues()) {
            return null;
        }

        return (MetaDataValue) fastSearchComboBox.getSelectedItem();
    }

    @EventSubscriber(eventClass = ThumbnailsPanelRefreshEvent.class)
    public void refresh(ThumbnailsPanelRefreshEvent evt) {
        if (searchTextArea.isEnabled()) {
            OriginOfDisplayedThumbnails typeOfDisplayedImages = evt.getTypeOfDisplayedImages();

            if (OriginOfDisplayedThumbnails.FILES_FOUND_BY_FAST_SEARCH.equals(typeOfDisplayedImages)) {
                search(searchTextArea.getText());
            }
        }
    }

    @EventSubscriber(eventClass = ThumbnailsChangedEvent.class)
    public void thumbnailsChanged(ThumbnailsChangedEvent evt) {
        OriginOfDisplayedThumbnails originOfDisplayedThumbnails = evt.getOriginOfDisplayedThumbnails();
        if (!OriginOfDisplayedThumbnails.FILES_FOUND_BY_FAST_SEARCH.equals(originOfDisplayedThumbnails)) {
            searchTextArea.setText("");
        }
    }

    private boolean isSearchAllDefinedMetaDataValues() {
        Object selItem = fastSearchComboBox.getSelectedItem();

        return (selItem != null) && selItem.equals(FastSearchComboBoxModel.ALL_DEFINED_META_DATA_VALUES);
    }

    private void addAutocompleteWordsOf(Xmp xmp) {
        if (!getPersistedAutocomplete()) {
            return;
        }

        if (isSearchAllDefinedMetaDataValues()) {
            AutocompleteUtil.addFastSearchAutocompleteData(autocomplete, xmp);
        } else {
            AutocompleteUtil.addAutocompleteData(getSearchMetaDataValue(), autocomplete, xmp);
        }
    }

    private boolean isAutocomplete() {
        return autocomplete != null && getPersistedAutocomplete();
    }

    @EventSubscriber(eventClass = XmpInsertedEvent.class)
    public void xmpInserted(XmpInsertedEvent evt) {
        addAutocompleteWordsOf(evt.getXmp());
    }

    @EventSubscriber(eventClass = XmpDeletedEvent.class)
    public void xmpDeleted(XmpDeletedEvent evt) {
        addAutocompleteWordsOf(evt.getXmp());
    }

    @EventSubscriber(eventClass = XmpUpdatedEvent.class)
    public void xmpUpdated(XmpUpdatedEvent evt) {
        addAutocompleteWordsOf(evt.getUpdatedXmp());
    }

    private boolean isDisplaySearchButton() {
        Preferences storage = Lookup.getDefault().lookup(Preferences.class);

        return storage.containsKey(AppPreferencesKeys.KEY_UI_DISPLAY_SEARCH_BUTTON)
                ? storage.getBoolean(AppPreferencesKeys.KEY_UI_DISPLAY_SEARCH_BUTTON)
                : true;
    }

    @EventSubscriber(eventClass = PreferencesChangedEvent.class)
    public void userPropertyChanged(PreferencesChangedEvent evt) {
        String key = evt.getKey();

        if (AppPreferencesKeys.KEY_UI_DISPLAY_SEARCH_BUTTON.equals(key)) {
            toggleDisplaySearchButton();
        }
    }

    private void toggleDisplaySearchButton() {
        int zOrder = getComponentZOrder(searchButton);
        boolean containsButton = zOrder >= 0;
        boolean displaySearchButton = isDisplaySearchButton();
        boolean toDo = containsButton && !displaySearchButton || !containsButton && displaySearchButton;

        if (!toDo) {
            return;
        }

        if (displaySearchButton) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.fill = java.awt.GridBagConstraints.BOTH;
            gbc.weighty = 1.0;
            gbc.insets = new java.awt.Insets(3, 3, 0, 0);
            add(searchButton, gbc);
        } else {
            remove(searchButton);
        }

        ComponentUtil.forceRepaint(this);
    }

    @Override
    public Action getSelectSearchComponentAction() {
        return selectSearchTextAreaAction;
    }

    @Override
    public Component getSearchComponent() {
        return this;
    }

    private class SelectSearchTextAreaAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        private SelectSearchTextAreaAction() {
            super(Bundle.getString(SelectSearchTextAreaAction.class, "SelectSearchTextAreaAction.Name"));
            putValue(SMALL_ICON, IconUtil.getImageIcon(SelectSearchTextAreaAction.class, "search.png"));
            putValue(ACCELERATOR_KEY, KeyEventUtil.getKeyStrokeMenuShortcut(KeyEvent.VK_F));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            searchTextArea.requestFocusInWindow();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        fastSearchComboBox = new javax.swing.JComboBox();
        searchButton = new javax.swing.JButton();
        searchTextAreaScrollPane = new javax.swing.JScrollPane();
        searchTextArea = new ImageTextArea();
        ((ImageTextArea) searchTextArea).setImage(
            AppLookAndFeel.getLocalizedImage(
                "/org/jphototagger/program/resource/images/textfield_search.png"));
        ((ImageTextArea) searchTextArea).setConsumeEnter(true);

        setName("Form"); // NOI18N
        setLayout(new java.awt.GridBagLayout());

        fastSearchComboBox.setModel(new org.jphototagger.program.module.search.FastSearchComboBoxModel());
        fastSearchComboBox.setName("fastSearchComboBox"); // NOI18N
        fastSearchComboBox.setRenderer(new org.jphototagger.program.module.search.FastSearchMetaDataValuesListCellRenderer());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(fastSearchComboBox, gridBagConstraints);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/program/module/search/Bundle"); // NOI18N
        searchButton.setText(bundle.getString("FastSearchPanel.searchButton.text")); // NOI18N
        searchButton.setMargin(new java.awt.Insets(1, 1, 1, 1));
        searchButton.setName("searchButton"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(searchButton, gridBagConstraints);

        searchTextAreaScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        searchTextAreaScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        searchTextAreaScrollPane.setMinimumSize(new java.awt.Dimension(7, 20));
        searchTextAreaScrollPane.setName("searchTextAreaScrollPane"); // NOI18N

        searchTextArea.setRows(1);
        searchTextArea.setMinimumSize(new java.awt.Dimension(0, 18));
        searchTextArea.setName("searchTextArea"); // NOI18N
        searchTextAreaScrollPane.setViewportView(searchTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(searchTextAreaScrollPane, gridBagConstraints);
    }//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox fastSearchComboBox;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextArea searchTextArea;
    private javax.swing.JScrollPane searchTextAreaScrollPane;
    // End of variables declaration//GEN-END:variables
}
