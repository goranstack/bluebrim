package com.bluebrim.compositecontent.client;

import java.awt.datatransfer.*;

import com.bluebrim.base.client.datatransfer.*;
import com.bluebrim.content.client.*;
import com.bluebrim.content.shared.*;
import com.bluebrim.image.client.*;
import com.bluebrim.layout.client.*;
import com.bluebrim.text.client.*;

/**
 * A concrete subclass that implements the initFlavorSet() method
 * Creation date: (2000-11-24 08:24:14)
 *
 * @author: Peter Jakubicki
 */
public class CoContentDataFlavorSetProvider extends CoDataFlavorSetProvider{
public CoContentDataFlavorSetProvider() {
	super();
}
public CoContentDataFlavorSetProvider(DataFlavor[] flavors) {
	super(flavors);
}
protected void initDragFlavorSet() {
	m_dragFlavorSets.put(com.bluebrim.text.shared.CoTextContentIF.FACTORY_KEY, new DataFlavor[] { CoTextClientConstants.TEXT_CONTENT_FLAVOR, CoAbstractContentClientConstants.CONTENT_FLAVOR });
	m_dragFlavorSets.put(com.bluebrim.image.shared.CoImageContentIF.FACTORY_KEY, new DataFlavor[] { CoImageClientConstants.IMAGE_CONTENT_FLAVOR, CoAbstractContentClientConstants.CONTENT_FLAVOR });
	m_dragFlavorSets.put(com.bluebrim.layout.shared.CoLayoutContentIF.FACTORY_KEY, new DataFlavor[] { CoLayoutClientConstants.LAYOUT_CONTENT_FLAVOR, CoAbstractContentClientConstants.CONTENT_FLAVOR });
	m_dragFlavorSets.put(CoContentIF.FACTORY_KEY, new DataFlavor[] { CoAbstractContentClientConstants.CONTENT_FLAVOR });
	m_dragFlavorSets.put(com.bluebrim.content.shared.CoWorkPieceIF.FACTORY_KEY, new DataFlavor[] { CoCompositeContentClientConstants.WORK_PIECE_FLAVOR, CoAbstractContentClientConstants.CONTENT_FLAVOR });
}
}
