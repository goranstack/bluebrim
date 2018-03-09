package com.bluebrim.swing.client;

import java.awt.*;

import javax.swing.*;

/**
	Subclass to <code>JPanel</code> with two added instance variables:
	<ul>
	<li>extraInsets <code>Insets</code> margins for placing components. (0,0,0,0) as default.
	<li>emptySize <code>Dimension</code> the default size when the panel is empty. (0,0) as default.
	</ul>
	<code>setEnabled</code> is reimplemented so it also enables/disables all components.
 */
public class CoPanel extends JPanel {
	// To avoid creating excess objects. /Markus
	private final static Insets ZERO_INSETS = new Insets(0,0,0,0);
	private final static Dimension ZERO_DIMENSION = new Dimension(0,0);

	private Insets m_extraInsets = ZERO_INSETS;
	private Dimension m_emptySize	= ZERO_DIMENSION;
public CoPanel() {
	super();
}
public CoPanel(Insets extraInsets) {
	super();
	setExtraInsets(extraInsets);
}
/**
 * CoPanel constructor comment.
 * @param layoutManager java.awt.LayoutManager
 */
public CoPanel(LayoutManager layoutManager) {
	super(layoutManager);
}
/**
 * CoPanel constructor comment.
 * @param layoutManager java.awt.LayoutManager
 */
public CoPanel(LayoutManager layoutManager,Insets extraInsets) {
	super(layoutManager);
	setExtraInsets(extraInsets);
}
/**
 * CoPanel constructor comment.
 * @param layoutManager java.awt.LayoutManager
 * @param isDoubleBuffered boolean
 */
public CoPanel(LayoutManager layoutManager, boolean isDoubleBuffered) {
	super(layoutManager, isDoubleBuffered);
}
/**
 * CoPanel constructor comment.
 * @param layoutManager java.awt.LayoutManager
 * @param isDoubleBuffered boolean
 */
public CoPanel(LayoutManager layoutManager, boolean isDoubleBuffered, Insets extraInsets) {
	super(layoutManager, isDoubleBuffered);
	setExtraInsets(extraInsets);
}
/**
 * CoPanel constructor comment.
 * @param isDoubleBuffered boolean
 */
public CoPanel(boolean isDoubleBuffered) {
	super(isDoubleBuffered);
}
private final Insets doGetInsets(Insets total) {
	Insets norm = super.getInsets(total);
	Insets extra = getExtraInsets();
	if (norm == total) {
		// total was reused
		total.top += extra.top;
		total.left += extra.left;
		total.bottom += extra.bottom;
		total.right += extra.right;
	} else {
		total.top = norm.top + extra.top;
		total.left = norm.left + extra.left;
		total.bottom = norm.bottom + extra.bottom;
		total.right = norm.right + extra.right;
	}
	return total;
}
public Dimension getEmptySize() {
	return m_emptySize;
}
public Insets getExtraInsets() {
	return m_extraInsets;
}
/**
 * Please try to supply your own reusable insets by using
 * the getInsets(Insets) method instead. /Markus
 *
 * @see #getInsets(Insets)
 */
public Insets getInsets() {
	if (m_extraInsets == ZERO_INSETS)
		return super.getInsets();
	else
		return doGetInsets(new Insets(0,0,0,0));
}
public Insets getInsets(Insets total) {
	if (m_extraInsets == ZERO_INSETS)
		return super.getInsets(total);
	else
		return doGetInsets(total);
}
public Dimension getPreferredSize() {
	if (getComponentCount() == 0) {
		Dimension tPrefSize = super.getPreferredSize();
		if ((tPrefSize == null) || (tPrefSize.width == 0 && tPrefSize.height == 0))
			return getEmptySize();
		else
			return tPrefSize;
	} else {
		return super.getPreferredSize();
	}

}
public void setEmptySize(Dimension size) {
	m_emptySize = size;
}
/**
	Enabling/disabling av en CoPanel innebär också en 
	enabling/disabling av dess komponenter.
  */
public void setEnabled(boolean state) {
	if ( state == isEnabled() ) return;
	setChildrenEnabled(state);
	super.setEnabled(state); // 2001-09-05, Dennis: moved from top of method body -> property change notification is performed after childrens states are set
	repaint();	
}
/**
 * Final since it's called from the constructor.
 * /Markus 2000-09-08
 */
public final void setExtraInsets(Insets insets) {
	m_extraInsets = (insets == null) ? ZERO_INSETS : insets;
}

protected void setChildrenEnabled(boolean state) {
	Component components[] = getComponents();
	for (int i = components.length-1; i>=0; i--)
	{
		components[i].setEnabled(state);
	}
}

protected void paintChildren(Graphics g) {
	super.paintChildren(g);
//	drawClassName(g);
}
	
/**
 * Only for debugging purposes. Displays the class name in yellow in a black box that is
 * justified to the right. Uncomment the call line in the paint method to activate the function. 
 */
private void drawClassName(Graphics g)
{
	Graphics2D g2d = (Graphics2D)g;
	Color color = g2d.getColor();
	g2d.setColor(Color.YELLOW);
	g2d.setBackground(Color.BLACK);
	String className = this.getClass().getName();
	className = className.substring(className.lastIndexOf('.') + 1);
	int textMargin = 4;
	Rectangle box = new Rectangle(0,5,g2d.getFontMetrics().stringWidth(className) + textMargin*2,20);
	box.x = getWidth() - box.width - 10;
	g2d.clearRect(box.x, box.y, box.width, box.height);
	g2d.setClip(box);
	g2d.drawString(className, box.x + textMargin, box.height);
	g2d.setColor(color);
	
}

}