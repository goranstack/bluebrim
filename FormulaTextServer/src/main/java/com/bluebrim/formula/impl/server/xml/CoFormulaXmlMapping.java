package com.bluebrim.formula.impl.server.xml;

import com.bluebrim.formula.impl.server.*;
import com.bluebrim.xml.shared.*;


/**
 * Xml-mapping for the formula domain
 * 
 * @author Göran Stäck 2002-10-24
 */
public class CoFormulaXmlMapping implements CoXmlMappingSPI {

	public void collectMappings(Mapper mapper) {
		mapper.map(CoFormulaHolder.class, CoFormulaHolder.XML_TAG);
	}

}