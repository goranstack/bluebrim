package com.bluebrim.font.impl.server.truetype;

import java.io.*;

import com.bluebrim.base.shared.*;

/**
 * Representation of an entry in the true type TOC. Each entry contains information about a table
 * in the TTF file, following the TOC.
 * @author Magnus Ihse (magnus.ihse@appeal.se) (2001-05-28 14:06:07)
 */

class CoTrueTypeTableEntry {
	public static final int LENGTH = (4 * 4); // length of entry in file: four fields of 4 bytes each

	private long m_checkSum;
	private long m_offset;
	private long m_length;
	private String m_name;
public long getCheckSum() {
	return m_checkSum;
}

public long getLength() {
	return m_length;
}

public String getName() {
	return m_name;
}

public long getOffset() {
	return m_offset;
}

public CoTrueTypeTableEntry(InputStream ttStream) throws com.bluebrim.font.shared.CoFontException {
	byte[] nameBuffer = new byte[4];
	try {
		ttStream.read(nameBuffer);
		m_name = new String(nameBuffer);

		m_checkSum = CoNativeDataUtil.readUint32(ttStream);
		m_offset = CoNativeDataUtil.readUint32(ttStream);
		m_length = CoNativeDataUtil.readUint32(ttStream);
	} catch (IOException e) {
		throw new com.bluebrim.font.shared.CoFontException("IO error while reading true type TableEntry", e);
	}
}

public String toString() {
	return getName() + " [" + getOffset() + ", " + getLength() + "]";
}


public long get4BytePaddedLength() {
	if (m_length % 4 != 0) {
		return m_length + (4 - (m_length % 4));
	} else {
		return m_length;
	}
}
}