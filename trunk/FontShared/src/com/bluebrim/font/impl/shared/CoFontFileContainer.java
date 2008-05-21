package com.bluebrim.font.impl.shared;
import java.io.*;

/**
 * An encapsulation of the font file(s) that the font is originally read from, with the possibility
 * to rewrite them to disk, possibly on another machine after serialization.
 * Creation date: (2001-05-03 15:08:42)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */

public interface CoFontFileContainer extends Serializable, com.bluebrim.xml.shared.CoXmlEnabledIF {
	public String getFontFileBaseName();


	public void writeFontFileToDisk(File path) throws com.bluebrim.font.shared.CoFontException;
}