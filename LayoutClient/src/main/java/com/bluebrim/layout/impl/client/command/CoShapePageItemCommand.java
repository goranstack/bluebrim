package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.transact.shared.*;

public abstract class CoShapePageItemCommand extends CoUndoableCommand {

	protected CoShapePageItemView m_targetView;



protected CoShapePageItemCommand( String name )
{
	super( name );
}

public void prepare( CoShapePageItemView targetView )
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( targetView != null, "targetView == null" );
	m_targetView = targetView;
}
}