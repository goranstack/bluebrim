package com.bluebrim.layout.impl.client.transfer;

import java.awt.geom.*;

import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;

public class CoTextOnTextContentOperation extends CoTextOnSomethingOperation {


public double operatorMatch (Object object)
{
	if
		(
			( object instanceof CoContentWrapperPageItemView )
		&&
			( ( (CoContentWrapperPageItemView) object ).getContentView() instanceof CoPageItemTextContentView )
		)
	{
		CoPageItemTextContentView tv = (CoPageItemTextContentView) ( (CoContentWrapperPageItemView) object ).getContentView();
		if ( tv.getTextLock() == CoPageItemTextContentIF.FIXED ) return NO_MATCH;
		if ( ( tv.getDocument() != null ) && ( tv.getTextLock() == CoPageItemTextContentIF.LOCKED ) ) return NO_MATCH;
		
		return OK_MATCH;
	} else {
		return NO_MATCH;
	}
}



public boolean operateOn (CoUndoableCommandExecutor executor, Object object, Point2D position, final Object operand)
{
	if ( operatorMatch (object) == NO_MATCH)
	{
		return false;
	}
	
	else
	{

		CoPageItemCommands.SET_TEXT.prepare( (CoShapePageItemView) object, (com.bluebrim.text.shared.CoTextContentIF) operand );
		executor.doit( CoPageItemCommands.SET_TEXT, null );
			
		return true;
	}			

}
}