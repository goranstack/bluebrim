package com.bluebrim.base.shared.geom;

/*
 * By Helena Rankegård
 */
 
public class CoTrapetsPoint implements java.io.Serializable
{
	public float m_x;
	public float m_y;
public CoTrapetsPoint() {
	super();
}
public CoTrapetsPoint (float x, float y) {
	this();
	setPoint(x, y);
}
public CoTrapetsPoint (CoTrapetsPoint p) {
	this();
	setPoint(p);
}
public boolean betterStartingPointThan (CoTrapetsPoint p) {
	return (m_y < p.m_y) || (m_y == p.m_y && m_x < p.m_x);
}
public static boolean closeEnough( double f1, double f2 )
{
	return Math.abs( f1 - f2 ) < 0.001;
}
public boolean equals (CoTrapetsPoint p) {
	return m_x == p.m_x && m_y == p.m_y;
}
public int findPointFurthestAway (CoTrapetsPoint p1, CoTrapetsPoint p2) {
	// looks a bit odd because static methods are not allowed
	float lenThis = p1.m_y + p2.m_y;
	float lenP1 = m_y + p2.m_y;
	float lenP2 = m_y + p1.m_y;
	if (lenThis >= lenP1 && lenThis >= lenP2)
		return 1;
	if (lenP1 >= lenThis && lenP1 >= lenP2)
		return 2;
	return 3;
}
boolean isEquivalentTo( CoTrapetsPoint p )
{
	return closeEnough( m_x, p.m_x ) && closeEnough( m_y, p.m_y );
}
public void setPoint (float x, float y) {
	m_x = x;
	m_y = y;
}
public void setPoint (CoTrapetsPoint p) {
	m_x = p.m_x;
	m_y = p.m_y;
}
public String toString () {
	return "(" + m_x + ",  " + m_y + ")";
}
public void translateBy( float dx, float dy )
{
	m_x += dx;
	m_y += dy;
}
}
