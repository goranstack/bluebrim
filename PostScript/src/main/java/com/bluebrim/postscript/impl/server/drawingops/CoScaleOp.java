package com.bluebrim.postscript.impl.server.drawingops;

import com.bluebrim.postscript.impl.shared.*;

/**
 * Scales the user space in postscript. This is equivalent of concatenating a scaling matrix to the CTM
 * (current transform matrix). Make sure to push the graphics state before doing this.
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see CoRotateOp
 * @see CoTranslateOp
 * @see CoPushGraphicsStateOp
 */
public class CoScaleOp extends CoDrawingOperation {
	private double m_sx;
	private double m_sy;
public CoScaleOp(double scaleX, double scaleY) {
	m_sx = scaleX;
	m_sy = scaleY;
}

public void generatePostscript(CoPostscriptHolder psHolder) {
	psHolder.includeComment("CoScaleOp sx -> " + m_sx + " sy -> " + m_sx);

	psHolder.getWriter().println(CoPostscriptUtil.psNum(m_sx) + CoPostscriptUtil.psNum(m_sy) + "scale");
}
}