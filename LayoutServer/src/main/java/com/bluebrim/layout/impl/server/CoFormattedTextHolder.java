package com.bluebrim.layout.impl.server;


import com.bluebrim.base.shared.CoObject;
import com.bluebrim.base.shared.debug.CoAssertion;
import com.bluebrim.text.shared.CoFormattedText;
import com.bluebrim.text.shared.CoFormattedTextHolderIF;
import com.bluebrim.text.shared.CoTextStyleApplier;

/**
 * Implementation of CoFormattedTextHolderIF used by CoPageItemTextContent when it is not projecting a CoTextContent
 * Creation date: (2000-06-02 11:42:10)
 * @author Dennis
 */
class CoFormattedTextHolder extends CoObject implements CoFormattedTextHolderIF {
	protected CoFormattedText m_doc;

	public String getFactoryKey() {
		return null;
	}

	public String getName() {
		return null;
	}
	public String getTextExtract(int charCount) {
		if (m_doc != null) {
			String ugly = m_doc.getText(0, Math.min(charCount, m_doc.getLength()));
			ugly = ugly.replace('\n', ' ').trim();
			return (charCount < m_doc.getLength()) ? ugly + "..." : ugly;
		}
		return "";
	}

	private void markDirty() {
		if (CoAssertion.SIMULATION_SUPPORT)
			CoAssertion.addChangedObject(this);
	}

	public CoFormattedTextHolder() {
		m_doc = new CoFormattedText();
	}

	public void copyFrom(CoFormattedTextHolder dh) {
		m_doc = dh.m_doc.deepClone();
	}

	public CoFormattedText getFormattedText(CoFormattedTextHolderIF.Context c) {
		prepare(m_doc, c);

		return m_doc;
	}

	public CoFormattedText getMutableFormattedText(CoFormattedTextHolderIF.Context c) {
		return getFormattedText( c);
	}

	public java.util.Collection getVariants() {
		return null;
	}

	public String getWriter() {
		return null;
	}

	protected void prepare(CoFormattedText doc, CoFormattedTextHolderIF.Context c) {
		CoTextStyleApplier a = null;
		if (c != null)
			a = c.getTextStyleApplier();
		if (a != null)
			a.apply(doc);
	}

	public void setFormattedText(CoFormattedText doc) {
		m_doc = doc;
		m_doc.setTimeStamp(System.currentTimeMillis());
		firePropertyChange(TEXT_PROPERTY, null, m_doc);
		markDirty();
	}

}