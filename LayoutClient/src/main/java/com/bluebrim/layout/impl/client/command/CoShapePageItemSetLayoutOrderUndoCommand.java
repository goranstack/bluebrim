package com.bluebrim.layout.impl.client.command;

import java.util.*;

import com.bluebrim.layout.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
 
public class CoShapePageItemSetLayoutOrderUndoCommand extends CoShapePageItemUndoCommand
{
	protected Comparator m_order;
public CoShapePageItemSetLayoutOrderUndoCommand( String name, CoShapePageItemIF target, Comparator order )
{
	super( name, target );
	
	m_order = order;
}
public boolean doRedo()
{
	doUndo();
	return true;
}
public boolean doUndo()
{
	m_order = ( (CoCompositePageItemIF) m_target ).setChildrenLayoutOrder( m_order );

	return true;
}
}
