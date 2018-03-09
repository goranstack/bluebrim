package com.bluebrim.font.impl.server.truetype;
import java.io.*;

import org.w3c.dom.*;

import com.bluebrim.font.impl.shared.*;

/**
 * Contains a TTF-file, with the possibility to write it back to disk.

 * Creation date: (2001-05-18 15:11:29)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public class CoTrueTypeFileContainer implements CoFontFileContainer {
	public final static String XML_TAG = "true-type-file";
	public final static String XML_FONT_FILE_NAME = "filename";
	public final static String XML_FONT_DATA = "data";
	
	private String m_fontFileName;
	private byte[] m_fontFileData;
public CoTrueTypeFileContainer(File fontFileNameAndPath) throws com.bluebrim.font.shared.CoFontException {
	super();
	try {
		FileInputStream fontStream = new FileInputStream(fontFileNameAndPath);

		m_fontFileData = new byte[fontStream.available()];

		fontStream.read(m_fontFileData);

		fontStream.close();
	} catch (IOException e) {
		throw new com.bluebrim.font.shared.CoFontException("Error while reading true type font file", e);
	}

	m_fontFileName = fontFileNameAndPath.getName();
}


public CoTrueTypeFileContainer(File fontFileNameAndPath, byte[] fontFileData) {
	super();
	m_fontFileName = fontFileNameAndPath.getName();
	m_fontFileData = fontFileData;
}


public String getFontFileBaseName() {
	return m_fontFileName;
}


public void writeFontFileToDisk(File path) throws com.bluebrim.font.shared.CoFontException {
	if (!path.isDirectory()) {
		path.mkdirs(); // Try to create the path
		if (!path.isDirectory()) {	// Does it still not exist?
			// FIXME: remove this warning message!
			System.out.println("ERROR: JAVA_FONTS is probably not correct. See warning message at Calvin startup time (beginning of log).");
			
			throw new com.bluebrim.font.shared.CoFontException("The path for font storage " + path + " is not valid.");
		}
	}

	File fontFile = new File(path, m_fontFileName);

	try {
		FileOutputStream fontStream = new FileOutputStream(fontFile);
		fontStream.write(m_fontFileData);
		fontStream.close();
	} catch (IOException e) {
		throw new com.bluebrim.font.shared.CoFontException("Can't create or write to true type font file " + fontFile, e);
	}
}

public void xmlAddSubModel(String parameter, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	if (subModel instanceof String) {
		if (XML_FONT_FILE_NAME.equals(parameter)) {
			m_fontFileName = (String) subModel;
		}
	} else if (subModel instanceof InputStream) {
		if (XML_FONT_DATA.equals(parameter)) {
			InputStream in = (InputStream) subModel;
			try {
				m_fontFileData = new byte[in.available()];
				in.read(m_fontFileData);
			} catch (IOException e) {
				throw new com.bluebrim.xml.shared.CoXmlReadException("Error reading inputstream of TrueType font data", e);
			}
		}
	}

	// Otherwise, ignore for compatibility
}





public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) throws com.bluebrim.xml.shared.CoXmlWriteException {
	visitor.exportString(XML_FONT_FILE_NAME, m_fontFileName);
	try {
		visitor.exportBinary(new ByteArrayInputStream(m_fontFileData), XML_FONT_DATA, ".ttf");
	} catch (IOException e) {
		throw new com.bluebrim.xml.shared.CoXmlWriteException("Error writing binary data of TrueType font data", e);
	}
}


public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	// Empty by design
}


/*
 * This constructor must only be called from XML import.
 */
 
private CoTrueTypeFileContainer() {
}


/**
 * Called before XML import. See {@link com.bluebrim.xml.shared.CoXmlImportEnabledIF} for details.
 */
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context) {
	return new CoTrueTypeFileContainer();
}
}