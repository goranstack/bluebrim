package com.bluebrim.layout.impl.server.geom;
import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.layout.impl.shared.*;

/**
 * Snap grid containing horizontal and vertical grid lines.
 * 
 * @author: Dennis Malmström
 */

public class CoCustomGrid extends CoSnapGrid implements CoCustomGridIF
{
	public static final String XML_TAG = "custom-grid";
	
	private static class MutableDouble implements java.io.Serializable
	{
		public double m_double;

		public MutableDouble( double d )
		{
			m_double = d;
		}

		public boolean equals( Object o )
		{
			if
				( o instanceof MutableDouble )
			{
				return m_double == ( (MutableDouble) o ).m_double;
			} else {
				return super.equals( o );
			}
		}
	};

	protected List m_xPositions = new ArrayList(); // [ MutableDouble ]
	protected List m_yPositions = new ArrayList(); // [ MutableDouble ]
/*
 * Used at XML import
 * Helena Rankegård 2001-10-29
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoCustomGrid();
}


public CoCustomGrid()
{
}


public void addXPosition( double x )
{
	if ( x <= 0 ) return;
	if ( x >= m_width ) return;
	
	MutableDouble d = new MutableDouble( x );
	if
		( ! m_xPositions.contains( d ) )
	{
		m_xPositions.add( d );
		invalidate();
	}
}


public void addYPosition( double y )
{
	if ( y <= 0 ) return;
	if ( y >= m_height ) return;
	
	MutableDouble d = new MutableDouble( y );
	if
		( ! m_yPositions.contains( d ) )
	{
		m_yPositions.add( d );
		invalidate();
	}
}


protected void createGridLines()
{
	m_gridLines = new ArrayList();

	Iterator i = m_xPositions.iterator();
	while
		( i.hasNext() )
	{
		MutableDouble d = (MutableDouble) i.next();
		m_gridLines.add( new CoVerticalGridLine( d.m_double, 0, m_height, CoGridLineIF.CUSTOM ) );
	}
	
	i = m_yPositions.iterator();
	while
		( i.hasNext() )
	{
		MutableDouble d = (MutableDouble) i.next();
		m_gridLines.add( new CoHorizontalGridLine( 0, d.m_double, m_width, CoGridLineIF.CUSTOM ) );
	}
}


public double findXPosition( double x, double range )
{
	Iterator i = m_xPositions.iterator();
	while
		( i.hasNext() )
	{
		double X = ( (MutableDouble) i.next() ).m_double;
		if ( x + range < X ) continue;
		if ( x - range > X ) continue;

		return X;
	}

	return Double.NaN;
}


public double findYPosition( double y, double range )
{
	Iterator i = m_yPositions.iterator();
	while
		( i.hasNext() )
	{
		double Y = ( (MutableDouble) i.next() ).m_double;
		if ( y + range < Y ) continue;
		if ( y - range > Y ) continue;

		return Y;
	}

	return Double.NaN;
}


public String getFactoryKey()
{
	return CUSTOM_GRID;
}


public java.awt.Shape getShape( int detailMask_notUsed )
{
	Collection gridLines = getGridLines();

	if ( gridLines.isEmpty() ) return null;
	
	java.awt.geom.GeneralPath shape = new java.awt.geom.GeneralPath();

	Iterator lines = gridLines.iterator();
	
	CoGridLineIF l = null;

	// columns
	while
		( lines.hasNext() )
	{
		l = (CoGridLineIF) lines.next();
		
		java.awt.geom.Line2D s = l.getShape();
		shape.moveTo( (float) s.getX1(), (float) s.getY1() );
		shape.lineTo( (float) s.getX2(), (float) s.getY2() );
	}

	return shape;
}


public double getXPosition( int i )
{
	return ( (MutableDouble) m_xPositions.get( i ) ).m_double;
}


public int getXPositionCount()
{
	return m_xPositions.size();
}


public double getYPosition( int i )
{
	return ( (MutableDouble) m_yPositions.get( i ) ).m_double;
}


public int getYPositionCount()
{
	return m_yPositions.size();
}


protected void invalidate()
{
	m_gridLines = null;
}


public boolean isEdgeGridLine( CoGridLineIF l )
{
	return false;
}


public void removeAllXPositions()
{
	m_xPositions.clear();
	invalidate();
}


public void removeAllYPositions()
{
	m_yPositions.clear();
	invalidate();
}


public void removeXPosition( double x )
{
	MutableDouble d = new MutableDouble( x );
	if
		( m_xPositions.contains( d ) )
	{
		m_xPositions.remove( d );
		invalidate();
	}
}


public void removeXPosition( int i )
{
	m_xPositions.remove( i );
	invalidate();
}


public void removeYPosition( double y )
{
	MutableDouble d = new MutableDouble( y );
	if
		( m_yPositions.contains( d ) )
	{
		m_yPositions.remove( d );
		invalidate();
	}
}


public void removeYPosition( int i )
{
	m_yPositions.remove( i );
	invalidate();
}


public void setSize( double w, double h )
{
	super.setSize( w, h );
}


/*
 * Used at XML import
 * Helena Rankegård 2001-10-29
 */
 
public void xmlAddSubModel(String name, Object subModel, com.bluebrim.xml.shared.CoXmlContext context)
{
}


/*
 * Used at XML import
 * Helena Rankegård 2001-10-29
 */
 
public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) 
{
}


/*
 * Used at XML export
 * Helena Rankegård 2001-10-29
 */
 
public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor)
{
}
}