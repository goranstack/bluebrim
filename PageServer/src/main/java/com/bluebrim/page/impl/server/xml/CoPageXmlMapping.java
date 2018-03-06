package com.bluebrim.page.impl.server.xml;

import com.bluebrim.page.impl.server.*;
import com.bluebrim.pagesize.server.*;
import com.bluebrim.xml.shared.*;

/**
 * Xml-mapping for the page domain
 * 
 * @author Göran Stäck 2002-10-24
 */
public class CoPageXmlMapping implements CoXmlMappingSPI {

	public void collectMappings(Mapper mapper) {
		mapper.map(CoSimplePage.class, CoSimplePage.XML_TAG);
		mapper.map(CoLayeredPageImpl.class, CoLayeredPageImpl.XML_TAG);
		mapper.map(CoPageSize.class, CoPageSize.XML_TAG, CoPageSizeModelBuilder.class);

	}

}
