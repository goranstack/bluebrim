package com.bluebrim.layout.impl.server;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.content.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.system.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * A page item image content projecting an layout content.
 * The layout content can be assigned directly or by specifying the layout order tag.
 * See CoLayoutArea for details on the later case.
  * 
 * @author: Dennis Malmström
 */

public class CoPageItemLayoutContent extends CoPageItemBoundedContent implements CoPageItemLayoutContentIF, CoPropertyChangeListener {
	private int m_layoutTag = -1; // tag deciding which layout in the workpiece to project, -1 means project no image

	public static final String XML_LAYOUT = "layout";
	public static final String XML_ORDER_TAG = "tag";
	public static final String XML_RECURSIVE_LEVEL_MAX_COUNT = "recursive-level-max-count";
	// xml tag constants
	public static final String XML_TAG = "page-item-layout-content";

	/*
		A layout content is basically a wrapper around a page item tree.
		A recursive structure can be achived by projecting a layout content in its own page item structure.
		Unless stopped this would lead to infinite recursion when creating page item views.
		This is handled by maximazing the number of recursive levels when creating page item views.
	*/

	// used in method createView_shallBeCalledBy_CoPageItemView_only() to handle recursion
	private transient boolean m_isRecursive;
	private CoLayoutContentIF m_layoutContent;
	private transient int m_recursiveLevelCounter = 0;
	private int m_recursiveLevelMaxCount = 0; // maximum number of recursive levels
	public CoPageItemLayoutContent() {
		super();
	}

	public CoPageItemLayoutContent(CoLayoutContentIF layout) {
		super();

		setLayoutContent(layout);
	}

	private boolean canSetLayout(CoLayoutContentIF layoutContent) {
		if (m_contentLock == FIXED)
			return false; // forbidden
		if (layoutContent == m_layoutContent)
			return false; // not needed
		if ((m_contentLock == LOCKED) && (layoutContent != null) && (m_layoutContent != null))
			return false; // forbidden

		return true;
	}


	public CoPageItemIF.State createState() {
		return new CoPageItemLayoutContentIF.State();
	}

	protected final void doAfterOrderTagChanged() {
		postLayoutTagChanged();
		markDirty("setOrderTag");
	}

	public Object getAttributeValue(java.lang.reflect.Field d) throws IllegalAccessException {
		try {
			return d.get(this);
		} catch (IllegalAccessException ex) {
			return super.getAttributeValue(d);
		}
	}

	protected double getContentHeight() {
		CoShapePageItemIF l = (CoShapePageItemIF) m_layoutContent.getLayout();
		return (l == null) ? 0 : l.getCoShape().getHeight();
	}

	protected double getContentWidth() {
		CoShapePageItemIF l = (CoShapePageItemIF) m_layoutContent.getLayout();
		return (l == null) ? 0 : l.getCoShape().getWidth();
	}


	public CoLayoutContentIF getLayoutContent() {
		return m_layoutContent;
	}

	public String getName() {
		if (m_layoutContent != null)
			return m_layoutContent.getName();
		else
			return "";
	}

	public int getOrderTag() {
		return m_layoutTag;
	}

	protected String getType() {
		return CoPageItemStringResources.getName(LAYOUT_CONTENT);
	}

	/**
	 * Return type name that can be used by a wrapper object.
	 */
	protected String getWrappersType() {
		return CoLayoutServerResources.getName(CoLayoutServerConstants.WRAPPED_LAYOUT_CONTENT);
	}

	// Find the workpiece of the first (from here and up) workpiece projecting layout area in the page item ancestor sequence.

	private CoWorkPieceIF getWorkPiece() {
		CoLayoutArea area = (CoLayoutArea) m_owner.getAncestor(CoLayoutAreaIF.class);

		while (area != null) {
			if (area.getWorkPiece() != null)
				return area.getWorkPiece();

			CoCompositePageItem pi = (CoCompositePageItem) area.getParent();

			if (pi == null)
				break;

			area = (CoLayoutArea) pi.getAncestor(CoLayoutAreaIF.class);
		}

		return null;
	}

	protected boolean hasContent() {
		return m_layoutContent != null;
	}

	public boolean isEmpty() {
		return m_layoutContent == null;
	}

	private boolean isWorkPieceLayoutContent() {
		return m_layoutTag >= 0;
	}

	// layout tag changed

	protected void postLayoutTagChanged() {
		if (m_owner != null) {
			m_owner.postDistributionBasisChange();
		}

	}

	// layout content property changed

	public void propertyChange(CoPropertyChangeEvent ev) {
		doAfterContentChanged();
	}


	public void setLayoutContent(CoLayoutContentIF layoutContent ) {
		if (!canSetLayout(layoutContent))
			return;

		if (m_layoutContent != null) {
			m_layoutContent.removePropertyChangeListener(this);
		}

		m_layoutContent = layoutContent;

		if (m_layoutContent != null) {
			m_layoutContent.addPropertyChangeListener(this);
		}

		doAfterContentChanged();
	}

	public void setOrderTag(int i) {
		if (m_contentLock == FIXED)
			return;
		if (m_layoutTag == i)
			return;
		if ((m_contentLock == LOCKED) && (i != -1) && (m_layoutTag != -1))
			return;

		m_layoutTag = i;

		doAfterOrderTagChanged();
	}

	public void visit(CoPageItemVisitor visitor, Object anything, boolean goDown) {
		visitor.doToLayoutContent(this, anything, goDown);
	}
	/*
	 * Used at XML export
	 * Helena Rankegård 2001-10-23
	 */

	public void xmlVisit(CoXmlVisitorIF visitor) {
		super.xmlVisit(visitor);

		if (getOrderTag() < 0) {
			if (getLayoutContent() != null)
				visitor.exportAsGOIorObject(XML_LAYOUT, getLayoutContent());
		} else {
			visitor.exportAttribute(XML_ORDER_TAG, Integer.toString(getOrderTag()));
		}

		visitor.exportAttribute(XML_RECURSIVE_LEVEL_MAX_COUNT, Integer.toString(getRecursiveLevelMaxCount()));
	}

	protected void collectState(CoPageItemIF.State s, CoPageItemIF.ViewState viewState) {
		super.collectState(s, viewState);

		CoPageItemLayoutContentIF.State S = (CoPageItemLayoutContentIF.State) s;
		CoPageItemLayoutContentIF.ViewState VS = (viewState instanceof CoPageItemLayoutContentIF.ViewState) ? (CoPageItemLayoutContentIF.ViewState) viewState : null;

		CoLayoutContentIF l = getLayoutContent();
		S.m_isNull = (l == null);

		if (S.m_isNull) {
			S.m_layoutContent = null;
		} else {
			boolean doSendLayout = false;

			if ((VS == null) || (m_recursiveLevelMaxCount != VS.m_recursiveLevelMaxCount)) {
				doSendLayout = true; // max recursion level count changed -> force view recreation
			} else {
				// compare clients timestamp with layouts, if same don't send layout
				doSendLayout = (VS.m_layoutContent != m_layoutContent) || (l.getTimeStamp() > VS.m_timeStamp);
			}

			if (doSendLayout) {
				S.m_layoutContent = l;
				S.m_timeStamp = l.getTimeStamp();
			} else {
				S.m_layoutContent = null;
			}
		}

		S.m_layoutTag = getOrderTag();
		S.m_workPiece = getWorkPiece();
		S.m_recursiveLevelMaxCount = getRecursiveLevelMaxCount();
	}

	public static CoContentWrapperPageItemIF createContentWrapper() {
		return new CoContentWrapperPageItem(new CoPageItemLayoutContent());
	}

	CoPageItemContentView createView(CoContentWrapperPageItemView owner) {
		// pretend we have no layout (in order to prevent recursive calls)
		CoLayoutContentIF tmp = m_layoutContent;
		m_layoutContent = null;

		if (m_isRecursive) {
			m_recursiveLevelCounter++;
		} else {
			m_recursiveLevelCounter = 0;
		}

		// create view
		CoPageItemLayoutContentView v = new CoPageItemLayoutContentView(owner, this, (CoPageItemLayoutContentIF.State) getState(null), m_isRecursive && (m_recursiveLevelCounter >= m_recursiveLevelMaxCount));

		m_isRecursive = true;

		// restore layout and sync view
		if (m_layoutContent != tmp) {
			m_layoutContent = tmp;
			v.modelChanged((CoPageItemLayoutContentIF.State) getState(null), null); // origin of any recursive calls
		}

		if (m_isRecursive)
			m_recursiveLevelCounter--;

		m_isRecursive = false;

		return v;
	}

	protected final void doAfterRecursiveLevelMaxCountChanged() {
		postRecursiveLevelMaxCountChanged();
		markDirty("setRecursiveLevelMaxCount");
	}

	protected void doDestroy() {
		setLayoutContent(null );
		super.doDestroy();
	}

	protected CoFormattedTextHolderIF getCaption() {
		return (m_layoutContent == null) ? null : m_layoutContent.getCaption();
	}

	public int getRecursiveLevelMaxCount() {
		return m_recursiveLevelMaxCount;
	}

	protected void postRecursiveLevelMaxCountChanged() {
	}

	public void setRecursiveLevelMaxCount(int i) {
		if (m_recursiveLevelMaxCount == i)
			return;

		m_recursiveLevelMaxCount = i;

		doAfterRecursiveLevelMaxCountChanged();
	}

	/*
	 * Used at XML import
	 * Helena Rankegård 2001-10-23
	 */

	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) {
		CoPageItemLayoutContent content = new CoPageItemLayoutContent();
		content.xmlInit(node.getAttributes(), context);
		return content;
	}
	
	

	/*
	 * Used at XML import.
	 * Helena Rankegård 2001-10-30
	 */
	public void xmlInit(NamedNodeMap map, CoXmlContext context) {
		super.xmlInit(map, context);

		// xml init
		m_layoutTag = CoModelBuilder.getIntAttrVal(map, XML_ORDER_TAG, -1);

		if (context.useGOI()) {
			String goiStr = CoModelBuilder.getAttrVal(map, XML_LAYOUT, null);
			if (goiStr != null) {
				CoLayoutContentIF l = ((CoContentRegistry) context.getValue(CoContentRegistry.class)).lookupLayoutContent(new CoGOI(goiStr));
	
				if (l == null) {
					System.err.println("Warning: could not find layout: \"" + goiStr + " \"");
				} else {
					setLayoutContent(l);
					m_layoutTag = -1;
				}
			}
		}

		m_recursiveLevelMaxCount = CoModelBuilder.getIntAttrVal(map, XML_RECURSIVE_LEVEL_MAX_COUNT, m_recursiveLevelMaxCount);
	}

	public void xmlAddSubModel(String parameter, Object subModel, CoXmlContext context) {
		super.xmlAddSubModel(parameter, subModel, context);
		if (!context.useGOI()) {
			if (parameter.equals(XML_LAYOUT)) {
				setLayoutContent((CoLayoutContentIF) subModel);
				m_layoutTag = -1;
			};
		}
	}

}