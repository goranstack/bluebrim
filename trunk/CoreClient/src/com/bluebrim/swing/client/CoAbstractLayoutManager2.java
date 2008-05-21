package com.bluebrim.swing.client;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;

/**
 * Abstract implementation of the LayoutManager2 interface.
 *
 * Implements empty stubs of methods seldom used by us.
 *
 * @author: Markus Persson 1999-07-08
 */
public abstract class CoAbstractLayoutManager2 implements LayoutManager2 {
/**
 * Adds the specified component to the layout, using the specified
 * constraint object. Not used by this class.
 *
 * @param comp the component to be added
 * @param constraints  where/how the component is added to the layout.
 * @see java.awt.LayoutManager2
 */
public void addLayoutComponent(Component comp, Object constraints) {
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
 * Returns the alignment along the x axis.  This specifies how
 * the component would like to be aligned relative to other 
 * components.  The value should be a number between 0 and 1
 * where 0 represents alignment along the origin, 1 is aligned
 * the furthest away from the origin, 0.5 is centered, etc.
 * @see java.awt.LayoutManager2
 */
public float getLayoutAlignmentX(Container target) {
	return 0f;
}
/**
 * Returns the alignment along the y axis.  This specifies how
 * the component would like to be aligned relative to other 
 * components.  The value should be a number between 0 and 1
 * where 0 represents alignment along the origin, 1 is aligned
 * the furthest away from the origin, 0.5 is centered, etc.
 * @see java.awt.LayoutManager2
 */
public float getLayoutAlignmentY(Container target) {
	return 0f;
}
/**
 * Invalidates the layout, indicating that if the layout manager
 * has cached information it should be discarded.
 */
public abstract void invalidateLayout(Container target);
/** 
 * Returns the maximum size of this component.
 * @see java.awt.Component#getMinimumSize()
 * @see java.awt.Component#getPreferredSize()
 * @see java.awt.LayoutManager2
 */
public Dimension maximumLayoutSize(Container target) {
	return preferredLayoutSize(target);
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
	return preferredLayoutSize(target);
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
 * Removes the specified component from the layout. Not used by
 * this class.  
 * @param comp the component to remove
 * @see       java.awt.Container#removeAll
 * @since     JDK1.0
 */
public void removeLayoutComponent(Component comp) {
}
}
