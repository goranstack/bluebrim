package com.bluebrim.layout.impl.client.editor;
import java.awt.event.*;
import java.awt.print.*;

import javax.print.attribute.*;

import com.bluebrim.transact.shared.*;

/**
 * Creation date: (2001-08-01 12:40:53)
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */

class CoChangePrintSettings extends CoLayoutEditorAction {

public CoChangePrintSettings(CoLayoutEditor e) {
	super(e);
}


public void actionPerformed(ActionEvent ev) {
	CoCommand c = new CoCommand("CHANGE_PRINT_SETTINGS") {
		public boolean doExecute() {
			doChangeSettings();
			return true;
		}
	};
	m_editor.getCommandExecutor().doit(c, null);
}




private void doChangeSettings() {
	PrinterJob printerJob = PrinterJob.getPrinterJob();
	PageFormat pageFormat = m_editor.getPageFormat();
	PrintRequestAttributeSet aset = m_editor.getPrintRequestAttributeSet();
	pageFormat = printerJob.pageDialog(aset);
	
	m_editor.setPageFormat(pageFormat);
}
}