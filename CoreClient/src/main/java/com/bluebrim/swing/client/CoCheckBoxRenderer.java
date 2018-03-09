package com.bluebrim.swing.client;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import javax.swing.table.TableCellRenderer;

import com.bluebrim.base.shared.CoConstants;

/**
 * List and table cell renderer for boolean display.
 * Also suitable as cell editor component in a DefaultCellEditor,
 * since only the box itself is sensitive to mouse presses.
 *
 * PENDING: Share common constants with CoStringRenderer.
 *
 * @author Markus Persson 2001-04-05
 */
public class CoCheckBoxRenderer extends JCheckBox implements TableCellRenderer, ListCellRenderer {
	private static final Object PENDING_VALUE = CoConstants.PENDING_VALUE;

	private static final Color PENDING_BACKGROUND = new Color(0xFF, 0xFF, 0xE0); // Light yellow
	private static final Color PENDING_SELECTED_BACKGROUND = new Color(0xFF, 0xF0, 0xE0); // Light orange

	private static final Border FOCUS_BORDER;
	private static final Border NO_FOCUS_BORDER;

	private Rectangle m_hitArea = new Rectangle();
	private boolean m_iconInstalled;
	
	static {
		FOCUS_BORDER = UIManager.getBorder("List.focusCellHighlightBorder");
		Insets insets;
		try {
			insets = FOCUS_BORDER.getBorderInsets(null);
		} catch (NullPointerException npe) {
			insets = new Insets(1, 2, 1, 2);
		}
		NO_FOCUS_BORDER = new EmptyBorder(insets);
	}

public CoCheckBoxRenderer() {
	setOpaque(true);
}


public int getHorizontalAlignment() {
	return CENTER;
}

public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	if (value != PENDING_VALUE) {
		setSelected((value != null && ((Boolean) value).booleanValue()));
		setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
	} else {
		setSelected(false);
		setBackground(isSelected ? PENDING_SELECTED_BACKGROUND : PENDING_BACKGROUND);
	}
	setEnabled(list.isEnabled());
	setBorder(cellHasFocus ? FOCUS_BORDER : NO_FOCUS_BORDER);
	return this;
}


/**
 * Returns the area in which mouse presses can affect the state of the check box.
 */
public Rectangle getLiveHitArea() {
	return m_hitArea;
}

public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	if (value != PENDING_VALUE) {
		setSelected((value != null && ((Boolean) value).booleanValue()));
		setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
	} else {
		setSelected(false);
		setBackground(isSelected ? PENDING_SELECTED_BACKGROUND : PENDING_BACKGROUND);
	}
	setEnabled(table.isEnabled());
	setBorder(hasFocus ? FOCUS_BORDER : NO_FOCUS_BORDER);
	return this;
}


public void updateUI() {
	m_iconInstalled = false;
	setUI(CoCheckBoxRendererUI.createUI(this));
}


public void validate() {
	if (!isValid()) {
		Rectangle hitArea = m_hitArea;
		if (!m_iconInstalled) {
			Icon icon = ((BasicRadioButtonUI) getUI()).getDefaultIcon();
			hitArea.width = icon.getIconWidth();
			hitArea.height = icon.getIconHeight();
			m_iconInstalled = true;
		}
		// Layout compatible with SwingUtilities.layoutCompoundLabel()
		hitArea.x = (getWidth() / 2) - (hitArea.width / 2);
		hitArea.y = (getHeight() / 2) - (hitArea.height / 2);
	}
	super.validate();
}
}