package com.bluebrim.layout.impl.client.command;

import java.util.*;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.transact.shared.*;

/**
 * Command for moving shape page items.
 *
 * @author: Dennis Malmström
 */

public class CoShapePageItemSetLayoutOrderCommand extends CoShapePageItemCommand
{





	private Comparator m_order;

protected CoShapePageItemSetLayoutOrderCommand( String name )
{
	super( name );
}

public final CoUndoCommand doExecute()
{
	Comparator oldOrder = ( (CoCompositePageItemView) m_targetView ).getCompositePageItem().setChildrenLayoutOrder( m_order );

	return new CoShapePageItemSetLayoutOrderUndoCommand( getName(), m_targetView.getShapePageItem(), oldOrder );
}

public void prepare( CoShapePageItemView targetView, Comparator order )
{
	prepare( targetView );
	m_order = order;
}
}