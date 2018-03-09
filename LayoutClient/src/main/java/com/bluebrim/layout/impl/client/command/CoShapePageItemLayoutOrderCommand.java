package com.bluebrim.layout.impl.client.command;

import com.bluebrim.transact.shared.*;

/**
 * Command for moving shape page items.
 *
 * @author: Dennis Malmström
 */

public abstract class CoShapePageItemLayoutOrderCommand extends CoShapePageItemCommand
{





protected CoShapePageItemLayoutOrderCommand( String name )
{
	super( name );
}

public final CoUndoCommand doExecute()
{
	return new CoShapePageItemLayoutOrderUndoCommand( getName(), m_targetView.getShapePageItem(), setLayoutOrder() );
}

public abstract int setLayoutOrder();
}