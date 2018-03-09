package com.bluebrim.base.client.datatransfer;
import java.awt.Component;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.MouseEvent;

public abstract class CoDragSourceListener implements DragSourceListener, DragGestureListener {
	private Component m_component = null;
	protected DragSource m_dragSource = DragSource.getDefaultDragSource();

	/**
	 * Added so that it can be inherited without Jikes complaining in CoAbstractSourceDnDManager (Jikes problem?)
	 */
	public CoDragSourceListener() {
	}

	public CoDragSourceListener(Component component) {
		m_component = component;
		m_dragSource.createDefaultDragGestureRecognizer(component, DnDConstants.ACTION_COPY_OR_MOVE, this);
	}

	/**
	 *	Anropas från #dragGestureRecognized och svarar med true<br>
	 *	Subklasserna specificerar villkor. 
	 */
	public boolean canStartDrag(DragGestureEvent e) {
		return true;
	}

	/**
	 * 	Anropas från #processMouseEvent i CoTextField och skall svara med true om museventet 
	 *	uppfyller villkoren för att starta ett drag & drop. 
	 */
	public boolean canStartDrag(MouseEvent e) {
		return true;
	}

	public void dragDropEnd(DragSourceDropEvent dsde) {
	}

	public void dragEnter(DragSourceDragEvent dsde) {
	}

	public void dragExit(DragSourceEvent dse) {
	}

	public void dragGestureRecognized(DragGestureEvent e) {

		if (canStartDrag(e)) {
			try {
				m_dragSource.startDrag(e, DragSource.DefaultMoveNoDrop, getTransferable(), this);
			} catch (InvalidDnDOperationException exception) {
				exception.printStackTrace();
			}
		}
	}

	public void dragOver(DragSourceDragEvent dsde) {
	}

	public void dropActionChanged(DragSourceDragEvent dsde) {
	}

	protected Component getComponent() {
		return m_component;
	}

	public abstract Transferable getTransferable();

}