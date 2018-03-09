package com.bluebrim.swing.client;
import java.awt.Component;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.event.MouseEvent;

import javax.swing.text.JTextComponent;

import com.bluebrim.base.client.datatransfer.CoDragSourceListener;
/**
 	Konkret subklass till CoDragSourceListener som hanterar drag som 
 	startar i en textvy, exempelvis ett CoTextField.
*/
public class CoTextDragSourceListener extends CoDragSourceListener {

/**
 * CoListDragSourceListener constructor comment.
 */
public CoTextDragSourceListener(Component c) {
	super(c);
	((CoDnDTextComponentIF )c).setDragSourceListener(this);
}


/**
 */
public boolean canStartDrag(DragGestureEvent e) {
	if (super.canStartDrag(e)) {
		int tSelStart = getTextComponent().getSelectionStart();
		int tSelEnd = getTextComponent().getSelectionEnd();

		if (tSelStart == tSelEnd)
			return false;

		if (!((CoTextField) e.getComponent()).isSelecting()) {
			int tIndexAtPoint = getTextComponent().viewToModel(e.getDragOrigin());
			boolean isInSelection = ((tIndexAtPoint >= tSelStart) && (tIndexAtPoint < tSelEnd));
			return isInSelection;
		} else
			return false;
	} else
		return false;
}


/**
 */
public boolean canStartDrag(MouseEvent e) {
	if (super.canStartDrag(e)) {
		if (e.getID() != MouseEvent.MOUSE_PRESSED)
			return false;

		int selStart = getTextComponent().getSelectionStart();
		int selEnd = getTextComponent().getSelectionEnd();
		if (selStart == selEnd)
			return false;

		int indexAtPoint = getTextComponent().viewToModel(e.getPoint());
		boolean isInSelection = ((indexAtPoint >= selStart) && (indexAtPoint < selEnd));
		return isInSelection;
	} else
		return false;

}


/**
 * 
 */
public void dragDropEnd(DragSourceDropEvent dsde) {
	super.dragDropEnd(dsde);
	getTextComponent().requestFocus();
}


/**
 * dragExit method comment.
 */
public void dragExit(DragSourceEvent dse) {
	getTextComponent().requestFocus();
	super.dragExit(dse);
}	


public JTextComponent getTextComponent() {
	return (JTextComponent )getComponent();
}


/**
 * getTransferable method comment.
 */
public Transferable getTransferable() {
	return new StringSelection(getTextComponent().getSelectedText());
}
}