package com.bluebrim.base.server.xml;

import com.bluebrim.base.shared.geom.*;
import com.bluebrim.xml.shared.*;

/**
 * Xml-mapping for the base.geom domain
 * 
 * @author Göran Stäck 2002-10-24
 */
public class CoBaseGeomXmlMapping implements CoXmlMappingSPI {

	public void collectMappings(Mapper mapper) {
		mapper.map(CoConcaveCorner.class, CoConcaveCorner.XML_TAG);
		mapper.map(CoRoundedCorner.class, CoRoundedCorner.XML_TAG);
		mapper.map(CoCubicBezierCurveShape.class, CoCubicBezierCurveShape.XML_TAG);
		mapper.map(CoPolygonShape.class, CoPolygonShape.XML_TAG);
		mapper.map(CoEllipseShape.class, CoEllipseShape.XML_TAG);
		mapper.map(CoRectangleShape.class, CoRectangleShape.XML_TAG);
		mapper.map(CoBoxedLineShape.class, CoBoxedLineShape.XML_TAG);
		mapper.map(CoLine.class, CoLine.XML_TAG);
		mapper.map(CoOrthogonalLine.class, CoOrthogonalLine.XML_TAG);
		mapper.map(CoBeveledCorner.class, CoBeveledCorner.XML_TAG);
	}

}