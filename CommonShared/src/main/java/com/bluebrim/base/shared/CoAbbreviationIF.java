package com.bluebrim.base.shared;

/**
 * <p>Interface for objects that can deliver a abbreviation suitable to be displayed to the user.</p>
 * <p>The abbreviation is not required to be unique in any sense, nor constant over time,
 * and shall thus <b>never</b> be used as a means to uniquely identify an object.</p>
 * Creation date: (2001-05-23 09:34:43)
 * @author Arvid Berg
 */
public interface CoAbbreviationIF {
/**
 * <p>Gets the user displayable abbreviation of this object.</p>
 * Creation date: (2001-05-23 09:41:13)
 * @return java.lang.String
 */
String getAbbreviation();
/**
 * <p>Set the user displayable abbreviation of this object.</p>
 * Creation date: (2001-05-23 09:40:45)
 * @param abbreviation java.lang.String
 */
void setAbbreviation(String abbreviation);
}
