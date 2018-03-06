package com.bluebrim.image.impl.server.xml;

import com.bluebrim.xml.shared.*;

/**
 * This class is a xml-context used during export/import
 * to access publication contents. 
 * 
 * Most in this class was taken from PageitemXmlContext. 
 * Content producent context is useful when pageitem specific 
 * xml-context cannot be created.
 * 
 * Creation date: (2001-02-12 14:24:08)
 * @author: Dennis
 */

public abstract class CoImageXmlContext extends CoXmlContext {
	/**
	 *	Sequence number used as a part of generated file names
	 *  Note that the order of e.g. images encountered upon export
	 *  may change from one save to another. Thus an image saved
	 *  with sequence number 12 may have another sequence number
	 *  when saved again later, depending on the structure of the 
	 *  page items to be saved.
	 */
	private int m_number = 0;

	/**
	 *	Determines whether inlining (Base64) is used for images
	 *  used upon export
	 */
	private boolean m_inlineImages = false;

	public CoImageXmlContext() {
	}

	public boolean doInlineImages() {
		return m_inlineImages;
	}

	/**
	 *	Returns the next sequence number. Because each saving operation
	 *  creates its own xml context, it is thread safe
	 */
	public int getNextNumber() {
		return m_number++;
	}

	public void setInlineImages(boolean mode) {
		m_inlineImages = mode;
	}

}