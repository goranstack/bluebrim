package com.bluebrim.layout.impl.shared;

import com.bluebrim.layout.shared.*;

/**
 * RMI-enabling interface for class CoDesktopLayoutArea.
 * See class for method details.
 * 
 * @author: Dennis Malmström
 */

 public interface CoDesktopLayoutAreaIF extends CoCompositePageItemIF, CoDesktopLayout
{

	// see CoPageItemIF.State
	public static class State extends CoCompositePageItemIF.State
	{
		// page item state needed by view, see CoDesktopLayoutArea for details
		public CoImmutableCustomGridIF m_customGrid;
	}

	CoImmutableCustomGridIF getCustomGrid();

	CoCustomGridIF getMutableCustomGrid();
}