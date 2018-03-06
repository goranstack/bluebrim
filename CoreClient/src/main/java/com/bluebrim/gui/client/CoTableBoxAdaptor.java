package com.bluebrim.gui.client;

import java.awt.Component;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.bluebrim.swing.client.CoTable;
import com.bluebrim.swing.client.CoTableBox;
/**
 * En CoTableBoxAdaptor knyter ihop en CoTableAspectAdator med en CoTableBox.
  */
public class CoTableBoxAdaptor extends CoComponentAdaptor implements TableModelListener, CoValueListener {
	CoTableBox tableBox;
	CoTableAspectAdaptor tableAspectAdaptor;
/**
 */
public CoTableBoxAdaptor (CoTableAspectAdaptor tableAspectAdaptor, CoTableBox tableBox) {
	setTableAspectAdaptor(tableAspectAdaptor);
	setTableBox(tableBox);
	tableAspectAdaptor.addValueListener(this);
	tableBox.getTable().setModel(tableAspectAdaptor);
	updateTableBox();
}
public void enableDisable(CoEnableDisableEvent e) {
	super.enableDisable(e);
	getTableBox().getTable().setEnabled(e.enable);
}
protected Component getComponent ( ) {
	return getTableBox();
}
/**
 */
public CoTableAspectAdaptor getTableAspectAdaptor () {
	return tableAspectAdaptor;
}
/**
 */
public CoTableBox getTableBox ( ) {
	return tableBox;
}
/**
 */
protected void setTableAspectAdaptor ( CoTableAspectAdaptor aTableAspectAdaptor) {
	tableAspectAdaptor = aTableAspectAdaptor;
}
/**
 * @param aValueable SE.corren.calvin.userinterface.CoListBox
 */
protected void setTableBox ( CoTableBox aTableBox) {
	tableBox = aTableBox;
	tableBox.setEnabled( tableAspectAdaptor.isEnabled() );

}
public void tableChanged (TableModelEvent e ) {
	updateTableBox();
}
/**
 */
public void updateTableBox() {
	CoTable tTable = getTableBox().getTable();
	tTable.revalidate();
	tTable.repaint(getTableBox().getBounds());
}
/**
 */
public void valueChange(CoValueChangeEvent anEvent) {
	updateTableBox();
}
}
