/*
 * @(#)ControllerDoubleklickThumbnail.java    Created on 2008-09-10
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

package org.jphototagger.program.controller.thumbnail;

import org.jphototagger.program.io.IoUtil;
import org.jphototagger.program.UserSettings;
import org.jphototagger.program.view.panels.ThumbnailsPanel;

/**
 * Kontroller für die Aktion: Doppelklick auf ein Thumbnail ausgelöst von
 * {@link org.jphototagger.program.view.panels.ThumbnailsPanel}.
 *
 * @author  Elmar Baumann
 */
public final class ControllerDoubleklickThumbnail {
    private final ThumbnailsPanel panel;

    public ControllerDoubleklickThumbnail(ThumbnailsPanel panel) {
        this.panel = panel;
    }

    public void doubleClickAtIndex(int index) {
        openImage(index);
    }

    private void openImage(int index) {
        if (panel.isIndex(index)) {
            IoUtil.execute(IoUtil
                .quoteForCommandLine(UserSettings.INSTANCE
                    .getDefaultImageOpenApp()), IoUtil
                        .quoteForCommandLine(panel.getFile(index)));
        }
    }
}