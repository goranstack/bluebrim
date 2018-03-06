package com.bluebrim.font.shared;
import java.rmi.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.font.impl.shared.*;
import com.bluebrim.font.shared.metrics.*;
import com.bluebrim.xml.shared.*;

/**
 * Remote interface for the Font Repository, i.e. the central which handles all fonts and their data.
 * Creation date: (2001-04-24 15:35:57)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */

public interface CoFontRepositoryIF extends Remote, CoXmlEnabledIF {

	public final static String PROPERTY_KEY_XML_DATA = "calvin.fonts.repository.xml"; // where xml data is stored, if it exists
	public final static String PROPERTY_KEY_FONT_PATH = "calvin.fonts.server.path"; // where the fonts are stored on the server

	public CoFontAwtData getAwtData(CoFontFace face);


	// FONT CATALOG METHODS

	// get catalog info
	public CoFontCatalog getCatalog(String name);


	public Map getCatalogs();  // [String -> CoFontCatalog]


	public String getFallbackFamily();


	// FONT FACE METHODS
	
	// get complete lists; may be large -- use only when neccessary!
	public Set getFontFaces();  // [CoFontFace]


	public CoFontCatalog getFontFamilies();  // get all font families


	public CoFontFileContainer getFontFileContainer(CoFontFace face);


public CoAbstractFontMapper getFontMapper();

	// get large binary data for a font face
	public CoFontMetricsData getMetricsData(CoFontFace face);


	public CoFontPostscriptData getPostscriptData(CoFontFace face);


	public Map getSpecMapping();  // [CoFontFaceSpec -> CoFontFace]


	public boolean hideFontFace(CoFontFaceSpec spec);


	public boolean permanentlyRemoveFontFace(CoFontFace face);


	public boolean removeCatalog(String name);


	public void setFallbackFamily(String familyName);


	// create/remove/update catalogs
	public boolean updateCatalog(String name, CoFontCatalog catalog);

	public void replaceWith(CoFontRepositoryIF other);
public void addAll(CoFontRepositoryIF other);

public Map getPostscriptDataItems();

public Map getMetricsDataItems();

public Map getFontFileContainers();

public Map getAwtDataItems();


public void installInitialData(CoStatusShower shower);

// create/remove font faces and associated spec->fontface mappings
public boolean registerNewFontFace(CoFontFaceSpec spec, CoFontMetricsData data, CoFontPostscriptData postscriptData, CoFontAwtData awtData, CoFontFileContainer fileContainer) throws CoFontException;
}