package com.bluebrim.layout.impl.client.editor;

import java.awt.*;

import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.shared.*;

/**
 * Layout editor operation: Duplicate the selected page items n times.
 * 
 * @author: Dennis
 */
 
public class CoDuplicateAndRepeat extends CoLayoutEditorAction
{
	protected CoDuplicateDialog m_duplicateDialog;
public CoDuplicateAndRepeat( String name, CoLayoutEditor e )
{
	super( name, e );
}
public CoDuplicateAndRepeat( CoLayoutEditor e )
{
	super( e );
}
public void actionPerformed(java.awt.event.ActionEvent arg1)
{
	if
		( m_duplicateDialog == null )
	{
		Container w = m_editor.getPanel().getTopLevelAncestor();
		if
			( w instanceof Frame )
		{
			m_duplicateDialog = new CoDuplicateDialog( this, (Frame) w );
		} else {
			m_duplicateDialog = new CoDuplicateDialog( this, (Dialog) w );
		}
	}

	m_duplicateDialog.open();
}
void doDuplicate( int count, double dx, double dy )
{
	int I = m_editor.getWorkspace().getSelectionManager().getSelectedViewCount();
	if ( I == 0 ) return;
	
	CoShapePageItemIF [] pi = new CoShapePageItemIF [ I * count ];

	for
		( int n = 0; n < count; n++ )
	{
		for
			( int i = 0; i < I; i++ )
		{
			CoShapePageItemIF tmp = m_editor.getWorkspace().getSelectionManager().getSelectedView( i ).getView().getShapePageItem();
			if ( tmp.isSlave() ) continue;
			
			tmp = (CoShapePageItemIF) tmp.deepClone();
			pi[ I * n + i ] = tmp;
			tmp.translate( dx * ( n + 1 ), dy * ( n + 1 ) );
			m_editor.getWorkspace().getRootView().addRecentlyCreatedPageItem( tmp );
		}
	}

	CoCompositePageItemIF newParent = m_editor.getWorkspace().getSelectionManager().getSelectedView( 0 ).getView().getParent().getCompositePageItem();		
	CoPageItemCommands.REPARENT_PAGE_ITEMS.prepare( "DUPLICATE PAGE ITEMS", pi, null, null, newParent, null );
	m_editor.getWorkspace().getSelectionManager().unselectAllViews();

	m_editor.getCommandExecutor().doit( CoPageItemCommands.REPARENT_PAGE_ITEMS, null );
	
}
}