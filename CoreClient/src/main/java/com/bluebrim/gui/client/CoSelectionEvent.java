package com.bluebrim.gui.client;

import java.util.EventObject;

import javax.swing.ListSelectionModel;

/**
 	Ekvivalent med ListSelectionEvent. Skapas från ett 'ListSelectionEvent' 
 	med en source som inte är en ListSelectionModel.<br>
 	@see CoListModel#listSelectionChanged.
 	@author Lasse Svadängs 971010
 */
public class CoSelectionEvent extends EventObject {
	ListSelectionModel selectionModel;
/**
  */
public CoSelectionEvent(Object source, ListSelectionModel selectionModel) {
	super(source);
	this.selectionModel 	= selectionModel;
}
/**
 * @return int
 */
public int getFirstIndex ( ) {
	return selectionModel.getMinSelectionIndex();
}
/**
 * @return int
 */
public int getLastIndex ( ) {
	return selectionModel.getMaxSelectionIndex();
}
/**
 * @return int
 */
public ListSelectionModel getListSelectionModel ( ) {
	return selectionModel;
}
/**
 * @return boolean
 */
public boolean getValueIsAdjusting ( ) {
	return selectionModel.getValueIsAdjusting();
}
/**
 * @return boolean
 */
public boolean isSelectedIndex ( int index) {
	return selectionModel.isSelectedIndex(index);
}
}
