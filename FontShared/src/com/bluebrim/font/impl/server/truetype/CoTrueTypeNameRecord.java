package com.bluebrim.font.impl.server.truetype;

import java.io.*;

import com.bluebrim.base.shared.*;

/**
 * Representation of a record in the true type "name" table, for a string representation for a specific
 * platform, in a specific character encoding and language.
 * Creation date: (2001-05-21 16:22:06)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public class CoTrueTypeNameRecord {
	private int m_platform;
	private int m_encoding;
	private int m_language;
	private int m_nameType;
	private int m_length;
	private int m_offset;
	private byte[] m_nameData;
/**
 * CoTrueTypeNameRecord constructor comment.
 */
public CoTrueTypeNameRecord(InputStream ttStream) throws IOException {
	m_platform = CoNativeDataUtil.readUint16(ttStream);
	m_encoding = CoNativeDataUtil.readUint16(ttStream);
	m_language = CoNativeDataUtil.readUint16(ttStream);
	m_nameType = CoNativeDataUtil.readUint16(ttStream);
	m_length = CoNativeDataUtil.readUint16(ttStream);
	m_offset = CoNativeDataUtil.readUint16(ttStream);
}


/**
 * No_description_given_yet<|>
 * Creation date: (2001-05-21 16:27:49)
 * @param 
 * @return 
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 * 
 * @return int
 */
public int getEncoding() {
	return m_encoding;
}


/**
 * No_description_given_yet<|>
 * Creation date: (2001-05-21 16:27:49)
 * @param 
 * @return 
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 * 
 * @return int
 */
public int getLanguage() {
	return m_language;
}


/**
 * No_description_given_yet<|>
 * Creation date: (2001-05-21 16:27:49)
 * @param 
 * @return 
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 * 
 * @return int
 */
public int getLength() {
	return m_length;
}


/**
 * No_description_given_yet<|>
 * Creation date: (2001-05-21 16:27:49)
 * @param 
 * @return 
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 * 
 * @return int
 */
public int getNameType() {
	return m_nameType;
}


/**
 * No_description_given_yet<|>
 * Creation date: (2001-05-21 16:27:49)
 * @param 
 * @return 
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 * 
 * @return int
 */
public int getOffset() {
	return m_offset;
}


/**
 * No_description_given_yet<|>
 * Creation date: (2001-05-21 16:27:49)
 * @param 
 * @return 
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 * 
 * @return int
 */
public int getPlatform() {
	return m_platform;
}

public void setNameDataArea(byte[] nameData) {
	m_nameData = nameData;
}

public String toString() {
	String s = getStringRepresentation();

	if (s != null) {
		return s;
	} else {
		return super.toString();
	}
}


/**
 * Returns String representation, if NameData area has been set. May return null.
 */
public String getStringRepresentation() {
	if (m_nameData != null) {
		if (getPlatform() == 0 || getPlatform() == 3) { // Apple Unicode or Microsoft Unicode
			return CoNativeDataUtil.readDoubleBytesAsString(m_nameData, getOffset(), getLength());
		} else if (getPlatform() == 1) { // Apple 8-bit
			// FIXME: should really take care of actual Mac encoding. Right now we just fall back to
			// whatever Java considers to be "platform default".
			return new String(m_nameData, getOffset(), getLength());
		}
		// Other platforms are unknown and ignored
	}
	return null;
}
}