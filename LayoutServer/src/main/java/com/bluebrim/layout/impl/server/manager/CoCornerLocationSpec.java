package com.bluebrim.layout.impl.server.manager;

import java.awt.geom.*;
import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.geom.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Beskriver placering av sidelement mot hörnet men en liten bit ut
 * CoDistance m_xInset bekriver denna förflyttning i columner, m_yInset används inte.
 * PENDING: get*Locations and used*SpaceAt is copies from CoUtilities, should not be corner specific.
 * Ask for used space (bottom, left, right, top) insted of check if used.
 * Creation date: (2000-09-20 10:15:36)
 * @author: Arvid Berg
 */

public abstract class CoCornerLocationSpec extends CoLocationSpec implements CoCornerLocationSpecIF
{
	protected class MutableProxy extends CoLocationSpec.MutableProxy implements CoRemoteCornerLocationSpecIF
	{
		public int getXInset(){return CoCornerLocationSpec.this.getXInset();}
		public boolean isAggressive() { return CoCornerLocationSpec.this.isAggressive(); }
		
		public void setAggressive( boolean b )
		{
			CoCornerLocationSpec.this.setAggressive( b );
			m_owner.notifyOwner();
		}

		public void setXInset(int inset)
		{
			CoCornerLocationSpec.this.setXInset(inset);
			m_owner.notifyOwner();
		}		

	}

	protected int m_xInset = 0;
	protected boolean m_isAggressive = false;
	public static final String XML_IS_AGGRESSIVE = "is-aggressive";
	public static final String XML_X_INSET = "x-inset";

/**
 * Insert the method's description here.
 * Creation date: (2000-09-20 15:41:07)
 */
public CoCornerLocationSpec() {}


protected final CoLocationSpec.MutableProxy createMutableProxy()
{
	return new MutableProxy();
}
public final CoLocationSpecIF deepClone()
{	
	try
	{
		CoCornerLocationSpec tSpec	= (CoCornerLocationSpec)clone();
//		tSpec.m_xInset				= (CoDistanceIF )m_xInset.deepClone();
//		tSpec.m_isAggressive		= m_isAggressive;
		return tSpec;
	}
	catch ( CloneNotSupportedException ex )
	{
		com.bluebrim.base.shared.debug.CoAssertion.assertTrue( false , getClass() + ".deepClone failed:\n" + ex );
		return null;
	}
	
}




/**
 * Returns how long from the edge the pageItem should be placed.
 * Creation date: (2000-09-20 10:26:37)
 * @return double
 */
protected final double getAddWidth(CoLayoutableContainerIF layoutArea,CoLayoutableIF layoutable) 
{
	if
		( m_xInset == 0 )
	{
		return 0;
	} else {
		CoImmutableColumnGridIF g = layoutable.getLayoutParent().getColumnGrid();
		int count = m_xInset;
//		int count = Math.min( m_xInset, g.getColumnCount() );
//		int count = Math.max( 0, Math.min( m_xInset, g.getColumnCount() - 1 ) );
		double spc = ( count <= 1 ) ? 0 : g.getColumnSpacing();
		double value = ( g.getColumnWidth() + spc ) * count - spc;
		return value;
	}
}
/**
 * Returnerar den av mottagarens punkter 
 * som ligger närmast den övre kanten. 
 * Creation date: (2000-09-20 09:24:47)
 * @param profile com.bluebrim.layout.impl.server.manager.CoProfile
 */
public final Point2D getBestPoint(Point2D referencePoint,CoProfile profile) 
{
	final double EQUALITY_TOLERANS = 0.01;
	Point2D best = null;
	
	double Dx = Double.MAX_VALUE;
	double Dy = Double.MAX_VALUE;
	
	Iterator e = profile.getElements();
	if
		( e.hasNext() )
	{
		best = (Point2D) e.next();
		Dx = Math.abs(referencePoint.getX()-best.getX());
		Dy = Math.abs(referencePoint.getY()-best.getY());
	}

	while
		( e.hasNext() )
	{
		Point2D p = (Point2D) e.next();
		double dx = Math.abs(referencePoint.getX()-p.getX());
		double dy = Math.abs(referencePoint.getY()-p.getY());
		if( ( dy + EQUALITY_TOLERANS < Dy ) || ( (dy + EQUALITY_TOLERANS ==Dy)&& (dx+EQUALITY_TOLERANS<=Dx)) )
		{
			Dy = dy;
			Dx= dx;	
			best = p;
		}		
		
	};

	return best;
}
/**
 * 	Skapar en lista med placeringspunkter mot det nedre vänstra hörnet.
 */
public final CoProfile getBottomLeftLocations( CoLayoutableContainerIF parent,
	                                               CoLayoutableIF layoutable,
	                                               boolean allAbsoluteSizePageItemsArePlaced )
{
	CoImmutableColumnGridIF grid = parent.getColumnGrid();
	boolean isInfiniteY = parent.getLayoutSpec().getHeightSpec() instanceof CoContentHeightSpecIF;
	
	CoProfile profile = new CoProfile();
	
	Point2D p = new Point2D.Double();
	Point2D d = new Point2D.Double();
	
	double minY = grid.getTopMarginPosition();
	double maxY = grid.getBottomMarginPosition();
	double prevY = Double.NaN;
	double prevX = Double.NaN;
	double minX = Double.MAX_VALUE;

	Iterator e = grid.getGridLines().iterator();
	while
		( e.hasNext() )
	{
		CoGridLineIF l = (CoGridLineIF) e.next();
		
		if ( l.getType() == CoGridLineIF.EDGE ) continue;
		
		if
			( l.snap( 0, ( minY + maxY ) / 2, Double.MAX_VALUE, CoGeometryConstants.LEFT_EDGE_MASK, p, d ) )
		{
			double x = p.getX();
			double y = Math.min( maxY,
				                   usedBottomSpaceAt( parent,
					                                    x,
					                                    layoutable,
					                                    allAbsoluteSizePageItemsArePlaced,
					                                    false ) );

			if
				( ( ( y != prevY ) || ( x < prevX ) ) && ( isInfiniteY || ( y > minY ) ) )
			{
				double w=getAddWidth(parent,layoutable);
				double tempX=grid.snap(x+w,
						( grid.getTopMarginPosition() + grid.getBottomMarginPosition() ) / 2,Double.MAX_VALUE,
						CoGeometryConstants.LEFT_EDGE_MASK, CoGeometryConstants.TO_RIGHT_MASK,true,null).getX();
						
				profile.addPoint( new Point2D.Double( tempX, y ) );
				if ( x < minX ) minX = x;
				prevY = y;
				prevX = x;
			}
		}
	}

	return profile;
}
/**
 * Skapar en lista med placeringspunkter mot det nedre högra hörnet.
 */
public final CoProfile getBottomRightLocations( CoLayoutableContainerIF parent, 
	                                                CoLayoutableIF layoutable,
	                                                boolean allAbsoluteSizePageItemsArePlaced )
{
	CoImmutableColumnGridIF grid = parent.getColumnGrid();
	boolean isInfiniteY = parent.getLayoutSpec().getHeightSpec() instanceof CoContentHeightSpecIF;
	
	CoProfile profile = new CoProfile();
	
	Point2D p = new Point2D.Double();
	Point2D d = new Point2D.Double();
	
	double minY = grid.getTopMarginPosition();
	double maxY = grid.getBottomMarginPosition();
	double prevY = Double.NaN;
	double prevX = Double.NaN;
	double minX = Double.MAX_VALUE;

	double w = layoutable.getLayoutWidth();
	w+=getAddWidth(parent,layoutable);
	Iterator e = grid.getGridLines().iterator();
	while
		( e.hasNext() )
	{
		CoGridLineIF l = (CoGridLineIF) e.next();
		
		if ( l.getType() == CoGridLineIF.EDGE ) continue;
		
		if
			( l.snap( 0, ( minY + maxY ) / 2, Double.MAX_VALUE, CoGeometryConstants.RIGHT_EDGE_MASK, p, d ) )
		{
			double x = p.getX();
			double y = Math.min( maxY,
				                   usedBottomSpaceAt( parent,
					                 x - w,
					                 layoutable,
					                 allAbsoluteSizePageItemsArePlaced,
					                 true ) );

			if
				( ( ( y != prevY ) || ( x > prevX ) ) && ( isInfiniteY || ( y > minY ) ) )
			{
				double wx=getAddWidth(parent,layoutable);
				double tempX=grid.snap(x-wx,
						( grid.getTopMarginPosition() + grid.getBottomMarginPosition() ) / 2,Double.MAX_VALUE,
						CoGeometryConstants.RIGHT_EDGE_MASK, CoGeometryConstants.TO_LEFT_MASK,true,null).getX();
						
				profile.addPoint( new Point2D.Double( tempX, y ) );
				if ( x < minX ) minX = x;
				prevY = y;
				prevX = x;
			}
		}
	}

	return profile;
}





/**
 * Skapar en lista med placeringspunkter mot det övre vänstra hörnet.
 */
public final CoProfile getTopLeftLocations( CoLayoutableContainerIF parent,
	                                           CoLayoutableIF layoutable,
	                                           boolean allAbsoluteSizePageItemsArePlaced )
{
	CoImmutableColumnGridIF grid = parent.getColumnGrid();
	boolean isInfiniteY = parent.getLayoutSpec().getHeightSpec() instanceof CoContentHeightSpecIF;
	
	CoProfile profile = new CoProfile();
	
	Point2D p = new Point2D.Double();
	Point2D d = new Point2D.Double();
	
	double minY = grid.getTopMarginPosition();
	double maxY = grid.getBottomMarginPosition();
	double prevY = Double.NaN;
	double prevX = Double.NaN;
	double minX = Double.MAX_VALUE;

	Iterator e = grid.getGridLines().iterator();
	while
		( e.hasNext() )
	{
		CoGridLineIF l = (CoGridLineIF) e.next();
		
		if ( l.getType() == CoGridLineIF.EDGE ) continue;
		
		if
			( l.snap( 0, ( minY + maxY ) / 2, Double.MAX_VALUE, CoGeometryConstants.LEFT_EDGE_MASK, p, d ) )
		{
			double x = p.getX();
			double y = Math.max( minY, 
				                   usedTopSpaceAt( parent,
					                                 x, 
					                                 layoutable,
					                                 allAbsoluteSizePageItemsArePlaced,
					                                 false ) );

			if
				( ( ( y != prevY ) || ( x < prevX ) ) && ( isInfiniteY || ( y < maxY ) ) )
			{
				double w=getAddWidth(parent,layoutable);
				double tempX=grid.snap(x+w,
						( grid.getTopMarginPosition() + grid.getBottomMarginPosition() ) / 2,Double.MAX_VALUE,
						CoGeometryConstants.LEFT_EDGE_MASK, CoGeometryConstants.TO_RIGHT_MASK,true,null).getX();
				
				
				profile.addPoint( new Point2D.Double( tempX, y ) );
				if ( x < minX ) minX = x;
				prevY = y;
				prevX = x;
			}
		}
	}

	return profile;
}
/**
 * Skapar en lista med placeringspunkter mot det övre högra hörnet.
 */
public final CoProfile getTopRightLocations( CoLayoutableContainerIF parent,
	                                             CoLayoutableIF layoutable,
	                                             boolean allAbsoluteSizePageItemsArePlaced )
{
	CoImmutableColumnGridIF grid = parent.getColumnGrid();
	boolean isInfiniteY = parent.getLayoutSpec().getHeightSpec() instanceof CoContentHeightSpecIF;
	
	CoProfile profile = new CoProfile();
	
	Point2D p = new Point2D.Double();
	Point2D d = new Point2D.Double();
	
	double minY = grid.getTopMarginPosition();
	double maxY = grid.getBottomMarginPosition();
	double prevY = Double.NaN;
	double prevX = Double.NaN;
	double minX = Double.MAX_VALUE;

	double w = layoutable.getLayoutWidth();
	w+=getAddWidth(parent,layoutable);
	
	Iterator e = grid.getGridLines().iterator();
	while
		( e.hasNext() )
	{
		CoGridLineIF l = (CoGridLineIF) e.next();
		
		if ( l.getType() == CoGridLineIF.EDGE ) continue;
		
		if
			( l.snap( 0, ( minY + maxY ) / 2, Double.MAX_VALUE, CoGeometryConstants.RIGHT_EDGE_MASK, p, d ) )
		{
			double x = p.getX();
			double y = Math.max( minY, usedTopSpaceAt( parent,
				                                         x - w,
				                                         layoutable,
				                                         allAbsoluteSizePageItemsArePlaced,
				                                         true ) );

			if
				( ( ( y != prevY ) || ( x > prevX ) ) && ( isInfiniteY || ( y < maxY ) ) )
			{
				double wx=getAddWidth(parent,layoutable);
				double tempX=grid.snap(x-wx,
						( grid.getTopMarginPosition() + grid.getBottomMarginPosition() ) / 2,Double.MAX_VALUE,
						CoGeometryConstants.RIGHT_EDGE_MASK, CoGeometryConstants.TO_LEFT_MASK,true,null).getX();
						
				profile.addPoint( new Point2D.Double( tempX, y ) );
				if ( x < minX ) minX = x;
				prevY = y;
				prevX = x;
			}
		}
	}

	return profile;
}



public final boolean isAggressive()
{
	return m_isAggressive;
}
/**
 * Insert the method's description here.
 * Creation date: (2000-09-20 15:27:36)
 * @return boolean
 */
protected final boolean isZero() 
{
	return m_xInset == 0;
}

public final void setAggressive( boolean b )
{
	m_isAggressive = b;
}


/*
 * Returnera lägsta y-koordinat för översidan bland de
 * sidelement som genomskärs av en vertikal linje vid x.
 * Om en grid skickas med avser svaret Y-koordinaten för
 * den baseline som ligger närmast ovanför överkanten på
 * det översta sidelement som linjen skär igenom .
 */
public final double usedBottomSpaceAt( CoLayoutableContainerIF layoutArea, double x, CoLayoutableIF pageItem, boolean allAbsoluteSizePageItemsArePlaced, boolean ignoreRunAround )
{
	double y = layoutArea.getLayoutHeight();
	
	CoImmutableColumnGridIF grid = layoutArea.getColumnGrid();

	double w = ( ! ignoreRunAround && pageItem.getDoRunAround() ) ? 0 : pageItem.getLayoutWidth();
	w+=getAddWidth(layoutArea,pageItem);
	
	Iterator e = layoutArea.getLayoutChildren().iterator();
	while
		(e.hasNext())
	{
		CoLayoutableIF pi = (CoLayoutableIF) e.next();
		double top = pi.getTopEdge();
		
		if ( grid != null ) top = CoLayoutManagerUtilities.getFirstHorizontalLineAbove( grid, top );
			
		if
			(
				( top < y )
			&&
				( CoLayoutManagerUtilities.containsX( pi, x, w ) )
			&&
				( CoLayoutManagerUtilities.usesBottomSpace( pi, pageItem, allAbsoluteSizePageItemsArePlaced ) )
			)
		{
			y = top;
		}
	};
	return y;
}
/*
 * Returnera högsta y-koordinat för undersidan bland de
 * sidelement som genomskärs av en vertikal linje vid x.
 * Om en grid skickas med avser svaret Y-koordinaten för
 * den baseline som ligger närmast nedanför underkanten på
 * det understa sidelement som x skär igenom .
 */
public final double usedTopSpaceAt( CoLayoutableContainerIF layoutArea, double x, CoLayoutableIF pageItem, boolean allAbsoluteSizePageItemsArePlaced, boolean ignoreRunAround )
{
	double tUsedTopSpace = 0;
	CoImmutableColumnGridIF grid = layoutArea.getColumnGrid();
		
	double w = ( ! ignoreRunAround && pageItem.getDoRunAround() ) ? 0 : pageItem.getLayoutWidth();
	//w+=m_xInset.getValue(layoutArea,pageItem);
	w+=getAddWidth(layoutArea,pageItem);
	
	Iterator e = layoutArea.getLayoutChildren().iterator();
	while (e.hasNext())
	{
		CoLayoutableIF tPageItem = (CoLayoutableIF) e.next();
		double tBottomEdge = tPageItem.getBottomEdge();
		
		if (grid != null)
			tBottomEdge = CoLayoutManagerUtilities.getFirstHorizontalLineBelow(grid,tBottomEdge);

		if
			(
				CoLayoutManagerUtilities.containsX( tPageItem, x, w )
			&&
				CoLayoutManagerUtilities.usesTopSpace( tPageItem, pageItem, allAbsoluteSizePageItemsArePlaced )
			&&
				( tBottomEdge > tUsedTopSpace )
			)
		{
			tUsedTopSpace = tBottomEdge;
		}
	}

	return tUsedTopSpace;
}





public boolean equals( Object o )
{
	if ( this == o ) return true;
	if
		( o instanceof CoCornerLocationSpec )
	{
		CoCornerLocationSpec s = (CoCornerLocationSpec) o;
		
		return
			( m_xInset == s.m_xInset )
		&&
			m_isAggressive == s.m_isAggressive;
	} else {
		return super.equals( o );
	}
}




/**
 * getXInset method comment.
 */
public final int getXInset() 
{
	return m_xInset;
}

/**
 * setXInsett method comment.
 */
public final void setXInset(int inset) 
{
	m_xInset=inset;
}

/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-25
 */
 
protected CoCornerLocationSpec( Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	super();

	// xml init
	NamedNodeMap map = node.getAttributes();
	
	m_isAggressive = CoModelBuilder.getBoolAttrVal( map, XML_IS_AGGRESSIVE, m_isAggressive );
	m_xInset = CoModelBuilder.getIntAttrVal( map, XML_X_INSET, m_xInset );
}


/**
 *	Used by a com.bluebrim.xml.shared.CoXmlVisitorIF instance when visiting an object.
 *  The object then uses the com.bluebrim.xml.shared.CoXmlVisitorIF interface to feed the
 *  visitor with state information
 */
	 
public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor )
{
	visitor.exportAttribute( XML_X_INSET, Integer.toString( m_xInset ) );
	visitor.exportAttribute( XML_IS_AGGRESSIVE, ( m_isAggressive ? Boolean.TRUE : Boolean.FALSE ).toString() );
}
}