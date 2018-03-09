package com.bluebrim.layout.impl.server.manager;
import org.w3c.dom.Node;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.CoLocationSpecIF;
import com.bluebrim.layout.impl.shared.CoPageItemStringResources;
import com.bluebrim.layout.impl.shared.CoRightLocationIF;
import com.bluebrim.layout.shared.*;

/**
 * Specificerar att ett sidelement skall placeras 
 * så långt åt höger som möjligt.
 */
 
public class CoRightLocation extends CoLocationSpec implements CoRightLocationIF
{
	public static final String XML_TAG = "right-location";
/*
 * Used at XML import
 * Helena Rankegård 2001-10-25
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoRightLocation();
}

CoRightLocation()
{
}


public boolean equals( Object o )
{
	return ( this == o ) || ( o instanceof CoRightLocation ) || super.equals( o );
}


public String getFactoryKey()
{
	return RIGHT_LOCATION;
}


public double getHeightAfterLocation( CoLayoutableIF layoutable )
{
	return getTopBottomHeightAfterLocation(  layoutable );
}


public  String getIconName ( ) {
	
	return "CoRightLocation.gif";
}


public static CoRightLocation getInstance()
{
	return (CoRightLocation) getFactory().getRightLocation();
}


public CoLocationSpecIF getLocationSpecAfterReshape( boolean dx0, boolean dy0, boolean dx1, boolean dy1 )
{
	if
		( dx1 )
	{
		return getFactory().getNoLocation();
	} else {
		return null;
	}
}


public String getType()
{
	return CoPageItemStringResources.getName(RIGHT_LOCATION);
}


public double getWidthAfterLocation( CoLayoutableIF layoutable )
{
	return getLeftWidthAfterLocation( layoutable );
}


public String getXmlTag()
{
	return XML_TAG;
}


/**
 * Placerar sidelementet mitt på sidan intill den högra marginalen.
 *
 */
public void placeLayoutable ( CoLayoutableIF layoutable )
{
	if
		( hasValidParent( layoutable, true ) )
	{
		CoLayoutableContainerIF parent = layoutable.getLayoutParent();
	
		double x = parent.getColumnGrid().getRightMarginPosition() - layoutable.getLayoutWidth();	
		double y = ( parent.getLayoutHeight() - layoutable.getLayoutHeight() ) / 2;
	//	tCenterLocation.setLocation(tX, tY);
		layoutable.setLayoutLocation( x, y);
	}
	
	layoutable.setLayoutSuccess( true );
}


public boolean usesRightSpace ( CoLayoutableContainerIF layoutArea ) {
	return true; 
}
}