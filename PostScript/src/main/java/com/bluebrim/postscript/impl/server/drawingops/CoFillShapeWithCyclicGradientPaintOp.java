package com.bluebrim.postscript.impl.server.drawingops;

import java.awt.*;
import java.awt.geom.*;

import com.bluebrim.postscript.impl.shared.*;

/**
 * Fills a shape using the specified cyclic GradientPaint. This drawing operation must only be usec if 
 * the GradientPaint really is cyclic. For non-cyclic (static) gradients, use CoFillShapeWithStaticGradientPaint.<p>
 *
 * Here is the commented and indented version of the postscript procedures:
 <pre>
 /cyclGradFill {
	4 dict begin
	/c2 exch def % Define first color
	/c1 exch def % Define second color
	/fM <<
		/FunctionType 2
		/Domain [ 0 1 ]
		/C0 c1
		/C1 c2
		/N 1
	>> def

	<<
		/PatternType 2
		/Shading <<
			/ShadingType 2
			/ColorSpace [ /DeviceCMYK ]
			/Coords [ 0 0 0 1 ]
			/Function fM
		>>
	>> % Push pattern dictionary on stack

	clippath pathbbox % Extract current clip boundary box
	pop exch pop exch % Stack is now: <maxX> <minX>
	2 div floor 2 mul % Align minX to closest lower multiple of 2
	dup /xMin exch def % ... and call it minX

	4 sub % Make sure we are not missing any part of the fill area
	repGrad

	fM /C0 c2 put % Swap colors
	fM /C1 c1 put

	xMin 3 sub % Start again, but on an odd x position, not even
	repGrad

	pop pop % Pop off maxX and the pattern dictionary
	end % Close our temporary dictionary
} def
</pre>
 *
 * <p><b>Creation date:</b> 2001-08-10
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 *
 * @see CoFillShapeWithBasicPaintOp
 * @see CoFillShapeWithTexturePaintOp
 * @see CoFillShapeWithStaticGradientPaintOp
 */

public class CoFillShapeWithCyclicGradientPaintOp extends CoDrawingOperation {
	private Shape m_shape;
	private GradientPaint m_gradient;
public void generatePostscript(CoPostscriptHolder psHolder) {
	// FIXME: note: the fill operation should be either fill or eofill, depending on path type.
	// Must check path type and change fill operator, possibly by sending fill procedure as a
	// parameter to the repGrad function, and executing it instead of hard-coding fill.
	// Or I'll just make two different repGrad functions.

	psHolder.includeComment("CoFillShapeWithCyclicGradientPaintOp shape -> " + m_shape + " gradientPaint -> " + m_gradient);

	if (psHolder.getTarget().getLevel() < 3) {
		// We don't need to check for abort, this is done in the prepare method.
		// Just insert some basic level 2 stuff.

		psHolder.getWriter().println("gs");
		// Set color to one of the fill colors
		CoPostscriptUtil.writeColorDefinition(m_gradient.getColor1(), psHolder.getWriter());
		psHolder.getWriter().println("sc");

		// First output the path for the Shape.
		psHolder.getWriter().print(CoPostscriptUtil.tracePath(m_shape));

		if (CoPostscriptUtil.isNonZeroWinding(m_shape)) {
			// Fill a path using the postscript default non-zero winding rule.
			psHolder.getWriter().println("fill");
		} else {
			// Fill a path using the alternative odd-even winding rule
			psHolder.getWriter().println("eofill");
		}
		psHolder.getWriter().println("gr");

		return;
	}

	// If no level limits, use the Level 3 algorithm

	psHolder.getWriter().println("gs");

	Point2D point1 = m_gradient.getPoint1();
	Point2D point2 = m_gradient.getPoint2();

	// First output the path for the Shape.
	psHolder.getWriter().print(CoPostscriptUtil.tracePath(m_shape));
	psHolder.getWriter().println("clip"); // Make sure we don't fill outside the shape

	// Calculate how to transform the CTM
	double xDistance = point1.getX() - point2.getX();
	double yDistance = point1.getY() - point2.getY();
	double distance = Math.sqrt(xDistance * xDistance + yDistance * yDistance);
	double angle = (360 - Math.toDegrees(-Math.atan2(xDistance, yDistance))) % 360.0;
	// PENDING: this expression could probably be cleaned up a bit. But hey, at least it works :)

	// Now transform the CTM so that point 1 lies on (0,0) and point 2 lies on (0, 1)

	psHolder.getWriter().println(CoPostscriptUtil.psX(point1.getX()) + CoPostscriptUtil.psY(point1.getY()) + "translate");

	// Negate theta due to inverted coordinate system
	psHolder.getWriter().println(CoPostscriptUtil.psNum((float) angle) + "rotate");

	psHolder.getWriter().println(CoPostscriptUtil.psLength(distance) + "dup scale");

	psHolder.getWriter().print("[ ");
	CoPostscriptUtil.writeColorDefinition(m_gradient.getColor1(), psHolder.getWriter());
	psHolder.getWriter().print("] [ ");
	CoPostscriptUtil.writeColorDefinition(m_gradient.getColor2(), psHolder.getWriter());
	psHolder.getWriter().println("] cyclGradFill");

	psHolder.getWriter().println("gr");
}


public CoFillShapeWithCyclicGradientPaintOp(Shape shape, GradientPaint gradient) {
	com.bluebrim.base.shared.debug.CoAssertion.preCondition(gradient.isCyclic(), "GradientPaint is not cyclic");
	
	m_shape = shape;
	m_gradient = gradient;
}


/**
 * Register postscript procedures used by gradient fill.
 * See class JavaDoc comment for full and commented source code to the postscript procedure.
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */

public void preparePostscript(CoPostscriptHolder psHolder) throws CoPostscriptLanguageLevelException {
	// We need Level 3

	if (psHolder.getTarget().getLevel() < 3) {
		if (psHolder.getTarget().isRequireHighPrecision()) {
			// Well then, abort
			throw new CoPostscriptLanguageLevelException("Can't reproduce cyclic gradient paint with less than Postscript Language Level 3");
		}
		// Otherwise we just skip the drawing part, and don't register any functions
	} else {
		psHolder.registerLanguageLevel(3);
		
		psHolder.registerPostscriptFunction("repGrad", 
			"{ 2 add " + 
			"dup 0 exch matrix translate " +
			"3 index exch makepattern " + 
			"setpattern " + 
			"gs fill gr " + 
			"2 copy le { exit } if " +
			"} loop pop", 
		"Repeated gradient fill", "<pattern> <xMax> <xStart>", "<pattern> <xMax>");

		psHolder.registerPostscriptFunction("cyclGradFill",
		"4 dict begin " + 
		"/c2 exch def /c1 exch def " + 
		"/fM << /FunctionType 2 /Domain [ 0 1 ] /C0 c1 /C1 c2 /N 1 >> def " + 
		"<< /PatternType 2 /Shading << /ShadingType 2 /ColorSpace [ /DeviceCMYK ] /Coords [ 0 0 0 1 ] /Function fM >> >> " + 
		"clippath pathbbox pop exch pop exch 2 div floor 2 mul dup /xMin exch def " + 
		"4 sub repGrad fM /C0 c2 put fM /C1 c1 put " + 
		"xMin 3 sub repGrad pop pop end ",
		"Cyclic gradient fill", "<color1> <color2>", "-");
	}

	CoPostscriptUtil.registerColor(m_gradient.getColor1(), psHolder);
	CoPostscriptUtil.registerColor(m_gradient.getColor2(), psHolder);
}
}