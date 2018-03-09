package com.bluebrim.font.impl.shared;
import java.awt.font.*;
import java.io.*;
import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.debug.*;

/**
 * Default implementation of com.bluebrim.font.shared.CoFontAwtData interface.
 * This implementation should be good enough for all practical applications. The main reason to use the interface
 * instead of just this implementation, is that the interface hides the set methods, making the object practically
 * immutable.
 *
 * <p><b>Creation date:</b> 2001-04-24
 * <br><b>Documentation last updated:</b> 2001-09-28
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se) (2001-09-28).
 *
 * @see com.bluebrim.font.shared.CoFontAwtData
 */
public class CoFontAwtDataImplementation implements com.bluebrim.font.shared.CoFontAwtData, Serializable {
	// Variables needed to define the AwtData
	private boolean m_isBold;
	private boolean m_isItalic;
	private String m_fontName;
	private boolean m_awtWorkaround_isType1;			// only needed for AWT bug workaround

	// Transient data which can be derived when needed
	private transient Map m_attributes;
public Map getAwtAttributes() {
	if (m_attributes == null) { // recalculate map
		m_attributes = new HashMap();

		if (m_fontName != null) {
			m_attributes.put(TextAttribute.FAMILY, m_fontName);
		}
/*
// Actually, this does not seem to be nessecary, at least not for Type1 fonts. Perhaps this whole complex
// AwtData class is not really needed, apart from font name and font size (which is set by com.bluebrim.font.shared.CoFont).
// On the other hand, it might still be needed for TrueType fonts.
		if (m_isBold) {
			m_attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
		}

		if (m_isItalic) {
			m_attributes.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
		}
*/		
	}

	return m_attributes;
}

public String getFontName() {
	return m_fontName;
}

public boolean isBold() {
	return m_isBold;
}

public boolean isItalic() {
	return m_isItalic;
}

public void setBold(boolean isBold) {
	if (isBold != m_isBold) {		// make sure we recalculate the attribute map if changed
		m_attributes = null;
	}
	m_isBold = isBold;
}

public void setFontName(java.lang.String fontName) {
	if (fontName == null || !fontName.equals(m_fontName)) { // make sure we recalculate the attribute map
															// if removing name, or if it is changed
		m_attributes = null;
	}

	m_fontName = fontName;
}

public void setItalic(boolean isItalic) {
	if (isItalic != m_isItalic) { // make sure we recalculate the attribute map if changed
		m_attributes = null;
	}

	m_isItalic = isItalic;
}

public boolean awtWorkaround_isType1() {
	return m_awtWorkaround_isType1;
}


/**
 * Constructs a new CoFontAwtDataImplementation object.
 *
 * @param isBold true if this font face is bold, according to AWT.
 * @param isItalic true if this font face is italic, according to AWT.
 * @param fontName the font name used to identify this font in AWT.
 */
public CoFontAwtDataImplementation(boolean isBold, boolean isItalic, String fontName) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(fontName, "fontName");
	
	m_isBold = isBold;
	m_isItalic = isItalic;
	m_fontName = fontName;
}


/**
 * Constructs a new CoFontAwtDataImplementation object.
 *
 * @param isBold true if this font face is bold, according to AWT.
 * @param isItalic true if this font face is italic, according to AWT.
 * @param fontName the font name used to identify this font in AWT.
 * @param awtWorkaround_isType1 true if this font is Type1, to enable a workaround for an AWT bug.
 */
public CoFontAwtDataImplementation(boolean isBold, boolean isItalic, String fontName, boolean awtWorkaround_isType1) {
	this(isBold, isItalic, fontName);
	m_awtWorkaround_isType1 = awtWorkaround_isType1;
}


public void awtWorkaround_setType1(boolean isType1) {
	m_awtWorkaround_isType1 = isType1;
}





public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) {
	visitor.exportString(XML_FONT_NAME, m_fontName);
	if (m_isBold) visitor.exportString(XML_IS_BOLD, "true");
	if (m_isItalic) visitor.exportString(XML_IS_ITALIC, "true");
	if (m_awtWorkaround_isType1) visitor.exportString(XML_IS_TYPE1, "true");
}


public void xmlAddSubModel(String parameter, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	if (subModel instanceof String) {
		if (XML_FONT_NAME.equals(parameter)) {
			m_fontName = (String) subModel;
		} else if (XML_IS_BOLD.equals(parameter) && subModel.equals("true")) {
			m_isBold = true;
		} else if (XML_IS_ITALIC.equals(parameter) && subModel.equals("true")) {
			m_isItalic = true;
		} else if (XML_IS_TYPE1.equals(parameter) && subModel.equals("true")) {
			m_awtWorkaround_isType1 = true;
		}
	}

	// Otherwise, ignore for compatibility
}


public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	// Empty by design
}


/**
 * Private, empty constructor. This constructor should only be used by the XML import.
 */
private CoFontAwtDataImplementation() {
}


/**
 * Called before XML import. See {@link com.bluebrim.xml.shared.CoXmlImportEnabledIF} for details.
 */
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context) {
	return new CoFontAwtDataImplementation();
}
}