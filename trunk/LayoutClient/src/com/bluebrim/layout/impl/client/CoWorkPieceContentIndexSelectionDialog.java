package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;

/**
 * Abstract Dialog for displaying a list of values for selection.
 * Upon selection in the list, the dialog is closed and the selected index is returned.
 *
 * @author: Dennis
 */
 
abstract class CoWorkPieceContentIndexSelectionDialog extends CoDialog
{
	protected CoList m_list;
	protected DefaultListModel m_listModel;
public CoWorkPieceContentIndexSelectionDialog( CoUserInterfaceBuilder b )
{
	super( new JFrame(), "", true );
	
	CoListBox tb = b.createListBox();

	m_list = tb.getList();
	m_listModel = new DefaultListModel();
	m_list.setModel( m_listModel );
	m_list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
	ListCellRenderer r = createRenderer();
	if ( r != null ) m_list.setCellRenderer( r );
	
	getContentPane().add( tb, BorderLayout.NORTH );

	m_list.getSelectionModel().addListSelectionListener(
		new ListSelectionListener()
		{
			public void valueChanged( ListSelectionEvent ev )
			{
				setVisible( false );
			}
		}
	);
}
protected ListCellRenderer createRenderer()
{
	return null;
}
protected int getMaxVisibleRowCount()
{
	return 10;
}
public final int open( Collection values ) // [ Object ]
{
	m_listModel.clear();

	int n = 0;
	Iterator i = values.iterator();
	while
		( i.hasNext() )
	{
		m_listModel.addElement( prepareElement( i.next() ) );
		n++;
	}

	m_list.setVisibleRowCount( Math.max( getMaxVisibleRowCount(), n ) );
	pack();
	setVisible( true );

	return m_list.getSelectedIndex();
}
protected abstract Object prepareElement( Object e );
}
