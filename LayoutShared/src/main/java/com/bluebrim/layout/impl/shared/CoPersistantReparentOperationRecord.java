package com.bluebrim.layout.impl.shared;

import java.io.*;

import com.bluebrim.layout.shared.*;


/**
 * See comments about reparent notification in CoLayoutArea.
 * 
 * @author: Dennis Malmström
 */

public class CoPersistantReparentOperationRecord implements Serializable
{
	protected final CoPageItemIF m_child;
	protected final CoCompositePageItemIF m_oldParent;
	protected final CoCompositePageItemIF m_newParent;

	protected int m_id;

public CoPageItemIF getChild()
{
	return m_child;
}
public int getId()
{
	return m_id;
}



public CoPersistantReparentOperationRecord( int id, CoPageItemIF child, CoCompositePageItemIF oldParent, CoCompositePageItemIF newParent )
{
	m_id = id;
	
	m_child = child;
	m_oldParent = oldParent;
	m_newParent = newParent;
}

public CoCompositePageItemIF getNewParent()
{
	return m_newParent;
}

public CoCompositePageItemIF getOldParent()
{
	return m_oldParent;
}
}