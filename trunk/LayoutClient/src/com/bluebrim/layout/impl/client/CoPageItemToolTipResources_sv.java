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
//		{ CoTopLeftLocationIF.TOP_LEFT_LOCATION, "Uppe till vänster" },
//		{ CoTopRightLocationIF.TOP_RIGHT_LOCATION, "Uppe till höger" },
//		{ CoBottomRightLocationIF.BOTTOM_RIGHT_LOCATION, "Nere till höger " },
//		{ CoBottomLeftLocationIF.BOTTOM_LEFT_LOCATION, "Nere till vänster" },
		{ CoLeftLocationIF.LEFT_LOCATION, "Till vänster " },
		{ CoRightLocationIF.RIGHT_LOCATION, "Till höger" },
		{ CoTopLocationIF.TOP_LOCATION, "Överst" },
		{ CoBottomLocationIF.BOTTOM_LOCATION, "Nederst " },
		{ CoCenterLocationIF.CENTER_LOCATION, "I mitten " },

		// Height Spec
		{ CoNoSizeSpecIF.NO_HEIGHT_SPEC, "Ingen höjdspecification" },
		{ CoAbsoluteHeightSpecIF.ABSOLUTE_HEIGHT_SPEC, "Absolut höjd" },
		{ CoFillHeightSpecIF.FILL_HEIGHT_SPEC, "Fyll ut på höjden" },		
		{ CoContentHeightSpecIF.CONTENT_HEIGHT_SPEC, "Låt innehållet styra" },		
		{ CoProportionalHeightSpecIF.PROPORTIONAL_HEIGHT_SPEC, "Proportionell höjd" },		
			
		// Height Spec
		{ CoNoSizeSpecIF.NO_WIDTH_SPEC, "Ingen breddspecification" },
		{ CoAbsoluteWidthSpecIF.ABSOLUTE_WIDTH_SPEC, "Absolut bredd" },
		{ CoFillWidthSpecIF.FILL_WIDTH_SPEC, "Fyll ut på bredden" },		
		{ CoContentWidthSpecIF.CONTENT_WIDTH_SPEC, "Låt innehållet styra" },		
		{ CoProportionalWidthSpecIF.PROPORTIONAL_WIDTH_SPEC, "Proportionell bredd" },		
	

	};	

/**
  * @return java.lang.Object[]
 */
public Object[][] getContents ( ) {
	return contents;
}
}