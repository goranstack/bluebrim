package com.bluebrim.postscript.shared;
import java.io.*;

/**
 * A target output represent a way to store the generated Postscript document. This is accomplished by two methods. 
 * First <code>selectOutput()</code> is called, which allows the user to select a file in interactive implementors,
 * or checks that a file is possible to create, or similar. Then <code>getOutputStream()</code> is called, which
 * returns an <code>OutputStream</code> to this output target.
 *
 * <p><b>Creation date:</b> 2001-08-22
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public interface CoPostscriptTargetOutput {
	public OutputStream getOutputStream();


	public boolean selectOutput();
}