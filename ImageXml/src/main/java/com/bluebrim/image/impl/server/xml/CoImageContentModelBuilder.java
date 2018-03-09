package com.bluebrim.image.impl.server.xml;

import java.io.*;
import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.content.impl.server.xml.*;
import com.bluebrim.image.impl.server.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Builds a CoImageContent object. It can be built from a filename
 * or from directly embedded base64 characters.
 */

public class CoImageContentModelBuilder extends CoContentModelBuilder {
	private Map m_attributes = null;
	private CoFormattedText m_caption = null;

	public CoImageContentModelBuilder(CoXmlParserIF parser) {
		super(parser);
	}
	/**
	 * @param subModel An {@link InputStream} from which the image is read.
	 */
	public void addSubModel(String parameter, Object subModel, CoXmlContext context) throws CoXmlReadException {
		if (parameter.equals("image")) {
			if (!(subModel instanceof File))
				throw new CoXmlReadException("Error reading image data from XML");
			File file = (File) subModel;
			((CoImageContent) m_model).init(file);

		} else if (parameter.equals("caption")) {
			// Fixme: export CoFormattedText
			if (!(subModel instanceof CoFormattedText))
				throw new CoXmlReadException("Error reading image caption from XML");
			m_caption = (CoFormattedText) subModel;
		} else if (parameter.equals("image_operation_dag")) {
			// no longer exist

		} else {
			throw new CoXmlReadException("Error parsing image from XML");
		}

	}
	/**
	 * Create the model. Publisher link not handled yet.
	 */
	public void createModel(Object superModel, Node node, CoXmlContext context) {
		m_attributes = getAllAttrAsMap(node);

		m_model = new CoImageContent();
	}

	public Object getModel(Node node, CoXmlContext context) {
		if (m_model != null) {
			if (m_attributes != null) {
				((CoImageContent) m_model).xmlInit(m_attributes, context);

				// Don't initialize the model twice
				m_attributes = null;
			}

			if (m_caption != null) {
				((CoImageContent) m_model).getCaption().setFormattedText( m_caption);

				// Don't set the caption twice
				m_caption = null;
			}

		}

		return super.getModel(node, context);
	}
}