package com.bluebrim.content.impl.server.xml;

import com.bluebrim.content.impl.server.*;
import com.bluebrim.xml.shared.*;

/**
 * Xml-mapping for the content domain
 * 
 * @author G�ran St�ck 2002-11-15
 */
public class CoContentXmlMapping implements CoXmlMappingSPI {

	public void collectMappings(Mapper mapper) {
		mapper.map(CoWorkPiece.class, CoWorkPiece.XML_TAG, CoWorkPieceModelBuilder.class);
		mapper.map(CoContentCollection.class, CoContentCollection.XML_TAG );
	}

}
