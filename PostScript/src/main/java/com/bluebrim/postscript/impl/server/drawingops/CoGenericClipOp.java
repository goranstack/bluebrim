package com.bluebrim.postscript.impl.server.drawingops;

import java.awt.*;

import com.bluebrim.postscript.impl.shared.*;

/**
 * Creates a generic clip in the postscript output. If the clipping area is rectangular, use
 * CoRectangularClipOp instead for efficiency and enhanced postscript code readability. Also note that
 * in postscript, there is no way of making the clip region larger, just to limit it more. The only way
 * "back" is to restore a saved graphics state.
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see CoRectangularClipOp
 */
public class CoGenericClipOp extends CoDrawingOperation {
	private Shape m_clip;
public CoGenericClipOp(Shape clip) {
	m_clip = clip;
}


public void generatePostscript(CoPostscriptHolder psHolder) {
	psHolder.includeComment("CoGenericClipOp shape -> " + m_clip);

	// First output the path for the Shape.
	psHolder.getWriter().print(CoPostscriptUtil.tracePath(m_clip));

	// Then we clip it.
	if (CoPostscriptUtil.isNonZeroWinding(m_clip)) {
		// Use non-zero winding rule
		psHolder.getWriter().println("clip");
	} else {
		// Use even-odd winding rule
		psHolder.getWriter().println("eoclip");
	}
}
}