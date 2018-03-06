package com.bluebrim.stroke.impl.shared;


/**
 * Implementation of a dash color that fetches the foreground color and shade from the com.bluebrim.stroke.shared.CoStrokePropertiesIF.
 * 
 * @author: Dennis Malmström
 */

public class CoForegroundDashColor extends com.bluebrim.stroke.impl.shared.CoDashColor implements com.bluebrim.stroke.impl.shared.CoForegroundDashColorIF
{

public com.bluebrim.stroke.shared.CoDashColorIF deepClone()
{
	return this;
}
public boolean equals( Object o )
{
	return ( o instanceof CoForegroundDashColor ) || super.equals( o );
}

public float getDashShade( com.bluebrim.stroke.shared.CoImmutableStrokePropertiesIF props )
{
	return props.getForegroundShade();
}
public String getFactoryKey() 
{
	return FOREGROUND_DASH_COLOR_SPEC;
}

public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor)
{
	visitor.exportAttribute( XML_COLOR, XML_TAG );
}

	public static final String XML_TAG = "foreground";

public com.bluebrim.paint.shared.CoColorIF getDashColor( com.bluebrim.stroke.shared.CoImmutableStrokePropertiesIF props )
{
	return props.getForegroundColor();
}

public static com.bluebrim.stroke.impl.shared.CoForegroundDashColorIF getInstance()
{
	return (com.bluebrim.stroke.impl.shared.CoForegroundDashColorIF) com.bluebrim.base.shared.CoFactoryManager.getFactory( FOREGROUND_DASH_COLOR_SPEC ).createObject();
}
}