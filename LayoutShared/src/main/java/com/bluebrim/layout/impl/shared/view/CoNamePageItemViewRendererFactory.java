package com.bluebrim.layout.impl.shared.view;

/**
 * A mapping from page item view "type" to page item view renderers that also paints the page item name
 * 
 * @author: Dennis Malmström
 */

public class CoNamePageItemViewRendererFactory extends CoDefaultPageItemViewRendererFactory {
	public static final CoNamePageItemViewRendererFactory INSTANCE = new CoNamePageItemViewRendererFactory();

	private CoLayoutAreaViewNameRenderer m_layoutArea = new CoLayoutAreaViewNameRenderer();
	private CoContentWrapperPageItemViewNameRenderer m_contentWrapper = new CoContentWrapperPageItemViewNameRenderer();

	public CoPageItemViewRenderer create(CoContentWrapperPageItemView pageItem) {
		return m_contentWrapper;
	}

	public CoPageItemViewRenderer create(CoLayoutAreaView v) {
		return create((CoCompositePageItemView) v);
	}

	public CoPageItemViewRenderer create(CoPageLayoutAreaView v) {
		return create((CoCompositePageItemView) v);
	}

	public String getName() {
		return "Namn";
	}

	public CoPageItemViewRenderer create(CoCompositePageItemView pageItem) {
		return m_layoutArea;
	}
}