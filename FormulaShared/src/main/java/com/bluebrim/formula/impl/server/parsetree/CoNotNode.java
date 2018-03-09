package com.bluebrim.formula.impl.server.parsetree;

import com.bluebrim.formula.shared.*;

/*
 * A NOT ( ! ) node
 */
 
public class CoNotNode extends CoUnaryNode {
public CoNotNode(CoFormulaNode node) {
	super(node);
}
public Object evaluate (CoVariableBinderIF variableBinder) throws CoFormulaEvaluationException {
	Object result = m_node.evaluate(variableBinder);
	if (result == null) return null;
	
	switch (m_node.getEvaluationType()) {
		case BOOLEAN_TYPE:
			setEvaluationType(BOOLEAN_TYPE);
			return new Boolean( ! ((Boolean) result).booleanValue());
			
		case NUMBER_TYPE:
			setEvaluationType(NUMBER_TYPE);
			break;

		case STRING_TYPE:
			setEvaluationType(STRING_TYPE);
			break;
			
		default:
			setEvaluationType(NO_TYPE);
			break;
	}
	
	throwErrorMessage();
	return null;
}
public String getIncorrectTypeErrorMessage () {
	return CoFormulaErrorResources.INCORRECT_USE_OF_OPERATOR_NOT;
}
public void putFormulaTextIn (CoFormulaText doc) {
	doc.addString(" ! ");
	m_node.putFormulaTextIn(doc);
}
public String toString () {
	return " ! " + m_node.toString();
}
}
