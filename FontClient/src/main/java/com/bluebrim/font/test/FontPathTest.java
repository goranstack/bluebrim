package com.bluebrim.font.test;

import java.awt.*;

/**
 * Class used to examine how to set java.awt.fonts property
 * 
 * @author Göran Stäck
 */
public class FontPathTest
{

    public static void main(String[] args)
    {
        FontPathTest fontPathTest = new FontPathTest();
        fontPathTest.listAllFonts();
        System.out.println("Klar");
    }
    
    private FontPathTest()
    {
    }
    
	private void listAllFonts() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//		Font[] fonts = ge.getAllFonts();
//		for (int i = 0; i < fonts.length; i++) {
//			System.out.println(fonts[i].getFontName(Locale.ENGLISH));
//		}
		
		String[] familyNames = ge.getAvailableFontFamilyNames();
		for (int i = 0; i < familyNames.length; i++) {
			System.out.println(familyNames[i]);
		}

	}

}
