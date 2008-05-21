package com.bluebrim.formula.shared;


/*
 * An token class
 */
 
public class CoToken {
	protected String m_tokenName;
	protected int m_tokenType;

	public static final CoToken END_TOKEN = new CoToken("", -1);
public CoToken(String token, int type) {
	super();
	
	m_tokenName = token;
	m_tokenType = type;
}
public boolean equals (String s) {
	return m_tokenName.equals(s);
}
public boolean isCondition () {
	return m_tokenType == CoFormulaScanner.SCAN_CONDITION;
}
public boolean isIdentifier () {
	return m_tokenType == CoFormulaScanner.SCAN_IDENTIFIER;
}
public boolean isNumber () {
	return m_tokenType == CoFormulaScanner.SCAN_NUMBER;
}
public boolean isOperator () {
	return m_tokenType == CoFormulaScanner.SCAN_OPERATOR;
}
public boolean isParanthesis () {
	return m_tokenType == CoFormulaScanner.SCAN_PARANTHESIS;
}
public boolean isString () {
	return m_tokenType == CoFormulaScanner.SCAN_STRING;
}
public String toString () {
/*	if (m_tokenType == CoFormulaScanner.SCAN_NUMBER)
		return m_tokenName + ", CoFormulaScanner.SCAN_NUMBER";
		
	else if (m_tokenType == CoFormulaScanner.SCAN_IDENTIFIER)
		return m_tokenName + ", CoFormulaScanner.SCAN_IDENTIFIER";
		
	else if (m_tokenType == CoFormulaScanner.SCAN_OPERATOR)
		return m_tokenName + ", CoFormulaScanner.SCAN_OPERATOR";
		
	else if (m_tokenType == CoFormulaScanner.SCAN_PARANTHESIS)
		return m_tokenName + ", CoFormulaScanner.SCAN_PARANTHESIS";
	*/	
	return m_tokenName;// + ", " + m_tokenType;
}
}
