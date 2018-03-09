package com.bluebrim.postscript.impl.shared;
import java.awt.geom.*;
import java.io.*;
import java.text.*;
import java.util.*;

import com.bluebrim.font.shared.*;
import com.bluebrim.postscript.impl.server.color.*;
import com.bluebrim.postscript.impl.server.drawingops.*;
import com.bluebrim.postscript.impl.shared.printing.*;
import com.bluebrim.postscript.shared.*;

/**
 * This class is responsible for keeping all Postscript classes together, and actually producing a resulting
 * Postscript file. It implements the CoPrintableGenerator interface, meaning it takes a CoShapePageItemIF
 * array, and writes output (in this case a Postscript file) to the specified OutputStream. To do this,
 * it creates a CoPostscriptPageHolder and CoPostscriptGraphics for each page, and calls paint on each of the
 * pageitems representing the pages, with a CoPostscriptGraphics. The paint operations are recorded as
 * DrawingOperations, and this list is then traversed, and generatePostscript() called for each paint
 * operation. Before and after this, necessary headers and trailers are printed to the OutputStream.
 * 
 * <p><b>Creation date:</b> 2001-06-01
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * PENDING: Bad design regarding page independence should be fixed.
 * Referring to pages 626-627 Appendix G (sections G.4.2-3) of the
 * red book 2nd edition (NOT 3rd edition where this is omitted).
 * The current way of doing gsave/grestore across page boundaries
 * breaks n-up manipulations, for instance.
 * 
 * The way I think it should be done is to define a procedure
 * (dynamically according to positioning and size selections) in the
 * document setup (according to page 625 as above NOT in the prolog).
 * This procedure is then executed for each page as follows:
 * <code>
 * /pgsave save def
 * my-page-setup
 * 
 * ... produce marks ...
 * 
 * pgsave restore
 * </code> 
 * /Markus 2002-08-28
 * 
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public class CoPostscriptGenerator {
	public final static boolean POSTSCRIPT_DEBUG_COMMENTS = true;
	public final static boolean POSTSCRIPT_DEBUG_OP_LABELS = true;
	public final static boolean POSTSCRIPT_DEBUG_FONTS = true;
	public final static boolean POSTSCRIPT_DEBUG_IMAGES = true;

	private CoPostscriptWriter m_psWriter;
	private CoPostscriptTarget m_psTarget;
	private Map m_allFunctions = new HashMap();
	private CoColorantSet m_allColorants = new CoColorantSet();
	private Set m_printerIncludedFontFaces = new HashSet(); // [CoFontFace]
	private Set m_documentExternalFontFaces; // [CoFontFace]
	private Set m_documentIncludedFontFaces; // [CoFontFace]
	private int m_maxLanguageLevel;
	private boolean m_trappingUsed;

	private double m_graphicsWidth;
	private double m_graphicsHeight;

	private double m_printedWidth;
	private double m_printedHeight;
	private double m_pageTranslateX;
	private double m_pageTranslateY;
	private double m_pageScaleFactor;

	public CoPostscriptGenerator(CoPostscriptTarget psTarget) {
		super();
		m_psTarget = psTarget;
	}

	protected void createDocumentSetup() throws CoPostscriptLanguageLevelException {
		m_psWriter.println("%%BeginSetup");
		m_psWriter.println("/preDoc save def"); // Save complete postscript state

		// Compensate for bug in Acrobat, if we're allowed to do it
		if (!m_psTarget.isEncapsulatable()) {
			m_psWriter.println("0 dict setpagedevice");
		}

		// Setup color separation
		createColorInitialization();

		// Setup trapping
		if (m_trappingUsed) {
			if (m_psTarget.getLevel() >= 3) { // We need Level 3
				createTrappingInitialization();
			} else if (m_psTarget.isRequireHighPrecision()) {
				// FIXME: check if trapping is acually used
				throw new CoPostscriptLanguageLevelException("Language Level 3 is required to include trapping");
			}
		}

		// Download fonts to include
		Iterator i = m_documentIncludedFontFaces.iterator();
		while (i.hasNext()) {
			CoFontFace fontFace = (CoFontFace) i.next();
			String fontName = fontFace.getPostscriptData().getPostscriptName();

			if (POSTSCRIPT_DEBUG_FONTS) {
				m_psWriter.println("%%BeginResource: font " + fontName);
				m_psWriter.writeln(fontFace.getPostscriptData().getPostscriptDefinition());
				m_psWriter.println("%%EndResource");
			} else {
				m_psWriter.println("%%IncludeResource: font " + fontName);
			}
		}

		// Temporarily force this: Dynamic scaling! /Markus 2002-09-02
		dynamicScale(m_graphicsWidth, m_graphicsHeight);
		
		// Translate to compensate for inverted coordinate system. /Markus
		m_psWriter.println("0 " + m_graphicsHeight + " translate");

		// Scale down an amount corresponding to the value scaled up when printing the coordinates
		if (CoPostscriptUtil.PS_COORDINATE_SCALE_FACTOR != 1) {
			m_psWriter.println("1 " + CoPostscriptUtil.PS_COORDINATE_SCALE_FACTOR + " div dup scale");
		}

		// STUPID Fixed scaling!
/*
		// Translate to compensate for inverted coordinate system, and to place in center, if needed
		double totalPageHeight = m_psTarget.getMedia().getHeight();
		m_psWriter.println(CoPostscriptUtil.psX(m_pageTranslateX) + CoPostscriptUtil.psY(m_pageTranslateY - totalPageHeight) + "translate");

		// Scale if necessary to fit on/fill page
		if (m_pageScaleFactor != 1.0) {
			m_psWriter.println(CoPostscriptUtil.psNum(m_pageScaleFactor) + "dup scale");
		}
*/
		// Save complete graphics state that should be used at start of each page
		m_psWriter.println("gs");

		m_psWriter.println("%%EndSetup");
	}

	/**
	 * PENDING: Modify and use somewhere. /Markus 2002-08-29
	 */
	protected void dynamicScale(double width, double height) {
		m_psWriter.println("% Autofit w x h on available paper if size differs enough");
		m_psWriter.println("5 dict begin");
		m_psWriter.println("/w " + width + " def");
		m_psWriter.println("/h " + height + " def");
		m_psWriter.println("clippath pathbbox");
		m_psWriter.println("dup 3 index sub h div");
		m_psWriter.println("2 index 5 index sub w div");
		m_psWriter.println("2 copy gt {exch} if pop"); // min
		m_psWriter.println("/s exch def");
		m_psWriter.println("s 1 sub abs 0.1 gt");
		m_psWriter.println("{");
		m_psWriter.println("  3 -1 roll add h s mul sub 2 div");
		m_psWriter.println("  3 1 roll add w s mul sub 2 div");
		m_psWriter.println("  exch translate");
		m_psWriter.println("  s s scale");
		m_psWriter.println("}");
		m_psWriter.println("{pop pop pop pop} ifelse");
		m_psWriter.println("end");
	}

	/**
	 * NOTE: Doesn't scale with scale factors.
	 * 
	 * @author Markus Persson 2002-08-30
	 */
	private static String psBoundingBox(RectangularShape r) {
		return r.getMinX() + " " + r.getMinY() + " " + r.getMaxX() + " " + r.getMaxY() + " ";
	}
	
	
	
	protected void calculateScaling() {
		double targetWidth = m_psTarget.getMedia().getWidth();
		double targetHeight = m_psTarget.getMedia().getHeight();
		m_pageScaleFactor = Math.min(targetWidth / m_graphicsWidth, targetHeight / m_graphicsHeight);

		if (!m_psTarget.isShrinkToFit() && m_pageScaleFactor < 1.0) { // Shall we refrain from scaling?
			m_pageScaleFactor = 1.0;
		}
		if (!m_psTarget.isEnlargeToFill() && m_pageScaleFactor > 1.0) { // Shall we refrain from scaling?
			m_pageScaleFactor = 1.0;
		}
		m_printedWidth = m_graphicsWidth * m_pageScaleFactor;
		m_printedHeight = m_graphicsHeight * m_pageScaleFactor;

		if (m_psTarget.isCenterHorizontally()) {
			m_pageTranslateX = (targetWidth - m_printedWidth) / 2;
		} else {
			m_pageTranslateX = 0.0;
		}

		if (m_psTarget.isCenterVertically()) {
			m_pageTranslateY = (targetHeight - m_printedHeight) / 2;
		} else {
			m_pageTranslateY = 0.0;
		}
	}

	protected void createSinglePage(int pageNum, CoPostscriptPaintable recordingGraphics, CoPostscriptPageHolder pageHolder) {
		// Page setup
		m_psWriter.println("%%Page: " + pageNum + " " + pageNum);
		m_psWriter.println("%%BeginPageSetup");
		m_psWriter.println("gr gs"); // Restore defined environment
		m_psWriter.println("/prePage save def");

		// Create trap zones, if any
		if (pageHolder.isTrappingUsed()) {
			m_psWriter.writeln(pageHolder.getSetupStream().toByteArray());
		}

		// Declare all resources used on this page
		// PENDING: all resources, not only fonts
		if (!pageHolder.getFontFaces().isEmpty()) {
			Iterator i = pageHolder.getFontFaces().iterator();
			CoFontFace fontFace = (CoFontFace) i.next();
			String fontName = fontFace.getPostscriptData().getPostscriptName();

			m_psWriter.println("%%PageResources: font " + fontName);
			while (i.hasNext()) {
				fontFace = (CoFontFace) i.next();
				fontName = fontFace.getPostscriptData().getPostscriptName();
				m_psWriter.println("%%+ font " + fontName);
			}
		}

		m_psWriter.println("%%EndPageSetup");

		// Page script
		List operations = recordingGraphics.getOperations();

		Iterator i = operations.iterator();

		while (i.hasNext()) {
			CoDrawingOperation operation = (CoDrawingOperation) i.next();
			operation.generatePostscript(pageHolder);
		}

		// Page trailer (sort of, anyway)
		m_psWriter.println("prePage restore");
		m_psWriter.println("showpage");
	}

	protected void createColorInitialization() {
		String colorantNames = new String();

		Iterator i = m_allColorants.getSeparationOrder().iterator();
		while (i.hasNext()) {
			CoColorant colorant = (CoColorant) i.next();
			colorantNames += "/" + colorant.getPostscriptName() + " ";
		}

		m_psWriter.println("<<");
		m_psWriter.println(" /Separations true");
		m_psWriter.println(" /ProcessColorModel /DeviceCMYK");
		m_psWriter.println(" /SeparationColorNames [ " + colorantNames + "]");
		m_psWriter.println(" /SeparationOrder [ " + colorantNames + "]");
		m_psWriter.println(">> setpagedevice");
	}

	/**
	 * Create the DSC Prolog for the Postscript file, i.e. declare document used resources etc.
	 *
	 * Creation date: (2001-08-13 11:12:05)
	 * @author Magnus Ihse (magnus.ihse@appeal.se)
	 */
	protected void createDocumentProlog(int numPages, String title) {

		// Declare as DSC conforming Postscript document
		m_psWriter.println("%!PS-Adobe-3.0" + m_psTarget.getTargetPostscriptHeader());

		// Document content information
		m_psWriter.println("%%Title: " + title);

		m_psWriter.println("%%Creator: BlueBrim ");
		m_psWriter.println("%%CreationDate: " + (new SimpleDateFormat()).format(new Date()));

		String copyright = "FIXME: No copyright yet";
		m_psWriter.println("%%Copyright: " + copyright);

		// Postscript code information	
		m_psWriter.println("%%DocumentData: Binary");
		m_psWriter.println("%%LanguageLevel: " + m_maxLanguageLevel);
		m_psWriter.println("%%ProofMode: NotifyMe");

		// Bounding box info
		double targetHeight = m_psTarget.getMedia().getHeight();
		String boundingBox =
			+ (int) Math.floor(m_pageTranslateX)
				+ " "
				+ (int) Math.floor(targetHeight - (m_pageTranslateY + m_printedHeight))
				+ " "
				+ (int) Math.ceil(m_pageTranslateX + m_printedWidth)
				+ " "
				+ (int) Math.ceil(targetHeight - m_pageTranslateY);
		String hiResBoundingBox =
			+m_pageTranslateX + " " + (targetHeight - (m_pageTranslateY + m_printedHeight)) + " " + (m_pageTranslateX + m_printedWidth) + " " + (targetHeight - m_pageTranslateY);
		m_psWriter.println("%%BoundingBox: " + boundingBox);
		m_psWriter.println("%%HiResBoundingBox: " + hiResBoundingBox);

		// Color information	
		StringBuffer processColors = new StringBuffer();
		Iterator colorants = m_allColorants.getSeparationOrder().iterator();
		while (colorants.hasNext()) {
			processColors.append(" ");
			processColors.append(((CoColorant) colorants.next()).getPostscriptName());
		}
		m_psWriter.println("%%DocumentProcessColors:" + processColors);
		//	m_psWriter.println("%%Requirements: color(separation)"); // FIXME: use this?

		// Page and media information
		m_psWriter.println("%%Pages: " + numPages);
		m_psWriter.println("%%PageOrder: Ascend");
		String pageOrientation = (m_psTarget.getMedia().getHeight() > m_psTarget.getMedia().getWidth()) ? "Portrait" : "Landscape";
		m_psWriter.println("%%Orientation: " + pageOrientation);
		String mediaDescriptor = m_psTarget.getMedia().getPostscriptDescriptor();
		if (mediaDescriptor != null) {
			m_psWriter.println("%%DocumentMedia: " + mediaDescriptor);
		}

		// Declare all fonts we need but do not include
		if (!m_documentExternalFontFaces.isEmpty()) {
			Iterator i = m_documentExternalFontFaces.iterator();
			CoFontFace fontFace = (CoFontFace) i.next();
			String fontName = fontFace.getPostscriptData().getPostscriptName();

			m_psWriter.println("%%DocumentNeededResources: font " + fontName);
			while (i.hasNext()) {
				fontFace = (CoFontFace) i.next();
				fontName = fontFace.getPostscriptData().getPostscriptName();
				m_psWriter.println("%%+ font " + fontName);
			}
		}

		// Declare included resources

		if (!m_documentIncludedFontFaces.isEmpty()) {
			Iterator i = m_documentIncludedFontFaces.iterator();
			CoFontFace fontFace = (CoFontFace) i.next();
			String fontName = fontFace.getPostscriptData().getPostscriptName();

			m_psWriter.println("%%DocumentSuppliedResources: font " + fontName);
			while (i.hasNext()) {
				fontFace = (CoFontFace) i.next();
				fontName = fontFace.getPostscriptData().getPostscriptName();
				m_psWriter.println("%%+ font " + fontName);
			}
		}

		m_psWriter.println("%%EndComments");

		// Create defaults

		m_psWriter.print("%%BeginDefaults\n" + "%%PageBoundingBox: " + boundingBox + "\n" + "%%EndDefaults\n");

		// Create procedures

		m_psWriter.print(
			"%%BeginProlog\n"
				+ "/m { moveto } bind def\n"
				+ "/l { lineto } bind def\n"
				+ "/C { curveto } bind def\n"
				+ "/n { newpath } bind def\n"
				+ "/c { closepath } bind def\n"
				+ "/gs { gsave } bind def\n"
				+ "/gr { grestore } bind def\n");

		if (!m_allFunctions.isEmpty()) {
			// Print out all postscript procedure definitions
			Iterator i = m_allFunctions.values().iterator();
			while (i.hasNext()) {
				m_psWriter.println((String) i.next());
			}
		}

		m_psWriter.println("%%EndProlog");
	}

	public static void createPostscriptFile(CoPageCollection pages, OutputStream out, CoPostscriptTarget target) {
		System.out.println("Starting postscript generation");

		(new CoPostscriptGenerator(target)).createPrintable(pages, out);

		System.out.println("Finished postscript generation");

		try {
			out.close();
		} catch (Exception ignored) {
		}
	}

	/**
	 * Create a printable representation of a CoShapePageItem.
	 * Creation date: (2001-06-01 13:05:03)
	 * @return true if generation was successful, false otherwise 
	 * @author Magnus Ihse (magnus.ihse@appeal.se)
	 */
	public boolean createPrintable(CoPageCollection pages, OutputStream out) {
		int pageNum = pages.getNumberOfPages();

		m_psWriter = new CoPostscriptWriter(out);
		System.out.println("Postscript: Starting painting of graphics");

		CoPostscriptPaintable[] paintableArray = new CoPostscriptPaintable[pageNum];

		// Iterate through pages and generate recording graphics
		for (int i = 0; i < pageNum; i++) {
			// Create a recording graphics to hold painting operations
			paintableArray[i] = new CoPostscriptPaintable();
			pages.getPrintablePage(i).paint(paintableArray[i]);
		}

		System.out.println("Postscript: Starting registering resources");

		// Iterate through recording graphics and register resources
		CoPostscriptPageHolder[] pageHolders = new CoPostscriptPageHolder[pageNum];
		try {
			for (int i = 0; i < pageNum; i++) {
				pageHolders[i] = new CoPostscriptPageHolder(m_psWriter, m_psTarget);
				List operations = paintableArray[i].getOperations();

				Iterator iter = operations.iterator();

				while (iter.hasNext()) {
					CoDrawingOperation operation = (CoDrawingOperation) iter.next();
					operation.preparePostscript(pageHolders[i]);
				}

			}

			// Collect all page resources to document resources

			Set allFontFaces = new HashSet(); // [CoFontFace]
			for (int i = 0; i < pageNum; i++) {
				allFontFaces.addAll(pageHolders[i].getFontFaces());
				m_allFunctions.putAll(pageHolders[i].getFunctions());
				m_allColorants.addAllColorants(pageHolders[i].getColorants());
				m_maxLanguageLevel = pageHolders[i].getLanguageLevel();
				if (pageHolders[i].isTrappingUsed()) {
					m_trappingUsed = true;
				}
			}
			CoPostscriptUtil.sortTrappingOrder(m_allColorants);

			// Calculate which fonts to include in postscript document
			m_documentExternalFontFaces = new HashSet(allFontFaces);
			m_documentExternalFontFaces.retainAll(m_printerIncludedFontFaces); // needed fonts which the printer already have

			m_documentIncludedFontFaces = new HashSet(allFontFaces);
			m_documentIncludedFontFaces.removeAll(m_documentExternalFontFaces); // send the rest to the printer

			// Find bounding box that fits all pages bounding boxes (i.e. find max width and height)
			m_graphicsWidth = pages.getWidth();
			m_graphicsHeight = pages.getHeight();

			// Adjust for scaling and/or centering to match target size
			calculateScaling();

			// *** Now we know all we need to know to be able to create the Prolog and Document Setup,
			// *** so let's do that...

			// Create document prolog
			System.out.println("Postscript: Starting Prolog generation");

			createDocumentProlog(pageNum, pages.getTitle());

			// Create document setup

			createDocumentSetup();

			// Iterate through recording graphics and generate postscript for each page
			System.out.println("Postscript: Starting Document script");
			for (int i = 0; i < pageNum; i++) {
				createSinglePage(i + 1, paintableArray[i], pageHolders[i]);
			}

			// Create document trailer

			m_psWriter.println("%%Trailer");
			m_psWriter.println("gr");
			m_psWriter.println("preDoc restore");
			m_psWriter.println("%%EOF");
		} catch (CoPostscriptException e) {
			// FIXME: better error handling
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			m_psWriter.close();
		}
		return true;
	}

	protected void createTrappingInitialization() {
		m_psWriter.println("%%ADBBeginTrapSetup");

		m_psWriter.println("<<");
		m_psWriter.println(" /Trapping true");
		m_psWriter.println(" /TrappingDetails <<");
		m_psWriter.println("  /Type 1001");

		m_psWriter.print("  /TrappingOrder [ ");
		Iterator i = m_allColorants.getTrappingOrder().iterator();
		while (i.hasNext()) {
			m_psWriter.print("/" + ((CoColorant) i.next()).getPostscriptName() + " ");
		}
		m_psWriter.println("]");

		m_psWriter.println("  /ColorDetails <<");
		i = m_allColorants.getTrappingOrder().iterator();
		while (i.hasNext()) {
			CoColorant colorant = (CoColorant) i.next();
			m_psWriter.println(
				"   /"
					+ colorant.getPostscriptName()
					+ " << /ColorantName ("
					+ colorant.getName()
					+ ") /ColorantType /"
					+ colorant.getTrappingType().getPostscriptName()
					+ " /NeutralDensity "
					+ colorant.getNeutralDensity()
					+ " >>");
		}
		m_psWriter.println("  >>");
		m_psWriter.println(" >>");
		m_psWriter.println(">> setpagedevice");

		m_psWriter.println("%%ADBEndTrapSetup");

		//	String loadTrapping = "%currentdict /Trapping /ProcSet findresource begin begin\n";

	}
}