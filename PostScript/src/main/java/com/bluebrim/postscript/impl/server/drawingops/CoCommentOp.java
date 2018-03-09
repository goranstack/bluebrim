package com.bluebrim.postscript.impl.server.drawingops;

import com.bluebrim.postscript.impl.shared.*;

/**
 * Write a comment to the postscript file. Makes no difference on the output rendering.
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public class CoCommentOp extends CoDrawingOperation {
	private String m_comment;
public CoCommentOp(String comment) {
	m_comment = comment;
}

public void generatePostscript(CoPostscriptHolder psHolder) {
	if (CoPostscriptGenerator.POSTSCRIPT_DEBUG_COMMENTS) {
		psHolder.includeComment(m_comment);
	}
}
}