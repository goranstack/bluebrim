package com.bluebrim.postscript.impl.server.drawingops;

import java.awt.*;

import com.bluebrim.postscript.impl.shared.*;

/**
 * Fills a shape with the BasicPaint (i.e. a color). The BasicPaint (i.e. postscript fill style) must
 * previously have been correctly set in this graphics context using CoSetBasicPaintOp. Note that there 
 * currently is no generic fill method, in contrast to the draw methods.
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see CoFillShapeWithTexturePaintOp
 * @see CoFillShapeWithCyclicGradientPaintOp
 * @see CoFillShapeWithStaticGradientPaintOp
 */
public class CoFillShapeWithBasicPaintOp extends CoDrawingOperation {
	private Shape m_shape;
public CoFillShapeWithBasicPaintOp(Shape shape) {
	m_shape = shape;	
}


public void generatePostscript(CoPostscriptHolder psHolder) {
	psHolder.includeComment("CoFillOp shape -> " + m_shape);

	// First output the path for the Shape.
	psHolder.getWriter().print(CoPostscriptUtil.tracePath(m_shape));

	if (CoPostscriptUtil.isNonZeroWinding(m_shape)) {
		// Fill a path using the postscript default non-zero winding rule.
		psHolder.getWriter().println("fill");
	} else {
		// Fill a path using the alternative odd-even winding rule
		psHolder.getWriter().println("eofill");
	}
}
}