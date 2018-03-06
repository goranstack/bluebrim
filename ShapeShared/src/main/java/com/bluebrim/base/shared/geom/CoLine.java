package com.bluebrim.base.shared.geom;
import java.awt.*;
import java.awt.geom.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Implementation of line shape
 * 
 * @author: Dennis Malmström
 */
 
public class CoLine extends CoShape implements CoLineIF
{
/*
 * Used at XML import
 * Helena Rankegård 2001-10-29
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoLine( node, context );
}
	// geometry
	protected double m_x1;
	protected double m_y1;
	protected double m_x2;
	protected double m_y2;

	// awt shape representation
	transient protected Line2D m_line;

	// mutable proxy	
	protected class MutableProxy extends CoShape.MutableProxy implements CoRemoteLineIF
	{
		// non mutating methods, delegate to outer instance
		public double getX1(){ return CoLine.this.getX1(); }
		public double getY1(){ return CoLine.this.getY1(); }
		public double getX2(){ return CoLine.this.getX2(); }
		public double getY2(){ return CoLine.this.getY2(); }
	  		
		// mutating methods, delegate to outer instance and nitofy owner
		public void setX1(double val)
		{
				if ( val == CoLine.this.getX1() ) return;
	    	CoLine.this.setX1(val); 
	    	notifyOwner( CoShapeIF.Owner.X | CoShapeIF.Owner.WIDTH );
		}

		public void setY1(double val)
		{
				if ( val == CoLine.this.getY1() ) return;
	    	CoLine.this.setY1(val); 
	    	notifyOwner( CoShapeIF.Owner.Y | CoShapeIF.Owner.HEIGHT );
		}

		public void setX2(double val)
		{
				if ( val == CoLine.this.getX2() ) return;
	    	CoLine.this.setX2(val); 
	    	notifyOwner( CoShapeIF.Owner.X | CoShapeIF.Owner.WIDTH );
		}
	
		public void setY2(double val)
		{
				if ( val == CoLine.this.getY2() ) return;
	    	CoLine.this.setY2(val); 
	    	notifyOwner( CoShapeIF.Owner.Y | CoShapeIF.Owner.HEIGHT );
		}

		public void setGeometry ( double x1, double y1, double x2, double y2)
		{
			CoLine.this.setGeometry(x1,y1,x2,y2);
	    notifyOwner( CoShapeIF.Owner.X | CoShapeIF.Owner.Y | CoShapeIF.Owner.WIDTH | CoShapeIF.Owner.HEIGHT );
		}
	}
		
	// xml tag constants
	public static final String XML_TAG = "line";
	public static final String XML_X1 = "x1";
	public static final String XML_X2 = "x2";
	public static final String XML_Y1 = "y1";
	public static final String XML_Y2 = "y2";

public CoLine()
{
	this( 0, 0, 0, 0 );
}


public CoLine(double x1, double y1, double x2, double y2)
{
	setGeometry(x1, y1, x2, y2);
}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-29
 */
 
protected CoLine( Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	super();

	// xml init
	NamedNodeMap map = node.getAttributes();
	
	m_x1 = CoModelBuilder.getDoubleAttrVal( map, XML_X1, m_x1 );
	m_y1 = CoModelBuilder.getDoubleAttrVal( map, XML_Y1, m_y1 );
	m_x2 = CoModelBuilder.getDoubleAttrVal( map, XML_X2, m_x2 );
	m_y2 = CoModelBuilder.getDoubleAttrVal( map, XML_Y2, m_y2 );
}


protected CoShape copyForTranslation()
{
	try
	{
		CoLine l = (CoLine) clone();
		l.m_line = null;
		return l;
	}
	catch ( CloneNotSupportedException e )
	{
		throw new InternalError();
	}
}


protected CoShape.MutableProxy createMutableProxy()
{
	return new MutableProxy();	
}


// top-left corner to bottom-right corner

public CoShapeIF createNewInstanceFrom( CoImmutableShapeIF aShape )
{
	CoLine S = (CoLine) createNewInstance();
	
	S.setX1(aShape.getX());
	S.setY1(aShape.getY());
	S.setX2(aShape.getX() + aShape.getWidth());
	S.setY2(aShape.getY() + aShape.getHeight());
	
	return S;
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
				// apply translation to first endpoint
				setX1( getX1() + dx );
				setY1( getY1() + dy );
			}
		},
		new CoReshapeHandleIF()
		{
			public final double getX() { return getX2(); }
			public final double getY() { return getY2(); }
			public final int getEdgeMask() { return CoGeometryConstants.ALL_EDGE_MASK; }
			public final void move( double dx, double dy )
			{
				// apply translation to last endpoint
				setX2( getX2() + dx );
				setY2( getY2() + dy );
			}
		}
	};

}


public CoShapeIF deepClone()
{	
	CoLine l = (CoLine) super.deepClone();
	l.m_line = null;
	return l;
}


// for a line: expansion = translation along normal vector

protected CoShapeIF doCreateExpandedInstance( double delta )
{
	// calculate normal vector
	double dy = getX1() - getX2();
	double dx = getY2() - getY1();
	double d = Math.sqrt( dy * dy + dx * dx ) / delta;
	dx /= d;
	dy /= d;
	
	return new CoLine( getX1() + dx, getY1() + dy, getX2() + dx, getY2() + dy );
}


public boolean equals( Object s )
{
	if ( s == this ) return true;

	if ( ! getClass().isInstance( s ) ) return false;
	
	CoLine l = (CoLine) s;

	return
		(
			( m_x1 == l.m_x1 )
		&&
			( m_y1 == l.m_y1 )
		&&
			( m_x2 == l.m_x2 )
		&&
			( m_y2 == l.m_y2 )
		);
}


public String getFactoryKey()
{
	return CoLineIF.LINE;
}


public double getHeight()
{
	return getY2() - getY1();
}


public Shape getShape()
{
	if
		( m_line == null )
	{
		// update awt shape representation
		if
			(
				( m_x2 < m_x1 )
			||
				(
					( m_x2 == m_x1 )
				&&
					( m_y2 < m_y1 )
				)
			)
		{
			m_line = new Line2D.Double( m_x2, m_y2, m_x1, m_y1 );
		} else {
			m_line = new Line2D.Double( m_x1, m_y1, m_x2, m_y2 );
		}
	}

	return m_line;
}


public double getWidth()
{
	return getX2() - getX1();
}


public double getX ( ) {
	return getX1();
}


public double getX1()
{
	return m_x1;
}


public double getX2()
{
	return m_x2;
}


public String getXmlTag()
{
	return XML_TAG;
}


public double getY ( ) {
	return getY1();
}


public double getY1()
{
	return m_y1;
}


public double getY2()
{
	return m_y2;
}


protected void invalidate()
{
	super.invalidate();
	
	m_line = null;
}


// lines are never closed

public boolean isClosedShape()
{
	return false;
}


// inside line <-> on stroke

public boolean isInside( Point2D m, double strokeWidth, double outsideStrokeWidth )
{
	double dx = m_x2 - m_x1;
	double dy = m_y2 - m_y1;
	double s = Math.sqrt( dx * dx + dy * dy );
	dx /= s;
	dy /= s;

	double d = outsideStrokeWidth - strokeWidth / 2;

	double x = m.getX() - m_x1 - d * dy;
	double y = m.getY() - m_y1 + d * dx;

	d = ( x * dx + y * dy ) / s;
	if ( d < 0 ) return false;
	if ( d > 1 ) return false;

	double S = ( x - m_x1 ) * dx + ( y - m_y1 ) * dy;
	double X = x - m_x1 - dx * S;
	double Y = y - m_y1 - dy * S;

	d = X * X + Y * Y;

	double D = Math.max( 4, strokeWidth / 2 );
	return ( d < D * D );
}


// isInside is accurate -> isOutside is complement

public boolean isOutside( Point2D m, double strokeWidth, double outsideStrokeWidth )
{
	return ! isInside( m, strokeWidth, outsideStrokeWidth );
}


// lines are by definition always normalized

public void normalize()
{
}


public void setGeometry(double x1, double y1, double x2, double y2)
{
	m_x1 = x1;
	m_y1 = y1;
	m_x2 = x2;
	m_y2 = y2;

	invalidate();
}


public void setHeight (double height ) {
	setY2(getY1() + height);
}


public void setTranslation (double x, double y ) {
	translateBy(x-getX(), y-getY());
}


public void setWidth (double width ) {
	setX2(getX1() + width);
}


public void setX(double x)
{
	setX1(x);
}


public void setX1(double x)
{
	m_x1 = x;

	invalidate();
}


public void setX2(double x)
{
	m_x2 = x;

	invalidate();
}


public void setY (double y ) {
	setY1(y);
}


public void setY1(double y)
{
	m_y1 = y;

	invalidate();
}


public void setY2(double y)
{
	m_y2 = y;

	invalidate();
}


public String toString()
{
	return "CoLine: " + m_x1 + " " + m_y1 + "   " + m_x2 + " " + m_y2;
}


public void translateBy(double dx, double dy)
{
	m_x1 += dx;
	m_y1 += dy;
	m_x2 += dx;
	m_y2 += dy;

	invalidate();
}


/*
 * Used at XML export
 * Helena Rankegård 2001-10-29
 */
 
public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor )
{
	super.xmlVisit( visitor );
	
	visitor.exportAttribute( XML_X1, Double.toString( m_x1 ) );
	visitor.exportAttribute( XML_Y1, Double.toString( m_y1 ) );
	visitor.exportAttribute( XML_X2, Double.toString( m_x2 ) );
	visitor.exportAttribute( XML_Y2, Double.toString( m_y2 ) );
}
}