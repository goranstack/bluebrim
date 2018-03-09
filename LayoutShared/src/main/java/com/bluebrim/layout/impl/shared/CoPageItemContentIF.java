package com.bluebrim.layout.impl.shared;




/**
 * RMI-enabling interface for class CoPageItemContent.
 * See class for method details.
 * 
 * @author: Dennis Malmström
 */

public interface CoPageItemContentIF extends CoPageItemIF
{
	// see CoPageItemIF.State
	public static class State extends CoPageItemIF.State
	{
		// page item state needed by view, see CoPageItemContent for details
		public String m_type;
	};
public CoContentWrapperPageItemIF getOwner();

}