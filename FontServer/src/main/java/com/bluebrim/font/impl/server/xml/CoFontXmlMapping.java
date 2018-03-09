package com.bluebrim.font.impl.server.xml;

import com.bluebrim.font.impl.server.*;
import com.bluebrim.font.impl.server.truetype.*;
import com.bluebrim.font.impl.server.type1.*;
import com.bluebrim.font.impl.shared.*;
import com.bluebrim.font.impl.shared.metrics.*;
import com.bluebrim.font.shared.*;
import com.bluebrim.xml.shared.*;


/**
 * Xml-mapping for the font domain
 * 
 * @author Göran Stäck 2002-10-24
 */
public class CoFontXmlMapping implements CoXmlMappingSPI {

	public void collectMappings(Mapper mapper) {
		mapper.map(CoFont.class, CoFont.XML_TAG);
		mapper.map(CoFontFace.class, CoFontFace.XML_TAG);
		mapper.map(CoFontMetricsDataImplementation.class, CoFontMetricsDataImplementation.XML_TAG);
		mapper.map(CoFontAwtDataImplementation.class, CoFontAwtDataImplementation.XML_TAG);
		mapper.map(CoFontPostscriptDataImplementation.class, CoFontPostscriptDataImplementation.XML_TAG);
		mapper.map(CoTrackingMetricsImplementation.class, CoTrackingMetricsImplementation.XML_TAG);
		mapper.map(CoHorizontalMetricsImplementation.class, CoHorizontalMetricsImplementation.XML_TAG);
		mapper.map(CoFixedWidthHorizontalMetrics.class, CoFixedWidthHorizontalMetrics.XML_TAG);
		mapper.map(CoLineMetricsImplementation.class, CoLineMetricsImplementation.XML_TAG);
		mapper.map(CoPairKerningMetricsImplementation.class, CoPairKerningMetricsImplementation.XML_TAG);
		mapper.map(CoPairKerningMetricsImplementation.CharPairKey.class, CoPairKerningMetricsImplementation.CharPairKey.XML_TAG);
		mapper.map(CoType1FileContainer.class, CoType1FileContainer.XML_TAG);
		mapper.map(CoTrueTypeFileContainer.class, CoTrueTypeFileContainer.XML_TAG);
		mapper.map(CoFontFaceSpec.XmlWrapper.class, CoFontFaceSpec.XmlWrapper.XML_TAG);
		mapper.map(CoFontRepository.class, CoFontRepository.XML_TAG);

	}

}