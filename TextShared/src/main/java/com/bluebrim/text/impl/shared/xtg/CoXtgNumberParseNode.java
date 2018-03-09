package com.bluebrim.text.impl.shared.xtg;

import java.io.*;

/**
 * Xtg number parse tree node
 * 
 * @author: Dennis
 */
 
public class CoXtgNumberParseNode extends CoXtgParseNode
{
	private String m_string;
	private float m_number;
/**
 * CoXtgRootParseNode constructor comment.
 */
public CoXtgNumberParseNode( String str, float f )
{
	m_string = str;
	m_number = f;
}
public void createXtg( PrintStream s )
{
}
public void dump( String indent )
{
	System.err.println( indent + this + " : " + m_number);
}
public void extract( com.bluebrim.text.shared.CoTypographyRuleIF r, CoXtgLogger l )
{
}
public void extract( com.bluebrim.text.shared.CoStyledDocumentIF d, CoXtgLogger logger )
{
}
public float getNumber()
{
	return m_number;
}
public String getString()
{
	return m_string;
}
boolean parse( CoXtgParser p, CoXtgLogger l ) throws CoXtgParseException
{
	return true;
}
}
