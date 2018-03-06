package com.bluebrim.solitarylayouteditor;

import com.bluebrim.xml.shared.*;

/**
 * Xml-mapping for the solitary layout editor domain
 * 
 * @author Göran Stäck 2002-10-24
 */
public class CoSolitaryLayoutEditorXmlMapping implements CoXmlMappingSPI {

	public void collectMappings(Mapper mapper) {
		mapper.map(CoLayoutDocument.class, CoLayoutDocument.XML_TAG);
		mapper.map(CoPageSet.class, CoPageSet.XML_TAG);

	}

}
