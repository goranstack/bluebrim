package com.bluebrim.swing.client;

import javax.swing.event.ListSelectionEvent;
/**
 * This type was created in VisualAge.
 */
public class CoChooserSelectionEvent extends ListSelectionEvent {
	boolean isSourceSelection;
	Object	selection[];
/**
 * CoChooserSelectionEvent constructor comment.
 * @param source java.lang.Object
 * @param firstIndex int
 * @param lastIndex int
 * @param isAdjusting boolean
 */
public CoChooserSelectionEvent(Object source, int firstIndex, int lastIndex, Object selection[], boolean isSourceSelection) {
	super(source, firstIndex, lastIndex, false);
	this.isSourceSelection 	= isSourceSelection;
	this.selection 			= selection;
}
public Object[] getSelection()
{
	return selection;
}
public boolean isSourceSelection()
{
	return isSourceSelection;
}
}
