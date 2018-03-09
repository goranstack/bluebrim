package com.bluebrim.postscript.impl.server.drawingops;

import java.awt.*;

import com.bluebrim.postscript.impl.shared.*;

/**
 * Draw a generic shape (outline) using a generic stroke (outlining method). This method generates more
 * clumsy postscript code than the BasicStroke Set/DrawWith combo, so they should be used if possible.
 * If now, this operation should be used, since it covers all possible combinations of Shapes and Strokes.
 * The color of the stroke is determined by the BasicPaint, which must have been previously set using
 * CoSetBasicPaintOp.
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public class CoDrawShapeWithGenericStrokeOp extends CoDrawingOperation {
	private Shape m_shape;
	private Stroke m_stroke;
public CoDrawShapeWithGenericStrokeOp(Shape shape, Stroke stroke) {
	m_shape = shape;
	m_stroke = stroke;
}

public void generatePostscript(CoPostscriptHolder psHolder) {

	psHolder.includeComment("CoDrawShapeOp shape -> " + m_shape + " stroke -> " + m_stroke);

	// Create a shape describing the outline of the stroke
	Shape strokeShape = m_stroke.createStrokedShape(m_shape);

	// First output the path for the Shape.
	psHolder.getWriter().print(CoPostscriptUtil.tracePath(strokeShape));

	if (CoPostscriptUtil.isNonZeroWinding(strokeShape)) {
		// Fill a path using the postscript default non-zero winding rule.
		psHolder.getWriter().println("fill");
	} else {
		// Fill a path using the alternative odd-even winding rule
		psHolder.getWriter().println("eofill");
	}
}
}