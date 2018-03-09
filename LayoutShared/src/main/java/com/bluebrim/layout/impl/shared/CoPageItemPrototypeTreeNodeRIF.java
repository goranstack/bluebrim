package com.bluebrim.layout.impl.shared;

import java.rmi.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.browser.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * RMI-enbling interface for CoPageItemPrototypeTreeNode.
 * 
 * @author: Dennis Malmström
 */
 
public interface CoPageItemPrototypeTreeNodeRIF extends CoTreeCatalogElementIF, CoObjectIF ,CoRenameable, Remote, CoXmlExportEnabledIF, CoLayoutTemplateFolder
{
	String FACTORY_KEY = "CoPageItemPrototypeTreeNodeIF";





String getName();
CoPageItemPrototypeCollectionIF getPrototypes(); // immutable
boolean isDeleteable();
boolean isEditable();
boolean isRenameable();
public CoPageItemPrototypeIF lookupPageItemPrototype(String name);
public void setDeleteable(boolean b);
void setName( String name );
public void setRenameable(boolean b);

public List getChildrenNodes();

CoPageItemPrototypeTreeNodeRIF deepClone();

public CoPageItemPrototypeTreeNodeRIF getChildNode(String name);
}