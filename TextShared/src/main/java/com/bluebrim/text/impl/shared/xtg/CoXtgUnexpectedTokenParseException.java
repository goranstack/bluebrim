package com.bluebrim.text.impl.shared.xtg;

/**
 * Xtg unexpected token exception
 * 
 * @author: Dennis
 */

public class CoXtgUnexpectedTokenParseException extends CoXtgParseException
{
	private Class m_expectedToken;
	private CoXtgToken m_token;
public CoXtgUnexpectedTokenParseException( CoXtgToken token, Class expectedToken )
{
	super( "Unexpected token, found " + token + " when expecting " + expectedToken );

	m_token = token;
	m_expectedToken = expectedToken;
}
public Class getExpectedToken()
{
	return m_expectedToken;
}
public CoXtgToken getToken()
{
	return m_token;
}
}
