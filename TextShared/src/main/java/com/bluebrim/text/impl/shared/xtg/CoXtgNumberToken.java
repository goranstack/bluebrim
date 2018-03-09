package com.bluebrim.text.impl.shared.xtg;

/**
 * Xtg scanner number token
 * 
 * @author: Dennis
 */
 
class CoXtgNumberToken extends CoXtgStringToken
{
	private float m_value;
public CoXtgNumberToken( String str, float v )
{
	super( str );
	
	m_value = v;
}
public float getFloat()
{
	return m_value;
}
}
