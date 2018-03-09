package com.bluebrim.base.client.datatransfer;

import java.awt.datatransfer.Clipboard;

/**
 * Utility class representing a global application clipboard.
 * This class is never instanciated, it has static methods to
 * be used within a client session.
 *
 * @author Ali Abida & Peter Jakubicki 2000-10-31.
 */
public class CoClipBoard {
	private static final Clipboard m_clipboard = new Clipboard("BlueBrim Clipboard");
public static Clipboard getClipboard() {
	return m_clipboard;
}
}
