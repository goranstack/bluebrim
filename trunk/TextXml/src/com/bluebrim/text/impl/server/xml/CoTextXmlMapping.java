package com.bluebrim.text.impl.server.xml;

import com.bluebrim.text.impl.server.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Xml-mapping for the text domain
 * 
 * @author Göran Stäck 2002-10-24
 */
public class CoTextXmlMapping implements CoXmlMappingSPI {

	public void collectMappings(Mapper mapper) {
		mapper.map(CoFormattedText.class, CoFormattedText.XML_TAG);
		mapper.map(CoStyledDocument.class, CoStyledDocument.XML_TAG, null, CoStyledDocumentXmlBuilder.class);
		mapper.map(CoTypographyRule.class, CoTypographyRule.XML_TAG);
		mapper.map(CoCharacterStyle.class, CoCharacterStyle.XML_TAG, null, CoCharacterStyleBuilder.class);
		mapper.map(CoParagraphStyle.class, CoParagraphStyle.XML_TAG, null, CoParagraphStyleBuilder.class);
		mapper.map(CoTagChain.class, CoTagChain.XML_TAG);
		mapper.map(CoTagGroup.class, CoTagGroup.XML_TAG);
		mapper.map(CoHyphenation.class, CoHyphenation.XML_TAG);
		mapper.map(CoLiangLineBreaker.class, CoLiangLineBreaker.XML_TAG);
		mapper.map(CoWordLineBreaker.class, CoWordLineBreaker.XML_TAG);
		mapper.map(CoAnywhereLineBreaker.class, CoAnywhereLineBreaker.XML_TAG);
		mapper.map(CoTextContent.class, CoTextContent.XML_TAG, CoTextModelBuilder.class, CoTextXmlBuilder.class);

	}

}
