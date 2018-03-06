package com.bluebrim.gui.client;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.SwingConstants;

import com.bluebrim.swing.client.CoPanel;
/**
 * Implements scrolling one page for block and a 1/10 of that for wheel or button
 * The LayoutManager handels linebreaking and you can set upp rows with setRowLength()
 * Creation date: (2001-11-13 13:42:21)
 * @author Arvid Berg
 */
public class CoScrollableFlowPanel extends CoPanel implements javax.swing.Scrollable {

	public static class FlowLayoutManager implements LayoutManager2 {
		java.util.List m_rows = new ArrayList();
		int m_height = 0;
		int m_width = 1;
		int m_count = 0;
		/**
		*/
		private void addComp(java.awt.Component comp) {
			m_width = Math.max(m_width, comp.getPreferredSize().width);
			m_height = Math.max(m_height, comp.getPreferredSize().height);
			m_count++;
		}
		public void addLayoutComponent(java.awt.Component comp, Object constraints) {
			addComp(comp);
		}
		public void addLayoutComponent(String name, java.awt.Component comp) {
			addComp(comp);
		}
		public float getLayoutAlignmentX(java.awt.Container target) {
			return 0;
		}
		public float getLayoutAlignmentY(java.awt.Container target) {
			return 0;
		}
		public void invalidateLayout(java.awt.Container target) {
		}
		public void layoutContainer(java.awt.Container parent) {
			layouter(parent, true);
		}
		/** Used both for calculationg preferredsize and layouting component
		*/
		private Dimension layouter(java.awt.Container parent, boolean layout) {
			int xWidth = parent.getSize().width;
			// Adjusts for equal spread
			int xGap = m_width == 0 ? 0 : (xWidth % m_width) / (m_count + 1);
			int count = parent.getComponentCount();
			int width = 0;
			int rWidth = 0;
			int height = 0;
			int wCount = 0;
			int x = xGap;
			int y = 0;
			int rowCount = 0;
			int length = 0;
			boolean linebreak = false;
			Component[] comp = parent.getComponents();
			int rc = xWidth / m_width; // calculates how many children per row
			xGap = ((xWidth % m_width)) / (rc + 1);
			x = xGap;
			for (int i = 0; i < count; i++) {
				// rows				
				if (layout)
					comp[i].setBounds(x, y, m_width, m_height);
				x += m_width + xGap;
				width += m_width + xGap;
				wCount++;
				length++;
				// if it is a end of a logic row then break
				linebreak = ((rowCount <= m_rows.size() - 1) && (length >= ((Integer) m_rows.get(rowCount)).intValue()));
				// logic of physical linebreak
				if (linebreak || wCount >= rc) {
					height += m_height;
					y += m_height;
					x = xGap;
					rWidth = Math.max(width, rWidth);
					wCount = 0;
					width = 0;
					if (linebreak) {
						rowCount++;
						length = 0;
						linebreak = false;
					}
				}
			}
			if (layout)
				return new Dimension(rWidth, height);
			else
				return new Dimension(m_width, height);
		}
		public java.awt.Dimension maximumLayoutSize(java.awt.Container target) {
			int count = target.getComponentCount();
			int width = 0;
			int height = 0;
			Component[] comp = target.getComponents();
			for (int i = 0; i < count; i++) {
				Dimension dim = comp[i].getPreferredSize();
				width += dim.width;
				height = Math.max(height, dim.height);
			}
			//return new Dimension(width, height);
			return new Dimension(100, 100);
		}
		public java.awt.Dimension minimumLayoutSize(java.awt.Container target) {
			/*int count = target.getComponentCount();
			int width = 0;
			int height = 0;
			Component[] comp = target.getComponents();
			for (int i = 0; i < count; i++) {
				Dimension dim = comp[i].getPreferredSize();
				height += dim.height;
				width = Math.max(width, dim.width);
			}
			//return new Dimension(width, height);
			//return new Dimension(m_width, m_height);*/
			return new Dimension(m_width,m_height);
		}
		public java.awt.Dimension preferredLayoutSize(java.awt.Container target) {
			return layouter(target, false);
			//return new Dimension(100,100);
		}
		public void removeLayoutComponent(java.awt.Component comp) {
			m_count--;
		}
		public void resetRows() {
			m_rows.clear();
		}
		public void setRowLength(int row, int length) {
			if (m_rows.size() == 0 || row >= m_rows.size())
				m_rows.add(row, new Integer(length));
			else
				m_rows.set(row, new Integer(length));
		}
	}
	public CoScrollableFlowPanel() {
		super(new FlowLayoutManager());
	}
	public CoScrollableFlowPanel(java.awt.Insets extraInsets) {
		super(new FlowLayoutManager(),extraInsets);
	}
	public CoScrollableFlowPanel(java.awt.LayoutManager layoutManager) {
		super(layoutManager);
	}
	public CoScrollableFlowPanel(java.awt.LayoutManager layoutManager, java.awt.Insets extraInsets) {
		super(layoutManager, extraInsets);
	}
	public CoScrollableFlowPanel(java.awt.LayoutManager layoutManager, boolean isDoubleBuffered) {
		super(layoutManager, isDoubleBuffered);
	}
	public CoScrollableFlowPanel(java.awt.LayoutManager layoutManager, boolean isDoubleBuffered, java.awt.Insets extraInsets) {
		super(layoutManager, isDoubleBuffered, extraInsets);
	}
	public CoScrollableFlowPanel(boolean isDoubleBuffered) {
		super(new FlowLayoutManager(),isDoubleBuffered);
	}
	public java.awt.Dimension getPreferredScrollableViewportSize() {
		
		return this.getPreferredSize();
	}
	public int getScrollableBlockIncrement(java.awt.Rectangle visibleRect, int orientation, int direction) {
		if (orientation == SwingConstants.HORIZONTAL)
			return (int) visibleRect.getWidth();
		else
			return (int) visibleRect.getHeight();
	}
/** 
*/	
public boolean getScrollableTracksViewportHeight() {
	return false;
}
/** Adapts the panel width to folow the vieports width
 *  if preferred width is less than viewprot width
 */
public boolean getScrollableTracksViewportWidth() {
	return true;
}
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return getScrollableBlockIncrement(visibleRect, orientation, direction) / 10;
	}
}
