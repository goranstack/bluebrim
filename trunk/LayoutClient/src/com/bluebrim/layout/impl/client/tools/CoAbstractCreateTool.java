package com.bluebrim.layout.impl.client.tools;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.text.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.bluebrim.base.shared.geom.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.observable.*;

/**
 * Abstract subclass for tools that create page items
 * 
 * @author Dennis Malmström
 */
public class CoAbstractCreateTool extends CoAbstractTool implements Icon {
	private static List m_instances = new ArrayList(); // [ CoAbstractCreateTool ]

	protected CoPageItemPrototypeIF m_prototype; // creating a page item isdone by cloning from this prototype
	private CoShapePageItemView m_prototypeView; // a view of the prototype is needed for rubberbanding

	protected Cursor m_originalCursor;

	protected Point2D m_mousePos;
	protected CoTargetViewKeeper m_targetTracker;
	protected Point2D m_shapePos;

	// crosshair cursor that snaps
	protected final Line2D m_extraCursorLine1 = new Line2D.Double();
	protected final Line2D m_extraCursorLine2 = new Line2D.Double();

	// prototype view cache
	private static Map m_prototypeViews = new HashMap(); // [ com.bluebrim.gemstone.shared.CoRef (page item id) -> CoShapePageItemView ]

	public CoAbstractCreateTool(CoPageItemPrototypeIF prototype, CoTool previousTool, CoLayoutEditor pageItemEditor) {
		super(previousTool, pageItemEditor);

		m_prototype = prototype;
		m_viewKey = m_prototype.getPageItemId();
		updatePrototypeView();

		m_instances.add(this);
	}

	public void activate(Point2D pos) {
		super.activate(pos);

		m_targetTracker = new CoTargetViewKeeper(this) {
			protected CoCompositePageItemView checkValidTargetView(CoCompositePageItemView v) {
				if ((v != null) && v.areChildrenLocked())
					return null;
				return v;
			}
		};

		m_viewPanel.setCursor(m_viewPanel.getRootView().getSelectionCursor());
	}

	protected void drawExtraCursor() {
		if (m_shapePos == null)
			return;

		if (m_targetTracker.getTargetView() == null)
			return;

		Graphics2D g = getXORGraphics();

		double x = m_shapePos.getX();
		double y = m_shapePos.getY();

		double d = 10 / m_viewPanel.getScreenScale();

		m_extraCursorLine1.setLine(x - d, y, x + d, y);
		m_extraCursorLine2.setLine(x, y - d, x, y + d);

		g.draw(m_extraCursorLine1);
		g.draw(m_extraCursorLine2);
	}

	protected CoTool getNextTool(int modifiers) {
		return ((modifiers & InputEvent.CTRL_MASK) != 0) ? this : null;
	}

	public CoPageItemPrototypeIF getPrototype() {
		return m_prototype;
	}

	public CoShapePageItemView getView() {
		return m_prototypeView;
	}

	public CoTool mouseEntered(MouseEvent e) {
		drawExtraCursor();

		return this;
	}

	public CoTool mouseExited(MouseEvent e) {
		drawExtraCursor();

		return this;
	}

	public CoTool mouseMoved(MouseEvent e) {
		drawExtraCursor();

		CoCompositePageItemView v =
			(CoCompositePageItemView) m_viewPanel.getRootView().findTopMostViewContaining(getLocation(e), null, true, false, -1);

		if (v.validateAdd(getView())) {
			m_targetTracker.setTargetView(v);
		} else {
			m_targetTracker.setTargetView(null);
		}

		m_mousePos = getLocation(e);

		m_viewPanel.untransform(m_mousePos);
		snap(m_mousePos);

		drawExtraCursor();

		return this;
	}

	protected void snap(Point2D p) {
		CoCompositePageItemView targetView = m_targetTracker.getTargetView();

		if ((targetView != null) && m_editor.getSnapToGrid()) // snap activated ?
			{
			Point2D d = new Point2D.Double(Double.MAX_VALUE, Double.MAX_VALUE);
			m_shapePos = p;
			m_shapePos =
				m_viewPanel.getRootView().snap(
					m_shapePos.getX(),
					m_shapePos.getY(),
					getSnapRange(),
					CoGeometryConstants.ALL_EDGE_MASK,
					CoGeometryConstants.ALL_DIRECTIONS_MASK,
					true,
					d);

			targetView.transformFromGlobal(m_shapePos);

			m_shapePos =
				targetView.snap(
					m_shapePos.getX(),
					m_shapePos.getY(),
					getSnapRange(),
					CoGeometryConstants.ALL_EDGE_MASK,
					CoGeometryConstants.ALL_DIRECTIONS_MASK,
					true,
					d);

			targetView.transformToGlobal(m_shapePos);
			p.setLocation(m_shapePos.getX(), m_shapePos.getY());
		} else {
			m_shapePos = p;
		}
	}

	private Icon getIcon() {
		return getView();
	}

	protected CoShapePageItemReparentCommand m_reparentCommand = new CoShapePageItemReparentCommand() {
		public void finish() {
			super.finish();
			getViewPanel().getRootView().addRecentlyCreatedPageItem(m_child);
		}
	};

	private static com.bluebrim.observable.CoChangedObjectListener m_toolPrototypeListener =
		new com.bluebrim.observable.CoAbstractChangedObjectListener() {
		public void changedServerObject(com.bluebrim.observable.CoChangedObjectEvent ev) {
			toolPrototypeChanged(ev);
		}
	};

	private Object m_viewKey;

	public void dispose() {
		m_instances.remove(this);
	}

	public int getIconHeight() {
		return getIcon().getIconHeight();
	}

	public int getIconWidth() {
		return getIcon().getIconWidth();
	}

	private static CoShapePageItemView getView(CoPageItemPrototypeIF p, Object viewKey) {
		CoShapePageItemView v = (CoShapePageItemView) m_prototypeViews.get(viewKey);
		if (v == null) {
			v = CoPageItemView.create(p.getPageItem(), null, CoPageItemView.DETAILS_EVERYTHING);

			CoObservable.addChangedObjectListener(m_toolPrototypeListener, p);
			m_prototypeViews.put(viewKey, v);
		}

		return v;
	}

	public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
		getIcon().paintIcon(c, g, x, y);
	}

	private static void toolPrototypeChanged(com.bluebrim.observable.CoChangedObjectEvent e) {
		CoPageItemPrototypeIF p = (CoPageItemPrototypeIF) e.getChangedObject();

		Object viewKey = p.getPageItemId();

		m_prototypeViews.remove(viewKey);

		getView(p, viewKey); // in case m_instances is empty

		Iterator i = m_instances.iterator();
		while (i.hasNext()) {
			((CoAbstractCreateTool) i.next()).updatePrototypeView();
		}
	}

	private void updatePrototypeView() {
		CoShapePageItemView old = m_prototypeView;
		m_prototypeView = getView(m_prototype, m_viewKey);

		if (old != m_prototypeView)
			m_editor.getPanel().repaint();
	}

	public String getName() {
		Object[] params = {m_prototype.getPageItem().getType()};
		return MessageFormat.format(CoPageItemToolResources.getName( CoPageItemToolConstants.CREATE_TOOL_NAME ),params);

	}

}