package com.bluebrim.formula.shared;

import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.formula.impl.server.parsetree.*;

/*
 * Parses formulas and returns the result as a parse tree (CoFormulaNode)
 */
 
public class CoFormulaParser
{
	// PENDING: Due to a bug in JDK'n we can't use brackets yet. 
	// When replacing this you must replace literals in CoFormulaScanner.scan as well.
	 
	public static final String RIGHT_BRACKET = "%";
	public static final String LEFT_BRACKET = "%";
	public static final String	LEFT_PARANTHESIS = "(";
	public static final String	RIGHT_PARANTHESIS = ")";
	
	protected List 				m_tokens;
	protected int 				m_tokenIndex;
	protected CoToken 			m_currentToken;
	protected CoNameBinderIF	m_nameBinder;

	protected CoConvertibleUnitSet m_units;
public CoFormulaParser () {
	super();
}


private static void throwErrorMessage (String errorMessage) throws CoFormulaParsingException {
	// get the translated message
	String str;
	try {
	    str = CoFormulaErrorResources.getName(errorMessage);
	} catch (MissingResourceException mre) {
	    str = errorMessage;
	}

	throw new CoFormulaParsingException(str);
}

private static void throwErrorMessage (String errorMessage, String extraMessage) throws CoFormulaParsingException{
	// get the translated message
	String str;
	try {
	    str = CoFormulaErrorResources.getName(errorMessage);
	} catch (MissingResourceException mre) {
	    str = errorMessage;
	}

	throw new CoFormulaParsingException(str + " " + extraMessage);
}

// reads the tokens in reverse order
// when no more tokens to read it sends CoToken.END_TOKEN
private void getNextToken () {
	m_tokenIndex--;
	
	if (m_tokenIndex >= 0) {
		m_currentToken = (CoToken)m_tokens.get(m_tokenIndex);
		
	}	else {
		m_currentToken = CoToken.END_TOKEN;
	}
}
// reset
private void init (List tokens, CoNameBinderIF nameBinder) {
	m_nameBinder = nameBinder;
	
	m_tokens = tokens;
	m_tokenIndex = m_tokens.size();

	// set m_currentToken to the first token
	getNextToken();
}
// is called when m_currentToken is a ")"
// (can be a paranthesis or condition expression)
private boolean isConditionExpression () {
	int i = m_tokenIndex;

	while (i > 0) {
		i--;
		CoToken t = (CoToken)m_tokens.get(i);
		if (t.equals("(")) return false;
		if (t.equals(";")) return true;
	}
	
	return false;
}
/* All parse methods are using m_currentToken that contains the token
 * they should start to operate on.
 * When a parse method loses control m_currentToken contains the next token
 * after last treated token.
 * The parse tree is build using reversed Polish notation.
 */

public CoFormulaNode parse (List tokens, CoNameBinderIF nameBinder) throws CoFormulaParsingException {
	if (tokens == null || tokens.size() == 0)
		return new CoEmptyNode();
		
//	m_parser.
		init (tokens, nameBinder);
	
	// build and return parse tree
	return //m_parser.
		parseExpression();
}
private CoFormulaNode parseBracketExpression () throws CoFormulaParsingException {
	getNextToken();

	// parse the expression inside the paranthesises
	CoFormulaNode node = parseExpression();

	if (m_currentToken.equals(LEFT_BRACKET)) {
		getNextToken();		
	} else {
		throwErrorMessage(CoFormulaErrorResources.MISSING_LEFT_BRACKET);
	}
	
	return new CoBracketNode(node);
}
private CoFormulaNode parseConditionExpression () throws CoFormulaParsingException {
	// starts with m_currentToken == ")", read next token
	getNextToken();

	// parse the else expression inside the paranthesises
	CoFormulaNode elseNode = parseExpression();

	if (m_currentToken.equals(";")) {
		getNextToken();
		CoFormulaNode thenNode = parseExpression();
			
		if (m_currentToken.equals(";")) {
			getNextToken();
			CoFormulaNode ifNode = parseExpression();

			if (m_currentToken.equals("(")) {
				getNextToken();
				
				if (m_currentToken.equals(CoFormulaScanner.RESERVED_WORD_IF)) {
					getNextToken();
					return new CoConditionNode(ifNode, thenNode, elseNode);
				
				} else {
					throwErrorMessage(CoFormulaErrorResources.THE_CONDITION_EXPRESSION_MISSING, CoFormulaScanner.RESERVED_WORD_IF);
				}
				
			} else {
				throwErrorMessage(CoFormulaErrorResources.MISSING_LEFTPARANTHESIS);
			}
				
		} else {
			throwErrorMessage(CoFormulaErrorResources.THE_CONDITION_EXPRESSION_MISSING, ";");
		}
		
	} else {
		throwErrorMessage(CoFormulaErrorResources.THE_CONDITION_EXPRESSION_MISSING, ";");
	}
	
	return null;
}
private CoFormulaNode parseExpression () throws CoFormulaParsingException {
	CoFormulaNode right = parseSimpleExpression();

	// an expression to calculate (no comparing)
	if (m_currentToken == CoToken.END_TOKEN || m_currentToken.equals("(") || m_currentToken.equals(";") || m_currentToken.equals(LEFT_BRACKET)) {
		return right;

	// a compare expression
	} else if (m_currentToken.equals("=")) {
		getNextToken();
		return new CoEqualNode(parseSimpleExpression(), right);
		
	} else if (m_currentToken.equals("<")) {
		getNextToken();
		return new CoLessNode(parseSimpleExpression(), right);
		
	} else if (m_currentToken.equals(">")) {
		getNextToken();
		return new CoGreaterNode(parseSimpleExpression(), right);
		
	} else if (m_currentToken.equals("<=")) {
		getNextToken();
		return new CoLessEqualNode(parseSimpleExpression(), right);
		
	} else if (m_currentToken.equals(">=")) {
		getNextToken();
		return new CoGreaterEqualNode(parseSimpleExpression(), right);
		
	} else if (m_currentToken.equals("!=")) {
		getNextToken();
		return new CoNotEqualNode(parseSimpleExpression(), right);
	}

	throwErrorMessage(CoFormulaErrorResources.FORMULA_INCORRECT_FORMULATED);
	return null;
}
// A factor is: a number, an identifier, a NOT-token or a paranthesized subexpression
private CoFormulaNode parseFactor () throws CoFormulaParsingException {
	CoFormulaNode node = null;

	// unit
	if
		( ( m_units != null ) && ( m_currentToken.m_tokenName.length() > 0 ) && m_units.contains( m_currentToken.m_tokenName ) )
	{
		CoConvertibleUnit u = (CoConvertibleUnit) m_units.getUnit( m_currentToken.m_tokenName );
		getNextToken();
		node = parseTerm();
		if
			( node instanceof CoUnitNode )
		{
			( (CoUnitNode) node ).setUnit( u );
		} else if
			( node instanceof CoNumberNode )
		{
			node = new CoUnitNode( node, u );
		} else {
			throwErrorMessage(CoFormulaErrorResources.UNEXPECTED_END);		
		}
	
	// a number
	} else if (m_currentToken.isNumber()) {
		node = new CoNumberNode(new Double(m_currentToken.m_tokenName));
		if
			( m_units != null )
		{
			node = new CoUnitNode( node, m_units.getCurrentUnit() );
		}
		getNextToken();

	// a string
	} else if (m_currentToken.isString()) {
		node = new CoStringNode(new String(m_currentToken.m_tokenName));
		getNextToken();

	// an identifier
	} else if (m_currentToken.isIdentifier()) {
		node = parseIdentifier();

	// a condition expression
	} else if (m_currentToken.equals(")") && isConditionExpression()) {
		node = parseConditionExpression();

	// a boolean expression
	} else if (m_currentToken.equals(CoFormulaScanner.RESERVED_WORD_TRUE)) {
		getNextToken();
		node = new CoBooleanNode(Boolean.TRUE);

	// a boolean expression
	} else if (m_currentToken.equals(CoFormulaScanner.RESERVED_WORD_FALSE)) {
		getNextToken();
		node = new CoBooleanNode(Boolean.FALSE);

	// a paranthesis expression
	} else if (m_currentToken.equals(")")) {
		node = parseParenthesisExpression();
		
	// a bracket expression
	} else if (m_currentToken.equals(RIGHT_BRACKET)) {
		node = parseBracketExpression();
		
	// unexpected end
	} else {
		throwErrorMessage(CoFormulaErrorResources.UNEXPECTED_END);
		return null;
	}
	
	// a not expression
	if (m_currentToken.equals("!")) {
		getNextToken();
		node = new CoNotNode(node);
		
	// a leading plus (do nothing)
	} else if (m_currentToken.equals("+")) {
		getNextToken();
		if
			(
				m_currentToken != CoToken.END_TOKEN
			&&
				! m_currentToken.isOperator()
			&& 
				! m_currentToken.equals("(")
			&&
				! m_currentToken.equals(";")
			)
		{
			// was not a leading +
			undoNextToken();
		}

	// a leading minus (negation)
	} else if (m_currentToken.equals("-")) {
		getNextToken();
		if (m_currentToken != CoToken.END_TOKEN && !m_currentToken.isOperator() && 
			!m_currentToken.equals("(") && !m_currentToken.equals(";"))
			// was not a leading 
			undoNextToken();
		else if ( ( m_units != null ) && ( m_currentToken.m_tokenName.length() > 0 ) && ( m_units.contains(m_currentToken.m_tokenName ) ) )
			// was not a leading  
			undoNextToken();
		else
			// is a leading -
			if
				( node instanceof CoUnitNode )
			{
				CoUnitNode tmp = (CoUnitNode) node;
				node = new CoUnitNode( new CoNegationNode( tmp.getNode() ), tmp.getUnit() );
			} else {
				node = new CoNegationNode(node);
			}	
	}
	
	return node;
}
// An identifier can only be a variable
private CoFormulaNode parseIdentifier () throws CoFormulaParsingException {
	if (m_currentToken == CoToken.END_TOKEN) {
		throwErrorMessage(CoFormulaErrorResources.UNEXPECTED_END);
		return null;
	}

	if (m_nameBinder == null) {
		throwErrorMessage(CoFormulaErrorResources.THE_MISSING_VARIABLE, m_currentToken.m_tokenName);
		return null;
	}
	
	CoAbstractVariableIF variableReference = m_nameBinder.bindName(m_currentToken.m_tokenName);
	if (variableReference == null) {
		throwErrorMessage(CoFormulaErrorResources.THE_MISSING_VARIABLE, m_currentToken.m_tokenName);
		return null;
	}
	
	CoFormulaNode node = new CoVariableNode(variableReference);
	getNextToken();
	return node;
}
private CoFormulaNode parseParenthesisExpression () throws CoFormulaParsingException {
	// starts with m_currentToken == ")", read next token
	getNextToken();

	// parse the expression inside the paranthesises
	CoFormulaNode node = parseExpression();

	if (m_currentToken.equals(LEFT_PARANTHESIS)) {
		getNextToken();		
	} else {
		throwErrorMessage(CoFormulaErrorResources.MISSING_LEFTPARANTHESIS);
	}
	
	return new CoParenthesisNode(node);
}
// Parses expressions with terms separated by +, - or | (logical OR) operator
private CoFormulaNode parseSimpleExpression () throws CoFormulaParsingException {

	CoFormulaNode right = null;

	/*
	if
		( ( m_units != null ) && ( m_currentToken.m_tokenName.length() > 0 ) && m_units.contains( m_currentToken.m_tokenName ) )
	{
		CoConvertibleUnit u = (CoConvertibleUnit) m_units.getUnit( m_currentToken.m_tokenName );
		getNextToken();
		CoFormulaNode node = parseTerm();
		if
			( node instanceof CoUnitNode )
		{
			right = node;
			( (CoUnitNode) right ).setUnit( u );
		} else {
			right = new CoUnitNode( node, u );
		}
	} else {
		right = parseTerm();
	}
	*/
	
	right = parseTerm();

	

	if (m_currentToken.equals("+")) {
		getNextToken();
		return new CoPlusNode(parseSimpleExpression(), right);

	} else if (m_currentToken.equals("-")) {
		getNextToken();
		return new CoMinusNode(parseSimpleExpression(), right);

	} else if (m_currentToken.equals("|") || m_currentToken.equals(CoFormulaScanner.RESERVED_WORD_OR)) {
		getNextToken();
		return new CoOrNode(parseSimpleExpression(), right);
	}
	
	return right;
}
// Parses expressions of factors separated by *, / or & (logical AND) operator
private CoFormulaNode parseTerm () throws CoFormulaParsingException {
	CoFormulaNode right = parseFactor();

	if (m_currentToken == CoToken.END_TOKEN) {
		return right;

	} else if (m_currentToken.equals("*")) {
		getNextToken();
		return new CoMultiplyNode(parseTerm(), right);

	} else if (m_currentToken.equals("/")) {
		getNextToken();
		return new CoDivideNode(parseTerm(), right);

	}else if (m_currentToken.equals("&") || m_currentToken.equals(CoFormulaScanner.RESERVED_WORD_AND)) {
		getNextToken();
		return new CoAndNode(parseTerm(), right);
	}
	
	return right;
}
public void setUnits( CoConvertibleUnitSet units )
{
	m_units = units;
}
private void undoNextToken () {
	if (m_tokenIndex < m_tokens.size() - 2)
		m_tokenIndex++;
		
	if (m_tokenIndex >= 0) {
		m_currentToken = (CoToken)m_tokens.get(m_tokenIndex);
		
	}	else {
		m_currentToken = CoToken.END_TOKEN;
	}
}
}
