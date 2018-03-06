package com.bluebrim.layout.impl.server.geom;

import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.*;

/**
 * Column grid that is derived by intersecting another column grid.
 * 
 * @author: Dennis Malmström
 */

public class CoDerivedColumnGrid extends CoColumnGrid
{
	protected CoColumnGrid m_source; // the grid from which this grid is derived
	

public CoDerivedColumnGrid( CoColumnGrid source )
{
	m_source = source;
}
protected void createGridLines()
{
	m_gridLines = new ArrayList();

	if
		( ( m_width == 0 ) || ( m_height == 0 )  )
	{
		return;
	}

	Iterator lines = m_source.getGridLines( m_x, m_width ).iterator();
	while
		( lines.hasNext() )
	{
		CoGridLineIF l = (CoGridLineIF) lines.next();

		if
			( l.getType() == CoGridLineIF.MARGIN )
		{
			// margins
			l = l.forcedIntersect( m_x, m_y, m_width, m_height );
			if
				( l != null )
			{
				l.translateBy( -m_x, -m_y );
				m_gridLines.add( l );
			}
		} else if
			( l.getType() != CoGridLineIF.EDGE )
		{
			// columns
			l = l.intersect( m_x, m_y, m_width, m_height );
			if
				( l != null )
			{
				l.translateBy( -m_x, -m_y );
				m_gridLines.add( l );
			}
		}
	}

	
}
protected CoColumnGrid createSnapshot( double x, double y, double w, double h )
{
	return m_source.createSnapshot( x + m_x, y + m_y, w, h );
}
public double getBottomMargin()
{
	return m_source.getBottomMargin();
}
public double getBottomMarginPosition()
{
	return m_source.getBottomMarginPosition( m_y, m_height );
}
public int getColumnCount()
{
	return m_source.getColumnCount();
}
protected com.bluebrim.text.shared.CoColumnGeometryIF getColumnGeometry( CoImmutableShapeIF shape, double x, double y ) 
{
	return m_source.getColumnGeometry( shape, x + m_x, y + m_y );
}
public double getColumnSpacing()
{
	return m_source.getColumnSpacing();
}
public double getColumnWidth()
{
	return m_source.getColumnWidth();
}
public double getLeftMargin()
{
	return m_source.getLeftMargin();
}
public double getLeftMarginPosition()
{
	return m_source.getLeftMarginPosition( m_x, m_width );
}

public double getRightMargin()
{
	return m_source.getRightMargin();
}
public double getRightMarginPosition()
{
	return m_source.getRightMarginPosition( m_x, m_width );
}
public double getTopMargin()
{
	return m_source.getTopMargin();
}
public double getTopMarginPosition()
{
	return m_source.getTopMarginPosition( m_y, m_height );
}

/**
 * invalidate method comment.
 */
protected void invalidate()
{
	m_gridLines = null;
}
public boolean isDerived()
{
	return true;
}



public boolean equals( Object o )
{
	if ( o == this ) return true;

	if ( ! ( o instanceof CoDerivedColumnGrid ) ) return false;

	CoDerivedColumnGrid s = (CoDerivedColumnGrid) o;
	
	return
		( m_x == s.m_x )
	&&
		( m_y == s.m_y )
	&&
		( m_width == s.m_width )
	&&
		( m_height == s.m_height )
	&&
		( m_source.equals( s.m_source ) );
}

protected void createGridLines( double x0, double x1 )
{
	createGridLines();
}

protected double getDx()
{
	double dx = m_x - m_source.getLeftMarginPosition() + m_source.getDx();
	if ( dx < 0 ) return 0;

	double cw = getColumnWidth() + getColumnSpacing();
	
	while
		( dx > cw )
	{
		dx -= cw;
	}
	
	return dx;
}

public boolean isLeftOutsideSensitive()
{
	return m_source.isLeftOutsideSensitive();
}

public boolean isSpread()
{
	return m_source.isSpread();
}

public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor)
{
	throw new UnsupportedOperationException( getClass() + " is not XML exportable" );
//	visitor.exportAttribute( XML_TYPE, XML_IS_DERIVED );
}
}