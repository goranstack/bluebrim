package com.bluebrim.layout.impl.server.geom;

import java.awt.geom.*;

import com.bluebrim.layout.impl.shared.*;

/**
 * Implementation of bottom grid line.
 * 
 * @author: Dennis Malmström
 */

public class CoTopGridLine extends CoHorizontalGridLine
{

public CoGridLineIF forcedIntersect( double x, double y, double w, double h )
{
	if
		( y > m_y )
	{
		CoGridLine l = (CoGridLine) intersect( x, m_y, w, h );
		if ( l != null ) l.m_y = y;
		return l;
	} else {
		return intersect( x, y, w, h );
	}

}
public double getDistance( double pos1, double pos2 )
{
	return getTopDistance( pos1, pos2 );
}
public boolean snap( double x, double y, double w, double h, double range, Point2D p, Point2D d )
{
	if
		( ( x > m_x + m_length ) || ( x + w < m_x ) )
	{
		return false;
	}

	double dy = Math.abs( y - m_y );
	if ( dy > range ) return false;
	
	p.setLocation( x, m_y );
	d.setLocation( Double.MAX_VALUE, dy );

	return true;
}
public boolean snap( double x, double y, double range, int edgeMask, int dirMask, Point2D p, Point2D d )
{
	if
		( ( edgeMask & TOP_EDGE_MASK ) == 0 )
	{
		return false;
	}

	return super.snap( x, y, range, edgeMask, dirMask, p, d );
}
public String toString()
{
	return "grid line (T) " + "( " + m_x + ", " + m_y + " ) " + m_length;
}

public CoTopGridLine( double x, double y, double length, int type )
{
	super( x, y, length, type );
}
}