package com.bluebrim.layout.impl.server.geom;

import java.awt.geom.*;

import com.bluebrim.layout.impl.shared.*;

/**
 * Implementation of right grid line.
 * 
 * @author: Dennis Malmström
 */

public class CoRightGridLine extends CoVerticalGridLine
{

public CoGridLineIF forcedIntersect( double x, double y, double w, double h )
{
	if
		( x + w < m_x )
	{
		CoGridLine l = (CoGridLine) intersect( x, y, m_x - x, h );
		if ( l != null ) l.m_x = x + w;
		return l;
	} else {
		return intersect( x, y, w, h );
	}
}
public double getDistance( double pos1, double pos2 )
{
	return getRightDistance( pos1, pos2 );
}
public boolean snap( double x, double y, double w, double h, double range, Point2D p, Point2D d )
{
	if
		( ( y > m_y + m_length ) || ( y + h < m_y ) )
	{
		return false;
	}

	double dx = Math.abs( x + w - m_x );
	if ( dx > range ) return false;
	
	p.setLocation( m_x - w, y );
	d.setLocation( dx, Double.MAX_VALUE );
	return true;
}
public boolean snap( double x, double y, double range, int edgeMask, int dirMask, Point2D p, Point2D d )
{
	if
		( ( edgeMask & RIGHT_EDGE_MASK ) == 0 )
	{
		return false;
	}
	
	return super.snap( x, y, range, edgeMask, dirMask, p, d );
}
public String toString()
{
	return "grid line (R) " + "( " + m_x + ", " + m_y + " ) " + m_length;
}

public CoRightGridLine( double x, double y, double length, int type )
{
	super( x, y, length, type );
}
}