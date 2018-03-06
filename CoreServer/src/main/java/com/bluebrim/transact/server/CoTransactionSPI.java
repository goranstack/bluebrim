package com.bluebrim.transact.server;

import com.bluebrim.observable.CoChangedObjectListener;
import com.bluebrim.transact.shared.*;

/**
 * @author Markus Persson 2002-08-15
 */
public interface CoTransactionSPI {

	/**
	 * Begin a new transaction.
	 */
	public void begin() throws CoBadTransactionStateException;

	/**
	 * Commit the current transaction.
	 */
	public void commit() throws CoBadTransactionStateException;

	/**
	 * Abort (rollback) the current transaction.
	 */
	public void abort();

	public boolean inTransaction();

	public void bind(String key, Object value);

	public void unbind(String key);

	public Object lookup(String key);

	public Object localViewOf(Object object);

	public void addChangedObjectListener(CoChangedObjectListener l, Object obj);

	public void removeChangedObjectListener(CoChangedObjectListener l, Object obj);
	
	/**
	 * Release the current session back to the pool.
	 */
	public void release();
	
	// Temp
	public void objectChanged(Object obj);
}
