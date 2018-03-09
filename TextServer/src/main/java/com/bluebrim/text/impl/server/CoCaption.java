package com.bluebrim.text.impl.server;


import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.content.server.*;
import com.bluebrim.text.shared.*;

/**
 * Creation date: (2000-06-02 11:42:10)
 * 
 * Changed (from package-private) to public. /Markus 2002-02-05
 * 
 * @author Dennis
 */
public class CoCaption extends CoObject implements CoFormattedTextHolderIF {
	private CoContent m_owner;
	protected CoFormattedText m_doc = new CoFormattedText();

	public CoCaption(CoContent owner) {
		m_owner = owner;
	}

	public String getFactoryKey() {
		return m_owner.getFactoryKey();
	}

	public String getName() {
		return m_owner.getName();
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
		m_owner.markDirty();

		if (CoAssertion.SIMULATION_SUPPORT)
			CoAssertion.addChangedObject(this);
	}

	public CoFormattedText getFormattedText( CoFormattedTextHolderIF.Context c) {
		prepare(m_doc, c);

		return m_doc;
	}

	public CoFormattedText getMutableFormattedText( CoFormattedTextHolderIF.Context c) {
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

	public void setFormattedText( CoFormattedText doc) {
		m_doc = doc;
		m_doc.setTimeStamp(System.currentTimeMillis());
		firePropertyChange(TEXT_PROPERTY, null, m_doc);
		markDirty();
	}

}