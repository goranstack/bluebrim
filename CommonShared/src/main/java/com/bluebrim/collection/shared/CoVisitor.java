package com.bluebrim.collection.shared;

import java.util.*;

/**
 * Creation date: (2000-11-17 09:21:08)
 * @author Dennis
 */
public class CoVisitor {

	protected boolean doContinue(Object result) {
		return true;
	}

	protected Object doit(Object target, Object result) {
		return result;
	}

	protected Iterator getChildren(Object target) {
		return CoIterators.empty();
	}

	public final Object visit(Object target, Object result) {
		result = doit(target, result);

		Object tmp = result;

		Iterator i = getChildren(target);
		while (doContinue(result) && i.hasNext()) {
			result = visit(i.next(), tmp);
		}
		return tmp;
	}
}
