package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.text.impl.client.*;

/**
 * Layout editor operation: Open ui for editing available hyphenations.
 * 
 * @author: Dennis
 */

class CoEditHyphenations extends CoExternalUIFrameAction
{
/**
 * CoEditColors constructor comment.
 * @param name java.lang.String
 * @param e com.bluebrim.layout.impl.client.editor.CoLayoutEditor
 */
public CoEditHyphenations(String name, CoLayoutEditor e) {
	super(name, e);
}
/**
 * CoEditColors constructor comment.
 * @param e com.bluebrim.layout.impl.client.editor.CoLayoutEditor
 */
public CoEditHyphenations(CoLayoutEditor e) {
	super(e);
}
public void actionPerformed( java.awt.event.ActionEvent ev )
{
	if
		( m_frame == null )
	{
		m_ui = new CoHyphenationCollectionUI();
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
	if
		( m_ui != null )
	{		
		m_ui.setDomain( c == null ? null : c.getPreferences() );
	}
}
}