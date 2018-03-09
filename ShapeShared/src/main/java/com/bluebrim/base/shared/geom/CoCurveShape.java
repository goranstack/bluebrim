package com.bluebrim.base.shared.geom;
import java.awt.geom.*;
import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.xml.shared.*;

/**
 * Abstract superclass for shapes defined by a sequence of control points
 * 
 * @author: Dennis Malmström
 */

public abstract class CoCurveShape extends CoBoundingShape implements CoCurveShapeIF
{
protected CoCurveShape() 
{
	super();
}
	// geometry
	protected double m_x;
	protected double m_y;
	protected double m_width;
	protected double m_height;
	protected boolean m_isClosed;
	protected List m_points = new ArrayList(); // [ Point2D ]

	// awt representation
	protected transient GeneralPath m_shape;

	// control point handles
	protected transient CoReshapeHandleIF[] m_pointHandles;

	// which set of reshape handles to use
	public static boolean m_usePointHandles = false;

	// inner subclass of com.bluebrim.base.shared.CoPoint2DDouble allowing CoCurveShape to catch manipulation of control points
	protected class Point extends CoPoint2DDouble
	{
		public Point()                     { super(); }
		public Point( double x, double y ) { super( x, y ); }
		public Point( Point2D p )          { super( p.getX(), p.getY() ); }
		
		public void setLocation( double x, double y )
		{
			super.setLocation( x, y );
			CoCurveShape.this.invalidate();
		}
		public void setLocation( Point2D p )
		{
			super.setLocation( p );
			CoCurveShape.this.invalidate();
		}
	};

	
	// control point reshape handle
	protected class ReshapeHandle implements CoReshapeHandleIF
	{
		protected Point2D m_point;

		public ReshapeHandle( Point2D p )
		{
			CoAssertion.assertTrue( p != null, "" );
			m_point = p;
		}
		public final double getX() { return CoCurveShape.this.m_x + CoCurveShape.this.m_width * m_point.getX(); }
		public final double getY() { return CoCurveShape.this.m_y + CoCurveShape.this.m_height * m_point.getY(); }
		public final int getEdgeMask() { return ALL_EDGE_MASK; }
		public void move( double dx, double dy )
		{
			if ( CoCurveShape.this.m_width > 0 ) dx = dx / CoCurveShape.this.m_width;
			if ( CoCurveShape.this.m_height > 0 ) dy = dy / CoCurveShape.this.m_height;
			m_point.setLocation( m_point.getX() + dx, m_point.getY() + dy );
			normalize();
		}
	};

	
	// mutable proxy
	protected class MutableProxy extends CoShape.MutableProxy implements CoRemoteCurveShapeIF
	{
		// non mutating methods, delegate to outer instance
		public boolean isClosed(){ return CoCurveShape.this.isClosed(); }
		public int getPointCount() { return CoCurveShape.this.getPointCount();  }
		public Point2D getPoint( int i ) { return CoCurveShape.this.getPoint( i ); }
		public int getIndexOfPoint( double x, double y ) { return CoCurveShape.this.getIndexOfPoint( x, y ); }
		
		// mutating methods, delegate to outer instance and notify owner
		public void setClosed( boolean b )
		{
			if ( b == CoCurveShape.this.isClosed() ) return;
			CoCurveShape.this.setClosed( b ); 
			notifyOwner( CoShapeIF.Owner.X | CoShapeIF.Owner.Y | CoShapeIF.Owner.WIDTH | CoShapeIF.Owner.HEIGHT );
		}

		public Point2D addPoint( double x, double y )
		{
			Point2D p = CoCurveShape.this.addPoint( x, y ); 
			notifyOwner( CoShapeIF.Owner.X | CoShapeIF.Owner.Y | CoShapeIF.Owner.WIDTH | CoShapeIF.Owner.HEIGHT );
			return p;
		}

		public Point2D insertPoint( int index )
		{
			Point2D p = CoCurveShape.this.insertPoint( index ); 
			notifyOwner( CoShapeIF.Owner.X | CoShapeIF.Owner.Y | CoShapeIF.Owner.WIDTH | CoShapeIF.Owner.HEIGHT );
			return p;
		}

		public void removePoint( double x, double y )
		{
			CoCurveShape.this.removePoint( x, y ); 
			notifyOwner( CoShapeIF.Owner.X | CoShapeIF.Owner.Y | CoShapeIF.Owner.WIDTH | CoShapeIF.Owner.HEIGHT );
		}

		public void reorderPoints()
		{
			CoCurveShape.this.reorderPoints();
			notifyOwner( 0 );
		}
	}

	public static final String XML_IS_CLOSED = "isClosed";
	// xml tag constants
	public static final String XML_POINTS = "points";

/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-29
 */
 
protected CoCurveShape( Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	super();

	// xml init
	NamedNodeMap map = node.getAttributes();
	
	m_x = CoModelBuilder.getDoubleAttrVal( map, XML_X, m_x );
	m_y = CoModelBuilder.getDoubleAttrVal( map, XML_Y, m_y );
	m_width = CoModelBuilder.getDoubleAttrVal( map, XML_WIDTH, m_width );
	m_height = CoModelBuilder.getDoubleAttrVal( map, XML_HEIGHT, m_height );
	
	m_isClosed = CoModelBuilder.getBoolAttrVal( map, XML_IS_CLOSED, m_isClosed );

	String points = CoModelBuilder.getAttrVal( map, XML_POINTS, null );

	if
		( points != null )
	{
		StringTokenizer t = new StringTokenizer( points, " " );
		
		while
			( t.hasMoreTokens() )
		{
			double x = CoXmlUtilities.parseDouble( t.nextToken(), Double.NaN );
			
			if
				( t.hasMoreTokens() )
			{
				double y = CoXmlUtilities.parseDouble( t.nextToken(), Double.NaN );
				if
					( ! Double.isNaN( x ) && ! Double.isNaN( y ) )
				m_points.add( new Point( x, y ) );
			}
		}
	}
}


public void clear()
{
	m_points.clear();
}


protected CoShape.MutableProxy createMutableProxy()
{
	return new MutableProxy();	
}


protected CoReshapeHandleIF[] createPointHandles()
{
	CoReshapeHandleIF[] l = new CoReshapeHandleIF [ m_points.size() ];
	int n = 0;
	Iterator i = m_points.iterator();
	while
		( i.hasNext() )
	{
		l[ n++ ] = new ReshapeHandle( (Point2D) i.next() );
	}
	
	return l;

}


public CoShapeIF deepClone()
{
	CoCurveShape s = (CoCurveShape) super.deepClone();
/*
	s.m_x = m_x;
	s.m_y = m_y;
	s.m_width = m_width;
	s.m_height = m_height;
	s.m_isClosed = m_isClosed;
*/

	// don't share control points
	s.m_points = new ArrayList();
	Iterator i = m_points.iterator();
	while
		( i.hasNext() )
	{
		Point2D p = (Point2D) i.next();
		s.m_points.add( s.new Point( p.getX(), p.getY() ) );
	}

	s.m_reshapeHandles = s.createReshapeHandles();
	s.m_pointHandles = s.createPointHandles();
	s.m_moveHandle = s.createMoveHandle();

	s.m_shape = null;

	return s;
}


public boolean equals( Object s )
{
	if ( s == this ) return true;

	if ( ! getClass().isInstance( s ) ) return false;
	
	CoCurveShape p = (CoCurveShape) s;
	return
		(
			( m_isClosed == p.m_isClosed )
		&&
			( m_x == p.m_x )
		&&
			( m_y == p.m_y )
		&&
			( m_width == p.m_width )
		&&
			( m_height == p.m_height )
		&&
			( m_points.equals( p.m_points ) )
		);
}


public double getHeight()
{
	return m_height;
}


public int getIndexOfPoint( double x, double y )
{
	int I = m_points.size();
	if
		( I > 3 )
	{
		x = ( x - m_x ) / m_width;
		y = ( y - m_y ) / m_height;
	}
	
	for
		( int i = 0; i < I; i++ )
	{
		Point p = (Point) m_points.get( i );
		if
			( closeEnough( p.x, p.y, x, y ) )
		{
			return i;
		}
	}
	
	return -1;
}


public Point2D getPoint( int i )
{
	return (Point2D) m_points.get( i );
}


public int getPointCount()
{
	return m_points.size();
}


public CoReshapeHandleIF[] getReshapeHandles()
{
	if
		( m_usePointHandles )
	{
		if
			( m_pointHandles == null )
		{
			m_pointHandles = createPointHandles();
		}
		return m_pointHandles;
	} else {
		if
			( m_reshapeHandles == null )
		{
			m_reshapeHandles = createReshapeHandles();
		}
		return m_reshapeHandles;
	}
}


public double getWidth()
{
	return m_width;
}


public double getX()
{
	return m_x;
}


public double getY()
{
	return m_y;
}


protected void invalidate()
{
	super.invalidate();
	
	m_shape = null;
	m_reshapeHandles = null;
	m_pointHandles = null;
}


public boolean isClosed()
{
	return m_isClosed;
}


public boolean isClosedShape()
{
	return false;
}


public void normalize()
{
	Iterator i = m_points.iterator();
	Point2D p = (Point2D) i.next();
	double x = p.getX();
	double y = p.getY();
	double X = x;
	double Y = y;
	
	while
		( i.hasNext() )
	{
		p = (Point2D) i.next();
		x = Math.min( x, p.getX() );
		y = Math.min( y, p.getY() );
		X = Math.max( X, p.getX() );
		Y = Math.max( Y, p.getY() );
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


public void setClosed( boolean b )
{
	m_isClosed = b;
	invalidate();
}


public void setHeight( double h )
{
	m_height = h;
	invalidate();
}


public void setWidth( double w )
{
	m_width = w;
	invalidate();
}


public void setX( double x )
{
	m_x = x;
	invalidate();
}


public void setY( double y )
{
	m_y = y;
	invalidate();
}


/*
 * Used at XML export
 * Helena Rankegård 2001-10-29
 */
 
public void xmlVisit( CoXmlVisitorIF visitor )
{
	super.xmlVisit( visitor );
	
	visitor.exportAttribute( XML_X, Double.toString( m_x ) );
	visitor.exportAttribute( XML_Y, Double.toString( m_y ) );
	visitor.exportAttribute( XML_WIDTH, Double.toString( m_width ) );
	visitor.exportAttribute( XML_HEIGHT, Double.toString( m_height ) );

	StringBuffer b = new StringBuffer();

	int I = m_points.size();
	for
		( int i = 0; i < I; i++ )
	{
		Point2D p = (Point2D) m_points.get( i );
		if ( i > 0 ) b.append( ' ' );
		b.append( p.getX() );
		b.append( ' ' );
		b.append( p.getY() );
	}
	
	visitor.exportAttribute( XML_POINTS, b.toString() );
	visitor.exportAttribute( XML_IS_CLOSED, ( m_isClosed ? Boolean.TRUE : Boolean.FALSE ).toString() );
}
}