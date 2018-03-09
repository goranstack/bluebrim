package com.bluebrim.layout.impl.client.command;

import java.util.*;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.transact.shared.*;

/**
 * Command for moving shape page items.
 *
 * @author: Dennis Malmström
 */

public abstract class CoShapePageItemChangeAcceptedTagsCommand extends CoShapePageItemCommand
{





	protected List m_tags;

protected CoShapePageItemChangeAcceptedTagsCommand( String name )
{
	super( name );
}

public final CoUndoCommand doExecute()
{
	CoUndoCommand undo = new CoShapePageItemAcceptedTagsUndoCommand( getName(), m_targetView.getShapePageItem() );

	doit();

	return undo;
}

protected abstract void doit();

public void prepare( CoShapePageItemView targetView, List tags )
{
	prepare( targetView );
	m_tags = tags;
}
}