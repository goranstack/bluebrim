package com.bluebrim.base.shared.geom;
import java.awt.*;
import java.awt.geom.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;

/**
 * Rectangular shape with rounded corners
 * 
 * @author: Dennis Malmström
 */
 
public class CoRoundedCorner extends CoCornerRectangle implements CoRoundedCornerIF, CoGeometryConstants
{
/*
 * Used at XML import
 * Helena Rankegård 2001-10-29
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoRoundedCorner( node, context );
}

	// awt shape representation
	transient protected RoundRectangle2D.Double m_rectangle;
	// xml tag constants
	public static final String XML_TAG = "reoundedCorner";

public CoRoundedCorner()
{
	super();
}


public CoRoundedCorner(double r )
{
	super(r);
}


public CoRoundedCorner(double x, double y, double width, double height, double r )
{
	super(x, y, width, height, r);
}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-29
 */
 
protected CoRoundedCorner( Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	super( node, context );

	// xml init
	//NamedNodeMap map = node.getAttributes();
}


protected CoShape copyForTranslation()
{
	try
	{
		CoRoundedCorner rc = (CoRoundedCorner) clone();
		rc.m_rectangle = null;
		return rc;
	}
	catch (CloneNotSupportedException e)
	{
		throw new InternalError();
	}
}


public CoShapeIF deepClone()
{
	CoRoundedCorner c = (CoRoundedCorner) super.deepClone();
	c.m_rectangle = null;
	return c;
}


protected CoShapeIF doCreateExpandedInstance( double delta )
{
	return new CoRoundedCorner( getX() - delta,
		                          getY() - delta,
		                          getWidth() + 2 * delta,
		                          getHeight() + 2 * delta,
		                          getCornerRadius() + 1 * delta );
}


public String getFactoryKey()
{
	return CoRoundedCornerIF.ROUNDED_CORNER;
}


public Shape getShape()
{
	if
		( m_rectangle == null )
	{
		m_rectangle = new RoundRectangle2D.Double( Math.min( m_x, m_x + m_width ),
			                                         Math.min( m_y, m_y + m_height ),
			                                         Math.abs( m_width ),
			                                         Math.abs( m_height ),
			                                         Math.abs( m_cornerRadius ) * 2,
			                                         Math.abs( m_cornerRadius ) * 2 );
	}
	
	return m_rectangle;
}


public String getXmlTag()
{
	return XML_TAG;
}


protected void invalidate()
{
	super.invalidate();
	
	m_rectangle = null;
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
		if
			( y - m_y < m_height / 2 )
		{
			// upper left
			dx = m_x + m_cornerRadius - x;
			dy = m_y + m_cornerRadius - y;
		} else {
			// lower left
			dx = m_x + m_cornerRadius - x;
			dy = m_y + m_height - m_cornerRadius - y;
		}
	} else {
		// right half
		if
			( y - m_y < m_height / 2 )
		{
			// upper right
			dx = m_x + m_width - m_cornerRadius - x;
			dy = m_y + m_cornerRadius - y;
		} else {
			// lower right
			dx = m_x + m_width - m_cornerRadius - x;
			dy = m_y + m_height - m_cornerRadius - y;
		}
	}

	// compare distance to corner radius
	double D = m_cornerRadius + outsideStrokeWidth;
	return dx * dx + dy * dy <= D * D;
}


public String toString()
{
	return "CoRoundedCorner: " + m_x + " " + m_y + "   " + m_width + " " + m_height + " " + m_cornerRadius;
}
}