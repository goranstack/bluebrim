package com.bluebrim.layout.impl.server.geom;
import org.w3c.dom.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Implementation of baseline grid.
 * 
 * @author: Dennis Malmström
 */

public class CoRegularBaseLineGrid extends CoBaseLineGrid implements CoBaseLineGridIF
{
	// specified geometry
	protected double m_y0;
	protected double m_dy;
	// xml tag constants
	//public static final String XML_BASELINE_GRID = "baselineGrid";
	public static final String XML_TAG = "regular-base-line-grid";
	public static final String XML_DY = "dy";
	public static final String XML_Y0 = "y0";
/*
 * Used at XML import
 * Helena Rankegård 2001-10-29
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoRegularBaseLineGrid(node, context);
}


/**
 * Co8ColumnGrid constructor comment.
 */
 
public CoRegularBaseLineGrid( double y0, double dy )
{
	set( y0, dy );
}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-29
 */
 
protected CoRegularBaseLineGrid(Node node, com.bluebrim.xml.shared.CoXmlContext context) 
{
	super();

	// xml init
	NamedNodeMap map = node.getAttributes();
	
	m_y0 = CoModelBuilder.getDoubleAttrVal( map, XML_Y0, m_y0 );
	m_dy = CoModelBuilder.getDoubleAttrVal( map, XML_DY, m_dy );
}


public boolean equals( Object g )
{
	if ( g == this ) return true;

	if ( ! ( g instanceof CoRegularBaseLineGrid ) ) return false;

	CoRegularBaseLineGrid s = (CoRegularBaseLineGrid) g;
	
	return
		( m_y == s.m_y )
	&&
		( m_y0 == s.m_y0 )
	&&
		( m_dy == s.m_dy );
}


public com.bluebrim.text.shared.CoBaseLineGeometryIF getBaseLineGeometry( double y ) 
{
	class CoBaseLineGeometry implements com.bluebrim.text.shared.CoBaseLineGeometryIF
	{
		private float m_y0;
		private float m_dy;

		public CoBaseLineGeometry( float y0, float dy )
		{
			m_y0 = y0;
			m_dy = dy;
		}

		public float getY0() { return m_y0; }
		public float getDeltaY() { return m_dy; }

		public boolean isEquivalentTo( com.bluebrim.text.shared.CoBaseLineGeometryIF g )
		{
			if ( ( m_dy == 0 ) && ( g.getDeltaY() == 0 ) ) return true;
			return ( g.getY0() == m_y0 ) && ( g.getDeltaY() == m_dy );
		}
	};

	double y0 = getY0Position( y );
	double dy = getDeltaY();
	
	return new CoBaseLineGeometry( (float) y0, (float) dy );
}


public double getDeltaY()
{
	return m_dy;
}


public double getY0()
{
	return m_y0;
}


public double getY0Position()
{
	return m_y0;
}


public void set( double y0, double dy )
{
	setY0( y0 );
	setDeltaY( dy );
}


public void setDeltaY( double dy )
{
	m_dy = dy;
	markDirty();
}


public void setY0( double y0 )
{
	m_y0 = y0;
	markDirty();
}


/*
 * Used at XML export
 * Helena Rankegård 2001-10-29
 */
 
public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor)
{
	super.xmlVisit( visitor );
	
	//visitor.exportAttribute( XML_TYPE, XML_BASELINE_GRID );
	visitor.exportAttribute( XML_Y0, Double.toString( m_y0 ) );
	visitor.exportAttribute( XML_DY, Double.toString( m_dy ) );
}
}