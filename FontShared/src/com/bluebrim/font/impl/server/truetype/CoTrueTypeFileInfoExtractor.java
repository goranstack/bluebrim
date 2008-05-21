package com.bluebrim.font.impl.server.truetype;
import java.io.*;
import java.util.*;

import com.bluebrim.font.impl.server.util.*;
import com.bluebrim.font.impl.shared.*;
import com.bluebrim.font.impl.shared.metrics.*;

/**
 * Fetches data from a parsed TTF-file and presents it in a way that corresponds to the Calvin font
 * system. This is the "public" class that is to be used by anyone interested in installing a TTF font
 * in Calvin.
 * Creation date: (2001-05-18 14:57:02)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public class CoTrueTypeFileInfoExtractor extends CoAbstractFontFileInfoExtractor {
	private final static int NAME_FAMILY_NAME = 1;
	private final static int NAME_FONT_NAME = 4;
	private final static int NAME_VERSION = 5;
	private final static int NAME_POSTSCRIPT_NAME = 6;
	private final static int NAME_PREFERRED_FAMILY_NAME = 16;
	
	private CoMessageLogger m_warningLog;
	private CoTrueTypeFileContainer m_fileContainer;
	private CoTrueTypeFileParser m_fontFileParser;
	private CoHorizontalMetricsImplementation m_horizontalMetrics;
	private com.bluebrim.font.shared.metrics.CoLineMetrics m_lineMetrics;
	private CoPairKerningMetricsImplementation m_pairKerningMetrics;
	private com.bluebrim.font.shared.metrics.CoTrackingMetrics m_trackingMetrics;
	private byte[] m_postscriptFontDefinition;

/**
 * Get the family name of the font face.
 * Creation date: (2001-05-18 14:57:02)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
protected String getFontFamilyName() {
	// First try to find "preferred family name", which is a more common family name denominator, like
	// "Arial" for "Arial Narrow".
	String familyName = m_fontFileParser.getBestMatchingName(NAME_PREFERRED_FAMILY_NAME);

	if (familyName == null) {
		// If not found, fall back to traditional family name
		familyName = m_fontFileParser.getBestMatchingName(NAME_FAMILY_NAME);
	}
	return familyName;
}

/**
 * Get the font face name. This could be something like "Helvetica Bold Oblique", and is primarily used by the
 * AWT to identify the font. This will probably never be exposed directly to the user.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
protected String getFontName() {
	return m_fontFileParser.getBestMatchingName(NAME_FONT_NAME);
}


/**
 * Get the unscaled horizontal metrics of the font face.
 * Creation date: (2001-05-18 14:57:02)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
protected com.bluebrim.font.shared.metrics.CoHorizontalMetrics getHorizontalMetrics() {
	if (m_horizontalMetrics == null) {
		// Create storage for normalized (i.e. mapped on unicode) horizontal metrics
		m_horizontalMetrics = new CoHorizontalMetricsImplementation();

		// Get glyph->unicode mapping, and glyph->advance width mapping	
		Map glyphMapping = m_fontFileParser.getGlyphMapping();
		int[] glyphAdvances = m_fontFileParser.getGlyphAdvances();

		// The last specified advance value is valid for all glyphs with higher glyph index too
		// (especially useful for monospaced fonts)
		float defaultAdvance = (float)glyphAdvances[glyphAdvances.length-1] / m_fontFileParser.getUnitsPerEm();

		// Iterator through all glyphs
		Iterator i = glyphMapping.keySet().iterator();
		while (i.hasNext()) {
			Integer glyphIntWrapper = (Integer) i.next();
			int glyphIndex = glyphIntWrapper.intValue();
			
			Set mappedUnicodeChars = (Set) glyphMapping.get(glyphIntWrapper);

			// ... and through all unicode chars that use that glyph
			Iterator j = mappedUnicodeChars.iterator();
			while (j.hasNext()) {
				Character unicodeCharWrapper = (Character) j.next();
				char ch = unicodeCharWrapper.charValue();

				float advance = 0;
				
				if (glyphIndex < glyphAdvances.length) {
					// Glyph has unique advance in advance array
					advance = (float)glyphAdvances[glyphIndex] / m_fontFileParser.getUnitsPerEm(); // Normalize advance to unit width
				} else { 
					// Use default value
					advance = defaultAdvance;
				}
				
				m_horizontalMetrics.setAdvance(ch, advance);
			}
		}
	}
	
	return m_horizontalMetrics;
}


/**
 * Get the italic angle, i.e. the number of degrees the caret should be tilted to have the same slope as the
 * general slope of the font.
 * Creation date: (2001-05-18 14:57:02)
 * @return The tilt in degrees, 0 means a vertical caret, and negative values (most common) means clockwise turning.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
protected float getItalicAngle() {
	return (float)m_fontFileParser.getItalicAngle();
}


/**
 * Get the unscaled line metrics of the font face.
 * Creation date: (2001-05-18 14:57:02)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
protected com.bluebrim.font.shared.metrics.CoLineMetrics getLineMetrics() {
	if (m_lineMetrics == null) {
		float unitsPerEm = m_fontFileParser.getUnitsPerEm();

		float ascender = m_fontFileParser.getAscent() / unitsPerEm;
		float descender = m_fontFileParser.getDescent() / unitsPerEm;
		float leading = m_fontFileParser.getLinegap() / unitsPerEm;
		float height = ascender+descender+leading; // per definition according to AWT.

		// These values are not available in the TTF file. The best way to get really good
		// results would be to read the bounding box of "x" and "H" and use them as xHeight & capHeight
		// FIXME: replace this guess with the Real Way(tm).
		float xHeight = ascender * 0.6f;
		float capHeight = ascender;

		m_lineMetrics = new CoLineMetricsImplementation(ascender, descender, height, leading,
			(float) m_fontFileParser.getStrikeoutPosition() / unitsPerEm,
			(float) m_fontFileParser.getStrikeoutThickness() / unitsPerEm,
			(float) m_fontFileParser.getUnderlinePosition() / unitsPerEm,
			(float) m_fontFileParser.getUnderlineThickness() / unitsPerEm,
			xHeight, capHeight);
	}

	return m_lineMetrics;
}


/**
 * Get the pair kerning data for this font face.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
protected com.bluebrim.font.shared.metrics.CoPairKerningMetrics getPairKerningMetrics() {

	if (m_pairKerningMetrics == null) {

		int unitsPerEm = m_fontFileParser.getUnitsPerEm();

		// Get pair kerning arrays and glyph->unicode mapping
		short[] distances = m_fontFileParser.getInternalKernPairDistance();
		int[] leftGlyphs = m_fontFileParser.getInternalKernPairLeftChar();
		int[] rightGlyphs = m_fontFileParser.getInternalKernPairRightChar();
		Map glyphMapping = m_fontFileParser.getGlyphMapping();

		// Create storage for normalized (i.e. mapped on unicode) pair kerning metrics
		m_pairKerningMetrics = new CoPairKerningMetricsImplementation();

		if (distances == null)
			return m_pairKerningMetrics;
			
		// Iterate through all glyph pair kerning mappings
		for (int i = 0; i < distances.length; i++) {
			// For each left glyph, find all chars that this glyph maps to
			Set leftUnicodeChars = (Set) glyphMapping.get(new Integer(leftGlyphs[i]));

			// ... and iterate through all these unicode chars
			Iterator leftCharIterator = leftUnicodeChars.iterator();
			while (leftCharIterator.hasNext()) {
				Character unicodeCharWrapper = (Character) leftCharIterator.next();
				char leftChar = unicodeCharWrapper.charValue();

				// Now, iterate through all chars that the right glyph maps to

				Set rightUnicodeChars = (Set) glyphMapping.get(new Integer(rightGlyphs[i]));

				// ... and iterate through all these unicode chars too
				Iterator rightCharIterator = rightUnicodeChars.iterator();
				while (rightCharIterator.hasNext()) {
					unicodeCharWrapper = (Character) rightCharIterator.next();
					char rightChar = unicodeCharWrapper.charValue();

					// Now for every leftChar and rightChar pair, store the normalized kern value

					m_pairKerningMetrics.setPairKerning(leftChar, rightChar, (float) distances[i] / unitsPerEm);
				}
			}
		}
	}

	return m_pairKerningMetrics;
}


/**
 * Get the font definition as postscript code. This code, when sent to the postscript interpreter, should 
 * properly define this font face.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public byte[] getPostscriptFontDefinition() {
	return m_postscriptFontDefinition;
}


/**
 * Get the postscript name of this font. This should be a unique name, and the same as the name given to the
 * font that is created by the postscript code returned by getPostscriptFontDefinition.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
protected String getPostscriptName() {
	return m_fontFileParser.getBestMatchingName(NAME_POSTSCRIPT_NAME);
}


/**
 * Get the tracking metrics (a.k.a. track kerning metrics or tracking curve) for this font face. The tracking
 * metrics is a way of changing the advance width for glyph position depending on font size. Normally, you'd
 * want to put glyphs non-linearly closer together in larger sizes. The tracking metrics contains information
 * about how much closer the glyphs should be placed in different sizes.
 * The tracking metrics is measured by 1/200 points, and is counted in the positive x axis, so a value of
 * -50 for a specific size, means that the glyphs of the font face at that size should be positioned 1/4 point
 * closer together.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
protected com.bluebrim.font.shared.metrics.CoTrackingMetrics getTrackingMetrics() {
	if (m_trackingMetrics == null) {
		// We never have any tracking metrics in TTF files, so create an empty tracking metrics
		m_trackingMetrics = new CoTrackingMetricsImplementation(new float[0], new float[0]);
	}
		
	return m_trackingMetrics;
}


/**
 * Get all warning messages that was generated during the parsing of the font file.
 * Creation date: (2001-05-18 14:57:02)
 * @return A possibly multi-line string containing all warning messages. May be null or the empty string if no warning messages was generated.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public CoMessageLogger getWarningLog() {
	return m_warningLog;
}


/**
 * Return the font version string. Each and every font, being a computer program for drawing glyphs,
 * may of course come in updated versions. The version string is a way for the font designer to
 * separate different versions of the same type face.
 * Creation date: (2001-05-18 14:57:02)
 * @return A string describing the font version.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public String getVersion() {
	return m_fontFileParser.getBestMatchingName(NAME_VERSION);
}





/**
 * Return a reasonable default for the style of this font face.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
protected com.bluebrim.font.shared.CoFontAttribute suggestedCSSStyle() {
	boolean italic;

	italic = ((m_fontFileParser.getFsSelection() & 0x01) == 0x01); // font is italic if bit 0 is set
	if (italic) {
		return com.bluebrim.font.shared.CoFontAttribute.ITALIC;
	} else {
		return com.bluebrim.font.shared.CoFontAttribute.NORMAL_STYLE;
	}
}


/**
 * Return a reasonable default for the variant of this font face.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
protected com.bluebrim.font.shared.CoFontAttribute suggestedCSSVariant() {
	return com.bluebrim.font.shared.CoFontAttribute.NORMAL_VARIANT;
}


/**
 * Return a reasonable default for the weight of this font face.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
protected com.bluebrim.font.shared.CoFontAttribute suggestedCSSWeight() {
	int weight = m_fontFileParser.getWeightValue();

	if (weight <= 100) {
		return com.bluebrim.font.shared.CoFontAttribute.W100;
	} else if (weight <= 200) {
		return com.bluebrim.font.shared.CoFontAttribute.W200;
	} else if (weight <= 300) {
		return com.bluebrim.font.shared.CoFontAttribute.W300;
	} else if (weight <= 400) {
		return com.bluebrim.font.shared.CoFontAttribute.W400;
	} else if (weight <= 500) {
		return com.bluebrim.font.shared.CoFontAttribute.W500;
	} else if (weight <= 600) {
		return com.bluebrim.font.shared.CoFontAttribute.W600;
	} else if (weight <= 700) {
		return com.bluebrim.font.shared.CoFontAttribute.W700;
	} else if (weight <= 800) {
		return com.bluebrim.font.shared.CoFontAttribute.W800;
	} else {
		return com.bluebrim.font.shared.CoFontAttribute.W900;
	}
}


public CoTrueTypeFileInfoExtractor(File fontFile) throws com.bluebrim.font.shared.CoFontException {
	m_warningLog = new CoMessageLogger();

	FileInputStream fontStream = null;

	try {
		fontStream = new FileInputStream(fontFile);
	} catch (IOException e) {
		throw new com.bluebrim.font.shared.CoFontException("Can't find or open true type font file: " + fontFile, e);
	}

	m_fontFileParser = new CoTrueTypeFileParser(m_warningLog, fontStream);
	m_fileContainer = new CoTrueTypeFileContainer(fontFile);

	CoTrueTypePostscript postscriptGenerator = new CoTrueTypePostscript(m_fontFileParser, this, fontFile, m_warningLog.getWriter());
	m_postscriptFontDefinition = postscriptGenerator.getPostscriptDefinition();

	try {
		fontStream.close();
	} catch (IOException ignored) {	}
}


/**
 * Check if a file is in a format that this class can handle. This is just done by simple heuristics
 * (like 'magic numbers'), so a successful return from this method is not a guarantee that the file
 * will be successfully parsed.
 * Creation date: (2001-04-20 14:36:20)
 * @return True iff this file is of a format that can be handled.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public static boolean canHandle(File fontFile) {
	return CoTrueTypeFileParser.canHandle(fontFile);
}


/**
 * Return a class containing this font file, with the possibility to write it back to disk, possibly after
 * serialization.
 * Creation date: (2001-04-20 14:36:20)
 * @author Magnus Ihse (magnus.ihse@appeal.se) 
 */
public CoFontFileContainer getFileContainer() {
	return m_fileContainer;
}

/**
 * Return a reasonable default for the stretch value of this font face.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
protected com.bluebrim.font.shared.CoFontAttribute suggestedCSSStretch() {
	int stretch = m_fontFileParser.getWidthValue();

	if (stretch <= 1) {
		return com.bluebrim.font.shared.CoFontAttribute.S100;
	} else if (stretch <= 2) {
		return com.bluebrim.font.shared.CoFontAttribute.S200;
	} else if (stretch <= 3) {
		return com.bluebrim.font.shared.CoFontAttribute.S300;
	} else if (stretch <= 4) {
		return com.bluebrim.font.shared.CoFontAttribute.S400;
	} else if (stretch <= 5) {
		return com.bluebrim.font.shared.CoFontAttribute.S500;
	} else if (stretch <= 6) {
		return com.bluebrim.font.shared.CoFontAttribute.S600;
	} else if (stretch <= 7) {
		return com.bluebrim.font.shared.CoFontAttribute.S700;
	} else if (stretch <= 8) {
		return com.bluebrim.font.shared.CoFontAttribute.S800;
	} else {
		return com.bluebrim.font.shared.CoFontAttribute.S900;
	}
}
}