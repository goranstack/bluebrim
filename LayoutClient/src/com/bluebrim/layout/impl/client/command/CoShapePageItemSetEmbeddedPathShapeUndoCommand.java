package com.bluebrim.layout.impl.client.command;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
 
public class CoShapePageItemSetEmbeddedPathShapeUndoCommand extends CoShapePageItemUndoCommand
{
	protected CoImmutableShapeIF m_shape;
	protected double m_imageX;
	protected double m_imageY;
public CoShapePageItemSetEmbeddedPathShapeUndoCommand( String name, CoShapePageItemIF target )
{
	super( name, target );

	m_shape = target.getTranslatedCoShape();

	m_imageX = getImageContent().getX();
	m_imageY = getImageContent().getY();
}
public boolean doRedo()
{
	return doUndo();
}
public boolean doUndo()
{
	CoImmutableShapeIF tmp = m_shape;
	double tmpX = m_imageX;
	double tmpY = m_imageY;
	
	m_shape = m_target.getTranslatedCoShape();
	m_imageX = getImageContent().getX();
	m_imageY = getImageContent().getY();

	m_target.setPosition( 0, 0 );
	m_target.setCoShape( tmp );
	getImageContent().setScaleAndPosition( Double.NaN, Double.NaN, tmpX, tmpY );

	return true;
}
protected CoPageItemImageContentIF getImageContent()
{
	return (CoPageItemImageContentIF) ( (CoContentWrapperPageItemIF) m_target ).getContent();
}
}
