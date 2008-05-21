package com.bluebrim.layout.impl.server.geom;

import java.awt.geom.*;
import java.util.*;


import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;


/**
 * Abstract superclass for all snap grids.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoSnapGrid extends CoObject implements CoImmutableSnapGridIF
{
	// bounds
	protected double m_x;
	protected double m_y;
	protected double m_width;
	protected double m_height;

	protected List m_gridLines; // [ CoGridLineIF ]
/**
 * Co8ColumnGrid constructor comment.
 */
public CoSnapGrid()
{
	super();
}
public Object clone() throws CloneNotSupportedException
{
	return super.clone();
}
protected abstract void createGridLines();
public CoImmutableSnapGridIF deepClone()
{
	try
	{
		CoImmutableSnapGridIF grid = (CoImmutableSnapGridIF) clone();
	
		return grid;
	}
	catch ( CloneNotSupportedException ex )
	{
		return null;
	}
}
public final Collection getGridLines()
{
	if
		( m_gridLines == null )
	{
		createGridLines();
	}

	return m_gridLines;
}
public double getHeight()
{
	return m_height;
}
public double getWidth()
{
	return m_width;
}
protected abstract void invalidate();
protected void setHeight( double h )
{
	m_height = h;
	invalidate();
}
protected void setSize( double w, double h )
{
	setHeight( h );
	setWidth( w );
	invalidate();
}
protected void setWidth( double w )
{
	m_width = w;
	invalidate();
}
protected void setX( double x )
{
	m_x = x;
	invalidate();
}
protected void setY( double y )
{
	m_y = y;
	invalidate();
}
public final Point2D snap( double x, double y, double width, double height, double range, Point2D d )
{
	Point2D P = new Point2D.Double( x, y );
	Point2D p = new Point2D.Double();
	
	if ( d == null ) d = new Point2D.Double( Double.MAX_VALUE, Double.MAX_VALUE );
	double DX = d.getX();
	double DY = d.getY();

	boolean didSnapX = false;
	boolean didSnapY = false;
	
	Iterator lines = getGridLines().iterator();

	while
		( lines.hasNext() )
	{
		CoGridLineIF line = (CoGridLineIF) lines.next();
		if
			( line.snap( x, y, width, height, range, p, d ) )
		{
			if
				( d.getX() < DX )
			{
				DX = d.getX();
				P.setLocation( p.getX(), P.getY() );
				didSnapX = true;
			}
			if
				( d.getY() < DY )
			{
				DY = d.getY();
				P.setLocation( P.getX(), p.getY() );
				didSnapY = true;
			}
		}
	}

	d.setLocation( DX, DY );

	// snap to integer grid
	CoLengthUnitSet u = CoLengthUnit.LENGTH_UNITS;

	didSnapX |= d.getX() != Double.MAX_VALUE;
	didSnapY |= d.getY() != Double.MAX_VALUE;

	x = didSnapX ? P.getX() : u.from( u.round( u.to( P.getX() ) ) );
	y = didSnapY ? P.getY() : u.from( u.round( u.to( P.getY() ) ) );
	P.setLocation( x, y );

	return P;
}
public final Point2D snap( double x, double y, double range, int edgeMask, int dirMask, boolean useEdgeGridLines, Point2D d )
{
	Point2D P = new Point2D.Double( x, y );
	Point2D p = new Point2D.Double();

	if ( d == null ) d = new Point2D.Double( Double.MAX_VALUE, Double.MAX_VALUE );
	double DX = d.getX();
	double DY = d.getY();

	boolean didSnapX = false;
	boolean didSnapY = false;
	
	Iterator lines = getGridLines().iterator();
	
	while
		( lines.hasNext() )
	{
		CoGridLineIF line = (CoGridLineIF) lines.next();
		if ( ( ! useEdgeGridLines ) && ( line.getType() == CoGridLineIF.EDGE ) ) continue;
		
		if
			( line.snap( x, y, range, edgeMask, dirMask, p, d ) )
		{
			if
				( d.getX() < DX )
			{
				DX = d.getX();
				P.setLocation( p.getX(), P.getY() );
				didSnapX = true;
			}
			if
				( d.getY() < DY )
			{
				DY = d.getY();
				P.setLocation( P.getX(), p.getY() );
				didSnapY = true;
			}
		}
	}

	d.setLocation( DX, DY );
	
	// snap to integer grid
	CoLengthUnitSet u = CoLengthUnit.LENGTH_UNITS;

	didSnapX |= d.getX() != Double.MAX_VALUE;
	didSnapY |= d.getY() != Double.MAX_VALUE;
	
	x = didSnapX ? P.getX() : u.from( u.round( u.to( P.getX() ) ) );
	y = didSnapY ? P.getY() : u.from( u.round( u.to( P.getY() ) ) );
	P.setLocation( x, y );
	
	return P;
}
public String toString()
{
	String str = super.toString();

	str += "\n";
	str += m_x + " " + m_y + "    " + m_width + " " + m_height + "\n";
	
	Iterator i = getGridLines().iterator();
	while ( i.hasNext() ) str += i.next().toString() + "\n";
	return str;
}

protected void setLocation( double x, double y )
{
	setX( x );
	setY( y );
}
}