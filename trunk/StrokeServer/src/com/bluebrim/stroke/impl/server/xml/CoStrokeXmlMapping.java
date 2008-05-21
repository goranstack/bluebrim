package com.bluebrim.stroke.impl.server.xml;

import com.bluebrim.stroke.impl.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Xml-mapping for the stroke domain
 * 
 * @author Göran Stäck 2002-10-24
 */
public class CoStrokeXmlMapping implements CoXmlMappingSPI {

	public void collectMappings(Mapper mapper) {
		mapper.map(CoStroke.class, CoStroke.XML_TAG);
		mapper.map(CoStrokeLayer.class, CoStrokeLayer.XML_TAG);
		mapper.map(CoDash.class, CoDash.XML_TAG);
		mapper.map(CoDashColor.class, CoDashColor.XML_TAG, CoDashColorModelBuilder.class );
		
	}

}

