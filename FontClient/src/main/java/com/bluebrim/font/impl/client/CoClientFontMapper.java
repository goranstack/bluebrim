package com.bluebrim.font.impl.client;
import java.awt.Font;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.bluebrim.base.shared.debug.CoAssertion;
import com.bluebrim.font.impl.shared.CoFontFileContainer;
import com.bluebrim.font.shared.CoAbstractFontMapper;
import com.bluebrim.font.shared.CoFontAwtData;
import com.bluebrim.font.shared.CoFontCatalog;
import com.bluebrim.font.shared.CoFontException;
import com.bluebrim.font.shared.CoFontFace;
import com.bluebrim.font.shared.CoFontFaceSpec;
import com.bluebrim.font.shared.CoFontPostscriptData;
import com.bluebrim.font.shared.CoFontRepositoryIF;
import com.bluebrim.font.shared.metrics.CoFontMetricsData;
import com.bluebrim.gui.client.CoGUI;
import com.bluebrim.observable.CoChangedObjectEvent;
import com.bluebrim.observable.CoChangedObjectListener;
import com.bluebrim.observable.CoObservable;

/**
 * A singleton caching client side interface to the font repository.
 * Creation date: (2001-04-25 12:52:41)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public class CoClientFontMapper extends CoAbstractFontMapper implements CoChangedObjectListener {
	private CoFontRepositoryIF m_server;
	private File m_fontPath;

	// copy of data on server, is updated when server is changed
	private Map m_catalogs; // [ String -> CoFontCatalog ]
	private Map m_specMapping; // [ CoFontFaceSpec -> CoFontFace ]
	private Set m_fontFaces; // [ CoFontFace ]
	private CoFontCatalog m_fontFamilies;
	private String m_fallbackFamily;

	// cache of requested data from server
	private Map m_metricsDataItems = new HashMap(); // [ CoFontFace -> CoFontMetricsData ]
	private Map m_awtDataItems = new HashMap(); // [ CoFontFace -> CoFontAwtData ]
	private Map m_postscriptDataItems = new HashMap(); // [ CoFontFace -> CoFontPostscriptData ]
	private Map m_fontFileContainers = new HashMap(); // [ CoFontFace -> CoFontFileContainers ]
	private Map m_specsPerFamily = new HashMap(); // [ String -> Set ] ( familyName -> [ CoFontFaceSpec ] )

	private Font[] m_allAwtFonts;
	private List m_newlyInstalledFonts = new LinkedList(); // [ CoFontFace ]

/**
 * Public constructor only to be used from CoAbstractFontMapper ...
 *
 * NOTE: To avoid compile time dependencies, this constructor is called using
 * reflection. This means that if it is ever changed, change the one place where
 * it it used, namely CoAbstractFontMapper.getFontMapper(). /Markus
 */
public CoClientFontMapper(CoFontRepositoryIF server) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(server, "server");

	// Check font path correctness
	String fontPath = System.getProperty("java.awt.fonts");
	// Note: this should really be System.getProperty, as opposed to CoSystemProxy.getProperty, since we must
	// get the same property as AWT is using.
	if (fontPath == null) {
		throw new RuntimeException("No font path specified. Please set the JAVA_FONTS environment variable to where your fonts are to be stored.");
	} else if (fontPath.indexOf(File.pathSeparator) != -1) {
		throw new RuntimeException("No more than one font path must be specified. Please correct the JAVA_FONTS environment variable, or similar.");
	}
	m_fontPath = new File(fontPath);

	if (!m_fontPath.isDirectory()) {
		// FIXME: This should really be an exception, but everyone got this wrong and I
		// can't fix that now...
		System.out.println("WARNING!!!");
		System.out.println("Font path (JAVA_FONTS environment variable) is not correct! Font transfer and display WILL NOT WORK!!!");
		System.out.println("Please correct this, typically by setting it to the 'fonts' directory in the client directory, ");
		System.out.println("or to the com.bluebrim.font.impl.shared.fonts resource in VAJ.");
	}
		
	System.out.println("Setting client font path to " + m_fontPath);
		
	// Get initial data from server, and subscribe to changes
	m_server = server;
	CoObservable.addChangedObjectListener(this, m_server);
	update();

	// We need to do this to properly "initialize" the AWT font machinery, due to some
	// obscure bug in AWT, that appearantly does not display all fonts correctly if they haven't
	// been "accessed" first.

	// also, since dynamic loading of fonts are not yet supported, this is the actual list of all fonts
	// actually available to AWT without restarting the VM.
	m_allAwtFonts = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
}


/**
 * Return a Set of the names of all CoFontCatalogs.
 * Creation date: (2001-05-15 12:23:51)
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */

public Set getAllCatalogs() {
	return m_catalogs.keySet();
}


/**
 * Fetch the AwtData from the repository for the associated CoFontFace, or from the cache if possible.
 */
public CoFontAwtData getAwtData(CoFontFace face) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(face, "face");
	
	CoFontAwtData data = (CoFontAwtData) m_awtDataItems.get(face);
	if (data == null) { // not in cache, fetch from server and store in cache
		data = m_server.getAwtData(face);
		m_awtDataItems.put(face, data);
	}
	return data;
}


/**
 * Return the CoFontCatalog associated with this font catalog name, or null if none.
 * Creation date: (2001-05-15 12:24:24)
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */

public CoFontCatalog getCatalog(String name) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(name, "name");
	
	return (CoFontCatalog) m_catalogs.get(name);
}


/**
 * Return the CoFontFace currently associated with this CoFontFaceSpec, or null if none. This will
 * only return an exact match. See getFontFaceOrFallback for a failsafe version.
 * Creation date: (2001-05-15 12:24:53)
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */

public CoFontFace getFontFace(CoFontFaceSpec spec) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(spec, "spec");
	
	return (CoFontFace) m_specMapping.get(spec);
}


public Set getFontFaces() {
	return m_fontFaces;
}


public CoFontCatalog getFontFamilies() {
	return m_fontFamilies;
}


/**
 * Fetch the CoFontFileContainer from the repository for the associated CoFontFace, or from the cache if possible.
 */
public CoFontFileContainer getFontFileContainer(CoFontFace face) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(face, "face");
	
	CoFontFileContainer data = (CoFontFileContainer) m_fontFileContainers.get(face);
	if (data == null) { // not in cache, fetch from server and store in cache
		data = m_server.getFontFileContainer(face);
		m_fontFileContainers.put(face, data);
	}
	return data;
}


/**
 * Fetch the font metrics data from the repository for the associated CoFontFace, or from the cache if possible.
 */
public CoFontMetricsData getMetricsData(CoFontFace face) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(face, "face");
	
	CoFontMetricsData data = (CoFontMetricsData) m_metricsDataItems.get(face);
	if (data == null) { // not in cache, fetch from server and store in cache
		data = m_server.getMetricsData(face);
		m_metricsDataItems.put(face, data);
	}
	return data;
}


/**
 * Fetch the postscript data from the repository for the associated CoFontFace, or from the cache if possible.
 */
public CoFontPostscriptData getPostscriptData(CoFontFace face) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(face, "face");
	
	CoFontPostscriptData data = (CoFontPostscriptData) m_postscriptDataItems.get(face);
	if (data == null) { // not in cache, fetch from server and store in cache
		data = m_server.getPostscriptData(face);
		m_postscriptDataItems.put(face, data);
	}
	return data;
}


public Set getSpecsForFamily(String familyName) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(familyName, "familyName");
	
	Set familySpecs = (Set) m_specsPerFamily.get(familyName);

	if (familySpecs == null) {	// not in cache, create set and put in cache
		familySpecs = new HashSet();

		Iterator i = m_specMapping.keySet().iterator();
		while (i.hasNext()) {
			CoFontFaceSpec spec = (CoFontFaceSpec) i.next();
			if (spec.getFamilyName().equals(familyName)) {
				familySpecs.add(spec);
			}
		}
		m_specsPerFamily.put(familyName, familySpecs);
	}
	
	return familySpecs;
}


/**
 * Download and install the files needed by AWT to present a CoFontFace on the screen. This should only be
 * called if awtFontIsAvailable returns false, to avoid unnecessary file transfers. Also, the same problem
 * applies to this function as to awtFontIsAvailable (see the comments for that).
 * Creation date: (2001-05-15 12:39:05)
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 */

public synchronized void installAwtFont(CoFontFace face) throws CoFontException {
	if (CoAssertion.ASSERT) CoAssertion.notNull(face, "face");

	System.out.println("About to transfer font face " + face);
	
	CoFontFileContainer container = getFontFileContainer(face);

	container.writeFontFileToDisk(m_fontPath);

	// If this is the first font to be transferred, then notify the user about this
	if (m_newlyInstalledFonts.isEmpty()) {
		// Add face to list of installed fonts.
		m_newlyInstalledFonts.add(face);
		CoGUI.warn("One more more new fonts has been installed on your system. To view these fonts correctly, you need to close down and restart BlueBrim.");
		// FIXME: i18n?
	} else {
		m_newlyInstalledFonts.add(face);
	}
}


private void invalidateCache() {
	// remove all cached data
	m_metricsDataItems = new HashMap();
	m_awtDataItems = new HashMap();
	m_postscriptDataItems = new HashMap();
	m_fontFileContainers = new HashMap();
	m_specsPerFamily = new HashMap();
}


/**
 * Returns true if the specified CoFontFace is available on this client for rendering on the
 * screen. If not, it should be requested from the server, and the client (actually, the local VM)
 * needs to be restarted.<p>
 * <b>Note!</b> Due to limitations in AWT, it is not possible to implement this method correctly on
 * the client. <p>
 * 
 * The problem is: AWT specifies fonts based on the font's
 * internal font name. And the fileContainer tries to write to a file with the original 
 * font name. So, you can't have two versions of the same font displayed on AWT at the same
 * time. So for the moment, we'll just check the availability of the fontfacespec, and
 * assume that this is good enough. This is a lie, of course, but it'll have to do...
 *
 * @param face the font face to check for availability.
 *
 * @return true if the specified CoFontFace is available on this client.
 *
 * @see #installAwtFont(CoFontFace)
 * @see #installAwtFontIfNeeded(CoFontFace)
 */
public boolean isAwtFontAvailable(CoFontFace face) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(face, "face");

	// First check if font is in AWT font list

	String faceName = face.getAwtData().getFontName();
	for (int i = 0; i < m_allAwtFonts.length; i++) {
		Font font = m_allAwtFonts[i];
		if (font.getFontName(Locale.ENGLISH).equals(faceName)) {
			return true;
		}
	}

	// Not found in list of currently installed fonts. See if it just recently have been
	// transfered. In that case, treat it as installed anyway.

	if (m_newlyInstalledFonts.contains(face)) {
		return true;
	}

	// Not found at all, so return false
	return false;
}


/**
 * Returns true if the CoFontFace is "removed". Note that "removed" actually means "hidden"; a CoFontFace
 * is normally never actually removed, but just made hidden so no new texts can be written with that font.
 * A complete removal would mean that old text no longer could be rendered correct, and that is an
 * unfortunate situation. */
public boolean isRemoved(CoFontFace face) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(face, "face");
	
// a fontface is "removed" (actually hidden) if there is no current mapping for its spec.
	return !m_specMapping.containsKey(face.getSpec());
}


public void serverObjectChanged(CoChangedObjectEvent e) {
	// just a naive solution. if anything at all has changed at the server, request it all...
	update();
}


// this is quite a naive solution, that transfers _all_ data from the server to all clients, whenever
// any of the data is changed. This might be quite a bit, especielly if the number of fonts is large,
// and very unneccesary if the only change is e.g. a new font catalog entry.
private void update() {
	m_catalogs = m_server.getCatalogs();
	m_specMapping = m_server.getSpecMapping();
	m_fontFaces = m_server.getFontFaces();
	m_fontFamilies = m_server.getFontFamilies();
	m_fallbackFamily = m_server.getFallbackFamily();
	// perhaps this is too naive, but at least it ensures that the cache is correct.
	invalidateCache();
}

public String getRealFallbackFamily() {
	return m_fallbackFamily;
}
}