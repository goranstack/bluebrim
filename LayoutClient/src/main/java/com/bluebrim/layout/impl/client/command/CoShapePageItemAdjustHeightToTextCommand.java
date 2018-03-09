package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.transact.shared.*;

/**
 * Command for moving shape page items.
 *
 * @author: Dennis Malmström
 */

public class CoShapePageItemAdjustHeightToTextCommand extends CoShapePageItemCommand
{





protected CoShapePageItemAdjustHeightToTextCommand()
{
	super( "SET HEIGHT TO TEXT" );
}

protected CoShapePageItemAdjustHeightToTextCommand( String name )
{
	super( name );
}

public CoUndoCommand doExecute()
{
	CoUndoCommand undo = new CoShapePageItemSetDimensionsUndoCommand( getName(), m_targetView.getShapePageItem() );

	CoPageItemAbstractTextContentView tv = (CoPageItemAbstractTextContentView) ( (CoContentWrapperPageItemView) m_targetView ).getContentView();
	tv.getPageItemAbstractTextContent().adjustHeightToText();

	return undo;
}
}