package com.bluebrim.stroke.impl.shared;


/**
 * Implementation of a dash color that fetches the background color and shade from the com.bluebrim.stroke.shared.CoStrokePropertiesIF.
 * 
 * @author: Dennis Malmström
 */

public class CoBackgroundDashColor extends com.bluebrim.stroke.impl.shared.CoDashColor implements com.bluebrim.stroke.impl.shared.CoBackgroundDashColorIF
{

public com.bluebrim.stroke.shared.CoDashColorIF deepClone()
{
	return this;
}
public boolean equals( Object o )
{
	return ( o instanceof CoBackgroundDashColor ) || super.equals( o );
}

public float getDashShade( com.bluebrim.stroke.shared.CoImmutableStrokePropertiesIF props )
{
	return props.getBackgroundShade();
}
public String getFactoryKey() 
{
	return BACKGROUND_DASH_COLOR_SPEC;
}

public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor)
{
	visitor.exportAttribute( XML_COLOR, XML_TAG );
}

	public static final String XML_TAG = "background";

public com.bluebrim.paint.shared.CoColorIF getDashColor( com.bluebrim.stroke.shared.CoImmutableStrokePropertiesIF props )
{
	return props.getBackgroundColor();
}

public static com.bluebrim.stroke.impl.shared.CoBackgroundDashColorIF getInstance()
{
	return (com.bluebrim.stroke.impl.shared.CoBackgroundDashColorIF) com.bluebrim.base.shared.CoFactoryManager.getFactory( BACKGROUND_DASH_COLOR_SPEC ).createObject();
}
}