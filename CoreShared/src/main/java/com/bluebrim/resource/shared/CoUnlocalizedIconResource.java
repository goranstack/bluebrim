package com.bluebrim.resource.shared;

import javax.swing.*;

/**
 * This must be subclassed to construct icon resources.
 * 
 * In the current version nothing more needs to be done since
 * the domain is determined from the class name of the subclass.
 * 
 * However, if icon resources are to be serialized,
 * canonicalization should be implemented and then subclasses
 * probably should do more, for sake of efficiency.
 * 
 * @author Markus Persson 2002-06-04
 */
public abstract class CoUnlocalizedIconResource implements CoIconResource {
	protected String m_fileName;
	private transient Icon m_icon;
	
	protected CoUnlocalizedIconResource(String fileName) {
		m_fileName = fileName;
	}
	
	public Icon icon() {
		if (m_icon == null) {
			m_icon = CoResourceLoader.loadIcon(this.getClass(), m_fileName);
		}
		return m_icon;
	}
}
