package com.bluebrim.text.shared;

import javax.swing.text.*;

import com.bluebrim.base.shared.*;

/**
 * Extension of javax.swing.text.TabExpander that adds protocol for rendering tab markers
 * 
 * @author: Dennis Malmström
 */

public interface CoTabExpander extends TabExpander
{

void drawTab( CoPaintable g, CoGlyphVector gv, float y, float x0, float x1, float tracking );
}