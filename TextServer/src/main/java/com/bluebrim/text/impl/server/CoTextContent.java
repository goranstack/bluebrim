package com.bluebrim.text.impl.server;



import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.content.server.*;
import com.bluebrim.content.shared.*;
import com.bluebrim.system.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.text.shared.swing.*;
import com.bluebrim.xml.shared.*;

public class CoTextContent extends CoAtomicContent implements CoTextContentIF, CoBaseLineGeometryIF, CoTextGeometryIF {
	protected String m_writer;
	protected CoFormattedText m_doc = new CoFormattedText();

	private boolean m_dirtyFlag;

	private int m_wordCount = -1;
	private int m_charCount = -1;
	private double m_columnMM = Float.NaN;

	public static final String XML_TAG = "text-content";
	public static final String XML_WRITER = "writer";

	/**
	  * @return a plain text rendering of this text
	  */
	public String getPlainText() {
		if (m_doc != null) {
			String ugly = m_doc.getText(0, m_doc.getLength());
			ugly = ugly.replace('\n', ' ').trim();
			return ugly;
		}
		return "";
	}


	public CoTextContent( CoGOI goi) {
		super(goi);

		init(null);
	}

	public CoTextContent( CoFormattedText doc) {

		init(doc);
	}

	public CoContentIF _copy() {
		//PENDING: deepClone() needs more work.... /Peter 001207
		CoTextContent textContent = (CoTextContent) deepClone();
		return textContent;
	}


	public CoTextContentIF deepClone() {
		// PENDING: super.deepClone() and CoStyledDocument.deepClone()

		CoTextContent c = new CoTextContent();

		c.setFormattedText( getFormattedText( null).deepClone());

		return c;
	}

	public CoTextContent() {
	}

	public int getCharCount() {
		if (m_charCount == -1) {
			m_charCount = getFormattedText( null).getLength();
		}

		return m_charCount;
	}

	public double getColumnMM(CoFormattedTextHolderIF.Context c) {
		if (false && Double.isNaN(m_columnMM)) {
			CoImmutableStyledDocumentIF doc = getFormattedText(c).createStyledDocument();

			//PENDING: get width from somewhere

			float w = 500; //(float) getContentProvider().getDefaultColumnGrid().getColumnWidth();
			if (w > 0) {
				double h = CoStyledTextMeasurer.getHeight(doc, w, this, this);

				m_columnMM = CoLengthUnit.MM.to(h);
			}
		}

		return m_columnMM;
	}

	public String getContentDescription() {
		return super.getContentDescription() + ", \"" + getTextExtract(20) + "\"";
	}

	// implementation of CoBaseLineGeometryIF
	public float getDeltaY() {
		return 0;
	}

	public String getFactoryKey() {
		return CoTextContentIF.FACTORY_KEY;
	}

	// implementation of CoTextGeometryIF
	public float getFirstBaselineOffset() {
		return 0;
	}

	// implementation of CoTextGeometryIF
	public String getFirstBaselineType() {
		return CoTextGeometryIF.BASELINE_ASCENT;
	}

	public CoFormattedText getFormattedText( CoFormattedTextHolderIF.Context c) {
		prepare(m_doc, c);
		return m_doc;
	}

	public String getIconName() {
		return ICON_NAME;
	}

	public String getIconResourceAnchor() {
		return CoTextContentIF.class.getName();
	}

	public String getIdentity() {
		return getName();
	}

	public CoFormattedText getMutableFormattedText( CoFormattedTextHolderIF.Context c) {
		return getFormattedText( c);
	}

	/**
	  * @return the first <code>charCount</code> characters of this text.  The returned
	  * text ends in <code>...</code> if the text has been truncated.
	  */
	public String getTextExtract(int charCount) {
		if (m_doc != null) {
			String ugly = m_doc.getText(0, Math.min(charCount, m_doc.getLength()));
			ugly = ugly.replace('\n', ' ').trim();
			return (charCount < m_doc.getLength()) ? ugly + "..." : ugly;
		}
		return "";
	}

	public float getVerticalAlignmentMaxInter() {
		return 0;
	}

	// implementation of CoTextGeometryIF
	public String getVerticalAlignmentType() {
		return CoTextGeometryIF.ALIGN_TOP;
	}

	public int getWordCount() {
		if (m_wordCount == -1) {
			m_wordCount = getFormattedText(null).getWordCount();
		}

		return m_wordCount;
	}

	public String getWriter() {
		return m_writer;
	}

	// implementation of CoBaseLineGeometryIF
	public float getY0() {
		return 0;
	}

	private void init(CoFormattedText doc) {
		m_doc = (doc != null) ? doc : new CoFormattedText();
		m_doc.setTimeStamp(System.currentTimeMillis());
		setName(CoTextServerResources.getName(CoTextServerConstants.UNTITLED_TEXT));
	}

	// implementation of CoBaseLineGeometryIF
	public boolean isEquivalentTo(CoBaseLineGeometryIF g) {
		return false;
	}

	public void markDirty() {
		m_dirtyFlag = !m_dirtyFlag;

		if (CoAssertion.SIMULATION_SUPPORT)
			CoAssertion.addChangedObject(this);
	}

	protected void prepare(CoFormattedText doc, CoFormattedTextHolderIF.Context c) {
		CoTextStyleApplier a = null;
		if (c != null)
			a = c.getTextStyleApplier();
		if (a != null)
			a.apply(doc);
	}

	protected void prepareCopy(CoContent copy) {
		super.prepareCopy(copy);

		CoTextContent tc = (CoTextContent) copy;

		tc.m_doc = m_doc.deepClone();
	}

	public void setFormattedText( CoFormattedText doc) {
		m_doc = doc;
		m_doc.setTimeStamp(System.currentTimeMillis());

		m_wordCount = m_charCount = -1;
		m_columnMM = Float.NaN;

		firePropertyChange(TEXT_PROPERTY, null, m_doc); // m_doc ????
		markDirty();
	}

	public void setWriter(String writer) {
		Object oldValue = writer;

		m_writer = writer;

		//	firePropertyChange(COTEXT_PROPERTY, oldValue, m_writer);
	}

	public String toString() {
		return super.toString() + " " + getName() + " (" + m_doc.getLength() + ")";
	}

	public void xmlInit(java.util.Map attributes, CoXmlContext context) {
		super.xmlInit(attributes, context);
		m_writer = CoXmlUtilities.parseString((String) attributes.get(XML_WRITER), m_writer);

	}

	public void xmlVisit(CoXmlVisitorIF visitor) {
		super.xmlVisit(visitor);

		visitor.export(getFormattedText( null));

		//PENDING: variants

		if (m_writer != null)
			visitor.exportAttribute(XML_WRITER, m_writer);

	}


    public String getType()
    {
    	return CoTextServerResources.getName( TEXT_CONTENT );    
    }
}