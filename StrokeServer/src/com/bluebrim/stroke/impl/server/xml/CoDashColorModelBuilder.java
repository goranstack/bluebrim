package com.bluebrim.stroke.impl.server.xml;
import org.w3c.dom.*;

import com.bluebrim.stroke.impl.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Builds colors for dashes
 * Creation date: (1999-09-28 11:29:46)
 * @author: Mikael Printz
 */

public class CoDashColorModelBuilder extends CoStateModelBuilder {

	public CoDashColorModelBuilder(CoXmlParserIF parser) {
		super(parser);
	}

	public void addSubModel(String name, Object model, CoXmlContext context) {
		((CoAbsoluteDashColor) m_model).xmlAddSubModel(model, context);
	}

	public void createModel(Object superModel, Node node, CoXmlContext context) throws CoXmlReadException {
		String type = getAttrVal(node, CoDashColor.XML_COLOR);

		CoDashColor c = null;

		if (type.equals(CoNoDashColor.XML_TAG)) {
			c = new CoNoDashColor();
		} else
			if (type.equals(CoForegroundDashColor.XML_TAG)) {
				c = new CoForegroundDashColor();
			} else
				if (type.equals(CoBackgroundDashColor.XML_TAG)) {
					c = new CoBackgroundDashColor();
				} else {
					c = new CoAbsoluteDashColor();
				}

		c.xmlInit(getAllAttrAsMap(node));

		m_model = c;
	}
}