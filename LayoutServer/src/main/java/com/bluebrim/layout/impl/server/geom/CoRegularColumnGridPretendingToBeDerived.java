package com.bluebrim.layout.impl.server.geom;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Used by shape page items when derived grid is requested and no parent is present
 * Creation date: (2001-10-11 13:50:45)
 * @author: Dennis
 */

public class CoRegularColumnGridPretendingToBeDerived extends CoRegularColumnGrid
{
public CoRegularColumnGridPretendingToBeDerived()
{
	super( 1 );
	setBounds( 0, 0, CoShapePageItemIF.INFINITE_DIMENSION, CoShapePageItemIF.INFINITE_DIMENSION, true );
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