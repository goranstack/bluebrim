package com.bluebrim.swing.client.jdj;

/**

	CoRolloverButton.java
	Created by PS Mediatech AB
	Copyright (c) 1998

	Vidareutveckling av RolloverButton
	Created by Claude Duguay
	Copyright (c) 1998

*/

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class CoRolloverButton extends JButton
{
	public CoRolloverButton(String name)
	{
		this(name, null);
	}
	public CoRolloverButton(String name, Icon icon)
	{
		super(name, icon);
		setOpaque(false);
		setMargin(new Insets(2, 2, 2, 2));
		setBorderPainted(false);
		setFocusPainted(false);
		setVerticalAlignment((icon == null) ? CENTER : TOP);
		setHorizontalAlignment(CENTER);
		setVerticalTextPosition(BOTTOM);
		setHorizontalTextPosition(CENTER);
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e)
			{
				setOpaque(true);
				setBorderPainted(true);
				repaint();
			}
			public void mouseExited(MouseEvent e)
			{
				setOpaque(false);
				setBorderPainted(false);
				repaint();
			}
		});
	}
	public boolean isDefaultButton()
	{
		return false;
	}
	public void setIcon(Icon icon)
	{
		super.setIcon(icon);
		setVerticalAlignment((icon == null) ? CENTER : TOP);		
	}
}
