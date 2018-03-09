package com.bluebrim.layout.impl.client.transfer;

//import se.corren.calvin.editorial.server.*;
import java.awt.geom.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;

//

public class CoWorkPieceOnLayoutAreaOperation extends CoWorkPieceOnSomethingOperation
{
private boolean dropOnCompositePageItem( CoUndoableCommandExecutor executor, CoCompositePageItemView targetView, Point2D position, final com.bluebrim.content.shared.CoWorkPieceIF wp )
{
	if ( position == null ) position =  new Point2D.Double( DEFAULT_X_POSITION, DEFAULT_Y_POSITION );

	CoPageItemFactoryIF f = (CoPageItemFactoryIF) CoFactoryManager.getFactory( CoPageItemFactoryIF.PAGE_ITEM_FACTORY );
	CoPageItemPreferencesIF p = targetView.getCompositePageItem().getPreferences();
	
	final CoLayoutAreaIF area = ( p == null ) ? f.createLayoutArea() : (CoLayoutAreaIF) p.getLayoutAreaPrototype().deepClone();

	double d = 10;
	double w = 200;
	double h = 100;
	double x = w + d + d;
	double y = d;

	Collection imageContents = new ArrayList();
	Iterator images = wp.getImages().iterator();
	int tag = 0;
	while
		( images.hasNext() )
	{
		com.bluebrim.image.shared.CoImageContentIF i = (com.bluebrim.image.shared.CoImageContentIF) images.next();

		CoContentWrapperPageItemIF cw = ( p == null ) ? f.createImageBox() : (CoContentWrapperPageItemIF) p.getImageboxPrototype().deepClone();
		cw.getMutableCoShape().setSize( w, h );
		cw.setPosition( x, y );
		y += h + d;
	
		CoPageItemImageContentIF at = (CoPageItemImageContentIF) cw.getContent();
		at.setOrderTag( tag++ );
		imageContents.add( at );
		area.add( cw );
	}

	double H = y;
	y = d;
	x = d;

	int n = 0;
	Iterator texts = wp.getTexts().iterator();
	while
		( texts.hasNext() )
	{
		com.bluebrim.text.shared.CoTextContentIF t = (com.bluebrim.text.shared.CoTextContentIF) texts.next();

		CoContentWrapperPageItemIF cw = ( p == null ) ? f.createWorkPieceTextBox() : (CoContentWrapperPageItemIF) p.getTextboxPrototype().deepClone();
		cw.getMutableCoShape().setSize( w, h );
		cw.setPosition( x, y );
		y += h + d;
	
		CoPageItemWorkPieceTextContentIF at = (CoPageItemWorkPieceTextContentIF) cw.getContent();
		at.setAcceptedTags( t.getFormattedText( null ).getUsedParagraphTags() );
		at.setOrderTag( n++ );

		area.add( cw );
	}

	H = Math.max( H, y );


	double W = 0;
	Iterator i = imageContents.iterator();
	while
		( i.hasNext() )
	{
		CoPageItemImageContentIF at = (CoPageItemImageContentIF) i.next();
		at.adjustContentToFitKeepAspectRatio( at.XY );
		at.adjustToScaledContentSize( at.XY );
		W = Math.max( W, at.getOwner().getCoShape().getWidth() );
	}
	
	area.getMutableCoShape().setSize( w + 2 * d + W + d, H );
	area.setWorkPiece( wp );

	CoPageItemCommands.REPARENT_PAGE_ITEM.prepare( "CREATE LAYOUT AREA", area, null, null, targetView.getCompositePageItem(), null,  position );
	executor.doit( CoPageItemCommands.REPARENT_PAGE_ITEM, null );
	
	return true;
}


private boolean dropOnWorkPieceAcceptingLayoutArea( CoUndoableCommandExecutor executor, CoLayoutAreaView target, Point2D position, com.bluebrim.content.shared.CoWorkPieceIF wp )
{
	CoPageItemCommands.SET_WORKPIECE.prepare( target, wp );
	executor.doit( CoPageItemCommands.SET_WORKPIECE, null );
		
	return true;
}


private boolean dropOnWorkPieceNonAcceptingLayoutArea( CoUndoableCommandExecutor executor, CoLayoutAreaView targetView, Point2D position, com.bluebrim.content.shared.CoWorkPieceIF wp )
{
	return dropOnCompositePageItem( executor, targetView, position, wp );
}


public boolean operateOn( CoUndoableCommandExecutor executor, Object object, Point2D position, Object operand )
{
	if
		( operatorMatch ( object ) == NO_MATCH)
	{
		return false;
	}
	

	if
		( object instanceof CoLayoutAreaView )
	{
		CoLayoutAreaView targetView = (CoLayoutAreaView) object;

		if
			( targetView.getLayoutArea().getAcceptsWorkPiece() )
		{
			return dropOnWorkPieceAcceptingLayoutArea( executor,targetView, position, (com.bluebrim.content.shared.CoWorkPieceIF) operand );
		} else {
			return dropOnWorkPieceNonAcceptingLayoutArea( executor,targetView, position, (com.bluebrim.content.shared.CoWorkPieceIF) operand );
		}
		
	} else {
		return dropOnCompositePageItem( executor,(CoCompositePageItemView) object, position, (com.bluebrim.content.shared.CoWorkPieceIF) operand );
		
	}
}


public double operatorMatch (Object object)
{
	if
		( object instanceof CoLayoutAreaView )
	{
		CoLayoutAreaView v = (CoLayoutAreaView) object;
		if ( v.getWorkPieceLock() == CoLayoutAreaIF.FIXED ) return NO_MATCH;
		
		if ( ( ! v.acceptsWorkPiece() ) && v.areChildrenLocked() ) return NO_MATCH;
		
		if
			( v.acceptsWorkPiece() && ( ! v.hasWorkPiece() || ( v.getWorkPieceLock() == CoLayoutAreaIF.UNLOCKED ) ) )
		{
			return PERFECT_MATCH;
		} else {
			return NO_MATCH;
		}
	} else if
		( object instanceof CoCompositePageItemView )
	{
		if ( ( (CoCompositePageItemView) object ).areChildrenLocked() ) return NO_MATCH;
		return OK_MATCH;
	} else {
		return NO_MATCH;
	}
}
}