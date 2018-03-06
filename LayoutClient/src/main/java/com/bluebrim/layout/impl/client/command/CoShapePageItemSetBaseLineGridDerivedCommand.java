package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.transact.shared.*;

/**
 * 
 *
 * @author: Dennis Malmström
 */

public class CoShapePageItemSetBaseLineGridDerivedCommand extends CoShapePageItemCommand
{





	protected boolean m_isDerived;

protected CoShapePageItemSetBaseLineGridDerivedCommand( String name )
{
	super( name );
}

public final CoUndoCommand doExecute()
{
	CoShapePageItemSetBaseLineGridDerivedUndoCommand undo = new CoShapePageItemSetBaseLineGridDerivedUndoCommand( getName(), m_targetView.getShapePageItem() );
	
	m_targetView.getShapePageItem().setDerivedBaseLineGrid( m_isDerived );
	
	return undo;
}

public void prepare( CoShapePageItemView targetView, boolean isDerived )
{
	prepare( targetView );
	m_isDerived = isDerived;
}
}