package com.bluebrim.stroke.impl.shared;


/**
 * Implementation of a dash color that always returns null -> transaprent stroke layer.
 * 
 * @author: Dennis Malmström
 */

public class CoNoDashColor extends com.bluebrim.stroke.impl.shared.CoDashColor implements com.bluebrim.stroke.impl.shared.CoNoDashColorIF
{

public com.bluebrim.stroke.shared.CoDashColorIF deepClone()
{
	return this;
}
public boolean equals( Object o )
{
	return ( o instanceof CoNoDashColor ) || super.equals( o );
}

public float getDashShade( com.bluebrim.stroke.shared.CoImmutableStrokePropertiesIF props )
{
	return 0;
}
public String getFactoryKey() 
{
	return NO_DASH_COLOR_SPEC;
}

public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor )
{
	visitor.exportAttribute( XML_COLOR, XML_TAG );
}

	public static final String XML_TAG = "none";

public com.bluebrim.paint.shared.CoColorIF getDashColor( com.bluebrim.stroke.shared.CoImmutableStrokePropertiesIF props )
{
	return null;
}

public static com.bluebrim.stroke.impl.shared.CoNoDashColorIF getInstance()
{
	return (com.bluebrim.stroke.impl.shared.CoNoDashColorIF) com.bluebrim.base.shared.CoFactoryManager.getFactory( NO_DASH_COLOR_SPEC ).createObject();
}
}