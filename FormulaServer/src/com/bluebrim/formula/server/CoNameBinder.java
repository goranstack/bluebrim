package com.bluebrim.formula.server;

import com.bluebrim.formula.shared.*;

/**
 * Creation date: (1999-12-15 15:56:16)
 * @author: Göran Stäck 
 */
public class CoNameBinder implements CoNameBinderIF {

	private CoAbstractVariablesHolderIF m_variables;

	public CoNameBinder(CoAbstractVariablesHolderIF variables) {
		m_variables = variables;
	}
	public CoAbstractVariableIF bindName(String name) {
		return m_variables.getVariable(name, true);
	}
}
