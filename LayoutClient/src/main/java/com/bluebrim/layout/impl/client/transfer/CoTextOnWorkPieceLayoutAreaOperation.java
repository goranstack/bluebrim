package com.bluebrim.layout.impl.client.transfer;

import java.awt.geom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Insert the type's description here.
 * Creation date: (1999-09-14 14:21:33)
 * @author: Gorfo
 */
public class CoTextOnWorkPieceLayoutAreaOperation extends CoTextOnLayoutAreaOperation
{



public boolean operateOn (CoUndoableCommandExecutor executor, Object object, Point2D position, Object operand)
{
	if
		( operatorMatch(object) == NO_MATCH )
	{
		return false;
	} else {
		final com.bluebrim.text.shared.CoTextContentIF text = (com.bluebrim.text.shared.CoTextContentIF) operand;
		
		if ( position == null ) position = new Point2D.Double( DEFAULT_X_POSITION, DEFAULT_Y_POSITION );

		CoPageItemFactoryIF factory = ((CoPageItemFactoryIF) CoFactoryManager.getFactory(CoPageItemFactoryIF.PAGE_ITEM_FACTORY));
		CoContentWrapperPageItemIF wrapper = factory.createWorkPieceTextBox();
		CoLayoutAreaIF articleLayout = ( (CoLayoutAreaView) object ).getLayoutArea();
		
		final com.bluebrim.content.shared.CoWorkPieceIF wp = articleLayout.getWorkPiece();
		java.util.List texts = wp.getTexts();
		int i = texts.indexOf( text );
		if
			( i == -1 )
		{
			com.bluebrim.transact.shared.CoCommand c = new com.bluebrim.transact.shared.CoCommand( "ADD TEXT" )
			{
				public boolean doExecute()
				{
					wp.addContent( text );
					return true;
				}
			};
			executor.doit( c, null );
			i = texts.indexOf( text );
		}

		CoPageItemWorkPieceTextContentIF content = (CoPageItemWorkPieceTextContentIF) wrapper.getContent();
		content.setOrderTag( i );
		content.setAcceptedTags( text.getFormattedText( null ).getUsedParagraphTags() );

		
		CoPageItemCommands.REPARENT_PAGE_ITEM.prepare( "CREATE TEXTBOX", wrapper, null, null, articleLayout, null, position );
		executor.doit( CoPageItemCommands.REPARENT_PAGE_ITEM, null );
		return true;
	}
}


public double operatorMatch (Object object)
{
	if
		( object instanceof CoLayoutAreaView )
	{
		CoLayoutAreaView lav = (CoLayoutAreaView) object;
		if ( lav.areChildrenLocked() ) return NO_MATCH;
		if ( lav.getWorkPiece() == null ) return NO_MATCH;
		return PERFECT_MATCH;
	} else {
		return NO_MATCH;
	}
}

}