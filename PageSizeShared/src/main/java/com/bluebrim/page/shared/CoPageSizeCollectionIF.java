package com.bluebrim.page.shared;
import java.rmi.Remote;
import java.util.List;

import com.bluebrim.base.shared.CoObjectIF;

/**
 * Insert the type's description here.
 * Creation date: (2001-05-10 11:59:16)
 * @author: Dennis
 */
 
public interface CoPageSizeCollectionIF extends CoObjectIF, CoPageSizeRegistry, Remote
{
	public CoPageSizeIF addPageSize(CoPageSizeIF aPaperSize);

	public CoPageSizeIF createPageSize();
	
	public CoPageSizeCollectionIF copy();

	public CoPageSizeIF getPageSizeByName(String name);

	public List getPageSizes();

	public CoPageSizeIF removePageSize(CoPageSizeIF aPageSize);
	
	public void createDefaultPageSizes();
}
