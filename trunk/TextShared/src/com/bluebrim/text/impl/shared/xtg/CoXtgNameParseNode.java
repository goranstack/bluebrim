package com.bluebrim.text.impl.shared.xtg;

import java.io.*;

/**
 * Xtg name parse tree node
 * 
 * @author: Dennis
 */
 
public class CoXtgNameParseNode extends CoXtgParseNode
{
	private String m_name;
/**
 * CoXtgRootParseNode constructor comment.
 */
public CoXtgNameParseNode( String name )
{
	m_name = name;
}
public void createXtg( PrintStream s )
{
}
public void dump( String indent )
{
	System.err.println( indent + this + " : " + m_name);
}
public void extract( com.bluebrim.text.shared.CoTypographyRuleIF r, CoXtgLogger l )
{
}
public void extract( com.bluebrim.text.shared.CoStyledDocumentIF d, CoXtgLogger logger )
{
}
public String getName()
{
	return m_name;
}
boolean parse( CoXtgParser p, CoXtgLogger l ) throws CoXtgParseException
{
	return true;
}
}
