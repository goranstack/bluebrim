package com.bluebrim.layout.impl.shared.view;

import java.awt.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * View for CoPageItemLayoutContent.
 * 
 * @author: Dennis Malmström
 */

public class CoPageItemLayoutContentView extends CoPageItemBoundedContentView implements CoImmutableOrderTaggedIF
{
	protected int m_layoutTag;
	private com.bluebrim.content.shared.CoWorkPieceIF m_workPiece;

public CoPageItemViewRenderer createRenderer( CoPageItemViewRendererFactory f )
{
	return f.create( this );
}

public int getOrderTag()
{
	return m_layoutTag;
}
public com.bluebrim.content.shared.CoWorkPieceIF getWorkPiece()
{
	return m_workPiece;
}




	protected CoShapePageItemView m_contentView; // layout view

	private CoPageItemViewRendererFactory m_rendererFactory;
	protected long m_timeStamp = -1;




public double getContentHeight()
{
	return m_contentView.getHeight();
}

public double getContentWidth()
{
	return m_contentView.getWidth();
}

protected CoPageItemIF.ViewState getViewState()
{
	return CoPageItemViewClientUtilities.m_pageItemLayoutContentViewState;
}

public boolean hasContent()
{
	return m_contentView != null;
}

protected boolean isAcceptingWorkPiece()
{
	return m_layoutTag != -1;
}

protected boolean isAttachedToWorkPiece()
{
	return getWorkPiece() != null;
}

protected boolean isContentClipped()
{
	if ( m_contentView == null ) return false;
	
	boolean isClipped = false;
	
	if
		( m_owner != null )
	{
		if
			( m_x < 0 )
		{
			isClipped = true;
		} else 	if
			( m_y < 0 )
		{
			isClipped = true;
		} else if
			( m_x + m_contentView.getWidth() * m_scaleX - 0.001 > m_contentView.getWidth() )
		{
			isClipped = true;
		} else if
			( m_y + m_contentView.getHeight() * m_scaleY - 0.001 > m_contentView.getHeight() )
		{
			isClipped = true;
		}
	}

	return isClipped;
}

protected boolean isProjectingContent()
{
	return hasContent();
}

protected void paintContentIcon( Graphics2D g )
{
	int x = CoPageItemViewClientUtilities.m_iconX;
	int y = CoPageItemViewClientUtilities.m_iconY;
	int w = CoPageItemViewClientUtilities.m_iconWidth - 2 * CoPageItemViewClientUtilities.m_iconX;
	int h = CoPageItemViewClientUtilities.m_iconHeight - 2 * CoPageItemViewClientUtilities.m_iconY;

	if
		( hasCaption() )
	{
		h -= 4;
		g.drawLine( x, y + h, x + w, y + h );
		g.drawLine( x, y + h + 2, x + w, y + h + 2 );
	}
	
	g.setColor( Color.blue );
	g.drawRect( x + 2, y + 2, (int) ( w / 2 - 1 - x ), (int) ( h / 2 - 1 - y ) );
	
	g.setColor( Color.red );
	g.drawOval( (int) ( w / 2 + 1 ), (int) ( h / 2 + 2 ), (int) ( w / 2 - 1 - x ), (int) ( h / 2 - 1 - y ) );
}

protected void prepare( CoPageItemIF.ViewState s )
{
	super.prepare( s );
	
	CoPageItemLayoutContentIF.ViewState S = (CoPageItemLayoutContentIF.ViewState) s;

	// send timestamp of cached layout (if its the same as the timestamp of the models layout then the layout need not be fetched)
	if
		( m_layoutContent != null )
	{
		S.m_timeStamp = m_timeStamp;
		S.m_layoutContent = m_layoutContent;
	} else {
		S.m_timeStamp = 0;
		S.m_layoutContent = null;
	}

	S.m_recursiveLevelMaxCount = m_recursiveLevelMaxCount;
}

public void setRenderer( CoPageItemViewRendererFactory f )
{
	m_rendererFactory = f;
	super.setRenderer( f );
	if ( m_contentView != null ) m_contentView.setRenderer( f );
}

private void sync( CoPageItemLayoutContentIF.State d )
{
	boolean doUpdateLayout = false;
	
	if
		( d.m_isNull )
	{
		doUpdateLayout = ( m_layoutContent != null );
		m_layoutContent = null;
	} else if
		( d.m_layoutContent != null )
	{
		doUpdateLayout = true;
		m_layoutContent = d.m_layoutContent;
	}

	if
		( m_isRecursionTerminator )
	{
		m_contentView = null;
	} else if
		( doUpdateLayout )
	{
		if
			( m_layoutContent == null )
		{
			m_contentView = null;
		} else {
			m_contentView = CoPageItemView.create((CoShapePageItemIF)m_layoutContent.getLayout(), m_rendererFactory, CoPageItemView.DETAILS_EVERYTHING );
			m_timeStamp = d.m_timeStamp;
		}
	}

	
	m_layoutTag = d.m_layoutTag;
	m_workPiece = d.m_workPiece;
	m_recursiveLevelMaxCount = d.m_recursiveLevelMaxCount;
}

public void updateAbsoluteGeometryCache( double scale )
{
	if ( m_contentView != null ) m_contentView.updateAbsoluteGeometryCache( scale );
}

	protected final boolean m_isRecursionTerminator;
	// page item state cache	
	protected com.bluebrim.layout.shared.CoLayoutContentIF m_layoutContent;
	private int m_recursiveLevelMaxCount;
	private final static long serialVersionUID = 1031266022712953997L;

public CoPageItemLayoutContentView( CoContentWrapperPageItemView owner, CoPageItemIF pageItem, CoPageItemLayoutContentIF.State d, boolean isRecursionTerminator )
{
	super( owner, pageItem, d );

	m_isRecursionTerminator = isRecursionTerminator;
	sync( d );
}

public com.bluebrim.layout.shared.CoLayoutContentIF getLayoutContent()
{
	return m_layoutContent;
}

public CoPageItemLayoutContentIF getPageItemLayoutContent()
{
	return (CoPageItemLayoutContentIF) getPageItem();
}

public int getRecursiveLevelMaxCount()
{
	return m_recursiveLevelMaxCount;
}

public void modelChanged( CoPageItemIF.State d, CoPageItemView.Event ev )
{
	super.modelChanged( d, ev );
	sync( (CoPageItemLayoutContentIF.State) d );
}
}