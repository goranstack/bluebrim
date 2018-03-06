package com.bluebrim.transact.shared;



/**
 * Server transaction service facade dispatching to transaction service
 * providers. All operations affect only the session/transaction associated
 * with the current thread. If no such session/transaction exists, one will
 * be created.
 * 
 * Note that all methods eventually dispatched to by this facade is
 * expected to take quite sometime to execute. Thus, the dispatching
 * overhead is insignificant, why no means to recieve the underlying
 * session/transaction (or similar) object is provided.
 * 
 * @author Markus Persson 2002-08-12
 */
public final class CoTransactionService {
//	private static CoTransactionSPI PROVIDER = (CoTransactionSPI) CoServices.getPreferredProvider(CoTransactionSPI.class);
//	private static boolean AVAILABLE = CoServices.isReal(PROVIDER);

	/**
	 * Prevent instantiation of this class.
	 * (Possibly, in the future, it will create one instance of itself
	 * and hand it to some maintainer for the ability to reinitialize
	 * its provider. This requires implementing a trivial interface.)
	 */
	private CoTransactionService() {
	}

	/**
	 * Whether this service is available or not. If it isn't, all other
	 * public static methods of this class will throw a
	 * <code>CoServiceNotAvailableException</code>.
	 */
	public static boolean isAvailable() {
		return true;
	}

	/**
	 * Begin a new transaction.
	 */
	public static void begin() throws CoBadTransactionStateException {
	//	PROVIDER.begin();
//		System.out.println("Begin transaction");
	}

	/**
	 * Commit the current transaction.
	 */
	public static void commit() throws CoBadTransactionStateException {
	//	PROVIDER.commit();
//		System.out.println("Commit transaction");
	}

	/**
	 * Abort (rollback) the current transaction.
	 */
	public static void abort() {
	//	PROVIDER.abort();
	}

	public static boolean inTransaction() {
		return false;
	//	return PROVIDER.inTransaction();
	}

	public static void unbind(String key) {
	//	PROVIDER.unbind(key);
	}

	public static Object lookup(String key) {
		return null;
	//	return PROVIDER.lookup(key);
	}

	/**
	 * Release the current session back to the pool.
	 */
	public static void release() {
	}
	
	}
