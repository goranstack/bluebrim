package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
 
public abstract class CoShapePageItemSetGeometryUndoCommand extends CoShapePageItemUndoCommand
{
	protected CoImmutableLocationSpecIF m_locationSpec;
	protected CoImmutableSizeSpecIF m_widthSpec;
	protected CoImmutableSizeSpecIF m_heightSpec;

	protected CoImmutableLocationSpecIF m_tmpLocationSpec;
	protected CoImmutableSizeSpecIF m_tmpWidthSpec;
	protected CoImmutableSizeSpecIF m_tmpHeightSpec;
public CoShapePageItemSetGeometryUndoCommand( String name, CoShapePageItemIF target )
{
	super( name, target );

	m_locationSpec = target.getLocationSpec();
	m_widthSpec = target.getWidthSpec();
	m_heightSpec = target.getHeightSpec();

}
public boolean doRedo()
{
	preSetGeometry();
	postSetGeometry();
	return true;
}
public boolean doUndo()
{
	preSetGeometry();
	postSetGeometry();
	return true;
}
protected void postSetGeometry()
{
	m_target.setLayoutSpecs( m_tmpLocationSpec, m_tmpWidthSpec, m_tmpHeightSpec );
}
protected void preSetGeometry()
{
	m_tmpLocationSpec = m_locationSpec;
	m_tmpWidthSpec = m_widthSpec;
	m_tmpHeightSpec = m_heightSpec;
	
	m_locationSpec = m_target.getLocationSpec();
	m_widthSpec = m_target.getWidthSpec();
	m_heightSpec = m_target.getHeightSpec();
}
}
