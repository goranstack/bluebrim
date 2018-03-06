package com.bluebrim.page.impl.server;
import com.bluebrim.base.shared.*;
import java.awt.geom.*;

/**
 * Representation of a physical page type, with a outer size (the paper's physical dimension) and
 * an inner size (the maximum printable area).
 *
 * PENDING: Integrate this class with com.bluebrim.page.shared.CoPageSizeIF. com.bluebrim.page.shared.CoPageSizeIF has the notion of name of the page sizes,
 * and connection to the UI for the user to select different page sizes, which this class lack. On the other
 * hand, this class has the notion of outer and inner dimensions (physical dimension vs printable area),
 * where the former is selected from a small, limited set (A4, Broadsheet, etc), but the latter can change
 * much depending on typesetter, printer, etc. Also, this class has the notion of portrait vs landscape.
 *
 * <p><b>Creation date:</b> 2001-10-08
 * <br><b>Documentation last updated:</b> 2001-10-30
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see com.bluebrim.page.shared.CoPageSizeIF
 * @see com.bluebrim.pagesize.server.CoPageSize
 */
public class CoSheetType {
	// Define common sheet sizes
	public final static Dimension2D PAPER_SIZE_A4 = new CoDimension2D(595, 842);
	public final static Dimension2D PAPER_SIZE_NEWSPAPER = new CoDimension2D(1125, 1587);

	// Declare some typical sheet types
	public final static CoSheetType SHEET_TYPE_A4 = createA4(72, false);
	public final static CoSheetType SHEET_TYPE_A4_LANDSCAPE = createA4(72, true);
	public final static CoSheetType SHEET_TYPE_NEWSPAPER = createNewspaper(0, false);
	
	private Dimension2D m_physicalDimension;
	private Rectangle2D m_printableArea;
	
	public Rectangle2D getPrintableArea() {
		return m_printableArea;
	}

	public Dimension2D getPhysicalDimensions() {
		return m_physicalDimension;
	}

public CoSheetType(Dimension2D physicalDimension, Rectangle2D printableArea) {
	m_physicalDimension = physicalDimension;
	m_printableArea = printableArea;
}


/**
 * CoSheetType constructor comment.
 */
public CoSheetType() {
	super();
}


private static Rectangle2D calculatePrintableArea(Dimension2D sheetSize, double margin) {
	return new Rectangle2D.Double(margin, margin,
		sheetSize.getWidth() - 2*margin, sheetSize.getHeight() - 2*margin);
}

private static Rectangle2D calculatePrintableArea(Rectangle2D outer, double margin) {
	return new Rectangle2D.Double(outer.getX() + margin, outer.getY() + margin,
		outer.getWidth() - 2*margin, outer.getHeight() - 2*margin);
}

public static CoSheetType createA4(double marginWidth, boolean landscape) {
	return createSheetType(PAPER_SIZE_A4, marginWidth, landscape);
}


public static CoSheetType createNewspaper(double marginWidth, boolean landscape) {
	return createSheetType(PAPER_SIZE_NEWSPAPER, marginWidth, landscape);
}


public static CoSheetType createSheetType(Dimension2D baseSize, double marginWidth, boolean landscape) {
	Dimension2D physicalDimension;
	
	if (landscape) {
		physicalDimension = makeLandscape(baseSize);
	} else {
		physicalDimension = baseSize;
	}
	
	Rectangle2D printableArea = calculatePrintableArea(physicalDimension, marginWidth);
	return new CoSheetType(physicalDimension, printableArea);
}


public double getHeight() {
	return m_printableArea.getHeight();
}


public double getWidth() {
	return m_printableArea.getWidth();
}


public double getX() {
	return m_printableArea.getX();
}


public double getY() {
	return m_printableArea.getY();
}


private static Dimension2D makeLandscape(Dimension2D sheetSize) {
	return new CoDimension2D(sheetSize.getHeight(), sheetSize.getWidth());
}
}