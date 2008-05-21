package com.bluebrim.layoutmanager;


/**
 * calculateDistance from p1 to p2 with dist=sqrt(x^2 +y^2).
 * Creation date: (2000-07-13 10:13:36)
 * @author: Arvid Berg & Masod Jalalian 
 */
public class CoDistConvex implements com.bluebrim.layout.impl.shared.layoutmanager.CoDistConvexIF 
{

public CoDistConvex() {
	super();
}
/**
 * calculateDistance from p1 to p2.
 */
public double calculateDistance(java.awt.geom.Point2D p1, java.awt.geom.Point2D p2) {
	return p1.distance(p2);
}
/**
 * getName method comment.
 */
public java.lang.String getName() {
	return CONVEX_DIST_CALC;
}
}
