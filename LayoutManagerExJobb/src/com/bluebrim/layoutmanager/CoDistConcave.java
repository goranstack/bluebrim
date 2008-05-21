package com.bluebrim.layoutmanager;


/**
 * calculateDistance from p1 to p2 with dist=log(x*y).
 * Creation date: (2000-07-13 10:16:01)
 * @author: Arvid Berg & Masod Jalalian 
 */
public class CoDistConcave implements com.bluebrim.layout.impl.shared.layoutmanager.CoDistConcaveIF {

public CoDistConcave() {
	super();
}
/**
 * calculateDistance from p1 to p2.
 */
public double calculateDistance(java.awt.geom.Point2D p1, java.awt.geom.Point2D p2) 
{
	double x=Math.abs(p1.getX()-p2.getX());
	double y=Math.abs(p1.getY()-p2.getY());
	return Math.log((x*y));
}
/**
 * getName method comment.
 */
public java.lang.String getName() {
	return CONCAVE_DIST_CALC;
}
}
