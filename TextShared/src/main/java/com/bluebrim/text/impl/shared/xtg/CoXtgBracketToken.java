package com.bluebrim.text.impl.shared.xtg;

/**
 * Xtg scanner bracketed list token
 * 
 * @author: Dennis
 */
 
class CoXtgBracketToken extends CoXtgStringToken
{
public CoXtgBracketToken( String str )
{
	super( str );
}
public CoXtgScanner getScanner( CoXtgLogger l )
{
	return new CoXtgBracketScanner( new java.io.StringReader( getString() ), l );
}
}
