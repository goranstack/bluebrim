package com.bluebrim.text.impl.server.xml;
import org.w3c.dom.*;

import com.bluebrim.content.impl.server.xml.*;
import com.bluebrim.text.impl.server.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Builds a com.bluebrim.text.impl.server.CoTextContent object from a String.
 * Sofar we handle nothing else than a plain String.
 */

public class CoTextModelBuilder extends CoContentModelBuilder {

	public CoTextModelBuilder(CoXmlParserIF parser) {
		super(parser);
	}

	public void addSubModel(String name, Object model, CoXmlContext context) {
		if (model instanceof CoFormattedText) {
			((CoTextContent) m_model).setFormattedText((CoFormattedText) model);
		} else {
			System.out.println(getClass() + ": Ignoring sub model of type " + model.getClass());
		}
	}

	public void createModel(Object superModel, Node node, CoXmlContext context) {
		CoTextContent t = new CoTextContent();
		t.xmlInit(getAllAttrAsMap(node), context);
		m_model = t;
	}
}