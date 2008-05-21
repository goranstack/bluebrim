package com.bluebrim.layout.impl.shared;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.shared.*;

/*
 */
public interface CoLayoutSpecFactoryIF extends CoFactoryIF {
	public CoLayoutSpecIF createLayoutSpec();
	CoSizeSpecIF getAbsoluteHeightSpec();

	CoSizeSpecIF getAbsoluteWidthSpec();

	public CoLocationSpecIF getBottomLeftLocation();
	public CoLocationSpecIF getBottomLocation();

	public CoLocationSpecIF getBottomRightLocation();
	public CoLocationSpecIF getCenterLocation();
	CoSizeSpecIF getContentHeightSpec();
	CoSizeSpecIF getContentWidthSpec();
	CoSizeSpecIF getFillHeightSpec();
	CoSizeSpecIF getFillWidthSpec();
	public CoLocationSpecIF getLeftLocation();
	CoLocationSpecIF getLocationSpec(String key);
	public CoLocationSpecIF getNoLocation();
	CoSizeSpecIF getNoSizeSpec();

	public CoLocationSpecIF getRightLocation();
	CoSizeSpecIF getSizeSpec(String key, CoShapePageItemIF owner);
	public CoLocationSpecIF getSlaveLocation();
	CoSizeSpecIF getSlaveSizeSpec();

	public CoLocationSpecIF getTopLeftLocation();
	public CoLocationSpecIF getTopLocation();

	public CoLocationSpecIF getTopRightLocation();

	CoSizeSpecIF getAbsoluteHeightSpec(double d);

	CoSizeSpecIF getAbsoluteWidthSpec(double d);

	CoSizeSpecIF getProportionalHeightSpec(boolean useTopLevelAncestorAsBase, double maxProportionValue);

	CoSizeSpecIF getProportionalHeightSpec(boolean useTopLevelAncestorAsBase, double maxProportionValue, double p);

	CoSizeSpecIF getProportionalWidthSpec(boolean useTopLevelAncestorAsBase, double maxProportionValue);

	CoSizeSpecIF getProportionalWidthSpec(boolean useTopLevelAncestorAsBase, double maxProportionValue, double p);
}