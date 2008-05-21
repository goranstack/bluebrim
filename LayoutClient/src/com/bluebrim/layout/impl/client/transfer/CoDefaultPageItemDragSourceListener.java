package com.bluebrim.layout.impl.client.transfer;

import java.awt.*;
import java.awt.event.*;

/**
 * Mouse listener responsible for dispatching page item dnd events from a Component.
 * Creation date: (2001-08-09 16:11:27)
 * @author: Dennis
 */
 
public class CoDefaultPageItemDragSourceListener extends CoPageItemDragSourceListener
{
	protected boolean m_isDragging = false;
	protected boolean m_canDrag = false;
public CoDefaultPageItemDragSourceListener( Component c, CoPageItemDragSource dragSource, boolean copyOnDrop )
{
	super( c, dragSource, copyOnDrop );
}
public void mouseDragged( MouseEvent ev )
{
	if
		( ( ev.getModifiers() & ev.BUTTON1_MASK ) != 0 )
	{
		if ( ! m_isDragging ) startDrag();
		m_isDragging = true;

		performDrag( ev );
	}
}
public void mousePressed( MouseEvent ev )
{
	if
		( ( ev.getModifiers() & ev.BUTTON1_MASK ) != 0 )
	{
		m_canDrag = m_dragSource.canStartDrag();
		if ( m_canDrag ) m_component.addMouseMotionListener( this );
	}
}
public void mouseReleased( MouseEvent ev )
{
	if
		( ( ev.getModifiers() & ev.BUTTON1_MASK ) != 0 )
	{
		if
			( m_canDrag )
		{
			m_component.removeMouseMotionListener( this );

			if
				( m_isDragging )
			{
				m_isDragging = false;
				endDrag( ev, false );
			}
		}
	}
}
}
