package com.bluebrim.base.client;

import java.text.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;

/**
 * Converter class implemented to handle phone numbers.
 * Input of phone numbres w/o an area code is supported.
 * If this is the case the phone number is concatenated to
 * a default area code, given in the constructor.
 * <br>
 * The phone number is stored in a canonicalized form, w/o
 * any white spaces and with a "-" separating the area code
 * from the phone number.
 * <br>
 * PENDING: How do we handle foreign numbers?
 */
public class CoPhoneNumberConverter extends CoConverter {
	private String m_defaultCountryCode;
	private String m_defaultAreaCode;
public CoPhoneNumberConverter(CoValueModel valueModel,String defaultCountryCode, String defaultAreaCode) {
	super(valueModel);
	m_defaultCountryCode	= defaultCountryCode;
	m_defaultAreaCode 		= defaultAreaCode;
}
protected String doFormat(Object aValue) {
	String 	str 		= aValue.toString();
	if (CoBaseUtilities.stringIsEmpty(str))
		return null;
	return CoBaseUtilities.formatPhoneNumber(str, m_defaultCountryCode, m_defaultAreaCode, getDelimiter());
}
protected Object doParse(String stringValue) throws ParseException{
	return CoBaseUtilities.canonicalizePhoneNumber(stringValue, m_defaultCountryCode,m_defaultAreaCode, getDelimiter());
}
protected Object getDefaultValue()
{
	return "012-34 56 78";	
}
public String getDelimiter()
{
	return "-";
}
protected String getType()
{
	return "Telefonnummer";	// PENDING: Localize
}
protected Object parseString(String newValue) throws ParseException
{
	if ( newValue == null ) return null;
	return doParse(newValue); 
}

}