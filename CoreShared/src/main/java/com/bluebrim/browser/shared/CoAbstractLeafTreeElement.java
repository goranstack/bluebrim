package com.bluebrim.browser.shared;

import com.bluebrim.base.shared.*;
/**
 * Abstract class that can be subclassed by classes implementing CoTreeCatalogElementIF but which are
 * leafs in the tree hierarchy, i e have no children.
 * Creation date: (1999-09-08 12:54:08)
 * @author: 
 */
public abstract class CoAbstractLeafTreeElement extends CoSimpleObject implements CoTreeCatalogElementIF {
/**
 * getElements method comment.
 */
public java.util.List getElements() {
	return null;
}
/**
 * getTreeCatalogElement method comment.
 */
public CoTreeCatalogElementIF getTreeCatalogElement() {
	return this;
}
}
