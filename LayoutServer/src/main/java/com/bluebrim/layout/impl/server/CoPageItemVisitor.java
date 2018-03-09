package com.bluebrim.layout.impl.server;

import java.util.*;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * A page item visitor that does nothing.
 *
 * @author: Dennis
 */

public class CoPageItemVisitor
{
/**
 * CoAbstractPageItemVisitor constructor comment.
 */
public CoPageItemVisitor() {
	super();
}


public void doToAbstractTextContent( CoPageItemAbstractTextContent textContent, Object anything, boolean goDown )
{
	doToPageItemContent(textContent, anything, goDown );
}

public void doToBoundedContent( CoPageItemBoundedContent bc, Object anything, boolean goDown )
{
	doToPageItemContent( bc, anything, goDown );
}

public void doToCompositePageItem(CoCompositePageItem layoutArea, Object anything, boolean goDown )
{
	doToShapePageItem(layoutArea, anything, goDown );
	if
		( goDown )
	{
		visitChildren( layoutArea, anything );
	}
}

public void doToContentWrapper(CoContentWrapperPageItem contentWrapper, Object anything, boolean goDown )
{
	doToShapePageItem(contentWrapper, anything, goDown );

	if
		( goDown )
	{
		visitContent( contentWrapper, anything );
	}
}

public void doToDesktopLayoutArea(CoDesktopLayoutArea layoutArea, Object anything, boolean goDown )
{
	doToCompositePageItem(layoutArea, anything, goDown );
}

public void doToImageContent( CoPageItemImageContent imageContent, Object anything, boolean goDown )
{
	doToBoundedContent( imageContent, anything, goDown );
}

public void doToLayoutArea(CoLayoutArea layoutArea, Object anything, boolean goDown )
{
	doToCompositePageItem(layoutArea, anything, goDown );
}

public void doToLayoutContent( CoPageItemLayoutContent imageContent, Object anything, boolean goDown )
{
	doToBoundedContent( imageContent, anything, goDown );
}

public void doToNoContent(CoPageItemNoContent noContent, Object anything, boolean goDown )
{
	doToPageItemContent(noContent, anything, goDown );
}

public void doToPageItem(CoPageItemIF pageItem, Object anything, boolean goDown ) {
   // FIXME: If this method is empty on purpose, could we please have a comment here saying so?
	   //Johan Walles (2001-06-13 10:40:13)
}

public void doToPageItemContent( CoPageItemContent pageItemContent, Object anything, boolean goDown )
{
	doToPageItem( pageItemContent, anything, goDown );
	
	if
		( ! goDown )
	{
		visitOwner( pageItemContent, anything );
	}
}

public void doToPageLayoutArea( CoPageLayoutArea l, Object anything, boolean goDown )
{
	doToShapePageItem( l, anything, goDown );
}

public void doToShapePageItem( CoShapePageItemIF pageItem, Object anything, boolean goDown )
{
	doToPageItem( pageItem, anything, goDown );
	if
		( ! goDown )
	{
		visitParent( pageItem, anything );
	}
}

public void doToTextContent(CoPageItemTextContent textContent, Object anything, boolean goDown )
{
	doToAbstractTextContent( textContent, anything, goDown );
}

public void doToWorkPieceTextContent(CoPageItemWorkPieceTextContent textContent, Object anything, boolean goDown )
{
	doToAbstractTextContent( textContent, anything, goDown );
}

protected void visitChildren( CoCompositePageItem parent, Object anything )
{
	
	Iterator i = parent.getChildren().iterator();
	while
		( i.hasNext() )
	{
		CoShapePageItem pi = (CoShapePageItem) i.next();
		pi.visit(this, anything, true);
	}
}

protected void visitContent( CoContentWrapperPageItem contentWrapper, Object anything )
{
	( (CoPageItemContent) contentWrapper.getContent() ).visit( this, anything, true );
}

protected void visitOwner( CoPageItemContent pageItemContent, Object anything )
{
	CoContentWrapperPageItem o = (CoContentWrapperPageItem) pageItemContent.getOwner();
	if ( o != null ) o.visit( this, anything, false );
}

protected void visitParent( CoShapePageItemIF child, Object anything )
{
	CoCompositePageItem p = (CoCompositePageItem) child.getParent();
	if ( p != null ) p.visit( this, anything, false );
}
}