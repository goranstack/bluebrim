package com.bluebrim.xml.shared;
/**
 * Exception signalling error during write/generation of XML data.
 *
 * Creation date: (2001-04-03 13:13:49)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
 
public class CoXmlWriteException extends Exception {
	private Exception encapsulated;
	
public CoXmlWriteException() {
	super();
}

public CoXmlWriteException(String s) {
	super(s);
}

public CoXmlWriteException(String s, Exception e) {
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

public CoXmlWriteException(Exception e) {
	super();
	encapsulated = e;
}
}