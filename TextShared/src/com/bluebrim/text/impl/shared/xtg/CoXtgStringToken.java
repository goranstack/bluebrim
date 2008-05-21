package com.bluebrim.text.impl.shared.xtg;

/**
 * Xtg scanner string token
 * 
 * @author: Dennis
 */
 
class CoXtgStringToken extends CoXtgToken
{
	private String m_string;
public CoXtgStringToken( String string )
{
	m_string = string;
}
public String getString()
{
	return m_string;
}
public String toString()
{
	return super.toString() + " : " + m_string;
}
}
