package com.bluebrim.base.shared;
import java.awt.*;

/**
 * PENDING: Not used. Remove or move to base project! /Markus 1999-11-30
 *
 * NOTE: The comments for this class is not up to date yet.
 * @author: Markus Persson 1999-06-04
 *
 * A flow layout arranges components in a left-to-right flow, much 
 * like lines of text in a paragraph. Flow layouts are typically used 
 * to arrange buttons in a panel. It will arrange
 * buttons left to right until no more buttons fit on the same line.
 * Each line is centered.
 * <p>
 * For example, the following picture shows an applet using the flow 
 * layout manager (its default layout manager) to position three buttons:
 * <p>
 * <img src="images-awt/FlowLayout-1.gif" 
 * ALT="Graphic of Layout for Three Buttons" 
 * ALIGN=center HSPACE=10 VSPACE=7>
 * <p>
 * Here is the code for this applet: 
 * <p>
 * <hr><blockquote><pre>
 * import java.awt.*;
 * import java.applet.Applet;
 * 
 * public class myButtons extends Applet {
 *     Button button1, button2, button3;
 *     public void init() {
 *         button1 = new Button("Ok");
 *         button2 = new Button("Open");
 *         button3 = new Button("Close");
 *         add(button1);
 *         add(button2);
 *         add(button3);
 *     }
 * }
 * </pre></blockquote><hr>
 * <p>
 * A flow layout lets each component assume its natural (preferred) size. 
 *
 */
public class CoFlowLayout implements LayoutManager {
	public static final int LEFT 	= 0;
	public static final int RIGHT 	= 1;
	public static final int TOP 	= 2;
	public static final int BOTTOM 	= 3;

	public static final int CENTER 	= 4;
	public static final int FILL 	= 5;

	private int hAlign;
	private int vAlign;
	private int hGap;
	private int vGap;


/**
 * Constructs a new CoGridFlowLayout with a horizontal fill and
 * vertical top alignment and a default 5-unit horizontal and
 * vertical gap.
 */
public CoFlowLayout() {
	this(FILL, TOP, 5, 5);
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
public CoFlowLayout(int hAlign, int vAlign) {
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
public CoFlowLayout(int hAlign, int vAlign, int hGap, int vGap) {
	this.hAlign = hAlign;
	this.vAlign = vAlign;
	this.hGap = hGap;
	this.vGap = vGap;
}


/**
 * Adds the specified component to the layout. Not used by this class.
 * @param name the name of the component
 * @param comp the component to be added
 * @since JDK1.0
 */
public void addLayoutComponent(String name, Component comp) {
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
 * Lays out the container. This method lets each component take 
 * its preferred size by reshaping the components in the 
 * target container in order to satisfy the constraints of
 * this <code>FlowLayout</code> object. 
 * @param target the specified component being laid out.
 * @see Container
 * @see       java.awt.Container#doLayout
 * @since     JDK1.0
 */
public void layoutContainer(Container target) {
	  synchronized (target.getTreeLock()) {
	Insets insets = target.getInsets();
	int maxwidth = target.getSize().width - (insets.left + insets.right + hGap*2);
	int nmembers = target.getComponentCount();
	int x = 0, y = insets.top + vGap;
	int rowh = 0, start = 0;

	for (int i = 0 ; i < nmembers ; i++) {
	    Component m = target.getComponent(i);
	    if (m.isVisible()) {
		Dimension d = m.getPreferredSize();
		m.setSize(d.width, d.height);

		if ((x == 0) || ((x + d.width) <= maxwidth)) {
		    if (x > 0) {
			x += hGap;
		    }
		    x += d.width;
		    rowh = Math.max(rowh, d.height);
		} else {
		    moveComponents(target, insets.left + hGap, y, maxwidth - x, rowh, start, i);
		    x = d.width;
		    y += vGap + rowh;
		    rowh = d.height;
		    start = i;
		}
	    }
	}
	moveComponents(target, insets.left + hGap, y, maxwidth - x, rowh, start, nmembers);
	  }
}


/**
 * Returns the minimum dimensions needed to layout the components
 * contained in the specified target container.
 * @param target the component which needs to be laid out 
 * @return    the minimum dimensions to lay out the 
 *                    subcomponents of the specified container.
 * @see #preferredLayoutSize
 * @see       java.awt.Container
 * @see       java.awt.Container#doLayout
 * @since     JDK1.0
 */
public Dimension minimumLayoutSize(Container target) {
	  synchronized (target.getTreeLock()) {
	Dimension dim = new Dimension(0, 0);
	int nmembers = target.getComponentCount();

	for (int i = 0 ; i < nmembers ; i++) {
	    Component m = target.getComponent(i);
	    if (m.isVisible()) {
		Dimension d = m.getMinimumSize();
		dim.height = Math.max(dim.height, d.height);
		if (i > 0) {
		    dim.width += hGap;
		}
		dim.width += d.width;
	    }
	}
	Insets insets = target.getInsets();
	dim.width += insets.left + insets.right + hGap*2;
	dim.height += insets.top + insets.bottom + vGap*2;
	return dim;
	  }
}


/** 
 * Centers the elements in the specified row, if there is any slack.
 * @param target the component which needs to be moved
 * @param x the x coordinate
 * @param y the y coordinate
 * @param width the width dimensions
 * @param height the height dimensions
 * @param rowStart the beginning of the row
 * @param rowEnd the the ending of the row
 */
private void moveComponents(Container target, int x, int y, int width, int height, int rowStart, int rowEnd) {
	  synchronized (target.getTreeLock()) {
	switch (hAlign) {
	case LEFT:
	    break;
	case CENTER:
	    x += width / 2;
	    break;
	case RIGHT:
	    x += width;
	    break;
	}
	for (int i = rowStart ; i < rowEnd ; i++) {
	    Component m = target.getComponent(i);
	    int dy = 0;
	    if (m.isVisible()) {
	    Dimension s = m.getSize();
		switch (vAlign) {
		case TOP:
		    break;
		case BOTTOM:
		    dy = (height - s.height);
		    break;
		case FILL:
			Dimension max = m.getMaximumSize();
			s.height = Math.min(height, max.height);
			m.setSize(s);
			// No break here!
		case CENTER:
		    dy = (height - s.height) / 2;
		    break;
		}
		m.setLocation(x, y + dy);
		x += hGap + s.width;
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
public Dimension preferredLayoutSize(Container target) {
	  synchronized (target.getTreeLock()) {
	Dimension dim = new Dimension(0, 0);
	int nmembers = target.getComponentCount();

	for (int i = 0 ; i < nmembers ; i++) {
	    Component m = target.getComponent(i);
	    if (m.isVisible()) {
		Dimension d = m.getPreferredSize();
		dim.height = Math.max(dim.height, d.height);
		if (i > 0) {
		    dim.width += hGap;
		}
		dim.width += d.width;
	    }
	}
	Insets insets = target.getInsets();
	dim.width += insets.left + insets.right + hGap*2;
	dim.height += insets.top + insets.bottom + vGap*2;
	return dim;
	  }
}


/**
 * Removes the specified component from the layout. Not used by
 * this class.  
 * @param comp the component to remove
 * @see       java.awt.Container#removeAll
 * @since     JDK1.0
 */
public void removeLayoutComponent(Component comp) {
}


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
 * Sets the verticalal alignment for this layout.
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
 * @param hGap the horizontal gap between components
 * @see        CoGridFlowLayout#getGapH
 */
public void setGapH(int hGap) {
	this.hGap = hGap;
}


/**
 * Sets the vertical gap between components.
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