package com.bluebrim.layout.impl.shared.layoutmanager;

import com.bluebrim.layout.impl.shared.*;

// Dennis, 2000-09-05

public class CoPageItemLayoutResources_sv extends CoPageItemLayoutResources
{
	static final Object[][] contents =
	{
		{ CoNoLayoutManagerIF.NO_LAYOUT_MANAGER, "Ingen" },
		{ CoRowLayoutManagerIF.ROW_LAYOUT_MANAGER, "Radvis" },
		{ CoColumnLayoutManagerIF.COLUMN_LAYOUT_MANAGER, "Kolumnvis" },
		{ CoReversedColumnLayoutManagerIF.REVERSED_COLUMN_LAYOUT_MANAGER, "Omvänt kolumnvis" },
		{ CoReversedRowLayoutManagerIF.REVERSED_ROW_LAYOUT_MANAGER, "Omvänt radvis" },
	};	

/**
  * @return java.lang.Object[]
 */
public Object[][] getContents ( ) {
	return contents;
}
}
