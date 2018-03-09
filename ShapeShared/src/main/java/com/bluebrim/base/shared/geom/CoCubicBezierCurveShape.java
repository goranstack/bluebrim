package com.bluebrim.base.shared.geom;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;

/**
 * Cubic bezier polynom curve shape
 * 
 * @author: Dennis Malmström
 */

public class CoCubicBezierCurveShape extends CoCurveShape implements CoCubicBezierCurveShapeIF
{
/*
 * Used at XML import
 * Helena Rankegård 2001-10-29
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoCubicBezierCurveShape( node, context );
}
	// constants
	public static final int FIRST_DERIVATIVE_CONTINUITY = 1;
	public static final int  SECOND_DERIVATIVE_CONTINUITY = 2;
	
	// point translation behavior
	public static boolean m_useStickyPointHandles = true;
	public static int m_continuity = SECOND_DERIVATIVE_CONTINUITY;

	// stroked shape cache, used for hit testing
	protected transient Shape m_strokedShape;
	protected transient double m_strokeWidth;

	
	// reshape handle for moving two control points around a pivot point
	protected class HingeReshapeHandle extends CoCurveShape.ReshapeHandle
	{
		protected Point2D m_twin; // other point to be moved
		protected Point2D m_hinge; // pivot point

		public HingeReshapeHandle( Point2D p, Point2D hinge, Point2D twin )
		{
			super( p );

			com.bluebrim.base.shared.debug.CoAssertion.assertTrue( twin != null, "" );
			com.bluebrim.base.shared.debug.CoAssertion.assertTrue( hinge != null, "" );

			m_twin = twin;
			m_hinge = hinge;
		}
		
		public void move( double dx, double dy )
		{
			double w = ( CoCubicBezierCurveShape.this.m_width > 0 ) ? CoCubicBezierCurveShape.this.m_width : 1;
			double h = ( CoCubicBezierCurveShape.this.m_height > 0 ) ? CoCubicBezierCurveShape.this.m_height : 1;
			
			m_point.setLocation( m_point.getX() + dx / w, m_point.getY() + dy / h );

			int c = getContinuity();
			if  (c >= SECOND_DERIVATIVE_CONTINUITY)
			{
				m_twin.setLocation( 2 * m_hinge.getX() - m_point.getX(), 2 * m_hinge.getY() - m_point.getY() );
			} 
			else if (c == FIRST_DERIVATIVE_CONTINUITY)
			{
				dx = m_hinge.getX() - m_twin.getX();
				dy = m_hinge.getY() - m_twin.getY();
				double k = Math.pow( dx * w, 2 ) + Math.pow( dy * h, 2 );
				
				dx = ( m_hinge.getX() - m_point.getX() );
				dy = ( m_hinge.getY() - m_point.getY() );
				k = Math.sqrt( k / ( Math.pow( dx * w, 2 ) + Math.pow( dy * h, 2 ) ) );

				m_twin.setLocation( m_hinge.getX() + dx * k, m_hinge.getY() + dy * k );
			} 
			else 
			{
				// no derivative continuity
			}
			
		}
	};



	// reshape handle for applying same translation to three points
	protected class StickyReshapeHandle extends CoCurveShape.ReshapeHandle
	{
		protected Point2D m_p1;
		protected Point2D m_p2;

		public StickyReshapeHandle( Point2D p, Point2D p1, Point2D p2 )
		{
			super( p );

			m_p1 = p1;
			m_p2 = p2;
		}
		
		public void move( double dx, double dy )
		{
			super.move( dx, dy );

			if
				( areHandlesSticky() )
			{
				if ( CoCubicBezierCurveShape.this.m_width > 0 ) dx = dx / CoCubicBezierCurveShape.this.m_width;
				if ( CoCubicBezierCurveShape.this.m_height > 0 ) dy = dy / CoCubicBezierCurveShape.this.m_height;
				if ( m_p1 != null ) m_p1.setLocation( m_p1.getX() + dx, m_p1.getY() + dy );
				if ( m_p2 != null ) m_p2.setLocation( m_p2.getX() + dx, m_p2.getY() + dy );
			}
		}
	};


	// xml tag constants
	public static final String XML_TAG = "cubicBezierCurve";

public CoCubicBezierCurveShape()
{
}


public CoCubicBezierCurveShape( GeneralPath gp, double width, double height )
{
	m_width = width;
	m_height = height;

	PathIterator i = gp.getPathIterator( null );

	double d [] = new double [ 6 ];
	
	while
		( ! i.isDone() )
	{
		int type = i.currentSegment( d );
		
		if
			( type == PathIterator.SEG_MOVETO )
		{
			m_points.add( new Point( d[ 1 ], d[ 0 ] ) ); // Note: x/y are swapped in gp
		} else {
			m_points.add( new Point( d[ 1 ], d[ 0 ] ) ); // Note: x/y are swapped in gp
			m_points.add( new Point( d[ 3 ], d[ 2 ] ) ); // Note: x/y are swapped in gp
			m_points.add( new Point( d[ 5 ], d[ 4 ] ) ); // Note: x/y are swapped in gp
		}

		i.next();
	}

	normalize();
}


// called by createExpandedInstance

private CoCubicBezierCurveShape( Collection points, boolean isClosed )
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
 
protected CoCubicBezierCurveShape( Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	super( node, context );

	// xml init
	//NamedNodeMap map = node.getAttributes();
}


public CoCubicBezierCurveShape( boolean isClosed )
{
	m_isClosed = isClosed;
}


public Point2D addPoint( double x, double y )
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( ! m_isClosed, "Can't call addPoint on closed curve" );

	return insertPoint( m_points.size(), x, y );

}


private static boolean areHandlesSticky()
{
	return m_useStickyPointHandles;
}


protected CoShape copyForTranslation()
{
	try
	{
		CoCubicBezierCurveShape copy = (CoCubicBezierCurveShape) clone();
		copy.invalidate();
		return copy;
	}
	catch ( CloneNotSupportedException e )
	{
		throw new InternalError();
	}
}


private CoReshapeHandleIF[] createClosedPointHandles()
{
	int N = m_points.size();
	
	CoReshapeHandleIF[] l = new CoReshapeHandleIF [ N ];

	for
		( int n = 0; n < N; n++ )
	{
		Point2D p = (Point2D) m_points.get( n );
		
		if
			( n == 0 )
		{
			// first interpolating point
			l[ n ] = new StickyReshapeHandle( p, (Point2D) m_points.get( 1 ), (Point2D) m_points.get( N - 1 ) );
			
		} else if
			( n == N - 1 )
		{
			// last tangent control point
			l[ n ] = new HingeReshapeHandle( p, (Point2D) m_points.get( 0 ), (Point2D) m_points.get( 1 ) );
			
		} else if
			( n == 1 )
		{
			// first tangent control point
			l[ n ] = new HingeReshapeHandle( p, (Point2D) m_points.get( 0 ), (Point2D) m_points.get( N - 1 ) );

		} else if
			( n % 3 == 0 )
		{
			// interpolating point
			l[ n ] = new StickyReshapeHandle( p, (Point2D) m_points.get( n - 1 ), (Point2D) m_points.get( n + 1 ) );
			
		} else if
			( n % 3 == 1 )
		{
			// "outgoing" tangent control point
			l[ n ] = new HingeReshapeHandle( p, (Point2D) m_points.get( n - 1 ), (Point2D) m_points.get( n - 2 ) );
			
		} else if
			( n % 3 == 2 )
		{
			// "incoming" tangent control point
			l[ n ] = new HingeReshapeHandle( p, (Point2D) m_points.get( n + 1 ), (Point2D) m_points.get( n + 2 ) );
		}
	}

	return l;

}


public CoShapeIF createNewInstanceFrom( CoImmutableShapeIF s )
{
	CoCubicBezierCurveShape S = (CoCubicBezierCurveShape) createNewInstance();
	double x = s.getX();
	double y = s.getY();
	double w = s.getWidth();
	double h = s.getHeight();
	S.setX( x );
	S.setY( y );
	S.setWidth( w );
	S.setHeight( h );

	boolean closeIt = false;

	if
		( ( w == 0 ) || ( h == 0 ) )
	{
		// collapsed shape
		S.m_points.add( S.new Point( 0, 0 ) );
		S.m_points.add( S.new Point( 1 / 3, 1 / 3 ) );
		S.m_points.add( S.new Point( 2 / 3, 2 / 3 ) );
		S.m_points.add( S.new Point( 1, 1 ) );
		S.setClosed( closeIt );
		return S;
	}

	double X = 0;
	double Y = 0;

	// extract shape segments and create equivalent cubic curve
	PathIterator i = s.getShape().getPathIterator( null );
	double [] d = new double [ 6 ];
	while
		( ! i.isDone() )
	{
		int type = i.currentSegment( d );
		switch
			( type )
		{
			case PathIterator.SEG_CLOSE :
				break;
				
			case PathIterator.SEG_QUADTO :
				S.m_points.add( new Point( ( d[ 0 ] - x ) / w, ( d[ 1 ] - y ) / h ) );
				S.m_points.add( new Point( ( d[ 0 ] - x ) / w, ( d[ 1 ] - y ) / h ) );
				X = ( d[ 2 ] - x ) / w;
				Y = ( d[ 3 ] - y ) / h;
				S.m_points.add( new Point( X, Y ) );
				break;
				
			case PathIterator.SEG_CUBICTO :
				S.m_points.add( new Point( ( d[ 0 ] - x ) / w, ( d[ 1 ] - y ) / h ) );
				S.m_points.add( new Point( ( d[ 2 ] - x ) / w, ( d[ 3 ] - y ) / h ) );
				X = ( d[ 4 ] - x ) / w;
				Y = ( d[ 5 ] - y ) / h;
				S.m_points.add( new Point( X, Y ) );
				break;
				
			case PathIterator.SEG_LINETO :
				double dx = ( d[ 0 ] - x ) / w - X;
				double dy = ( d[ 1 ] - y ) / h - Y;
				S.m_points.add( new Point( X + dx / 3, Y + dy / 3 ) );
				S.m_points.add( new Point( X + dx * 2 / 3, Y + dy * 2 / 3 ) );
				S.m_points.add( new Point( X + dx, Y + dy ) );
				X += dx;
				Y += dy;
				break;

			case PathIterator.SEG_MOVETO :
				X = ( d[ 0 ] - x ) / w;
				Y = ( d[ 1 ] - y ) / h;
				S.m_points.add( new Point( X, Y ) );
				break;
		}
		i.next();
	}

	S.setClosed( closeIt );

	return S;
}


private CoReshapeHandleIF[] createNonClosedPointHandles()
{
	int N = m_points.size();
	
	CoReshapeHandleIF[] l = new CoReshapeHandleIF [ N ];

	for
		( int n = 0; n < N; n++ )
	{
		Point2D p = (Point2D) m_points.get( n );
		
		if
			( n == 0 )
		{
			// first interpolating point
			l[ n ] = new StickyReshapeHandle( p, (Point2D) m_points.get( n + 1 ), null );	
		} else if
			( n == N - 1 )
		{
			// last interpolating point
			l[ n ] = new StickyReshapeHandle( p, (Point2D) m_points.get( n - 1 ), null );
		} else if
			( n % 3 == 0 )
		{
			// interpolating point
			l[ n ] = new StickyReshapeHandle( p, (Point2D) m_points.get( n - 1 ), (Point2D) m_points.get( n + 1 ) );
		} else if
			( n == 1 )
		{
			// first tangent control point
			l[ n ] = new ReshapeHandle( p );
		} else if
			( n == N - 2 )
		{
			// last tangent control point
			l[ n ] = new ReshapeHandle( p );
		} else if
			( n % 3 == 1 )
		{
			// "outgoing" tangent control point
			l[ n ] = new HingeReshapeHandle( p, (Point2D) m_points.get( n - 1 ), (Point2D) m_points.get( n - 2 ) );
		} else if
			( n % 3 == 2 )
		{
			// "incoming" tangent control point
			l[ n ] = new HingeReshapeHandle( p, (Point2D) m_points.get( n + 1 ), (Point2D) m_points.get( n + 2 ) );
		}
	}

	return l;

}


protected CoReshapeHandleIF[] createPointHandles()
{
	if
		( m_isClosed )
	{
		return createClosedPointHandles();
	} else {
		return createNonClosedPointHandles();
	}
}


public CoShapeIF deepClone()
{
	CoCubicBezierCurveShape s = (CoCubicBezierCurveShape) super.deepClone();

	m_strokedShape = null;

	return s;
}


protected CoShapeIF doCreateExpandedInstance( double delta )
{
	if
		( delta == 0 )
	{
		return this;
	}

	if
		( m_points.isEmpty() )
	{
		return this;
	}








	
	abstract class Segment
	{
		public abstract void addPoints( Point2D from, List target );
		public abstract Point2D getTo();
	};

	class LineSegment extends Segment
	{
		private Point2D m_to;
		
		public LineSegment( double [] f )
		{
			m_to = new Point2D.Double( f[ 0 ], f[ 1 ] );
		}
		public void addPoints( Point2D from, List target )
		{
			target.add( from );
			target.add( m_to );
			target.add( m_to );
		}
		public Point2D getTo() { return m_to; }
	};

	class CubicSegment extends Segment
	{
		private Point2D m_p0;
		private Point2D m_p1;
		private Point2D m_to;
		
		public CubicSegment( double [] f )
		{
			m_p0 = new Point2D.Double( f[ 0 ], f[ 1 ] );
			m_p1 = new Point2D.Double( f[ 2 ], f[ 3 ] );
			m_to = new Point2D.Double( f[ 4 ], f[ 5 ] );
		}
		public void addPoints( Point2D from, List target )
		{
			target.add( m_p0 );
			target.add( m_p1 );
			target.add( m_to );
		}
		public Point2D getTo() { return m_to; }
	};

	
	
	
	
	
	
	
		
	boolean isNeg = delta < 0;
	
	List l = new ArrayList();
	
	Stroke stroke = new BasicStroke( (float) Math.abs( delta ) * 2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER );
	PathIterator i = stroke.createStrokedShape( getShape() ).getPathIterator( null );

	double [] f = new double [ 6 ];


	
	Point2D p0 = null;
	
	while
		( ! i.isDone() )
	{
		int t = i.currentSegment( f );

		switch
			( t )
		{
			case PathIterator.SEG_MOVETO :
			p0 = new Point2D.Double( f[ 0 ], f[ 1 ] );
			break;
			
			case PathIterator.SEG_LINETO :
			l.add( new LineSegment( f ) );
			break;
			
			case PathIterator.SEG_QUADTO :
			break;
			
			case PathIterator.SEG_CUBICTO :
			l.add( new CubicSegment( f ) );
			break;
			
		}

		i.next();
	}

	int N = l.size() / 2;
	if
		( isNeg )
	{
		int n = N;
		while	( N-- > 0 ) l.remove( n );
	} else {
		p0 = ( (Segment) l.get( N - 1 ) ).getTo();
		while	( N-- > 0 ) l.remove( 0 );
	}
	
	if ( ! l.isEmpty() ) l.remove( l.size() - 1 );

	List l2 = new ArrayList();

	l2.add( p0 );
	Point2D p = p0;
	for
		( int n = 0; n < l.size(); n++ )
	{
		Segment s = (Segment) l.get( n );
		s.addPoints( p, l2 );
		p = s.getTo();
	}
	
	CoCubicBezierCurveShape s = new CoCubicBezierCurveShape( l2, m_isClosed );

	return s;
}


// assure correct set of tangent control points at ends

private void fixClosedPoints()
{
	int I = m_points.size();

	if ( I < 2 ) return;
	
	boolean hasExtraPoints = ( I % 3 == 0 );
	
	if
		( m_isClosed )
	{
		if
			( hasExtraPoints )
		{
			m_points.remove( --I );
			m_points.remove( --I );
		}
	
		Point2D p0 = (Point2D) m_points.get( I - 1 );
		Point2D p1 = (Point2D) m_points.get( I - 2 );
		m_points.add( new Point( 2 * p0.getX() - p1.getX(), 2 * p0.getY() - p1.getY() ) );

		p0 = (Point2D) m_points.get( 0 );
		p1 = (Point2D) m_points.get( 1 );
		m_points.add( new Point( 2 * p0.getX() - p1.getX(), 2 * p0.getY() - p1.getY() ) );

		normalize();
	} else {
		if
			( hasExtraPoints )
		{
			m_points.remove( --I );
			m_points.remove( --I );
			normalize();
		}
	}
}


// calculate the bounds of a segment

private void getBounds( Point2D p0, Point2D p1, Point2D p2, Point2D p3, Rectangle2D r )
{
	// endpoints
	double x0 = Math.min( p0.getX(), p3.getX() );
	double x1 = Math.max( p0.getX(), p3.getX() );
	double y0 = Math.min( p0.getY(), p3.getY() );
	double y1 = Math.max( p0.getY(), p3.getY() );

	
	// find the extreme points of the x polynom
	double A = p3.getX() - 3 * p2.getX() + 3 * p1.getX() - p0.getX();
	double B = 3 * p2.getX() - 6 * p1.getX() + 3 * p0.getX();
	double C = 3 * p1.getX() - 3 * p0.getX();
	double D = p0.getX();
	
	if
		( A == 0 )
	{
		if
			( B != 0 )
		{
			double t = - C / ( 2 * B );
			if
				( ( t >= 0 ) && ( t <= 1 ) )
			{
				double x = t * t * B + t * C + D;
				x0 = Math.min( x0, x );
				x1 = Math.max( x1, x );
			}
		}
	} else {
		double t = - B / ( 3 * A ) + Math.sqrt( Math.pow( B / ( 3 * A ), 2 ) - C / ( 3 * A ) );
		if
			( ( t >= 0 ) && ( t <= 1 ) )
		{
			double x = t * t * t * A + t * t * B + t * C + D;
			x0 = Math.min( x0, x );
			x1 = Math.max( x1, x );
		}
		
		t = - B / ( 3 * A ) - Math.sqrt( Math.pow( B / ( 3 * A ), 2 ) - C / ( 3 * A ) );
		if
			( ( t >= 0 ) && ( t <= 1 ) )
		{
			double x = t * t * t * A + t * t * B + t * C + D;
			x0 = Math.min( x0, x );
			x1 = Math.max( x1, x );
		}
	}

	
	// find the extreme points of the y polynom
	A = p3.getY() - 3 * p2.getY() + 3 * p1.getY() - p0.getY();
	B = 3 * p2.getY() - 6 * p1.getY() + 3 * p0.getY();
	C = 3 * p1.getY() - 3 * p0.getY();
	D = p0.getY();
	
	if
		( A == 0 )
	{
		if
			( B == 0 )
		{
		} else {
			double t = - C / ( 2 * B );
			if
				( ( t >= 0 ) && ( t <= 1 ) )
			{
				double y = t * t * B + t * C + D;
				y0 = Math.min( y0, y );
				y1 = Math.max( y1, y );
			}
		}
	} else {
		double t = - B / ( 3 * A ) + Math.sqrt( Math.pow( B / ( 3 * A ), 2 ) - C / ( 3 * A ) );
		if
			( ( t >= 0 ) && ( t <= 1 ) )
		{
			double y = t * t * t * A + t * t * B + t * C + D;
			y0 = Math.min( y0, y );
			y1 = Math.max( y1, y );
		}
		
		t = - B / ( 3 * A ) - Math.sqrt( Math.pow( B / ( 3 * A ), 2 ) - C / ( 3 * A ) );
		if
			( ( t >= 0 ) && ( t <= 1 ) )
		{
			double y = t * t * t * A + t * t * B + t * C + D;
			y0 = Math.min( y0, y );
			y1 = Math.max( y1, y );
		}
	}

	r.setRect( x0, y0, x1 - x0, y1 - y0 );
}


private static int getContinuity()
{
	return m_continuity;
}


public String getFactoryKey()
{
	return CUBIC_BEZIER_CURVE;
}


// get the last interpolating control point

public CoReshapeHandleIF getLastPointHandle()
{
	Point p = null;
	
	if
		( m_isClosed )
	{
		p = (Point) m_points.get( m_points.size() - 1 - 2 );
	} else {
		p = (Point) m_points.get( m_points.size() - 1 );
	}

	return new ReshapeHandle( p );
}


public Shape getShape()
{
	if
		( m_shape == null )
	{
		m_shape = new GeneralPath();

		// the curve
		if
			( ! m_points.isEmpty() )
		{
			Iterator i = m_points.iterator();
			Point2D p0 = (Point2D) i.next();
			Point2D p1 = null;
			Point2D p2 = null;
			Point2D p3 = null;
			m_shape.moveTo( (float) ( m_x + m_width * p0.getX() ), (float) ( m_y + m_height * p0.getY() ) );
			
			while
				( i.hasNext() )
			{
				p1 = (Point2D) i.next();
				if ( ! i.hasNext() ) break;
				p2 = (Point2D) i.next();
				if ( ! i.hasNext() ) break;
				p3 = (Point2D) i.next();
				
				m_shape.curveTo( (float) ( m_x + m_width * p1.getX() ), (float) ( m_y + m_height * p1.getY() ),
												 (float) ( m_x + m_width * p2.getX() ), (float) ( m_y + m_height * p2.getY() ),
												 (float) ( m_x + m_width * p3.getX() ), (float) ( m_y + m_height * p3.getY() ) );
			}

			if
				( m_isClosed && m_usePointHandles )
			{
				p3 = p0;
				m_shape.curveTo( (float) ( m_x + m_width * p1.getX() ), (float) ( m_y + m_height * p1.getY() ),
												 (float) ( m_x + m_width * p2.getX() ), (float) ( m_y + m_height * p2.getY() ),
												 (float) ( m_x + m_width * p3.getX() ), (float) ( m_y + m_height * p3.getY() ) );
					
			}

			// tangent lines
			if
				( m_editMode )
			{
				i = m_points.iterator();
				p0 = (Point2D) i.next();
				Point2D p = p0;
				while
					( i.hasNext() )
				{
					p1 = (Point2D) i.next();
					if ( ! i.hasNext() ) break;
					p2 = (Point2D) i.next();
					if ( ! i.hasNext() ) break;
					p3 = (Point2D) i.next();
					
					m_shape.moveTo( (float) ( m_x + m_width * p0.getX() ), (float) ( m_y + m_height * p0.getY() ) );
					m_shape.lineTo( (float) ( m_x + m_width * p1.getX() ), (float) ( m_y + m_height * p1.getY() ) );
					m_shape.moveTo( (float) ( m_x + m_width * p2.getX() ), (float) ( m_y + m_height * p2.getY() ) );
					m_shape.lineTo( (float) ( m_x + m_width * p3.getX() ), (float) ( m_y + m_height * p3.getY() ) );
					p0 = p3;
				}

				if
					( m_isClosed )
				{
					p3 = p;
					m_shape.moveTo( (float) ( m_x + m_width * p0.getX() ), (float) ( m_y + m_height * p0.getY() ) );
					m_shape.lineTo( (float) ( m_x + m_width * p1.getX() ), (float) ( m_y + m_height * p1.getY() ) );
					m_shape.moveTo( (float) ( m_x + m_width * p2.getX() ), (float) ( m_y + m_height * p2.getY() ) );
					m_shape.lineTo( (float) ( m_x + m_width * p3.getX() ), (float) ( m_y + m_height * p3.getY() ) );
						
				}
			}
		}
	}

	return m_shape;
}


private Shape getStrokedShape( double strokeWidth )
{
	if
		( ( m_strokedShape == null ) || ( m_strokeWidth != strokeWidth ) )
	{
		m_strokeWidth = strokeWidth;
		if
			( strokeWidth == 0 )
		{
			m_strokedShape = getShape();
		} else {
			Stroke stroke = new BasicStroke( 2 * (float) m_strokeWidth );
			m_strokedShape = stroke.createStrokedShape( getShape() );
		}
	}

	return m_strokedShape;
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
		// extrapolate before
		Point2D p0 = getPoint( 0 );
		Point2D p1 = getPoint( 1 );
		double X = p0.getX();
		double Y = p0.getY();
		double x = p1.getX();
		double y = p1.getY();
		double dx = X - x;
		double dy = Y - y;

		m_points.add( 0, new Point( X + 1 * dx, Y + 1 * dy ) );
		m_points.add( 0, new Point( X + 2 * dx, Y + 2 * dy ) );
		m_points.add( 0, p = new Point( X + 3 * dx, Y + 3 * dy ) );
		
	} else if
		( ( i == -1 ) || ( i >= I ) )
	{	
		// extrapolate beyond
		Point2D p0 = getPoint( I - 1 );
		Point2D p1 = getPoint( I - 2 );
		double X = p0.getX();
		double Y = p0.getY();
		double x = p1.getX();
		double y = p1.getY();
		double dx = X - x;
		double dy = Y - y;
		m_points.add( new Point( X + 1 * dx, Y + 1 * dy ) );
		m_points.add( new Point( X + 2 * dx, Y + 2 * dy ) );
		m_points.add( p = new Point( X + 3 * dx, Y + 3 * dy ) );

	} else {
		// interpolate
		Point2D p3 = getPoint( i );
		Point2D p2 = getPoint( i - 1 );
		Point2D p1 = getPoint( i - 2 );
		Point2D cp = getPoint( i - 3 );

		double x1 = p1.getX();
		double y1 = p1.getY();
		double x2 = p2.getX();
		double y2 = p2.getY();
		double x3 = p3.getX();
		double y3 = p3.getY();
		double xc = cp.getX();
		double yc = cp.getY();
		
		double xq = ( x3 - 3 * x2 + 3 * x1 - xc ) / 8 + ( 3 * x2 - 6 * x1 + 3 *xc ) / 4 + ( 3 * x1 - 3 * xc ) / 2 + xc;
		double yq = ( y3 - 3 * y2 + 3 * y1 - yc ) / 8 + ( 3 * y2 - 6 * y1 + 3 *yc ) / 4 + ( 3 * y1 - 3 * yc ) / 2 + yc;

		Point q = new Point( xq, yq );
		Point q1 = new Point( ( x1 + xc ) / 2, ( y1 + yc ) / 2 );
		Point q2 = new Point( ( x2 + 2 * x1 + xc ) / 4, ( y2 + 2 * y1 + yc ) / 4 );
		Point r1 = new Point( xq + ( x3 + x2 - x1 - xc ) / 8, yq + ( y3 + y2 - y1 - yc ) / 8 );
		Point r2 = new Point( xq + ( 3 * x3 + x2 - 3 * x1 - xc ) / 8, yq + ( 3 * y3 + y2 - 3 * y1 - yc ) / 8 );

		m_points.remove( i - 2 );
		m_points.remove( i - 2 );
		
		m_points.add( i - 2, r2 );
		m_points.add( i - 2, r1 );
		m_points.add( i - 2, q );
		m_points.add( i - 2, q2 );
		m_points.add( i - 2, q1 );
		
		p = q;
	}
	
	normalize();

	invalidate();
	
	return p;
}


/**
 * Adds an anchor point with the specified coordinates and
 * automatically calculates the associated control points.
 *
 * @author Dennis Malmström
 */
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

		double dx = x - X;
		double dy = y - Y;

		if
			( n > 0 )
		{
			m_points.add( new Point( X + 1 * dx / 3, Y + 1 * dy / 3 ) );
			m_points.add( new Point( X + 2 * dx / 3, Y + 2 * dy / 3 ) );
			m_points.add( p = new Point( x, y ) );
		} else {
			m_points.add( 0, p = new Point( x, y ) );
			m_points.add( 1, new Point( X + 1 * dx / 3, Y + 1 * dy / 3 ) );
			m_points.add( 2, new Point( X + 2 * dx / 3, Y + 2 * dy / 3 ) );
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
			p = (Point) m_points.get( m_points.size() - 1 );
			double X = p.getX();
			double Y = p.getY();
			double dx = x - X;
			double dy = y - Y;
			m_points.add( new Point( X + 1 * dx / 3, Y + 1 * dy / 3 ) );
			m_points.add( new Point( X + 2 * dx / 3, Y + 2 * dy / 3 ) );
			m_points.add( p = new Point( x, y ) );

		} else {
			if ( n < 0 ) n = 0;
			p = (Point) m_points.get( n );
			double X = p.getX();
			double Y = p.getY();
			double dx = x - X;
			double dy = y - Y;
			m_points.add( n, new Point( X + 1 * dx / 3, Y + 1 * dy / 3 ) );
			m_points.add( n, new Point( X + 2 * dx / 3, Y + 2 * dy / 3 ) );
			m_points.add( n, p = new Point( x, y ) );
		}
		normalize();
	}

	invalidate();
	
	return p;
}


protected void invalidate()
{
	super.invalidate();
	m_strokedShape = null;
}


// use Java2D hit testing

public boolean isInside( Point2D p, double strokeWidth, double outsideStrokeWidth )
{
	if
		( getShape().contains( p ) )
	{
		return true;
	}
	
	if
		( getStrokedShape( outsideStrokeWidth ).contains( p ) )
	{
		return true;
	}

	return false;
}


// isInside is accurate -> isOutside is complement

public boolean isOutside( Point2D m, double strokeWidth, double outsideStrokeWidth )
{
	return ! isInside( m, strokeWidth, outsideStrokeWidth );
}


public void normalize()
{
	Iterator i = m_points.iterator();
	if ( ! i.hasNext() ) return;
	
	Point2D p = (Point2D) i.next();
	double x = p.getX();
	double y = p.getY();
	double X = x;
	double Y = y;

	// use segment box instead
	Point2D p0 = p;
	Point2D p1 = null;
	Point2D p2 = null;
	Point2D p3 = null;
			
	Rectangle2D r = new Rectangle2D.Double();
	
	while
		( i.hasNext() )
	{
		p1 = (Point2D) i.next();
		if ( ! i.hasNext() ) break;
		p2 = (Point2D) i.next();
		if 
			( ! i.hasNext() )
		{
			if
				( m_isClosed )
			{
				p3 = p;

				getBounds( p0, p1, p2, p3, r );

				x = Math.min( x, r.getX() );
				y = Math.min( y, r.getY() );
				X = Math.max( X, r.getX() + r.getWidth() );
				Y = Math.max( Y, r.getY() + r.getHeight() );
			}
			break;
		}
		
		p3 = (Point2D) i.next();

		getBounds( p0, p1, p2, p3, r );

		x = Math.min( x, r.getX() );
		y = Math.min( y, r.getY() );
		X = Math.max( X, r.getX() + r.getWidth() );
		Y = Math.max( Y, r.getY() + r.getHeight() );
		
		p0 = p3;
	}


	
	double w = X - x;
	double h = Y - y;
	
	m_x += m_width * x;
	m_y += m_height * y;
	m_width *= w;
	m_height *= h;
	if ( m_width == 0 ) m_width = w;
	if ( m_height == 0 ) m_height = h;
	
	i = m_points.iterator();
	while
		( i.hasNext() )
	{
		p = (Point2D) i.next();
		X = ( w > 0 ) ? ( p.getX() - x ) / w : 0;
		Y = ( h > 0 ) ? ( p.getY() - y ) / h : 0;
		if ( m_width < 0 ) X = 1 - X;
		if ( m_height < 0 ) Y = 1 - Y;
		p.setLocation( X, Y );
	}

	m_x = Math.min( m_x, m_x + m_width );
	m_y = Math.min( m_y, m_y + m_height );
	m_width = Math.abs( m_width );
	m_height = Math.abs( m_height );

	invalidate();

}


public void removePoint( double x, double y )
{
	removePoint( getIndexOfPoint( x, y ) );
}


public void removePoint( int i )
{
	int I = m_points.size();
	if ( I <= 4 ) return;

	if
		( i != -1 )
	{
		if
			( i % 3 == 0 )
		{
			if
				( i == 0 )
			{
				m_points.remove( 0 );
				m_points.remove( 0 );
				m_points.remove( 0 );
			} else if
				( i == m_points.size() - 1 )
			{
				m_points.remove( m_points.size() - 1 );
				m_points.remove( m_points.size() - 1 );
				m_points.remove( m_points.size() - 1 );
			} else {
				m_points.remove( i - 1 );
				m_points.remove( i - 1 );
				m_points.remove( i - 1 );
			}
			normalize();
			invalidate();
		}
	}

}


public void reorderPoints()
{
	int I = m_points.size();

	if
		( I % 3 == 0 )
	{
		m_points.remove( --I );
		m_points.remove( --I );
	}

	for
		( int i = 0, j = I - 1; i < I / 2; i++, j-- )
	{
		Object p = m_points.get( i );
		m_points.set( i, m_points.get( j ) );
		m_points.set( j, p );
	}

	if
		( m_isClosed )
	{
		fixClosedPoints();
	}
	
	invalidate();
}


public void setClosed( boolean b )
{
	if
		( b != m_isClosed )
	{
		super.setClosed( b );
	
		fixClosedPoints();
	}
}


public void setEditMode( boolean b )
{
	super.setEditMode( b );
	m_shape = null;
}


// assure second derivative continuity for this curve

public void smooth()
{
	int I = m_points.size();

	if ( I < 4 ) return;

	double k = 3;
	
	if
		( m_isClosed )
	{
		Point p0 = (Point) m_points.get( 3 );
		Point p1 = (Point) m_points.get( 0 );
		Point p2 = (Point) m_points.get( I - 3 );

		double dx = ( p0.getX() - p2.getX() ) / k;
		double dy = ( p0.getY() - p2.getY() ) / k;

		double l1 = Math.sqrt( Math.pow( p0.getX() - p1.getX(), 2 ) + Math.pow( p0.getY() - p1.getY(), 2 ) );
		double l2 = Math.sqrt( Math.pow( p2.getX() - p1.getX(), 2 ) + Math.pow( p2.getY() - p1.getY(), 2 ) );

		( (Point) m_points.get( 0 ) ).setLocation( p1.getX() + dx * l1 / ( l1 + l2 ), p1.getY() + dy * l1 / ( l1 + l2 ) );
		( (Point) m_points.get( I - 1 ) ).setLocation( p1.getX() - dx * l2 / ( l1 + l2 ), p1.getY() - dy * l2 / ( l1 + l2 ) );

		for
			( int i = 3; i < I - 3; i += 3 )
		{
			p0 = (Point) m_points.get( i - 3 );
			p1 = (Point) m_points.get( i );
			p2 = (Point) m_points.get( i + 3 );

			dx = ( p0.getX() - p2.getX() ) / k;
			dy = ( p0.getY() - p2.getY() ) / k;

			l1 = Math.sqrt( Math.pow( p0.getX() - p1.getX(), 2 ) + Math.pow( p0.getY() - p1.getY(), 2 ) );
			l2 = Math.sqrt( Math.pow( p2.getX() - p1.getX(), 2 ) + Math.pow( p2.getY() - p1.getY(), 2 ) );

			( (Point) m_points.get( i - 1 ) ).setLocation( p1.getX() + dx * l1 / ( l1 + l2 ), p1.getY() + dy * l1 / ( l1 + l2 ) );
			( (Point) m_points.get( i + 1 ) ).setLocation( p1.getX() - dx * l2 / ( l1 + l2 ), p1.getY() - dy * l2 / ( l1 + l2 ) );
		}
	} else {
		
		Point p0 = (Point) m_points.get( 0 );
		Point p1 = (Point) m_points.get( 3 );
		( (Point) m_points.get( 1 ) ).setLocation( ( 2 * p0.getX() + p1.getX() ) / 3, ( 2 * p0.getY() + p1.getY() ) / k );
		
		p0 = (Point) m_points.get( I - 1 );
		p1 = (Point) m_points.get( I - 4 );
		( (Point) m_points.get( I - 2 ) ).setLocation( ( 2 * p0.getX() + p1.getX() ) / 3, ( 2 * p0.getY() + p1.getY() ) / k );

		for
			( int i = 3; i < I - 1; i += 3 )
		{
			p0 = (Point) m_points.get( i - 3 );
			p1 = (Point) m_points.get( i );
			Point p2 = (Point) m_points.get( i + 3 );

			double dx = ( p0.getX() - p2.getX() ) / k;
			double dy = ( p0.getY() - p2.getY() ) / k;

			double l1 = Math.sqrt( Math.pow( p0.getX() - p1.getX(), 2 ) + Math.pow( p0.getY() - p1.getY(), 2 ) );
			double l2 = Math.sqrt( Math.pow( p2.getX() - p1.getX(), 2 ) + Math.pow( p2.getY() - p1.getY(), 2 ) );

			( (Point) m_points.get( i - 1 ) ).setLocation( p1.getX() + dx * l1 / ( l1 + l2 ), p1.getY() + dy * l1 / ( l1 + l2 ) );
			( (Point) m_points.get( i + 1 ) ).setLocation( p1.getX() - dx * l2 / ( l1 + l2 ), p1.getY() - dy * l2 / ( l1 + l2 ) );
		}
	}
	
}


public String toString()
{
	return "CoCubicBezierCurveShape: " + m_x + " " + m_y + "   " + m_width + " " + m_height + " " + m_isClosed + " " + m_points.size();
}
}