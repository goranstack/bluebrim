package com.bluebrim.postscript.impl.server.drawingops;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;

import com.bluebrim.postscript.impl.shared.*;

/**
 * Draws a bitmap (com.bluebrim.image.shared.CoRenderableImage) on the postscript output.
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public class CoDrawRenderableImageOp extends CoDrawingOperation {
	private static final int HEX_LINE_LENGTH = 38;

	private RenderedImage m_image;

	public CoDrawRenderableImageOp(RenderedImage image) {
		m_image = image;
	}

	public void generatePostscript(CoPostscriptHolder psHolder) {

		psHolder.includeComment("CoDrawImageOp image -> " + m_image);

//		RenderedImage rendered = m_image.createDefaultRendering();
//		Dimension2D pixelSize =
//			new CoDimension2D(
//				m_image.getPrecisionWidth() / rendered.getWidth(),
//				m_image.getPrecisionHeight() / rendered.getHeight());

		CoPostscriptWriter writer = psHolder.getWriter();

		writer.println("gs");

		// Adjust for fitting the coordinate system of the EPS into our coordinate system

		psHolder.getWriter().println(CoPostscriptUtil.PS_COORDINATE_SCALE_FACTOR + " dup scale");

		writer.print("/DeviceRGB setcolorspace ");

//		if ((pixelSize.getWidth() != 1.0) || (pixelSize.getHeight() != 1.0)) {
//			writer.println(
//				CoPostscriptUtil.psNum(pixelSize.getHeight()) + CoPostscriptUtil.psNum(pixelSize.getWidth()) + "scale");
//		}

		writer.println(
			"/w "
				+ CoPostscriptUtil.psNum(m_image.getWidth())
				+ "def "
				+ "/h "
				+ CoPostscriptUtil.psNum(m_image.getHeight())
				+ "def w h scale");

		int numBands = m_image.getData().getNumBands();

		if (CoPostscriptGenerator.POSTSCRIPT_DEBUG_IMAGES) {
			writer
				.print("<<\n" + "/ImageType 1\n" + "/Width w\n" + "/Height h\n" + "/ImageMatrix [w 0 0 h neg 0 0]\n" +
			// invert Y due to inverted coordinate system
			"/BitsPerComponent 8\n"
				+ "/MultipleDataSources false\n"
				+ "/DataSource currentfile /ASCII85Decode filter /DCTDecode filter\n"
				+ "/Decode [");
			for (int i = 0; i < numBands; i++) {
				writer.print("0 1 ");
			}
			writer.println("]\n" + ">>");

			// FIXME: should print actual number of hex lines to be written...
			writer.println("%%BeginData: (unknown length)");
			writer.println("image");
			ascii85Encode(m_image, writer);
			writer.println(">\n%%EndData"); // ">" is EOD marker
		} else {
			writer.println("0.5 setgray 0 0 w h neg rectfill");
		}

		writer.println("gr");
	}

	protected void ascii85Encode(RenderedImage image, CoPostscriptWriter writer) {
		try {
			ImageIO.write(m_image, "jpg", new Ascii85OutputStream(writer.getFlushedOutputStream()));
		} catch (IOException e) {
		}

	}

	public void preparePostscript(CoPostscriptHolder psHolder) {
		// PENDING: Don't assume all images to be full color
		// Also: how detect spot colors in images?
		CoPostscriptUtil.registerCMYK(psHolder);
	}
}