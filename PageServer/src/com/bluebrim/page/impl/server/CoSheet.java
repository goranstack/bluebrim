package com.bluebrim.page.impl.server;
import java.awt.geom.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.page.shared.*;
import com.bluebrim.postscript.shared.*;
/**
 * Representation of a physical sheet of paper, with graphics placed on it. 
 *
 * <p><b>Creation date:</b> 2001-10-08
 * <br><b>Documentation last updated:</b> 2001-10-29
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 * 
 * @see com.bluebrim.calvin.sheet.CoSheetType
 * @see com.bluebrim.page.impl.server.CoPrintablePage
 */
public class CoSheet {
	private final CoSheetType m_sheetType;
	private List m_pages = new LinkedList(); // [com.bluebrim.page.impl.server.CoPrintablePage]
	private List m_pagePlacements = new LinkedList(); // [java.awt.Rectangle2D]
	private List m_pageRotations = new LinkedList(); // [Double]
	private CoViewable m_background;
	private CoPostscriptPage m_postscriptPage;


public void setBackground(CoViewable background) {
	m_background = background;
}

	public CoSheetType getSheetType() {
		return m_sheetType;
	}

/**
 * Returns a "flattened" version of the sheet, i.e. a pageitem representing the complete
 * sheet with all pages and the background. WARNING! Not implemented yet!
 */
public CoLayout getFlattened() {
	// FIXME
	return null;
}


public CoSheet(CoSheetType sheetType) {
	super();
	m_sheetType = sheetType;
}


public com.bluebrim.postscript.shared.CoPostscriptPage getPostscriptPage() {
	if (m_postscriptPage == null) {
		m_postscriptPage = new CoPostscriptPage("");		// FIXME: set correct title
	}
	
	return m_postscriptPage;
}


/**
 * Convenience method.
 */
public void addPage(CoPage page, double xPos, double yPos, double width, double height) {
	addPage(page, new Rectangle2D.Double(xPos, yPos, width, height), 0);
}


/**
 * Convenience method.
 */
public void addPage(CoPage page, double xPos, double yPos, double width, double height, double rotation) {
	addPage(page, new Rectangle2D.Double(xPos, yPos, width, height), rotation);
}


/**
 * Adds a page to the sheet. <p>
 *
 * FIXME: Currently no checking is made if the pageBoundingBox actually is inside
 * the sheet. This should probably be made in the future.
 */
public void addPage(CoPage page, Rectangle2D pageBoundingBox, double rotation) {
	m_pages.add(page);
	m_pagePlacements.add(pageBoundingBox);
	m_pageRotations.add(new Double(rotation));
}
}