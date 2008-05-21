package com.bluebrim.font.impl.shared;

import com.bluebrim.font.impl.server.util.*;

/**
 * Interface for information that is possible to extract from a font file.
 * This interface is implemented by CoAbstractFontFileInfo, and a new font file format most
 * likely wants to subclass this implementation.
 * Creation date: (2001-04-12 15:34:49)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 * @see CoAbstractFontFileInfo
 */
public interface CoFontFileInfoExtractor {

public CoFontFileContainer getFileContainer();

/**
 * Check if a file is in a format that this class can handle. This is just done by simple heuristics
 * (like 'magic numbers'), so a successful return from this method is not a guarantee that the file
 * will be successfully parsed.
 * Creation date: (2001-04-12 15:37:28)
 * @return True iff this file is of a format that can be handled.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
 /* However, you can't define static methods in an interface (why???). So I'll just humbly request
 that all implementors of this interface also implements canHandle() for consistency. :-) 
public static boolean canHandle(File fontFile);
*/

/**
 * Return the data (mostly metrics) of this font face.
 * Creation date: (2001-04-12 15:45:31)
 * @return A class containing the (metrics) data of this font face.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public com.bluebrim.font.shared.metrics.CoFontMetricsData getMetricsData();

/**
 * Get the postscript representation of the font file. 
 * Creation date: (2001-04-12 15:37:28)
 * @return A class containing the font data of this font needed by the postscript generation.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public com.bluebrim.font.shared.CoFontPostscriptData getPostscriptData();

/**
 * Get all warning messages that was generated during the parsing of the font file.
 * Creation date: (2001-04-12 15:39:24)
 * @return A possibly multi-line string containing all warning messages. May be null or the empty string if no warning messages was generated.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public CoMessageLogger getWarningLog();

/**
 * Return the font version string. Each and every font, being a computer program for drawing glyphs,
 * may of course come in updated versions. The version string is a way for the font designer to
 * separate different versions of the same type face.
 * Creation date: (2001-04-12 16:13:49)
 * @return A string describing the font version.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public String getVersion();

/**
 * Return a suggested AwtData object for this font file. Due to limits in AWT, this is not directly
 * usable without checking that the font displays correctly with these attributes. However, this is a
 * reasonable default to start with.
 * Creation date: (2001-04-12 16:15:43)
 * @return A suggested AwtData attribute holder for this font face.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public com.bluebrim.font.shared.CoFontAwtData suggestedAwtData();

/**
 * Return a suggested FontFaceSpec for this font file. Connecting a FontFaceSpec to a font file is
 * really a desicion that has to be done by the user, as it contains arbitrariness and domain specific
 * typographic knowledge. However, the font contains information that can be used to suggest a 
 * reasonable default.
 * Creation date: (2001-04-12 16:15:43)
 * @return A suggested font face spec for this font face.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public com.bluebrim.font.shared.CoFontFaceSpec suggestedFontFaceSpec();
}