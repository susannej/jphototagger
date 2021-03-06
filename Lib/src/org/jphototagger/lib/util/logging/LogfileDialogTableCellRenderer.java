package org.jphototagger.lib.util.logging;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.jphototagger.lib.util.Bundle;

/**
 * Renders the {@code java.util.logging.Level} icons displayed in the GUI of
 * {@code org.jphototagger.lib.dialog.LogfileDialog}. Also formats dates and selected table rows.
 *
 * @author Elmar Baumann
 */
public final class LogfileDialogTableCellRenderer implements TableCellRenderer {

    private static final Color SEL_BACKGROUND_COLOR = new Color(251, 225, 146);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(Bundle.getString(LogfileDialogTableCellRenderer.class, "LogfileDialogTableCellRenderer.DateFormat"));
    private static final Map<Level, ImageIcon> ICON_OF_LEVEL = new HashMap<>();
    private final JLabel cellLabel = new JLabel();

    static {
        ICON_OF_LEVEL.put(Level.CONFIG, org.jphototagger.resources.Icons.getIcon("icon_logfiledialog_config.png"));
        ICON_OF_LEVEL.put(Level.FINE, org.jphototagger.resources.Icons.getIcon("icon_logfiledialog_fine.png"));
        ICON_OF_LEVEL.put(Level.FINER, org.jphototagger.resources.Icons.getIcon("icon_logfiledialog_finer.png"));
        ICON_OF_LEVEL.put(Level.FINEST, org.jphototagger.resources.Icons.getIcon("icon_logfiledialog_finest.png"));
        ICON_OF_LEVEL.put(Level.INFO, org.jphototagger.resources.Icons.getIcon("icon_logfiledialog_info.png"));
        ICON_OF_LEVEL.put(Level.SEVERE, org.jphototagger.resources.Icons.getIcon("icon_logfiledialog_severe.png"));
        ICON_OF_LEVEL.put(Level.WARNING, org.jphototagger.resources.Icons.getIcon("icon_logfiledialog_warning.png"));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Level) {
            cellLabel.setIcon(getIcon((Level) value));
        } else if (value instanceof Date) {
            renderDate(cellLabel, (Date) value);
        } else {
            cellLabel.setText(value == null ? "" : value.toString());
        }

        renderSelection(cellLabel, isSelected);

        int prefHeight = cellLabel.getPreferredSize().height;
        int rowHeight = table.getRowHeight(row);
        if (prefHeight > rowHeight) {
            table.setRowHeight(prefHeight);
        }

        return cellLabel;
    }

    private void renderSelection(JLabel cellLabel, boolean isSelected) {
        cellLabel.setForeground(Color.BLACK);
        cellLabel.setBackground(isSelected
                ? SEL_BACKGROUND_COLOR
                : Color.WHITE);
        cellLabel.setOpaque(true);
    }

    private void renderDate(JLabel cellLabel, Date date) {
        cellLabel.setText(DATE_FORMAT.format(date));
    }

    private static Icon getIcon(Level level) {
        return ICON_OF_LEVEL.get(level);
    }
}
