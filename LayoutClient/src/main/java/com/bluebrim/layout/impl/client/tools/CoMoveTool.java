package com.bluebrim.layout.impl.client.tools;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

import com.bluebrim.base.client.command.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.client.transfer.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * Implementation of "reshape mode" (used for dragging page items)
 * 
 * @author: Dennis Malmström
 */
public class CoMoveTool extends CoAbstractTool implements CoPageItemDragSource
{
	protected boolean m_wasDragged;

	protected Point2D m_mousePos;
	protected Point2D m_draggedShapePos;
	
	protected CoShapeIF m_draggedShape;
	protected double m_dx = 0;
	protected double m_dy = 0;

	protected CoShapePageItemView m_draggedView;
	protected List m_views;
	protected List m_shapes;

	protected boolean m_keepParent = false;
	protected CoTargetViewKeeper m_targetTracker;
	protected final CoMoveToolDragSourceListener m_dragSourceListener;

protected CoMoveTool( CoTool previousTool, CoLayoutEditor pageItemEditor, boolean keepParent )
{
	super( previousTool, pageItemEditor );
	
	m_keepParent = keepParent;

	m_dragSourceListener = new CoMoveToolDragSourceListener( m_editor.getWorkspace(), this, false );
}

public CoMoveTool( CoTool previousTool, CoLayoutEditor pageItemEditor, boolean keepParent, CoShapePageItemView view )
{
	this( previousTool, pageItemEditor, keepParent );
	
	List v = new ArrayList();
	v.add( view );
	
	setViews( view, v );
}

public CoMoveTool( CoTool previousTool, CoLayoutEditor pageItemEditor, boolean keepParent, CoShapePageItemView view, Iterator views )
{
	this( previousTool, pageItemEditor, keepParent );

	List v = new ArrayList();
	while
		( views.hasNext() )
	{
		v.add( views.next() );
	}
	
	setViews( view, v );
}

public CoMoveTool( CoTool previousTool, CoLayoutEditor pageItemEditor, boolean keepParent, CoShapePageItemView view, List views )
{
	this( previousTool, pageItemEditor, keepParent );

	setViews( view, views );
}

public void activate( Point2D pos )
{
	super.activate( pos );

	if
		( ( m_mousePos == null ) || ( m_draggedView == null ) )
	{
		m_mousePos = pos;

		m_targetTracker = new CoTargetViewKeeper( this )
		{
			protected CoCompositePageItemView checkValidTargetView( CoCompositePageItemView v )
			{
				if ( v == null ) return null;
				if ( v.areChildrenLocked() ) return null;

				if
					( ! v.validateAdd( m_draggedView ) )
				{
					return m_targetView;
				}
				
				Iterator selectedViews = m_views.iterator();
				while
					( selectedViews.hasNext() )
				{
					CoShapePageItemView piv = (CoShapePageItemView) selectedViews.next();
					if
						( v.isChildOf( piv ) )
					{
						return m_targetView;
					}
				}
				
				return v;
			}

			protected void drawShapeOfTarget( Graphics2D g )
			{
				if ( ! m_keepParent ) super.drawShapeOfTarget( g );
			}

		};
	}
	
}

public CoShapePageItemView getDraggedView ()
{
	return m_draggedView;
}

public Point2D getMousePos()
{
	return m_mousePos;
}

public List getViews ()
{
	return m_views;
}

public CoTool mouseDragged( MouseEvent e )
{
	if
		( m_dragSourceListener.isActive() )
	{
//		System.err.println( "CoMoveTool.mouseDragged: dnd active -> do nothing" );
		return this;
	}

	
	if ( m_draggedView == null ) return this;
	
	Point2D p = getLocation( e );
	double dx = p.getX() - m_mousePos.getX();
	double dy = p.getY() - m_mousePos.getY();

	// mouse trembling filter
	if
		( ( ! m_wasDragged ) && ( dx * dx < m_moveTreshold ) && ( dy * dy < m_moveTreshold ) )
	{
		return this;
	}

	
	// start DnD ???
	if
		( shouldInitDnD( e ) )
	{
		m_dragSourceListener.start();
		return this;
	}

	
	m_mousePos = p;

	
	// find parent
	CoCompositePageItemView target = (CoCompositePageItemView) m_viewPanel.getRootView().findTopMostViewContaining( getLocation( e ), m_views, true, false, -1 );
	m_targetTracker.setTargetView( target );

	m_viewPanel.transform( m_draggedShapePos );
	m_draggedShapePos.setLocation( m_draggedShapePos.getX() + dx, m_draggedShapePos.getY() + dy );
	m_viewPanel.untransform( m_draggedShapePos );

	Graphics2D g = getXORGraphics();
	
	if
		( target == m_targetTracker.getTargetView() )
	{
		// first time only
		if
			( ! m_wasDragged )
		{
			xorDraw( g );
		}

		// transform to local coordinates

		Point2D p2 = null;
		if
			( m_editor.getSnapToGrid() )
		{
			// snap
			Point2D d = new Point2D.Double( Double.MAX_VALUE, Double.MAX_VALUE );
			p2 = (Point2D) m_draggedShapePos.clone();
			target.transformFromGlobal( m_draggedShapePos );

			p2 = m_viewPanel.getRootView().snap( p2.getX(), p2.getY(), m_draggedShape.getWidth(), m_draggedShape.getHeight(), getSnapRange(), d );
			target.transformFromGlobal( p2 );
			p2 = target.snap( p2.getX(), p2.getY(), m_draggedShape.getWidth(), m_draggedShape.getHeight(), getSnapRange(), d );
		} else {
			// no snap
			target.transformFromGlobal( m_draggedShapePos );
			p2 = (Point2D) m_draggedShapePos.clone();
		}
			
		// beräkna delta ( i globala koordinatsystemet )
		target.transformToGlobal( p2 );

		
		dx = p2.getX() - m_draggedShape.getX();
		dy = p2.getY() - m_draggedShape.getY();
			
		// transformera m_draggedShapePos från lokala koordinater
		target.transformToGlobal( m_draggedShapePos );



		autoScroll( e );

		xorDraw( g );

		Iterator shapes = m_shapes.iterator();
		while
			( shapes.hasNext() )
		{
			( (CoShapeIF) shapes.next() ).getMoveHandle().move( dx, dy );
		}

		xorDraw( g );
	
		m_wasDragged = true;
	}


	
	return this;
}

public CoTool mouseReleased( MouseEvent e )
{
	if
		( m_dragSourceListener.isActive() )
	{
//		System.err.println( "CoMoveTool.mouseReleased: dnd active -> do nothing" );
		m_viewPanel.repaint();
		return m_previousTool;
	}

	if 
		( m_wasDragged )
	{
		CoCompoundUndoableCommand.INSTANCE.reset( "MOVE PAGE ITEMS" );
		
		CoCompositePageItemView targetView = m_keepParent ? null : m_targetTracker.getTargetView();
		m_targetTracker.setTargetView( null );
		
		Iterator views = m_views.iterator();
		Iterator shapes = m_shapes.iterator();
		while
			( views.hasNext() )
		{
			CoShapePageItemView v = (CoShapePageItemView) views.next();
			CoShapeIF s = (CoShapeIF) shapes.next();
			Point2D p = new Point2D.Double( s.getX(), s.getY() );

			if
				( 
					( targetView != null ) // don't drop it into nothing
				&&
					( targetView != v ) // don't drop it into itself
				&&
					( ! targetView.getPageItemId().equals( v.getPageItemId() ) ) // don't drop it into itself
				&&
					( targetView != v.getParent() ) // don't drop it into its parent
				&&
					(
						( v.getParent() == null ) // don't drop it into its parent
					||
						( ! targetView.getPageItemId().equals( v.getParent().getPageItemId() ) ) // don't drop it into its parent
					)
				)
			{
				// reparent page item
				targetView.transformFromGlobal( p );
				CoCompoundUndoableCommand.INSTANCE.add( createReparentCommand( v, targetView, p, false ) );

			} else {
				// move page item
				v.getParent().transformFromGlobal( p );
				CoCompoundUndoableCommand.INSTANCE.add( createMoveCommand( v, p ) );
			}
		}

		m_editor.getCommandExecutor().doit( CoCompoundUndoableCommand.INSTANCE, null );

		m_wasDragged = false;

		xorDraw( getXORGraphics() );
		
	} else {
		// no move -> interpret as click
		selectClickedView( getViewAt( getLocation( e ), false ), e );
	}


	return m_previousTool;
}

/**
 * Called from constructor
 */
protected final void setViews( CoShapePageItemView draggedView, List views )
{
	m_views = new ArrayList();
	m_shapes = new ArrayList();

	Iterator vs = views.iterator();
	while
		( vs.hasNext() )
	{
		CoShapePageItemView v = (CoShapePageItemView) vs.next();
		if 
			( ! isMoveable( v ) )
		{
			continue;
		}
		
		CoShapeIF s = v.getCoShape().deepClone();

		// store global position in shape
		Point2D p = new Point2D.Double( v.getX(), v.getY() );
		if ( v.getParent() != null ) v.getParent().transformToGlobal( p );
		s.setTranslation( p.getX(), p.getY() );

		m_views.add( v );
		m_shapes.add( s );

		if
			( v == draggedView )
		{
			m_draggedView = draggedView;
			m_draggedShape = s;
			m_draggedShapePos = p;
			m_dx = - p.getX();
			m_dy = - p.getY();
		}
	}
}
public boolean shouldEndDnD ( MouseEvent e )
{
	return ( getArea( e ) == INSIDE );
}
protected boolean shouldInitDnD ( MouseEvent e )
{
	return ( getArea( e ) == OUTSIDE ) && ! isAutoscrolling();
}
protected void xorDraw( Graphics2D g )
{
	AffineTransform t = g.getTransform();
	
	Iterator shapes = m_shapes.iterator();
	Iterator views = m_views.iterator();
	while
		( shapes.hasNext() )
	{
		g.setTransform( t );
		CoShapeIF s = (CoShapeIF) shapes.next();
		double x = s.getX();
		double y = s.getY();

		CoShapePageItemView v = (CoShapePageItemView) views.next();
		
		if
			( v != m_draggedView )
		{
			g.translate( m_draggedShape.getX() + m_dx, m_draggedShape.getY() + m_dy );
		} else {
			g.translate( x + m_dx, y + m_dy );
		}
		
		g.transform( v.getAffineTransform() );

		s.setTranslation( 0, 0 );
		g.draw( s.getShape() );
		s.setTranslation( x, y );
	}

	g.setTransform( t );
}




public boolean canStartDrag()
{
	return true;
}

protected CoUndoableCommand createMoveCommand( CoShapePageItemView view, Point2D pos )
{
	return CoShapePageItemSetPositionCommand.create( "", view, pos.getX(), pos.getY() );
}

protected CoUndoableCommand createReparentCommand( CoShapePageItemView childView, CoCompositePageItemView newParentView, final Point2D newPos, boolean doCloneChild )
{
	CoShapePageItemIF child = childView.getShapePageItem();

	CoCompositePageItemIF oldParent = ( childView.getParent() == null ) ? null : childView.getParent().getCompositePageItem();
	CoCompositePageItemIF newParent = newParentView.getCompositePageItem();

	CoRef oldParentId = ( childView.getParent() == null ) ? null : childView.getParent().getPageItemId();
	CoRef newParentId = newParentView.getPageItemId();
	
	if
		( doCloneChild )
	{
		child = (CoShapePageItemIF) child.deepClone();
		oldParent= null;
	}
	
	CoShapePageItemReparentCommand c = new CoShapePageItemReparentCommand();
	c.prepare( "REPARENT PAGE ITEM", child, oldParent, oldParentId, newParent, newParentId, newPos );

	return c;
//	m_editor.getCommandExecutor().doit( c, null );
}


public CoShapePageItemIF getSnappingPageItem() {
	return null;
}

public CoShapePageItemView getSnappingPageItemView()
{
	return m_draggedView;
}

public List getTransferablePageItems()
{
	return null;
}

public List getTransferablePageItemViews()
{
	return m_views;
}
}