package com.bluebrim.text.impl.shared;

import java.util.*;

/**
 * Enumerable attribute value
 *
 * @author: Dennis Malmström
 */

public class CoEnumValue implements java.io.Serializable
{
	private final String m_string;

	private static Map m_allInstances = new HashMap(); // [ String -> CoEnumValue ]
	
	static final long serialVersionUID = -3858715781840330807L;
public CoEnumValue( String string )
{
	m_string = string;

	// make sure there is only one of each
	if
		( m_allInstances.get( toString() ) != null )
	{
		throw new RuntimeException( "Duplicate CoEnumValue instances" );
	}
	
	m_allInstances.put( toString(), this );
}
public boolean equals( Object o )
{
	if ( this == o )                      return true;
	if ( ! ( o instanceof CoEnumValue ) ) return false;
	                                      return m_string.equals( o.toString() );
}
public static CoEnumValue getEnumValue( String s )
{
	if ( s == null ) return null;
	return (CoEnumValue) m_allInstances.get( s );
}
public int hashCode()
{
	return m_string.hashCode();
}
public String toString()
{
	return m_string;
}
}