package com.bluebrim.font.impl.server.type1;
import java.io.*;
import java.util.*;

import com.bluebrim.font.impl.server.util.*;
import com.bluebrim.font.impl.shared.metrics.*;
import com.bluebrim.postscript.shared.*;

/**
 * Read and parse Type1 font information and metrics from an AFM (Adobe Font Metrics) file.
 * Creation date: (2001-04-03 12:21:09)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */

class CoType1AfmFileParser {
	private PrintWriter m_warnings;
	
	private CoHorizontalMetricsImplementation m_horizontalMetrics;
	private com.bluebrim.font.shared.metrics.CoLineMetrics m_lineMetrics;
	private CoPairKerningMetricsImplementation m_pairKerning;
	private com.bluebrim.font.shared.metrics.CoTrackingMetrics m_trackingMetrics;

	private float m_italicAngle = 0.0f;  // slant of italic font, for caret adjustment etc
	private String m_version;  // font file version string

	private String m_weight;
	private String m_fontName;

	private String m_postscriptName;

	private String m_familyName;
	
	private static final float TYPE1_SCALE_FACTOR = 1.0f / 1000.0f;
	private static final float UNDEFINED_VALUE = -10000.0f; // ugly, i know





protected void parseCharMetrics(LineNumberReader in, int numEntries) throws com.bluebrim.font.shared.CoFontException {
	m_horizontalMetrics = new CoHorizontalMetricsImplementation();
	int actualEntries = 0;
	try {
		while (in.ready()) { // loop through lines
			String line = in.readLine();
			StringTokenizer tok = new StringTokenizer(line);
			actualEntries++;
			String width = null;
			String name = null;

			while (tok.hasMoreTokens()) {  // loop through tokens on this line
				String keyword = tok.nextToken();

/* Some notes about parsing the metrics format. The Adobe Font Metrics specification
(Adobe tech note 5004) is not especially clear at this point. I make this interpretation:
  - WX info has precedence before W0X info. W1X info is, i guess, for backwards writing (i.e.
hebrew) and is not considered.
  - N field is not formally required, but a line with C == -1 and no N field would be pointless,
 so I rely on N solely.
 FIXME: the correct way would of course be to first rely on C, looking it up using the default
 encoding for this font.
  - No other fields are interpreted by Calvin at the moment. */
				
				if ("EndCharMetrics".equals(keyword)) {
					return;		// end of char metrics data
				} else if ("WX".equals(keyword)) {
					width = tok.nextToken();
				} else if ("W0X".equals(keyword)) {
					if (width == null) { // only if not already set by WX
						width = tok.nextToken();
					}
				} else if ("N".equals(keyword)) {
					name = tok.nextToken();
				}

				// now clear to next semicolon
				while (tok.hasMoreTokens() && !tok.nextToken().equals(";")) {
					// do nothing, use nextToken's side effect
				}
			}  // while (tok.hasMoreTokens())
			if (name == null || width == null) {
				m_warnings.println("AFM parse warning: glyph name or horizontal width is not specified for glyph at line " + in.getLineNumber() + ".");
				m_warnings.println("Offending line: " + line);
			} else {
				setHorizontalAdvance(name, Integer.parseInt(width));
			}
					
		}  // while (in.ready())

	} catch (NumberFormatException e) {
		throw new com.bluebrim.font.shared.CoFontException("Syntax error in AFM-file (" + in + "). Malformed value at line " + in.getLineNumber() + ".", e);
	} catch (NoSuchElementException e) {
		throw new com.bluebrim.font.shared.CoFontException("Syntax error in AFM-file (" + in + "). Missing element(s) at line " + in.getLineNumber() + ".");
	} catch (IOException e) {
		throw new com.bluebrim.font.shared.CoFontException("Error reading from AFM-file (" + in + ") at line " + in.getLineNumber() + ".", e);
	}
	if (actualEntries != numEntries) {
		m_warnings.println("Inconsistency in AFM file: Actual number of CharMetrics (" + actualEntries + ") does not correspond with nominal value (" + numEntries + ").");
	}
}

/**
 * 
 */
protected void parseAfmFile(InputStream afmFile) throws com.bluebrim.font.shared.CoFontException {
	LineNumberReader in = new LineNumberReader(new InputStreamReader(afmFile));
	String line = null;
	// FIXME: what should I really do if these values are not provided in the AFM file?
	float ascender = UNDEFINED_VALUE;
	float descender = UNDEFINED_VALUE;
	float underlinePosition = UNDEFINED_VALUE;
	float underlineThickness = UNDEFINED_VALUE;
	float xHeight = UNDEFINED_VALUE;
	float capHeight = UNDEFINED_VALUE;
	
	try {
		line = in.readLine();
		StringTokenizer tok = new StringTokenizer(line);

		if (!"StartFontMetrics".equals(tok.nextToken())) {
			throw new com.bluebrim.font.shared.CoFontException("Not a correct AFM-file (No StartFontMetrics): " + afmFile);
		}

		while (in.ready()) {
			line = in.readLine();
			tok = new StringTokenizer(line);

			if (tok.hasMoreElements()) {
				String keyword = tok.nextToken();
				if ("StartCharMetrics".equals(keyword)) {
					parseCharMetrics(in, Integer.parseInt(tok.nextToken()));
				} else if ("StartKernData".equals(keyword)) {
					parseKernData(in);
				} else if ("FontName".equals(keyword)) {
					m_postscriptName = tok.nextToken("").trim(); 
					// nextToken("") sets delimiter set to empty, i.e. get rest of line
				} else if ("FullName".equals(keyword)) {
					m_fontName = tok.nextToken("").trim();
				} else if ("FamilyName".equals(keyword)) {
					m_familyName = tok.nextToken("").trim();
				} else if ("Weight".equals(keyword)) {
					m_weight = tok.nextToken("").trim();
				} else if ("Version".equals(keyword)) {
					m_version = tok.nextToken("").trim();
				} else if ("ItalicAngle".equals(keyword)) {
					m_italicAngle = Float.parseFloat(tok.nextToken());
// Global font metrics					
				} else if ("Ascender".equals(keyword)) {
					ascender = Float.parseFloat(tok.nextToken()) * TYPE1_SCALE_FACTOR;
				} else if ("Descender".equals(keyword)) {
					descender = Math.abs(Float.parseFloat(tok.nextToken()) * TYPE1_SCALE_FACTOR);
					// AFM stores descender as a negative value, but we want a distance
//FIXME: actually, Underline* could be encapsulated in a StartDirection/EndDirection block with
//a direction other than 0, in which case we really shouldn't just use it. No encapsulating
//block is equivalent to a block with direction 0.
				} else if ("UnderlinePosition".equals(keyword)) {
					underlinePosition = Float.parseFloat(tok.nextToken()) * TYPE1_SCALE_FACTOR;
				} else if ("UnderlineThickness".equals(keyword)) {
					underlineThickness = Float.parseFloat(tok.nextToken()) * TYPE1_SCALE_FACTOR;
				} else if ("XHeight".equals(keyword)) {
					xHeight = Float.parseFloat(tok.nextToken()) * TYPE1_SCALE_FACTOR;
				} else if ("CapHeight".equals(keyword)) {
					capHeight = Float.parseFloat(tok.nextToken()) * TYPE1_SCALE_FACTOR;
				}
				// otherwise, we just ignore the line
			}
		}

		if (m_postscriptName == null) {
			throw new com.bluebrim.font.shared.CoFontException("Not a correct AFM-file (No FontName): " + afmFile);
		}
	} catch (NumberFormatException e) {
		throw new com.bluebrim.font.shared.CoFontException("Syntax error in AFM-file (" + afmFile + "). Malformed value at line " + in.getLineNumber() + ": " + line, e);
	} catch (NoSuchElementException e) {
		throw new com.bluebrim.font.shared.CoFontException("Syntax error in AFM-file (" + afmFile + "). Missing element(s) at line " + in.getLineNumber() + ": " + line);
	} catch (IOException e) {
		throw new com.bluebrim.font.shared.CoFontException("Error reading from AFM-file (" + afmFile + ") at line " + in.getLineNumber() + ".", e);
	} finally {
		try {
			in.close();
		} catch (IOException ignored) { }
	}

	// check if optional (but essential) elements is missing
	if (ascender == UNDEFINED_VALUE) {
		m_warnings.println("Warning: No Ascender value given in file. Defaulting to 0.");
		ascender = 0.0f;
	} 
	if (descender == UNDEFINED_VALUE) {
		m_warnings.println("Warning: No Descender value given in file. Defaulting to 0.");
		descender = 0.0f;
	}
	if (underlinePosition == UNDEFINED_VALUE) {
		m_warnings.println("Warning: No UnderlinePosition value given in file. Defaulting to 0.");
		underlinePosition = 0.0f;
	}
	if (underlineThickness == UNDEFINED_VALUE) {
		m_warnings.println("Warning: No UnderlineThickness value given in file. Defaulting to 0.");
		underlineThickness = 0.0f;
	}
	if (capHeight == UNDEFINED_VALUE) {
		m_warnings.println("Warning: No CapHeight value given in file. Defaulting to Ascender, " + ascender);
		capHeight = ascender;
	}
	if (xHeight == UNDEFINED_VALUE) {
		m_warnings.println("Warning: No XHeight value given in file. Defaulting to CapHeight, " + ascender);
		xHeight = capHeight;
	}
	if (m_fontName == null) {
		m_warnings.println("Warning: No FullName was given in file. Defaulting to the postscript name, " + m_postscriptName);
		m_fontName = m_postscriptName;
	}
	if (m_familyName == null) {
		m_familyName = new StringTokenizer(m_postscriptName, "-").nextToken();
		m_warnings.println("Warning: No FamilyName was given in file. Defaulting to the first part of postscript name, " + m_familyName);
	}
	if (m_weight == null) {
		m_warnings.println("Warning: No Weight given in file. Defaulting to normal weight.");
		m_weight = "";
	}
	if (m_version == null) {
		m_warnings.println("Warning: No Version given in file. Defaulting to '1.0'.");
		m_version = "1.0";
	}

	/* Note about unavailable metrics: these metrics is not generally available in the AFM file.
	However, it is legal to define user-specific parameters and enter in the AFM file, as long as
	the keyword does not begin with an uppercase letter. This might be an idea for future possibilities
	to add this info to font files. For now, just produce reasonable defaults. */
	
	float leading = 0; // spacing between lines
	float strikethroughThickness = underlineThickness;  // assume same as underline thickness
	float strikethroughPosition = xHeight / 2;   // place strikethrough at middle of x-height

	float height = ascender+descender+leading; // per definition according to AWT.

	m_lineMetrics = new CoLineMetricsImplementation(ascender, descender, height, leading,
		strikethroughPosition, strikethroughThickness, underlinePosition, underlineThickness,
		xHeight, capHeight );
	
	// System.out.println("AFM file data: FontName: " + m_fontName + ", FullName: " + m_fullName + 
	//	", FamilyName: " + m_familyName + ", Weight: " + m_weight);

}

protected void parseKernData(LineNumberReader in) throws com.bluebrim.font.shared.CoFontException {
	String line = null;
	
	try {
		while (in.ready()) { // loop through lines
			line = in.readLine();
			StringTokenizer tok = new StringTokenizer(line);

			if (tok.hasMoreTokens()) {
				String keyword = tok.nextToken();

				if ("EndKernData".equals(keyword)) {
					return;		// end of kern data
				} else if ("StartKernPairs".equals(keyword)) {
					parseKernPairs(in, Integer.parseInt(tok.nextToken()));
				} else if ("StartKernPairs0".equals(keyword)) {
					parseKernPairs(in, Integer.parseInt(tok.nextToken()));
				}
				// FIXME: handle KernTracking data too

			}
					
		}  // while (in.ready())

	} catch (NumberFormatException e) {
		throw new com.bluebrim.font.shared.CoFontException("Syntax error in AFM-file (" + in + "). Malformed value at line " + in.getLineNumber() + ": " + line, e);
	} catch (NoSuchElementException e) {
		throw new com.bluebrim.font.shared.CoFontException("Syntax error in AFM-file (" + in + "). Missing element(s) at line " + in.getLineNumber() + ".");
	} catch (IOException e) {
		throw new com.bluebrim.font.shared.CoFontException("Error reading from AFM-file (" + in + ") at line " + in.getLineNumber() + ".", e);
	}
}

protected void parseKernPairs(LineNumberReader in, int numPairs) throws com.bluebrim.font.shared.CoFontException {
	m_pairKerning = new CoPairKerningMetricsImplementation();
	int actualPairs = 0;
	try {
		while (in.ready()) { // loop through lines
			String line = in.readLine();
			StringTokenizer tok = new StringTokenizer(line);
			actualPairs++;
			
			String glyph1 = null;
			String glyph2 = null;
			String kernWidth = null;

			if (tok.hasMoreTokens()) {
				String keyword = tok.nextToken();
				
				if ("EndKernPairs".equals(keyword)) {
					return;		// end of kern pair data
				} else if ("KPX".equals(keyword)) { // horizontal kerning
					glyph1 = tok.nextToken();
					glyph2 = tok.nextToken();
					kernWidth = tok.nextToken();
				} else if ("KP".equals(keyword)) { // 2D kerning, ignore y (fourth parameter)
					glyph1 = tok.nextToken();
					glyph2 = tok.nextToken();
					kernWidth = tok.nextToken();
				}

			}
			if (glyph1 != null) {
				setKernPair(glyph1, glyph2, Integer.parseInt(kernWidth));
			}
					
		}  // while (in.ready())

	} catch (NumberFormatException e) {
		throw new com.bluebrim.font.shared.CoFontException("Syntax error in AFM-file (" + in + "). Malformed value at line " + in.getLineNumber() + ".", e);
	} catch (NoSuchElementException e) {
		throw new com.bluebrim.font.shared.CoFontException("Syntax error in AFM-file (" + in + "). Missing element(s) at line " + in.getLineNumber() + ".");
	} catch (IOException e) {
		throw new com.bluebrim.font.shared.CoFontException("Error reading from AFM-file (" + in + ") at line " + in.getLineNumber() + ".", e);
	}
	if (actualPairs != numPairs) {
		m_warnings.println("Inconsistency in AFM file: Actual number of KernPairs (" + actualPairs + ") does not correspond with nominal value (" + numPairs + ").");
	}
}

protected void setHorizontalAdvance(String name, int width) {
	char ch = CoPostscriptGlyphNames.glyphNameToChar(name);

	if (ch != '\u0000') { // FIXME: this error signalling method ought to change
		m_horizontalMetrics.setAdvance(ch, (float)(width * TYPE1_SCALE_FACTOR));

		/* add kerning for possible alternative also */
		if (CoPostscriptGlyphNames.glyphNameHasAlternative(name)) {
			char alt_ch = CoPostscriptGlyphNames.glyphNameToAlternativeChar(name);
			m_horizontalMetrics.setAdvance(alt_ch, (float)(width * TYPE1_SCALE_FACTOR));
		}
	} else {
		m_warnings.println("AFM warning: Glyph name with no know unicode value encountered in char metrics: " + name);
	}

//	System.out.println("Horizontal Advance: " + name + ": " + width);
}

protected void setKernPair(String glyph1, String glyph2, int kernWidth) {
	char ch1 = CoPostscriptGlyphNames.glyphNameToChar(glyph1);
	char ch2 = CoPostscriptGlyphNames.glyphNameToChar(glyph2);

	if (ch1 != '\u0000' && ch2 != '\u0000') { // FIXME: this error signalling method ought to change
		m_pairKerning.setPairKerning(ch1, ch2, kernWidth * TYPE1_SCALE_FACTOR);

		/* add kerning for possible alternatives also */
		if (CoPostscriptGlyphNames.glyphNameHasAlternative(glyph1)) {
			char alt_ch1 = CoPostscriptGlyphNames.glyphNameToAlternativeChar(glyph1);
			m_pairKerning.setPairKerning(alt_ch1, ch2, kernWidth * TYPE1_SCALE_FACTOR);
			if (CoPostscriptGlyphNames.glyphNameHasAlternative(glyph2)) {
				char alt_ch2 = CoPostscriptGlyphNames.glyphNameToAlternativeChar(glyph2);
				m_pairKerning.setPairKerning(alt_ch1, alt_ch2, kernWidth * TYPE1_SCALE_FACTOR);
			}
		} else if (CoPostscriptGlyphNames.glyphNameHasAlternative(glyph2)) {
			char alt_ch2 = CoPostscriptGlyphNames.glyphNameToAlternativeChar(glyph2);
			m_pairKerning.setPairKerning(ch1, alt_ch2, kernWidth * TYPE1_SCALE_FACTOR);
		}
			
	} else {
		m_warnings.println("AFM warning: Glyph name(s) with no know unicode value encountered in kern pair: " + glyph1 + ", " + glyph2);
	}

	//	System.out.println("Kern pair: " + glyph1 + " & " + glyph2 + ": " + kernWidth);
}

/**
 * getFontFamilyName method comment.
 */
public String getFontFamilyName() {
	return m_familyName;
}

/**
 * getHorizontalMetrics method comment.
 */
public com.bluebrim.font.shared.metrics.CoHorizontalMetrics getHorizontalMetrics() {
	return m_horizontalMetrics;
}

/**
 * getLineMetrics method comment.
 */
public com.bluebrim.font.shared.metrics.CoLineMetrics getLineMetrics() {
	return m_lineMetrics;
}

/**
 * getItalicAngle method comment.
 */
public com.bluebrim.font.shared.metrics.CoPairKerningMetrics getPairKerningMetrics() {
	return m_pairKerning;
}

/**
 * getPostscriptName method comment.
 */
public String getPostscriptName() {
	return m_postscriptName;
}

public com.bluebrim.font.shared.metrics.CoTrackingMetrics getTrackingMetrics() {
	if (m_trackingMetrics == null) {
		// No tracking metrics found, so create an empty tracking metrics
		m_trackingMetrics = new CoTrackingMetricsImplementation(new float[0], new float[0]);
	}
		
	return m_trackingMetrics;
}

/**
 * Return the font version string. Each and every font, being a computer program for drawing glyphs,
 * may of course come in updated versions. The version string is a way for the font designer to
 * separate different versions of the same type face.
 * Creation date: (2001-04-27 16:45:59)
 * @return A string describing the font version.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public String getVersion() {
	return m_version;
}

/**
 * getHorizontalMetrics method comment.
 */
public CoType1AfmFileParser(CoMessageLogger logger, InputStream afmStream) throws com.bluebrim.font.shared.CoFontException {
	m_warnings = logger.getWriter();
	parseAfmFile(afmStream);
}

/**
 * <no description given yet>
 * Creation date: (2001-04-27 17:45:34)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 * 
 * @return java.lang.String
 */
public String getFontName() {
	return m_fontName;
}

public float getItalicAngle() {
	return m_italicAngle;
}



/**
 * Return the font version string. Each and every font, being a computer program for drawing glyphs,
 * may of course come in updated versions. The version string is a way for the font designer to
 * separate different versions of the same type face.
 * Creation date: (2001-04-27 16:45:59)
 * @return A string describing the font version.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public String getWeightName() {
	return m_weight;
}
}