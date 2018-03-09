package com.bluebrim.layout.impl.server;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.bluebrim.layout.shared.*;
import com.bluebrim.resource.shared.CoOldResourceBundle;

/**
 * @author G�ran St�ck 2002-09-02
 *
 */
public class CoLayoutServerResources extends CoOldResourceBundle implements CoLayoutServerConstants {
	public static CoOldResourceBundle rb = null;
	static final Object[][] contents = {
		{CoLayoutContentIF.LAYOUT_CONTENT,	"Layout"},	        
		{UNTITLED_LAYOUT,	"New layout"},
		{WRAPPED_IMAGE_CONTENT,	"Image box"},
		{WRAPPED_TEXT_CONTENT,	"Text box"},
		{WRAPPED_LAYOUT_CONTENT,	"Layout box"},
		{WRAPPED_WORKPIECE_TEXT_CONTENT,	"Projecting text box"},
		
		{TEXT_BOX_PROTOTYPE,	"Text box prototype"},
		{IMAGE_BOX_PROTOTYPE,	"Image box prototype"},
		{CAPTION_BOX_PROTOTYPE,	"Caption text box prototype"},
		{LAYOUT_AREA_PROTOTYPE,	"Layout area prototype"},
				
	};

	/**
	  S�tter om rb n�r Locale har �ndrats. 
	 */
	protected static CoOldResourceBundle getBundle() {
		if (rb == null)
			rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoLayoutServerResources.class.getName());
		return rb;
	}

	public Object[][] getContents() {
		return contents;
	}

	/**
	  Svara med det namn som h�r till nyckeln aKey.
	  Saknas en resurs f�r aKey s� svara med denna.
	 */
	public static String getName(String aKey) {
		try {
			return getBundle().getString(aKey);
		} catch (MissingResourceException e) {
			return aKey;
		}
	}
	/**
	  S�tter om rb n�r Locale har �ndrats. 
	 */
	public static void resetBundle() {
		rb = (CoOldResourceBundle) ResourceBundle.getBundle(CoLayoutServerResources.class.getName());
		rb.resetLookup();
	}
}
