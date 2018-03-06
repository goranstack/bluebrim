package com.bluebrim.postscript.impl.shared.printing;
import java.awt.print.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.postscript.shared.*;

/**
 * Implementation of the AWT Pageable interface. This class is needed to use the pre-JDK 1.4 printing system.
 * For 1.4, it will probably become obsolete. At the moment, it is also (partially) used by CoSheet, but this
 * is bad design and should be removed.
 *
 * <p><b>Creation date:</b> 2001-08-20
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see CoPostscriptPage
 */
public class CoPageCollection implements Pageable {
	private List m_printables = new ArrayList(); // [CoPrintablePage]
	private PageFormat m_pageFormat;
	private transient String m_title;

	public CoPageCollection() {
		this(new PageFormat());
	}

	public CoPageCollection(PageFormat pageFormat) {
		m_pageFormat = pageFormat;
	}

	public void addPrintablePage(CoPostscriptPage page) {
		m_printables.add(page);
	}

	public CoPaintable[] createPaintableRepresentation() {
		CoPaintable[] paintables = new CoPaintable[m_printables.size()];

		for (int i = 0; i < m_printables.size(); i++) {
			((CoPostscriptPage) m_printables.get(i)).paint(paintables[i]);
		}

		return paintables;
	}

	public double getWidth() {
		double width = 0.0;
		Iterator i = m_printables.iterator();
		while (i.hasNext()) {
			CoPostscriptPage page = (CoPostscriptPage) i.next();
			width = Math.max(width, page.getWidth());
		}
		return width;
	}

	public double getHeight() {
		double height = 0.0;
		Iterator i = m_printables.iterator();
		while (i.hasNext()) {
			CoPostscriptPage page = (CoPostscriptPage) i.next();
			height = Math.max(height, page.getHeight());
		}
		return height;
	}

	/**
	 * Returns the number of pages in the set.
	 * To enable advanced printing features,
	 * it is recommended that <code>Pageable</code>
	 * implementations return the true number of pages 
	 * rather than the
	 * UNKNOWN_NUMBER_OF_PAGES constant.
	 * @return the number of pages in this <code>Pageable</code>.
	 */
	public int getNumberOfPages() {
		return m_printables.size();
	}

	/**
	 * Returns the <code>PageFormat</code> of the page specified by
	 * <code>pageIndex</code>.
	 * @param pageIndex the zero based index of the page whose
	 *            <code>PageFormat</code> is being requested
	 * @return the <code>PageFormat</code> describing the size and
	 *		orientation.
	 * @exception <code>IndexOutOfBoundsException</code>
	 *          the <code>Pageable</code> does not contain the requested
	 *		page.
	 */
	public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
		if (pageIndex < m_printables.size()) {
			return m_pageFormat;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	/**
	 * Returns the <code>Printable</code> instance responsible for
	 * rendering the page specified by <code>pageIndex</code>.
	 * @param pageIndex the zero based index of the page whose
	 *            <code>Printable</code> is being requested
	 * @return the <code>Printable</code> that renders the page.
	 * @exception <code>IndexOutOfBoundsException</code>
	 *            the <code>Pageable</code> does not contain the requested
	 *		  page.
	 */
	public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
		return (CoPostscriptPage) m_printables.get(pageIndex);
	}

	public CoPostscriptPage getPrintablePage(int pageIndex) throws IndexOutOfBoundsException {
		return (CoPostscriptPage) m_printables.get(pageIndex);
	}

	public List getPrintablePages() {
		return m_printables;
	}

	public String getTitle() {
		if (m_title == null) {
			String title = new String();
			Iterator i = m_printables.iterator();
			if (i.hasNext()) {
				title = ((CoPostscriptPage) i.next()).getTitle();
				while (i.hasNext()) {
					title += ", " + ((CoPostscriptPage) i.next()).getTitle();
				}
			}
			m_title = title;
		}

		return m_title;
	}

	public void setPageFormat(PageFormat pageFormat) {
		m_pageFormat = pageFormat;
	}
}