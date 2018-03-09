package com.bluebrim.formula.impl.server.parsetree;

import com.bluebrim.formula.shared.*;

/*
 * Node for a condition
 */
 
public class CoConditionNode extends CoFormulaNode {
	protected CoFormulaNode m_ifNode;
	protected CoFormulaNode m_thenNode;
	protected CoFormulaNode m_elseNode;
public CoConditionNode(CoFormulaNode ifNode, CoFormulaNode thenNode, CoFormulaNode elseNode) {
	super();
	
	m_ifNode = ifNode;
	m_thenNode = thenNode;
	m_elseNode = elseNode;
}
public Object evaluate(CoVariableBinderIF variableBinder) throws CoFormulaEvaluationException {
	Object result = m_ifNode.evaluate(variableBinder);	
	if (result == null) return null;
	if (m_ifNode.getEvaluationType() != BOOLEAN_TYPE) throwErrorMessage();

	CoFormulaNode node = null;
	if (((Boolean) result).booleanValue())
		node = m_thenNode;
	else
		node = m_elseNode;
	
	result = node.evaluate(variableBinder);
	if (result == null) return null;
	
	switch (node.getEvaluationType()) {
		case BOOLEAN_TYPE:
			setEvaluationType(BOOLEAN_TYPE);
			return result;
			
		case NUMBER_TYPE:
			setEvaluationType(NUMBER_TYPE);
			return result;
			
		case STRING_TYPE:
			setEvaluationType(STRING_TYPE);
			return result;
			
		default:
			setEvaluationType(NO_TYPE);
			break;
	}
	
	throwErrorMessage();
	return null;
}
public String getIncorrectTypeErrorMessage() {
	return CoFormulaErrorResources.INCORRECT_CONDITION_EXPRESSION;
}
public void putFormulaTextIn (CoFormulaText doc) {
	doc.addString( CoFormulaScanner.RESERVED_WORD_IF + " ( ");
	m_ifNode.putFormulaTextIn(doc);
	doc.addString(" ; ");
	m_thenNode.putFormulaTextIn(doc);
	doc.addString(" ; ");
	m_elseNode.putFormulaTextIn(doc);
	doc.addString(" ) ");
}
public String toString () {
	return CoFormulaScanner.RESERVED_WORD_IF + " ( " +  m_ifNode.toString() + " ; " +
				m_thenNode.toString() + " ; " + m_elseNode.toString() + " ) ";
}
}
