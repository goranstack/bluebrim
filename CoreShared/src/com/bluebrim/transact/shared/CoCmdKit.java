package com.bluebrim.transact.shared;

import com.bluebrim.base.shared.debug.*;
import com.bluebrim.gemstone.shared.*;

/**
 * Helper class that can execute a command in a transaction.
 * 
 * This is separated from CoTransactionService because it belongs
 * more to commands and should be moved along with commands if
 * they are moved to a different domain. It is not included in
 * the command classes themselves to isolate them from transaction
 * knowledge which belongs to the server only. (Although commands
 * in the future most likely should remain on the server, one can
 * imagine that some can be transferred from the client.)
 * 
 * These methods have been factored out from session implementations
 * where they where duplicated.
 * 
 * @author Markus Persson 2002-09-13
 */
public class CoCmdKit {

	/**
	 * Prevent instantiation of this class.
	 */
	private CoCmdKit() {
	}

	/**
	 * Execute <code>cmd</code> in a transaction with 5 retries.
	 * 
	 * @author Markus Persson 2002-09-13
	 */
	public static void doInTransaction(CoCommand cmd) throws CoTransactionException {
		doInTransaction(cmd, 5);
	}

	/**
	 * Execute <code>cmd</code> in a transaction with <code>retries</code> retries.
	 * 
	 * @author Markus Persson 2002-09-13
	 */
	public static void doInTransaction(CoCommand cmd, int retries) throws CoTransactionException {
		while (retries-- >= 0) {
			try {
				cmd.prepare();
				CoTransactionService.begin();
				if (!cmd.doExecute()) {
					throw new CoTransactionCommandException();
				}
				CoTransactionService.commit();
				cmd.finish();
				return;
				// PENDING: Should be a different exception signaling conflict.
				// Bad state exception should not be caught? /Markus 2002-09-12
			} catch (CoBadTransactionStateException e) {
				cmd.abort();
				CoTransactionService.abort();
				// Fall thru to retries
			} catch (CoSilentTransactionCommandException e) {
				cmd.abort();
				CoTransactionService.abort();
				return;
			} catch (CoTransactionCommandException e) {
				cmd.abort();
				CoTransactionService.abort();
				throw new CoTransactionException(e.getMessage());
			} catch (Throwable t) {
				cmd.abort();
				CoTransactionService.abort();
				throw new CoTransactionException("Transaction failed:", t);
			}
		}

		throw new CoTransactionException("Transaction failed despite retries.");
	}

	/**
	 * Execute <code>cmd</code> in a transaction with 5 retries and marks
	 * <code>modifiedObject</code> as modified for simulation purposes.
	 * 
	 * NOTE: Passing an array or list of objects to mark as modified will
	 * not work anymore. Such annoying code should be fixed immediately ...
	 * 
	 * @deprecated use a markAsModified method instead. Aspects would be good.
	 * @author Markus Persson 2002-09-13
	 */
	public static void doInTransaction(CoCommand cmd, Object modifiedObject) throws CoTransactionException {
		int retries = 5;
		while (retries-- >= 0) {
			try {
				cmd.prepare();
				CoTransactionService.begin();
				if (!cmd.doExecute()) {
					throw new CoTransactionCommandException();
				}
				if (CoAssertion.SIMULATION_SUPPORT) {
					CoAssertion.addChangedObject(modifiedObject);
				}
				CoTransactionService.commit();
				cmd.finish();
				return;
				// PENDING: Should be a different exception signaling conflict.
				// Bad state exception should not be caught? /Markus 2002-09-12
			} catch (CoBadTransactionStateException e) {
				cmd.abort();
				CoTransactionService.abort();
				// Fall thru to retries
			} catch (CoSilentTransactionCommandException e) {
				cmd.abort();
				CoTransactionService.abort();
				return;
			} catch (CoTransactionCommandException e) {
				cmd.abort();
				CoTransactionService.abort();
				throw new CoTransactionException(e.getMessage());
			} catch (Throwable t) {
				cmd.abort();
				CoTransactionService.abort();
				throw new CoTransactionException("Transaction failed:", t);
			}
		}

		throw new CoTransactionException("Transaction failed despite retries.");
	}

	/**
	 * Execute <code>cmd</code> in a transaction with 5 retries.
	 * 
	 * @author Markus Persson 2002-09-13
	 */
	public static CoUndoCommand doInTransaction(CoUndoableCommand cmd) throws CoTransactionException {
		return doInTransaction(cmd, 5);
	}

	/**
	 * Execute <code>cmd</code> in a transaction with <code>retries</code> retries.
	 * 
	 * @author Markus Persson 2002-09-13
	 */
	public static CoUndoCommand doInTransaction(CoUndoableCommand cmd, int retries) throws CoTransactionException {
		while (retries-- >= 0) {
			try {
				cmd.prepare();
				CoTransactionService.begin();
				CoUndoCommand undo = cmd.doExecute();

				if (undo == null) {
					throw new CoTransactionCommandException("No undo command returned from undoable command " + cmd.getName());
				}
				CoTransactionService.commit();
				cmd.finish();
				return undo;
				// PENDING: Should be a different exception signaling conflict.
				// Bad state exception should not be caught? /Markus 2002-09-12
			} catch (CoBadTransactionStateException e) {
				cmd.abort();
				CoTransactionService.abort();
				// Fall thru to retries
			} catch (CoSilentTransactionCommandException e) {
				cmd.abort();
				CoTransactionService.abort();
				return null;
			} catch (CoTransactionCommandException e) {
				cmd.abort();
				CoTransactionService.abort();
				throw new CoTransactionException(e.getMessage());
			} catch (Throwable t) {
				cmd.abort();
				CoTransactionService.abort();
				throw new CoTransactionException("Transaction failed:", t);
			}
		}

		throw new CoTransactionException("Transaction failed despite retries.");
	}
	
	/**
	 * Execute <code>cmd</code> in a transaction with <code>retries</code> retries.
	 * 
	 * @author Markus Persson 2002-09-13
	 */
	public static void redoInTransaction(CoUndoCommand cmd, int retries) throws CoTransactionException {
		while (retries-- >= 0) {
			try {
				cmd.prepareRedo();
				CoTransactionService.begin();
				cmd.doRedo();
				CoTransactionService.commit();
				cmd.finishRedo();
				return;
				// PENDING: Should be a different exception signaling conflict.
				// Bad state exception should not be caught? /Markus 2002-09-13
			} catch (CoBadTransactionStateException e) {
				cmd.abortRedo();
				CoTransactionService.abort();
				// Fall thru to retries
			} catch (CoSilentTransactionCommandException e) {
				cmd.abortRedo();
				CoTransactionService.abort();
				return;
			} catch (CoTransactionCommandException e) {
				cmd.abortRedo();
				CoTransactionService.abort();
				throw new CoTransactionException(e.getMessage());
			} catch (Throwable t) {
				cmd.abortRedo();
				CoTransactionService.abort();
				throw new CoTransactionException("Transaction failed:", t);
			}
		}

		throw new CoTransactionException("Transaction failed despite retries.");
	}

	/**
	 * Execute <code>cmd</code> in a transaction with <code>retries</code> retries.
	 * 
	 * @author Markus Persson 2002-09-13
	 */
	public static void undoInTransaction(CoUndoCommand cmd, int retries) throws CoTransactionException {
		while (retries-- >= 0) {
			try {
				cmd.prepareUndo();
				CoTransactionService.begin();
				cmd.doUndo();
				CoTransactionService.commit();
				cmd.finishUndo();
				return;
				// PENDING: Should be a different exception signaling conflict.
				// Bad state exception should not be caught? /Markus 2002-09-13
			} catch (CoBadTransactionStateException e) {
				cmd.abortUndo();
				CoTransactionService.abort();
				// Fall thru to retries
			} catch (CoSilentTransactionCommandException e) {
				cmd.abortUndo();
				CoTransactionService.abort();
				return;
			} catch (CoTransactionCommandException e) {
				cmd.abortUndo();
				CoTransactionService.abort();
				throw new CoTransactionException(e.getMessage());
			} catch (Throwable t) {
				cmd.abortUndo();
				CoTransactionService.abort();
				throw new CoTransactionException("Transaction failed:", t);
			}
		}

		throw new CoTransactionException("Transaction failed despite retries.");
	}

}
