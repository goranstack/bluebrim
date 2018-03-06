package com.bluebrim.text.impl.shared.xtg;

/**
 * Xtg scanner attribute list token
 * 
 * @author: Dennis
 */
 
class CoXtgAttributesToken extends CoXtgStringToken
{
public CoXtgAttributesToken( String str )
{
	super( str );
}
public CoXtgScanner getScanner( CoXtgLogger l )
{
	return new CoXtgAttributesScanner( new java.io.StringReader( getString() ), l );
}
}
