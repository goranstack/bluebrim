package com.bluebrim.layout.impl.client.editor;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

import com.bluebrim.transact.shared.*;

/**
 * Export to EPS file
 * Creation date: (2001-08-01 12:40:53)
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */

abstract class CoExportToFile extends CoLayoutEditorAction {
	private final static boolean DEBUG_USE_STATIC_FILENAMES = System.getProperty("calvin.debug.quicksave", "false").equals("true");

public CoExportToFile(CoLayoutEditor e) {
	super(e);
}


public void actionPerformed(ActionEvent ev) {
	CoCommand c = new CoCommand("EXPORT_FILE") {
		public boolean doExecute() {
			doExportFile();
			return true;
		}
	};
	m_editor.getCommandExecutor().doit(c, null);
}


protected abstract void doExportFile();

protected File getFile(String dialogTitle, String fileDescription, String fileExtension) {
	// FIXME: debug code only
	if (DEBUG_USE_STATIC_FILENAMES) {
		return new File("C:\\ps_save\\Test." + fileExtension);
	}
	
	final String completeDescription = fileDescription + "(*." + fileExtension + ")";
	final String dottedFileExtension = "." + fileExtension;
	
	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setDialogTitle(dialogTitle);
	fileChooser.setApproveButtonText("Spara"); // FIXME

	fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
		public String getDescription() {
			return completeDescription;
		}
		public boolean accept(File file) {
			if (file.isDirectory())
				return true;
			return file.getName().toLowerCase().endsWith(dottedFileExtension);
		}
	});

	if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) { // null => center on screen...
		return fileChooser.getSelectedFile();
	} else {
		return null;
	}
}
}