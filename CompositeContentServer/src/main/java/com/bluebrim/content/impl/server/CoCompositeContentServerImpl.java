package com.bluebrim.content.impl.server;

import com.bluebrim.content.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * @author Göran Stäck 2002-04-25
 */
public class CoCompositeContentServerImpl implements CoCompositeContentServer
{	
	public CoWorkPieceIF createWorkPiece( CoLayoutParameters layoutParameters) 
	{
		return new CoWorkPiece( layoutParameters);
	}

}
