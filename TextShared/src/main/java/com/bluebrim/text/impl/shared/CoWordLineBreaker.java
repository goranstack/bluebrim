package com.bluebrim.text.impl.shared;

import javax.swing.text.*;

/**
 * Implementation of a linebreaker that allows breaking between words.
 * 
 * @author: Dennis Malmström
 */

public class CoWordLineBreaker extends CoAbstractLineBreaker implements CoWordLineBreakerIF
{


//	private static CoWordLineBreaker m_instance; // singleton

	private CoBetweenWordsBreakPointIterator m_iterator;
public static CoWordLineBreaker create()
{
	return new CoWordLineBreaker();
}
public CoLineBreakerIF.BreakPointIteratorIF getBreakPoints( Segment text )
{
	// Note: The lazy initialization below is not a problem transaction-wise.
	//       This method can't be called on a server object because the return value isn't rmi-capable.
	if ( m_iterator == null ) m_iterator = new CoBetweenWordsBreakPointIterator();
	
	m_iterator.set( text );
	
	return m_iterator;
}
public java.lang.String getFactoryKey()
{
	return BETWEEN_WORDS_LINE_BREAKER;
}

private CoWordLineBreaker()
{
}

	public static final String XML_TAG = "word-line-breaker";

public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel( Object superModel, org.w3c.dom.Node node, com.bluebrim.xml.shared.CoXmlContext context )
{
	return create();
}
}