package com.bluebrim.layout.impl.shared;

import java.io.*;
import java.util.*;

import com.bluebrim.layout.shared.*;

/**
 * RMI-enbling interface for CoPageItemEditorContext.
 * 
 * @author: Dennis Malmstr�m
 */

public interface CoPageItemEditorContextIF extends Serializable, CoLayoutEditorContext
{
	public CoPageItemPreferencesIF getPreferences();

	public CoPageItemPrototypeTreeRootIF getPrototypes(); // [ CoPageItemPrototypeTreeRootIF ]
	
	public List getContentRecievers(); // [ CoContentReciever ]
	
	public CoPageItemPrototypeCollectionIF getTools();
}