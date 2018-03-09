package com.bluebrim.base.shared.geom;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;

/**
 * A shape resulting from subtracting a set of shapes from a base shape
 * 
 * @author: Dennis Malmström
 */

public class CoRunAroundShape extends CoBoundingShape
{
	protected CoShape m_baseShape = new CoRectangleShape(); // the base shape (left side of the subtraction)
	protected AffineTransform m_transform = new AffineTransform(); // translation of the base shape
	
	protected List m_subtractedShapes = new ArrayList(); // [CoShapeIF] subtracted shapes

	// shape arithmatic engine
	protected transient Area m_area;
public CoRunAroundShape(CoImmutableShapeIF shape)
{
	super();
	
	m_baseShape = (CoShape) shape.deepClone();

	m_transform.setToTranslation( m_baseShape.getX(), m_baseShape.getY() );
	m_baseShape.setTranslation( 0, 0 );
}
// calculate : base shape - subtracted shapes

protected Area computeArea()
{
	Area a = new Area( m_baseShape.getShape() );
	
	Iterator i = m_subtractedShapes.iterator();
	while
		( i.hasNext() )
	{
		Shape s = ( (CoShapeIF) i.next() ).getShape();
		a.subtract( new Area( s ) );
	}

	return a;
}
protected CoShape copyForTranslation()
{
	try
	{
		CoRunAroundShape c = (CoRunAroundShape) clone();
		c.m_transform = new AffineTransform(m_transform);
		c.invalidateArea();
		return c;
	}
	catch (CloneNotSupportedException e)
	{
		throw new InternalError();
	}
}
protected CoReshapeHandleIF[] createReshapeHandles()
{
	return new CoReshapeHandleIF[ 0 ];
}
public CoShapeIF deepClone()
{
	CoRunAroundShape shape = (CoRunAroundShape) super.deepClone();
	
	shape.m_transform = new AffineTransform( m_transform );
	
	shape.m_baseShape = (CoBoundingShape) m_baseShape.deepClone();
	
	int I = m_subtractedShapes.size();
	List ss = new ArrayList();
	for
		( int i = 0; i < I; i++ )
	{
		ss.add( (( CoShape) m_subtractedShapes.get( i ) ).deepClone() );
	}
	shape.m_subtractedShapes = ss;
	shape.m_area = computeArea();
	return shape;
}
protected CoShapeIF doCreateExpandedInstance( double delta )
{
	if
		( delta == 0 )
	{
		return this;
	}

	CoRunAroundShape s = new CoRunAroundShape( m_baseShape.createExpandedInstance( delta ) ); // expand base shape
	
	Iterator i = m_subtractedShapes.iterator();
	while
		( i.hasNext() )
	{
		s.subtractSimpleShape( ( (CoShape) i.next() ).createExpandedInstance( - delta ) ); // shrink subtracted shapes
	}

	s.translateBy( m_transform.getTranslateX(), m_transform.getTranslateY() );
	
	return s;
}
public static void dump( java.awt.Shape shape ) 
{
	PathIterator pi = shape.getPathIterator( new AffineTransform() );

	double [] d = new double [ 6 ];

	System.err.println( "------------------" );
	
	while
		( ! pi.isDone() )
	{
		int type = pi.currentSegment( d );
			
		switch
			( type )
		{
	 		case PathIterator.SEG_MOVETO :
	 			System.err.println( "MOVE TO " + d[ 0 ] + ", " + d[ 1 ] );
	 		break;
	 		
	 		case PathIterator.SEG_LINETO :
	 			System.err.println( "LINE TO " + d[ 0 ] + ", " + d[ 1 ] );
	 		break;
	 		
	 		case PathIterator.SEG_QUADTO :
	 			System.err.println( "QUAD TO " + d[ 0 ] + ", " + d[ 1 ] + ", " + d[ 2 ] + ", " + d[ 3 ] );
	 		break;
	 		
	 		case PathIterator.SEG_CUBICTO :
	 			System.err.println( "CUB  TO " + d[ 0 ] + ", " + d[ 1 ] + ", " + d[ 2 ] + ", " + d[ 3 ] + ", " + d[ 4 ] + ", " + d[ 5 ] );
	 		break;
	 		
	 		case PathIterator.SEG_CLOSE :
	 			System.err.println( "CLOSE" );
	 		break;
		};

	pi.next();

		
	}
}
public boolean equals( Object s )
{
	if ( s == this ) return false;

	if ( ! ( s instanceof CoRunAroundShape ) ) return false;
	
	CoRunAroundShape r = (CoRunAroundShape) s;

	if ( ! m_transform.equals( r.m_transform ) ) return false;
	if ( ! m_baseShape.equals( r.m_baseShape ) ) return false;
	if ( m_subtractedShapes.size() != r.m_subtractedShapes.size() ) return false;

	Iterator i1 = m_subtractedShapes.iterator();
	while
		( i1.hasNext() )
	{
		if ( ! r.m_subtractedShapes.contains( (CoShape) i1.next() ) ) return false;
	}
	
	return true;
}
protected Area getArea()
{
	if ( m_area == null ) m_area = computeArea();
	return m_area;
}
// Add separated transaltion

public Rectangle2D getBounds2D()
{
	Rectangle2D b = super.getBounds2D();
	b.setRect( b.getX() + getX(), b.getY() + getY(), b.getWidth(), b.getHeight() );
	return b;
}
public String getFactoryKey()
{
	return null;
}
public double getHeight()
{
	return m_baseShape.getHeight();
}
public Shape getShape()
{
	double x = m_transform.getTranslateX();
	double y = m_transform.getTranslateY();
	if
		( m_subtractedShapes.isEmpty() )
	{
		CoShapeIF s = m_baseShape.copyTranslatedBy( x, y );
		return s.getShape();
	} else {
		
		// Area won't transform properly, let's use a GeneralPath instead.
		GeneralPath gp = new GeneralPath( getArea() );

		AffineTransform t = new AffineTransform();
		t.translate( x, y );
		
		gp.transform( m_transform );
		return gp;
	}

}
// Used to extract column geometry for text layout, see CoTrapetsesBuilder.getTrapetses

public List getTrapetses(Rectangle2D.Float bounds)
{
	Area a = computeArea();
	
	Area a2 = new Area( bounds );
	try
	{
		a2.transform( m_transform.createInverse() );
	}
	catch ( NoninvertibleTransformException ex )
	{
		CoAssertion.assertTrue( false, "Fatal exception in " + getClass() + ".getTrapetses( Rectangle2D.Float )\n" + ex );
	}
	a.intersect( a2 );
	
	return CoTrapetsesBuilder.getTrapetses( a.getPathIterator( m_transform, 2 ), bounds );
}
public double getWidth()
{
	return m_baseShape.getWidth();
}
public double getX()
{
	return m_transform.getTranslateX();
}
public double getY()
{
	return m_transform.getTranslateY();
}
// isInside and isOutside aren't accurate -> we must implement this method

public boolean hit( Graphics2D g, Rectangle r )
{
	boolean hit = m_baseShape.hit( g, r );
	if ( ! hit ) return false;

	Iterator i = m_subtractedShapes.iterator();
	while
		( i.hasNext() )
	{
		if ( ( (CoImmutableShapeIF) i.next() ).hit( g, r ) ) return false;
	}
	return true;
}
protected void invalidate()
{
	super.invalidate();

	invalidateArea();
}
protected void invalidateArea()
{
	m_area = null;
}
public boolean isClosedShape()
{
	return true;
}
// Since nothing can be known about the accuraccy of the isInside calls made to other shapes, nothing can be known about the accuracy of this method

public boolean isInside( Point2D p, double strokeWidth, double outsideStrokeWidth )
{
	// make sure it is inside base shape
	if ( ! m_baseShape.isInside( p, strokeWidth, outsideStrokeWidth ) ) return false;

	// check if outside subtracted shapes
	Iterator i = m_subtractedShapes.iterator();
	while
		( i.hasNext() )
	{
		if ( ( (CoImmutableShapeIF) i.next() ).isInside( p, -strokeWidth, -outsideStrokeWidth ) ) return false;
	}
	
	return true;
}
public void normalize()
{
}
// clear subtracted shapes

public void reset()
{
	m_subtractedShapes.clear();

	invalidate();
}
public void setHeight(double height)
{
	m_baseShape.setHeight(height);

	invalidate();
}
public void setWidth(double width)
{
	m_baseShape.setWidth(width);

	invalidate();
}
public void setX(double x)
{
	m_transform.setToTranslation(x, getY());

	Area tmp = m_area;
	invalidate();
	m_area = tmp;
}
public void setY(double y)
{
	m_transform.setToTranslation(getX(), y);

	Area tmp = m_area;
	invalidate();
	m_area = tmp;
}

public void translateBy(double x, double y)
{
	m_transform.translate(x, y);

	Area tmp = m_area;
	invalidate();
	m_area = tmp;
}

public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor )
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( false, "illegal call" );
}

// add a shape to subtracted shapes set

public boolean subtractSimpleShape( CoImmutableShapeIF shape )
{
	// copy and transform to coordinate space of base shape
	CoShapeIF s = shape.copyTranslatedBy( -getX(), -getY() );

	// ignore non-intersecting shapes
	Rectangle2D r = s.getShape().getBounds2D();
	if
		( m_baseShape.intersects( r.getX(), r.getY(), r.getWidth(), r.getHeight() ) )
	{
		s = s.createExpandedInstance( 0.001 );  // compensate for numerical bug in Area
		m_subtractedShapes.add( s );
		getArea().subtract( new Area( s.getShape() ) );
		return true;
	} else {
		return false;
	}
}
}