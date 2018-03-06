package com.bluebrim.layout.impl.server.geom;

import java.awt.geom.*;

import com.bluebrim.layout.impl.shared.*;

/**
 * Implementation of vertical grid line.
 * 
 * @author: Dennis Malmström
 */

public class CoVerticalGridLine extends CoGridLine
{

public CoGridLineIF forcedIntersect( double x, double y, double w, double h )
{
	if
		( x > m_x )
	{
		CoGridLine l = (CoGridLine) intersect( m_x, y, w, h );
		if ( l != null ) l.m_x = x;
		return l;
	} else if
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
	return Math.min( getLeftDistance( pos1, pos2 ), getRightDistance( pos1, pos2 ) );
}
protected double getLeftDistance( double pos1, double pos2 )
{
	return Math.abs( m_x - pos1 );
}
protected double getRightDistance( double pos1, double pos2 )
{
	return Math.abs( m_x - pos2 );
}
public Line2D getShape()
{
	return new Line2D.Double( m_x, m_y, m_x, m_y + m_length );
}
public CoGridLineIF intersect( double x, double y, double w, double h )
{
	try
	{
		if ( y + h < m_y ) return null;
		if ( y > m_y + m_length ) return null;
		if ( x > m_x ) return null;
		if ( x + w < m_x ) return null;
		
		double Y = Math.max( m_y, y );
		double L = Math.min( Y + m_length, y + h ) - Y;
		if ( L == 0 ) return null;
		CoGridLine l = (CoGridLine) clone();
		l.m_y = Y;
		l.m_length = L;
		return l;
	}
	catch ( CloneNotSupportedException ex )
	{
		com.bluebrim.base.shared.debug.CoAssertion.assertTrue( false, getClass() + ".clone() failed" );
		return null;
	}
	
}
public boolean snap( double x, double y, double w, double h, double range, Point2D p, Point2D d )
{
	if
		( ( y > m_y + m_length ) || ( y + h < m_y ) )
	{
		return false;
	}

	
	double dx1 = Math.abs( x - m_x ); // left distance
	double dx2 = Math.abs( x + w - m_x ); // right distance
	
	if
		( dx1 < dx2 )
	{
		if ( dx1 > range ) return false;
		p.setLocation( m_x, y );
		d.setLocation( dx1, Double.MAX_VALUE );
	} else {
		if ( dx2 > range ) return false;
		p.setLocation( m_x - w, y );
		d.setLocation( dx2, Double.MAX_VALUE );
	}
	return true;
}
public boolean snap( double x, double y, double range, int edgeMask, int dirMask, Point2D p, Point2D d )
{
	if
		( ( edgeMask & VERTICAL_EDGE_MASK ) == 0 )
	{
		return false;
	}
	
	if
		( ( y > m_y + m_length ) || ( y < m_y ) )
	{
		return false;
	}

	if ( ( x > m_x ) && ( ( dirMask & TO_LEFT_MASK ) == 0 ) ) return false;
	if ( ( x < m_x ) && ( ( dirMask & TO_RIGHT_MASK ) == 0 ) ) return false;
	
	double dx = Math.abs( x - m_x );

	if ( dx > range ) return false;
	
	p.setLocation( m_x, y );
	d.setLocation( dx, Double.MAX_VALUE );

	return true;
}
public String toString()
{
	return "grid line (V) " + "( " + m_x + ", " + m_y + " ) " + m_length;
}

public CoVerticalGridLine( double x, double y, double length, int type )
{
	super( x, y, length, type );
}
}