package com.bluebrim.font.shared;
import java.io.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.debug.*;

/**
 * Representation of a font face, that is a font at no specific size.
 *
 * Font abstraction that do not require actual java.awt.Font
 * objects. However, unlike com.bluebrim.font.shared.CoFontFaceSpec, objects of this
 * class represents existing fonts registered with the system
 * only. Also, a fontface is versioned, i.e. a spec may
 * represent different fontfaces in different points in time.<p>
 *
 * A CoFontFace knows how to derive a com.bluebrim.font.shared.CoFont, which is a representation
 * of this font face at a specific size. It also includes a bunch of
 * convenience methods to access data associated with this CoFontFace from
 * the CoFontMapper.<p>
 *
 * All metrics that is derived by methods from this class is unscaled, i.e. it is
 * correct for a font size of 1. Normally, this metrics should not be used directly.
 * Instead, the corresponding metrics should be gotten from com.bluebrim.font.shared.CoFont, which is correctly
 * scaled.<p>
 *
 * <p><b>Documentation last updated:</b> 2001-09-28
 *
 * @author Markus Persson 2000-08-31
 * @author Magnus Ihse (magnus.ihse@appeal.se) (Redesigned and completely rewritten, 2001-05-07).
 */
public class CoFontFace implements Serializable, Comparable, com.bluebrim.xml.shared.CoXmlEnabledIF {
	public final static String XML_TAG = "font-face";
	public final static String XML_VERSION = "version";

	// The variables that uniquely defines this typeface
	private com.bluebrim.font.shared.CoFontFaceSpec m_spec;
	private int m_version;
	
/**
 * Private, empty constructor. This constructor should only be called from XML import.
 */
private CoFontFace() {
}


/**
 * Create a CoFontFace with a specific version. Consider this a private constructor. Normally, a request for
 * a CoFontFace with a specific version should be directed to CoFontMapper instead. If the next version is needed,
 * use createNextVersion().
 * Use the static {@link #createFontFace(com.bluebrim.font.shared.CoFontFaceSpec,int)} instead for public use.
 *
 * @param spec the font face spec to derive the font face from
 * @param version the font face version
 *
 * @see #createFontFace(com.bluebrim.font.shared.CoFontFaceSpec,int)
 */
public CoFontFace(com.bluebrim.font.shared.CoFontFaceSpec spec, int version) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(spec, "spec");
	
	m_spec = spec;
	m_version = version;
}


public int compareTo(Object o) {
	int specComparision = m_spec.compareTo(((CoFontFace) o).m_spec);
	if (specComparision != 0) {
		return specComparision;
	} else {
		return m_version - ((CoFontFace) o).m_version;
	} 
}


/**
 * Create a CoFontFace with a specific version. Normally, a request for
 * a CoFontFace with a specific version should be directed to CoFontMapper instead. 
 *
 * @param spec the font face spec to derive the font face from
 * @param version the font face version
 *
 * @return a CoFontFace object.
 *
 * @see com.bluebrim.font.shared.CoAbstractFontMapper#getFontFace(com.bluebrim.font.shared.CoFontFaceSpec,int)
 * @see #createNextVersion
 */
public static CoFontFace createFontFace(com.bluebrim.font.shared.CoFontFaceSpec spec, int version) {
	CoFontFace face = getMapper().getFontFace(spec, version);

	if (face == null) {
		face = new CoFontFace(spec, version);
	}

	return face;
}


/**
 * Create and return a CoFontFace with the version incremented one step. This method is preferred to calling the
 * constructor of this class.
 *
 * @return a new CoFontFace.
 */
public CoFontFace createNextVersion() {
	return new CoFontFace(m_spec, m_version + 1);
}


public boolean equals(Object obj) {
	if (obj == this) return true;
	if (! (obj instanceof CoFontFace) ) return false;

	CoFontFace other = (CoFontFace) obj;
	if (! m_spec.equals(other.m_spec) ) return false;
	if (m_version != other.m_version) return false;

	return true;
}


/**
 * Fetches the AwtData from the repository for this font face. This is a convenience method for
 * accessing the Font Mapper.
 *
 * @return the AwtData associated with this CoFontFace.
 *
 * @see com.bluebrim.font.shared.CoAbstractFontMapper#getAwtData(CoFontFace)
 */
public com.bluebrim.font.shared.CoFontAwtData getAwtData() {
	return getMapper().getAwtData(this);	// delegate to FontMapper
}


/**
 * Returns the unscaled em space for this font face. Note that this has to be scaled to a particular font
 * size. More likely, the <code>getEmSpace</code> method of <code>com.bluebrim.font.shared.CoFont</code> is more useful,
 * since it returns a value scaled to a font size.<p>
 * <b>Warning!</b> The definition of the em space is vague. It typically refers to either a square of the same width as the
 * font size (i.e., for a 12 point font, the em space is 12 point wide), or the width of the capital letter
 * 'M', or sometimes to just a value at about this width, not more specified. The 'M' width seems to be the 
 * currently most widespread. See for instance
 * <a href="http://lists.w3.org/Archives/Public/www-style/2000Jan/0123.html">this collection of different 
 * em definitions</a>.
 * For the opposite view, see for instance 
 * <a href="http://lists.w3.org/Archives/Public/www-font/2000JanMar/0075.html">this message</a>.<p>
 *
 * The em space in Calvin is calculated the following way:<br>
 * <ul><li>First, the font is checked for the em dash character (Unicode EM_DASH, '\\u2014'). If it is found, its width is
 * returned as the em space width.</li>
 * <li>Otherwise, the width of the capital letter 'M' is used</li>
 * <li>If, for some reason, there is no letter 'M' in the font face, the height of the font is returned as a final
 * fallback.</li></ul><p>
 *
 * The em space for the unscaled font face is returned as the em space in points (i.e. 1/72 inch) for a font with 
 * font size 1 point in this font face. 
 *
 * @return the em space for this font face in points (of a fontsize of 1 pt).
 *
 * @see com.bluebrim.font.shared.CoFont#getEmSpace
 */
public float getEmSpace() {
	float emSpace = 1.0f; // Use font size (which is 1, since font face is unscaled) as fallback

	com.bluebrim.font.shared.metrics.CoFontMetricsData metrics = getMetrics();

	if (metrics.advanceExistsFor(CoUnicode.EM_DASH)) { // Check for EM_DASH
		emSpace = metrics.getAdvance(CoUnicode.EM_DASH);
	} else if (metrics.advanceExistsFor('M')) { // Check for M
		emSpace = metrics.getAdvance('M');
	}

	return emSpace;

	/* // Old definition:
	return getMetrics().getAdvance('0') * 2;
	*/
}


/**
 * Returns a com.bluebrim.font.shared.CoFont at a specific font size derived from this font face. This method contains no scaling parameters,
 * and is the preferred way of creating a new <code>com.bluebrim.font.shared.CoFont</code>. The constructor of <code>com.bluebrim.font.shared.CoFont</code> should never
 * be called directly. If scaling is needed, use the other form of this method.
 *
 * @param fontSize the font size of the new font.
 *
 * @return a com.bluebrim.font.shared.CoFont which is a derivation of this font face at the specified font size.
 *
 * @see #getFont(float,float,float)
 */
public com.bluebrim.font.shared.CoFont getFont(float fontSize) {
	return new com.bluebrim.font.shared.CoFont(this, fontSize);
}


/**
 * Returns a com.bluebrim.font.shared.CoFont at a specific font size and with a specific scaling, derived from this font face. 
 * If no scaling is needed, use the other form of this method. The constructor of <code>com.bluebrim.font.shared.CoFont</code> should never
 * be called directly. 
 *
 * @param fontSize the font size of the new font.
 * @param xScale the horizontal scale of this font, 1.0f is equal to no scaling
 * @param yScale the vertical scale of this font, 1.0f is equal to no scaling
 *
 * @return a com.bluebrim.font.shared.CoFont which is a derivation of this font face at the specified font size and scaling
 *
 * @see #getFont(float)
 */
public com.bluebrim.font.shared.CoFont getFont(float fontSize, float xScale, float yScale) {
	return new com.bluebrim.font.shared.CoFont(this, fontSize, xScale, yScale);
}


/**
 * Returns the Font Mapper used by this class. This is a convenience method for a call to
 * {@link com.bluebrim.font.shared.CoAbstractFontMapper#getFontMapper}.
 *
 * @return an instance of com.bluebrim.font.shared.CoAbstractFontMapper that this class uses.
 *
 * @see com.bluebrim.font.shared.CoAbstractFontMapper#getFontMapper
 */
protected static com.bluebrim.font.shared.CoAbstractFontMapper getMapper() {
	// NOTE: Do NOT "cache" in statics, it will cause GS/J errors! /Markus 2001-10-11
	return com.bluebrim.font.shared.CoAbstractFontMapper.getFontMapper();
}


/**
 * Returns the unscaled font metrics for this font face. It is probably more relevant to call the corresponding
 * methods for a specific com.bluebrim.font.shared.CoFont. This is a convenience method for accessing the Font Mapper.
 *
 * @return the unscaled font metrics for this font face.
 *
 * @see com.bluebrim.font.shared.CoAbstractFontMapper#getMetricsData(CoFontFace)
 */
public com.bluebrim.font.shared.metrics.CoFontMetricsData getMetrics() {
	return getMapper().getMetricsData(this);	// delegate to FontMapper
}


/**
 * Fetch the postscript data from the repository for this font face. This is a convenience method for
 * accessing the Font Mapper.
 *
 * @return the postscript data associated with this CoFontFace.
 *
 * @see com.bluebrim.font.shared.CoAbstractFontMapper#getPostscriptData(CoFontFace)
 */
public com.bluebrim.font.shared.CoFontPostscriptData getPostscriptData() {
	return getMapper().getPostscriptData(this); // delegate to FontMapper
}


/**
 * Return the font face spec of this font face. The spec, combined with the version,
 * uniquely describes this font face.
 *
 * @return the com.bluebrim.font.shared.CoFontFaceSpec defining this font face
 */
public com.bluebrim.font.shared.CoFontFaceSpec getSpec() {
	return m_spec;
}


/**
 * Return the version of this CoFontFace. The version, combined with the spec,
 * uniquely describes this fontface.
 *
 * @return the version of this font face.
 */
public int getVersion() {
	return m_version;
}


public int hashCode() {
	return m_spec.hashCode() + m_version;
}


/**
 * Returns true if this is not the latest CoFontFace corresponding to this spec. If this is the case, then the user
 * should be informed about this. This is a convenience method that delegates to the font mapper.
 *
 * @return true if the font is not removed, and a newer version exists, and false otherwise.
 *
 * @see com.bluebrim.font.shared.CoAbstractFontMapper#hasNewerVersion(CoFontFace)
 */
public boolean hasNewerVersion() {
	return getMapper().hasNewerVersion(this); // delegate to FontMapper
}


/**
 * Returns true if the spec this font face corresponds to has been removed from the system. If this is the case, then the user
 * should be informed about this. This is a convenience method that delegates to the font mapper.
 * Note that "removed" actually means "hidden"! For an explanation of this, see 
 * {@link com.bluebrim.font.shared.CoAbstractFontMapper#isRemoved(CoFontFace)}.
 *
 * @return true if the CoFontFace is "removed" (i.e. hidden).
 *
 * @see com.bluebrim.font.shared.CoAbstractFontMapper#isRemoved(CoFontFace)
 */
public boolean isRemoved() {
	return getMapper().isRemoved(this); // delegate to FontMapper
}


public String toString() {
	return m_spec.toString();
}


/**
 * Returns a string describing this font face, including version number. This is not something
 * that the user normally is interested of, but it could be needed when manipulating fonts to
 * differentiate them.
 *
 * @return a string describing this font face, including version number.
 */
public String toVersionedString() {
	return m_spec.toString() + " (v " + m_version + ")";
}


public void xmlAddSubModel(String parameter, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	if (subModel instanceof Number) {
		if (XML_VERSION.equals(parameter)) {
			m_version = ((Number) subModel).intValue();
		}
	} else if (subModel instanceof com.bluebrim.font.shared.CoFontFaceSpec.XmlWrapper) {
		m_spec = ((com.bluebrim.font.shared.CoFontFaceSpec.XmlWrapper) subModel).getFontFaceSpec();
	}

	// Otherwise, ignore for compatibility
}


/**
 * Called before XML import. See {@link com.bluebrim.xml.shared.CoXmlImportEnabledIF} for details.
 */
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context) {
	return new CoFontFace();
}


public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
	// Empty by design
}


public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) {
	visitor.export(XML_VERSION, new Integer(m_version));
	visitor.export(new com.bluebrim.font.shared.CoFontFaceSpec.XmlWrapper(m_spec));
}
}