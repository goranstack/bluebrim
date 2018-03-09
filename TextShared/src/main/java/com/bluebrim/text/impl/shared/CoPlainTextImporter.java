package com.bluebrim.text.impl.shared;

import java.io.*;

/**
 * Class for importing plain text
 *
 * @author: Dennis Malmström
 */
 
public class CoPlainTextImporter extends com.bluebrim.text.shared.CoAbstractTextImporter
{
public CoPlainTextImporter()
{
}
public com.bluebrim.text.shared.CoStyledDocument doImport( Reader r, com.bluebrim.text.shared.CoStyledDocument doc ) throws javax.swing.text.BadLocationException
{
	if ( doc == null ) doc = new com.bluebrim.text.shared.CoStyledDocument();

	String text = readText( r );

	doc.clear();

	doc.insertString( 0, text, null );
	
	return doc;
}
}
