package com.bluebrim.base.shared.geom;
import java.awt.*;
import java.awt.geom.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;

/**
 * Rectangular shape with beveled corners
 * 
 * @author: Dennis Malmström
 */

public class CoBeveledCorner extends CoCornerRectangle implements CoBeveledCornerIF, CoGeometryConstants
{
	// awt shape representation
	transient protected GeneralPath m_shape;
	// xml tag constants
	public static final String XML_TAG = "beveledCorner";
/*
 * Used at XML import
 * Helena Rankegård 2001-10-29
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoBeveledCorner( node, context );
}

public CoBeveledCorner()
{
	super();
}


public CoBeveledCorner(double r )
{
	super(r);
}


public CoBeveledCorner(double x, double y, double width, double height, double r )
{
	super(x, y, width, height, r);
}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-29
 */
 
protected CoBeveledCorner( Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	super( node, context );

	// xml init
	//NamedNodeMap map = node.getAttributes();
}


protected CoShape copyForTranslation()
{
	try
	{
		CoBeveledCorner c = (CoBeveledCorner) clone();
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
	CoBeveledCorner c = (CoBeveledCorner) super.deepClone();
	c.m_shape = null;
	return c;
}


protected CoShapeIF doCreateExpandedInstance( double delta )
{
	return new CoBeveledCorner( getX() - delta,
		                           getY() - delta,
		                           getWidth() + 2 * delta,
		                           getHeight() + 2 * delta,
		                           getCornerRadius() + delta - delta * Math.tan( Math.PI / 8 ) );
}


public String getFactoryKey()
{
	return CoBeveledCornerIF.BEVELED_CORNER;
}


public Shape getShape()
{
	if
		( m_shape == null )
	{
		m_shape = new java.awt.geom.GeneralPath();

		float width  = (float) m_width;
		float height = (float) m_height;
		float x1 = (float) m_x;
		float y1 = (float) m_y;

		if
			( width < 0 )
		{
			x1 += width;
			width = - width;
		}

		if
			( height < 0 )
		{
			y1 += height;
			height = - height;
		}
		
		float yCut	 = Math.min((float)m_cornerRadius, height/2);
		float xCut 	 = Math.min((float)m_cornerRadius, width/2);

		float x2 = x1 + width;
		float y2 = y1 + height;

		m_shape.moveTo(x1 + xCut, y1); 
		m_shape.lineTo( x1 + width - xCut, y1);
		m_shape.lineTo(x1 + width, y1 + yCut);
		m_shape.lineTo(x1 + width, y1 + height - yCut);
		m_shape.lineTo(x1 + width - xCut, y1 + height);
		m_shape.lineTo(x1 + xCut, y1 + height);
		m_shape.lineTo(x1 , y1 + height - yCut);
		m_shape.lineTo(x1 , y1 + yCut);
		m_shape.closePath();
	}
	
	return m_shape;
}


public String getXmlTag()
{
	return XML_TAG;
}


protected void invalidate()
{
	super.invalidate();
	
	m_shape = null;
}


// hit test rounded corners

protected boolean isOnCorner( double x, double y, double outsideStrokeWidth )
{
	double dx = 0;
	double dy = 0;
	
	// calculate distance to closest corner
	if
		( x - m_x < m_width / 2 )
	{
		// left half
		dx = x - m_x;
	} else {
		// right half
		dx = m_x + m_width - x;
	}
	
	if
		( y - m_y < m_height / 2 )
	{
		// upper
		dy = y - m_y;
	} else {
		// lower
		dy = m_y + m_height - y;
	}

	// compare distance to corner radius
	double d = m_cornerRadius - outsideStrokeWidth * ( 1 + Math.tan( Math.PI / 8 ) );
	
	return dx + dy >= d;
}


public String toString()
{
	return "CoBeveledCorner: " + m_x + " " + m_y + "   " + m_width + " " + m_height + " " + m_cornerRadius;
}
}