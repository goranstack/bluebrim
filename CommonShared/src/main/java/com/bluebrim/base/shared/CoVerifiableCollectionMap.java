package com.bluebrim.base.shared;
import java.util.*;

import com.bluebrim.collection.shared.*;

/**
 * A map of verifiable collections. Adding to and removing from the collections is done by
 * specifying a factorykey as an argument
 * Creation date: (2000-06-09 16:22:01)
 * @author: 
 */
public class CoVerifiableCollectionMap extends CoCollectionMap {


/**
 * CoFreezableCollectionMap constructor comment.
 */
public CoVerifiableCollectionMap() {
	super();
}


public boolean add(Object obj, Object key) {
	return getOrAddCollection(key).add(obj);
}


public void each(CoCollections.EachDo eachDo, Object key) {
	CoCollections.each(getOrAddCollection(key), eachDo);
}


public Iterator iterator(Object key) {
	return getOrAddCollection(key).iterator();
}


public void registerVerifier(CoVerifiableCollection.Verifier verifier, Object key) {
	CoVerifiableCollection coll = (CoVerifiableCollection)getOrAddCollection(key);
	coll.registerVerifier(verifier);
}


public void registerVerifierForAll(CoVerifiableCollection.Verifier verifier) {
	Iterator iter = m_collections.values().iterator();
	while(iter.hasNext()) {
		((CoVerifiableCollection)iter.next()).registerVerifier(verifier);
	}
}


public boolean remove(Object obj, Object key) {
	return getOrAddCollection(key).remove(obj);
}
}