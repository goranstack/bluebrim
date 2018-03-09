package com.bluebrim.postscript.impl.server.drawingops;

import com.bluebrim.postscript.impl.shared.*;

/**
 * Rotates the user space in postscript. This is equivalent of concatenating a rotation matrix to the CTM
 * (current transform matrix). Make sure to push the graphics state before doing this.
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see CoScaleOp
 * @see CoTranslateOp
 * @see CoPushGraphicsStateOp
 */
public class CoRotateOp extends CoDrawingOperation {
	private double m_angle;
	private double m_x;
	private double m_y;
public CoRotateOp(double angle) {
	this(angle, 0.0, 0.0);
}

public CoRotateOp(double angle, double x, double y) {
	m_angle = angle;
	m_x = x;
	m_y = y;
}


public void generatePostscript(CoPostscriptHolder psHolder) {
	psHolder.includeComment("CoRotateOp  x -> " + m_x + "  y -> " + m_y + "  angle -> " + m_angle);

	if (m_x != 0.0 || m_y != 0.0) { // only translate if neccessary
		psHolder.getWriter().println(CoPostscriptUtil.psX(m_x) + CoPostscriptUtil.psY(m_y) + "translate");
	}

	// Negate theta due to inverted coordinate system
	psHolder.getWriter().println(CoPostscriptUtil.psNum(-Math.toDegrees(m_angle)) + "rotate");
	
	if (m_x != 0.0 || m_y != 0.0) {		// do a reverse translation
		psHolder.getWriter().println(CoPostscriptUtil.psX(-m_x) + CoPostscriptUtil.psY(-m_y) + "translate");
	}
}
}