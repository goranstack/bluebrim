package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Layout editor operation: Move a page item to the back (first in z-order -> drawn in bottom).
 * 
 * @author: Dennis
 */

public class CoSendToBack extends CoLayoutEditorAction
{
public CoSendToBack( String name, CoLayoutEditor e )
{
	super( name, e );
}
public CoSendToBack( CoLayoutEditor e )
{
	this( null, e );
}
public void actionPerformed(java.awt.event.ActionEvent arg1)
{
	CoShapePageItemView v = m_editor.getCurrentShapePageItemView();

	if
		( v != null )
	{
		CoPageItemCommands.SEND_TO_BACK.prepare( v );
		m_editor.getCommandExecutor().doit( CoPageItemCommands.SEND_TO_BACK, v );
	}
}
}