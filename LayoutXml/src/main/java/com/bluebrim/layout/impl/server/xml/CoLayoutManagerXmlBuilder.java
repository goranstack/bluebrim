package com.bluebrim.layout.impl.server.xml;

import com.bluebrim.layout.impl.shared.layoutmanager.*;

/**
 * Builds XML for layout specs
 * 
 * @author: Dennis
 */
 
public class CoLayoutManagerXmlBuilder extends CoPageItemXmlBuilder
{
public CoLayoutManagerXmlBuilder() {
	super();
}
protected String getTag()
{
	Object obj = getVisitor().getCurrentModel();
	String tag = null;

	if
		( obj instanceof CoLayoutManager )
	{
		return ( (CoLayoutManager) obj ).xmlGetTag();
	}
	
	//PENDING : Other subclasses ?		
	return tag;
}
}