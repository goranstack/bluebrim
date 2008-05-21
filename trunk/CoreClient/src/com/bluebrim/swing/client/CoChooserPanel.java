package com.bluebrim.swing.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
CoChooserPanel är en panel som byggs upp av tre komponenter:
<ul>
<li>source 		- en panel med rubrik och en lista med element
<li>destination - en panel med rubrik och en lista med element
<li>buttons 	- en knapprad som används för att flytta element mellan 'source' och 'destination'.
</ul>
Använder en egen layoutManager - ChooserLayout - som lägger ut de tre komponenterna
så att knappraden alltid får sin 'preferred width' medans 'source' coh 'destination' delar 
på resten av utrymmet.
 */
public class CoChooserPanel extends CoPanel
{
	private EventListenerList		listenerList = new EventListenerList();
	private ListSelectionListener 	selectionListener;
	private CoListBox 				sourceBox;
	private CoListBox 				destinationBox;
	private CoButton 				addButton;
	private CoButton 				removeButton;
	
	private int orientation;
	public static int SOURCE_TO_THE_LEFT = 0;
	public static int SOURCE_TO_THE_RIGHT = 1;

	private int m_move;
	public static int REMOVE_FROM_SOURCE 	= 0;
	public static int COPY_FROM_SOURCE 		= 1;
	
	protected static final String LEFT = "left";
	protected static final String MIDDLE = "middle";
	protected static final String RIGHT = "right";
	
	public class ChooserLayout implements LayoutManager2, java.io.Serializable
	{
		int hgap;
		int vgap;
		Component left;
		Component middle;
		Component right;

		/**
		 */
		public ChooserLayout()
		{
			this(0, 0);
		}
		/**
		 */
		public ChooserLayout(int hgap, int vgap)
		{
			this.hgap = hgap;
			this.vgap = vgap;
		}
		/**
		 */
		public void addLayoutComponent(Component comp, Object constraints)
		{
			synchronized (comp.getTreeLock())
			{
				if ((constraints == null) || (constraints instanceof String))
				{
					addLayoutComponent((String) constraints, comp);
				}
				else
				{
					throw new IllegalArgumentException("cannot add to layout: constraint must be a string (or null)");
				}
			}
		}
		/**
		 */
		public void addLayoutComponent(String name, Component comp)
		{
			synchronized (comp.getTreeLock())
			{
				if (LEFT.equals(name))
				{
					left = comp;
				}
				else
					if (RIGHT.equals(name))
					{
						right = comp;
					}
					else
						if (MIDDLE.equals(name))
						{
							middle = comp;
						}
						else
						{
							throw new IllegalArgumentException("cannot add to layout: unknown constraint: " + name);
						}
			}
		}
		/**
		 */
		public int getHgap()
		{
			return hgap;
		}
		/**
		 * Returns the alignment along the x axis.  This specifies how
		 * the component would like to be aligned relative to other 
		 * components.  The value should be a number between 0 and 1
		 * where 0 represents alignment along the origin, 1 is aligned
		 * the furthest away from the origin, 0.5 is centered, etc.
		 */
		public float getLayoutAlignmentX(Container parent)
		{
			return 0.5f;
		}
		/**
		 * Returns the alignment along the y axis.  This specifies how
		 * the component would like to be aligned relative to other 
		 * components.  The value should be a number between 0 and 1
		 * where 0 represents alignment along the origin, 1 is aligned
		 * the furthest away from the origin, 0.5 is centered, etc.
		 */
		public float getLayoutAlignmentY(Container parent)
		{
			return 0.5f;
		}
		/**
		 */
		public Component getLeft()
		{
			return left;
		}
		/**
		 */
		public Component getMiddle()
		{
			return middle;
		}
		/**
		 */
		public Component getRight()
		{
			return right;
		}
		/**
		 */
		public int getVgap()
		{
			return vgap;
		}
		/**
		 * Invalidates the layout, indicating that if the layout manager
		 * has cached information it should be discarded.
		 */
		public void invalidateLayout(Container target)
		{
		}
		/**
		 */
		public void layoutContainer(Container target)
		{
			if (left == null && middle == null && right == null)
				return;
			synchronized (target.getTreeLock())
			{
				Insets insets = target.getInsets();
				Dimension tSize = target.getSize();
				int top = insets.top;
				int bottom = tSize.height - insets.bottom;
				int tLeft = insets.left;
				int tRight = tSize.width - insets.right;
				int tNmbrOfGaps = 0;
				int tMiddleWidth = 0;
				int tLeftRightWidth = 0;
				boolean tHasLeft = left != null && left.isVisible();
				boolean tHasMiddle = middle != null && middle.isVisible();
				boolean tHasRight = right != null && right.isVisible();
				if (tHasMiddle)
				{
					Dimension d = middle.getPreferredSize();
					tMiddleWidth = d.width;
					tNmbrOfGaps += 1;
				}
				if ((tHasLeft) && (tHasRight))
				{
					tNmbrOfGaps += 1;
					tLeftRightWidth = (tRight - tMiddleWidth - insets.left - tNmbrOfGaps * hgap) / 2;
				}
				else
					if ((tHasLeft) || (tHasRight))
					{
						tLeftRightWidth = (tRight - tMiddleWidth - insets.left - tNmbrOfGaps * hgap);
					}
				if (tHasLeft)
				{
					left.setBounds(tLeft, top, tLeftRightWidth, bottom - top);
					tLeft += tLeftRightWidth + hgap;
				}
				if (tHasMiddle)
				{
					Dimension d = middle.getPreferredSize();
					int tOffset = (bottom - top - d.height) / 2;
					middle.setBounds(tLeft, top + tOffset, tMiddleWidth, bottom - top - tOffset);
					tLeft += tMiddleWidth + hgap;
				}
				if (tHasRight)
				{
					right.setBounds(tLeft, top, tLeftRightWidth, bottom - top);
				}
			}
		}
		/**
		 */
		public Dimension maximumLayoutSize(Container target)
		{
			return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
		}
		/**
		 * Determines the minimum size of the <code>target</code> container 
		 * using this layout manager. 
		 * <p>
		 * This method is called when a container calls its 
		 * <code>getMinimumSize</code> method. Most applications do not call 
		 * this method directly. 
		 * @param   target   the container in which to do the layout.
		 * @return  the minimum dimensions needed to lay out the subcomponents 
		 *          of the specified container.
		 * @see     java.awt.Container  
		 * @see     java.awt.CoChooserLayout#preferredLayoutSize
		 * @see     java.awt.Container#getMinimumSize()
		 * @since   JDK1.0
		 */
		public Dimension minimumLayoutSize(Container target)
		{
			synchronized (target.getTreeLock())
			{
				Dimension dim = new Dimension(0, 0);
				if ((left != null) && left.isVisible())
				{
					Dimension d = left.getMinimumSize();
					dim.width += d.width + hgap;
					dim.height = Math.max(d.height, dim.height);
				}
				if ((middle != null) && middle.isVisible())
				{
					Dimension d = middle.getMinimumSize();
					dim.width += d.width + hgap;
					dim.height = Math.max(d.height, dim.height);
				}
				if ((right != null) && right.isVisible())
				{
					Dimension d = right.getMinimumSize();
					dim.width += d.width;
					dim.height = Math.max(d.height, dim.height);
				}
				Insets insets = target.getInsets();
				dim.width += insets.left + insets.right;
				dim.height += insets.top + insets.bottom;
				return dim;
			}
		}
		/**
		 * Determines the preferred size of the <code>target</code> 
		 * container using this layout manager, based on the components
		 * in the container. 
		 * <p>
		 * Most applications do not call this method directly. This method
		 * is called when a container calls its <code>getPreferredSize</code> 
		 * method.
		 * @param   target   the container in which to do the layout.
		 * @return  the preferred dimensions to lay out the subcomponents 
		 *          of the specified container.
		 * @see     java.awt.Container  
		 * @see     java.awt.CoChooserLayout#minimumLayoutSize  
		 * @see     java.awt.Container#getPreferredSize()
		 * @since   JDK1.0
		 */
		public Dimension preferredLayoutSize(Container target)
		{
			synchronized (target.getTreeLock())
			{
				Dimension dim = new Dimension(0, 0);
				if ((left != null) && left.isVisible())
				{
					Dimension d = left.getPreferredSize();
					dim.width += d.width + hgap;
					dim.height = Math.max(d.height, dim.height);
				}
				if ((middle != null) && middle.isVisible())
				{
					Dimension d = middle.getPreferredSize();
					dim.width += d.width + hgap;
					dim.height = Math.max(d.height, dim.height);
				}
				if ((right != null) && right.isVisible())
				{
					Dimension d = right.getPreferredSize();
					dim.width += d.width;
					dim.height = Math.max(d.height, dim.height);
				}
				Insets insets = target.getInsets();
				dim.width += insets.left + insets.right;
				dim.height += insets.top + insets.bottom;
				return dim;
			}
		}
		/**
		 */
		public void removeLayoutComponent(Component comp)
		{
			synchronized (comp.getTreeLock())
			{
				if (comp == right)
				{
					right = null;
				}
				else
					if (comp == left)
					{
						left = null;
					}
					else
						if (comp == middle)
						{
							middle = null;
						}
			}
		}
		/**
		 */
		public void setHgap(int hgap)
		{
			this.hgap = hgap;
		}
		/**
		 */
		public void setVgap(int vgap)
		{
			this.vgap = vgap;
		}
		/**
		 */
		public String toString()
		{
			return getClass().getName() + "[hgap=" + hgap + ",vgap=" + vgap + "]";
		}
	}
/**
 * CoChooserPanel constructor comment.
 */
public CoChooserPanel(String sourceLabel, String destLabel, int orientation) {
	this(sourceLabel,destLabel,orientation, REMOVE_FROM_SOURCE);

}
/**
 * CoChooserPanel constructor comment.
 */
public CoChooserPanel(String sourceLabel, String destLabel, int orientation, int move) {
	super();
	
	m_move = move;
	setLayout(new ChooserLayout());
	this.orientation					= orientation;

	boolean	tSourceToTheLeft			= orientation == SOURCE_TO_THE_LEFT;

	add(createSourcePanel(sourceLabel), tSourceToTheLeft ? LEFT : RIGHT);
	add(createDestinationPanel(destLabel), tSourceToTheLeft ? RIGHT : LEFT);
	add(createButtonPanel(orientation), MIDDLE);

	addChooserSelectionListener(new CoChooserSelectionListener() {
		public void valueChanged(CoChooserSelectionEvent e)
		{
			if (!e.getValueIsAdjusting())
			{
				if (e.isSourceSelection())
					enableAddButton( true );
				else
					enableRemoveButton( true );
			}
		}
	});


}
public void addChooserEventListener(CoChooserEventListener l)
{
	listenerList.add(CoChooserEventListener.class, l);
}
public void addChooserSelectionListener(CoChooserSelectionListener l)
{
	if (selectionListener == null)
	{
		selectionListener = new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				if (!e.getValueIsAdjusting())
					fireSelectionValueChanged(e.getSource(), e.getFirstIndex(), e.getLastIndex());
			}
		};
		getSourceBox().getList().addListSelectionListener(selectionListener);
		getDestinationBox().getList().addListSelectionListener(selectionListener);
	}
	listenerList.add(CoChooserSelectionListener.class, l);
}
/**
 * CoChooserPanel constructor comment.
 */
protected CoPanel createButtonPanel(int orientation) {

	CoPanel 	tButtonPanel			= new CoPanel(new Insets(2,2,2,2));
	tButtonPanel.setLayout(new BoxLayout(tButtonPanel, BoxLayout.Y_AXIS));
	tButtonPanel.add(Box.createRigidArea(new Dimension(0,10)));
	CoButton	tLeftToRightButton		= createLeftToRightButton();
	tButtonPanel.add(tLeftToRightButton);
	tButtonPanel.add(Box.createRigidArea(new Dimension(0,5)));
	CoButton	tRightToLeftButton		= createRightToLeftButton();
	tButtonPanel.add(tRightToLeftButton);
	tButtonPanel.add(Box.createRigidArea(new Dimension(0,10)));

	addButton	 						= (orientation == SOURCE_TO_THE_LEFT) ? tLeftToRightButton : tRightToLeftButton;
	removeButton						= (orientation == SOURCE_TO_THE_LEFT) ? tRightToLeftButton : tLeftToRightButton;
	addButton.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e)
		{
			fireChooserEvent(CoChooserEvent.ADD_EVENT);
		}
	});
	
	removeButton.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e)
		{
			fireChooserEvent(CoChooserEvent.REMOVE_EVENT);
		}
	});

	return tButtonPanel;
}
/**
 */
protected CoListBox createDestinationBox() {
	return new CoListBox();
}
/**
 * CoChooserPanel constructor comment.
 */
protected CoPanel createDestinationPanel( String destLabel) {

	CoPanel tDestinationPanel			= new CoPanel(new BorderLayout());
	tDestinationPanel.add(new JLabel(destLabel), BorderLayout.NORTH);
	destinationBox						= createDestinationBox();
	destinationBox.setAlignmentX(Component.LEFT_ALIGNMENT);
	tDestinationPanel.add(destinationBox, BorderLayout.CENTER);

	return tDestinationPanel;

}
/**
 */
protected CoButton createLeftToRightButton() {
	return new CoButton(">>");
}
/**
 */
protected CoButton createRightToLeftButton() {
	return new CoButton("<<");
}
/**
 */
protected CoListBox createSourceBox() {
	return new CoListBox();
}
/**
 */
protected CoPanel createSourcePanel(String sourceLabel) {
	
	CoPanel tSourcePanel				= new CoPanel(new BorderLayout());
	tSourcePanel.add(new JLabel(sourceLabel), BorderLayout.NORTH);
	sourceBox							= createSourceBox();
	sourceBox.setAlignmentX(Component.LEFT_ALIGNMENT);
	tSourcePanel.add(sourceBox, BorderLayout.CENTER);
	return tSourcePanel;

}
public void enableBoxes(boolean state)
{
	sourceBox.getList().setEnabled(state);
	destinationBox.getList().setEnabled(state);
}
public void enableButtons(boolean state)
{
	enableAddButton( state );
	enableRemoveButton( state );
}
protected void fireChooserEvent(boolean addEvent)
{
	Object[] 		listeners 	= listenerList.getListenerList();
	CoChooserEvent 	e 			= null;
	for (int i = listeners.length - 2; i >= 0; i -= 2)
	{
		if (listeners[i] == CoChooserEventListener.class)
		{
			if (e == null)
			{
				e = new CoChooserEvent(this, addEvent, addEvent ? getSourceBox().getList().getSelectedValues() : getDestinationBox().getList().getSelectedValues() );
				if
					(m_move == REMOVE_FROM_SOURCE)
				{
					if
						( addEvent )
					{	
						getSourceBox().getList().clearSelection();
					} else {
						getDestinationBox().getList().clearSelection();
					}
				}
			}
			((CoChooserEventListener) listeners[i + 1]).handleChooserEvent(e);
		}
	}
}
protected void fireSelectionValueChanged(Object source, int firstIndex, int lastIndex)
{
	Object[] listeners 			= listenerList.getListenerList();
	CoChooserSelectionEvent e 	= null;
	for (int i = listeners.length - 2; i >= 0; i -= 2)
	{
		if (listeners[i] == CoChooserSelectionListener.class)
		{
			if (e == null)
			{
				boolean tIsSourceSelection 	= source == getSourceBox().getList();
				Object 	selection[]			= tIsSourceSelection ? getSourceSelection() : getDestinationSelection();
				e = new CoChooserSelectionEvent(this, firstIndex, lastIndex, selection, tIsSourceSelection);
			}
			((CoChooserSelectionListener) listeners[i + 1]).valueChanged(e);
		}
	}
}
final public CoButton getAddButton()
{
	return addButton;
}
final public CoPanel getButtonPanel()
{
	return (CoPanel )getChooserLayout().getMiddle();
}
private ChooserLayout getChooserLayout()
{
	return(ChooserLayout)getLayout();
}
final public CoListBox getDestinationBox()
{
	return destinationBox;
}
final public CoPanel getDestinationPanel()
{
	return (CoPanel )((orientation == SOURCE_TO_THE_LEFT) ? getChooserLayout().getRight() : getChooserLayout().getLeft());
}
final public Object[] getDestinationSelection()
{
	return destinationBox.getList().getSelectedValues();
}
final public CoButton getRemoveButton()
{
	return removeButton;
}
final public CoListBox getSourceBox()
{
	return sourceBox;
}
final public CoPanel getSourcePanel()
{
	return (CoPanel )((orientation == SOURCE_TO_THE_LEFT) ? getChooserLayout().getLeft() : getChooserLayout().getRight());
}
final public Object[] getSourceSelection()
{
	return sourceBox.getList().getSelectedValues();
}
public void removeChooserEventListener(CoChooserEventListener l)
{
	listenerList.remove(CoChooserEventListener.class, l);
}
public void removeChooserSelectionListener(CoChooserSelectionListener l)
{
	listenerList.remove(CoChooserSelectionListener.class, l);
}
public void setBackground(Color background, boolean updateComponents)
{
	setBackground(background);
	if (updateComponents)
	{
		getDestinationPanel().setBackground(background);
		getSourcePanel().setBackground(background);
		getButtonPanel().setBackground(background);
	}
}
public void setDestinationBox(CoListBox aListBox)
{
	if (destinationBox != null)
		remove(destinationBox);
	destinationBox = aListBox;
	add(destinationBox, (orientation == SOURCE_TO_THE_LEFT) ? RIGHT : LEFT);
}
public void setForeground(Color foreground, boolean updateComponents)
{
	setForeground(foreground);
	if (updateComponents)
	{
		getDestinationPanel().setForeground(foreground);
		getSourcePanel().setForeground(foreground);
		getButtonPanel().setForeground(foreground);
	}
}
public void setOpaque(boolean state, boolean updateComponents)
{
	setOpaque(state);
	if (updateComponents)
	{
		getSourcePanel().setOpaque(state);
		getButtonPanel().setOpaque(state);
		getDestinationPanel().setOpaque(state);
	}
}
public void setSourceBox(CoListBox aListBox)
{
	if (sourceBox != null)
		remove(sourceBox);
	sourceBox = aListBox;
	add(sourceBox, (orientation == SOURCE_TO_THE_LEFT) ? LEFT : RIGHT);
}

public void enableAddButton(boolean state)
{
	addButton.setEnabled(state && getSourceSelection().length > 0);
}

public void enableRemoveButton(boolean state)
{
	removeButton.setEnabled(state && getDestinationSelection().length > 0);
}
}