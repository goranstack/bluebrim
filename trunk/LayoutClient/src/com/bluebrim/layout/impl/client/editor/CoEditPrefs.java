package com.bluebrim.layout.impl.client.editor;

import java.awt.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.shared.*;

/**
 * Layout editor operation: Open editor preferences dialog.
 * 
 * @author: Dennis
 */

class CoEditPrefs extends CoExternalUIDialogAction
{
/**
 * CoEditColors constructor comment.
 * @param name java.lang.String
 * @param e com.bluebrim.layout.impl.client.editor.CoLayoutEditor
 */
public CoEditPrefs(String name, CoLayoutEditor e) {
	super(name, e);
}
/**
 * CoEditColors constructor comment.
 * @param e com.bluebrim.layout.impl.client.editor.CoLayoutEditor
 */
public CoEditPrefs(CoLayoutEditor e) {
	super(e);
}
public void actionPerformed( java.awt.event.ActionEvent ev )
{
	if
		( m_dialog == null )
	{
		CoLayoutEditorPrefsUI ui = new CoLayoutEditorPrefsUI();
		ui.buildForComponent();
		Window w = (Window) m_editor.getPanel().getTopLevelAncestor();
		if
			( w instanceof Frame )
		{
			m_dialog = new CoDialog( (Frame) w, false, ui );
		} else {
			m_dialog = new CoDialog( (Dialog) w, false, ui );
		}
		m_dialog.setTitle( CoLayouteditorUIStringResources.getName( m_editor.APPLICATION_PREFS ) );
	}

	m_dialog.show();
}
void setContext( CoPageItemEditorContextIF c )
{
}
}
