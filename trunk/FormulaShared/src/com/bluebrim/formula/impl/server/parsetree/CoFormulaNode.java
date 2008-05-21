package com.bluebrim.formula.impl.server.parsetree;

import com.bluebrim.formula.shared.*;


/*
 * An abstract class
 */
 
public abstract class CoFormulaNode implements CoTypedFormulaNodeIF {
	protected int m_evaluationType = NO_TYPE;
public CoFormulaNode() {
	super();
}
public abstract Object evaluate (CoVariableBinderIF variableBinder) throws CoFormulaEvaluationException ;
public int getEvaluationType () {
	return m_evaluationType;
}
public abstract String getIncorrectTypeErrorMessage ();
public boolean isEvaluationType (int type) {
	return m_evaluationType == type;
}
public abstract void putFormulaTextIn (CoFormulaText doc);
/*
 * Presents the formula with resolved variables in the doc
 */
public void putFormulaTextWithResolvedVariablesIn (CoVariableBinderIF variableBinder, CoFormulaText doc) throws CoFormulaEvaluationException {
	putFormulaTextIn(doc);
}
public void setEvaluationType (int type) {
	m_evaluationType = type;
}
public void throwErrorMessage () throws CoFormulaEvaluationException {
	throw new CoFormulaEvaluationException(getIncorrectTypeErrorMessage());
}
}
