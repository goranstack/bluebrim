package com.bluebrim.layout.impl.shared.view;

import com.bluebrim.layout.impl.shared.*;

/**
 * View for CoPageItemTextContent.
 * 
 * @author: Dennis Malmström
 */

public class CoPageItemTextContentView extends CoPageItemAbstractTextContentView
{

public CoPageItemViewRenderer createRenderer( CoPageItemViewRendererFactory f )
{
	return f.create( this );
}

protected boolean isProjectingContent()
{
	return m_isProjecting;
}

	private boolean m_isProjecting;
	private final static long serialVersionUID = -7468373093381719069L;

public CoPageItemTextContentView( CoContentWrapperPageItemView owner, CoPageItemIF pageItem, CoPageItemTextContentIF.State d )
{
	super( owner, pageItem, d );

	sync( d );
}

public CoPageItemTextContentIF getPageItemTextContent()
{
	return (CoPageItemTextContentIF) getPageItem();
}

public void modelChanged( CoPageItemIF.State d, CoPageItemView.Event ev )
{
	super.modelChanged( d, ev );
	sync( (CoPageItemTextContentIF.State) d );
}

private void sync( CoPageItemTextContentIF.State d )
{
	m_isProjecting = d.m_isProjecting;

	m_documentView.setDummyText( null );
}
}