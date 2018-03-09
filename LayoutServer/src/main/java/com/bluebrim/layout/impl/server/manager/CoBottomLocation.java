package com.bluebrim.layout.impl.server.manager;
import org.w3c.dom.Node;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.CoBottomLocationIF;
import com.bluebrim.layout.impl.shared.CoLocationSpecIF;
import com.bluebrim.layout.impl.shared.CoPageItemStringResources;
import com.bluebrim.layout.shared.*;
import com.bluebrim.layout.shared.CoImmutableColumnGridIF;

//

public class CoBottomLocation extends CoLocationSpec implements CoBottomLocationIF
{
	public static final String XML_TAG = "bottom-location";
/*
 * Used at XML import
 * Helena Rankegård 2001-10-25
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoBottomLocation();
}


CoBottomLocation()
{
}


public boolean equals( Object o )
{
	return ( this == o ) || ( o instanceof CoBottomLocation ) || super.equals( o );
}


public  String getFactoryKey ( ) {
	
	return BOTTOM_LOCATION;
}


public double getHeightAfterLocation( CoLayoutableIF layoutable )
{
	return getTopHeightAfterLocation(  layoutable );
}


public  String getIconName ( ) {
	
	return "CoBottomLocation.gif";
}


public static CoBottomLocation getInstance()
{
	return (CoBottomLocation) getFactory().getBottomLocation();
}


public CoLocationSpecIF getLocationSpecAfterReshape( boolean dx0, boolean dy0, boolean dx1, boolean dy1 )
{
	if
		( dy1 )
	{
		return getFactory().getNoLocation();
	} else {
		return null;
	}
}


public  String getType ( ) {
	
	return CoPageItemStringResources.getName( BOTTOM_LOCATION);
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
 * Placerar sidelementet mitt på den nedre marginalen.
 *
 */
public void placeLayoutable( CoLayoutableIF layoutable )
{
	if
		( hasValidParent( layoutable, true ) )
	{
		CoLayoutableContainerIF parent = layoutable.getLayoutParent();

		CoImmutableColumnGridIF grid = parent.getColumnGrid();
		double maxY = grid.getBottomMarginPosition();
		
		double x = (parent.getLayoutWidth() - layoutable.getLayoutWidth()) / 2;
	//	double tY = layoutArea.getColumnGrid().getBottomMargin() - layoutable.getCoShape().getHeight();	
		double y = parent.getColumnGrid().getBottomMarginPosition() - layoutable.getLayoutHeight();
		y=Math.min(maxY,CoLayoutManagerUtilities.usedBottomSpaceAt(x,layoutable,false,false));
	//	tCenterLocation.setLocation( x, y );
		layoutable.setLayoutLocation( x, y-layoutable.getLayoutHeight() );
	}
	
	layoutable.setLayoutSuccess( true );
}


public boolean usesBottomSpace ( CoLayoutableContainerIF layoutArea ) {
	return true;
}
}