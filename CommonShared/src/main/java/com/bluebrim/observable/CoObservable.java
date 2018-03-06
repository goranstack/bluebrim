package com.bluebrim.observable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Replacement for the ODBMS simulation aproach in the BlueBrim
 * project.
 * 
 * @author Göran Stäck 2003-04-09
 *
 */
public class CoObservable {

	private static CoObservable m_instance = new CoObservable();
	
	// Should be identity based ...
	private Map m_changedObjectListeners = new HashMap();

	private void _addChangedObjectListener(CoChangedObjectListener l, Object obj) {
		Set listeners = (Set) m_changedObjectListeners.get(obj);
		if (listeners == null) {
			listeners = new HashSet();
			m_changedObjectListeners.put(obj, listeners);
		}
		listeners.add(l);
	}

	private void _removeChangedObjectListener(CoChangedObjectListener l, Object obj) {
		Set listeners = (Set) m_changedObjectListeners.get(obj);
		if (listeners != null) {
			listeners.remove(l);
			if (listeners.size() == 0) {
				m_changedObjectListeners.remove(obj);
			}
		}
	}

	private void _objectChanged(Object changedObject) {
		// It's necessary to clone the set as it can be
		// modified by the listener.
		HashSet listeners = (HashSet) m_changedObjectListeners.get(changedObject);
		if (listeners != null) {
			CoChangedObjectEvent event = new CoChangedObjectEvent(this, changedObject);
			Iterator iter = ((Set) listeners.clone()).iterator();
			while (iter.hasNext()) {
				((CoChangedObjectListener) iter.next()).serverObjectChanged(event);
			}
		}

	}


	public static void addChangedObjectListener(CoChangedObjectListener l, Object obj) {
		m_instance._addChangedObjectListener(l, obj);
	}

	public static void removeChangedObjectListener(CoChangedObjectListener l, Object obj) {
		m_instance._removeChangedObjectListener(l, obj);
	}

	public static void objectChanged(Object changedObject) {
		m_instance._objectChanged(changedObject);
	}


}
