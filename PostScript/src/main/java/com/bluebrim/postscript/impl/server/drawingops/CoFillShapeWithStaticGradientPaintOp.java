package com.bluebrim.postscript.impl.server.drawingops;

import java.awt.*;
import java.awt.geom.*;

import com.bluebrim.postscript.impl.shared.*;

/**
 * Fills a shape using the specified static (non-cyclic) GradientPaint. This drawing operation must only 
 * be usec if the GradientPaint really is non-cyclic. For cyclic gradients, use 
 * CoFillShapeWithCyclicGradientPaint.
 *
 * Here is the commented and indented version of the postscript procedure used.
 <pre>
/staticGradPat {
3 dict begin
/c2 exch def % Define first color
/c1 exch def % Define second color
/coords exch def % Define coordinates

% Define corresponding gradient fill pattern dictionary
<<  
	/PatternType 2
	/Shading <<
		/ShadingType 2
		/ColorSpace [ /DeviceCMYK ]
		/Coords coords
		/Extend [ true true ]
		/Function <<
			/FunctionType 2
			/Domain [ 0 1 ]
			/C0 c1
			/C1 c2
			/N 1
		>>
	>>
>>
end

% Prepare and set this as filling pattern
matrix makepattern setpattern 
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
 * @see CoFillShapeWithCyclicGradientPaintOp
 */
public class CoFillShapeWithStaticGradientPaintOp extends CoDrawingOperation {
	private Shape m_shape;
	private GradientPaint m_gradient;
public void generatePostscript(CoPostscriptHolder psHolder) {
	psHolder.includeComment("CoFillShapeWithStaticGradientPaintOp shape -> " + m_shape + " gradientPaint -> " + m_gradient);

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
	// First output the path for the Shape.
	psHolder.getWriter().print(CoPostscriptUtil.tracePath(m_shape));

	// Output coordinates
		
	Point2D point1 = m_gradient.getPoint1();
	Point2D point2 = m_gradient.getPoint2();

	psHolder.getWriter().print("[ " + CoPostscriptUtil.psX(point1.getX()) + CoPostscriptUtil.psY(point1.getY()) +
		CoPostscriptUtil.psX(point2.getX()) + CoPostscriptUtil.psY(point2.getY()) + "] ");

	// Output colors
	psHolder.getWriter().print("[ ");
	CoPostscriptUtil.writeColorDefinition(m_gradient.getColor1(), psHolder.getWriter());
	psHolder.getWriter().print("] [ ");
	CoPostscriptUtil.writeColorDefinition(m_gradient.getColor2(), psHolder.getWriter());
	psHolder.getWriter().print("] statGradPat ");
	
	if (CoPostscriptUtil.isNonZeroWinding(m_shape)) {
		// Fill a path using the postscript default non-zero winding rule.
		psHolder.getWriter().println("fill");
	} else {
		// Fill a path using the alternative odd-even winding rule
		psHolder.getWriter().println("eofill");
	}

	psHolder.getWriter().println("gr");
}


public CoFillShapeWithStaticGradientPaintOp(Shape shape, GradientPaint gradient) {
	com.bluebrim.base.shared.debug.CoAssertion.preCondition(!gradient.isCyclic(), "GradientPaint is not static (non-cyclic)");

	m_shape = shape;
	m_gradient = gradient;
}


/**
 * Register postscript procedures used by static gradient fill.
 * See class JavaDoc comment for full and commented source code to the postscript procedure.
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */

public void preparePostscript(CoPostscriptHolder psHolder) throws CoPostscriptLanguageLevelException {
	// We need Level 3

	if (psHolder.getTarget().getLevel() < 3) {
		if (psHolder.getTarget().isRequireHighPrecision()) {
			// Well then, abort
			throw new CoPostscriptLanguageLevelException("Can't reproduce static gradient paint with less than Postscript Language Level 3");
		}
		// Otherwise we just skip the drawing part, and don't register any functions
	} else {
		psHolder.registerLanguageLevel(3);
		
		psHolder.registerPostscriptFunction("statGradPat", 
			"3 dict begin /c2 exch def /c1 exch def /coords exch def " +
			"<< /PatternType 2 /Shading << /ShadingType 2 /ColorSpace [ /DeviceCMYK ] " + 
			"/Coords coords /Extend [ true true ] /Function << /FunctionType 2 " + 
			"/Domain [ 0 1 ] /C0 c1 /C1 c2 /N 1 >> >> >> end " + 
			"matrix makepattern setpattern", 
			"Static gradient fill", "<coordinates> <color1> <color2>", "-");
	}
	
	CoPostscriptUtil.registerColor(m_gradient.getColor1(), psHolder);
	CoPostscriptUtil.registerColor(m_gradient.getColor2(), psHolder);
}
}