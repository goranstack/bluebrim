package com.bluebrim.formula.shared;
import java.rmi.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.xml.shared.*;

public interface CoFormulaHolderIF extends CoObjectIF, Remote, CoXmlEnabledIF {
	public static final String XML_TAG = "formula-holder";

public CoFormulaStyledDocumentIF getFormulaText ();


public void setFormula (Object formula, CoAbstractVariablesHolderIF variableNames) throws CoFormulaException;
}