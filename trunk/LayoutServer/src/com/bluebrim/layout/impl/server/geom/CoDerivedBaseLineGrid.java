package com.bluebrim.layout.impl.server.geom;

/**
 * Baseline grid that is derived by intersecting another baseline grid.
 * 
 * @author: Dennis Malmström
 */

public class CoDerivedBaseLineGrid extends CoBaseLineGrid
{
	protected CoBaseLineGrid m_source; // the grid from which this grid is derived

public CoDerivedBaseLineGrid( CoBaseLineGrid source )
{
	m_source = source;
}


public boolean equals( Object g )
{
	if ( g == this ) return true;

	if ( ! ( g instanceof CoDerivedBaseLineGrid ) ) return false;

	CoDerivedBaseLineGrid s = (CoDerivedBaseLineGrid) g;
	
	return ( m_y == s.m_y ) && m_source.equals( s.m_source );
}


public com.bluebrim.text.shared.CoBaseLineGeometryIF getBaseLineGeometry( double y ) 
{
	return m_source.getBaseLineGeometry( y + m_y );
}


public double getDeltaY()
{
	return m_source.getDeltaY();
}


public double getY0()
{
	return m_source.getY0();
}


public double getY0Position()
{
	return m_source.getY0Position( m_y );
}


public boolean isDerived()
{
	return true;
}


public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor)
{
	throw new UnsupportedOperationException( getClass() + " is not XML exportable" );
//	visitor.exportAttribute( XML_TYPE, XML_IS_DERIVED );
}
}