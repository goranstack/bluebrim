package com.bluebrim.font.impl.client;
import java.io.*;
import java.rmi.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.font.impl.server.type1.*;
import com.bluebrim.font.impl.shared.*;
import com.bluebrim.font.impl.shared.metrics.*;
import com.bluebrim.font.shared.*;
import com.bluebrim.font.shared.metrics.*;

/**
 * Client-side interface to the font repository, for manipulating the fonts
 * and font data stored in the font repository.<p>
 *
 * Creation date: (2001-04-24 17:14:13)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public class CoFontRepositoryManager extends CoSimpleObject {
	// where to start counting font versions
	private final static int INITIAL_FONT_FACE_VERSION = 0;

	private static CoFontRepositoryManager SINGLETON;

	private CoFontRepositoryIF m_server;
	private List m_familyUnion;


	public class Family extends CoSimpleObject {
		private String m_name;

		public Family(String name) {
			m_name = name;
		}

		public String getName() {
			return m_name;
		}
	}

/**
 * Private constructor, only called from getSingleton, and just once.
 */
private CoFontRepositoryManager(CoFontRepositoryIF server) {
	if (CoAssertion.ASSERT) CoAssertion.notNull(server, "server");
	
	m_server = server;
}


public boolean createFontFace(CoFontFaceSpec spec, CoFontMetricsData data, CoFontPostscriptData postscriptData, CoFontAwtData awtData, CoFontFileContainer fileContainer) throws CoFontException {
	return m_server.registerNewFontFace(spec, data, postscriptData, awtData, fileContainer);
}


public static void createTestData(CoFontRepositoryIF server) {
	CoFontRepositoryManager man = new CoFontRepositoryManager(server);
//	man.TESTING_ONLY_createTestData();
	man.readInitFontData("c:\\ps_save\\fontList.txt");
}


public List getFontFamilyList() {
	// FIXME: Improve with sorting and pending changes. /Markus 2001-10-15
	Collection nameList = CoAbstractFontMapper.getFontMapper().getFontFamilies();
	List families = new ArrayList(nameList.size());
	Iterator names = nameList.iterator();
	while (names.hasNext()) {
		families.add(new Family((String) names.next()));
	}
	return families;
}


public static synchronized CoFontRepositoryManager getSingleton() {
	if (SINGLETON == null) {
		CoFontRepositoryIF server = null;
		try
	    {
		    server = CoFontServerProvider.getFontServer().getFontRepository();
	    } catch (RemoteException e)
	    {
	        throw new RuntimeException(e);
	    }

		
		SINGLETON = new CoFontRepositoryManager(server);
	}

	return SINGLETON;
}


public boolean hideFontFace(CoFontFaceSpec spec) {
	return m_server.hideFontFace(spec);
}


public void installFontFile(CoFontFaceSpec spec, CoFontFileInfoExtractor font, CoFontAwtData awtData) throws CoFontException {
	installFontFile(spec, font, awtData, null, null, null);
}


/**
 * createFontFace method comment.
 * if fontfacedata, postscript or container is null, data is read from parser.
 */
public void installFontFile(CoFontFaceSpec spec, CoFontFileInfoExtractor font, CoFontAwtData awtData, 
			CoFontMetricsData metricsData, CoFontPostscriptData postscriptData, CoFontFileContainer fileContainer) 
			throws CoFontException {

	if (metricsData == null) {
		metricsData = font.getMetricsData();
	}

	if (postscriptData == null) {
		postscriptData = font.getPostscriptData();
	}
	
	if (fileContainer == null) {
		fileContainer = font.getFileContainer();
	}	
	
	// workaround to identify type1 fonts, which awt currently have some problems with
	if (font instanceof CoType1FileInfoExtractor) {
		awtData.awtWorkaround_setType1(true);
	}

	createFontFace(spec, metricsData, postscriptData, awtData, fileContainer);
}


public void installFontFile(CoFontFaceSpec spec, File fontFile, CoFontAwtData awtData) throws CoFontException {
	CoFontFileInfoExtractor font;

	font = CoAbstractFontFileInfoExtractor.parseFontFile(fontFile);
	if (font == null) {
		throw new CoFontException("Unknown font file format in file '" + fontFile + "'.");
	}
	
	installFontFile(spec, font, awtData);
}


public void installFontFile(File fontFile, CoStatusShower shower) throws CoFontException {
	CoFontFileInfoExtractor font;

	font = CoAbstractFontFileInfoExtractor.parseFontFile(fontFile);
	if (font == null) {
		throw new CoFontException("Unknown font file format in file '" + fontFile + "'.");
	}
	CoFontFaceSpec fontFaceSpec = font.suggestedFontFaceSpec();
	CoFontAwtData fontAwtData = font.suggestedAwtData();
	shower.showStatus("Installing: " + fontFaceSpec.toString() + " using " + fontAwtData.getFontName());

	installFontFile(fontFaceSpec, font, fontAwtData);	
}


private void installFontFile(String fileName, CoFontFaceSpec spec, CoFontAwtData awtData, CoTrackingMetrics tracking) throws CoFontException {
	System.out.println("Installing font: " + fileName + " as " + spec);
	
	File file = new File(fileName);
	CoFontFileInfoExtractor parser = CoAbstractFontFileInfoExtractor.parseFontFile(file);
	if (spec == null) {
		spec = parser.suggestedFontFaceSpec();
	}
	if (awtData == null) {
		awtData = parser.suggestedAwtData();
	}
	CoFontMetricsData metrics = parser.getMetricsData();
	if (tracking != null) {
		metrics = new CoFontMetricsDataImplementation(metrics.getHorizontalMetrics(), metrics.getLineMetrics(),
			metrics.getPairKerningMetrics(), tracking, metrics.getItalicAngle());
	}

	installFontFile(spec, parser, awtData, metrics, parser.getPostscriptData(), parser.getFileContainer());
}


public boolean permanentlyRemoveFontFace(CoFontFace face) {
	return m_server.permanentlyRemoveFontFace(face);
}


private void readInitFontData(String initFileName) {
	String fontHome = System.getProperty("java.awt.fonts");

	if (fontHome.length() == 0)
		fontHome = CoFontFace.class.getResource("").getFile() + "fonts";

	fontHome += File.separatorChar;

	System.out.println("Reading initial fonts from " + fontHome);
	try {

		// Parse the text font init file
		FileReader inFile = new FileReader(initFileName);
		LineNumberReader in = new LineNumberReader(inFile);

		String line = null;

		while (in.ready()) {
			line = in.readLine();
			if (line.charAt(0) == '#') continue; // Skip line if comment
			if (line.length() == 0) continue; // Skip empty lines
			
			StringTokenizer tokenizer = new StringTokenizer(line, ",");
			if (tokenizer.countTokens() < 6) {
				System.out.println("Error in font init file on line " + in.getLineNumber());
				System.out.println("Line is: " + line);
				System.out.println("Aborting font reading!");
				return;
			}
			String fontName = tokenizer.nextToken();
			String fileName = tokenizer.nextToken();
			int weight = Integer.parseInt(tokenizer.nextToken());
			int style = Integer.parseInt(tokenizer.nextToken());
			int variant = Integer.parseInt(tokenizer.nextToken());
			int stretch = Integer.parseInt(tokenizer.nextToken());

			installFontFile(fontHome + fileName, new CoFontFaceSpec(fontName, weight, style, variant, stretch), null, null);
		}
	} catch (CoFontException e) {
		System.out.println("ERROR: Test data fonts were not correctly read!!!");
		System.out.println(e.toString());
	} catch (IOException e) {
		System.out.println("Font reading error.");
		System.out.println(e.toString());
	}
}


/**
 * returns false if remove fails.
 */
public boolean removeCatalog(String name) {
	return m_server.removeCatalog(name);
}


public void setFallbackFamily(String familyName) {
	m_server.setFallbackFamily(familyName);
}


private void TESTING_ONLY_createTestData() {
	String fontHome = System.getProperty("java.awt.fonts");

	if (fontHome.length() == 0)
		fontHome = CoFontFace.class.getResource("").getFile() + "fonts";

	fontHome += File.separatorChar;

	System.err.println("Reading test data fonts from " + fontHome);

	CoFontFace f = null;
	String name = null;
	try {

		// Arial
		name = "Arial";

		installFontFile(fontHome + "arial.ttf",
			new CoFontFaceSpec(name, CoFontConstants.NORMAL_WEIGHT, CoFontConstants.NORMAL_STYLE, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null, null);
				
		installFontFile(fontHome + "ariali.ttf",
			new CoFontFaceSpec(name, CoFontConstants.NORMAL_WEIGHT, CoFontConstants.ITALIC, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null, null);
		
		installFontFile(fontHome + "arialbd.ttf",
			new CoFontFaceSpec(name, CoFontConstants.BOLD, CoFontConstants.NORMAL_STYLE, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null, null);
		
		installFontFile(fontHome + "arialbi.ttf",
			new CoFontFaceSpec(name, CoFontConstants.BOLD, CoFontConstants.ITALIC, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null, null);
		
		// Concorde
		name = "Concorde BE";

		installFontFile(fontHome + "Qqrg____.pfb", 
			new CoFontFaceSpec(name, CoFontConstants.NORMAL_WEIGHT, CoFontConstants.NORMAL_STYLE, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null,
			new CoTrackingMetricsImplementation(new float[] { 0, 24, 36, 48, 64, 65 }, 
				new float[] { 0, -2, -4, -2, -4, -4 }));

		installFontFile(fontHome + "Qqm_____.pfb", 
			new CoFontFaceSpec(name, CoFontConstants.BOLD, CoFontConstants.NORMAL_STYLE, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null,
			new CoTrackingMetricsImplementation(new float[] { 0, 24, 36, 48, 64, 65 }, 
				new float[] { 0, -2, -4, -2, -4, -4 }));

		installFontFile(fontHome + "Qqi_____.pfb", 
			new CoFontFaceSpec(name, CoFontConstants.NORMAL_WEIGHT, CoFontConstants.ITALIC, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null,
			new CoTrackingMetricsImplementation(new float[] { 0, 24, 36, 48, 64, 65 }, 
				new float[] { 0, -2, -4, -2, -4, -4 }));

		installFontFile(fontHome + "Qqmi____.pfb", 
			new CoFontFaceSpec(name, CoFontConstants.BOLD, CoFontConstants.ITALIC, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null,
			new CoTrackingMetricsImplementation(new float[] { 0, 24, 36, 48, 64, 65 }, 
				new float[] { 0, -2, -4, -2, -4, -4 }));


		// Franklin Gothic Book
		name = "ITC Franklin Gothic";
		installFontFile(fontHome + "Frw_____.pfb", 
			new CoFontFaceSpec(name, CoFontConstants.NORMAL_WEIGHT, CoFontConstants.NORMAL_STYLE, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH), 
			null, null);
		
		installFontFile(fontHome + "Frwo____.pfb", 
			new CoFontFaceSpec(name, CoFontConstants.NORMAL_WEIGHT, CoFontConstants.ITALIC, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH), 
			null, null);

		// ITC Franklin Gothic (Heavy)
//		name = "ITC Franklin Gothic";
		installFontFile(fontHome + "Frh_____.pfb",
			new CoFontFaceSpec(name, CoFontConstants.W900, CoFontConstants.NORMAL_WEIGHT, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null, null);

		installFontFile(fontHome + "Frho____.pfb",
			new CoFontFaceSpec(name, CoFontConstants.W900, CoFontConstants.ITALIC, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null, null);


		// Futura Medium
		name = "Futura Medium";
		installFontFile(fontHome + "Fu______.pfb", 
			new CoFontFaceSpec(name, CoFontConstants.NORMAL_WEIGHT, CoFontConstants.NORMAL_STYLE, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null, null);
		

		installFontFile(fontHome + "Fub_____.pfb", 
			new CoFontFaceSpec(name, CoFontConstants.BOLD, CoFontConstants.NORMAL_STYLE, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null, null);

		installFontFile(fontHome + "Fuo_____.pfb", 
			new CoFontFaceSpec(name, CoFontConstants.NORMAL_WEIGHT, CoFontConstants.ITALIC, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null, null);

		installFontFile(fontHome + "Fubo____.pfb", 
			new CoFontFaceSpec(name, CoFontConstants.BOLD, CoFontConstants.ITALIC, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null, null);
		
		// Futura Book
		name = "Futura Book";
		installFontFile(fontHome + "Fuw_____.pfb", 
			new CoFontFaceSpec(name, CoFontConstants.NORMAL_WEIGHT, CoFontConstants.NORMAL_STYLE, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null, null);
		
		installFontFile(fontHome + "Fuwo____.pfb", 
			new CoFontFaceSpec(name, CoFontConstants.NORMAL_WEIGHT, CoFontConstants.ITALIC, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null, null);

		installFontFile(fontHome + "Fub_____.pfb", 
			new CoFontFaceSpec(name, CoFontConstants.BOLD, CoFontConstants.NORMAL_STYLE, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null, null);

		installFontFile(fontHome + "Fubo____.pfb", 
			new CoFontFaceSpec(name, CoFontConstants.BOLD, CoFontConstants.ITALIC, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null, null);

		// Antique Olive (Black)
		name = "Antique Olive";
		installFontFile(fontHome + "Aqbl____.pfb",
			new CoFontFaceSpec(name, CoFontConstants.BOLD, CoFontConstants.NORMAL_STYLE, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null, null);

		// ITC Stone Serif
		name = "ITC Stone Serif";
		installFontFile(fontHome + "Sr______.pfb",
			new CoFontFaceSpec(name, CoFontConstants.NORMAL_WEIGHT, CoFontConstants.NORMAL_STYLE, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null, null);
		
		installFontFile(fontHome + "Sri_____.pfb",
			new CoFontFaceSpec(name, CoFontConstants.NORMAL_WEIGHT, CoFontConstants.ITALIC, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null, null);

		installFontFile(fontHome + "Srb_____.pfb",
			new CoFontFaceSpec(name, CoFontConstants.BOLD, CoFontConstants.NORMAL_STYLE, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null, null);

		installFontFile(fontHome + "Srbi____.pfb",
			new CoFontFaceSpec(name, CoFontConstants.BOLD, CoFontConstants.ITALIC, CoFontConstants.NORMAL_VARIANT, CoFontConstants.NORMAL_STRETCH),
			null, null);
			
		
	} catch (CoFontException e) {
		System.out.println("ERROR: Test data fonts were not correctly read!!!");
		System.out.println(e.toString());
	}
}


public boolean updateCatalog(String name, CoFontCatalog catalog) {
	return m_server.updateCatalog(name, catalog);
}
}