package com.bluebrim.layout.impl.shared.layoutmanager;

import java.rmi.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Creation date: (2000-06-05)
 * @author: Dennis
 */

public interface CoImmutableLayoutManagerIF
		extends
			Cloneable,
			CoFactoryElementIF,
			Remote,
			CoXmlExportEnabledIF {
	public static String LAYOUT_MANAGER = "layout_manager";
	public CoLayoutManagerIF deepClone();
	boolean doesSetSize();
	String getLocalizedName();
	String getPanelClassName();
}