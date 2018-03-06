package com.bluebrim.formula.shared;

import java.rmi.*;



/**
 * Interface for CoVariableBinder.
 * Creation date: (1999-12-14 23:58:13)
 * @author: Göran Stäck
 */
public interface CoVariableBinderIF extends Remote {
public Object bindValue(CoAbstractVariableIF variable) throws CoFormulaException;
}
