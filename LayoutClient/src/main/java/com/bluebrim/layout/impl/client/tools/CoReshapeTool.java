package com.bluebrim.layout.impl.client.tools;

import java.awt.event.*;
import java.awt.geom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.geom.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.client.view.*;
import com.bluebrim.layout.impl.shared.view.*;

/** 
 * Implementation of "reshape mode" (used for moving reshape handles)
 * 
 * @author: Dennis Malmström
 */

public class CoReshapeTool extends CoAbstractTool {
	CoShapePageItemView m_view;
	CoReshapeHandleIF m_handle;

	CoShapeIF m_draggedShape;

	boolean m_wasDragged;

	// original rotation point
	double m_X;
	double m_Y;

	public CoReshapeTool(
		CoTool previousTool,
		CoLayoutEditor pageItemEditor,
		boolean keepAspectRatio,
		boolean isContentSticky,
		CoSelectedView v,
		CoReshapeHandleIF handle) {
		super(previousTool, pageItemEditor);

		// initialize state
		m_view = v.getView();
		m_handle = handle;
		m_draggedShape = v.getShape();
		m_draggedShape.setEditMode(true);
		m_draggedShape.setKeepAspectRatio(keepAspectRatio);

		m_X = m_draggedShape.getWidth() / 2;
		m_Y = m_draggedShape.getHeight() / 2;

		if (isContentSticky && (m_view instanceof CoContentWrapperPageItemView)) {
			CoPageItemContentView cv = ((CoContentWrapperPageItemView) m_view).getContentView();
			if (cv instanceof CoPageItemBoundedContentView) {
				m_boundedContentView = (CoPageItemBoundedContentView) cv;
			}
		}

	}

	public void activate(Point2D pos) {
		super.activate(pos);

		// xor-draw rubberband shape
		xorDraw();
	}
	public void deactivate(Point2D pos) {
		super.deactivate(pos);

		m_boundedContentView = null;
	}

	public CoTool mouseDragged(MouseEvent e) {
		// xor-draw rubberband shape
		xorDraw();

		// mouse position (unzoomed)
		Point2D mousePosition = getLocation(e);
		m_viewPanel.untransform(mousePosition);

		// snap mouse position
		if (m_editor.getSnapToGrid() && !m_viewPanel.getRootView().isModelView(m_view) // desktop has no snap grid
		) {
			// transform mousePosition to local koordinates
			Point2D d = new Point2D.Double(Double.MAX_VALUE, Double.MAX_VALUE);
			mousePosition =
				m_viewPanel.getRootView().snap(
					mousePosition.getX(),
					mousePosition.getY(),
					getSnapRange(),
					m_handle.getEdgeMask(),
					CoGeometryConstants.ALL_DIRECTIONS_MASK,
					true,
					d);

			m_view.getParent().transformFromGlobal(mousePosition);
			mousePosition =
				m_view.getParent().snap(
					mousePosition.getX(),
					mousePosition.getY(),
					getSnapRange(),
					m_handle.getEdgeMask(),
					CoGeometryConstants.ALL_DIRECTIONS_MASK,
					true,
					d);

			// transform mousePosition to global koordinates
			m_view.getParent().transformToGlobal(mousePosition);
		}

		m_view.transformFromGlobal(mousePosition);

		// calculate movement
		double dx = mousePosition.getX() - m_handle.getX();
		double dy = mousePosition.getY() - m_handle.getY();

		boolean didAutoscroll = autoScroll(e);

		// apply movement
		double w0 = m_draggedShape.getWidth();
		double h0 = m_draggedShape.getHeight();

		m_handle.move(dx, dy);

		if (m_boundedContentView != null) {
			double w1 = m_draggedShape.getWidth();
			double h1 = m_draggedShape.getHeight();
			m_boundedContentView.scaleContent(w1 / w0, h1 / h0);
		}

		// xor-draw rubberband shape
		xorDraw();

		m_wasDragged = true;
		return this;
	}

	public CoTool mouseReleased(MouseEvent e) {
		if (m_wasDragged) {
			// make sure rubberband shape has positive dimensions
			m_draggedShape.normalize();

			// calculate translation
			double dx = m_draggedShape.getX();
			double dy = m_draggedShape.getY();

			// handle rotation
			if (!m_view.getTransform().isIdentity()) {
				// new rotation point
				double x = m_draggedShape.getWidth() / 2.0 + m_draggedShape.getX();
				double y = m_draggedShape.getHeight() / 2.0 + m_draggedShape.getY();

				// delta
				Point2D p = new Point2D.Double(x - m_X, y - m_Y);

				// rotate delta
				AffineTransform t = AffineTransform.getRotateInstance(m_view.getTransform().getRotation());
				t.transform(p, p);

				// position delta
				dx = p.getX() + m_X - m_draggedShape.getWidth() / 2;
				dy = p.getY() + m_Y - m_draggedShape.getHeight() / 2;
			}

			// apply translation to page item
			m_draggedShape.setEditMode(false);
			m_draggedShape = m_draggedShape.deepClone();
			m_draggedShape.setTranslation(dx, dy);

			CoPageItemCommands.RESHAPE.prepare(m_view, m_draggedShape, m_boundedContentView != null);
			m_editor.getCommandExecutor().doit(CoPageItemCommands.RESHAPE, null);
		}

		m_draggedShape.setKeepAspectRatio(false);

		return m_previousTool;
	}

	private void xorDraw() {
		java.awt.Graphics2D g = getXORGraphics();
		AffineTransform t = g.getTransform();

		g.transform(m_view.getAffineTransform());

		g.draw(m_draggedShape.getShape());

		g.setTransform(t);
	}

	CoPageItemBoundedContentView m_boundedContentView;
}