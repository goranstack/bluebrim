package com.bluebrim.formula.impl.server.parsetree;

import com.bluebrim.formula.shared.*;

/*
 * An abstract class
 */
 
public abstract class CoLeafNode extends CoFormulaNode {

public void putFormulaTextIn (CoFormulaText doc) {
	doc.addString(toString());
}
}
