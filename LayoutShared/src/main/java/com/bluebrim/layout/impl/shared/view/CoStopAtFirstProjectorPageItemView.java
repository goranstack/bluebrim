package com.bluebrim.layout.impl.shared.view;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * 
 * Creation date: (2001-10-15 17:11:29)
 * @author: Dennis
 */
 
public class CoStopAtFirstProjectorPageItemView extends CoShapePageItemView
{
	private final static long serialVersionUID = 1895193018194756611L;
public CoStopAtFirstProjectorPageItemView( CoPageItemIF pageItem, CoCompositePageItemView parent, CoShapePageItemIF.State d )
{
	super( pageItem, parent, d, DETAILS_STOP_AT_FIRST_PROJECTOR );
}
public CoPageItemViewRenderer createRenderer( CoPageItemViewRendererFactory f )
{
	return f.create( this );
}
/**
 * visit method comment.
 */
public boolean visit(CoPageItemViewVisitor visitor)
{
	return false;
}
}
