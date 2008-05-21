package com.bluebrim.postscript.impl.server.drawingops;

import com.bluebrim.postscript.impl.shared.*;

/**
 * Translates the user space in postscript. This is equivalent of concatenating a translation matrix to the CTM
 * (current transform matrix). Make sure to push the graphics state before doing this.
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 * 
 * @see CoRotateOp
 * @see CoScaleOp
 * @see CoPushGraphicsStateOp
 */

public class CoTranslateOp extends CoDrawingOperation {
	private double m_x;
	private double m_y;
public CoTranslateOp(double x, double y) {
	m_x = x;
	m_y = y;
}

public void generatePostscript(CoPostscriptHolder psHolder) {
	psHolder.includeComment("CoTranslateOp x -> " + m_x + " " + m_y);

	psHolder.getWriter().println(CoPostscriptUtil.psX(m_x) + CoPostscriptUtil.psY(m_y) + "translate");
}
}