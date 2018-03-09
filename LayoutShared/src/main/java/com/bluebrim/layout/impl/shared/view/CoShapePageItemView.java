package com.bluebrim.layout.impl.shared.view;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.observable.*;
import com.bluebrim.stroke.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * Proxy for CoShapePageItem.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoShapePageItemView extends CoPageItemView
{
	protected CoPageItemViewRenderer m_renderer;

	protected CoPageItemIF m_pageItem; // page item

	// page item state cache	
	protected double m_x;
	protected double m_y;
	protected CoImmutableTransformIF m_transform;
	protected CoImmutableShapeIF m_shape;
	protected CoImmutableShapeIF m_effectiveShape;
	protected CoImmutableStrokePropertiesIF m_strokeProperties;
	protected boolean m_strokeEffectiveShape;
	protected CoImmutableFillStyleIF m_fillStyle;
	protected CoImmutableRunAroundSpecIF m_runAroundSpec;

	protected String m_name;
	protected boolean m_doRunAround;
	protected boolean m_supressPrintout;
	protected boolean m_layoutFailed;
	protected boolean m_isSlave;
	protected int m_slavePosition;

	// page item rendering stuff
	protected transient CoStrokeRenderer m_strokeRenderer;
	protected transient Paint m_fillPaint;

	// hit testing stuff
	protected transient Graphics2D m_hitTestGraphics;
	protected transient Rectangle m_hitTestRectangle;

	// global bounds (coordinate space of swing component used to paint view)
	protected transient int m_X;
	protected transient int m_Y;
	protected transient int m_W;
	protected transient int m_H;

	protected boolean m_hasSlave;


	private CoPageItemViewAdapter m_adapter;
	protected boolean m_areDimensionsLocked;
	protected CoImmutableSizeSpecIF m_heightSpec;
	protected boolean m_isLocationLocked;
	protected CoImmutableLocationSpecIF m_locationSpec;
	private transient CoChangedObjectListener m_modelListener; // server object listener
	protected CoCompositePageItemView m_parent;
	protected CoImmutableSizeSpecIF m_widthSpec;
	private Collection m_viewListeners; // view listeners

	protected transient int m_D; // outer part of stroke
	protected int m_detailMode = DETAILS_EVERYTHING;
	private double m_height;
	protected final CoRef m_pageItemId; // page item id
	private double m_width;
	private final static long serialVersionUID = -8458954996743855039L;

public CoShapePageItemView( CoPageItemIF pageItem, CoCompositePageItemView parent, CoShapePageItemIF.State d, int detailMode )
{
	m_parent = parent;
	
	m_detailMode = detailMode;
	m_pageItem = pageItem;
	m_pageItemId = ( m_pageItem == null ) ? null : m_pageItem.getId();

	sync( d );
}

// hit test (point in local coordinate space)

protected boolean containsPoint( Point2D point )
{
	float d = m_strokeProperties.getOutsideWidth();

	if
		( m_effectiveShape.isInside( point, m_strokeProperties.getWidth(), d ) )
	{
		return true;
	}

	if
		( m_effectiveShape.isOutside( point, m_strokeProperties.getWidth(), d ) )
	{
		return false;
	}
	
	// hit test on shape
	if
		( m_hitTestGraphics == null )
	{
		m_hitTestGraphics = getHitTestGraphics();

		AffineTransform t = m_hitTestGraphics.getTransform();
		t.setToTranslation( 0, 0 );
		m_hitTestGraphics.setTransform( t );
		
		m_hitTestRectangle = new Rectangle();
	}

	m_hitTestGraphics.setStroke( new BasicStroke( 2 * d ) );
	m_hitTestRectangle.setBounds( (int) ( 0.5 + point.getX() ), (int) ( 0.5 + point.getY() ), 1, 1 );
	boolean hit = m_effectiveShape.hit( m_hitTestGraphics, m_hitTestRectangle );

	return hit;
}

// collect all views that are insode/on a given rectangle

public List findViewsIn( CoRectangleShapeIF s, final boolean partOfIsEnough, final boolean ignoreChildLock )
{
	// shape bounds
	final double X0 = s.getX();
	final double Y0 = s.getY();
	final double X1 = X0 + s.getWidth();
	final double Y1 = Y0 + s.getHeight();

	final Point2D p0 = new Point2D.Double();
	final Point2D p1 = new Point2D.Double();
	final Point2D p2 = new Point2D.Double();
	final Point2D p3 = new Point2D.Double();
	
	class CoHitTestVisitor extends CoPageItemViewVisitor
	{
		public List found = new ArrayList();
		
		private void test( CoShapePageItemView v )
		{
			// get bounds if view
			Rectangle2D r = v.m_shape.getShape().getBounds2D();
			p0.setLocation( r.getX(), r.getY() );
			p1.setLocation( r.getX() + r.getWidth(), r.getY() );
			p2.setLocation( r.getX() + r.getWidth(), r.getY() + r.getHeight() );
			p3.setLocation( r.getX(), r.getY() + r.getHeight() );

			// to global coordinate space
			v.transformToGlobal( p0 );
			v.transformToGlobal( p1 );
			v.transformToGlobal( p2 );
			v.transformToGlobal( p3 );

			// recalc bounds (in case of rotation)
			double x0 = Math.min( Math.min( p0.getX(), p1.getX() ), Math.min( p2.getX(), p3.getX() ) );
			double y0 = Math.min( Math.min( p0.getY(), p1.getY() ), Math.min( p2.getY(), p3.getY() ) );
			double x1 = Math.max( Math.max( p0.getX(), p1.getX() ), Math.max( p2.getX(), p3.getX() ) );
			double y1 = Math.max( Math.max( p0.getY(), p1.getY() ), Math.max( p2.getY(), p3.getY() ) );

			// hit test
			if
				( partOfIsEnough )
			{
				if ( x1 < X0 ) return;
				if ( y1 < Y0 ) return;
				if ( x0 > X1 ) return;
				if ( y0 > Y1 ) return;
			} else {
				if ( x0 < X0 ) return;
				if ( y0 < Y0 ) return;
				if ( x1 > X1 ) return;
				if ( y1 > Y1 ) return;
			}
			
			found.add( v );
		}
		
		public boolean visitLeafView( CoShapePageItemView leaf )
		{
			test( leaf );
			return true;
		}
		
		public boolean visitCompositePageItemView( CoCompositePageItemView composite )
		{
			test( composite );
			return ignoreChildLock || ! composite.areChildrenLocked();
		}
	}

	
	CoHitTestVisitor v = new CoHitTestVisitor();
	visit( v );
	return v.found;
}
public AffineTransform getAffineTransform()
{
	return getAffineTransform(null);
}
public final CoImmutableShapeIF getCoShape()
{
	return m_shape;
}
public final boolean getDoRunAround()
{
	return m_doRunAround;
}
public final CoImmutableShapeIF getEffectiveCoShape()
{
	return m_effectiveShape;
}
public final CoImmutableFillStyleIF getFillStyle()
{
	return m_fillStyle;
}
public double getHeight()
{
	return m_shape.getHeight();
}



public String getName()
{
	return m_name;
}
public CoPageItemIF getPageItem ( )
{
	return m_pageItem;
}
protected CoPageItemViewRenderer getRenderer()
{
	return m_renderer;
}
public double getRotation()
{
	return m_transform.getRotation();
}
public CoImmutableRunAroundSpecIF getRunAroundSpec()
{
	return m_runAroundSpec;
}
public int getSlavePosition()
{
	return m_slavePosition;
}
public boolean getStrokeEffectiveShape()
{
	return m_strokeEffectiveShape;
}
public CoImmutableStrokePropertiesIF getStrokeProperties()
{
	return m_strokeProperties;
}
public boolean getSupressPrintout()
{
	return m_supressPrintout;
}
public CoImmutableTransformIF getTransform()
{
	return m_transform;
}
public double getWidth()
{
	return m_shape.getWidth();
}
public final double getX()
{
	return m_x;
}
public final double getY()
{
	return m_y;
}
public boolean hasSlave()
{
	return m_hasSlave;
}
// is this view a leaf ?

protected boolean isLeaf()
{
	return true;
}
// can this view be moved ?

public boolean isMoveable()
{
	return ! m_isLocationLocked && ! isSlave();
}
// can this view be reshaped ?

public boolean isReshapeable()
{
	return ! m_areDimensionsLocked && ! isSlave();
}
// can this view be selected ?

public boolean isSelectable()
{
	return true;
}
public boolean isSlave()
{
	return m_isSlave;
}


public void setRenderer( CoPageItemViewRendererFactory f )
{
	m_renderer = f.createRenderer( this );
}

/**
 * Syncronize page item state cache
 */
private boolean sync( CoShapePageItemIF.State d )
{
	boolean boundsChanged = ( m_shape == null ) || ( m_width != d.m_shape.getWidth() ) || ( m_height != d.m_shape.getHeight() );
	
	m_x = d.m_x;
	m_y = d.m_y;

	m_isLocationLocked = d.m_isLocationLocked;
	m_areDimensionsLocked = d.m_areDimensionsLocked;
	
	m_transform = d.m_transform;
	m_shape = d.m_shape;
	m_width = m_shape.getWidth();
	m_height = m_shape.getHeight();
	m_effectiveShape = d.m_effectiveShape;

	m_strokeProperties = d.m_strokeProperties;
	m_strokeEffectiveShape = d.m_strokeEffectiveShape;
	m_strokeRenderer = null;

	m_fillStyle = d.m_fillStyle;
	m_fillPaint = null;

	m_runAroundSpec = d.m_runAroundSpec;

	m_locationSpec = d.m_locationSpec;
	m_widthSpec = d.m_widthSpec;
	m_heightSpec = d.m_heightSpec;

	m_name = d.m_name;

	m_doRunAround = d.m_doRunAround;
	m_supressPrintout = d.m_supressPrintout;
	
	m_layoutFailed = d.m_layoutFailed;
	
	updateAbsoluteGeometryCache( getScreenScale() );

	m_isSlave = d.m_isSlave;
	m_hasSlave = d.m_hasSlave;
	m_slavePosition = d.m_slavePosition;

	return boundsChanged;
}




// update global bounds cache
// PENDING: a "invalidate-assure" would be more effective ?

public void updateAbsoluteGeometryCache( double scale )
{
	if ( scale == 0 ) return;

	float d = m_strokeProperties.getOutsideWidth();

	
	if
		( m_transform.isIdentity() )
	{
		// no rotation -> only top-left and bottom-right corners need be considerred
		Point2D p0 = new Point2D.Double( - d, - d );
		Point2D p1 = new Point2D.Double( m_shape.getWidth() + d, m_shape.getHeight() + d );
		transformToGlobal( p0 );
		transformToGlobal( p1 );

		m_X = (int) Math.min( Integer.MAX_VALUE, Math.floor( ( p0.getX() ) * scale ) );
		m_Y = (int) Math.min( Integer.MAX_VALUE, Math.floor( ( p0.getY() ) * scale ) );

		m_W = (int) Math.max( Integer.MIN_VALUE, Math.min( Integer.MAX_VALUE, Math.ceil( ( p1.getX() ) * scale ) ) ) - m_X;
		m_H = (int) Math.max( Integer.MIN_VALUE, Math.min( Integer.MAX_VALUE, Math.ceil( ( p1.getY() ) * scale ) ) ) - m_Y;

		Point2D p = new Point2D.Double( 0, 0 );
		transformToGlobal( p );
		int tmp = (int) Math.min( Integer.MAX_VALUE, Math.floor( ( p.getX() ) * scale ) );
		m_D = (int) Math.abs( tmp - m_X );

		if
			( m_W < 0 )
		{
			m_X += m_W;
			m_W = - m_W;
		}
		
		if
			( m_H < 0 )
		{
			m_Y += m_H;
			m_H = - m_H;
		}
	} else {
		// rotation -> consider all corners
		Point2D p0 = new Point2D.Double( - d, - d );
		Point2D p1 = new Point2D.Double( m_shape.getWidth() + d, - d );
		Point2D p2 = new Point2D.Double( m_shape.getWidth() + d, m_shape.getHeight() + d );
		Point2D p3 = new Point2D.Double( - d, m_shape.getHeight() + d );
		transformToGlobal( p0 );
		transformToGlobal( p1 );
		transformToGlobal( p2 );
		transformToGlobal( p3 );
		
		m_X = (int) Math.round( Math.min( Math.min( p0.getX(), p1.getX() ), Math.min( p2.getX(), p3.getX() ) ) * scale );
		m_Y = (int) Math.round( Math.min( Math.min( p0.getY(), p1.getY() ), Math.min( p2.getY(), p3.getY() ) ) * scale );
		m_W = (int) Math.round( Math.max( Math.max( p0.getX(), p1.getX() ), Math.max( p2.getX(), p3.getX() ) ) * scale ) - m_X;
		m_H = (int) Math.round( Math.max( Math.max( p0.getY(), p1.getY() ), Math.max( p2.getY(), p3.getY() ) ) * scale ) - m_Y;
		m_D = -1;
	}

}






protected Shape getIconShape()
{
	return (Shape) CoPageItemViewClientUtilities.m_iconShapes.get( m_shape.getFactoryKey() );
}

protected CoPageItemIF.ViewState getViewState()
{
	return CoPageItemViewClientUtilities.m_pageItemViewState;
}

boolean validateAddTo( CoCompositePageItemView parent, boolean isDirectParent )
{
	return true;//( m_parentConstraint == null ) || m_parentConstraint.validateAddTo( parent, isDirectParent );
}

boolean validateRemoveFrom( CoCompositePageItemView parent, boolean isDirectParent )
{
	return true;//( ( m_parentConstraint == null ) || m_parentConstraint.validateRemoveFrom( parent, isDirectParent ) );
}











public void addViewListener( CoPageItemView.Listener l )
{
	if
		( m_viewListeners == null )
	{
		m_viewListeners = new ArrayList();
	}

	if
		( ! m_viewListeners.contains( l ) )
	{
		m_viewListeners.add( l );
	}
}

public boolean areDimensionsLocked()
{
	return m_areDimensionsLocked;
}

// create and return server object listener
// Should only be called when regestering server object listener

public CoChangedObjectListener createPageItemListener()
{
	// consistence check: listener already present
	CoAssertion.assertTrue( ( m_modelListener == null ), "Illegal call to " + getClass() + ".getPageItemListener()" );
	
	m_modelListener = 
		new CoAbstractChangedObjectListener()
		{
			public void changedServerObject( CoChangedObjectEvent e )
			{
				CoShapePageItemView.this.handlePageItemChange( e );
			}
		};
		
	return m_modelListener;
}

// find the top most view containing a given point
// maxDepth: -1 -> infinity

public CoShapePageItemView findTopMostViewContaining( Point2D point, List exclusionSet, boolean excludeLeafs, boolean ignoreChildLock, int maxDepth )
{
	// check filters
	if ( excludeLeafs && isLeaf() ) return null;
	if ( ( exclusionSet != null ) && ( exclusionSet.contains( this ) ) ) return null;

	// hit test
	transformFromParent( point );
	boolean hit = containsPoint( point );
	transformToParent( point );

	return hit ? this : null;
}

// notify view listeners of view state change

protected final void fireViewChanged()// CoPageItemView source, boolean boundsChanged, int structuralChange, CoShapePageItemView child, int index )
{
	if
		( m_viewListeners != null )
	{
		Iterator i = m_viewListeners.iterator();
		while
			( i.hasNext() )
		{
			( (CoPageItemView.Listener) i.next() ).viewChanged( CoPageItemViewClientUtilities.m_event );//source, boundsChanged, structuralChange, child, index );
		}
	}

}

// notify view listeners of view state change

protected final void fireViewChanged( CoPageItemView source, boolean boundsChanged, int structuralChange, CoShapePageItemView child, int index )
{
	CoPageItemViewClientUtilities.m_event.reset();
	
	CoPageItemViewClientUtilities.m_event.m_source = source;
	CoPageItemViewClientUtilities.m_event.m_boundsChanged = boundsChanged;
	CoPageItemViewClientUtilities.m_event.m_structuralChange = structuralChange;
	CoPageItemViewClientUtilities.m_event.m_child = child;
	CoPageItemViewClientUtilities.m_event.m_index = index;

	fireViewChanged();
}

public CoPageItemViewAdapter getAdapter()
{
	return m_adapter;
}

// Return and clear server object listener
// Should only be called when  unregestering server object listener

public CoChangedObjectListener getAndClearPageItemListener()
{
	// consistence check: attempt to remove nonexistant listener
	CoAssertion.assertTrue( ( m_modelListener != null ),
		                                         "Illegal call to " + getClass() + ".getAndClearPageItemListener()" );

	
	CoChangedObjectListener tmp = m_modelListener;
	m_modelListener = null;
	return tmp;
}

public Container getContainer()
{
	if
		( m_adapter != null )
	{
		return m_adapter.getContainer( this );
	} else  if
		( m_parent != null )
	{
		return m_parent.getContainer(); // delegate to parent
	} else {
		return null;
	}
}

public CoImmutableSizeSpecIF getHeightSpec()
{
	return m_heightSpec;
}

protected Graphics2D getHitTestGraphics()
{
	Graphics2D g = ( m_parent != null ) ? g = m_parent.getHitTestGraphics() : null;
	
	if
		( ( g != null ) && ( m_adapter != null ) )
	{
		m_adapter.prepareHitTestGraphicsFor( g, this );
	}

	return g;
}

public CoImmutableLocationSpecIF getLocationSpec()
{
	return m_locationSpec;
}

public CoCustomGridIF getMutableCustomGrid()
{
	return null;
}

public CoCompositePageItemView getParent ( )
{
	return m_parent;
}

// delegate to parent

protected double getScreenScale()
{
	return ( m_parent == null ) ? 1 : m_parent.getScreenScale();
}

public CoShapePageItemIF getShapePageItem()
{
	return (CoShapePageItemIF) getPageItem();
}

public CoImmutableSizeSpecIF getWidthSpec()
{
	return m_widthSpec;
}

// Handle page item change notification

private void handlePageItemChange( CoChangedObjectEvent e )
{
	// get page item state
	CoShapePageItemIF.State s = (CoShapePageItemIF.State) getModelData( true );

	if
		( DO_TRACE_NOTIFICATIONS )
	{
		System.err.println( "#####################################" );
		System.err.println( "### Page Item Change Notification ###" );
		System.err.println( getShapePageItem().getName() + " : " + this ); // which page item changed
		if
			( s.m_reasons.length() < 2 )
		{
			System.err.println( "       WARNING WARNING WARNING" );
			System.err.println( "      WARNING WARNING WARNING" );
			System.err.println( "     WARNING WARNING WARNING" );
			System.err.println( "    WARNING WARNING WARNING" );
			System.err.println( "   WARNING WARNING WARNING" );
			System.err.println( "  WARNING WARNING WARNING" );
			System.err.println( " WARNING WARNING WARNING" );
			System.err.println( "WARNING WARNING WARNING" );
			System.err.println( "  No reasons given, a call to markDirty is missing somewhere" );
		} else {
			System.err.println( "Reasons:\n" + s.m_reasons );                            // what changed
		}
		System.err.println( "#####################################" );
	}


	// check for reparent operation
	handleReparentTransactions( s );

	repaint();

	// sync page item state cache
	CoPageItemViewClientUtilities.m_event.reset();
	CoPageItemViewClientUtilities.m_event.m_source = this;
	modelChanged( s, CoPageItemViewClientUtilities.m_event );

	// request repaint
	repaint();

	// notify view listeners
	fireViewChanged();
}



// is this view (grand^n)parent of v ?

public boolean isChildOf( CoShapePageItemView v )
{
	// traverse parent path
	CoShapePageItemView p = getParent();
	while
		( p != null )
	{
		if ( p == v ) return true; // v found
		p = p.getParent();
	}

	return false;
}

public boolean isLocationLocked()
{
	return m_isLocationLocked;
}

public void modelChanged( CoPageItemIF.State d, CoPageItemView.Event ev )
{
	super.modelChanged( d, ev );
	
	ev.m_boundsChanged |= sync( (CoShapePageItemIF.State) d );
}

public void paint( CoPaintable g, Rectangle bounds )
{
	if ( m_adapter != null ) m_adapter.prePaint( this, g, bounds );
	
	super.paint( g, bounds );

	if ( m_adapter != null ) m_adapter.postPaint( this, g, bounds );

}

// delegate to parent

protected void postHandleReparent()
{
	if ( m_parent != null ) m_parent.postHandleReparent();
}

// delegate to parent

protected void preHandleReparent()
{
	if ( m_parent != null ) m_parent.preHandleReparent();
}

public void removeViewListener( CoPageItemView.Listener l )
{
	if
		( m_viewListeners != null )
	{
		m_viewListeners.remove( l );
	}
}

public void setAdapter( CoPageItemViewAdapter a )
{
	m_adapter = a;
}

public void setParent( CoCompositePageItemView parent )
{
	m_parent = parent;

	updateAbsoluteGeometryCache( getScreenScale() );
}

public Point2D snap( double x, double y, double width, double height, double range, Point2D delta )
{
	return getColumnGrid().snap( x, y, width, height, range, delta );
}

public Point2D snap( double x, double y, double range, int edgeMask, int dirMask, boolean useEdges, Point2D delta )
{
	return getColumnGrid().snap( x, y, range, edgeMask, dirMask, useEdges, delta );
}

// transform p to the coordinate space of this view from the global coordinate space

public void transformFromGlobal( Point2D p )
{
	if ( getParent() != null ) getParent().transformFromGlobal( p );
	transformFromParent( p );
}

// transform p to the coordinate space of this view from the coordinate space of its parent

protected void transformFromParent( Point2D p )
{
	if
		( m_adapter != null )
	{
		m_adapter.transformFromParent( this, p );
	} else {
		p.setLocation( p.getX() - m_x, p.getY() - m_y );
	}

	if ( m_transform != null ) m_transform.untransform( p );
}

// transform p from the coordinate space of this view to the global coordinate space

public void transformToGlobal( Point2D p )
{
	transformToParent( p );
	if ( getParent() != null ) getParent().transformToGlobal( p );
}

// transform p from the coordinate space of this view to the coordinate space of its parent

protected void transformToParent( Point2D p )
{
	if ( m_transform != null ) m_transform.transform( p );
	
	if
		( m_adapter != null )
	{
		m_adapter.transformToParent( this, p );
	} else {
		p.setLocation( p.getX() + m_x, p.getY() + m_y );
	}
}

public AffineTransform getAffineTransform(CoShapePageItemView useAsReference)
{
	AffineTransform a;

	if
		( this == useAsReference )
	{
		return new AffineTransform();
	}
	
	if 
		( getParent() != null )
	{	
		a = getParent().getAffineTransform();
	} else {
		a = new AffineTransform();
	}

	a.translate( m_x, m_y );
	
	if
		( m_adapter != null )
	{
		m_adapter.prepareAffineTransformFor( a, this );
	}
		
	m_transform.applyOn( a );

	return a;
}


// Traverse parent path and return first ancestor that is an instance of the supplied class.

public CoShapePageItemView getAncestor( Class c )
{
	if
		( c.isInstance( this ) )
	{
		return this;
	} else {
		return ( m_parent == null ) ? null : m_parent.getAncestor( c );
	}
}

public CoRef getPageItemId()
{
	return m_pageItemId;
}

// delegate to parent

protected void handleReparent( CoShapePageItemIF child, CoRef childId, CoCompositePageItemIF newParent, CoRef newParentId, CoCompositePageItemIF oldParent, CoRef oldParentId )
{
	if ( m_parent != null ) m_parent.handleReparent( child, childId, newParent, newParentId, oldParent, oldParentId );
}

public final void repaint()
{
	repaint( m_X, m_Y, m_W, m_H, 0 );
}

public void repaint( int x, int y, int w, int h, int frameWidth )
{
	if
		( m_parent != null )
	{
		m_parent.repaint( x, y, w, h, frameWidth );
	}
}

protected void repaintAll()
{
	if ( m_parent != null ) m_parent.repaintAll();
}

public final void repaintFrame( int frameWidth )
{
	int x = m_X;
	int y = m_Y;
	int w = m_W;
	int h = m_H;
	
	if
		( true || ! repairFrameOnly() ) // PENDING: disabled due to bug
	{
		frameWidth = 0; // force repaint of entire view
		
	} else if
		( frameWidth != 0 )
	{
		// frame only repaint requested
		if
			( m_D < 0 )
		{
			// m_D less than 0 indicates rotation -> force repaint of entire view
			frameWidth = 0;
		} else if
			( frameWidth < m_D )
		{
			// repaint frame only -> remove outside width effect
			x += m_D;
			y += m_D;
			w -= 2 * m_D;
			h -= 2 * m_D;
		}
	}

	repaint( x, y, w, h, frameWidth );
}

protected boolean repairFrameOnly()
{
	return false;
}
}