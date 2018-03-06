package com.bluebrim.image.impl.server.xml;

import com.bluebrim.image.impl.server.*;
import com.bluebrim.xml.shared.*;

/**
 * Xml-mapping for the image domain
 * 
 * @author Göran Stäck 2002-10-24
 */
public class CoImageXmlMapping implements CoXmlMappingSPI {

	public void collectMappings(Mapper mapper) {
		mapper.map(CoImageContent.class, CoImageContent.XML_TAG, CoImageContentModelBuilder.class, CoImageContentXmlBuilder.class);
	}

}
