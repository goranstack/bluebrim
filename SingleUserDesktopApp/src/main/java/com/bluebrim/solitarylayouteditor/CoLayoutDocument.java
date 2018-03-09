package com.bluebrim.solitarylayouteditor;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.compositecontent.client.*;
import com.bluebrim.content.impl.server.*;
import com.bluebrim.content.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.server.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.menus.client.*;
import com.bluebrim.observable.*;
import com.bluebrim.page.impl.server.*;
import com.bluebrim.page.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Used in the standalone layout editor for holding the objects that is 
 * saved or retrieved together in one file. Its designed as a client side object
 * and has function for configuring and open the LayoutEditor.
 * 
 * @author Göran Stäck 2002-09-17
 *
 */
public class CoLayoutDocument implements CoXmlEnabledIF, CoLayoutParametersProvider, CoMenuExtender, CoFileable {

	public static final String XML_TAG = "layout-document";
	public static final String NAME_IN_ZIP_ARCHIVE = XML_TAG + ".xml";

	private static final String XML_PAGES = "pages";
	private static final String XML_MASTER_PAGES = "master-pages";
	private static final String XML_DESKTOP = "desktop";

	// Transient data
	protected final transient CoEnableDisableManager m_hasUnSavedData = new CoEnableDisableManager() {
		public boolean isEnabled() {
			return m_dirty;
		}
	};

	private transient CoSolitaryLayoutEditor m_application;
	private transient String m_name;
	private transient CoFileStoreSupport m_fileStoreSupport;
	private transient CoLayoutEditor m_layoutEditor;
	private transient boolean m_dirty = false;
	private transient CoChangedObjectListener m_changedChildrenListener;
	private transient List m_contentReceiversList = new ArrayList();
	private transient CoLayoutEditorConfiguration m_mainEditorConfiguration;
	private transient CoLayoutEditorConfiguration m_subEditorConfiguration;
	private transient CoPageItemEditorContext m_layoutEditorContext;
	private transient CoPageSetChangeListener m_pageSetChangeListener = new CoPageSetChangeListener() {
		public void pageSetChange(EventObject evt) {
			setUnsaved();
		}
	};

	// Persistent data
	private CoLayoutParameters m_layoutParameters;
	private CoLayoutTemplateCollection m_tools;
	private CoLayoutTemplateRootFolder m_templates;
	private CoContentCollectionIF m_contentCollection;
	private CoPageSet m_masterPages;
	private CoPageSet m_pages;	 
	private CoDesktopLayoutAreaIF m_desktop;

		
	public CoLayoutDocument(CoSolitaryLayoutEditor application) {
		preInitTransient(application);
		initPersistent();
		postInitTransient();
		registerAsListenerForChangedObject();
// Temporary lines only for testing
//		new CoWorkPieceTestData().createWorkPieces(m_contentCollection, CoResourceLoader.getStream(CoWorkPieceTestData.class, "test/testArticles"), m_layoutParameters);
//		new CoLayoutTemplateTestData().createArticleTemplates(m_templates);
//		CoTextTestData.createStyleSheets(m_layoutParameters.getTypographyRule());
//		new CoTextTestData().createTagChains((CoStyledTextPreferencesIF)m_layoutParameters);
// 
	}
	
	/**
	 * Constructor used at XML-import. The final initialization is done in the
	 * <code>xmlImportFinished</code> method.
	 */
	protected CoLayoutDocument(Object parentObject, Node node, CoXmlContext context) {
		preInitTransient((CoSolitaryLayoutEditor)context.getValue(CoSolitaryLayoutEditor.class));
	}

	/**
	 * Perform initialization of transient data before XML-import because 
	 * it's needed in the XML-import process.
	 */
	private final void preInitTransient(CoSolitaryLayoutEditor application) {
		m_application = application;
		m_name = CoStringResources.getName(CoConstants.UNTITLED) + " " + application.getUntitledNumber();
	}
	
	/**
	 * Perform initialization of transient data after XML-import because 
	 * the initialization depends on persistent data created in the XML-import process.
	 */
	private final void postInitTransient() {
		m_contentReceiversList = new ArrayList();
		m_contentReceiversList.add(m_contentCollection);
		m_layoutEditorContext = new CoPageItemEditorContext((CoPageItemPreferencesIF) m_layoutParameters, (CoPageItemPrototypeCollectionIF)m_tools, m_contentReceiversList, (CoPageItemPrototypeTreeRootIF)m_templates);
		m_subEditorConfiguration = CoLayoutEditorConfiguration.EDITORIAL_INSTANCE.copy();
		m_changedChildrenListener = new CoChangedObjectListener() {
				public void serverObjectChanged(CoChangedObjectEvent e) {
					CoLayoutDocument.this.setUnsaved();
				}
		};
	}
		
	/**
	 * Listen to all persistent objects that report when
	 * they are changed. Any changes in persistent objects
	 * put the document in a non saved state that enables
	 * the save menu item and trigger a save question dialog
	 * if the user close the document window
	 */
	private void registerAsListenerForChangedObject() {
		Iterator iter = collectChangeReportingObjects().iterator();
		while (iter.hasNext())
			CoObservable.addChangedObjectListener(m_changedChildrenListener, iter.next());
		m_masterPages.addPageSetChangeListener(m_pageSetChangeListener);
		m_pages.addPageSetChangeListener(m_pageSetChangeListener);
	}
	
	/**
	 * One of the children has changed. Set the document in a non saved state
	 * and indicate that state in the window title.
	 */
	private void setUnsaved() {
		if (m_dirty || m_layoutEditor == null) return;
		m_dirty = true;
		updateWindowTitle();
		updateEnableDisableState();
	}
	
	private void setSaved() {
		m_dirty = false;
		updateWindowTitle();
		updateEnableDisableState();
	}
	
	private void updateWindowTitle() {
		CoFrame frame = m_layoutEditor.getCurrentFrame();
		if (m_dirty)
			frame.setTitle("*" + m_name);
		else
			frame.setTitle(m_name); 
	}
	
	
	private List collectChangeReportingObjects() {
		List list = new ArrayList();
		list.add(m_layoutParameters);
		list.add(m_desktop);
		list.add(m_tools);
		list.add(m_contentCollection);
		return list;
	}
	
	/**
	 * Initialization of persistent data is unnecessary if the variable
	 * is imported from XML. That's why that kind of initialization is extracted
	 * to this method and not called from the constructor when importing from
	 * XML. But since som of the variables can be omitted in the XML the method
	 * is called after the XML is processed in case some variables where omitted
	 * in the XML.
	 */
	private final void initPersistent() {
		if (m_layoutParameters == null) {
			m_layoutParameters = CoLayoutServerImpl.getInstance().getDefaultLayoutParameters();
			m_layoutParameters.getPageSizeCollection().createDefaultPageSizes();
		}

		if (m_desktop == null)
			m_desktop = new CoDesktopLayoutArea(m_layoutParameters);

		if (m_tools == null)
			m_tools = CoPageItemPrototypeCollection.createEditorialEditorTools();

		if (m_templates == null)
			m_templates = CoLayoutServerImpl.getInstance().createLayoutTemplateTree(m_layoutParameters);

		if (m_contentCollection == null)
			m_contentCollection = new CoContentCollection( m_layoutParameters, m_name);

		if (m_pages == null)
			m_pages = new CoPageSet(m_desktop, m_layoutParameters, "");		

		if (m_masterPages == null)
			m_masterPages = new CoPageSet(m_desktop, m_layoutParameters, "M");		
	}
	
	public void xmlVisit(CoXmlVisitorIF visitor) throws CoXmlWriteException {
		visitor.export(m_layoutParameters);
		visitor.export(m_desktop);
//		visitor.export(m_tools);  Implement this later
		visitor.export(m_templates);
		visitor.export(m_contentCollection);
		visitor.export(CoXmlWrapperFlavors.NAMED, XML_MASTER_PAGES, m_masterPages);
		visitor.export(CoXmlWrapperFlavors.NAMED, XML_PAGES, m_pages);
	}

	public void xmlAddSubModel(String name, Object subModel, CoXmlContext context) {
		if (name == null) {
			if (subModel instanceof CoLayoutParameters) {
				m_layoutParameters = (CoLayoutParameters)subModel;
			} else if (subModel instanceof CoLayoutTemplateCollection) {
				m_tools = (CoLayoutTemplateCollection)subModel;
			} else if (subModel instanceof CoLayoutTemplateRootFolder) {
				m_templates = (CoLayoutTemplateRootFolder)subModel;
			} else if (subModel instanceof CoContentCollectionIF) {
				m_contentCollection = (CoContentCollectionIF)subModel;
				context.putValue(CoContentRegistry.class, new CoSimpleContentRegistry(m_contentCollection));
			} else if (subModel instanceof CoDesktopLayoutAreaIF) {
				m_desktop = (CoDesktopLayoutAreaIF)subModel;
			}

		} else 
			if (name.equals(XML_PAGES)) {
					m_pages = (CoPageSet)subModel;
				}
			 else
				if (name.equals(XML_MASTER_PAGES)) {
					m_masterPages = (CoPageSet)subModel;
					};
	}
	
	public void xmlImportFinished(Node node, CoXmlContext context) {
		initPersistent();	// Initialize the variables that was omitted in the Xml
		postInitTransient();
		registerAsListenerForChangedObject();
		m_desktop.activateLayoutEngine();
	}
	
	public List getLayoutEditorModels() {
		List pages = m_pages.getPages();
		int size = pages.size();
		List models = new ArrayList(size);
		for ( int i = 0; i < size; i++ ) {
			CoLayeredPage layeredPage = (CoLayeredPage)pages.get(i);
			models.add( new CoLayoutEditorModel( layeredPage.getLayouts(), layeredPage.getName() ) );

		}
		return models;
	}
	
	/**
	 * Creates a new layered page with one or two layers
	 * depending on the occurrence of at least one master page.
	 */ 
	public CoLayeredPage createAndAddPage(CoPageSizeIF pageSize) {
		CoLayeredPage page = new CoLayeredPageImpl(m_pages);
		if (m_masterPages.isNotEmpty())
			page.addLayer(m_masterPages.getFirstPage());
		page.addLayer(new CoSimplePage(m_pages, pageSize));
		m_pages.add(page);
		updateEditor();
		return page;
	}
	
	public CoPage createAndAddMasterPage(CoPageSizeIF pageSize) {
		CoPage page = new CoSimplePage(m_masterPages, pageSize);
		m_masterPages.add(page);
		updateEditor();
		return page;
	}
	
	private void updateEditor() {
		if (m_layoutEditor != null)
			m_layoutEditor.setModels(getLayoutEditorModels(), m_desktop);
	}

	private void updateEnableDisableState() {
		m_hasUnSavedData.update();
	}
	
	public void applyMasterPage(CoPage masterPage, CoLayeredPage page) {
		List layers = page.getLayers();
		if (layers.contains(masterPage))
			return;
		layers.add(0,masterPage);
	}
	
	public void openEditor(CoLayoutEditorConfiguration configuration) {
		m_layoutEditor = new CoLayoutEditor(configuration, m_name);
		m_layoutEditor.setContext(m_layoutEditorContext);
		m_layoutEditor.setModels(getLayoutEditorModels(), (CoDesktopLayoutAreaIF) m_desktop);
		m_layoutEditor.openInWindow();
		updateWindowTitle();
		updateEnableDisableState();
		JFrame frame = m_layoutEditor.getCurrentFrame();
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {			
		    /**
		     * Invoked when the user attempts to close the window
		     * from the window's system menu.  If the program does not 
		     * explicitly hide or dispose the window while processing 
		     * this event, the window close operation will be cancelled.
		     */
			public void windowClosing(WindowEvent e) {
				if (saveIfUserWantsOrCancel()) {
		            Window w = e.getWindow();
		            w.dispose();
		            m_application.isClosing(CoLayoutDocument.this);
		            releaseDocument();

				}
			}
		});
	}
	
	private void releaseDocument()
	{
		m_application = null;
		m_changedChildrenListener = null;
		m_contentCollection = null;
		m_contentReceiversList = null;
		m_desktop = null;
		m_fileStoreSupport = null;
		m_layoutEditor = null;
		m_layoutEditorContext = null;
		m_layoutParameters = null;
		m_mainEditorConfiguration = null;
		m_masterPages = null;
		m_pages = null;
		m_pageSetChangeListener = null;
		m_subEditorConfiguration = null;
		m_templates = null;
		m_tools = null;
	}
	
	/**
	 * The LayoutEditor has a feature for adding external UI's. The default window title 
	 * of the UI's is used when adding menu items in the tools menu. 
	 */
	public void addUIsToConfiguration(CoLayoutEditorConfiguration configuration) {
		// CoPageItemPreferencesUI
		CoDomainUserInterface ui = new CoPageItemPreferencesUI();
		ui.setDomain(m_layoutParameters);
		configuration.add(ui);

		// CoContentCollectionUI
		ui = new CoContentCollectionUI(CoLayoutEditor.createLayoutEditorDialog(m_subEditorConfiguration),createContentCollectionUIContext());
		ui.setDomain(m_contentCollection);
		configuration.add(ui);

	}
	
	private CoUIContext createContentCollectionUIContext() {
		return new CoUIContext() {
			public Object getStateFor(String key) {
				if (key.equals(CoLayoutEditorContext.KEY)) {
					return m_layoutEditorContext;
				} else
					if (key.equals(CoDesktopLayout.DESKTOP_LAYOUT)) {
						return m_desktop;
					} else
						if (key.equals(CoLayoutEditorConfiguration.LAYOUT_EDITOR_CONFIGURATION)) {
							return m_subEditorConfiguration;
						} else
							if (key.equals(CoFormattedTextHolderIF.Context.KEY)) {
								return m_layoutParameters;
							} else {
								return null;
							}
			}

		};
	}
	
	private void save(int operationType) {
		int result;
		try {
			result = m_fileStoreSupport.save(operationType);
		} catch (IOException e) {
			CoErrorDialog.open("Kunde inte spara", e);
			return;
		} catch (RuntimeException e) {
			CoErrorDialog.open("Kunde inte skapa dataström", e);
			return;
		}
		if (result == CoFileStoreSupport.SAVE_SUCCEEDED) {
			m_name = m_fileStoreSupport.getFileName();
			setSaved();
		}
	}
	
	private CoXmlContext createXmlContext(List attachements) {
		Object[][] contextObjects = { 
			{ CoLayoutParameters.class, m_layoutParameters }, 
			{ CoPageSizeRegistry.class, m_layoutParameters.getPageSizeCollection() },
			{ CoSolitaryLayoutEditor.class, this }
		};
		CoXmlContext xmlContext = new CoXmlContext(contextObjects);
		xmlContext.setAttachements(attachements);
		xmlContext.setUseGOI(false);
		return xmlContext;
	}

	public CoDesktopLayout getDesktop() {
		return m_desktop;
	}
		
	public CoLayoutEditor getLayoutEditor() {
		return m_layoutEditor;
	}

	/**
	 * Ask the user if he wants to save with an option to cancel the question.
	 *
	 * @return boolean  false if the user canceled the question or closed the dialog 
	 */
	public boolean saveIfUserWantsOrCancel() {
		if (!m_dirty) 
			return true;
		int answer = CoGUI.cancellableQuestion("Vill du spara ändringar i " + m_name, m_layoutEditor.getWindow());
		if (answer == JOptionPane.YES_OPTION)
			save(CoFileStoreSupport.SAVE);
		return !(answer == JOptionPane.CANCEL_OPTION || answer == JOptionPane.CLOSED_OPTION); 		
	}

	public CoLayoutParameters getLayoutParameters() {
		return m_layoutParameters;
	}
	
	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) {
		CoLayoutDocument layoutDocument = new CoLayoutDocument(superModel, node, context);
		return layoutDocument;
	}
	
	public void extendMenu(CoMenuBar menuBar, CoMenuBuilder builder) {
		CoSubMenu fileMenu = (CoSubMenu)menuBar.getMenu(0);
		JMenuItem mi;

		mi = fileMenu.insertBefore(CoLayouteditorUIStringResources.MENU_FILE_PRINT_SETTINGS, CoLayoutEditorClientConstants.MENU_FILE_OPEN_DOCUMENT);
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					m_application.loadDocumentFromFile(m_layoutEditor.getCurrentFrame());
				} catch (IOException e) {
					CoErrorDialog.open("Något fel uppstod vid Öppna ", e);
				}
			}
		});

		fileMenu.insertSeparatorBefore(CoLayouteditorUIStringResources.MENU_FILE_PRINT_SETTINGS);

		mi = fileMenu.insertBefore(CoLayouteditorUIStringResources.MENU_FILE_PRINT_SETTINGS, CoLayoutEditorClientConstants.MENU_FILE_SAVE_DOCUMENT);
		m_hasUnSavedData.add(mi);
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				save(CoFileStoreSupport.SAVE);
			}
		});

		mi = fileMenu.insertBefore(CoLayouteditorUIStringResources.MENU_FILE_PRINT_SETTINGS, CoLayoutEditorClientConstants.MENU_FILE_SAVE_DOCUMENT_AS);
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				save(CoFileStoreSupport.SAVE_AS);
			}
		});

		fileMenu.insertSeparatorBefore(CoLayouteditorUIStringResources.MENU_FILE_PRINT_SETTINGS);

		JMenu pageMenu = new JMenu("Sida");		
		menuBar.insertBefore(CoLayoutEditorClientConstants.MENU_VIEW, pageMenu);
		
		
		pageMenu.add(mi = new JMenuItem("Lägg till sida"));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				createAndAddPage(m_application.getDefaultPageSize());
			}
		});

		pageMenu.add(mi = new JMenuItem("Lägg till mallsida"));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				createAndAddMasterPage(m_application.getDefaultPageSize());
			}
		});

	}

	public Component getComponent() {
		return m_layoutEditor.getWindow();
	}

	public void writeToStream(OutputStream outputStream, List attachments) throws Exception {
		Writer writer = null;
		writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF8"));
		writeXml(attachments, writer);
		writer.flush();
	}

	private void writeXml(List attachments, Writer writer) throws CoXmlGenerationException, CoOutputGenerationException {
		CoXmlOutputGenerator gen = new CoXmlOutputGenerator(createXmlContext(attachments));
		gen.execute(this);
		gen.output(writer);
//		XStream xstream = new XStream();
//		String xml = xstream.toXML(this);
//		try {
//			writer.write(xml);
//		} catch (IOException e) {
//			throw new CoOutputGenerationException("Unable to write xml to writer");
//		}
	}

	public String getName() {
		return m_name;
	}

	/**
	 * When a new <code>CoLayoutDocument</code> is created the supplied
	 * fileStoreSupport has not been used for saving yet and therefor
	 * lacks a filename. If that is the case we keep the initial name value.
	 * When a <code>CoLayoutDocument</code> i loaded from a file we set
	 * our name to the file name.
	 */
	public void setFileStoreSupport(CoFileStoreSupport fileStoreSupport) {
		fileStoreSupport.setFileable(this);
		m_fileStoreSupport = fileStoreSupport;
		if (m_fileStoreSupport.hasFile())
			m_name = m_fileStoreSupport.getFileName();
	}

	public String getNameInZipArchive() {
		return NAME_IN_ZIP_ARCHIVE;
	}

}
