package com.bluebrim.layout.impl.server.geom;

import java.awt.geom.*;

import com.bluebrim.layout.impl.shared.*;

/**
 * Implementation of horizontal grid line.
 * 
 * @author: Dennis Malmström
 */

public class CoHorizontalGridLine extends CoGridLine
{

public CoGridLineIF forcedIntersect( double x, double y, double w, double h )
{
	if
		( y + h < m_y )
	{
		CoGridLine l = (CoGridLine) intersect( x, y, w, m_y - y );
		if ( l != null ) l.m_y = y + h;
		return l;
	} else 	if
		( y > m_y )
	{
		CoGridLine l = (CoGridLine) intersect( x, m_y, w, h );
		if ( l != null ) l.m_y = y;
		return l;
	} else {
		return intersect( x, y, w, h );
	}
}
protected double getBottomDistance( double pos1, double pos2 )
{
	return Math.abs( m_y - pos2 );
}
public double getDistance( double pos1, double pos2 )
{
	return Math.min( getTopDistance( pos1, pos2 ), getBottomDistance( pos1, pos2 ) );
}
public Line2D getShape()
{
	return new Line2D.Double( m_x, m_y, m_x + m_length, m_y );
}
protected double getTopDistance( double pos1, double pos2 )
{
	return Math.abs( m_y - pos1 );
}
public CoGridLineIF intersect( double x, double y, double w, double h )
{
	try
	{
		if ( x + w < m_x ) return null;
		if ( x > m_x + m_length ) return null;
		if ( y > m_y ) return null;
		if ( y + h < m_y ) return null;
		
		double X = Math.max( m_x, x );
		double L = Math.min( X + m_length, x + w ) - X;
		if ( L == 0 ) return null;
		CoGridLine l = (CoGridLine) clone();
		l.m_x = X;
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
		( ( x > m_x + m_length ) || ( x + w < m_x ) )
	{
		return false;
	}

	double dy1 = Math.abs( y - m_y ); // top distance
	double dy2 = Math.abs( y + h - m_y ); // bottom distance
	
	if
		( dy1 < dy2 )
	{
		if ( dy1 > range ) return false;
		p.setLocation( x, m_y );
		d.setLocation( Double.MAX_VALUE, dy1 );
	} else {
		if ( dy2 > range ) return false;
		p.setLocation( x, m_y - h );
		d.setLocation( Double.MAX_VALUE, dy2 );
	}

	return true;
}
public boolean snap( double x, double y, double range, int edgeMask, int dirMask, Point2D p, Point2D d )
{
	if
		( ( edgeMask & HORIZONTAL_EDGE_MASK ) == 0 )
	{
		return false;
	}
	
	if
		( ( x > m_x + m_length ) || ( x < m_x ) )
	{
		return false;
	}

	if ( ( y > m_y ) && ( ( dirMask & UP_MASK ) == 0 ) ) return false;
	if ( ( y < m_y ) && ( ( dirMask & DOWN_MASK ) == 0 ) ) return false;
	
	double dy = Math.abs( y - m_y );
	
	if ( dy > range ) return false;
	
	p.setLocation( x, m_y );
	d.setLocation( Double.MAX_VALUE, dy );

	return true;
}
public String toString()
{
	return "grid line (H) " + "( " + m_x + ", " + m_y + " ) " + m_length;
}

public CoHorizontalGridLine( double x, double y, double length, int type )
{
	super( x, y, length, type );
}
}