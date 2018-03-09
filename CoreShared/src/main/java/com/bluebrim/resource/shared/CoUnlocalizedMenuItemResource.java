package com.bluebrim.resource.shared;

import javax.swing.*;

/**
 * @author Markus Persson 2002-10-31
 */
public class CoUnlocalizedMenuItemResource implements CoMenuItemResource {

	private String m_text;
	private char m_mnemonic;
	private KeyStroke m_accelerator;

	public CoUnlocalizedMenuItemResource(String text, char mnemonic, KeyStroke accelerator) {
		m_text = text;
		m_mnemonic = mnemonic;
		m_accelerator = accelerator;
	}

	public String text() {
		return m_text;
	}

	public char mnemonic() {
		return m_mnemonic;
	}

	public KeyStroke accelerator() {
		return m_accelerator;
	}

}
