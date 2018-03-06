package com.bluebrim.layout.impl.client.transfer;

import java.awt.geom.*;

import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;

public class CoTextOnWorkPieceTextContentOperation extends CoTextOnTextContentOperation
{


public double operatorMatch(Object object)
{
	if
		(
			( object instanceof CoContentWrapperPageItemView )
		&&
			( ( (CoContentWrapperPageItemView) object ).getContentView() instanceof CoPageItemWorkPieceTextContentView )
		)
	{
		CoPageItemWorkPieceTextContentView tv = (CoPageItemWorkPieceTextContentView) ( (CoContentWrapperPageItemView) object ).getContentView();

		if ( tv.getTextLock() == CoPageItemWorkPieceTextContentIF.FIXED ) return NO_MATCH;
		if ( tv.getWorkPiece() == null ) return NO_MATCH;
		if ( tv.getTextLock() == CoPageItemWorkPieceTextContentIF.LOCKED ) return NO_MATCH;
		return PERFECT_MATCH;
	} else {
		return NO_MATCH;
	}
}



public boolean operateOn( CoUndoableCommandExecutor executor, final Object object, Point2D position, Object operand)
{
	if
		( operatorMatch(object) == NO_MATCH )
	{
		return false;
	} else {
		final com.bluebrim.text.shared.CoTextContentIF text = (com.bluebrim.text.shared.CoTextContentIF) operand;
		
		com.bluebrim.transact.shared.CoCommand c = new com.bluebrim.transact.shared.CoCommand( "SET TEXT TAG" )
		{
			public boolean doExecute()
			{
				CoPageItemWorkPieceTextContentIF articleText = (CoPageItemWorkPieceTextContentIF) ( (CoPageItemWorkPieceTextContentView) ( (CoContentWrapperPageItemView) object ).getContentView() ).getPageItem();

				com.bluebrim.content.shared.CoWorkPieceIF wp = articleText.getWorkPiece();
				java.util.List texts = wp.getTexts();
				int i = texts.indexOf( text );
				if
					( i == -1 )
				{
					wp.addContent( text );
					i = texts.indexOf( text );
				}
				
				articleText.setOrderTag( i );
				return true;
			}
		};

		executor.doit( c, null );
		return true;
	}

}
}