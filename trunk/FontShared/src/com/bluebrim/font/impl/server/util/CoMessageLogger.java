package com.bluebrim.font.impl.server.util;

import java.io.*;

/**
 * Simple message logging class, primary used by the font file parsers for reporting warnings and
 * non-fatal errors in the font files when read.
 * Creation date: (2001-04-06 10:36:52)
 * @author Magnus Ihse <magnus.ihse@appeal.se> (2001-05-03 15:13:36)
 */
public class CoMessageLogger  {
	private PrintWriter m_messageWriter;
	private CharArrayWriter m_messageStorage;



public boolean messagesAvailable() {
	return (m_messageStorage.toString().length() > 0);
}

public CoMessageLogger() {
	super();
	m_messageStorage = new CharArrayWriter();
	m_messageWriter = new PrintWriter(m_messageStorage, true); // true == autoflush
}



public String getMessages() {
	return m_messageStorage.toString();
}



public PrintWriter getWriter() {
	return m_messageWriter;
}


public String toString() {
	return getMessages();
}
}