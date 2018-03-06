package com.bluebrim.layout.impl.client.view;

import javax.swing.event.*;
import javax.swing.tree.*;

import com.bluebrim.layout.impl.shared.view.*;

/**
 * 
 * 
 * @author: Dennis
 */
 
public class CoPageItemViewTreeModel implements TreeModel, CoPageItemView.Listener
{
	protected EventListenerList m_listenerList = new EventListenerList();

	protected CoPageItemView m_root;
public CoPageItemViewTreeModel( CoPageItemView root )
{
	m_root = root;

	m_root.visit( m_addViewListenerVisitor );
}
public void addTreeModelListener( TreeModelListener l )
{
	m_listenerList.add( TreeModelListener.class, l );
}
public void dispose()
{
	m_root.visit( m_removeViewListenerVisitor );
}
protected void fireTreeNodesChanged(Object source, Object[] path, int[] childIndices, Object[] children)
{
	// Guaranteed to return a non-null array
	Object[] listeners = m_listenerList.getListenerList();
	TreeModelEvent e = null;
	// Process the listeners last to first, notifying
	// those that are interested in this event
	for (int i = listeners.length - 2; i >= 0; i -= 2)
	{
		if (listeners[i] == TreeModelListener.class)
		{
			// Lazily create the event:
			if (e == null)
				e = new TreeModelEvent(source, path, childIndices, children);
			((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
		}
	}
}
protected void fireTreeStructureChanged( Object source, Object[] path, int[] childIndices, Object[] children )
{
	// Guaranteed to return a non-null array
	Object[] listeners = m_listenerList.getListenerList();
	TreeModelEvent e = null;
	// Process the listeners last to first, notifying
	// those that are interested in this event
	for (int i = listeners.length - 2; i >= 0; i -= 2)
	{
		if (listeners[i] == TreeModelListener.class)
		{
			// Lazily create the event:
			if (e == null)
				e = new TreeModelEvent(source, path, childIndices, children);
			((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
		}
	}
}
public Object getChild( Object parent, int index )
{
	if
		( parent instanceof CoRootView )
	{
		CoRootView r = (CoRootView) parent;
		if
			( index > r.getChildCount() - 1 )
		{
			return r.getModelViews().get( index - r.getChildCount() );
		}
	}
	
	if
		( parent instanceof CoCompositePageItemView )
	{
		return ( (CoCompositePageItemView) parent ).getChildAt( index );
	} else {
		return null;
	}
}
public int getChildCount( Object parent )
{
	if
		( parent instanceof CoRootView )
	{
		return ( (CoRootView) parent ).getChildCount() + ( (CoRootView) parent ).getModelViews().size();
	} else if
		( parent instanceof CoCompositePageItemView )
	{
		return ( (CoCompositePageItemView) parent ).getChildCount();
	} else {
		return 0;
	}
}
public int getIndexOfChild( Object parent, Object child )
{
	if
		( parent instanceof CoRootView )
	{
		CoRootView r = (CoRootView) parent;
		int i = r.getModelViews().indexOf( child );
		if
			( i != -1 )
		{
			return i;
		}
	}

	if
		( parent instanceof CoCompositePageItemView )
	{
		return ( (CoCompositePageItemView) parent ).getIndexOfChild( (CoShapePageItemView) child );
	} else {
		return -1;
	}
}
public Object getRoot()
{
	return m_root;
}
public boolean isLeaf( Object node )
{
	return 
		( node instanceof CoContentWrapperPageItemView );/*
	||
		! ( (CoCompositePageItemView) node ).hasChildren();
		*/
}
public void removeTreeModelListener( TreeModelListener l )
{
	m_listenerList.remove( TreeModelListener.class, l );
}
public void valueForPathChanged( TreePath path, Object newValue )
{
}


	private CoPageItemViewVisitor m_addViewListenerVisitor =
		new CoPageItemViewVisitor()
		{
			public boolean visitView( CoPageItemView v )
			{
				( (CoShapePageItemView) v ).addViewListener( CoPageItemViewTreeModel.this );
				return true;
			}
			public boolean visitContentView( CoPageItemContentView v )
			{
				return true;
			}
		};
	private CoPageItemViewVisitor m_removeViewListenerVisitor =
		new CoPageItemViewVisitor()
		{
			public boolean visitView( CoPageItemView v )
			{
				( (CoShapePageItemView) v ).removeViewListener( CoPageItemViewTreeModel.this );
				return true;
			}
			public boolean visitContentView( CoPageItemContentView v )
			{
				return true;
			}
		};

protected void fireTreeNodesInserted(Object source, Object[] path, int[] childIndices, Object[] children)
{
	// Guaranteed to return a non-null array
	Object[] listeners = m_listenerList.getListenerList();
	TreeModelEvent e = null;
	// Process the listeners last to first, notifying
	// those that are interested in this event
	for (int i = listeners.length - 2; i >= 0; i -= 2)
	{
		if (listeners[i] == TreeModelListener.class)
		{
			// Lazily create the event:
			if (e == null)
				e = new TreeModelEvent(source, path, childIndices, children);
			((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
		}
	}
}

protected void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices, Object[] children)
{
	// Guaranteed to return a non-null array
	Object[] listeners = m_listenerList.getListenerList();
	TreeModelEvent e = null;
	// Process the listeners last to first, notifying
	// those that are interested in this event
	for (int i = listeners.length - 2; i >= 0; i -= 2)
	{
		if (listeners[i] == TreeModelListener.class)
		{
			// Lazily create the event:
			if (e == null)
				e = new TreeModelEvent(source, path, childIndices, children);
			((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
		}
	}
}

public void viewChanged( CoPageItemView.Event ev )// CoPageItemView view, boolean boundsChanged, int structuralChange, CoShapePageItemView child, int index )
{
	CoPageItemView view = ev.getSource();
	
	if
		( ev.getStructuralChange() == ev.STRUCTURAL_CHANGE_ADD )
	{
		Object [] path = CoPageItemView.getPath( (CoShapePageItemView) view, (CoShapePageItemView) m_root );
		fireTreeNodesInserted( this, path, new int [] { ev.getIndex() }, new Object [] { ev.getChild() } );
		ev.getChild().visit( m_addViewListenerVisitor );

	} else if
		( ev.getStructuralChange() == ev.STRUCTURAL_CHANGE_REMOVE )
	{
		Object [] path = CoPageItemView.getPath( (CoShapePageItemView) view, (CoShapePageItemView) m_root );
		fireTreeNodesRemoved( this, path, new int [] { ev.getIndex() }, new Object [] { ev.getChild() } );
		ev.getChild().visit( m_removeViewListenerVisitor );

	} else if
		( ev.getStructuralChange() == ev.STRUCTURAL_CHANGE_REORDER )
	{
		Object [] path = CoPageItemView.getPath( (CoShapePageItemView) view, (CoShapePageItemView) m_root );
		
		CoCompositePageItemView parent = (CoCompositePageItemView) view;
		int I = parent.getChildCount();
		
		int [] indices = new int [ I ];
		Object [] nodes = new Object [ I ];
		for
			( int i = 0; i < I; i++ )
		{
			indices[ i ] = i;
			nodes[ i ] = parent.getChildAt( i );
		}
		
		fireTreeNodesChanged( this, path, indices, nodes );

	} else {
		if
			( view instanceof CoPageItemContentView )
		{
			view = ( (CoPageItemContentView) view ).getOwner();
		}
		fireTreeNodesChanged( this, CoPageItemView.getPath( (CoShapePageItemView) view, (CoShapePageItemView) m_root ), null, null );
	}
}
}