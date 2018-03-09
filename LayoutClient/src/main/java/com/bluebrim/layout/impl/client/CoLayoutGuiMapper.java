package com.bluebrim.layout.impl.client;

import java.awt.datatransfer.*;

import com.bluebrim.content.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.client.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * This class register association between domain objects and 
 * user interface names for the layout domain.
 *  
 * @author Göran Stäck 2002-11-08 
 *
 */
public class CoLayoutGuiMapper implements CoDomainGuiMappingSPI {

	public void collectMappings(Mapper mapper) {
		mapper.addKey(CoLayoutContentIF.FACTORY_KEY, "com.bluebrim.layout.impl.client.CoLayoutContentUI");

		mapper.addKey(CoLayoutContentIF.FACTORY_KEY, new DataFlavor[] { CoLayoutClientConstants.LAYOUT_CONTENT_FLAVOR, CoAbstractContentClientConstants.CONTENT_FLAVOR });
		mapper.addKey(CoPageLayoutAreaIF.PAGE_LAYER_LAYOUT_AREA, new DataFlavor[] { CoPageItemUIConstants.PAGE_LAYER_LAYOUT_AREA_FLAVOR });
	}

}

/* Code fragments from CoAutomaticUIKit
	private final static int LAYOUT		= 25;
	case LAYOUT :
			return new DataFlavor[] { CoContentUIConstants.LAYOUT_CONTENT_FLAVOR, CoContentUIConstants.CONTENT_FLAVOR };
		// Page items
	private final static int PAGE_LAYER_LAYOUT_AREA	= 89;
	// Page items
		case PAGE_LAYER_LAYOUT_AREA :
			return new DataFlavor[] { CoPageItemUIConstants.PAGE_LAYER_LAYOUT_AREA_FLAVOR };

			case LAYOUT:
				return new CoLayoutContentUI(obj, context);
//				return new CoLayoutViewer(obj);

	add(CoLayoutContentIF.FACTORY_KEY, LAYOUT);
	add(CoInsertionRequestIF.FACTORY_KEY_PREFIX + CoLayoutContentIF.FACTORY_KEY, LAYOUT_INSERTION_REQUEST);
	add(CoInsertionRequestIF.FACTORY_KEY_PREFIX + CoImageContentIF.FACTORY_KEY, IMAGE_INSERTION_REQUEST);
	add(CoInsertionRequestIF.FACTORY_KEY_PREFIX + CoTextContentIF.FACTORY_KEY, TEXT_INSERTION_REQUEST);
	add(CoInsertionRequestIF.FACTORY_KEY_PREFIX + CoWorkPieceIF.FACTORY_KEY, WORKPIECE_INSERTION_REQUEST);

	// Page items
	add( CoPageLayoutAreaIF.PAGE_LAYER_LAYOUT_AREA, PAGE_LAYER_LAYOUT_AREA );
		// Insertion requests
		case IMAGE_INSERTION_REQUEST :
			return new DataFlavor[] { CoLayoutImplClientConstants.IMAGE_INSERTION_REQUEST_FLAVOR };
		case LAYOUT_INSERTION_REQUEST :
			return new DataFlavor[] { CoLayoutImplClientConstants.LAYOUT_INSERTION_REQUEST_FLAVOR };
		case TEXT_INSERTION_REQUEST :
			return new DataFlavor[] { CoLayoutImplClientConstants.TEXT_INSERTION_REQUEST_FLAVOR };
		case WORKPIECE_INSERTION_REQUEST :
			return new DataFlavor[] { CoLayoutImplClientConstants.WORKPIECE_INSERTION_REQUEST_FLAVOR };

import com.bluebrim.layout.impl.client.CoLayoutContentUI;
import com.bluebrim.layout.impl.client.CoLayoutImplClientConstants;
import com.bluebrim.layout.impl.client.CoPageItemUIConstants;
import com.bluebrim.layout.impl.shared.CoPageLayoutAreaIF;
import com.bluebrim.layout.shared.CoLayoutContentIF;

	// Insertion requests
	private final static int IMAGE_INSERTION_REQUEST		= 29;
	private final static int LAYOUT_INSERTION_REQUEST		= 30;
	private final static int TEXT_INSERTION_REQUEST			= 31;
	private final static int WORKPIECE_INSERTION_REQUEST	= 32;

*/			
