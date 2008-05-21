package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:19:21)
 * @author: Dennis
 */
 
public class CoShapePageItemAdjustBoundedContentUndoCommand extends CoShapePageItemSetGeometryUndoCommand
{
	protected double m_w;
	protected double m_h;
	
	protected double m_contentX;
	protected double m_contentY;
	
	protected double m_contentScaleX;
	protected double m_contentScaleY;
public CoShapePageItemAdjustBoundedContentUndoCommand( String name, CoShapePageItemIF target )
{
	super( name, target );

	CoPageItemBoundedContentIF bc = (CoPageItemBoundedContentIF) ( (CoContentWrapperPageItemIF) m_target ).getContent();
	java.awt.geom.Rectangle2D d = m_target.getCoShape().getBounds2D();
	m_w = d.getWidth();
	m_h = d.getHeight();
	m_contentX = bc.getX();
	m_contentY = bc.getY();	
	m_contentScaleX = bc.getScaleX();
	m_contentScaleY = bc.getScaleY();
}
public boolean doRedo()
{
	return doUndo();
}
public boolean doUndo()
{
	preSetGeometry();
	
	double w = m_w;
	double h = m_h;
	double contentX = m_contentX;
	double contentY = m_contentY;
	double contentScaleX = m_contentScaleX;
	double contentScaleY = m_contentScaleY;

	CoPageItemBoundedContentIF bc = (CoPageItemBoundedContentIF) ( (CoContentWrapperPageItemIF) m_target ).getContent();

	java.awt.geom.Rectangle2D d = m_target.getCoShape().getBounds2D();
	m_w = d.getWidth();
	m_h = d.getHeight();
	m_contentX = bc.getX();
	m_contentY = bc.getY();	
	m_contentScaleX = bc.getScaleX();
	m_contentScaleY = bc.getScaleY();

	m_target.getMutableCoShape().setSize( w, h );
	bc.setScaleAndPosition( contentScaleX, contentScaleY, contentX, contentY );
	
	postSetGeometry();

	return true;
}


}
