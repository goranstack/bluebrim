package com.bluebrim.layout.impl.shared.view;

import java.awt.*;

import com.bluebrim.layout.impl.shared.*;

/**
 * View for CoPageItemContent.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoPageItemContentView extends CoPageItemView
{
	private CoPageItemViewRenderer m_renderer;
	
	protected CoContentWrapperPageItemView m_owner;
	protected final CoPageItemIF m_pageItem; // page item
	
	// page item state cache	
	protected String m_type;





;



		

		

		









;



		

		

		










// delegate to owner view

public double getHeight()
{
	return m_owner.getHeight();
}
// delegate to owner view

protected Graphics2D getHitTestGraphics()
{
	return m_owner.getHitTestGraphics();
}


public CoContentWrapperPageItemView getOwner()
{
	return m_owner;
}
public CoPageItemIF getPageItem ( )
{
	return m_pageItem;
}

protected final CoPageItemViewRenderer getRenderer()
{
	return m_renderer;
}
// delegate to owner view

protected double getScreenScale()
{
	return m_owner.getScreenScale();
}
public String getType()
{
	return m_type;
}
// delegate to owner view

public double getWidth()
{
	return m_owner.getWidth();
}



public void setOwner(CoContentWrapperPageItemView owner)
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( m_owner == null, "Owner can only be set once" );
	
	m_owner = owner;

	refresh();
}
public void setRenderer( CoPageItemViewRendererFactory f )
{
	m_renderer = f.createRenderer( this );
}
private void sync( CoPageItemContentIF.State d )
{
	m_type = d.m_type;
}

public void updateAbsoluteGeometryCache( double scale )
{
}



public boolean visit(CoPageItemViewVisitor visitor)
{
	return visitor.visitContentView(this);
}



// use owners shape

protected Shape getIconShape()
{
	return (Shape) CoPageItemViewClientUtilities.m_iconShapes.get( m_owner.getCoShape().getFactoryKey() );
}

protected CoPageItemIF.ViewState getViewState()
{
	return CoPageItemViewClientUtilities.m_pageItemContentViewState;
}

boolean validateAddTo( CoCompositePageItemView parent, boolean isDirectParent )
{
	return true;
}

boolean validateRemoveFrom( CoCompositePageItemView parent, boolean isDirectParent )
{
	return true;
}

// delegate to owner view

public Container getContainer()
{
	if
		( m_owner != null )
	{	
		return m_owner.getContainer();
	} else {
		return null;
	}
}

public CoPageItemContentIF getPageItemContent()
{
	return (CoPageItemContentIF) getPageItem();
}

public void modelChanged( CoPageItemIF.State d, CoPageItemView.Event ev )
{
	super.modelChanged( d, ev );
	sync( (CoPageItemContentIF.State) d );
}

	protected final com.bluebrim.transact.shared.CoRef m_pageItemId; // page item id
	private final static long serialVersionUID = -8132803224888793117L;

public CoPageItemContentView( CoContentWrapperPageItemView owner, CoPageItemIF pageItem, CoPageItemIF.State d )
{
	m_owner = owner;

	m_pageItem = pageItem;
	m_pageItemId = m_pageItem.getId();

	sync( (CoPageItemContentIF.State) d );
}

public void dispose()
{
	super.dispose();

	m_owner = null;
}

public com.bluebrim.transact.shared.CoRef getPageItemId()
{
	return m_pageItemId;
}
}