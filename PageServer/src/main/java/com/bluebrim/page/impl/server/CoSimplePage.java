package com.bluebrim.page.impl.server;
import java.util.*;

import org.w3c.dom.*;

import com.bluebrim.layout.impl.server.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.page.shared.*;
import com.bluebrim.xml.shared.*;
/**
 * The simples possible implementation of the CoPage interface
 * Creation date: (2001-11-22 11:53:12)
 * @author Arvid Berg
 */
public class CoSimplePage extends CoContextualPageImpl {

	public static final String XML_TAG = "page";

	private CoPageLayout m_pageLayout;
	private CoLayoutChangeListener m_layoutChangeListener = new CoLayoutChangeListener() {
		public void layoutChange(EventObject evt) {
			firePageChange(evt);
		}
	};

	public CoSimplePage(CoPageContext pageContext) {
		super(pageContext);
	}

	public CoSimplePage(CoPageContext pageContext, CoPageSizeIF pageSize) {
		super(pageContext);
		setLayout(CoLayoutServerImpl.getInstance().createPageLayout(this));
		m_pageLayout.setPageSize(pageSize);
	}

	private CoSimplePage(Object superModel, Node node, CoXmlContext context) {
		super((CoPageContext)superModel);
	}

	private final void setLayout(CoPageLayout pageLayout ) {
		m_pageLayout = pageLayout;
		m_pageLayout.addLayoutChangeListener(m_layoutChangeListener);
	}

	public List getLayouts() {
		List tmp = new ArrayList();
		tmp.add(m_pageLayout);
		return tmp;
	}


	public void xmlVisit(CoXmlVisitorIF visitor) throws CoXmlWriteException {
		visitor.export(m_pageLayout);
	}

	public void xmlAddSubModel(String parameter, Object subModel, CoXmlContext context) throws CoXmlReadException {
		if (parameter == null) {
			if (subModel instanceof CoPageLayout) {
				setLayout((CoPageLayout)subModel);
			}
		}
	}

	public void xmlImportFinished(Node node, CoXmlContext context) throws CoXmlReadException {
		m_pageLayout.activateLayoutEngine();
	}

	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) {
		return new CoSimplePage(superModel, node, context);
	}

}