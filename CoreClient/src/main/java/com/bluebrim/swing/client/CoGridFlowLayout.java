package com.bluebrim.swing.client;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

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
 * of the column. In similar fashion, the row heights are determined.
 *
 * A flow layout lets each component assume its natural (preferred) size. 
 *
 */
public class CoGridFlowLayout extends CoAbstractGridLayout {
	// Not implemented yet.
	public static final int MINIMIZE_CIRCUMFERENCE 	= -1;

	// The number of columns used to determine preferredLayoutSize.
	private int preferredColumns = MINIMIZE_CIRCUMFERENCE;

	// The minimum number of columns.
	private int minimumColumns = 1;

	// The maximum number of columns.
	private int maximumColumns = Integer.MAX_VALUE;

	// The number of columns must be an integral multiple of columnStep.
	private int columnStep = 1;
/**
 * Constructs a new CoGridFlowLayout with a horizontal left fill and
 * vertical top alignment and a default 5-unit horizontal and
 * vertical gap.
 */
public CoGridFlowLayout() {
	this(MINIMIZE_CIRCUMFERENCE, LEFT_FILL, TOP, 5, 5);
}
/**
 * Constructs a new CoGridFlowLayout with a horizontal left fill and
 * vertical top alignment and a default 5-unit horizontal and
 * vertical gap.
 */
public CoGridFlowLayout(int preferredColumns) {
	this(preferredColumns, LEFT_FILL, TOP, 5, 5);
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
public CoGridFlowLayout(int hAlign, int vAlign) {
	this(MINIMIZE_CIRCUMFERENCE, hAlign, vAlign, 5, 5);
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
public CoGridFlowLayout(int preferredColumns, int hAlign, int vAlign) {
	this(preferredColumns, hAlign, vAlign, 5, 5);
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
public CoGridFlowLayout(int hAlign, int vAlign, int hGap, int vGap) {
	this(MINIMIZE_CIRCUMFERENCE, hAlign, vAlign,hGap, vGap);
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
public CoGridFlowLayout(int preferredColumns, int hAlign, int vAlign, int hGap, int vGap) {
	this.preferredColumns = preferredColumns;
	this.hAlign = hAlign;
	this.vAlign = vAlign;
	this.hGap = hGap;
	this.vGap = vGap;
}
/**
 * Calculates the minimum and maximum dimensions for this layout given
 * the components in the specified target container.
 * @param target the component which needs to be laid out
 * @return    the preferred dimensions to lay out the 
 *                    subcomponents of the specified container.
 * @see Container
 * @see #minimumLayoutSize
 * @see #maximumLayoutSize
 * @see       java.awt.Container#getPreferredSize
 * @since     JDK1.0
 */
protected void computeMinMaxSize(Container target) {
	int nMembers = target.getComponentCount();
	Dimension max = new Dimension(0,0);
	Dimension min = new Dimension(0,0);
	for (int i = 0; i < nMembers; i++) {
		Component m = target.getComponent(i);
		if (m.isVisible()) {
			Dimension d = m.getPreferredSize();
			max.height += d.height;
			max.width += d.width;
			min.height += Math.max(min.height, d.height);
			min.width += Math.max(min.width, d.width);
		}
	}
	
	Insets insets = target.getInsets();
	max.width += insets.left + insets.right;
	max.height += insets.top + insets.bottom;
	min.width += insets.left + insets.right;
	min.height += insets.top + insets.bottom;
}
/**
 * Calculate necessary sizes. This method lets each component take 
 * its preferred size by reshaping the components in the 
 * target container in order to satisfy the constraints of
 * this <code>CoGridFlowLayout</code> object. 
 * @param target the specified component being laid out.
 * @see Container
 * @see java.awt.Container#doLayout
 */
protected GridState computeState(Container target) {
	return computeState(target, getMinimumColumns(), getMaximumColumns(), getColumnStep());
}
/**
 * Calculate necessary state. This method returns a state
 * containing characteristics necessary to render a grid.
 * @param target the specified component being laid out.
 * @see Container
 * @see java.awt.Container#doLayout
 */
protected GridState computeState(Container target, int minColumns, int maxColumns, int columnStep) {
	Insets insets = target.getInsets();
	int maxWidth = target.getSize().width - (insets.left + insets.right);
	int nMembers = target.getComponentCount();
	int nVisible = 0;
	int compWidths[] = new int[nMembers];
	int totWidth = -this.hGap, totHeight = 0;

	int columns = 0;
	for (int i = 0; i < nMembers; i++) {
		Component m = target.getComponent(i);
		if (m.isVisible()) {
			// Is this OK to do here? If I don't, the sizes are cached too much when filling.
			m.invalidate();
			int width = m.getPreferredSize().width;
			compWidths[nVisible++] = width;
			if ((totWidth += this.hGap + width) <= maxWidth)
				columns++;
		}
	}

	// Make sure columnStep is at least one.
	columnStep = Math.max(1, columnStep);

	// Make sure minColumns is at least columnStep and columnStep aligned.
	minColumns = Math.max(columnStep, ((minColumns+columnStep-1)/columnStep)*columnStep);

	// Make sure maxColumns is at least columnStep and columnStep aligned.
	maxColumns = Math.max(columnStep, ((maxColumns+columnStep-1)/columnStep)*columnStep);

	// Make sure we have at least minColumns and no more than maxColumns columns.
	columns = (Math.min(Math.max(columns, minColumns), maxColumns)/columnStep)*columnStep;
		
		
	// Now, columns is an upper limit on how many columns we can have.
	// Decrease columns until every row fits, or only minColumns columns are left.
	int colWidths[] = new int[columns];
	columnDecreasing :
	do {
		totWidth = -this.hGap;
		for (int i = 0; i < nVisible; i++) {
			if (i < columns) {
				colWidths[i] = compWidths[i];
				totWidth += this.hGap + colWidths[i];
			} else {
				int col = i % columns;
				int colWidth = Math.max(colWidths[col], compWidths[i]);
				if (((totWidth += colWidth - colWidths[col]) > maxWidth) && (columns > minColumns))
					continue columnDecreasing;
				colWidths[col] = colWidth;
			}
		}
		break columnDecreasing;
	} while ((columns -= columnStep) > 0);	// Should always be true.
	Dimension size = new Dimension(Math.max(0, totWidth) + insets.left + insets.right, totHeight + insets.top + insets.bottom);

	return new GridState(nVisible, columns, colWidths, size);
}
/**
 * Gets the minimum number of columns.
 */
public int getColumnStep() {
	return this.columnStep;
}
/**
 * Gets the maximum number of columns.
 *
 * Make sure this never is less than 1.
 */
public int getMaximumColumns() {
	return this.maximumColumns;
}
/**
 * Gets the minimum number of columns.
 *
 * Make sure this never is less than 1.
 */
public int getMinimumColumns() {
	return this.minimumColumns;
}
/**
 * Gets the preferred number of columns.
 *
 * Make sure this is never less than 1.
 */
public int getPreferredColumns() {
	return (preferredColumns > 0) ? preferredColumns : 2;
}
/**
 * Calculates the minimum and maximum dimensions for this layout given
 * the components in the specified target container.
 * @param target the component which needs to be laid out
 * @return    the preferred dimensions to lay out the 
 *                    subcomponents of the specified container.
 * @see Container
 * @see #minimumLayoutSize
 * @see #maximumLayoutSize
 * @see       java.awt.Container#getPreferredSize
 * @since     JDK1.0
 */
public Dimension maximumLayoutSize(Container target) {
	int nMembers = target.getComponentCount();
	Dimension max = new Dimension(0,0);
	for (int i = 0; i < nMembers; i++) {
		Component m = target.getComponent(i);
		if (m.isVisible()) {
			Dimension d = m.getPreferredSize();
			max.height += d.height;
			max.width += d.width;
		}
	}
	
	Insets insets = target.getInsets();
	max.width += insets.left + insets.right;
	max.height += insets.top + insets.bottom;
	return max;
}
/**
 * Calculates the minimum and maximum dimensions for this layout given
 * the components in the specified target container.
 * @param target the component which needs to be laid out
 * @return    the preferred dimensions to lay out the 
 *                    subcomponents of the specified container.
 * @see Container
 * @see #minimumLayoutSize
 * @see #maximumLayoutSize
 * @see       java.awt.Container#getPreferredSize
 * @since     JDK1.0
 */
public Dimension minimumLayoutSize(Container target) {
	int nMembers = target.getComponentCount();
	Dimension min = new Dimension(0,0);
	for (int i = 0; i < nMembers; i++) {
		Component m = target.getComponent(i);
		if (m.isVisible()) {
			Dimension d = m.getPreferredSize();
			min.height += Math.max(min.height, d.height);
			min.width += Math.max(min.width, d.width);
		}
	}
	
	Insets insets = target.getInsets();
	min.width += insets.left + insets.right;
	min.height += insets.top + insets.bottom;
	return min;
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
public Dimension preferredLayoutSize(Container target) {
	synchronized (target.getTreeLock()) {
		return computeState(target, getPreferredColumns()).size;
	}
}
/**
 * Sets the maximum number of columns.
 */
public void setColumnStep(int step) {
	this.columnStep = step;
}
/**
 * Sets the maximum number of columns.
 */
public void setMaximumColumns(int columns) {
	this.maximumColumns = columns;
}
/**
 * Sets the minimum number of columns.
 */
public void setMinimumColumns(int columns) {
	this.minimumColumns = columns;
}
/**
 * Sets the preferred number of columns.
 */
public void setPreferredColumns(int columns) {
	this.preferredColumns = columns;
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
