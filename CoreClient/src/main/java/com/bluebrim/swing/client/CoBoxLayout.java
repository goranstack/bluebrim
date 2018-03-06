package com.bluebrim.swing.client;

import java.awt.AWTError;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.io.PrintStream;
import java.io.Serializable;

import javax.swing.SizeRequirements;
/**
 	Kopia av BoxLayout som gör alla ingående komponenter lika stora som 
 	den som har störst storlek, dvs en sorts kombination av funktionaliteten 
 	hos BoxLayout och GridLayout.
 	För att få fram detta beteende borde det ha räckt med att subklassa BoxLayout och implementera
 	om #checkRequest. Om inte alla nödvändiga instansvariabler hade varit privata i BoxLayout!!
 */
public class CoBoxLayout implements LayoutManager2, Serializable  {


	/**
	 * Specifies that components should be laid out left to right.
	 */
	public static final int X_AXIS = 0;
	
	/**
	 * Specifies that components should be laid out top to buttom.
	 */
	public static final int Y_AXIS = 1;

	private int axis;
	private Container target;

	private transient SizeRequirements[] xChildren;
	private transient SizeRequirements[] yChildren;
	private transient SizeRequirements xTotal;
	private transient SizeRequirements yTotal;
	
	private transient PrintStream dbg;
	public CoBoxLayout(Container target, int axis) {
		if (axis != X_AXIS && axis != Y_AXIS) {
			throw new AWTError("Invalid axis");
		}
		this.axis = axis;
		this.target = target;
	}
	/**
	 * Constructs a BoxLayout that 
	 * produces debugging messages.
	 *
	 * @param target  the container that needs to be laid out
	 * @param axis  the axis to lay out components along; can be either
	 *              <code>BoxLayout.X_AXIS</code>
	 *              or <code>BoxLayout.Y_AXIS</code>
	 * @param dbg  the stream to which debugging messages should be sent,
	 *   null if none
	 */
	public CoBoxLayout(Container target, int axis, PrintStream dbg) {
		this(target, axis);
		this.dbg = dbg;
	}
	/**
	 * Not used by this class.
	 *
	 * @param comp the component
	 * @param constraints constraints
	 */
	public void addLayoutComponent(Component comp, Object constraints) {
	}
	/**
	 * Not used by this class.
	 *
	 * @param name the name of the component
	 * @param comp the component
	 */
	public void addLayoutComponent(String name, Component comp) {
	}
	void checkContainer(Container target) {
		if (this.target != target) {
			throw new AWTError("BoxLayout can't be shared");
		}
	}
void checkRequests()
{
	if (xChildren == null || yChildren == null)
	{
		int n 			= target.getComponentCount();
		xChildren 		= new SizeRequirements[n];
		yChildren 		= new SizeRequirements[n];
		int minWidth 	= 0;
		int maxWidth 	= 0;
		int prefWidth	= 0;
		int minHeight	= 0;
		int maxHeight	= 0;
		int prefHeight 	= 0;
		
		for (int i = 0; i < n; i++)
		{
			Component c 	= target.getComponent(i);
			Dimension min 	= c.getMinimumSize();
			Dimension pref 	= c.getPreferredSize();
			Dimension max 	= c.getMaximumSize();
			if (min.width > minWidth)
				minWidth = min.width;
			if (max.width > maxWidth)
				maxWidth = max.width;
			if (pref.width > prefWidth)
				prefWidth = pref.width;

			if (min.height > minHeight)
				minHeight = min.height;
			if (max.height > maxHeight)
				maxHeight = max.height;
			if (pref.height > prefHeight)
				prefHeight = pref.height;
		}
		for (int i = 0; i < n; i++)
		{
			Component c = target.getComponent(i);
			xChildren[i] = new SizeRequirements(minWidth, prefWidth, maxWidth, c.getAlignmentX());
			yChildren[i] = new SizeRequirements(minHeight, prefHeight, maxHeight, c.getAlignmentY());
		}
		if (axis == X_AXIS)
		{
			xTotal = SizeRequirements.getTiledSizeRequirements(xChildren);
			yTotal = SizeRequirements.getAlignedSizeRequirements(yChildren);
		}
		else
		{
			xTotal = SizeRequirements.getAlignedSizeRequirements(xChildren);
			yTotal = SizeRequirements.getTiledSizeRequirements(yChildren);
		}
	}
}
	/**
	 * Returns the alignment along the X axis for the container.
	 * If the box is horizontal, the default
	 * alignment will be returned. Otherwise, the alignment needed
	 * to place the children along the X axis will be returned.
	 *
	 * @param target  the container
	 * @return the alignment >= 0.0f && <= 1.0f
	 * @exception AWTError  if the target isn't the container specified to the
	 *                      BoxLayout constructor
	 */
	public float getLayoutAlignmentX(Container target) {
		checkContainer(target);
		checkRequests();
		return xTotal.alignment;
	}
	/**
	 * Returns the alignment along the Y axis for the container.
	 * If the box is vertical, the default
	 * alignment will be returned. Otherwise, the alignment needed
	 * to place the children along the Y axis will be returned.
	 *
	 * @param target  the container
	 * @return the alignment >= 0.0f && <= 1.0f
	 * @exception AWTError  if the target isn't the container specified to the
	 *                      BoxLayout constructor
	 */
	public float getLayoutAlignmentY(Container target) {
		checkContainer(target);
		checkRequests();
		return yTotal.alignment;
	}
	/**
	 * Indicates that a child has changed its layout related information,
	 * and thus any cached calculations should be flushed.
	 *
	 * @param target  the affected container
	 *
	 * @exception AWTError  if the target isn't the container specified to the
	 *                      BoxLayout constructor
	 */
	public void invalidateLayout(Container target) {
		checkContainer(target);
		xChildren = null;
		yChildren = null;
		xTotal = null;
		yTotal = null;
	}
	/**
	 * Called by the AWT <!-- XXX CHECK! --> when the specified container
	 * needs to be laid out.
	 *
	 * @param target  the container to lay out
	 *
	 * @exception AWTError  if the target isn't the container specified to the
	 *                      BoxLayout constructor
	 */
	public void layoutContainer(Container target) {
		checkContainer(target);
		checkRequests();
		
		int nChildren = target.getComponentCount();
		int[] xOffsets = new int[nChildren];
		int[] xSpans = new int[nChildren];
		int[] yOffsets = new int[nChildren];
		int[] ySpans = new int[nChildren];

		// determine the child placements
		Dimension alloc = target.getSize();
		Insets in = target.getInsets();
		alloc.width -= in.left + in.right;
		alloc.height -= in.top + in.bottom;
		if (axis == X_AXIS) {
			SizeRequirements.calculateTiledPositions(alloc.width, xTotal,
													 xChildren, xOffsets,
													 xSpans);
			SizeRequirements.calculateAlignedPositions(alloc.height, yTotal,
													   yChildren, yOffsets,
													   ySpans);
		} else {
			SizeRequirements.calculateAlignedPositions(alloc.width, xTotal,
													   xChildren, xOffsets,
													   xSpans);
			SizeRequirements.calculateTiledPositions(alloc.height, yTotal,
													 yChildren, yOffsets,
													 ySpans);
		}

		// flush changes to the container
		for (int i = 0; i < nChildren; i++) {
			Component c = target.getComponent(i);
			c.setBounds((int) Math.min((long) in.left + (long) xOffsets[i], Integer.MAX_VALUE),
						(int) Math.min((long) in.top + (long) yOffsets[i], Integer.MAX_VALUE),
						xSpans[i], ySpans[i]);

		}
		if (dbg != null) {
			for (int i = 0; i < nChildren; i++) {
				Component c = target.getComponent(i);
				dbg.println(c.toString());
				dbg.println("X: " + xChildren[i]);
				dbg.println("Y: " + yChildren[i]);
			}
		}
			
	}
	/**
	 * Returns the minimum dimensions needed to lay out the components
	 * contained in the specified target container.
	 *
	 * @param target  the container that needs to be laid out 
	 * @return the dimenions >= 0 && <= Integer.MAX_VALUE
	 * @exception AWTError  if the target isn't the container specified to the
	 *                      BoxLayout constructor
	 * @see #preferredLayoutSize
	 * @see #minimumLayoutSize
	 */
	public Dimension maximumLayoutSize(Container target) {
		checkContainer(target);
		checkRequests();

		Dimension size = new Dimension(xTotal.maximum, yTotal.maximum);
		Insets insets = target.getInsets();
		size.width = (int) Math.min((long) size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE);
		size.height = (int) Math.min((long) size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE);
		return size;
	}
	/**
	 * Returns the minimum dimensions needed to lay out the components
	 * contained in the specified target container.
	 *
	 * @param target  the container that needs to be laid out 
	 * @return the dimensions >= 0 && <= Integer.MAX_VALUE
	 * @exception AWTError  if the target isn't the container specified to the
	 *                      BoxLayout constructor
	 * @see #preferredLayoutSize
	 * @see #maximumLayoutSize
	 */
	public Dimension minimumLayoutSize(Container target) {
		checkContainer(target);
		checkRequests();

		Dimension size = new Dimension(xTotal.minimum, yTotal.minimum);
		Insets insets = target.getInsets();
		size.width = (int) Math.min((long) + size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE);
		size.height = (int) Math.min((long) + size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE);
		return size;
	}
	/**
	 * Returns the preferred dimensions for this layout, given the components
	 * in the specified target container.
	 *
	 * @param target  the container that needs to be laid out
	 * @return the dimensions >= 0 && <= Integer.MAX_VALUE
	 * @exception AWTError  if the target isn't the container specified to the
	 *                      BoxLayout constructor
	 * @see Container
	 * @see #minimumLayoutSize
	 * @see #maximumLayoutSize
	 */
	public Dimension preferredLayoutSize(Container target) {
		checkContainer(target);
		checkRequests();

		Dimension size = new Dimension(xTotal.preferred, yTotal.preferred);
		Insets insets = target.getInsets();
		size.width = (int) Math.min((long) size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE);
		size.height = (int) Math.min((long) size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE);
		return size;
	}
	/**
	 * Not used by this class.
	 *
	 * @param comp the component
	 */
	public void removeLayoutComponent(Component comp) {
	}
/**
 */
public void setAxis(int axis)
{
	this.axis = axis;
	invalidateLayout(target);
	layoutContainer(target);
}
}
