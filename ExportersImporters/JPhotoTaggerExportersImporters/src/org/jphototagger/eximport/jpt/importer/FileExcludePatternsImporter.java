package org.jphototagger.eximport.jpt.importer;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.filechooser.FileFilter;
import org.jphototagger.domain.repository.FileExcludePatternsRepository;
import org.jphototagger.domain.repository.RepositoryDataImporter;
import org.jphototagger.eximport.jpt.exporter.FileExcludePatternsExporter;
import org.jphototagger.eximport.jpt.exporter.FileExcludePatternsExporter.CollectionWrapper;
import org.jphototagger.lib.xml.bind.StringWrapper;
import org.jphototagger.lib.xml.bind.XmlObjectImporter;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Elmar Baumann
 */
@ServiceProvider(service = RepositoryDataImporter.class)
public final class FileExcludePatternsImporter implements RepositoryDataImporter {

    private final FileExcludePatternsRepository repo = Lookup.getDefault().lookup(FileExcludePatternsRepository.class);

    @Override
    public void importFromFile(File file) {
        if (file == null) {
            throw new NullPointerException("file == null");
        }

        try {
            FileExcludePatternsExporter.CollectionWrapper wrapper =
                    (CollectionWrapper) XmlObjectImporter.importObject(file,
                    FileExcludePatternsExporter.CollectionWrapper.class);

            for (StringWrapper stringWrapper : wrapper.getCollection()) {
                if (!repo.existsFileExcludePattern(stringWrapper.getString())) {
                    repo.saveFileExcludePattern(stringWrapper.getString());
                }
            }
        } catch (Throwable t) {
            Logger.getLogger(FileExcludePatternsImporter.class.getName()).log(Level.SEVERE, null, t);
        }
    }

    @Override
    public FileFilter getFileFilter() {
        return FileExcludePatternsExporter.FILE_FILTER;
    }

    @Override
    public String getDisplayName() {
        return FileExcludePatternsExporter.DISPLAY_NAME;
    }

    @Override
    public Icon getIcon() {
        return ImportPreferences.ICON;
    }

    @Override
    public String getDefaultFilename() {
        return FileExcludePatternsExporter.DEFAULT_FILENAME;
    }

    @Override
    public int getPosition() {
        return FileExcludePatternsExporter.POSITION;
    }

    @Override
    public boolean isJPhotoTaggerData() {
        return true;
    }
}
