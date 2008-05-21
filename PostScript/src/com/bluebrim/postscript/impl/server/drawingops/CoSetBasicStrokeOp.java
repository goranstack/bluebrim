package com.bluebrim.postscript.impl.server.drawingops;


import java.awt.*;

import com.bluebrim.postscript.impl.shared.*;
import com.bluebrim.stroke.shared.*;

/**
 * Sets the basic stroke. To draw an object with the basic stroke, use CoDrawShapeWithBasicStrokeOp. Note that
 * any calls to Push/Pop GraphicsState will destroy the basic stroke.
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public class CoSetBasicStrokeOp extends CoDrawingOperation {
	private BasicStroke m_stroke;
public void generatePostscript(CoPostscriptHolder psHolder) {
	psHolder.includeComment("CoSetStrokeOp  m_stroke -> " + m_stroke);

	float lineWidth = m_stroke.getLineWidth();
	float miterLimit = m_stroke.getMiterLimit();
	int lineCap = m_stroke.getEndCap();
	int lineJoin = m_stroke.getLineJoin();
	float[] dashArray = m_stroke.getDashArray();
	float dashOffset = m_stroke.getDashPhase();

	if (dashArray == null) {
		dashArray = new float[0];
	}

	psHolder.getWriter().print("[ ");
	for (int i=0; i<dashArray.length; i++) {
		psHolder.getWriter().print(CoPostscriptUtil.psLength(dashArray[i]));
	}
		
	psHolder.getWriter().println("] " + CoPostscriptUtil.psLength(dashOffset) + 
		CoPostscriptUtil.psNum(miterLimit) + CoPostscriptUtil.psInt(lineCap) + 
		CoPostscriptUtil.psInt(lineJoin) + CoPostscriptUtil.psLength(lineWidth) + "baseStroke");
}


public CoSetBasicStrokeOp(BasicStroke stroke) {
	m_stroke = stroke;
}


public CoSetBasicStrokeOp(CoBasicStrokeProxy stroke) {
	m_stroke = stroke.getImplementation();
}


public void preparePostscript(CoPostscriptHolder psHolder) {
	psHolder.registerPostscriptFunction("baseStroke", 
		"setlinewidth setlinejoin setlinecap setmiterlimit setdash", "Set Basic Stroke", 
		"<dashArray> <dashOffset> <miterLimit> <lineCap> <lineJoin> <lineWidth>", "-");
}
}