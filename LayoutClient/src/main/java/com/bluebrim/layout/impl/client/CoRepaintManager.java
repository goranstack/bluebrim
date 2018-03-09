package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.util.*;

import javax.swing.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-10-16 09:44:57)
 * @author: 
 */
 
public class CoRepaintManager extends RepaintManager
{
	private Map m_dirtyRegions = new HashMap(); // [ CoPageItemEditorPanel -> LinkedList[ Rectangle ] ]
/**
 * CoRepaintManager constructor comment.
 */
public CoRepaintManager() {
	super();
}
public synchronized void addDirtyRegion( JComponent c, int x, int y, int w, int h )
{
	if
		( ! CoPageItemEditorPanel.USE_REPAINT_AREA_SEPARATION || ! ( c instanceof CoPageItemEditorPanel ) )
	{
		super.addDirtyRegion( c, x, y, w, h );
	} else {
		
			/* Special cases we don't have to bother with.
		 */
		if ((w <= 0) || (h <= 0) || (c == null))
		{
			return;
		}

		if ((c.getWidth() <= 0) || (c.getHeight() <= 0))
		{
			return;
		}

		if
			( m_dirtyRegions.containsKey( c ) )
		{
			( (LinkedList) m_dirtyRegions.get( c ) ).add( new Rectangle( x, y, w, h ) );
		} else {
			LinkedList ll = new LinkedList();
			ll.add( new Rectangle( x, y, w, h ) );
			m_dirtyRegions.put( c, ll );
		}
		
	}
		
}
public void paintDirtyRegions()
{
	super.paintDirtyRegions();

	if
		( CoPageItemEditorPanel.USE_REPAINT_AREA_SEPARATION )
	{
		Iterator i = m_dirtyRegions.entrySet().iterator();
		while
			( i.hasNext() )
		{
			Map.Entry e = (Map.Entry) i.next();

			LinkedList ll = (LinkedList) e.getValue();
			CoPageItemEditorPanel edp = (CoPageItemEditorPanel) e.getKey();

			int N = ll.size();
			
			while
				( ! ll.isEmpty() )
			{
				Rectangle R = (Rectangle) ll.removeFirst();
				N--;

				int n = N;
				while
					( n > 0 )
				{
					Rectangle r = (Rectangle) ll.removeFirst();
					n--;

					if
						( R.intersects( r ) )
					{
						R.add( r );
						N--;
					} else {
						ll.add( r );
					}
				}

				edp.paintImmediately( R );
			}
			
		}

		m_dirtyRegions.clear();
	}
	
}
}
