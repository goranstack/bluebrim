package com.bluebrim.layout.impl.server;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bluebrim.base.shared.CoRectangle2DFloat;
import com.bluebrim.base.shared.geom.CoTrapets;
import com.bluebrim.base.shared.geom.CoTrapetsPoint;

/*
 * By Helena Rankegård
 */

public class CoTrapetsesColumn implements com.bluebrim.text.shared.CoColumnGeometryIF.CoColumnIF, java.io.Serializable
{
	public CoTrapetsHolder m_holderTop;
	public CoTrapetsHolder m_holderBottom;
	// the bounds of the column
	public CoRectangle2DFloat m_bounds2D;
	// stores the y there isNarrowing changes from false to true (see setNarrowing)
	public float m_narrowingY = 0;
	// stored the margins set on the column
	public float m_leftMargin = 0;
	public float m_rightMargin = 0;
	
	// markers to use to improve performance
	// a getRange is always called first and only  if
	// isNarrowing returns true getMinimumRange is called
	// by saving the holder and trapets asked for in getRange
	// the performance in getMinimumRange can be improved
	float m_markY = -1;
	int m_markIndex = -1;
	CoTrapetsHolder m_markHolder = null;
	CoTrapets m_markTrapets = null;

	//------------- inner class -------------
	
	public class CoTrapetsHolder implements java.io.Serializable
	{
		// holds trapetses with the same min and max y
		public List m_trapetses = new ArrayList();
		public CoTrapetsHolder m_before = null;
		public CoTrapetsHolder m_next = null;
		public float m_yMin;
		public float m_yMax;
		
		public CoTrapetsHolder (CoTrapets trapets) {
			m_yMin = trapets.getYMin();
			m_yMax = trapets.getYMax();
			add(trapets);
		}

		boolean isEquivalentTo( CoTrapetsHolder c )
		{
			if ( ! closeEnough( m_yMin, c.m_yMin ) ) return false;
			if ( ! closeEnough( m_yMax, c.m_yMax ) ) return false;
			int I = m_trapetses.size();
			int J = c.m_trapetses.size();
			if ( I != J ) return false;
			for
				( int i = 0; i < I; i++ )
			{
				CoTrapets t0 = getTrapets( i );
				CoTrapets t1 = c.getTrapets( i );
				if ( ! t0.isEquivalentTo( t1 ) ) return false;
			}

			return true;
		}

		// insert a trapets (sorted with the trapets with the smallest x first in m_trapetses)
		public void add (CoTrapets trapets) {
			boolean stop = false;
			int i = 0;
			while (!stop &&  i < m_trapetses.size()) {
				if (((CoTrapets)m_trapetses.get(i)).m_p1.m_x > trapets.m_p1.m_x) {
					m_trapetses.add( i, trapets);
					stop = true;
				}
				i++;
			}
			if (!stop)
				m_trapetses.add(trapets);
		}
		public  CoTrapets getTrapets (int index) {
			if (m_trapetses.size() > index)
				return (CoTrapets)m_trapetses.get(index);
			else
				return null;
		}
		public boolean trapetsesCoveredByTrapetsesBelow (float[] arr) {
			boolean covered = true;
			// as soon as a trapets is not covered return false
			for (int i = 0; covered && i < m_trapetses.size(); i++) {
				CoTrapets trapets = (CoTrapets)m_trapetses.get(i);
				boolean notCovered = true;
				for (int j = 0; notCovered && j < arr.length; j += 2) {
					notCovered = ! (trapets.m_p4.m_x >= arr[j] && trapets.m_p3.m_x <= arr[j+1]);
				}
				covered = ! notCovered;
			}
			return covered;
		}
	}


public CoTrapetsesColumn( List trapetses, Rectangle2D.Float bounds )
{
	super();
	
	m_bounds2D = new CoRectangle2DFloat();
	m_bounds2D.setRect( bounds );
	
	organizeTrapetses( trapetses );
	normalize();
	setNarrowingY();
	fillHolesWithTrapetses();
	
	m_markY = -1;
	m_markHolder = null;
	m_markIndex = -1;
	m_markTrapets = null;
		
	setMargins( 0, 0 );

//	writeColumn();
}


static boolean closeEnough( double f1, double f2 )
{
	return CoTrapetsPoint.closeEnough( f1, f2 );
}


// Fills up the column with trapetses so for any y (in bounds) at least one trapets will be found

protected void fillHolesWithTrapetses() {
	CoTrapetsHolder holder = m_holderTop;
	CoTrapets trapets;
	
	while (holder != null) {
		if (holder.m_before == null && holder.m_yMin > 0) {
			// a hole is found
			// create a trapets before the first trapetses
			trapets = new CoTrapets(0, 0, 1, 0, 1, holder.m_yMin, 0, holder.m_yMin);
			m_holderTop = new CoTrapetsHolder(trapets);
			m_holderTop.m_next = holder;
			holder.m_before = m_holderTop;
		}
		
		if (holder.m_next == null && holder.m_yMax < m_bounds2D.height) {
			// a hole is found
			// create a trapets after all trapetses
			if
				( m_bounds2D.height - holder.m_yMax < 0.01 )
			{
				CoTrapets t = (CoTrapets) holder.m_trapetses.get( holder.m_trapetses.size() - 1 );
				t.m_p3.m_y = t.m_p4.m_y = holder.m_yMax = m_bounds2D.height;
			} else {
				trapets = new CoTrapets(0, holder.m_yMax, 1, holder.m_yMax,
														  1, m_bounds2D.height, 0, m_bounds2D.height);
				m_holderBottom = new CoTrapetsHolder(trapets);
				m_holderBottom.m_before = holder;
				holder.m_next = m_holderBottom;			
				holder = holder.m_next; // step one
			}
			
		} else if (holder.m_next != null && holder.m_yMax < holder.m_next.m_yMin) {
			// a hole is found
			// create a trapets between holder and holder.m_next
			if
				( holder.m_next.m_yMin - holder.m_yMax < 0.01 )
			{
				CoTrapets t = (CoTrapets) holder.m_trapetses.get( holder.m_trapetses.size() - 1 );
				t.m_p3.m_y = t.m_p4.m_y = holder.m_yMax = holder.m_next.m_yMin;
			} else {
				trapets = new CoTrapets(0, holder.m_yMax, 1, holder.m_yMax,
														  1, holder.m_next.m_yMin, 0, holder.m_next.m_yMin);
				CoTrapetsHolder h = new CoTrapetsHolder(trapets);
				holder.m_next.m_before = h;
				h.m_next = holder.m_next;
				holder.m_next = h;
				h.m_before = holder;
				holder = holder.m_next; // step one
			}
		}

		holder = holder.m_next; // step one
	}
}


// Returns the bounds of the column
// (see interface com.bluebrim.text.shared.CoColumnGeometryIF.CoColumnIF)

public CoRectangle2DFloat getBounds () {
	return m_bounds2D;
}


// Returns the minimum range of the column between y0 and y1
// The range is stored in range: range[0] stores the x value and range[1] the width
// (see interface com.bluebrim.text.shared.CoColumnGeometryIF.CoColumnIF)

public void getMinimalRange( float y0, float y1, float[] range )
{
	if ( y0 < 0 ) y0 = 0;
	if ( y1 < 0 ) y1 = 0;
	
	if
		( m_markHolder == null )
	{
		range[0] = 0;
		range[1] = Float.MAX_VALUE; 
		return;
	}

	float [] tmp = new float [ 2 ];
	
	if
		( m_markHolder.m_yMax >= y1 )
	{
		// both y0 and y1 are in the same holder (they are held by the marker attributes)
		// calc minimum range between m_markY (= y0) and y1 by using m_markTrapets
		getRange( y1, 0, tmp );
		range[ 1 ] = Math.min( range[ 0 ] + range[ 1 ], tmp[ 0 ] + tmp[ 1 ] );
		range[ 0 ] = Math.max( range[ 0 ], tmp[ 0 ] );
		range[ 1 ] -= range[0];
		return;
	} else {
		// calc minimum range between m_markY (= y0) and yMax for m_markHolder
		getRange( m_markHolder.m_yMax, 0, tmp );
		range[ 1 ] = Math.min( range[ 0 ] + range[ 1 ], tmp[ 0 ] + tmp[ 1 ] );
		range[ 0 ] = Math.max( range[ 0 ], tmp[ 0 ] );
		range[ 1 ] -= range[0];
	}

	CoTrapetsHolder holder = m_markHolder.m_next;
	CoTrapets trapets = null;
	while
		( holder != null && holder.m_yMax < y1 && range[1] > 1 )
	{
		getRange( holder.m_yMin, 0, tmp );
		range[ 1 ] = Math.min( range[ 0 ] + range[ 1 ], tmp[ 0 ] + tmp[ 1 ] );
		range[ 0 ] = Math.max( range[ 0 ], tmp[ 0 ] );
		range[ 1 ] -= range[0];

		getRange( holder.m_yMax, 0, tmp );
		range[ 1 ] = Math.min( range[ 0 ] + range[ 1 ], tmp[ 0 ] + tmp[ 1 ] );
		range[ 0 ] = Math.max( range[ 0 ], tmp[ 0 ] );
		range[ 1 ] -= range[0];
		
		holder = holder.m_next;
	}

	if
		( holder != null )
	{
		getRange( holder.m_yMin, 0, tmp );
		range[ 1 ] = Math.min( range[ 0 ] + range[ 1 ], tmp[ 0 ] + tmp[ 1 ] );
		range[ 0 ] = Math.max( range[ 0 ], tmp[ 0 ] );
		range[ 1 ] -= range[0];
	}

	getRange( y1, 0, tmp );
	range[ 1 ] = Math.min( range[ 0 ] + range[ 1 ], tmp[ 0 ] + tmp[ 1 ] );
	range[ 0 ] = Math.max( range[ 0 ], tmp[ 0 ] );
	range[ 1 ] -= range[0];

	// the width may not be 0 or smaller
	if (range[1] <= 0) range[1] = 1;
}


// Returns the minimum range of the column between y0 and y1
// The range is stored in range: range[0] stores the x value and range[1] the width
// (see interface com.bluebrim.text.shared.CoColumnGeometryIF.CoColumnIF)

private void getMinimalRange_old( float y0, float y1, float[] range )
{
	// BUG: Does not take margins into account
	
	if ( y0 < 0 ) y0 = 0;
	if ( y1 < 0 ) y1 = 0;
	
	if
		( m_markHolder == null )
	{
		range[0] = 0;
		range[1] = Float.MAX_VALUE; 
		return;
	}
	
	if
		( m_markHolder.m_yMax >= y1 )
	{
		// both y0 and y1 are in the same holder (they are held by the marker attributes)
		// calc minimum range between m_markY (= y0) and y1 by using m_markTrapets
		range[0] = java.lang.Math.max(range[0], m_markTrapets.getX0(y1));
		range[1] = java.lang.Math.min(m_markTrapets.getX1(y0), m_markTrapets.getX1(y1)) - range[0];
		return;
	} else {
		// calc minimum range between m_markY (= y0) and yMax for m_markHolder
		range[0] = java.lang.Math.max(range[0], m_markTrapets.getX0(m_markHolder.m_yMax));
		range[1] = java.lang.Math.min(m_markTrapets.getX1(y0), m_markTrapets.getX1(m_markHolder.m_yMax)) - range[0];
	}

	CoTrapetsHolder holder = m_markHolder.m_next;
	CoTrapets trapets = null;
	while
		(holder != null && holder.m_yMax < y1 && range[1] > 1)
	{
		// calc minimum range between yMin and yMax for holder
		for
			(int i = 0; i < holder.m_trapetses.size(); i++)
		{
			trapets = (CoTrapets)holder.m_trapetses.get(i);
			if
				(
					(
						( trapets.m_p1.m_x <= range[0] || trapets.m_p4.m_x <= range[0] )
					&&
				 		( trapets.m_p2.m_x > range[0] || trapets.m_p3.m_x > range[0] )
				 	)
				||
					(
						( trapets.m_p1.m_x >= range[0] || trapets.m_p4.m_x >= range[0] )
					&&
				 		( trapets.m_p1.m_x <= range[0] + range[1] || trapets.m_p4.m_x <= range[0] + range[1] )
				 	)
				)
			{
				// found the trapets to use, increase i to stop for-statement
				break;
//				i = holder.m_trapetses.size();
			}
		}
		
		if
			(trapets == null)
		{
			range[0] = 0;
			range[1] = 1;
		} else {
			range[0] = java.lang.Math.max(range[0], trapets.getX0(holder.m_yMin));
			range[0] = java.lang.Math.max(range[0], trapets.getX0(holder.m_yMax));
			range[1] = java.lang.Math.min(
								range[1],
								java.lang.Math.min(trapets.getX1(holder.m_yMin), trapets.getX1(holder.m_yMax)) - range[0]);
		}
		
		// This WHILE loop seems to fail to terminate.
		// The statement below is an attept to solve this problem.
		// Whether this is the correct remedy is however beyond my knowledge.
		//   -- GF 990903
		holder = holder.m_next;

		
	}

	if
		( holder != null && range[1] > 1 )
	{
		// calc minimum range between yMin and y1 for holder
		for
			(int i = 0; i < holder.m_trapetses.size(); i++)
		{
			trapets = (CoTrapets)holder.m_trapetses.get(i);
			if
				(
					(
						( trapets.m_p1.m_x <= range[0] || trapets.m_p4.m_x <= range[0] )
					&&
				  	( trapets.m_p2.m_x > range[0] || trapets.m_p3.m_x > range[0] )
				  )
				||
					(
						( trapets.m_p1.m_x >= range[0] || trapets.m_p4.m_x >= range[0] )
					&&
				 		( trapets.m_p1.m_x <= range[0] + range[1] || trapets.m_p4.m_x <= range[0] + range[1] )
				 	)
				)
			{
				// found the trapets to use, increase i to stop for-statement
				break;
//				i = holder.m_trapetses.size();
			}
		}
		if
			(trapets == null)
		{
			range[0] = 0;
			range[1] = 1;
		} else {
			range[0] = java.lang.Math.max(range[0], trapets.getX0(holder.m_yMin));
			range[0] = java.lang.Math.max(range[0], trapets.getX0(y1));
			range[1] = java.lang.Math.min(
								range[1],
								java.lang.Math.min(trapets.getX1(holder.m_yMin), trapets.getX1(y1)) - range[0]);
		}
	}

	// the width may not be 0 or smaller
	if (range[1] <= 0) range[1] = 1;
}


// Returns the range of the column at y
// The range is stored in range: range[0] stores the x value and range[1] the width
// (see interface com.bluebrim.text.shared.CoColumnGeometryIF.CoColumnIF)

public boolean getRange( float y, int index, float[] range )
{
	if ( y < 0 ) y = 0;
	
	if
		( m_markY != y )
	{
		m_markHolder = getTrapetsHolder( y );
		if
			( m_markHolder == null )
		{
			// return the whole width
			range[ 0 ] = 0;
			range[ 1 ] = Float.MAX_VALUE; 
			return false;
		}
		m_markY = y;
		m_markTrapets = m_markHolder.getTrapets( index );
		m_markIndex = index;
		
	} else if
		( index != m_markIndex )
	{
		m_markTrapets = m_markHolder.getTrapets( index );
		m_markIndex = index;
	}
	
	range[ 0 ] = Math.max( m_markTrapets.getX0( y ), m_leftMargin );
	range[ 1 ] = Math.min( m_markTrapets.getX1( y ), (float) m_bounds2D.getWidth() - m_rightMargin ) - range[ 0 ];
	
	return ( m_markHolder != null ) ? m_markHolder.m_trapetses.size() > index + 1 : false;
}


// Returns the trapets that contains the y
// More than one trapets can contain y, the index tells with one to return

protected CoTrapets getTrapets (float y, int index) {
	CoTrapetsHolder holder = getTrapetsHolder(y);
	
	if (holder != null)
		return holder.getTrapets(index);
	
	return null;
}


// Returns the trapets holder that contains the trapetses that contains y

protected CoTrapetsHolder getTrapetsHolder( float y )
{
	if ( y < 0 ) y = 0;
	
	CoTrapetsHolder holder = m_holderTop;
	while
		( holder != null )
	{
		if
			( ( holder.m_yMin <= y ) && ( holder.m_yMax >= y ) )
		{
			return holder;
		}
		holder = holder.m_next;
	}
	return null;
}


public boolean isEquivalentTo( com.bluebrim.text.shared.CoColumnGeometryIF.CoColumnIF c )
{
	if ( ! ( c instanceof CoTrapetsesColumn ) ) return false;

	CoTrapetsesColumn C = (CoTrapetsesColumn) c;

	if ( ! closeEnough( getBounds().getX(), c.getBounds().getX() ) ) return false;
	if ( ! closeEnough( getBounds().getY(), c.getBounds().getY() ) ) return false;
	if ( ! closeEnough( getBounds().getWidth(), c.getBounds().getWidth() ) ) return false;
	if ( ! closeEnough( getBounds().getHeight(), c.getBounds().getHeight() ) ) return false;
	
	CoTrapetsHolder h0 = m_holderTop;
	CoTrapetsHolder h1 = C.m_holderTop;

	while
		( true )
	{
		if ( ( h0 == null ) && ( h1 == null ) ) return true;
		if ( ( h0 == null ) || ( h1 == null ) ) return false;
		if ( ! h0.isEquivalentTo( h1 ) ) return false;
		h0 = h0.m_next;
		h1 = h1.m_next;
	}
}


// Returns true if the range is narrowing from y and further down
// (see interface com.bluebrim.text.shared.CoColumnGeometryIF.CoColumnIF)

public boolean isNarrowing(float y)
{
	if ( y < 0 ) y = 0;
	return y < m_narrowingY;
}


public boolean isRectangular()
{
	return m_holderTop == m_holderBottom;
}


protected void normalize()
{
	float dy = m_holderTop.m_yMin;
	m_bounds2D.y += dy;
	m_bounds2D.height -= dy;

	CoTrapetsHolder holder = m_holderTop;
		
	while
		( holder != null )
	{
		holder.m_yMin -= dy;
		holder.m_yMax -= dy;

		int I = holder.m_trapetses.size();
		for
			( int i = 0; i < I; i++ )
		{
			CoTrapets trapets = (CoTrapets) holder.m_trapetses.get( i );
			trapets.translateBy( 0, -dy );
		}

		holder = holder.m_next;
	}
}


// Inserts all trapetses (sorted with the trapets with the smallest y first)

protected void organizeTrapetses(List trapetses) {
	Iterator elements = trapetses.iterator();
	
	while (elements.hasNext()) {
		CoTrapets trapets = (CoTrapets)elements.next();

		boolean stop = false;
		// find a holder for the trapets or create a new one
		CoTrapetsHolder holder;
		holder = m_holderTop;
		while (!stop && holder != null) {
			if (holder.m_yMin == trapets.getYMin()) {
				// store trapets with the other trapetses with the same min and max y
				holder.add(trapets);
				stop = true;
			} else if (holder.m_yMin > trapets.getYMin()) {
				// create a new holder for this trapets and insert it
				CoTrapetsHolder t = new CoTrapetsHolder(trapets);
				t.m_next = holder;
				t.m_before = holder.m_before;
				holder.m_before = t;
				if (t.m_before == null)
					m_holderTop = t; // t in first position
				stop = true;
			} else {
				holder = holder.m_next;
			}
		}

		if (!stop) {
			CoTrapetsHolder t = new CoTrapetsHolder(trapets);
			if (m_holderTop == null) {
				// no holder exists
				m_holderTop = t; 
				m_holderBottom = t;
			} else {
				// no matching holder was found, create a new one, insert last
				m_holderBottom.m_next = t; 
				t.m_before = m_holderBottom;
				m_holderBottom = t;
			}
		}
	}
}


// Sets the left and right margins of the column
// (see interface com.bluebrim.text.shared.CoColumnGeometryIF.CoColumnIF)

public void setMargins (float leftMargin, float rightMargin) {
	m_leftMargin = leftMargin;
	m_rightMargin = rightMargin;
}


// Finds the y where isNarrowing changes from false to true
// looking from the bottom up (from biggest y to smallest y)
// By running this method ones and saving the y value the isNarrowing method
// can immediately decide if isNarrowing is true or false
 
protected void setNarrowingY () {
	m_narrowingY = m_holderBottom.m_yMax;

	if ( true ) return;
	// algorithm below is broken, let's play it safe and asume that entire column is narrowing.
	
	CoTrapetsHolder holder = m_holderBottom;
	boolean stop = false;
	
	while
		(!stop && holder != null)
	{
		CoTrapetsHolder holder1 = holder.m_before;
		float[] arr = new float[holder.m_trapetses.size() * 2];
		for
			(int i = 0; !stop && i < holder.m_trapetses.size(); i += 2)
		{
			CoTrapets trapets = (CoTrapets) holder.m_trapetses.get(i);
			// check that the trapets is not narrowing (stop if it is narrowing)
			stop = trapets.m_p4.m_x > trapets.m_p1.m_x && trapets.m_p2.m_x > trapets.m_p3.m_x;
			arr[i] = trapets.m_p1.m_x;
			arr[i + 1] = trapets.m_p2.m_x;
		}
		
		if
			(stop)
		{
			m_narrowingY = holder.m_yMax;
		} else if
			(holder1 != null && holder.m_yMin == holder1.m_yMax)
		{
			// check the trapetses of the holder above (holder1) that is connected to holder
			// all its trapetses must be covered by the trapetses below
			stop = !holder1.trapetsesCoveredByTrapetsesBelow(arr);
			if
				(stop)
			{
				m_narrowingY = holder.m_yMin;
			}
		}
		holder = holder1;
	}


}


// A method just for testing purposes

public void writeColumn () {
	System.out.println("Trapetser i en kolumn");
	System.out.println("Narrowing y : " + m_narrowingY + " Bounds : " + m_bounds2D);
	CoTrapetsHolder holder = m_holderTop;
	while (holder != null) {
		System.out.print("Holder  (ymin, ymax) : (" + holder.m_yMin + ", " + holder.m_yMax + ")");
		System.out.println(" size : " + holder.m_trapetses.size());
		for (int i = 0; i < holder.m_trapetses.size(); i++) {
			System.out.println((CoTrapets)holder.m_trapetses.get(i));
		}
		holder = holder.m_next;
	}
	System.out.println("");
}
}