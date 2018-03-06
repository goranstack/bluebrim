package com.bluebrim.layout.impl.client.command;

import com.bluebrim.base.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * 
 *
 * @author: Dennis Malmström
 */

public class CoShapePageItemReorderCurveCommand extends CoShapePageItemCommand
{





protected CoShapePageItemReorderCurveCommand( String name )
{
	super( name );
}

public CoUndoCommand doExecute()
{
	CoUndoCommand undo = new CoShapePageItemReorderCurveUndoCommand( getName(), m_targetView.getShapePageItem() );
	( (CoCurveShapeIF) m_targetView.getShapePageItem().getMutableCoShape() ).reorderPoints();
	return undo;
}
}