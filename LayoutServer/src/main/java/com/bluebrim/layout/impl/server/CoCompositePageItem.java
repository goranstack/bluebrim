package com.bluebrim.layout.impl.server;

import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.server.manager.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.layoutmanager.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * A layout area that has children (other page items).
 * The children are sorted in two ways:
 *  - The Z order e.g. the order in which the children are painted.
 *  - The layout order e.g. the order in which the layout engine lays out the children.
 * 
 * @author: Dennis Malmström
 */
 
public abstract class CoCompositePageItem extends CoShapePageItem implements CoCompositePageItemIF, CoLayoutableContainerIF
{
	// children
	protected List m_children = new ArrayList(); // Z order,  [ CoShapePageItem ]
	protected List m_layoutChildren = new ArrayList(); // layout order, [ CoShapePageItem ], INVARIANT: m_layoutChildren.contains( x ) <-> m_children.contains( x )
	private transient int [] m_layoutOrder; // INVARIANT: m_children[ m_layoutOrder[ n ] ] == m_layoutChildren[ n ]



	// layout manager stuff
	protected CoLayoutManager m_layoutManager = createDefaultLayoutManager();
	private CoLayoutManager.Owner m_layoutManagerOwner; // Implementation of "mutable proxy" pattern





	// layout engine stuff
	protected transient boolean m_childrenLayoutIsInProgress = false; // is layout engine laying out the children

	// reparent notification stuff
	private List m_reparentOperationRecords;
	private static final int m_maxReparentOperationRecordID = Integer.MAX_VALUE;
	private static final int m_reparentOperationRecordBufferLimit = 100;

	
	private CoLayoutManagerIF m_mutableLayoutManagerProxy = createMutableLayoutManagerProxy(); // Implementation of "mutable proxy" pattern

	// child add/remove lock (this lock is not enforced, only carried, by this class)
	protected boolean m_areChildrenLocked;
	/* REPARENT NOTIFICATION
		Every time a child is added to or removed from a layout area a record describing the operation is stored in m_reparentOperationRecords (see addReparentOperationRecord).
		Each such record has a unique and order defining integer id.
		When a client receives a change notifiaction for a layout area it fetches the outstanding reparent records.
		By storing the id of the latest accessed record the client can avoid fetching the same record twice (see getNextReparentOperationRecord).
		Since a layout area has no way of knowing how many clients are pressent it is impossible to decide exactly when a record can be discarded.
		Currently this is solved by limiting the length of the server side record-buffer.
		This method has two drawbacks:
		 - It is theoretically possible for a record to be discarded before all clients have fetched it.
		 - Database space is used for storing records that will never be fetched.
		A much better method would be to send the reparent record aling with the notification, but that is not possible with the notification mechanism currently in use.
	*/

	// xml tag constants
	public static final String XML_CHILDREN_LOCKED = "children-locked";
	public static final String XML_LAYOUT_ORDER = "layout-order";



	private transient List m_childrenIds; // [ CoRef ] ( cached list of children ids, used in getState )


// add some children

public final void add( CoShapePageItemIF pageItems [] )
{
	add( pageItems, null );
}

// add a child

public final void add( CoShapePageItemIF pageItem )
{
	add( pageItem, null );
}

protected void addReparentOperationRecord( CoPageItemIF child, CoCompositePageItemIF oldParent, CoCompositePageItemIF newParent )
{
	int id = 0;

	// calculate new id
	if
		( m_reparentOperationRecords == null )
	{
		m_reparentOperationRecords = new ArrayList();
	} else {
		id = ( (CoPersistantReparentOperationRecord) m_reparentOperationRecords.get( m_reparentOperationRecords.size() - 1 ) ).getId();
		if
			( id == m_maxReparentOperationRecordID )
		{
			id = 0;
		} else {
			id++;
		}
	}

	// create and add record
	m_reparentOperationRecords.add( new CoPersistantReparentOperationRecord( id, child, oldParent, newParent ) );

	// limit buffer size
	while
		( m_reparentOperationRecords.size() > m_reparentOperationRecordBufferLimit )
	{
		m_reparentOperationRecords.remove( 0 );
	}
	
	markDirty( "reparent transaction record added (" + id + ")" );
}
public boolean areChildrenLocked()
{
	return m_areChildrenLocked;
}
// Move child one step back (z-order)

void bringForward( CoShapePageItem child )
{
	int i = m_children.indexOf( child );
	
	if
		( i >= 0 && i < m_children.size() - 1 )
	{
		m_children.add( i + 2, child );
		m_children.remove( i );
		
		doAfterChildOrderChanged();
	}
}
// Move child to last position (z-order)

void bringToFront( CoShapePageItem child )
{
	if
		( m_children.contains( child ) )
	{
		m_children.remove( child );
		m_children.add( child );
		
		doAfterChildOrderChanged();
	}
}
// See CoPageItemPositionIF

public CoPoint2DDouble calculatePosition( CoPageItemPositionIF p, CoShapePageItemIF child )
{
	return p.place( this, child, null );
}
protected Object clone() throws CloneNotSupportedException
{
	CoCompositePageItem clone = (CoCompositePageItem) super.clone();
	
	clone.m_children = new ArrayList();
	clone.m_layoutChildren = new ArrayList();
	clone.m_layoutOrder = null;

	return clone;
}
// is child of ?

public boolean contains( CoShapePageItemIF pi )
{
	return m_children.contains( pi );
}
private CoLayoutManager createDefaultLayoutManager()
{
	CoLayoutManagerFactoryIF f = (CoLayoutManagerFactoryIF) CoFactoryManager.getFactory( CoLayoutManagerIF.LAYOUT_MANAGER );

	return (CoLayoutManager) f.getNoLayoutManager();
}

// See CoPageItem.createState

public CoPageItemIF.State createState()
{
	return new CoCompositePageItemIF.State();
}

protected void deepCopy( CoPageItem copy )
{
	super.deepCopy( copy );
	
	CoCompositePageItem c = (CoCompositePageItem) copy;

	int I = ( m_children != null ) ? m_children.size() : 0;

	List slaves = new ArrayList();
	
	for 
		( int i = 0; i < I; i++ )
	{
		CoShapePageItem child = (CoShapePageItem) m_children.get( i );
		if ( child.isSlave() ) continue;
		
		CoShapePageItem newChild = (CoShapePageItem) child.deepClone();
		c.m_children.add( newChild );
		newChild.postAddTo( c );
		
		if
			( newChild.hasSlave() )
		{
			// save slave and its position
			slaves.add(
				new Object []
				{
					new Integer( getIndexOfChild( child.getSlave() ) ),
					newChild.getSlave()
				}
			);
		}
	}

	// sort slaves
	Collections.sort(
		slaves,
		new Comparator()
		{
			public int compare( Object o1, Object o2 )
			{
				Integer i1 = (Integer) ( (Object[]) o1 )[ 0 ];
				Integer i2 = (Integer) ( (Object[]) o2 )[ 0 ];
				return i1.intValue() - i2.intValue();
			}
		}
	);

	// insert slaves
	Iterator slavesi = slaves.iterator();
	while
		( slavesi.hasNext() )
	{
		Object [] o = (Object[]) slavesi.next();
		CoShapePageItem	child = (CoShapePageItem) o[ 1 ];
		child.setParent( c );
		c.m_children.add( ( (Integer) o[ 0 ] ).intValue(), child );
	}

	// layout order
	for 
		( int i = 0; i < I; i++ )
	{
		int n = getIndexOfChild( (CoShapePageItemIF) m_layoutChildren.get( i ) );
		c.m_layoutChildren.add( i, (CoShapePageItemIF) c.m_children.get( n ) );
	}

	c.invalidateLayoutOrder();
	c.m_reparentOperationRecords = null;

	c.m_layoutManagerOwner = null;
	if
		( m_layoutManager != null )
	{	
		c.m_layoutManager = (CoLayoutManager) m_layoutManager.deepClone();
	}
	c.m_mutableLayoutManagerProxy = c.createMutableLayoutManagerProxy();
}

// add a child
// return slave if present

private final CoShapePageItem doAdd( CoShapePageItem pageItem, boolean useSmartOrdering )
{
	// synchronize layout engine activation state
	pageItem.setLayoutEngineActive( isLayoutEngineActive() );

	CoShapePageItem slave = (CoShapePageItem) pageItem.getSlave();
	
	// remove from previous parent
	CoCompositePageItem oldParent = (CoCompositePageItem) pageItem.getParent();
	if
		( oldParent != null )
	{
		oldParent.remove( pageItem, this );
		if ( slave != null ) oldParent.remove( slave, this );
	}

	int I = m_children.size();
	int i = I;
	
	if
		( useSmartOrdering )
	{
		// calculate z-order position of child
		int n = pageItem.getPositionWeight();
		i = 0;
		
		while
			( i < I )
		{
			if
				( ( (CoShapePageItem) m_children.get( i ) ).getPositionWeight() > n )
			{
				break;
			};
			i++;
		}
	}

	// add to new parent (==this)
	if
		( i == I )
	{
		m_children.add( pageItem );
		if ( slave != null ) m_children.add( slave );
	} else {
		m_children.add( i, pageItem );
		if ( slave != null ) m_children.add( i + 1, slave );
	}
		
	m_layoutChildren.add( pageItem );
	if ( slave != null ) m_layoutChildren.add( slave );

	addReparentOperationRecord( pageItem, oldParent, this );
	if ( slave != null ) addReparentOperationRecord( slave, oldParent, this );

	return slave;
}
protected final void doAfterChildLayoutOrderChanged()
{
	postChildLayoutOrderChanged();

	performLocalLayout();
	
	markDirty( "ChildLayoutOrderChanged" );
}
protected final void doAfterChildOrderChanged()
{
	postChildOrderChanged();

	performLocalLayout();
	
	markDirty( "ChildOrderChanged" );
}
protected final void doAfterChildrenLockedChanged()
{
	postChildrenLockedChanged();
	
	markDirty( "ChildrenLockedChanged" );
}
protected final void doAfterLayoutManagerChanged()
{
	postLayoutManagerChanged();

	performLocalLayout();
	
	markDirty( "LayoutManagerChanged" );
}

protected void doSetLayoutEngineActive( boolean a )
{
	// propagate to children
	Iterator e = m_children.iterator();
	while
		( e.hasNext() )
	{
		( (CoShapePageItem) e.next() ).doSetLayoutEngineActive( a );
	}
	
	super.doSetLayoutEngineActive( a );
}

public Object getAttributeValue( java.lang.reflect.Field d ) throws IllegalAccessException
{
	try
	{
		return d.get( this );
	}
	catch ( IllegalAccessException ex )
	{
		return super.getAttributeValue( d );
	}
}
public CoShapePageItemIF getChildAt( int i )
{
	return (CoShapePageItemIF) m_children.get( i );
}
public int getChildCount()
{
	return m_children.size();
}
public List getChildren()
{
	return m_children;
}
// Minimum height spanning all children

public double getContentHeight()
{
	double y0 = Double.MAX_VALUE; 
	double y1 = 0;
	
	Iterator e = m_children.iterator();
	while
		( e.hasNext() )
	{
		CoShapePageItem pi = (CoShapePageItem) e.next();
		if ( pi.getTopEdge() < y0	) y0 = pi.getTopEdge();
		if ( pi.getBottomEdge() > y1 ) y1 = pi.getBottomEdge();
	};
			
	double h = y1 - y0;
	
	if
		( ! m_columnGrid.isDerived() )
	{
		h += m_columnGrid.getTopMargin() + m_columnGrid.getBottomMargin();
	}

	if ( h < 0 ) h = 0;
		
	return h;
}
// Minimum width spanning all children

public double getContentWidth()
{
	double x0 = Double.MAX_VALUE;
	double x1 = 0;
	
	Iterator e = m_children.iterator();
	while
		(e.hasNext())
	{
		CoShapePageItem pi = (CoShapePageItem) e.next();
		if ( pi.getLeftEdge() < x0	) x0 = pi.getLeftEdge();
		if ( pi.getRightEdge() > x1	) x1 = pi.getRightEdge();
	};
			
	double w = x1 - x0;

	if
		( ! m_columnGrid.isDerived() )
	{
		w += m_columnGrid.getLeftMargin() + m_columnGrid.getRightMargin();
	}

	if ( w < 0 ) w = 0;
		
	return w;
	
}

public int getIndexOfChild( CoShapePageItemIF child )
{
	return m_children.indexOf( child );
}
public CoShapePageItemIF getLayoutChildAt( int i )
{
	return (CoShapePageItemIF) m_layoutChildren.get( i );
}
public List getLayoutChildren()
{
	return m_layoutChildren;
}
public int getLayoutIndexOfChild( CoShapePageItemIF child )
{
	return m_layoutChildren.indexOf( child );
}
public CoImmutableLayoutManagerIF getLayoutManager()
{
	return m_layoutManager;
}
// Note: m_layoutOrder is transient -> it is ok to initialize it like this (lazy)

public int [] getLayoutOrder()
{
	if
		( m_layoutOrder == null )
	{
		// refresh layout order cache
		int I = m_layoutChildren.size();
		m_layoutOrder = new int [ I ];
		for
			( int i = 0; i < I; i++ )
		{
			m_layoutOrder[ i ] = m_children.indexOf( m_layoutChildren.get( i ) );
		}
	}
	
	return m_layoutOrder;
}
public CoLayoutManagerIF getMutableLayoutManager()
{
	return m_mutableLayoutManagerProxy;
}
// return the reparent record with an id succeeding the supplied id

public CoReparentOperationRecord [] getNextReparentOperationRecords( int id )
{
	if ( m_reparentOperationRecords == null ) return null; // no records available
	
	int successorId = ( id == m_maxReparentOperationRecordID ) ? 0 : ( id + 1 );

	int I = m_reparentOperationRecords.size();
	int i = I - 1;

	while
		( ( (CoPersistantReparentOperationRecord) m_reparentOperationRecords.get( i ) ).getId() != successorId )
	{
		i--;
		if
			( i == -1 )
		{
			// successor not found
			return null;
		}
	}

	if ( I == i ) return null;
	
	CoReparentOperationRecord [] tmp = new CoReparentOperationRecord [ I - i ];
	for
		( int n = 0; i < I; i++, n++ )
	{
		tmp[ n ] = new CoReparentOperationRecord( (CoPersistantReparentOperationRecord) m_reparentOperationRecords.get( i ) );
	}
	
	return tmp;
}



public boolean hasChildren()
{
	return getChildCount() > 0;
}
private void invalidateLayoutOrder()
{
	m_layoutOrder = null;
	m_childrenIds = null;
}
public boolean isFirstChild( CoShapePageItemIF child )
{
	return m_children.get( 0 ) == child;
}
public boolean isFirstLayoutChild( CoShapePageItemIF child )
{
	return m_layoutChildren.get( 0 ) == child;
}
public boolean isLastChild( CoShapePageItemIF child )
{
	return m_children.get( m_children.size() - 1 ) == child;
}
public boolean isLastLayoutChild( CoShapePageItemIF child )
{
	return m_layoutChildren.get( m_layoutChildren.size() - 1 ) == child;
}
/*
 * travel up in structure to a CoPageLayerLayoutArea or asume default
 */
public boolean isLeftSide()
{
	return ( m_parent == null ) ? true : m_parent.isLeftSide();
	
}
// Move child to first position (layout-order)

void moveToFirstLayoutPosition( CoShapePageItem child )
{
	if
		( m_layoutChildren.contains( child ) )
	{
		m_layoutChildren.remove( child );
		m_layoutChildren.add( 0, child );
		
		doAfterChildLayoutOrderChanged();
	}
}
// Move child to last position (layout-order)

void moveToLastLayoutPosition( CoShapePageItem child )
{
	if
		( m_layoutChildren.contains( child ) )
	{
		m_layoutChildren.remove( child );
		m_layoutChildren.add( child );
		
		doAfterChildLayoutOrderChanged();
	}
}
// Move child one step towards first position (layout-order)

void moveTowardsFirstLayoutPosition( CoShapePageItem child )
{
	int i = m_layoutChildren.indexOf( child );
	
	if
		( i > 0 )
	{
		m_layoutChildren.remove( i );
		m_layoutChildren.add( i - 1, child );
		
		doAfterChildLayoutOrderChanged();
	}
}
// Move child one step towards last position (layout-order)

void moveTowardsLastLayoutPosition( CoShapePageItem child )
{
	int i = m_layoutChildren.indexOf( child );
	
	if
		( i >= 0 && i < m_layoutChildren.size() - 1 )
	{
		m_layoutChildren.add( i + 2, child );
		m_layoutChildren.remove( i );
		
		doAfterChildLayoutOrderChanged();
	}
}
// See CoShapePageItem

void performLayout( int sizeSpecMask, boolean skipLocation )
{
	if
		( m_layoutSpec.isContentDependent() )
	{
		super.performLayout( CoLayoutSpec.ALL_BUT_CONTENT, skipLocation ); 
		performLayoutOnChildren( false );
		super.performLayout( CoLayoutSpec.CONTENT, skipLocation );
		
	} else {
		
		super.performLayout( CoLayoutSpec.ALL, skipLocation );
		performLayoutOnChildren( false );
	}
}
// run layout engine on children

protected void performLayoutOnChildren( boolean skipLocation )
{
	if ( m_childrenLayoutIsInProgress ) return; // already in progress

	m_childrenLayoutIsInProgress = true;

	Iterator i = getLayoutChildren().iterator();
	while
		( i.hasNext() )
	{
		( (CoShapePageItem) i.next() ).invalidateLayout();
	}

	
	if
		( ! m_layoutManager.isNull() )
	{
		// if layout manager present use it
		if
			( ! m_layoutManager.doesSetSize() )
		{
			i = getLayoutChildren().iterator();
			while
				( i.hasNext() )
			{
				( (CoShapePageItem) i.next() ).performLayout( CoLayoutSpec.ALL, true );
			}
		}

		m_layoutManager.layout( this );
		
	} else {

		// no layout manager present -> run layout engine
		i = getLayoutChildren().iterator();
		while
			( i.hasNext() )
		{
			( (CoShapePageItem) i.next() ).performLayout( CoLayoutSpec.ALL, skipLocation );
		}
	}

	m_childrenLayoutIsInProgress = false;
}

protected void postChildLayoutOrderChanged()
{
	invalidateLayoutOrder();
	
	requestLocalLayout();
	
	updateChildrensEffectiveShapes();
}
protected void postChildOrderChanged()
{
	invalidateLayoutOrder();
}
protected void postChildrenLockedChanged()
{
}
protected void postDoRunAroundChanged()
{
	super.postDoRunAroundChanged();

	updateChildrensEffectiveShapes();
}

protected void postLayoutManagerChanged()
{
	requestLocalLayout();
}

// remove some children

public void remove( CoShapePageItemIF pageItems [] )
{
	remove( pageItems, null );
}
// remove some children

protected void remove( CoShapePageItemIF pageItems [], CoCompositePageItemIF newParent )
{
	boolean didRemove = false;

	for
		( int i = 0; i < pageItems.length; i++ )
	{
		CoShapePageItemIF pi = pageItems[ i ];
		if
			( pi == null )
		{
			continue;
		}
		
		if
			( ! m_children.contains( pi ) )
		{
			continue;
		}

		CoShapePageItemIF slave = pi.getSlave();

		m_children.remove( pi );
		m_layoutChildren.remove( pi );
		addReparentOperationRecord( pi, this, newParent );
		( (CoShapePageItem) pi ).postRemoveFrom( this );

		didRemove = true;

		if
			( slave != null )
		{
			m_children.remove( slave );
			m_layoutChildren.remove( slave );
			addReparentOperationRecord( slave, this, newParent );
			( (CoShapePageItem) slave ).postRemoveFrom( this );
		}

		postSubTreeStructureChange( false, pi, newParent );

		if
			( slave != null )
		{
			postSubTreeStructureChange( false, slave, newParent );
		}
	}

	if
		( didRemove )
	{
		postRemove( null, pageItems );
		postSubTreeStructureChange();
		performLocalLayout();
	}
}
// remove a child

public void remove(CoShapePageItemIF pageItem)
{
	remove( pageItem, null );
}
// remove a child

protected void remove(CoShapePageItemIF pageItem, CoCompositePageItemIF newParent )
{
	if
		( ! m_children.contains( pageItem ) )
	{
		return;
	}

	CoShapePageItemIF slave = pageItem.getSlave();
	
	m_children.remove( pageItem );
	m_layoutChildren.remove( pageItem );
	addReparentOperationRecord( pageItem, this, newParent );
	( (CoShapePageItem) pageItem ).postRemoveFrom( this );

	if
		( slave != null )
	{
		m_children.remove( slave );
		m_layoutChildren.remove( slave );
		addReparentOperationRecord( slave, this, newParent );
		( (CoShapePageItem) slave ).postRemoveFrom( this );
	}
	
	postRemove( pageItem, null );
	postSubTreeStructureChange( false, pageItem, newParent );
	if ( slave != null ) postSubTreeStructureChange( false, slave, newParent );
	postSubTreeStructureChange();
	performLocalLayout();
}
// Reshape (y-wise) to minimal shape spanning all children without changing childrens global positions

public void reshapeToContentHeight()
{
	double dy = 0;

	if
		( hasChildren() )
	{
		dy = Double.MAX_VALUE;
		Iterator e = m_children.iterator();
		while
			( e.hasNext() )
		{
			CoShapePageItem pi = (CoShapePageItem) e.next();
			if ( pi.getTopEdge() < dy ) dy = pi.getTopEdge();
		};
	}

	dy -= m_columnGrid.getTopMarginPosition();

	if ( dy == 0.0 ) return;
	
	if ( getLocationSpec().getFactoryKey() == CoNoLocationIF.NO_LOCATION) setY( m_y + dy );
	translateChildrenBy( 0, -dy );
}
// Reshape (x-wise) to minimal shape spanning all children without changing childrens global positions

public void reshapeToContentWidth()
{
	double dx = 0;

	if
		( hasChildren() )
	{
		dx = Double.MAX_VALUE;
		Iterator e = m_children.iterator();
		while
			( e.hasNext() )
		{
			CoShapePageItem pi = (CoShapePageItem) e.next();
			if ( pi.getLeftEdge() < dx ) dx = pi.getLeftEdge();
		};
	}

	dx -= m_columnGrid.getLeftMarginPosition();
	
	if ( dx == 0.0 ) return;
	
	if ( getLocationSpec().getFactoryKey() == CoNoLocationIF.NO_LOCATION ) setX( m_x + dx );
	translateChildrenBy( -dx, 0 );
}
// Move child one step forward (z-order)

void sendBackwards( CoShapePageItem child )
{
	int i = m_children.indexOf( child );
	
	if
		( i > 0 )
	{
		m_children.remove( i );
		m_children.add( i - 1, child );
		
		doAfterChildOrderChanged();
	}
}
// Move child to first position (z-order)

void sendToBack( CoShapePageItem child )
{
	if
		( m_children.contains( child ) )
	{
		m_children.remove( child );
		m_children.add( 0, child );
		
		doAfterChildOrderChanged();
	}
}
// Change layout order by applying an ordering relation

public Comparator setChildrenLayoutOrder( Comparator c )
{
	Comparator oldOrder = new CoCompositePageItemIF.CurrentOrder( m_layoutChildren );
	
	if
		( hasChildren() )
	{
		Collections.sort( m_layoutChildren, c );
		/*
		int I = getChildCount();
		boolean dirty = true;
		while
			( dirty )
		{
			dirty = false;
			for
				( int i = 0; i < I - 1; i++ )
			{
				CoShapePageItemIF child1 = getLayoutChildAt( i );
				CoShapePageItemIF child2 = getLayoutChildAt( i + 1 );
				if
					( c.compare( child1, child2 ) >= 0 )
				{
					m_layoutChildren.add( i + 2, child1 );
					m_layoutChildren.remove( i );
					dirty = true;
				};
			}
		}
			*/
		
		doAfterChildLayoutOrderChanged();
	}

	return oldOrder;
}
public void setChildrenLocked( boolean l )
{
	if ( m_areChildrenLocked == l ) return;

	m_areChildrenLocked = l;

	doAfterChildrenLockedChanged();
}
public void setLayoutManager( CoLayoutManagerIF m )
{
	if ( m_layoutManager == m ) return;
	
	m_layoutManager = (CoLayoutManager) m;
	m_mutableLayoutManagerProxy = createMutableLayoutManagerProxy();

	doAfterLayoutManagerChanged();
}
void setLayoutOrder( CoShapePageItem child, int n )
{
	int i = m_layoutChildren.indexOf( child );

	if ( i == n ) return;
	
	if
		( i < n )
	{
		m_layoutChildren.add( n, child );
		m_layoutChildren.remove( i );
	} else {
		m_layoutChildren.remove( i );
		m_layoutChildren.add( n, child );
	}
		
	doAfterChildLayoutOrderChanged();

}
void setZOrder( CoShapePageItem child, int n )
{
	int i = m_children.indexOf( child );

	if ( i == n ) return;
	
	if
		( i < n )
	{
		m_children.add( n, child );
		m_children.remove( i );
	} else {
		m_children.remove( i );
		m_children.add( n, child );
	}
		
	doAfterChildOrderChanged();

}
// translate all children that have no location spec

private void translateChildrenBy(double dx, double dy)
{
	if ( ( dx == 0.0 ) && ( dy == 0.0 ) ) return;
	
	Iterator e = m_children.iterator();
	while
		( e.hasNext() )
	{
		CoShapePageItem pi = (CoShapePageItem) e.next();
		if
			( pi.getLocationSpec().isAbsolutePosition() )//getFactoryKey() == CoNoLocationIF.NO_LOCATION )
		{
			pi.setX( pi.getX() + dx );
			pi.setY( pi.getY() + dy );
		}
	};
}


protected void updateChildrensEffectiveShapes()
{
	Iterator e = m_children.iterator();

	while
		( e.hasNext() )
	{
		( (CoShapePageItem) e.next() ).updateEffectiveShape();
	}
}
protected void updateEffectiveShape()
{
	super.updateEffectiveShape();
	updateChildrensEffectiveShapes();
}
public void visit( CoPageItemVisitor visitor, Object anything, boolean goDown )
{
	visitor.doToCompositePageItem( this, anything, goDown );
}
/*
 * Used at XML import
 * Helena Rankegård 2001-10-23
 */
 
public void xmlAddSubModel( String name, Object subModel, CoXmlContext context )
{
	boolean didHandle = false;
	
	if
		( name == null )
	{
		if
			( subModel instanceof CoShapePageItem )
		{
			m_children.add(subModel);
			((CoShapePageItem)subModel).setParent(this);
//			((CoShapePageItem)subModel).postAddTo( this );
			didHandle = true;
		} else  if
			(subModel instanceof CoLayoutManagerIF)
		{
			setLayoutManager( (CoLayoutManagerIF) subModel );
			didHandle = true;
		}
	}
	
	if ( ! didHandle ) super.xmlAddSubModel( name, subModel, context );
}
/*
 * Used at XML export
 * Helena Rankegård 2001-10-23
 */
 
public void xmlVisit(CoXmlVisitorIF visitor)
{
	super.xmlVisit( visitor );

	visitor.exportAttribute( XML_CHILDREN_LOCKED, ( m_areChildrenLocked ? Boolean.TRUE : Boolean.FALSE ).toString() );

	visitor.export( getLayoutManager() );

	StringBuffer s = new StringBuffer();
	int I = getLayoutOrder().length;
	for
		( int i = 0; i < I; i++ )
	{
		if ( i > 0 ) s.append( ' ' );
		s.append( m_layoutOrder[ i ] );
	}
	visitor.exportAttribute( XML_LAYOUT_ORDER, s.toString() );

	
	I = getChildCount();
	for
		( int i = 0; i < I; i++ )
	{
		CoShapePageItemIF pi = getChildAt( i );
		visitor.export( pi );
	}

}



// add some children

public final void add( CoShapePageItemIF pageItems [], CoPoint2DDouble positions [] )
{
	add( pageItems, positions, true );
}

// add some children

public final void add( CoShapePageItemIF pageItems [], CoPoint2DDouble positions [], boolean useSmartOrdering )
{
	boolean didAdd = false;

	for
		( int i = 0; i < pageItems.length; i++ )
	{
		CoShapePageItemIF pi = pageItems[ i ];
		if
			( pi == null )
		{
			continue;
		}
		
		com.bluebrim.base.shared.debug.CoAssertion.assertTrue( pi != this, "Attempt to add page item to itself" );
		
		if
			( m_children.contains( pi ) )
		{
			continue; // already child of this layout area
		}

		didAdd = true;
		
		CoCompositePageItemIF oldParent = pi.getParent();
		
		CoShapePageItem slave = doAdd( (CoShapePageItem) pi, useSmartOrdering );
		if
			(
				( positions != null )
			&&
				( positions[ i ] != null )
			)
		{
			pi.setPosition( positions[ i ].getX(), positions[ i ].getY() );
		}
		
		( (CoShapePageItem) pi ).postAddTo( this );
		if ( slave != null ) slave.postAddTo( this );

		postSubTreeStructureChange( true, pi, oldParent );
		if ( slave != null ) postSubTreeStructureChange( true, slave, oldParent );
	}

	if
		( didAdd )
	{
		postAdd( null, pageItems );
		postSubTreeStructureChange();
		
		performLocalLayout();
	}
}

// add a child

public final void add( CoShapePageItemIF pageItem, CoPoint2DDouble pos )
{
	add( pageItem, pos, true );
}

// add a child

public final void add( CoShapePageItemIF pageItem, CoPoint2DDouble pos, boolean useSmartOrdering )
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( pageItem != this, "Attempt to add page item to itself" );
	
	if
		( pageItem == null )
	{
		return;
	}
	
	if
		( m_children.contains( pageItem ) )
	{
		return; // already child of this layout area
	}

	CoCompositePageItemIF oldParent = pageItem.getParent();

	CoShapePageItem slave = doAdd( (CoShapePageItem) pageItem, useSmartOrdering );
	if ( pos != null ) pageItem.setPosition( pos.getX(), pos.getY() );

	( (CoShapePageItem) pageItem ).postAddTo( this );
	if ( slave != null ) slave.postAddTo( this );
	
	postAdd( pageItem, null );
	if ( slave != null ) postSubTreeStructureChange( true, slave, oldParent );
	postSubTreeStructureChange( true, pageItem, oldParent );
	postSubTreeStructureChange();
	
	performLocalLayout();
}

protected void collectState( CoPageItemIF.State s, CoPageItemIF.ViewState viewState )
{
	super.collectState( s, viewState  );
	
	CoCompositePageItemIF.State S = (CoCompositePageItemIF.State) s;

	// Note: m_childrenIds is transient -> it is ok to initialize it like this (lazy)
	if
		( m_childrenIds == null )
	{
		m_childrenIds = new ArrayList();
		int I = getChildCount();
		for
			( int i = 0; i < I; i++ )
		{
			m_childrenIds.add( getChildAt( i ).getId() );
		}
	}
	S.m_childrenIds = m_childrenIds;	

	S.m_layoutOrder = getLayoutOrder();
	S.m_layoutManager = getLayoutManager();
	S.m_areChildrenLocked = m_areChildrenLocked;

	S.m_columnGrid = getColumnGrid();
	S.m_baseLineGrid = getBaseLineGrid();
	S.m_clipping = getInteriorShape();

	if
		( viewState != null )
	{
		S.m_reparentOperationRecords = getNextReparentOperationRecords( ( (CoCompositePageItemIF.ViewState) viewState ).m_reparentOperationRecordID );
	} else {
		S.m_reparentOperationRecords = null;
	}
}

private CoLayoutManagerIF createMutableLayoutManagerProxy()
{
	if
		( m_layoutManagerOwner == null )
	{
		m_layoutManagerOwner =
			new CoLayoutManager.Owner()
			{
				public void update()
				{
			   	doAfterLayoutManagerChanged();
				}
			};
	}
	
	return m_layoutManager.getMutableProxy( m_layoutManagerOwner );
}

CoShapePageItemView createView( CoCompositePageItemView parent, int detailMode )
{
	CoCompositePageItemView view = (CoCompositePageItemView) newView( parent, detailMode );

	// synchronize reparent record id
	int id = -1;
	if
		( m_reparentOperationRecords != null )
	{
		id = ( (CoPersistantReparentOperationRecord) m_reparentOperationRecords.get( m_reparentOperationRecords.size() - 1 ) ).getId();
	}
	view.setReparentTransactionRecordId( id );

	// create views for children
	Iterator e = m_children.iterator();
	while
		( e.hasNext() )
	{
		CoShapePageItem s = (CoShapePageItem) e.next();
		CoShapePageItemView v = s.createView( parent, detailMode );
		if ( v != null ) view.add( v );
	};

	return view;
}

protected void doDestroy()
{
	// dispatch to children
	Iterator e = m_children.iterator();
	while
		( e.hasNext() )
	{
		( (CoShapePageItem) e.next() ).destroy();
	}

	super.doDestroy();
}

protected Iterator getSibblingsToRunAround( CoShapePageItem child )
{
	Iterator e = new Iterator()
	{
		int m_I = m_children.size() - 1;

		public boolean hasNext()
		{
			return m_I >= 0;
		}

		public Object next()
		{
			return m_children.get( m_I-- );
		}

		public void remove() {}
	};

	return e;
}

protected abstract CoShapePageItemView newView( CoCompositePageItemView parent, int detailMode );

protected void postAdd( CoShapePageItemIF pageItem, CoShapePageItemIF pageItems [] )
{
	invalidateLayoutOrder();
	
	requestLocalLayout();

	updateChildrensEffectiveShapes();
}

protected void postColumnGridChanged()
{
	super.postColumnGridChanged();

	requestLocalLayout();
}

protected void postRemove( CoShapePageItemIF pageItem, CoShapePageItemIF pageItems [] )
{
	invalidateLayoutOrder();

	requestLocalLayout();

	updateChildrensEffectiveShapes();
}

protected void updateChildrensBaseLineGrids()
{
	Iterator e = m_children.iterator();

	while
		( e.hasNext() )
	{
		( (CoShapePageItem) e.next() ).updateBaseLineGrid();
	}
}

protected void updateChildrensColumnGrids()
{
	Iterator e = m_children.iterator();

	while
		( e.hasNext() )
	{
		( (CoShapePageItem) e.next() ).updateColumnGrid();
	}
}


/*
 * Used at XML import
 * Helena Rankegård 2001-10-23
 */
 
public void xmlImportFinished( Node node, CoXmlContext context )
{
	NamedNodeMap map = node.getAttributes();
	
	String points = CoModelBuilder.getAttrVal( map, XML_LAYOUT_ORDER, null); //(String) attributes.get( XML_LAYOUT_ORDER );
	
	if
		( points != null )
	{
		int I = m_children.size();
		Object [] tmp = new Object [ I ];
		
		StringTokenizer t = new StringTokenizer( points, " " );

		int i = 0;
		while
			( t.hasMoreTokens() )
		{
			int n = CoXmlUtilities.parseInt( t.nextToken(), -1 );
			if
				( n == -1 )
			{
				throw new IllegalArgumentException( "invalid layout order" );
			}

			tmp[ i++ ] = m_children.get( n );
		}

		for
			( i = 0; i < I; i++ )
		{
			if
				( tmp[ i ] == null )
			{
				throw new IllegalArgumentException( "invalid layout order" );
			}
		}

		m_layoutChildren.clear();
		m_layoutChildren.addAll( Arrays.asList( tmp ) );
	}


	super.xmlImportFinished( node, context );
}


/*
 * Used at XML import.
 * Helena Rankegård 2001-10-30
 */
 
public void xmlInit( NamedNodeMap map, CoXmlContext context )
{
	super.xmlInit( map, context );

	// xml init
	m_areChildrenLocked = CoModelBuilder.getBoolAttrVal( map, XML_CHILDREN_LOCKED, m_areChildrenLocked );
}
}