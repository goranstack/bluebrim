package com.bluebrim.xml.shared;
/**
 * Exception signalling error during read/import of XML data.
 *
 * Creation date: (2001-04-03 13:13:49)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */

public class CoXmlReadException extends Exception {
	private Exception encapsulated;

	public CoXmlReadException() {
		super();
	}

	public CoXmlReadException(String s) {
		super(s);
	}

	public CoXmlReadException(String s, Exception e) {
		super(s);
		encapsulated = e;
	}

	public String getMessage() {
		if (encapsulated == null) {
			return (super.getMessage() == null) ? "" : super.getMessage();
		} else {
			return ((super.getMessage() == null) ? "" : super.getMessage()) + "\n" + encapsulated.getMessage();
		}
	}

	public CoXmlReadException(Exception e) {
		super();
		encapsulated = e;
	}
}