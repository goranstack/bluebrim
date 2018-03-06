package com.bluebrim.layout.impl.server;

import java.util.Map;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.CoContentWrapperPageItemIF;
import com.bluebrim.layout.impl.shared.CoPageItemContentIF;
import com.bluebrim.layout.impl.shared.CoPageItemIF;
import com.bluebrim.layout.impl.shared.view.CoCompositePageItemView;
import com.bluebrim.layout.impl.shared.view.CoContentWrapperPageItemView;
import com.bluebrim.layout.impl.shared.view.CoPageItemContentView;
import com.bluebrim.layout.impl.shared.view.CoPageItemView;
import com.bluebrim.layout.impl.shared.view.CoShapePageItemView;
import com.bluebrim.layout.impl.shared.view.CoStopAtFirstProjectorPageItemView;

/**
 * This class implements a page item that holds a CoPageItemContent.
 * See CoPageItemContent for more information on the relationship between CoContentWrapperPageItem and CoPageItemContent.
 * 
 * @author: Dennis Malmström
 */

public class CoContentWrapperPageItem extends CoShapePageItem implements CoContentWrapperPageItemIF {
	// xml tag constants
	public static final String XML_TAG = "content-wrapper-page-item";

	protected CoPageItemContent m_content; // INVARIANT: m_content != null
	public CoContentWrapperPageItem() {
		this(new CoPageItemNoContent());
	}
	public CoContentWrapperPageItem(CoPageItemContentIF content) {
		super();

		setContent(content);
	}

	// See CoPageItem.createState

	public CoPageItemIF.State createState() {
		return new CoContentWrapperPageItemIF.State();
	}

	public CoPageItemContentIF getContent() {
		return m_content;
	}
	public double getContentHeight() {
		if (m_content.isEmpty()) {
			return m_content.getEmptyHeight();
		} else {
			return m_content.getHeight();
		}
	}
	public double getContentWidth() {
		if (m_content.isEmpty()) {
			return m_content.getEmptyWidth();
		} else {
			return m_content.getWidth();
		}
	}

	public String getName() {
		String name = m_content.getName();
		if (name.length() > 0)
			return getType() + ": " + name;
		else
			return getType();
	}
	
	// Delegate to content

	public String getType() {
		return m_content.getWrappersType();
	}
	
	// Delegate to content

	int getPositionWeight() {
		return m_content.getPositionWeight();
	}

	// Delegate to content

	protected void handleShapeTranslation(double dx, double dy) {
		m_content.handleShapeTranslation(dx, dy);
	}

	// Handle and delegate to content

	protected void postSlaveChanged() {
		super.postSlaveChanged();

		m_content.postSlaveChanged();
	}
	public void reshapeToContentHeight() {
	}
	public void reshapeToContentWidth() {
	}
	public final void setContent(CoPageItemContentIF content) {
		com.bluebrim.base.shared.debug.CoAssertion.assertTrue(content != null, "Content can not be null");

		m_content = (CoPageItemContent) content;
		m_content.setOwner(this);

		markDirty("setContent");
	}

	/*
	 * Used at XML export
	 * Helena Rankegård 2001-10-23
	 */

	public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) {
		super.xmlVisit(visitor);

		visitor.export(getContent());
	}

	public Object getAttributeValue(java.lang.reflect.Field d) throws IllegalAccessException {
		try {
			return d.get(this);
		} catch (IllegalAccessException ex) {
			return super.getAttributeValue(d);
		}
	}

	// Handle and delegate to content

	protected void postAddTo(CoCompositePageItem parent) {
		super.postAddTo(parent);

		m_content.postAddTo(parent);
	}

	// Handle and delegate to content

	protected void postRemoveFrom(CoCompositePageItem parent) {
		m_content.postRemoveFrom(parent);

		super.postRemoveFrom(parent);
	}

	protected void collectState(CoPageItemIF.State s, CoPageItemIF.ViewState viewState) {
		super.collectState(s, viewState);

		CoContentWrapperPageItemIF.State S = (CoContentWrapperPageItemIF.State) s;

		S.m_clipping = getInteriorShape(); // the clipping shape used when drawing the contents
		S.m_contentId = m_content.getId();
		S.m_contentState = (CoPageItemContentIF.State) m_content.getState((viewState == null) ? null : ((CoContentWrapperPageItemIF.ViewState) viewState).m_contentViewState);
	}

	public CoPageItemContentView createContentView_shallBeCalledBy_CoContentWrapperPageItemView_only() {
		return m_content.createView(null);
	}

	CoShapePageItemView createView(CoCompositePageItemView parent, int detailMode) {
		if (detailMode == CoPageItemView.DETAILS_STOP_AT_FIRST_PROJECTOR) {
			return new CoStopAtFirstProjectorPageItemView(this, parent, (CoContentWrapperPageItemIF.State) getState(null));
		}

		CoContentWrapperPageItemView v = new CoContentWrapperPageItemView(this, parent, (CoContentWrapperPageItemIF.State) getState(null), detailMode);
		v.setContentView(m_content.createView(v)); // create content view

		return v;
	}

	protected void deepCopy(CoPageItem copy) {
		super.deepCopy(copy);

		CoContentWrapperPageItem c = (CoContentWrapperPageItem) copy;

		c.m_content = (CoPageItemContent) m_content.deepClone();
		c.m_content.setOwner(c);
	}

	protected void doDestroy() {
		// dispatch to content
		m_content.destroy();

		super.doDestroy();
	}

	protected void bindTextVariableValues( Map values) {
		super.bindTextVariableValues(values);
		if (m_content != null)
			m_content.bindTextVariableValues(values);
	}

	// CoContentWrapperPageItem has no children

	protected void updateChildrensBaseLineGrids() {
	}

	// CoContentWrapperPageItem has no children

	protected void updateChildrensColumnGrids() {
	}

	public void visit(CoPageItemVisitor visitor, Object anything, boolean goDown) {
		visitor.doToContentWrapper(this, anything, goDown);
	}

	/*
	 * Used at XML import
	 * Helena Rankegård 2001-10-23
	 */

	public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context) {
		CoContentWrapperPageItem pi = new CoContentWrapperPageItem();
		pi.xmlInit(node.getAttributes(), context);
		return pi;
	}

	/*
	 * Used at XML import.
	 * Helena Rankegård 2001-10-30
	 */
	public void xmlInit(NamedNodeMap map, com.bluebrim.xml.shared.CoXmlContext context) {
		super.xmlInit(map, context);

		// xml init
	}

	public String getFactoryKey() {
		return m_content.getFactoryKey();
	}

	/*
	 * Used at XML import
	 * Helena Rankegård 2001-10-30
	 */
	public void xmlAddSubModel(String name, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) {
		boolean didHandle = false;

		if (name == null) {
			if (subModel instanceof CoPageItemContentIF) {
				setContent((CoPageItemContentIF) subModel);
				didHandle = true;
			}
		}

		if (!didHandle)
			super.xmlAddSubModel(name, subModel, context);
	}


}