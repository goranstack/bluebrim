package com.bluebrim.layout.impl.shared.view;


/**
 * The default mapping from page item view "type" to page item view renderer.
 * 
 * @author: Dennis Malmström
 */

public class CoDefaultPageItemViewRendererFactory extends CoPageItemViewRendererFactory
{
	public static final CoDefaultPageItemViewRendererFactory INSTANCE = new CoDefaultPageItemViewRendererFactory();
	
	private static final CoLayoutAreaViewRenderer m_layoutArea = new CoLayoutAreaViewRenderer();

	private static final CoPageLayoutAreaViewRenderer m_pageLayerLayoutArea = new CoPageLayoutAreaViewRenderer();

	private static final CoContentWrapperPageItemViewRenderer m_contentWrapperPageItem = new CoContentWrapperPageItemViewRenderer();
	private static final CoPageItemNoContentViewRenderer m_noContent = new CoPageItemNoContentViewRenderer();
	private static final CoPageItemAbstractTextContentViewRenderer m_textContent = new CoPageItemAbstractTextContentViewRenderer();
	private static final CoPageItemLayoutContentViewRenderer m_layoutContent = new CoPageItemLayoutContentViewRenderer();

	private static final CoPageItemImageContentViewRenderer m_imageContent = new CoPageItemImageContentViewRenderer();

protected CoDefaultPageItemViewRendererFactory()
{
}
public CoPageItemViewRenderer create( CoContentWrapperPageItemView pageItem )
{
	return m_contentWrapperPageItem;
}
public CoPageItemViewRenderer create( CoLayoutAreaView pageItem )
{
	return m_layoutArea;
}


public CoPageItemViewRenderer create( CoPageItemAbstractTextContentView pageItem )
{
	return m_textContent;
}
public CoPageItemViewRenderer create( CoPageItemNoContentView pageItem )
{
	return m_noContent;
}


public CoPageItemViewRenderer create( CoPageLayoutAreaView pageItem )
{
	return m_pageLayerLayoutArea;
}

public String getName()
{
	return "Normal";
}

public CoPageItemViewRenderer create( CoPageItemImageContentView pageItem )
{
	return m_imageContent;
}

public CoPageItemViewRenderer create( CoPageItemLayoutContentView pageItem )
{
	return m_layoutContent;
}

	private static final CoStopAtFirstProjectorPageItemViewRenderer m_stopAtFirstProjectorPageItem = new CoStopAtFirstProjectorPageItemViewRenderer();

public CoPageItemViewRenderer create( CoStopAtFirstProjectorPageItemView v )
{
	return m_stopAtFirstProjectorPageItem;
}
}