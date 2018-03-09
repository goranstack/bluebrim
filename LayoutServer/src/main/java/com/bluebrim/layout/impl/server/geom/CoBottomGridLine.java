package com.bluebrim.layout.impl.server.geom;

import java.awt.geom.*;

import com.bluebrim.layout.impl.shared.*;

/**
 * Implementation of bottom grid line.
 * 
 * @author: Dennis Malmström
 */

public class CoBottomGridLine extends CoHorizontalGridLine
{

public CoGridLineIF forcedIntersect( double x, double y, double w, double h )
{
	if
		( y + h < m_y )
	{
		CoGridLine l = (CoGridLine) intersect( x, y, w, m_y - y );
		if ( l != null ) l.m_y = y + h;
		return l;
	} else {
		return intersect( x, y, w, h );
	}
}
public double getDistance( double pos1, double pos2 )
{
	return getBottomDistance( pos1, pos2 );
}
public boolean snap( double x, double y, double w, double h, double range, Point2D p, Point2D d )
{
	if
		( ( x > m_x + m_length ) || ( x + w < m_x ) )
	{
		return false;
	}

	double dy = Math.abs( y + h - m_y );
	if ( dy > range ) return false;

	p.setLocation( x, m_y - h );
	d.setLocation( Double.MAX_VALUE, dy );

	return true;
}
public boolean snap( double x, double y, double range, int edgeMask, int dirMask, Point2D p, Point2D d )
{
	if
		( ( edgeMask & BOTTOM_EDGE_MASK ) == 0 )
	{
		return false;
	}
	
	return super.snap( x, y, range, edgeMask, dirMask, p, d );
}
public String toString()
{
	return "grid line (B) " + "( " + m_x + ", " + m_y + " ) " + m_length;
}

public CoBottomGridLine( double x, double y, double length, int type )
{
	super( x, y, length, type );
}
}