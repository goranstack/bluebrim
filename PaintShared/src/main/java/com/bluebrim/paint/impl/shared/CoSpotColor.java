package com.bluebrim.paint.impl.shared;
import java.awt.*;

import com.bluebrim.base.shared.*;

/**
 * Instances of CoSpotColor represents userdefined spot colors.
 * Spot colors represents specific inks and uses separate plates.
 *
 * Corrected for GS/J 4.1 by Markus Persson 2000-11-28
 *
 * Simple, but "incorrect" CMYK handling added by Magnus Ihse (magnus.ihse@appeal.se) (2001-07-23 13:46:42).
 * FIXME: This is not a correct solution. The spot color needs to be given a device-specific name ("postscriptName")
 * and, when generating postscript, the colorspace needs to be changed to N-colorspace and the spot color specified
 * as an extra color, outside the four regular CMYK colors.
 */
public class CoSpotColor extends com.bluebrim.paint.impl.shared.CoUserDefinedColor implements com.bluebrim.paint.impl.shared.CoSpotColorIF
{

	private transient Color m_cachedPreview;
	private int m_cyan;
	private int m_magenta;
	private int m_yellow;
	private int m_black;
	private int m_rgb;
	public static final String XML_B = "b";
	public static final String XML_G = "g";
	public static final String XML_R = "r";



public CoSpotColor() 
{
	this( Color.white );
}
public CoSpotColor( Color color ) 
{
	this( color, CoStringResources.getName( CoConstants.UNTITLED ) );
}
public CoSpotColor(Color color, String name) {
	super();
	m_name = name;
	setColor( color );
}
public void copyFrom(com.bluebrim.paint.impl.shared.CoEditableColorIF source) {
	com.bluebrim.paint.impl.shared.CoSpotColorIF src = (com.bluebrim.paint.impl.shared.CoSpotColorIF) source;

	m_name = new String(src.getName());
	setColor(src.getPreviewColor());
}
public com.bluebrim.paint.impl.shared.CoEditableColorIF createObject()
{
	return new CoSpotColor();
}

public boolean equals(Object o) {
	// NOTE: Inefficient to check super.equals()! /Markus 2000-11-28
	return super.equals(o) && (o instanceof CoSpotColor)
		&& (((CoSpotColor) o).m_rgb == m_rgb);
}
public String getFactoryKey (){
	return SPOT_COLOR;
}
public Color getPreviewColor() {
	if (m_cachedPreview == null) {
		return m_cachedPreview = new Color(m_rgb);
	}
	return m_cachedPreview;
}
public String getType ( )
{
	return SPOT_COLOR;
}
public void setColor(Color c) {
	m_cachedPreview = c;
	m_rgb = c.getRGB();
	calculateCMYK(c);
	markDirty();
}
public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor)
{
	super.xmlVisit( visitor );

	Color c = getPreviewColor();
	
	visitor.exportAttribute( XML_R, Integer.toString( c.getRed() ) );
	visitor.exportAttribute( XML_G, Integer.toString( c.getGreen() ) );
	visitor.exportAttribute( XML_B, Integer.toString( c.getBlue() ) );
}
	





public float getBlackPercentage()
{
	return m_black;
}

public float getCyanPercentage()
{
	return m_cyan;
}

public float getMagentaPercentage()
{
	return m_magenta;
}

public float getYellowPercentage()
{
	return m_yellow;
}

public void xmlInit( java.util.Map attributes, com.bluebrim.xml.shared.CoXmlContext context )
{
	super.xmlInit( attributes, context );

	Color c = getPreviewColor();
	
	int r = com.bluebrim.xml.shared.CoXmlUtilities.parseInt( (String) attributes.get( XML_R ), c.getRed() );
	int g = com.bluebrim.xml.shared.CoXmlUtilities.parseInt( (String) attributes.get( XML_G ), c.getGreen() );
	int b = com.bluebrim.xml.shared.CoXmlUtilities.parseInt( (String) attributes.get( XML_B ), c.getBlue() );

	setColor( new Color( r, g, b ) );
}


protected void calculateCMYK(Color color) {
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

	// Adjust to percentage
	m_cyan = cyan * 100 / 255;
	m_magenta = magenta * 100 / 255;
	m_yellow = yellow * 100 / 255;
	m_black = black * 100 / 255;
}

public com.bluebrim.paint.shared.CoColorIF deepClone()
{
	CoSpotColor tColor = (CoSpotColor )super.deepClone();

	// Not non-null anywhere, but if so would interfer with GS/J's NP list. /Markus 2000-11-28
/*
	if
		( m_colorSpace != null )
	{
		try
		{
			tColor.m_colorSpace = (com.bluebrim.paint.impl.shared.CoColorSpace )m_colorSpace.clone();
		}
		catch ( CloneNotSupportedException ex )
		{
			throw new RuntimeException( m_colorSpace.getClass() + ".clone() failed" );
		}
		
	}
*/
	
//	if ( m_halftoneColor != null ) tColor.m_halftoneColor = (com.bluebrim.paint.impl.shared.CoProcessColor )m_halftoneColor.deepClone();
	return tColor;
}
}