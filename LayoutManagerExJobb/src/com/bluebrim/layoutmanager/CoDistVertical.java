package com.bluebrim.layoutmanager;


/**
 * calculateDistance from p1 to p2 with dist=x.
 * Creation date: (2000-07-13 10:22:15)
 * @author: Arvid Berg & Masod Jalalian 
 */
public class CoDistVertical implements com.bluebrim.layout.impl.shared.layoutmanager.CoDistVerticalIF 
{
/**
 * CoDistVertical constructor comment.
 */
public CoDistVertical() {
	super();
}
/**
 * calculateDistance from p1 to p2.
 */
public double calculateDistance(java.awt.geom.Point2D p1, java.awt.geom.Point2D p2) {
	return Math.abs(p1.getX()-p2.getX());
}
/**
 * getName method comment.
 */
public java.lang.String getName() {
	return VERTICAL_DIST_CALC;
}
}
