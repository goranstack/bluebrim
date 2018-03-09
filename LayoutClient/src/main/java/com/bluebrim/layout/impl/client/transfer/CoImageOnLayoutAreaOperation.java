package com.bluebrim.layout.impl.client.transfer;

import java.awt.geom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;

public class CoImageOnLayoutAreaOperation extends CoImageOnSomethingOperation {


private boolean dropOnLayoutAreaWithoutWorkPiece( CoUndoableCommandExecutor executor, CoCompositePageItemIF target, Point2D position, final com.bluebrim.image.shared.CoImageContentIF image )
{
	if (position == null) position = new Point2D.Double(DEFAULT_X_POSITION, DEFAULT_Y_POSITION);

	CoPageItemFactoryIF factory = ((CoPageItemFactoryIF) CoFactoryManager.getFactory(CoPageItemFactoryIF.PAGE_ITEM_FACTORY));
	CoContentWrapperPageItemIF wrapper = factory.createImageBox();
	((CoPageItemImageContentIF) wrapper.getContent()).setImageContent(image);
	((CoPageItemImageContentIF) wrapper.getContent()).adjustToContentSize( CoPageItemImageContentIF.XY );
	
	CoPageItemCommands.REPARENT_PAGE_ITEM.prepare( "CREATE IMAGEBOX", wrapper, null, null, target, null, position);
	executor.doit( CoPageItemCommands.REPARENT_PAGE_ITEM, null );
	return true;
}


private boolean dropOnLayoutAreaWithWorkPiece( CoUndoableCommandExecutor executor, CoLayoutAreaIF target, Point2D position, final com.bluebrim.image.shared.CoImageContentIF image )
{
	if ( position == null ) position = new Point2D.Double( DEFAULT_X_POSITION, DEFAULT_Y_POSITION );

	CoPageItemFactoryIF 				factory 		= ((CoPageItemFactoryIF) CoFactoryManager.getFactory(CoPageItemFactoryIF.PAGE_ITEM_FACTORY));
	final CoContentWrapperPageItemIF wrapper = factory.createImageBox();


	final com.bluebrim.content.shared.CoWorkPieceIF wp 	= target.getWorkPiece();
	java.util.List images = wp.getImages();
	int i = images.indexOf( image );
	if
		( i == -1 )
	{
		com.bluebrim.transact.shared.CoCommand c = new com.bluebrim.transact.shared.CoCommand( "ADD IMAGE" )
		{
			public boolean doExecute()
			{
				wp.addContent( image );
				return true;
			}
		};
		executor.doit( c, null );
		
		i = images.indexOf( image );
	}

	( (CoPageItemImageContentIF) wrapper.getContent() ).setOrderTag( i );
	((CoPageItemImageContentIF) wrapper.getContent()).adjustToContentSize( CoPageItemImageContentIF.XY );

	CoPageItemCommands.REPARENT_PAGE_ITEM.prepare( "CREATE IMAGEBOX", wrapper, null, null, target, null, position );
	executor.doit( CoPageItemCommands.REPARENT_PAGE_ITEM, null );
	return true;
}


public boolean operateOn(CoUndoableCommandExecutor executor, Object object, Point2D position, Object operand)
{
	if (operatorMatch(object) == NO_MATCH) {
		return false;
	} else {
		CoCompositePageItemIF pi = ((CoCompositePageItemView) object).getCompositePageItem();

		if
			( ( pi instanceof CoLayoutAreaIF ) && ( ( (CoLayoutAreaIF) pi ).getWorkPiece() != null ) )
		{
			return dropOnLayoutAreaWithWorkPiece( executor,(CoLayoutAreaIF) pi, position, (com.bluebrim.image.shared.CoImageContentIF) operand );
		} else {
			return dropOnLayoutAreaWithoutWorkPiece( executor,pi, position, (com.bluebrim.image.shared.CoImageContentIF) operand );
		}
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