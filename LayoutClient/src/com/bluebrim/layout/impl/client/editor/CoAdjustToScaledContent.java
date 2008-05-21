package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Layout editor operation: Adjust the bounds of an content box to the size of the content after the content scale is applied.
 * 
 * @author: Dennis
 */
 
public class CoAdjustToScaledContent extends CoLayoutEditorAction
{
public CoAdjustToScaledContent( String name, CoLayoutEditor e )
{
	super( name, e );
}
public CoAdjustToScaledContent( CoLayoutEditor e )
{
	super( e );
}
public void actionPerformed(java.awt.event.ActionEvent arg1)
{
	CoPageItemBoundedContentView v = m_editor.getCurrentBoundedContentView();

	if
		( v != null )
	{
		CoPageItemCommands.ADJUST_BOUNDED_CONTENT.prepare( v.getOwner(), 0, 0, 0, CoPageItemBoundedContentIF.XY );
		m_editor.getCommandExecutor().doit( CoPageItemCommands.ADJUST_BOUNDED_CONTENT, v );
	}
}
}