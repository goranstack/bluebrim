package com.bluebrim.layout.impl.shared;

import com.bluebrim.content.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * RMI-enabling interface for class CoPageItemLayoutContent.
 * See class for method details.
 * 
 * @author: Dennis Malmström
 */

public interface CoPageItemLayoutContentIF extends CoPageItemBoundedContentIF, CoOrderTaggedIF {
	// see CoPageItemIF.State
	public static class State extends CoPageItemBoundedContentIF.State {
		// page item state needed by view, see CoPageItemLayoutContent for details
		public CoLayoutContentIF m_layoutContent;
		public long m_timeStamp;
		public boolean m_isNull;

		public int m_layoutTag;
		public CoWorkPieceIF m_workPiece;
		public int m_recursiveLevelMaxCount;
	};

	public static String LAYOUT_CONTENT = "LAYOUT_CONTENT";

	public static class ViewState extends CoPageItemIF.ViewState {
		public CoLayoutContentIF m_layoutContent;
		public long m_timeStamp;
		public int m_recursiveLevelMaxCount;
	}

	CoLayoutContentIF getLayoutContent();

	int getRecursiveLevelMaxCount();

	public void setLayoutContent(CoLayoutContentIF layout);

	void setRecursiveLevelMaxCount(int i);
}