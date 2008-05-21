package com.bluebrim.layoutmanager;


/**
 * calculateDistance from p1 to p2 with dist=max(x,y).
 * Creation date: (2000-07-13 10:16:01)
 * @author: Arvid Berg & Masod Jalalian 
 */
public class CoDistRectangle implements com.bluebrim.layout.impl.shared.layoutmanager.CoDistRectangleIF 
{

public CoDistRectangle() {
	super();
}
/**
 * calculateDistance from p1 to p2.
 */
public double calculateDistance(java.awt.geom.Point2D p1, java.awt.geom.Point2D p2) 
{
	double k=1.3;
	return Math.max(k*Math.abs(p1.getX()-p2.getX()),Math.abs(p1.getY()-p2.getY()));
}
/**
 * getName method comment.
 */
public java.lang.String getName() {
	return RECTANGLE_DIST_CALC;
}
}
