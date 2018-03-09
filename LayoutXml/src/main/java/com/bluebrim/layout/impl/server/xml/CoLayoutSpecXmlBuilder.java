package com.bluebrim.layout.impl.server.xml;

import com.bluebrim.layout.impl.server.manager.*;

/**
 * Builds XML for layout specs
 * 
 * @author: Dennis
 */
 
public class CoLayoutSpecXmlBuilder extends CoPageItemXmlBuilder
{
public CoLayoutSpecXmlBuilder() {
	super();
}
protected String getTag()
{
	Object obj = getVisitor().getCurrentModel();
	String tag = null;

	if
		( obj instanceof CoLocationSpec )
	{
		return ( (CoLocationSpec) obj ).getXmlTag();
	} else if
		( obj instanceof CoSizeSpec )
	{
		return ( (CoSizeSpec) obj ).getXmlTag();
	}
	
	//PENDING : Other subclasses ?		
	return tag;
}
}