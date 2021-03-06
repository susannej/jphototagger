package org.jphototagger.lib.swing;

import java.awt.event.MouseEvent;

/**
 * @author Elmar Baumann
 */
public final class MouseEventUtil {

    /**
     * Workaround for some operating systems where
     * {@code java.awt.event.MouseEvent#isPopupTrigger()} does not return true
     * if the user clicked down the right mouse button.
     *
     * @param  evt  mouse event
     * @return true if the right mouse button is down
     */
    public static boolean isPopupTrigger(MouseEvent evt) {
        if (evt == null) {
            throw new NullPointerException("evt == null");
        }

        return evt.isPopupTrigger() || (evt.getModifiers() == 4);
    }

    /**
     * A more descriptive variant of
     * {@code java.awt.event.MouseEvent#getClickCount()} == 2.
     *
     * @param  evt  mouse event
     * @return true, if the user clicked twice or more often
     */
    public static boolean isDoubleClick(MouseEvent evt) {
        if (evt == null) {
            throw new NullPointerException("evt == null");
        }

        return evt.getClickCount() >= 2;
    }

    /**
     * Returns, whether the left mouse button was clicked.
     *
     * @param  evt  mouse event
     * @return true if the left mouse button was clicked
     */
    public static boolean isLeftClick(MouseEvent evt) {
        if (evt == null) {
            throw new NullPointerException("evt == null");
        }

        return evt.getButton() == MouseEvent.BUTTON1;
    }

    private MouseEventUtil() {
    }
}
