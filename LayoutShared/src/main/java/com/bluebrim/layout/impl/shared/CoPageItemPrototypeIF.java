package com.bluebrim.layout.impl.shared;

import java.rmi.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.browser.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.transact.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * RMI-enabling interface for CoPageItemPrototype
 *
 * @author: Dennis Malmström
 */

public interface CoPageItemPrototypeIF extends CoLayoutTemplate, CoPageItemHolderIF, CoTreeCatalogElementIF, CoRenameable, Remote, CoXmlExportEnabledIF
{
	public final static String FACTORY_KEY = "page_item_prototype";
	public final static String ICON_NAME = "CoPageItemPrototypeIF.gif";

	// property names
	public final static String NAME = "CoPageItemPrototypeIF.NAME";
	public final static String DESCRIPTION = "CoPageItemPrototypeIF.DESCRIPTION";
	
	CoPageItemPrototypeIF deepClone();
	public String getDescription();
	public String getName();
	
	boolean isDeleteable();
	boolean isRenameable();
	public void setDeleteable(boolean b);
	public void setDescription(String description);
	public void setName(String name);
	
	CoShapePageItemIF clonePageItem();
	
	CoShapePageItemIF getPageItem();
	
	CoRef getPageItemId();
}