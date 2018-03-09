package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * @author Markus Persson
 */
public class CoViewIcon implements Icon {
	private Dimension m_size;
	protected CoView m_view;
	private	SimpleFixedBoundsWrapper m_simple = new SimpleFixedBoundsWrapper(true);


//	private	CoFixedBoundsWrapper m_transformable = new CoScalingFixedBoundsWrapper(true,true);

	public class SimpleFixedBoundsWrapper extends CoView {
		private CoView m_child;
		private boolean m_centered;
		private Dimension2D m_childSize;

		public SimpleFixedBoundsWrapper(boolean centered) {
			this(null, null, centered);
		}

		public SimpleFixedBoundsWrapper(CoShapePageItemView child, Dimension2D childSize, boolean centered) {
			setChild(child);
			setChildSize(childSize);
			setCentered(centered);
		}

		public double getHeight() {
			return m_childSize.getHeight();
		}

		public double getWidth() {
			return m_childSize.getWidth();
		}

		public void paint(CoPaintable g) {
			if (m_child != null) {
				double s = 1;
				if (m_childSize != null) {
					double sx = m_childSize.getWidth() / (m_child.getWidth());
					double sy = m_childSize.getHeight() / (m_child.getHeight());
					s = Math.min(sx, sy);
				}
//				AffineTransform t = g.getTransform();
				double ex = 0;
				double ey = 0;
				if (m_centered) {
					ex = m_childSize.getWidth() - s*m_child.getWidth();
					ey = m_childSize.getHeight() - s*m_child.getHeight();
					g.translate(ex/2, ey/2);
				}
				g.scale(s, s);
				m_child.paint(g);
				g.unscale(s, s);
				if (m_centered) {
					g.untranslate(ex/2, ey/2);
				}
			}
		}

		public final void setChildSize(Dimension2D childSize) {
			m_childSize = childSize;
		}
		
		public final void setChild(CoView child) {
			m_child = child;
		}

		public Container getContainer() {
			return null;
		}

		public final void setCentered(boolean centered) {
			m_centered = centered;
		}
	}
public CoViewIcon(int w, int h) {
	this(null, w, h);
}
public CoViewIcon(CoView view, int w, int h) {
	setSize(new Dimension(w,h));
	setView(view);
}
public int getIconHeight() {
	return m_size.height;
}
public int getIconWidth() {
	return m_size.width;
}
/**
 * PENDING: Use Graphics.create() instead of storing old values? /Markus
 */
public void paintIcon(Component c, Graphics g, int x, int y) {
	Graphics2D g2d = (Graphics2D)g;
	Font oldFont 	= g2d.getFont();
	Object oldOutlineHint 		= g2d.getRenderingHint(CoPageItemViewRenderer.PAINT_OUTLINE);
	Object oldOutlineStyleHint 	= g2d.getRenderingHint(CoPageItemViewRenderer.PAINT_OUTLINE_STYLE);	
	g2d.setRenderingHint(CoPageItemViewRenderer.PAINT_OUTLINE, CoPageItemViewRenderer.PAINT_OUTLINE_ON);
	g2d.setRenderingHint(CoPageItemViewRenderer.PAINT_OUTLINE_STYLE, CoPageItemViewRenderer.PAINT_OUTLINE_SOLID);

	AffineTransform oldTransform = g2d.getTransform();
	// Too leave room for outline.
	g2d.translate(x + 1, y + 1);
	m_view.paint(CoScreenPaintable.wrap(g2d));
	g2d.setTransform(oldTransform);

	g2d.setFont(oldFont);
	if (oldOutlineHint != null) {
		g2d.setRenderingHint(CoPageItemViewRenderer.PAINT_OUTLINE, oldOutlineHint);
	}
	if (oldOutlineStyleHint != null) {
		g2d.setRenderingHint(CoPageItemViewRenderer.PAINT_OUTLINE_STYLE, oldOutlineStyleHint);
	}	
}
/**
 * To make room for the one pixel outline, the view sizes are set
 * to two pixels less than the icon's.
 */
public final void setSize(Dimension size) {
	m_size = size;
	
	Dimension viewSize = new Dimension(size.width - 2, size.height - 2);
	m_simple.setChildSize(viewSize);
//	m_transformable.setChildSize(viewSize);
}
public final void setView(CoView view) {
	/*
	if (view instanceof CoShapePageItemView) {
		m_transformable.setChild((CoShapePageItemView)view);
		m_view = m_transformable;
	} else {
	*/
		m_simple.setChild(view);
		m_view = m_simple;
//	}
}
}