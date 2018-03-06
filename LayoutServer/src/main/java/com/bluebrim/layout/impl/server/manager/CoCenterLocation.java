package com.bluebrim.layout.impl.server.manager;
import org.w3c.dom.Node;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.CoCenterLocationIF;
import com.bluebrim.layout.impl.shared.CoLocationSpecIF;
import com.bluebrim.layout.impl.shared.CoPageItemStringResources;
import com.bluebrim.layout.shared.*;

//

public class CoCenterLocation extends CoLocationSpec implements CoCenterLocationIF
{
	public static final String XML_TAG = "center-location";

/*
 * Used at XML import
 * Helena Rankegård 2001-10-25
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoCenterLocation();
}


CoCenterLocation()
{
}


public boolean equals( Object o )
{
	return ( this == o ) || ( o instanceof CoCenterLocation ) || super.equals( o );
}


public  String getFactoryKey ( ) {
	
	return CENTER_LOCATION;
}


public double getHeightAfterLocation( CoLayoutableIF layoutable )
{
	return getTopBottomHeightAfterLocation(  layoutable );
}


public  String getIconName ( ) {
	
	return "CoCenterLocation.gif";
}


public static CoCenterLocation getInstance()
{
	return (CoCenterLocation) getFactory().getCenterLocation();
}


public CoLocationSpecIF getLocationSpecAfterReshape( boolean dx0, boolean dy0, boolean dx1, boolean dy1 )
{
	if
		( dx0 || dx1 || dy0 || dy1 )
	{
		return getFactory().getNoLocation();
	} else {
		return null;
	}
}


public  String getType ( ) {
	
	return CoPageItemStringResources.getName( CENTER_LOCATION);
}


public double getWidthAfterLocation( CoLayoutableIF layoutable )
{
	return getLeftRightWidthAfterLocation( layoutable );
}


public String getXmlTag()
{
	return XML_TAG;
}


/**
 * Placerar sidelementet mitt i onLayoutArea.
 *
 */
public void placeLayoutable( CoLayoutableIF layoutable )
{
	if
		( hasValidParent( layoutable, true ) )
	{	
		CoLayoutableContainerIF parent = layoutable.getLayoutParent();

		double x = (parent.getLayoutWidth() - layoutable.getLayoutWidth()) / 2;
		double y = (parent.getLayoutHeight() - layoutable.getLayoutHeight()) / 2;	
	//	tCenterLocation.setLocation(tX, tY);
		layoutable.setLayoutLocation( x, y );
	}
	
	layoutable.setLayoutSuccess( true );
}
}