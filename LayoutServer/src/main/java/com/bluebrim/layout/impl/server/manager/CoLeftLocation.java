package com.bluebrim.layout.impl.server.manager;
import org.w3c.dom.Node;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.CoLeftLocationIF;
import com.bluebrim.layout.impl.shared.CoLocationSpecIF;
import com.bluebrim.layout.impl.shared.CoPageItemStringResources;
import com.bluebrim.layout.shared.*;

//

public class CoLeftLocation extends CoLocationSpec implements CoLeftLocationIF
{
	public static final String XML_TAG = "left-location";
/*
 * Used at XML import
 * Helena Rankegård 2001-10-25
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoLeftLocation();
}

CoLeftLocation()
{
}


public boolean equals( Object o )
{
	return ( this == o ) || ( o instanceof CoLeftLocation ) || super.equals( o );
}


public String getFactoryKey()
{
	return LEFT_LOCATION;
}


public double getHeightAfterLocation( CoLayoutableIF layoutable )
{
	return getTopBottomHeightAfterLocation(  layoutable );
}


public  String getIconName ( ) {
	
	return "CoLeftLocation.gif";
}


public static CoLeftLocation getInstance()
{
	return (CoLeftLocation) getFactory().getLeftLocation();
}


public CoLocationSpecIF getLocationSpecAfterReshape( boolean dx0, boolean dy0, boolean dx1, boolean dy1 )
{
	if
		( dx0 )
	{
		return getFactory().getNoLocation();
	} else {
		return null;
	}
}


public String getType()
{
	return CoPageItemStringResources.getName(LEFT_LOCATION);
}


public double getWidthAfterLocation( CoLayoutableIF layoutable )
{
	return getRightWidthAfterLocation( layoutable );
}


public String getXmlTag()
{
	return XML_TAG;
}


/**
 * Placerar sidelementet mitt på sidan intill den vänstra marginalen.
 *
 */
public void placeLayoutable( CoLayoutableIF layoutable )
{
	if
		( hasValidParent( layoutable, true ) )
	{
		CoLayoutableContainerIF parent = layoutable.getLayoutParent();
	
		double x = parent.getColumnGrid().getLeftMarginPosition();	
		double y = (parent.getLayoutHeight() - layoutable.getLayoutHeight()) / 2;
	//	tCenterLocation.setLocation(tX, tY);
		layoutable.setLayoutLocation( x, y );
	}
	
	layoutable.setLayoutSuccess( true );
}


public boolean usesLeftSpace ( CoLayoutableContainerIF layoutArea ) {
	return true; 
}
}