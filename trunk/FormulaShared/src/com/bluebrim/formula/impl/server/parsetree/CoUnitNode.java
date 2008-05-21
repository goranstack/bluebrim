package com.bluebrim.formula.impl.server.parsetree;

import com.bluebrim.base.shared.*;
import com.bluebrim.formula.shared.*;

//

public class CoUnitNode extends CoUnaryNode
{
	private CoConvertibleUnit m_unit;
public CoUnitNode( CoFormulaNode node, CoConvertibleUnit unit )
{
	super( node );

	m_unit = unit;
}
public Object evaluate( CoVariableBinderIF variableBinder ) throws CoFormulaEvaluationException
{
	Object operand = m_node.evaluate( variableBinder );
	if ( operand == null ) return null;

	switch (m_node.getEvaluationType()) {
		case BOOLEAN_TYPE:
			setEvaluationType(BOOLEAN_TYPE);
			break;
			
		case NUMBER_TYPE:
			setEvaluationType(NUMBER_TYPE);
			return new Double( m_unit.from( ( (Number) operand ).doubleValue() ) );

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
public String getIncorrectTypeErrorMessage()
{
	return CoFormulaErrorResources.INCORRECT_USE_OF_UNIT;
}
public CoConvertibleUnit getUnit()
{
	return m_unit;
}
public void putFormulaTextIn (CoFormulaText doc)
{
	m_node.putFormulaTextIn( doc );
	
	doc.addString( m_unit.getName() );
}
public void setUnit( CoConvertibleUnit unit )
{
	m_unit = unit;
}
public String toString () {
	return m_node.toString() + m_unit.getName();
}
}
