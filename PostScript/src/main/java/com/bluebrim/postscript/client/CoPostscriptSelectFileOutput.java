package com.bluebrim.postscript.client;
import java.io.*;

import javax.swing.*;

/**
 * A com.bluebrim.postscript.shared.CoPostscriptTargetOutput on the client that displays a file selector, and allows the user to save
 * the Postscript to a file.
 *
 * <p><b>Creation date:</b> 2001-08-22
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public class CoPostscriptSelectFileOutput implements com.bluebrim.postscript.shared.CoPostscriptTargetOutput {
	private final static boolean DEBUG_USE_STATIC_FILENAMES = System.getProperty("calvin.debug.quicksave", "false").equals("true");
	// FIXME: i18n the following text!
	public final static com.bluebrim.postscript.shared.CoPostscriptTargetOutput EPS_FILE = new CoPostscriptSelectFileOutput("Spara EPS-fil", "EPS-filer", "eps");
	public final static com.bluebrim.postscript.shared.CoPostscriptTargetOutput PS_FILE = new CoPostscriptSelectFileOutput("Spara Postscriptfil", "Postscriptfiler", "ps"); 
	private OutputStream m_out;

	private String m_dialogTitle;
	private String m_fileDescription;
	private String m_fileExtension;

public CoPostscriptSelectFileOutput(String dialogTitle, String fileDescription, String fileExtension) {
	m_dialogTitle = dialogTitle;
	m_fileDescription = fileDescription;
	m_fileExtension = fileExtension;
}


public OutputStream getOutputStream() {
	return m_out;
}


public boolean selectOutput() {
	File outputFile = showFileDialog();

	if (outputFile == null) {
		return false;
	}

	try {
		m_out = new FileOutputStream(outputFile);
	} catch (IOException e) {
		return false;
	}

	return true;
}


protected File showFileDialog() {
	// FIXME: debug code only
	if (DEBUG_USE_STATIC_FILENAMES) {
		return new File("C:\\ps_save\\Test." + m_fileExtension);
	}

	final String completeDescription = m_fileDescription + "(*." + m_fileExtension + ")";
	final String dottedFileExtension = "." + m_fileExtension;

	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setDialogTitle(m_dialogTitle);
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