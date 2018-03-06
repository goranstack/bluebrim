package com.bluebrim.base.client;

import java.text.ParseException;

import com.bluebrim.base.shared.CoBaseUtilities;
import com.bluebrim.gui.client.CoConverter;
import com.bluebrim.gui.client.CoValueModel;

/**
 * Converter class implemented to handle bank account numbers.
 * <br>
 * The bank account number is stored without white spaces and no "-"
 * separating the clearing number from the "räkningsnummer"
 * author Karin Gyulai 000811
 * <br>
 * 
 */
public class CoBankAccountNumberConverter extends CoConverter {
	

public CoBankAccountNumberConverter(CoValueModel valueModel) {
	super(valueModel);


}
protected String doFormat(Object aValue) {
	String 	str 		= aValue.toString();
	if (CoBaseUtilities.stringIsEmpty(str))
		return null;
	
	// Remove white spaces and dashes here too?
	//String 	canonString = CoBaseUtilities.removeAllWhitespaces(str);
	

	StringBuffer buffer = new StringBuffer();
	buffer.append(str);
	buffer.insert(4,getDelimiter());
	return buffer.toString();
	
}
protected Object doParse(String stringValue) throws ParseException{
	if (CoBaseUtilities.stringIsEmpty(stringValue))
	return null;
	
	String 	canonString = CoBaseUtilities.removeAllWhitespaces(stringValue);
	canonString = CoBaseUtilities.removeFromString(canonString, '-');

	int len = canonString.length();
	if ((len < 8 ) || (len > 16))
		throw new ParseException("Wrong format", 0);

	
	String clearingNumber = canonString.substring(0,4);
	
	// Verify clearing number
	// Not yet Implemented
	
	// Verify the correctnes af the bank account
	// check sum algorithm?
	// Not Yet Implemented
	try{
		return new Long(canonString);
	}
	catch(Exception e ){
		throw new ParseException("Wrong format", 0);
	}
	
}
protected Object getDefaultValue()
{
	return "cccc yyyyyyyy";	
}
public String getDelimiter()
{
	return "-";
}
protected String getType()
{
	return "Bankkontonummer";	// PENDING: Localize
}




protected Object parseString(String newValue) throws ParseException
{
	if ( newValue == null ) return null;
	return doParse(newValue.toString()); 
}



}