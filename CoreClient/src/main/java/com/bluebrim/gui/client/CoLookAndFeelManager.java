package com.bluebrim.gui.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

/**
 * Global manager on the client side, responsible
 * for handling and dispatching changes of
 * the current L&F.
 * <br>
 * Have some static utility methods to be used when
 * changing the L&F. Also has support to build a
 * menu so the user can choose L&F.If this is the case
 * a singleton must be created by calling <code>create</code>
 * of this class or one of its subclasses.
 * <br>
 * @author Lasse S 2001-04-20
 */
public class CoLookAndFeelManager {
	private static CoLookAndFeelManager INSTANCE;

	protected boolean m_initialized = true;
	
	protected class ToggleLookAndFeelListener implements ItemListener {

		public void itemStateChanged(ItemEvent e) {
			JRadioButtonMenuItem rb = (JRadioButtonMenuItem) e.getSource();
			if (rb.isSelected())
				handleLookAndFeelSelection(rb.getText());

		}

	}

	protected static void setLookAndFeelManager(CoLookAndFeelManager mgr) {
		INSTANCE = mgr;
	}

	protected CoLookAndFeelManager() {
	}

	private boolean _initialized() {
		return m_initialized;
	}

	private static void addIfMissing(UIDefaults defaults, String key, Object value) {
		Object resource = defaults.get(key);
		if (resource == null)
			defaults.put(key, value);
	}

	private void addToMenu(JComponent menu) {
		addToMenu(menu, new ButtonGroup(), new ToggleLookAndFeelListener());
	}

	protected void addToMenu(JComponent menu, ButtonGroup group, ToggleLookAndFeelListener toggleLookAndFeelListener) {

		JRadioButtonMenuItem windowsMenuItem = (JRadioButtonMenuItem) menu.add(new JRadioButtonMenuItem("Windows"));
		windowsMenuItem.setSelected(UIManager.getLookAndFeel().getName().equals("Windows"));
		group.add(windowsMenuItem);
		windowsMenuItem.addItemListener(toggleLookAndFeelListener);

		JRadioButtonMenuItem metalMenuItem = (JRadioButtonMenuItem) menu.add(new JRadioButtonMenuItem("Java"));
		metalMenuItem.setSelected(UIManager.getLookAndFeel().getName().equals("Metal"));
		group.add(metalMenuItem);
		metalMenuItem.addItemListener(toggleLookAndFeelListener);

	}

	public static void buildLookAndFeelMenu(JComponent menu) {
		if (!isInitialized())
			return;
		INSTANCE.addToMenu(menu);
	}

	public static void create() {
		setLookAndFeelManager(new CoLookAndFeelManager());
	}

	protected void handleLookAndFeelSelection(String selectedItem) {
		if (selectedItem.equals("Windows")) {
			setLookAndFeel(new WindowsLookAndFeel());
		} else if (selectedItem.equals("Java")) {
			setLookAndFeel(new MetalLookAndFeel());
		}

	}

	public static boolean isInitialized() {
		return INSTANCE != null && INSTANCE._initialized();
	}

	private static void postInitializeUIDefaults(UIDefaults defaults) {
		addIfMissing(defaults, CoUIConstants.FOCUS_COLOR, Color.darkGray);
		addIfMissing(defaults, CoUIConstants.HEADLINE_COLOR, Color.darkGray);

		Color focusColor = defaults.getColor(CoUIConstants.FOCUS_COLOR);
		addIfMissing(defaults, CoUIConstants.BUTTON_FOCUS, focusColor);
		addIfMissing(defaults, CoUIConstants.TOGGLEBUTTON_FOCUS, focusColor);
		addIfMissing(defaults, CoUIConstants.RADIOBUTTON_FOCUS, focusColor);
		addIfMissing(defaults, CoUIConstants.CHECKBOX_FOCUS, focusColor);

		addIfMissing(defaults, CoUIConstants.HEADLINE_FONT, CoUIConstants.GARAMOND_18_LIGHT);
		addIfMissing(defaults, CoUIConstants.SLIM_HEADLINE_FONT, CoUIConstants.GARAMOND_14_LIGHT);

		Font textFieldFont = defaults.getFont(CoUIConstants.TEXTFIELD_FONT);
		addIfMissing(defaults, CoUIConstants.LABELED_TEXT_FIELD_FONT, textFieldFont.deriveFont(textFieldFont.getSize() - 2.0f));
		addIfMissing(defaults, CoUIConstants.LABELED_TEXT_FIELD_FOREGROUND, UIManager.getColor(CoUIConstants.TEXTFIELD_FOREGROUND));
		addIfMissing(defaults, CoUIConstants.LABELED_TEXT_FIELD_BACKGROUND, defaults.getColor(CoUIConstants.PANEL_BACKGROUND));
		addIfMissing(defaults, CoUIConstants.LABELED_TEXT_FIELD_BORDER, BorderFactory.createLineBorder(Color.black));

		defaults.put("OptionPane.yesButtonText", CoUIStringResources.getName(CoUIConstants.YES));
		defaults.put("OptionPane.noButtonText", CoUIStringResources.getName(CoUIConstants.NO));
		defaults.put("OptionPane.cancelButtonText", CoUIStringResources.getName(CoUIConstants.CANCEL));
		defaults.put("OptionPane.okButtonText", CoUIStringResources.getName(CoUIConstants.OK));

		//FileChooser
		defaults.put("FileChooser.acceptAllFileFilterText", CoUIStringResources.getName(CoUIConstants.ACCEPT_ALL_FILES));
		defaults.put("FileChooser.cancelButtonText", CoUIStringResources.getName(CoUIConstants.CANCEL));
		defaults.put("FileChooser.saveButtonText", CoUIStringResources.getName(CoUIConstants.SAVE));
		defaults.put("FileChooser.openButtonText", CoUIStringResources.getName(CoUIConstants.OPEN));
		defaults.put("FileChooser.updateButtonText", CoUIStringResources.getName(CoUIConstants.UPDATE));
		defaults.put("FileChooser.helpButtonText", CoUIStringResources.getName(CoUIConstants.HELP));

		defaults.put("FileChooser.cancelButtonToolTipText", CoUIStringResources.getName(CoUIConstants.ABORT_FILE_CHOOSER_DIALOG));
		defaults.put("FileChooser.saveButtonToolTipText", CoUIStringResources.getName(CoUIConstants.SAVE_SELECTED_FILE));
		defaults.put("FileChooser.openButtonToolTipText", CoUIStringResources.getName(CoUIConstants.OPEN_SELECTED_FILE));
		defaults.put("FileChooser.updateButtonToolTipText", CoUIStringResources.getName(CoUIConstants.UPDATE_DIRECTORY_LISTINGS));
		defaults.put("FileChooser.helpButtonToolTipText", CoUIStringResources.getName(CoUIConstants.FILECHOOSER_HELP));

		defaults.put("FileChooser.lookInLabelText", CoUIStringResources.getName(CoUIConstants.LOOK_IN));
		defaults.put("FileChooser.fileNameLabelText", CoUIStringResources.getName(CoUIConstants.FILE_NAME));
		defaults.put("FileChooser.filesOfTypeLabelText", CoUIStringResources.getName(CoUIConstants.FILES_OF_TYPE));
		defaults.put("FileChooser.upFolderToolTipText", CoUIStringResources.getName(CoUIConstants.UP_ONE_LEVEL));
		defaults.put("FileChooser.homeFolderToolTipText", CoUIStringResources.getName(CoUIConstants.HOME));
		defaults.put("FileChooser.newFolderToolTipText", CoUIStringResources.getName(CoUIConstants.NEW_FOLDER));
		defaults.put("FileChooser.listViewButtonToolTipText", CoUIStringResources.getName(CoUIConstants.LIST));
		defaults.put("FileChooser.detailsViewButtonToolTipText", CoUIStringResources.getName(CoUIConstants.DETAILED_LIST));
/*
		defaults.put("ColorChooser.swatchesNameText", CoStrokeUIResources.getName("SWATCHES"));
		defaults.put("ColorChooser.swatchesRecentText", CoStrokeUIResources.getName("RECENT"));
		defaults.put("ColorChooser.background", CoUIConstants.VERY_LIGHT_GRAY);
		defaults.put("ColorChooser.foreground", CoUIConstants.VERY_LIGHT_GRAY);
		defaults.put("ColorChooser.rgbRedText", CoStrokeUIResources.getName("RED"));
		defaults.put("ColorChooser.rgbGreenText", CoStrokeUIResources.getName("GREEN"));
		defaults.put("ColorChooser.rgbBlueText", CoStrokeUIResources.getName("BLUE"));
		defaults.put("ColorChooser.previewText", CoStrokeUIResources.getName("PREVIEW"));
*/
	}

	public static void setLookAndFeel(LookAndFeel lf) {
		try {
			UIManager.setLookAndFeel(lf);
			postInitializeUIDefaults(UIManager.getDefaults());
			CoWindowList.updateWindows();
		} catch (UnsupportedLookAndFeelException exc) {
			System.err.println("Stödjer inte denna L&F " + lf.getName());

			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				CoWindowList.updateWindows();
			} catch (Exception exc2) {
				exc2.printStackTrace();
			}
		} catch (Exception exc) {
			System.err.println("Kunde inte ladda L&F " + lf.getName());
			exc.printStackTrace();
		}
	}
}