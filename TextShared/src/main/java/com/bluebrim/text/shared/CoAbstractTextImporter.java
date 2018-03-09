package com.bluebrim.text.shared;
import java.io.*;

import com.bluebrim.text.impl.shared.*;

/**
 * Abstract class defining the protocol for importing text into a com.bluebrim.text.shared.CoStyledDocument
 * It also holds a collection of available implementations
 *
 * @author: Dennis Malmström
 */

public abstract class CoAbstractTextImporter
{
	// available importers (when importing a text of unknown format, the importers are tried in order until one succeeds)
	private static CoAbstractTextImporter [] m_importers =
		{
			new CoXtgTextImporter(),
			new CoRTFImporter(),
			new CoPlainTextImporter(),
		};

public abstract com.bluebrim.text.shared.CoStyledDocument doImport( java.io.Reader r, com.bluebrim.text.shared.CoStyledDocument doc ) throws Exception;
public static CoAbstractTextImporter [] getImporters()
{
	return m_importers;
}
// returns the log of the import process

public String getLog()
{
	return null;
}
// read the stream into a file

protected String readText( Reader r )
{
	StringBuffer	 sb;
	BufferedReader in;

	int i;
	
	try
	{					
		in = new BufferedReader( r );
		sb = new StringBuffer();
		
		while
			( ( i = in.read() ) != -1 )
		{
			sb.append((char)i);
		}
		
		in.close();
		
		return new String( sb );
		
	}
		catch (IOException e)
	{
		System.out.println("IOException");
		return "";
	}
}
}