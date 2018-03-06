package com.bluebrim.layout.impl.client.editor;
import java.awt.event.*;
import java.awt.print.*;
import java.util.*;

import com.bluebrim.postscript.client.*;
import com.bluebrim.postscript.impl.shared.printing.*;
import com.bluebrim.transact.shared.*;

/**
 * Export to Postscript file
 * Creation date: (2001-08-01 12:40:53)
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */

class CoAwtPrint extends CoLayoutEditorAction {

	public CoAwtPrint(CoLayoutEditor e) {
		super(e);
	}

	public void actionPerformed(ActionEvent ev) {
		CoCommand c = new CoCommand("AWT_PRINT") {
			public boolean doExecute() {
				doPrint();
				return true;
			}
		};
		m_editor.getCommandExecutor().doit(c, null);
	}

	private void doPrint() {
		// Create a pageCollection to print
		List namedViewables = m_editor.getNamedViewables();
		CoPostscriptClientPrintJob collector = new CoPostscriptClientPrintJob(null, namedViewables);
		CoPageCollection pages = collector.getPages();

		// Let the user modify the print job before starting it

		PrinterJob printerJob = PrinterJob.getPrinterJob();
		printerJob.setPageable(pages);
		printerJob.setJobName(pages.getTitle());
		PageFormat pageFormat = m_editor.getPageFormat();

		pages.setPageFormat(pageFormat);

		if (printerJob.printDialog()) {
			try {
				printerJob.print();
			} catch (PrinterException e) {
				e.printStackTrace(); // FIXME: better error handling
			}
		}
	}
}