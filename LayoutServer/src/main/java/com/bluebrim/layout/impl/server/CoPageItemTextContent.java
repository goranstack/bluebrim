package com.bluebrim.layout.impl.server;

import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.content.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.system.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * A page item text content projecting a text that is owned by a document holder.
 * If no document holder is supplied one is automaticaly provided. This results in a situation where the page item
 * projects its own document holder rather than one that is owned by something else.
 * 
 * @author: Dennis Malmström
 */

public class CoPageItemTextContent
	extends CoPageItemAbstractTextContent
	implements CoPageItemTextContentIF, CoFormattedTextHolderIF.Context, CoPropertyChangeListener {

	// xml tag constants
	public static final String XML_TAG = "page-item-text-content";
	public static final String XML_PROJECTED_FORMATTED_TEXT = "projected_formatted-text";
	public static final String XML_FORMATTED_TEXT = "formatted-text";
	public static final String XML_LOCK = "lock";

	private boolean m_isProjecting; // true if page item doesn't own m_documentHolder
	private CoFormattedTextHolderIF m_textHolder;

	public CoPageItemTextContent() {
		this(null);
	}

	public String getName() {
		if (m_isProjecting)
			return m_textHolder.getName();
		else
			return m_textHolder.getTextExtract(10);
	}
	
	protected String getType() {
		return CoPageItemStringResources.getName(TEXT_CONTENT);
	}
	
	/**
	 * Return type name that can be used by a wrapper object.
	 */
	protected String getWrappersType() {
		return CoLayoutServerResources.getName(CoLayoutServerConstants.WRAPPED_TEXT_CONTENT);
	}

	public boolean isEmpty() {
		return getFormattedText().getLength() == 0;
	}

	/*
	 * Used at XML export
	 * Helena Rankegård 2001-10-23
	 */
	public void xmlVisit(CoXmlVisitorIF visitor) {
		super.xmlVisit(visitor);

		if (!m_isProjecting) {
			visitor.export(
				CoXmlWrapperFlavors.NAMED,
				XML_FORMATTED_TEXT,
				m_textHolder.getFormattedText(this).createStyledDocument());

		} else
			visitor.exportAsGOIorObject(XML_PROJECTED_FORMATTED_TEXT, (CoTextContentIF)m_textHolder);

	}

	public Object getAttributeValue(java.lang.reflect.Field d) throws IllegalAccessException {
		try {
			return d.get(this);
		} catch (IllegalAccessException ex) {
			return super.getAttributeValue(d);
		}
	}

	private CoPageItemTextContent(CoFormattedTextHolderIF text) {
		super();

		setFormattedTextHolder(text);
	}

	private boolean canSetFormattedTextHolder(CoFormattedTextHolderIF text) {
		if (getTextLock() == FIXED)
			return false; // forbidden
		if (!m_isProjecting && (text == null))
			return false; // not needed
		if (m_textHolder == text)
			return false; // not needed
		if ((getTextLock() == LOCKED) && (text != null) && m_isProjecting)
			return false; // forbidden

		return true;
	}

	protected void collectState(CoPageItemIF.State s, CoPageItemIF.ViewState viewState) {
		super.collectState(s, viewState);

		CoPageItemTextContentIF.State S = (CoPageItemTextContentIF.State) s;

		S.m_isProjecting = m_isProjecting;
	}

	public static CoContentWrapperPageItemIF createContentWrapper() {
		CoContentWrapperPageItem cw = new CoContentWrapperPageItem(new CoPageItemTextContent());
		cw.setDoRunAround(true);

		return cw;
	}

	/**
	 * Creates a textbox with an initial text and preferred run around.
	 * The method was introduced to eliminate the need for instansiating CoStyledDocument
	 * outside the text domain.
	 * @param initialString
	 * @param doRunAround
	 * @return CoContentWrapperPageItemIF
	 */
	public static CoContentWrapperPageItemIF createContentWrapper(String initialString, boolean doRunAround) {
		CoPageItemTextContent pageItemTextContent = new CoPageItemTextContent();
		pageItemTextContent.getMutableFormattedText().from(initialString);
		CoContentWrapperPageItem cw = new CoContentWrapperPageItem(pageItemTextContent);
		cw.setDoRunAround(doRunAround);

		return cw;
	}

	public CoPageItemIF.State createState() {
		return new CoPageItemTextContentIF.State();
	}

	CoPageItemContentView createView(CoContentWrapperPageItemView owner) {
		return new CoPageItemTextContentView(owner, this, (CoPageItemTextContentIF.State) getState(null));
	}

	protected void deepCopy(CoPageItem copy) {
		super.deepCopy(copy);

		CoPageItemTextContent c = (CoPageItemTextContent) copy;

		c.m_textHolder = null;

		if (m_isProjecting) {
			c.setFormattedTextHolder(m_textHolder);
		} else {
			c.setFormattedTextHolder(null);
			((CoFormattedTextHolder) c.m_textHolder).copyFrom((CoFormattedTextHolder) m_textHolder);
		}
	}

	protected void doDestroy() {
		m_textHolder.removePropertyChangeListener(this);

		super.doDestroy();
	}

	public CoFormattedText getFormattedText() {
		CoFormattedText text = m_textHolder.getFormattedText( this);

		applyStylesToText(text);

		return text;
	}

	public CoFormattedTextHolderIF getFormattedTextHolder() {
		return m_textHolder;
	}


	public CoFormattedText getMutableFormattedText() {
		CoFormattedText doc = null;

		doc = m_textHolder.getMutableFormattedText(this);

		applyStylesToText(doc);

		return doc;
	}

	protected void bindTextVariableValues(Map values) {
		super.bindTextVariableValues(values);

		values.put("Skribent", m_textHolder.getWriter());
	}

	// handle text changes

	public void propertyChange(CoPropertyChangeEvent event) {
		if (event.getPropertyName().equals(CoFormattedTextHolderIF.TEXT_PROPERTY))
			doAfterTextChanged();
	}


	public synchronized void setFormattedTextHolder(CoFormattedTextHolderIF text ) {
		if (m_textHolder != null) {
			if (!canSetFormattedTextHolder(text))
				return;
		}

		m_isProjecting = (text != null);

		if (text == null) {
			text = new CoFormattedTextHolder();
		}

		if (m_textHolder != null)
			m_textHolder.removePropertyChangeListener(this); // unregister document holder listener
		m_textHolder = text;
		m_textHolder.addPropertyChangeListener(this); // register document holder listener


		doAfterTextChanged();
	}


		public synchronized void updateFormattedText(CoFormattedText doc) {
		m_textHolder.setFormattedText(doc);
	}

	public void visit(CoPageItemVisitor visitor, Object anything, boolean goDown) {
		visitor.doToTextContent(this, anything, goDown);
	}

	public void xmlAddSubModel(String name, Object subModel, CoXmlContext context) {
		if (subModel instanceof CoStyledDocumentIF) {
			if (XML_FORMATTED_TEXT.equals(name)) {
				setFormattedTextHolder(null);
				getFormattedTextHolder().setFormattedText(new CoFormattedText((CoStyledDocumentIF) subModel));
			}
		} else
			if (name.equals(XML_PROJECTED_FORMATTED_TEXT)) {
				setFormattedTextHolder((CoFormattedTextHolderIF)subModel);
				};

	}

	/*
	 * Used at XML import
	 * Helena Rankegård 2001-10-23
	 */

	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) {
		CoPageItemTextContent content = new CoPageItemTextContent();
		content.xmlInit(node.getAttributes(), context);
		return content;
	}

	/*
	 * Used at XML import.
	 * Helena Rankegård 2001-10-30
	 */

	public void xmlInit(NamedNodeMap map, CoXmlContext context) {
		super.xmlInit(map, context);

		if (context.useGOI()) {
			String goiStr = CoModelBuilder.getAttrVal(map, XML_PROJECTED_FORMATTED_TEXT, null);
			if (goiStr != null) {
				CoTextContentIF t = ((CoContentRegistry) context.getValue(CoContentRegistry.class)).lookupTextContent(new CoGOI(goiStr));
	
				if (t == null) {
					System.err.println("Warning: could not find text: \"" + goiStr + " \"");
				} else {
					setFormattedTextHolder(t);
				}
			}
		}
	}
}