package com.bluebrim.xml.shared;
import java.io.*;
import java.net.*;
import java.util.*;

import com.bluebrim.base.shared.debug.*;

/**
 * This class holds context information used by the model builders and XML builders
 * during object instantiaton. It gives the builders access to information
 * they need in order to build business objects and in some cases information on how and
 * where to store the XML representation of business objects.
 * <p>
 * Subclasses hold specific context regarding different domains, such as pageitem export/import
 * and other business object domains
 * <p>
 * Creation date: (1999-09-30 14:59:42)
 * @author: 
 */
public class CoXmlContext implements Serializable {
	private Map m_values;

	private boolean m_useDtd = false;
	private boolean m_useGOI = true;

	private class HashMapMap extends HashMap {
		public Object put(Object key, Object val) {
			HashMap map = (HashMap) super.get(key);
			if (map == null) {
				map = new HashMap();
				super.put(key, map);
			}
			return map.put(val, val);
		}

		public Object get(Object key, Object key2) {
			HashMap map = (HashMap) super.get(key);
			if (map == null) {
				return null;
			} else {
				return map.get(key2);
			}
		}
	}

	public CoXmlContext() {
	}

	
	public CoXmlContext(Object[][] values) {
		m_values = new HashMap(values.length);
		for (int i = 0; i < values.length; ++i) {
			m_values.put(values[i][0], values[i][1]);
		}		
	}

	/**
	 *	Name of top node in the case that a collection of objects are to be xml-ified
	 */
	private String m_topNodeName;

	/**
	 *	Name of the DTD used
	 */
	private String m_dtdName;

	/**
	 *	Registered leaf classes
	 */
	private HashMapMap m_leafClasses = new HashMapMap();

	/* 2001-10-23, Dennis: not used anywhere
	// Replaced URL with this. /Markus 2000-08-15
	private CoFileSystemProxy m_fileSystemProxy = new CoLocalFileSystemProxy(null);
	*/

	/**
	 * Returns a seekable stream corresponding to fileName.
	 *
	 * @author Markus Persson 2000-08-15
	 */
	/* 2001-10-23, Dennis: not used anywhere
	public SeekableStream resolveToSeekableStream(String fileName) throws IOException {
		return m_fileSystemProxy.getSeekableStreamFor(fileName);
	}
	*/

	/**
	 * Returns an input stream corresponding to fileName.
	 *
	 * @author Markus Persson 2000-08-15
	 */
	/* 2001-10-23, Dennis: not used anywhere
	public InputStream resolveToInputStream(String fileName) throws IOException {
		return m_fileSystemProxy.getInputStreamFor(fileName);
	}
	*/

	/**
	 * Path to where the xml file is to be written.
	 * Used when parts of the data are to be saved in separate files.
	 * <p>
	 * This path includes the file name.  It must be accessed using the
	 * <code>set</code> and <code>get</code> methods.
	 */
	// Path and file name temporarily (?) moved up from CoPageItemXmlContext.
	private String m_pathWithFileName;

	/**
	 *	State that can be used for carrying sequence number information
	 *  between subsequent exports (e.g. when using the same context to
	 *  export to multiple XML-files, whilst maintaining uniqueness of
	 *  numbered resources over all XML-files)
	 *
	 */
	private int m_sequenceNumber = 0;
	
	private List m_attachements;

	public String getDtdName() {
		return m_dtdName;
	}

	/**
	 * @return The name of the XML file (without the path).
	 */
	private final String getFileName() {
		if (m_pathWithFileName == null)
			return null;

		int lastSeparatorIndex = m_pathWithFileName.lastIndexOf(File.separator);

		if (lastSeparatorIndex == -1)
			return m_pathWithFileName;
		else
			return m_pathWithFileName.substring(lastSeparatorIndex + 1);
	}

	/**
	 * @return <code>null</code> if the XML file name is <code>null</code>,
	 *		  everything before the last dot if the XML file name contains a dot,
	 *        or the XML file name itself if it doesn't contain a dot.
	 */
	public final String getFileNamePrefix() {
		String fileName = getFileName();

		if (fileName == null) {
			return null;
		}

		int idx = fileName.indexOf(".");
		if (idx == -1) {
			return fileName;
		}
		return fileName.substring(0, idx);
	}

	/**
	 * @return The path to the XML file, without the file name, and without a trailing
	 * {@link java.io.File#separator separator character}.
	 */
	public final String getFilePathWithoutFileName() {
		if (m_pathWithFileName == null)
			return null;

		int lastSeparatorIndex = m_pathWithFileName.lastIndexOf(File.separator);

		if (lastSeparatorIndex == -1)
			return "";
		else
			return m_pathWithFileName.substring(0, lastSeparatorIndex);
	}

	public int getSequenceNumber() {
		return m_sequenceNumber;
	}

	public String getTopNodeName() {
		return m_topNodeName;
	}

	/**
	 *	Implement this to add to the initialization of the
	 *  XML parser. Specifically, new builders can be registered
	 *
	 */
	public void initializeXmlParser(CoXmlParserIF parser) {
	}

	public int nextSequenceNumber() {
		return m_sequenceNumber++;
	}

	// Allows the context to perform post load setup
	public void postLoadSetup() {
	}
	
	public Object getValue(Object key) {
		return m_values.get(key);
	}
	
	public void putValue(Object key, Object value) {
		m_values.put(key,value);
	}

	/**
	 *	Registers a childclass for a parentclass for which the export recursion shold stop.
	 *  This is used by the xml visitor to determine when to stop
	 *  exporting in a tree of objects
	 */
	public void registerStopAtClass(Class parentClass, Class childClass) {
		m_leafClasses.put(parentClass, childClass);
	}

	public void resetSequenceNumber() {
		setSequenceNumber(0);
	}

	/**
	 * This method accepts a relative or absolute file name and
	 * returns a java <code>File</code> pointing out a file that
	 * exists in the file system.
	 * <p>
	 * Creation date: (2001-06-01 14:21:34)
	 * 
	 * @author Johan Walles
	 */
	public File resolveFileName(String fileName) throws FileNotFoundException {
		File returnMe;

		returnMe = new File(fileName);
		if (returnMe.exists())
			return returnMe;

		returnMe = new File(getFilePathWithoutFileName() + File.separator + fileName);
		if (returnMe.exists())
			return returnMe;

		throw new FileNotFoundException(fileName + " not found");
	}

	public void setDtdName(String dtdName) {
		m_dtdName = dtdName;
	}

	/**
	 * Set the name of the file.  The path <em>must not</em> contain any occurence of the
	 * current platform's {@link java.lang.io.File#separator separator character}.
	 */
	public void setFileName(String fileName) {
		CoAssertion.assertTrue(fileName != null, "A null file name must be set using setFileNameAndPath()");

		CoAssertion.assertTrue(getFilePathWithoutFileName() != null, "Current path is null, call setFileNameAndPath() instead.");

		// Assert that the fileName does not contain a File.pathSeparator
		CoAssertion.assertTrue((fileName.indexOf(File.separator) == -1), "The file name must contain a File.separator.");

		m_pathWithFileName = getFilePathWithoutFileName() + File.separator + fileName;
	}

	/**
	 * Creation date: (2001-03-23 11:41:42)
	 * @param fileNameAndPath A string containing the file name and
	 * the complete path to the file, or <code>null</code>.
	 */
	public void setFileNameAndPath(String fileNameAndPath) {
		if (fileNameAndPath == null) {
			m_pathWithFileName = null;
			return;
		}

		// Sanity check the file name and path String
		CoAssertion.assertTrue(fileNameAndPath.indexOf(File.separator) != -1, "The path must contain at least one File.separator");

		m_pathWithFileName = fileNameAndPath;
	}

	public void setSequenceNumber(int seqNo) {
		m_sequenceNumber = seqNo;
	}

	public void setTopNodeName(String nodeName) {
		m_topNodeName = nodeName;
	}

	public void setUrl(URL url) {
		//2001-10-23, Dennis: not used anywhere	m_fileSystemProxy.setBaseURL(url);
	}

	public void setUseDtd(boolean useDtd) {
		m_useDtd = useDtd;
	}

	public boolean stopAt(Object parent, Object child) {
		if (parent == null || child == null) {
			return false;
		}
		return m_leafClasses.get(parent.getClass(), child.getClass()) != null;
	}

	public boolean useDtd() {
		return m_useDtd;
	}

	public boolean useGOI() {
		return m_useGOI;
	}

	/**
	 * Control the use of GOI's at XML-export. False means that all objects
	 * are exported. True means that certain objects is exported by a GOI reference
	 * instead of the object itself. Which object is hard coded but should be parameterized
	 * as soon we can figure out how to express such a thing. /Göran S
	 */ 
	public void setUseGOI(boolean useGOI) {
		m_useGOI = useGOI;
	}

	public List getAttachements() {
		return m_attachements;
	}

	public void setAttachements(List attachements) {
		m_attachements = attachements;
	}

}