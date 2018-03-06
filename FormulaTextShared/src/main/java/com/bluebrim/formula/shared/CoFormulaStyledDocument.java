package com.bluebrim.formula.shared;

import javax.swing.text.*;

import com.bluebrim.text.shared.*;

/*
 * A document that treats variables in the text as an atom.
 *
 * see CoFormulaTextField
 */
 
public class CoFormulaStyledDocument extends CoStyledDocument implements CoFormulaStyledDocumentIF {
	// Attribute name
	public static final String FORMULA_VARIABLE = "formula_variable";
	private MutableAttributeSet attr = new CoSimpleAttributeSet();

public CoFormulaStyledDocument() {
	super();
}
// insert text without marking it as atomic
public void addString(String str) {
	try {
		insertString(getLength(), str, attr);
		
	} catch (BadLocationException e) {
	}
}
public int getLength() {
	return super.getLength();
}
// insert text marking it as atomic
public void insertFormulaVariable(int offset, int length, String name) {
	//remove selected text
	if (length > 0) {
		try { remove(offset, length); }
		catch (Throwable t) { return; }
	}
	// insert variable name and mark it as a atomic
	try {
		MutableAttributeSet as = new CoSimpleAttributeSet();
		as.addAttribute(FORMULA_VARIABLE, name);
		insertString(offset, name, as);
	} catch (Throwable t) {
	}
}
public boolean isAtomic(Element elem) {
	return (elem.getAttributes().isDefined(FORMULA_VARIABLE) );
}
public String toString () {
	try {
		return getText(0, getLength());
		
	} catch (BadLocationException ble) {
		System.out.println("err i formula styled document::toString");
		return "";
	}
}
}