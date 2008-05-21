package com.bluebrim.text.impl.client;

import java.text.*;

import com.bluebrim.base.shared.*;



/**
 * Creation date: (2000-05-31 15:33:02)
 * @author: Dennis
 */
 
public class CoTextUiUtilities
{
	protected static final Format m_converter = NumberFormat.getInstance( java.util.Locale.getDefault() );
private CoTextUiUtilities()
{
	super();
}
private static float doParse( String s ) throws ParseException
{
	double d = CoLengthUnitSet.parse( s, Double.NaN, CoLengthUnit.LENGTH_UNITS );
	if ( Double.isNaN( d ) ) throw new ParseException( "", 0 );
	return (float) d;
}



}