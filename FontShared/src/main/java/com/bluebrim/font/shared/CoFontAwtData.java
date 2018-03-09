package com.bluebrim.font.shared;
import java.io.*;
import java.util.*;

/**
 * All data that is needed by AWT to correctly display this font on the screen.
 * Creation date: (2001-05-03 15:08:00)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */

public interface CoFontAwtData extends Serializable, com.bluebrim.xml.shared.CoXmlEnabledIF {
	public final static String XML_TAG = "font-awt-data";
	public final static String XML_FONT_NAME = "font-name";
	public final static String XML_IS_BOLD = "bold";
	public final static String XML_IS_ITALIC = "italic";
	public final static String XML_IS_TYPE1 = "type1";

	
	/** FontFace is Type1. Only needed for AWT bug workaround.
	*/
	public boolean awtWorkaround_isType1();


	/** FontFace is Type1. Only needed for AWT bug workaround.
	*/
	public void awtWorkaround_setType1(boolean isType1);


	/**
	* Return the Map needed by AWT to correctly display this font.
	*/
	
	public Map getAwtAttributes();


	public String getFontName();


	public boolean isBold();


	public boolean isItalic();


	/** Tell AWT to display this font as bold.
	*/
	public void setBold(boolean isBold);


	public void setFontName(String fontName);


	/** Tell AWT to display this font as oblique.
	*/
	public void setItalic(boolean isItalic);
}