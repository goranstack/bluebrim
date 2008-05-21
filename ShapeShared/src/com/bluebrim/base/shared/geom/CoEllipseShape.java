package com.bluebrim.base.shared.geom;
import java.awt.*;
import java.awt.geom.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Ellipse shape
 * 
 * @author: Dennis Malmström
 */
 
public class CoEllipseShape extends CoBoundingShape implements CoEllipseShapeIF
{
/*
 * Used at XML import
 * Helena Rankegård 2001-10-29
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoEllipseShape( node, context );
}
	// geometry
	protected double m_x;
	protected double m_y;
	protected double m_width;
	protected double m_height;

	// awt shape representation
	transient protected Ellipse2D.Double m_ellipse;
	// xml tag constants
	public static final String XML_TAG = "ellipse";

public CoEllipseShape()
{
}


public CoEllipseShape(double x, double y, double width, double height)
{
	this();

	m_x = x;
	m_y = y;
	m_width = width;
	m_height = height;
}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-29
 */
 
protected CoEllipseShape( Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
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
		CoEllipseShape e = (CoEllipseShape) clone();
		e.m_ellipse = null;
		return e;
	}
	catch ( CloneNotSupportedException e )
	{
		throw new InternalError();
	}
}


public CoShapeIF deepClone()
{
	CoEllipseShape s = (CoEllipseShape) super.deepClone();
	
	s.m_ellipse = null;

	return s;
}


protected CoShapeIF doCreateExpandedInstance( double delta )
{
	return new CoEllipseShape( getX() - delta,
		                         getY() - delta,
		                         getWidth() + 2 * delta,
		                         getHeight() + 2 * delta );
}


public boolean equals( Object s )
{
	if ( s == this ) return true;

	if ( ! ( s instanceof CoEllipseShape ) ) return false;
	
	CoEllipseShape e = (CoEllipseShape) s;
	return
		(
			( m_x == e.m_x )
		&&
			( m_y == e.m_y )
		&&
			( m_width == e.m_width )
		&&
			( m_height == e.m_height )
		);
}


public String getFactoryKey()
{
	return CoEllipseShapeIF.ELLIPSE_SHAPE;
}


public double getHeight()
{
	return m_height;
}


public Shape getShape()
{
	if
		( m_ellipse == null )
	{
		m_ellipse = new Ellipse2D.Double( Math.min( m_x, m_x + m_width ),
			                                Math.min( m_y, m_y + m_height ),
			                                Math.abs( m_width ),
			                                Math.abs( m_height ) );
	}

	return m_ellipse;
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
	
	m_ellipse = null;
}


public boolean isInside( Point2D m, double strokeWidth, double outsideStrokeWidth )
{
	// check bounds first
	if ( super.isOutside( m, strokeWidth, outsideStrokeWidth ) ) return false;
	
	double a = m_width / 2;
	double b = m_height / 2;
	
	double x = m.getX() - a;
	double y = m.getY() - b;

	a += outsideStrokeWidth;
	b += outsideStrokeWidth;

	double d = ( x * x ) / ( a * a ) + ( y * y ) / ( b * b );

	return d <= 1;
}


// isInside is accurate -> isOutside is complement

public boolean isOutside( Point2D m, double strokeWidth, double outsideStrokeWidth )
{
	return ! isInside( m, strokeWidth, outsideStrokeWidth );
}


public void normalize()
{
	m_x = Math.min( m_x, m_x + m_width );
	m_y = Math.min( m_y, m_y + m_height );
	m_width = Math.abs( m_width );
	m_height = Math.abs( m_height );
	invalidate();
}


public void setHeight(double height)
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
	return "CoEllipseShape: " + m_x + " " + m_y + "   " + m_width + " " + m_height;
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