package com.bluebrim.text.impl.shared.xtg;

import java.io.*;

/**
 * Xtg quoted string parse tree node
 * 
 * @author: Dennis
 */
 
public class CoXtgQstringParseNode extends CoXtgParseNode
{
	private String m_qstring;
/**
 * CoXtgRootParseNode constructor comment.
 */
public CoXtgQstringParseNode( String qs )
{
	m_qstring = qs;
}
public void createXtg( PrintStream s )
{
}
public void dump( String indent )
{
	System.err.println( indent + this + " : " + m_qstring);
}
public void extract( com.bluebrim.text.shared.CoTypographyRuleIF r, CoXtgLogger l )
{
}
public void extract( com.bluebrim.text.shared.CoStyledDocumentIF d, CoXtgLogger l )
{
}
public String getQstring()
{
	return m_qstring;
}
boolean parse( CoXtgParser p, CoXtgLogger l ) throws CoXtgParseException
{
	return true;
}
}
