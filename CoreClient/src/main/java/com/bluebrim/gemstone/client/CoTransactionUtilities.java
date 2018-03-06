package com.bluebrim.gemstone.client;

import java.text.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.gemstone.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.observable.*;
import com.bluebrim.transact.shared.*;

/**
 * Contains static help methods used when making persistent changes on Gemstone/J.
 * Creation date: (2000-01-08 17:38:11)
 * @author Lasse Svadängs
 */
public class CoTransactionUtilities implements CoGemStoneUIConstants {

	private static class UndoCommandWrapper extends CoAbstractUndoCommand {
		private CoUndoCommand m_command;

		public UndoCommandWrapper(CoUndoCommand command) {
			m_command = command;
		}

		public String getName() {
			return m_command.getName();
		}
		public void setName(String name) {
			m_command.setName(name);
		}

		public final void undo() {
			super.undo();
			CoTransactionUtilities.undo(m_command, null);
		}

		public final void redo() {
			super.redo();
			CoTransactionUtilities.redo(m_command, null);
		}
/*
		public boolean addEdit( javax.swing.undo.UndoableEdit e ) { return m_command.addEdit( e ); }
		public boolean canRedo() { return m_command.canRedo(); }
		public boolean canUndo() { return m_command.canUndo(); }
		public void die() { m_command.die(); }
		public String getPresentationName() { return m_command.getPresentationName(); }
		public String getRedoPresentationName() { return m_command.getRedoPresentationName(); }
		public String getUndoPresentationName() { return m_command.getUndoPresentationName(); }
		public boolean isSignificant() { return m_command.isSignificant(); }
		public boolean replaceEdit( javax.swing.undo.UndoableEdit e ) { return m_command.replaceEdit( e ); }
*/
	}

	private CoTransactionUtilities() {
	}

	public static void execute(CoCommand cmd, Object target) {
		try {
			CoCmdKit.doInTransaction(cmd);
			CoObservable.objectChanged(target);
		} catch (CoTransactionException e) {
			handleTransactionException(e, cmd);
		}
	}

	public static void handleTransactionException(CoTransactionException e, String errorMsg) {
//		showErrorDialog(e.m_detail, errorMsg);
		showErrorDialog(e, errorMsg);
	}

	private static void handleTransactionException(CoTransactionException e, CoCommand transaction) {
		if (e.m_detail != null || CoBaseUtilities.stringIsEmpty(e.getMessage())) {
			MessageFormat msgFormat = new MessageFormat(CoGsUIStringResources.getName(UNABLE_TO_COMMIT_NO_ERROR));
			handleTransactionException(e, msgFormat.format(new Object[] { transaction.getName()}));
		} else
			handleTransactionException(e, e.getMessage());

	}

	public static void showErrorDialog(Throwable e, String errorMsg) {
		CoErrorDialog.open(errorMsg, e);
	}

	public static CoAbstractUndoCommand execute(CoUndoableCommand cmd, Object target) {
		try {
			// NOTE: Won't work since outside of transaction. /Markus
			if (CoAssertion.SIMULATION_SUPPORT) {
				CoAssertion.addChangedObject(target);
			}
			CoUndoCommand undo = CoCmdKit.doInTransaction(cmd);
			return new UndoCommandWrapper(undo);
		} catch (CoTransactionException e) {
			handleTransactionException(e, cmd);
			return null;
		}
	}

	private static void handleTransactionException(CoTransactionException e, CoAbstractUndoCommand transaction) {
		if (e.m_detail != null || CoBaseUtilities.stringIsEmpty(e.getMessage())) {
			MessageFormat msgFormat = new MessageFormat(CoGsUIStringResources.getName(UNABLE_TO_COMMIT_NO_ERROR));
			handleTransactionException(e, msgFormat.format(new Object[] { transaction.getName()}));
		} else
			handleTransactionException(e, e.getMessage());

	}

	private static void handleTransactionException(CoTransactionException e, CoUndoableCommand transaction) {
		if (e.m_detail != null || CoBaseUtilities.stringIsEmpty(e.getMessage())) {
			MessageFormat msgFormat = new MessageFormat(CoGsUIStringResources.getName(UNABLE_TO_COMMIT_NO_ERROR));
			handleTransactionException(e, msgFormat.format(new Object[] { transaction.getName()}));
		} else
			handleTransactionException(e, e.getMessage());

	}

	public static void redo(CoUndoCommand cmd, Object target) {
		try {
			// NOTE: Won't work since outside of transaction. /Markus
			if (CoAssertion.SIMULATION_SUPPORT) {
				CoAssertion.addChangedObject(target);
			}
			CoCmdKit.redoInTransaction(cmd, 0);
		} catch (CoTransactionException e) {
			handleTransactionException(e, cmd);
		}
	}

	public static void undo(CoUndoCommand cmd, Object target) {
		try {
			// NOTE: Won't work since outside of transaction. /Markus
			if (CoAssertion.SIMULATION_SUPPORT) {
				CoAssertion.addChangedObject(target);
			}
			CoCmdKit.undoInTransaction(cmd, 0);
		} catch (CoTransactionException e) {
			handleTransactionException(e, cmd);
		}
	}

	/**
	 * Convenience method where the target is null, since the target is a
	 * bug-prone old relic that puts object modification responsibility for
	 * Gemstone simulation and the real thing in totally different places.
	 *
	 * @author Markus Persson 2001-10-19
	 */
	public static void execute(CoCommand command) {
		execute(command, null);
	}

	/**
	 * Convenience method where the target is null, since the target is a
	 * bug-prone old relic that puts object modification responsibility for
	 * Gemstone simulation and the real thing in totally different places.
	 *
	 * @author Markus Persson 2001-10-19
	 */
	public static void execute(CoUndoableCommand command) {
		execute(command, null);
	}

}