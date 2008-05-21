package com.bluebrim.transact.shared;

import java.lang.ref.*;
import java.util.*;

/**
 * CoRefManager concretization using local mapping.
 * 
 * This is primarily intended for GemStone simulation.
 * It seems that weak references would be a good thing
 * here since you only should make references to objects
 * being held somewhere (in reality "persistent objects").
 *
 * @author Markus Persson 2001-01-15
 */
public class CoLocalRefManager extends CoRefManager {
	private Map m_refToWeakObj = new WeakHashMap();
	private Map m_objToRef = new WeakHashMap();
	private int m_nextID = 0;

	public synchronized CoRef getRefTo(Object object) {
		// The double check idiom is broken. Probably applies
		// here as well, which is why we simply synchronize
		// the entire method.
		CoRef ref = (CoRef) m_objToRef.get(object);
		if (ref == null) {
			ref = new CoLocalRef(m_nextID++, object);
			m_objToRef.put(object, ref);
			m_refToWeakObj.put(ref, new WeakReference(object));
		}
		return ref;
	}

	/**
	 * Return a SemiPersistent ID for the given object.
	 *
	 * @see CoRef#getSpidFor(Object)
	 * @see CoSpidable
	 */
	public int getSpidFor(Object obj) {
		return ((CoLocalRef) getRefTo(obj)).m_id;
	}

	public synchronized Object resolve(CoLocalRef ref) {
		WeakReference weak = (WeakReference) m_refToWeakObj.get(ref);
		return (weak != null) ? weak.get() : null;
	}
}
