package com.bluebrim.layout.impl.shared;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * RMI-enabling interface for class CoContentWrapperPageItem.
 * See class for method details.
 * 
 * @author: Dennis Malmström
 */
 
public interface CoContentWrapperPageItemIF extends CoShapePageItemIF
{
	// see CoPageItemIF.State
	public static class State extends CoShapePageItemIF.State
	{
		// page item state needed by view, see CoContentWrapperPageItem for details
		public CoImmutableShapeIF m_clipping;

		public CoRef m_contentId;
		public CoPageItemContentIF.State m_contentState;
	};
	
	public static class ViewState extends CoPageItemIF.ViewState
	{
		public CoPageItemIF.ViewState m_contentViewState;
	}

	public CoPageItemContentIF getContent();
	
	public void setContent(CoPageItemContentIF content);
	
	public CoPageItemContentView createContentView_shallBeCalledBy_CoContentWrapperPageItemView_only();
}