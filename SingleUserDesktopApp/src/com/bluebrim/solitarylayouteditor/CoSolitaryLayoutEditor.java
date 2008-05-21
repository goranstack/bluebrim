package com.bluebrim.solitarylayouteditor;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.util.zip.*;

import javax.imageio.spi.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import org.apache.batik.util.gui.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.compositecontent.client.*;
import com.bluebrim.content.impl.server.*;
import com.bluebrim.font.client.*;
import com.bluebrim.font.impl.client.*;
import com.bluebrim.font.impl.server.*;
import com.bluebrim.font.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.image.client.*;
import com.bluebrim.image.impl.server.*;
import com.bluebrim.image.impl.shared.*;
import com.bluebrim.layout.client.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.server.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.menus.client.*;
import com.bluebrim.page.shared.*;
import com.bluebrim.resource.shared.*;
import com.bluebrim.spellchecker.client.*;
import com.bluebrim.spellchecker.impl.server.*;
import com.bluebrim.text.client.*;
import com.bluebrim.text.impl.server.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Starts <code>CoLayoutEditor</code> as a standalone application with
 * file system persistance.
 * 
 * @author Göran Stäck 2002-09-12
 *
 */
public class CoSolitaryLayoutEditor implements CoNamed {

	public static final String RELEASE_NOTES_URL = "CoSolitaryLayoutEditor.RELEASE_NOTES_URL";
	private static JFrame m_releaseNotesWindow;
	private List m_layoutDocuments = new ArrayList(); // [ CoLayoutDocument ]
	private int m_untitledNumber = 1; // Used as appendix for naming untitled documents
	private CoFileStoreSupport fileStoreSupport;

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
//		UIManager.setLookAndFeel("com.stefankrause.xplookandfeel.XPLookAndFeel"); 
//		Icon splashImage = CoResourceLoader.loadIcon("solitarylayouteditor", "BlueBrimLogo.png");
		Locale.setDefault(Locale.ENGLISH);
		URL splashUrl = CoSolitaryLayoutEditor.class.getResource("/BlueBrimLogo.png");
		if (splashUrl == null) 
			throw new RuntimeException("Can't find resource /BlueBrimLogo.png");
		Icon splashImage = new ImageIcon(splashUrl);
		CoSplashScreen splash = new CoSplashScreen(splashImage, CoUIConstants.BLUEBRIM_YELLOW);
  		splash.toFront();
   		splash.setVisible(true);		
		CoSolitaryLayoutEditor app = new CoSolitaryLayoutEditor(splash);		
		app.run(splash);
 		splash.close();
	}

	public CoSolitaryLayoutEditor(CoSplashScreen splash) {
//		CoLookAndFeelManager.setLookAndFeel(new MetalLookAndFeel());
		CoFactoryManager factoryManager = new CoLocalFactoryManager();
		CoFactoryManager.setInstance(factoryManager);
		new CoPageItemFactories().init(true);
		addSystemProperties();		// Only to make the font repository happy
		initSingletons(splash);
		installFonts(splash);
		IIORegistry.getDefaultInstance().registerServiceProvider(new CoEpsImageReaderSpi());
		fileStoreSupport = new CoFileStoreSupport(getZipDocumentFileFilter(), true);

	}

	private void run(CoStatusShower shower) {
		createNewDocument(shower);
	}

    private final void initSingletons(CoStatusShower shower)
    {
        // Font
    	shower.showStatus("Initialize font server");
        CoFontServer fontServer = new CoFontServerImpl();
        CoFontServerProvider.setFontServer(fontServer);
        CoFontClient.setFontServer(fontServer);
        
        // SpellChecker
    	shower.showStatus("Initialize spelling checker");
        CoSpellCheckerServerImpl spellCheckerServer = new CoSpellCheckerServerImpl();
        CoSpellCheckerClient.setSpellCheckerServer(spellCheckerServer);
        
		// Image
    	shower.showStatus("Initialize image server");
		CoImageClient.setInstance(new CoLocalImageClient());
		

		// Layout	
    	shower.showStatus("Initialize layout server");
		CoLayoutServerImpl layoutServer = new CoLayoutServerImpl();
		CoLayoutServerImpl.setInstance(layoutServer);
		CoLayoutClient.setLayoutServer(layoutServer);
		
        // Text
	   	shower.showStatus("Initialize text server");
        CoTextServerImpl textServer = new CoTextServerImpl();
        CoTextServerProvider.setTextServer(textServer);
        CoTextClient.setTextServer(textServer);

		// Same instance is used as both layout- and text parameters
	   	shower.showStatus("Initialize default layout preferences");
        CoPageItemPreferences defaultPageItemPreferences = createDefaultPageItemPreferences();
        layoutServer.setDefaultLayoutParameters(defaultPageItemPreferences);
        textServer.setDefaultStyledTextPreferences(defaultPageItemPreferences);
       
		
		CoCompositeContentServerImpl compositeContentServer = new CoCompositeContentServerImpl();
		CoCompositeContentClient.setCompositeContentServer(compositeContentServer);

    }
        
    private CoPageItemPreferences createDefaultPageItemPreferences()
    {
        CoPageItemPreferences defaultPageItemPreferences = new CoPageItemPreferences();
        defaultPageItemPreferences.addRGBColors();	// Move to color domain
        defaultPageItemPreferences.addCMYKColors();	// Move to color domain
        defaultPageItemPreferences.addDefaultStrokes();	// Move to stroke domain
        // Move to font domain
		Iterator i = CoAbstractFontMapper.getFontMapper().getFontFamilies().iterator();
		while (i.hasNext()) {
		    defaultPageItemPreferences.addFontFamily((String) i.next());
		}
        
		return defaultPageItemPreferences;

    }
    
	private CoLayoutEditorConfiguration createMainEditorConfiguration(CoLayoutDocument layoutDocument) {
		CoLayoutEditorConfiguration configuration = CoLayoutEditorConfiguration.EDITORIAL_INSTANCE.copy();
		configuration.setMenuExtender(createMenuExtender(layoutDocument));
		layoutDocument.addUIsToConfiguration(configuration);
		return configuration;
	}

	/**
	 * The page size that is applied on new documents. The user should be able to change this
	 * in the application preferences dialog.
	 */
	public CoPageSizeIF getDefaultPageSize() {
		return CoLayoutServerImpl.getInstance().getDefaultLayoutParameters().getPageSizeCollection().getPageSizeByName(CoPageSizeIF.A4);
	}

	/**
	 * Shows a filebrowser and let the user pick a file to import.
	 * The file returned from file stored support is the main file in the zip
	 * archive along with the attachement files. The files is placed in a
	 * temporary directory that is removed if something goes wrong.
	 */
	public void loadDocumentFromFile(CoFrame parent) throws IOException {
		File file = fileStoreSupport.chooseZipFileForLoading(parent, CoLayoutDocument.NAME_IN_ZIP_ARCHIVE);
		if (file != null) {
			CoXmlConsumer consumer = new CoXmlConsumer(createXmlContext(file));
			Object importedXml = null;
			InputStream stream = null;
			try {
				stream = new BufferedInputStream(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				CoGUI.error("Hittar inte: " + file.getName(), parent);
				stream.close();
				deleteDirectory(file.getParentFile());
				return;
			}
			try {
				importedXml = consumer.readModelFrom(stream);
			} catch (CoXmlReadException e) {
				CoGUI.error("Fel vid läsning av: " + fileStoreSupport.getFileName() + e.getMessage(), parent);
				stream.close();
				deleteDirectory(file.getParentFile());
				return;
			}
			if (importedXml != null) {
				CoLayoutDocument layoutDocument = (CoLayoutDocument) importedXml;
				layoutDocument.setFileStoreSupport(fileStoreSupport);
				openEditor(layoutDocument);
			}
			stream.close();
			deleteDirectory(file.getParentFile());
		}
	}

	/**
	 * Delete all files in the directory and the directory as well
	 */
	private void deleteDirectory(File dir) {
		if (!dir.isDirectory())
			return;
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		};
		dir.delete();
	}

	private CoXmlContext createXmlContext(File file) {
		CoPageItemPreferencesIF pageItemPreferences =
			(CoPageItemPreferencesIF) CoLayoutServerImpl.getInstance().getDefaultLayoutParameters();
		Object[][] contextObjects = { { CoSolitaryLayoutEditor.class, this }, {
				CoLayoutParameters.class, pageItemPreferences }, {
				CoPageSizeRegistry.class, pageItemPreferences.getPageSizeCollection()
				}
		};
		CoXmlContext xmlContext = new CoXmlContext(contextObjects);
		xmlContext.setUseGOI(false);
		xmlContext.setFileNameAndPath(file.getPath());
		return xmlContext;
	}

	private CoMenuExtender createMenuExtender(final CoLayoutDocument document) {
		return new CoMenuExtender() {
			public void extendMenu(CoMenuBar menuBar, CoMenuBuilder builder) {
				CoSubMenu fileMenu = (CoSubMenu) menuBar.getMenu(0);
				fileMenu.remove(0); // Remove spawn action menu for two reasons. 
				// 1) Spawn is not very useful in a stand alone layout editor
				// 2) The menu item is called "open" which is needed for file related action 
				fileMenu.remove(0); // Remove the second menu item that also has to do with the spawn action
				fileMenu.remove(0); // And finally remove the separator.

				JMenuItem mi;

				mi =
					fileMenu.insertBefore(
						CoLayoutEditorClientConstants.MENU_FILE_PRINT_SETTINGS,
						CoLayoutEditorClientConstants.MENU_FILE_NEW_DOCUMENT);
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						createNewDocument(null);
					}
				});

				document.extendMenu(menuBar, builder);

				fileMenu.addSeparator();

				mi = fileMenu.add(CoLayoutEditorClientConstants.MENU_FILE_EXIT);
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						Iterator iter = m_layoutDocuments.iterator();
						while (iter.hasNext()) {
							CoLayoutDocument doc = (CoLayoutDocument) iter.next();
							// Cancel the quit action if the user cancel the save question.
							if (!doc.saveIfUserWantsOrCancel())
								return;
						};
						System.exit(0);
					}
				});

				CoSubMenu helpMenu = (CoSubMenu) menuBar.getMenu(8);
				mi = helpMenu.add(CoSolitaryLayoutEditorConstants.MENU_HELP_RELEASE_NOTES);
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						showReleaseNotes();
					}
				});
				
				mi = helpMenu.add(new JMenuItem("Show memory monitor"));
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						showMemoryMonitor();
					}
				});


			}

		};
	}

	/**
	 * Creates a new layout document with one master page and one page
	 * with two layers where one of the layers is the master page.
	 */
	private void createNewDocument(CoStatusShower shower) 
	{
		if (shower != null)
			shower.showStatus("Create new empty layout");
		CoLayoutDocument layoutDocument = new CoLayoutDocument(this);
		layoutDocument.createAndAddMasterPage(getDefaultPageSize());
		layoutDocument.createAndAddPage(getDefaultPageSize());
		layoutDocument.setFileStoreSupport(fileStoreSupport);
		if (shower != null)
			shower.showStatus("Open layout editor");
		openEditor(layoutDocument);
	}

	private void openEditor(CoLayoutDocument layoutDocument) {
		layoutDocument.openEditor(createMainEditorConfiguration(layoutDocument));
		m_layoutDocuments.add(layoutDocument);
	}

	/**
	 * Is called by the document when the user close the documents window.
	 */
	public void isClosing(CoLayoutDocument layoutDocument) {
		m_layoutDocuments.remove(layoutDocument);
		if (m_layoutDocuments.size() < 1)
			System.exit(0);
	}

	public String getName() {
		return "fristående";
	}

	public int getUntitledNumber() {
		return m_untitledNumber++;
	}

	private FileFilter getDocumentFileFilter() {
		return new FileFilter() {
			public boolean accept(File file) {
				if (file.isDirectory())
					return true;
				String name = file.getName().toLowerCase();
				return name.endsWith(".xml");
			}
			public String getDescription() {
				return "XML";
			}

		};
	}

	private FileFilter getZipDocumentFileFilter() {
		return new FileFilter() {
			public boolean accept(File file) {
				if (file.isDirectory())
					return true;
				ZipFile zipFile;
				try {
					zipFile = new ZipFile(file);
				} catch (ZipException e) {
					return false;
				} catch (IOException e) {
					return false;
				}
				return (zipFile.getEntry(CoLayoutDocument.NAME_IN_ZIP_ARCHIVE) != null);
			}
			public String getDescription() {
				return "Layout editor document";
			}

		};
	}

	private void addSystemProperties() {
		// CoFontRepositor was designed to run headless on server and could therefor load
		// it's fonts from anywhere since the JVM on the server did not have to load the same fonts.
		// Now when we run as a single desktop app we direct that property to where the JVM
		// load it's fonts.
		System.setProperty(CoFontRepositoryIF.PROPERTY_KEY_FONT_PATH, getJavaFontDir().getPath());
	}

	
	private void showReleaseNotes() {
		URL url = CoResourceLoader.getURL(CoSolitaryLayoutEditor.class, CoSolitaryLayoutEditorResources.getName(RELEASE_NOTES_URL));
		CoLayoutEditor.showHTMLwindow(url, m_releaseNotesWindow, CoGUI.centerOnScreen(800, Integer.MAX_VALUE));
	}
	
	private void showMemoryMonitor() {
		new MemoryMonitor().setVisible(true);
	}
	
	private void installFonts(CoStatusShower shower) {
		CoFontRepositoryManager manager = CoFontRepositoryManager.getSingleton();
		installFonts(shower, manager, getJavaFontDir());
//		System.out.println("Fonts in GraphicsEnvironment:");
//		listAllFonts();
	}

	private void installFonts(CoStatusShower shower, CoFontRepositoryManager manager, File fontDir) {
		if (fontDir == null)
			return;
		File[] fonts = fontDir.listFiles();
		for (int i = 0; i < fonts.length; ++i) {
			File file = fonts[i];
			if (file.isDirectory()) 
				continue;
			try {
				manager.installFontFile(file, shower);
			} catch (RuntimeException e) {
				System.out.println("Unable to install font " + file.getName() + " " + e);				
			} catch (CoFontException e) {
				System.out.println("Unable to install font " + file.getName() + " " + e);
			}
		};
	}

	private File getJavaFontDir() {
		String javaHome = System.getProperty("java.home");
		return new File(javaHome, "lib/fonts");
	}
	
//	private String getHostFontDir()
//	{
//		return sun.font.FontManager.getFontPath(true);    // Java 1.5
//	    return sun.awt.font.NativeFontWrapper.getFontPath(true); // Java 1.4 	    
//	}
		
	private void listAllFonts() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts = ge.getAllFonts();

		// Iterate the font family names
		for (int i = 0; i < fonts.length; i++) {
			System.out.println(fonts[i].getFontName(Locale.ENGLISH));
		}
	}
	
}
