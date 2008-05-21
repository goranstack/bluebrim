package com.bluebrim.content.impl.server.xml;
import org.w3c.dom.*;

import com.bluebrim.content.impl.server.*;
import com.bluebrim.content.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Builds a com.bluebrim.mediaproduction.impl.server.CoWorkPiece object with it´s texts, images and layouts.
 * Creation date: (2000-11-17)
 * @author: Monika Czerska
 */

public class CoWorkPieceModelBuilder extends CoContentModelBuilder {
	public CoWorkPieceModelBuilder(CoXmlParserIF parser) {
		super(parser);
	}

	public void addSubModel(String name, Object model, CoXmlContext context) throws CoXmlReadException {
		if (model instanceof CoAtomicContentIF) {
			((CoWorkPiece) m_model).addContent((CoAtomicContentIF) model);
		} else {
			super.addSubModel(name, model, context);
		}
	}

	public void createModel(Object superModel, Node node, CoXmlContext context) {
		CoWorkPiece wp = new CoWorkPiece((CoLayoutParameters)context.getValue(CoLayoutParameters.class));
		wp.xmlInit(getAllAttrAsMap(node), context);
		m_model = wp;
	}
}