package com.bluebrim.swing.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
/**
 	Subklass till BasicButtonUI som fungerar som UI-klass till CoDropShadowButton.
 	Om BasicButtonUI#paint vore bättre faktoriserad så skulle denna klass vara onödig. 
 */
public class CoPopupMenuButtonUI extends BasicButtonUI {
/**
 * DropShadowButtonUI constructor comment.
 */
public CoPopupMenuButtonUI() {
	super();
}
/**
 */
public void paint(java.awt.Graphics g, JComponent jComponent)
{
		CoPopupMenuButton tButton 	= (CoPopupMenuButton)jComponent;
		ButtonModel buttonModel 		= tButton.getModel();
		Dimension dimension 				= tButton.getSize();
		FontMetrics fontMetrics 		= g.getFontMetrics();
		boolean isPressed 				= false;
		Insets insets 						= new Insets(0,0,0,0);
		//Insets insets 						= jComponent.getInsets();
		Rectangle rectangle1 			= new Rectangle(dimension);
		Rectangle rectangle2 			= new Rectangle();
		Rectangle rectangle3 			= new Rectangle();
		Font font 						= jComponent.getFont();
		g.setFont(font);
		String string = SwingUtilities.layoutCompoundLabel(	fontMetrics, 
																		tButton.getText(), 
																		tButton.getIcon(), 
																		tButton.getVerticalAlignment(), 
																		tButton.getHorizontalAlignment(), 
																		tButton.getVerticalTextPosition(), 
																		tButton.getHorizontalTextPosition(), 
																		rectangle1, rectangle2, rectangle3, 
																		(tButton.getText() == null) ? 0 : 4);
		if (buttonModel.isEnabled())
		{
			// Rita ramen och fyll den
			g.setColor(Color.lightGray.brighter());
			g.fillRect(rectangle1.x+1, rectangle1.y+1, rectangle1.width-2, rectangle1.height+1);
			g.setColor(Color.black);
			g.drawRect(rectangle1.x, rectangle1.y, rectangle1.width-2, rectangle1.height-2);
			g.drawLine(rectangle1.x+2, rectangle1.height-1, rectangle1.width, rectangle1.height-1);
			g.drawLine(rectangle1.width-1, rectangle1.y+3, rectangle1.width-1, rectangle1.height-1);
			// Rita pilen
			g.drawLine(rectangle1.x+5, 	rectangle1.height/2-4, rectangle1.x+5+9, rectangle1.height/2-4);
			g.drawLine(rectangle1.x+5+1, rectangle1.height/2-3, rectangle1.x+5+8, rectangle1.height/2-3);
			g.drawLine(rectangle1.x+5+2, rectangle1.height/2-2, rectangle1.x+5+7, rectangle1.height/2-2);
			g.drawLine(rectangle1.x+5+3, rectangle1.height/2-1, rectangle1.x+5+6, rectangle1.height/2-1);
			g.drawLine(rectangle1.x+5+4, rectangle1.height/2-0, rectangle1.x+5+5, rectangle1.height/2-0);
		}
		else
		{
			// Rita ramen och fyll den
			g.setColor(Color.lightGray);
			g.fillRect(rectangle1.x+1, rectangle1.y+1, rectangle1.width-1, rectangle1.height+1);
			g.setColor(Color.darkGray);
			g.drawRect(rectangle1.x, rectangle1.y, rectangle1.width-2, rectangle1.height-2);
			g.drawLine(rectangle1.x+2, rectangle1.height-1, rectangle1.width, rectangle1.height-1);
			g.drawLine(rectangle1.width-1, rectangle1.y+3, rectangle1.width-1, rectangle1.height-1);
			// Rita pilen
			g.setColor(Color.gray);
			g.drawLine(rectangle1.x+5, 	rectangle1.height/2-4, rectangle1.x+5+9, rectangle1.height/2-4);
			g.drawLine(rectangle1.x+5+1, rectangle1.height/2-3, rectangle1.x+5+8, rectangle1.height/2-3);
			g.drawLine(rectangle1.x+5+2, rectangle1.height/2-2, rectangle1.x+5+7, rectangle1.height/2-2);
			g.drawLine(rectangle1.x+5+3, rectangle1.height/2-1, rectangle1.x+5+6, rectangle1.height/2-1);
			g.drawLine(rectangle1.x+5+4, rectangle1.height/2-0, rectangle1.x+5+5, rectangle1.height/2-0);
		
		}	
			
		// Rita texten
		if (string != null && !string.equals(""))
		{
			if (buttonModel.isEnabled())
			{
				g.setColor(tButton.getForeground());
				BasicGraphicsUtils.drawString(g, string, buttonModel.getMnemonic(), rectangle1.x + 20, rectangle3.y + fontMetrics.getAscent() + insets.top);
			}
			else
			{
				g.setColor(tButton.getBackground().brighter());
				BasicGraphicsUtils.drawString(g, string, buttonModel.getMnemonic(), rectangle1.x + 20, rectangle3.y + fontMetrics.getAscent());
				g.setColor(tButton.getBackground().darker());
				BasicGraphicsUtils.drawString(g, string, buttonModel.getMnemonic(), rectangle1.x + 20 - 1, rectangle3.y + fontMetrics.getAscent() - 1);
			}
		}

}
}
