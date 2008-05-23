package com.bluebrim.font.test;

import java.io.*;
import java.awt.*;

import javax.swing.*;

public class FontLoadingDemo
{
	
	private Point windowLocation = new Point(10, 10);
	
	public static void main(String[] args) throws FontFormatException, IOException
	{
		FontLoadingDemo instance = new FontLoadingDemo();
		instance.loadFont(FontLoadingDemo.class.getResourceAsStream("VeraSe.ttf"), Font.TRUETYPE_FONT);
		instance.loadFont(FontLoadingDemo.class.getResourceAsStream("a010013l.pfb"), Font.TYPE1_FONT);
	}

	private void loadFont(InputStream in, int fontType) throws FontFormatException, IOException
	{
		in = FontLoadingDemo.class.getResourceAsStream("VeraSe.ttf");
		Font dynamicFont = Font.createFont(fontType, in);
		Font dynamicFont32Pt = dynamicFont.deriveFont(32f);

		// draw something with it
		JLabel testLabel = new JLabel("Dynamically loaded font \"" + dynamicFont.getName() + "\"");
		testLabel.setFont(dynamicFont32Pt);
		JFrame frame = new JFrame("Font Loading Demo");
		frame.getContentPane().add(testLabel);
		frame.pack();
		frame.setLocation(windowLocation);
		frame.setVisible(true);
		windowLocation.x = windowLocation.x + frame.getHeight();
		windowLocation.y = windowLocation.y + frame.getHeight();
	}
}

/*
 * Swing Hacks Tips and Tools for Killer GUIs By Joshua Marinacci, Chris Adamson First Edition June
 * 2005 Series: Hacks ISBN: 0-596-00907-0 http://www.oreilly.com/catalog/swinghks/
 */
