package com.bluebrim.layout.impl.shared.view;


/**
 * Abstract superclass mapping from page item view "type" to page item view renderer.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoPageItemViewRendererFactory
{
public CoPageItemViewRenderer create( CoContentWrapperPageItemView v )
{
	return create( (CoShapePageItemView) v );
}
public CoPageItemViewRenderer create( CoLayoutAreaView v )
{
	return create( (CoCompositePageItemView) v );
}


public CoPageItemViewRenderer create( CoPageItemAbstractTextContentView v )
{
	return create( (CoPageItemContentView) v );
}
public CoPageItemViewRenderer create( CoPageItemBoundedContentView v )
{
	return create( (CoPageItemContentView) v );
}
public CoPageItemViewRenderer create( CoPageItemContentView v )
{
	return create( (CoPageItemView) v );
}


public CoPageItemViewRenderer create( CoPageItemImageContentView v )
{
	return create( (CoPageItemBoundedContentView) v );
}
public CoPageItemViewRenderer create( CoPageItemLayoutContentView v )
{
	return create( (CoPageItemBoundedContentView) v );
}
public CoPageItemViewRenderer create( CoPageItemNoContentView v )
{
	return create( (CoPageItemContentView) v );
}
public CoPageItemViewRenderer create( CoPageItemView v )
{
	return CoPageItemViewRenderer.DEFAULT_RENDERER;
}


public CoPageItemViewRenderer create( CoPageItemWorkPieceTextContentView v )
{
	return create( (CoPageItemAbstractTextContentView) v );
}
public CoPageItemViewRenderer create( CoPageLayoutAreaView v )
{
	return create( (CoCompositePageItemView) v );
}
public CoPageItemViewRenderer create( CoShapePageItemView v )
{
	return create( (CoPageItemView) v );
}

public final CoPageItemViewRenderer createRenderer( CoPageItemView v )
{
	return v.createRenderer( this );
}
public abstract String getName();

public CoPageItemViewRenderer create( CoCompositePageItemView v )
{
	return create( (CoShapePageItemView) v );
}

public CoPageItemViewRenderer create( CoPageItemTextContentView v )
{
	return create( (CoPageItemAbstractTextContentView) v );
}

public CoPageItemViewRenderer create( CoStopAtFirstProjectorPageItemView v )
{
	return create( (CoShapePageItemView) v );
}
}