package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.transact.shared.*;

/**
 * 
 *
 * @author: Dennis Malmström
 */

public class CoShapePageItemSetEmbeddedPathShapeCommand extends CoShapePageItemCommand
{





	protected String m_pathName;

protected CoShapePageItemSetEmbeddedPathShapeCommand( String name )
{
	super( name );
}

public CoUndoCommand doExecute()
{
	CoUndoCommand undo = new CoShapePageItemSetEmbeddedPathShapeUndoCommand( getName(), m_targetView.getShapePageItem() );

	getImageContentView().getPageItemImageContent().setEmbeddedPathShape( m_pathName );

	return undo;
}

protected CoPageItemImageContentView getImageContentView()
{
	return (CoPageItemImageContentView) ( (CoContentWrapperPageItemView) m_targetView ).getContentView();
}

public void prepare( CoShapePageItemView targetView, String pathName )
{
	prepare( targetView );
	
	m_pathName = pathName;
}


}