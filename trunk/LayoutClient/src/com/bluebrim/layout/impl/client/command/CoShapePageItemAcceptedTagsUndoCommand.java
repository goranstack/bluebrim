package com.bluebrim.layout.impl.client.command;

import java.util.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
 
public class CoShapePageItemAcceptedTagsUndoCommand extends CoShapePageItemUndoCommand
{
	protected List m_tags;
public CoShapePageItemAcceptedTagsUndoCommand( String name, CoShapePageItemIF target )
{
	super( name, target );
	
	m_tags = ( (CoPageItemWorkPieceTextContentIF) ( (CoContentWrapperPageItemIF) target ).getContent() ).getAcceptedTags();
}
public boolean doRedo()
{
	doUndo();
	return true;
}
public boolean doUndo()
{
	List tags = m_tags;

	CoPageItemWorkPieceTextContentIF wptc = (CoPageItemWorkPieceTextContentIF) ( (CoContentWrapperPageItemIF) m_target ).getContent();
	
	m_tags = wptc.getAcceptedTags();

	wptc.setAcceptedTags( tags );
	
	return true;
}
}
