package com.bluebrim.base.client;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * Table column with multiple names, tooltip and sorting indication ability.
 *
 * @see CoSortableTableHeaderRenderer
 * @author Markus Persson 2001-04-19
 */
public class CoSortableTableColumn extends TableColumn {
	public static final int SORT_DESCENDING = -1;
	public static final int SORT_NONE = 0;
	public static final int SORT_ASCENDING = 1;

	private String m_longName;
	private String m_name;

public CoSortableTableColumn(int modelIndex, int width, TableCellRenderer cellRenderer, TableCellEditor cellEditor, String name, String longName) {
	super(modelIndex, width, cellRenderer, cellEditor);
	m_name = name;
	m_longName = longName;
}
public Object getHeaderValue() {
	return this;
}
public String getLongName() {
	return m_longName;
}
public String getName() {
	return m_name;
}
public int getSortDirection() {
	return SORT_NONE;
}
}
