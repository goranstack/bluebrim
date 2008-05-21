package com.bluebrim.resource.shared;

import java.net.*;

/**
 * This must be subclassed to construct URL resources.
 * 
 * In the current version nothing more needs to be done since
 * the domain is determined from the class name of the subclass.
 * 
 * However, if URL resources are to be serialized,
 * canonicalization should be implemented and then subclasses
 * probably should do more, for sake of efficiency.
 * 
 * @author Markus Persson 2002-06-05
 */
public class CoUnlocalizedUrlResource implements CoUrlResource {
	protected String m_fileName;
	private transient URL m_url;
	
	protected CoUnlocalizedUrlResource(String fileName) {
		m_fileName = fileName;
	}
	
	public URL url() {
		if (m_url == null) {
			m_url = CoResourceLoader.getURL(this.getClass(), m_fileName);
		}
		return m_url;
	}

}

