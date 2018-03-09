package com.bluebrim.postscript.impl.shared;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.postscript.impl.shared.printing.*;

/**
 * An implementation of CoPrintableGenerator that is capable of producing an EPS file with a TIFF preview.
 * It does the actual postscript generation by calling CoPostscriptGenerator, and the generated code is
 * integrated in the EPS file.
 *
 * <p><b>Creation date:</b> 2001-07-24
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public class CoEpsGenerator {
	private final static int PAGE_ITEM_DPI = 72;
	private final static int DEFAULT_PREVIEW_DPI = 14;
	private final static int EPS_HEADER_LENGTH = 30;
	private final static long EPS_HEADER_MAGIC_COOKIE = 0xC5D0D3C6;
	private final static int EPS_NO_CHECKSUM = 0xFFFF;
	
	private com.bluebrim.postscript.shared.CoPostscriptTarget m_psTarget;
	private int m_previewDpi;
	private int m_previewWidth;
	private int m_previewHeight;
public CoEpsGenerator(com.bluebrim.postscript.shared.CoPostscriptTarget psTarget) {
	this(psTarget, DEFAULT_PREVIEW_DPI);
}


public CoEpsGenerator(com.bluebrim.postscript.shared.CoPostscriptTarget psTarget, int previewDpi) {
	super();
	m_psTarget = psTarget;
	m_previewDpi = previewDpi;
}


public static void createEpsFile(CoPageCollection pages, File outputFile, com.bluebrim.postscript.shared.CoPostscriptTarget target) {
	OutputStream out = null;
	try {
		out = new BufferedOutputStream(new FileOutputStream(outputFile));
		System.out.println("Starting EPS generation");

		(new CoEpsGenerator(target)).createPrintable(pages, out);
		out.close();

		System.out.println("Finished EPS generation");

	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		try {
			out.close();
		} catch (Exception e) {
		}
	}

}

public static void createEpsFile(CoPageCollection pages, OutputStream out, com.bluebrim.postscript.shared.CoPostscriptTarget target) {
	try {
		System.out.println("Starting EPS generation");

		(new CoEpsGenerator(target)).createPrintable(pages, out);
		out.close();

		System.out.println("Finished EPS generation");

	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		try {
			out.close();
		} catch (Exception e) {
		}
	}

}

/**
 * Create a printable representation of several CoShapePageItems, one per page.
 * Creation date: (2001-07-24 17:58:33)
 * @return true if generation was successful, false otherwise 
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public boolean createPrintable(CoPageCollection pages, OutputStream out) {
	ByteArrayOutputStream tiffStream = new ByteArrayOutputStream();
	if (!createTiffPreview(pages, tiffStream)) {
		// TIFF preview creation failed
		return false;
	}

	// Generate Postscript

	// PENDING: this could be very large. Perhaps we should first write a dummy EPS header,
	// then generate the postscript directly to the file, and finally seek back to the
	// beginning of the file and write a new, correct header, now knowing the size of the
	// postscript part.
	ByteArrayOutputStream psStream = new ByteArrayOutputStream();
	(new CoPostscriptGenerator(m_psTarget)).createPrintable(pages, psStream);

	// Write header
	
	try {
		
		int tiffStart = EPS_HEADER_LENGTH; // Tiff follows directly on header
		int psStart = tiffStart + tiffStream.size(); // PS part comes after Tiff

		CoNativeDataUtil.writeUint32(out, EPS_HEADER_MAGIC_COOKIE);
		CoNativeDataUtil.writeUint32IntelEndian(out, psStart);
		CoNativeDataUtil.writeUint32IntelEndian(out, psStream.size());
		CoNativeDataUtil.writeUint32IntelEndian(out, 0); // Start of Meta file, not used
		CoNativeDataUtil.writeUint32IntelEndian(out, 0); // Length of Meta file, not used
		CoNativeDataUtil.writeUint32IntelEndian(out, tiffStart);
		CoNativeDataUtil.writeUint32IntelEndian(out, tiffStream.size());
		CoNativeDataUtil.writeUint16(out, EPS_NO_CHECKSUM); // Ignore checksum	

		// Now write the two parts, TIFF and Postscript
		tiffStream.writeTo(out);
		psStream.writeTo(out);

		out.close();
	} catch (IOException e) {
		return false;
	}

	return true;
}


protected boolean createTiffPreview(CoPageCollection pages, OutputStream out) {
	// Generate TIFF Preview. Only use first page
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue(pages.getNumberOfPages() >= 1, "Must be at least one page");

	com.bluebrim.postscript.shared.CoPostscriptPage page = pages.getPrintablePage(0);
	
	// Scale and translate graphics to match view size
	double pageWidth = page.getWidth();
	double pageHeight = page.getHeight();

	double scaleFactor = (double)m_previewDpi / PAGE_ITEM_DPI;
	
	int previewWidth = (int)(pageWidth * scaleFactor);
	int previewHeight = (int)(pageHeight * scaleFactor);

	// Create preview holder and graphics for that preview image buffer

	// There's a bug in TIFFImageEncoder, which can't handle TYPE_INT_RGB, which perhaps would be
	// the most natural choice otherwise... oh well.
	BufferedImage preview = new BufferedImage(previewWidth, previewHeight, BufferedImage.TYPE_3BYTE_BGR);
	
	CoScreenPaintable paintable = CoScreenPaintable.wrap( preview.createGraphics() );
	paintable.getGraphics2D().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	// Make background opaque white
	Rectangle background = new Rectangle(0, 0, previewWidth, previewHeight);
	paintable.setColor(Color.white);
	paintable.fill(background);
	
	// Scale to make dimensions equal to pageitemview's dimensions	
	paintable.scale(scaleFactor, scaleFactor);

	// Translate if necessary
	if (page.getX() != 0.0 || page.getY() != 0.0) {
		paintable.translate(-page.getX(), -page.getY());
	}

	// Paint to the graphics
	page.paint(paintable);

	paintable.releaseDelegate();

	// Create TIFF file from preview image
	try {
		ImageIO.write(preview, "tiff", out);
	} catch (IOException e) {
		return false;
	}

	return true;
}
}