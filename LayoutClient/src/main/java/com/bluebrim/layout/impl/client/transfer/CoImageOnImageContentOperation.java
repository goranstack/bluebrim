package com.bluebrim.layout.impl.client.transfer;

import java.awt.geom.*;

import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;

public class CoImageOnImageContentOperation extends CoImageOnSomethingOperation
{


public double operatorMatch( Object object )
{
	if
		(
			( object instanceof CoContentWrapperPageItemView )
		&&
			( ( ( CoContentWrapperPageItemView ) object ).getContentView() instanceof CoPageItemImageContentView )
		)
	{
		CoPageItemImageContentView iv = (CoPageItemImageContentView) ( ( CoContentWrapperPageItemView ) object ).getContentView();
		if ( iv.getContentLock() == CoPageItemImageContentIF.FIXED ) return NO_MATCH;
		
		if
			( ! iv.hasContent() || ( iv.getContentLock() == CoPageItemImageContentIF.UNLOCKED ) )
		{
			return PERFECT_MATCH;
		} else {
			return NO_MATCH;
		}
	} else {
		return NO_MATCH;
	}
}





public boolean operateOn(CoUndoableCommandExecutor executor, Object object, Point2D position, Object operand)
{
	if
		( operatorMatch(object) == NO_MATCH )
	{
		return false;
	}

	CoPageItemCommands.SET_IMAGE.prepare( (CoShapePageItemView) object, (com.bluebrim.image.shared.CoImageContentIF) operand );
	executor.doit( CoPageItemCommands.SET_IMAGE, null );

	return true;

}
}