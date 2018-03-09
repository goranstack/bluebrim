package com.bluebrim.paint.impl.shared;
import java.awt.*;
import java.util.*;
import java.util.List;

import com.bluebrim.base.shared.*;

/**
 * Instances of com.bluebrim.paint.impl.shared.CoCMYKColor represents a multi ink color whose components are
 * the process colors cyan, magenta, yellow and black plus additionally defined
 * spot colors.
 */

public class CoExtendedMultiInkColor extends com.bluebrim.paint.impl.shared.CoMultiInkColor implements com.bluebrim.paint.impl.shared.CoExtendedMultiInkColorIF
{
	private transient Color m_previewColorCache;
	private transient float[][] m_grabbedColorsCache;


	private List m_shadedTrappableColors; // [ ShadedTrappableColor ]

	public static class ShadedTrappableColor implements com.bluebrim.xml.shared.CoXmlExportEnabledIF
	{
		public static final String XML_TAG = "shaded-trappable-color";
		public static final String XML_SHADE = "shade";
		
		float m_shade;
		com.bluebrim.paint.impl.shared.CoTrappableColorIF m_color;

		public ShadedTrappableColor()
		{
			this( null, 0 );
		}

		public ShadedTrappableColor( com.bluebrim.paint.impl.shared.CoTrappableColorIF color, float shade )
		{
			m_color = color;
			m_shade = shade;
		}

		public boolean equals( Object o )
		{
			return
				( o == this )
			||
				(
					( o instanceof ShadedTrappableColor )
				&&
					( ( (ShadedTrappableColor) o ).m_shade == m_shade )
				&&
					( ( (ShadedTrappableColor) o ).m_color.equals( m_color ) )
				);
		}
		
		public void xmlAddSubModel( String name, Object subModel, com.bluebrim.xml.shared.CoXmlContext context )
		{
			if
				( ( name == null ) && ( subModel instanceof com.bluebrim.paint.impl.shared.CoTrappableColorIF ) )
			{
				m_color = (com.bluebrim.paint.impl.shared.CoTrappableColorIF) subModel;
			} else {
				System.out.println(getClass().getName() + "  Ignoring sub model : " + subModel);
			}
		}
		
		public void xmlInit( java.util.Map attributes, com.bluebrim.xml.shared.CoXmlContext context )
		{
			m_shade = com.bluebrim.xml.shared.CoXmlUtilities.parseFloat( (String) attributes.get( XML_SHADE ), m_shade );
		}
	
		public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor )
		{
			visitor.exportAttribute( XML_SHADE, Float.toString( m_shade ) );
			visitor.export( m_color );			
		}	
	}




public CoExtendedMultiInkColor() 
{
	super();
	m_shadedTrappableColors = new ArrayList();
	/*
	m_shades = new ArrayList();
	m_trappableColors = new ArrayList();
	*/
}
public void addShadeColor( com.bluebrim.paint.impl.shared.CoTrappableColorIF trappableColor , float shade )
{
	m_shadedTrappableColors.add( new ShadedTrappableColor( trappableColor, shade ) );
//	m_trappableColors.add( trappableColor );		
//	m_shades.add( new Float(shade) );
	markDirty();
}
private boolean arrayEqual(float[][] first, float[][] second) {
	if (first.length != second.length)
		return false;

	for (int i = 0; i < first.length; i++) {
		for (int j = 0; j < 4; j++) {
			if (first[i][j] != second[i][j])
				return false;
		}
	}

	return true;
}
/*
 * This method calculates a resulting color by mixing
 * spot colors and process colors with different shades.
 * It is accomplished using Neugebauers equations for
 * color mixing.
 */
private float[] calcResultingColor(float[][] rgbaColors) {
	if (rgbaColors.length == 0)
		return new float[] {1f, 1f, 1f};

	// Loop limits.
	int colors = rgbaColors.length;
	int mixes = (1 << colors); // mixes = 2^colors

	float[] resultingColor = new float[] {0f, 0f, 0f};
	float[] mixedColor = new float[3];
	for (int mix = 0; mix < mixes; mix++) {
		float area = 1f; // Make it possible to use *=
		mixedColor[0] = mixedColor[1] = mixedColor[2] = 1f;	// White

		for (int color = 0, colorMask = mixes >>> 1; color < colors; color++, colorMask >>>= 1) {
			if ((mix & colorMask) != 0) {
				float[] colorToMixWith = rgbaColors[color];
				area *= colorToMixWith[3];
				mixedColor[0] *= colorToMixWith[0];
				mixedColor[1] *= colorToMixWith[1];
				mixedColor[2] *= colorToMixWith[2];
			} else
				area *= (1 - rgbaColors[color][3]);
		}
		resultingColor[0] += area * mixedColor[0];
		resultingColor[1] += area * mixedColor[1];
		resultingColor[2] += area * mixedColor[2];
	}
	return resultingColor;
}
public void copyFrom( com.bluebrim.paint.impl.shared.CoEditableColorIF source )
{
	m_name = new String( ((CoExtendedMultiInkColor )source).getName() );

	m_shadedTrappableColors.clear();
	Iterator i = ( (CoExtendedMultiInkColor) source ).m_shadedTrappableColors.iterator();
	while
		( i.hasNext() )
	{
		ShadedTrappableColor c = (ShadedTrappableColor) i.next();
		m_shadedTrappableColors.add( new ShadedTrappableColor( c.m_color, c.m_shade ) );
	}
}
public com.bluebrim.paint.impl.shared.CoEditableColorIF createObject()
{
	return new CoExtendedMultiInkColor();
}
public boolean equals( Object o )
{
	return
		super.equals( o ) &&
		( o instanceof CoExtendedMultiInkColor ) &&
		( (CoExtendedMultiInkColor) o ).m_shadedTrappableColors.equals( m_shadedTrappableColors );
//		( (CoExtendedMultiInkColor) o ).m_shades.equals( m_shades ) &&
//		( (CoExtendedMultiInkColor) o ).m_trappableColors.equals( m_trappableColors );
}
/**
 * This method was created in VisualAge.
 * @return float
 */
public float getBlackPercentage(){
	return getShade((com.bluebrim.paint.impl.shared.CoTrappableColorIF)CoFactoryManager.createObject(com.bluebrim.paint.impl.shared.CoProcessBlackIF.PROCESS_BLACK));
}
/**
 * This method was created in VisualAge.
 * @return float
 */
public float getCyanPercentage(){
	return getShade((com.bluebrim.paint.impl.shared.CoTrappableColorIF)CoFactoryManager.createObject(com.bluebrim.paint.impl.shared.CoProcessCyanIF.PROCESS_CYAN));
}
public String getFactoryKey ()
{
	return FACTORY_KEY;
}
/**
 * This method was created in VisualAge.
 * @return float
 */
public float getMagentaPercentage() {
	return getShade((com.bluebrim.paint.impl.shared.CoTrappableColorIF)CoFactoryManager.createObject(com.bluebrim.paint.impl.shared.CoProcessMagentaIF.PROCESS_MAGENTA));
}
public Color getPreviewColor() {
	float[][] rgbaColors = grabPreviewColors();
	if (m_previewColorCache == null || m_grabbedColorsCache == null || !arrayEqual(m_grabbedColorsCache, rgbaColors)) {
		float[] color = calcResultingColor(rgbaColors);
		m_previewColorCache = new Color(Math.min(1f,color[0]), Math.min(1f,color[1]), Math.min(1f,color[2]));
		m_grabbedColorsCache = rgbaColors;
	}
	return m_previewColorCache;
}
public float getShade( com.bluebrim.paint.impl.shared.CoTrappableColorIF trapColor )
{
	ShadedTrappableColor c = getShadedTrappableColor( trapColor );

	return ( c == null ) ? 0f : c.m_shade;
}


/**
 * This method was created in VisualAge.
 * @return float
 */
public float getYellowPercentage(){
	return getShade((com.bluebrim.paint.impl.shared.CoTrappableColorIF)CoFactoryManager.createObject(com.bluebrim.paint.impl.shared.CoProcessYellowIF.PROCESS_YELLOW));
}
private float[][] grabPreviewColors()
{
	int I = m_shadedTrappableColors.size();
	
	float [][] colors = new float[ I ][];
	
	for
		( int i = 0; i < I; i++ )
	{
		float[] color = new float[ 4 ];

		ShadedTrappableColor c = (ShadedTrappableColor) m_shadedTrappableColors.get( i );
		
		c.m_color.getPreviewColor().getRGBColorComponents( color );
		
		color[3] = c.m_shade / 100f;
		colors[i] = color;
	}
	
	return colors;
}
public void removeShadeColor( com.bluebrim.paint.impl.shared.CoTrappableColorIF trappableColor )
{
	ShadedTrappableColor c = getShadedTrappableColor( trappableColor );

	if
		( c != null )
	{
		m_shadedTrappableColors.remove( c );
		markDirty();
	}
}
/**
 * Tom tills vidare. En bra konversion från RGB till CMYK måste ske
 * i den här metoden.
 */
public void setColor(Color c)
{
	markDirty();
}
public void setShade( com.bluebrim.paint.impl.shared.CoTrappableColorIF trapColor , float value )
{
	ShadedTrappableColor c = getShadedTrappableColor( trapColor );

	if
		( c != null )
	{
		c.m_shade = value;
	} else {
		addShadeColor( trapColor , value );
	}
	
	markDirty();
}





private ShadedTrappableColor getShadedTrappableColor( com.bluebrim.paint.impl.shared.CoTrappableColorIF trapColor )
{
	int I = m_shadedTrappableColors.size();

	for
		( int i = 0; i < I; i++ )
	{
		ShadedTrappableColor c = (ShadedTrappableColor) m_shadedTrappableColors.get( i );
		if
			( c.m_color.equals( trapColor ) )
		{
			return c;
		}
	}
	
	return null;
}

public void xmlAddSubModel( String name, Object subModel, com.bluebrim.xml.shared.CoXmlContext context )
{
	if
		( ( name == null ) && ( subModel instanceof ShadedTrappableColor ) )
	{
		m_shadedTrappableColors.add( (ShadedTrappableColor) subModel );
	} else {
		super.xmlAddSubModel( name, subModel, context );
	}
}

public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor)
{
	super.xmlVisit( visitor );

	visitor.export( m_shadedTrappableColors.iterator() );
}
}