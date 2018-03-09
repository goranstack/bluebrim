package com.bluebrim.formula.impl.server.parsetree;

/**
 * CoVariableNode delegates evaluation to its variable. This inteface let the variable set
 * evaluationType for the CoVariableNode without exposing the whole protocol for the variable.
 * Creation date: (1999-12-16 02:15:45)
 * @author: Göran Stäck. 
 */
public interface CoTypedFormulaNodeIF {
	public static final int NO_TYPE = 0;
	public static final int BOOLEAN_TYPE = 1;
	public static final int NUMBER_TYPE = 2;
	public static final int STRING_TYPE = 3;

public void setEvaluationType (int type);
}
