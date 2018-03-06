package com.bluebrim.text.impl.shared;

import javax.swing.text.*;

/**
 * Implementation of a linebreaker that allows breaking anywhere.
 * 
 * @author: Dennis Malmström
 */

public class CoAnywhereLineBreaker extends CoAbstractLineBreaker implements CoAnywhereLineBreakerIF
{


//	private static CoAnywhereLineBreaker m_instance; // singleton

	// iterators used to define "anywhere"
	private CoIntervalBreakPointIterator m_positiveIterator;
	private CoBadBreakPointIterator m_negativeIterator;
	private CoDifferenceBreakPointIterator m_iterator;
public static CoAnywhereLineBreaker create()
{
	return new CoAnywhereLineBreaker();
}
public CoLineBreakerIF.BreakPointIteratorIF getBreakPoints( Segment text )
{
	// Note: The lazy initialization below is not a problem transaction-wise.
	//       This method can't be called on a server object because the return value isn't rmi-capable.
	if ( m_positiveIterator == null ) m_positiveIterator = new CoIntervalBreakPointIterator();
	if ( m_negativeIterator == null ) m_negativeIterator = new CoBadBreakPointIterator();
	if ( m_iterator == null ) m_iterator = new CoDifferenceBreakPointIterator();

	m_positiveIterator.set( text.count - 1, 1 ); // all breakpoints in text
	m_negativeIterator.set( text ); // bad breakpoints (see CoBadBreakPointIterator)
	m_iterator.set( m_positiveIterator, m_negativeIterator ); // anywhere = all breakpoints - bad breakpoints
	
	return m_iterator;
}
public java.lang.String getFactoryKey()
{
	return ANYWHERE_LINE_BREAKER;
}

private CoAnywhereLineBreaker()
{
}

	public static final String XML_TAG = "anywhere-line-breaker";

public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel( Object superModel, org.w3c.dom.Node node, com.bluebrim.xml.shared.CoXmlContext context )
{
	return create();
}
}