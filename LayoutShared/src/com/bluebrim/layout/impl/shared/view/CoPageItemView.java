package com.bluebrim.layout.impl.shared.view;

import java.awt.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Abstract ancestor of all page item view classes.
 * Page item views are client side proxies for page items.
 * Their most important tasks are:
 *  - maintaining a cache of page item state.
 *  - monitoring changes in page items and forwarding them to view listeners.
 *  - Painting page items to a CoPaintable.
 *  - Acting as icons for themselves
 * 
 * @author: Dennis Malmström
 */

public abstract class CoPageItemView extends CoView implements Icon
{
	// debugging/tracing switches
	public static boolean DO_TRACE_NOTIFICATIONS = false; // trace page item notifications on console

	// details filters used when creating views
	public static final int DETAILS_EVERYTHING = 0;              // show everything
	public static final int DETAILS_STOP_AT_FIRST_PROJECTOR = 1; // stop creating views at content projecting page items.

	private final static long serialVersionUID = 283037994125026222L;

	// view change event
	public static class Event
	{
		public static int STRUCTURAL_CHANGE_NONE = 0;
		public static int STRUCTURAL_CHANGE_ADD = 1;
		public static int STRUCTURAL_CHANGE_REMOVE = 2;
		public static int STRUCTURAL_CHANGE_REORDER = 3;

		CoPageItemView m_source;
		boolean m_boundsChanged;
		int m_structuralChange;
		CoShapePageItemView m_child;
		int m_index;

		public Event()
		{
			reset();
		}
		
		public CoPageItemView getSource()
		{
			return m_source;
		}
		
		public boolean didBoundsChange()
		{
			return m_boundsChanged;
		}
		
		public int getStructuralChange()
		{
			return m_structuralChange;
		}
		
		public CoShapePageItemView getChild()
		{
			return m_child;
		}
		
		public int getIndex()
		{
			return m_index;
		}

		public void reset()
		{
			m_source = null;
			m_boundsChanged = false;
			m_structuralChange = STRUCTURAL_CHANGE_NONE;
			m_child = null;
			m_index = -1;
		}
	}

	// interface for view listeners
	public interface Listener
	{
		void viewChanged( Event ev );
	}



public abstract CoPageItemViewRenderer createRenderer( CoPageItemViewRendererFactory f );
// cleanup, must be called when view is discarted

public void dispose()
{
}


public CoImmutableBaseLineGridIF getBaseLineGrid()
{
	return null;
}

public abstract Container getContainer();

public CoImmutableColumnGridIF getColumnGrid()
{
	return null;
}
// the cursor used on this view when the content tool is active

public Cursor getContentCursor()
{
	return getSelectionCursor();
}
// Graphics2D instance used for hit testing

protected abstract Graphics2D getHitTestGraphics();
// fetch page item state from server

protected CoPageItemIF.State getModelData( boolean supplyViewState )
{
	CoPageItemIF pi = getPageItem();
	
	if
		( supplyViewState )
	{
		CoPageItemIF.ViewState viewState = getViewState();
		if ( viewState != null ) prepare( viewState );
		return pi.getState( viewState );
	} else {
		return pi.getState( null );
	}
}
// the cursor used on this view when move tool is active

public Cursor getMoveCursor()
{
	return Cursor.getPredefinedCursor( Cursor.MOVE_CURSOR );
}
// the model of the view

public abstract CoPageItemIF getPageItem();
public static CoShapePageItemView [] getPath( CoShapePageItemView from, CoShapePageItemView to )
{
	return getPath( from, to, 0 );
}
private static CoShapePageItemView [] getPath( CoShapePageItemView from, CoShapePageItemView to, int i )
{
	if
		( from == to )
	{
		CoShapePageItemView [] a = new CoShapePageItemView [ i + 1 ];
		a[ 0 ] = to;
		return a;
	}

	CoShapePageItemView parent = from.getParent();

	if ( parent == null ) return null;
	
	CoShapePageItemView [] a = getPath( parent, to, i + 1 );
	a[ a.length - 1 - i ] = from;

	return a;
}
// key used to map this view to a popup menu

public Object getPopupMenuKey()
{
	return getClass();
}
protected abstract CoPageItemViewRenderer getRenderer();
// the cursor used on this view when reshape tool is active

public Cursor getReshapeCursor()
{
	return Cursor.getPredefinedCursor( Cursor.HAND_CURSOR );
}
// the cursor used on this view when create tool is active

public Cursor getRubberbandCreateCursor()
{
	return Cursor.getPredefinedCursor( Cursor.CROSSHAIR_CURSOR );
}
// current display scale

protected abstract double getScreenScale();
// the cursor used on this view when selection tool is active

public Cursor getSelectionCursor()
{
	return Cursor.getDefaultCursor();
}






// hook for collecting view state, see getModelData

protected void prepare( CoPageItemIF.ViewState s )
{
}
// force update of page item state cache

public void refresh()
{
	modelChanged( getModelData( false ), CoPageItemViewClientUtilities.m_event );
}

public abstract void setRenderer( CoPageItemViewRendererFactory f );
// page item view visitor engine

public abstract boolean visit( CoPageItemViewVisitor visitor );












protected Color getIconBackgroundColor()
{
	return Color.white;
}

protected abstract Shape getIconShape();

// return the state of the view that is relevant when fetching page item state

protected CoPageItemIF.ViewState getViewState()
{
	return null;
}

protected void paintContentIcon( Graphics2D g )
{
}

// paint icon

protected void paintIcon( Graphics2D g )
{
	Object tmp = g.getRenderingHint( RenderingHints.KEY_ANTIALIASING );
	g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

	Shape s = getIconShape();
	if
		( s != null ) 
	{
		g.setStroke( new BasicStroke( 0.7f ) );
		
		g.setColor( getIconBackgroundColor() );
		g.fill( s );

		Shape clip = g.getClip();
		g.setClip( s );

		g.setColor( Color.black );
		paintContentIcon( g );
		
		g.setClip( clip );
		
		g.setColor( Color.black );
		g.draw( s );

		
		g.setColor( Color.red );
		
		if
			( isProjectingContent() )
		{
			g.fillRect( CoPageItemViewClientUtilities.m_iconX, CoPageItemViewClientUtilities.m_iconY, 5, 5 );
		}

		if
			( isAcceptingWorkPiece() )
		{
			g.fillRect( CoPageItemViewClientUtilities.m_iconX, CoPageItemViewClientUtilities.m_iconHeight - CoPageItemViewClientUtilities.m_iconY - 4, 5, 5 );
		}

		if
			( isAttachedToWorkPiece() )
		{
			g.fillRect( CoPageItemViewClientUtilities.m_iconWidth - CoPageItemViewClientUtilities.m_iconX - 4, CoPageItemViewClientUtilities.m_iconHeight - CoPageItemViewClientUtilities.m_iconY - 4, 5, 5 );
		}

	}

	g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, tmp );
}


// used for icon rendering

protected boolean isAcceptingWorkPiece()
{
	return false;
}


// used for icon rendering

protected boolean isAttachedToWorkPiece()
{
	return false;
}


// used for icon rendering

protected boolean isProjectingContent()
{
	return false;
}

// see create( CoShapePageItemIF pi, CoPageItemViewRendererFactory f, int detailMode  )

public static CoShapePageItemView create( CoShapePageItemIF pi, int detailMode )
{
	return create( pi, CoDefaultPageItemViewRendererFactory.INSTANCE, detailMode );
}

// The only proper way for client side code to create views for a page item structure.

public static CoShapePageItemView create( CoShapePageItemIF pi, CoPageItemViewRendererFactory f, int detailMode )
{
	if ( f == null ) return create( pi, detailMode  );

	CoShapePageItemView v = pi.createView_shallBeCalledBy_CoPageItemView_only( detailMode );

	v.prepareForClient();
	v.setRenderer( f );

	return v;
}

public CoImmutableCustomGridIF getCustomGrid()
{
	return null;
}

public int getIconHeight()
{
	return CoPageItemViewClientUtilities.m_iconHeight;
}

public int getIconWidth()
{
	return CoPageItemViewClientUtilities.m_iconWidth;
}

public abstract com.bluebrim.transact.shared.CoRef getPageItemId();

// most views don't handle reparent operations

protected boolean handleReparentTransactions( CoPageItemIF.State state )
{
	return false;
}

// hook for synchronizing page item state cache

public void modelChanged( CoPageItemIF.State d, CoPageItemView.Event ev )
{
}

public final void paint( CoPaintable g )
{
	paint( g, null );
}

// Paint view
// By deferring the actual painting to a renderer object, rendering style can be changed without recreating the views

public void paint( CoPaintable g, Rectangle bounds )
{
	getRenderer().paint( g, this, bounds );
}

public void paintIcon( Component c, Graphics g, int x, int y )
{
	g.translate( x, y );
	CoPageItemView.this.paintIcon( (Graphics2D) g );
	g.translate( -x, -y );
}

protected void prepareForClient()
{
}
}