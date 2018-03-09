package com.bluebrim.layout.impl.shared.view;

import java.awt.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Proxy for CoContentWrapperPageItem.
 * 
 * @author: Dennis Malmström
 */

public class CoContentWrapperPageItemView extends CoShapePageItemView
{
	protected CoPageItemContentView m_contentView; // view for the page item content of the content wrapper

	// content wrapper state cache
	protected CoImmutableShapeIF m_clipping;
	
	// page item rendering stuff
	protected transient Shape m_clipShape;

	private CoPageItemViewRendererFactory m_rendererFactory;




public CoPageItemViewRenderer createRenderer( CoPageItemViewRendererFactory f )
{
	return f.create( this );
}
// propagate to content view

public void dispose()
{
	super.dispose();

	m_contentView.dispose();
}
// delegate to content view

public CoImmutableBaseLineGridIF getBaseLineGrid()
{
	return m_contentView.getBaseLineGrid();
}
// delegate to content view

public CoImmutableColumnGridIF getColumnGrid()
{
	return m_contentView.getColumnGrid();
}
// delegate to content view

public Cursor getContentCursor()
{
	return m_contentView.getContentCursor();
}
public CoPageItemContentView getContentView()
{
	return m_contentView;
}

// delegate to content view

public Object getPopupMenuKey()
{
	return m_contentView.getPopupMenuKey();
}

// propagate to content view

public void refresh()
{
	super.refresh();
	m_contentView.refresh();
}
public void setContentView( CoPageItemContentView cv )
{
	// can only be called immediatelly after constructor
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( m_contentView == null, "Illegal call to " + getClass() + ".setContentView( CoPageItemContentView ), content view already set" );
	
	m_contentView = cv;
}
public void setRenderer( CoPageItemViewRendererFactory f )
{
	m_rendererFactory = f;
	super.setRenderer( f );
	m_contentView.setRenderer( f );
}
private void sync( CoContentWrapperPageItemIF.State d )
{
	if
		( m_contentView != null ) // Never create content view in first call (allways from constructor)
	{
		if
			( ! m_contentView.getPageItemId().equals( d.m_contentId ) )
		{
			// page item content changed
			
			// destroy previous content view
			if
				( m_contentView != null )
			{
				m_contentView.dispose();
			}

			// create new content view
			m_contentView = getContentWrapperPageItem().createContentView_shallBeCalledBy_CoContentWrapperPageItemView_only();
			m_contentView.setOwner( this );
			m_contentView.setRenderer( m_rendererFactory );
		}
	}
	
	m_clipping = d.m_clipping;
	m_clipShape = null;
}
// propagate to content view

public void updateAbsoluteGeometryCache( double scale )
{
	super.updateAbsoluteGeometryCache( scale );

	if ( m_contentView != null ) m_contentView.updateAbsoluteGeometryCache( scale );
}


// propagate to content view

public boolean visit(CoPageItemViewVisitor visitor)
{
	if
		( visitor.visitLeafView( this ) )
	{
		return visitor.visitContentView( m_contentView );
	}

	return false;
}

protected boolean isAcceptingWorkPiece()
{
	return m_contentView.isAcceptingWorkPiece();
}

protected boolean isAttachedToWorkPiece()
{
	return m_contentView.isAttachedToWorkPiece();
}

protected boolean isProjectingContent()
{
	return m_contentView.isProjectingContent();
}

protected void paintIcon( Graphics2D g )
{
	m_contentView.paintIcon( g );
}

// propagate to content view

boolean validateAddTo( CoCompositePageItemView parent, boolean isDirectParent )
{
	return super.validateAddTo( parent, isDirectParent ) && m_contentView.validateAddTo( parent, isDirectParent );
}

// propagate to content view

boolean validateRemoveFrom( CoCompositePageItemView parent, boolean isDirectParent )
{
	return super.validateRemoveFrom( parent, isDirectParent ) && m_contentView.validateRemoveFrom( parent, isDirectParent );
}



public CoContentWrapperPageItemIF getContentWrapperPageItem()
{
	return (CoContentWrapperPageItemIF) getPageItem();
}

protected CoPageItemIF.ViewState getViewState()
{
	return CoPageItemViewClientUtilities.m_contentWrapperPageItemViewState;
}

public void modelChanged( CoPageItemIF.State d, CoPageItemView.Event ev )
{
	super.modelChanged( d, ev );

	sync( (CoContentWrapperPageItemIF.State) d );
	
//	m_contentView.ownerModelChanged(); // tell content view                  really ????




	m_contentView.modelChanged( ( (CoContentWrapperPageItemIF.State) d ).m_contentState, ev );
}

protected void prepare( CoPageItemIF.ViewState s )
{
	super.prepare( s );

	( (CoContentWrapperPageItemIF.ViewState) s ).m_contentViewState = m_contentView.getViewState();
	m_contentView.prepare( ( (CoContentWrapperPageItemIF.ViewState) s ).m_contentViewState );
}

public String toString()
{
	return super.toString() + " ( " + ( ( m_contentView == null ) ? null : m_contentView.toString() ) + " )";
}

	private final static long serialVersionUID = 4017557316642764508L;

public CoContentWrapperPageItemView( CoPageItemIF pageItem, CoCompositePageItemView parent, CoContentWrapperPageItemIF.State d, int detailMode )
{
	super( pageItem, parent, d, detailMode );
	
	sync( d );
}

// propagate to content view

protected void prepareForClient()
{
	super.prepareForClient();

	m_contentView.prepareForClient();
}
}