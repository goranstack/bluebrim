package com.bluebrim.swing.client;
import java.awt.event.MouseEvent;

import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class CoTextArea extends JTextArea {
	private boolean m_focusTraversable = true;
  private CoTextDragSourceListener m_dragSourceListener;
protected boolean m_isSelecting = false;
  protected boolean m_isDragging = false;

/**
 * CoTextArea constructor comment.
 */
public CoTextArea() {
	super();
}


/**
 * CoTextArea constructor comment.
 * @param rows int
 * @param columns int
 */
public CoTextArea(int rows, int columns) {
	super(rows, columns);
}


/**
 * CoTextArea constructor comment.
 * @param text java.lang.String
 */
public CoTextArea(String text) {
	super(text);
}


/**
 * CoTextArea constructor comment.
 * @param text java.lang.String
 * @param rows int
 * @param columns int
 */
public CoTextArea(String text, int rows, int columns) {
	super(text, rows, columns);
}


/**
 * CoTextArea constructor comment.
 * @param doc javax.swing.text.Document
 */
public CoTextArea(javax.swing.text.Document doc) {
	super(doc);
}


/**
 * CoTextArea constructor comment.
 * @param doc javax.swing.text.Document
 * @param text java.lang.String
 * @param rows int
 * @param columns int
 */
public CoTextArea(javax.swing.text.Document doc, String text, int rows, int columns) {
	super(doc, text, rows, columns);
}


/**
 */
public CoTextDragSourceListener getDragSourceListener () {
	return m_dragSourceListener;
}


public boolean isFocusTraversable() {
	return m_focusTraversable && super.isFocusTraversable();
}


public boolean isSelecting()
{
	return m_isSelecting;
}


	/** 
	 */   
protected void processMouseEvent(MouseEvent e) {
	if (m_dragSourceListener == null) 
		super.processMouseEvent(e);
	else
	{
		if (m_dragSourceListener.canStartDrag(e))
			 m_isDragging = true;
		super.processMouseEvent(e);
	}

	if (e.getID() == MouseEvent.MOUSE_RELEASED || e.getID() == MouseEvent.MOUSE_CLICKED)
		m_isSelecting = false;

}


	/** 
	 */   
protected void processMouseMotionEvent(MouseEvent e) {
	if (!m_isDragging && e.getID() == MouseEvent.MOUSE_DRAGGED ){
		m_isSelecting = true;
	}
	super.processMouseMotionEvent(e);

}


/**
 * @param l com.bluebrim.swing.client.CoTextDragSourceListener
 */
public void setDragSourceListener ( CoTextDragSourceListener listener) {
	m_dragSourceListener = listener;
	setCaret(new DefaultCaret() {

		public void mousePressed(MouseEvent e) {
			if (m_isDragging)
				return;
			super.mousePressed(e);
		}
		public void mouseDragged(MouseEvent e) {
			if (m_isDragging)
				return;
			super.mouseDragged(e);
		}
		public void mouseClicked(MouseEvent e) {
			if (m_isDragging) {
				m_isDragging = false;
				int pos = viewToModel(e.getPoint());
				select(pos, pos);
			}
			super.mouseClicked(e);
		}
	});
}


public void setFocusTraversable(boolean traversable) {
	m_focusTraversable = traversable;
}
}