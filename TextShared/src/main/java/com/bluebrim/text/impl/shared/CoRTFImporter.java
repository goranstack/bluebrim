package com.bluebrim.text.impl.shared;

import java.io.*;

import javax.swing.text.rtf.*;

/**
 * Class for importing rtf
 *
 * @author: Dennis Malmström
 */
 
public class CoRTFImporter extends com.bluebrim.text.shared.CoAbstractTextImporter
{
public CoRTFImporter()
{
}
public com.bluebrim.text.shared.CoStyledDocument doImport( Reader r, com.bluebrim.text.shared.CoStyledDocument doc ) throws javax.swing.text.BadLocationException
{
	if ( doc == null ) doc = new com.bluebrim.text.shared.CoStyledDocument();

	doc.clear();

	try
	{
		m_editorKit.read( r, doc, 0 );
		doc.convertFromRTF();
	}
	catch ( IOException ex )
	{
		System.out.println("IOException");
	}
	
	return doc;
}

	private RTFEditorKit m_editorKit = new RTFEditorKit();
}