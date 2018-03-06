package com.bluebrim.layout.impl.server.manager;

import java.util.*;
import java.awt.geom.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.base.shared.geom.*;
import com.bluebrim.layout.impl.server.geom.*;
/**
 * Beskriver den kontur som skapas av sidelement som är
 * placerade mot en av sidorna i en layoutArea. T ex så
 * är konturen av de sidelement som är placerade mot
 * överkanten lika med den samling punkter som utgörs av
 * sidelementens nedre vänster och högerhörn. Alla värden
 * i profilerna är koordinater i en layoutAreas koordinatsystem.
 * 
 */
public class CoProfile implements java.io.Serializable
{
	protected List m_points = new ArrayList();

	protected static final double EQUALITY_TOLERANS = 0.01;
public CoProfile()
{
}
public void addPoint(Point2D point)
{
	m_points.add(point);
}
/**
 * Anropas EFTER det att sidelementet har placerats. Används vid
 * beräkning av sidelementets utsträckning i höjdled.
 */
 // never called
public static CoProfile createBottomProfileForHeightDetermination( CoLayoutableIF layoutable )
{
	// Skapa en profil med överkanten på de sidelement som är placerade
	// i layoutens underkant och som vertikalt "ligger i vägen" för layoutable.
	CoImmutableColumnGridIF grid = layoutable.getLayoutParent().getColumnGrid();
	CoProfile p = new CoProfile();
	
	if
		( ! layoutable.getDoRunAround() )
	{
		Iterator e = layoutable.getLayoutParent().getLayoutChildren().iterator();
		while
			( e.hasNext() )
		{
			CoLayoutableIF l = (CoLayoutableIF) e.next();
			if
				( CoLayoutManagerUtilities.usesBottomSpace( l, layoutable, true ) )
			{
				if
					( CoLayoutManagerUtilities.isAnObstructionBelow( l, layoutable ) )
				{
					double y = CoLayoutManagerUtilities.getFirstHorizontalLineAbove( grid, l.getTopEdge() );
					p.addPoint( new Point2D.Double( 0, y ) );
				}
			}
		}
	}
	
	if
		( p.isEmpty() )
	{
		p.addPoint( new Point2D.Double( 0, grid.getBottomMarginPosition() ) );
	}
	
	return p;
}
/**
 * Anropas EFTER det att sidelementet har placerats. Används vid
 * beräkning av sidelementets utsträckning i sidled då pageItem
 * är förankrad i sin högersida.
 */
public static CoProfile createLeftProfileForWidthDetermination ( CoLayoutableIF layoutable  ) { // never called


	// Skapa en profil med högerkanten på de sidelement som är placerade
	// i layoutens vänsterkant och som horisontellt "ligger i vägen" för layoutable. 
	CoImmutableColumnGridIF grid = (CoColumnGrid) layoutable.getLayoutParent().getColumnGrid();
	
	CoProfile p = new CoProfile();
	if
		( ! layoutable.getDoRunAround() )
	{
		Iterator e = layoutable.getLayoutParent().getLayoutChildren().iterator();
		while
			( e.hasNext() )
		{
			CoLayoutableIF l = (CoLayoutableIF)e.next();
			if
				( CoLayoutManagerUtilities.usesLeftSpace( l, layoutable, true ) )
			{
				if
					( CoLayoutManagerUtilities.isAnObstructionToTheLeftOf( l, layoutable ) )
				{
					double x = CoLayoutManagerUtilities.getLeftEdgeForTheFirstColumnToTheRightOf( grid, l.getRightEdge() );
					p.addPoint( new Point2D.Double( x, 0 ) );
				}
			}
		}
	}
	
	if
		( p.isEmpty() )
	{	
		p.addPoint( new Point2D.Double( grid.getLeftMarginPosition(), 0 ) );
	}
	
	return p;

}
/**
 * Anropas EFTER det att sidelementet har placerats. Används vid
 * beräkning av sidelementets utsträckning i sidled då pageItem
 * är förankrad i sin vänstersida.
 */
public static CoProfile createRightProfileForWidthDetermination( CoLayoutableIF layoutable ) // never called

{
	// Skapa en profil med övre vänstra hörnen på de sidelement som är placerade
	// i layoutens högerkant och som horisontellt "ligger i vägen" för layoutable.
	CoImmutableColumnGridIF grid = layoutable.getLayoutParent().getColumnGrid();
	CoProfile p = new CoProfile();

	if
		( ! layoutable.getDoRunAround() )
	{
		Iterator e = layoutable.getLayoutParent().getLayoutChildren().iterator();
		while
			( e.hasNext() )
		{
			CoLayoutableIF l = (CoLayoutableIF) e.next();
			if
				( CoLayoutManagerUtilities.usesRightSpace( l, layoutable, true ) )
			{
				if
					( CoLayoutManagerUtilities.isAnObstructionToTheRightOf( l, layoutable ) )
				{
					double x = CoLayoutManagerUtilities.getRightEdgeForTheFirstColumnToTheLeftOf( grid, l.getLeftEdge() );
					p.addPoint( new Point2D.Double( x, 0 ) );
				}
			}
		}
	}
	
	if
		( p.isEmpty() )
	{
		p.addPoint( new Point2D.Double( grid.getRightMarginPosition(), 0 ) );
	}
	
	return p;
}
/**
 * Anropas EFTER det att sidelementet har placerats. Används vid
 * beräkning av sidelementets utsträckning i höjdled.
 */
public static CoProfile createTopProfileForHeightDetermination(CoLayoutableIF layoutable) // never called

{
	// Skapa en profil med nedre högra hörnen på de sidelement som är placerade
	// i layoutens överkant och som vertikalt "ligger i vägen" för layoutable.
	
	CoImmutableColumnGridIF grid = (CoColumnGrid) layoutable.getLayoutParent().getColumnGrid();
	CoProfile p = new CoProfile();
	
	if
		( ! layoutable.getDoRunAround() )
	{
		Iterator e = layoutable.getLayoutParent().getLayoutChildren().iterator();
		while
			( e.hasNext() )
		{
			CoLayoutableIF l = (CoLayoutableIF) e.next();
			if
				( CoLayoutManagerUtilities.usesTopSpace( l, layoutable, true ) )
			{
				if
					( CoLayoutManagerUtilities.isAnObstructionAbove( l, layoutable ) )
				{
					double y = CoLayoutManagerUtilities.getFirstHorizontalLineBelow( grid, l.getBottomEdge() );
					p.addPoint( new Point2D.Double( 0, y ) );
				}
			}
		}
	}
	
	if
		( p.isEmpty() )
	{
		p.addPoint( new Point2D.Double( 0, grid.getTopMarginPosition() ) );
	}
	
	return p;
}
/**
 * Returnerar den av mottagarens punkter 
 * som ligger närmast det övre vänstra hörnet.
 */
public Point2D getClosestTo( Point2D referencePoint )
{
	Point2D best = null;
	
	double D = Double.MAX_VALUE;
	
	Iterator e = m_points.iterator();
	if
		( e.hasNext() )
	{
		best = (Point2D) e.next();
		D = referencePoint.distance( best );
	}

	while
		( e.hasNext() )
	{
		Point2D p = (Point2D) e.next();
		double d = referencePoint.distance( p );
		if
			( d + EQUALITY_TOLERANS <= D )
		{
			D = d;
			best = p;
		}
	};

	return best;
}
/**
 * Returnerar den av mottagarens punkter 
 * som ligger närmast det övre vänstra hörnet.
 */
public Point2D getClosestToBottomLeft(CoLayoutableIF layoutable)
{
	Point2D left = layoutable.getLayoutParent().getColumnGrid().snap( 0, 0, Double.MAX_VALUE, CoGeometryConstants.LEFT_EDGE_MASK, CoGeometryConstants.ALL_DIRECTIONS_MASK, false, null );
	Point2D bottom = layoutable.getLayoutParent().getColumnGrid().snap( 0, layoutable.getLayoutParent().getLayoutHeight(), Double.MAX_VALUE, CoGeometryConstants.BOTTOM_EDGE_MASK, CoGeometryConstants.ALL_DIRECTIONS_MASK, false, null );
	return getClosestTo( new Point2D.Double( left.getX(), bottom.getY() ) );
}
/**
 * Returnerar den av mottagarens punkter 
 * som ligger närmast det övre vänstra hörnet.
 */
public Point2D getClosestToBottomRight(CoLayoutableIF layoutable)
{
	Point2D right = layoutable.getLayoutParent().getColumnGrid().snap( layoutable.getLayoutParent().getLayoutWidth(), 0, Double.MAX_VALUE, CoGeometryConstants.RIGHT_EDGE_MASK, CoGeometryConstants.ALL_DIRECTIONS_MASK, false, null );
	Point2D bottom = layoutable.getLayoutParent().getColumnGrid().snap( 0, layoutable.getLayoutParent().getLayoutHeight(), Double.MAX_VALUE, CoGeometryConstants.BOTTOM_EDGE_MASK, CoGeometryConstants.ALL_DIRECTIONS_MASK, false, null );
	return getClosestTo( new Point2D.Double( right.getX(), bottom.getY() ) );
}
/**
 * Returnerar den av mottagarens punkter 
 * som ligger närmast det övre vänstra hörnet.
 */
public Point2D getClosestToTopLeft(CoLayoutableIF layoutable)
{
	Point2D left = layoutable.getLayoutParent().getColumnGrid().snap( 0, 0, Double.MAX_VALUE, CoGeometryConstants.LEFT_EDGE_MASK, CoGeometryConstants.ALL_DIRECTIONS_MASK, false, null );
	Point2D top = layoutable.getLayoutParent().getColumnGrid().snap( 0, 0, Double.MAX_VALUE, CoGeometryConstants.TOP_EDGE_MASK, CoGeometryConstants.ALL_DIRECTIONS_MASK, false, null );
	return getClosestTo( new Point2D.Double( left.getX(), top.getY() ) );
}
/**
 * Returnerar den av mottagarens punkter 
 * som ligger närmast det övre vänstra hörnet.
 */
public Point2D getClosestToTopRight(CoLayoutableIF layoutable)
{
	Point2D right = layoutable.getLayoutParent().getColumnGrid().snap( layoutable.getLayoutParent().getLayoutWidth(), 0, Double.MAX_VALUE, CoGeometryConstants.RIGHT_EDGE_MASK, CoGeometryConstants.ALL_DIRECTIONS_MASK, false, null );
	Point2D top = layoutable.getLayoutParent().getColumnGrid().snap( 0, 0, Double.MAX_VALUE, CoGeometryConstants.TOP_EDGE_MASK, CoGeometryConstants.ALL_DIRECTIONS_MASK, false, null );
	return getClosestTo( new Point2D.Double( right.getX(), top.getY() ) );
}
public Iterator getElements()
{
	return m_points.iterator();
}
public Point2D getFirstElement ( ) {
	return (Point2D)m_points.get(0);
}
/**
 * Returnerar det högsta X-värdet bland mottagarens punkter
 */
public double getHighestX ( ) {
	double x = 0;
	Iterator e = m_points.iterator();
	while (e.hasNext())
	{
		x = Math.max(x,((Point2D)e.next()).getX()); 
			
	};	
	
	return x;
}
/**
 * Returnerar det högsta Y-värdet bland mottagarens punkter
 */
public double getHighestY ( ) {
	double y = 0;
	Iterator e = m_points.iterator();
	while (e.hasNext())
	{
		y = Math.max(y,((Point2D)e.next()).getY()); 
			
	};	
	
	return y;
}
/**
 * Returnerar det lägsta X-värdet bland mottagarens punkter
 */
public double getLowestX ( ) {
	double x = getHighestX();
	Iterator e = m_points.iterator();
	while (e.hasNext())
	{
		x = Math.min(x,((Point2D)e.next()).getX()); 
			
	};	
	
	return x;
}
/**
 * Returnerar det lägsta Y-värdet bland mottagarens punkter
 */
public double getLowestY ( ) {
	double y = getHighestY();
	Iterator e = m_points.iterator();
	while (e.hasNext())
	{
		y = Math.min(y,((Point2D)e.next()).getY()); 
			
	};	
	
	return y;
}
/**
 * Eftersom vi arbetar med flyttal måste vi tillåta viss tolerans.
 * Eventuellt kommer denna klass att ändras så att den arbetar
 * med CoDistancePoint i stället och då försvinner detta problem
 * eftersom CoDistancePoint kan implementera likhet för de subklasser
 * uttrycker antal kolumner, rader etc. 
 */
public static boolean hasSameX ( Point2D p1, Point2D p2) {
	return (Math.abs(p1.getX() - p2.getX()) < EQUALITY_TOLERANS);
}
/**
 * Eftersom vi arbetar med flyttal måste vi tillåta viss tolerans.
 * Eventuellt kommer denna klass att ändras så att den arbetar
 * med CoDistancePoint i stället och då försvinner detta problem
 * eftersom CoDistancePoint kan implementera likhet för de subklasser
 * uttrycker antal kolumner, rader etc. 
 */
public static boolean hasSameY ( Point2D p1, Point2D p2) {
	return (Math.abs(p1.getY() - p2.getY()) < EQUALITY_TOLERANS);
}
public boolean isEmpty ( ) {
	return m_points.isEmpty();
}
public void removePoint ( Point2D point) {
	m_points.remove(point);
}
public String toString (){

	String tString = super.toString() + "\n";
	Iterator e = m_points.iterator();
	while
		(e.hasNext())
	{
		Point2D tPoint = (Point2D)e.next();
		tString = new String (tString+"X = " + tPoint.getX() + "   Y = " + tPoint.getY() + "\n");
	}
	return tString;
}
}
