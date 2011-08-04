package org.jphototagger.repositoryfilebrowser;

import java.util.Comparator;

/**
 *
 *
 * @author Elmar Baumann
 */
public final class FileNodeComparator implements Comparator<FileNode> {

    public static final FileNodeComparator INSTANCE = new FileNodeComparator();

    @Override
    public int compare(FileNode o1, FileNode o2) {
        String fileName1 = o1.getFile().getName();
        String fileName2 = o2.getFile().getName();

        return fileName1.compareToIgnoreCase(fileName2);

    }

    private FileNodeComparator() {
    }

}
