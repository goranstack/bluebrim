package com.bluebrim.image.impl.client;

import java.awt.datatransfer.*;

import com.bluebrim.content.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.image.client.*;
import com.bluebrim.image.shared.*;

/**
 * This class register association between domain objects and 
 * user interface names for the image domain.
 *  
 * @author Göran Stäck 2002-11-08 
 *
 */
public class CoImageGuiMapper implements CoDomainGuiMappingSPI {

	public void collectMappings(Mapper mapper) {
		mapper.addKey(CoImageContentIF.FACTORY_KEY, "com.bluebrim.image.impl.client.CoImageContentUI");
		mapper.addKey(CoImageContentIF.FACTORY_KEY, new DataFlavor[] { CoImageClientConstants.IMAGE_CONTENT_FLAVOR, CoAbstractContentClientConstants.CONTENT_FLAVOR });
	}

}



