package com.bluebrim.font.impl.server.truetype;
import java.io.*;
import java.util.*;

import com.bluebrim.base.shared.*;

/**
 * Create a Type42 representation of the True Type font. Type 42 is a wrapper used in Postscript to
 * transfer True Type fonts to Postscript rasterizers capable of interpreting TT fonts. <p>
 *
 * Creation date: (2001-05-29 10:46:30)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public class CoTrueTypePostscript {
	private final static int BINARY_LINE_LENGTH = 32; // number of hex bytes per line for binary data
	
	private CoTrueTypeFileParser m_fontParser;
	private CoTrueTypeFileInfoExtractor m_infoExtractor;
	private PrintWriter m_warnings;
	
	private final static String TT_VERSION = "1.0";  // used in DCS header
	private StringBuffer m_psDefinition = new StringBuffer();
	private byte[] m_psByteArray;

public CoTrueTypePostscript(CoTrueTypeFileParser fontParser, CoTrueTypeFileInfoExtractor infoExtractor, File fontFile, PrintWriter warnings) throws com.bluebrim.font.shared.CoFontException {
	m_fontParser = fontParser;
	m_infoExtractor = infoExtractor;
	m_warnings = warnings;

	try {
		FileInputStream ttStream = new FileInputStream(fontFile);
		makeType42Definition(ttStream);
	} catch (FileNotFoundException ignored) {
		// This should never happen, since the file has been read from previously by the Parser
	}
}


public byte[] getPostscriptDefinition() {
	if (m_psByteArray == null) {
		try {
			m_psByteArray = m_psDefinition.toString().getBytes("ISO8859_1"); // actually, ASCII should do, but this seems more safe just in case
		} catch (UnsupportedEncodingException ignored) {
			// ISO8859_1 is supported in the JRE core... this should never happen...
		}
	}

	return m_psByteArray;
}


protected void makeGlyphMapping() {
	// Get glyph->unicode mapping
	Map glyphMapping = m_fontParser.getGlyphMapping();
	int numMappings = glyphMapping.values().size();

	// Print postscript CharStrings header	
	postscriptPrintln("/CharStrings " + (numMappings+1) + " dict dup begin");  // + 1 because of the .notdef

	postscriptPrintln(" /.notdef 0 def");

	// Iterate through all glyphs and generate mapping
	List glyphIndexList = new ArrayList(glyphMapping.keySet());
	
	// Sorting is not necessary but produces more comprehensible postscript code
	Collections.sort(glyphIndexList);
	Iterator i = glyphIndexList.iterator();
	while (i.hasNext()) {
		Integer glyphIntWrapper = (Integer) i.next();
		int glyphIndex = glyphIntWrapper.intValue();

		Set mappedUnicodeChars = (Set) glyphMapping.get(glyphIntWrapper);

		// ... and through all unicode chars that use that glyph
		Iterator j = mappedUnicodeChars.iterator();
		String previousGlyphName = null;
		while (j.hasNext()) {
			Character unicodeCharWrapper = (Character) j.next();

			// Get postscript name for this unicode char
			String glyphName = com.bluebrim.postscript.shared.CoPostscriptGlyphNames.charToGlyphName(unicodeCharWrapper.charValue());

			if (glyphName != null) {
				if (!glyphName.equals(previousGlyphName)) {		// Ignore duplicates
					postscriptPrintln(" /" + glyphName + " " + glyphIndex + " def");
				}
				previousGlyphName = glyphName;
			} else {
				m_warnings.println("Warning: Glyph " + glyphIndex + " maps to unicode with value " + (int)unicodeCharWrapper.charValue() + " which has no known postscript name");
			}

		}
	}

	// Print postscript CharString trailer
	postscriptPrintln("end readonly def");
}


protected void makeTrueTypeBinaryData(InputStream ttStream) throws com.bluebrim.font.shared.CoFontException {
	// Write binary data header
	postscriptPrint("/sfnts [");

	try {
		byte[] b;

		// read TTF header and table directory
		b = new byte[12 + m_fontParser.getNumTables() * CoTrueTypeTableEntry.LENGTH]; // size of ttf header + table dir
		ttStream.read(b);
		postscriptPrintHex(b);

		Iterator entries = m_fontParser.getTableDir().iterator();
		while (entries.hasNext()) {
			// We assume the file is correct. A corrupted file should have been discovered by the InfoExtractor.

			CoTrueTypeTableEntry entry = (CoTrueTypeTableEntry) entries.next();

			if (entry.getName().equals("glyf")) {
				// Glyf table need special attention. If it is larger than 64 kB, it should be split in multiple
				// strings. However, the split must occur at glyph definition boundaries.
				int[] glyphLocations = m_fontParser.getGlyphLocations();

				int previousSequenceStart = 0;

				for (int i = 0; i < glyphLocations.length - 1; i++) {
					if (glyphLocations[i + 1] - glyphLocations[previousSequenceStart] > 65535) {
						b = new byte[glyphLocations[i] - glyphLocations[previousSequenceStart]];
						ttStream.read(b);
						postscriptPrintHex(b);
						previousSequenceStart = i;
					}
				}
				// Write final part
				b = new byte[glyphLocations[glyphLocations.length - 1] - glyphLocations[previousSequenceStart]];
				ttStream.read(b);
				postscriptPrintHex(b);
			} else {
				// Normal table, just write everything
				// There could theoretically be a problem with tables > 64 kB, but the Type42 manual explicitly
				// says there's no way to handle that, so let's just hope it never happens :-)	
				b = new byte[(int) entry.get4BytePaddedLength()];
				ttStream.read(b);
				postscriptPrintHex(b);
			}
		}
	} catch (IOException e) {
		throw new com.bluebrim.font.shared.CoFontException("Error reading from TTF File (" + ttStream + ")", e);
	}

	// Write binary data trailer
	postscriptPrintln("] def");
}


protected void makeType42Definition(InputStream ttStream) throws com.bluebrim.font.shared.CoFontException {
	// DSC header
	postscriptPrintln("%!PS-TrueTypeFont-" + TT_VERSION + "-" + m_fontParser.getFontRevision());
	if (m_fontParser.getMaxMemoryType42() > 0) {
		postscriptPrintln("%%VMUsage: " + m_fontParser.getMinMemoryType42() + " " + m_fontParser.getMaxMemoryType42());
	}

	// Font definition header
	postscriptPrintln("11 dict begin");
	postscriptPrintln("/FontName /" + m_infoExtractor.getPostscriptName() + " def");
	postscriptPrintln("/FontBBox [" + m_fontParser.getBBoxXMin() + " " + + m_fontParser.getBBoxYMin() + " " 
				      + m_fontParser.getBBoxXMax() + " " + m_fontParser.getBBoxYMax() + "] def");

	// Static data for all Type42 fonts...
	postscriptPrintln("/FontType 42 def");
	postscriptPrintln("/Encoding StandardEncoding def");
	postscriptPrintln("/PaintType 0 def");
	postscriptPrintln("/FontMatrix [1 0 0 1 0 0] def");

	// Define postscript name -> glyph index mapping
	makeGlyphMapping();

	// Include truetype binary data
	makeTrueTypeBinaryData(ttStream);
	
	// Definition trailer	
	postscriptPrintln("FontName currentdict end definefont pop");
}

protected void postscriptPrint(String s) {
	m_psDefinition.append(s);
}


protected void postscriptPrintHex(byte[] b) {
	postscriptPrintHex(b, 0, b.length);
}


/**
 * Encodes binary data as hex codes, ie "FF" for the byte value 255. The generated hex code is
 * line wrapped after 80 characters.
 * Creation date: (2001-03-30 13:33:38)
 * @param adjustPos The number of chars already written, for wrapping purposes
 * @return The resulting adjustPos, for next call.
 */
protected void postscriptPrintHex(byte[] b, int startPos, int len) {
	postscriptPrintln("<");

	for (int i = 0; i < len; i++) {
		CoNativeDataUtil.appendHexByte(b[i + startPos], m_psDefinition);
		if ((i + 1) % BINARY_LINE_LENGTH == 0) {
			postscriptPrintln();
		}
	}

	postscriptPrint("00>");		// yes, there should be an extra 0 byte. Why? Ask Adobe...
}


protected void postscriptPrintln() {
	m_psDefinition.append('\n');
}


protected void postscriptPrintln(String s) {
	m_psDefinition.append(s);
	postscriptPrintln();
}
}