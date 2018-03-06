package com.bluebrim.layout.impl.server.manager;
import java.awt.geom.Point2D;

import org.w3c.dom.Node;

import com.bluebrim.base.shared.geom.CoGeometryConstants;
import com.bluebrim.layout.shared.*;

/**
 * Beskriver placering av sidelement mot hörnet men en liten bit ut
 * CoDistance m_xInset bekriver denna förflyttning i columner, m_yInset används inte.
 * PENDING: get*Locations and used*SpaceAt is copies from CoUtilities, should not be corner specific.
 * Ask for used space (bottom, left, right, top) insted of check if used.
 * Creation date: (2000-09-20 10:15:36)
 * @author: Arvid Berg
 */

public class CoTopRightLocationSpec extends CoCornerLocationSpec
{
	public static final String XML_TAG = "top-right-location";
/*
 * Used at XML import
 * Helena Rankegård 2001-10-25
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoTopRightLocationSpec( node, context );
}

/**
 * Insert the method's description here.
 * Creation date: (2000-09-20 15:41:07)
 */
public CoTopRightLocationSpec() {}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-25
 */
 
protected CoTopRightLocationSpec( Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	super( node, context );

	// xml init
}


/**
 * Placerar sidelementet så nära point som möjligt.
 *
 */
public void doPlaceTopRight(CoLayoutableIF layoutable, CoProfile profile)
{	
	Point2D ytop = layoutable.getLayoutParent().getColumnGrid().snap( 0, 0, Double.MAX_VALUE, CoGeometryConstants.TOP_EDGE_MASK, CoGeometryConstants.ALL_DIRECTIONS_MASK, false, null );
	Point2D best=getBestPoint( new Point2D.Double( Double.NaN, ytop.getY() ) ,profile);	
	
	if
		( best == null )
	{
//		layoutable.setTopLeft( new Point2D.Double( -10, -10 ) ); // provisorium, ska väl skrika till eller lägga den i en katalog eller nå't
	} else {
		double left = best.getX() - layoutable.getLayoutWidth();
		double top = best.getY();
		if ( left >= 0 ) layoutable.setLayoutLocation( left, top );
	}
	
	layoutable.setLayoutSuccess( best != null );
}


public boolean equals( Object o )
{
	if ( this == o ) return true;
	if ( ! ( o instanceof CoTopRightLocationSpec ) ) return false;
	return super.equals( o );
}


/**
 * getFactoryKey method comment.
 */
public java.lang.String getFactoryKey() 
{	
	return TOP_RIGHT;
}


/**
 * getHeightAfterLocation method comment.
 */
public double getHeightAfterLocation( CoLayoutableIF layoutable) 
{
	return getBottomHeightAfterLocation(layoutable);
}


public String getIconName()
{

	return null; //"CoBottomRightLocation.gif";
}


/**
 * getLocationSpecAfterReshape method comment.
 */
public com.bluebrim.layout.impl.shared.CoLocationSpecIF getLocationSpecAfterReshape(boolean dx0, boolean dy0, boolean dx1, boolean dy1) 
{
	if
		( dx1 || dy0 )
	{
		return getFactory().getNoLocation();
	} else {
		return null;
	}	
}


public java.lang.String getType()
{
	return TOP_RIGHT;
}


/**
 * getWidthAfterLocation method comment.
 */
public double getWidthAfterLocation( CoLayoutableIF layoutable) 
{
	return getLeftWidthAfterLocation(layoutable);
}


public String getXmlTag()
{
	return XML_TAG;
}


/**
 * isBottomLocation method comment.
 */
public boolean isBottomLocation()
{
	return false;
}


/**
 * isTopLocation method comment.
 */
public boolean isTopLocation()
{
	return true;
}


/**
 * placePageItem method comment.
 */
public void placeLayoutable( CoLayoutableIF layoutable )
{
	CoProfile p = null;
	
	if
		( hasValidParent( layoutable, true ) )
	{
		CoLayoutableContainerIF parent = layoutable.getLayoutParent();
		
		if
			(!isZero())
		{
			p = getTopRightLocations( parent, layoutable, false );
			doPlaceTopRight( layoutable, p );
		} else {
			if(isAggressive())
				p = CoLayoutManagerUtilities.getTopRightLocations( parent.getColumnGrid(), layoutable ); 
			else
				p = CoLayoutManagerUtilities.getTopRightLocations( layoutable, false );
			super.doPlaceTopRight( layoutable, p );
		}
	} else {
		layoutable.setLayoutSuccess( true );
	}
}


public boolean usesBottomSpace(CoLayoutableContainerIF layoutArea)
{
	return false;
	
}


public boolean usesLeftSpace(CoLayoutableContainerIF layoutArea)
{
	return false;
}


public boolean usesRightSpace(CoLayoutableContainerIF layoutArea)
{
	return true;
}


public boolean usesTopSpace(CoLayoutableContainerIF layoutArea)
{
	return true;
}
}