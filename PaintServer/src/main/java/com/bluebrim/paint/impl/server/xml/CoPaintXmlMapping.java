package com.bluebrim.paint.impl.server.xml;

import com.bluebrim.paint.impl.shared.*;
import com.bluebrim.xml.shared.*;


/**
 * Xml-mapping for the paint domain
 * 
 * @author Göran Stäck 2002-10-24
 */
public class CoPaintXmlMapping implements CoXmlMappingSPI {

	public void collectMappings(Mapper mapper) {
		mapper.map(CoColor.class, CoColor.XML_TAG, CoColorModelBuilder.class );
		mapper.map(CoExtendedMultiInkColor.ShadedTrappableColor.class, CoExtendedMultiInkColor.ShadedTrappableColor.XML_TAG, CoColorModelBuilder.class );
	}

}
