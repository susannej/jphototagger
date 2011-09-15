package org.jphototagger.program.cache;

import java.io.File;

import org.jphototagger.domain.xmp.Xmp;

/**
 *
 * @author Martin Pohlack
 */
public class XmpCacheIndirection extends CacheIndirection {

    Xmp xmp;

    public XmpCacheIndirection(File _file) {
        super(_file);
        xmp = null;
    }

    @Override
    public boolean isEmpty() {
        return xmp == null;
    }
}
