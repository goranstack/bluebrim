package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.transact.shared.*;

/**
 * 
 *
 * @author: Dennis Malmström
 */

public class CoShapePageItemAdjustBoundedContentCommand extends CoShapePageItemCommand
{





	private int m_adjustContentToFitKeepAspectRatioMask;
	private int m_adjustContentToFitMask;
	private int m_adjustToContentSizeMask;
	private int m_adjustToScaledContentSizeMask;

protected CoShapePageItemAdjustBoundedContentCommand( String name )
{
	super( name );
}

public CoUndoCommand doExecute()
{
	CoUndoCommand undo = new CoShapePageItemAdjustBoundedContentUndoCommand( getName(), m_targetView.getShapePageItem() );
	
	CoPageItemBoundedContentView bcv = (CoPageItemBoundedContentView) ( (CoContentWrapperPageItemView) m_targetView ).getContentView();

	
	if
		( m_adjustContentToFitMask != 0 )
	{
		bcv.getPageItemBoundedContent().adjustContentToFit( m_adjustContentToFitMask );
	}

	if
		( m_adjustContentToFitKeepAspectRatioMask != 0 )
	{
		bcv.getPageItemBoundedContent().adjustContentToFitKeepAspectRatio( m_adjustContentToFitKeepAspectRatioMask );
	}

	if
		( m_adjustToContentSizeMask != 0 )
	{
		bcv.getPageItemBoundedContent().adjustToContentSize( m_adjustToContentSizeMask );
	}

	if
		( m_adjustToScaledContentSizeMask != 0 )
	{
		bcv.getPageItemBoundedContent().adjustToScaledContentSize( m_adjustToScaledContentSizeMask );
	}
	
	return undo;
}

public void prepare( CoShapePageItemView targetView, int adjustContentToFitMask, int adjustContentToFitKeepAspectRatioMask, int adjustToContentSizeMask, int adjustToScaledContentSizeMask )
{
	super.prepare( targetView );

	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( adjustContentToFitMask * adjustContentToFitKeepAspectRatioMask == 0, "Illegal adjust mask combination" );
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( adjustToContentSizeMask * adjustToScaledContentSizeMask == 0, "Illegal adjust mask combination" );
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( adjustContentToFitMask + adjustContentToFitKeepAspectRatioMask + adjustToContentSizeMask + adjustToScaledContentSizeMask != 0, "Illegal adjust mask combination" );
	
	m_adjustContentToFitMask = adjustContentToFitMask;
	m_adjustContentToFitKeepAspectRatioMask = adjustContentToFitKeepAspectRatioMask;
	m_adjustToContentSizeMask = adjustToContentSizeMask;
	m_adjustToScaledContentSizeMask = adjustToScaledContentSizeMask;
}
}