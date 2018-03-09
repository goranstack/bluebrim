package com.bluebrim.layout.impl.client.editor;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.geom.*;
import java.util.List;

import com.bluebrim.base.client.datatransfer.*;
import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.transfer.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;

/**
 *
 * @Author: Dennis
 */

class CoPageItemHolderPaletteViewPane extends CoShapePageItemViewPane implements CoPageItemDragSource, DragGestureListener
{
	private Insets m_insets;
	private Color m_selectionColor;
	private CoLayoutHolder m_holder;
	private boolean m_isSelected;
	private String m_label;


public Color getBackground()
{
	return m_isSelected ? m_selectionColor : super.getBackground();
}

public CoLayoutHolder getHolder()
{
	return m_holder;
}

public Insets getInsets()
{
	return m_insets;
}

public String getLabel()
{
	return m_label;
}

/**
 * getSnappingPageItem method comment.
 */
public CoShapePageItemIF getSnappingPageItem() {
	return null;
}

public CoShapePageItemView getSnappingPageItemView()
{
	return (CoShapePageItemView) m_views.get( 0 );
}

public List getTransferablePageItems() 
{
	return null;
}

public List getTransferablePageItemViews()
{
	return m_views;
}

public boolean isOpaque()
{
	return m_isSelected;
}

public boolean isSelected()
{
	return m_isSelected;
}

public void setHolder( CoLayoutHolder h )
{
	m_holder = h;
	m_label = m_holder.getName();

	if
		( m_holder != null )
	{
		String d = m_holder.getDescription();
		if
			( ( d != null ) && ( d.length() < 0 ) )
		{
			setToolTipText( m_holder.getName() + ", " + d );
		} else {
			setToolTipText( m_holder.getName() );
		}
	} else {
		setToolTipText( null );
	}

}



public void setSelected( boolean b )
{
	m_isSelected = b;
}

protected boolean useOutsideStrokeWidth()
{
	return false;
}

public CoPageItemHolderPaletteViewPane( 
	Point2D pos,
	Dimension2D size,
	Insets insets,
	Color selectionColor,
	boolean isPageItemDragSource, // prepare for page item drag
	boolean isNormalDragSource    // prepare for normal drag
)
{
	super( pos, size, true, false );
	
	m_insets = insets;
	m_selectionColor = selectionColor;

	if ( isPageItemDragSource ) new CoDefaultPageItemDragSourceListener( this, this, true );
	if ( ! isPageItemDragSource & isNormalDragSource ) prepareDrag( DnDConstants.ACTION_COPY_OR_MOVE );
}

public boolean canStartDrag()
{
	return true;
}

public void dragGestureRecognized( DragGestureEvent e )
{
	CoDataTransferKit.dragUsing( e, ( (CoShapePageItemIF) getHolder().getLayouts().get( 0 ) ).deepClone(), null );
}

public void prepareDrag( int permittedActions )
{
	CoDataTransferKit.setupDragGestureRecognizer( this, permittedActions, this );
}
}