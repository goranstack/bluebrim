package com.bluebrim.postscript.impl.shared;
/**
 * Generic postscript exception.
 *
 * <p><b>Creation date:</b> 2001-08-13
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public class CoPostscriptException extends Exception {
	private Exception encapsulated;
public CoPostscriptException() {
	super();
}

public CoPostscriptException(String s) {
	super(s);
}

public CoPostscriptException(String s, Exception e) {
	super(s);
	encapsulated = e;
}

public String getMessage() {
	if (encapsulated == null) {
		return super.getMessage();
	} else {
		return super.getMessage() + "\nEncapsulated exception: " + encapsulated;
	}
}

public CoPostscriptException(Exception e) {
	super();
	encapsulated = e;
}
}