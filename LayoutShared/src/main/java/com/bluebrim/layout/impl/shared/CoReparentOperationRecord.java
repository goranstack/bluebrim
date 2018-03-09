package com.bluebrim.layout.impl.shared;

import com.bluebrim.transact.shared.*;

/**
 * See comments about reparent notification in CoLayoutArea.
 * 
 * @author: Dennis Malmström
 */
 
public class CoReparentOperationRecord extends CoPersistantReparentOperationRecord
{
	protected final CoRef m_childId;
	protected final CoRef m_oldParentId;
	protected final CoRef m_newParentId;
public CoReparentOperationRecord( CoPersistantReparentOperationRecord r )
{
	super( r.getId(), r.getChild(), r.getOldParent(), r.getNewParent() );
	
	m_childId = ( m_child == null ) ? null : m_child.getId();
	m_oldParentId = ( m_oldParent == null ) ? null : m_oldParent.getId();
	m_newParentId = ( m_newParent == null ) ? null : m_newParent.getId();
}
public CoRef getChildId()
{
	return m_childId;
}
public CoRef getNewParentId()
{
	return m_newParentId;
}
public CoRef getOldParentId()
{
	return m_oldParentId;
}
}
