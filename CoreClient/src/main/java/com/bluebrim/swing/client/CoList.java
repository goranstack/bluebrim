package com.bluebrim.swing.client;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicListUI;

import com.bluebrim.base.client.datatransfer.CoDataTransferKit;
import com.bluebrim.base.client.datatransfer.CoDnDDataProvider;

/**
 * Subclass to <code>JList</code> that implements some methods that uses
 * the optimized methods in <code>CoBasicListUI</code>. This class is used
 * as list class by a <code>CoListBox</code>. 
 */
public class CoList extends JList implements DragGestureListener {
	public static String SELECTION_CHANGED	= "selection_changed";
	private boolean m_isPointingAtSelected = false;
	private CoDnDDataProvider m_dragDataProvider;

	protected class ListUI extends CoBasicListUI {
		public ListUI()
		{
			super();
		}
		protected MouseInputListener createMouseInputListener() {
			return new BasicListUI.MouseInputHandler() {

				//mousePressed fires deselection, hence derailing
				public void mousePressed(MouseEvent e) {
					if (m_isPointingAtSelected)
						return; //ready for dragging
					super.mousePressed(e);
				}
				//mouseDragged fires deselection, hence derailing
				public void mouseDragged(MouseEvent e) {
					if (m_isPointingAtSelected)
						return; 				//ready for dragging
					super.mouseDragged(e);
				}

				public void mouseClicked(MouseEvent e) {
					Point p = e.getPoint();
					int pointIndex = locationToIndex(CoList.this,p);

					if (m_isPointingAtSelected) {
						if (e.isShiftDown()) 	//redefining selection with a Shift down
							{
							setSelectionInterval(getAnchorSelectionIndex(), pointIndex);
							super.mouseClicked(e);
							return;
						}
						if (e.isControlDown()) 	//redefining selection with a Ctrl down
							{
							removeSelectionInterval(pointIndex, pointIndex);
							super.mouseClicked(e);
							return;
						} else 					//deselecting
							{
							m_isPointingAtSelected = false;
							setSelectedIndex(pointIndex);
						}
					}
					super.mouseClicked(e);
				}
			};
		}
	}

public CoList() {
	super();
}


/**
 * @param arg1 java.lang.Object[]
 */
public CoList(java.lang.Object[] arg1) {
	super(arg1);
}


/**
 * @param arg1 java.util.Vector
 */
public CoList(java.util.Vector arg1) {
	super(arg1);
}


/**
 * @param model javax.swing.ListModel
 */
public CoList(ListModel model) {
	super(model);
}


public void dragGestureRecognized(DragGestureEvent e) {
	int row = locationToIndex(e.getDragOrigin());
	
	if ((row != -1) && isSelectedIndex(row)) {
		// Since the row we were dragging from is selected, at least
		// one row must be selected why we don't check this again ...
		CoDataTransferKit.dragUsing(e, getSelectedValues(), m_dragDataProvider);
	}
}

public void ensureIndexIsVisible(int index, int listSize) {
	CoBasicListUI ui = (CoBasicListUI) getUI();
	Rectangle cellBounds = (ui != null) ? ui.getCellBounds(this, index, index, listSize) : null;
	if (cellBounds != null) {
		scrollRectToVisible(cellBounds);
	}
}


public void fireVetoableChange(String propertyName, Object oldValue, Object newValue) throws PropertyVetoException {
	super.fireVetoableChange(propertyName, oldValue, newValue);
}


/**
 * Overrides <code>getPreferredScrollableViewportSize</code> in <code>Jlist</code> and
 * uses <code>getPreferredSize</code> as width when the list is empty. 
 */
public Dimension getPreferredScrollableViewportSize() {
		Dimension tDimension 	= null;
		Insets insets 			= getInsets();
		int dx 					= insets.left + insets.right;
		int dy 					= insets.top + insets.bottom;
		int tListSize			= getModel().getSize();
		int visibleRowCount 	= getVisibleRowCount();
		int fixedCellWidth 		= getFixedCellWidth();
		int fixedCellHeight 	= getFixedCellHeight();

		if ((fixedCellWidth > 0) && (fixedCellHeight > 0)) 
		{
			int width 	= fixedCellWidth + dx;
			int height 	= (visibleRowCount * fixedCellHeight) + dy;
			tDimension	= new Dimension(width, height);
		}
		else if (tListSize > 0) {
			int width 		= (fixedCellWidth > 0) ? fixedCellWidth+dx : getPreferredSize().width;
	  		Rectangle r 	= getUI().getCellBounds(this,0, 0);
	  		int height		= (fixedCellHeight > 0) ? fixedCellHeight : ((r != null) ? r.height : 16);
			tDimension		= new Dimension(width, (visibleRowCount * height) + dy);
		}
		else 
		{
			fixedCellWidth 	= (fixedCellWidth > 0) ? fixedCellWidth+dx : getPreferredSize().width;
			fixedCellHeight = (fixedCellHeight > 0) ? fixedCellHeight : 16;
			tDimension		= new Dimension(fixedCellWidth, (fixedCellHeight * visibleRowCount)+dy);
		}
		return tDimension;
}


public void prepareDrag(int permittedActions, CoDnDDataProvider dataProvider) {
	m_dragDataProvider = dataProvider;
	CoDataTransferKit.setupDragGestureRecognizer(this, permittedActions, this);
}

protected void processMouseEvent(MouseEvent e) {

	if (e.getID() == MouseEvent.MOUSE_PRESSED) {
		Point p = e.getPoint();
		int pointIndex = locationToIndex(p);

		m_isPointingAtSelected = isSelectedIndex(pointIndex);
	}
	super.processMouseEvent(e);
}


public void updateUI() {
	setUI(new ListUI());
}
}