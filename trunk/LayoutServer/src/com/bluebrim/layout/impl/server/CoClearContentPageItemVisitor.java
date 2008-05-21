package com.bluebrim.layout.impl.server;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * A page item visitor that clears all references to workpieces.
 * 
 * @author: Dennis
 */

public class CoClearContentPageItemVisitor extends CoPageItemVisitor
{

public CoClearContentPageItemVisitor()
{
	super();
}








public void doToLayoutArea(CoLayoutArea layoutArea, Object anything, boolean goDown)
{
	layoutArea.setWorkPiece( null );

	super.doToLayoutArea(layoutArea, anything, goDown );
}
}