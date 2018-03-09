package com.bluebrim.text.impl.shared.xtg;

/**
 * Xtg scanner quoted string token
 * 
 * @author: Dennis
 */
 
class CoXtgQuotedStringToken extends CoXtgStringToken
{
public CoXtgQuotedStringToken( String str )
{
	super( str );
}
public String toString()
{
	return super.toString() + " : \"" + getString() + "\"";
}
}
