package com.bluebrim.base.client;
import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;

import com.bluebrim.resource.shared.*;
/**
 * Table header renderer with multiple names, tooltip and sorting indication ability.
 *
 * PENDING: Don't inherit from JLabel. Paint everything by hand.
 * PENDING: Show qualified name (tool tip) in header if it's wide enough?
 *
 * @see CoSortableTableColumn
 * @author Markus Persson 2001-04-19
 */
public class CoSortableTableHeaderRenderer extends JLabel implements TableCellRenderer {
	private boolean m_initialized;



public CoSortableTableHeaderRenderer() {
	setBorder(UIManager.getBorder("TableHeader.cellBorder"));
	setOpaque(true);
}

public CoSortableTableHeaderRenderer(JTable table) {
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
//	CoSortableTableColumn col = (CoSortableTableColumn) value;
	m_col = (CoSortableTableColumn) value;
/*
	setText(col.getName());
	setToolTipText(col.getLongName());
*/
	
	return this;
}

public int getHorizontalAlignment() {
	return CENTER;
}

	private CoSortableTableColumn m_col;
	private static Icon[] SORT_ICONS = new Icon[3];
	static {
		SORT_ICONS[0] = CoResourceLoader.loadIcon(CoSortableTableHeaderRenderer.class, "descending.gif");
		SORT_ICONS[2] = CoResourceLoader.loadIcon(CoSortableTableHeaderRenderer.class, "ascending.gif");
	}


public int getHorizontalTextPosition() {
	return LEFT;
}

public Icon getIcon() {
	return (m_col == null) ? null : SORT_ICONS[m_col.getSortDirection() + 1];
}

public String getText() {
	return (m_col == null) ? null : m_col.getName();
}

public String getToolTipText() {
	return (m_col == null) ? null : m_col.getLongName();
}

public void updateUI() {
	super.updateUI();
	m_initialized = false;
}
}