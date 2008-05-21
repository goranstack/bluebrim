package com.bluebrim.base.shared.geom;
import java.awt.geom.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Abstract superclass for rectangular shapes with shaped corners
 * 
 * @author: Dennis Malmström
 */
 
public abstract class CoCornerRectangle extends CoBoundingShape implements CoCornerRectangleIF
{

	// geometry
	protected double m_x;
	protected double m_y;
	protected double m_width;
	protected double m_height;
	protected double m_cornerRadius;

	// Mutable proxy
	protected class MutableProxy extends CoShape.MutableProxy implements CoRemoteCornerRectangleIF
	{
		// non mutating methods, delegate to outer instance
		public double getCornerRadius(){ return CoCornerRectangle.this.getCornerRadius(); }

		// mutating methods, delegate to outer instance and nitofy owner
		public void setCornerRadius(double r)
		{
	  	if ( r == CoCornerRectangle.this.getCornerRadius() ) return;
			CoCornerRectangle.this.setCornerRadius(r); 
			notifyOwner( 0 );
		}
	}
	// xml tag constants
	public static final String XML_CORNER_RADIUS = "cornerRadius";

public CoCornerRectangle()
{
}


public CoCornerRectangle(double r)
{
	m_cornerRadius = r;
}


public CoCornerRectangle(double x, double y, double width, double height, double r )
{
	m_x = x;
	m_y = y;
	m_width = width;
	m_height = height;
	m_cornerRadius = r;
}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-29
 */
 
protected CoCornerRectangle( Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	super();

	// xml init
	NamedNodeMap map = node.getAttributes();
	
	m_x = CoModelBuilder.getDoubleAttrVal( map, XML_X, m_x );
	m_y = CoModelBuilder.getDoubleAttrVal( map, XML_Y, m_y );
	m_width = CoModelBuilder.getDoubleAttrVal( map, XML_WIDTH, m_width );
	m_height = CoModelBuilder.getDoubleAttrVal( map, XML_HEIGHT, m_height );
	m_cornerRadius = CoModelBuilder.getDoubleAttrVal( map, XML_CORNER_RADIUS, m_cornerRadius );
}


protected CoShape.MutableProxy createMutableProxy()
{
	return new MutableProxy();	
}


public CoShapeIF createNewInstanceFrom( CoImmutableShapeIF s )
{
	CoCornerRectangle S = (CoCornerRectangle) super.createNewInstanceFrom( s );

	if 
		(s instanceof CoCornerRectangle)
	{
		S.setCornerRadius(((CoCornerRectangleIF)s).getCornerRadius());
	}	else {
		double r = Math.min( s.getWidth(), s.getHeight() ) / 4;
		S.setCornerRadius( r );
	}
	return S;
}


// add a corner radius handle

protected CoReshapeHandleIF[] createReshapeHandles()
{
	CoReshapeHandleIF[] tmp = super.createReshapeHandles();
	
	CoReshapeHandleIF[] tmp2 = new CoReshapeHandleIF [ tmp.length + 1 ];
	
	tmp2[ 0 ] = new CoReshapeHandleIF() // radius
		{
			public final double getX() { return CoCornerRectangle.this.getX() + getCornerRadius(); }
			public final double getY() { return CoCornerRectangle.this.getY(); }
			public final int getEdgeMask() { return NO_EDGE_MASK; }
			public final void move( double dx, double dy )
			{
				setCornerRadius( getCornerRadius() + dx );
			}
		};

	for ( int i = 0; i < tmp.length; i++ ) tmp2[ i + 1 ] = tmp[ i ];

	return tmp2;
}


public boolean equals( Object s )
{
	if ( s == this ) return true;

	if ( ! getClass().isInstance( s ) ) return false;
	
	CoCornerRectangle r = (CoCornerRectangle) s;
	
	return
		(
			( m_x == r.m_x )
		&&
			( m_y == r.m_y )
		&&
			( m_width == r.m_width )
		&&
			( m_height == r.m_height )
		&&
			( m_cornerRadius == r.m_cornerRadius )
		);
}


public double getCornerRadius()
{
	return m_cornerRadius;
}


public double getHeight()
{
	return m_height;
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


public boolean isInside( Point2D p, double strokeWidth, double outsideStrokeWidth )
{
	// first check bounds
	if ( super.isOutside( p, strokeWidth, outsideStrokeWidth ) ) return false;

	double x = p.getX();
	double y = p.getY();

	if
		(
			( x >= m_x + m_cornerRadius )
		&&
			( y >= m_y - outsideStrokeWidth )
		&&
			( x <= m_x + m_width - m_cornerRadius )
		&&
			( y <= m_y + m_height + outsideStrokeWidth )
		)
	{
		return true; // inside shape and outside corners -> hit
	}

	if
		(
			( x >= m_x - outsideStrokeWidth )
		&&
			( y >= m_y + m_cornerRadius )
		&&
			( x <= m_x + m_width + outsideStrokeWidth )
		&&
			( y <= m_y + m_height - m_cornerRadius )
		)
	{
		return true; // inside shape and outside corners -> hit
	}

	// check corners
	return isOnCorner( x, y, outsideStrokeWidth );
}


// hit test corners, must be accurate

protected abstract boolean isOnCorner( double x, double y, double outsideStrokeWidth );


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

	m_cornerRadius = Math.abs( m_cornerRadius );

	invalidate();
}


public void setCornerRadius(double r)
{
	m_cornerRadius = r;
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
	return super.toString() + " " + getCornerRadius();
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
	visitor.exportAttribute( XML_CORNER_RADIUS, Double.toString( m_cornerRadius ) );
}
}