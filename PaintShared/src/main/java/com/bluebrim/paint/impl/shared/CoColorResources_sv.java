package com.bluebrim.paint.impl.shared;

public class CoColorResources_sv extends com.bluebrim.paint.impl.shared.CoColorResources {
	static final Object[][] contents =
	{
		// Color types
		{com.bluebrim.paint.impl.shared.CoNoColorIF.NO_COLOR,						"Ingen färg"},
		{com.bluebrim.paint.impl.shared.CoSpotColorIF.SPOT_COLOR,					"Dekorfärg"},				
		{com.bluebrim.paint.impl.shared.CoWhiteColorIF.WHITE_COLOR,				"Vit"},									
		{com.bluebrim.paint.impl.shared.CoProcessColorIF.PROCESS_COLOR,			"Process färg"},					
		{com.bluebrim.paint.impl.shared.CoMultiInkColorIF.MULTI_INK_COLOR,			"Multi-Ink färg"},			
		{com.bluebrim.paint.impl.shared.CoUserDefinedColorIF.USER_DEFINED_COLOR,	"Egendefinierad färg"},
		{com.bluebrim.paint.impl.shared.CoRegistrationColorIF.REGISTRATION_COLOR,	"Passmärke"},	

		// Process Colors
		{com.bluebrim.paint.impl.shared.CoProcessCyanIF.PROCESS_CYAN,				"Cyan"},				
		{com.bluebrim.paint.impl.shared.CoProcessBlackIF.PROCESS_BLACK,			"Svart"},
		{com.bluebrim.paint.impl.shared.CoProcessYellowIF.PROCESS_YELLOW,			"Gul"},			
		{com.bluebrim.paint.impl.shared.CoProcessMagentaIF.PROCESS_MAGENTA,		"Magenta"},
	};
/**
 * @return java.lang.Object[]
 */
public Object[][] getContents ( ) {
	return contents;
}
}
