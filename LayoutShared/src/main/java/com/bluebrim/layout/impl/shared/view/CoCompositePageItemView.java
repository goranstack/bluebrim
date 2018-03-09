package com.bluebrim.layout.impl.shared.view;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.layoutmanager.*;
import com.bluebrim.layout.shared.*;

/**
 * Proxy for CoLayoutArea.
 * 
 * @author: Dennis Malmström
 */
 
public abstract class CoCompositePageItemView extends CoShapePageItemView
{


	// layout area state cache
	protected List m_children;
	protected int [] m_layoutOrder;
	protected CoImmutableLayoutManagerIF m_layoutManager;
	protected int m_reparentTransactionRecordId = -1;
		
	protected double m_width;
	protected double m_height;

	protected CoImmutableColumnGridIF m_columnGrid;
	protected CoImmutableBaseLineGridIF m_baseLineGrid;

	protected com.bluebrim.text.shared.CoBaseLineGeometryIF m_baseLineGeometry;
	protected CoImmutableShapeIF m_clipping;

	// page item rendering stuff
	protected transient CoColumnGridRenderer m_columnGridRenderer;
	protected transient Shape m_clipShape;
	

;







// add a child view att a certain position

public void add( int i, CoShapePageItemView shapeItemView)
{
	CoCompositePageItemIF parent = (CoCompositePageItemIF) m_pageItem;

	// since we can't be sure that the client cached child order is valid this is the best we can do
	// the child order will be synchronized by a call to sync.
	int n = 0;
	int N = m_children.size();
	for
		( ; n < N; n++ )
	{
		int index = parent.getIndexOfChild( ( (CoShapePageItemView) m_children.get( n ) ).getShapePageItem() );
		if ( index == -1 ) continue;
		if ( i > index ) break;
	}
	i = n;
	
	m_children.add( i, shapeItemView );
	shapeItemView.setParent( this );
}
// add a child view

public void add( CoShapePageItemView shapeItemView )
{
	m_children.add( shapeItemView );
	shapeItemView.setParent( this );
}
public boolean areChildrenLocked()
{
	return m_areChildrenLocked;
}
public CoPageItemViewRenderer createRenderer( CoPageItemViewRendererFactory f )
{
	return f.create( this );
}
// propagate to children

public void dispose()
{
	super.dispose();

	disposeChildren();
}

public CoImmutableBaseLineGridIF getBaseLineGrid()
{
	return m_baseLineGrid;
}

public int getChildCount()
{
	return m_children.size();
}
public List getChildren()
{
	return m_children;
}
protected Shape getClipping()
{
	if
		( m_clipShape == null )
	{
		m_clipShape = m_clipping.getShape();
	}
	
	return m_clipShape;
}
public CoImmutableColumnGridIF getColumnGrid()
{
	return m_columnGrid;
}

public int getIndexOfChild( CoPageItemView v )
{
	return m_children.indexOf( v );
}
public CoPageItemView getLayoutChildAt( int i )
{
	return (CoPageItemView) ( ( m_layoutOrder != null ) ? m_children.get( m_layoutOrder[ i  ] ) : null );
}
public int getLayoutChildCount()
{
	return ( m_layoutOrder != null ) ? m_layoutOrder.length : 0;
}
public int getLayoutIndexOfChild( CoPageItemView v )
{
	if ( m_layoutOrder == null ) return -1;
	
	int I = m_layoutOrder.length;
	for
		( int i = 0; i < I; i++ )
	{
		if
			( m_children.get( m_layoutOrder[ i ] ) == v )
		{
			return i;
		}
	}

	return -1;
}
public CoImmutableLayoutManagerIF getLayoutManager()
{
	return m_layoutManager;
}


public boolean hasChildren()
{
	return m_children.size() > 0;
}
protected boolean isLeaf()
{
	return false;
}
public void lockChildren( boolean b )
{
//	m_childLock = b;
}

protected void prepare( CoPageItemIF.ViewState s )
{
	super.prepare( s );
	( (CoCompositePageItemIF.ViewState) s ).m_reparentOperationRecordID = m_reparentTransactionRecordId;
}
// propagate to children

public void refresh()
{
	super.refresh();
	
	Iterator e = m_children.iterator();
	while
		( e.hasNext() )
	{
		CoShapePageItemView v = (CoShapePageItemView) e.next();
		v.refresh();
	};
}
// remove child view

public void remove( CoShapePageItemView v )
{
	m_children.remove( v );
	v.setParent( null );
}

public void setRenderer( CoPageItemViewRendererFactory f )
{
	super.setRenderer( f );
	
	Iterator e = m_children.iterator();
	while
		( e.hasNext() )
	{
		CoShapePageItemView v = (CoShapePageItemView) e.next();
		v.setRenderer( f );
	};
}
// called by page item right after creation to aviod having to flush old reparent records

public void setReparentTransactionRecordId( int reparentTransactionRecordId )
{
	m_reparentTransactionRecordId = reparentTransactionRecordId;
}


// propagate to children

public void updateAbsoluteGeometryCache( double scale )
{
	super.updateAbsoluteGeometryCache( scale );

	if
		( m_children != null )
	{
		Iterator e = m_children.iterator();
		while
			( e.hasNext() )
		{
			( (CoShapePageItemView) e.next() ).updateAbsoluteGeometryCache( scale );
		};
	}
}
// check if a child can be added to this layout area

public boolean validateAdd( CoShapePageItemView pageItemView )
{
	/*
	if
		( ( m_childConstraint != null ) && ! m_childConstraint.validateAdd( pageItemView ) )
	{
		return false;
	}
*/
	if
		( ! pageItemView.validateAddTo( this, true ) )
	{
		return false;
	}

	return true;
}

// check if a child can be removed from this layout area

public boolean validateRemove( CoShapePageItemView pageItemView )
{
	/*
	if
		( ( m_childConstraint != null ) && ! m_childConstraint.validateRemove( pageItemView ) )
	{
		return false;
	}
*/

	if
		( ! pageItemView.validateRemoveFrom( this, true ) )
	{
		return false;
	}

	return true;
}

// propagate to children

public boolean visit( CoPageItemViewVisitor visitor )
{
	if
		( visitor.visitCompositePageItemView( this ) )
	{
		Iterator e = m_children.iterator();
		while
			( e.hasNext() )
		{
			if ( ! ( (CoPageItemView) e.next() ).visit( visitor ) ) return false;
		};

		return true;
	}

	return false;
}



protected CoPageItemIF.ViewState getViewState()
{
	return CoPageItemViewClientUtilities.m_compositePageItemViewState;
}



// propagate to children

boolean validateAddTo( CoCompositePageItemView parent, boolean isDirectParent )
{
	if
		( ! super.validateAddTo( parent, isDirectParent ) )
	{
		return false;
	}

	Iterator i = m_children.iterator();
	while
		( i.hasNext() )
	{
		if
			( ! ( (CoShapePageItemView) i.next() ).validateAddTo( parent, false ) )
		{
			return false;
		}
	}

	return true;
}

// propagate to children

boolean validateRemoveFrom( CoCompositePageItemView parent, boolean isDirectParent )
{
	if
		( ! super.validateRemoveFrom( parent, isDirectParent ) )
	{
		return false;
	}

	Iterator i = m_children.iterator();
	while
		( i.hasNext() )
	{
		if
			( ! ( (CoShapePageItemView) i.next() ).validateRemoveFrom( parent, false ) )
		{
			return false;
		}
	}

	return true;
}

	protected boolean m_areChildrenLocked;
	private final static long serialVersionUID = -8835505460734249836L;

public CoCompositePageItemView( CoPageItemIF pageItem, CoCompositePageItemView parent, CoCompositePageItemIF.State d, int detailMode )
{
	super( pageItem, parent, d, detailMode );
	
	sync( d, null );
}

protected void disposeChildren()
{
	Iterator e = m_children.iterator();
	while
		( e.hasNext() )
	{
		( (CoShapePageItemView) e.next() ).dispose();
	};
}

// find the top most view containing a given point
// maxDepth: -1 -> infinity

public CoShapePageItemView findTopMostViewContaining( Point2D point, List exclusionSet, boolean excludeLeafs, boolean ignoreChildLock, int maxDepth )
{
	// check this view
	CoShapePageItemView v = super.findTopMostViewContaining( point, exclusionSet, excludeLeafs, ignoreChildLock, maxDepth );
	if ( ! ignoreChildLock && areChildrenLocked() ) return v;
	if ( maxDepth == 0 ) return v;

	
	if
		( v != null )
	{
		// inside this view -> check children
		transformFromParent( point );

		int I = m_children.size();
		for
			( int i = I - 1; i >= 0; i-- )
		{
			CoShapePageItemView c = ( (CoShapePageItemView) m_children.get( i ) ).findTopMostViewContaining( point, exclusionSet, excludeLeafs, ignoreChildLock, maxDepth - 1 );
			if
				( c != null )
			{
				v = c;
				break;
			}
		}
	
		transformToParent( point );
	}
	
	return v;
}

public CoShapePageItemView getChildAt( int i )
{
	return (CoShapePageItemView) m_children.get( i );
}

public CoCompositePageItemIF getCompositePageItem()
{
	return (CoCompositePageItemIF) getPageItem();
}

protected boolean handleReparentTransactions( CoPageItemIF.State state )
{
	CoReparentOperationRecord [] reparentOperationRecords = ( (CoCompositePageItemIF.State) state ).m_reparentOperationRecords;
	if ( reparentOperationRecords == null ) return false;

	preHandleReparent();
	
	for
		( int i = 0; i < reparentOperationRecords.length; i++ )
	{
		CoReparentOperationRecord r = reparentOperationRecords[ i ];
		m_reparentTransactionRecordId = r.getId();
		handleReparent( (CoShapePageItemIF) r.getChild(), r.getChildId(), r.getNewParent(), r.getNewParentId(), r.getOldParent(), r.getOldParentId() );
	}

	postHandleReparent();

	return true;
}

public void modelChanged( CoPageItemIF.State d, CoPageItemView.Event ev )
{
	super.modelChanged( d, ev );
	
	sync( (CoCompositePageItemIF.State) d, ev );
}

// propagate to children

protected void prepareForClient()
{
	super.prepareForClient();

	Iterator e = m_children.iterator();
	while
		( e.hasNext() )
	{
		( (CoShapePageItemView) e.next() ).prepareForClient();
	};
}

protected boolean repairFrameOnly()
{
	return m_children.size() > 20;
}

private boolean resortChildViews( List childrenIds )
{
	boolean dirty = false;
	
	int I = m_children.size();
	for
		( int i = 0; i < I; i++ )
	{
		com.bluebrim.transact.shared.CoRef childId = (com.bluebrim.transact.shared.CoRef) childrenIds.get( i );

		for
			( int n = i + 1; n < I; n++ )
		{
			if
				( ( (CoPageItemView) m_children.get( n ) ).getPageItemId().equals( childId ) )
			{
				Object tmp = m_children.get( n );
				m_children.set( n, m_children.get( i ) );
				m_children.set( i, tmp );
				dirty = true;
				break;
			}

			// not found
		}
	}

	return dirty;
}

/**
 * Mottagaren fyller i sina instansvariabler genom 
 * att fråga ut sidelementet i argumentet.
 */
private void sync( CoCompositePageItemIF.State d, CoPageItemView.Event ev )
{
//	CoCompositePageItemIF pi = (CoCompositePageItemIF) m_pageItem;

	m_areChildrenLocked = d.m_areChildrenLocked;
	
	m_layoutManager = d.m_layoutManager;

	if
		( m_children == null )
	{
		// first call, child views are added later
		m_children = new ArrayList();
		m_layoutOrder = d.m_layoutOrder;
		
	} else if
		( d.m_childrenIds.size() == m_children.size() )
	{
		m_layoutOrder = d.m_layoutOrder;

		int I = d.m_childrenIds.size();
		for
			( int i = 0; i < I; i++ )
		{
			if
				( ! ( (CoPageItemView) m_children.get( i ) ).getPageItemId().equals( d.m_childrenIds.get( i ) ) )
			{
				// child order changed
				if
					( resortChildViews( d.m_childrenIds ) )
				{
					ev.m_structuralChange = Event.STRUCTURAL_CHANGE_REORDER;
				}
				break;
			}
		}
		
	} else {
		// view and page item children set are out of sync -> disable layout order
		m_layoutOrder = null;
	}

	
	
	
	m_width = m_shape.getWidth();
	m_height = m_shape.getHeight();
	
	m_columnGrid = d.m_columnGrid;
	m_columnGridRenderer = null;

	m_baseLineGrid = d.m_baseLineGrid;
	m_baseLineGeometry = m_baseLineGrid.getBaseLineGeometry( 0 );

	m_clipping = d.m_clipping;
	m_clipShape = null;
}
}