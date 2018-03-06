package com.bluebrim.layout.impl.client.tools;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;

import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.client.view.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Abstract superclass for layout editor tools.
 * Among its features are:
 *
 * - References to a layout editor and its workspace.
 * - Support for autoscrolling.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoAbstractTool extends CoTool
{
	protected CoLayoutEditor m_editor;
	protected CoPageItemEditorPanel m_viewPanel; // workspace of m_editor

	private CoAutoscroller m_autoscroller;

	protected CoTool m_previousTool;


	private static final double m_snapRange = 15; // range (in pixels) for snapping

	// auto scroll stuff
	protected static final int AUTO_SCROLL_INCREMENT = 20;
	protected static final int AUTO_SCROLL_HIT_RANGE = 20;

	public static final int INSIDE = 0; // see getArea
	public static final int INZONE = 1; // see getArea
	public static final int OUTSIDE = 2; // see getArea

	// mouse trembling filter parameter
	public static final int m_moveTreshold = 4 * 4;
public CoAbstractTool( CoTool previousTool, CoLayoutEditor pageItemEditor )
{
	m_editor = pageItemEditor;

	m_viewPanel = m_editor.getWorkspace();

	m_previousTool = previousTool;

	m_autoscroller = m_viewPanel.getAutoscroller( AUTO_SCROLL_INCREMENT );
}
public void activate( Point2D pos )
{
}
protected boolean autoScroll( MouseEvent e )
{
	/*
	if
		( ( ! m_autoscroller.isActive() ) && ( getArea( e ) == INZONE ) )
	{
		// start autoscrolling
		m_autoscroller.setScrollVector( getAutoScrollVector( e ) );
		m_autoscroller.start();
	} else if
		( getArea( e ) == INZONE )
	{
		// continue autoscrolling
		m_autoscroller.setScrollVector( getAutoScrollVector( e ) );
	} else if
		( getArea( e ) != INZONE )
	{
		// stop autoscrolling
		m_autoscroller.stop();
	}
*/
	
	if
		( getArea( e ) == INZONE )
	{
		if
			( ! m_autoscroller.isActive() )
		{
			// start autoscrolling
			m_autoscroller.setScrollVector( getAutoScrollVector( e ) );
			m_autoscroller.start();
		} else {
			// continue autoscrolling
			m_autoscroller.setScrollVector( getAutoScrollVector( e ) );
		}
	} else {
		// stop autoscrolling
		if
			( m_autoscroller.isActive() )
		{
			m_autoscroller.stop();
		}
	}
	
	return false;
}
public void deactivate( Point2D pos )
{
}
// in which "zone" did the event take place ?
//
// INSIDE  inside workspace
// INZONE  outside workspace but closer to it than AUTO_SCROLL_HIT_RANGE
// OUTSIDE outside workspace and farther from it than AUTO_SCROLL_HIT_RANGE

public int getArea( MouseEvent e )
{
	// edge relative coordinates
	double x0 = e.getX() + m_viewPanel.getX();
	double x1 = m_viewPanel.getParent().getBounds().width - x0;
	double y0 = e.getY() + m_viewPanel.getY();
	double y1 = m_viewPanel.getParent().getBounds().height - y0;

	if
		( ( x0 >= 0 ) && ( x1 >= 0 ) && ( y0 >= 0 ) && ( y1 >= 0 ) )
	{
		return INSIDE;
	}

	if
		(
			( y0 <= - AUTO_SCROLL_HIT_RANGE )
		||
			( y1 <= - AUTO_SCROLL_HIT_RANGE )
		||
			( x0 <= - AUTO_SCROLL_HIT_RANGE )
		||
			( x1 <= - AUTO_SCROLL_HIT_RANGE )
		)
	{
		return OUTSIDE;
	}

	return INZONE;
}
// unit vector defining which way to autoscroll

private Point getAutoScrollVector( MouseEvent e )
{
	// edge relative coordinates
	double x0 = e.getX() + m_viewPanel.getX();
	double x1 = m_viewPanel.getParent().getBounds().width - x0;
	double y0 = e.getY() + m_viewPanel.getY();
	double y1 = m_viewPanel.getParent().getBounds().height - y0;

	int dy = 0;
	if      ( y0 < 0 ) dy = -1;
	else if ( y1 < 0 ) dy = 1;

	int dx = 0;
	if      ( x0 < 0 ) dx = -1;
	else if ( x1 < 0 ) dx = 1;

	return new Point( dx, dy );
}
protected JScrollBar getHorizontalScrollBar()
{
	return ( (JScrollPane) m_viewPanel.getParent().getParent() ).getHorizontalScrollBar();
}
protected double getSnapRange()
{
	
	return m_snapRange / m_viewPanel.getScreenScale();
}
protected JScrollBar getVerticalScrollBar()
{
	return ( (JScrollPane) m_viewPanel.getParent().getParent() ).getVerticalScrollBar();
}
protected CoShapePageItemView getViewAt( Point2D p, boolean ignoreChildLock )
{
	CoShapePageItemView v = m_viewPanel.getRootView().getViewWithMoveHandleContaining( p );
	if
		( v == null )
	{
		v = m_viewPanel.getRootView().findTopMostViewContaining( p, null, false, ignoreChildLock, -1 );
	}

	return v;
}
public CoPageItemEditorPanel getViewPanel ()
{
	return m_viewPanel;
}
protected Graphics2D getXORGraphics()
{
	return m_viewPanel.getXORGraphics();
}
protected boolean isAutoscrolling()
{
	return m_autoscroller.isActive();
}
protected final boolean isMoveable( CoShapePageItemView v )
{
	return ( v != null ) && v.isMoveable() && ! m_viewPanel.getRootView().isModelView( v );
}
protected final boolean isReshapeable( CoShapePageItemView v )
{
	return ( v != null ) && v.isReshapeable();
}
protected final boolean isSelectable( CoShapePageItemView v )
{
	return ( v != null ) && v.isSelectable();
}
protected void selectClickedView( CoShapePageItemView view, MouseEvent e )
{
	if ( view == null ) return;

	if
		( ( e.getModifiers() & MouseEvent.CTRL_MASK ) != 0 )
	{
		// select parent
		if
			( ! m_viewPanel.getRootView().isModelView( view ) )
		{
			view = view.getParent();
		}
		
	} else if
		( ( e.getModifiers() & MouseEvent.ALT_MASK ) != 0 )
	{
		// select highest ancestor that is not a model 
		CoShapePageItemView v = view.getParent();
		while
			( ( ! ( v.getParent() instanceof CoRootView ) ) && ! m_viewPanel.getRootView().isModelView( v.getParent() ) )
		{
			v = v.getParent();
		}

		if ( v != null ) view = v;
	}

	boolean editSelection = ( ( e.getModifiers() & MouseEvent.SHIFT_MASK ) != 0 );


	
	m_viewPanel.getSelectionManager().beginSelectionTransaction();

	if
		( isSelectable( view ) )
	{
		if
			( editSelection )
		{
			m_viewPanel.getSelectionManager().toggleSelection( view );
		} else if
			( ! m_viewPanel.getSelectionManager().isSelected( view ) )
		{
			m_viewPanel.getSelectionManager().unselectAllViews();
			m_viewPanel.getSelectionManager().select( view );
		}
	} else {
		m_viewPanel.getSelectionManager().unselectAllViews();
	}
	
	m_viewPanel.getSelectionManager().endSelectionTransaction();
}
}