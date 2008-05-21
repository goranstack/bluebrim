package com.bluebrim.text.impl.shared.xtg;

import java.io.*;

/**
 * Xtg text parse tree node
 * 
 * @author: Dennis
 */
 
public class CoXtgTextParseNode extends CoXtgParseNode
{
	private String m_text;
/**
 * CoXtgRootParseNode constructor comment.
 */
public CoXtgTextParseNode( String text )
{
	m_text = text;
}
public void createXtg( PrintStream s )
{
}
public void dump( String indent )
{
	System.err.println( indent + this + " : " + m_text);
}
public void extract( com.bluebrim.text.shared.CoTypographyRuleIF r, CoXtgLogger l )
{
}
public void extract( com.bluebrim.text.shared.CoStyledDocumentIF d, CoXtgLogger l )
{
}
public String getText()
{
	return m_text;
}
boolean parse( CoXtgParser p, CoXtgLogger l ) throws CoXtgParseException
{
	return true;
}
}
