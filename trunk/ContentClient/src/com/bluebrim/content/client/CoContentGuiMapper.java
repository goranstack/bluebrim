package com.bluebrim.content.client;

import java.awt.datatransfer.*;

import com.bluebrim.content.shared.*;
import com.bluebrim.gui.client.*;

/**
 * This class register association between domain objects and 
 * user interface names for the content domain.
 *  
 * @author Göran Stäck 2002-08-16 
 *
 */
public class CoContentGuiMapper implements CoDomainGuiMappingSPI {

	public void collectMappings(Mapper mapper) {
		mapper.addKey(CoContentIF.FACTORY_KEY, new DataFlavor[] { CoAbstractContentClientConstants.CONTENT_FLAVOR });
	}

}

/* Code fragments from CoAutomaticUIKit
	add(CoContentIF.FACTORY_KEY, CONTENT);
	add(CoWorkPieceIF.FACTORY_KEY, WORKPIECE);


	private final static int CONTENT	= 23;
	private final static int WORKPIECE	= 27;


		case CONTENT :
			return new DataFlavor[] { CoContentUIConstants.CONTENT_FLAVOR };
		case WORKPIECE :
			return new DataFlavor[] { CoContentUIConstants.WORK_PIECE_FLAVOR, CoContentUIConstants.CONTENT_FLAVOR };

			
					case WORKPIECE:
				return new CoWorkPieceUI(obj, context);

*/