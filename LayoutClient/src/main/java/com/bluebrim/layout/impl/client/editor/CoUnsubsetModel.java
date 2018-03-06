package com.bluebrim.layout.impl.client.editor;

import java.util.*;

/**
 * Layout editor operation: Restore to previous model (see CoSubsetModel).
 * 
 * @author: Dennis
 */
 
public class CoUnsubsetModel extends CoLayoutEditorAction
{
public CoUnsubsetModel( String name, CoLayoutEditor e )
{
	super( name, e );
}
public CoUnsubsetModel( CoLayoutEditor e )
{
	super( e );
}
public void actionPerformed(java.awt.event.ActionEvent arg1)
{
	if
		( ! m_editor.m_modelStack.isEmpty() )
	{
		m_editor.setModels( (List) m_editor.m_modelStack.pop(), m_editor.getDesktop() );
		m_editor.fitInWindow();
		m_editor.updateEnableDisableState();
	}
}
}