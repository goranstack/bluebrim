package com.bluebrim.paint.impl.shared;
import java.awt.*;

/**
 * CoColorSpaceConverter class provides methods for converting colors
 * between different color spaces such as CMYK and RGB. 
 */
public class CoColorSpaceConverter 
{
public static float[] cmyk2rgb(float cyanPercentage, float magentaPercentage, float yellowPercentage, float blackPercentage )
{
	float redPercentage 	 = (float )( 100.0f - Math.min( 100.0f , cyanPercentage    + blackPercentage ) );
	float greenPercentage = (float )( 100.0f - Math.min( 100.0f , magentaPercentage + blackPercentage ) );
	float bluePercentage  = (float )( 100.0f - Math.min( 100.0f , yellowPercentage  + blackPercentage ) );
	
	return new float[]{ redPercentage , greenPercentage , bluePercentage }; 
}
public static Color cmyk2rgbColor(float cyanPercentage, float magentaPercentage, float yellowPercentage, float blackPercentage )
{
	float[] rgb = cmyk2rgb(cyanPercentage, magentaPercentage, yellowPercentage, blackPercentage);
	return new Color( rgb[0]/100.0f , rgb[1]/100.0f , rgb[2]/100.0f );
}
private static float min(float c, float m, float y)
{
	return Math.min( Math.min( c, m ), y );
}
public static float[] rgb2cmyk(float redPercentage, float greenPercentage, float bluePercentage)
{
	float cyanPercentage 	= (float )( 100.0f - redPercentage );
	float magentaPercentage = (float )( 100.0f - greenPercentage );
	float yellowPercentage  = (float )( 100.0f - bluePercentage );
	float blackPercentage   = (float )( min( cyanPercentage, magentaPercentage, yellowPercentage ) );
	
	return new float[]{ cyanPercentage , magentaPercentage , yellowPercentage , blackPercentage };
}
}
