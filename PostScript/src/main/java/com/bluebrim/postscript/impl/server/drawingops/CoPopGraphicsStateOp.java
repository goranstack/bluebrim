package com.bluebrim.postscript.impl.server.drawingops;
import com.bluebrim.postscript.impl.shared.*;

/**
 * Pops the postscript graphics state from the postscript graphics stack (i.e. performes a gsave).
 * The postscript graphics state contains, among other things, the current paint, current font, current clip
 * and current transform matrix.
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see CoPushGraphicsStateOp
 */
public class CoPopGraphicsStateOp extends CoDrawingOperation {
public CoPopGraphicsStateOp() {
}


public void generatePostscript(CoPostscriptHolder psHolder) {
	psHolder.includeComment("CoPopTransformOp");

	psHolder.getWriter().println("gr");
}
}