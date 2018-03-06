package com.bluebrim.layout.impl.shared.view;

import java.awt.*;

import com.bluebrim.layout.impl.shared.*;

/**
 * View for CoPageItemBoundedContent.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoPageItemBoundedContentView extends CoPageItemContentView
{
	// page item state cache	
	protected double m_scaleX;
	protected double m_scaleY;
	protected double m_x;
	protected double m_y;
	protected boolean m_flipX;
	protected boolean m_flipY;
	protected boolean m_isClipped;

	




public Cursor getContentCursor()
{
	if
		( hasContent() )
	{
		return Cursor.getPredefinedCursor( Cursor.MOVE_CURSOR );
	} else {
		return getSelectionCursor();
	}
}
public abstract double getContentHeight();
public abstract double getContentWidth();
public boolean getFlipX()
{
	return m_flipX;
}
public boolean getFlipY()
{
	return m_flipY;
}
public double getScaleX()
{
	return m_scaleX;
}
public double getScaleY()
{
	return m_scaleY;
}
public double getX()
{
	return m_x;
}
public double getY()
{
	return m_y;
}
public abstract boolean hasContent();
protected abstract boolean isContentClipped();



// Used when rubberband-reshaping content.
// This is one of the very few methods that allow the view state to differ from the page item state.
// Care must be taken to make sure that the view state doesn't remain unsynch'ed after using this method.

public void scaleContent( double sx, double sy )
{
	m_x *= sx;
	m_scaleX *= sx;

	m_y *= sy;
	m_scaleY *= sy;
}
private void sync( CoPageItemBoundedContentIF.State d )
{
	m_scaleX = d.m_scaleX;
	m_scaleY = d.m_scaleY;

	m_x = d.m_x;
	m_y = d.m_y;

	m_flipX = d.m_flipX;
	m_flipY = d.m_flipY;

	m_hasCaption = d.m_hasCaption;
	m_contentLock = d.m_contentLock;
	
	m_isClipped = isContentClipped();
}
// Used when rubberband-moving content.
// This is one of the very few methods that allow the view state to differ from the page item state.
// Care must be taken to make sure that the view state doesn't remain unsynch'ed after using this method.

public void translateContent( double dx, double dy )
{
	m_x += dx;
	m_y += dy;
}

	protected boolean m_hasCaption;

public boolean hasCaption()
{
	return m_hasCaption;
}

	protected int m_contentLock;
	private final static long serialVersionUID = 8630597917531200189L;

public CoPageItemBoundedContentView( CoContentWrapperPageItemView owner, CoPageItemIF pageItem, CoPageItemBoundedContentIF.State d )
{
	super( owner, pageItem, d );
	
	sync( d );
}

public int getContentLock()
{
	return m_contentLock;
}

public CoPageItemBoundedContentIF getPageItemBoundedContent()
{
	return (CoPageItemBoundedContentIF) getPageItem();
}

public void modelChanged( CoPageItemIF.State d, CoPageItemView.Event ev )
{
	super.modelChanged( d, ev );
	sync( (CoPageItemBoundedContentIF.State) d );
}
}