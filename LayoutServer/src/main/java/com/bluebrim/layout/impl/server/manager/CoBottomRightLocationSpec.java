package com.bluebrim.layout.impl.server.manager;
import java.awt.geom.Point2D;

import org.w3c.dom.Node;

import com.bluebrim.base.shared.geom.CoGeometryConstants;
import com.bluebrim.layout.shared.*;

/**
 * Beskriver placering av sidelement mot h�rnet men en liten bit ut
 * CoDistance m_xInset bekriver denna f�rflyttning i columner, m_yInset anv�nds inte.
 * PENDING: get*Locations and used*SpaceAt is copies from CoUtilities, should not be corner specific.
 * Ask for used space (bottom, left, right, top) insted of check if used.
 * Creation date: (2000-09-20 10:15:36)
 * @author: Arvid Berg
 */

public class CoBottomRightLocationSpec extends CoCornerLocationSpec
{
	public static final String XML_TAG = "bottom-right-location";
/*
 * Used at XML import
 * Helena Rankeg�rd 2001-10-25
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoBottomRightLocationSpec( node, context );
}

/**
 * Insert the method's description here.
 * Creation date: (2000-09-20 15:41:07)
 */
public CoBottomRightLocationSpec() {}


/*
 * Contructor used for XML import.
 * Helena Rankeg�rd 2001-10-25
 */
 
protected CoBottomRightLocationSpec( Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	super( node, context );

	// xml init
}


/**
 * Placerar sidelementet s� n�ra point som m�jligt.
 *
 */
public void doPlaceBottomRight( CoLayoutableIF layoutable, CoProfile profile)
{	
	Point2D ybottom = layoutable.getLayoutParent().getColumnGrid().snap( 0, layoutable.getLayoutParent().getLayoutHeight(), Double.MAX_VALUE, CoGeometryConstants.BOTTOM_EDGE_MASK, CoGeometryConstants.ALL_DIRECTIONS_MASK, false, null );
	Point2D best = getBestPoint( new Point2D.Double( Double.NaN, ybottom.getY() ) ,profile);
	
	if
		( best == null )
	{
//		layoutable.setTopLeft( new Point2D.Double( -10, -10 ) ); // provisorium, ska v�l skrika till eller l�gga den i en katalog eller n�'t
	} else {
		double left = best.getX() - layoutable.getLayoutWidth();
		double top = best.getY() - layoutable.getLayoutHeight();
		if ( left >= 0 ) layoutable.setLayoutLocation( left, top );
	}
	
	layoutable.setLayoutSuccess( best != null );
}


public boolean equals( Object o )
{
	if ( this == o ) return true;
	if ( ! ( o instanceof CoBottomRightLocationSpec ) ) return false;
	return super.equals( o );
}


public java.lang.String getFactoryKey() 
{	
	return BOTTOM_RIGHT;
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
		( dx1 || dy1 )
	{
		return getFactory().getNoLocation();
	} else {
		return null;
	}	
}


public java.lang.String getType()
{
	return BOTTOM_RIGHT;
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
	CoProfile p = null;
	
	if
		( hasValidParent( layoutable, true ) )
	{
		CoLayoutableContainerIF parent = layoutable.getLayoutParent();
		if(!isZero())
		{
			p = getBottomRightLocations( parent, layoutable, false );
			doPlaceBottomRight(  layoutable, p );
		}
		else
		{
			if(isAggressive())
				p = CoLayoutManagerUtilities.getBottomRightLocations( parent.getColumnGrid(), layoutable ); 	
			else
				p = CoLayoutManagerUtilities.getBottomRightLocations( layoutable, false );
			super.doPlaceBottomRight( layoutable, p );
		}
	} else {
		layoutable.setLayoutSuccess( true );
	}
		

	
}


/*
 * De CoLocationSpec's som staplar sidelement mot underkanten och upp�t returnerar sant.
 * Alla andra returnerar falskt.
 */
public boolean usesBottomSpace(CoLayoutableContainerIF layoutArea)
{
	return true;
	
}


/*
 * De CoLocationSpec's som staplar sidelement mot v�nsterkanten returnerar sant.
 * Alla andra returnerar falskt.
 */
public boolean usesLeftSpace(CoLayoutableContainerIF layoutArea)
{
	return false;
}


/*
 * De CoLocationSpec's som staplar sidelement mot h�gerkanten returnerar sant.
 * Alla andra returnerar falskt.
 */
public boolean usesRightSpace(CoLayoutableContainerIF layoutArea)
{
	return true;
}


/*
 * De CoLocationSpec's som staplar sidelement mot �verkanten och ned�t returnerar sant.
 * Alla andra returnerar falskt.
 */
public boolean usesTopSpace(CoLayoutableContainerIF layoutArea)
{
	return false;
}
}