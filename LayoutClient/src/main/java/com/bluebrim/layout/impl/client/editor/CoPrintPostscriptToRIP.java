package com.bluebrim.layout.impl.client.editor;
import java.io.*;

/**
 * <no description entered yet><|>
 * Creation date: (2001-08-01 14:17:55)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public class CoPrintPostscriptToRIP extends CoExportPostscript {
/**
 * CoPrintPostscriptToFile constructor comment.
 * @param e com.bluebrim.layout.impl.client.editor.CoLayoutEditor
 */
public CoPrintPostscriptToRIP(CoLayoutEditor e) {
	super(e);
	m_target = com.bluebrim.postscript.shared.CoPostscriptTarget.TYPESETTER;
}


protected OutputStream getOutputStream() {
	File defaultFile = new File("C:\\ps_save\\RIP.ps");
	try {
		return new FileOutputStream(defaultFile);
	} catch (IOException e) {
		return null;
	}
}
}