package com.bluebrim.base.shared.debug;

import java.io.*;

/**
 * Implementation of CoDiffReport that writes messages to a PrintStream.
 * <p>
 * Creation date: (2001-03-06 10:44:40)
 * @author: Dennis
 */

public class CoDefaultDiffReport implements CoDiffReport
{
	private PrintStream m_stream;
public CoDefaultDiffReport()
{
	this( System.err );
}
public CoDefaultDiffReport( PrintStream stream )
{
	m_stream = stream;
}
public void comparing( String prefix, String attributeName, CoDiffable diffable1, CoDiffable diffable2 )
{
	m_stream.print( " " );
	
	if
		( attributeName == null )
	{
		m_stream.println( prefix + "*** diff " + diffable1 + " ~ " + diffable2 );
	} else {
		m_stream.println( prefix + "*** diff " + attributeName + " : " + diffable1 + " ~ " + diffable2 );
	}
}
public void differentArrayLengths( String prefix, String attributeName, int length1, int length2 )
{
	m_stream.print( "#" );
	
	m_stream.println( prefix + "  " + attributeName + ".length : " + length1 + " != " + length2 );
}
public void differentCollectionLengths( String prefix, String attributeName, int length1, int length2 )
{
	m_stream.print( "#" );
	
	m_stream.println( prefix + "  " + attributeName + ".size() : " + length1 + " != " + length2 );
}
public void diffFailed( Exception ex )
{
	m_stream.println( "Diff failed : " + ex );
	ex.printStackTrace( m_stream );
}
public void notEqual( String prefix, String attributeName, int index, Object value1, Object value2 )
{
	m_stream.print( "#" );
	
	if
		( index == -1 )
	{
		m_stream.println( prefix + "  " + attributeName + " : " + value1 + " ! equals " + value2 );
	} else {
		m_stream.println( prefix + "  " + attributeName + "[ " + index + " ] : " + value1 + " ! equals " + value2 );
	}
}
public void notIdentical( String prefix, String attributeName, int index, Object value1, Object value2 )
{
	m_stream.print( "#" );
	
	if
		( index == -1 )
	{
		m_stream.println( prefix + "  " + attributeName + " : " + value1 + " != " + value2 );
	} else {
		m_stream.println( prefix + "  " + attributeName + "[ " + index + " ] : " + value1 + " != " + value2 );
	}
}
public void typeClash( String prefix, String attributeName, int index, Object value1, Object value2 )
{
	m_stream.print( "#" + prefix + "  " );

	if
		( index == -1)
	{
		m_stream.print( attributeName + ": type clash: " );
	} else {
		m_stream.print( attributeName + "[" + index + "]: type clash: " );
	}

	m_stream.println(value1.getClass() + " not equal to " + value2.getClass());
}

public void typeClash( String prefix, CoDiffable diffable1, CoDiffable diffable2 )
{
	m_stream.print( "#" );
	
	m_stream.println(
		prefix +
		"  type clash: " +
		diffable1.getClass() +
		" not equal to " +
		diffable2.getClass());
}
}