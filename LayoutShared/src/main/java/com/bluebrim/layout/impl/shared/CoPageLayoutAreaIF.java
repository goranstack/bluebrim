package com.bluebrim.layout.impl.shared;

import com.bluebrim.layout.shared.*;
import com.bluebrim.page.shared.*;

/**
 * RMI-enabling interface for class CoPageLayoutArea.
 * See class for method details.
 * 
 * @author: Dennis Malmström
 */

public interface CoPageLayoutAreaIF extends CoCompositePageItemIF, CoPageLayout
{

	public static String PAGE_LAYER_LAYOUT_AREA = "PAGE_LAYER_LAYOUT_AREA";
	
	// see CoPageItemIF.State
	public static class State extends CoCompositePageItemIF.State
	{
		// page item state needed by view, see CoPageLayerLayoutArea for details
		public String m_pageSizeName;
		public CoPageSizeIF m_pageSize;
		public boolean m_isSpread;
		public CoImmutableCustomGridIF m_customGrid;
		public boolean m_isLeftSideOfSpread;
		public boolean m_isRightSideOfSpread;
	};


	CoPage getPage();
	
	CoImmutableCustomGridIF getCustomGrid();
	
	CoCustomGridIF getMutableCustomGrid();
	
	CoPageSizeIF getPageSize();
	
	void setPageSize( CoPageSizeIF s );
	
	public boolean isSpread();
	
	public void setSpread( boolean s );
}