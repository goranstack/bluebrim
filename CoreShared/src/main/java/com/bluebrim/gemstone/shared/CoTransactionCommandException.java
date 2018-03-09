package com.bluebrim.gemstone.shared;


/**
 * An unchecked exception that can be thrown by <code>CoCommand.doExecute()</code> 
 * when, for some reason, the action in this method is unable to correctly execute. 
 * This exception is catched in the session managers <code>doInTransantion</code> and 
 * transformed to a <code>CoTransactionException</code> with the original exception's
 * message as its own. This message is then displayed for the user.
 * Creation date: (1999-12-28 12:44:01)
 * @author: Lasse Svadängs
 */
public class CoTransactionCommandException extends RuntimeException {

	public CoTransactionCommandException() {
		super();
	}

	public CoTransactionCommandException(String message) {
		super(message);
	}
}