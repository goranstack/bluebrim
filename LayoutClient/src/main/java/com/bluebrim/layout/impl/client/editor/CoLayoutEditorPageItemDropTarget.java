package com.bluebrim.layout.impl.client.editor;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.layout.impl.client.tools.*;
import com.bluebrim.layout.impl.client.transfer.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.transact.shared.*;

/**
 * Drop target for page item dnd in a layout editor
 * Creation date: (2001-08-10 10:05:26)
 * @author: Dennis
 */

class CoLayoutEditorPageItemDropTarget extends CoPageItemDropTarget {
	private CoLayoutEditor m_editor;

	private CoMoveTool m_moveTool;

	private Point m_localPosition = new Point();
	private int m_dx;
	private int m_dy;

	private class MoveTool extends CoMoveTool {
		private CoPageItemDragSourceListener m_dragSourceListener;

		public MoveTool(CoPageItemDragSourceListener l) {
			super(null, CoLayoutEditorPageItemDropTarget.this.m_editor, false, l.getSnappingPageItemView(), l.getTransferablePageItemViews());

			m_dragSourceListener = l;

			double x0 = m_draggedShape.getX() + m_draggedShape.getWidth() / 2;
			double y0 = m_draggedShape.getY() + m_draggedShape.getHeight() / 2;

			Iterator shapes = m_shapes.iterator();
			while (shapes.hasNext()) {
				CoShapeIF s = (CoShapeIF) shapes.next();
				s.translateBy(-x0, -y0);
			}
			m_draggedShapePos.setLocation(m_draggedShapePos.getX() - x0, m_draggedShapePos.getY() - y0);
		}

		protected CoUndoableCommand createReparentCommand(CoShapePageItemView childView, CoCompositePageItemView newParentView, Point2D newPos, boolean doCloneChild) {
			// keep location spec if not absolute
			if (!childView.getShapePageItem().getLocationSpec().isAbsolutePosition())
				newPos = null;
			return super.createReparentCommand(childView, newParentView, newPos, m_dragSourceListener.doCopyOnDrop());
		}

		protected boolean shouldInitDnD(MouseEvent e) {
			return false;
		}
	};

	public CoLayoutEditorPageItemDropTarget(CoLayoutEditor editor) {
		super(editor.getWorkspace());

		m_editor = editor;
	}
	private void calculateDelta(MouseEvent ev) {
		Point p = ev.getComponent().getLocationOnScreen();
		m_dx = p.x;
		m_dy = p.y;
		p = m_component.getLocationOnScreen();
		m_dx -= p.x;
		m_dy -= p.y;
	}
	private void createMoveTool(final CoPageItemDragSourceListener l) {
		//	System.err.println( "new CoMoveTool" );
		m_moveTool = new MoveTool(l);
	}
	protected boolean drag(CoPageItemDragSourceListener l, MouseEvent ev) {
		//	System.err.println( "CoLayoutEditorPageItemDropTarget.drag" );
		super.drag(l, ev);

		getLocalPos(ev);

		MouseEvent e = new MouseEvent(m_component, -1, -1, -1, m_localPosition.x, m_localPosition.y, 0, false);
		m_moveTool.mouseDragged(e);

		if (m_moveTool.getArea(e) == CoAbstractTool.OUTSIDE) {
			return false;
		} else {
			calculateDelta(ev);
			return true;
		}
	}
	protected void dragEnter(CoPageItemDragSourceListener l, MouseEvent ev) {
		CoAssertion.assertTrue(ev.getComponent() != m_component, "Illegal state");

		//	System.err.println( "CoLayoutEditorPageItemDropTarget.dragEnter " );
		super.dragEnter(l, ev);

		calculateDelta(ev);

		createMoveTool(l);
		m_moveTool.activate(new Point(0, 0));
	}
	protected void dragExit(CoPageItemDragSourceListener l, MouseEvent ev) {
		//	System.err.println( "CoLayoutEditorPageItemDropTarget.dragExit" );
		super.dragExit(l, ev);

		m_moveTool.deactivate(getLocalPos(ev));
		m_moveTool = null;

		m_editor.getWorkspace().repaint();
	}
	protected void drop(CoPageItemDragSourceListener l, MouseEvent ev) {
		//	System.err.println( "CoLayoutEditorPageItemDropTarget.drop" );
		getLocalPos(ev);
		m_moveTool.mouseReleased(new MouseEvent(m_component, -1, -1, -1, m_localPosition.x, m_localPosition.y, 0, false));
		m_moveTool.deactivate(m_localPosition);
		m_moveTool = null;
	}
	private Point getLocalPos(MouseEvent ev) {
		m_localPosition.setLocation(ev.getPoint());
		m_localPosition.translate(m_dx, m_dy);
		return m_localPosition;
	}
}