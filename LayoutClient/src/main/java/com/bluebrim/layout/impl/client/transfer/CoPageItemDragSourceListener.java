package com.bluebrim.layout.impl.client.transfer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;

/**
 * Mouse listener responsible for dispatching page item dnd events.
 * Creation date: (2001-08-09 16:11:27)
 * @author: Dennis
 */
 
public abstract class CoPageItemDragSourceListener implements MouseListener, MouseMotionListener
{
	protected final CoPageItemDragSource m_dragSource;
	protected final Component m_component;
	protected final boolean m_copyOnDrop;

	protected CoShapePageItemView m_snappingView;
	protected List m_views;

	private CoPageItemDropTarget m_dropTarget;
	
	private Cursor m_originalCursor;
	
	private static final Cursor m_offDropTargetCursor = Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR );
	private static final Cursor m_onDropTargetCursor = Cursor.getPredefinedCursor( Cursor.CROSSHAIR_CURSOR );
protected CoPageItemDragSourceListener( Component c, CoPageItemDragSource dragSource, boolean copyOnDrop )
{
	m_dragSource = dragSource;
	m_component = c;
	m_copyOnDrop = copyOnDrop;

	m_component.addMouseListener( this );
}
public boolean doCopyOnDrop()
{
	return m_copyOnDrop;
}
protected void endDrag( MouseEvent ev, boolean cancel )
{
//	System.err.println( "CoPageItemDragSourceListener.endDrag, cancel = " + cancel );

	m_component.setCursor( m_originalCursor );

	if
		( m_dropTarget != null )
	{
		if ( ! cancel ) m_dropTarget.drop( this, ev );
		m_dropTarget = null;
		m_originalCursor = null;
	}
}
public CoShapePageItemView getSnappingPageItemView()
{
	if
		( m_snappingView == null )
	{
		m_snappingView = m_dragSource.getSnappingPageItemView();

		if
			( m_snappingView == null )
		{
			com.bluebrim.transact.shared.CoRef id = m_dragSource.getSnappingPageItem().getId();
			Iterator i = getTransferablePageItemViews().iterator();
			while
				( i.hasNext() )
			{
				CoShapePageItemView v = (CoShapePageItemView) i.next();
				if
					( v.getPageItemId().equals( id ) )
				{
					m_snappingView = v;
					break;
				}
			}
		}

		com.bluebrim.base.shared.debug.CoAssertion.assertTrue( m_snappingView != null, "Snapping view missing" );
	}
	
	return m_snappingView;
}
public List getTransferablePageItemViews()
{
	if
		( m_views == null )
	{
		m_views = m_dragSource.getTransferablePageItemViews();

		if
			( m_views == null )
		{
			m_views = new ArrayList();
			
			Iterator pageItems = m_dragSource.getTransferablePageItems().iterator();
			while
				( pageItems.hasNext() )
			{
				CoShapePageItemIF pi = (CoShapePageItemIF) pageItems.next();
				m_views.add( CoPageItemView.create( pi, CoPageItemView.DETAILS_EVERYTHING ) );
			}
		}

	}
	
	return m_views;
}
public boolean hasDropTarget()
{
	return m_dropTarget != null;
}
	/**
	 * Invoked when the mouse has been clicked on a component.
	 */
public void mouseClicked(java.awt.event.MouseEvent e) {}
public void mouseDragged( MouseEvent ev )
{
}
	/**
	 * Invoked when the mouse enters a component.
	 */
public void mouseEntered(java.awt.event.MouseEvent e) {}
public void mouseExited( MouseEvent ev )
{
}
	/**
	 * Invoked when the mouse button has been moved on a component
	 * (with no buttons no down).
	 */
public void mouseMoved(java.awt.event.MouseEvent e) {}
public void mousePressed( MouseEvent ev )
{
}
public void mouseReleased( MouseEvent ev )
{
}
protected void performDrag( MouseEvent ev )
{
//	System.err.println( "CoPageItemDragSourceListener.performDrag" );

	if
		( m_dropTarget != null )
	{
		boolean x = m_dropTarget.drag( this, ev );
		if ( x ) return;
	}
	
	Point p = com.bluebrim.gui.client.CoGUI.localToScreen( ev.getComponent(), ev.getPoint() );
	
	CoPageItemDropTarget t = CoPageItemDropTarget.findDropTargetAt( p.x, p.y );

	if
		( m_dropTarget == t )
	{
		if ( m_dropTarget != null ) m_dropTarget.drag( this, ev );
	} else {
		if
			( m_dropTarget != null )
		{
			m_component.setCursor( m_offDropTargetCursor );
			m_dropTarget.dragExit( this, ev );
		}
		m_dropTarget = t;
			
		if
			( m_dropTarget != null )
		{
			m_component.setCursor( m_onDropTargetCursor );
			m_dropTarget.dragEnter( this, ev );
		}
	}
}
protected void startDrag()
{
//	System.err.println( "CoPageItemDragSourceListener.startDrag" );

	m_snappingView = null;
	m_views = null;

	m_originalCursor = m_component.getCursor();
	m_component.setCursor( m_offDropTargetCursor );
}
}
