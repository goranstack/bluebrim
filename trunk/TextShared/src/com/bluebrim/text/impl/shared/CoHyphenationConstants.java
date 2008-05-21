package com.bluebrim.text.impl.shared;

import com.bluebrim.font.shared.*;

/**
 * Hyphenation constants
 * 
 * @author: Dennis Malmström
 */

public interface CoHyphenationConstants
{
	public static final char HYPHENATION_POINT_CHARACTER = CoUnicode.HYPHENATION_POINT;
	public static final String HYPHENATION_POINT_STRING = "" + HYPHENATION_POINT_CHARACTER;

	public static final char ANTI_HYPHENATION_POINT_CHARACTER = '\uFFFF';
	public static final String ANTI_HYPHENATION_POINT_STRING = "" + ANTI_HYPHENATION_POINT_CHARACTER;
}
