package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.transact.shared.*;

/**
 * 
 *
 * @author: Dennis Malmström
 */

public class CoShapePageItemRemoveCaptionCommand extends CoShapePageItemCommand
{





protected CoShapePageItemRemoveCaptionCommand( String name )
{
	super( name );
}

public CoUndoCommand doExecute()
{
	CoUndoCommand undo =
		new CoShapePageItemCaptionUndoCommand( 
			getName(),
			m_targetView.getShapePageItem(),
			( (CoPageItemBoundedContentView) ( (CoContentWrapperPageItemView) m_targetView ).getContentView() ).getPageItemBoundedContent().removeCaption()
		);

	return undo;
}
}