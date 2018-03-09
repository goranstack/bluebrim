package com.bluebrim.layout.impl.client.command;

import com.bluebrim.transact.shared.*;

/**
 * Command for moving shape page items.
 *
 * @author: Dennis Malmström
 */

public abstract class CoShapePageItemZOrderCommand extends CoShapePageItemCommand
{





protected CoShapePageItemZOrderCommand( String name )
{
	super( name );
}

public final CoUndoCommand doExecute()
{
	return new CoShapePageItemZOrderUndoCommand( getName(), m_targetView.getShapePageItem(), setZOrder() );
}

public abstract int setZOrder();
}