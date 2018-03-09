package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
 
public class CoShapePageItemCaptionUndoCommand extends CoShapePageItemUndoCommand
{
	protected CoShapePageItemIF m_caption;
public CoShapePageItemCaptionUndoCommand( String name, CoShapePageItemIF target, CoShapePageItemIF oldCaption )
{
	super( name, target );
	
	m_caption = oldCaption;
}
public boolean doRedo()
{
	doUndo();
	return true;
}
public boolean doUndo()
{
	if
		( m_caption == null )
	{
		m_caption = ( (CoPageItemBoundedContentIF) ( (CoContentWrapperPageItemIF) m_target ).getContent() ).removeCaption();
	} else {
		( (CoPageItemBoundedContentIF) ( (CoContentWrapperPageItemIF) m_target ).getContent() ).setCaption( m_caption );
		m_caption = null;
	}

	return true;
}
}
