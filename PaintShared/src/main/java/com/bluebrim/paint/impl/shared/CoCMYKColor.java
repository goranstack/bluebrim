package com.bluebrim.paint.impl.shared;
import java.awt.*;

/**
 * Instances of CoCMYKColor represents a multi ink color whose components are
 * the process colors cyan, magenta, yellow and black.
 */
public class CoCMYKColor extends com.bluebrim.paint.impl.shared.CoMultiInkColor implements com.bluebrim.paint.impl.shared.CoCMYKColorIF 
{
	protected float m_blackPercentage;
	protected float m_cyanPercentage;
	protected float m_magentaPercentage;
	protected float m_yellowPercentage;
	public static final String XML_C = "c";
	public static final String XML_K = "k";
	public static final String XML_M = "m";
	public static final String XML_Y = "y";

public CoCMYKColor() 
{
	this( 0f, 0f, 0f, 0f );
}
public CoCMYKColor( float cyan, float magenta, float yellow, float black ) 
{
	super();
	m_cyanPercentage = cyan;
	m_magentaPercentage = magenta;
	m_yellowPercentage = yellow;
	m_blackPercentage = black;
}
public void copyFrom( com.bluebrim.paint.impl.shared.CoEditableColorIF source )
{
}
public com.bluebrim.paint.impl.shared.CoEditableColorIF createObject()
{
	return new CoCMYKColor();
}
public String getFactoryKey ()
{
	return FACTORY_KEY;
}
/*
 * Metoden görs klar senare
 */ 
public Color getPreviewColor(){

	//PostScript Language Reference Manual
	
	float red = (float)(1.0 - Math.min(1.0, (m_cyanPercentage + m_blackPercentage)/100.0));
	float green = (float)(1.0 - Math.min(1.0, (m_magentaPercentage + m_blackPercentage)/100.0));
	float blue = (float)(1.0 - Math.min(1.0, (m_yellowPercentage + m_blackPercentage)/100.0));
	
	return new Color(red, green, blue);
}
/**
 * This method was created in VisualAge.
 * @param cyan float
 */
public void setBlackPercentage(float black){

	m_blackPercentage = black;
	markDirty();
}
/**
 * Tom tills vidare. En bra konversion från RGB till CMYK måste ske
 * i den här metoden.
 */
public void setColor(Color c){
	markDirty();
}
/**
 * This method was created in VisualAge.
 * @param cyan float
 */
public void setCyanPercentage(float cyan){

	m_cyanPercentage = cyan;
	markDirty();
}
/**
 * This method was created in VisualAge.
 * @param cyan float
 */
public void setMagentaPercentage(float magenta){

	m_magentaPercentage = magenta;
	markDirty();
}
/**
 * This method was created in VisualAge.
 * @param cyan float
 */
public void setYellowPercentage(float yellow){

	m_yellowPercentage = yellow;
	markDirty();
}










public float getBlackPercentage()
{
	return m_blackPercentage;
}

public float getCyanPercentage()
{
	return m_cyanPercentage;
}

public float getMagentaPercentage()
{
	return m_magentaPercentage;
}

public float getYellowPercentage()
{
	return m_yellowPercentage;
}

public void xmlInit( java.util.Map attributes, com.bluebrim.xml.shared.CoXmlContext context )
{
	super.xmlInit( attributes, context );

	setCyanPercentage( com.bluebrim.xml.shared.CoXmlUtilities.parseFloat( (String) attributes.get( XML_C ), getCyanPercentage() ) );
	setMagentaPercentage( com.bluebrim.xml.shared.CoXmlUtilities.parseFloat( (String) attributes.get( XML_M ), getMagentaPercentage() ) );
	setYellowPercentage( com.bluebrim.xml.shared.CoXmlUtilities.parseFloat( (String) attributes.get( XML_Y ), getYellowPercentage() ) );
	setBlackPercentage( com.bluebrim.xml.shared.CoXmlUtilities.parseFloat( (String) attributes.get( XML_K ), getBlackPercentage() ) );

}

public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor)
{
	super.xmlVisit(visitor);
	
	visitor.exportAttribute( XML_C, Float.toString( getCyanPercentage() ) );
	visitor.exportAttribute( XML_M, Float.toString( getMagentaPercentage() ) );
	visitor.exportAttribute( XML_Y, Float.toString( getYellowPercentage() ) );
	visitor.exportAttribute( XML_K, Float.toString( getBlackPercentage() ) );
}
}