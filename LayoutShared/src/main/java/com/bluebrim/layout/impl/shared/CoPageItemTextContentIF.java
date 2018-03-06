package com.bluebrim.layout.impl.shared;

////import se.corren.calvin.editorial.interfaces.*;

import com.bluebrim.text.shared.*;

/**
 * RMI-enabling interface for class CoPageItemTextContent.
 * See class for method details.
 * 
 * @author: Dennis Malmström
 */

public interface CoPageItemTextContentIF extends CoPageItemAbstractTextContentIF {

	public static final String TEXT_CONTENT = "TEXT_CONTENT";

	// see CoPageItemIF.State
	public static class State extends CoPageItemAbstractTextContentIF.State {
		// page item state needed by view, see CoPageItemTExtContent for details
		public boolean m_isProjecting;
	}

	public CoFormattedTextHolderIF getFormattedTextHolder();

	public void setFormattedTextHolder(CoFormattedTextHolderIF text);
}