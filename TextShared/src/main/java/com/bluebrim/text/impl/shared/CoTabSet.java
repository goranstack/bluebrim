package com.bluebrim.text.impl.shared;

import java.util.*;

/**
 * Implementation of tab stop set
 * 
 * @author: Dennis Malmström
 */

public class CoTabSet implements CoTabSetIF
{
	private List m_tabStops = new ArrayList(); // tab stops
	
	private transient List m_positionOrder; // sorted by position

	
	// mutable proxy (with a less than relation)
	private class TabStop extends CoTabStop implements Comparable
	{
		public CoTabStopIF copy()
		{
			TabStop t = new TabStop();
			copyInto( t );
			return t;
		}
		
		public void setPosition( float pos )
		{
			super.setPosition( pos );
			m_positionOrder = null;
		}

		public int compareTo( Object o )
		{
			float delta = m_position - ( (TabStop) o ).m_position;
			if ( delta == 0 ) return 0;
			if ( delta < 0 ) return -1;
			return 1;
		}
	}
public CoTabStopIF addTabStop()
{
	CoTabStopIF t = new TabStop();
	m_tabStops.add( t );
	m_positionOrder = null;
	return t;
}
public CoTabSetIF copy()
{
	CoTabSet s = new CoTabSet();

	Iterator i = m_tabStops.iterator();
	while
		( i.hasNext() )
	{
		s.m_tabStops.add( ( (CoTabStopIF) i.next() ).copy() );
	}

	return s;
}
public int getIndexOfTabStop( CoTabStopIF t )
{
	return m_tabStops.indexOf( t );
}
public CoTabStopIF getTabAfter( float location )
{
	if
		( m_positionOrder == null )
	{
		m_positionOrder = new ArrayList( m_tabStops );
		Collections.sort( m_positionOrder );
	}

	CoTabStopIF t = null;
	Iterator i = m_positionOrder.iterator();
	while
		( i.hasNext() )
	{
		t = (CoTabStopIF) i.next();
		if
			( t.getPosition() > location )
		{
			return t;
		}
	}

	return null;
}
public CoTabStopIF getTabStop( int i )
{
	return (CoTabStopIF) m_tabStops.get( i );
}
public int getTabStopCount()
{
	return m_tabStops.size();
}
public void removeTabStop( CoTabStopIF tabStop )
{
	m_tabStops.remove( tabStop );
	m_positionOrder = null;
}
}
