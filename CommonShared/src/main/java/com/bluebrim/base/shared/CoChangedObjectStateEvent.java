package com.bluebrim.base.shared;

import java.util.*;

/**
 * An event passed from ui servant to listener on the client
 * when the ui servan gets notified of a change to the server object.
 * <br>
 * This event also has a map containing the values for all 
 * changed attributes. This map is built in the servant, comparing 
 * against the last sent map. 
 */
public class CoChangedObjectStateEvent extends EventObject {
	private Object 	m_changedObject;
	private Map	   	m_changedValues;
/**
 * com.bluebrim.gemstone.shared.CoChangedObjectEvent constructor comment.
 * @param source java.lang.Object
 */
public CoChangedObjectStateEvent(Object source, Object changedObject, Map changedValues) {
	this(source, changedObject, changedValues, true);
}
public Object getChangedObject()
{
	return m_changedObject;
}
public Map getChangedValues()
{
	return m_changedValues;
}

	private boolean m_updateInSeparateThread = true;

/**
 * com.bluebrim.gemstone.shared.CoChangedObjectEvent constructor comment.
 * @param source java.lang.Object
 */
public CoChangedObjectStateEvent(Object source, Object changedObject, Map changedValues, boolean updateInSeparateThread) {
	super(source);
	m_changedObject 			= changedObject;
	m_changedValues 			= changedValues;
	m_updateInSeparateThread 	= updateInSeparateThread;
}

public boolean needsUpdateInSeparateThread()
{
	return m_updateInSeparateThread;
}
}