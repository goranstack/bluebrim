package com.bluebrim.swing.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
/**
 	Subklass till TitledBorder som vid utritandet av titeln tar hänsyn till om 
 	dess komponent är transparent eller ej.
 	@see #paintBorder 
 */
public class CoTitledBorder extends TitledBorder {
/**
 * CoTitledBorder constructor comment.
 * @param title java.lang.String
 */
public CoTitledBorder(String title) {
	super(title);
}
/**
 * CoTitledBorder constructor comment.
 * @param title java.lang.String
 * @param titlePosition java.lang.String
 */
public CoTitledBorder(String title, int titlePosition) {
	this(title);
	setTitlePosition(titlePosition);
}
/**
 * CoTitledBorder constructor comment.
 * @param border javax.swing.border.Border
 */
public CoTitledBorder(Border border) {
	super(border);
}
/**
 * CoTitledBorder constructor comment.
 * @param border javax.swing.border.Border
 * @param title java.lang.String
 */
public CoTitledBorder(Border border, String title) {
	super(border, title);
}
/**
 * CoTitledBorder constructor comment.
 * @param border javax.swing.border.Border
 * @param title java.lang.String
 * @param titleJustification int
 * @param titlePosition int
 */
public CoTitledBorder(Border border, String title, int titleJustification, int titlePosition) {
	super(border, title, titleJustification, titlePosition);
}
/**
 * CoTitledBorder constructor comment.
 * @param border javax.swing.border.Border
 * @param title java.lang.String
 * @param titleJustification int
 * @param titlePosition int
 * @param titleFont java.awt.Font
 */
public CoTitledBorder(Border border, String title, int titleJustification, int titlePosition, Font titleFont) {
	super(border, title, titleJustification, titlePosition, titleFont);
}
/**
 * CoTitledBorder constructor comment.
 * @param border javax.swing.border.Border
 * @param title java.lang.String
 * @param titleJustification int
 * @param titlePosition int
 * @param titleFont java.awt.Font
 * @param titleColor java.awt.Color
 */
public CoTitledBorder(Border border, String title, int titleJustification, int titlePosition, Font titleFont, Color titleColor) {
	super(border, title, titleJustification, titlePosition, titleFont, titleColor);
}
	/**
	 * Implementeringen stulen från superklassen. Dålig faktorisering!
	 */
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

		if (getTitle() == null || getTitle().equals("")) 
		{
			getBorder().paintBorder(c, g, x, y, width, height);
			return;
		}
		boolean		componentIsOpaque	= (c instanceof JComponent) && ((JComponent )c).isOpaque();
		Rectangle grooveRect 			= new Rectangle(	EDGE_SPACING, 
																				EDGE_SPACING,
											 									width - (EDGE_SPACING * 2),
											 									height - (EDGE_SPACING * 2));
		Font font 							= g.getFont();
		Color color 							= g.getColor();

		if(getTitleFont() != null)
			g.setFont(getTitleFont());

		FontMetrics fm 				= g.getFontMetrics();
		int         fontHeight 	= fm.getHeight();
		int         descent 		= fm.getDescent();
		int         ascent 			= fm.getAscent();
		Point       textLoc 		= new Point();
		int         diff;
		int         stringWidth 	= fm.stringWidth(getTitle());
		Insets      insets;

		if(getBorder() != null)
			insets = getBorder().getBorderInsets(c);
		else
			insets = new Insets(0, 0, 0, 0);

		switch (getTitlePosition()) {
			case ABOVE_TOP:
				diff = ascent + descent + (Math.max(EDGE_SPACING,
								TEXT_SPACING*2) - EDGE_SPACING);
				grooveRect.y += diff;
				grooveRect.height -= diff;
				textLoc.y = grooveRect.y - (descent + TEXT_SPACING);
				break;
			case TOP:
			case DEFAULT_POSITION:
				diff 					= Math.max(0, ((ascent/2) + TEXT_SPACING) - EDGE_SPACING);
				grooveRect.y 		+= diff;
				grooveRect.height -= diff;
				textLoc.y 			= (grooveRect.y - descent) + (insets.top + ascent + descent)/2;
				break;
			case BELOW_TOP:
				textLoc.y = grooveRect.y + insets.top + ascent + TEXT_SPACING;
				break;
			case ABOVE_BOTTOM:
				textLoc.y = (grooveRect.y + grooveRect.height) -
				(insets.bottom + descent + TEXT_SPACING);
				break;
			case BOTTOM:
				grooveRect.height -= fontHeight/2;
				textLoc.y = ((grooveRect.y + grooveRect.height) - descent) +
						((ascent + descent) - insets.bottom)/2;
				break;
			case BELOW_BOTTOM:
				grooveRect.height -= fontHeight;
				textLoc.y = grooveRect.y + grooveRect.height + ascent +
						TEXT_SPACING;
				break;
		}

		getBorder().paintBorder(c, g, grooveRect.x, grooveRect.y, grooveRect.width, grooveRect.height);

		switch (getTitleJustification()) {
			case LEFT:
			case DEFAULT_JUSTIFICATION:
				textLoc.x = grooveRect.x + TEXT_INSET_H + insets.left;
				break;
			case RIGHT:
				textLoc.x = (grooveRect.x + grooveRect.width) -
						(stringWidth + TEXT_INSET_H + insets.right);
				break;
			case CENTER:
				textLoc.x = grooveRect.x +
						((grooveRect.width - stringWidth) / 2);
				break;
		}
		if (componentIsOpaque)
		{
			g.setColor(c.getBackground());
			g.fillRect(textLoc.x - TEXT_SPACING, textLoc.y - (fontHeight-descent),
				   		 stringWidth + (2 * TEXT_SPACING), fontHeight - descent);
		}	
		g.setColor(getTitleColor());
		g.drawString(getTitle(), textLoc.x, textLoc.y);

		g.setFont(font);
		g.setColor(color);
	}
}
