/*
 * @(#)FileSort.java    Created on 
 *
 * Copyright (C) 2009-2010 by the JPhotoTagger developer team.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package org.jphototagger.lib.comparator;

import java.io.File;

import java.util.Comparator;

/**
 * Sorting of files.
 *
 * @author Elmar Baumann
 */
public enum FileSort {
    PATHS_ASCENDING(new ComparatorFilesPathsAscCi()),
    PATHS_DESCENDING(new ComparatorFilesPathsDescCi()),
    NAMES_ASCENDING(new ComparatorFilesNamesAscCi()),
    NAMES_DESCENDING(new ComparatorFilesNamesDescCi()),
    TYPES_ASCENDING(new ComparatorFilesSuffixesAscCi()),
    TYPES_DESCENDING(new ComparatorFilesSuffixesDescCi()),
    LAST_MODIFIED_ASCENDING(new ComparatorFilesLastModifiedAsc()),
    LAST_MODIFIED_DESCENDING(new ComparatorFilesLastModifiedDesc()),
    NO_SORT(new ComparatorFilesNoSort()),
    ;

    private final Comparator<File> comparator;

    private FileSort(Comparator<File> comparator) {
        this.comparator = comparator;
    }

    public Comparator<File> getComparator() {
        return comparator;
    }
}