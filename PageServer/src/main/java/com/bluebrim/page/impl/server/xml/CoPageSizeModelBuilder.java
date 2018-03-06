package com.bluebrim.page.impl.server.xml;
import org.w3c.dom.*;

import com.bluebrim.pagesize.server.*;
import com.bluebrim.xml.shared.*;

public class CoPageSizeModelBuilder extends CoStateModelBuilder
{

	public CoPageSizeModelBuilder(CoXmlParserIF parser) {
		super(parser);
	}
	
	
	public void addSubModel(String name, Object model, CoXmlContext context) {
	}
	
	public void createModel(Object superModel, Node node, CoXmlContext context ) throws CoXmlReadException
	{
		CoPageSize s = new CoPageSize();
		s.xmlInit( getAllAttrAsMap( node ) );
		m_model = s;
	}
	
}