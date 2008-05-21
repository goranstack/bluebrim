package com.bluebrim.font.impl.server.util;
import java.io.*;

/**
 * A simple extension to InputStream, which knows its current position in the file. Used for debugging
 * purposes only, not in production code.
 * Creation date: (2001-05-22 21:59:02)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public class CoPositionalInputStream extends InputStream {
	InputStream m_stream;
	int m_pos;

/**
 * CoPositionalInputStream constructor comment.
 */
public CoPositionalInputStream(InputStream stream) {
	super();
	m_stream = stream;
	m_pos = 0;			// Just for clarity...
}


public int getPosition() {
	return m_pos;
}


	/**
	 * Reads the next byte of data from the input stream. The value byte is
	 * returned as an <code>int</code> in the range <code>0</code> to
	 * <code>255</code>. If no byte is available because the end of the stream
	 * has been reached, the value <code>-1</code> is returned. This method
	 * blocks until input data is available, the end of the stream is detected,
	 * or an exception is thrown.
	 *
	 * <p> A subclass must provide an implementation of this method.
	 *
	 * @return     the next byte of data, or <code>-1</code> if the end of the
	 *             stream is reached.
	 * @exception  IOException  if an I/O error occurs.
	 */
public int read() throws IOException {
	m_pos++;
	return m_stream.read();
}


public String toString() {
	return "Position " + m_pos + " in " + m_stream;
}
}