package com.bluebrim.text.shared;
import java.awt.*;

/**
 * Hints for rendering formatted text.
 * 
 * @author: Dennis Malmström
 */

public interface CoTextRenderingHints
{
	public static final RenderingHints.Key PAINT_FORMAT_CHARACTERS = new RenderingHints.Key( 0 )
	{
		public boolean isCompatibleValue( Object v )
		{
			return ( v == PAINT_FORMAT_CHARACTERS_ON ) || ( v == PAINT_FORMAT_CHARACTERS_OFF );
		}
	};
	public static final Object PAINT_FORMAT_CHARACTERS_ON 	= Boolean.TRUE;
	public static final Object PAINT_FORMAT_CHARACTERS_OFF 	= Boolean.FALSE;


	
	public static final RenderingHints.Key PAINT_DUMMY_TEXT = new RenderingHints.Key( 0 )
	{
		public boolean isCompatibleValue( Object v )
		{
			return ( v == PAINT_DUMMY_TEXT_ON ) || ( v == PAINT_DUMMY_TEXT_OFF );
		}
	};
	public static final Object PAINT_DUMMY_TEXT_ON 	= Boolean.TRUE;
	public static final Object PAINT_DUMMY_TEXT_OFF 	= Boolean.FALSE;
}
