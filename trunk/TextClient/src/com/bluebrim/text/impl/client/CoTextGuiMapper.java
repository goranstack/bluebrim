package com.bluebrim.text.impl.client;

import java.awt.datatransfer.*;

import com.bluebrim.content.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.text.client.*;
import com.bluebrim.text.shared.*;

/**
 * This class register association between domain objects and 
 * user interface names for the text domain.
 *  
 * @author Göran Stäck 2002-11-08 
 *
 */
public class CoTextGuiMapper implements CoDomainGuiMappingSPI {

	public void collectMappings(Mapper mapper) {
		mapper.addKey(CoTextContentIF.FACTORY_KEY, "com.bluebrim.text.impl.client.CoTextContentUI");
		mapper.addKey(CoTextContentIF.FACTORY_KEY, new DataFlavor[] { CoTextClientConstants.TEXT_CONTENT_FLAVOR, CoAbstractContentClientConstants.CONTENT_FLAVOR });
	}

}