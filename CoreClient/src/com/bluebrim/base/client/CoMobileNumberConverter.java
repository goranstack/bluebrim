package com.bluebrim.base.client;

import java.text.ParseException;

import com.bluebrim.base.shared.CoBaseUtilities;
import com.bluebrim.gui.client.CoConverter;
import com.bluebrim.gui.client.CoValueModel;

/**
 * Converter class implemented to handle mobile phone numbers.
 * <br>
 * The phone number is stored in a canonicalized form, w/o
 * any white spaces and with a "-" separating the area code
 * from the phone number.
 * <br>
 * PENDING: How do we handle foreign numbers?
 */
public class CoMobileNumberConverter extends CoConverter {
	private String m_defaultCountryCode;

	public CoMobileNumberConverter(CoValueModel valueModel, String defaultCountryCode) {
		super(valueModel);
		m_defaultCountryCode = defaultCountryCode;

	}
	protected String doFormat(Object aValue) {
		String str = aValue.toString();
		if (CoBaseUtilities.stringIsEmpty(str))
			return null;
		return CoBaseUtilities.formatPhoneNumber(str, m_defaultCountryCode, null, getDelimiter());
	}
	protected Object doParse(String stringValue) throws ParseException {
		return CoBaseUtilities.canonicalizePhoneNumber(stringValue, m_defaultCountryCode, null, getDelimiter());
	}
	protected Object getDefaultValue() {
		return "070-412 34 56";
	}
	public String getDelimiter() {
		return "-";
	}
	protected String getType() {
		return "Mobilnummer"; // PENDING: Localize
	}
	protected Object parseString(String newValue) throws ParseException {
		if (newValue == null)
			return null;
		return doParse(newValue);
	}

}