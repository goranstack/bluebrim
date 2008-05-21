package com.bluebrim.layout.impl.shared;



/**
 * Interface for a serializable and mutable fill style
 * 
 * @author: Dennis Malmström
 */

public interface CoFillStyleIF extends CoImmutableFillStyleIF
{
	public interface Owner
	{
		void notifyOwner();
	}
}
