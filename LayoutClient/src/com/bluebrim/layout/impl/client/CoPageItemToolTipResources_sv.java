package com.bluebrim.layout.impl.client;

//import com.bluebrim.layout.impl.server.text.*;
import com.bluebrim.layout.impl.shared.*;

/**
 * @author: Dennis
 */

public class CoPageItemToolTipResources_sv extends CoPageItemUIStringResources
{
	static final Object[][] contents =
	{
		{ CoNoLocationIF.NO_LOCATION, "Ingen placeringsregel" },
//		{ CoTopLeftLocationIF.TOP_LEFT_LOCATION, "Uppe till v�nster" },
//		{ CoTopRightLocationIF.TOP_RIGHT_LOCATION, "Uppe till h�ger" },
//		{ CoBottomRightLocationIF.BOTTOM_RIGHT_LOCATION, "Nere till h�ger " },
//		{ CoBottomLeftLocationIF.BOTTOM_LEFT_LOCATION, "Nere till v�nster" },
		{ CoLeftLocationIF.LEFT_LOCATION, "Till v�nster " },
		{ CoRightLocationIF.RIGHT_LOCATION, "Till h�ger" },
		{ CoTopLocationIF.TOP_LOCATION, "�verst" },
		{ CoBottomLocationIF.BOTTOM_LOCATION, "Nederst " },
		{ CoCenterLocationIF.CENTER_LOCATION, "I mitten " },

		// Height Spec
		{ CoNoSizeSpecIF.NO_HEIGHT_SPEC, "Ingen h�jdspecification" },
		{ CoAbsoluteHeightSpecIF.ABSOLUTE_HEIGHT_SPEC, "Absolut h�jd" },
		{ CoFillHeightSpecIF.FILL_HEIGHT_SPEC, "Fyll ut p� h�jden" },		
		{ CoContentHeightSpecIF.CONTENT_HEIGHT_SPEC, "L�t inneh�llet styra" },		
		{ CoProportionalHeightSpecIF.PROPORTIONAL_HEIGHT_SPEC, "Proportionell h�jd" },		
			
		// Height Spec
		{ CoNoSizeSpecIF.NO_WIDTH_SPEC, "Ingen breddspecification" },
		{ CoAbsoluteWidthSpecIF.ABSOLUTE_WIDTH_SPEC, "Absolut bredd" },
		{ CoFillWidthSpecIF.FILL_WIDTH_SPEC, "Fyll ut p� bredden" },		
		{ CoContentWidthSpecIF.CONTENT_WIDTH_SPEC, "L�t inneh�llet styra" },		
		{ CoProportionalWidthSpecIF.PROPORTIONAL_WIDTH_SPEC, "Proportionell bredd" },		
	

	};	

/**
  * @return java.lang.Object[]
 */
public Object[][] getContents ( ) {
	return contents;
}
}