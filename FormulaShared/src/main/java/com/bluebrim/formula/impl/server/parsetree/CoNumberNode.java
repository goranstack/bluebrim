package com.bluebrim.formula.impl.server.parsetree;

import java.text.*;

import com.bluebrim.formula.shared.*;

/*
 * A NUMBER ( 0, 1, 2 .. ) node
 */
 
public class CoNumberNode extends CoValueNode {
public CoNumberNode(double n) {
	this(new Double(n));
}
public CoNumberNode(Double n) {
	super(n);
	
	setEvaluationType(NUMBER_TYPE);
}
public Object evaluate (CoVariableBinderIF variableBinder)  throws CoFormulaEvaluationException {
	return new Double(((Double)m_value).doubleValue());
}
/*
 * PENDING: This method is not suited for general use. Works only together with putFormulaTextWithResolvedVariables
 * to achive some kind of formatting of money that can be used on numbers as well.
 */
protected String format() {
	return toString();
}
public String getIncorrectTypeErrorMessage () {
	return CoFormulaErrorResources.INCORRECT_USE_OF_NUMBER;
}
/*
 * PENDING: Temporary solution until we can handle money in formulas. For now we can't separate money from numbers.
 * Therefor the best we can do is to format all numbers with maximum fraction digits = 2. 
 */
public void putFormulaTextWithResolvedVariablesIn (CoVariableBinderIF variableBinder, CoFormulaText doc) throws CoFormulaEvaluationException {
	NumberFormat tFormat = NumberFormat.getNumberInstance();
	tFormat.setMaximumFractionDigits(2);
	doc.addString(tFormat.format((Double)m_value));
}
}
