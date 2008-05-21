package com.bluebrim.postscript.impl.server.drawingops;
import com.bluebrim.postscript.impl.shared.*;

/**
 * Pushes the postscript graphics state on the postscript graphics stack (i.e. performes a gsave).
 * The postscript graphics state contains, among other things, the current paint, current font, current clip
 * and current transform matrix.
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see CoPopGraphicsStateOp
 */
public class CoPushGraphicsStateOp extends CoDrawingOperation {
public CoPushGraphicsStateOp() {
}


public void generatePostscript(CoPostscriptHolder psHolder) {
	psHolder.includeComment("CoPushTransformOp");

	psHolder.getWriter().println("gs");
}
}