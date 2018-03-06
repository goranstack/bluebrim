package com.bluebrim.font.impl.server.truetype;
import java.io.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.font.impl.server.util.*;

/**
 * The core TTF parser. This class takes care of all the messy details of the TTF file, parsing it into some
 * kind of intermediary format which can be transformed into something more useful by the corresponding 
 * FileInfoExctractor class.
 * Creation date: (2001-04-06 11:06:00)
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public class CoTrueTypeFileParser {

/** Simple comparator class, used to compare the placement in the TTF file of tables. A table is
* "smaller" than another, if it if placed before the other in the file.
*/
protected class TableDirOffsetComparator implements Comparator {
	public int compare(Object o1, Object o2) {
		if (!(o1 instanceof CoTrueTypeTableEntry)) {
			throw new ClassCastException();
		}
		if (!(o2 instanceof CoTrueTypeTableEntry)) {
			throw new ClassCastException();
		}

		// return 0 if equal, negative if o is larger than this, or vice versa
		CoTrueTypeTableEntry entry1 = (CoTrueTypeTableEntry) o1;
		CoTrueTypeTableEntry entry2 = (CoTrueTypeTableEntry) o2;

		if (entry1.getOffset() == entry2.getOffset()) {
			return 0;
		}
		if (entry1.getOffset() < entry2.getOffset()) {
			return -1;
		} else {
			return 1;
		}
	}
};

	private int m_numTables;
	private List m_tableDir; // CoTrueTypeTableEntry[]
	private int m_unitsPerEm; // scaling factor for all measurements
	private final static byte[] TTF_VERSION = new byte[] { 0, 1, 0, 0 };
	private final static byte[] APPLE_TYPE = new byte[] { 0x74, 0x72, 0x75, 0x65 }; // "true"

	private final static Set REQUIRED_TABLES = new HashSet(Arrays.asList(new String[] { "cmap", "head", 
		"hmtx", "hhea", "maxp", "name", "post", "OS/2", "loca", "glyf" }));
	
	private int m_weightValue;
	private int m_widthValue;
	private int m_strikeoutThickness;
	private int m_strikeoutPosition;
	private int m_fsSelection;
	private int m_ascent;
	private int m_descent;
	private int m_linegap;
	private int m_numHMetrics;
	private double m_fontRevision;
	private int m_bBoxXMin;
	private int m_bBoxXMax;
	private int m_bBoxYMin;
	private int m_bBoxYMax;
	private int m_locaTableFormat;
	
	private int[] m_internalKernPairLeftChar;
	private int[] m_internalKernPairRightChar;
	private short[] m_internalKernPairDistance;

	private byte[] m_internalHmtxTable;
	private byte[] m_internalLocaTable;
	private int[] m_glyphAdvances;
	private int[] m_glyphLocations;

	private double m_italicAngle;

	private short m_underlinePosition;
	private short m_underlineThickness;
	private boolean m_isFixedPitch;
	private long m_minMemoryType42;
	private long m_maxMemoryType42;

	private CoTrueTypeNameRecord[] m_nameRecords;
	private byte[] m_nameData;

	private Map m_glyphMapping = new HashMap();  // [ Integer -> Set [ Character ] ] (glyph -> set of unicode chars)
//	private CoMessageLogger m_warningLog = new CoMessageLogger();
	private PrintWriter m_warnings;





protected void parseNameTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException {
	int format = CoNativeDataUtil.readUint16(ttStream);
	if (format != 0) {
		m_warnings.println("Warning: Name table format is not 0. Still trying to parse table, though.");
	}

	int nameRecordCount = CoNativeDataUtil.readUint16(ttStream);
	int nominalHeaderSize = CoNativeDataUtil.readUint16(ttStream); // not really needed
	m_nameRecords = new CoTrueTypeNameRecord[nameRecordCount];
	for (int i = 0; i < nameRecordCount; i++) {
		m_nameRecords[i] = new CoTrueTypeNameRecord(ttStream);
	}

	int filePos = 6 + nameRecordCount * 12;
	if (nominalHeaderSize != filePos) {
		m_warnings.println("Warning: Actual and specified start of name string data is not identical. (Actual: " + filePos + ", specified: " + nominalHeaderSize + "). Ignoring specified size.");
	}

	int nameDataSize = (int)entry.getLength() - filePos;
	m_nameData = new byte[nameDataSize];

	ttStream.read(m_nameData);

	// Now tell all namerecords where they can find the data area
	for (int i = 0; i < nameRecordCount; i++) {
		m_nameRecords[i].setNameDataArea(m_nameData);
	}
}

protected void parseHeadTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException, com.bluebrim.font.shared.CoFontException {
	assertTableLength(54, entry);
	
	/* Note about some fields:
		* version is not checked. it is assumed to be 1.0.
		* macStyle font info is ignored, and the fsSelection in the OS/2 table is used instead.
		Other fields are of no interest to us.
	*/
	ttStream.skip(4);
	m_fontRevision = CoNativeDataUtil.readFixed32(ttStream);
	ttStream.skip(10);
	m_unitsPerEm = CoNativeDataUtil.readUint16(ttStream);
	ttStream.skip(16);
	m_bBoxXMin = CoNativeDataUtil.readShort16(ttStream);
	m_bBoxYMin = CoNativeDataUtil.readShort16(ttStream);
	m_bBoxXMax = CoNativeDataUtil.readShort16(ttStream);
	m_bBoxYMax = CoNativeDataUtil.readShort16(ttStream);

	ttStream.skip(6);
	m_locaTableFormat = CoNativeDataUtil.readUint16(ttStream);
	ttStream.skip(2);
}

protected void parsePostTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException, com.bluebrim.font.shared.CoFontException {
	if (entry.getLength() < 24) {
		throw new com.bluebrim.font.shared.CoFontException("Error in TTF file: table " + entry.getName() + " is shorter than expected (" + entry.getLength() + " when it should be at least 24).");
	}
	
	ttStream.skip(4); // skip formatType
	m_italicAngle = CoNativeDataUtil.readFixed32(ttStream);

	m_underlinePosition = CoNativeDataUtil.readShort16(ttStream);
	m_underlineThickness = CoNativeDataUtil.readShort16(ttStream);
	m_isFixedPitch = (CoNativeDataUtil.readUint16(ttStream) != 0); // false iff 0
	ttStream.skip(2); // reserved value
	m_minMemoryType42 = CoNativeDataUtil.readUint32(ttStream);
	m_maxMemoryType42 = CoNativeDataUtil.readUint32(ttStream);
	int filePos = 24; // number of bytes we've read so far

	// Now we'll just skip the rest of the table, which includes mostly mapping for glyph indeces to
	// postscript names. However, we'll use our own mapping from unicode to postscript names instead, to
	// ensure consistency.
	
	ttStream.skip(entry.getLength() - filePos);
}

protected void parseMaxpTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException, com.bluebrim.font.shared.CoFontException {
	skipTable(ttStream, entry);
	
/* // Not needed, actually
	assertTableLength(32, entry);
		
 // note: version is ignored, and assumed to be 1.0. 
 	ttStream.skip(4);
 	m_numGlyphs = CoNativeDataUtil.readUint16(ttStream);
 	ttStream.skip(entry.getLength() - 6);		// skip to end of table
*/
}

protected void parseCmapTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException, com.bluebrim.font.shared.CoFontException {
	int version = CoNativeDataUtil.readUint16(ttStream);
	if (version > 1) {
		m_warnings.println("Warning: cmap table version number is " + version + ", and not 0 or 1 as expected.");
	}
	int subTableCount = CoNativeDataUtil.readUint16(ttStream);
	int[] platform = new int[subTableCount];
	int[] encoding = new int[subTableCount];
	long[] offset = new long[subTableCount];

	int encodingTablesStart = 4 + (8 * subTableCount);	// == length of headers
	int unicodeEncodingSubtable = -1;					// where is the unicode encoding?
	for (int i = 0; i < subTableCount; i++) {
		platform[i] = CoNativeDataUtil.readUint16(ttStream);
		encoding[i] = CoNativeDataUtil.readUint16(ttStream);
		offset[i] = CoNativeDataUtil.readUint32(ttStream) - encodingTablesStart; // normalize to start of encoding tables

		if (platform[i] == 3 && encoding[i] == 1) {
			// This is Microsoft platform with unicode encoding. If it exists, we do want to use it.
			unicodeEncodingSubtable = i;	// Save the position of this subtable
		}
	}

	if (unicodeEncodingSubtable == -1) {	// No unicode found
		// FIXME: a better solution would be to handle a fallback to i.e. a Mac format
		throw new com.bluebrim.font.shared.CoFontException("Unicode encoding not found in true type file. Can't map font.");
	}

	ttStream.skip(offset[unicodeEncodingSubtable]);		// jump to start of table
	long filePos = encodingTablesStart + offset[unicodeEncodingSubtable];

	int subTableFormat = CoNativeDataUtil.readUint16(ttStream);
	int subTableLength = CoNativeDataUtil.readUint16(ttStream);
	if (subTableFormat != 4) {
		// FIXME: should be able to handle other formats, like format 6
		throw new com.bluebrim.font.shared.CoFontException("Unicode encoding table in cmap is in unknown format (is: " + subTableFormat + ", should be: 4). Can't read font.");
	}

	readCmapSubTableFormat4(ttStream, subTableLength-4); // subTableLength-4 == length of remaining table
	filePos += subTableLength;

	ttStream.skip(entry.getLength() - filePos); // make sure we go to table end if not there
	checkActualTableLength(filePos, entry);		// check that we're not past table end
}

protected void parseHheaTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException, com.bluebrim.font.shared.CoFontException {
	assertTableLength(36, entry);
	
	/* notes about some fields:
		 version is not checked.
	     ascent, descent and lineGap is not used -- see os/2 table
	 */
	ttStream.skip(34);
	m_numHMetrics = CoNativeDataUtil.readUint16(ttStream);
	ttStream.skip(entry.getLength() - 36);		// skip to end of table
}

protected void parseHmtxTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException {
	// we can't be sure we've got the numHMetrics count, since it may reside in a table not yet read...
	// so we'll just have to read the whole table unparsed into memory, and parse it at a later time,
	// when all of the font file is guaranteed to have been read.
	m_internalHmtxTable = new byte[(int)entry.getLength()];
	ttStream.read(m_internalHmtxTable);
}

protected void parseOS_2Table(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException, com.bluebrim.font.shared.CoFontException {

	// assert table length.
	// can't use assertTableLength() due to different lengths in Microsoft's spec... *sigh*

	if (entry.getLength() > 86) {
		m_warnings.println("Warning: table " + entry.getName() + " is longer than expected (" + entry.getLength() + " instead of " + 86 + "). Ignoring additional information.");
	}
	if (entry.getLength() < 68) {
		throw new com.bluebrim.font.shared.CoFontException("Error in TTF file: table " + entry.getName() + " is shorter than expected (" + entry.getLength() + " instead of " + 68 + ").");
	}
	
	ttStream.skip(4);
	m_weightValue = CoNativeDataUtil.readUint16(ttStream);
	m_widthValue = CoNativeDataUtil.readUint16(ttStream);
	ttStream.skip(18);
	m_strikeoutThickness = CoNativeDataUtil.readUint16(ttStream);
	m_strikeoutPosition = CoNativeDataUtil.readUint16(ttStream);
	ttStream.skip(32);
	m_fsSelection = CoNativeDataUtil.readUint16(ttStream);
	ttStream.skip(4);
	long filePos = 68;
	// this is the basic 68 bytes that's always available, according to spec
	if (entry.getLength() >= 78) {
		m_ascent = CoNativeDataUtil.readUint16(ttStream);
		// Note! It seems as though the Descender is a negative two's complement number,
		// contrary to documentation!!! God how I hate Microsoft's so-called "documentation". :-(
		// update: only for some TTF fonts, appearantly... *sigh*... other are correct, or at least
		// has a positive number. My solution: the number is read as a signed value, and then
		// the absolute value is returned.
		m_descent = Math.abs(CoNativeDataUtil.readShort16(ttStream));
		m_linegap = CoNativeDataUtil.readUint16(ttStream);
		filePos += 6;
	} else {
		m_warnings.println("Warning: No sTypoAscent/Descent/LineGap information in OS/2 table.");
		// FIXME set default values
	}
	ttStream.skip((entry.getLength() - filePos)); // skip to end of table
}

protected void parseKernTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException, com.bluebrim.font.shared.CoFontException {
	int tableVersion = CoNativeDataUtil.readUint16(ttStream);
	int filePos = 2;

	if (tableVersion != 0) {
		ttStream.skip((entry.getLength() - filePos)); // skip to end of table
		m_warnings.println("Warning: Incorrect version number (only version 0 accepted) in kern table (pair kerning data).");
		return;
	}

	// Note! Apple's documentation of this table contains _major_ differences compared to Microsoft's.
	// For a start, the first field on the table (the version field) is 4 bytes, not 2 ... *sigh*
	// Luckily, Apple specifies that the version number should be 1, not 0.
	// FIXME: At the moment, we do not support Apple-style fonts, the kerning table is just ignored.

	int subTableCount = CoNativeDataUtil.readUint16(ttStream);
	filePos += 2;

	for (int i = 0; i < subTableCount; i++) {
		int subTableHeaderSize = 6; // size of this header
		int subTableVersion = CoNativeDataUtil.readUint16(ttStream);
		int subTableLength = CoNativeDataUtil.readUint16(ttStream) - subTableHeaderSize; // specified length includes header
		int coverage = CoNativeDataUtil.readUint16(ttStream);
		filePos += subTableHeaderSize;

		// We only check one special kind of table. Many more could be present, but is not likely to be
		// on Windows fonts. The kind of table we're looking for has a coverage value of 1 or 9, which is calculated
		// as follows:
		// horizontal (bit 0) = 1 -- horizontal metrics
		// minumum (bit 1) = 0 -- we want kerning data, not minumum data (what's that, anyway?)
		// cross-stream (bit 2) = 0 -- we want kerning in the same direction as the text flow
		// override (bit 3) = dont' care -- we ignore the accumulate/override distinction, and treat all tables as overriding
		// format (bits 8-15) = 0 -- format 0 is the only one used by Windows, so we'll stick to it

		if ((coverage == 1) || (coverage == 9)) { // okay, we got our table
			int kernPairCount = CoNativeDataUtil.readUint16(ttStream);
			ttStream.skip(6); // skip binary search help data
			m_internalKernPairLeftChar = new int[kernPairCount];
			m_internalKernPairRightChar = new int[kernPairCount];
			m_internalKernPairDistance = new short[kernPairCount];

			for (int j = 0; j < kernPairCount; j++) {
				m_internalKernPairLeftChar[j] = CoNativeDataUtil.readUint16(ttStream);
				m_internalKernPairRightChar[j] = CoNativeDataUtil.readUint16(ttStream);
				m_internalKernPairDistance[j] = CoNativeDataUtil.readShort16(ttStream);
			}
			if (subTableLength != (8 + (6 * kernPairCount))) {
				throw new com.bluebrim.font.shared.CoFontException("Pair kerning table subtable type 0 is of incorrect length");
			}
		} else {
			ttStream.skip(subTableLength);
		}
		filePos += subTableLength;
	}

	checkActualTableLength(filePos, entry);
	ttStream.skip(entry.getLength() - filePos); // make sure we're at the end of the table if there's any problem
}


protected void parseGlyfTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException {
	// just ignore
	skipTable(ttStream, entry);
}

protected void parseLocaTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException {
	// we can't be sure we've got the locaTableFormat specifier, since it may reside in a table not yet read...
	// so we'll just have to read the whole table unparsed into memory, and parse it at a later time,
	// when all of the font file is guaranteed to have been read.
	m_internalLocaTable = new byte[(int)entry.getLength()];
	ttStream.read(m_internalLocaTable);
}

protected void skipTable(InputStream ttStream, CoTrueTypeTableEntry entry) throws IOException {
	ttStream.skip(entry.getLength()); // just skip past this table
}

public boolean canHandleFile(InputStream file) {
	byte[] identifier = new byte[4];

	try {
		file.read(identifier, 0, 4);

		// check identifier
		if (Arrays.equals(identifier, TTF_VERSION) || Arrays.equals(identifier, APPLE_TYPE)) {
			return true;
		}
	} catch (IOException ignored) { }
	return false;
}

protected long readOffsetSubtable(InputStream ttStream) throws com.bluebrim.font.shared.CoFontException {
	byte[] identifier = new byte[4];
	byte[] buffer = new byte[2];

	try {
		ttStream.read(identifier, 0, 4);

		// check identifier
		if (!Arrays.equals(identifier, TTF_VERSION) && 
			!Arrays.equals(identifier, APPLE_TYPE)) {
			throw new com.bluebrim.font.shared.CoFontException("Not a TrueType file: " + ttStream);
		}

		m_numTables = CoNativeDataUtil.readUint16(ttStream);
		ttStream.skip(3 * 2); // skip three posts of 2 byte long data

	} catch (IOException e) {
		throw new com.bluebrim.font.shared.CoFontException("Error reading from TTF File (" + ttStream + ").", e);
	}
	
	return 12;	// number of bytes read or skipped
}

protected long readTableDirectory(InputStream ttStream) throws com.bluebrim.font.shared.CoFontException {
	m_tableDir = new ArrayList(m_numTables);

	for (int i = 0; i < m_numTables; i++) {
		CoTrueTypeTableEntry entry = new CoTrueTypeTableEntry(ttStream);
		m_tableDir.add(entry);
	}

	return (m_numTables * CoTrueTypeTableEntry.LENGTH);
}

/* we need the following tables:
cmap - mappa glyph-id till tecken (INFO)
head - allmän info (INFO)
hmtx - horisontalmetrik (INFO)
hhea - support för hmtx (INFO)
maxp - antal glypher (INFO)
name - glyphnamn för postscript (PS)
post - mer ps info (PS)
os/2 - ? mer metrikinfo? (INFO)
loca - postitioner i glyftabellen för uppdelning (PS)
glyf - glyftabellen, för uppdelning (PS)
som alla är obligatoriska.

kern - kerninginformation

type42-formatet vill ha följande tabeller, om de finns (övriga kommer att ignoreras och
kan med fördel hoppas över):
head, hhea, hmtx, maxp, loca, glyf, prep, fgpm, cvt_, samt vhea, vmtx om MetricsCount är 0, 2
eller inte angivet.
*/
protected void parseTTFile(InputStream ttStream) throws com.bluebrim.font.shared.CoFontException {
	Set requiredTablesLeft = new HashSet(REQUIRED_TABLES); // start with original list
	long filePos = 0;

	// read TTF header and table directory
	filePos += readOffsetSubtable(ttStream);
	filePos += readTableDirectory(ttStream);

	try {
		// sort directory table according to offset of referenced tables
		Collections.sort(m_tableDir, new TableDirOffsetComparator());

		Iterator entries = m_tableDir.iterator();
		while (entries.hasNext()) {
			CoTrueTypeTableEntry entry = (CoTrueTypeTableEntry) entries.next();

			if (entry.getOffset() < filePos) { // specified position is already passed - fatal!
				throw new com.bluebrim.font.shared.CoFontException("Invalid TTF file: table " + entry.getName() + " starts at overlapping address.");
			}

			// adjust for padding
			if (entry.getOffset() > filePos) {
				if (!((entry.getOffset() % 4 == 0) && (entry.getOffset() - filePos <= 3))) {
					// this is not normal padding to 4-byte boundary
					m_warnings.println(
						"Warning: TTF file contains strangely aligned tables. There is "
							+ (entry.getOffset() - filePos)
							+ " bytes of junk data before table "
							+ entry.getName());
				}
				// in any case, skip over extra bytes
				ttStream.skip(entry.getOffset() - filePos);
				filePos = entry.getOffset();
			}

			String name = entry.getName();
			requiredTablesLeft.remove(name); 

			if (name.equals("name")) {
				parseNameTable(ttStream, entry); // required table
			} else if (name.equals("head")) {
				parseHeadTable(ttStream, entry);
			} else if (name.equals("post")) { // required table
				parsePostTable(ttStream, entry);
			} else if (name.equals("maxp")) { // required table
				parseMaxpTable(ttStream, entry);
			} else if (name.equals("cmap")) { // required table
				parseCmapTable(ttStream, entry);
//	skipTable(ttStream, entry);
			} else if (name.equals("hhea")) { // required table
				parseHheaTable(ttStream, entry);
			} else if (name.equals("hmtx")) { // required table
				parseHmtxTable(ttStream, entry);
			} else if (name.equals("OS/2")) { // required table
				parseOS_2Table(ttStream, entry);
			} else if (name.equals("glyf")) { // required table
				parseGlyfTable(ttStream, entry);
			} else if (name.equals("loca")) { // required table
				parseLocaTable(ttStream, entry);
			} else if (name.equals("kern")) { // optional table
				parseKernTable(ttStream, entry);
			} else {
				skipTable(ttStream, entry); // other optional table
			}

			// update filePos, assuming all parse*Table() just read correct number of bytes
			filePos += entry.getLength();
		}

		if (ttStream.available() != 0) {
			m_warnings.println("Warning: TTF file contains data beyond last specified table.");
		}

		if (!requiredTablesLeft.isEmpty()) {
			throw new com.bluebrim.font.shared.CoFontException("Not all required tables was found in TTF file (" + ttStream + "). The following table(s) were missing: " +
				requiredTablesLeft);
		}

		// Second pass processing for some tables...
		handleHmtxTable();
		handleLocaTable();

	} catch (IOException e) {
		throw new com.bluebrim.font.shared.CoFontException("Error reading from TTF File (" + ttStream + ")", e);
	}
}


protected void assertTableLength(long expected, CoTrueTypeTableEntry entry) throws com.bluebrim.font.shared.CoFontException {
	if (entry.getLength() > expected) {
		m_warnings.println("Warning: table " + entry.getName() + " is longer than expected (" + entry.getLength() + " instead of " + expected + "). Ignoring additional information.");
	}
	if (entry.getLength() < expected) {
		throw new com.bluebrim.font.shared.CoFontException("Error in TTF file: table " + entry.getName() + " is shorter than expected (" + entry.getLength() + " instead of " + expected + ").");
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
	try {
		FileInputStream in = new FileInputStream(fontFile);
		byte[] identifier = new byte[4];

		in.read(identifier, 0, 4);

		// check identifier
		if (Arrays.equals(identifier, TTF_VERSION) || Arrays.equals(identifier, APPLE_TYPE)) {
			return true;
		}
	} catch (IOException ignored) { }
	return false;
}


protected void checkActualTableLength(long actual, CoTrueTypeTableEntry entry) throws com.bluebrim.font.shared.CoFontException {
	if (actual < entry.getLength()) {
		m_warnings.println("Warning: table " + entry.getName() + " was actually shorter than declared (" + actual + " instead of " + entry.getLength() + "). Ignoring additional information.");
	}
	if (actual > entry.getLength()) {
		throw new com.bluebrim.font.shared.CoFontException("Error in TTF file: table " + entry.getName() + " was actually longer than declared (" + actual + " instead of " + entry.getLength() + ").");
	}
}


protected String getBestMatchingName(int nameType) {
	// Check through all name records which nameType tag matches the specified (i.e. family name, copyright string, etc).
	// This type may exist in several variants, which should (hopefully) contain the same text, but in different encodings.
	// We'll try to find a string that matches our requirements first, and if this isn't found, we'll try to find another encoding
	// and try to make the best of the situation.
	// Our preference is in the following order:
	// 1st Choice: Platform: 0 (Unicode); Encoding: any; Language: any (n/a)
	// 2nd Choice: Platform: 3 (Microsoft, as unicode); Encoding: any; Language: american english (0x0409)
	// 3rd Choice: Platform: 3 (Microsoft, as unicode); Encoding: any; Language: british english (0x0809)
	// 4th Choice: Platform: 1 (Mac); Encoding: MacRoman; Language: english
	// 5th Choice: Platform: 3 (Microsoft, as unicode); Encoding: any; Language: any (FALLBACK)
	// 6th Choice: Platform: 1 (Mac); Encoding: any; Language: any (EVEN MORE FALLBACK)

	// First create a List of all name records that match our name type
	List records = new LinkedList(); // all records matching 
	for (int i = 0; i < m_nameRecords.length; i++) {
		CoTrueTypeNameRecord record = m_nameRecords[i];
		if (record.getNameType() == nameType) {
			records.add(record);
		}
	}

	// 1st Choice: Platform: 0 (Unicode); Encoding: any; Language: any (n/a)
	Iterator i = records.iterator();
	while (i.hasNext()) {
		CoTrueTypeNameRecord record = (CoTrueTypeNameRecord) i.next();
		if (record.getPlatform() == 0) {
			return record.getStringRepresentation();
		}
	}

	// 2nd Choice: Platform: 3 (Microsoft, as unicode); Encoding: any; Language: american english (0x0409)
	i = records.iterator();
	while (i.hasNext()) {
		CoTrueTypeNameRecord record = (CoTrueTypeNameRecord) i.next();
		if (record.getPlatform() == 3 && record.getLanguage() == 0x0409) {
			return record.getStringRepresentation();
		}
	}

	// 3rd Choice: Platform: 3 (Microsoft, as unicode); Encoding: any; Language: british english (0x0809)
	i = records.iterator();
	while (i.hasNext()) {
		CoTrueTypeNameRecord record = (CoTrueTypeNameRecord) i.next();
		if (record.getPlatform() == 3 && record.getLanguage() == 0x0809) {
			return record.getStringRepresentation();
		}
	}

	// 4th Choice: Platform: 1 (Mac); Encoding: MacRoman; Language: english
	i = records.iterator();
	while (i.hasNext()) {
		CoTrueTypeNameRecord record = (CoTrueTypeNameRecord) i.next();
		if (record.getPlatform() == 1 && record.getEncoding() == 0 && record.getLanguage() == 0) {
			return record.getStringRepresentation();
		}
	}
	
	// 5th Choice: Platform: 3 (Microsoft, as unicode); Encoding: any; Language: any (FALLBACK)
	i = records.iterator();
	while (i.hasNext()) {
		CoTrueTypeNameRecord record = (CoTrueTypeNameRecord) i.next();
		if (record.getPlatform() == 3) {
			return record.getStringRepresentation();
		}
	}
	
	// 6th Choice: Platform: 1 (Mac); Encoding: any; Language: any (EVEN MORE FALLBACK)
	i = records.iterator();
	while (i.hasNext()) {
		CoTrueTypeNameRecord record = (CoTrueTypeNameRecord) i.next();
		if (record.getPlatform() == 1) {
			return record.getStringRepresentation();
		}
	}

	// No matching string found, return null
	return null;
}

/**
 * fsSelection is a flag byte, most important is bit 0 which specifices if the font is italic or not (1=italic).
 * Creation date: (2001-05-28 11:15:36)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public int getFsSelection() {
	return m_fsSelection;
}

public int[] getGlyphAdvances() {
	return m_glyphAdvances;
}


public Map getGlyphMapping() {
	return m_glyphMapping;
}

public short[] getInternalKernPairDistance() {
	return m_internalKernPairDistance;
}

public int[] getInternalKernPairLeftChar() {
	return m_internalKernPairLeftChar;
}


public int[] getInternalKernPairRightChar() {
	return m_internalKernPairRightChar;
}


public double getItalicAngle() {
	return m_italicAngle;
}

public int getLinegap() {
	return m_linegap;
}

public int getStrikeoutPosition() {
	return m_strikeoutPosition;
}

public int getStrikeoutThickness() {
	return m_strikeoutThickness;
}

public short getUnderlineThickness() {
	return m_underlineThickness;
}


/**
 * Size of em square in TrueType units. Divide by this number to normalize metrics to em square.
 * Creation date: (2001-05-23 15:12:25)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public int getUnitsPerEm() {
	return m_unitsPerEm;
}

protected void handleHmtxTable() {
	m_glyphAdvances = new int[m_numHMetrics];
	for (int i = 0; i < m_numHMetrics; i++) {
		m_glyphAdvances[i] = CoNativeDataUtil.read2BytesAsInt(m_internalHmtxTable, i*4);
	}
	m_internalHmtxTable = null; // release memory
}

protected void readCmapSubTableFormat4(InputStream ttStream, int length) throws IOException {
	int version = CoNativeDataUtil.readUint16(ttStream);
	if (version != 0) {
		m_warnings.println("Warning: cmap subtable (type 4) version number is " + version + ", and not 0 as expected.");
	}

	int segmentCount = CoNativeDataUtil.readUint16(ttStream) / 2; // yes, the value is stored as x2... *sigh*
	ttStream.skip(6); // skip redundant precalculated search values

	int[] startChar = new int[segmentCount]; // first unicode value for this range
	int[] endChar = new int[segmentCount]; // last unicode value for this range
	short[] delta = new short[segmentCount]; // delta to add to all chars in this range (signed)
	int[] offset = new int[segmentCount]; // offset into mapping array, or 0 if delta is used instead for this range

	for (int i = 0; i < segmentCount; i++) {
		endChar[i] = CoNativeDataUtil.readUint16(ttStream);
	}

	ttStream.skip(2); // reserved value, should be 0

	for (int i = 0; i < segmentCount; i++) {
		startChar[i] = CoNativeDataUtil.readUint16(ttStream);
	}

	for (int i = 0; i < segmentCount; i++) {
		delta[i] = CoNativeDataUtil.readShort16(ttStream);
	}

	for (int i = 0; i < segmentCount; i++) {
		offset[i] = CoNativeDataUtil.readUint16(ttStream);
	}

	int glyphIndexArrayLength = (length - 12 - (8 * segmentCount)) / 2; // based on what remains of this table
	int[] glyphIndexArray = new int[glyphIndexArrayLength];
	for (int i = 0; i < glyphIndexArrayLength; i++) {
		glyphIndexArray[i] = CoNativeDataUtil.readUint16(ttStream);
	}

	for (int segment = 0; segment < segmentCount; segment++) {
		if (offset[segment] == 0) {
			// Just use delta values
			for (int i = startChar[segment]; i <= endChar[segment]; i++) {
				registerMapping((char) i, (short)(i + delta[segment]));  // make sure we do arithmetics modulo 65536
			}
		} else {
			// We must read data from the glyphIndexArray too...
			for (int i = startChar[segment]; i <= endChar[segment]; i++) {
				int glyphIndexOffset = (i - startChar[segment]) 
									 + (offset[segment] / 2) 
									 + (segment - segmentCount);
				if (glyphIndexOffset >= glyphIndexArrayLength) {
					m_warnings.println("Warning: Glyph index out of array bounds in cmap. Char value: " + i);
				} else {
					int glyphIndex = glyphIndexArray[glyphIndexOffset];
					if (glyphIndex != 0) { // 0 means don't map, or map to UNDEF (which is equivalent)
						
						// Note! There is a discrepancy in the documentation between Apple and Microsoft, about whether
						// to consider delta when using the offset. Apple say's "don't", Microsoft says "add delta".
						// The FreeType implementation does add delta. Also, all fonts I've checked have had delta == 0
						// when offset != 0, so in that case there's no differnce. Considering all this, I decided to
						// use Microsofts spec. /Magnus Ihse (magnus.ihse@appeal.se) (2001-05-22 19:29:48)
						glyphIndex = (short) (glyphIndex + delta[segment]);		// make sure we do arithmetics modulo 65536
						
						registerMapping((char) i, glyphIndex);
					}
				}
			}
		}
	}
}


protected void registerMapping(char ch, int glyphIndex) {
	// A single glyph may be mapped to from several unicode characters... so we need this rather
	// complex solution with a map from glyph value to collections of chars...
	
	if (glyphIndex != 0) {		// don't register 0, which is UNDEF
		Integer glyphIntWrapper = new Integer(glyphIndex);

		Set unicodeSet = (Set) m_glyphMapping.get(glyphIntWrapper);

		if (unicodeSet == null) {
			// No such glyph registered yet
			unicodeSet = new HashSet();
			m_glyphMapping.put(glyphIntWrapper, unicodeSet);
		}

		// unicodeSet is now a reference to a Set contained in the Map, so modifications to unicodeSet
		// will also modify the Set as stored in the Map...

		unicodeSet.add(new Character(ch));
	}
}


public CoTrueTypeFileParser(CoMessageLogger warningLog, InputStream ttFile) throws com.bluebrim.font.shared.CoFontException {
	super();
	m_warnings = warningLog.getWriter();
	parseTTFile(ttFile);
}


public int getAscent() {
	return m_ascent;
}

public int getDescent() {
	return m_descent;
}

public short getUnderlinePosition() {
	return m_underlinePosition;
}

public int getWeightValue() {
	return m_weightValue;
}

public int getWidthValue() {
	return m_widthValue;
}


public int getBBoxXMax() {
	return m_bBoxXMax;
}


public int getBBoxXMin() {
	return m_bBoxXMin;
}


public int getBBoxYMax() {
	return m_bBoxYMax;
}


public int getBBoxYMin() {
	return m_bBoxYMin;
}


public double getFontRevision() {
	return m_fontRevision;
}


public int[] getGlyphLocations() {
	return m_glyphLocations;
}


public long getMaxMemoryType42() {
	return m_maxMemoryType42;
}


public long getMinMemoryType42() {
	return m_minMemoryType42;
}


public int getNumTables() {
	return m_numTables;
}


public List getTableDir() {
	return m_tableDir;
}


protected void handleLocaTable() {
	/* note: the loca table can be either "long" or "short", where short 
	means: use 16 bits, and divide position by 2, and long 
	means: use 32 bits, and store correct position. 
	The format is specified in a field in the "head" table. (duh!) */

	if (m_locaTableFormat == 0) {
		int locaTableLength = m_internalLocaTable.length / 2;
		m_glyphLocations = new int[locaTableLength];
		for (int i = 0; i < locaTableLength; i++) {
			// Short version: 2 bytes per position, and the value read should be multiplied with 2
			m_glyphLocations[i] = CoNativeDataUtil.read2BytesAsInt(m_internalLocaTable, i * 2) * 2;
		}
	} else {
		int locaTableLength = m_internalLocaTable.length / 4;
		m_glyphLocations = new int[locaTableLength];
		for (int i = 0; i < locaTableLength; i++) {
			// Long version: 4 bytes per position, and the value read is the correct position
			m_glyphLocations[i] = CoNativeDataUtil.read2BytesAsInt(m_internalLocaTable, i * 4);
		}
	}
}
}