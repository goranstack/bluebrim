package com.bluebrim.layout.impl.server.xml;
import org.w3c.dom.*;

import com.bluebrim.content.impl.server.xml.*;
import com.bluebrim.layout.impl.server.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Builds a layout content
 * Creation date: (2000-11-17)
 * @author: Monika Czerska
 */

public class CoLayoutContentModelBuilder extends CoContentModelBuilder {
	public CoLayoutContentModelBuilder(CoXmlParserIF parser) {
		super(parser);
	}

	public void addNode(Node node, CoXmlContext context) {
		CoFormattedText doc = createFormattedTextFrom(node);

		if (doc != null) {
			((CoLayoutContent) m_model).getCaption().setFormattedText( doc);
		} else {
			super.addNode(node, context);
		}
	}

	public void addSubModel(String name, Object model, CoXmlContext context) throws CoXmlReadException {
		if (model instanceof CoCompositePageItemIF) {
			((CoLayoutContent) m_model).setLayout((CoCompositePageItemIF) model);
		} else {
			super.addSubModel(name, model, context);
		}
	}

	public void createModel(Object superModel, Node node, CoXmlContext context) {
		CoLayoutContent t = new CoLayoutContent();
		t.xmlInit(getAllAttrAsMap(node), context);
		m_model = t;
	}
}