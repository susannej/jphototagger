package org.jphototagger.program.helper;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bushe.swing.event.EventBus;

import org.openide.util.Lookup;

import org.jphototagger.api.concurrent.Cancelable;
import org.jphototagger.api.progress.ProgressEvent;
import org.jphototagger.api.progress.ProgressListener;
import org.jphototagger.api.preferences.Preferences;
import org.jphototagger.domain.event.listener.ProgressListenerSupport;
import org.jphototagger.domain.metadata.exif.Exif;
import org.jphototagger.domain.image.ImageFile;
import org.jphototagger.domain.metadata.event.UpdateMetadataCheckEvent;
import org.jphototagger.domain.metadata.event.UpdateMetadataCheckEvent.Type;
import org.jphototagger.domain.metadata.xmp.XmpIptc4XmpCoreDateCreatedMetaDataValue;
import org.jphototagger.domain.metadata.xmp.XmpLastModifiedMetaDataValue;
import org.jphototagger.domain.programs.Program;
import org.jphototagger.domain.repository.ActionsAfterRepoUpdatesRepository;
import org.jphototagger.domain.repository.ImageFilesRepository;
import org.jphototagger.domain.repository.InsertIntoRepository;
import org.jphototagger.domain.repository.ThumbnailsRepository;
import org.jphototagger.domain.metadata.xmp.Xmp;
import org.jphototagger.exif.ExifMetadata;
import org.jphototagger.exif.cache.ExifCache;
import org.jphototagger.image.util.ThumbnailCreatorService;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.program.app.AppFileFilters;
import org.jphototagger.program.app.AppLookAndFeel;
import org.jphototagger.program.app.AppPreferencesKeys;
import org.jphototagger.xmp.XmpMetadata;

/**
 * Inserts or updates image file metadata - EXIF, thumbnail, XMP - into the
 * repository.
 *
 * @author Elmar Baumann
 */
public final class InsertImageFilesIntoRepository extends Thread implements Cancelable {

    private final ImageFilesRepository imageFileRepo = Lookup.getDefault().lookup(ImageFilesRepository.class);
    private final ActionsAfterRepoUpdatesRepository actionsAfterRepoUpdatesRepo = Lookup.getDefault().lookup(ActionsAfterRepoUpdatesRepository.class);
    private final ProgressListenerSupport pls = new ProgressListenerSupport();
    private ProgressEvent progressEvent = new ProgressEvent.Builder().source(this).build();
    private final Set<InsertIntoRepository> what = new HashSet<InsertIntoRepository>();
    private final List<File> imageFiles;
    private boolean cancel;
    private static final Logger LOGGER = Logger.getLogger(InsertImageFilesIntoRepository.class.getName());
    private final ThumbnailsRepository tnRepo = Lookup.getDefault().lookup(ThumbnailsRepository.class);

    /**
     * Constructor.
     *
     * @param imageFiles image files, whoes metadatada shall be inserted or
     *                   updated
     * @param what       metadata to saveAction
     */
    public InsertImageFilesIntoRepository(List<File> imageFiles, InsertIntoRepository... what) {
        super("JPhotoTagger: Inserting image files into repository");

        if (imageFiles == null) {
            throw new NullPointerException("imageFiles == null");
        }

        if (what == null) {
            throw new NullPointerException("what == null");
        }

        this.imageFiles = new ArrayList<File>(imageFiles);
        this.what.addAll(Arrays.asList(what));
    }

    @Override
    public void run() {
        int count = imageFiles.size();
        int index = 0;

        notifyStarted();

        for (index = 0; !cancel && !isInterrupted() && (index < count); index++) {
            File imgFile = imageFiles.get(index);

            // Notify before inserting to enable progress listeners displaying
            // the current image file
            notifyPerformed(index + 1, imgFile);

            if (checkExists(imgFile)) {
                deleteFromRepositoryXmpIfAbsentInFilesystem(imgFile);
                ImageFile imageFile = createImageFile(imgFile);

                if (isUpdate(imageFile)) {
                    setExifDateToXmpDateCreated(imageFile);
                    logInsertImageFile(imageFile);
                    imageFileRepo.saveOrUpdateImageFile(imageFile);
                    runActionsAfterInserting(imageFile);
                }
            }
        }

        notifyEnded(index);
    }

    private boolean isUpdate(ImageFile imageFile) {
        return (imageFile.getExif() != null) || (imageFile.getXmp() != null) || (imageFile.getThumbnail() != null);
    }

    private ImageFile createImageFile(File imgFile) {
        ImageFile imageFile = new ImageFile();

        imageFile.setFile(imgFile);
        imageFile.setLastmodified(imgFile.lastModified());

        if (isUpdateThumbnail(imgFile)) {
            imageFile.addToSaveIntoRepository(InsertIntoRepository.THUMBNAIL);
            createAndSetThumbnail(imageFile);
        }

        if (isUpdateXmp(imgFile)) {
            imageFile.addToSaveIntoRepository(InsertIntoRepository.XMP);
            setXmp(imageFile);
        }

        if (isUpdateExif(imgFile)) {
            imageFile.addToSaveIntoRepository(InsertIntoRepository.EXIF);
            setExif(imageFile);
        }

        return imageFile;
    }

    private boolean isUpdateThumbnail(File imageFile) {
        return what.contains(InsertIntoRepository.THUMBNAIL)
                || (what.contains(InsertIntoRepository.OUT_OF_DATE)
                && (!existsThumbnail(imageFile) || !isThumbnailUpToDate(imageFile)));
    }

    private boolean existsThumbnail(File imageFile) {
        return tnRepo.existsThumbnail(imageFile);
    }

    private boolean isUpdateExif(File imageFile) {
        return what.contains(InsertIntoRepository.EXIF)
                || (what.contains(InsertIntoRepository.OUT_OF_DATE) && !isImageFileUpToDate(imageFile));
    }

    private boolean isUpdateXmp(File imageFile) {
        return what.contains(InsertIntoRepository.XMP)
                || (what.contains(InsertIntoRepository.OUT_OF_DATE) && !isXmpUpToDate(imageFile));
    }

    private boolean isImageFileUpToDate(File imageFile) {
        long dbTime = imageFileRepo.findImageFilesLastModifiedTimestamp(imageFile);
        long fileTime = imageFile.lastModified();

        return fileTime == dbTime;
    }

    private boolean isThumbnailUpToDate(File imageFile) {
        File tnFile = tnRepo.findThumbnailFile(imageFile);

        if ((tnFile == null) || !tnFile.exists()) {
            return false;
        }

        long lastModifiedTn = tnFile.lastModified();
        long lastModifiedImg = imageFile.lastModified();

        return lastModifiedTn >= lastModifiedImg;
    }

    private boolean isXmpUpToDate(File imageFile) {
        File xmpFile = XmpMetadata.getSidecarFile(imageFile);

        return (xmpFile == null)
                ? isScanForEmbeddedXmp() && isEmbeddedXmpUpToDate(imageFile)
                : isXmpSidecarFileUpToDate(imageFile, xmpFile);
    }

    private boolean isScanForEmbeddedXmp() {
        Preferences storage = Lookup.getDefault().lookup(Preferences.class);

        return storage.containsKey(AppPreferencesKeys.KEY_SCAN_FOR_EMBEDDED_XMP)
                ? storage.getBoolean(AppPreferencesKeys.KEY_SCAN_FOR_EMBEDDED_XMP)
                : false;
    }

    private boolean isXmpSidecarFileUpToDate(File imageFile, File sidecarFile) {
        long dbTime = imageFileRepo.findXmpFilesLastModifiedTimestamp(imageFile);
        long fileTime = sidecarFile.lastModified();

        return fileTime == dbTime;
    }

    private boolean isEmbeddedXmpUpToDate(File imageFile) {
        long dbTime = imageFileRepo.findXmpFilesLastModifiedTimestamp(imageFile);
        long fileTime = imageFile.lastModified();

        if (dbTime == fileTime) {
            return true;
        }

        // slow if large image file whitout XMP
        boolean hasEmbeddedXmp = XmpMetadata.getEmbeddedXmp(imageFile) != null;

        if (!hasEmbeddedXmp) {    // Avoid unneccesary 2nd calls
            imageFileRepo.setLastModifiedToXmpSidecarFileOfImageFile(imageFile, fileTime);
        }

        return !hasEmbeddedXmp;
    }

    private void createAndSetThumbnail(ImageFile imageFile) {
        File file = imageFile.getFile();
        Image thumbnail = ThumbnailCreatorService.INSTANCE.createScaledOrFromEmbeddedThumbnail(file);

        imageFile.setThumbnail(thumbnail);

        if (thumbnail == null) {
            logErrorNullThumbnail(file);
            imageFile.setThumbnail(AppLookAndFeel.ERROR_THUMBNAIL);
        }
    }

    private void setExif(ImageFile imageFile) {
        File file = imageFile.getFile();
        Exif exif = null;

        if (!AppFileFilters.INSTANCE.isUserDefinedFileType(file)) {
            ExifCache.INSTANCE.deleteCachedExifTags(file);
            exif = ExifMetadata.getExif(file);
        }

        if ((exif != null) && !exif.isEmpty()) {
            imageFile.setExif(exif);
        } else {
            imageFile.setExif(null);
        }
    }

    private void setXmp(ImageFile imageFile) {
        File imgFile = imageFile.getFile();
        Xmp xmp = null;

        try {
            xmp = XmpMetadata.hasImageASidecarFile(imgFile)
                    ? XmpMetadata.getXmpFromSidecarFileOf(imgFile)
                    : isScanForEmbeddedXmp()
                    ? XmpMetadata.getEmbeddedXmp(imgFile)
                    : null;
        } catch (IOException ex) {
            Logger.getLogger(InsertImageFilesIntoRepository.class.getName()).log(Level.SEVERE, null, ex);

            return;
        }

        writeSidecarFileIfNotExists(imgFile, xmp);

        if ((xmp != null) && !xmp.isEmpty()) {
            imageFile.setXmp(xmp);
        }
    }

    private void setExifDateToXmpDateCreated(ImageFile imageFile) {
        Exif exif = imageFile.getExif();
        Xmp xmp = imageFile.getXmp();
        boolean hasExif = exif != null;
        boolean hasXmp = xmp != null;
        boolean hasXmpDateCreated = hasXmp && xmp.contains(XmpIptc4XmpCoreDateCreatedMetaDataValue.INSTANCE);
        boolean hasExifDate = hasExif && (exif.getDateTimeOriginal() != null);

        if (hasXmpDateCreated || !hasXmp || !hasExif || !hasExifDate) {
            return;
        }

        xmp.setValue(XmpIptc4XmpCoreDateCreatedMetaDataValue.INSTANCE, exif.getXmpDateCreated());

        File sidecarFile = XmpMetadata.suggestSidecarFile(imageFile.getFile());

        if (sidecarFile.canWrite()) {
            XmpMetadata.writeXmpToSidecarFile(xmp, sidecarFile);
            xmp.setValue(XmpLastModifiedMetaDataValue.INSTANCE, sidecarFile.lastModified());
        }
    }

    private void writeSidecarFileIfNotExists(File imageFile, Xmp xmp) {
        if ((xmp != null) && !XmpMetadata.hasImageASidecarFile(imageFile)
                && XmpMetadata.canWriteSidecarFileForImageFile(imageFile)) {
            File sidecarFile = XmpMetadata.suggestSidecarFile(imageFile);

            XmpMetadata.writeXmpToSidecarFile(xmp, sidecarFile);
        }
    }

    private void runActionsAfterInserting(ImageFile imageFile) {
        if (!isRunActionsAfterInserting(imageFile)) {
            return;
        }

        File imgFile = imageFile.getFile();
        List<Program> actions = actionsAfterRepoUpdatesRepo.findAllActions();

        for (Program action : actions) {
            StartPrograms programStarter = new StartPrograms();

            programStarter.startProgram(action, Collections.singletonList(imgFile), true);
        }
    }

    private void deleteFromRepositoryXmpIfAbsentInFilesystem(File file) {
        if (XmpMetadata.hasImageASidecarFile(file)) {
            return;
        }

        if (imageFileRepo.existsXmpForFile(file)) {
            LOGGER.log(Level.INFO, "Deleting from Repository XMP of file ''{0}'' - it does not have (anymore) a XMP sidecar file", file);
            imageFileRepo.deleteXmpOfFile(file);
        }
    }

    private boolean isRunActionsAfterInserting(ImageFile imageFile) {
        return isExecuteActionsAfterImageChangeInDbAlways()
                || (isExecuteActionsAfterImageChangeInDbIfImageHasXmp() && (imageFile.getXmp() != null));
    }

    private boolean isExecuteActionsAfterImageChangeInDbIfImageHasXmp() {
        Preferences storage = Lookup.getDefault().lookup(Preferences.class);

        return storage.containsKey(AppPreferencesKeys.KEY_EXECUTE_ACTIONS_AFTER_IMAGE_CHANGE_IN_DB_IF_IMAGE_HAS_XMP)
                ? storage.getBoolean(AppPreferencesKeys.KEY_EXECUTE_ACTIONS_AFTER_IMAGE_CHANGE_IN_DB_IF_IMAGE_HAS_XMP)
                : false;
    }

    private boolean isExecuteActionsAfterImageChangeInDbAlways() {
        Preferences storage = Lookup.getDefault().lookup(Preferences.class);

        return storage.containsKey(AppPreferencesKeys.KEY_EXECUTE_ACTIONS_AFTER_IMAGE_CHANGE_IN_DB_ALWAYS)
                ? storage.getBoolean(AppPreferencesKeys.KEY_EXECUTE_ACTIONS_AFTER_IMAGE_CHANGE_IN_DB_ALWAYS)
                : false;
    }

    /**
     * A <em>soft</em> interrupt: I/O operations can finishing their current
     * process.
     */
    @Override
    public void cancel() {
        cancel = true;
    }

    private void notifyUpdateMetadataCheckListener(Type type, File file) {
        UpdateMetadataCheckEvent evt = new UpdateMetadataCheckEvent(type, file);

        EventBus.publish(evt);
    }

    /**
     * Adds a progress listener.
     *
     * {@code ProgressEvent#getInfo()} contains a {@code java.lang.String} of
     * the updated
     * @param listener
     */
    public void addProgressListener(ProgressListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener == null");
        }

        pls.add(listener);
    }

    public void removeProgressListener(ProgressListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener == null");
        }

        pls.remove(listener);
    }

    private void notifyStarted() {
        notifyUpdateMetadataCheckListener(Type.CHECK_STARTED, null);
        progressEvent.setMinimum(0);
        progressEvent.setMaximum(imageFiles.size());
        progressEvent.setValue(0);
        pls.notifyStarted(progressEvent);
    }

    private void notifyPerformed(int value, File file) {
        logPerformed(file);
        notifyUpdateMetadataCheckListener(Type.CHECKING_FILE, file);
        progressEvent.setValue(value);
        progressEvent.setInfo(file);
        pls.notifyPerformed(progressEvent);
    }

    private void notifyEnded(int filecount) {
        logEnded(filecount);
        notifyUpdateMetadataCheckListener(Type.CHECK_FINISHED, null);
        pls.notifyEnded(progressEvent);
    }

    private void logErrorNullThumbnail(File file) {
        LOGGER.log(Level.WARNING, "Thumbnail couldn''t be created for image file ''{0}''", file);
    }

    private void logPerformed(File file) {
        LOGGER.log(Level.FINEST, "Synchronizing ''{0}'' with the repository", file);
    }

    private void logEnded(int filecount) {
        LOGGER.log(Level.INFO, "Synchronized {0} image files with the repository", filecount);
    }

    private boolean checkExists(File imageFile) {
        if (!imageFile.exists()) {
            LOGGER.log(Level.WARNING, "Image file ''{0}'' does not (longer) exist and will not be updated in the repository", imageFile);

            return false;
        }

        return true;
    }

    private void logInsertImageFile(ImageFile data) {
        Object[] params = {data.getFile().getAbsolutePath(), (data.getExif() == null)
            ? Bundle.getString(InsertImageFilesIntoRepository.class, "InsertImageFilesIntoRepository.Info.StartInsert.No")
            : Bundle.getString(InsertImageFilesIntoRepository.class, "InsertImageFilesIntoRepository.Info.StartInsert.Yes"), (data.getXmp() == null)
            ? Bundle.getString(InsertImageFilesIntoRepository.class, "InsertImageFilesIntoRepository.Info.StartInsert.No")
            : Bundle.getString(InsertImageFilesIntoRepository.class, "InsertImageFilesIntoRepository.Info.StartInsert.Yes"), (data.getThumbnail() == null)
            ? Bundle.getString(InsertImageFilesIntoRepository.class, "InsertImageFilesIntoRepository.Info.StartInsert.No")
            : Bundle.getString(InsertImageFilesIntoRepository.class, "InsertImageFilesIntoRepository.Info.StartInsert.Yes")};

        LOGGER.log(Level.INFO, "Add metadata into the repository of file ''{0}'': EXIF: {1}, XMP: {2}, Thumbnail: {3}", params);
    }
}
