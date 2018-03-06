package com.bluebrim.layout.impl.shared;

import java.awt.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.content.shared.*;

/**
 * RMI-enbling interface for CoPageItemFactory.
 * 
 * @author: Dennis Malmström
 */
public interface CoPageItemFactoryIF extends CoFactoryIF {
	public static final String PAGE_ITEM_FACTORY = "page_item_factory";

	public CoContentWrapperPageItemIF createCaptionBox();

	public CoFillStyleIF createFillStyle(Color c);

	public CoContentWrapperPageItemIF createImageBox();

	public CoLayoutAreaIF createLayoutArea();

	public CoContentWrapperPageItemIF createLayoutBox();

	public CoContentWrapperPageItemIF createNoContentBox();

	public CoContentWrapperPageItemIF createWorkPieceTextBox();

	public CoLayoutAreaIF createLayoutArea(CoWorkPieceIF wp);

	public CoPageItemContentIF createPageItemContent(String type, CoPageItemContentIF deriveFrom);

	public CoPageLayoutAreaIF createPageLayerLayoutArea();

	public CoRunAroundSpecIF createRunAroundSpec(String factoryKey);

	public CoContentWrapperPageItemIF createTextBox();

	public CoContentWrapperPageItemIF createTextBox(String initialText, boolean doRunAround);

}