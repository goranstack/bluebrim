package com.bluebrim.layout.client;

import java.awt.datatransfer.*;

import com.bluebrim.base.client.datatransfer.*;
import com.bluebrim.layout.shared.*;

/**
 * @author Göran Stäck 2002-11-06
 */
public interface CoLayoutClientConstants {
	String LAYOUT_PARAMETERS = "layout_parameters";
	String LAYOUT_PROTOTYPES = "layout_prototypes";
	DataFlavor LAYOUT_CONTENT_FLAVOR	= CoDataTransferKit.domainFlavor(CoLayoutContentIF.class, CoLayoutContentIF.FACTORY_KEY); 
}
