package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * Command for moving shape page items.
 *
 * @author: Dennis Malmström
 */

public abstract class CoShapePageItemSetDoubleCommand extends CoShapePageItemCommand
{





	protected double m_double;

protected CoShapePageItemSetDoubleCommand( String name )
{
	super( name );
}

protected CoUndoCommand createUndoCommand( double originalValue, double newValue )
{
	return new CoShapePageItemSetDoubleUndoCommand( getName(), m_targetView.getShapePageItem(), this, originalValue, newValue );
}

public final CoUndoCommand doExecute()
{
	CoUndoCommand undo = createUndoCommand( getDouble( m_targetView ), m_double );
	
	setDouble( m_targetView.getShapePageItem(), m_double );
	
	return undo;
}

public abstract double getDouble( CoShapePageItemView targetView );

public void prepare( CoShapePageItemView targetView, double d )
{
	prepare( targetView );
	m_double = d;
}

public abstract void setDouble( CoShapePageItemIF target, double d );
}