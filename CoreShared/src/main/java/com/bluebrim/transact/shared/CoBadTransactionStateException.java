package com.bluebrim.transact.shared;

/**
 * Exception thrown when trying to commit without
 * a transaction in progress or trying to begin
 * when one is in progress.
 * 
 * PENDING: Replace with IllegalStateException?
 * 
 * @author Markus Persson 2002-08-26
 */
public class CoBadTransactionStateException extends Exception {

	public CoBadTransactionStateException() {
	}

	public CoBadTransactionStateException(String message) {
		super(message);
	}
}
