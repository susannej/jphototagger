package de.elmar_baumann.lib.event;

import java.awt.event.MouseEvent;

/**
 * 
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/10/24
 */
public final class MouseEventUtil {

    /**
     * Workaround for some operating systems where 
     * {@link java.awt.event.MouseEvent#isPopupTrigger()} does not return true
     * if the user clicked down the right mouse button.
     * 
     * @param  e  mouse event
     * @return true if the right mouse button is down
     */
    public static boolean isPopupTrigger(MouseEvent e) {
        return e.isPopupTrigger() || e.getModifiers() == 4;
    }

    /**
     * A more descriptive variant of 
     * {@link java.awt.event.MouseEvent#getClickCount()} == 2.
     * 
     * @param  e  mouse event
     * @return true, if the user clicked twice or more often
     */
    public static boolean isDoubleClick(MouseEvent e) {
        return e.getClickCount() >= 2;
    }

    /**
     * Returns, whether the left mouse button was clicked.
     * 
     * @param  e  mouse event
     * @return true if the left mouse button was clicked
     */
    public static boolean isLeftClick(MouseEvent e) {
        return e.getButton() == MouseEvent.BUTTON1;
    }

    private MouseEventUtil() {
    }
}
