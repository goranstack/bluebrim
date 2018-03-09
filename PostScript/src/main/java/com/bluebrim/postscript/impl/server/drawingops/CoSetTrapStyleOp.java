package com.bluebrim.postscript.impl.server.drawingops;

import java.io.*;

import com.bluebrim.postscript.impl.server.color.*;
import com.bluebrim.postscript.impl.shared.*;

/**
 * NOT IMPLEMENTED -- Defines trap style for the following trap zones.
 *
 * PENDING: This should not be implemented either. Trap zones as a concept is removed from Calvin, and should instead be
 * implemented on a page-wide basis.
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @deprecated No replacement, implement page-wide trapping instead.
 */
public class CoSetTrapStyleOp extends CoDrawingOperation {
	private CoTrapStyle m_trapStyle;
public void generatePostscript(CoPostscriptHolder psHolder) {
	// Do nothing during second pass.
}


public CoSetTrapStyleOp(CoTrapStyle trapStyle) {
	m_trapStyle = trapStyle;
}


public void preparePostscript(CoPostscriptHolder psHolder) {
	psHolder.registerPostscriptFunction("tS", "/Trapping /ProcSet findresource /settrapparams get exec", "settrapparams Shorthand", 
		"-", "-");

	psHolder.registerLanguageLevel(3);

	PrintWriter writer = psHolder.getSetupWriter();
	
	writer.println("<<");
	writer.println(" /TrappingEnabled " + (m_trapStyle.isTrappingEnabled() ? "true" : "false"));
	writer.println(" /SlidingTrapLimit " + m_trapStyle.getSlidingTrapLimit());
	writer.println(" /ImageInternalTrapping " + (m_trapStyle.isImageInternalTrapping() ? "true" : "false"));
	writer.println(" /ImageToObjectTrapping " + (m_trapStyle.isImageToObjectTrapping() ? "true" : "false"));
	writer.println(" /ImageTrapPlacement /" + m_trapStyle.getImageTrapPlacement().getPlacement());
	writer.println(">> tS");
}
}