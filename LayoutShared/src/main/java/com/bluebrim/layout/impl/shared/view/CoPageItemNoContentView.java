package com.bluebrim.layout.impl.shared.view;

import com.bluebrim.layout.impl.shared.*;

/**
 * View for CoPageItemNoContent.
 * 
 * @author: Dennis Malmström
 */

public class CoPageItemNoContentView extends CoPageItemContentView
{



public CoPageItemViewRenderer createRenderer( CoPageItemViewRendererFactory f )
{
	return f.create( this );
}


public CoPageItemNoContentIF getPageItemNoContent()
{
	return (CoPageItemNoContentIF) getPageItem();
}

	private final static long serialVersionUID = 4310848844389529902L;

public CoPageItemNoContentView( CoContentWrapperPageItemView owner, CoPageItemIF pageItem, CoPageItemIF.State d )
{
	super( owner, pageItem, d );
}
}