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
 * Svarar sant om en vertikal linje vid X sk�r genom mottagaren.
 * Till�ter en viss (0.1) tolerans.
 */
public static boolean containsX( CoLayoutableIF pi, double x, double w )
{
	if ( x + w + EQUALITY_TOLERANS < pi.getLeftEdge() ) return false;
	if ( x - EQUALITY_TOLERANS > pi.getRightEdge() ) return false;
	return true;
}
/**
 *	Svarar sant om en horisontell linje vid Y sk�r genom mottagaren
 *
 *	Anm 1
 *	Anta att Y �r lika med mottagarens underkant.
 *	Om mottagaren �r placerad mot ett av de �vre h�rnen eller mot �verkanten ska linjen
 *	inte anses sk�ra genom mottagarens underkant.
 *	Detta f�r att man d� vill hitta det X i mottagarens underkant som ligger l�ngst 
 *	fr�n f�r�lderns (layoutAreans) mitt.
 *	Undantag: D� radst�dradraster anv�nds eller d� underkanten ligger p� den undre marginalen 
 *	ska mottagaren alltid anses inneh�lla Y. 
 *
 *	Anm 2
 *	Anta att Y �r lika med mottagarens �verkant.
 *	Om mottagaren �r placerad mot ett av de nedre h�rnen eller mot nederkanten ska linjen
 *	inte anses sk�ra genom mottagarens �verkant.
 *	Detta f�r att man d� vill hitta det X i mottagarens �verkant som ligger l�ngst 
 *	fr�n f�r�lderns (layoutAreans) mitt.
 *	Undantag: D� radst�dradraster anv�nds eller d� underkanten ligger p� den �vre marginalen 
 *	ska mottagaren alltid anses inneh�lla Y. 
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
 * 	Skapar en lista med placeringspunkter mot det nedre v�nstra h�rnet.
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
 * 	Skapar en lista med placeringspunkter mot det nedre v�nstra h�rnet.
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
 * Skapar en lista med placeringspunkter mot det nedre h�gra h�rnet.
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
 * Skapar en lista med placeringspunkter mot det nedre h�gra h�rnet.
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
 * Svarar med Y f�r den horisontella linje i mottagaren 
 * som ligger n�rmast ovanf�r argumentet.
 */
public static double getFirstHorizontalLineAbove( CoImmutableColumnGridIF grid, double y )
{
	return y;
}
/**
 * Svarar med Y f�r den horisontella linje i mottagaren 
 * som ligger n�rmast nedanf�r argumentet.
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
 * Svarar med leftEdge f�r den kolumn i mottagaren 
 * som ligger n�rmast till h�ger om argumentet.
 */
public static double getLeftEdgeForTheFirstColumnToTheRightOf( CoImmutableColumnGridIF grid, double x)
{
	return grid.snap( x, ( grid.getTopMarginPosition() + grid.getBottomMarginPosition() ) / 2, Double.MAX_VALUE, CoGeometryConstants.LEFT_EDGE_MASK, CoGeometryConstants.TO_RIGHT_MASK, null ).getX();
}
/**
 * Svarar med rightEdge f�r den kolumn i mottagaren 
 * som ligger n�rmast till v�nster om argumentet.
 */
public static double getRightEdgeForTheFirstColumnToTheLeftOf(CoImmutableColumnGridIF grid, double x)
{
	return grid.snap( x, ( grid.getTopMarginPosition() + grid.getBottomMarginPosition() ) / 2, Double.MAX_VALUE, CoGeometryConstants.RIGHT_EDGE_MASK, CoGeometryConstants.TO_LEFT_MASK, null ).getX();
}
/**
 * Skapar en lista med placeringspunkter mot det �vre v�nstra h�rnet.
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
 * Skapar en lista med placeringspunkter mot det �vre v�nstra h�rnet.
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
 * Skapar en lista med placeringspunkter mot det �vre h�gra h�rnet.
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
 * Skapar en lista med placeringspunkter mot det �vre h�gra h�rnet.
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
 * pageItem mot ett t�nkt tak d�r mottagaren redan �r placerad.
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
 * Svarar sant om pageItem hamnar uppe p� mottagaren. 
 * d� man placerar pageItem mot en t�nkt botten d�r mottagaren
 * redan �r placerad.
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
 * Svarar sant om pageItem hamnar till h�ger om mottagaren om man placerar
 * pageItem mot en t�nkt v�nstersida d�r mottagaren redan �r placerad.
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
 * Svarar sant om pageItem hamnar till v�nster om mottagaren. 
 * d� man placerar pageItem mot en t�nkt h�gersida d�r mottagaren
 * redan �r placerad.
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
 * Returnera l�gsta y-koordinat f�r �versidan bland de
 * sidelement som genomsk�rs av en vertikal linje vid x.
 * Om en grid skickas med avser svaret Y-koordinaten f�r
 * den baseline som ligger n�rmast ovanf�r �verkanten p�
 * det �versta sidelement som linjen sk�r igenom .
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
 * Returnera h�gsta y-koordinat f�r undersidan bland de
 * sidelement som genomsk�rs av en vertikal linje vid x.
 * Om en grid skickas med avser svaret Y-koordinaten f�r
 * den baseline som ligger n�rmast nedanf�r underkanten p�
 * det understa sidelement som x sk�r igenom .
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
 *		 mottagaren antingen ligger s� n�ra den nedre kanten  
 *		 att pageItem inte kan placeras nedanf�r mottagaren
 * eller om
 *		 de sidelement som ligger under mottagaren g�r det om�jligt att
 *		 placera pageItem nedanf�r mottagaren
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
 * Ett sidelement som ligger s� n�ra den v�nstra kanten att layoutable inte kan 
 * placeras till v�nster om detsamma, svarar sant.
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
 * Ett sidelement som ligger s� n�ra den h�gra kanten att layoutable inte kan 
 * placeras till h�ger om detsamma, svarar sant.
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
 * Ett sidelement som ligger s� n�ra den �vre kanten att layoutable inte kan 
 * placeras ovanf�r detsamma, svarar sant.
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