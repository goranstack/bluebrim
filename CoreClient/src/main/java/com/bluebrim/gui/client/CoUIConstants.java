package com.bluebrim.gui.client;

import java.awt.Color;
import java.awt.Font;

import javax.swing.UIManager;

/**
 * Constants used in <code>CoUIDefaults</code>, i.e string constants used to define 
 * visual properties in the userinterface widgets. 
 * Also contains numeruous constants used in common userinterface classes.
 */
public interface CoUIConstants {
	// NOTE: "public static final" is implicit in interfaces. /Markus

	// Strings
	String TRANSPARENT_WIDGETS = "transparent_widgets";

//	com.bluebrim.xmlstorage.impl.server.shared.Resource keys

	String MESSAGE = "message";
	String BROADCAST_MESSAGE = "broadcast_message";

	String ADD_ITEM_MNEMONIC = "add_item_mnemonic";

	String FILE_MENU = "file_menu";
	String FILE_MENU_MNEMONIC = "file_menu_mnemonic";
	String HELP_MENU = "help_menu";
	String SAVE_AS = "save_as";
	String SAVE_AS_ITEM = "save_as_item";
	String SAVE_WORKSHEET = "save_worksheet";

	String DEFAULT_NUMERICAL_MACROS = "default_numerical_macros";
	String DEFAULT_ALPHA_NUMERICAL_MACROS = "default_alpha_numerical_macros";
	String EDIT = "EDIT";
	String ARCHIVE = "ARCHIVE";
	String CANCEL = "CANCEL";
	String OK = "OK";
	String CONTINUE = "CONTINUE";
	String CANCEL_MNEMONIC = "CANCEL_MNEMONIC";
	String OK_MNEMONIC = "OK_MNEMONIC";
	String READY = "READY";
	String READY_MNEMONIC = "READY_MNEMONIC";
	String SAVE_CHANGES = "SAVE_CHANGES";
	String YES = "YES";
	String YES_MNEMONIC = "YES_MNEMONIC";
	String NO = "NO";
	String NO_MNEMONIC = "NO_MNEMONIC";
	String NOT_EDITABLE = "NOT_EDITABLE";
	String DATE = "DATE";
	String TIMESTAMP = "TIMESTAMP";
	String NUMBER = "NUMBER";
	String CURRENCY = "CURRENCY";
	String PERCENT = "PERCENT";
	String CONVERTER_PARSE_ERROR = "CONVERTER_PARSE_ERROR";
	String CLEAR = "clear";
	String CLEAR_MNEMONIC = "clear_mnemonic";
	String REMOVE_ELEMENT_MESSAGE = "remove_element_message";
	String RENAME = "rename";


	String ACCEPT_ALL_FILES = "ACCEPT_ALL_FILES";
	String SAVE = "SAVE";
	String SAVE_MNEMONIC = "save_mnemonic";
	String OPEN = "OPEN";
	String UPDATE = "UPDATE";
	String HELP = "HELP";
	String ABORT_FILE_CHOOSER_DIALOG = "ABORT_FILE_CHOOSER_DIALOG";
	String SAVE_SELECTED_FILE = "SAVE_SELECTED_FILE";
	String OPEN_SELECTED_FILE = "OPEN_SELECTED_FILE";
	String UPDATE_DIRECTORY_LISTINGS = "UPDATE_DIRECTORY_LISTINGS";
	String FILECHOOSER_HELP = "FILECHOOSER_HELP";
	String LOOK_IN = "LOOK_IN";
	String FILE_NAME = "FILE_NAME";
	String FILES_OF_TYPE = "FILES_OF_TYPE";
	String UP_ONE_LEVEL = "UP_ONE_LEVEL";
	String NEW_FOLDER = "NEW_FOLDER";
	String LIST = "LIST";
	String DETAILED_LIST = "DETAILED_LIST";
	String HOME = "HOME";

	// Worksheet
	String WORKSHEET = "worksheet";
	String UNTITLED_WORKSHEET = "untitled_worksheet";
	String OPEN_COMPONENT_FRAME = "open_component_frame";
	String CLOSE_COMPONENT_FRAME = "close_component_frame";
	String OPEN_QUERY_FRAME = "open_query_frame";
	String CLOSE_QUERY_FRAME = "close_query_frame";

	String LAYOUT_MENU = "layout_menu";
	String ADJUST_SIZE = "adjust_size";
	String FRAME_STYLE = "frame_style";
	String PALETTE_STYLE = "palette_style";
	String ARRANGE_FRAMES = "arrange_frames";
	String PACK_FRAMES = "pack_frames";

	// Misc UI
	String MOVE_UP = "move_up";
	String MOVE_DOWN = "move_down";
	String NONE_CHOSEN = "none_chosen";

	// CoEnumeration
	String ENUMERATED_VALUES = "enumerated_values";

	// Fonts

	Font COURIER_9 = new Font("courier", Font.PLAIN, 9);
	Font COURIER_10 = new Font("courier", Font.PLAIN, 10);
	Font COURIER_12 = new Font("courier", Font.PLAIN, 12);

	Font HELVETICA_9 = new Font("helvetica", Font.PLAIN, 9);
	Font HELVETICA_10 = new Font("helvetica", Font.PLAIN, 10);
	Font HELVETICA_12 = new Font("helvetica", Font.PLAIN, 12);

	Font HELVETICA_9_BOLD = new Font("helvetica", Font.BOLD, 9);
	Font HELVETICA_10_BOLD = new Font("helvetica", Font.BOLD, 10);
	Font HELVETICA_12_BOLD = new Font("helvetica", Font.BOLD, 12);
	Font HELVETICA_14_BOLD = new Font("helvetica", Font.BOLD, 14);
	Font HELVETICA_16_BOLD = new Font("helvetica", Font.BOLD, 16);
	Font HELVETICA_18_BOLD = new Font("helvetica", Font.BOLD, 18);
	Font HELVETICA_20_BOLD = new Font("helvetica", Font.BOLD, 20);
	Font HELVETICA_22_BOLD = new Font("helvetica", Font.BOLD, 22);
	Font HELVETICA_24_BOLD = new Font("helvetica", Font.BOLD, 24);
	Font HELVETICA_26_BOLD = new Font("helvetica", Font.BOLD, 26);
	Font HELVETICA_28_BOLD = new Font("helvetica", Font.BOLD, 28);
	Font HELVETICA_30_BOLD = new Font("helvetica", Font.BOLD, 30);
	Font HELVETICA_32_BOLD = new Font("helvetica", Font.BOLD, 32);
	Font HELVETICA_34_BOLD = new Font("helvetica", Font.BOLD, 34);
	Font HELVETICA_36_BOLD = new Font("helvetica", Font.BOLD, 36);

	Font GARAMOND_12_LIGHT = new Font("garamond_light_condensed", Font.PLAIN, 12);
	Font GARAMOND_14_LIGHT = new Font("garamond_light_condensed", Font.PLAIN, 14);
	Font GARAMOND_16_LIGHT = new Font("garamond_light_condensed", Font.PLAIN, 16);
	Font GARAMOND_18_LIGHT = new Font("garamond_light_condensed", Font.PLAIN, 18);
	Font GARAMOND_24_LIGHT = new Font("garamond_light_condensed", Font.PLAIN, 24);
	Font GARAMOND_28_LIGHT = new Font("garamond_light_condensed", Font.PLAIN, 28);
	Font GARAMOND_32_LIGHT = new Font("garamond_light_condensed", Font.PLAIN, 32);
	Font GARAMOND_36_LIGHT = new Font("garamond_light_condensed", Font.PLAIN, 36);
	Font GARAMOND_48_LIGHT = new Font("garamond_light_condensed", Font.PLAIN, 48);

	//	Color and font for the formStyledTextFieldLabel()
	Font ARIAL_10 = new Font("arial", Font.PLAIN, 10);

	Color LABEL_BORDER_COLOR = UIManager.getColor("controlDarkShadow");
	Color LABEL_DARK_BLUE = UIManager.getColor("controlDarkShadow");
	Color LABEL_LIGHT_BLUE = UIManager.getColor("controlShadow");

	//	Color 
	Color VERY_LIGHT_GRAY = new Color(245, 245, 245);
	Color LIGHT_GRAY = Color.lightGray;
	Color GRAY = Color.gray;
	Color DARK_GRAY = Color.darkGray;
	Color BLUEBRIM_YELLOW = new Color(252, 228, 172);
	//	Color BLUEBRIM_YELLOW	= new Color(254, 230, 170);

	String FOCUS_COLOR = "focus_color";

	// Buttons
	String BUTTON_FONT = "Button.font";
	String BUTTON_BACKGROUND = "Button.background";
	String BUTTON_FOREGROUND = "Button.foreground";
	String BUTTON_BORDER = "Button.border";
	String BUTTON_MARGIN = "Button.margin";
	String BUTTON_TEXT_ICON_GAP = "Button.textIconGap";
	String BUTTON_TEXT_SHIFT_OFFSET = "Button.textShiftOffset";
	String BUTTON_FOCUS = "Button.focus";

	String TOGGLEBUTTON_FONT = "ToggleButton.font";
	String TOGGLEBUTTON_BACKGROUND = "ToggleButton.background";
	String TOGGLEBUTTON_FOREGROUND = "ToggleButton.foreground";
	String TOGGLEBUTTON_BORDER = "ToggleButton.border";
	String TOGGLEBUTTON_MARGIN = "ToggleButton.margin";
	String TOGGLEBUTTON_TEXT_ICON_GAP = "ToggleButton.textIconGap";
	String TOGGLEBUTTON_TEXT_SHIFT_OFFEST = "ToggleButton.textShiftOffset";
	String TOGGLEBUTTON_FOCUS = "ToggleButton.focus";

	String RADIOBUTTON_FONT = "RadioButton.font";
	String RADIOBUTTON_BACKGROUND = "RadioButton.background";
	String RADIOBUTTON_FOREGROUND = "RadioButton.foreground";
	String RADIOBUTTON_BORDER = "RadioButton.border";
	String RADIOBUTTON_MARGIN = "RadioButton.margin";
	String RADIOBUTTON_TEXT_ICON_GAP = "RadioButton.textIconGap";
	String RADIOBUTTON_TEXT_SHIFT_OFFEST = "RadioButton.textShiftOffset";
	String RADIOBUTTON_ICON = "RadioButton.icon";
	String RADIOBUTTON_FOCUS = "RadioButton.focus";

	String CHECKBOX_FONT = "CheckBox.font";
	String CHECKBOX_BACKGROUND = "CheckBox.background";
	String CHECKBOX_FOREGROUND = "CheckBox.foreground";
	String CHECKBOX_BORDER = "CheckBox.border";
	String CHECKBOX_MARGIN = "CheckBox.margin";
	String CHECKBOX_TEXT_ICON_GAP = "CheckBox.textIconGap";
	String CHECKBOX_TEXT_SHIFT_OFFEST = "CheckBox.textShiftOffset";
	String CHECKBOX_ICON = "CheckBox.icon";
	String CHECKBOX_FOCUS = "CheckBox.focus";

	// ComboBox
	String COMBOBOX_FONT = "ComboBox.font";
	String COMBOBOX_BACKGROUND = "ComboBox.background";
	String COMBOBOX_FOREGROUND = "ComboBox.foreground";
	String COMBOBOX_SELECTION_BACKGROUND = "ComboBox.selectionBackground";
	String COMBOBOX_SELECTION_FOREGROUND = "ComboBox.selectionForeground";
	String COMBOBOX_DISABLED_BACKGROUND = "ComboBox.disabledBackground";
	String COMBOBOX_DISABLED_FOREGROUND = "ComboBox.disabledForeground";

	// CoOptionMenu
	String OPTION_MENU_FONT = COMBOBOX_FONT;
	String OPTION_MENU_BACKGROUND = COMBOBOX_BACKGROUND;
	String OPTION_MENU_FOREGROUND = COMBOBOX_FOREGROUND;

	/* FileChooser
	
	"FileChooser.cancelButtonMnemonic";
	"FileChooser.saveButtonMnemonic";
	"FileChooser.openButtonMnemonic";
	"FileChooser.updateButtonMnemonic";
	"FileChooser.helpButtonMnemonic";
	
	"FileChooser.newFolderIcon";
	"FileChooser.upFolderIcon";
	"FileChooser.homeFolderIcon";
	"FileChooser.detailsViewIcon";
	"FileChooser.listViewIcon";
	
	"FileView.directoryIcon";
	"FileView.fileIcon";
	"FileView.computerIcon";
	"FileView.hardDriveIcon";
	"FileView.floppyDriveIcon";
	
	
	"DesktopIcon.border";
	
	*/
	// Internal frame
	String INTERNALFRAME_TITLEFONT = "InternalFrame.titleFont";
	String INTERNALFRAME_BORDER = "InternalFrame.border";
	String INTERNALFRAME_ICON = "InternalFrame.icon";
	String INTERNALFRAME_PALETTE_BORDER = "InternalFrame.paletteBorder";
	String INTERNALFRAME_PALETTE_TITLE_HEIGHT = "InternalFrame.paletteTitleHeight";

	/* Default frame icons are undefined for Basic. 
	"InternalFrame.maximizeIcon";
	"InternalFrame.minimizeIcon";
	"InternalFrame.iconifyIcon";
	"InternalFrame.closeIcon";
	
	"InternalFrame.activeTitleBackground";
	"InternalFrame.activeTitleForeground";
	"InternalFrame.inactiveTitleBackground";
	"InternalFrame.inactiveTitleForeground";
	*/

	// Desktop
	String DESKTOP_BACKGROUND = "Desktop.background";

	// Label
	String LABEL_FONT = "Label.font";
	String LABEL_BACKGROUND = "Label.background";
	String LABEL_FOREGROUND = "Label.foreground";
	String LABEL_DISABLED_FOREGROUND = "Label.disabledForeground";
	String LABEL_DISABLED_SHADOW = "Label.disabledShadow";
	String LABEL_BORDER = "Label.border";

	// List
	String LIST_FONT = "List.font";
	String LIST_BACKGROUND = "List.background";
	String LIST_FOREGROUND = "List.foreground";
	String LIST_SELECTION_BACKGROUND = "List.selectionBackground";
	String LIST_SELECTION_FOREGROUND = "List.selectionForeground";
	String LIST_FOCUS_CELL_HIGHTIGHT_BORDER = "List.focusCellHighlightBorder";
	String LIST_BORDER = "List.border";
	String LIST_CELL_RENDERER = "List.cellRenderer";

	// Menus
	String MENUBAR_FONT = "MenuBar.font";
	String MENUBAR_BACKGROUND = "MenuBar.background";
	String MENUBAR_FOREGROUND = "MenuBar.foreground";
	String MENUBAR_BORDER = "MenuBar.border";

	String MENUITEM_FONT = "MenuItem.font";
	String MENUITEM_ACCELERATOR_FONT = "MenuItem.acceleratorFont";
	String MENUITEM_BACKGROUND = "MenuItem.background";
	String MENUITEM_FOREGROUND = "MenuItem.foreground";
	String MENUITEM_SELECTION_BACKGROUND = "MenuItem.selectionForeground";
	String MENUITEM_SELECTION_FOREGROUND = "MenuItem.selectionBackground";
	String MENUITEM_DISABLED_FOREGROUND = "MenuItem.disabledForeground";
	String MENUITEM_ACCELERATOR_FOREGROUND = "MenuItem.acceleratorForeground";
	String MENUITEM_ACCELERATOR_BACKGROUND = "MenuItem.acceleratorSelectionForeground";
	String MENUITEM_BORDER = "MenuItem.border";
	String MENUITEM_BORDER_PAINTED = "MenuItem.borderPainted";
	String MENUITEM_MARGIN = "MenuItem.margin";
	String MENUITEM_CHECK_ICON = "MenuItem.checkIcon";
	String MENUITEM_ARROW_ICON = "MenuItem.arrowIcon";

	/* 
		"RadioButtonMenuItem.font";
		"RadioButtonMenuItem.acceleratorFont";
		"RadioButtonMenuItem.background";
		"RadioButtonMenuItem.foreground";
		"RadioButtonMenuItem.selectionForeground";
		"RadioButtonMenuItem.selectionBackground";
		"RadioButtonMenuItem.disabledForeground";
		"RadioButtonMenuItem.acceleratorForeground";
		"RadioButtonMenuItem.acceleratorSelectionForeground";
		"RadioButtonMenuItem.border";
		"RadioButtonMenuItem.borderPainted";
		"RadioButtonMenuItem.margin"; 2; 2),
		"RadioButtonMenuItem.checkIcon";
		"RadioButtonMenuItem.arrowIcon";
	
		"CheckBoxMenuItem.font";
		"CheckBoxMenuItem.acceleratorFont";
		"CheckBoxMenuItem.background";
		"CheckBoxMenuItem.foreground";
		"CheckBoxMenuItem.selectionForeground";
		"CheckBoxMenuItem.selectionBackground";
		"CheckBoxMenuItem.disabledForeground";
		"CheckBoxMenuItem.acceleratorForeground";
		"CheckBoxMenuItem.acceleratorSelectionForeground";
		"CheckBoxMenuItem.border";
		"CheckBoxMenuItem.borderPainted";
		"CheckBoxMenuItem.margin"; 2; 2),
		"CheckBoxMenuItem.checkIcon";
		"CheckBoxMenuItem.arrowIcon";
	*/

	String MENU_FONT = "Menu.font";
	String MENU_ACCELERATOR_FONT = "Menu.acceleratorFont";
	String MENU_BACKGROUND = "Menu.background";
	String MENU_FOREGROUND = "Menu.foreground";
	String MENU_SELECTION_BACKGROUND = "Menu.selectionForeground";
	String MENU_SELECTION_FOREGROUND = "Menu.selectionBackground";
	String MENU_DISABLED_FOREGROUND = "Menu.disabledForeground";
	String MENU_ACCELERATOR_FOREGROUND = "Menu.acceleratorForeground";
	String MENU_ACCELERATOR_BACKGROUND = "Menu.acceleratorSelectionForeground";
	String MENU_BORDER = "Menu.border";
	String MENU_BORDER_PAINTED = "Menu.borderPainted";
	String MENU_MARGIN = "Menu.margin";
	String MENU_CHECK_ICON = "Menu.checkIcon";
	String MENU_ARROW_ICON = "Menu.arrowIcon";
	String MENU_CONSUMES_TABS = "Menu.consumesTabs";

	String POPUPMENU_FONT = "PopupMenu.font";
	String POPUPMENU_BACKGROUND = "PopupMenu.background";
	String POPUPMENU_FOREGROUND = "PopupMenu.foreground";
	String POPUPMENU_BORDER = "PopupMenu.border";

	// OptionPane
	String OPTION_PANE_FONT = "OptionPane.font";
	String OPTION_PANE_BACKGROUND = "OptionPane.background";
	String OPTION_PANE_FOREGROUND = "OptionPane.foreground";
	String OPTION_PANE_MESSAGE_FOREGROUND = "OptionPane.messageForeground";
	String OPTION_PANE_BORDER = "OptionPane.border";
	String OPTION_PANE_MESSAGE_AREA_BORDER = "OptionPane.messageAreaBorder";
	String OPTION_PANE_BUTTON_AREA_BORDER = "OptionPane.buttonAreaBorder";
	String OPTION_PANE_MINIMUMSIZE = "OptionPane.minimumSize";
	String OPTION_PANE_ERROR_ICON = "OptionPane.errorIcon";
	String OPTION_PANE_INFORMATION_ICON = "OptionPane.informationIcon";
	String OPTION_PANE_WARNING_ICON = "OptionPane.warningIcon";
	String OPTION_PANE_QUESTION_ICON = "OptionPane.questionIcon";

	// Panel
	String PANEL_FONT = "Panel.font";
	String PANEL_BACKGROUND = "Panel.background";
	String PANEL_FOREGROUND = "Panel.foreground";

	/* ProgressBar
	"ProgressBar.font";
	"ProgressBar.foreground";
	"ProgressBar.background";
	"ProgressBar.selectionForeground";
	"ProgressBar.selectionBackground";
	"ProgressBar.border";
	"ProgressBar.cellLength";
	"ProgressBar.cellSpacing";
	*/

	// Separator
	String SEPARATOR_SHADOW = "Separator.shadow";
	String SEPARATOR_HIGHLIGHT = "Separator.highlight";

	/*** ScrollBar/ScrollPane/Viewport
	"ScrollBar.background";
	"ScrollBar.foreground";
	"ScrollBar.track";
	"ScrollBar.trackHighlight";
	"ScrollBar.thumb";
	"ScrollBar.thumbHighlight";
	"ScrollBar.thumbDarkShadow";
	"ScrollBar.thumbLightShadow";
	"ScrollBar.border";
	"ScrollBar.minimumThumbSize";
	"ScrollBar.maximumThumbSize";
	
	"ScrollPane.font";
	"ScrollPane.background";
	"ScrollPane.foreground";
	"ScrollPane.border";
	"ScrollPane.viewportBorder";
	
	"Viewport.font";
	"Viewport.background";
	"Viewport.foreground";
	
	
	// *** Slider
	"Slider.foreground";
	"Slider.background";
	"Slider.highlight";
	"Slider.shadow";
	"Slider.focus";
	"Slider.border";
	"Slider.focusInsets";
	*/

	// SplitPane
	String SPLITPANE_BACKGROUND = "SplitPane.background"; //Color
	String SPLITPANE_HIGHTLOIGHT = "SplitPane.highlight"; //Color
	String SPLITPANE_SHADOW = "SplitPane.shadow"; //Color
	String SPLITPANE_BORDER = "SplitPane.border"; //Border
	String SPLITPANE_DIVIDER_SIZE = "SplitPane.dividerSize"; //Integer

	// TabbedPane
	String TABBEDPANE_FONT = "TabbedPane.font"; //Font
	String TABBEDPANE_BACKGROUND = "TabbedPane.background"; //Color
	String TABBEDPANE_FOREGROUND = "TabbedPane.foreground"; //Color
	String TABBEDPANE_LIGHT_HIGHLIGHT = "TabbedPane.lightHighlight"; //Color
	String TABBEDPANE_HIGHLIGHT = "TabbedPane.highlight"; //Color
	String TABBEDPANE_SHADOW = "TabbedPane.shadow"; //Color
	String TABBEDPANE_DARK_SHADOW = "TabbedPane.darkShadow"; //Color
	String TABBEDPANE_FOCUS = "TabbedPane.focus"; //Color
	String TABBEDPANE_TEXT_ICON_GAP = "TabbedPane.textIconGap"; //Integer
	String TABBEDPANE_TAB_INSETS = "TabbedPane.tabInsets"; //Insets
	String TABBEDPANE_SELECTED_TAB_PAD_INSETS = "TabbedPane.selectedTabPadInsets"; //Insets
	String TABBEDPANE_TAB_AREA_INSETS = "TabbedPane.tabAreaInsets"; //Insets
	String TABBEDPANE_CONTENT_BORDER_INSETS = "TabbedPane.contentBorderInsets"; //Insets
	String TABBEDPANE_TAB_RUN_OVERLAY = "TabbedPane.tabRunOverlay"; //Integer

	String TABBEDPANE_TAB_AREA_BACKGROUND = "TabbedPane.tabAreaBackground"; //Color
	String TABBEDPANE_SELECTED = "TabbedPane.selected"; //Color
	String TABBEDPANE_SELECT_HIGHTLIGHT = "TabbedPane.selectHighlight"; //Color

	// Table
	String TABLE_FONT = "Table.font";
	String TABLE_FOREGROUND = "Table.foreground"; // cell text color
	String TABLE_BACKGROUND = "Table.background"; // cell background color
	String TABLE_SELECTION_FOREGROUND = "Table.selectionForeground";
	String TABLE_SELECTION_BACKGROUND = "Table.selectionBackground";
	String TABLE_GRID_COLOR = "Table.gridColor"; // grid line color
	String TABLE_FOCUS_CELL_BACKGROUND = "Table.focusCellBackground";
	String TABLE_FOCUS_CELL_FOREGROUND = "Table.focusCellForeground";
	String TABLE_FOCUS_CELL_HIGHTLIGHT_BORDER = "Table.focusCellHighlightBorder";
	String TABLE_SCROLLPANE_BORDER = "Table.scrollPaneBorder";

	String TABLEHEADER_FONT = "TableHeader.font";
	String TABLEHEADER_FOREGROUND = "TableHeader.foreground"; // header text color
	String TABLEHEADER_BACKGROUND = "TableHeader.background"; // header background
	String TABLEHEADER_CELL_BORDER = "TableHeader.cellBorder";

	// Text
	String TEXTFIELD_FONT = "TextField.font";
	String TEXTFIELD_BACKGROUND = "TextField.background";
	String TEXTFIELD_FOREGROUND = "TextField.foreground";
	String TEXTFIELD_INACTIVE_FOREGROUND = "TextField.inactiveForeground";
	String TEXTFIELD_SELECTION_BACKGROUND = "TextField.selectionBackground";
	String TEXTFIELD_SELECTION_FOREGROUND = "TextField.selectionForeground";
	String TEXTFIELD_CARET_FOREGROUND = "TextField.caretForeground";
	String TEXTFIELD_CARET_BLINKRATE = "TextField.caretBlinkRate";
	String TEXTFIELD_BORDER = "TextField.border";
	String TEXTFIELD_MARGIN = "TextField.margin";
	String TEXTFIELD_KEY_BINDINGS = "TextField.keyBindings";
	/*
		"PasswordField.font";
		"PasswordField.background";
		"PasswordField.foreground";
		"PasswordField.inactiveForeground";
		"PasswordField.selectionBackground";
		"PasswordField.selectionForeground";
		"PasswordField.caretForeground";
		"PasswordField.caretBlinkRate";
		"PasswordField.border";
			"PasswordField.margin";
		"PasswordField.keyBindings";
	*/
	String TEXTAREA_FONT = "TextArea.font";
	String TEXTAREA_BACKGROUND = "TextArea.background";
	String TEXTAREA_FOREGROUND = "TextArea.foreground";
	String TEXTAREA_INACTIVE_FOREGROUND = "TextArea.inactiveForeground";
	String TEXTAREA_SELECTION_BACKGROUND = "TextArea.selectionBackground";
	String TEXTAREA_SELECTION_FOREGROUND = "TextArea.selectionForeground";
	String TEXTAREA_CARET_FOREGROUND = "TextArea.caretForeground";
	String TEXTAREA_CARET_BLINKRATE = "TextArea.caretBlinkRate";
	String TEXTAREA_BORDER = "TextArea.border";
	String TEXTAREA_MARGIN = "TextArea.margin";
	String TEXTAREA_KEY_BINDINGS = "TextArea.keyBindings";
	/*
		"TextPane.font";
		"TextPane.background";
		"TextPane.foreground";
		"TextPane.selectionBackground";
		"TextPane.selectionForeground";
		"TextPane.caretForeground";
		"TextPane.caretBlinkRate";
		"TextPane.inactiveForeground";
		"TextPane.border";
		"TextPane.margin";
		"TextPane.keyBindings";
	
		"EditorPane.font";
		"EditorPane.background";
		"EditorPane.foreground";
		"EditorPane.selectionBackground";
		"EditorPane.selectionForeground";
		"EditorPane.caretForeground";
		"EditorPane.caretBlinkRate";
		"EditorPane.inactiveForeground";
		"EditorPane.border";
		"EditorPane.margin";
		"EditorPane.keyBindings";
	*/
	
	// TitledBorder
	String TITLEDBORDER_FONT = "TitledBorder.font";
	String TITLEDBORDER_TITLE_COLOR = "TitledBorder.titleColor";
	String TITLEDBORDER_BORDER = "TitledBorder.border";

	// ToolBar
	String TOOLBAR_FONT = "ToolBar.font";
	String TOOLBAR_BACKGROUND = "ToolBar.background";
	String TOOLBAR_FOREGROUND = "ToolBar.foreground";
	String TOOLBAR_DOCKING_BACKGROUND = "ToolBar.dockingBackground";
	String TOOLBAR_DOCKING_FOREGROUND = "ToolBar.dockingForeground";
	String TOOLBAR_FLOATING_BACKGROUND = "ToolBar.floatingBackground";
	String TOOLBAR_FLOATING_FOREGROUND = "ToolBar.floatingForeground";
	String TOOLBAR_BORDER = "ToolBar.border";
	String TOOLBAR_SEPARATOR_SIZE = "ToolBar.separatorSize";

	// ToolTips
	String TOOLTIP_FONT = "ToolTip.font";
	String TOOLTIP_BACKGROUND = "ToolTip.background";
	String TOOLTIP_FOREGROUND = "ToolTip.foreground";
	String TOOLTIP_BORDER = "ToolTip.border";

	// Tree
	String TREE_FONT = "Tree.font";
	String TREE_BACKGROUND = "Tree.background";
	String TREE_FOREGROUND = "Tree.foreground";
	String TREE_HASH = "Tree.hash";
	String TREE_TEXT_FOREGROUND = "Tree.textForeground";
	String TREE_TEXT_BACKGROUND = "Tree.textBackground";
	String TREE_SELECTION_FOREGROUND = "Tree.selectionForeground";
	String TREE_SELECTION_BACKGROUND = "Tree.selectionBackground";
	String TREE_SELECTION_BORDER_COLOR = "Tree.selectionBorderColor";
	String TREE_EDITOR_BORDER = "Tree.editorBorder";
	String TREE_LEFT_CHILD_INDENT = "Tree.leftChildIndent";
	String TREE_RIGHT_CHILD_INDENT = "Tree.rightChildIndent";
	String TREE_ROW_HEIGHT = "Tree.rowHeight";
	String TREE_SCROLLS_ON_EXPAND = "Tree.scrollsOnExpand";
	String TREE_OPEN_ICON = "Tree.openIcon";
	String TREE_CLOSED_ICON = "Tree.closedIcon";
	String TREE_LEAF_ICON = "Tree.leafIcon";
	String TREE_EXPANDED_ICON = "Tree.expandedIcon";
	String TREE_COLLAPSED_ICON = "Tree.collapsedIcon";
	String TREE_CHANGE_SELECTION_WITH_FOCUS = "Tree.changeSelectionWithFocus";
	String TREE_DRAW_FICUS_BORDER_AROUND_ICON = "Tree.drawsFocusBorderAroundIcon";

	String SLIM_HEADLINE_FONT = "SlimHeadlineLabel.font";
	String HEADLINE_FONT = "HeadlineLabel.font";
	String HEADLINE_COLOR = "HeadlineLabel.color";

	// Labeled textfield
	String LABELED_TEXT_FIELD_FONT = "LabeledTextField.font";
	String LABELED_TEXT_FIELD_BACKGROUND = "LabeledTextField.background";
	String LABELED_TEXT_FIELD_FOREGROUND = "LabeledTextField.foreground";
	String LABELED_TEXT_FIELD_BORDER = "LabeledTextField.border";

	String FROM_DATE = "from_date";
	// Time Period
	String CANCEL_BUTTON = "cancel_button";
	String OK_BUTTON = "ok_button";
	String TIME_PERIOD = "time_period";
	String TIME_PERIOD_FROM_DATE = "time_period_from_date";
	String TIME_PERIOD_TO_DATE = "time_period_to_date";
	String TIME_PERIOD_DELIMITER = "time_period_delimiter";

	// Bookmark
	String BOOKMARK_NAME = "bookmark_name";
	String BOOKMARK = "bookmark";
	String SAVE_IN_FOLDER = "save_in_folder";
	String SAVE_AS_BOOKMARK = "save_as_bookmark";
	String OPEN_BOOKMARK = "open_bookmark";
	String SAVE_IN_EXISTING = "save_in_existing";
	String SAVE_IN_NEW = "save_in_new";
	String FOLDER_NAME = "folder name";

	// CoFolderViewUI
	String FOLDER_LIST = "folder_list";

	// Menus
	String PRINT = "Print ...";
	String PREVIEW_PRINT = "Preview print ...";
	String PRINT_OPTIONS = "Print options ...";

	// CoPrintTableOptionUI
	String LANDSCAPE = "landscape";
	String TABLE_STYLE = "table_style";
	String PREVIEW_IS_MISSING = "preview_is_missing";
	String COLUMN_PRINT_PREVIEW = "Column print preview";
	String PAPER_PRINT_PREVIEW = "Paper print preview";
	String PAPER_MARGIN = "Paper margin";
	
	String SAVE_AS_XML = "save_as_xml";
	String LOAD_FROM_XML = "load_from_xml";
	String UNKNOWN = "unknown";

}