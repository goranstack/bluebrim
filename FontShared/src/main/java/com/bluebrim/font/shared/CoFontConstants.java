package com.bluebrim.font.shared;
/**
 * Constants for specifying font faces.
 *
 * These constants are inspired by and at least used to try to follow
 * the CSS2 specification as closely as possible.
 *
 * @author Markus Persson
 * @author Dennis (?)
 * @author Magnus Ihse <magnus.ihse@appeal.se> (2001-05-03 13:18:52)
 * @author Markus Persson 2001-08-31 (Semi-restored order)
 */
public interface CoFontConstants {
	// NOTE: "public static final" is implicit in interfaces! /Markus

	// Axis enumeration
	int NOT_AN_AXIS = 0; // For NORMAL, DEFAULT and AS_IS if they will remain.
	int WEIGHT_AXIS = 1;
	int STYLE_AXIS = 2;
	int VARIANT_AXIS = 3;
	int STRETCH_AXIS = 4;
 

	// IMPORTANT: If these values are ever changed, make sure the distanceTo method in int
	// is updated to take care of these new values, and try to make these values fit as good as possible
	// in the current value space.


	// Common
//	int NORMAL = 0;
	// Weird
	int DEFAULT = -9999;
	int AS_IS = -9998;

	// Style
	int ROMAN = 0;
	int ITALIC = 1;
	int NORMAL_STYLE = ROMAN;
	
	// Note: Oblique is considered to be an operation performed on a typeface (slanting it), rather than a
	// specification of a font face. Serif fonts which has a "italic" counterpart named "Oblique" is still
	// considered to be ITALIC for this purpose. /Ihse (CSS2 violation! /Markus)
//	int OBLIQUE = 2;

	
	// Variant
	int NORMAL_VARIANT = 0;
	int SMALL_CAPS = 1;

	
	// Weight
	int W100 = -300;
	int W200 = -200;
	int W300 = -100;
	int W400 = 0;
	int W500 = 100;
	int W600 = 200;
	int W700 = 300;
	int W800 = 400;
	int W900 = 500;

	// Convenience names. Note that this is more of a suggestion of reasonable values for e.g. a
	// semibold font, than a normative regulation. /Ihse
	// (Actually NORMAL and BOLD are CSS2 alternative names for weight, which may or may not
	// coincide with the name assigned by the font maker/vendor. /Markus)
	int THIN = W100;
	int EXTRALIGHT = W200;
	int ULTRALIGHT = W200;
	int LIGHT = W300;
	int NORMAL_WEIGHT = W400;
	int MEDIUM = W500;
	int BOOK = W500;
	int SEMIBOLD = W600;
	int DEMIBOLD = W600;
	int BOLD = W700;
	int ULTRABOLD = W800;
	int EXTRABOLD = W800;
	int BLACK = W900;
	int HEAVY = W900;

	
	// Stretch
	int S100 = -400;
	int S200 = -300;
	int S300 = -200;
	int S400 = -100;
	int S500 = 0;
	int S600 = 100;
	int S700 = 200;
	int S800 = 300;
	int S900 = 400;

	// Convenience names. Note that this is more of a suggestion of reasonable values
	// than a normative regulation. /Ihse
	// (Actually these are the CSS2 assigned names for stretch, which may or may not
	// coincide with the name assigned by the font maker/vendor. /Markus)
	int ULTRA_CONDENSED = S100;
	int EXTRA_CONDENSED = S200;
	int CONDENSED = S300;
	int SEMI_CONDENSED = S400;
	int NORMAL_STRETCH = S500;
	int SEMI_EXPANDED = S600;
	int EXPANDED = S700;
	int EXTRA_EXPANDED = S800;
	int ULTRA_EXPANDED = S900;


/*	
	// List of possible values for each axis.
	int[] STYLE_OPTIONS = new int[] {
		NORMAL_STYLE, ITALIC };

	int[] VARIANT_OPTIONS = new int[] {
		NORMAL_VARIANT, SMALL_CAPS };

	int[] WEIGHT_OPTIONS = new int[] {
		W100, W200, W300, W400, W500, W600, W700, W800, W900 };

	int[] STRETCH_OPTIONS = new int[] {
		ULTRA_CONDENSED, EXTRA_CONDENSED, CONDENSED, SEMI_CONDENSED, NORMAL_STRETCH,
		SEMI_EXPANDED, EXPANDED, EXTRA_EXPANDED, ULTRA_EXPANDED };

	// Index to the default, normal, attribute of each *_OPTION array.
	int STYLE_OPTIONS_DEFAULT = 0;
	int VARIANT_OPTIONS_DEFAULT = 0;
	int WEIGHT_OPTIONS_DEFAULT = 3;
	int STRETCH_OPTIONS_DEFAULT = 4;
*/

}