package com.bluebrim.layout.impl.client.editor;

import java.util.*;

import com.bluebrim.layout.impl.shared.view.*;

/**
 * Layout editor operation: Make the selected page items the new model of the layout editor and push old model on stack.
 * 
 * @author: Dennis
 */
 
public class CoSubsetModel extends CoLayoutEditorAction
{
public CoSubsetModel( String name, CoLayoutEditor e )
{
	super( name, e );
}
public CoSubsetModel( CoLayoutEditor e )
{
	super( e );
}
public void actionPerformed(java.awt.event.ActionEvent arg1)
{
	List models = new ArrayList();

	CoShapePageItemView v = m_editor.getCurrentShapePageItemView();

	if
		( v != null )
	{
		models.add( new CoLayoutEditorModel( v.getShapePageItem(), v.getName() ) );
	} else {
		int I = m_editor.getWorkspace().getSelectionManager().getSelectedViewCount();
		if ( I == 0 ) return;
		
		for
			( int i = 0; i < I; i++ )
		{
			v = m_editor.getWorkspace().getSelectionManager().getSelectedView( i ).getView();
			models.add( new CoLayoutEditorModel( v.getShapePageItem(), v.getName() ) );
		}
	}

	m_editor.m_modelStack.push( m_editor.getModels() );
	m_editor.setModels( models, m_editor.getDesktop() );
	m_editor.fitInWindow();
	m_editor.updateEnableDisableState();
}

public void prepare( CoShapePageItemView v )
{
	setEnabled( ! v.isSlave() );
}
}