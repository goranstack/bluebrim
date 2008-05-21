package com.bluebrim.layout.shared;

import com.bluebrim.xml.shared.*;

/**
 * This is an attempt to create an interface that can replace 
 * <code>CoPageItemPrototypeTreeNodeRIF</code> when refered from outside the layout domain. The
 * reason for doing this is to minimize what is exposed in the core part of the layout domain. 
 * <br>
 * The working strategi is to replace all external references to code>CoPageItemPrototypeTreeNodeRIF</code>
 * with this interface and add requested methods until everyone is happy.  
 * <br>
 * 
 * @author Göran Stäck 2002-04-22
 */

public interface CoLayoutTemplateFolder extends CoXmlExportEnabledIF {
	public void setDeleteable(boolean state);

	public void setRenameable(boolean state);

	public CoLayoutTemplate getLayoutTemplateByName(String name);
	
	/**
	 * PENDING: This method has to do with the present implementation of the interface and
	 * should be replaced with an alternate design. <br>
	 * If you create and add a subfolder that also should be treated as a root folder you
	 * must call this method and store the result as the root folder.
	 */
	public CoLayoutTemplateRootFolder makeToRootFolderAsWell(CoLayoutTemplateRootFolder ownerRoot);


}
