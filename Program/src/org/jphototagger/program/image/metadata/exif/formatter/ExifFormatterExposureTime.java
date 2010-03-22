/*
 * @(#)ExifFormatterExposureTime.java    Created on 2009-06-10
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

package org.jphototagger.program.image.metadata.exif.formatter;

import org.jphototagger.program.image.metadata.exif.datatype.ExifRational;
import org.jphototagger.program.image.metadata.exif.Ensure;
import org.jphototagger.program.image.metadata.exif.ExifTag;
import org.jphototagger.lib.generics.Pair;

/**
 * Formats an EXIF entry of the dataType {@code ExifTag.Id#EXPOSURE_TIME}.
 *
 * @author  Elmar Baumann
 */
public final class ExifFormatterExposureTime extends ExifFormatter {
    public static final ExifFormatterExposureTime INSTANCE =
        new ExifFormatterExposureTime();

    private ExifFormatterExposureTime() {}

    @Override
    public String format(ExifTag exifTag) {
        Ensure.exifTagId(exifTag, ExifTag.Id.EXPOSURE_TIME);

        if (ExifRational.byteCount() == exifTag.rawValue().length) {
            ExifRational time = new ExifRational(exifTag.rawValue(),
                                    exifTag.byteOrder());
            Pair<Integer, Integer> pair        = getAsExposureTime(time);
            int                    numerator   = pair.getFirst();
            int                    denominator = pair.getSecond();

            if (denominator > 1) {
                return Integer.toString(numerator) + " / "
                       + Integer.toString(denominator) + " s";
            } else if (numerator > 1) {
                return Integer.toString(numerator) + " s";
            } else if (numerator / denominator == 1) {
                return "1 s";
            }
        }

        return "?";
    }

    private static Pair<Integer, Integer> getAsExposureTime(ExifRational er) {
        int    numerator   = er.numerator();
        int    denominator = er.denominator();
        double result      = (double) numerator / (double) denominator;

        if (result < 1) {
            return new Pair<Integer, Integer>(1,
                            (int) ((double) denominator / (double) numerator
                                   + 0.5));
        } else if (result >= 1) {
            return new Pair<Integer,
                            Integer>((int) ((double) numerator
                                            / (double) denominator + 0.5), 1);
        } else {
            return new Pair<Integer, Integer>(0, 0);
        }
    }
}