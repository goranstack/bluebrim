package com.bluebrim.base.shared.geom;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;

/**
 * Polygon shape
 * 
 * @author: Dennis Malmström
 */

public class CoPolygonShape extends CoCurveShape implements CoPolygonShapeIF
{
	// xml tag constants
	public static final String XML_TAG = "polygon";
/*
 * Used at XML import
 * Helena Rankegård 2001-10-29
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoPolygonShape( node, context );
}

public CoPolygonShape()
{
}


public CoPolygonShape( int sideCount, double w, double h, boolean isClosed )
{
	if ( sideCount < 3 ) sideCount = 3;
	
	m_width = w;
	m_height = h;
	m_isClosed = isClosed;
	
	for
		( int i = 0; i < sideCount; i++ )
	{
		m_points.add( new Point() );
	}

	makeRegular();
}


// called by createExpandedInstance

private CoPolygonShape( Collection points, boolean isClosed )
{
	m_isClosed = isClosed;
	
	Iterator i = points.iterator();
	double x = Double.MAX_VALUE;
	double y = Double.MAX_VALUE;
	double X = Double.MIN_VALUE;
	double Y = Double.MIN_VALUE;
	while
		( i.hasNext() )
	{
		Point2D p = (Point2D) i.next();
		x = Math.min( x, p.getX() );
		y = Math.min( y, p.getX() );
		X = Math.max( X	, p.getX() );
		Y = Math.max( Y, p.getY() );
	}

	double w = X - x;
	double h = Y - y;
	
	i = points.iterator();
	while
		( i.hasNext() )
	{
		Point2D p = (Point2D) i.next();
		m_points.add( new Point( ( p.getX() - x ) / w, ( p.getY() - y ) / h ) );
	}
	
	m_x = x;
	m_y = y;
	m_width = w;
	m_height = h;
}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-29
 */
 
protected CoPolygonShape( Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	super( node, context );

	// xml init
	//NamedNodeMap map = node.getAttributes();
}


public Point2D addPoint( double x, double y )
{
	return insertPoint( m_points.size(), x ,y );
}


protected CoShape copyForTranslation()
{
	try
	{
		CoPolygonShape copy = (CoPolygonShape) clone();
		copy.invalidate();
		return copy;
	}
	catch ( CloneNotSupportedException e )
	{
		throw new InternalError();
	}
}


public CoShapeIF createNewInstanceFrom( CoImmutableShapeIF s )
{
	CoPolygonShape S = (CoPolygonShape) createNewInstance();
	
	double x = s.getX();
	double y = s.getY();
	double w = s.getWidth();
	double h = s.getHeight();
	
	S.setX( x );
	S.setY( y );
	S.setWidth( w );
	S.setHeight( h );
	S.m_isClosed = false;

	if
		( ( w == 0 ) || ( h == 0 ) )
	{
		// collapsed shape
		S.m_isClosed = true;
		S.m_points.add( S.new Point( 0, 0 ) );
		S.m_points.add( S.new Point( 1, 0 ) );
		S.m_points.add( S.new Point( 1, 1 ) );
		S.m_points.add( S.new Point( 0, 1 ) );
		return S;
	}

	// flatten shape and sample control points
	PathIterator i = s.getShape().getPathIterator( null, 1 );
	double [] d = new double [ 6 ];
	while
		( ! i.isDone() )
	{
		int type = i.currentSegment( d );
		switch
			( type )
		{
			case PathIterator.SEG_CLOSE :
				S.m_isClosed = true;
				break;
				
			case PathIterator.SEG_LINETO :
			case PathIterator.SEG_MOVETO :
				S.m_points.add( new Point( ( d[ 0 ] - x ) / w, ( d[ 1 ] - y ) / h ) );
				break;
		}
		i.next();
	}

	// closed shape analasys
	if
		( ! S.m_isClosed )
	{
		if
			( S.m_points.size() > 1 )
		{
			if
				( S.m_points.get( 0 ).equals( S.m_points.get( S.m_points.size() - 1 ) ) )
			{
				S.m_isClosed = true;
			}
						
		}
	}

	if
		( S.m_isClosed )
	{
		if
			( S.m_points.size() > 1 )
		{
			if
				( S.m_points.get( 0 ).equals( S.m_points.get( S.m_points.size() - 1 ) ) )
			{
				S.m_points.remove( S.m_points.size() - 1 );
			}
						
		}
	}
	
	return S;
}


protected CoShapeIF doCreateExpandedInstance( double delta )
{
	CoPolygonShape s = new CoPolygonShape( expandPoints( delta ), m_isClosed );
	return s;
}


private List expandPoints( double delta )
{
	List points = new ArrayList();

	Point2D p, p0, p1;

	int I = m_points.size();
	for
		( int i = 0; i < I; i++ )
	{
		p = (Point2D) m_points.get( i );
		if
			( i > 0 )
		{
			p1 = (Point2D) m_points.get( i - 1 );
		} else {
			if
				( m_isClosed )
			{
				p1 = (Point2D) m_points.get( I - 1 );
			} else {
				p1 = null;
			}
		}
		
		if
			( i < I - 1 )
		{
			p0 = (Point2D) m_points.get( i + 1 );
		} else {
			if
				( m_isClosed )
			{
				p0 = (Point2D) m_points.get( 0 );
			} else {
				p0 = null;
			}
		}

		double x = m_x + p.getX() * m_width;
		double y = m_y + p.getY() * m_height;

		double x0 = Double.NaN;
		double y0 = Double.NaN;
		double x1 = Double.NaN;
		double y1 = Double.NaN;

		if
			( p0 != null )
		{
			x0 = m_x + p0.getX() * m_width - x;
			y0 = m_y + p0.getY() * m_height - y;
			double s = Math.sqrt( x0 * x0 + y0 * y0 );
			x0 /= s;
			y0 /= s;
		}
		
		if
			( p1 != null )
		{
			x1 = x - ( m_x + p1.getX() * m_width );
			y1 = y - ( m_y + p1.getY() * m_height );
			double s = Math.sqrt( x1 * x1 + y1 * y1 );
			x1 /= s;
			y1 /= s;
		}

		if
			( p0 == null )
		{
			x0 = x1;
			y0 = y1;
		} else if
			( p1 == null )
		{
			x1 = x0;
			y1 = y0;
		}

		double c = ( x0 * x0 + y0 * y0 - y0 * y1 - x0 * x1 );
		double s = ( x1 * y0 - x0 * y1 );
		if ( Math.abs( c ) > 1e-10 ) c /= s;

		x = x + delta * ( y1 + x1 * c );
		y = y + delta * ( - x1 + y1 * c );

		points.add( new Point2D.Double( x, y ) );
	}

	return points;
}


public String getFactoryKey()
{
	return CoPolygonShapeIF.POLYGON_SHAPE;
}


public Shape getShape()
{
	if
		( m_shape == null )
	{
		m_shape = new GeneralPath();

		if
			( ! m_points.isEmpty() )
		{
			Iterator i = m_points.iterator();
			Point2D p = (Point2D) i.next();
			m_shape.moveTo( (float) ( m_x + m_width * p.getX() ), (float) ( m_y + m_height * p.getY() ) );
			
			while
				( i.hasNext() )
			{
				p = (Point2D) i.next();
				m_shape.lineTo( (float) ( m_x + m_width * p.getX() ), (float) ( m_y + m_height * p.getY() ) );
			}

			if ( m_isClosed ) m_shape.closePath();
		}
	}

	return m_shape;
}


public String getXmlTag()
{
	return XML_TAG;
}


public Point2D insertPoint( int i )
{
	int I = m_points.size();
	
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( I >= 3, "" );

	Point p = null;

	if
		( i == 0 )
	{
		// extrapolate before first point
		Point2D p0 = getPoint( 0 );
		Point2D p1 = getPoint( 1 );
		double x = 1.5 * p0.getX() - 0.5 * p1.getX();
		double y = 1.5 * p0.getY() - 0.5 * p1.getY();
		m_points.add( 0, p = new Point( x, y ) );
		
	} else if
		( ( i == -1 ) || ( i >= I ) )
	{	
		// extrapolate beyond last point
		Point2D p0 = getPoint( I - 1 );
		Point2D p1 = getPoint( I - 2 );
		double x = 1.5 * p0.getX() - 0.5 * p1.getX();
		double y = 1.5 * p0.getY() - 0.5 * p1.getY();
		m_points.add( p = new Point( x, y ) );

	} else {
		// interpolate
		Point2D p0 = getPoint( i );
		Point2D p1 = getPoint( i - 1 );
		double x = 0.5 * p0.getX() + 0.5 * p1.getX();
		double y = 0.5 * p0.getY() + 0.5 * p1.getY();
		m_points.add( i, p = new Point( x, y ) );
	}
	
	normalize();

	invalidate();
	
	return p;
}


public Point2D insertPoint( int n, double x, double y )
{
	Point p = null;

	if
		( m_points.isEmpty() )
	{
		// first point, just insert it
		m_x = x;
		m_y = y;
		p = new Point( 0, 0 );
		m_points.add( p );
		
	} else if
		( m_points.size() == 1 )
	{
		// second point -> calulate end points and normalize
		double x0 = Math.min( m_x, x );
		double y0 = Math.min( m_y, y );
		double x1 = Math.max( m_x, x );
		double y1 = Math.max( m_y, y );

		double X = m_x;
		double Y = m_y;
		
		m_x = x0;
		m_y = y0;
		m_width = x1 - x0;
		m_height = y1 - y0;

		if
			( m_width != 0 )
		{
			X = ( X - m_x ) / m_width;
			x = ( x - m_x ) / m_width;
		} else {
			X = X - m_x;
			x = x - m_x;
		}

		if
			( m_height != 0 )
		{
			Y = ( Y - m_y ) / m_height;
			y = ( y - m_y ) / m_height;
		} else {
			Y = Y - m_y;
			y = y - m_y;
		}
		
		m_points.clear();
		m_points.add( new Point( X, Y ) );

		if
			( n > 0 )
		{
			m_points.add( new Point( x, y ) );
		} else {
			m_points.add( 0, new Point( x, y ) );
		}
		
	} else {

		// insert point and normalize
		if
			( m_width != 0 )
		{
			x = ( x - m_x ) / m_width;
		} else {
			m_width = x - m_x;
			x = 1;
		}

		if
			( m_height != 0 )
		{
			y = ( y - m_y ) / m_height;
		} else {
			m_height = y - m_y;
			y = 1;
		}

		if
			( n >= m_points.size() )
		{
			m_points.add( new Point( x, y ) );
		} else {
			if ( n < 0 ) n = 0;
			m_points.add( n, new Point( x, y ) );
		}
		normalize();
	}

	invalidate();
	
	return p;
}


/*
	Algorithm:
	 1) create a horizontal line that intersects p.
	 2) count the number of times that line intersects the polygon on each side (left and right) of p.
	 3) odd number of intersections on both sides -> p is inside polygon.
*/

public boolean isInside( Point2D p, double strokeWidth, double outsideStrokeWidth )
{
	double x = ( p.getX() - m_x ) / m_width;
	double y = ( p.getY() - m_y ) / m_height;
	
	boolean leftHit = false;
	boolean rightHit = false;

	List points = m_points;
	
	if
		( outsideStrokeWidth != 0 )
	{	
		points = expandPoints( outsideStrokeWidth );
		int I = points.size();
		for
			( int i = 0; i < I; i++ )
		{
			Point2D P = (Point2D) points.get( i );
			P.setLocation( ( P.getX() - m_x ) / m_width, ( P.getY() - m_y ) / m_height );
		}
	}

	// traverse ploygon segments (lines)
	int I = points.size();
	for
		( int i = 0; i < I - 1; i++ )
	{
		Point2D p0 = (Point2D) points.get( i );
		Point2D p1 = (Point2D) points.get( i + 1 );

		// calculate segment bounds
		double x0 = p0.getX();
		double x1 = p1.getX();
		double y0 = p0.getY();
		double y1 = p1.getY();

		// does horizontal line at y intersect segment ?
		if
			( ( y0 - y ) * ( y - y1 ) >= 0 )
		{
			if
				( y0 == y1 )
			{
				// horizontal segment
				if
					( y == y0 )
				{
					if ( x1 >= x ) rightHit = ! rightHit; // intersection to the right of x
					if ( x0 <= x ) leftHit = ! leftHit; // intersection to the left of x
				}
			} else {
				double X = x0 + ( x0 - x1 ) * ( y - y0 ) / ( y0 - y1 );
				if ( X >= x ) rightHit = ! rightHit; // intersection to the right of x
				if ( X <= x ) leftHit = ! leftHit; // intersection to the left of x
			}
		}
	}

	// segment between first and last point
	{
		Point2D p0 = (Point2D) points.get( points.size() - 1 );
		Point2D p1 = (Point2D) points.get( 0 );

		double x0 = p0.getX();
		double x1 = p1.getX();
		double y0 = p0.getY();
		double y1 = p1.getY();

		if
			( ( y0 - y ) * ( y - y1 ) >= 0 )
		{
			if
				( y0 == y1 )
			{
				if
					( y == y0 )
				{
					if ( x1 >= x ) rightHit = ! rightHit;
					if ( x0 <= x ) leftHit = ! leftHit;
				}
			} else {
				double X = x0 + ( x0 - x1 ) * ( y - y0 ) / ( y0 - y1 );
				if ( X >= x ) rightHit = ! rightHit;
				if ( X <= x ) leftHit = ! leftHit;
			}
		}
	}

	return leftHit && rightHit; // odd number of intersections on both sides -> (x,y) is inside polygon
}


// isInside is accurate -> isOutside is complement

public boolean isOutside( Point2D m, double strokeWidth, double outsideStrokeWidth )
{
	return ! isInside( m, strokeWidth, outsideStrokeWidth );
}


public void makeRegular()
{
	int I = m_points.size();
	if ( I < 3 ) return;

	double da = 2 * Math.PI / I;
	for
		( int i = 0; i < I; i++ )
	{
		double x = 0.5 + 0.5 * Math.cos( da * i );
		double y = 0.5 + 0.5 * Math.sin( da * i );
		( (Point2D) m_points.get( i ) ).setLocation( x, y );
	}
}


public void removePoint( double x, double y )
{
	int I = m_points.size();
	if ( I <= 3 ) return;

	int i = getIndexOfPoint( x, y );
	if
		( i != -1 )
	{
		m_points.remove( i );
		normalize();
		invalidate();
	}

}


public void reorderPoints()
{
	int I = m_points.size();
	for
		( int i = 0, j = I - 1; i < I / 2; i++, j-- )
	{
		Object p = m_points.get( i );
		m_points.set( i, m_points.get( j ) );
		m_points.set( j, p );
	}

	invalidate();
}


public String toString()
{
	return "CoPolygonShape: " + m_x + " " + m_y + "   " + m_width + " " + m_height + " " + m_isClosed + " " + m_points.size();
}
}