package com.bluebrim.layout.impl.client.transfer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

/**
 * Drop target for page item dnd.
 * Creation date: (2001-08-09 16:18:45)
 * @author: Dennis
 */
 
public abstract class CoPageItemDropTarget
{
	protected final JComponent m_component;	
	private static List m_instances = new ArrayList(); // [ CoPageItemDropTarget ]

public CoPageItemDropTarget( JComponent c )
{
	super();

	m_component = c;

	m_instances.add( this );
}
// return true if this drag stays inside this target

protected boolean drag( CoPageItemDragSourceListener l, MouseEvent ev )
{
	return false;
}

protected void dragEnter( CoPageItemDragSourceListener l, MouseEvent ev )
{
	( (Window) m_component.getTopLevelAncestor() ).toFront(); // ugly way of handling the fact that areas that are covered by other windows are not ignored.
}

protected void dragExit( CoPageItemDragSourceListener l, MouseEvent ev )
{
}

protected abstract void drop( CoPageItemDragSourceListener l, MouseEvent ev );

static CoPageItemDropTarget findDropTargetAt( int x, int y )
{
	Iterator i = m_instances.iterator();

	while
		( i.hasNext() )
	{
		CoPageItemDropTarget t = (CoPageItemDropTarget) i.next();
		Component c = t.m_component;

		if ( ! c.isEnabled() ) continue;

		if
			( c.getParent() instanceof javax.swing.JViewport )
		{
			c = c.getParent();
		}

		try
		{
			// pending: avoid covered areas 
			Point p = com.bluebrim.gui.client.CoGUI.localToScreen( c );
			if 
				( c.contains( x - p.x, y - p.y ) )
			{
				return t;
			}
		}
		catch ( IllegalComponentStateException ex )
		{
//			i.remove();
		}
	}

	return null;
}
}
