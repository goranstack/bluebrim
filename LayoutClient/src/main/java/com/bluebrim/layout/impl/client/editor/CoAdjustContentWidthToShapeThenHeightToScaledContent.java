package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Layout editor operation: Adjust the image x-scale of an content box to the width of the shape,
 *                          then adjust the height of the shape to the height of the content after applying the x-scale in both dimensions.
 * 
 * @author: Dennis
 */
 
public class CoAdjustContentWidthToShapeThenHeightToScaledContent extends CoLayoutEditorAction
{
public CoAdjustContentWidthToShapeThenHeightToScaledContent( String name, CoLayoutEditor e )
{
	super( name, e );
}
public CoAdjustContentWidthToShapeThenHeightToScaledContent( CoLayoutEditor e )
{
	super( e );
}
public void actionPerformed(java.awt.event.ActionEvent arg1)
{
	CoPageItemBoundedContentView v = m_editor.getCurrentBoundedContentView();

	if
		( v != null )
	{
		CoPageItemCommands.ADJUST_BOUNDED_CONTENT.prepare( v.getOwner(), 0, CoPageItemBoundedContentIF.X, 0, CoPageItemBoundedContentIF.Y );
		m_editor.getCommandExecutor().doit( CoPageItemCommands.ADJUST_BOUNDED_CONTENT, v );

	}
}
}