package com.bluebrim.base.shared.geom;

import java.awt.*;
import java.awt.geom.*;

import com.bluebrim.base.shared.*;

/**
 * Proxy for a shape with a transform applied to it
 * 
 * @author: Dennis Malmström
 */

public class CoTransformedShape extends CoShape
{
	private CoShape m_shape; // nominal shape
	private AffineTransform m_transform;

	private transient Shape m_cachedShape; // transformed shape
public CoTransformedShape(	CoShapeIF shape, AffineTransform transform )
{
	m_shape = (CoShape) shape;
	m_transform = transform;
}
protected CoShape copyForTranslation()
{
	return new CoTransformedShape( m_shape.copyForTranslation(), m_transform );
}
public CoShapeIF createNewInstanceFrom( CoImmutableShapeIF s )
{
	return new CoTransformedShape( m_shape.createNewInstanceFrom( s ), m_transform );
}
protected CoReshapeHandleIF[] createReshapeHandles()
{
	return new CoReshapeHandleIF[ 0 ];
}
protected CoShapeIF doCreateExpandedInstance( double delta )
{
	return new CoTransformedShape( m_shape.createExpandedInstance( delta ), m_transform );
}
public String getFactoryKey()
{
	return null;
}
public double getHeight()
{
	return m_shape.getHeight();
}
public Shape getShape()
{
	if
		( m_cachedShape == null )
	{
		m_cachedShape = m_transform.createTransformedShape( m_shape.getShape() );
	}
	
	return m_cachedShape;
}
public double getWidth()
{
	return m_shape.getWidth();
}
public double getX()
{
	return m_shape.getX();
}
public double getY()
{
	return m_shape.getY();
}
public boolean isClosedShape()
{
	return m_shape.isClosedShape();
}
public void normalize()
{
	m_shape.normalize();

	invalidate();
}
public void setHeight( double h )
{
	m_shape.setHeight( h );

	invalidate();
}
public void setWidth( double width )
{
	m_shape.setWidth( width );

	invalidate();
}
public void setX( double x )
{
	m_shape.setX( x );

	invalidate();
}
public void setY( double y )
{
	m_shape.setY( y );

	invalidate();
}

public boolean equals( Object o )
{
	if ( o == this ) return true;

	if ( ! ( o instanceof CoTransformedShape ) ) return false;
	
	CoTransformedShape s = (CoTransformedShape) o;

	return ( m_shape.equals( s.m_shape ) && m_transform.equals( s.m_transform ) );
}

public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor )
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( false, "illegal call" );
}
}