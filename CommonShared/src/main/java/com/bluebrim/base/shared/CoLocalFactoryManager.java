package com.bluebrim.base.shared;
import java.util.*;

import com.bluebrim.collection.shared.*;

/**
 * @see CoFactoryManager
 */
public class CoLocalFactoryManager extends CoFactoryManager{
	private Map m_factories;

public CoLocalFactoryManager ( ) {
	m_factories	 = new HashMap(100);
}


/**
 */
public void add(String key, CoFactoryIF factory) {
	m_factories.put(key, factory);
//	CoAssertion.logTimer("---"+key+" ");
}


public List getFactories(CoCollections.Selector selector) {
	// Can't use CoCollections.select since it cannot instantiate the correct type of collection
	// Gotta do it on my own
	Iterator iter = m_factories.values().iterator();
	List list = new ArrayList();
	while(iter.hasNext()) {
		Object obj = iter.next();
		if(selector.select(obj)) {
			list.add(obj);
		}
	}
	return list;
}


/**
 */
public CoFactoryIF getFactoryFor(String key) {
	return (CoFactoryIF )m_factories.get(key);
}


public Object getKeyFor(CoFactoryIF factory) {
	Iterator iter = m_factories.entrySet().iterator();
	while(iter.hasNext()) {
		Map.Entry entry = (Map.Entry)iter.next();
		if(entry.getValue().equals(factory)) {
			return entry.getKey();
		}
	}
	return null;
}


}