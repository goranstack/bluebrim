package com.bluebrim.formula.impl.server;
import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.formula.impl.server.parsetree.*;
import com.bluebrim.formula.server.*;
import com.bluebrim.formula.shared.*;
import com.bluebrim.xml.shared.*;

/**
 *	
 */

public class CoFormulaHolder extends CoObject implements CoFormulaHolderIF {

	protected CoFormulaNode m_parseTree;
	private static CoFormulaParser m_formulaParser = new CoFormulaParser();
	private static CoFormulaScanner m_formulaScanner = new CoFormulaScanner();

	public CoFormulaHolder() {
		super();
		//set default row
		try {
			setFormula(new String("0"), null);
		} catch (CoFormulaException e) {
		}
	}

	public String getFactoryKey() {
		return "CoFormulaParserCoVariableInterval";
	}

	public CoFormulaStyledDocumentIF getFormulaText() {
		CoFormulaStyledDocumentIF doc = new CoFormulaStyledDocument();
		if (m_parseTree == null)
			return doc;
		m_parseTree.putFormulaTextIn(doc);
		return doc;
	}

	public CoFormulaNode getParseTree() {
		return m_parseTree;
	}

	public final void setFormula(Object formula, CoAbstractVariablesHolderIF variableNames) throws CoFormulaException {
		if (formula == null)
			formula = "";
		try {
			CoNameBinderIF nameBinder = new CoNameBinder(variableNames);
			CoFormulaNode parseTree = m_formulaParser.parse(m_formulaScanner.scan(formula.toString()), nameBinder);
			m_parseTree = parseTree;
		} catch (CoFormulaException e) {
			m_parseTree = new CoErrorNode(formula.toString() + " " + e.getMessage());
			throw e;
		}
	}

	public void xmlAddSubModel(String parameter, Object subModel, CoXmlContext context) throws CoXmlReadException {
		if ("formula".equals(parameter) && (subModel instanceof String)) {
			try {
				setFormula((String) subModel, (CoAbstractVariablesHolderIF) context.getValue(CoAbstractVariablesHolderIF.class));
			} catch (CoFormulaException fee) {
				throw new CoXmlReadException(fee.getMessage());
			}

		}
	}

	public static CoXmlImportEnabledIF xmlCreateModel(
		Object superModel,
		Node node,
		CoXmlContext context) {
		return new CoFormulaHolder();
	}

	public void xmlImportFinished(Node node, CoXmlContext context) {

	}

	public void xmlVisit(CoXmlVisitorIF visitor) {
		visitor.exportString("formula", getFormulaText().toString());
	}
}