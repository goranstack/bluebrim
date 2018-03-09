package com.bluebrim.resource.shared;

import javax.swing.*;

/**
 * This must be subclassed to construct menu item resources.
 * 
 * @author Markus Persson 2002-10-31
 */
public abstract class CoDefMenuItemResource implements CoMenuItemResource {
	private int m_ordinal;
	private transient CoMenuItemResource m_resource;
	
	protected CoDefMenuItemResource(int ordinal) {
		m_ordinal = ordinal;
	}
	
	private CoMenuItemResource resource() {
		if (m_resource == null) {
			m_resource = obtainResource();
		}
		return m_resource;
	}

	public String text() {
		return resource().text();
	}

	public KeyStroke accelerator() {
		return resource().accelerator();
	}

	public char mnemonic() {
		return resource().mnemonic();
	}
	
	protected abstract CoMenuItemResource obtainResource();
}
