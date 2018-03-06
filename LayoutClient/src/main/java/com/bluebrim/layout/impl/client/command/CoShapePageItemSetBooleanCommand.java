package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * 
 *
 * @author: Dennis Malmström
 */

public abstract class CoShapePageItemSetBooleanCommand extends CoShapePageItemCommand
{





	protected boolean m_boolean;

protected CoShapePageItemSetBooleanCommand( String name )
{
	super( name );
}

public final CoUndoCommand doExecute()
{
	CoUndoCommand undo = new CoShapePageItemSetBooleanUndoCommand( getName(), m_targetView.getShapePageItem(), this, getBoolean( m_targetView ), m_boolean );

	setBoolean( m_targetView.getShapePageItem(), m_boolean );
	
	return undo;
}

public abstract boolean getBoolean( CoShapePageItemView targetView );

public void prepare( CoShapePageItemView targetView, boolean b )
{
	prepare( targetView );
	m_boolean = b;
}

public abstract void setBoolean( CoShapePageItemIF target, boolean b );
}