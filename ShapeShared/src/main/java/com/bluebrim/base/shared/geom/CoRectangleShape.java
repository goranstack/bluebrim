package com.bluebrim.base.shared.geom;
import java.awt.*;
import java.awt.geom.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Rectangle shape
 * 
 * @author: Dennis Malmström
 */

public class CoRectangleShape extends CoBoundingShape implements CoGeometryConstants, CoRectangleShapeIF
{
/*
 * Used at XML import
 * Helena Rankegård 2001-10-29
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoRectangleShape( node, context );
}
	// geometry
	protected double m_x;
	protected double m_y;
	protected double m_width;
	protected double m_height;
	
	// awt shape representation
	transient protected Shape m_shape;
	// xml tag constants
	public static final String XML_TAG = "rectangle";

public CoRectangleShape()
{
}


public CoRectangleShape(double x, double y, double width, double height)
{
	this();

	m_x = x;
	m_y = y;
	m_width = width;
	m_height = height;
}


public CoRectangleShape(RectangularShape rectangularShape)
{
	this( rectangularShape.getX(), 
		    rectangularShape.getY(), 
		    rectangularShape.getWidth(), 
		    rectangularShape.getHeight() );
}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-29
 */
 
protected CoRectangleShape( Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	super();

	// xml init
	NamedNodeMap map = node.getAttributes();
	
	m_x = CoModelBuilder.getDoubleAttrVal( map, XML_X, m_x );
	m_y = CoModelBuilder.getDoubleAttrVal( map, XML_Y, m_y );
	m_width = CoModelBuilder.getDoubleAttrVal( map, XML_WIDTH, m_width );
	m_height = CoModelBuilder.getDoubleAttrVal( map, XML_HEIGHT, m_height );
}


protected CoShape copyForTranslation()
{
	try
	{
		CoRectangleShape r = (CoRectangleShape) clone();
		r.m_shape = null;
		return r;
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
	CoShapeIF s = new CoRectangleShape( getX() - delta, getY() - delta, getWidth() + 2 * delta, getHeight() + 2 * delta );
	return s;
}


public boolean equals( Object s )
{
	if ( s == this ) return true;

	if ( ! ( s instanceof CoRectangleShape ) ) return false;
	
	CoRectangleShape r = (CoRectangleShape) s;
	return
		(
			( m_x == r.m_x )
		&&
			( m_y == r.m_y )
		&&
			( m_width == r.m_width )
		&&
			( m_height == r.m_height )
		);
}


public String getFactoryKey ()
{
	return CoRectangleShapeIF.RECTANGLE_SHAPE;
}


public double getHeight()
{
	return m_height;
}


public Shape getShape()
{
	if
		( m_shape == null )
	{
		m_shape = new Rectangle2D.Double( Math.min( m_x, m_x + m_width ),
			                                Math.min( m_y, m_y + m_height ),
			                                Math.abs( m_width ),
			                                Math.abs( m_height ) );
	}

	
	return m_shape;
}


public double getWidth()
{
	return m_width;
}


public double getX()
{
	return m_x;
}


public String getXmlTag()
{
	return XML_TAG;
}


public double getY()
{
	return m_y;
}


protected void invalidate()
{
	super.invalidate();
	
	m_shape = null;
}


// bounds = shape -> inherited isOutside is accurate

public boolean isInside( Point2D p, double strokeWidth, double outsideStrokeWidth )
{
	return ! isOutside( p, strokeWidth, outsideStrokeWidth );
}


public void normalize()
{
	m_x = Math.min( m_x, m_x + m_width );
	m_y = Math.min( m_y, m_y + m_height );
	m_width = Math.abs( m_width );
	m_height = Math.abs( m_height );

	invalidate();
}


public void setHeight( double height )
{
	m_height = height;

	invalidate();
}


public void setWidth(double width)
{
	m_width = width;

	invalidate();
}


public void setX(double x)
{
	m_x = x;

	invalidate();
}


public void setY(double y)
{
	m_y = y;

	invalidate();
}


public String toString()
{
	return "CoRectangleShape: " + m_x + " " + m_y + "   " + m_width + " " + m_height;
}


/*
 * Used at XML export
 * Helena Rankegård 2001-10-29
 */
 
public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor )
{
	super.xmlVisit( visitor );
	
	visitor.exportAttribute( XML_X, Double.toString( m_x ) );
	visitor.exportAttribute( XML_Y, Double.toString( m_y ) );
	visitor.exportAttribute( XML_WIDTH, Double.toString( m_width ) );
	visitor.exportAttribute( XML_HEIGHT, Double.toString( m_height ) );
}
}