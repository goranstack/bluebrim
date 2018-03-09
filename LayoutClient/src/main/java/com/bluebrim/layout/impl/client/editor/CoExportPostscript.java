package com.bluebrim.layout.impl.client.editor;
import java.awt.event.*;
import java.io.*;

import com.bluebrim.postscript.client.*;
import com.bluebrim.postscript.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * Export to Postscript file
 * Creation date: (2001-08-01 12:40:53)
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */

abstract class CoExportPostscript extends CoLayoutEditorAction {
	protected CoPostscriptTarget m_target;

public CoExportPostscript(CoLayoutEditor e) {
	super(e);
}


public void actionPerformed(ActionEvent ev) {
	CoCommand c = new CoCommand("GENERATE_POSTSCRIPT") {
		public boolean doExecute() {
			doPrintPostscript();
			return true;
		}
	};
	m_editor.getCommandExecutor().doit(c, null);
}


private void doPrintPostscript() {

	// Create a pageCollection to print
	java.util.List namedViewables = m_editor.getNamedViewables();

	// Get an outputstream
	OutputStream out = getOutputStream();

	// Generate Postscript file
	if (out != null) {
		CoPostscriptClientPrintJob printJob = new CoPostscriptClientPrintJob(m_target, namedViewables);
		printJob.generatePostscript(out);
	}

	try {
		out.close();
	} catch (Exception ignored) {
	}
}


protected abstract OutputStream getOutputStream();
}