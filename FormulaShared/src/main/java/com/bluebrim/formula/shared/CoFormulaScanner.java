package com.bluebrim.formula.shared;

import java.io.*;
import java.util.*;

import com.bluebrim.base.shared.*;


/*
 * Scans through a String and separates the string content into tokens
 * Returns an array of CoToken instances.
 */
 
public class CoFormulaScanner {
	public static final int SCAN_PARANTHESIS = 0;
	public static final int SCAN_NUMBER = 1;
	public static final int SCAN_OPERATOR = 2;
	public static final int SCAN_IDENTIFIER = 3;
	public static final int SCAN_CONDITION = 4;
	public static final int SCAN_BOOLEAN = 5;
	public static final int SCAN_STRING = 6;
	public static final int SCAN_BRACKET = 7;
	public static final int SCAN_UNIT = 8;

	public static final String RESERVED_WORD_IF = CoFormulaResources.getUIString(CoFormulaResources.RESERVED_WORD_IF).toLowerCase();
	public static final String RESERVED_WORD_TRUE = CoFormulaResources.getUIString(CoFormulaResources.RESERVED_WORD_TRUE).toLowerCase();
	public static final String RESERVED_WORD_FALSE = CoFormulaResources.getUIString(CoFormulaResources.RESERVED_WORD_FALSE).toLowerCase();
	public static final String RESERVED_WORD_AND = CoFormulaResources.getUIString(CoFormulaResources.RESERVED_WORD_AND).toLowerCase();
	public static final String RESERVED_WORD_OR = CoFormulaResources.getUIString(CoFormulaResources.RESERVED_WORD_OR).toLowerCase();

	protected CoConvertibleUnitSet m_units;
public List scan (String formula) throws CoFormulaScanningException {
	List tokens = new ArrayList();

	try {
		// scan through formula and create tokens
		CoStreamTokenizer st = new CoStreamTokenizer(new StringReader(formula));
		
		// init interna table (how to parse the string)
		st.resetSyntax();
		st.wordChars('A', 'Z');
		st.wordChars('a', 'z');
		st.wordChars('_',  '_');
		st.wordChars('Ä', 'Ö');
		st.wordChars('ä', 'ö');
		st.wordChars('0', '9');
		st.parseNumbers();

		CoToken token;
		int type;
		boolean longOperator = false;
		boolean aString = false;
		
		while ((type = st.nextToken()) != CoStreamTokenizer.TT_EOF) {
			if (type == '"') {
				longOperator = false;
				aString = !aString;
				if (aString)
					// a new string to start building
					token = new CoToken("", SCAN_STRING);
				else
					// string is complete
					continue;
					
			} else if (aString) {
				token = (CoToken)tokens.get(tokens.size()-1);
				if (type == CoStreamTokenizer.TT_WORD) {
					token.m_tokenName += st.sval;
				} else if (type == CoStreamTokenizer.TT_NUMBER) {
					token.m_tokenName += Double.toString(st.nval);
				} else {
					token.m_tokenName += String.valueOf((char)type);
				}
				longOperator = false;
				continue;
				
			} else if (type == ' ' || type == CoStreamTokenizer.TT_EOL || type == '	') {
				continue;
				
			} else if (type == CoStreamTokenizer.TT_WORD) {
				if (RESERVED_WORD_IF.equals(st.sval.toLowerCase())) {
					token = new CoToken(RESERVED_WORD_IF, SCAN_CONDITION);
						
				} else if (RESERVED_WORD_TRUE.equals(st.sval.toLowerCase())) {
					token = new CoToken(RESERVED_WORD_TRUE, SCAN_BOOLEAN);
						
				} else if (RESERVED_WORD_FALSE.equals(st.sval.toLowerCase())) {
					token = new CoToken(RESERVED_WORD_FALSE, SCAN_BOOLEAN);
					
				} else if (RESERVED_WORD_AND.equals(st.sval.toLowerCase())) {
					token = new CoToken(RESERVED_WORD_AND, SCAN_OPERATOR);
					
				} else if (RESERVED_WORD_OR.equals(st.sval.toLowerCase())) {
					token = new CoToken(RESERVED_WORD_OR, SCAN_OPERATOR);
					
				} else if ( ( m_units != null ) && m_units.contains( st.sval.toLowerCase())) {
					token = new CoToken(st.sval.toLowerCase(), SCAN_UNIT);
					
				} else {
					// must be a variable
					if (tokens.size() > 0 && ((CoToken)tokens.get(tokens.size()-1)).isIdentifier()) {
						token = (CoToken)tokens.get(tokens.size()-1);
						token.m_tokenName += " " + st.sval;
						continue;
					}
					token = new CoToken(st.sval, SCAN_IDENTIFIER);
				}
				longOperator = false;
				
			} else if (type == CoStreamTokenizer.TT_NUMBER) {
				token = new CoToken(Double.toString(st.nval), SCAN_NUMBER);
				longOperator = false;
				
			} else if (type == '+' || type == '-' || type == '/' || type == '*' || type == '|' || type == '&') {
				token = new CoToken(String.valueOf((char)type), SCAN_OPERATOR);
				longOperator = false;
					
			} else if (type == '=' || type == '<' || type == '>' || type == '!') {
				if (longOperator) {
					// <=, >=, !=
					token = (CoToken)tokens.get(tokens.size()-1);
					token.m_tokenName += String.valueOf((char)type);
					longOperator = false;
					continue;
				} else {
					token = new CoToken(String.valueOf((char)type), SCAN_OPERATOR);
					longOperator = true;
				}
				
			} else if (type == ';') {
				token = new CoToken(String.valueOf((char)type), SCAN_CONDITION);
				longOperator = false;
				
			} else if (type == '(' || type == ')') {
				token = new CoToken(String.valueOf((char)type), SCAN_PARANTHESIS);
				longOperator = false;
				
			} else if (type == '%' || type == '%') {
				token = new CoToken(String.valueOf((char)type), SCAN_BRACKET);
				longOperator = false;
				
			} else {
				// an error occured
				throw new CoFormulaScanningException(CoFormulaErrorResources.getName(CoFormulaErrorResources.THE_UNEXPECTED_TOKEN) + " " + String.valueOf((char)type));
			}
			
			tokens.add(token);
		}
	
		return tokens;
	
	} catch (IOException ioe)  {
		// indicate that the tokens could not be read
		throw new CoFormulaScanningException(CoFormulaErrorResources.getName(CoFormulaErrorResources.CAN_NOT_READ_FORMULA));
	}
}
public void setUnits( CoConvertibleUnitSet units )
{
	m_units = units;
}
}
