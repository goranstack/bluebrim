package com.bluebrim.base.shared;

import java.io.*;
import java.util.*;

/**
 * Standalone serializable implementation of CoUIContext.
 *
 * This is intended for use in bookmarks and any other case where launching
 * a standalone UI that needs more state then their domain object can provide.
 * Possibly also usable in UI servants.
 *
 * @see CoUIContext
 * @author Markus Persson 2000-10-02
 */
public class CoGenericUIContext implements CoUIContext, Serializable {
	private Map m_envMap;

public CoGenericUIContext(String[] keysToCopy, CoUIContext sourceContext) {
	m_envMap = new HashMap(keysToCopy.length);
	addStateFrom(keysToCopy, sourceContext);
}
/**
 * Add state to this context object.
 *
 * The string array is most often is statically defined in the UI class
 * calling this method. The same context object is then passed from each
 * subclassing level using super.getCopyOfCurrentRequiredUIContext() and
 * state is added at each level.
 *
 * @author Markus Persson 2000-10-02
 */
public final void addStateFrom(String[] keysToCopy, CoUIContext sourceContext) {
	if (sourceContext == null)
		return;

	int size = keysToCopy.length;
	Map map = m_envMap;
	for(int i = 0; i < size; i++) {
		String key = keysToCopy[i];
		map.put(key, sourceContext.getStateFor(key));
	}
}
/**
 * Implementation of CoUIContext.
 *
 * Returns the state given by this context for the given key.
 *
 * @author Markus Persson 2000-10-02
 */
public Object getStateFor(String key) {
	return m_envMap.get(key);
}
}
