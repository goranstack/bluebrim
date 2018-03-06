package com.bluebrim.base.shared;

/**
 *
 * <p>Interface for classes that has a format() method in order to
 * deliver a (often localized) representation of itself.</p>
 *
 * <p>This interface is somewhat similar to CoNamed and (parts of)
 * CoVisualizable. However, the String returned from format() is
 * thought to describe the entire state of the reciever, whereas
 * what getName() and the suspiciously named getIdentity() returns
 * normally is merely a small part of such a state.</p>
 *
 * <p>Still, a unified replacement for toString() would be welcome.</p>
 *
 * <p>Notice also that using the format() method for exporting to
 * XML or similar is no good, since it is localized. The interface
 * CoStringExportable extends this interface with a stringExport()
 * method for this case.</p>
 *
 * @see #format()
 * @see CoStringExportable
 * @see CoNamed
 * @author Markus Persson 2000-09-06
 */
public interface CoFormattable {
/**
 * Return default localized string representation.
 *
 * This is the preferred way to produce output intended for end users.
 *
 * NOTE: Do NOT use for export (like in XML), since such output
 * should be locale neutral. Use stringExport() instead.
 *
 * @see com.bluebrim.base.shared.CoDate#format()
 * @see com.bluebrim.base.shared.CoDate#export()
 * @author Markus Persson 2000-08-09
 */
public String format();
}
