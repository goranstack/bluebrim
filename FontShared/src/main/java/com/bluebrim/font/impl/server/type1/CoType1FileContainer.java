package com.bluebrim.font.impl.server.type1;
import java.io.*;

import org.w3c.dom.*;

import com.bluebrim.font.impl.shared.*;

/* Contains an AFM file and a PFB-file, together representing a type1 font.
 * Creation date: (2001-04-24 12:50:01)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public class CoType1FileContainer implements CoFontFileContainer, Serializable {
	public final static String XML_TAG = "type1-file";
	public final static String XML_FONT_FILE_NAME = "font-filename";
	public final static String XML_FONT_DATA = "font-data";
	public final static String XML_AFM_FILE_NAME = "afm-filename";
	public final static String XML_AFM_DATA = "afm-data";

	private String m_fontFileName;
	private String m_afmFileName;
	private byte[] m_fontFileData;
	private byte[] m_afmFileData;
public CoType1FileContainer(File fontFileNameAndPath, File afmFileNameAndPath) throws com.bluebrim.font.shared.CoFontException {
	super();
	try {
		FileInputStream fontStream = new FileInputStream(fontFileNameAndPath);
		FileInputStream afmStream = new FileInputStream(afmFileNameAndPath);

		m_fontFileData = new byte[fontStream.available()];
		m_afmFileData = new byte[afmStream.available()];

		fontStream.read(m_fontFileData);
		afmStream.read(m_afmFileData);

		fontStream.close();
		afmStream.close();
	} catch (IOException e) {
		throw new com.bluebrim.font.shared.CoFontException("Error while reading AFM font or metrics (AFM) file", e);
	}

	m_fontFileName = fontFileNameAndPath.getName();
	m_afmFileName = afmFileNameAndPath.getName();
}



public CoType1FileContainer(File fontFileNameAndPath, File afmFileNameAndPath, byte[] fontFileData, byte[] afmFileData) {
	super();
	m_fontFileName = fontFileNameAndPath.getName();
	m_afmFileName = afmFileNameAndPath.getName();
	m_fontFileData = fontFileData;
	m_afmFileData = afmFileData;
}



public String getFontFileBaseName() {
	return m_fontFileName;
}



public void writeFontFileToDisk(File path) throws com.bluebrim.font.shared.CoFontException {
	if (!path.isDirectory()) {
		path.mkdirs(); // Try to create the path
		if (!path.isDirectory()) { // Does it still not exist?
			// FIXME: remove this warning message!
			System.out.println("ERROR: JAVA_FONTS is probably not correct. See warning message at Calvin startup time (beginning of log).");
			
			throw new com.bluebrim.font.shared.CoFontException("The path for font storage " + path + " is not valid.");
		}
	}

	File afmPath = new File(path, "afm");
	if (!afmPath.isDirectory()) {
		afmPath.mkdirs(); // Try to create the path for afm storage
		if (!afmPath.isDirectory()) { // Does it still not exist?
			throw new com.bluebrim.font.shared.CoFontException("The path for afm file storage " + afmPath + " is not valid.");
		}
	}

	File fontFile = new File(path, m_fontFileName);
	File afmFile = new File(afmPath, m_afmFileName);

	try {
		FileOutputStream fontStream = new FileOutputStream(fontFile);
		fontStream.write(m_fontFileData);
		fontStream.close();
	} catch (IOException e) {
		throw new com.bluebrim.font.shared.CoFontException("Can't create or write to Type1 font data file " + fontFile, e);
	}

	try {
		FileOutputStream afmStream = new FileOutputStream(afmFile);
		afmStream.write(m_afmFileData);
		afmStream.close();
	} catch (IOException e) {
		throw new com.bluebrim.font.shared.CoFontException("Can't create or write to Type1 companion metrics data file (AFM) " + afmFile, e);
	}
}


public void xmlAddSubModel(String parameter, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	if (subModel instanceof String) {
		if (XML_FONT_FILE_NAME.equals(parameter)) {
			m_fontFileName = (String) subModel;
		} else if (XML_AFM_FILE_NAME.equals(parameter)) {
			m_afmFileName = (String) subModel;
		}
	} else if (subModel instanceof InputStream) {
		if (XML_FONT_DATA.equals(parameter)) {
			InputStream in = (InputStream) subModel;
			try {
				m_fontFileData = new byte[in.available()];
				in.read(m_fontFileData);
			} catch (IOException e) {
				throw new com.bluebrim.xml.shared.CoXmlReadException("Error reading inputstream of Type 1 font data", e);
			}
		} else if (XML_AFM_DATA.equals(parameter)) {
			InputStream in = (InputStream) subModel;
			try {
				m_afmFileData = new byte[in.available()];
				in.read(m_afmFileData);
			} catch (IOException e) {
				throw new com.bluebrim.xml.shared.CoXmlReadException("Error reading inputstream of Type 1 AFM metric data", e);
			}
		}
	}

	// Otherwise, ignore for compatibility
}


public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) throws com.bluebrim.xml.shared.CoXmlWriteException {
	visitor.exportString(XML_FONT_FILE_NAME, m_fontFileName);
	visitor.exportString(XML_AFM_FILE_NAME, m_afmFileName);
	
	try {
		visitor.exportBinary(new ByteArrayInputStream(m_fontFileData), XML_FONT_DATA, ".pfb");
		visitor.exportBinary(new ByteArrayInputStream(m_fontFileData), XML_AFM_DATA, ".afm");
	} catch (IOException e) {
		throw new com.bluebrim.xml.shared.CoXmlWriteException("Error writing binary data of postscript font data", e);
	}
}


public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	// Empty by design
}


/**
 * This constructor should only be called from XML import.
 */
private CoType1FileContainer() {
}


/**
 * Called before XML import. See {@link com.bluebrim.xml.shared.CoXmlImportEnabledIF} for details.
 */
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context) {
	return new CoType1FileContainer();
}
}