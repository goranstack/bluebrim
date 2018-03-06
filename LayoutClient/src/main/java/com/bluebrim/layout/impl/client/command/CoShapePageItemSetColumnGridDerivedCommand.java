package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.transact.shared.*;

/**
 * 
 *
 * @author: Dennis Malmström
 */

public class CoShapePageItemSetColumnGridDerivedCommand extends CoShapePageItemCommand
{





	protected boolean m_isDerived;

protected CoShapePageItemSetColumnGridDerivedCommand( String name )
{
	super( name );
}

public final CoUndoCommand doExecute()
{
	CoShapePageItemSetColumnGridDerivedUndoCommand undo = new CoShapePageItemSetColumnGridDerivedUndoCommand( getName(), m_targetView.getShapePageItem() );
	
	m_targetView.getShapePageItem().setDerivedColumnGrid( m_isDerived );
	
	return undo;
}

public void prepare( CoShapePageItemView targetView, boolean isDerived )
{
	prepare( targetView );
	m_isDerived = isDerived;
}
}