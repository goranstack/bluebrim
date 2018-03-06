package com.bluebrim.layout.impl.client.editor;


/**
 * Export to Postscript file
 * Creation date: (2001-08-01 12:40:53)
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */

class CoExportAsPostscript extends CoExportToFile {
	private final static com.bluebrim.postscript.shared.CoPostscriptTarget TARGET = new com.bluebrim.postscript.shared.CoPostscriptTarget.PostscriptFile() {
		protected void initialize() {
			setShrinkToFit(true);
			setEnlargeToFill(true);
			setMedia(com.bluebrim.postscript.shared.CoPostscriptMedia.A4);
			setCenterHorizontally(true);
			setCenterVertically(true);
		}
	};

	// FIXME: for debugging
	private final static com.bluebrim.postscript.shared.CoPostscriptTarget TARGET2 = new com.bluebrim.postscript.shared.CoPostscriptTarget.PostscriptFile() {
		protected void initialize() {
			setShrinkToFit(false);
			setEnlargeToFill(false);
			setMedia(com.bluebrim.postscript.shared.CoPostscriptMedia.NEWSPAPER);
			setCenterHorizontally(false);
			setCenterVertically(false);
		}
	};

public CoExportAsPostscript(CoLayoutEditor e) {
	super(e);
}


protected void doExportFile() {
	// Create a pageCollection to print
	java.util.List namedViewables = m_editor.getNamedViewables();

	com.bluebrim.postscript.client.CoPostscriptClientPrintJob printJob = new com.bluebrim.postscript.client.CoPostscriptClientPrintJob(TARGET, namedViewables);
	printJob.generatePostscript(com.bluebrim.postscript.client.CoPostscriptSelectFileOutput.PS_FILE);
}
}