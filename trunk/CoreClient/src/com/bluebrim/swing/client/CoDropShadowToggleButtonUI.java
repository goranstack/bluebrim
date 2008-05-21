package com.bluebrim.swing.client;

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.basic.*;
/**
 	Subklass till BasicToggleButtonUI som fungerar som UI-klass till CoDropShadowToggleButton.
 	Om BasicToggleButtonUI#paint vore bättre faktoriserad så skulle denna klass vara onödig. 
 */
public class CoDropShadowToggleButtonUI extends BasicToggleButtonUI {
/**
 * DropShadowButtonUI constructor comment.
 */
public CoDropShadowToggleButtonUI() {
	super();
}
/**
 * This method was imported from a .class file.
 * No source code is available.
 */
public void paint(java.awt.Graphics g, JComponent jComponent)
{
		CoDropShadowToggleButton tButton 	= (CoDropShadowToggleButton)jComponent;
		ButtonModel buttonModel 		= tButton.getModel();
		Dimension dimension 			= tButton.getSize();
		FontMetrics fontMetrics 		= g.getFontMetrics();
		boolean isPressed 				= false;
		Insets insets 					= jComponent.getInsets();
		Rectangle rectangle1 			= new Rectangle(dimension);
		if ((buttonModel.isArmed() && buttonModel.isPressed()) || buttonModel.isSelected())
		{
			isPressed 						= true;
			rectangle1.x 					+= insets.right;
			rectangle1.y 					+= insets.bottom;
			rectangle1.width 				-= (insets.left+insets.right);
			rectangle1.height 				-= (insets.top+insets.bottom);
		}
		else
		{
			rectangle1.x 					+= insets.left;
			rectangle1.y 					+= insets.top;
			rectangle1.width 				-= (insets.right + insets.left);
			rectangle1.height 				-= (insets.bottom + insets.top);
		}		
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
		if (jComponent.isOpaque())
		{
			g.setColor(tButton.getBackground());
			g.fillRect(rectangle1.x, rectangle1.y, rectangle1.width, rectangle1.height);
		}
		if (tButton.getIcon() != null)
		{
			Icon icon = null;
			if (!buttonModel.isEnabled())
				icon = tButton.getDisabledIcon();
			else if (buttonModel.isPressed() && buttonModel.isArmed())
			{
				icon = tButton.getPressedIcon();
				if (icon == null)
					icon = tButton.getIcon();
			}
	      else if (buttonModel.isSelected())
				icon = tButton.getSelectedIcon();
			else if (tButton.isRolloverEnabled() && buttonModel.isRollover())
				icon = tButton.getRolloverIcon();
			if (icon == null)
				icon = tButton.getIcon();
			if (buttonModel.isPressed() && buttonModel.isArmed())
				icon.paintIcon(jComponent, g, rectangle2.x + insets.left, rectangle2.y + insets.top);
			else
				icon.paintIcon(jComponent, g, rectangle2.x, rectangle2.y);
		}
		if (string != null && !string.equals(""))
		{
			if (buttonModel.isEnabled())
			{
				g.setColor(tButton.getForeground());
				BasicGraphicsUtils.drawString(g, string, buttonModel.getMnemonic(), rectangle3.x + insets.left, rectangle3.y + fontMetrics.getAscent() + insets.top);
			}
			else
			{
				g.setColor(tButton.getBackground().brighter());
				BasicGraphicsUtils.drawString(g, string, buttonModel.getMnemonic(), rectangle3.x, rectangle3.y + fontMetrics.getAscent());
				g.setColor(tButton.getBackground().darker());
				BasicGraphicsUtils.drawString(g, string, buttonModel.getMnemonic(), rectangle3.x - 1, rectangle3.y + fontMetrics.getAscent() - 1);
			}
		}
		if (tButton.isFocusPainted() && tButton.hasFocus())
		{
			g.setColor(Color.blue);
			g.drawRect(rectangle1.x+2, rectangle1.y+2, rectangle1.width-4, rectangle1.height-4);
			g.drawRect(rectangle1.x+3, rectangle1.y+3, rectangle1.width-6, rectangle1.height-6);
//			BasicGraphicsUtils.drawDashedRect(g, rectangle1.x+5, rectangle1.y+4, rectangle1.width - 10, rectangle1.height - 8);
		}
}
}
