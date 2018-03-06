package com.bluebrim.font.impl.shared;
import java.io.*;

import com.bluebrim.font.impl.server.truetype.*;
import com.bluebrim.font.impl.server.type1.*;
import com.bluebrim.font.impl.shared.metrics.*;

/**
 * Abstract superclass for font file parsers (i.e. extracting font data from type1/truetype font files).
 * A font parser (FontFileInfoExtractor) is responsible for reading a font file (or files, depending on font format)
 * and extract all available information in the file that is needed by Calvin. This class also defines some
 * public non-abstract methods, which basically merges the data from the protected, abstract methods into bigger chunks
 * of data. Also provided is some methods for dispatching a font file of unknown format to the correct font file parser
 * for parsing, and checking whether the file is of any known font format.
 *
 * <p><b>Creation date:</b> 2001-04-06
 * <br><b>Documentation last updated:</b> 2001-09-18
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 * 
 * @see com.bluebrim.font.impl.server.type1.CoType1FileInfoExtractor
 * @see com.bluebrim.font.impl.server.truetype.CoTrueTypeFileInfoExtractor
 */
public abstract class CoAbstractFontFileInfoExtractor implements CoFontFileInfoExtractor {
/**
 * Default constructor, only to be called by subclasses.
 */
protected CoAbstractFontFileInfoExtractor() {
}


/**
 * Check if a file is in a format that any of the font handlers can handle. This is just done by simple heuristics
 * (like 'magic numbers'), so a successful return from this method is not a guarantee that the file
 * will be successfully parsed. At the moment, two different font formats is supported: TrueType and Type 1.
 *
 * @param fontFile the font file to check.
 *
 * @return true if this file is of a format that can be handled, false otherwise.
 *
 * @see com.bluebrim.font.impl.server.type1.CoType1FileInfoExtractor#canHandle(File)
 * @see com.bluebrim.font.impl.server.truetype.CoTrueTypeFileInfoExtractor#canHandle(File)
 */
public static boolean canHandleFontFile(File fontFile) {
	if (CoType1FileInfoExtractor.canHandle(fontFile) || 
		CoTrueTypeFileInfoExtractor.canHandle(fontFile)) {
		return true;
	} else {
		return false;
	}
}


/**
 * Returns the family name of the font face.
 *
 * @return the family name of the font face.
 */
protected abstract String getFontFamilyName();


/**
 * Returns the font face name. This could be something like "Helvetica Bold Oblique", and is primarily used by the
 * AWT to identify the font. This will probably never be exposed directly to the user.
 *
 * @return the font face name.
 */
protected abstract String getFontName();


/**
 * Returns the unscaled horizontal metrics of the font face.
 *
 * @return the unscaled horizontal metrics of the font face.
 */
protected abstract com.bluebrim.font.shared.metrics.CoHorizontalMetrics getHorizontalMetrics();


/**
 * Returns the italic angle, i.e. the number of degrees the caret should be tilted to have the same slope as the
 * general slope of the font. 0 means a vertical caret, and negative values (most common) means clockwise turning.
 *
 * @return the tilt in degrees.
 */
protected abstract float getItalicAngle();


/**
 * Returns the unscaled line metrics of the font face.
 *
 * @return the unscaled line metrics of the font face.
 */
protected abstract com.bluebrim.font.shared.metrics.CoLineMetrics getLineMetrics();


/**
 * Returns a class representing all metrics data available for this font face. This implementation combines the
 * abstract get*Metrics() methods and the getItalicAngle method in this class.
 *
 * @return a combination of all metrics for this font.
 *
 * @see #getHorizontalMetrics
 * @see #getLineMetrics
 * @see #getPairKerningMetrics
 * @see #getTrackingMetrics
 * @see #getItalicAngle
 */
public com.bluebrim.font.shared.metrics.CoFontMetricsData getMetricsData() {
	return new CoFontMetricsDataImplementation(getHorizontalMetrics(), getLineMetrics(), getPairKerningMetrics(), getTrackingMetrics(), getItalicAngle());
}


/**
 * Returns the pair kerning data for this font face.
 * 
 * @return the pair kerning data for this font face.
 */
protected abstract com.bluebrim.font.shared.metrics.CoPairKerningMetrics getPairKerningMetrics();


/**
 * Returns the postscript representation of the font file. This implementation combines the abstract getPostscript*()
 * methods in this class.
 *
 * @return a class containing the font data of this font needed by the postscript generation.
 *
 * @see #getPostscriptFontDefinition
 * @see #getPostscriptName
 */
public com.bluebrim.font.shared.CoFontPostscriptData getPostscriptData() {
	return new CoFontPostscriptDataImplementation(getPostscriptFontDefinition(), getPostscriptName());
}


/**
 * Returns the font definition as postscript code. This code, when sent to the postscript interpreter, should 
 * properly define this font face.
 *
 * @return a byte array consising of the postscript definition of this font.
 *
 * @see #getPostscriptName
 */
protected abstract byte[] getPostscriptFontDefinition();


/**
 * Returns the postscript name of this font. This should be a unique name, and the same as the name given to the
 * font that is created by the postscript code returned by getPostscriptFontDefinition.
 *
 * @return the postscript name of the font.
 *
 * @see #getPostscriptFontDefinition
 */
protected abstract String getPostscriptName();


/**
 * Returns the tracking metrics (a.k.a. track kerning metrics or tracking curve) for this font face. The tracking
 * metrics is a way of changing the advance width for glyph position depending on font size. Normally, you'd
 * want to put glyphs non-linearly closer together in larger sizes. The tracking metrics contains information
 * about how much closer the glyphs should be placed in different sizes.
 * The tracking metrics is measured by 1/200 points, and is counted in the positive x axis, so a value of
 * -50 for a specific size, means that the glyphs of the font face at that size should be positioned 1/4 point
 * closer together.
 *
 * @return the tracking metrics for the font face.
 */
protected abstract com.bluebrim.font.shared.metrics.CoTrackingMetrics getTrackingMetrics();


/**
 * Parses a font file for font information. If the specified file is of a format that any of the font parsers can
 * handle (currently TrueType and Type 1), the corresponding font parser (font file info extractor) is instantiated,
 * resulting in a complete parse of that font file.
 *
 * @param fontFile the font file to parse.
 *
 * @return a <code>CoFontFileInfoExtractor</code> which contains parsed information about the font file.
 *
 * @throws com.bluebrim.font.shared.CoFontException if the selected font parser can't successfully parse the font.
 *
 * @see com.bluebrim.font.impl.server.type1.CoType1FileInfoExtractor
 * @see com.bluebrim.font.impl.server.truetype.CoTrueTypeFileInfoExtractor
 */
public static CoFontFileInfoExtractor parseFontFile(File fontFile) throws com.bluebrim.font.shared.CoFontException {
	CoFontFileInfoExtractor font;

	if (CoType1FileInfoExtractor.canHandle(fontFile)) {
		font = new CoType1FileInfoExtractor(fontFile);
	} else if (CoTrueTypeFileInfoExtractor.canHandle(fontFile)) {
		font = new CoTrueTypeFileInfoExtractor(fontFile);
	} else {
		throw new com.bluebrim.font.shared.CoFontException("Unrecognized font file format in file " + fontFile.getPath());
	}
	return font;
}


/**
 * Returns a reasonable default for the AwtData associated with this font face. Note however that this by no
 * means is guaranteed to work correctly without modification. It should, however, be a good starting point
 * for the user to manipulate the AwtData from. The AwtData object is composed by the abstract methods
 * suggestedCSS*() in this class, and getFontName().
 *
 * @return a "guesstimate" of the needed AwtData for this font.
 */
public com.bluebrim.font.shared.CoFontAwtData suggestedAwtData() {
	boolean isBold = suggestedCSSWeight().getValue() >= com.bluebrim.font.shared.CoFontConstants.SEMIBOLD;
	boolean isItalic = suggestedCSSStyle().getValue() == com.bluebrim.font.shared.CoFontConstants.ITALIC;
	
	com.bluebrim.font.shared.CoFontAwtData data = new CoFontAwtDataImplementation(isBold, isItalic, getFontName());
	return data;
}


/**
 * Returns a reasonable default for the stretch value of this font face.
 *
 * @return a reasonable default for the stretch value of this font face.
 */
protected abstract com.bluebrim.font.shared.CoFontAttribute suggestedCSSStretch();


/**
 * Returns a reasonable default for the style of this font face.
 *
 * @return a reasonable default for the style of this font face.
 */
protected abstract com.bluebrim.font.shared.CoFontAttribute suggestedCSSStyle();


/**
 * Returns a reasonable default for the variant of this font face.
 *
 * @return a reasonable default for the variant of this font face.
 */
protected abstract com.bluebrim.font.shared.CoFontAttribute suggestedCSSVariant();


/**
 * Returns a reasonable default for the weight of this font face.
 *
 * @return a reasonable default for the weight of this font face.
 */
protected abstract com.bluebrim.font.shared.CoFontAttribute suggestedCSSWeight();


/**
 * Returns a reasonable default for a font face spec, describing this font. This spec must be checked by the user,
 * however, since it is not possible to derive a good spec automatically. A font face spec should specify the
 * typographic "feel" of a font, and this is not explicitly coded in the font face, in a way that is possible
 * to parse with 100% success. It is however possible to make a good guess.
 *
 * @return a "guesstimate" of a suitable <code>com.bluebrim.font.shared.CoFontFaceSpec</code> for this font face.
 *
 * @see #getFontFamilyName
 * @see #suggestedCSSWeight
 * @see #suggestedCSSStyle
 * @see #suggestedCSSVariant
 * @see #suggestedCSSStretch
 */
public com.bluebrim.font.shared.CoFontFaceSpec suggestedFontFaceSpec() {
	return new com.bluebrim.font.shared.CoFontFaceSpec(getFontFamilyName(), suggestedCSSWeight(), suggestedCSSStyle(), 
							  suggestedCSSVariant(), suggestedCSSStretch());
}
}