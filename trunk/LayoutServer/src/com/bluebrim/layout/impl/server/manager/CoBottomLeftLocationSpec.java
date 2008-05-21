package com.bluebrim.layout.impl.server.manager;
import java.awt.geom.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.geom.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Beskriver placering av sidelement mot hörnet men en liten bit ut
 * CoDistance m_xInset bekriver denna förflyttning i columner, m_yInset används inte.
 * PENDING: get*Locations and used*SpaceAt is copies from CoUtilities, should not be corner specific.
 * Ask for used space (bottom, left, right, top) insted of check if used.
 * Creation date: (2000-09-20 10:15:36)
 * @author: Arvid Berg
 */

public class CoBottomLeftLocationSpec extends CoCornerLocationSpec
{
	public static final String XML_TAG = "bottom-left-location";
/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-25
 */
 
protected CoBottomLeftLocationSpec( Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	super( node, context );

	// xml init
}

/**
 * Insert the method's description here.
 * Creation date: (2000-09-20 15:41:07)
 */
public CoBottomLeftLocationSpec() {}


/**
 * Placerar sidelementet så nära point som möjligt.
 *
 */
public void doPlaceBottomLeft( CoLayoutableIF layoutable, CoProfile profile)
{	
	Point2D ybottom = layoutable.getLayoutParent().getColumnGrid().snap( 0, layoutable.getLayoutParent().getLayoutHeight(), Double.MAX_VALUE, CoGeometryConstants.BOTTOM_EDGE_MASK, CoGeometryConstants.ALL_DIRECTIONS_MASK, false, null );
	Point2D best=getBestPoint( new Point2D.Double( Double.NaN, ybottom.getY() ) ,profile);
	
	if
		( best == null )
	{
//		layoutable.setTopLeft( new Point2D.Double( -10, -10 ) ); // provisorium, ska väl skrika till eller lägga den i en katalog eller nå't
	} else {
		double left = best.getX();
		double top = best.getY() - layoutable.getLayoutHeight();
		if ( left < layoutable.getLayoutParent().getLayoutWidth() ) layoutable.setLayoutLocation( left, top );
	}
	
	layoutable.setLayoutSuccess( best != null );
}


public boolean equals( Object o )
{
	if ( this == o ) return true;
	if ( ! ( o instanceof CoBottomLeftLocationSpec ) ) return false;
	return super.equals( o );
}


public java.lang.String getFactoryKey()
{
	return BOTTOM_LEFT;
}


/**
 * getHeightAfterLocation method comment.
 */
public double getHeightAfterLocation( CoLayoutableIF layoutable) 
{
	return getTopHeightAfterLocation(layoutable);
}


public  String getIconName ( ) {
	
	return null;//"CoBottomRightLocation.gif";
}


/**
 * getLocationSpecAfterReshape method comment.
 */
public com.bluebrim.layout.impl.shared.CoLocationSpecIF getLocationSpecAfterReshape(boolean dx0, boolean dy0, boolean dx1, boolean dy1) 
{
	if
		( dx0 || dy1 )
	{
		return getFactory().getNoLocation();
	} else {
		return null;
	}	
}


/**
 * getType method comment.
 */
public java.lang.String getType() {
	return BOTTOM_LEFT;
}


/**
 * getWidthAfterLocation method comment.
 */
public double getWidthAfterLocation( CoLayoutableIF layoutable) 
{
	return getRightWidthAfterLocation(layoutable);
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
	return true;
}


/**
 * isTopLocation method comment.
 */
public boolean isTopLocation()
{
	return false;
}


/**
 * placePageItem method comment.
 */
public void placeLayoutable( CoLayoutableIF layoutable )
{
	if
		( hasValidParent( layoutable, true ) )
	{
		CoLayoutableContainerIF parent = layoutable.getLayoutParent();

		CoProfile p = null;
		
		if(!isZero())
		{
			p = getBottomLeftLocations( parent, layoutable, false );
			doPlaceBottomLeft( layoutable, p );
		}
		else
		{
			if(isAggressive())
				p = CoLayoutManagerUtilities.getBottomLeftLocations( parent.getColumnGrid(), layoutable ); 	
			else
				p = CoLayoutManagerUtilities.getBottomLeftLocations( layoutable, false );
			super.doPlaceBottomLeft( layoutable, p );
		}

	} else {
		layoutable.setLayoutSuccess( true );
	}

}


/*
 * De CoLocationSpec's som staplar sidelement mot underkanten och uppåt returnerar sant.
 * Alla andra returnerar falskt.
 */
public boolean usesBottomSpace(CoLayoutableContainerIF layoutArea)
{
	return true;
	
}


/*
 * De CoLocationSpec's som staplar sidelement mot vänsterkanten returnerar sant.
 * Alla andra returnerar falskt.
 */
public boolean usesLeftSpace(CoLayoutableContainerIF layoutArea)
{
	return true;
}


/*
 * De CoLocationSpec's som staplar sidelement mot högerkanten returnerar sant.
 * Alla andra returnerar falskt.
 */
public boolean usesRightSpace(CoLayoutableContainerIF layoutArea)
{
	return false;
}


/*
 * De CoLocationSpec's som staplar sidelement mot överkanten och nedåt returnerar sant.
 * Alla andra returnerar falskt.
 */
public boolean usesTopSpace(CoLayoutableContainerIF layoutArea)
{
	return false;
}


/*
 * Used at XML import
 * Helena Rankegård 2001-10-25
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoBottomLeftLocationSpec( node, context );
}
}