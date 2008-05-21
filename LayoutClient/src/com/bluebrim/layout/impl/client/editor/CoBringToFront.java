package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Layout editor operation: Move a page item to the front (last in z-order -> drawn on top).
 * 
 * @author: Dennis
 */

public class CoBringToFront extends CoLayoutEditorAction
{
public CoBringToFront( String name, CoLayoutEditor e )
{
	super( name, e );
}
public CoBringToFront( CoLayoutEditor e )
{
	this( null, e );
}
public void actionPerformed(java.awt.event.ActionEvent arg1)
{
	CoShapePageItemView v = m_editor.getCurrentShapePageItemView();

	if
		( v != null )
	{
		CoPageItemCommands.BRING_TO_FRONT.prepare( v );
		m_editor.getCommandExecutor().doit( CoPageItemCommands.BRING_TO_FRONT, v );
	}
}
}