package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Layout editor operation: Adjust the content scale of an image box to the bounds of the shape without changing the aspect ratio of the scaled content.
 * 
 * @author: Dennis
 */
 
public class CoAdjustContentToShapeKeepAspectRatio extends CoLayoutEditorAction
{
public CoAdjustContentToShapeKeepAspectRatio( String name, CoLayoutEditor e )
{
	super( name, e );
}
public CoAdjustContentToShapeKeepAspectRatio( CoLayoutEditor e )
{
	super( e );
}
public void actionPerformed(java.awt.event.ActionEvent arg1)
{
	CoPageItemBoundedContentView v = m_editor.getCurrentBoundedContentView();

	if
		( v != null )
	{
		CoPageItemCommands.ADJUST_BOUNDED_CONTENT.prepare( v.getOwner(), 0, CoPageItemBoundedContentIF.XY, 0, 0 );
		m_editor.getCommandExecutor().doit( CoPageItemCommands.ADJUST_BOUNDED_CONTENT, v );
	}
}
}