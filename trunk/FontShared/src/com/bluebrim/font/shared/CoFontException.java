package com.bluebrim.font.shared;

/**
 * Generic font exception.
 * Creation date: (2001-04-03 13:13:49)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public class CoFontException extends Exception {
	private Exception encapsulated;


public CoFontException() {
	super();
}

public CoFontException(String s) {
	super(s);
}

public CoFontException(String s, Exception e) {
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

public CoFontException(Exception e) {
	super();
	encapsulated = e;
}
}