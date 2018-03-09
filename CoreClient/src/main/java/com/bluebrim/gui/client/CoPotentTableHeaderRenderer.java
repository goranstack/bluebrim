package com.bluebrim.gui.client;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * Table header renderer with tooltip ability.
 *
 * PENDING: Clean up!
 * PENDING: Inherit from CoStringRenderer?
 * PENDING: Show qualified name (tool tip) in header if it's wide enough?
 *
 * @author Markus Persson 2000-04-07
 * @author Markus Persson 2001-03-29
 */
public class CoPotentTableHeaderRenderer extends DefaultTableCellRenderer {
	private boolean m_initialized;



public CoPotentTableHeaderRenderer() {
	setBorder(UIManager.getBorder("TableHeader.cellBorder"));
}

public CoPotentTableHeaderRenderer(JTable table) {
	this();
	checkInit(table);
}

private void checkInit(JTable table) {
	if (!m_initialized && (table != null)) {
		JTableHeader header = table.getTableHeader();
		if (header != null) {
			setForeground(header.getForeground());
			setBackground(header.getBackground());
			setFont(header.getFont());
			m_initialized = true;
		}
	}
}

public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	checkInit(table);
	// Should always be true, but better safe than sorry. /Markus
	if (value instanceof String[]) {
		String[] names = (String[]) value;
		setText(names[0]);
		setToolTipText(names[1]);
	} else {
		String text = (value == null) ? "" : value.toString();
		setText(text);
		setToolTipText(text);
	}
	return this;
}

public int getHorizontalAlignment() {
	return CENTER;
}

public int getHorizontalTextPosition() {
	return LEFT;
}

public void updateUI() {
	super.updateUI();
	m_initialized = false;
}
}