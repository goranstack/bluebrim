package com.bluebrim.layout.impl.server.xml;

import com.bluebrim.layout.impl.server.*;
import com.bluebrim.layout.impl.server.decorator.*;
import com.bluebrim.layout.impl.server.geom.*;
import com.bluebrim.layout.impl.server.manager.*;
import com.bluebrim.layout.impl.shared.layoutmanager.*;
import com.bluebrim.layoutmanager.*;
import com.bluebrim.xml.shared.*;

/**
 * Xml-mapping for the layout domain
 * 
 * @author Göran Stäck 2002-10-24
 */
public class CoLayoutXmlMapping implements CoXmlMappingSPI {

	public void collectMappings(Mapper mapper) {

		// page items
		mapper.map(CoLayoutEditorModelXmlWrapper.class, CoLayoutEditorModelXmlWrapper.XML_TAG); // temp. should later be removed
		mapper.map(CoContentWrapperPageItem.class, CoContentWrapperPageItem.XML_TAG);
		mapper.map(CoLayoutArea.class, CoLayoutArea.XML_TAG);
		mapper.map(CoDesktopLayoutArea.class, CoDesktopLayoutArea.XML_TAG);
		mapper.map(CoPageItemImageContent.class, CoPageItemImageContent.XML_TAG);
		mapper.map(CoPageItemLayoutContent.class, CoPageItemLayoutContent.XML_TAG);
		mapper.map(CoPageItemTextContent.class, CoPageItemTextContent.XML_TAG);
		mapper.map(CoPageItemWorkPieceTextContent.class, CoPageItemWorkPieceTextContent.XML_TAG);
		mapper.map(CoPageLayoutArea.class, CoPageLayoutArea.XML_TAG);
		mapper.map(CoPageItemNoContent.class, CoPageItemNoContent.XML_TAG);

		// location spec
		mapper.map(CoBottomLocation.class, CoBottomLocation.XML_TAG);
		mapper.map(CoLeftLocation.class, CoLeftLocation.XML_TAG);
		mapper.map(CoCenterLocation.class, CoCenterLocation.XML_TAG);
		mapper.map(CoNoLocation.class, CoNoLocation.XML_TAG);
		mapper.map(CoRightLocation.class, CoRightLocation.XML_TAG);
		mapper.map(CoSlaveLocation.class, CoSlaveLocation.XML_TAG);
		mapper.map(CoTopLocation.class, CoTopLocation.XML_TAG);
		mapper.map(CoBottomLeftLocationSpec.class, CoBottomLeftLocationSpec.XML_TAG);
		mapper.map(CoBottomRightLocationSpec.class, CoBottomRightLocationSpec.XML_TAG);
		mapper.map(CoBottomInsideLocationSpec.class, CoBottomInsideLocationSpec.XML_TAG);
		mapper.map(CoBottomOutsideLocationSpec.class, CoBottomOutsideLocationSpec.XML_TAG);
		mapper.map(CoTopLeftLocationSpec.class, CoTopLeftLocationSpec.XML_TAG);
		mapper.map(CoTopRightLocationSpec.class, CoTopRightLocationSpec.XML_TAG);
		mapper.map(CoTopInsideLocationSpec.class, CoTopInsideLocationSpec.XML_TAG);
		mapper.map(CoTopOutsideLocationSpec.class, CoTopOutsideLocationSpec.XML_TAG);

		// grid
		mapper.map(CoRegularBaseLineGrid.class, CoRegularBaseLineGrid.XML_TAG);
		mapper.map(CoRegularColumnGrid.class, CoRegularColumnGrid.XML_TAG);
		mapper.map(CoCustomGrid.class, CoCustomGrid.XML_TAG);

		// size spec
		mapper.map(CoNoSizeSpec.class, CoNoSizeSpec.XML_TAG);
		mapper.map(CoSlaveSizeSpec.class, CoSlaveSizeSpec.XML_TAG);
		mapper.map(CoProportionalWidthSpec.class, CoProportionalWidthSpec.XML_TAG);
		mapper.map(CoProportionalHeightSpec.class, CoProportionalHeightSpec.XML_TAG);
		mapper.map(CoFillWidthSpec.class, CoFillWidthSpec.XML_TAG);
		mapper.map(CoFillHeightSpec.class, CoFillHeightSpec.XML_TAG);
		mapper.map(CoContentWidthSpec.class, CoContentWidthSpec.XML_TAG);
		mapper.map(CoContentHeightSpec.class, CoContentHeightSpec.XML_TAG);
		mapper.map(CoAbsoluteWidthSpec.class, CoAbsoluteWidthSpec.XML_TAG);
		mapper.map(CoAbsoluteHeightSpec.class, CoAbsoluteHeightSpec.XML_TAG);

		// run around spec
		mapper.map(CoBoundingBoxRunAroundSpec.class, CoBoundingBoxRunAroundSpec.XML_TAG);
		mapper.map(CoNoneRunAroundSpec.class, CoNoneRunAroundSpec.XML_TAG);
		mapper.map(CoShapeRunAroundSpec.class, CoShapeRunAroundSpec.XML_TAG);

		// fill style
		mapper.map(CoGradientFill.class, CoGradientFill.XML_TAG);
		mapper.map(CoPatternFill.class, CoPatternFill.XML_TAG);
		mapper.map(CoLayoutManager.class, CoLayoutManagerXmlBuilder.class);

		// page item prototype stuff
		mapper.map(CoPageItemPrototype.class, CoPageItemPrototype.XML_TAG);
		mapper.map(CoPageItemPrototypeTreeNode.class, CoPageItemPrototypeTreeNode.XML_TAG);
		mapper.map(CoPageItemPrototypeTreeRoot.class, CoPageItemPrototypeTreeRoot.XML_TAG);
		mapper.map(CoPageItemPreferences.class, CoPageItemPreferences.XML_TAG);
		mapper.map(CoLayoutContent.class, CoLayoutContent.XML_TAG, null, CoLayoutContentXmlBuilder.class);

		mapper.map(CoAdPlacementLayoutManager.XML_TAG, CoLayoutManagerModelBuilder.class);
		mapper.map(CoColumnLayoutManager.XML_TAG, CoLayoutManagerModelBuilder.class);
		mapper.map(CoExjobbLayoutManager.XML_TAG, CoLayoutManagerModelBuilder.class);
		mapper.map(CoNoLayoutManager.XML_TAG, CoLayoutManagerModelBuilder.class);
		mapper.map(CoReversedColumnLayoutManager.XML_TAG, CoLayoutManagerModelBuilder.class);
		mapper.map(CoReversedRowLayoutManager.XML_TAG, CoLayoutManagerModelBuilder.class);
		mapper.map(CoRowLayoutManager.XML_TAG, CoLayoutManagerModelBuilder.class);
		mapper.map(CoRectangleLayoutManager.XML_TAG, CoLayoutManagerModelBuilder.class);

		// fill style
		mapper.map(CoPageItemPrototypeTreeNode.XML_TAG, CoPageItemPrototypeTreeModelBuilder.class);
		mapper.map(CoPageItemPrototypeTreeRoot.XML_TAG, CoPageItemPrototypeTreeModelBuilder.class);
		mapper.map(CoPageItemPrototype.XML_TAG, CoPageItemPrototypeModelBuilder.class);

	}

}
