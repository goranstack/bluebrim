package com.bluebrim.layout.impl.client.command;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
 
public class CoShapePageItemReorderCurveUndoCommand extends CoShapePageItemUndoCommand
{
public CoShapePageItemReorderCurveUndoCommand( String name, CoShapePageItemIF target )
{
	super( name, target );
}
public boolean doRedo()
{
	doUndo();
	return true;
}
public boolean doUndo()
{
	( (CoCurveShapeIF) m_target.getMutableCoShape() ).reorderPoints();
	return true;
}
}
