package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.shared.*;

/**
 * Abstract superclass for layout editor operations that involve opening other ui's.
 * 
 * @author: Dennis
 */
 
abstract class CoExternalUIAction extends CoLayoutEditorAction
{
	CoDomainUserInterface m_ui;
public CoExternalUIAction(String name, CoLayoutEditor e)
{
	super(name, e);
	
	register();
}
public CoExternalUIAction(CoLayoutEditor e)
{
	super(e);

	register();
}
abstract void close();
private void register()
{
	m_editor.register( this );
}
abstract void setContext( CoPageItemEditorContextIF c );
}
