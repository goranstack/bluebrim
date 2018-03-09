package com.bluebrim.text.impl.shared;

import java.rmi.*;
import java.util.*;

import com.bluebrim.base.shared.*;

/**
 * Interface defining the protocol of a collection of available font family names.
 * 
 * @author: Dennis Malmström
 */
public interface CoFontFamilyCollectionIF extends CoObjectIF, Remote {
	void addFontFamily(String color);
	List getFontFamilyNames(); // [ String ]
	void removeAllFontFamilyNames();
	void removeFontFamilyNames(Object[] fonts);

	int getImmutableFontFamilyCount();
}