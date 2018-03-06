package com.bluebrim.transact.shared;
import java.io.*;

/**
 * Reference to a persistant object. Can be used on
 * a server to obtain a (view of a) persistant object.
 * Corresponds to the GemStone/J interface SharedObjRef.
 *
 * NOTE: CoRef:s should NEVER be made persistant. As they
 * are intended to be sent to the client they must be "weak"
 * references then. Although it is possible to make them
 * strong while on the server, it could be confusing. Which is
 * why the reference is maintained using a OID that although
 * stable during continuous periods of client accessibility,
 * may change during migration and similar operations.
 *
 * @author Markus Persson 2000-09-05
 */
public abstract class CoRef implements Serializable {
	protected static final CoRefManager MANAGER = initializeManager();

/**
 * Resolve reference.
 *
 * NOTE: While it may seem that all subclasses to CoRef
 * implements this method exactly the same, separate
 * implementations are required because we rely on method
 * overloading which is determined at compile time, NOT
 * dynamically at runtime.
 *
 * @author Markus Persson 2001-01-15
 */
public abstract Object get();


private static final CoRefManager initializeManager() {
//	if (CoEnv.inGemstoneVM()) {
//		return new CoGemstoneRefManager();
//	} else {
		return new CoLocalRefManager();
//	}
}


public static final CoRef to(Object obj) {
	return MANAGER.getRefTo(obj);
}
}