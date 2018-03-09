package com.bluebrim.postscript.impl.server.drawingops;

import java.awt.geom.*;

import com.bluebrim.postscript.impl.shared.*;

/**
 * Creates a rectangular clip in the postscript output. If the clipping area is not rectangular, use
 * CoGenericClipOp instead, which can handle arbitrary shaped clips. Also note that in postscript, there 
 * is no way of making the clip region larger, just to limit it more. The only way "back" is to restore 
 * a saved graphics state.
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see CoGenericClipOp
 */
public class CoRectangularClipOp extends CoDrawingOperation {
	private Rectangle2D m_clip;
public void generatePostscript(CoPostscriptHolder psHolder) {
	psHolder.includeComment("CoRectangularClipOp rectangle -> " + m_clip);

	psHolder.getWriter().println(
		CoPostscriptUtil.psX(m_clip.getMinX()) + 
		CoPostscriptUtil.psY(m_clip.getMinY()) +
		CoPostscriptUtil.psX(m_clip.getWidth()) + 
		CoPostscriptUtil.psY(m_clip.getHeight()) + "rectclip");
}


public CoRectangularClipOp(Rectangle2D clip) {
	m_clip = clip;
}
}