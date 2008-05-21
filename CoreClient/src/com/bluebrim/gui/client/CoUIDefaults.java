package com.bluebrim.gui.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * Subclass to <code>UIDefaults</code> that is used by <code>CoUserInterfaceBuilder</code>
 * to keep fonts, colors etc for its userinterface model.
 * <br>
 * An instance of this class is loaded with, for example, the color used as background for a list,
 * the font used by the nodes in a tree view etc. The keys used are the same as those used by the
 * L&F and the <code>UIManager</code>, i.e "Label.font", "Tree.background" etc.
 * <br>
 * An instance of <code>CoUIDefaults</code> is created by the userinterface model when its userinterface builder
 * is created. The <code>setUIDefaults</code> method is then called with the new instance as an argument. 
 * This method can be reimplemented in the userinterface subclass and in this way the the defaults can be loaded with
 * properties specific to the current userinterface model.
 * <br>
 * The "prepare..." methods in the userinterface builder asks the CoUIDefault instance for fonts, colors etc when a widget 
 * is created. 
 * If the properties asked for aren't defined in the <code>CoUIDefaults</code> they are fetched from 
 * the <code>UIMananger</code>.
 * <br>
 * The following methods are from <code>CoUserInterface</code> 
 * <pre><code>
 *  	protected CoUIDefaults createUIDefaults() 
 *  	{
 * 			CoUIDefaults tDefaults =  new CoUIDefaults();
 * 			setUIDefaults(tDefaults);
 * 			return tDefaults;
 * 		}
 * 	
 * 		protected void setUIDefaults(CoUIDefaults defaults) 
 * 		{
 * 			defaults.put(TREE_TEXT_BACKGROUND, null);
 * 			defaults.put(PANEL_BACKGROUND, CoUIConstants.VERY_LIGHT_GRAY);
 * 
 * 		}
 * </code></pre>
 *
 * NOTE: Added code that modifies the UIManager state, which thus affects
 * all swing components created thereafter. (In this case, after this class
 * is loaded, which at the latest happens when an CoUserInterface subclass
 * is being built.) Check link below! /Markus 2000-08-03
 * @see #getSwingDefaults()
 */
public class CoUIDefaults extends UIDefaults implements CoUIConstants {

	private static CoUIDefaults INSTANCE;
	static {
		INSTANCE = new CoUIDefaults();
		initializeSwingDefaults();
	}

	public static CoUIDefaults getInstance() {
		return INSTANCE;
	}

	public CoUIDefaults() {
		super();
		initializeDefaults();
	}

	/**
	 * CoUIDefaults constructor comment.
	 * @param keyValueList java.lang.Object[]
	 */
	public CoUIDefaults(java.lang.Object[] keyValueList) {
		super(keyValueList);
	}

	public boolean getBoolean(Object key) {
		Boolean tBoolean = (Boolean) super.get(key);
		return (tBoolean != null) ? tBoolean.booleanValue() : false;
	}

	/**
	 * If the value of <code>key</code> is a Border return it, otherwise
	 * return null.
	 */
	public Border getBorder(Object key) {
		Border tBorder = super.getBorder(key);
		return (tBorder != null) ? tBorder : UIManager.getBorder(key);
	}

	public Color getColor(Object key) {
		Color tColor = super.getColor(key);
		return (tColor != null) ? tColor : UIManager.getColor(key);
	}

	protected Insets getDefaultInsets() {
		return new Insets(2, 2, 2, 2);
	}

	private Object[] getDefaults() {
		Font textFieldFont = getFont(TEXTFIELD_FONT);
		Object[] defaults = {
		};

		return defaults;
	}

	public Font getFont(Object key) {
		Font tFont = super.getFont(key);
		return (tFont != null) ? tFont : UIManager.getFont(key);
	}

	public Icon getIcon(Object key) {
		Icon tIcon = super.getIcon(key);
		return (tIcon != null) ? tIcon : UIManager.getIcon(key);
	}

	public Insets getInsets(Object key) {
		Insets tInsets = (Insets) super.get(key);
		return (tInsets != null) ? tInsets : getDefaultInsets();
	}

	public int getInt(Object key) {
		Object value = super.get(key);
		return (value instanceof Integer) ? ((Integer) value).intValue() : UIManager.getInt(key);
	}

	public String getString(Object key) {
		String tString = super.getString(key);
		return (tString != null) ? tString : UIManager.getString(key);
	}

	/**
	 * Similar to getDefaults, except that the changes here affect the standard
	 * Swing L&F system, not only Lasse's almost-but-slower equivalent. That is
	 * this is used to modify the defaults held by UIManager, which Lasse's
	 * mechanisms fall back upon when nothing is overridden.
	 *
	 * Also this method is static and only run once in each VM
	 * (at class loading, so it could be more often if class GC is enabled.)
	 * This method of invocation works since every CoUserInterface subclass
	 * creates an instance of this class. In my opinion, though, this
	 * builder/default creation is wasteful. It will most likely be better
	 * to move to a L&F or -- more likely -- theme implementation, which
	 * could probably handle most cases so the prepare methods in the builder
	 * wouldn't have to do so much.
	 *
	 * @author Markus Persson 2000-08-03
	 */
	private static final Object[] getSwingDefaults() {
		Object[] defaults = {
		};

		return defaults;
	}

	private void initializeDefaults() {
		putDefaults(getDefaults());
	}

	/**
	 * Statically initialize the UIManager state.
	 *
	 * @see #getSwingDefaults()
	 * @author Markus Persson 2000-08-03
	 */
	public static final void initializeSwingDefaults() {
		UIManager.getDefaults().putDefaults(getSwingDefaults());
	}
}