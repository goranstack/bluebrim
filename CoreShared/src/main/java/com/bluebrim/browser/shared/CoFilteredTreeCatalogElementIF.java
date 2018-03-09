package com.bluebrim.browser.shared;

import java.util.*;


/**
 * This is an extension of <code>CoTreeCatalogElementIF</code> to be used by those classes
 * that will be displayed in a tree view and whose childs are may be filtered before shown.
 * Creation date: (1999-09-08 12:36:36)
 * @author:Lasse 
 */
public interface CoFilteredTreeCatalogElementIF extends CoTreeCatalogElementIF {
public List getElements (CoTreeCatalogFilterIF filter );
}
