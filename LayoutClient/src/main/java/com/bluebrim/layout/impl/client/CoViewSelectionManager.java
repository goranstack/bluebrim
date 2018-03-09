package com.bluebrim.layout.impl.client;

import java.util.*;

import javax.swing.event.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.client.view.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Keeper of selected views in a layout editor.
 *
 * Selection listeners ar notified when the set of selected views changes.
 *
 * Also see CoSelectedView
 * 
 * @author: Dennis Malmström
*/

public class CoViewSelectionManager
{
	private List m_selectedViews = new ArrayList(); // [ CoSelectedView ] the selected views
	private CoSelectedView.Renderer m_renderer;

	private boolean m_isInTransaction; // delay selection listener notification


	
	public class SelectionChangedEvent extends EventObject
	{
		public SelectionChangedEvent()
		{
			super( CoViewSelectionManager.this );
		}
	};
	
	public static interface SelectionListener extends EventListener
	{
		void selectionChanged( SelectionChangedEvent e );
	};
	
	private EventListenerList m_selectListeners = new EventListenerList();
	
	private SelectionChangedEvent m_event = new SelectionChangedEvent();
public CoViewSelectionManager()
{
	this( CoSelectedView.HANDLES_AND_OUTLINE_RENDERER );
}
public CoViewSelectionManager( CoSelectedView.Renderer r )
{
	m_renderer = r;
}
public void addSelectionListener( SelectionListener l )
{
	m_selectListeners.add( SelectionListener.class, l );
}

// add a page item view to the set of selected views, clear previous selection if requested

private boolean doSelect( CoShapePageItemView v, boolean doUnselect )
{
	if
		( ! isSelected( v ) )
	{
		if
			( doUnselect || ! isAncestorSelected( v ) )
		{
			if
				( doUnselect )
			{
				doUnselectAllViews(); // clear previous selection
			} else if
				( v instanceof CoCompositePageItemView )
			{
				doUnselectChildren( (CoCompositePageItemView) v ); // unselect any selected (grand^n)children
			}

			CoSelectedView sv = new CoSelectedView( v, m_renderer );
			m_selectedViews.add( sv );
			sv.repaint();

			m_dirty = true;
			return true;
		}
	}

	return false;
}
// remove a page item view from the set of selected views

private boolean doUnselect( CoShapePageItemView v )
{
	int I = m_selectedViews.size();
	for
		( int i = 0; i < I; i++ )
	{
		CoSelectedView sv = (CoSelectedView) m_selectedViews.get( i );
		
		if
			( sv.equals( v ) )
		{
			sv.repaint();
			sv.destroy();
			m_selectedViews.remove( i );
			m_dirty = true;
			return true;
		}
	}

	return false;
}
// remove all page item views from the set of selected views

private boolean doUnselectAllViews()
{
	int I = m_selectedViews.size();
	for
		( int i = 0; i < I; i++ )
	{
		CoSelectedView sv = (CoSelectedView) m_selectedViews.get( i );
		sv.repaint();
		sv.destroy();
	}
	m_selectedViews.clear();
	m_dirty = true;
	return true;
}


public void fireSelectionChanged()
{
	if ( m_isInTransaction ) return; // notification delayed
	
	Object[] listeners = m_selectListeners.getListenerList();
	
	for
		( int i = listeners.length - 2; i >= 0; i -= 2 )
	{
		if
			( listeners[ i ] == SelectionListener.class )
		{
			( (SelectionListener) listeners[ i + 1 ] ).selectionChanged( m_event );
		}
	}
}
public List getSelected() // [ CoSelectedView ]
{
	return m_selectedViews;
}
// return the outline shapes of the selected views

public Iterator getSelectedShapes() // [ CoShapeIF ]
{
	return new Iterator()
	{
		private Iterator e = m_selectedViews.iterator();
		
		public boolean hasNext()
		{
			return e.hasNext();
		}

		public Object next()
		{
			return ( (CoSelectedView) e.next() ).getShape();
		}

		public void remove()
		{
			e.remove();
		}
	};
}
// if only one view is selected, return it, otherwise return null

public CoShapePageItemView getSelectedView()
{
	if
		( m_selectedViews.size() == 1 )
	{
		return ( (CoSelectedView) m_selectedViews.get( 0 ) ).getView();
	} else {
		return null;
	}
}
public CoSelectedView getSelectedView( int i )
{
	return (CoSelectedView) m_selectedViews.get( i );
}
// find the selected view owning a given reshape handle

public CoSelectedView getSelectedView( CoReshapeHandleIF h )
{
	int I = m_selectedViews.size();
	for
		( int i = 0; i < I; i++ )
	{
		CoSelectedView v = (CoSelectedView) m_selectedViews.get( i );
		if ( v.doesOwnHandle( h ) ) return v;
	}

	return null;
}
public int getSelectedViewCount()
{
	return m_selectedViews.size();
}
// return the selected views

public Iterator getSelectedViews() // [ CoShapePageItmView ]
{
	return new Iterator()
	{
		private Iterator e = m_selectedViews.iterator();
		
		public boolean hasNext()
		{
			return e.hasNext();
		}

		public Object next()
		{
			return ( (CoSelectedView) e.next() ).getView();
		}

		public void remove()
		{
			e.remove();
		}
	};

}

public boolean isSelected( CoShapePageItemView v )
{
	int I = m_selectedViews.size();
	for
		( int i = 0; i < I; i++ )
	{
		if ( ( (CoSelectedView) m_selectedViews.get( i ) ).getView().equals( v ) ) return true;
	}
	return false;
}
// force synchronization of selected views

public void refresh()
{
	Iterator i = m_selectedViews.iterator();
	while
		( i.hasNext() )
	{
		( (CoSelectedView) i.next() ).modelChanged();
	};
}
public void removeSelectionListener( SelectionListener l )
{
	m_selectListeners.remove( SelectionListener.class, l );
}
// select a view

public void select( CoShapePageItemView v )
{
	select( v, false );
}
// select a view, clear previous selection if requested

public void select( CoShapePageItemView v, boolean doUnselect )
{
	if
		( doSelect( v, doUnselect ) )
	{
		fireSelectionChanged();
	}
}
// toggle selection state of a view

public void toggleSelection( CoShapePageItemView v )
{
	if
		( isSelected( v ) )
	{
		unselect( v );
	} else {
		select( v );
	}
}
// unselect a view

public void unselect( CoShapePageItemView v )
{
	if
		( doUnselect( v ) )
	{
		fireSelectionChanged();
	}
}
// unselect all views

public void unselectAllViews()
{
	if
		( doUnselectAllViews() )
	{
		fireSelectionChanged();
	}
}



// unselect all selected (grand^n)children of a layout area view

private boolean doUnselectChildren( CoCompositePageItemView v )
{
	Iterator e = v.getChildren().iterator();
	while
		( e.hasNext() )
	{
		CoShapePageItemView child = (CoShapePageItemView) e.next();
		doUnselect( child );
		if ( child instanceof CoCompositePageItemView ) doUnselectChildren( (CoCompositePageItemView) child );
	}

	return true;
}

// unselect all children of a view

public void unselectChildren( CoCompositePageItemView v )
{
	unselectChildren( v, true );
}

// unselect all children of a view, notify only if requested

public void unselectChildren( CoCompositePageItemView v, boolean notify )
{
	if
		( doUnselectChildren( v ) )
	{
		if ( notify ) fireSelectionChanged();
	}
}

	private boolean m_dirty;

// Begin a selection transaction
// Listener notification is delayed until endTransaction() is called.

public void beginSelectionTransaction()
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( ! m_isInTransaction, "Transaction already in progress" );
	m_isInTransaction = true;
	m_dirty = false;
}

// End a selection transaction

public void endSelectionTransaction()
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( m_isInTransaction, "No transaction already in progress" );
	m_isInTransaction = false;
	if ( m_dirty ) fireSelectionChanged();
}

// return the selected views

public List getSelectedViews( List l ) // [ CoShapePageItmView ]
{
	if ( l == null ) l = new ArrayList();

	Iterator i = m_selectedViews.iterator();
	while
		( i.hasNext() )
	{
		l.add( ( (CoSelectedView) i.next() ).getView() );
	}

	return l;
}

// is any ancestor of v selected ?

private boolean isAncestorSelected( CoShapePageItemView v )
{
	CoCompositePageItemView parent = v.getParent();

	if ( parent == null ) return false;
	
	if ( isSelected( parent ) ) return true;
	
	return isAncestorSelected( parent );
}

public boolean isInTransaction()
{
	return m_isInTransaction;
}

public void repaint()
{
	Iterator i = getSelectedViews();
	while
		( i.hasNext() )
	{
		( (CoShapePageItemView) i.next() ).repaint();
	}
}

public void setSelected( List views ) // [ CoShapePageItemView ]
{
	boolean isDifferent = false;
	
	Iterator i = views.iterator();
	while
		( i.hasNext() )
	{
		CoShapePageItemView v = (CoShapePageItemView) i.next();
		if
			( ! isSelected( v ) )
		{
			isDifferent = true;
			break;
		}
	}

	if
		( ! isDifferent )
	{
		i = getSelectedViews();
		while
			( i.hasNext() )
		{
			CoShapePageItemView v = (CoShapePageItemView) i.next();
			if
				( ! views.contains( v ) )
			{
				isDifferent = true;
				break;
			}
		}
	}

	if
		( isDifferent )
	{
		doUnselectAllViews();
		
		i = views.iterator();
		while
			( i.hasNext() )
		{
			doSelect( (CoShapePageItemView) i.next(), false );
		}

		fireSelectionChanged();
	}
}
}