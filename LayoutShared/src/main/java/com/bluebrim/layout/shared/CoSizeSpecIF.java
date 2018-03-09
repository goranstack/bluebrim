package com.bluebrim.layout.shared;





//

public interface CoSizeSpecIF extends CoImmutableSizeSpecIF
{
	public interface Owner
	{
		void notifyOwner();
	}
void configure( CoShapePageItemIF pi );
}