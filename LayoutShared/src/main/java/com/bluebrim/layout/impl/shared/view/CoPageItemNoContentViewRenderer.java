package com.bluebrim.layout.impl.shared.view;

import java.awt.*;

import com.bluebrim.base.shared.*;

/**
 * Renderer for CoPageItemNoContentView
 * 
 * @author: Dennis Malmström
 */

public class CoPageItemNoContentViewRenderer extends CoPageItemContentViewRenderer
{

// not much to do here

public void paint( CoPaintable g, CoPageItemView v, Rectangle bounds )
{
	g.addComment( "NO CONTENT" );
}
}