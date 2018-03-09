package com.bluebrim.gui.client;

/*
	Implementering av TreeCellRenderer och subklass till JPanel som används i en 
	CoTree och som hämtar sina färger och sin font från en CoUIDefaults som skickas in som ett argument i konstruktorn.
  	I allt övrigt en skamlös kopiering av BasicTreeCellRenderer.
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;

/**
 */

public abstract class CoPanelTreeCellRenderer extends JPanel implements TreeCellRenderer
{
	protected boolean 			selected;
	protected boolean 			hasFocus;
	transient protected Icon 	closedIcon;
	transient protected Icon 	leafIcon;
	transient protected Icon 	openIcon;
	protected Color 			textSelectionColor;
	protected Color 			textNonSelectionColor;
	protected Color 			backgroundSelectionColor;
	protected Color 			backgroundNonSelectionColor;
	protected Color 			borderSelectionColor;
	
/**
  */
public CoPanelTreeCellRenderer()
{
	super();
	setDoubleBuffered(false);
	setOpaque(false);
	setLeafIcon(UIManager.getIcon("Tree.leafIcon"));
	setClosedIcon(UIManager.getIcon("Tree.closedIcon"));
	setOpenIcon(UIManager.getIcon("Tree.openIcon"));
	setTextSelectionColor(UIManager.getColor("Tree.selectionForeground"));
	setTextNonSelectionColor(UIManager.getColor("Tree.textForeground"));
	setBackgroundSelectionColor(UIManager.getColor("Tree.selectionBackground"));
	setBackgroundNonSelectionColor(UIManager.getColor("Tree.background"));
	setBorderSelectionColor(UIManager.getColor("Tree.selectionBorderColor"));
	setFont(UIManager.getFont("Tree.font"));
}
/**
  * Return the background color based 
  * upon the selection state.
  */
public Color getBackgroundColor() {
	
	if(selected) 
	{
	   return  getBackgroundSelectionColor();
	} 
	else if (isOpaque())
	{
	   Color bColor = getBackgroundNonSelectionColor();
	   return (bColor == null)
				? getBackground()
				: bColor;
	}
	else
		return null;
}
	/**
	  * Returns the background color to be used for non selected nodes.
	  */
	public Color getBackgroundNonSelectionColor() {
	return backgroundNonSelectionColor;
	}
	/**
	  * Returns the color to use for the background if node is selected.
	  */
	public Color getBackgroundSelectionColor() {
	return backgroundSelectionColor;
	}
	/**
	  * Returns the color the border is drawn.
	  */
	public Color getBorderSelectionColor() {
	return borderSelectionColor;
	}
	/**
	  * Returns the icon used to represent non-leaf nodes that are not
	  * expanded.
	  */
	public Icon getClosedIcon() {
	return closedIcon;
	}
	/**
	  * Returns the default icon used to represent non-leaf nodes that are not
	  * expanded.
	  */
	public Icon getDefaultClosedIcon() {
	return closedIcon;
	}
	/**
	  * Returns the default icon used to represent leaf nodes.
	  */
	public Icon getDefaultLeafIcon() {
	return leafIcon;
	}
	/**
	  * Returns the default icon used to represent non-leaf nodes that are expanded.
	  */
	public Icon getDefaultOpenIcon() {
	return openIcon;
	}
	/**
	  * Returns the icon used to represent leaf nodes.
	  */
	public Icon getLeafIcon() {
	return leafIcon;
	}
	/**
	  * Returns the icon used to represent non-leaf nodes that are expanded.
	  */
	public Icon getOpenIcon() {
	return openIcon;
	}
	public Dimension getPreferredSize() {
	Dimension        retDimension = super.getPreferredSize();

	if(retDimension != null)
	    retDimension = new Dimension(retDimension.width + 3,
					 retDimension.height);
	return retDimension;
	}
	/**
	  * Returns the color the text is drawn with when the node isn't selected.
	  */
	public Color getTextNonSelectionColor() {
	return textNonSelectionColor;
	}
	/**
	  * Returns the color the text is drawn with when the node is selected.
	  */
	public Color getTextSelectionColor() {
	return textSelectionColor;
	}
/**
  * Configures the renderer based on the passed in components.
  * The value is set from messaging value with toString().
  * The foreground color is set based on the selection and the icon
  * is set based on on leaf and expanded.
  */
public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
{
	selected 			= sel;
	this.hasFocus 		= hasFocus;
	if (sel)
		setForeground(getTextSelectionColor());
	else
		setForeground(getTextNonSelectionColor());
	setValue(value, row, sel, expanded, leaf, hasFocus);
	revalidate();
	return this;
}
/**
  * Paints the value.  The background is filled based on selected.
  */
public void paint(Graphics g)
{
	Color bColor = getBackgroundColor();
	if (bColor != null)
	{
		g.setColor(bColor);
		g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
	}
	super.paint(g);

	if (hasFocus)
	{
		g.setColor(getBorderSelectionColor());
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
	}
}
	/**
	  * Sets the background color to be used for non selected nodes.
	  */
	public void setBackgroundNonSelectionColor(Color newColor) {
	backgroundNonSelectionColor = newColor;
	}
	/**
	  * Sets the color to use for the background if node is selected.
	  */
	public void setBackgroundSelectionColor(Color newColor) {
	backgroundSelectionColor = newColor;
	}
	/**
	  * Sets the color to use for the border.
	  */
	public void setBorderSelectionColor(Color newColor) {
	borderSelectionColor = newColor;
	}
	/**
	  * Sets the icon used to represent non-leaf nodes that are not expanded.
	  */
	public void setClosedIcon(Icon newIcon) {
	closedIcon = newIcon;
	}
public abstract void setIcon(Icon newIcon);
	/**
	  * Sets the icon used to represent leaf nodes.
	  */
	public void setLeafIcon(Icon newIcon) {
	leafIcon = newIcon;
	}
	/**
	  * Sets the icon used to represent non-leaf nodes that are expanded.
	  */
	public void setOpenIcon(Icon newIcon) {
	openIcon = newIcon;
	}
public abstract void setText(String text);
	/**
	  * Sets the color the text is drawn with when the node isn't selected.
	  */
	public void setTextNonSelectionColor(Color newColor) {
	textNonSelectionColor = newColor;
	}
	/**
	  * Sets the color the text is drawn with when the node is selected.
	  */
	public void setTextSelectionColor(Color newColor) {
	textSelectionColor = newColor;
	}
/**
  */
protected void setValue(Object value, int row, boolean isSelected,boolean expanded, boolean leaf, boolean hasFocus)
{
	if (value instanceof Icon)
	{
		if (leaf)
		{
			setIcon(getLeafIcon());
		}
		else if (expanded)
		{
			setIcon(getOpenIcon());
		}
		else
		{
			setIcon(getClosedIcon());
		}
	}
	else
		setText(value != null ? value.toString() : null);
}
}
