package com.bluebrim.font.impl.server.type1;
import java.io.*;
import java.util.*;

import com.bluebrim.font.impl.server.util.*;
import com.bluebrim.font.impl.shared.*;

/**
 * Read and parse Type1 font information and metrics from an AFM (Adobe Font Metrics) file.
 * Creation date: (2001-04-03 12:21:09)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */

public class CoType1FileInfoExtractor extends CoAbstractFontFileInfoExtractor {
	
	private CoType1AfmFileParser m_afmFile;
	private CoType1PfbFileParser m_pfbFile;
	private CoMessageLogger m_warningLog = new CoMessageLogger();
	private CoFontFileContainer m_fileContainer;
	
	private static HashMap weightNameMapping = new HashMap();

	static {
		weightNameMapping.put("THIN", com.bluebrim.font.shared.CoFontAttribute.W100);
		weightNameMapping.put("EXTRALIGHT", com.bluebrim.font.shared.CoFontAttribute.W200);
		weightNameMapping.put("ULTRALIGHT", com.bluebrim.font.shared.CoFontAttribute.W200);
		weightNameMapping.put("LIGHT", com.bluebrim.font.shared.CoFontAttribute.W300);
		weightNameMapping.put("MEDIUM", com.bluebrim.font.shared.CoFontAttribute.W500);
		weightNameMapping.put("SEMIBOLD", com.bluebrim.font.shared.CoFontAttribute.W600);
		weightNameMapping.put("DEMIBOLD", com.bluebrim.font.shared.CoFontAttribute.W600);
		weightNameMapping.put("BOLD", com.bluebrim.font.shared.CoFontAttribute.W700);
		weightNameMapping.put("ULTRABOLD", com.bluebrim.font.shared.CoFontAttribute.W800);
		weightNameMapping.put("EXTRABOLD", com.bluebrim.font.shared.CoFontAttribute.W800);
		weightNameMapping.put("BLACK", com.bluebrim.font.shared.CoFontAttribute.W900);
		weightNameMapping.put("HEAVY", com.bluebrim.font.shared.CoFontAttribute.W900);
	}









protected com.bluebrim.font.shared.CoFontAttribute suggestedCSSStyle(){
	String fontName = m_afmFile.getFontName();
	
	if (fontName.indexOf("Italic") != -1) {
		return com.bluebrim.font.shared.CoFontAttribute.ITALIC;
	} else if (fontName.indexOf("Oblique") != -1) {
		return com.bluebrim.font.shared.CoFontAttribute.ITALIC;
	} else if (fontName.indexOf("Slanted") != -1) {
		return com.bluebrim.font.shared.CoFontAttribute.ITALIC;
	} else {
		return com.bluebrim.font.shared.CoFontAttribute.ROMAN;
	}
}

protected com.bluebrim.font.shared.CoFontAttribute suggestedCSSVariant(){
	String fontName = m_afmFile.getFontName();
	
	if (fontName.indexOf("SC") != -1) {
		return com.bluebrim.font.shared.CoFontAttribute.SMALL_CAPS;
	} else if (fontName.indexOf("SmallCaps") != -1) {
		return com.bluebrim.font.shared.CoFontAttribute.SMALL_CAPS;
	} else if (fontName.indexOf("Small Caps") != -1) {
		return com.bluebrim.font.shared.CoFontAttribute.SMALL_CAPS;
	} else {
		return com.bluebrim.font.shared.CoFontAttribute.NORMAL_VARIANT;
	}
}

protected com.bluebrim.font.shared.CoFontAttribute suggestedCSSWeight(){
	String weightName = normalizeWeightDescription(m_afmFile.getWeightName());
	com.bluebrim.font.shared.CoFontAttribute attr = (com.bluebrim.font.shared.CoFontAttribute)weightNameMapping.get(weightName);
	if (attr != null) {
		return attr;
	} else {
		return com.bluebrim.font.shared.CoFontAttribute.NORMAL_WEIGHT;
	}
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
	return CoType1PfbFileParser.canHandle(fontFile);
}

/**
 * Get the postscript representation of the font file. This is postscript source code defining the
 * complete font, ready to be inserted in a postscript stream.
 * Creation date: (2001-04-20 14:36:20)
 * @return A byte array containing postscript source in ASCII.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public byte[] getPostscriptFontDefinition() {
	return m_pfbFile.getPostscriptFontDefinition();
}

protected static File guessAfmFileName(File fontFile) {
	File afmFile;
	String fileName = fontFile.toString();
	
	int i = fileName.lastIndexOf('.');
	if (i == -1) {
		afmFile = new File(fileName + ".afm"); // no extension found, try just adding .afm
	} else {
		fileName = fileName.substring(0, i) + ".afm";  // replace extension with .afm
		afmFile = new File(fileName);
		if (!afmFile.exists()) {		// not found, try looking in subdirs
			File tryFile = new File(afmFile.getParent() + File.separator + "afm" + File.separator + afmFile.getName());
			if (tryFile.exists()) {
				afmFile = tryFile;
			} else {
				// this is our last try. If this doesn't work, just return a File pointing to a non-existing file
				afmFile = new File(afmFile.getParent() + File.separator + "pfm" + File.separator + afmFile.getName());
			}
		}
	}
	
	return afmFile;
}

private String normalizeWeightDescription(String weight) {
	StringBuffer dst = new StringBuffer();
	char[] src = weight.toCharArray();

	for (int i=0; i < src.length; i++) {
		if (!Character.isWhitespace(src[i]) && src[i] != '-' && src[i] != '_') {
			dst.append(Character.toUpperCase(src[i]));
		}
	}
	return dst.toString();
}

/**
 * Takes a font definition file (PFA/PFB), and tries to guess the name of the corresponding 
 * font metrics file (AFM). If the AFM file is found, both is read and parsed, otherwise an
 * exception is thrown. 
 * Creation date: (2001-04-19 10:17:00)
 * @param fontFile The File pointing to the font definition file (PFA/PFB).
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public CoType1FileInfoExtractor(File fontFile) throws com.bluebrim.font.shared.CoFontException {
	this(fontFile, guessAfmFileName(fontFile));
}

/**
 * Read and parse a afmFile, and store warning messages in the local log stream, and read the
 * corresponding font definition file (PFA/PFB) for postscript generation.
 * Creation date: (2001-04-19 10:17:00)
 * @param afmFile The File pointing to the AFM file.
 * @param fontFile The File pointing to the font definition file (PFA/PFB).
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public CoType1FileInfoExtractor(File fontFile, File afmFile) throws com.bluebrim.font.shared.CoFontException {
	FileInputStream fontStream = null;
	FileInputStream afmStream = null;

	try {
		fontStream = new FileInputStream(fontFile);
	} catch (IOException e) {
		throw new com.bluebrim.font.shared.CoFontException("Can't find or open font definition file: " + fontFile, e);
	}

	try {
		afmStream = new FileInputStream(afmFile);
	} catch (IOException e) {
		throw new com.bluebrim.font.shared.CoFontException("Can't find or open font metrics file: " + afmFile, e);
	}

	m_afmFile = new CoType1AfmFileParser(m_warningLog, afmStream);
	m_pfbFile = new CoType1PfbFileParser(fontStream);
	m_fileContainer = new CoType1FileContainer(fontFile, afmFile);

	try {
		fontStream.close();
		afmStream.close();
	} catch (IOException ignored) {
	}
}


/**
 * getFileContainer method comment.
 */
public CoFontFileContainer getFileContainer() {
	return m_fileContainer;
}

/**
 * getFontFamilyName method comment.
 */
protected String getFontFamilyName() {
	return m_afmFile.getFontFamilyName();
}

/**
 * getHorizontalMetrics method comment.
 */
protected com.bluebrim.font.shared.metrics.CoHorizontalMetrics getHorizontalMetrics() {
	return m_afmFile.getHorizontalMetrics();
}

/**
 * getLineMetrics method comment.
 */
protected com.bluebrim.font.shared.metrics.CoLineMetrics getLineMetrics() {
	return m_afmFile.getLineMetrics();
}

/**
 * getItalicAngle method comment.
 */
protected com.bluebrim.font.shared.metrics.CoPairKerningMetrics getPairKerningMetrics() {
	return m_afmFile.getPairKerningMetrics();
}

/**
 * getPostscriptName method comment.
 */
protected String getPostscriptName() {
	return m_afmFile.getPostscriptName();
}

/**
 * getTrackingMetrics method comment.
 */
protected com.bluebrim.font.shared.metrics.CoTrackingMetrics getTrackingMetrics() {
	return m_afmFile.getTrackingMetrics();
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
	return m_afmFile.getVersion();
}

/**
 * Get all warning messages that was generated during the parsing of the font file.
 * Creation date: (2001-04-27 17:23:28)
 * @return A possibly multi-line string containing all warning messages. May be null or the empty string if no warning messages was generated.
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public CoMessageLogger getWarningLog() {
	return m_warningLog;
}



/**
 * getItalicAngle method comment.
 */
protected float getItalicAngle() {
	return m_afmFile.getItalicAngle();
}


/**
 * <no description given yet>
 * Creation date: (2001-04-27 17:45:34)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 * 
 * @return java.lang.String
 */
protected String getFontName() {
	return m_afmFile.getFontName();
}


protected com.bluebrim.font.shared.CoFontAttribute suggestedCSSStretch(){
	String fontName = m_afmFile.getFontName();
	
	if (fontName.indexOf("Condensed") != -1) {
		return com.bluebrim.font.shared.CoFontAttribute.CONDENSED;
	} else if (fontName.indexOf("Expanded") != -1) {
		return com.bluebrim.font.shared.CoFontAttribute.EXPANDED;
	} else {
		return com.bluebrim.font.shared.CoFontAttribute.NORMAL_STRETCH;
	}
}
}