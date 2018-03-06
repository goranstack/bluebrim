package com.bluebrim.layout.impl.client.tools;

import java.awt.*;
import java.awt.event.*;

import com.bluebrim.layout.impl.client.transfer.*;

/**
 * Mouse listener responsible for dispatching page item dnd events from a move tool in a layout editor.
 * Creation date: (2001-08-09 16:11:27)
 * @author: Dennis
 */
 
public class CoMoveToolDragSourceListener extends CoPageItemDragSourceListener
{
	private boolean m_isDragging = false;
public CoMoveToolDragSourceListener( Component c, CoPageItemDragSource dragSource, boolean copyOnDrop )
{
	super( c, dragSource, copyOnDrop );
}
public boolean isActive()
{
	return m_isDragging;
}
public void mouseDragged( MouseEvent ev )
{
//	System.err.println( "CoMoveToolDragSourceListener.mouseDragged" );
	performDrag( ev );
}
public void mouseEntered( MouseEvent ev )
{
	if
		( m_isDragging )
	{
//		System.err.println( "CoMoveToolDragSourceListener.mouseEntered" );
		m_component.removeMouseMotionListener( this );

		m_isDragging = false;
		endDrag( ev, true );
	}
}
public void mouseReleased( MouseEvent ev )
{
	if
		( m_isDragging )
	{
//		System.err.println( "CoMoveToolDragSourceListener.mouseReleased" );
		m_component.removeMouseMotionListener( this );

		m_isDragging = false;
		endDrag( ev, false );
	}
}
public void start()
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( ! m_isDragging, "Illegal state" );
	
//	System.err.println( "CoMoveToolDragSourceListener.start" );

	m_component.addMouseMotionListener( this );
	startDrag();
	m_isDragging = true;
}
}
