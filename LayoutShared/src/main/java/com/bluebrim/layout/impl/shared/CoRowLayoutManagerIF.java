package com.bluebrim.layout.impl.shared;

import com.bluebrim.layout.impl.shared.layoutmanager.*;

/**
 * Creation date: (2000-06-05 13:41:30)
 * @author: Dennis
 */
 
public interface CoRowLayoutManagerIF extends CoLayoutManagerIF
{
	public static final String ROW_LAYOUT_MANAGER = "ROW_LAYOUT_MANAGER";
double getGap();
void setGap( double g );
}
