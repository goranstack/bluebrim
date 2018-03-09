package com.bluebrim.layoutmanager;


/**
 * calculateDistance from p1 to p2 with dist=y.
 * Creation date: (2000-07-13 10:19:40)
 * @author: Arvid Berg & Masod Jalalian  
 */
public class CoDistHorizontal implements com.bluebrim.layout.impl.shared.layoutmanager.CoDistHorizontalIF 
{
/**
 * CoDistHorizontal constructor comment.
 */
public CoDistHorizontal() {
	super();
}
/**
 * calculateDistance from p1 to p2.
 */
public double calculateDistance(java.awt.geom.Point2D p1, java.awt.geom.Point2D p2) {
	return Math.abs(p1.getY()-p2.getY());
}
/**
 * getName method comment.
 */
public java.lang.String getName() {
	return HORIZONTAL_DIST_CALC;
}
}
