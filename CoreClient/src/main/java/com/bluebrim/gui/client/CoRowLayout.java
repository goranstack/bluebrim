package com.bluebrim.gui.client;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Point;

import javax.swing.SizeRequirements;

/*
	A layout manager that places components top justified in a single row.
	The configuration possibilities are:
	Sticky - If set all components are stretched to the height of the highest one (default = false).
	Spacing - Spacing between the components (default = 0).
	Fill - If the constant FILL is passed as layout constraint ( in the call to Container.add( Component, Object ) )
	       then that particular component will be stretched horizontally to fill the container (default = none).
	       If the constant FILL is passed as layout constraint for several components in the same container
	       the last call will overwrite the state set by the previous ones.
*/

public class CoRowLayout implements LayoutManager2 {
	public static final String FILL = "FILL";

	private static final boolean DEBUG = false;

	private int m_spacing;
	private boolean m_sticky = false;
	private Component m_fillComponent;

	private transient SizeRequirements[] m_w;
	private transient SizeRequirements m_H;
	private transient SizeRequirements m_W;

	public CoRowLayout() {
		this(0);
	}
	public CoRowLayout(int spacing) {
		this(spacing, false);
	}
	public CoRowLayout(int spacing, boolean sticky) {
		m_spacing = spacing;
		m_sticky = sticky;
	}
	public CoRowLayout(boolean sticky) {
		this(0, sticky);
	}
	public void addLayoutComponent(Component comp, Object constraints) {
		if (constraints == FILL) {
			m_fillComponent = comp;
		}
	}
	public void addLayoutComponent(String name, Component comp) {
		if (name == FILL) {
			m_fillComponent = comp;
		}
	}
	void checkRequests(Container target) {
		if (m_W == null) {
			int I = target.getComponentCount();

			m_H = new SizeRequirements();
			m_W = new SizeRequirements();
			m_w = new SizeRequirements[I];
			for (int i = 0; i < I; i++) {
				m_w[i] = new SizeRequirements();
			}

			for (int i = 0; i < I; i++) {
				Component c = target.getComponent(i);

				Dimension min = c.getMinimumSize();
				Dimension typ = c.getPreferredSize();
				Dimension max = c.getMaximumSize();

				m_w[i].minimum = (int) min.width;
				m_w[i].preferred = (int) typ.width;
				m_w[i].maximum = (int) max.width;

				m_H.minimum = (int) Math.max((long) m_H.minimum, min.height);
				m_H.preferred = (int) Math.max((long) m_H.preferred, typ.height);
				m_H.maximum = (int) Math.max((long) m_H.maximum, max.height);

				m_W.minimum += m_w[i].minimum + ((i == 0) ? 0 : m_spacing);
				m_W.preferred += m_w[i].preferred + ((i == 0) ? 0 : m_spacing);
				m_W.maximum += m_w[i].maximum + ((i == 0) ? 0 : m_spacing);

				if (m_W.minimum < 0)
					m_W.minimum = Integer.MAX_VALUE;
				if (m_W.preferred < 0)
					m_W.preferred = Integer.MAX_VALUE;
				if (m_W.maximum < 0)
					m_W.maximum = Integer.MAX_VALUE;
			}
		}
	}
	public float getLayoutAlignmentX(Container target) {
		checkRequests(target);
		return m_W.alignment;
	}
	public float getLayoutAlignmentY(Container target) {
		checkRequests(target);
		return m_H.alignment;
	}
	public void invalidateLayout(Container target) {
		m_W = null;
		m_w = null;
		m_H = null;
	}
	public void layoutContainer(Container target) {
		checkRequests(target);

		int I = target.getComponentCount();

		// determine the child placements
		Dimension alloc = target.getSize();

		Insets in = target.getInsets();

		long H = (m_fillComponent != null) ? Math.max((long) m_H.preferred, (long) target.getBounds().getHeight()) : (long) m_H.preferred;
		long X = 0;
		int n = -1;
		for (int i = 0; i < I; i++) {
			Component c = target.getComponent(i);
			if (c == m_fillComponent)
				n = i;

			int x = (int) Math.min(X + (long) in.left, Short.MAX_VALUE);
			int y = (int) Math.min((long) in.top, Short.MAX_VALUE);
			int w = (int) Math.min((long) m_w[i].preferred, Short.MAX_VALUE);
			int h = m_sticky ? (int) Math.min(H, Short.MAX_VALUE) : c.getPreferredSize().height;
			X += w + m_spacing;

			if (DEBUG) {
				System.err.print(x);
				System.err.print(" ");
				System.err.print(y);
				System.err.print(" ");
				System.err.print(w);
				System.err.print(" ");
				System.err.print(h);
				System.err.print(" ::: ");
				System.err.println(c);
			}

			c.setBounds(x, y, w, h);
		}

		X -= m_spacing;
		long W = (long) target.getBounds().getWidth();
		if ((m_fillComponent != null) && (W > X)) {
			Dimension d = m_fillComponent.getSize();
			int dW = (int) (W - X);
			d.width += dW;
			m_fillComponent.setSize(d);
			for (int i = n + 1; i < I; i++) {
				Component c = target.getComponent(i);
				Point p = c.getLocation();
				p.x += dW;
				c.setLocation(p);
			}
		}
	}
	public Dimension maximumLayoutSize(Container target) {
		checkRequests(target);
		Dimension size = new Dimension(m_W.maximum, m_H.maximum);
		Insets insets = target.getInsets();
		size.width = (int) Math.min((long) size.width + (long) insets.left + (long) insets.right, Short.MAX_VALUE);
		size.height = (int) Math.min((long) size.height + (long) insets.top + (long) insets.bottom, Short.MAX_VALUE);
		return size;
	}
	public Dimension minimumLayoutSize(Container target) {
		checkRequests(target);
		Dimension size = new Dimension(m_W.minimum, m_H.minimum);
		Insets insets = target.getInsets();
		size.width = (int) Math.min((long) size.width + (long) insets.left + (long) insets.right, Short.MAX_VALUE);
		size.height = (int) Math.min((long) size.height + (long) insets.top + (long) insets.bottom, Short.MAX_VALUE);
		return size;
	}
	public Dimension preferredLayoutSize(Container target) {
		checkRequests(target);
		Dimension size = new Dimension(m_W.preferred, m_H.preferred);
		Insets insets = target.getInsets();
		size.width = (int) Math.min((long) size.width + (long) insets.left + (long) insets.right, Short.MAX_VALUE);
		size.height = (int) Math.min((long) size.height + (long) insets.top + (long) insets.bottom, Short.MAX_VALUE);
		return size;
	}
	public void removeLayoutComponent(Component comp) {
		if (m_fillComponent == comp)
			m_fillComponent = null;
	}
}