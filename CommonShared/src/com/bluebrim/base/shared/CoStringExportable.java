package com.bluebrim.base.shared;


/**
 * <p>CoStringExportable is an interface that extends CoFormattable
 * with a stringExport() method providing a locale neutral complete
 * string representation. Complete in the sense that when the class
 * is known, all state required to recreate the object should be
 * contained in the string.</p>
 *
 * <p>Support for parsing is unfortunately hard to express in an
 * interface, since such methods typically are static or reside
 * in formatting classes such as subclasses of java.text.Format.</p>
 *
 * @see #stringExport()
 * @see CoFormattable
 * @author Markus Persson 2000-09-06
 */
public interface CoStringExportable extends CoFormattable {
/**
 * Return locale neutral complete string representation. Complete
 * in the sense that when the class is known, all state required
 * to recreate the object should be contained in the string.
 *
 * This method is the only supported way of obtaining such a string
 * representation when exporting to XML and similar.
 *
 * NOTE: The returned string should be fully humanly readable.
 * (This is a XML design goal.) However, for end user display,
 * use format() instead since it takes locale into account.
 *
 * Support for parsing is unfortunately hard to express in an
 * interface, since such methods typically are static or reside
 * in formatting classes such as subclasses of java.text.Format.
 *
 * @see CoFormattable#format()
 * @see com.bluebrim.base.shared.CoDate#parseExported(String)
 * @see java.text.Format
 * @author Markus Persson 2000-09-06
 */
public String stringExport();
}
