package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.postscript.client.*;
import com.bluebrim.postscript.shared.*;

/**
 * Export to EPS file
 * Creation date: (2001-08-01 12:40:53)
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */

class CoExportAsEPS extends CoExportToFile {
	private final static CoPostscriptTarget TARGET = new CoPostscriptTarget.EpsFile() {
		protected void initialize() {
			setShrinkToFit(true);
			setEnlargeToFill(true);
			setMedia(CoPostscriptMedia.A4);
			setCenterHorizontally(true);
			setCenterVertically(true);
		}
	};

	// FIXME: for debugging
	private final static CoPostscriptTarget TARGET2 = new CoPostscriptTarget.EpsFile() {
		protected void initialize() {
			setShrinkToFit(false);
			setEnlargeToFill(false);
			setMedia(CoPostscriptMedia.NEWSPAPER);
			setCenterHorizontally(false);
			setCenterVertically(false);
		}
	};

	public CoExportAsEPS(CoLayoutEditor e) {
		super(e);
	}

	protected void doExportFile() {
		// Create a pageCollection to print
		java.util.List namedViewables = m_editor.getNamedViewables();

		CoPostscriptClientPrintJob printJob = new CoPostscriptClientPrintJob(TARGET, namedViewables);
		printJob.generatePostscript(CoPostscriptSelectFileOutput.EPS_FILE);
	}
}