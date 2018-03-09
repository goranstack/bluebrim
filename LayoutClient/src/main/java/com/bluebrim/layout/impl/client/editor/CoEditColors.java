package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.shared.*;

/**
 * Layout editor operation: Open ui for editing available colors.
 * 
 * @author: Dennis
 */

class CoEditColors extends CoExternalUIFrameAction
{
/**
 * CoEditColors constructor comment.
 * @param name java.lang.String
 * @param e com.bluebrim.layout.impl.client.editor.CoLayoutEditor
 */
public CoEditColors(String name, CoLayoutEditor e) {
	super(name, e);
}
/**
 * CoEditColors constructor comment.
 * @param e com.bluebrim.layout.impl.client.editor.CoLayoutEditor
 */
public CoEditColors(CoLayoutEditor e) {
	super(e);
}
public void actionPerformed( java.awt.event.ActionEvent ev )
{
	if
		( m_frame == null )
	{
		m_ui = new com.bluebrim.paint.impl.client.CoColorCollectionUI();
		m_frame = new CoFrame( m_ui );
		m_frame.setDefaultCloseOperation( CoFrame.HIDE_ON_CLOSE );
		m_frame.pack();

		( new com.bluebrim.observable.CoDefaultServerObjectListener( m_ui ) ).initialize();
		
		setContext( m_editor.getContext() );
	}
	
	m_frame.show();
}
void setContext( CoPageItemEditorContextIF c )
{
	if ( m_ui != null ) m_ui.setDomain( c.getPreferences() );
}
}