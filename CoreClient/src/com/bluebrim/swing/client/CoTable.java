package com.bluebrim.swing.client;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.bluebrim.base.client.datatransfer.CoDataTransferKit;
import com.bluebrim.base.client.datatransfer.CoDnDDataProvider;
import com.bluebrim.base.shared.CoNamed;

public class CoTable extends JTable implements DragGestureListener {
	private CoDnDDataProvider m_dragDataProvider;
	private int m_visibleRowCount = -1;

public CoTable() {
	super();
}


public CoTable(int numRows, int numColumns) {
	super(numRows, numColumns);
}


public CoTable(TableModel dm) {
	super(dm);
}


public CoTable(TableModel dm, TableColumnModel cm) {
	super(dm, cm);
}


public CoTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
	super(dm, cm, sm);
}


/**
 * Overridden to stop tables from shrinking all the time when the model is changed.
 *
 * PENDING: Note that this is a temporary solution ...
 *
 * Now also dispatches creation to column model if supported.
 *
 * @author Markus Persson 1999-11-20
 */
public void createDefaultColumnsFromModel() {
	TableModel m = getModel();
	if (m != null) {
		// Remove any current columns
		TableColumnModel cm = getColumnModel();
		cm.removeColumnModelListener(this);
		if (cm instanceof CoTableColumnModel) {
			((CoTableColumnModel) cm).createColumnsFrom(m);
			// PENDING: Temporary. How to do in the future? /Markus
			setAutoResizeMode((cm.getColumnCount() <= 5) ?
				AUTO_RESIZE_NEXT_COLUMN : AUTO_RESIZE_OFF);
		} else {
			while (cm.getColumnCount() > 0)
				cm.removeColumn(cm.getColumn(0));

			// Create new columns from the data model info
			for (int i = 0; i < m.getColumnCount(); i++) {
				TableColumn newColumn = new TableColumn(i, 125);
				addColumn(newColumn);
			}
		}
		cm.addColumnModelListener(this);
	}

	// Added.
	sizeColumnsToFit(-1);
}


public void dragGestureRecognized(DragGestureEvent e) {
	int row = rowAtPoint(e.getDragOrigin());

	if ((row != -1) && isRowSelected(row)) {
		// Since the row we were dragging from is selected, at least
		// one row must be selected why we don't check this again ...

		// NOTE: We pass ZERO_OBJECTS here instead of objectified
		// row indices, part because I'm lazy and part because some
		// large tables cannot easily find the indices but knows
		// which objects are selected. Thus we leave it up to the data
		// provider to find out the selection by itself. /Markus
		CoDataTransferKit.dragUsing(e, CoDataTransferKit.ZERO_OBJECTS, m_dragDataProvider);
	}
}


/**
 * Added support for the case where the column Class actually is an interface
 * for which no editor has been defined. Since interfaces have no superclass,
 * the normal strategy of climbing the class hierarchy in order to eventually
 * fall back on the editor for <b>Object</b> fails.
 *
 * @author Markus Persson 1999-07-01
 */
public TableCellEditor getDefaultEditor(Class columnClass) {
	TableCellEditor editor = super.getDefaultEditor(columnClass);
	if (editor == null)
		editor = super.getDefaultEditor(Object.class);
	return editor;
}


/**
 * Added support for the case where the column Class actually is an interface
 * for which no renderer has been defined. Since interfaces have no superclass,
 * the normal strategy of climbing the class hierarchy in order to eventually
 * fall back on the renderer for <b>Object</b> fails.
 *
 * @author Markus Persson 1999-07-01
 */
public TableCellRenderer getDefaultRenderer(Class columnClass) {
	TableCellRenderer renderer = super.getDefaultRenderer(columnClass);
	if (renderer == null)
		renderer = super.getDefaultRenderer(Object.class);
	return renderer;
}


public static TableCellEditor getDoubleCellEditor()
{
	JTextField tTextField = new JTextField();
	tTextField.setHorizontalAlignment(JTextField.RIGHT);
	tTextField.setBorder(BorderFactory.createLineBorder(Color.black));
	DefaultCellEditor tEditor =  new DefaultCellEditor(tTextField) {
		NumberFormat m_formatter = NumberFormat.getInstance();
		public Object getCellEditorValue()
		{
			String s = (String)delegate.getCellEditorValue();
			try {
				return Double.valueOf (s);
			} catch (NumberFormatException e) {
				return new Double(0.00);
			}
		}
		public Component getTableCellEditorComponent( JTable table, Object value, boolean isSelected, int row, int column)
		{
			delegate.setValue (value==null ? "0" : m_formatter.format(((Double)value).doubleValue()));
			return editorComponent;
		}
	};
	return tEditor;
}


/**
 * Returns an editable version of the cell value at <i>row</i> and <i>column</i>.
 *
 * The distinction between this method and getValueAt() has been added to
 * enable different representations for values that are merely displayed
 * and for values that will be edited. The intention was to reduce work and
 * network traffic for items that never are edited. However, this may not
 * be such a saving after all ...
 *
 * <b>NOTE</b>: The column is specified in the table view's display
 *              order, and not in the TableModel's column order.  This is
 *              an important distinction because as the user rearranges
 *              the columns in the table, what is at column 2 changes.
 *              Meanwhile the user's actions never affect the model's
 *              column ordering.
 *
 * @see JTable#getValueAt(int,int)
 * @author Markus Persson 2000-07-28
 */
public Object getEditableValueAt(int row, int column) {
	TableModel tableModel = getModel();

	// Disabled for now ... /Markus
//	if (tableModel instanceof com.bluebrim.source.client.CoSourceViewTableModel) {
//		return ((com.bluebrim.source.client.CoSourceViewTableModel)tableModel).getEditableValueAt(row, convertColumnIndexToModel(column));
//	} else {
		return getValueAt(row, column);
//	}
}


public static TableCellRenderer getEnumerationCellRenderer()
{
	return new DefaultTableCellRenderer()
	{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			return super.getTableCellRendererComponent(table, (value != null) ?  ((CoNamed) value).getName() : "", isSelected, hasFocus, row, column);
		}
	};
}


public static TableCellEditor getIntegerCellEditor()
{
	JTextField tTextField = new JTextField();
	tTextField.setHorizontalAlignment(JTextField.RIGHT);
	tTextField.setBorder(BorderFactory.createLineBorder(Color.black));
	DefaultCellEditor tEditor =  new DefaultCellEditor(tTextField) {
		NumberFormat m_formatter = NumberFormat.getInstance();
		public Object getCellEditorValue()
		{
			String s = (String)delegate.getCellEditorValue();
			try {
				return Integer.valueOf (s);
			} catch (NumberFormatException e) {
				return new Integer(0);
			}
		}
		public Component getTableCellEditorComponent( JTable table, Object value, boolean isSelected, int row, int column)
		{
			delegate.setValue (value==null ? "0" : m_formatter.format(((Integer)value).intValue()));
			return editorComponent;
		}
	};
	return tEditor;
}


public Dimension getPreferredScrollableViewportSize() {
	Dimension size = super.getPreferredScrollableViewportSize();

	if (m_visibleRowCount >= 0)
		size.height = m_visibleRowCount * (getRowHeight() + getRowMargin());

	return size;
}


public void prepareDrag(int permittedActions, CoDnDDataProvider dataProvider) {
	m_dragDataProvider = dataProvider;
	CoDataTransferKit.setupDragGestureRecognizer(this, permittedActions, this);
}

protected void processMouseEvent(MouseEvent e) {
	super.processMouseEvent(e);
}


protected void processMouseMotionEvent(MouseEvent e) {
	super.processMouseMotionEvent(e);
}


/**
 * Similar to the method of the same name in JList, except
 * that when rows is less than zero, the preferred scrollable
 * viewport size is determined as for JTable.
 *
 * @see javax.swing.JList#setVisibleRowCount(int)
 * @author Markus Persson
 */
public void setVisibleRowCount(int rows) {
	int oldValue = m_visibleRowCount;
	m_visibleRowCount = rows;
	firePropertyChange("visibleRowCount", oldValue, rows);
}


/**
 * Overridden because when all the data changes the selection should not remain.
 *
 * Possibly though, if the selected objects (by some definition) exists in this new
 * data, they should probably remain selected. However, this cannot easily be
 * implemented here, since we at the time the event is recieved don't know what
 * objects used to be selected.
 *
 * @author Markus Persson 2000-04-13
 */
public void tableChanged(TableModelEvent e) {
	if ((e != null) && (e.getType() == TableModelEvent.UPDATE)
		&& (e.getLastRow() == Integer.MAX_VALUE)) {
		// All the data changed => Clear selection. /Markus
		resizeAndRepaint();
	}
	super.tableChanged(e);
}
}