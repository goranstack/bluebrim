package com.bluebrim.layout.impl.server.manager;
import java.awt.geom.Point2D;

import org.w3c.dom.Node;

import com.bluebrim.base.shared.CoFactoryManager;
import com.bluebrim.base.shared.CoPropertyChangeListener;
import com.bluebrim.base.shared.CoSimpleObject;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.CoLocationSpecIF;
import com.bluebrim.layout.shared.*;

/**
 * Abstrakt superklass till objekt som specificerar placering
 * av sidelement. Alla klasser är Singelton-pattern.
 */
 
public abstract class CoLocationSpec extends CoSimpleObject implements CoLocationSpecIF, com.bluebrim.xml.shared.CoXmlEnabledIF
{
	protected class MutableProxy implements CoRemoteLocationSpecIF
	{
		public CoLocationSpecIF.Owner m_owner;

		public void removePropertyChangeListener( CoPropertyChangeListener l ) { CoLocationSpec.this.removePropertyChangeListener( l ); }
		public void addPropertyChangeListener( CoPropertyChangeListener l ) { CoLocationSpec.this.addPropertyChangeListener( l ); }
		public CoLocationSpecIF deepClone() { return CoLocationSpec.this.deepClone(); }
		public String getFactoryKey() { return CoLocationSpec.this.getFactoryKey(); }
		public String getType() { return CoLocationSpec.this.getType(); }
		public boolean isAbsolutePosition() { return CoLocationSpec.this.isAbsolutePosition(); }
		public void configure( CoShapePageItemIF pi ) { CoLocationSpec.this.configure( pi ); }
	}
/*
 * Used at XML import
 * Helena Rankegård 2001-10-23
 */
 
public void xmlAddSubModel( String name, Object subModel, com.bluebrim.xml.shared.CoXmlContext context )
{
}


public void configure( CoShapePageItemIF pi )
{
}


protected MutableProxy createMutableProxy()
{
	return new MutableProxy();
}


public CoLocationSpecIF deepClone()
{
	return this;
}


/**
 * Placerar sidelementet så nära point som möjligt.
 *
 */
public void doPlaceBottomLeft( CoLayoutableIF layoutable, CoProfile profile)
{
	Point2D best = profile.getClosestToBottomLeft( layoutable );
	if
		( best == null )
	{
//		layoutable.setTopLeft( new Point2D.Double( -10, -10 ) ); // provisorium, ska väl skrika till eller lägga den i en katalog eller nå't
	} else {
		double left = best.getX();
		double top = best.getY() - layoutable.getLayoutHeight();
		layoutable.setLayoutLocation( left, top );
	}
	
	layoutable.setLayoutSuccess( best != null );
}


/**
 * Placerar sidelementet så nära point som möjligt.
 *
 */
public void doPlaceBottomRight( CoLayoutableIF layoutable, CoProfile profile)
{
	Point2D best = profile.getClosestToBottomRight( layoutable );
	if
		( best == null )
	{
//		layoutable.setTopLeft( new Point2D.Double( -10, -10 ) ); // provisorium, ska väl skrika till eller lägga den i en katalog eller nå't
	} else {
		double left = best.getX() - layoutable.getLayoutWidth();
		double top = best.getY() - layoutable.getLayoutHeight();
		layoutable.setLayoutLocation( left, top );
	}
	
	layoutable.setLayoutSuccess( best != null );
}


/**
 * Placerar sidelementet så nära point som möjligt.
 *
 */
public void doPlaceTopLeft( CoLayoutableIF layoutable, CoProfile profile)
{
	Point2D best = profile.getClosestToTopLeft( layoutable );
	if
		( best == null )
	{
//		layoutable.setTopLeft( new Point2D.Double( -10, -10 ) ); // provisorium, ska väl skrika till eller lägga den i en katalog eller nå't
	} else {
		double left = best.getX();
		double top = best.getY();
		layoutable.setLayoutLocation( left, top );
	}
	
	layoutable.setLayoutSuccess( best != null );
}


/**
 * Placerar sidelementet så nära point som möjligt.
 *
 */
public void doPlaceTopRight(CoLayoutableIF layoutable, CoProfile profile)
{
	Point2D best = profile.getClosestToTopRight( layoutable );
	if
		( best == null )
	{
//		layoutable.setTopLeft( new Point2D.Double( -10, -10 ) ); // provisorium, ska väl skrika till eller lägga den i en katalog eller nå't
	} else {
		double left = best.getX() - layoutable.getLayoutWidth();
		double top = best.getY();
		layoutable.setLayoutLocation( left, top );
	}
	
	layoutable.setLayoutSuccess( best != null );
}


/*
 *	Svarar med den största utbredning som pageItem kan ha åt HÖGER
 */
protected double getBottomHeightAfterLocation(  CoLayoutableIF layoutable )
{
	CoProfile targetProfile = CoProfile.createBottomProfileForHeightDetermination( layoutable );
		
	double H	= CoLayoutManagerUtilities.getHeightFromTopToBottomMargin( layoutable.getLayoutParent() );
	double h = targetProfile.getLowestY() - layoutable.getTopEdge();
	
	if ( h == 0 )	 return H;
		
	if
		( h < H )
	{
		return h;
	} else {
		return H;
	}
}


protected static final CoLayoutSpecFactoryIF getFactory()
{
	return (CoLayoutSpecFactoryIF) CoFactoryManager.getFactory( CoLayoutSpecIF.LAYOUT_SPEC );
}


public abstract double getHeightAfterLocation( CoLayoutableIF layoutable );


public String getIconResourceAnchor()
{
	return CoLocationSpecIF.class.getName();
}


protected double getLeftRightWidthAfterLocation( CoLayoutableIF layoutable )
{
	double rw = getRightWidthAfterLocation( layoutable );
	double lw = getLeftWidthAfterLocation( layoutable );

	double w = 2 * Math.min( rw, - lw );

	layoutable.setLayoutX( ( layoutable.getLayoutParent().getLayoutWidth() - w ) / 2 );
	
	return w;
}


/*
 *	Svarar med den största utbredning som pageItem kan ha åt VÄNSTER
 */
protected double getLeftWidthAfterLocation( CoLayoutableIF layoutable )
{
	CoProfile anchorProfile = CoProfile.createLeftProfileForWidthDetermination( layoutable );
		
	double W	= CoLayoutManagerUtilities.getWidthFromLeftToRightMargin( layoutable.getLayoutParent() );
	double w = anchorProfile.getLowestX() - layoutable.getLeftEdge();
	
	if ( w == 0 )	return W;
		
	if
		( w < W )
	{
		return w;
	} else {
		return W;
	}
}


public abstract CoLocationSpecIF getLocationSpecAfterReshape( boolean dx0, boolean dy0, boolean dx1, boolean dy1 ); // return null means return this


public CoLocationSpecIF getMutableProxy( CoLocationSpecIF.Owner owner )
{
	MutableProxy mp = createMutableProxy();
	mp.m_owner = owner;
	return mp;
}


public static CoLocationSpecIF getNoLocation()
{
	CoLayoutSpecFactoryIF f = (CoLayoutSpecFactoryIF) CoFactoryManager.getFactory( CoLayoutSpecIF.LAYOUT_SPEC );
	return f.getNoLocation();
}


/*
 *	Svarar med den största utbredning som pageItem kan ha åt HÖGER
 */
protected double getRightWidthAfterLocation( CoLayoutableIF layoutable )
{
	CoProfile targetProfile = CoProfile.createRightProfileForWidthDetermination( layoutable );
		
	double W	= CoLayoutManagerUtilities.getWidthFromLeftToRightMargin( layoutable.getLayoutParent() );
	double w = targetProfile.getLowestX() - layoutable.getLeftEdge();
	
	if ( w == 0 )	 return W;
		
	if
		( w < W )
	{
		return w;
	} else {
		return W;
	}
}


public static CoLocationSpecIF getSlaveLocation()
{
	CoLayoutSpecFactoryIF f = (CoLayoutSpecFactoryIF) CoFactoryManager.getFactory( CoLayoutSpecIF.LAYOUT_SPEC );
	return f.getSlaveLocation();
}


protected double getTopBottomHeightAfterLocation( CoLayoutableIF layoutable )
{
	double bh = getBottomHeightAfterLocation( layoutable );
	double th = getTopHeightAfterLocation( layoutable );

	double h = 2 * Math.min( bh, - th );

	layoutable.setLayoutY( ( layoutable.getLayoutParent().getLayoutHeight() - h ) / 2 );
	
	return h;
}


/*
 *	Svarar med den största utbredning som pageItem kan ha NEDÅT
 */
protected double getTopHeightAfterLocation( CoLayoutableIF layoutable )
{
	CoProfile anchorProfile = CoProfile.createTopProfileForHeightDetermination( layoutable );
		
	double H	= CoLayoutManagerUtilities.getHeightFromTopToBottomMargin( layoutable.getLayoutParent() );
	double h = anchorProfile.getLowestY() - layoutable.getTopEdge();
	
	if ( h == 0 )	return H;
		
	if
		( h < H )
	{
		return h;
	} else {
		return H;
	}
}


public abstract String getType();


public abstract double getWidthAfterLocation( CoLayoutableIF layoutable );


public abstract String getXmlTag();


public int hashCode()
{
	return getFactoryKey().hashCode();
}


public final boolean hasValidParent( CoLayoutableIF layoutable, boolean checkParentDimensions )
{
	CoLayoutableContainerIF parent = layoutable.getLayoutParent();

	if ( parent == null ) return false;

	if ( ! checkParentDimensions ) return true;

	return parent.hasFiniteDimensions();
}


public boolean isAbsolutePosition ()
{
	return false;
}


public boolean isBottomLocation()
{
	return false;
}


public boolean isNull()
{
	return false;
}


public boolean isTopLocation()
{
	return false;
}


public abstract void placeLayoutable( CoLayoutableIF layoutable );


public String toString()
{
	return getType();
}


/*
 * De CoLocationSpec's som staplar sidelement mot underkanten och uppåt returnerar sant.
 * Alla andra returnerar falskt.
 */
public boolean usesBottomSpace(CoLayoutableContainerIF layoutArea)
{
	return false;
}


/*
 * De CoLocationSpec's som staplar sidelement mot vänsterkanten returnerar sant.
 * Alla andra returnerar falskt.
 */
public boolean usesLeftSpace(CoLayoutableContainerIF layoutArea)
{
	return false;
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
 * Helena Rankegård 2001-10-23
 */
 
public void xmlImportFinished( Node node, com.bluebrim.xml.shared.CoXmlContext context )
{
}


/**
 *	Used by a com.bluebrim.xml.shared.CoXmlVisitorIF instance when visiting an object.
 *  The object then uses the com.bluebrim.xml.shared.CoXmlVisitorIF interface to feed the
 *  visitor with state information
 */
public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor)
{
}
}