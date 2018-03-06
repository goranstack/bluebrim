package com.bluebrim.postscript.impl.shared;
import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;

import com.bluebrim.postscript.impl.server.color.*;
import com.bluebrim.postscript.shared.*;

/**
 * Utility class for postscript generation in DrawingOperations.
 *
 * <p><b>Creation date:</b> 2001-06-01
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */
public final class CoPostscriptUtil {
	private final static double FP_CUTOFF = 1.0E-12;  // consider everything smaller than this equal to 0.0
	public final static int PS_COORDINATE_SCALE_FACTOR = 100;	// scale all coordinates with this value
	
 	// Constants related to the charset used for postscript files. /Markus
 	public final static String ISO_LATIN_1_CONVERTER_KEY = "ISO-8859-1";
// 	public final static String ISO_LATIN_1_PS_ENCODING_NAME = "ISOLatin1Encoding";  // not used


	public static String getGlyphName(char ch) {
		String glyphName = CoPostscriptGlyphNames.charToGlyphName(ch);
	
		if (glyphName != null) {
			return ("/" + glyphName);
		} else {
			//PENDING: better solution needed
			System.out.println("Postscript generation problem: we have a unicode char with no glyph name. Printing @ instead.");
			System.out.println("Offending char: " + (int) ch);
			return ("/at");
		}
	}
	
	
	public static boolean isNonZeroWinding(Shape shape) {
		PathIterator path = shape.getPathIterator(null);
		
		return (path.getWindingRule() == PathIterator.WIND_NON_ZERO);
	}
	
	
	/**
	 * Returns a normalized value of a double, if the double is "close enough" to the normal value, otherwise
	 * the same value will be returned. E.g., if normalValue is 1.0, and the value is 0.9999999999, then 1.0 
	 * is returned.
	 * Creation date: (2001-06-27 14:13:35)
	 * @author Magnus Ihse (magnus.ihse@appeal.se)
	 */
	protected static double normalizeValue(double value, double normalValue) {
		if (Math.abs((value - normalValue)) < FP_CUTOFF) {
			return normalValue;
		} else {
			return value;
		}
	}
	
	
	public static String psInt(int value) {
		return value + " ";
	}
	
	public static String psLength(double length) {
		return psLong(Math.round(length * PS_COORDINATE_SCALE_FACTOR));
	}
	
	public static String psLong(long value) {
		return value + " ";
	}
	
	public static String psNum(double value) {
		/*
		long rounded = Math.round(value);
		if (Math.abs(value - rounded) < FP_CUTOFF) {	// Is this close enought to an integer?
			return rounded + " ";		// if so, print it as an integer
	 	} else {
			return value + " ";
	 	}
	 	*/
	 	return psNum((float) value);	// Force casting to float to get rid of float->double converting issues
	 									// i.e. lots of numbers like 39.999999952613708 instead of 40.0
	 									// Should really be fixed at the source by using double all the way
	}
	
	public static String psNum(float value) {
		long rounded = Math.round(value);
		if (Math.abs(value - rounded) < FP_CUTOFF) {	// Is this close enought to an integer?
			return rounded + " ";		// if so, print it as an integer
	 	} else {
			return value + " ";
	 	}
	}
	
	public static String psX(double xCoordinate) {
		return psLong(Math.round(xCoordinate * PS_COORDINATE_SCALE_FACTOR));
	}
	
	public static String psY(double yCoordinate) {
		// Negate Y values to compensate for postscript inverted Y-axis
		return psLong(Math.round(-yCoordinate * PS_COORDINATE_SCALE_FACTOR));
	}
	

	/**
	 * Given a Java2D <code>PathIterator</code> instance,
	 * this method translates that into a PostScript path.
	 */
	public static String tracePath(Shape shape) {
		StringBuffer s = new StringBuffer();
		float[] segment = new float[6];
		int segmentType;
		float penX = 0;
		float penY = 0;
		float startPathX = 0;
		float startPathY = 0;
	
		PathIterator pathIter = shape.getPathIterator(null);
		
		s.append("n ");		// newpath
		while (pathIter.isDone() == false) {
			segmentType = pathIter.currentSegment(segment);
			switch (segmentType) {
				case PathIterator.SEG_MOVETO :
					penX = segment[0];
					penY = segment[1];
					/* moveto marks the start of a new subpath
					 * and we need to remember that starting
					 * position so that we know where the
					 * pen returns to with a close path.
					 */
					startPathX = penX;
					startPathY = penY;
					s.append(psX(startPathX) + psY(startPathY) + "m ");	// moveto
					break;
					
				case PathIterator.SEG_LINETO :
					penX = segment[0];
					penY = segment[1];
					s.append(psX(penX) + psY(penY) + "l "); // lineto
					break;
	
					/* Convert the quad path to a bezier.*/
				case PathIterator.SEG_QUADTO :
					float c1x = penX + (segment[0] - penX) * 2 / 3;
					float c1y = penY + (segment[1] - penY) * 2 / 3;
					float c2x = segment[2] - (segment[2] - segment[0]) * 2 / 3;
					float c2y = segment[3] - (segment[3] - segment[1]) * 2 / 3;
					penX = segment[2];
					penY = segment[3];
					s.append(psX(c1x) + psY(c1y) + psX(c2x) + psY(c2y) + psX(penX) + psY(penY) + "C ");
					break;
					
				case PathIterator.SEG_CUBICTO :
					penX = segment[4];
					penY = segment[5];
					s.append(psX(segment[0]) + psY(segment[1]) + psX(segment[2]) + psY(segment[3]) +
						psX(segment[4]) + psY(segment[5]) + "C ");
					break;
					
				case PathIterator.SEG_CLOSE :
					penX = startPathX;
					penY = startPathY;
					s.append(" c "); // closepath
					break;
			}
			pathIter.next();
		}
		return s.toString();
	}
	
	private static float[] calculateCMYK(Color color) {
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();

		int cyan = 255 - red;
		int magenta = 255 - green;
		int yellow = 255 - blue;

		// Find minimum for black
		int black = cyan;
		if (black > magenta) {
			black = magenta;
		}
		if (black > yellow) {
			black = yellow;
		}

		// Subtract black part from CMY colors
		cyan -= black;
		magenta -= black;
		yellow -= black;

		float[] cmyk = new float[4];

		cmyk[0] = cyan / 255.0f;
		cmyk[1] = magenta / 255.0f;
		cmyk[2] = yellow / 255.0f;
		cmyk[3] = black / 255.0f;
		return cmyk;
	}

	
	public static void writeColorDefinition(Color color, PrintWriter writer) {
		float[] cmyk = calculateCMYK(color);
	
		float cyan = cmyk[0];
		float magenta = cmyk[1];
		float yellow = cmyk[2];
		float black = cmyk[3];
	
		// Special case for the two most common cases, black and white
		if (cyan == 0.0f && magenta == 0.0f && yellow == 0.0f) {
			if (black == 0.0f) {
				writer.print("cW ");	// pure white
				return;
			} else if (black == 1.0f) {
				writer.print("cK ");	// pure black
				return;
			}
		}
	
		writer.print(CoPostscriptUtil.psNum(cyan) + CoPostscriptUtil.psNum(magenta) + 
			CoPostscriptUtil.psNum(yellow) + CoPostscriptUtil.psNum(black));
	}
	
	public static void writeGlyphNames(StringBuffer chars, PrintWriter writer) {
		for (int i = 0; i < chars.length(); i++) {
			writer.print(getGlyphName(chars.charAt(i)));
		}
	}
	
	
	public static boolean needsTrapping(Paint paint) {
		if (! (paint instanceof Color) ) {
			return true;
		}
	
		Color color = (Color) paint;
		
		float[] cmyk = calculateCMYK(color);
	
		float cyan = cmyk[0];
		float magenta = cmyk[1];
		float yellow = cmyk[2];
		float black = cmyk[3];
	
		// Special case for the two most common cases, black and white
		if (cyan == 0.0f && magenta == 0.0f && yellow == 0.0f) {
			return false;
		} else {
			return true;
		}
	}
	
	
	public static void registerCMYK(CoPostscriptHolder psHolder) {
		psHolder.registerColorant(CoColorant.CYAN);
		psHolder.registerColorant(CoColorant.MAGENTA);
		psHolder.registerColorant(CoColorant.YELLOW);
		psHolder.registerColorant(CoColorant.BLACK);	
	}
	
	
	public static void registerColor(Color color, CoPostscriptHolder psHolder) {
		float[] cmyk = calculateCMYK(color);
	
		float cyan = cmyk[0];
		float magenta = cmyk[1];
		float yellow = cmyk[2];
		float black = cmyk[3];
	
		if (cyan > 0.0) {
			psHolder.registerColorant(CoColorant.CYAN);
		}
		if (magenta > 0.0) {
			psHolder.registerColorant(CoColorant.MAGENTA);
		}
		if (yellow > 0.0) {
			psHolder.registerColorant(CoColorant.YELLOW);
		}
		if (black > 0.0) {
			psHolder.registerColorant(CoColorant.BLACK);
		}
	
		// Special case for the two most common cases, black and white
		if (cyan == 0.0f && magenta == 0.0f && yellow == 0.0f) {
			if (black == 0.0f) {	// pure white
				psHolder.registerPostscriptFunction("cW", "0 0 0 0", "White CMYK definition", 
					"-", "<cyan> <magenta> <yellow> <black>");
			} else if (black == 1.0f) {	// pure black
				psHolder.registerPostscriptFunction("cK", "0 0 0 1", "Black CMYK definition", 
					"-", "<cyan> <magenta> <yellow> <black>");
			}
		}
	}
	
	
	public static void sortTrappingOrder(CoColorantSet colorants) {
		Comparator densityComparator = new Comparator() {
			public int compare(Object o1, Object o2) {
			// Yeah, this IS correct. I *am* allowed to throw a ClassCastException if the types are not
			// handled by this Comparator.
				CoColorant colorant1 = (CoColorant) o1;
				CoColorant colorant2 = (CoColorant) o2;
	
				// Returns a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
				return (new Double(colorant1.getNeutralDensity())).compareTo(new Double(colorant2.getNeutralDensity()));
			}
		};
	
		// Note that getTrappingOrder() gives us the actual list, not a clone. Therefore the sort will
		// affect the actual list, and no setTrappingOrder is needed.
		Collections.sort(colorants.getTrappingOrder(), densityComparator);
	}
}