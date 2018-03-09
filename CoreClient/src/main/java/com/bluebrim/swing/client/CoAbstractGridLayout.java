package com.bluebrim.swing.client;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;

/**
 * NOTE: The comments for this class is not up to date yet.
 * @author: Markus Persson 1999-06-04
 *
 * Class not fully functional, yet!
 *
 * A flow layout arranges components in a left-to-right flow, much 
 * like lines of text in a paragraph. Flow layouts are typically used 
 * to arrange buttons in a panel. It will arrange
 * buttons left to right until no more buttons fit on the same line.
 * <p>
 * The grid is determined using the components natural (preferred) size.
 * For each column, the width of its widest component becomes the width
 * of the column. The heights of the rows are determined in a similar
 * fashion.
 *
 * A flow layout lets each component assume its natural (preferred) size. 
 *
 */
public abstract class CoAbstractGridLayout extends CoAbstractLayoutManager2 {
	// Alignment constants.
	// PENDING: Different constants for LEFT and TOP to dectect mistakes?
	public static final int LEFT 			= 0;
	public static final int TOP 			= 0;
	public static final int CENTER 			= 1;
	public static final int RIGHT 			= 2;
	public static final int BOTTOM 			= 2;

	// Used to mask out the position from alignment fields.
	public static final int POSITION_MASK	= LEFT | TOP | CENTER | RIGHT | BOTTOM;

	// Must be even powers of two, greater than the above.
	public static final int FILL 			= 4;
	// Not final. Means the grid fills up all available space ... somehow.
	public static final int NOSLACK			= 8;

	public static final int LEFT_FILL 		= LEFT | FILL;
	public static final int TOP_FILL 		= TOP | FILL;
	public static final int CENTER_FILL 	= CENTER | FILL;
	public static final int RIGHT_FILL 		= RIGHT | FILL;
	public static final int BOTTOM_FILL 	= BOTTOM | FILL;

	public static final int LEFT_NOSLACK 	= LEFT | FILL | NOSLACK;
	public static final int TOP_NOSLACK 	= TOP | FILL | NOSLACK;
	public static final int CENTER_NOSLACK 	= CENTER | FILL | NOSLACK;
	public static final int RIGHT_NOSLACK 	= RIGHT | FILL | NOSLACK;
	public static final int BOTTOM_NOSLACK 	= BOTTOM | FILL | NOSLACK;

	protected int hAlign;
	protected int vAlign;
	protected int hGap;
	protected int vGap;

	// Cached state
	protected GridState m_cachedState;

	// Nested class to hold state information.
	protected static class GridState {
		public int columns;
		public int[] columnWidths;
		public int rows;
		public Dimension size;

		public GridState(int numVisible, int columns, int columnWidths[], Dimension size) {
			this.columns = columns;
			this.columnWidths = columnWidths;
			this.rows = (numVisible + columns - 1) / columns;
			this.size = size;
		}
	}

/**
 * Constructs a new CoGridFlowLayout with a horizontal left fill and
 * vertical top alignment and a default 5-unit horizontal and
 * vertical gap.
 */
public CoAbstractGridLayout() {
	this(LEFT_FILL, TOP, 5, 5);
}
/**
 * <p>Constructs a new CoGridFlowLayout with the specified alignment and a
 * default 5-unit horizontal and vertical gap.</p>
 * <p>The values of the alignment arguments must either be one of 
 * <code>CoGridFlowLayout.CENTER</code> or <code>CoGridFlowLayout.FILL</code>
 * or one of the direction specific values:
 * <code>CoGridFlowLayout.LEFT</code> or <code>CoGridFlowLayout.RIGHT</code>
 * for the horizontal and
 * <code>CoGridFlowLayout.TOP</code> or <code>CoGridFlowLayout.BOTTOM</code>, 
 * for the vertical.</p>
 * @param hAlign the horizontal alignment value
 * @param vAlign the vetical alignment value
 */
public CoAbstractGridLayout(int hAlign, int vAlign) {
	this(hAlign, vAlign, 5, 5);
}
/**
 * <p>Constructs a new CoGridFlowLayout with the specified alignment
 * and the specified horizontal and vertical gaps.</p>
 * <p>The values of the alignment arguments must either be one of 
 * <code>CoGridFlowLayout.CENTER</code> or <code>CoGridFlowLayout.FILL</code>
 * or one of the direction specific values:
 * <code>CoGridFlowLayout.LEFT</code> or <code>CoGridFlowLayout.RIGHT</code>
 * for the horizontal and
 * <code>CoGridFlowLayout.TOP</code> or <code>CoGridFlowLayout.BOTTOM</code>, 
 * for the vertical.</p>
 * @param hAlign the horizontal alignment value
 * @param vAlign the vetical alignment value
 * @param hgap    the horizontal gap between components.
 * @param vgap    the vertical gap between components.
 */
public CoAbstractGridLayout(int hAlign, int vAlign, int hGap, int vGap) {
	this.hAlign = hAlign;
	this.vAlign = vAlign;
	this.hGap = hGap;
	this.vGap = vGap;
}
/** 
 * Adjusts the elements in the specified row vertically, if there is any slack.
 * @param target the container containing components to be moved
 * @param y the y coordinate
 * @param height the height dimensions
 * @param rowStart the first component index of this row
 * @param rowEnd the first component index of the next row
 */
protected void adjustComponentsV(Container target, int y, int height, int rowStart, int rowEnd) {
	synchronized (target.getTreeLock()) {
		for (int i = rowStart; i < rowEnd; i++) {
			Component m = target.getComponent(i);
			int dy = 0;
			if (m.isVisible()) {
				Dimension s = m.getSize();
				Point pos = m.getLocation();
				if ((vAlign & FILL) == FILL) {
					Dimension max = m.getMaximumSize();
					s.height = Math.min(height, max.height);
					m.setSize(s);
				}
				switch (vAlign & POSITION_MASK) {
					default:
					case TOP:
						pos.y = y;
						break;
					case CENTER :
						pos.y = y + (height - s.height)/2;
						break;
					case BOTTOM :
						pos.y = y + (height - s.height);
						break;
				}
				m.setLocation(pos);
			}
		}
	}
}
/**
 * Returns the preferred dimensions for this layout given the components
 * in the specified target container.
 * @param target the component which needs to be laid out
 * @return    the preferred dimensions to lay out the 
 *                    subcomponents of the specified container.
 * @see Container
 * @see #minimumLayoutSize
 * @see       java.awt.Container#getPreferredSize
 * @since     JDK1.0
 */
protected Dimension computeLayoutSize(Container target, int columns) {
	int nMembers = target.getComponentCount();
	int colWidths[] = new int[columns];
	int totWidth = -this.hGap, totHeight = 0;
	int rowHeight = 0;
	for (int i = 0, vis = 0; i < nMembers; i++) {
		Component m = target.getComponent(i);
		if (m.isVisible()) {
			Dimension d = m.getPreferredSize();
			int col = vis % columns;
			if ((col == 0) && (vis != 0)) {
				totHeight += this.vGap + rowHeight;
				rowHeight = 0;
			}
			rowHeight = Math.max(rowHeight, d.height);
			if (vis < columns) {
				colWidths[vis] = d.width;
				totWidth += this.hGap + d.width;
			} else {
				int colWidth = Math.max(colWidths[col], d.width);
				totWidth += colWidth - colWidths[col];
				colWidths[col] = colWidth;
			}
			vis++;
		}
	}
	totHeight += rowHeight;
	
	Dimension dim = new Dimension(Math.max(0, totWidth), totHeight);
	Insets insets = target.getInsets();
	dim.width += insets.left + insets.right;
	dim.height += insets.top + insets.bottom;
	return dim;
}
/**
 * Calculate necessary state. This method returns a state
 * containing characteristics necessary to render a grid.
 * @param target the specified component being laid out.
 * @see Container
 * @see java.awt.Container#doLayout
 */
protected abstract GridState computeState(Container target);
/**
 * Returns the preferred dimensions for this layout given the components
 * in the specified target container.
 * @param target the component which needs to be laid out
 * @return    the preferred dimensions to lay out the 
 *                    subcomponents of the specified container.
 * @see Container
 * @see #minimumLayoutSize
 * @see       java.awt.Container#getPreferredSize
 * @since     JDK1.0
 */
protected GridState computeState(Container target, int columns) {
	// Make sure columns is at least one.
	columns = Math.max(1, columns);
	
	int nMembers = target.getComponentCount();
	int colWidths[] = new int[columns];
	int totWidth = -this.hGap, totHeight = 0;
	int rowHeight = 0, vis = 0;
	for (int i = 0; i < nMembers; i++) {
		Component m = target.getComponent(i);
		if (m.isVisible()) {
			Dimension d = m.getPreferredSize();
			int col = vis % columns;
			if ((col == 0) && (vis != 0)) {
				totHeight += this.vGap + rowHeight;
				rowHeight = 0;
			}
			rowHeight = Math.max(rowHeight, d.height);
			if (vis < columns) {
				colWidths[vis] = d.width;
				totWidth += this.hGap + d.width;
			} else {
				int colWidth = Math.max(colWidths[col], d.width);
				totWidth += colWidth - colWidths[col];
				colWidths[col] = colWidth;
			}
			vis++;
		}
	}
	totHeight += rowHeight;
	
	Dimension dim = new Dimension(Math.max(0, totWidth), totHeight);
	Insets insets = target.getInsets();
	dim.width += insets.left + insets.right;
	dim.height += insets.top + insets.bottom;
	return new GridState(vis, columns, colWidths, dim);
}
/**
 * Gets the horizontal alignment for this layout.
 * Possible values are <code>CoGridFlowLayout.LEFT</code>,  
 * <code>CoGridFlowLayout.RIGHT</code>, <code>CoGridFlowLayout.CENTER</code>
 * or <code>CoGridFlowLayout.FILL</code>.
 * @return     the horizontal alignment value for this layout.
 * @see        CoGridFlowLayout#setAlignmentH
 */
public int getAlignmentH() {
	return hAlign;
}
/**
 * Gets the vertical alignment for this layout.
 * Possible values are <code>CoGridFlowLayout.TOP</code>,  
 * <code>CoGridFlowLayout.BOTTOM</code>, <code>CoGridFlowLayout.CENTER</code>
 * or <code>CoGridFlowLayout.FILL</code>.
 * @return     the vertical alignment value for this layout.
 * @see        CoGridFlowLayout#setAlignmentV
 */
public int getAlignmentV() {
	return vAlign;
}
/**
 * Gets the horizontal gap between components.
 * @return     the horizontal gap between components.
 * @see        CoGridFlowLayout#setGapH
 */
public int getGapH() {
	return hGap;
}
/**
 * Gets the vertical gap between components.
 * @return     the vertical gap between components.
 * @see        CoGridFlowLayout#setGapV
 */
public int getGapV() {
	return vGap;
}
/**
 * Invalidates the layout, indicating that if the layout manager
 * has cached information it should be discarded.
 */
public void invalidateLayout(Container target) {
	m_cachedState = null;
}
/**
 * Lays out the container. This method lets each component take 
 * its preferred size by reshaping the components in the 
 * target container in order to satisfy the constraints of
 * this <code>CoGridFlowLayout</code> object. 
 * @param target the specified component being laid out.
 * @see Container
 * @see       java.awt.Container#doLayout
 */
public void layoutContainer(Container target) {
	synchronized (target.getTreeLock()) {
		
		if (m_cachedState == null)
			m_cachedState = computeState(target);
			
		Insets insets = target.getInsets();
		int nMembers = target.getComponentCount();
		int columns = m_cachedState.columns;
		int colWidths[] = m_cachedState.columnWidths;
		
		if ((hAlign & NOSLACK) == NOSLACK) {
			int newWidths[] = new int[columns];
			int xSlackLeft = target.getSize().width - m_cachedState.size.width;
			for (int col = 0; col < columns; col++) {
				int slackHere = xSlackLeft/(columns-col);
				xSlackLeft -= slackHere;
				newWidths[col] = colWidths[col] + slackHere;
			}
			colWidths = newWidths;
		}

		int rowsLeft = m_cachedState.rows;
		int ySlackLeft = target.getSize().height - m_cachedState.size.height;

		int x = 0, y = insets.top - this.vGap;
		int rowHeight = 0, start = 0;
		int col = 0;
		for (int i = 0; i < nMembers; i++) {
			Component m = target.getComponent(i);
			if (m.isVisible()) {
				if (col == 0) {
					adjustComponentsV(target, y, rowHeight, start, i);
					x = insets.left;
					y += this.vGap + rowHeight;
					rowHeight = 0;
					start = i;
				}
				int dx = 0;
				Dimension s = m.getPreferredSize();
				if ((hAlign & FILL) != 0) {
					Dimension max = m.getMaximumSize();
					s.width = Math.min(colWidths[col], max.width);
				}
				switch (hAlign & POSITION_MASK) {
					default :
					case LEFT :
						break;
					case CENTER :
						dx = (colWidths[col] - s.width) / 2;
						break;
					case RIGHT :
						dx = colWidths[col] - s.width;
						break;
				}
				m.setLocation(x + dx, y);
				m.setSize(s);
				rowHeight = Math.max(rowHeight, s.height);
				x += colWidths[col] + this.hGap;
				col = (col + 1) % columns;
				if ((col == 0) && ((vAlign & NOSLACK) != 0)) {
					int slackHere = ySlackLeft/(rowsLeft--);
					ySlackLeft -= slackHere;
					rowHeight += slackHere;
				}
			}
		}
		if (((vAlign & NOSLACK) != 0) && (rowsLeft > 0))
			rowHeight += ySlackLeft;
		adjustComponentsV(target, y, rowHeight, start, nMembers);
	}
}
/**
 * Returns the preferred dimensions for this layout given the components
 * in the specified target container.
 * @param target the component which needs to be laid out
 * @return    the preferred dimensions to lay out the 
 *                    subcomponents of the specified container.
 * @see Container
 * @see #minimumLayoutSize
 * @see       java.awt.Container#getPreferredSize
 * @since     JDK1.0
 */
public abstract Dimension preferredLayoutSize(Container target);
/**
 * Sets the horizontal alignment for this layout.
 * Possible values are <code>CoGridFlowLayout.LEFT</code>,  
 * <code>CoGridFlowLayout.RIGHT</code>, <code>CoGridFlowLayout.CENTER</code>
 * or <code>CoGridFlowLayout.FILL</code>.
 * @param      hAlign the horizontal alignment value.
 * @see        CoGridFlowLayout#getAlignmentH
 */
public void setAlignmentH(int hAlign) {
	this.hAlign = hAlign;
}
/**
 * Sets the vertical alignment for this layout.
 * Possible values are <code>CoGridFlowLayout.TOP</code>,  
 * <code>CoGridFlowLayout.BOTTOM</code>, <code>CoGridFlowLayout.CENTER</code>
 * or <code>CoGridFlowLayout.FILL</code>.
 * @param      vAlign the vertical alignment value.
 * @see        CoGridFlowLayout#getAlignmentV
 */
public void setAlignmentV(int vAlign) {
	this.vAlign = vAlign;
}
/**
 * Sets the horizontal gap between components.
 * Unlike <code>java.awt</code> layouts, the gap is only between components.
 * @param hGap the horizontal gap between components
 * @see        CoGridFlowLayout#getGapH
 */
public void setGapH(int hGap) {
	this.hGap = hGap;
}
/**
 * Sets the vertical gap between components.
 * Unlike <code>java.awt</code> layouts, the gap is only between components.
 * @param vGap the vertical gap between components
 * @see        CoGridFlowLayout#getGapV
 */
public void setGapV(int vGap) {
	this.vGap = vGap;
}
/**
 * Returns a string representation of this <code>CoGridFlowLayout</code>
 * object and its values.
 * @return     a string representation of this layout.
 */
public String toString() {
	String hStr = "";
	switch (hAlign) {
	  case LEFT:    hStr = ",hAlign=left"; break;
	  case RIGHT:   hStr = ",hAlign=right"; break;
	  case CENTER:  hStr = ",hAlign=center"; break;
	  case FILL:    hStr = ",hAlign=fill"; break;
	}
	String vStr = "";
	switch (vAlign) {
	  case TOP:     vStr = ",vAlign=top"; break;
	  case BOTTOM:  vStr = ",vAlign=bottom"; break;
	  case CENTER:  vStr = ",vAlign=center"; break;
	  case FILL:    vStr = ",vAlign=fill"; break;
	}
	return getClass().getName() + "[hgap=" + hGap + ",vgap=" + vGap + hStr + vStr + "]";
}
}
