package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * Command for moving shape page items.
 *
 * @author: Dennis Malmström
 */

public abstract class CoShapePageItemSetIntegerCommand extends CoShapePageItemCommand
{





	protected int m_integer;

protected CoShapePageItemSetIntegerCommand( String name )
{
	super( name );
}

public final CoUndoCommand doExecute()
{
	CoUndoCommand undo = new CoShapePageItemSetIntegerUndoCommand( getName(), m_targetView.getShapePageItem(), this, getInteger( m_targetView ), m_integer );
	
	setInteger( m_targetView.getShapePageItem(), m_integer );
	
	return undo;
}

public abstract int getInteger( CoShapePageItemView targetView );

public void prepare( CoShapePageItemView targetView, int i )
{
	prepare( targetView );
	m_integer = i;
}

public abstract void setInteger( CoShapePageItemIF target, int i );
}