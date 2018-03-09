package com.bluebrim.base.shared;

import java.util.*;
/**
 * The string tokenizer class allows an application to break a 
 * string into tokens. This class breaks up the source 
 * string backwards, e g starting from the end.
 * <br>
 * This class doesn't implement <code>Enumeration</code>
 * Creation date: (2000-05-29 12:12:29)
 * @author: Lasse
 */
public class CoReversedStringTokenizer
{
	private int m_currentPosition;
	private String m_source;
	private String m_delimiters;
	
/**
 * CoReversedStringTokenizer constructor comment.
 */
public CoReversedStringTokenizer() {
	super();
}
public CoReversedStringTokenizer(String source, String delimiters)
{
	m_source 			= source;
	m_delimiters 		= delimiters;
	m_currentPosition 	= m_source.length()-1;
}
public boolean hasMoreTokens()
{
	skipDelimiters();
	return (m_currentPosition >= 0);
}
public String nextToken()
{
	skipDelimiters();
	if (m_currentPosition < 0)
	{
		throw new NoSuchElementException();
	}
	int start = m_currentPosition;
	while ((m_currentPosition >= 0) && (m_delimiters.indexOf(m_source.charAt(m_currentPosition)) < 0))
	{
		m_currentPosition--;
	}
	if ((start == m_currentPosition) && (m_delimiters.indexOf(m_source.charAt(m_currentPosition)) >= 0))
	{
		m_currentPosition--;
	}
	return m_source.substring(m_currentPosition+1, start+1);
}
private void skipDelimiters()
{
	while ((m_currentPosition >= 0) && (m_delimiters.indexOf(m_source.charAt(m_currentPosition)) >= 0))
	{
		m_currentPosition--;
	}
}
}
