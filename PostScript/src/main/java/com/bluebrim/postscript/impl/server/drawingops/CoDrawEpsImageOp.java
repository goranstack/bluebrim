package com.bluebrim.postscript.impl.server.drawingops;
import java.awt.geom.*;

import com.bluebrim.postscript.impl.shared.*;

/**
 * Include an EPS image in the postscript code.
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public class CoDrawEpsImageOp extends CoDrawingOperation {

	private byte[] m_epsPostscriptDefinition;
	private Rectangle2D m_epsBoundingBox;

	public void generatePostscript(CoPostscriptHolder psHolder) {
		psHolder.includeComment("CoDrawEpsImageOp image BoundingBox -> " + m_epsBoundingBox);

		psHolder.getWriter().println("gs");

		// Adjust for fitting the coordinate system of the EPS into our coordinate system

		psHolder.getWriter().println(
			CoPostscriptUtil.psX(0) + CoPostscriptUtil.psY(m_epsBoundingBox.getHeight()) + "translate");
		psHolder.getWriter().println(CoPostscriptUtil.PS_COORDINATE_SCALE_FACTOR + " dup scale");

		psHolder.getWriter().println("BeginEPSF");
		if (CoPostscriptGenerator.POSTSCRIPT_DEBUG_IMAGES) {
			psHolder.getWriter().println("%%BeginDocument: (includedEPSfile)");
			psHolder.getWriter().writeln(m_epsPostscriptDefinition);
			psHolder.getWriter().println("%%EndDocument");
		} else {
			psHolder.getWriter().println(
				"0.5 setgray 0 0 "
					+ CoPostscriptUtil.psNum(m_epsBoundingBox.getWidth())
					+ CoPostscriptUtil.psNum(m_epsBoundingBox.getHeight())
					+ "rectfill");
		}
		psHolder.getWriter().println("EndEPSF");

		psHolder.getWriter().println("gr");
	}

	public CoDrawEpsImageOp(byte[] epsPostscriptDefinition, Rectangle2D epsBoundingBox) {
		m_epsPostscriptDefinition = epsPostscriptDefinition;
		m_epsBoundingBox = epsBoundingBox;
	}

	public void preparePostscript(CoPostscriptHolder psHolder) {
		psHolder.registerPostscriptFunction(
			"BeginEPSF",
			"/b4_Inc_state save def /dict_count countdictstack def /op_count count 1 sub def userdict begin /showpage { } def 0 setgray 0 setlinecap 1 setlinewidth 0 setlinejoin 10 setmiterlimit [ ] 0 setdash newpath /languagelevel where { pop languagelevel 1 ne { false setstrokeadjust false setoverprint } if } if",
			"Prepare for EPS inclusion",
			"-",
			"-");

		psHolder.registerPostscriptFunction(
			"EndEPSF",
			"count op_count sub { pop } repeat countdictstack dict_count sub { end } repeat b4_Inc_state restore",
			"Finalize after EPS inclusion",
			"-",
			"-");

		// PENDING: should really register the EPS document as a resource for DSC

		// PENDING: Don't assume all EPS to be full color
		// How do I detect color usage in EPS files? Possible solution: Look for process color DSC header --
		// if present, trust it, if absent, assume full color (or should the user be able to specify that an
		// EPS file is not full color?)
		CoPostscriptUtil.registerCMYK(psHolder);
	}
}