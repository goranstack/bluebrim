package com.bluebrim.text.impl.shared;

import java.io.*;

import com.bluebrim.text.impl.shared.xtg.*;

/**
 * Class for importing text in the xtg format.
 *
 * @author: Dennis Malmström
 */
 
public class CoXtgTextImporter extends com.bluebrim.text.shared.CoAbstractTextImporter 
{
	private String m_log;
public CoXtgTextImporter()
{
}
public com.bluebrim.text.shared.CoStyledDocument doImport( Reader r, com.bluebrim.text.shared.CoStyledDocument doc ) throws Exception
{
	if ( doc == null ) doc = new com.bluebrim.text.shared.CoStyledDocument();

	CoXtgParser p = new CoXtgParser( r );
	if
		( ! p.parse() )
	{
		throw new Exception( "Not proper xtg format" );
	}
	
	p.extract( doc );

	m_log = p.getLog();
	
	return doc;
}
public String getLog()
{
	return m_log;
}
}
