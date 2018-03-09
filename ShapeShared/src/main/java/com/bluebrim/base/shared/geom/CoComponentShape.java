package com.bluebrim.base.shared.geom;

import java.awt.*;
import java.awt.geom.*;

import com.bluebrim.base.shared.*;

/**
 * Rectangle shape with dimensions that are derived from those of a java.awt.Component
 * 
 * @author: Dennis Malmström
 */
 

public class CoComponentShape extends CoBoundingShape implements CoGeometryConstants, CoRectangleShapeIF
{
	protected Component m_component;

	// geoemtry
	protected double m_inset;
	
	// awt shape representation
	transient protected Shape m_shape;
public CoComponentShape( Component c, double inset )
{
	m_component = c;
	m_inset = inset;
}
protected CoShape copyForTranslation()
{
	try
	{
		CoRectangleShape c = (CoRectangleShape) clone();
		c.m_shape = null;
		return c;
	}
	catch (CloneNotSupportedException e)
	{
		throw new InternalError();
	}
}
public CoShapeIF deepClone()
{
	CoRectangleShape s = (CoRectangleShape) super.deepClone();
	s.m_shape = null;
	return s;
}
protected CoShapeIF doCreateExpandedInstance( double delta )
{
	CoShapeIF s = new CoRectangleShape( getX() - delta,
		                                  getY() - delta,
		                                  getWidth() + 2 * delta,
		                                  getHeight() + 2 * delta );
	return s;
}
public boolean equals( Object s )
{
	if ( s == this ) return true;

	if ( ! getClass().isInstance( s ) ) return false;
	
	CoComponentShape S = (CoComponentShape) s;

	return
		(
			( m_inset == S.m_inset )
		&&
			( m_component == S.m_component )
		);
}
public String getFactoryKey ()
{
	return CoRectangleShapeIF.RECTANGLE_SHAPE;
}
public double getHeight()
{
	return m_component.getHeight() - 2 * m_inset;
}
public Shape getShape()
{
	boolean invalid = false;

	if
		( m_shape == null )
	{
		invalid = true;
	} else {
		// is awt representation valid ?
		Rectangle2D b = m_shape.getBounds2D();
		if
			(
			 ( b.getWidth() != getWidth() )
			||
			 ( b.getHeight() != getHeight() )
			)
		{
			invalid = true;
		}
	}
	
	if
		( invalid )
	{
		m_shape = new Rectangle2D.Double( getX(), getY(), getWidth(), getHeight() );
	}

	return m_shape;
}
public double getWidth()
{
	return m_component.getWidth() - 2 * m_inset;
}
public double getX()
{
	return m_inset;
}
public double getY()
{
	return m_inset;
}
// bounds = shape -> inherited isOutside is accurate

public boolean isInside( Point2D p, double strokeWidth, double outsideStrokeWidth )
{
	return ! isOutside( p, strokeWidth, outsideStrokeWidth );
}
// geometry is  immutable

public void normalize()
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( false, "Illegal call to " + getClass() + ".normalize" );
}
// geometry is  immutable

public void setHeight( double height )
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( false, "Illegal call to " + getClass() + ".setHeight" );
}
// geometry is  immutable

public void setWidth(double width)
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( false, "Illegal call to " + getClass() + ".setWidth" );
}
// geometry is  immutable

public void setX(double x)
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( false, "Illegal call to " + getClass() + ".setX" );
}
// geometry is  immutable

public void setY(double y)
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( false, "Illegal call to " + getClass() + ".setY" );
}

public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor )
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( false, "illegal call" );
}
}