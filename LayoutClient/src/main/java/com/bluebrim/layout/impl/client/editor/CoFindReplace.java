package com.bluebrim.layout.impl.client.editor;

import java.awt.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.shared.*;

/**
 * Layout editor operation: Open editor preferences dialog.
 * 
 * @author: Dennis
 */

class CoFindReplace extends CoExternalUIDialogAction
{
public CoFindReplace(String name, CoLayoutEditor e) {
	super(name, e);
}
public CoFindReplace(CoLayoutEditor e) {
	super(e);
}
public void actionPerformed( java.awt.event.ActionEvent ev )
{
	if
		( m_dialog == null )
	{
		CoStyledTextSearchPanel p = new CoStyledTextSearchPanel( m_editor.getUIBuilder() );
		
		Window w = (Window) m_editor.getPanel().getTopLevelAncestor();
		if
			( w instanceof Frame )
		{
			m_dialog = new CoDialog( (Frame) w, CoLayouteditorUIStringResources.getName( "MENU.EDIT.FIND_REPLACE" ), false );
		} else {
			m_dialog = new CoDialog( (Dialog) w, CoLayouteditorUIStringResources.getName( "MENU.EDIT.FIND_REPLACE" ), false );
		}
		m_dialog.getContentPane().add( p );
		p.setEditor( m_editor );
		m_dialog.pack();
		m_dialog.setLocationRelativeTo( m_editor.getPanel() );
	}

	m_dialog.show();
}
void setContext( CoPageItemEditorContextIF c )
{
}
}
