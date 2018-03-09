package com.bluebrim.formula.impl.server;

import java.util.*;

import com.bluebrim.formula.shared.*;

/**
 * Abstract superclass for objects that takes part in evaluation of formulas by
 * providing a context for binding variables to values.
 * Creation date: (1999-12-15 15:35:42)
 * @author: Göran Stäck
 */
public abstract class CoVariableBinder implements CoVariableBinderIF {
	private  List 		m_loggedVariables	= new ArrayList();
/**
 * CoVariableBinder constructor comment.
 */
public CoVariableBinder() {
	super();
}
/*
 * Binds the variable in the argument to a value. The value is expressed as a CoFormulaNode.
 * Delegate the binding to the variable it self who knows what kind of specific CoVariableBinder
 * we are. The variable will type cast us to that specific kind and can then ask us for objects
 * he needs for the binding.
 * 
 */
public Object bindValue(CoAbstractVariableIF variable) throws CoFormulaException {
	if (isLooping(variable)) {
		m_loggedVariables.clear();
		throw new CoVariableBindingException(CoFormulaErrorResources.getName(CoFormulaErrorResources.LOOPING_SEQUENCE_FOUND_IN_NODE)+" "+variable.toString());
	};

	return variable.bindValue(this);
}
private boolean isLooping (CoAbstractVariableIF variable) {
	boolean isLooping = false;
	
	for (int i = 0; !isLooping && i < m_loggedVariables.size(); i++) {
		// is looping if variable has been logged before
		isLooping = (m_loggedVariables.get(i) == variable);
	}
	m_loggedVariables.add(variable);
	return isLooping;
}
}
