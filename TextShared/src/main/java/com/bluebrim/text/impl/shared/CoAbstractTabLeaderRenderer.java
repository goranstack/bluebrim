package com.bluebrim.text.impl.shared;

import com.bluebrim.base.shared.*;
import com.bluebrim.text.shared.*;


/**
 * Abstract class for tab stop leader renderers.
 * 
 * @author: Dennis Malmström
 */

public abstract class CoAbstractTabLeaderRenderer implements CoTabStopIF.Renderer
{

public abstract void paint( CoPaintable g, CoGlyphVector gv, float y, float x0, float x1, float tracking );
}