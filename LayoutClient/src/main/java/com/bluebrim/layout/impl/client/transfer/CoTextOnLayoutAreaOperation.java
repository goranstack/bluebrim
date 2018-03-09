package com.bluebrim.layout.impl.client.transfer;

import java.awt.geom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;

public class CoTextOnLayoutAreaOperation extends CoTextOnSomethingOperation {


public boolean operateOn (CoUndoableCommandExecutor executor, Object object, Point2D position, final Object operand)
{
	if ( operatorMatch (object) == NO_MATCH)
	{
		return false;
	}
	
	else
	{
		if (position == null)
			position =  new Point2D.Double (DEFAULT_X_POSITION, DEFAULT_Y_POSITION);
		
		CoPageItemIF pi = (( CoShapePageItemView )object) .getPageItem ();
		CoPageItemFactoryIF factory = ( (CoPageItemFactoryIF) CoFactoryManager.getFactory( CoPageItemFactoryIF.PAGE_ITEM_FACTORY ) );
		
		CoContentWrapperPageItemIF wrapper = factory.createTextBox ();
		(  ( CoPageItemTextContentIF) (( CoContentWrapperPageItemIF ) wrapper).getContent ()).setFormattedTextHolder( (com.bluebrim.text.shared.CoTextContentIF) operand );
		
		CoShapeIF shape = wrapper.getMutableCoShape();
		shape.setHeight ( DEFAULT_TEXTBOX_HEIGHT );
		shape.setWidth ( DEFAULT_TEXTBOX_WIDTH );

		CoPageItemCommands.REPARENT_PAGE_ITEM.prepare( "CREATE TEXTBOX", wrapper, null, null, (CoCompositePageItemIF) pi, null, position );
		executor.doit( CoPageItemCommands.REPARENT_PAGE_ITEM, null );
		return true;
	}

}


public double operatorMatch (Object object)
{
	if
		( object instanceof CoCompositePageItemView )
	{
		CoCompositePageItemView v = (CoCompositePageItemView) object;
		if ( v.areChildrenLocked() ) return NO_MATCH;
		
		return OK_MATCH;
	} else {
		return NO_MATCH;
	}
}
}