package com.bluebrim.font.impl.server.type1;
import java.io.*;

import com.bluebrim.base.shared.*;

/**
 * Read and parse a Type1 font file, possibly converting it to postscript source code from binary packing.
 * Creation date: (2001-04-03 12:21:09)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
class CoType1PfbFileParser {
	private final static int PFB_MARKER = 128;
	private final static int BINARY_LINE_LENGTH = 32; // number of hex bytes per line for binary data
	private final static int ASCII_BLOCK = 1;
	private final static int BINARY_BLOCK = 2;
	private final static int END_OF_FILE = 3;
	private ByteArrayOutputStream pfaData;

/**
 * CoType1FontFile constructor comment.
 */
public CoType1PfbFileParser(InputStream type1File) throws com.bluebrim.font.shared.CoFontException {
	super();
	try {
		pfaData = new ByteArrayOutputStream();
		readType1FontFile(type1File, pfaData);
	} finally {
		try {
			pfaData.close();
		} catch (IOException ignored) { }
	}
}

protected void expandPfbFile(InputStream pfbStream, OutputStream pfaStream) throws com.bluebrim.font.shared.CoFontException {
	try {
		while (pfbStream.available() > 0) { // still got data
			// read block start byte
			int ch = pfbStream.read();
			if (ch != PFB_MARKER) {
				throw new com.bluebrim.font.shared.CoFontException("Malformed PFB file (" + pfbStream + "): Marker byte (0x80) expected but not found. (Actual value found: " + ch + ".");
			}

			// read block type definition
			ch = pfbStream.read();
			int len;
			byte[] buf;

			switch (ch) {
				case ASCII_BLOCK:
					len = (int) CoNativeDataUtil.readUint32IntelEndian(pfbStream);
					buf = new byte[len];
					pfbStream.read(buf, 0, len);
					for (int i = 0; i < buf.length; i++) {
						if (buf[i] == (byte) '\r') { // fix line breaks
							buf[i] = (byte) '\n';
						}
					}
					pfaStream.write(buf);
					break;

				case BINARY_BLOCK:
					len = (int) CoNativeDataUtil.readUint32IntelEndian(pfbStream);
					buf = new byte[len];
					pfbStream.read(buf, 0, len);

					for (int i = 0; i < buf.length; i++) {
						CoNativeDataUtil.writeHexByte(buf[i], pfaStream); // decode to hex
						if ((i+1) % BINARY_LINE_LENGTH == 0) {
							pfaStream.write('\n');
						}
					}
					pfaStream.write('\n');
					break;

				case END_OF_FILE:
					if (pfbStream.available() > 0) {
						throw new com.bluebrim.font.shared.CoFontException("Unexpected data in PFB file (" + pfbStream + ") after EOF mark.");
					}
					pfaStream.flush();
					return;

				case -1:
					throw new com.bluebrim.font.shared.CoFontException("Unexpected end of file in PFB file (" + pfbStream + "). Expected block type identifier after PFB marker.");

				default:
					throw new com.bluebrim.font.shared.CoFontException("Unknown block type in PFB file (" + pfbStream + "): ASCII or BINARY supported, but found: " + ch);
			}
		}
	} catch (IOException e) {
		throw new com.bluebrim.font.shared.CoFontException("I/O error occured while reading PFB file (" + pfbStream + ")", e);
	}
}

protected void readPfaFile(InputStream inStream, OutputStream outStream) throws com.bluebrim.font.shared.CoFontException {
	try {
		byte[] buf = new byte[65535];
		while (inStream.available() > 0) { // still got data
			int len = Math.min(inStream.available(), 65535);
			inStream.read(buf, 0, len);
			outStream.write(buf, 0, len);
		}
		outStream.flush();
	} catch (IOException e) {
		throw new com.bluebrim.font.shared.CoFontException("I/O error occured while reading PFA file (" + inStream + ")", e);
	}
}

protected void readType1FontFile(InputStream type1File, OutputStream postscriptFile) throws com.bluebrim.font.shared.CoFontException {
	PushbackInputStream pushBack = new PushbackInputStream(type1File);
	int ch = '\0';
	try {
		ch = pushBack.read();		
		pushBack.unread(ch);
		
		if (ch == '%') {
			readPfaFile(pushBack, postscriptFile);
		} else if (ch == PFB_MARKER) {
			expandPfbFile(pushBack, postscriptFile);
		} else {
			throw new com.bluebrim.font.shared.CoFontException("Unknown font file format: File is neither PFA nor PFB.");
		}
	} catch (IOException e) {
		throw new com.bluebrim.font.shared.CoFontException("Error reading from font file (" + type1File + ").", e);
	} finally {
		try {
			pushBack.close();
		} catch (IOException ignored) { }
	}
}

public static boolean canHandle(File fontFile) {
	int ch = '\0';

	try {
		InputStream inStream = new FileInputStream(fontFile);
		ch = inStream.read();
	} catch (IOException e) {
		return false;
	}

	if (ch == '%' || ch == PFB_MARKER) {
		return true;
	}
	return false;
}

public byte[] getPostscriptFontDefinition() {
	return pfaData.toByteArray();
}
}