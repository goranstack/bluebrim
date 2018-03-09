package com.bluebrim.swing.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;
/**
 	Borderklass som ritar upp en mjuk skugga som border.
 	Instansvariabler:
 	<ul>
 	<li>	m_dropX 	skuggans förskjutning i x-led 
 	<li>	m_dropy 	skuggans förskjutning i y-led 
 	</ul>
 */
public class CoDropShadowBorder extends AbstractBorder {
	protected int m_dropX;
	protected int m_dropY;
public CoDropShadowBorder() {
	this(4,4);
}
public CoDropShadowBorder(int dropX, int dropY) {
	super();
	m_dropX 	= dropX;
	m_dropY 	= dropY;
}
protected void doPaintBorder(Component component, Graphics g, int x, int y, int width, int height)
{
	final int LEFT		= 0;
	final int TOP		= 1;
	final int RIGHT		= 2;
	final int BOTTOM	= 3;
	
	int alpha[] 		= {20, 20, 32, 32, 32, 64, 64};
	int insets[][][] 	= { {{7, 6, 2, 0}, {7, 6, 2, 1}, {6, 6, 0, 2}, {5, 5, 2, 3}, {4, 3, 5, 6}}, 
							{{8, 6, 2, 1}, {7, 5, 3, 2}, {6, 4, 5, 3}, {5, 7, 1, 5}}, 
							{{8, 7, 3, 2}, {7, 7, 3, 3}, {7, 8, 2, 4}, {6, 5, 5, 5}}, 
							{{9, 6, 4, 3}, {8, 8, 3, 4}, {7, 8, 3, 5}, {6, 8, 3, 6}}, 
							{{9, 7, 5, 4}, {8, 6, 5, 4}, {7, 8, 3, 6}}, 
							{{9, 7, 5, 5}, {8, 7, 4, 6}}, 
							{{9, 8, 5, 6}}};
	for (int i = 0; i < insets.length; i++)
	{
		int c = 128;
		g.setColor(new Color(c, c, c, alpha[i]));
		int iArraySize = insets[i].length;
		for (int j = 0; j < iArraySize; j++)
		{
			int iBoundsArray[] = insets[i][j];
			g.fillRect(x + iBoundsArray[LEFT], height - m_dropY, width - (x + iBoundsArray[LEFT]) - iBoundsArray[RIGHT], m_dropY - iBoundsArray[BOTTOM]);
			g.fillRect(width - m_dropX, y + iBoundsArray[TOP], m_dropX - iBoundsArray[RIGHT], height - (y + iBoundsArray[TOP]+iBoundsArray[TOP]) - iBoundsArray[BOTTOM]);
		}
	}
}
public Insets getBorderInsets(Component component) {
	return new Insets(0, 0, m_dropX, m_dropY);
}
public void paintBorder(Component component, Graphics g, int x, int y, int width, int height) {
	Color color = g.getColor();
	prepareBorderPaint(component, g, x, y, width, height);
	doPaintBorder(component, g, x, y, width, height);
	g.setColor(color);
}
protected void prepareBorderPaint(Component component, Graphics g, int x, int y, int width, int height) {
	g.setColor(component.getParent().getBackground());
	g.fillRect(width - m_dropX, 0, width, height);
	g.fillRect(0, height - m_dropY, width, height);
}
}
