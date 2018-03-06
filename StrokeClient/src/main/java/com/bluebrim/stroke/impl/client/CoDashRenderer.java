package com.bluebrim.stroke.impl.client;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

import javax.swing.Icon;

import com.bluebrim.gui.client.CoListCellRenderer;

public class CoDashRenderer extends CoListCellRenderer implements Icon
{
	com.bluebrim.stroke.shared.CoDashIF m_dash;
	float m_lineWidth;
public CoDashRenderer()
{
	m_lineWidth = 5f;
}
public CoDashRenderer(com.bluebrim.stroke.shared.CoDashIF ds)
{
	this();
	m_dash = ds;
}
public com.bluebrim.stroke.shared.CoDashIF getDash()
{
	return m_dash;
}
public Icon getIcon()
{
	return this;
}
public int getIconHeight()
{
	return (int) (15 * m_lineWidth);
}
public int getIconWidth()
{
	return (int) (30 * m_lineWidth);
}
protected Shape getShape(float w, float h)
{
	return new Line2D.Float(0, h / 2, w, h / 2);
}
public void paintDash(Graphics g, int x, int y, int w, int h)
{
	Graphics2D g2 = (Graphics2D)g;
	g2.setTransform(new AffineTransform(1, 0, 0, 1, m_lineWidth / 2 + x, m_lineWidth / 2 + y));
	com.bluebrim.stroke.shared.CoDashIF ds = getDash();
	if (ds != null)
	{
		g2.setStroke(ds.createStroke(m_lineWidth, 0));
		g2.setPaint(Color.black);
		g2.draw(getShape(w - m_lineWidth, h - m_lineWidth));
	}
}
public void paintIcon(Component c, Graphics g, int x, int y)
{
	paintDash(g, x, y, getIconWidth(), getIconHeight());
}
public void setDash(com.bluebrim.stroke.shared.CoDashIF ds)
{
	m_dash = ds;
}
public void setLineWidth(float width)
{
	m_lineWidth = width;
}
/**
 *  This method is sent to the renderer by the drawing table to
 *  configure the renderer appropriately before drawing.  Return
 *  the Component used for drawing.
 *
 * @param	table		the JTable that is asking the renderer to draw.
 *						This parameter can be null.
 * @param	value		the value of the cell to be rendered.  It is
 *						up to the specific renderer to interpret
 *						and draw the value.  eg. if value is the
 *						String "true", it could be rendered as a
 *						string or it could be rendered as a check
 *						box that is checked.  null is a valid value.
 * @param	isSelected	true is the cell is to be renderer with
 *						selection highlighting
 * @param	row			the row index of the cell being drawn.  When
 *						drawing the header the rowIndex is -1.
 * @param	column		the column index of the cell being drawn
 */
protected void setValue(Object value, int index, boolean isSelected, boolean cellHasFocus)
{
	super.setValue(value, index, isSelected, cellHasFocus);
	if (value instanceof com.bluebrim.stroke.shared.CoDashIF)
		setDash((com.bluebrim.stroke.shared.CoDashIF) value);
	else
		setDash(null);
}
}
