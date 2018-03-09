package com.bluebrim.postscript.impl.server.drawingops;

import java.awt.*;

import com.bluebrim.postscript.impl.shared.*;

/**
 * Draws a generic shape using the basic stroke. The basic stroke (i.e. the postscript stroke mechanism)
 * must have been correctly set with CoSetBasicStrokeOp in this graphics context before using this 
 * drawing operation.
 * The color of the stroke is determined by the BasicPaint, which must have been previously set using
 * CoSetBasicPaintOp.
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public class CoDrawShapeWithBasicStrokeOp extends CoDrawingOperation {
	private Shape m_shape;
public void generatePostscript(CoPostscriptHolder psHolder) {

	psHolder.includeComment("CoDrawShapeWithBasicStrokeOp shape -> " + m_shape);

	// First output the path for the Shape.
	psHolder.getWriter().print(CoPostscriptUtil.tracePath(m_shape));
	// Then we stroke it.
	psHolder.getWriter().println("stroke");
}


public CoDrawShapeWithBasicStrokeOp(Shape shape) {
	m_shape = shape;
}
}