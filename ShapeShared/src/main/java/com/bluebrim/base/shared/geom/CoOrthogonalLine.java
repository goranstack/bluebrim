package com.bluebrim.base.shared.geom;
import java.awt.geom.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;

/**
 * Implementation of orthogonal line shape (a linw where ( ( x1 == x2 ) || ( y1 == y2 ) ) )
 * 
 * @author: Dennis Malmström
 */
  
public class CoOrthogonalLine extends CoLine
{
/*
 * Used at XML import
 * Helena Rankegård 2001-10-29
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoOrthogonalLine( node, context );
}
	// xml tag constants
	public static final String XML_TAG = "orthogonalLine";

public CoOrthogonalLine()
{
}


public CoOrthogonalLine( double x1, double y1, double x2, double y2 )
{
	super( x1, y1, x2, y2 );
}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-29
 */
 
protected CoOrthogonalLine( Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	super( node, context );

	// xml init
	//NamedNodeMap map = node.getAttributes();
}


public CoShape createNewInstanceFrom( CoBoundingShape shape )
{
	CoLine l = (CoOrthogonalLine) createNewInstance();
	
	if
		( shape.getHeight() > shape.getWidth() )
	{
		l.setGeometry(shape.getX() + (shape.getWidth() / 2), shape.getY(), shape.getX() + (shape.getWidth() / 2), shape.getY() + shape.getHeight());
	} else {
		l.setGeometry(shape.getX(), shape.getY() + (shape.getHeight() / 2), shape.getX() + shape.getWidth(), shape.getY() + (shape.getHeight() / 2));
	}
	
	return l;
}


protected CoReshapeHandleIF[] createReshapeHandles()
{
	return new CoReshapeHandleIF[]
	{
		new CoReshapeHandleIF()
		{
			public final double getX() { return getX1(); }
			public final double getY() { return getY1(); }
			public final int getEdgeMask() { return CoGeometryConstants.ALL_EDGE_MASK; }
			public final void move( double dx, double dy )
			{
				// apply translation to first endpoint (must apply for both dimensions in same call)
				setGeometry( getX1() + dx, getY1() + dy, getX2(), getY2() );
			}
		},
		new CoReshapeHandleIF()
		{
			public final double getX() { return getX2(); }
			public final double getY() { return getY2(); }
			public final int getEdgeMask() { return CoGeometryConstants.ALL_EDGE_MASK; }
			public final void move( double dx, double dy )
			{
				// apply translation to last endpoint (must apply for both dimensions in same call)
				setGeometry( getX1(), getY1(), getX2() + dx, getY2() + dy );
			}
		}
	};

}


public String getFactoryKey()
{
	return ORTHOGONAL_LINE;
}


public String getXmlTag()
{
	return XML_TAG;
}


// project to whichever dimension is "closest"

public void setGeometry( double x1, double y1, double x2, double y2 )
{
	Point2D fixedPoint;
	Point2D newPoint;
	
	double X1	= getX1();
	double Y1	= getY1();
	double X2	= getX2();
	double Y2	= getY2();
	
	boolean firstPointFixed = ! closeEnough( x2, y2, X2, Y2 );
	if
		( ! firstPointFixed )
	{
		// move last endpoint
		fixedPoint = new Point2D.Double( X2, Y2 );
		newPoint = new Point2D.Double( x1, y1 );
	} else {		
		// move first endpoint
		fixedPoint = new Point2D.Double( X1, Y1 );
		newPoint = new Point2D.Double( x2, y2 );
	}
	
	// project
	if
		( Math.abs( newPoint.getX() - fixedPoint.getX() ) >= Math.abs( newPoint.getY() - fixedPoint.getY() ) )
	{
		newPoint.setLocation( newPoint.getX(), fixedPoint.getY() );
	} else {
		newPoint.setLocation( fixedPoint.getX(), newPoint.getY() );
	}

	if
		( firstPointFixed )
	{
		m_x1 = fixedPoint.getX();
		m_y1 = fixedPoint.getY();
		m_x2 = newPoint.getX();
		m_y2 = newPoint.getY();
	} else {
		m_x1 = newPoint.getX();
		m_y1 = newPoint.getY();
		m_x2 = fixedPoint.getX();
		m_y2 = fixedPoint.getY();
	}

	invalidate();
}


public void setX1(double x)
{
	setGeometry( x, getY1(), getX2(), getY2() );
}


public void setX2(double x)
{
	setGeometry( getX1(), getY1(), x, getY2() );
}


public void setY1(double y)
{
	setGeometry( getX1(), y, getX2(), getY2() );
}


public void setY2(double y)
{
	setGeometry( getX1(), getY1(), getX2(), y );
}
}