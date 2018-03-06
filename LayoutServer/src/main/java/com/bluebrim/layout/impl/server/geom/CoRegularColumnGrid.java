package com.bluebrim.layout.impl.server.geom;
import java.awt.geom.*;
import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.geom.*;
import com.bluebrim.layout.impl.server.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Implementation of column grid.
 * 
 * @author: Dennis Malmström
 */

public class CoRegularColumnGrid extends CoColumnGrid implements CoColumnGridIF
{
/*
 * Used at XML import
 * Helena Rankegård 2001-10-29
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoRegularColumnGrid(node, context);
}
	// specified grid geometry

	// margins
	protected double m_leftMargin;
	protected double m_rightMargin;
	protected double m_topMargin;
	protected double m_bottomMargin;

	// columns
	protected double m_spacing = 10;
	protected int m_columnCount = 1;


	
	// derived
	protected double m_columnWidth;
	public static final String XML_BOTTOM_MARGIN = "bottomMargin";
	public static final String XML_COLUMN_COUNT = "columnCount";
	// xml tag constants
	//public static final String XML_COLUMN_GRID = "columnGrid";
	public static final String XML_TAG = "regular-column-grid";
	public static final String XML_LEFT_MARGIN = "leftMargin";
	public static final String XML_RIGHT_MARGIN = "rightMargin";
	public static final String XML_SPACING = "spacing";
	public static final String XML_TOP_MARGIN = "topMargin";
	protected boolean m_isLeftOutsideSensitive = false;
	protected boolean m_isSpread = false;

/**
 * Co8ColumnGrid constructor comment.
 */
public CoRegularColumnGrid()
{
	this( 1 );
}


/**
 * Co8ColumnGrid constructor comment.
 */
public CoRegularColumnGrid( int columnCount )
{
	super();

	m_columnCount = columnCount;

//	invalidate();
}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-29
 */
 
protected CoRegularColumnGrid(Node node, com.bluebrim.xml.shared.CoXmlContext context) 
{
	super();

	// xml init
	NamedNodeMap map = node.getAttributes();
	
	m_topMargin = CoModelBuilder.getDoubleAttrVal( map, XML_TOP_MARGIN, m_topMargin );
	m_bottomMargin = CoModelBuilder.getDoubleAttrVal( map, XML_BOTTOM_MARGIN, m_bottomMargin );
	m_leftMargin = CoModelBuilder.getDoubleAttrVal( map, XML_LEFT_MARGIN, m_leftMargin );
	m_rightMargin = CoModelBuilder.getDoubleAttrVal( map, XML_RIGHT_MARGIN, m_rightMargin );
	m_columnCount = CoModelBuilder.getIntAttrVal( map, XML_COLUMN_COUNT, m_columnCount );
	m_spacing = CoModelBuilder.getDoubleAttrVal( map, XML_SPACING, m_spacing );
}


protected void createGridLines()
{
	createGridLines( 0, m_width );
}


protected void createGridLines( double x0, double x1 )
{
	m_gridLines = new ArrayList();

	if
		( ( m_width == 0 ) || ( m_height == 0 )  )
	{
		return;
	}
	
	double leftMargin = getEffectiveLeftMargin();
	double rightMargin = getEffectiveRightMargin();

	// edges
	m_gridLines.add( new CoTopGridLine( 0, 0, m_width, CoGridLineIF.EDGE ) );
	m_gridLines.add( new CoBottomGridLine( 0, m_height, m_width, CoGridLineIF.EDGE ) );

	if ( x0 <= 0 ) m_gridLines.add( new CoLeftGridLine( 0, 0, m_height, CoGridLineIF.EDGE ) );
	if ( x1 >= m_width ) m_gridLines.add( new CoRightGridLine( m_width, 0, m_height, CoGridLineIF.EDGE ) );

	// top and bottom margins
	double pos = Math.max( 0, m_topMargin );
	pos = Math.min( pos, m_height );
	m_gridLines.add( new CoTopGridLine( 0, pos, m_width, CoGridLineIF.MARGIN ) );

	pos = Math.max( 0, m_bottomMargin );
	pos = Math.min( pos, m_height );
	m_gridLines.add( new CoBottomGridLine( 0, m_height - pos, m_width, CoGridLineIF.MARGIN ) );

	// left and right margins
	double lmPos = Math.min( Math.max( 0, leftMargin ), m_width );
	double rmPos = Math.min( Math.max( 0, rightMargin ), m_width );
	if
		( m_isSpread )
	{	
		m_gridLines.add( new CoLeftGridLine( lmPos, 0, m_height, CoGridLineIF.MARGIN ) );
		m_gridLines.add( new CoRightGridLine( m_width / 2 - rmPos, 0, m_height, CoGridLineIF.MARGIN ) );
		
		if
			( m_isLeftOutsideSensitive )
		{
			double tmp = lmPos;
			lmPos = rmPos;
			rmPos = tmp;
		}
		m_gridLines.add( new CoLeftGridLine( m_width / 2 + lmPos, 0, m_height, CoGridLineIF.MARGIN ) );
		m_gridLines.add( new CoRightGridLine( m_width - rmPos, 0, m_height, CoGridLineIF.MARGIN ) );
		m_gridLines.add( new CoVerticalGridLine( m_width / 2, 0, m_height, CoGridLineIF.MARGIN ) );
	} else {
		m_gridLines.add( new CoLeftGridLine( lmPos, 0, m_height, CoGridLineIF.MARGIN ) );
		m_gridLines.add( new CoRightGridLine( m_width - rmPos, 0, m_height, CoGridLineIF.MARGIN ) );
	}

	// columns
	double x = leftMargin + m_columnWidth;
	double dx = lmPos - leftMargin;
	pos = 0;
	if
		( m_height > 0 )
	{
		int I = m_columnCount - 1;
		for
			( int i = 0; i < I; i++ )
		{
			pos = x;
			if ( pos >= x0 ) m_gridLines.add( new CoRightGridLine( pos, 0, m_height, CoGridLineIF.COLUMN ) );
			
			pos = x + m_spacing / 2;
			if ( pos >= x0 ) m_gridLines.add( new CoCenterGridLine( pos, 0, m_height, CoGridLineIF.GAP ) );
			
			pos = x + m_spacing;
			if ( pos >= x0 ) m_gridLines.add( new CoLeftGridLine( pos, 0, m_height, CoGridLineIF.COLUMN ) );
			
			if
				( m_isSpread )
			{
				pos = m_width / 2 + x + dx;
				if ( pos >= x0 ) m_gridLines.add( new CoRightGridLine( pos, 0, m_height, CoGridLineIF.COLUMN ) );
			
				pos = m_width / 2 + x + dx + m_spacing / 2;
				if ( pos >= x0 ) m_gridLines.add( new CoCenterGridLine( pos, 0, m_height, CoGridLineIF.GAP ) );

				pos = m_width / 2 + x + dx + m_spacing;
				if ( pos >= x0 ) m_gridLines.add( new CoLeftGridLine( pos, 0, m_height, CoGridLineIF.COLUMN ) );
			}
			x += (float) ( m_spacing + m_columnWidth );
			if ( x > x1 ) break;
		}
	}
/*
	// column spacing
	x = leftMargin + m_columnWidth;
	if
		( m_height > 0 )
	{
		int I = m_columnCount - 1;
		for
			( int i = 0; i < I; i++ )
		{
			if ( x + m_spacing / 2 >= x0 ) m_gridLines.add( new CoCenterGridLine( x + m_spacing / 2, 0, m_height, CoGridLineIF.GAP ) );
			if
				( m_isSpread )
			{
				if ( m_width / 2 + x + dx + m_spacing / 2 >= x0 ) m_gridLines.add( new CoCenterGridLine( m_width / 2 + x + dx + m_spacing / 2, 0, m_height, CoGridLineIF.GAP ) );
			}
			x += (float) ( m_spacing + m_columnWidth );
			if ( x > x1 ) break;
		}
	}
	*/
}


protected CoColumnGrid createSnapshot( double x, double y, double w, double h )
{
	CoRegularColumnGrid g = new CoRegularColumnGrid();

	// spacing
	g.m_spacing = m_spacing;

	g.m_leftMargin = g.m_rightMargin = 0;

	// top margin
	if
		( y + h <= m_topMargin )
	{
		g.m_topMargin = 0;
	} else if
		( y <= m_topMargin )
	{
		g.m_topMargin = m_topMargin - y;
	} else {
		g.m_topMargin = 0;
	}

	// bottom margin
	if
		( y >= m_height - m_bottomMargin )
	{
		g.m_bottomMargin = 0;
	} else if
		( y + h >= m_height - m_bottomMargin )
	{
		g.m_bottomMargin = y + h - ( m_height - m_bottomMargin );
	} else {
		g.m_bottomMargin = 0;
	}

	// column count
	double x0 = m_leftMargin + m_columnWidth;
	while
		( x0 <= x )
	{
		x0 += m_columnWidth + m_spacing;
	}
	int I = 1;

	if
		( g.m_leftMargin == -1 )
	{
		g.m_leftMargin = x0 - x - m_columnWidth;
	}

	x += w;

	x0 += m_spacing;
	while
		( x0 < x )
	{
		I++;
		x0 += m_columnWidth + m_spacing;
	}

	if
		( g.m_rightMargin == -1 )
	{
		g.m_rightMargin = x - x0 + m_spacing;
	}

	g.m_columnCount = I;
	
	if
		(I == 1)
	{
		g.m_columnWidth = w;
		g.m_rightMargin = g.m_leftMargin = 0;
	}
	
	g.invalidate();
	return g;
}


public boolean equals( Object o )
{
	if ( o == this ) return true;

	if ( ! ( o instanceof CoRegularColumnGrid ) ) return false;

	CoRegularColumnGrid s = (CoRegularColumnGrid) o;
	
	return
		( m_x == s.m_x )
	&&
		( m_y == s.m_y )
	&&
		( m_width == s.m_width )
	&&
		( m_height == s.m_height )
	&&
		( m_columnCount == s.m_columnCount )
	&&
		( m_spacing == s.m_spacing )
	&&
		( m_bottomMargin == s.m_bottomMargin )
	&&
		( m_topMargin == s.m_topMargin )
	&&
		( m_leftMargin == s.m_leftMargin )
	&&
		( m_rightMargin == s.m_rightMargin );
}


public double getBottomMargin()
{
	return m_bottomMargin;
}


public double getBottomMarginPosition()
{
	return m_height - m_bottomMargin;
}


public int getColumnCount()
{
	return m_columnCount;
}


public com.bluebrim.text.shared.CoColumnGeometryIF getColumnGeometry( CoImmutableShapeIF shape, double X, double Y ) 
{
	// Stores all the columns that the shape can be splitt into
	// Each column is described in one or more trapetses
	CoTrapetsesColumns columns = new CoTrapetsesColumns();

	double leftMargin = getEffectiveLeftMargin();
	double rightMargin = getEffectiveRightMargin();

	// too small column width or shape height to paint anything in column
	if 
		(
			( shape == null ) 
		||
			( shape.getHeight() < MIN_COLUMN_HEIGHT )
		||
			( m_columnWidth < MIN_COLUMN_WIDTH )
		||
			( leftMargin >= X + shape.getWidth() )
		||
			( 0 >= Y + shape.getHeight() )
		) 
	{
		return columns;
	}

	// Stores the bounds of a column
	Rectangle2D.Float b = new Rectangle2D.Float();
	CoRunAroundShape rs = ( shape instanceof CoRunAroundShape ) ? 
	                      	(CoRunAroundShape) shape :
	                      	new CoRunAroundShape( shape );

	// create new shape for each column
	int I = m_columnCount;
	int i = 0;
	double x = leftMargin;
	while
		( i < I )
	{
		if ( x >= X + shape.getWidth() ) break;
		
		x = leftMargin + i * ( m_columnWidth + m_spacing );

		if
			( x + m_columnWidth < X )
		{
			i++;
			continue;
		}
		
		// the bounds of the column
		b.x = (float) java.lang.Math.max( x, shape.getX() + X );
		b.y = (float) java.lang.Math.max( 0, shape.getY() + Y );
		b.width = (float) java.lang.Math.min( shape.getWidth() + shape.getX() + X, x + m_columnWidth ) - b.x;
		b.height = (float) java.lang.Math.min( m_height, shape.getY() + Y + shape.getHeight() ) - b.y;

		b.x -= (float) X;
		b.y -= (float) Y;
	  
		// store the shape
		columns.add( rs.getTrapetses( b ), b );

		x += m_columnWidth;
		i++;
	}

	if
		( m_isSpread )
	{
		i = 0;
		double lm = m_isLeftOutsideSensitive ? rightMargin : leftMargin;
		x = m_width / 2 + lm;
		while
			( i < I )
		{
			if ( x >= X + shape.getWidth() ) break;
			
			x = m_width / 2 + lm + i * ( m_columnWidth + m_spacing );

			if
				( x + m_columnWidth < X )
			{
				i++;
				continue;
			}
			
			// the bounds of the column
			b.x = (float) java.lang.Math.max( x, shape.getX() + X );
			b.y = (float) java.lang.Math.max( 0, shape.getY() + Y );
			b.width = (float) java.lang.Math.min( shape.getWidth() + shape.getX() + X, x + m_columnWidth ) - b.x;
			b.height = (float) java.lang.Math.min( m_height, shape.getY() + Y + shape.getHeight() ) - b.y;

			b.x -= (float) X;
			b.y -= (float) Y;
		  
			// store the shape
			columns.add( rs.getTrapetses( b ), b );

			x += m_columnWidth;
			i++;
		}
	}
	
	return columns;
}


public double getColumnSpacing()
{
	return m_spacing;
}


public double getColumnWidth()
{
	return m_columnWidth;
}


protected double getDx()
{
	return 0;
}


protected final double getEffectiveLeftMargin()
{
	return ( m_isLeftOutsideSensitive && ! m_isLeftOutside ) ? m_rightMargin : m_leftMargin;
}


protected final double getEffectiveRightMargin()
{
	return ( m_isLeftOutsideSensitive && ! m_isLeftOutside ) ? m_leftMargin : m_rightMargin;
}


public double getLeftMargin()
{
	return m_leftMargin;
}


public double getLeftMarginPosition()
{
	return m_leftMargin;
}


public double getRightMargin()
{
	return m_rightMargin;
}


public double getRightMarginPosition()
{
	return m_width - m_rightMargin;
}


public double getTopMargin()
{
	return m_topMargin;
}


public double getTopMarginPosition()
{
	return m_topMargin;
}


protected void invalidate()
{
	m_gridLines = null;
}


public boolean isLeftOutsideSensitive()
{
	return m_isLeftOutsideSensitive;
}


public boolean isSpread()
{
	return m_isSpread;//m_isLeftOutsideSensitive;
}


protected void recalc()
{
	double w = m_isSpread ? m_width / 2 : m_width;
	m_columnWidth = ( w - m_leftMargin - m_rightMargin - ( m_columnCount - 1 ) * m_spacing ) / m_columnCount;
}


public void set( int columnCount, double spacing, double left, double top, double right, double bottom )
{
	m_columnCount = columnCount;
	m_spacing = spacing;
	m_leftMargin = left;
	m_rightMargin = right;
	m_topMargin = top;
	m_bottomMargin = bottom;
	recalc();
}


public void setBottomMargin( double bottom )
{
	m_bottomMargin = bottom;
	recalc();
}


public void setColumnCount( int c )
{
	m_columnCount = c;
	recalc();
}


public void setColumnSpacing( double s )
{
	m_spacing = s;
	recalc();
}


public void setLeftMargin( double left )
{
	m_leftMargin = left;
//		if ( Double.isNaN( m_leftMargin ) ) System.err.println( "///////////////////////setLeftMargin" );
//		if ( Double.isNaN( m_leftMargin ) ) Thread.dumpStack();
	recalc();
}


public void setLeftOutsideSensitive( boolean s )
{
	m_isLeftOutsideSensitive = s;
}


public void setMargins( double left, double top, double right, double bottom )
{
	m_leftMargin = left;
	m_rightMargin = right;
	m_topMargin = top;
	m_bottomMargin = bottom;
	recalc();
}


public void setRightMargin( double right )
{
	m_rightMargin = right;
	recalc();
}


public void setSpread( boolean s )
{
	m_isSpread = s;
	recalc();
}


public void setTopMargin( double top )
{
	m_topMargin = top;
	recalc();
}


protected void setWidth( double w )
{
	super.setWidth( w );
	recalc();
}


public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor )
{
	super.xmlVisit( visitor );
	
//	visitor.exportAttribute( XML_TYPE, XML_COLUMN_GRID );

	visitor.exportAttribute( XML_TOP_MARGIN, Double.toString(m_topMargin));
	visitor.exportAttribute( XML_BOTTOM_MARGIN, Double.toString(m_bottomMargin));
	visitor.exportAttribute( XML_LEFT_MARGIN, Double.toString(m_leftMargin));
	visitor.exportAttribute( XML_RIGHT_MARGIN, Double.toString(m_rightMargin));
	visitor.exportAttribute( XML_COLUMN_COUNT, Integer.toString(m_columnCount));
	visitor.exportAttribute( XML_SPACING, Double.toString(m_spacing));
}
}