package com.bluebrim.layout.impl.client.editor;
import java.io.*;

import javax.swing.*;

/**
 * <no description entered yet><|>
 * Creation date: (2001-08-01 14:17:55)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public class CoExportPostscriptToFile extends CoExportPostscript {
/**
 * CoPrintPostscriptToFile constructor comment.
 * @param e com.bluebrim.layout.impl.client.editor.CoLayoutEditor
 */
public CoExportPostscriptToFile(CoLayoutEditor e) {
	super(e);
	m_target = com.bluebrim.postscript.shared.CoPostscriptTarget.DESKTOP_PRINTER;
}


protected OutputStream getOutputStream() {
	// FIXME: localize strings!
	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setDialogTitle("Spara Postscriptfil");
	fileChooser.setApproveButtonText("Spara");

	fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
		public String getDescription() {
			return "Postscript-filer (*.ps)";
		}
		public boolean accept(File file) {
			if (file.isDirectory())
				return true;
			return file.getName().toLowerCase().endsWith(".ps");
		}
	});

	if (fileChooser.showSaveDialog(m_editor.getWorkspace()) == JFileChooser.APPROVE_OPTION) {
		try {
			return new FileOutputStream(fileChooser.getSelectedFile());
		} catch (IOException e) {
			return null;
		}
	} else {
		return null;
	}
}
}