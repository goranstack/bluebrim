package com.bluebrim.swing.client;

import java.awt.*;

public class CoBorderLayout implements LayoutManager2, java.io.Serializable {
	private int m_hgap;
	private int m_vgap;
	private Component m_north;
	private Component m_west;
	private Component m_east;
	private Component m_south;
	private Component m_center;

	public static final String NORTH = "North";
	public static final String SOUTH = "South";
	public static final String EAST = "East";
	public static final String WEST = "West";
	public static final String CENTER = "Center";

	public CoBorderLayout() {
		this(0, 0);
	}
	public CoBorderLayout(int hgap, int vgap) {
		m_hgap = hgap;
		m_vgap = vgap;
	}
	public void addLayoutComponent(Component comp, Object constraints) {
		synchronized (comp.getTreeLock()) {
			if ((constraints == null) || (constraints instanceof String)) {
				addLayoutComponent((String) constraints, comp);
			} else {
				throw new IllegalArgumentException("cannot add to layout: constraint must be a string (or null)");
			}
		}
	}
	public void addLayoutComponent(String name, Component comp) {
		synchronized (comp.getTreeLock()) {
			if (name == null) {
				name = CENTER;
			}

			if (CENTER.equals(name)) {
				m_center = comp;
			} else if (NORTH.equals(name)) {
				m_north = comp;
			} else if (SOUTH.equals(name)) {
				m_south = comp;
			} else if (EAST.equals(name)) {
				m_east = comp;
			} else if (WEST.equals(name)) {
				m_west = comp;
			} else {
				throw new IllegalArgumentException("cannot add to layout: unknown constraint: " + name);
			}
		}
	}
	private Component getChild(String key) {
		Component result = null;
		if (key == NORTH) {
			result = m_north;
		} else if (key == SOUTH) {
			result = m_south;
		} else if (key == WEST) {
			result = m_west;
		} else if (key == EAST) {
			result = m_east;
		} else if (key == CENTER) {
			result = m_center;
		}
		if (result != null && !result.isVisible()) {
			result = null;
		}
		return result;
	}
	public int getHgap() {
		return m_hgap;
	}
	public float getLayoutAlignmentX(Container parent) {
		return 0.5f;
	}
	public float getLayoutAlignmentY(Container parent) {
		return 0.5f;
	}
	public int getVgap() {
		return m_vgap;
	}
	public void invalidateLayout(Container target) {
	}
	public void layoutContainer(Container target) {
		synchronized (target.getTreeLock()) {
			Insets insets = target.getInsets();
			int top = insets.top;
			int bottom = target.getHeight() - insets.bottom;
			int left = insets.left;
			int right = target.getWidth() - insets.right;

			Component c = null;
			if ((c = getChild(NORTH)) != null) {
				c.setSize(right - left, c.getHeight());
				Dimension d = c.getPreferredSize();
				c.setBounds(left, top, right - left, d.height);
				top += d.height + m_vgap;
			}
			if ((c = getChild(SOUTH)) != null) {
				c.setSize(right - left, c.getHeight());
				Dimension d = c.getPreferredSize();
				c.setBounds(left, bottom - d.height, right - left, d.height);
				bottom -= d.height + m_vgap;
			}
			if ((c = getChild(EAST)) != null) {
				c.setSize(c.getWidth(), bottom - top);
				Dimension d = c.getPreferredSize();
				c.setBounds(right - d.width, top, d.width, bottom - top);
				right -= d.width + m_hgap;
			}
			if ((c = getChild(WEST)) != null) {
				c.setSize(c.getWidth(), bottom - top);
				Dimension d = c.getPreferredSize();
				c.setBounds(left, top, d.width, bottom - top);
				left += d.width + m_hgap;
			}
			if ((c = getChild(CENTER)) != null) {
				c.setBounds(left, top, right - left, bottom - top);
			}
		}
	}
	public Dimension maximumLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {

			long width = 0;
			long height = 0;

			Component c = null;
			if ((c = getChild(EAST)) != null) {
				Dimension d = c.getMaximumSize();
				width += d.width + m_hgap;
				height = Math.max(d.height, height);
			}
			if ((c = getChild(WEST)) != null) {
				Dimension d = c.getMaximumSize();
				width += d.width + m_hgap;
				height = Math.max(d.height, height);
			}
			if ((c = getChild(CENTER)) != null) {
				Dimension d = c.getMaximumSize();
				width += d.width;
				height = Math.max(d.height, height);
			}
			if ((c = getChild(NORTH)) != null) {
				Dimension d = c.getMaximumSize();
				width = Math.max(d.width, width);
				height += d.height + m_vgap;
			}
			if ((c = getChild(SOUTH)) != null) {
				Dimension d = c.getMaximumSize();
				width = Math.max(d.width, width);
				height += d.height + m_vgap;
			}
			Insets insets = target.getInsets();
			width += insets.left + insets.right;
			height += insets.top + insets.bottom;
			return new Dimension((int) Math.min((long) Integer.MAX_VALUE, width), (int) Math.min((long) Integer.MAX_VALUE, height));
		}
	}
	public Dimension minimumLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Dimension dim = new Dimension(0, 0);

			Component c = null;
			if ((c = getChild(EAST)) != null) {
				Dimension d = c.getMinimumSize();
				dim.width += d.width + m_hgap;
				dim.height = Math.max(d.height, dim.height);
			}
			if ((c = getChild(WEST)) != null) {
				Dimension d = c.getMinimumSize();
				dim.width += d.width + m_hgap;
				dim.height = Math.max(d.height, dim.height);
			}
			if ((c = getChild(CENTER)) != null) {
				Dimension d = c.getMinimumSize();
				dim.width += d.width;
				dim.height = Math.max(d.height, dim.height);
			}
			if ((c = getChild(NORTH)) != null) {
				Dimension d = c.getMinimumSize();
				dim.width = Math.max(d.width, dim.width);
				dim.height += d.height + m_vgap;
			}
			if ((c = getChild(SOUTH)) != null) {
				Dimension d = c.getMinimumSize();
				dim.width = Math.max(d.width, dim.width);
				dim.height += d.height + m_vgap;
			}
			Insets insets = target.getInsets();
			dim.width += insets.left + insets.right;
			dim.height += insets.top + insets.bottom;
			return dim;
		}
	}
	public Dimension preferredLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Dimension dim = new Dimension(0, 0);

			Component c = null;
			if ((c = getChild(EAST)) != null) {
				Dimension d = c.getPreferredSize();
				dim.width += d.width + m_hgap;
				dim.height = Math.max(d.height, dim.height);
			}
			if ((c = getChild(WEST)) != null) {
				Dimension d = c.getPreferredSize();
				dim.width += d.width + m_hgap;
				dim.height = Math.max(d.height, dim.height);
			}
			if ((c = getChild(CENTER)) != null) {
				Dimension d = c.getPreferredSize();
				dim.width += d.width;
				dim.height = Math.max(d.height, dim.height);
			}
			if ((c = getChild(NORTH)) != null) {
				Dimension d = c.getPreferredSize();
				dim.width = Math.max(d.width, dim.width);
				dim.height += d.height + m_vgap;
			}
			if ((c = getChild(SOUTH)) != null) {
				Dimension d = c.getPreferredSize();
				dim.width = Math.max(d.width, dim.width);
				dim.height += d.height + m_vgap;
			}
			Insets insets = target.getInsets();
			dim.width += insets.left + insets.right;
			dim.height += insets.top + insets.bottom;
			return dim;
		}
	}
	public void removeLayoutComponent(Component comp) {
		synchronized (comp.getTreeLock()) {
			if (comp == m_center) {
				m_center = null;
			} else if (comp == m_north) {
				m_north = null;
			} else if (comp == m_south) {
				m_south = null;
			} else if (comp == m_east) {
				m_east = null;
			} else if (comp == m_west) {
				m_west = null;
			}
		}
	}
	public void setHgap(int hgap) {
		m_hgap = hgap;
	}
	public void setVgap(int vgap) {
		m_vgap = vgap;
	}
	public String toString() {
		return getClass().getName() + "[hgap=" + m_hgap + ",vgap=" + m_vgap + "]";
	}
}
