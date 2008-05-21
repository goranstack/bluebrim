package com.bluebrim.formula.shared;

import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.formula.impl.server.parsetree.*;

/*
 * Evaluates formulas and returns the result values
 */
 
public class CoFormula {
	// inputs needed to parse
	protected String m_formula;
	protected CoNameBinderIF m_nameBinder;

	// inputs needed to evaluate
	protected CoVariableBinderIF m_variableBinder;

	// stores the result after scanning and parsing
	protected List m_scanTokens;
	protected CoFormulaNode m_parseTree;
	
	// stores the result of an evaluation
	protected Object m_result;

	// unit handling stuff
	protected CoConvertibleUnitSet m_units;
	
	protected CoFormulaScanner m_scanner = new CoFormulaScanner();
	protected CoFormulaParser m_parser = new CoFormulaParser();

public CoFormula() {
	super();
}
public CoFormula(CoVariableBinderIF variableBinder, String formula) {
	this();
	m_variableBinder = variableBinder;
	m_formula = formula;
}
// The main method for CoFormula
// Evaluates the m_formula and stores the rsult in m_result
// If variables are used in m_formula it gets the variables values from m_variableBinder.

private void evaluate () throws CoFormulaException {
	init();
	scan();
	parse();
	traverseAndCalc();
}
public void evaluate (String formula) throws CoFormulaException {
	// set indata
	m_formula = formula;

	// evaluate
	evaluate();
}
private void evaluate (CoVariableBinderIF variableBinder, String formula) throws CoFormulaException {
	// set indata
	m_variableBinder = variableBinder;
	m_formula = formula;

	// evaluate
	evaluate();
}
public Boolean evaluateBooleanFormula(String formula) throws CoFormulaException {
	evaluate(formula);
	return getBooleanResult();
}
public Boolean evaluateBooleanFormula(CoVariableBinderIF variableBinder, String formula) throws CoFormulaException {
	evaluate(variableBinder, formula);
	return getBooleanResult();
}
public Number evaluateNumberFormula(String formula) throws CoFormulaException
{
	evaluate(formula);
	return getNumberResult();
}
public Number evaluateNumberFormula(CoVariableBinderIF variableBinder, String formula) throws CoFormulaException {
	evaluate(variableBinder, formula);
	return getNumberResult();
}
private Boolean getBooleanResult () {
	if (m_result == null || ! (m_result instanceof Boolean)) return null;
	
	return (Boolean) m_result;
}
private Number getNumberResult () {
	if (m_result == null || ! (m_result instanceof Number)) return null;
	
	return (Number) m_result;
}
public Object getResult () {
	return m_result;
}
// reset result
private void init () {
	m_result = null;
}
private void parse () throws CoFormulaParsingException {
	// check that a formula exists
	if (m_scanTokens == null) {
		throw new CoFormulaParsingException(CoFormulaErrorResources.getName(CoFormulaErrorResources.MISSING_FORMULA));
	}

	// go ahead with parsing (build parse tree), all indata exists
	m_parseTree = m_parser.parse(m_scanTokens, m_nameBinder);
}
private void scan () throws CoFormulaScanningException {
	// check that a formula exists
	if (m_formula == null) {
		throw new CoFormulaScanningException(CoFormulaErrorResources.getName(CoFormulaErrorResources.MISSING_FORMULA));
	}

	// go ahead with scanning, all indata exists
	m_scanTokens = m_scanner.scan(m_formula);
}
public void setNameBinder (CoNameBinderIF nameBinder) {
	m_nameBinder = nameBinder;
}
public void setUnits( CoConvertibleUnitSet units )
{
	m_units = units;
	m_scanner.setUnits( m_units );
	m_parser.setUnits( m_units );
}
public void setVariableBinder (CoVariableBinderIF variableBinder) {
	m_variableBinder = variableBinder;
}
public String toString () {
	if (m_result == null)
		return "Variables : " + m_variableBinder + "\nFormel : " + m_formula + "\nResultat : null";
	else
		return "Variables : " + m_variableBinder + "\nFormel : " + m_formula + "\nResultat : " + m_result.toString();
}
// Traverses the parse tree and calculates the formula
private void traverseAndCalc () throws CoFormulaEvaluationException {
	if (m_parseTree == null) return;
	
	m_result = m_parseTree.evaluate(m_variableBinder);
}
}
