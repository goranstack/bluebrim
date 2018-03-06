package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
 
public class CoShapePageItemSetBaseLineGridDerivedUndoCommand extends CoShapePageItemUndoCommand
{
	protected boolean m_isDerived;
	protected CoImmutableBaseLineGridIF m_baseLineGrid;
public CoShapePageItemSetBaseLineGridDerivedUndoCommand( String name, CoShapePageItemIF target )
{
	super( name, target );

	m_isDerived = m_target.getBaseLineGrid().isDerived();

	if
		( m_isDerived )
	{
		m_baseLineGrid = null;
	} else {
		m_baseLineGrid = m_target.getBaseLineGrid();
	}
}
public boolean doRedo()
{
	m_target.setDerivedBaseLineGrid( ! m_isDerived );

	return true;
}
public boolean doUndo()
{
	m_target.setDerivedBaseLineGrid( m_isDerived );

	if
		( m_baseLineGrid != null )
	{
		m_target.getMutableBaseLineGrid().set(  m_baseLineGrid.getY0(), m_baseLineGrid.getDeltaY() );
	}

	return true;
}
}
