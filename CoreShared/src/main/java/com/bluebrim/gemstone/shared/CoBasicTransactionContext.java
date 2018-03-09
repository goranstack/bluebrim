package com.bluebrim.gemstone.shared;

import java.util.*;

/**
	An implementation of <code>com.bluebrim.gemstone.shared.CoTransactionContextIF</code> that can collect all objects that 
	are changed by a transaction. When running in a test environment without GemStone/J, this information is 
	used by the session manager for a simulation of GemStone/J's notifying mechanism.
 */
public class CoBasicTransactionContext implements CoTransactionContextIF {
	Set transactionObjects;

	public CoBasicTransactionContext() {
		transactionObjects = new TreeSet();
	}

	public void addTransactionObject(Object o) {
		transactionObjects.add(o);
	}

	public List getTransactionObjects() {
		return new ArrayList(transactionObjects);
	}

	public void resetTransactionObjects(Object o) {
		transactionObjects.clear();
	}
}
