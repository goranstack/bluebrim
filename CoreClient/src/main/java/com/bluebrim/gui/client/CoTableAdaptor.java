package com.bluebrim.gui.client;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
/**
 * En CoTableAdaptor knyter ihop en CoTableAspectAdator med en JTable (utan JScrollPane).
  */
public class CoTableAdaptor extends CoComponentAdaptor implements TableModelListener, CoValueListener {
	JTable table;
	CoTableAspectAdaptor tableAspectAdaptor;
/**
 */
public CoTableAdaptor (CoTableAspectAdaptor tableAspectAdaptor, JTable table) {
	setTableAspectAdaptor(tableAspectAdaptor);
	setTable(table);
	tableAspectAdaptor.addValueListener(this);
	tableAspectAdaptor.addTableModelListener(this);
	table.setModel(tableAspectAdaptor);
	updateTable();
}
protected Component getComponent ( ) {
	return getTable();
}
/**
 */
public JTable getTable ( ) {
	return table;
}
/**
 */
public CoTableAspectAdaptor getTableAspectAdaptor () {
	return tableAspectAdaptor;
}
/**
  */
protected void setTable ( JTable aTable) {
	table = aTable;
	table.setEnabled( tableAspectAdaptor.isEnabled() );

}
/**
 */
protected void setTableAspectAdaptor ( CoTableAspectAdaptor aTableAspectAdaptor) {
	tableAspectAdaptor = aTableAspectAdaptor;
}
public void tableChanged (TableModelEvent e ) {
	updateTable();
}
/**
 */
public void updateTable() {
	getTable().revalidate();
	getTable().repaint();
}
/**
 */
public void valueChange(CoValueChangeEvent anEvent) {
	updateTable();
}
}
