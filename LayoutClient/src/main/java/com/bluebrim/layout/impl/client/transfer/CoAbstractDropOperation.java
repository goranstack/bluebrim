package com.bluebrim.layout.impl.client.transfer;

import java.awt.geom.*;

import com.bluebrim.base.shared.debug.*;
import com.bluebrim.gemstone.shared.*;
import com.bluebrim.layout.impl.client.*;
import com.bluebrim.transact.shared.*;

/**
 * This type was created by a moron.
 */
public abstract class CoAbstractDropOperation {
	public final static double NO_MATCH = 0.0;
	public final static double OK_MATCH = 0.5;
	public final static double PERFECT_MATCH = 1.0;

	public CoAbstractDropOperation() {
		super();
	}

	protected void handleTransactionException(CoCommand c, CoTransactionException ex) {
		CoAssertion.trace("CoCommand failed: " + c + "\n" + ex);
	}

	protected void handleTransactionException(CoUndoableCommand c, CoTransactionException ex) {
		CoAssertion.trace("CoCommand failed: " + c + "\n" + ex);
	}

	public abstract boolean operateOn(CoUndoableCommandExecutor executor, Object object, Point2D position, Object operand);

	public abstract double operatorMatch(Object object);

	public abstract String getDescription(String nameOfTarget);
}