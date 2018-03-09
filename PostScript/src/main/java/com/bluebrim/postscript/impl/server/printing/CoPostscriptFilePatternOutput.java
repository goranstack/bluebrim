package com.bluebrim.postscript.impl.server.printing;
import java.io.*;

/**
 * Server-side com.bluebrim.postscript.shared.CoPostscriptTargetOutput for generating output to a file, with a specified file pattern.
 * In the future, it will be possible to include variables and let them expand to e.g. todays date, the page
 * number, used colorants, etc. This is however not possible at the moment.
 *
 * <p><b>Creation date:</b> 2001-08-22
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public class CoPostscriptFilePatternOutput implements com.bluebrim.postscript.shared.CoPostscriptTargetOutput {
	private OutputStream m_out;
	private String m_pattern;

public CoPostscriptFilePatternOutput(String filenamePattern) {
	m_pattern = filenamePattern;
}


public OutputStream getOutputStream() {
	return m_out;
}


protected String processPattern() {
	// FIXME: do something better here...
	return m_pattern;
}


public boolean selectOutput() {
	String fileName = processPattern();
	
	if (fileName == null) {
		return false;
	}

	try {
		m_out = new FileOutputStream(fileName);
	} catch (IOException e) {
		return false;
	}

	return true;
}
}