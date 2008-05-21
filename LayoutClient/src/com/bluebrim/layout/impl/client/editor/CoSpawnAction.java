package com.bluebrim.layout.impl.client.editor;

////import se.corren.calvin.editorial.interfaces.*;

import java.util.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;

/**
 * Layout editor operation: Open a new layout editor with the current (popup menu owner) page item as the model.
 * 
 * @author: Dennis
 */
 
class CoSpawnAction extends CoLayoutEditorAction
{
public CoSpawnAction( String name, CoLayoutEditor e )
{
	super( name, e );
}


public CoSpawnAction( CoLayoutEditor e )
{
	super( e );
}


public void actionPerformed(java.awt.event.ActionEvent arg1)
{
	CoShapePageItemView v = m_editor.getCurrentShapePageItemView();

	if
		( v != null )
	{
		doSpawn( v.getShapePageItem(), m_editor.getDesktop(), null, null );
	}
}




protected CoLayoutEditor doSpawn( List models, CoDesktopLayoutAreaIF desktop, String name, CoLayoutEditorConfiguration app )
{
	CoLayoutEditorDialog d = CoLayoutEditor.createLayoutEditorDialog( app );
	CoLayoutEditor e = d.getEditor();
		
	e.setContext( m_editor.getContext() );
	e.setModels( models, desktop );
		
	d.open( m_editor.getWorkspace(), name );
	e.fitInWindow();
		
	return e;
}

protected CoLayoutEditor doSpawn( CoShapePageItemIF model, CoDesktopLayoutAreaIF desktop, String name, CoLayoutEditorConfiguration app )
{
	if ( app == null ) app = m_editor.getConfiguration().getSpawnedConfiguration( model );
	if ( name == null ) name = model.getName();

	CoLayoutEditorDialog d = CoLayoutEditor.createLayoutEditorDialog( app );
	CoLayoutEditor e = d.getEditor();

	e.setContext( m_editor.getContext() );
	e.setModel( model, desktop );
		
	d.open( m_editor.getWorkspace(), name );
	e.fitInWindow();
		
	return e;
}

public void prepare( CoShapePageItemView v )
{
	boolean e = ! v.isSlave();
	
	setEnabled( e );
}
}