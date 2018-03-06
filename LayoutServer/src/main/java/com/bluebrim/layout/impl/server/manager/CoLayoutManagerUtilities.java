package com.bluebrim.layout.impl.server.manager;

import java.awt.geom.Point2D;
import java.util.Iterator;

import com.bluebrim.base.shared.geom.CoGeometryConstants;
import com.bluebrim.layout.impl.shared.CoContentHeightSpecIF;
import com.bluebrim.layout.impl.shared.CoGridLineIF;
import com.bluebrim.layout.shared.*;
import com.bluebrim.layout.shared.CoImmutableColumnGridIF;

//

public class CoLayoutManagerUtilities
{
	static final double EQUALITY_TOLERANS = 0.1;
private static boolean areEqual( double d1, double d2 )
{
	return ((d1 + EQUALITY_TOLERANS >= d2) && (d1 - EQUALITY_TOLERANS <= d2));
}
/**
 * Svarar sant om en vertikal linje vid X skär genom mottagaren.
 * Tillåter en viss (0.1) tolerans.
 */
public static boolean containsX( CoLayoutableIF pi, double x, double w )
{
	if ( x + w + EQUALITY_TOLERANS < pi.getLeftEdge() ) return false;
	if ( x - EQUALITY_TOLERANS > pi.getRightEdge() ) return false;
	return true;
}
/**
 *	Svarar sant om en horisontell linje vid Y skär genom mottagaren
 *
 *	Anm 1
 *	Anta att Y är lika med mottagarens underkant.
 *	Om mottagaren är placerad mot ett av de övre hörnen eller mot överkanten ska linjen
 *	inte anses skära genom mottagarens underkant.
 *	Detta för att man då vill hitta det X i mottagarens underkant som ligger längst 
 *	från förälderns (layoutAreans) mitt.
 *	Undantag: Då radstödradraster används eller då underkanten ligger på den undre marginalen 
 *	ska mottagaren alltid anses innehålla Y. 
 *
 *	Anm 2
 *	Anta att Y är lika med mottagarens överkant.
 *	Om mottagaren är placerad mot ett av de nedre hörnen eller mot nederkanten ska linjen
 *	inte anses skära genom mottagarens överkant.
 *	Detta för att man då vill hitta det X i mottagarens överkant som ligger längst 
 *	från förälderns (layoutAreans) mitt.
 *	Undantag: Då radstödradraster används eller då underkanten ligger på den övre marginalen 
 *	ska mottagaren alltid anses innehålla Y. 
 */
public static boolean containsY ( CoLayoutableIF layoutable, double y )
{
	/*
	String key = layoutable.getLayoutSpec().getLocationSpec().getFactoryKey();
	boolean topLocation	= key.equals( CoTopLeftLocationIF.TOP_LEFT_LOCATION ) || key.equals( CoTopRightLocationIF.TOP_RIGHT_LOCATION );
	*/
	boolean topLocation	= ( (CoLocationSpec) layoutable.getLayoutSpec().getLocationSpec() ).isTopLocation();
	double bottom = layoutable.getBottomEdge();
	if
		( areEqual( y, bottom ) && topLocation )
	{
		return areEqual( bottom, layoutable.getLayoutParent().getColumnGrid().getBottomMarginPosition() );// || parent.usesHorizontalLinesForLayout();
	}
	
//	boolean bottomLocation = key.equals( CoBottomLeftLocationIF.BOTTOM_LEFT_LOCATION ) || key.equals( CoBottomRightLocationIF.BOTTOM_RIGHT_LOCATION );
	boolean bottomLocation	= ( (CoLocationSpec) layoutable.getLayoutSpec().getLocationSpec() ).isBottomLocation();
	double top = layoutable.getTopEdge();
	if
		( areEqual( y, top ) && bottomLocation )
	{
		return areEqual( top, layoutable.getLayoutParent().getColumnGrid().getTopMarginPosition());// || parent.usesHorizontalLinesForLayout();
	}
				
	return ( y + EQUALITY_TOLERANS >= top ) && ( y - EQUALITY_TOLERANS <= bottom );
}
/**
 * 	Skapar en lista med placeringspunkter mot det nedre vänstra hörnet.
 */
public static CoProfile getBottomLeftLocations( CoImmutableColumnGridIF grid, CoLayoutableIF layoutable )
{
	CoProfile profile = new CoProfile();
	boolean isInfiniteY = layoutable.getLayoutParent().getLayoutSpec().getHeightSpec() instanceof CoContentHeightSpecIF;
	
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
		
		if ( l.getType() == l.EDGE ) continue;
		
		if
			( l.snap( 0, ( minY + maxY ) / 2, Double.MAX_VALUE, CoGeometryConstants.LEFT_EDGE_MASK, p, d ) )
		{
			double x = p.getX();
			double y = maxY;

			if
				( ( ( y != prevY ) || ( x < prevX ) ) && ( isInfiniteY || ( y > minY ) ) )
			{
				profile.addPoint( new Point2D.Double( x, y ) );
				if ( x < minX ) minX = x;
				prevY = y;
				prevX = x;
			}
		}
	}

	return profile;
}
/**
 * 	Skapar en lista med placeringspunkter mot det nedre vänstra hörnet.
 */
public static CoProfile getBottomLeftLocations( CoLayoutableIF layoutable,
	                                               boolean allAbsoluteSizePageItemsArePlaced )
{
	CoImmutableColumnGridIF grid = layoutable.getLayoutParent().getColumnGrid();
	boolean isInfiniteY = layoutable.getLayoutParent().getLayoutSpec().getHeightSpec() instanceof CoContentHeightSpecIF;
	
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
		
		if ( l.getType() == l.EDGE ) continue;
		
		if
			( l.snap( 0, ( minY + maxY ) / 2, Double.MAX_VALUE, CoGeometryConstants.LEFT_EDGE_MASK, p, d ) )
		{
			double x = p.getX();
			double y = Math.min( maxY,
				                   usedBottomSpaceAt( x,
					                                    layoutable,
					                                    allAbsoluteSizePageItemsArePlaced,
					                                    false ) );

			if
				( ( ( y != prevY ) || ( x < prevX ) ) && ( isInfiniteY || ( y > minY ) ) )
			{
				profile.addPoint( new Point2D.Double( x, y ) );
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
public static CoProfile getBottomRightLocations( CoImmutableColumnGridIF grid, CoLayoutableIF layoutable )
{
	CoProfile profile = new CoProfile();
	boolean isInfiniteY = layoutable.getLayoutParent().getLayoutSpec().getHeightSpec() instanceof CoContentHeightSpecIF;
	
	Point2D p = new Point2D.Double();
	Point2D d = new Point2D.Double();
	
	double minY = grid.getTopMarginPosition();
	double maxY = grid.getBottomMarginPosition();
	double prevY = Double.NaN;
	double prevX = Double.NaN;
	double minX = Double.MAX_VALUE;

	double w = layoutable.getLayoutWidth();
	
	Iterator e = grid.getGridLines().iterator();
	while
		( e.hasNext() )
	{
		CoGridLineIF l = (CoGridLineIF) e.next();
		
		if ( l.getType() == l.EDGE ) continue;
		
		if
			( l.snap( 0, ( minY + maxY ) / 2, Double.MAX_VALUE, CoGeometryConstants.RIGHT_EDGE_MASK, p, d ) )
		{
			double x = p.getX();
			double y = maxY;

			if
				( ( ( y != prevY ) || ( x > prevX ) ) && ( isInfiniteY || ( y > minY ) ) )
			{
				profile.addPoint( new Point2D.Double( x, y ) );
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
public static CoProfile getBottomRightLocations(  CoLayoutableIF layoutable,
	                                                boolean allAbsoluteSizePageItemsArePlaced )
{
	CoImmutableColumnGridIF grid = layoutable.getLayoutParent().getColumnGrid();
	boolean isInfiniteY = layoutable.getLayoutParent().getLayoutSpec().getHeightSpec() instanceof CoContentHeightSpecIF;
	
	CoProfile profile = new CoProfile();
	
	Point2D p = new Point2D.Double();
	Point2D d = new Point2D.Double();
	
	double minY = grid.getTopMarginPosition();
	double maxY = grid.getBottomMarginPosition();
	double prevY = Double.NaN;
	double prevX = Double.NaN;
	double minX = Double.MAX_VALUE;

	double w = layoutable.getLayoutWidth();
	
	Iterator e = grid.getGridLines().iterator();
	while
		( e.hasNext() )
	{
		CoGridLineIF l = (CoGridLineIF) e.next();
		
		if ( l.getType() == l.EDGE ) continue;
		
		if
			( l.snap( 0, ( minY + maxY ) / 2, Double.MAX_VALUE, CoGeometryConstants.RIGHT_EDGE_MASK, p, d ) )
		{
			double x = p.getX();
			double y = Math.min( maxY,
				                   usedBottomSpaceAt(
					                 x - w,
					                 layoutable,
					                 allAbsoluteSizePageItemsArePlaced,
					                 true ) );

			if
				( ( ( y != prevY ) || ( x > prevX ) ) && ( isInfiniteY || ( y > minY ) ) )
			{
				profile.addPoint( new Point2D.Double( x, y ) );
				if ( x < minX ) minX = x;
				prevY = y;
				prevX = x;
			}
		}
	}

	return profile;
}
/**
 * Svarar med Y för den horisontella linje i mottagaren 
 * som ligger närmast ovanför argumentet.
 */
public static double getFirstHorizontalLineAbove( CoImmutableColumnGridIF grid, double y )
{
	return y;
}
/**
 * Svarar med Y för den horisontella linje i mottagaren 
 * som ligger närmast nedanför argumentet.
 */
public static double getFirstHorizontalLineBelow( CoImmutableColumnGridIF grid, double y )
{
	return y;
}
public static double getHeightFromTopToBottomMargin( CoLayoutableIF pi )
{
	CoImmutableColumnGridIF g = pi.getColumnGrid();
	return g.getBottomMarginPosition() - g.getTopMarginPosition();
}
/**
 * Svarar med leftEdge för den kolumn i mottagaren 
 * som ligger närmast till höger om argumentet.
 */
public static double getLeftEdgeForTheFirstColumnToTheRightOf( CoImmutableColumnGridIF grid, double x)
{
	return grid.snap( x, ( grid.getTopMarginPosition() + grid.getBottomMarginPosition() ) / 2, Double.MAX_VALUE, CoGeometryConstants.LEFT_EDGE_MASK, CoGeometryConstants.TO_RIGHT_MASK, null ).getX();
}
/**
 * Svarar med rightEdge för den kolumn i mottagaren 
 * som ligger närmast till vänster om argumentet.
 */
public static double getRightEdgeForTheFirstColumnToTheLeftOf(CoImmutableColumnGridIF grid, double x)
{
	return grid.snap( x, ( grid.getTopMarginPosition() + grid.getBottomMarginPosition() ) / 2, Double.MAX_VALUE, CoGeometryConstants.RIGHT_EDGE_MASK, CoGeometryConstants.TO_LEFT_MASK, null ).getX();
}
/**
 * Skapar en lista med placeringspunkter mot det övre vänstra hörnet.
 */
public static CoProfile getTopLeftLocations( CoImmutableColumnGridIF grid, CoLayoutableIF layoutable )
{
	CoProfile profile = new CoProfile();
	boolean isInfiniteY = layoutable.getLayoutParent().getLayoutSpec().getHeightSpec() instanceof CoContentHeightSpecIF;
	
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
		
		if ( l.getType() == l.EDGE ) continue;
		
		if
			( l.snap( 0, ( minY + maxY ) / 2, Double.MAX_VALUE, CoGeometryConstants.LEFT_EDGE_MASK, p, d ) )
		{
			double x = p.getX();
			double y = minY;

			if
				( ( ( y != prevY ) || ( x < prevX ) ) && ( isInfiniteY || ( y < maxY ) ) )
			{
				profile.addPoint( new Point2D.Double( x, y ) );
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
public static CoProfile getTopLeftLocations( CoLayoutableIF layoutable,
	                                           boolean allAbsoluteSizePageItemsArePlaced )
{
	CoImmutableColumnGridIF grid = layoutable.getLayoutParent().getColumnGrid();
	boolean isInfiniteY = layoutable.getLayoutParent().getLayoutSpec().getHeightSpec() instanceof CoContentHeightSpecIF;
	
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
		
		if ( l.getType() == l.EDGE ) continue;
		
		if
			( l.snap( 0, ( minY + maxY ) / 2, Double.MAX_VALUE, CoGeometryConstants.LEFT_EDGE_MASK, p, d ) )
		{
			double x = p.getX();
			double y = Math.max( minY, 
				                   usedTopSpaceAt( x, 
					                                 layoutable,
					                                 allAbsoluteSizePageItemsArePlaced,
					                                 false ) );

			if
				( ( ( y != prevY ) || ( x < prevX ) ) && ( isInfiniteY || ( y < maxY ) ) )
			{
				profile.addPoint( new Point2D.Double( x, y ) );
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
public static CoProfile getTopRightLocations( CoImmutableColumnGridIF grid, CoLayoutableIF layoutable )
{
	CoProfile profile = new CoProfile();
	boolean isInfiniteY = layoutable.getLayoutParent().getLayoutSpec().getHeightSpec() instanceof CoContentHeightSpecIF;
	
	Point2D p = new Point2D.Double();
	Point2D d = new Point2D.Double();
	
	double minY = grid.getTopMarginPosition();
	double maxY = grid.getBottomMarginPosition();
	double prevY = Double.NaN;
	double prevX = Double.NaN;
	double minX = Double.MAX_VALUE;

	double w = layoutable.getLayoutWidth();

	Iterator e = grid.getGridLines().iterator();
	while
		( e.hasNext() )
	{
		CoGridLineIF l = (CoGridLineIF) e.next();
		
		if ( l.getType() == l.EDGE ) continue;
		
		if
			( l.snap( 0, ( minY + maxY ) / 2, Double.MAX_VALUE, CoGeometryConstants.RIGHT_EDGE_MASK, p, d ) )
		{
			double x = p.getX();
			double y = minY;

			if
				( ( ( y != prevY ) || ( x > prevX ) ) && ( isInfiniteY || ( y < maxY ) ) )
			{
				profile.addPoint( new Point2D.Double( x, y ) );
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
public static CoProfile getTopRightLocations( CoLayoutableIF layoutable, boolean allAbsoluteSizePageItemsArePlaced )
{
	CoImmutableColumnGridIF grid = layoutable.getLayoutParent().getColumnGrid();
	boolean isInfiniteY = layoutable.getLayoutParent().getLayoutSpec().getHeightSpec() instanceof CoContentHeightSpecIF;
	
	CoProfile profile = new CoProfile();
	
	Point2D p = new Point2D.Double();
	Point2D d = new Point2D.Double();
	
	double minY = grid.getTopMarginPosition();
	double maxY = grid.getBottomMarginPosition();
	double prevY = Double.NaN;
	double prevX = Double.NaN;
	double minX = Double.MAX_VALUE;

	double w = layoutable.getLayoutWidth();

	Iterator e = grid.getGridLines().iterator();
	while
		( e.hasNext() )
	{
		CoGridLineIF l = (CoGridLineIF) e.next();
		
		if ( l.getType() == l.EDGE ) continue;
		
		if
			( l.snap( 0, ( minY + maxY ) / 2, Double.MAX_VALUE, CoGeometryConstants.RIGHT_EDGE_MASK, p, d ) )
		{
			double x = p.getX();
			double y = Math.max( minY, usedTopSpaceAt( x - w,
				                                         layoutable,
				                                         allAbsoluteSizePageItemsArePlaced,
				                                         true ) );

			if
				( ( ( y != prevY ) || ( x > prevX ) ) && ( isInfiniteY || ( y < maxY ) ) )
			{
				profile.addPoint( new Point2D.Double( x, y ) );
				if ( x < minX ) minX = x;
				prevY = y;
				prevX = x;
			}
		}
	}

	return profile;
}
public static double getWidthFromLeftToRightMargin( CoLayoutableIF pi )
{
	CoImmutableColumnGridIF g = pi.getColumnGrid();
	return g.getRightMarginPosition() - g.getLeftMarginPosition();
}
/*
 * Svarar sant om pageItem hamnar under mottagaren om man trycker upp
 * pageItem mot ett tänkt tak där mottagaren redan är placerad.
 */
public static boolean isAnObstructionAbove( CoLayoutableIF obstructed, CoLayoutableIF obstructor )
{
	if ( obstructed.getDoRunAround() ) return false;
	
	return
		(
			(
				( containsX( obstructed, obstructor.getLeftEdge(), obstructor.getLayoutWidth() ) )
			|| 
				( containsX( obstructor, obstructed.getLeftEdge(), obstructed.getLayoutWidth() ) ) // redunant ?
			)
		&&
			( 
				( obstructed.getBottomEdge() < obstructor.getTopEdge() )
			||
				( areEqual( obstructed.getBottomEdge(), obstructor.getTopEdge() ) )
			)
		);
}
/*
 * Svarar sant om pageItem hamnar uppe på mottagaren. 
 * då man placerar pageItem mot en tänkt botten där mottagaren
 * redan är placerad.
 */
public static boolean isAnObstructionBelow( CoLayoutableIF obstructed, CoLayoutableIF obstructor )
{
	if ( obstructed.getDoRunAround() ) return false;
	
	return
		(
			(
				( containsX( obstructed, obstructor.getLeftEdge(), obstructor.getLayoutWidth() ) )
			|| 
				( containsX( obstructor, obstructed.getLeftEdge(), obstructed.getLayoutWidth() ) ) // redunant ?
			)
		&&
			( 
				( obstructed.getTopEdge() > obstructor.getBottomEdge() )
			||
				( areEqual( obstructed.getTopEdge(), obstructor.getBottomEdge() ) )
			)
		);
}
/*
 * Svarar sant om pageItem hamnar till höger om mottagaren om man placerar
 * pageItem mot en tänkt vänstersida där mottagaren redan är placerad.
 */
public static boolean isAnObstructionToTheLeftOf( CoLayoutableIF obstructed, CoLayoutableIF obstructor )
{
	return
		(
			( 
				( containsY( obstructed, obstructor.getTopEdge() ) )
			||
				( containsY( obstructed, obstructor.getBottomEdge() ) )
			|| 
				( containsY( obstructor, obstructed.getTopEdge() ) )
			)
		&&
			( 
				( obstructed.getRightEdge() < obstructor.getLeftEdge() )
			||
				( areEqual( obstructed.getRightEdge(), obstructor.getLeftEdge() ) )
			)
		);
}
/*
 * Svarar sant om pageItem hamnar till vänster om mottagaren. 
 * då man placerar pageItem mot en tänkt högersida där mottagaren
 * redan är placerad.
 */
public static boolean isAnObstructionToTheRightOf( CoLayoutableIF obstructed, CoLayoutableIF obstructor )
{
	return
		(
			( 
				( containsY( obstructed, obstructor.getTopEdge() ) )
			||
				( containsY( obstructed, obstructor.getBottomEdge() ) )
			|| 
				( containsY( obstructor, obstructed.getTopEdge() ) )
			)
		&&
			( 
			 	( obstructed.getLeftEdge() > obstructor.getRightEdge() )
			||
				( areEqual( obstructed.getLeftEdge(), obstructor.getRightEdge() ) )
			)
		);
}
/*
 * Returnera lägsta y-koordinat för översidan bland de
 * sidelement som genomskärs av en vertikal linje vid x.
 * Om en grid skickas med avser svaret Y-koordinaten för
 * den baseline som ligger närmast ovanför överkanten på
 * det översta sidelement som linjen skär igenom .
 */
public static double usedBottomSpaceAt( double x, CoLayoutableIF layoutable, boolean allAbsoluteSizePageItemsArePlaced, boolean ignoreRunAround )
{
	double y = layoutable.getLayoutParent().getLayoutHeight();
	
	CoImmutableColumnGridIF grid = layoutable.getLayoutParent().getColumnGrid();

	double w = ( ! ignoreRunAround && layoutable.getDoRunAround() ) ? 0 : layoutable.getLayoutWidth();
	
	Iterator e = layoutable.getLayoutParent().getLayoutChildren().iterator();
	while
		(e.hasNext())
	{
		CoLayoutableIF pi = (CoLayoutableIF) e.next();
		double top = pi.getTopEdge();
		
		if ( grid != null ) top = getFirstHorizontalLineAbove( grid, top );
			
		if
			(
				( top < y )
			&&
				( containsX( pi, x, w ) )
			&&
				( usesBottomSpace( pi, layoutable, allAbsoluteSizePageItemsArePlaced ) )
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
public static double usedTopSpaceAt( double x, CoLayoutableIF layoutable, boolean allAbsoluteSizePageItemsArePlaced, boolean ignoreRunAround )
{
	double tUsedTopSpace = 0;
	CoImmutableColumnGridIF grid = layoutable.getLayoutParent().getColumnGrid();
		
	double w = ( ! ignoreRunAround && layoutable.getDoRunAround() ) ? 0 : layoutable.getLayoutWidth();

	Iterator e = layoutable.getLayoutParent().getLayoutChildren().iterator();
	while (e.hasNext())
	{
		CoLayoutableIF tPageItem = (CoLayoutableIF) e.next();
		double tBottomEdge = tPageItem.getBottomEdge();
		
		if (grid != null)
			tBottomEdge = getFirstHorizontalLineBelow(grid,tBottomEdge);

		if
			(
				containsX( tPageItem, x, w )
			&&
				usesTopSpace( tPageItem, layoutable, allAbsoluteSizePageItemsArePlaced )
			&&
				( tBottomEdge > tUsedTopSpace )
			)
		{
			tUsedTopSpace = tBottomEdge;
		}
	}

	return tUsedTopSpace;
}
/*
 * Svarar sant om 
 *		 mottagaren antingen ligger så nära den nedre kanten  
 *		 att pageItem inte kan placeras nedanför mottagaren
 * eller om
 *		 de sidelement som ligger under mottagaren gör det omöjligt att
 *		 placera pageItem nedanför mottagaren
 */
public static boolean usesBottomSpace( CoLayoutableIF user, CoLayoutableIF layoutable, boolean allAbsoluteSizePageItemsArePlaced)
{
	if ( ! user.hasValidLayout() ) return false;
	
	if ( layoutable == user ) return false;
	if ( allAbsoluteSizePageItemsArePlaced && ( layoutable == user ) ) return false;

	double bottomMarginPos = layoutable.getLayoutParent().getColumnGrid().getBottomMarginPosition();
	
	if ( bottomMarginPos <= user.getBottomEdge() ) return true;
	
	if ( layoutable.getLayoutHeight() > ( bottomMarginPos - user.getBottomEdge() ) ) return true;
	
	return ( (CoLayoutSpec) user.getLayoutSpec() ).usesBottomSpace(layoutable.getLayoutParent());
}
/*
 * Ett sidelement som ligger så nära den vänstra kanten att layoutable inte kan 
 * placeras till vänster om detsamma, svarar sant.
 */
public static boolean usesLeftSpace( CoLayoutableIF user,
	                            CoLayoutableIF layoutable,
	                            boolean allAbsoluteSizePageItemsArePlaced )
{
	if ( ! user.hasValidLayout() ) return false;
	
	if ( layoutable == user ) return false;
	if
		( allAbsoluteSizePageItemsArePlaced )
	{	
		if ( layoutable == user ) return false;
	}
	
	if
		( user.getLeftEdge() - layoutable.getLayoutParent().getColumnGrid().getLeftMarginPosition() <= 0 )
	{
		return true;
	} else {
		if ( layoutable.getLayoutWidth() > ( user.getLeftEdge() - layoutable.getLayoutParent().getColumnGrid().getLeftMarginPosition()) ) return true;
		return ( (CoLayoutSpec) user.getLayoutSpec() ).usesLeftSpace( layoutable.getLayoutParent() );
	}
	
}
/*
 * Ett sidelement som ligger så nära den högra kanten att layoutable inte kan 
 * placeras till höger om detsamma, svarar sant.
 */
public static boolean usesRightSpace(CoLayoutableIF user,
	                            CoLayoutableIF layoutable,
	                            boolean allAbsoluteSizePageItemsArePlaced)
{
	if ( ! user.hasValidLayout() ) return false;
	
	if ( layoutable == user ) return false;
	if
		( allAbsoluteSizePageItemsArePlaced )
	{
		if ( layoutable == user ) return false;
	}
	
	if
		( layoutable.getLayoutParent().getColumnGrid().getRightMarginPosition() - user.getRightEdge() <= 0 )
	{
		return true;
	} else {
		if ( layoutable.getLayoutWidth() > (layoutable.getLayoutParent().getColumnGrid().getRightMarginPosition() - user.getRightEdge() ) ) return true;
		return ( (CoLayoutSpec) user.getLayoutSpec() ).usesRightSpace( layoutable.getLayoutParent() );
	}
}
/*
 * Ett sidelement som ligger så nära den övre kanten att layoutable inte kan 
 * placeras ovanför detsamma, svarar sant.
 */
public static boolean usesTopSpace( CoLayoutableIF user, CoLayoutableIF layoutable, boolean allAbsoluteSizePageItemsArePlaced  )
{
	if ( ! user.hasValidLayout() ) return false;
	
	if ( layoutable == user ) return false;
	if ( allAbsoluteSizePageItemsArePlaced && ( layoutable == user ) ) return false;

	if ( user.getTopEdge() <= layoutable.getLayoutParent().getColumnGrid().getTopMarginPosition() ) return true;
	
	if ( layoutable.getLayoutHeight() > ( user.getTopEdge() - layoutable.getLayoutParent().getColumnGrid().getTopMarginPosition() ) ) return true;

	return ( (CoLayoutSpec) user.getLayoutSpec() ).usesTopSpace( layoutable.getLayoutParent() );

}
}