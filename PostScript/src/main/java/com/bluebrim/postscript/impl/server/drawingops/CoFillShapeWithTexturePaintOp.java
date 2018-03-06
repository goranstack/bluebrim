package com.bluebrim.postscript.impl.server.drawingops;

import java.awt.*;

import com.bluebrim.postscript.impl.shared.*;

/**
 * NOT IMPLEMENTED -- Should fill a shape using a TexturePaint.
 * Idea: Generalize to a CoFillShapeWithGenericPaint, and base it on the Raster available from
 * the PaintContext in the Paint. This shouldn't be much harder than implementing a TexturePaint only,
 * and it should cover all generic Paints (more or less well, however).
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see CoFillShapeWithBasicPaintOp
 * @see CoFillShapeWithCyclicGradientPaintOp
 * @see CoFillShapeWithStaticGradientPaintOp
 */
public class CoFillShapeWithTexturePaintOp extends CoDrawingOperation {
	private Shape m_shape;
	private TexturePaint m_texture;
public void generatePostscript(CoPostscriptHolder psHolder) {
	psHolder.includeComment("CoSetPaintOp texturePaint -> " + m_texture);

	// PENDING: implement!
}


public CoFillShapeWithTexturePaintOp(Shape shape, TexturePaint texture) {
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue(false, "TexturePaint not implemented yet");
	
	m_shape = shape;
	m_texture = texture;
}
}