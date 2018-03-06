package com.bluebrim.content.shared;

import com.bluebrim.base.shared.*;
import com.bluebrim.browser.shared.*;
import com.bluebrim.system.shared.*;
import com.bluebrim.xml.shared.*;

public interface CoContentIF extends CoObjectIF, CoXmlExportEnabledIF, CoRenameable, CoCatalogElementIF, CoDistinguishable, Cloneable {
	String FACTORY_KEY = "content";

	public CoContentIF copy();

	String getContentDescription();

}