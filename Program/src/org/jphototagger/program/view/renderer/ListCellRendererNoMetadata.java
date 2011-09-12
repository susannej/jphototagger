package org.jphototagger.program.view.renderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.jphototagger.domain.metadata.MetaDataValue;
import org.jphototagger.program.model.ListModelNoMetadata;

/**
 * Renders list items of {@link ListModelNoMetadata}.
 *
 * @author Elmar Baumann
 */
public final class ListCellRendererNoMetadata extends DefaultListCellRenderer {

    private static final long serialVersionUID = -5033440934166574955L;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof MetaDataValue) {
            MetaDataValue mdValue = (MetaDataValue) value;

            label.setText(mdValue.getDescription());
            label.setIcon(TableIcons.getIcon(mdValue.getCategory()));
        }

        return label;
    }
}
