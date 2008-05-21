package com.bluebrim.page.impl.server;
import java.util.*;

import org.w3c.dom.Node;

import com.bluebrim.page.shared.CoLayeredPage;
import com.bluebrim.page.shared.CoPage;
import com.bluebrim.page.shared.CoPageChangeListener;
import com.bluebrim.page.shared.CoPageContext;
import com.bluebrim.xml.shared.*;
/**
 * A page stack puts one or more pages on top of each others. The upper left corner
 * of all pages is positioned at the same point. The order of the pages in the stack
 * affects the order the are presented in UI's. The page items on the topmost page is
 * drawn first. A page stack is used for creating pages containing page items that is
 * shared with other pages for example header and footer. The concept is common in
 * various programs and is often presented such as pages have a background pages (PowerPoint) or 
 * pages are based on a master pages (QuarkXPress, InDesign).
 * Page stacks are treated as pages in many situations and therefore implements the <code>CoPage</code> interface.
 * Creation date: (2001-11-21 12:07:45)
 * @author: Göran Stäck 
 */
public class CoLayeredPageImpl extends CoContextualPageImpl implements CoLayeredPage {

	public static final String XML_TAG = "layered-page";
	private static final String XML_LAYERS = "layers";
	private List m_layers = new ArrayList();  // [ CoPage ]
	private CoPageChangeListener m_pageChangeListener = new CoPageChangeListener() {
		public void pageChange(EventObject evt) {
			firePageChange(evt);
		}
	};

	public CoLayeredPageImpl(CoPageContext pageContext) {
		super(pageContext);
	}

	private CoLayeredPageImpl(Object superModel, Node node, CoXmlContext context) {
		this((CoPageContext)superModel);
	}

	public CoPage addLayer(CoPage page) {
		m_layers.add(page);
		firePageChange(this);
		page.addPageChangeListener(m_pageChangeListener);
		return page;
	}

	/**
	 * Get the single top page item element from each page in the stack of layers
	 */
	public List getLayouts() {
		List tmp = new LinkedList();
		Iterator iter = m_layers.iterator();
		while (iter.hasNext()) {
			tmp.addAll(((CoPage) iter.next()).getLayouts());
		}
		return tmp;
	}

	/**
	 * Do not add or remove elements in the list unless you want to override
	 * event broadcasting.
	 */
	public List getLayers() { // [ CoPage ]
		return m_layers;
	}

	/**
	 * Delegate to each element in the stack
	 */
	public void bindTextVariableValues(Map values) {
		Iterator iter = m_layers.iterator();
		while (iter.hasNext()) {
			((CoPage) iter.next()).bindTextVariableValues(values);
		}
	}

	public void xmlVisit(CoXmlVisitorIF visitor) throws CoXmlWriteException {
		visitor.export(XML_LAYERS, m_layers); 
	}

	public void xmlAddSubModel(String name, Object subModel, CoXmlContext context) throws CoXmlReadException {
		if (name.equals(XML_LAYERS)) {
			Iterator i = (Iterator) subModel;
			while (i.hasNext()) {
				addLayer((CoPage)i.next());
			}

		}
	}

	public void xmlImportFinished(Node node, CoXmlContext context) throws CoXmlReadException {
	}
	
	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) {
		return new CoLayeredPageImpl(superModel, node, context);
	}

	public String getContextualNameFor(CoPage page) {
		if (m_layers.contains(page))
			return getPageContext().getContextualNameFor(this);
		else
			return "";
	}

	public boolean isLeftSide(CoPage page) {
		return getPageContext().isLeftSide(page);
	}

}