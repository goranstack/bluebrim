package com.bluebrim.text.shared.swing;
import java.awt.*;

/**
 * Interface for text views that can be highlighted
 * 
 * @author: Dennis Malmstr�m
 */
 
interface CoHighlightableView
{
void paintSelectionShadow( Graphics2D g, int from, int to, Shape allocation );
}
