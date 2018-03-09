package com.bluebrim.postscript.impl.server.drawingops;

import java.awt.*;

import com.bluebrim.postscript.impl.shared.*;

/**
 * NOT IMPLEMENTED -- Defines a trap zone.
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
public class CoTrapZoneOp extends CoDrawingOperation {
	private Shape m_trapZone;
public CoTrapZoneOp(Shape trapZone) {
	m_trapZone = trapZone;
}


public void generatePostscript(CoPostscriptHolder psHolder) {
	// Do nothing during second pass.
}


public void preparePostscript(CoPostscriptHolder psHolder) {
	psHolder.registerPostscriptFunction("tz", "/Trapping /ProcSet findresource /settrapzone get exec", "settrapzone Shorthand", 
		"-", "-");

	// Notify holder that we need proper trapping settings
	psHolder.registerTrappingUse();

	psHolder.registerLanguageLevel(3);

	com.bluebrim.base.shared.debug.CoAssertion.assertTrue(CoPostscriptUtil.isNonZeroWinding(m_trapZone), "Can't handle even-odd winding rule");

	// First output the path for the Shape.
	psHolder.getSetupWriter().print(CoPostscriptUtil.tracePath(m_trapZone));
	
	// TrapZones only use non-zero winding rule
	psHolder.getSetupWriter().println("tz");
}
}