package com.bluebrim.postscript.impl.server.drawingops;

import java.awt.*;

import com.bluebrim.postscript.impl.shared.*;

/**
 * Sets the basic paint (i.e. a color). To fill an object using the basic paint, use 
 * CoFillShapeWithBasicPaintOp. Note that any calls to Push/Pop GraphicsState will destroy the basic paint.
 * Also note that the basic paint determines the color of the outline drawn by any of the DrawShape operations.
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see CoFillShapeWithBasicPaintOp
 */
public class CoSetBasicPaintOp extends CoDrawingOperation {
	private Color m_color;
public void generatePostscript(CoPostscriptHolder psHolder) {
	psHolder.includeComment("CoSetPaintOp color -> " + m_color);

	CoPostscriptUtil.writeColorDefinition(m_color, psHolder.getWriter());
	psHolder.getWriter().println("sc");
}


public CoSetBasicPaintOp(Color color) {
	m_color = color;
}


public void preparePostscript(CoPostscriptHolder psHolder) {
	psHolder.registerPostscriptFunction("sc", "setcmykcolor", "setcmykcolor Shorthand", 
		"<cyan> <magenta> <yellow> <black>", "-");

	CoPostscriptUtil.registerColor(m_color, psHolder);
}
}