package com.bluebrim.xml.shared;
import org.w3c.dom.*;

/**
 * 
 * @author: Dennis
 */

public class CoXmlUtilities {
	private CoXmlUtilities() {
		super();
	}

	public static Boolean parseBoolean(String xmlAttributeValue, Boolean invalidValue) {
		if (xmlAttributeValue == null)
			return invalidValue;

		if (xmlAttributeValue.equals(Boolean.TRUE.toString())) {
			return Boolean.TRUE;
		} else
			if (xmlAttributeValue.equals(Boolean.FALSE.toString())) {
				return Boolean.FALSE;
			} else {
				return invalidValue;
			}
	}

	public static Boolean parseBoolean(Node xmlAttribute, boolean invalidValue) {
		if (xmlAttribute == null)
			return invalidValue ? Boolean.TRUE : Boolean.FALSE;
		return parseBoolean(xmlAttribute.getNodeValue(), invalidValue);
	}
	
	public static Boolean parseBoolean(Node xmlAttribute, Boolean invalidValue) {
		if (xmlAttribute == null)
			return invalidValue;
		return parseBoolean(xmlAttribute.getNodeValue(), invalidValue);
	}

	public static Boolean parseBoolean(String xmlAttributeValue, boolean invalidValue) {
		return parseBoolean(xmlAttributeValue, invalidValue ? Boolean.TRUE : Boolean.FALSE);
	}

	public static double parseDouble(String xmlAttributeValue, double invalidValue) {
		if (xmlAttributeValue == null)
			return invalidValue;

		try {
			return Double.valueOf(xmlAttributeValue).doubleValue();
		} catch (NumberFormatException ex) {
			return invalidValue;
		}
	}

	public static float parseFloat(Node xmlAttribute, float invalidValue) {
		if (xmlAttribute == null)
			return invalidValue;
		return parseFloat(xmlAttribute.getNodeValue(), invalidValue);
	}


	public static float parseFloat(String xmlAttributeValue, float invalidValue) {
		if (xmlAttributeValue == null)
			return invalidValue;

		try {
			return Double.valueOf(xmlAttributeValue).floatValue();
		} catch (NumberFormatException ex) {
			return invalidValue;
		}
	}

	public static int parseInt(Node xmlAttribute, int invalidValue) {
		if (xmlAttribute == null)
			return invalidValue;
		return parseInt(xmlAttribute.getNodeValue(), invalidValue);
	}


	public static int parseInt(String xmlAttributeValue, int invalidValue) {
		if (xmlAttributeValue == null)
			return invalidValue;

		try {
			return Integer.valueOf(xmlAttributeValue).intValue();
		} catch (NumberFormatException ex) {
			return invalidValue;
		}
	}

	public static long parseLong(String xmlAttributeValue, long invalidValue) {
		if (xmlAttributeValue == null)
			return invalidValue;

		try {
			return Long.valueOf(xmlAttributeValue).longValue();
		} catch (NumberFormatException ex) {
			return invalidValue;
		}
	}

	public static String parseString(Node xmlAttribute, String invalidValue) {
		if (xmlAttribute == null)
			return invalidValue;

		return xmlAttribute.getNodeValue();
	}

	public static String parseString(String xmlAttributeValue, String invalidValue) {
		if (xmlAttributeValue == null)
			return invalidValue;

		return xmlAttributeValue;
	}
}
