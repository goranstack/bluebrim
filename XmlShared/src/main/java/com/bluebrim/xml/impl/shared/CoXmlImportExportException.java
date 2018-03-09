package com.bluebrim.xml.impl.shared;

public class CoXmlImportExportException extends Exception {


public String getMessage() {
	return super.getMessage() + " Before import: " + m_before + ". After export: " + m_after;
}


/**
 * CoConversionException constructor comment.
 */
public CoXmlImportExportException() {
	super();
}





public CoXmlImportExportException(String msg) {
	super(msg);
}


public CoXmlImportExportException(String msg, String before, String after) {
	super(msg);
	m_before = before;
	m_after	 = after;
}

	private String m_after;
	private String m_before;

public CoXmlImportExportException(Throwable t) {
	super(t.toString());
}
}