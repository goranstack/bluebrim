package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Layout editor operation: adjust the height of a text box to the height needed to layout the text.
 * 
 * @author: Dennis
 */
 
public class CoAdjustHeightToText extends CoLayoutEditorAction
{
public CoAdjustHeightToText( String name, CoLayoutEditor e )
{
	super( name, e );
}
public CoAdjustHeightToText( CoLayoutEditor e )
{
	super( e );
}
public void actionPerformed(java.awt.event.ActionEvent arg1)
{
	CoPageItemAbstractTextContentView v = m_editor.getCurrentTextContentView();

	if
		( v != null )
	{
		CoPageItemCommands.ADJUST_HEIGHT_TO_TEXT.prepare( v.getOwner() );
		m_editor.getCommandExecutor().doit( CoPageItemCommands.ADJUST_HEIGHT_TO_TEXT, null );
	}
}

public void prepare( CoShapePageItemView v )
{
	setEnabled( ! v.isSlave() );
}
}