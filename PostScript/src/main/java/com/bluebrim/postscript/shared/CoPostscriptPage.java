package com.bluebrim.postscript.shared;
import java.awt.*;
import java.awt.print.*;

import com.bluebrim.base.shared.*;

/**
 * Implementation of the AWT Printable interface. This class is needed to use the pre-JDK 1.4 printing system.
 * For 1.4, it will probably become obsolete. At the moment, it is also (partially) used by CoSheet, but this
 * is bad design and should be removed.
 *
 * <p><b>Creation date:</b> 2001-08-20
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 * @author Göran Stäck
 */
public class CoPostscriptPage implements Printable {
	private CoViewable m_viewable;
	private String 	m_title;
	private CoView		m_view;


public CoPostscriptPage(String title) {
	m_title = title;
}


public CoPostscriptPage(CoViewable viewable, String title) {
	m_viewable = viewable;
	m_title = title;
}


private CoView getView() {
	if (m_view == null)
		m_view = m_viewable.getView();
	return m_view;
}


public double getHeight() {
	return getView().getHeight();
}


public int getNumPageItems() {
	return 1;
}

public String getTitle() {
	return m_title;
}


public double getWidth() {
	return getView().getWidth();
}


public double getX() {
	// Views has no position so this makes no sense.
	return 0;
}


public double getY() {
	// Views has no position so this makes no sense.
	return 0;
}


public void paint(CoPaintable paintable) {
	getView().paint(paintable);
}


/**
 * Prints the page at the specified index into the specified 
 * {@link Graphics} context in the specified
 * format.  A <code>PrinterJob</code> calls the 
 * <code>Printable</code> interface to request that a page be
 * rendered into the context specified by 
 * <code>graphics</code>.  The format of the page to be drawn is
 * specified by <code>pageFormat</code>.  The zero based index
 * of the requested page is specified by <code>pageIndex</code>. 
 * If the requested page does not exist then this method returns
 * NO_SUCH_PAGE; otherwise PAGE_EXISTS is returned.
 * The <code>Graphics</code> class or subclass implements the
 * {@link PrinterGraphics} interface to provide additional
 * information.  If the <code>Printable</code> object
 * aborts the print job then it throws a {@link PrinterException}.
 * @param graphics the context into which the page is drawn 
 * @param pageFormat the size and orientation of the page being drawn
 * @param pageIndex the zero based index of the page to be drawn
 * @return PAGE_EXISTS if the page is rendered successfully
 *         or NO_SUCH_PAGE if <code>pageIndex</code> specifies a
 *	       non-existent page.
 * @exception java.awt.print.PrinterException
 *         thrown when the print job is terminated.
 */
public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
	if (pageIndex != 0) {
		return Printable.NO_SUCH_PAGE;
	}

	CoPaintable paintable = CoScreenPaintable.wrap(graphics);

	double targetWidth = pageFormat.getImageableWidth();
	double targetHeight = pageFormat.getImageableHeight();
	double graphicsWidth = getWidth();
	double graphicsHeight = getHeight();
	double pageScaleFactor = Math.min(targetWidth / graphicsWidth, targetHeight / graphicsHeight);

	double printedWidth = graphicsWidth * pageScaleFactor;
	double printedHeight = graphicsHeight * pageScaleFactor;

	double pageTranslateX = (targetWidth - printedWidth) / 2 + pageFormat.getImageableX();
	double pageTranslateY = (targetHeight - printedHeight) / 2 + pageFormat.getImageableY();

	paintable.translate(pageTranslateX, pageTranslateY);
	paintable.scale(pageScaleFactor, pageScaleFactor);
	
	getView().paint(paintable);
	
	return Printable.PAGE_EXISTS;
}


public String toString() {
	return this.getClass().getName() + "[" + getTitle() + "]";
}


}