package com.bluebrim.gemstone.shared;

import java.io.*;
import java.util.*;

/**
	Interface which will be implemented by classes that can function
	as a context for a transaction, eg keep information and/or state that
	will be sent to the business object on the server and used to make sure
	that all objects that need to participate in the transaction are reached.
	When running in a test environment without GemStone/J it can also be used
	to collect all these objects so the session manager knows all objects that
	are to be viewed as changed by the transaction.
 */
public interface CoTransactionContextIF extends Serializable {
	public void addTransactionObject(Object o);
	public List getTransactionObjects();
}
