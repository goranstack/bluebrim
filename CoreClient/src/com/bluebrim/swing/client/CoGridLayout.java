package com.bluebrim.swing.client;

import java.awt.Container;
import java.awt.Dimension;

/**
 * WARNING: This layout currently returns the same minimum as preferred size.
 * This may or may not remain so in the future. /Markus 1999-11-01
 *
 * NOTE: The comments for this class is not up to date yet.
 * @author: Markus Persson 1999-06-04
 *
 * Class not fully functional, yet!
 *
 * The grid is determined using the components natural (preferred) size.
 * For each column, the width of its widest component becomes the width
 * of the column. In similar fashion, the row heights are determined.
 */
public class CoGridLayout extends CoAbstractGridLayout {
	// The number of columns.
	private int columns = 2;
/**
 * Constructs a new CoGridLayout with a horizontal left fill and
 * vertical top alignment and a default 5-unit horizontal and
 * vertical gap.
 */
public CoGridLayout() {
	this(2, LEFT_FILL, TOP, 5, 5);
}
/**
 * Constructs a new CoGridLayout with a horizontal left fill and
 * vertical top alignment and a default 5-unit horizontal and
 * vertical gap.
 */
public CoGridLayout(int columns) {
	this(columns, LEFT_FILL, TOP, 5, 5);
}
/**
 * <p>Constructs a new CoGridLayout with the specified alignment and a
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
public CoGridLayout(int hAlign, int vAlign) {
	this(2, hAlign, vAlign, 5, 5);
}
/**
 * <p>Constructs a new CoGridLayout with the specified alignment and a
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
public CoGridLayout(int columns, int hAlign, int vAlign) {
	this(columns, hAlign, vAlign, 5, 5);
}
/**
 * <p>Constructs a new CoGridLayout with the specified alignment
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
public CoGridLayout(int hAlign, int vAlign, int hGap, int vGap) {
	this(2, hAlign, vAlign,hGap, vGap);
}
/**
 * <p>Constructs a new CoGridLayout with the specified alignment
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
public CoGridLayout(int columns, int hAlign, int vAlign, int hGap, int vGap) {
	this.columns = columns;
	this.hAlign = hAlign;
	this.vAlign = vAlign;
	this.hGap = hGap;
	this.vGap = vGap;
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
	return computeState(target, getColumns());
}
/**
 * Gets the number of columns.
 *
 * Make sure this is never less than 1.
 */
public int getColumns() {
	return (this.columns > 0) ? this.columns : 2;
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
		return computeState(target, getColumns()).size;
	}
}
/**
 * Sets the number of columns.
 */
public void setColumns(int columns) {
	this.columns = columns;
}
/**
 * Returns a string representation of this <code>CoGridLayout</code>
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
