package com.bluebrim.page.shared;

import com.bluebrim.system.shared.CoGOI;

/**
 * Implemented by objects that can deliver page sizes by key.
 * 
 * @author Göran Stäck 2002-09-15
 *
 */
public interface CoPageSizeRegistry {
	public CoPageSizeIF lookupPageSize(CoGOI goi);
}
