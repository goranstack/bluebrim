package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.transact.shared.*;

/**
 * 
 *
 * @author: Dennis Malmström
 */

public class CoShapePageItemAddCaptionCommand extends CoShapePageItemCommand
{





protected CoShapePageItemAddCaptionCommand( String name )
{
	super( name );
}

public CoUndoCommand doExecute()
{
	CoUndoCommand undo = new CoShapePageItemCaptionUndoCommand( getName(), m_targetView.getShapePageItem(), null );

	( (CoPageItemBoundedContentView) ( (CoContentWrapperPageItemView) m_targetView ).getContentView() ).getPageItemBoundedContent().setCaption( null );

	return undo;
}
}