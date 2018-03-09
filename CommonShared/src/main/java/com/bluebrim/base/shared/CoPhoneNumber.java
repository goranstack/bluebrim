package com.bluebrim.base.shared;
import java.io.*;

/**
 * Small class used to represent phone numbers where
 * an object is benificial.
 *
 * @author Markus Persson 2001-04-26
 */
public class CoPhoneNumber implements Serializable {
	private String m_countryCode;
	private String m_areaCode;
	private String m_localNumber;
	private transient String m_canonicalCache;

public String getAreaCode() {
	return m_areaCode;
}

public String getCountryCode() {
	return "0" + m_countryCode;
}

public String getLocalNumber() {
	return m_localNumber;
}

public String getCanonical() {
	if (m_canonicalCache == null) {
		return m_canonicalCache = '+' + m_countryCode + '-' + m_areaCode + '-' + m_localNumber;
	}
	return m_canonicalCache;
}

/**
 * NOTE: Current representation (which only is assured to be valid for Sweden)
 * requires the area code to be given here without the initial zero. The
 * getAreaCode() method however returns with the zero. That's one reason why
 * this constructor is private.
 */
private CoPhoneNumber(String countryCode, String areaCode, String localNumber) {
	m_countryCode = countryCode;
	m_areaCode = areaCode;
	m_localNumber = localNumber;
}


/**
 * Quick method for canonicalized representation (+46-8-7341600).
 *
 * PENDING: Do other methods for other (generic) representations.
 */
public static CoPhoneNumber forCanonical(String canonicalized) {
	if (CoBaseUtilities.stringIsEmpty(canonicalized))
		return null;

	int first = canonicalized.indexOf('-');
	int second = canonicalized.indexOf('-', first + 1);

	if ((first < 0) || (second < 0))
		throw new IllegalArgumentException("Must use canonicalized format +46-8-7341600!");

	return new CoPhoneNumber(canonicalized.substring(1, first),
							canonicalized.substring(first + 1, second),
							canonicalized.substring(second + 1));

}

public String toString() {
	return getCanonical();
}
}