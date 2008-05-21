package com.bluebrim.stroke.impl.shared;
import com.bluebrim.base.shared.*;
import com.bluebrim.paint.impl.shared.*;
import com.bluebrim.paint.shared.*;
import com.bluebrim.stroke.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Implementation of a dash color that holds the color and shade instead of fetching them from the com.bluebrim.stroke.shared.CoStrokePropertiesIF.
 * 
 * @author: Dennis Malmström
 */

public class CoAbsoluteDashColor extends CoDashColor implements CoAbsoluteDashColorIF
{
	private CoColorIF m_color;
	private float m_shade;
	public static final String XML_SHADE = "shade";

public CoAbsoluteDashColor()
{
	m_color = (CoColorIF) CoFactoryManager.createObject( CoWhiteColorIF.WHITE_COLOR );
	m_shade = 100;
}

public CoDashColorIF deepClone()
{
	return new CoAbsoluteDashColor( m_color, m_shade );
}


public float getDashShade( CoImmutableStrokePropertiesIF props )
{
	return getShade();
}
public String getFactoryKey() 
{
	return ABSOLUTE_DASH_COLOR_SPEC;
}
public float getShade()
{
	return m_shade;
}
protected void markDirty()
{
}

public void setShade( float s )
{
	m_shade = s;
	
	markDirty();
}
public void xmlVisit( CoXmlVisitorIF visitor )
{
	visitor.exportAttribute( XML_SHADE, Float.toString( getShade() ) );

	visitor.export( getColor() );
}




private CoAbsoluteDashColor( CoColorIF c, float shade )
{
	m_color = c;
	m_shade = shade;
}

public CoColorIF getColor()
{
	return m_color;
}

public CoColorIF getDashColor( CoImmutableStrokePropertiesIF props )
{
	return getColor();
}

public void setColor( CoColorIF c )
{
	m_color = c;
	
	markDirty();
}

public void xmlAddSubModel( Object model, CoXmlContext context )
{
	if
		( model instanceof CoColorIF )
	{
		setColor( (CoColorIF) model );
	}
}

public void xmlInit( java.util.Map attributes )
{
	m_shade = CoXmlUtilities.parseFloat( (String) attributes.get( XML_SHADE ), m_shade );
}
}