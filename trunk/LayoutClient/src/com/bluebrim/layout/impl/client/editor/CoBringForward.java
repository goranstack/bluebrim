package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Layout editor operation: Move a page item one position (in z-order) towards front.
 * 
 * @author: Dennis
 */

public class CoBringForward extends CoLayoutEditorAction
{
public CoBringForward( String name, CoLayoutEditor e )
{
	super( name, e );
}
public CoBringForward( CoLayoutEditor e )
{
	this( null, e );
}
public void actionPerformed(java.awt.event.ActionEvent arg1)
{
	CoShapePageItemView v = m_editor.getCurrentShapePageItemView();

	if
		( v != null )
	{
		CoPageItemCommands.BRING_FORWARD.prepare( v );
		m_editor.getCommandExecutor().doit( CoPageItemCommands.BRING_FORWARD, v );
	}
}
}