package com.bluebrim.swing.client;

// Written by David Flanagan.  Copyright (c) 1996 O'Reilly & Associates.
// You may study, use, modify, and distribute this example for any purpose.
// This example is provided WITHOUT WARRANTY either expressed or implied.

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.bluebrim.base.shared.CoObjectIF;
import com.bluebrim.resource.shared.CoResourceLoader;

public class MultiLineLabel extends JComponent implements SwingConstants {
	protected String[] 		lines;         				// The lines of text to display
	protected int 			num_lines;          		// The number of lines
	protected int 			line_height;       			// Total height of the font
	protected int 			line_ascent;        		// Font height above baseline
	protected int[] 		line_widths;      			// How wide each line is
	protected int 			max_width;          		// The width of the widest line
	protected int 			alignment 					= LEFT;   	// The alignment of the text.
	protected int 			m_horizontalTextPosition 	= RIGHT;   	// The horizontal position of the text relative the icon.
	protected int 			m_verticalTextPosition 		= CENTER;   // The vertical position of the text relative the icon.
	private   int			m_iconTextGap 	= 4;
	private   Icon			m_icon;
	
	
	private class Tokenizer implements Iterator {
		
		private int 	currentPosition;
		private int 	maxPosition;
		private String 	str;
		private String 	delimiters;

		public Tokenizer(String str) {
			this(str, " \t\n\r\f");
		}
		public Tokenizer(String str, String delim) {
			currentPosition = 0;
			this.str 		= str;
			maxPosition 	= str.length();
			delimiters 		= delim;
		}

		public int countTokens() {
			int count 	= 0;
			int currpos = currentPosition;

			while (currpos < maxPosition) 
			{
			    while ((currpos < maxPosition) && isDelimiter(currpos)) 
			    {
				    System.out.println("Found delimiter at "+currpos);
					currpos++;
				    if (currpos >= maxPosition || isDelimiter(currpos))
				    	break;
			    }

			    if (currpos >= maxPosition) 
			    {
					break;
			    }

			    while (currpos < maxPosition && !isDelimiter(currpos)) 
			    {
					currpos++;
			    }
			    count++;

			}
			return count;
		}
		
		private boolean isDelimiter(char ch)
		{
			return delimiters.indexOf(ch) >= 0;
		}
		
		private boolean isDelimiter(int pos)
		{
			return isDelimiter(str.charAt(pos));
		}
		
		public boolean hasNext() {
			skipDelimiters();
			return (currentPosition < maxPosition);
		}

		public Object next () {
			return nextToken();
		}

		public String nextToken() {
			if (isDelimiter(currentPosition) && (currentPosition+1 < maxPosition) && isDelimiter(currentPosition+1))
			{
				currentPosition++;
				return "";
			}
			else
			{
				skipDelimiters();

				if (currentPosition >= maxPosition) 
				{
			    	throw new NoSuchElementException();
				}

				int start = currentPosition;
				while (currentPosition < maxPosition && !isDelimiter(currentPosition)) 
				{
				    currentPosition++;
				}
				return str.substring(start, currentPosition);
			}
		}
		
		private void skipDelimiters() {
			while (currentPosition < maxPosition)
			{
				if (!isDelimiter(currentPosition))
			   	 	break;
			   	 else
			   	 {
					if (currentPosition+1 >= maxPosition || isDelimiter(currentPosition+1))
					{
						break;
					}
					else
			   			currentPosition++;
						
			   	 }
			}
		}
		public void remove() {}
	}	
	
  public static void main( String args[] )
	{

	  JFrame f = new JFrame( "MultiLine Label" );
	  f.addWindowListener(new WindowAdapter() {
		  public void windowClosed(WindowEvent e)
		  {
			  System.exit(1);
		  }
	  });
	  
	  MultiLineLabel label = new MultiLineLabel(  "This is a test to see how this text is wrapped into many, many different " +
												   "paragraphs.\nThis sentence is the last one in this first paragraph, and a " +
												   "single blank line should separate this from the next paragraph.\n\n" +
												   "And here we have the second paragraph, splendidly separated and made\n" +
												   "nicely readable through the very impressive, resizable word-wrapping " +
												   "panel.\n\n" +
												   "You should try resizing this frame, by the way; especially try narrowing " +
												   "it until the vertical scrollbar appears.\nA horizontal scrollbar will " +
												   "never be rendered because this would defeat the word-wrapping.\n" ); 
	  f.getContentPane().add(label);
	  label.setIcon(CoResourceLoader.loadIcon(CoObjectIF.class,"CoBookmarkIF.gif"));

	  f.pack();
	  f.setVisible( true );
	}


	public MultiLineLabel(String label) {
		this(label,LEFT);
	}


	public MultiLineLabel(String label, int alignment) {
		newLabel(label);
		this.alignment = alignment;
		setForeground(UIManager.getColor("Label.foreground"));
		setBackground(UIManager.getColor("Label.background"));
	}


	// This method is invoked after our Canvas is first created
	// but before it can actually be displayed.  After we've
	// invoked our superclass's addNotify() method, we have font
	// metrics and can successfully call measure() to figure out
	// how big the label is.
	public void addNotify() { super.addNotify(); measure(); }


private Point calculateIconPosition(Insets insets, Dimension size)
{

	Point p 		= new Point();
	
	int textHeight 	= num_lines * line_height;
	int iconWidth 	= m_icon.getIconWidth();
	int iconHeight 	= m_icon.getIconHeight();
	
	switch (m_horizontalTextPosition)
	{
		case LEFT:
			p.x  = size.width - insets.right - iconWidth - m_iconTextGap;
			break;
		case CENTER:
			p.x  = (size.width - insets.left - insets.right - iconWidth)/2;
			break;
		case RIGHT:
			p.x  = insets.left;
			break;
	}
	switch (m_verticalTextPosition)
	{
		case TOP:
			p.y = insets.top+textHeight + m_iconTextGap;
			break;
		case CENTER:
			p.y = (size.height - insets.top - insets.bottom - iconHeight) / 2;
			break;
		case BOTTOM:
			int textIconHeight 	= iconHeight + m_iconTextGap + textHeight;
			p.y					= (size.height - insets.top - insets.bottom - textIconHeight)/2;
			break;
	}


	return p;
}


private Point calculateTextPosition(Insets insets, Dimension size)
{

	Point p 		= new Point();
	int textHeight 	= num_lines * line_height;
	
	switch (alignment)
	{
		case LEFT :
			p.x = insets.left;
			break;
		case CENTER :
			p.x =  size.width - insets.left - insets.bottom;
			break;
		case RIGHT :
			p.x = size.width - insets.right;
			break;
	}
	
	if (m_icon != null)
	{
		int iconWidth 	= m_icon.getIconWidth();
		int iconHeight 	= m_icon.getIconHeight();
		
		switch (m_horizontalTextPosition)
		{
			case RIGHT:
				p.x += iconWidth + m_iconTextGap;
				break;
			case LEFT:
				p.x -= iconWidth + m_iconTextGap;
				break;
		}
		switch (m_verticalTextPosition)
		{
			case TOP:
				p.y = insets.top;
				break;
			case CENTER:
				p.y = (size.height - insets.top - insets.bottom - textHeight) / 2;
				break;
			case BOTTOM:
				int textIconHeight 	= iconHeight + m_iconTextGap + textHeight;
				p.y					= (size.height - insets.top - insets.bottom - textIconHeight)/2 + iconHeight + m_iconTextGap;
				break;
		}

	}
	else
	{
		p.y = (size.height - insets.top - insets.bottom - textHeight) / 2;

	}

	return p;
}


	public int getAlignment() { return alignment; }


	// This method figures out how the font is, and how wide each
	// line of the label is, and how wide the widest line is.
	protected void measure() {
		FontMetrics fm = this.getFontMetrics(this.getFont());
		// If we don't have font metrics yet, just return.
		if (fm == null) return;
		
		line_height = fm.getHeight();
		line_ascent = fm.getAscent();
		max_width = 0;
		for(int i = 0; i < num_lines; i++) {
			line_widths[i] = fm.stringWidth(lines[i]);
			if (line_widths[i] > max_width) max_width = line_widths[i];
		}
		revalidate();
	}


	// This method is called when the layout manager wants to know
	// the bare minimum amount of space we need to get by.
	public Dimension minimumSize() {
		int    	width  = max_width;
		int 	height = num_lines * line_height;
		if (m_icon != null)
		{
			switch (m_horizontalTextPosition)
			{
				case LEFT:
				case RIGHT:
					width += m_icon.getIconWidth() + m_iconTextGap;
					break;
				case CENTER:
					width = Math.max(width,  m_icon.getIconWidth());
				break;
			}
			switch (m_verticalTextPosition)
			{
				case TOP:
				case BOTTOM:
					height += m_icon.getIconHeight() + m_iconTextGap;
					break;
				case CENTER:
					height = Math.max(height,  m_icon.getIconHeight());
				break;
			}
		}
		return new Dimension(width, height);
	}


	// This method breaks a specified label up into an array of lines.
	// It uses the Tokenizer utility class.
	private void newLabel(String label) {
		Tokenizer t = new Tokenizer(label, "\n");
		num_lines 	= t.countTokens();
		lines 		= new String[num_lines];
		line_widths = new int[num_lines];
		int 	  i = 0;
		while (t.hasNext() && i<num_lines)
		{
			String line = t.nextToken();
			lines[i] 	= line;
			i++;
		}
	}


	// This method draws the label (applets use the same method).
	// Note that it handles the margins and the alignment, but that
	// it doesn't have to worry about the color or font--the superclass
	// takes care of setting those in the Graphics object we're passed.
	public void paintComponent(Graphics g) {
		
		Insets 		insets 		= getInsets();
		Dimension 	d 			= getSize();
		
		Point 		textStart	= calculateTextPosition(insets, d);
		int 		x 			= 0;					
		int 		y 			= line_ascent + textStart.y;
		
		for(int i = 0; i < num_lines; i++, y += line_height) 
		{
			switch(alignment) 
			{
				case LEFT:
					x = textStart.x; 
					break;
				case CENTER:
				default:
					x = (textStart.x - line_widths[i])/2; break;
				case RIGHT:
					x = textStart.x - line_widths[i]; break;
			}
			g.drawString(lines[i], x, y);
		}

		if (m_icon != null)
		{
			Point iconPos	= calculateIconPosition(insets, d);
			m_icon.paintIcon(this, g, iconPos.x, iconPos.y);
		}
	}


	// This method is called by a layout manager when it wants to
	// know how big we'd like to be.  
	public Dimension preferredSize() {
		Insets 	insets = getInsets();
		int    	width  = max_width + insets.left + insets.right;
		int 	height = num_lines * line_height + insets.top+insets.bottom;
		if (m_icon != null)
		{
			switch (m_horizontalTextPosition)
			{
				case LEFT:
				case RIGHT:
					width += m_icon.getIconWidth() + m_iconTextGap;
					break;
				case CENTER:
					width = Math.max(width,  m_icon.getIconWidth() + insets.left + insets.right);
				break;
			}
			switch (m_verticalTextPosition)
			{
				case TOP:
				case BOTTOM:
					height += m_icon.getIconHeight() + m_iconTextGap;
					break;
				case CENTER:
					height = Math.max(height,  m_icon.getIconHeight() + insets.top + insets.bottom);
				break;
			}
		}
		return new Dimension(width, height);
	}


public void setAlignment(int a)
{
	alignment = a;
	repaint();
}


	public void setFont(Font f) {
		super.setFont(f);
		measure();
		repaint();
	}


	public void setForeground(Color c) { 
		super.setForeground(c); 
		repaint(); 
	}


public void setHorizontalTextPosition(int position)
{
	m_horizontalTextPosition = position;
	revalidate();
	repaint();
}


public void setIcon(Icon icon)
{
	m_icon = icon;
	revalidate();
	repaint();
}


public void setIconTextGap(int gap)
{
	m_iconTextGap = gap;
	revalidate();
	repaint();
}


	// Methods to set the various attributes of the component
	public void setText(String label) {
		newLabel(label);
		measure();
		repaint();
	}


public void setVerticalTextPosition(int position)
{
	m_verticalTextPosition = position;
	revalidate();
	repaint();
}
}