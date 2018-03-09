package com.bluebrim.layout.impl.client.transfer;

import java.awt.geom.*;

import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;

public class CoLayoutOnLayoutContentOperation extends CoLayoutOnSomethingOperation {


public double operatorMatch(Object object) {
	if
		(
			( object instanceof CoContentWrapperPageItemView )
		&&
			( ( ( CoContentWrapperPageItemView ) object ).getContentView() instanceof CoPageItemLayoutContentView )
		)
	{
		CoPageItemLayoutContentView iv = (CoPageItemLayoutContentView) ( ( CoContentWrapperPageItemView ) object ).getContentView();
		if ( iv.getContentLock() == CoPageItemLayoutContentIF.FIXED ) return NO_MATCH;
		
		if
			( ! iv.hasContent() || ( iv.getContentLock() == CoPageItemLayoutContentIF.UNLOCKED ) )
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

	CoPageItemCommands.SET_LAYOUT.prepare( (CoShapePageItemView) object, (com.bluebrim.layout.shared.CoLayoutContentIF) operand );
	executor.doit( CoPageItemCommands.SET_LAYOUT, null );

	return true;
}
}