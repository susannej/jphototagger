package de.elmar_baumann.imv.image.metadata.exif.entry;

import de.elmar_baumann.imv.image.metadata.exif.datatype.ExifByteOrder;
import de.elmar_baumann.imv.resource.Bundle;
import java.util.HashMap;
import java.util.Map;

/**
 * GPS latitude.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2009/03/17
 */
public final class ExifGpsLatitude {

    /**
     * Indicates whether the latitude is north or south latitude.
     */
    public enum Ref {

        NORTH, SOUTH
    }
    private static final Map<String, Ref> REF_OF_STRING =
            new HashMap<String, Ref>();
    private static final Map<Ref, String> LOCALIZED_STRING_OF_REF =
            new HashMap<Ref, String>();

    static {
        REF_OF_STRING.put("N", Ref.NORTH);
        REF_OF_STRING.put("S", Ref.SOUTH);

        LOCALIZED_STRING_OF_REF.put(Ref.NORTH,
                Bundle.getString("ExifGpsLatitudeRefNorth")); // NOI18N
        LOCALIZED_STRING_OF_REF.put(Ref.SOUTH,
                Bundle.getString("ExifGpsLatitudeRefSouth")); // NOI18N
    }
    private Ref ref;
    private ExifDegrees degrees;

    public ExifGpsLatitude(byte[] refRawValue, byte[] degreesRawValue,
            ExifByteOrder byteOrder) {
        if (!isRefRawValueByteCountOk(refRawValue))
            throw new IllegalArgumentException(
                    "Illegal ref raw value byte count: " + refRawValue.length); // NOI18N
        if (!isRawValueByteCountOk(degreesRawValue))
            throw new IllegalArgumentException(
                    "Illegal raw value byte count: " + degreesRawValue.length); // NOI18N

        this.ref = getRef(refRawValue);
        this.degrees = new ExifDegrees(degreesRawValue, byteOrder);
    }

    private static Ref getRef(byte[] rawValue) {
        String s = null;
        if (rawValue != null && rawValue.length == 2) {
            s = new StringBuilder(1).append((char) new Byte(rawValue[0]).
                    intValue()).toString();
        }
        return REF_OF_STRING.get(s);
    }

    public static int getRawValueByteCount() {
        return 24;
    }

    public static int getRefRawValueByteCount() {
        return 2;
    }

    public static boolean isRawValueByteCountOk(byte[] rawValue) {
        return rawValue.length == getRawValueByteCount();
    }

    public static boolean isRefRawValueByteCountOk(byte[] rawValue) {
        return rawValue.length == getRefRawValueByteCount();
    }

    public String localizedString() {
        return ExifGpsUtil.degreesToString(degrees) + " " +
                LOCALIZED_STRING_OF_REF.get(ref);
    }

    public ExifDegrees getDegrees() {
        return degrees;
    }

    public Ref getRef() {
        return ref;
    }
}
