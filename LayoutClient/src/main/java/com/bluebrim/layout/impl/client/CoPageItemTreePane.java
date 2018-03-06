package com.bluebrim.layout.impl.client;

import javax.swing.event.*;
import javax.swing.tree.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.view.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.swing.client.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-04-24 13:49:29)
 * @author: Dennis
 */
 
public class
	CoPageItemTreePane
extends
	CoTree
implements
	CoViewSelectionManager.SelectionListener,
	TreeSelectionListener,
	TreeModelListener
{
	protected CoViewSelectionManager m_selectionManager;

	private boolean m_isUpdatingSelection = false;






public CoPageItemTreePane( CoUserInterfaceBuilder b )
{
	this( b, null );
}
public CoPageItemTreePane( CoUserInterfaceBuilder b, CoViewSelectionManager sm )
{
	super();

	b.prepareTree( this );

	setCellRenderer( new CoPageItemViewTreeCellRenderer() );

	m_selectionManager = sm;
	if ( m_selectionManager != null ) m_selectionManager.addSelectionListener( this );

	getSelectionModel().addTreeSelectionListener( this );
}
public void dispose()
{
	setRoot( null );
}
public void selectionChanged( CoViewSelectionManager.SelectionChangedEvent e )
{
	if ( ! ( getModel() instanceof CoPageItemViewTreeModel ) ) return;

	CoShapePageItemView r = (CoShapePageItemView) ( (CoPageItemViewTreeModel) getModel() ).getRoot();

	if ( r == null ) return;
	
	m_isUpdatingSelection = true;
	
	TreePath [] tps = new TreePath [ m_selectionManager.getSelectedViewCount() ];
	int n = 0;
	
	java.util.Iterator i = m_selectionManager.getSelectedViews();
	while
		( i.hasNext() )
	{
		CoShapePageItemView v = (CoShapePageItemView) i.next();

		tps[ n++ ] = new TreePath( CoPageItemView.getPath( v, r ) );
	}

	getSelectionModel().setSelectionPaths( tps );

	m_isUpdatingSelection = false;
}
public void setRoot( CoShapePageItemView r )
{
	TreeModel oldModel = getModel();
	if
		( oldModel != null )
	{
		oldModel.addTreeModelListener( this );
		if
			( oldModel instanceof CoPageItemViewTreeModel )
		{
			( (CoPageItemViewTreeModel) oldModel ).dispose();
		}
	}

	if
		( r != null )
	{
		TreeModel newModel = new CoPageItemViewTreeModel( r );
		setModel( newModel );
		newModel.addTreeModelListener( this );
	} else {
		setModel( new DefaultTreeModel( new DefaultMutableTreeNode() {} ) );
	}
}
public void treeNodesChanged( TreeModelEvent e )
{
	repaint();
}
public void treeNodesInserted( TreeModelEvent e )
{
}
public void treeNodesRemoved( TreeModelEvent e )
{
}
public void treeStructureChanged( TreeModelEvent e )
{
}
public void valueChanged( TreeSelectionEvent ev )
{
	if
		( ( m_selectionManager != null ) && ! m_selectionManager.isInTransaction() )
	{
		CoShapePageItemView r = (CoShapePageItemView) ( (CoPageItemViewTreeModel) getModel() ).getRoot();
		
		boolean startedSelectionTransaction = false;
		
		TreePath [] paths = ev.getPaths();
		for
			( int i = 0; i < paths.length; i++ )
		{
			if
				( ! ev.isAddedPath( paths[ i ] ) )
			{
				CoShapePageItemView v = (CoShapePageItemView) paths[ i ].getLastPathComponent();
				if ( v == r ) continue;
				if
					( m_selectionManager.isSelected( v ) )
				{
					if
						( ! startedSelectionTransaction )
					{
						m_selectionManager.beginSelectionTransaction();
						startedSelectionTransaction = true;
					}
					m_selectionManager.unselect( v );
				}
			}
		}
		
		for
			( int i = 0; i < paths.length; i++ )
		{
			if
				( ev.isAddedPath( paths[ i ] ) )
			{
				CoShapePageItemView v = (CoShapePageItemView) paths[ i ].getLastPathComponent();
				if ( v == r ) continue;
				if
					( ! m_selectionManager.isSelected( v ) )
				{
					if
						( ! startedSelectionTransaction )
					{
						m_selectionManager.beginSelectionTransaction();
						startedSelectionTransaction = true;
					}
					m_selectionManager.unselect( v );
				}
				m_selectionManager.select( v, false );
			}
		}

		if ( startedSelectionTransaction ) m_selectionManager.endSelectionTransaction();
	}

	repaint();
}
}
