package com.bluebrim.layout.impl.client.transfer;

import java.awt.geom.*;
import java.util.*;

import com.bluebrim.layout.impl.client.*;

class CoCompoundDropOperation extends CoAbstractDropOperation {

	private List m_operations = new ArrayList();

	public CoCompoundDropOperation() {
		super();
	}

	public double operatorMatch(Object object) {
		CoAbstractDropOperation op = getBestMatchingOperation(object);

		return (op != null) ? op.operatorMatch(object) : NO_MATCH;
	}

	public void addOperation(CoAbstractDropOperation operation) {
		m_operations.add(operation);
	}

	private CoAbstractDropOperation getBestMatchingOperation(Object target) {
		CoAbstractDropOperation op, bestOp = null;
		double bestVal = NO_MATCH;
		Iterator i = m_operations.iterator();

		while (i.hasNext()) {
			op = (CoAbstractDropOperation) i.next();
			if (op.operatorMatch(target) > bestVal) {
				bestVal = op.operatorMatch(target);
				bestOp = op;
			}
		}

		return bestOp;
	}

	public boolean operateOn(CoUndoableCommandExecutor executor, Object target, Point2D position, Object operand) {
		CoAbstractDropOperation operation = getBestMatchingOperation(target);

		return operation.operateOn(executor, target, position, operand);
	}

	public String getDescription(String nameOfTarget) {
		if (m_operations.isEmpty()) {
			return "";
		} else {
			return ((CoAbstractDropOperation) m_operations.get(0)).getDescription(nameOfTarget);
		}
	}
}