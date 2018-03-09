package com.bluebrim.solitarylayouteditor;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.w3c.dom.Node;

import com.bluebrim.base.shared.CoEventListenerList;
import com.bluebrim.layout.shared.CoDesktopLayout;
import com.bluebrim.layout.shared.CoLayoutParameters;
import com.bluebrim.page.shared.CoPage;
import com.bluebrim.page.shared.CoPageChangeListener;
import com.bluebrim.page.shared.CoPageContext;
import com.bluebrim.xml.shared.CoXmlContext;
import com.bluebrim.xml.shared.CoXmlEnabledIF;
import com.bluebrim.xml.shared.CoXmlImportEnabledIF;
import com.bluebrim.xml.shared.CoXmlReadException;
import com.bluebrim.xml.shared.CoXmlVisitorIF;
import com.bluebrim.xml.shared.CoXmlWriteException;

/**
 * A simple implementation of <code>CoPageContext</code> that originally
 * was made for standalone layout editor.
 * 
 * @author Göran Stäck 2002-10-01
 *
 */
public class CoPageSet implements CoPageContext, CoXmlEnabledIF {

	public static final String XML_TAG = "page-set";

	// Transient data
	private CoDesktopLayout m_desktop;
	private CoLayoutParameters m_layoutParameters;
	private String m_namePrefix = "";
	private CoEventListenerList m_listeners = new CoEventListenerList();
	private CoPageChangeListener m_pageChangeListener = new CoPageChangeListener() {
		public void pageChange(EventObject evt) {
			firePageSetChange(evt);
		}
	};

	// Persistent data. (Not the List but its elements)
	private List m_pages = new ArrayList();

	public CoPageSet(CoDesktopLayout desktop, CoLayoutParameters layoutParameters, String namePrefix) {
		m_desktop = desktop;
		m_layoutParameters = layoutParameters;
		m_namePrefix = namePrefix;
	}
	
	/**
	 * Used for XML-import
	 */	
	private CoPageSet(Object superModel, Node node, CoXmlContext context) {
		CoLayoutDocument doc = (CoLayoutDocument)superModel;
		m_desktop = doc.getDesktop();
		m_layoutParameters = doc.getLayoutParameters();
		m_namePrefix = "";
	}
	
	/**
	 * PENDING: Replace this method with several for adding and removing at 
	 * specific position for example addLast, insertFirst, insertBefore(CoPage page)
	 */	
	public void add(CoPage page) {
		m_pages.add(page);
		firePageSetChange(this);
		page.addPageChangeListener(m_pageChangeListener);
	}

	public String getName() {
		return null;
	}

	public CoLayoutParameters getLayoutParameters() {
		return m_layoutParameters;
	}

	public CoDesktopLayout getDesktop() {
		return m_desktop;
	}

	/**
	 * The object model concerning left and right pages and spreads
	 * is not complete. The concept of spreads is not working toghether
	 * with the concept of left and right pages in the present implementation
	 */
	public boolean isLeftSide(CoPage page) {
		return (m_pages.indexOf(page) % 2 == 0) ? false : true;
	}

	/**
	 * Returns page number with a prefix
	 */
	public String getContextualNameFor(CoPage page) {
		if (m_pages.contains(page))
			return m_namePrefix + (m_pages.indexOf(page) + 1);
		else
			return m_namePrefix;
	}

	public void xmlVisit(CoXmlVisitorIF visitor) throws CoXmlWriteException {
		visitor.export(m_pages.iterator());
	}

	public void xmlAddSubModel(String name, Object subModel, CoXmlContext context) throws CoXmlReadException {
		if (subModel instanceof CoPage) {
			m_pages.add(subModel);
			((CoPage)subModel).addPageChangeListener(m_pageChangeListener);
		}
	}

	public void xmlImportFinished(Node node, CoXmlContext context) throws CoXmlReadException {
	}
	
	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) {
		return new CoPageSet(superModel, node, context);
	}
	
	public void setNamePrefix(String namePrefix) {
		m_namePrefix = namePrefix;
	}

	/**
	 * Do not add or remove elements in the list.
	 */
	public List getPages() {
		return m_pages;
	}
	
	public boolean isNotEmpty() {
		return m_pages.size() > 0;
	}
	
	public CoPage getFirstPage() {
		return (CoPage)m_pages.get(0);
	}
	
	public void addPageSetChangeListener(CoPageSetChangeListener listener) {
		m_listeners.add(CoPageSetChangeListener.class, listener);
	}

	public void removePageSetChangeListener(CoPageSetChangeListener listener) {
		m_listeners.remove(CoPageSetChangeListener.class, listener);
	}
		
	/**
	 * Fires EventObject to the listeners.
	 */
	protected void firePageSetChange(Object source) {
		Object[] targets;

		synchronized (this) {
			if (m_listeners.getListenerCount() < 1)
				return;
			targets = (Object[]) m_listeners.getListenerList().clone();
		}
		EventObject evt = new EventObject(source);

		for (int i = 0; i < targets.length; i += 2) {
			if ((Class) targets[i] == CoPageSetChangeListener.class) {
				CoPageSetChangeListener target = (CoPageSetChangeListener) targets[i + 1];
				target.pageSetChange(evt);
			}
		}
	}


}
