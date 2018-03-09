package com.bluebrim.layout.impl.shared;

import com.bluebrim.layout.shared.*;



public interface CoLocationSpecIF extends CoImmutableLocationSpecIF
{
	public interface Owner
	{
		void notifyOwner();
	}
void configure( CoShapePageItemIF pi );
}
