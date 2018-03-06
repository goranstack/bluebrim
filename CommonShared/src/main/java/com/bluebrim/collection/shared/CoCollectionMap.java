package com.bluebrim.collection.shared;

import java.util.*;

import com.bluebrim.base.shared.*;

/**
 * A map of collections. Adding to and removing from the collections is done by
 * specifying a factorykey as an argument
 * Creation date: (2001-08-20 16:22:01)
 * @author: Mikael Printz
 */
public class CoCollectionMap {
	protected Map m_collections = new HashMap();





/**
 *	Returns an iterator over all elements in the collection map
 */	
public Iterator iterator() {
	Iterator iter = m_collections.keySet().iterator();
	Iterator retIter = null;
	Object key = null;
	if(iter.hasNext()) {
		key = iter.next();
		retIter = iterator(key);
	}
	 
	while(iter.hasNext()) {
		key = iter.next();
		retIter = CoIterators.concat(retIter, iterator(key));
	}
	return retIter;
}


/**
 * CoFreezableCollectionMap constructor comment.
 */
public CoCollectionMap() {
	super();
}


public boolean add(Object obj, Object key) {
	return getOrAddCollection(key).add(obj);
}


public void each(CoCollections.EachDo doer) {
	Iterator iter = m_collections.keySet().iterator();
	while(iter.hasNext()) {
		each(doer, iter.next());
	}
}


public void each(CoCollections.EachDo eachDo, Object key) {
	CoCollections.each(getOrAddCollection(key), eachDo);
}


public CoVerifiableCollection get(Object key) {
	return (CoVerifiableCollection)m_collections.get(key);
}


protected CoVerifiableCollection getOrAddCollection(Object obj) {
	CoVerifiableCollection coll = (CoVerifiableCollection)m_collections.get(obj);
	if(coll == null) {
		coll = new CoVerifiableCollection();
		m_collections.put(obj, coll);
	}
	return coll;
}


public Iterator iterator(Object key) {
	return getOrAddCollection(key).iterator();
}


public Set keySet() {
	return m_collections.keySet();
}








public boolean remove(Object obj, Object key) {
	return getOrAddCollection(key).remove(obj);
}
}