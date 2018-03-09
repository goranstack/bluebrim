package com.bluebrim.layout.impl.server.geom;

/**
 * Used by shape page items when derived grid is requested and no parent is present
 * Creation date: (2001-10-11 13:50:45)
 * @author: 
 */

public class CoRegularBaseLineGridPretendingToBeDerived extends CoRegularBaseLineGrid
{
public CoRegularBaseLineGridPretendingToBeDerived()
{
	super( 0, 20 );
}


public boolean isDerived()
{
	return true;
}


public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor)
{
	throw new UnsupportedOperationException( getClass() + " is not XML exportable" );
}
}