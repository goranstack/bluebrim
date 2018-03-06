package com.bluebrim.base.shared.geom;

import java.awt.geom.*;
import java.util.*;

/**
 * By Helena Rankegård
 * Creates CoTrapets:es from the points given by a PathIterator.
 *
 * NOTE: The clockwise notion used here is based on a left hand system
 * (which from a mathematical standpoint is unusual). /Markus
 *
 * @see #getTrapetses(PathIterator pathIterator, Rectangle2D.Float bounds)
 */
 
public class CoTrapetsesBuilder
{
	private static boolean m_pathIteratorIsClockwise = testPathIteratorDirection();
protected static float getNextY (float startingY, float startingX, float x2, int i1, int i3, List points) {
	CoTrapetsPoint p = (CoTrapetsPoint)points.get(i3);
	float y = p.m_y;
	
	for (int i = i1; i != i3; i = (i + 1) % points.size()) {
		p = (CoTrapetsPoint)points.get(i);
		if (p.m_y > startingY && p.m_y < y)
			y = p.m_y;
		else if (p.m_y == startingY && p.m_x > startingX && p.m_x < x2)
			y = p.m_y;
	}

	return y;
}
// gets the points from moveto to next moveto or close
// note: first segment type is always SEG_MOVETO
protected static List getPoints (PathIterator pathIterator, float x, float y )
{
	List points =
		m_pathIteratorIsClockwise ?
		new ArrayList() :
		new ArrayList()
		{
			public boolean add( Object o ) { add( 0, o ); return true; }
		};
	
	float[] arr = new float[6];
	boolean goOn = ! pathIterator.isDone();
	boolean firstLoop = true;
	
	while
		( goOn && ! pathIterator.isDone() )
	{
		int type = pathIterator.currentSegment( arr );
		switch
			( type )
		{
			case PathIterator.SEG_MOVETO:
				if
					( ! firstLoop )
				{
					goOn = false;
					break;
				} else {
					// fall through
				}
				
			case PathIterator.SEG_LINETO:
				CoTrapetsPoint p = new CoTrapetsPoint( arr[0] - x, arr[1] - y );
				points.add( p );
				break;
				/*
			case PathIterator.SEG_QUADTO:		
				points.add(new CoTrapetsPoint(arr[0] - x, arr[1] - y));
				points.add(new CoTrapetsPoint(arr[2] - x, arr[3] - y));
				break;
			case PathIterator.SEG_CUBICTO:					
				points.add(new CoTrapetsPoint(arr[0] - x, arr[1] - y));
				points.add(new CoTrapetsPoint(arr[2] - x, arr[3] - y));
				points.add(new CoTrapetsPoint(arr[4] - x, arr[5] - y));
				break;
				*/
			case PathIterator.SEG_CLOSE:					
				pathIterator.next();
				goOn = false;
				break;
		}
		
		if ( goOn ) pathIterator.next();
		firstLoop = false;
	}

	// see if first and last points are the same, if so remove the last point
	if (!points.isEmpty() && ((CoTrapetsPoint)points.get( 0 )).equals((CoTrapetsPoint)points.get( points.size()-1)))
		points.remove(points.size() - 1);
	
	return points;
}
protected static int getStartingPoint (List points) {
	int i = -1;

	if (points.size() > 0) {
		// let the first point be the best point so far
		i = 0;
		CoTrapetsPoint bestP = (CoTrapetsPoint)points.get(0);
		
		for (int k = 1; k < points.size(); k++) {
			CoTrapetsPoint p = (CoTrapetsPoint)points.get(k);
			if (p.betterStartingPointThan(bestP)) {
				// found a better point, replace old with this new one
				bestP = p;
				i = k;
			}
		}
	}
		
	return i;
}
/*
 * Creates and stores instanses of CoTrapets in a GsVectorIF
 * Returns the CoSillyList with all trapetses.
 * The points in pathIterator are adjusted accordingly to the input bounds.
 */
 
public static List getTrapetses (PathIterator pathIterator, Rectangle2D.Float bounds)
{
	List allTrapetses = new ArrayList();
	int n = 0;
	while
		( ! pathIterator.isDone() )
	{
		// get points from seg_moveto to another seg_moveto or close
		List points = getPoints( pathIterator, bounds.x, bounds.y );

		if
			( points != null && ! points.isEmpty() )
		{
			while ( getTrapetses( points, allTrapetses ) );
		}
	}
	
	// all points have been read and trapetses have been created from these points
	// see if some of trapetses can be merged together
	mergeTrapetsesIfPossible( allTrapetses );

	return allTrapetses;
}
// asumes that the points are stored in a clockwise direction

protected static boolean getTrapetses( List points, List allTrapetses )
{
	boolean moreTrapetsesToBeFound = true;

	if
		( points.size() < 3 )
	{
		// no useful trapets can be constructed
		points.clear();
		moreTrapetsesToBeFound = false;
		
	} else {
		// find the point with the smallest y and if needed also the smallest x
		int i = getStartingPoint(points);
		CoTrapetsPoint p = (CoTrapetsPoint)points.get(i);
		
		// start comparing startpoint with point i + 1 (higher i)
		int i1= (i + 1) % points.size();
		CoTrapetsPoint p1 = (CoTrapetsPoint)points.get(i1); // get next
		CoTrapetsPoint p2 = p;
		while
			(i != i1 && p.m_y == p1.m_y)
		{
			i1 = (i1 + 1) % points.size();
			p2 = p1; // save the last point with the same y as p
			p1 = (CoTrapetsPoint)points.get(i1); // get next
		}
		if
			(i == i1)
		{
			// all points have same y, ie the points forms a line, no useful trapets
			points.clear();
			return false; // no more trapetses to be found
		}
		
		// now, get point i - 1 (lower i)
		int i3 = (i == 0) ? points.size() - 1 :  i - 1;
		CoTrapetsPoint p3 = (CoTrapetsPoint)points.get(i3); // get next

		// get the smallest y but bigger than or equal to y of the starting point
		float y = getNextY(p.m_y, p.m_x, p2.m_x, i1, i3, points);

		if
			(y == p.m_y)
		{
			// there exists one or more points with the same y as the starting point
			// and with an x between p.x and p2.x among the remaining points, 
			// ie must split the points in two or more vectors and find each separate trapetses
			List v = new ArrayList();
			while
				(points.size() > 1)
			{
				v.add(new CoTrapetsPoint(p));
				do {
					points.remove(i);
					if ( points.size() == 0 ) break;
					i = (i == 0) ? points.size() - 1 : i - 1;
					p = (CoTrapetsPoint)points.get(i);
					v.add(1,new CoTrapetsPoint(p));
				}
					while (p.m_y != y && points.size() > 0 );
				
				while (getTrapetses (v, allTrapetses)) {}
			}
			points.clear();
			moreTrapetsesToBeFound = false;
			
		} else if
			(p1.m_y == p3.m_y && y == p1.m_y)
		{
			// make a trapets of p, p2, p1 and p3 and store it in allTrapetses
			allTrapetses.add(new CoTrapets(p, p2, p1, p3));
			// remove p and p2 (may be the same point)
			if
				(i < i1)
			{
				while
					(i != i1)
				{
					points.remove(i);
					i1--;
				}
			} else {
				i1 += points.size();
				while
					(i != i1)
				{
					if
						( i >= points.size())
					{
						points.remove(0);
					} else {
						points.remove(i);
					}
					i1--;
				}
			}
			
		} else if
			(p1.m_y < p3.m_y && p1.m_y == y)
		{
			// make a trapets of p, p2, p1 and tp and store it in allTrapetses
			float x = p.m_x + (p1.m_y - p.m_y) * (p3.m_x - p.m_x) / (p3.m_y - p.m_y);
			CoTrapetsPoint tp = new CoTrapetsPoint(x, p1.m_y);
			allTrapetses.add(new CoTrapets(p, p2, p1, tp));
			// remove p and p2 (may be the same point)
			if
				(i < i1)
			{
				while
					(i != i1)
				{
					points.remove(i);
					i1--;
				}
			} else {
				i1 += points.size();
				while
					(i != i1)
				{
					if
						( i >= points.size())
					{
						points.remove(0);
					} else {
						points.remove(i);
					}
					i1--;
				}
			}
			// add tp at position i
			if
				( i >= points.size())
			{
				points.add(0,tp);
			} else {
				points.add(i,tp);
			}
			
		} else if
			(p1.m_y > p3.m_y && p3.m_y == y)
		{
			// make a trapets of p, p2, tp and p3 and store it in allTrapetses
			float x = p2.m_x + (p3.m_y - p2.m_y) * (p1.m_x - p2.m_x) / (p1.m_y - p2.m_y);
			CoTrapetsPoint tp = new CoTrapetsPoint(x, p3.m_y);
			allTrapetses.add(new CoTrapets(p, p2, tp, p3));
			// remove p and p2 (may be the same point (in between)
			if
				(i < i1)
			{
				while
					(i != i1)
				{
					points.remove(i);
					i1--;
				}
			} else {
				i1 += points.size();
				while
					(i != i1)
				{
					if 
						( i >= points.size())
					{
						points.remove(0);
					} else {
						points.remove(i);
					}
					i1--;
				}
			}
			// add tp at position i
			if ( i >= points.size())
				points.add(0,tp);
			else
				points.add(i,tp);
				
		} else {
			// make a trapets of p, p2, (x1, y) and (x3, y) and store it in allTrapetses
			float x1 = p2.m_x + (y - p2.m_y) * (p1.m_x - p2.m_x) / (p1.m_y - p2.m_y);
			float x3 = p.m_x + (y - p.m_y) * (p3.m_x - p.m_x) / (p3.m_y - p.m_y);			
			CoTrapetsPoint tp1 = new CoTrapetsPoint(x1, y);
			CoTrapetsPoint tp3 = new CoTrapetsPoint(x3, y);
			allTrapetses.add(new CoTrapets(p, p2, tp1, tp3));
			// remove p and p2 (may be the same point (in between)
			if (i < i1) {
				while (i != i1) {
					points.remove(i);
					i1--;
				}
			} else {
				i1 += points.size();
				while (i != i1) {
					if ( i >= points.size())
						points.remove(0);
					else
						points.remove(i);
					i1--;
				}
			}
			// add tp1 and tp3 at position i
			if
				(i >= points.size())
			{
				points.add(0,tp1);
				points.add(0,tp3);
			} else {
				points.add(i,tp1);
				points.add(i,tp3);
			}
		}
	}

	return moreTrapetsesToBeFound;
}
protected static void mergeTrapetsesIfPossible( List allTrapetses )
{
	for
		( int i = 0; i < allTrapetses.size(); i++ )
	{
		CoTrapets trapets = (CoTrapets) allTrapetses.get( i );

		// check if trapets can be included into another trapets
		boolean isPossible = true;
		for
			( int j = 0; isPossible && j < allTrapetses.size(); j++ )
		{
			if ( i == j ) continue;
			
			CoTrapets trapets1 = (CoTrapets) allTrapetses.get( j );
			// the trapetses can not have the same y in their starting points
			// if so they lie beside each other and form two separate parts
			isPossible = trapets.m_p1.m_y != trapets1.m_p1.m_y;
		}
		
		if ( ! isPossible ) continue;
		
		for
			( int j = 0; j < allTrapetses.size(); j++ )
		{
			if ( i == j ) continue;
			
			CoTrapets trapets1 = (CoTrapets) allTrapetses.get( j );
			if
				( trapets.include( trapets1 ) )
			{
				// is included, result stored in this
				allTrapetses.remove( trapets1 );
				// start all over
				j = 0;
			}
		}
	}
}
private static boolean testPathIteratorDirection()
{
	PathIterator i = ( new Area( new Rectangle2D.Double( 0, 0, 10, 10 ) ) ).getPathIterator( new AffineTransform() );
	
	double [] d = new double [ 6 ];
	i.currentSegment( d );
	double x0 = d[ 0 ];
	double y0 = d[ 1 ];
	
	i.next();
	i.currentSegment( d );
	double x1 = d[ 0 ];
	double y1 = d[ 1 ];

	if ( x0 == 0 && y0 == 0 ) return ( x1 == 10 && y1 == 0 );
	if ( x0 == 10 && y0 == 0 ) return ( x1 == 10 && y1 == 10 );
	if ( x0 == 10 && y0 == 10 ) return ( x1 == 0 && y1 == 10 );
	if ( x0 == 0 && y0 == 10 ) return ( x1 == 0 && y1 == 0 );
	
	return true;
}
}
