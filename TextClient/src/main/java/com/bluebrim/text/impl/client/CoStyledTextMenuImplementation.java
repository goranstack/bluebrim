package com.bluebrim.text.impl.client;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.text.AttributeSet;

import com.bluebrim.font.shared.CoFontAttribute;
import com.bluebrim.menus.client.CoCheckboxMenuItem;
import com.bluebrim.menus.client.CoMenuBuilder;
import com.bluebrim.menus.client.CoMenuItem;
import com.bluebrim.menus.client.CoSubMenu;
import com.bluebrim.menus.client.CoTriStateCheckBoxMenuItem;
import com.bluebrim.paint.impl.client.CoColorSampleIcon;
import com.bluebrim.paint.shared.CoColorIF;
import com.bluebrim.swing.client.CoButtonGroup;
import com.bluebrim.swing.client.CoUndoHandler;
import com.bluebrim.text.impl.client.actions.CoActionUtilities;
import com.bluebrim.text.impl.shared.CoEnumValue;
import com.bluebrim.text.impl.shared.CoStyleConstants;
import com.bluebrim.text.shared.CoStyledDocument;
import com.bluebrim.text.shared.CoStyledDocumentIF;
import com.bluebrim.text.shared.CoTagChainIF;
import com.bluebrim.text.shared.CoTextConstants;
import com.bluebrim.text.shared.CoTextEditorContextIF;

/*
 * Skapar en högerknappsmeny.
 * När användaren klickar med högerknappen så anropas updatePopupMenu
 * där menyalternativen uppdateras beroende på var i texten användaren
 * befinner sig.
 * För att skapa menyn krävs att de Actions som ska knytas
 * till de olika menyalternativen skickas in. En editor (CoAbstractTextPane),
 * CoStyleRuleIF, CoLocalCharacterStyleUI, CoLocalParagraphStyleUI och
 * CoUndoHandlerIF måste sättas för att menyn ska fungera fullt ut.
 */

 
public abstract class CoStyledTextMenuImplementation
{
	private CoAbstractTextEditor m_editor;
	private Map m_actions;

	private CoUndoHandler m_undoHandler;
	
	private CoCharacterStyleActionUI m_characterStyle;
	private CoParagraphStyleActionUI m_paragraphStyle;
	private CoTextMeasurementPrefsUI m_measurementPrefs;
	
	// references to menus and menuitems to update in attributeChanged
	private CoSubMenu m_paragraphTagMenu;
	private CoButtonGroup m_paragraphTagGroup;
	
	private CoSubMenu m_characterTagMenu;
	private CoButtonGroup m_characterTagGroup;
	
	private CoSubMenu m_tagChainMenu;
	private CoButtonGroup m_tagChainGroup;

	private CoMenuItem m_copyMenuItem;
	private CoMenuItem m_copyStyleMenuItem;
	private CoMenuItem m_cutMenuItem;
	private CoMenuItem m_pasteMenuItem;
	
	private CoMenuItem m_undoMenuItem;
	private CoMenuItem m_redoMenuItem;

	private CoSubMenu m_insertMenu;
	
	private CoSubMenu m_styleMenu;

	private CoSubMenu m_fontMenu;
	private CoButtonGroup m_fontGroup;

	private CoSubMenu m_fontSizeMenu;
	private CoButtonGroup m_fontSizeGroup;
	
	private CoSubMenu m_alignmentMenu;
	private CoButtonGroup m_alignmentGroup;

	private CoSubMenu m_underlineMenu;
	private CoButtonGroup m_underlineGroup;

	private CoSubMenu m_verticalPositionMenu;
	private CoButtonGroup m_verticalPositionGroup;
	
	private CoSubMenu m_colorMenu;
	private CoButtonGroup m_colorGroup;

	private CoSubMenu m_shadeMenu;
	private CoButtonGroup m_shadeGroup;

	private CoMenuItem m_clearCharacterStylesMenuItem;
	
	private CoMenuItem m_characterStylesMenuItem;
	private CoMenuItem m_paragraphStylesMenuItem;
	private CoMenuItem m_measurementPrefsMenuItem;


	private CoMenuBuilder m_builder;
	private CoMenuItem m_clearParagraphStylesMenuItem;




private void createAlignmentMenuItems()
{
	m_alignmentMenu = createMenu( getResourceString( CoTextConstants.ALIGNMENT));
	add( m_alignmentMenu );
	m_alignmentGroup = new CoButtonGroup();

	Action a = getAction(CoStyledEditorKit.alignmentParagraphAction);
	
	String name = getResourceString( "UNKNOWN" );
	JMenuItem mi = createCheckboxMenuItem( name );
	m_alignmentMenu.add( mi );
	mi.addActionListener( a );
	mi.setActionCommand( null );
	m_alignmentGroup.add(mi);

	CoEnumValue[] keys = new CoEnumValue[]
	{
		CoTextConstants.ALIGN_LEFT,
		CoTextConstants.ALIGN_CENTER,
		CoTextConstants.ALIGN_RIGHT,
		CoTextConstants.ALIGN_JUSTIFIED,
		CoTextConstants.ALIGN_FORCED
	};
	
	for
		( int i = 0; i < keys.length; i++ )
	{
		String key = keys[i].toString();
		name = getResourceString(key);
		mi = createCheckboxMenuItem( name );
		m_alignmentMenu.add( mi );
		mi.addActionListener( a );
		mi.setActionCommand(key);
		m_alignmentGroup.add(mi);
	}
}
private void createCharacterTagMenuItems ( Iterator tags )
{
	// build the CHARACTER TAG menu
	
	if (m_characterTagMenu == null) {
		m_characterTagMenu = createMenu(getResourceString(CoTextConstants.CHARACTER_TAG));
		add(m_characterTagMenu);
	}	else {
		m_characterTagMenu.removeAll();
	}
	
	if
		( m_characterTagsMenuItem == null )
	{
		// create character style dialog
		m_characterTagsMenuItem = createMenuItem( getResourceString( "CHARACTER_TAGS_DIALOG" ) );
		m_characterTagsMenuItem.addActionListener(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent e )
				{
					m_characterTag.open();
				}
			}
		);
	}

	m_characterTagMenu.add( m_characterTagsMenuItem );
	m_characterTagMenu.addSeparator();

	
	m_characterTagGroup = new CoButtonGroup();

	Action a = getAction(CoStyledEditorKit.tagCharacterAction);
 	
	// insert a check box menu item without a name for removing defined tag
	JMenuItem mi = createCheckboxMenuItem(CoStyledDocument.DEFAULT_TAG_NAME);
	mi.addActionListener(a);
	m_characterTagMenu.add(mi);
	m_characterTagGroup.add(mi);
	
	if
		( tags != null )
	{
		while
			( tags.hasNext() )
	 	{
			mi = createCheckboxMenuItem( (String) tags.next() );
			mi.addActionListener(a);
			m_characterTagMenu.add(mi);
			m_characterTagGroup.add(mi);
		}
	}
}

private void createColorMenuItems( Collection colors )
{
	if
		( m_colorMenu == null )
	{
		m_colorMenu = createMenu( getResourceString(CoTextConstants.FOREGROUND_COLOR) );
		add( m_colorMenu );
	} else {
		m_colorMenu.removeAll();
	}
		
	m_colorGroup = new CoButtonGroup();
	Action a = getAction(CoStyledEditorKit.foregroundColorCharacterAction);

	// Clear color
	JMenuItem mi = createCheckboxMenuItem( CoTextConstants.NO_VALUE.toString() );
	m_colorMenu.add( mi );
	mi.addActionListener( a );
	m_colorGroup.add(mi);

	Dimension dim = new Dimension( 12, 12 );
		
	if
		( colors != null )
	{
		Iterator i = colors.iterator();
		while
			( i.hasNext() )
	 	{
			CoColorIF c = (CoColorIF) i.next();
			mi = createCheckboxMenuItem( c.getName() );
			m_colorMenu.add( mi );
			mi.addActionListener( a );
			mi.setIcon( new CoColorSampleIcon( c, dim ) );
			m_colorGroup.add(mi);
		}
	}
}
private void createDialogsMenuItems () {

	if (m_characterStylesMenuItem == null) {
		// create character style dialog
		m_characterStylesMenuItem = createMenuItem(getResourceString("CHARACTER_STYLE_DIALOG"));
		m_characterStylesMenuItem.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					m_characterStyle.open();
				}
			});
		add(m_characterStylesMenuItem);
	}
	
	if (m_paragraphStylesMenuItem == null) {
		// create paragraph style dialog
		m_paragraphStylesMenuItem = createMenuItem(getResourceString("PARAGRAPH_STYLE_DIALOG"));
		m_paragraphStylesMenuItem.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					m_paragraphStyle.open();
				}
			});
		add(m_paragraphStylesMenuItem);
	}
	
	if (m_measurementPrefsMenuItem == null) {
		// create measurement prefs dialog
		m_measurementPrefsMenuItem = createMenuItem( getResourceString( "MEASUREMENTS_PREFS_DIALOG" ) );
		m_measurementPrefsMenuItem.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					m_measurementPrefs.open();
				}
			});
		add(m_measurementPrefsMenuItem);
	}
	
	m_characterStylesMenuItem.setEnabled(m_editor != null && m_characterStyle != null);
	m_paragraphStylesMenuItem.setEnabled(m_editor != null && m_paragraphStyle != null);
	m_measurementPrefsMenuItem.setEnabled(m_editor != null && m_measurementPrefs != null);
}
private void createEditMenuItems()
{
	m_copyMenuItem = createMenuItem( getResourceString( "COPY" ) );
	m_copyMenuItem.addActionListener( getAction( CoStyledEditorKit.copyAction ) );
	add( m_copyMenuItem );
	
	m_copyStyleMenuItem = createMenuItem( getResourceString( "COPY_STYLE" ) );
	m_copyStyleMenuItem.addActionListener( getAction( CoStyledEditorKit.copyCharacterOrParagraphAttributesAction ) );
	add( m_copyStyleMenuItem );
	
	m_cutMenuItem = createMenuItem( getResourceString( "CUT" ) );
	m_cutMenuItem.addActionListener( getAction( CoStyledEditorKit.cutAction ) );
	add( m_cutMenuItem );
	
	m_pasteMenuItem = createMenuItem( getResourceString( "PASTE" ) );
	m_pasteMenuItem.addActionListener( getAction( CoStyledEditorKit.pasteAction ) );
	add( m_pasteMenuItem );
}
private void createFileMenuItems()
{
	CoMenuItem mi = createMenuItem( getResourceString( "GET_TEXT" ) );
	mi.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				if
					( CoXtgFileChooser.getInstance().showDialog( m_editor ) == JFileChooser.APPROVE_OPTION )
				{
					m_editor.importStyledDocument( CoXtgFileChooser.getInstance().getSelectedFile() );
				}
			}
		}
	);



	add( mi );
}
private void createFontMenuItems(java.util.List fonts)
{

	if 
		(m_fontMenu == null)
	{
		m_fontMenu = createMenu( getResourceString(CoTextConstants.FONT_FAMILY) );
		add( m_fontMenu );
	} else {
		m_fontMenu.removeAll();
	}
		
	m_fontGroup = new CoButtonGroup();
	Action a = getAction(CoStyledEditorKit.fontFamilyCharacterAction);

	// Menu item to remove font 
	JMenuItem mi = createCheckboxMenuItem( " " );
	m_fontMenu.add( mi );
	mi.addActionListener( a );
	m_fontGroup.add(mi);

	// Menu items for the fonts

	if 
		( fonts != null )
	{
		Iterator iter = fonts.iterator();
		while
			( iter.hasNext() )
		{
			String fontName = (String)iter.next();
			mi = createCheckboxMenuItem( fontName );
			m_fontMenu.add( mi );
			mi.addActionListener( a );
			mi.setActionCommand(fontName);
			m_fontGroup.add(mi);
		}
	}
}
private void createFontSizeMenuItems()
{
	m_fontSizeMenu = createMenu( getResourceString(CoTextConstants.FONT_SIZE) );
	add( m_fontSizeMenu );
	m_fontSizeGroup = new CoButtonGroup();

	// clear size
	Action a = getAction(CoStyledEditorKit.fontSizeCharacterAction);
	JCheckBoxMenuItem mi = createCheckboxMenuItem( " " );
	m_fontSizeMenu.add( mi );
	mi.addActionListener( a );
	m_fontSizeGroup.add(mi);
	
	String name = getResourceString("OTHER");
	a = getAction(CoStyledEditorKit.otherFontSizeCharacterAction);
	mi = createCheckboxMenuItem( name );
	m_fontSizeMenu.add( mi );
	mi.addActionListener( a );
	m_fontSizeGroup.add(mi);
	
	a = getAction(CoStyledEditorKit.fontSizeCharacterAction);
	String[] sizes = CoTextStringResources.getNames( "FONT_SIZE_OPTIONS" );

	if 
		( sizes != null )
	{
		for
			( int i = 0; i < sizes.length; i++ )
		{
			mi = createCheckboxMenuItem( sizes[ i ] );
			m_fontSizeMenu.add( mi );
			mi.addActionListener( a );
			m_fontSizeGroup.add(mi);
		}
	}
}


private void createParagraphTagMenuItems( Iterator tags )
{
	// build the PARAGRAPH TAG menu
	
	if (m_paragraphTagMenu == null) {
		m_paragraphTagMenu = createMenu(getResourceString(CoTextConstants.PARAGRAPH_TAG));
		add(m_paragraphTagMenu);
	} else {
		m_paragraphTagMenu.removeAll();
	}
	
	if
		( m_paragraphTagsMenuItem == null )
	{
		// create paragraph style dialog
		m_paragraphTagsMenuItem = createMenuItem( getResourceString( "PARAGRAPH_TAGS_DIALOG" ) );
		m_paragraphTagsMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F11, 0 ) );
		m_paragraphTagsMenuItem.addActionListener(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent e )
				{
					m_paragraphTag.open();
				}
			}
		);
	}

	m_paragraphTagMenu.add( m_paragraphTagsMenuItem );
	m_paragraphTagMenu.addSeparator();

	
	m_paragraphTagGroup = new CoButtonGroup();
	
	Action a = getAction(CoStyledEditorKit.tagParagraphAction);

	// insert a check box menu item without a name for removing defined tag
	JMenuItem mi = createCheckboxMenuItem(CoStyledDocument.DEFAULT_TAG_NAME);
	mi.addActionListener(a);
	m_paragraphTagMenu.add(mi);
	m_paragraphTagGroup.add(mi);
	
	if
		( tags != null )
	{
		while
			( tags.hasNext() )
	 	{
			mi = createCheckboxMenuItem( (String) tags.next() );
			mi.addActionListener(a);
			m_paragraphTagMenu.add(mi);
			m_paragraphTagGroup.add(mi);
		}
	}
}
private void createShadeMenuItems()
{
	String name = getResourceString(CoTextConstants.FOREGROUND_SHADE);
	m_shadeMenu = createMenu( name );
	add( m_shadeMenu );
	m_shadeGroup = new CoButtonGroup();

	// clear shade
	Action a = getAction(CoStyledEditorKit.foregroundShadeCharacterAction);
	JCheckBoxMenuItem mi = createCheckboxMenuItem( " " );
	m_shadeMenu.add( mi );
	mi.addActionListener( a );
	m_shadeGroup.add(mi);

	name = getResourceString("OTHER");
	a = getAction(CoStyledEditorKit.otherForegroundShadeCharacterAction);
	mi = createCheckboxMenuItem( name );
	m_shadeMenu.add( mi );
	mi.addActionListener( a );
	m_shadeGroup.add(mi);

	a = getAction(CoStyledEditorKit.foregroundShadeCharacterAction);
	String[] shades = CoTextStringResources.getNames( "FOREGROUND_SHADE_OPTIONS" );
	if 
		( shades != null )
	{
		for
			( int i = 0; i < shades.length; i++ )
		{
			mi = createCheckboxMenuItem( shades[ i ] + " %");
			m_shadeMenu.add( mi );
			mi.addActionListener( a );
			mi.setActionCommand( shades[ i ] );
			m_shadeGroup.add(mi);
		}
	}

}
private void createSpellCheckingMenuItems()
{
	CoMenuItem i = createMenuItem( getResourceString( "CHECK_SPELLING" ) );
	i.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				m_editor.checkSpelling( getSpellCheckProperties() );
			}
		}
	);
	add( i );

	i = createMenuItem( getResourceString( "SPELL_CHECKING_OPTIONS" ) );
	i.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				m_editor.editSpellCheckOptions( (Frame) m_editor.getTopLevelAncestor(),
					                              getSpellCheckProperties() );
			}
		}
	);
	add( i );


	
				

}
private void createStyleMenuItems()
{
	m_styleMenu = createMenu( getResourceString("STYLE") );
	add( m_styleMenu );

	String name = null;
	Action a = null;
	JMenuItem mi = null;
	
	name = CoTextStringResources.getName(CoTextConstants.WEIGHT);
	a = getAction(CoStyledEditorKit.boldCharacterAction);
	mi = createTriStateCheckBoxMenuItem( name );
	m_styleMenu.add( mi );
	mi.addActionListener( a );

	name = CoTextStringResources.getName(CoTextConstants.STYLE);
	a = getAction(CoStyledEditorKit.italicCharacterAction);
	mi = createTriStateCheckBoxMenuItem( name );
	m_styleMenu.add( mi );
	mi.addActionListener( a );

	// UNDERLINE SUB MENU
	m_underlineMenu = createMenu( getResourceString(CoTextConstants.UNDERLINE));
	m_styleMenu.add( m_underlineMenu );
	a = getAction(CoStyledEditorKit.underlineCharacterAction);
	m_underlineGroup = new CoButtonGroup();

	name = CoTextStringResources.getName("UNKNOWN");
	mi = createCheckboxMenuItem( name );
	m_underlineMenu.add( mi );
	mi.addActionListener( a );
	mi.setActionCommand( null );
	m_underlineGroup.add(mi);

	CoEnumValue[] keys = new CoEnumValue []
	{
		CoTextConstants.UNDERLINE_NONE,
		CoTextConstants.UNDERLINE_NORMAL,
		CoTextConstants.UNDERLINE_WORD
	};
	for
		(int i = 0; i < keys.length; i++)
	{
		String key = keys[i].toString();
		name = CoTextStringResources.getName(key);
		mi = createCheckboxMenuItem( name );
		m_underlineMenu.add( mi );
		mi.addActionListener( a );
		mi.setActionCommand( key );
		m_underlineGroup.add(mi);
	}
	//UNDERLINE SUB MENU end
	
	name = CoTextStringResources.getName(CoTextConstants.STRIKE_THRU);
	a = getAction(CoStyledEditorKit.strikeThruCharacterAction);
	mi = createTriStateCheckBoxMenuItem( name );
	m_styleMenu.add( mi );
	mi.addActionListener( a );

	/*
	name = CoTextStringResources.getName(CoTextConstants.OUTLINE);
	a = getAction(CoStyledEditorKit.outlineCharacterAction);
	mi = createTriStateCheckBoxMenuItem( name );
	m_styleMenu.add( mi );
	mi.addActionListener( a );
*/

	name = CoTextStringResources.getName(CoTextConstants.SHADOW);
	a = getAction(CoStyledEditorKit.shadowCharacterAction);
	mi = createTriStateCheckBoxMenuItem( name );
	m_styleMenu.add( mi );
	mi.addActionListener( a );

	name = CoTextStringResources.getName(CoTextConstants.ALL_CAPS);
	a = getAction(CoStyledEditorKit.allCapsCharacterAction);
	mi = createTriStateCheckBoxMenuItem( name );
	m_styleMenu.add( mi );
	mi.addActionListener( a );
/*
	name = CoTextStringResources.getName(CoTextConstants.VARIANT);
	a = getAction(CoStyledEditorKit.smallCapsCharacterAction);
	mi = createTriStateCheckBoxMenuItem( name );
	m_styleMenu.add( mi );
	mi.addActionListener( a );
*/

	// VERTICAL POSITION SUB MENU
	m_verticalPositionMenu = createMenu( getResourceString(CoTextConstants.VERTICAL_POSITION) );
	m_styleMenu.add( m_verticalPositionMenu );
	a = getAction(CoStyledEditorKit.verticalPositionCharacterAction);
	m_verticalPositionGroup = new CoButtonGroup();

	name = CoTextStringResources.getName("UNKNOWN");
	mi = createCheckboxMenuItem( name );
	m_verticalPositionMenu.add( mi );
	mi.addActionListener( a );
	mi.setActionCommand( null );
	m_verticalPositionGroup.add(mi);

	keys = new CoEnumValue []
	{
		CoTextConstants.VERTICAL_POSITION_NONE,
		CoTextConstants.VERTICAL_POSITION_SUPERSCRIPT,
		CoTextConstants.VERTICAL_POSITION_SUBSCRIPT
	};
	for
		(int i = 0; i < keys.length; i++)
	{
		String key = keys[i].toString();
		name = CoTextStringResources.getName(key);
		mi = createCheckboxMenuItem( name );
		m_verticalPositionMenu.add( mi );
		mi.addActionListener( a );
		mi.setActionCommand(key);
		m_verticalPositionGroup.add(mi);
	}
	//VERTICAL POSITION SUB MENU end
	
	name = CoTextStringResources.getName(CoTextConstants.SUPERIOR);
	a = getAction(CoStyledEditorKit.superiorCharacterAction);
	mi = createTriStateCheckBoxMenuItem( name );
	m_styleMenu.add( mi );
	mi.addActionListener( a );
}
private void createTagChainMenuItems( List chains )
{
	// build the TAG CHAIN menu
	
	if
		( m_tagChainMenu == null )
	{
		m_tagChainMenu = createMenu( getResourceString( CoTextConstants.TAG_CHAIN ) );
		add( m_tagChainMenu );
	} else {
		m_tagChainMenu.removeAll();
	}
	
	m_tagChainGroup = new CoButtonGroup();
	
	Action a = getAction(CoStyledEditorKit.tagChainAction);

	// insert a check box menu item without a name for removing defined tag
	JMenuItem mi = createCheckboxMenuItem(CoStyledDocument.DEFAULT_TAG_NAME);
	mi.addActionListener(a);
	m_tagChainMenu.add(mi);
	m_tagChainGroup.add(mi);
	
	if
		( chains != null )
	{
		for
			( int i = 0; i < chains.size(); i++ )
	 	{
		 	CoTagChainIF c = (CoTagChainIF) chains.get( i );
			mi = createCheckboxMenuItem( c.getName() );
			mi.addActionListener(a);
			m_tagChainMenu.add(mi);
			m_tagChainGroup.add(mi);

			List l = c.getChain();
			String tip = "";
			for ( int n = 0; n < l.size(); n++ ) tip += ( ( n == 0 ) ? "" : ", " ) + l.get( n ).toString();
			mi.setToolTipText( tip );
		}
	}
}
private void createUndoMenuItems ()
{
	m_undoMenuItem = createMenuItem( getResourceString( "UNDO" ) );
	m_undoMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, Event.ALT_MASK ) );
	add( m_undoMenuItem );
	
	m_redoMenuItem = createMenuItem( getResourceString( "REDO" ) );
	m_redoMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, Event.ALT_MASK | Event.CTRL_MASK ) );
	add( m_redoMenuItem );
}
private Action getAction( String s )
{
	return (Action) m_actions.get( s );
}
public String getResourceString(String nm)
{
	String str;
	try
	{
		str = CoTextStringResources.getName(nm);
	}
	catch (MissingResourceException mre)
	{
		str = null;
	}

	return str;
}

protected Object getSpellCheckProperties()
{
//	try
//    {
//        return CoSpellCheckerClient.getSpellCheckerServer().getSpellCheckProperties();
//    } catch (RemoteException e)
//    {
//        throw new RuntimeException(e);
//    }
	throw new UnsupportedOperationException("Until Wintertree spell checker is replaced by a GPL spell checker");

}

protected abstract void setAllEnabled( boolean b );


public void setCoCharacterStyleUI(CoCharacterStyleActionUI ui)
{
	if ( m_characterStyle == ui ) return;
	
	m_characterStyle = ui;
	if ( m_editor != null && m_characterStyle != null ) m_characterStyle.setEditor( m_editor );
	createDialogsMenuItems();
}

public void setContext( CoTextEditorContextIF context )
{
	if
		( context != null )
	{
		createParagraphTagMenuItems( context.getParagraphTagNames().iterator() );
		createCharacterTagMenuItems( context.getCharacterTagNames().iterator() );
		createTagChainMenuItems( context.getTagChains() );
		createFontMenuItems( context.getFontFamilyNames() );
		createColorMenuItems( context.getColors() );
		if ( m_measurementPrefs != null ) m_measurementPrefs.setContext( context );
	 	createInsertMenuItems( context.getMacros().keySet() );
	} else {
		createParagraphTagMenuItems( null );
		createCharacterTagMenuItems( null );
		createFontMenuItems( null );
		createColorMenuItems( null );
		if ( m_measurementPrefs != null ) m_measurementPrefs.setContext( null );
	 	createInsertMenuItems( null );
	}

}
public void setCoParagraphStyleUI(CoParagraphStyleActionUI ui)
{
	if ( m_paragraphStyle == ui ) return;

	m_paragraphStyle = ui;
	if (m_editor != null && m_paragraphStyle != null) m_paragraphStyle.setEditor(m_editor);
	createDialogsMenuItems();
}

public void setEditor( CoAbstractTextEditor pane )
{
	if ( m_editor == pane ) return;
	
	m_editor = pane;
	
	createDialogsMenuItems();

	setUndoHandler( ( pane == null ) ? null : pane.getUndoHandler() );
}
public void setTextMeasurementPrefsUI(CoTextMeasurementPrefsUI ui)
{
	if ( m_measurementPrefs == ui ) return;

	m_measurementPrefs = ui;
	createDialogsMenuItems();
}
private void updateAlignmentMenuItems(AttributeSet attr)
{
	CoEnumValue e = CoStyleConstants.getAlignment(attr);
	if
		( e == CoStyleConstants.AS_IS_ENUM_VALUE )
	{
		m_alignmentGroup.setSelected(null, true);
	} else {
		m_alignmentGroup.setSelected((e == null) ? CoTextConstants.NO_VALUE.toString() : e.toString());
	}
}
private void updateCharacterTagMenuItems (AttributeSet attr) 
{
	String tag = CoStyleConstants.getCharacterTag(attr);
	if
		( tag == null )
	{
		m_characterTagGroup.setSelected(CoStyledDocument.DEFAULT_TAG_NAME);
	} else if
		( tag == CoStyleConstants.AS_IS_STRING_VALUE )
	{
		m_characterTagGroup.setSelected( null, true );
	} else {
		m_characterTagGroup.setSelected( null, true );
		m_characterTagGroup.setSelected( tag );
	}

}
private void updateClearCharacterStyles(AttributeSet attr)
{
	m_clearCharacterStylesMenuItem.setEnabled(true);
}
private void updateColorMenuItems (AttributeSet attr)
{
	String color = CoStyleConstants.getForegroundColor(attr);
	if
		(color == null)
	{
		m_colorGroup.setSelected(" ");
	} else if
		(color == CoStyleConstants.AS_IS_STRING_VALUE )
	{
		m_colorGroup.setSelected(null, true);
	} else {
		m_colorGroup.setSelected(color);
	}
}
private void updateEditMenuItems (boolean textSelected) {
	m_copyMenuItem.setEnabled(textSelected);
	m_cutMenuItem.setEnabled(textSelected);
}
private void updateFontMenuItems (AttributeSet attr)
{
	String s = CoStyleConstants.getFontFamily(attr);
	if
		(s == null)
	{
		m_fontGroup.setSelected(" ");
	} else if
		(s == CoStyleConstants.AS_IS_STRING_VALUE)
	{
		m_fontGroup.setSelected(null, true);
	} else {
		m_fontGroup.setSelected(s);
	}
}
private void updateFontSizeMenuItems (AttributeSet attr)
{
	Float f = CoStyleConstants.getFontSize(attr);
	
	if
		(f == null)
	{
		m_fontSizeGroup.setSelected(" ");
		return;
	}
	
	if
		(f == CoStyleConstants.AS_IS_FLOAT_VALUE)
	{
		m_fontSizeGroup.setSelected(null, true);
		return;
	}
	
	m_fontSizeGroup.setSelected(getResourceString("OTHER"));

	m_fontSizeGroup.setSelected( java.text.NumberFormat.getInstance( Locale.getDefault() ).format( f.floatValue() ) );
}
private void updateParagraphTagMenuItems (AttributeSet attr)
{
	String tag = CoStyleConstants.getParagraphTag(attr);
	if
		( tag == null )
	{
		m_paragraphTagGroup.setSelected(CoStyledDocument.DEFAULT_TAG_NAME);
	} else if
		( tag == CoStyleConstants.AS_IS_STRING_VALUE )
	{
		m_paragraphTagGroup.setSelected( null, true );
	} else {
		m_paragraphTagGroup.setSelected( null, true );
		m_paragraphTagGroup.setSelected( tag );
	}

}

private void updateShadeMenuItems (AttributeSet attr)
{
	Float f = CoStyleConstants.getForegroundShade(attr);
	if 
		(f == null)
	{
		m_shadeGroup.setSelected(" ");
	} else if
		(f == CoStyleConstants.AS_IS_FLOAT_VALUE)
	{
		m_shadeGroup.setSelected(null, true);
	} else if
		((f.intValue() % 10) == 0)
	{
		m_shadeGroup.setSelected("" + f.intValue());
	} else {
		m_shadeGroup.setSelected(getResourceString("OTHER"));
	}
}
private void updateStyleMenuItems( AttributeSet attr )
{
	Boolean b;
	Integer i ;
	String s;
	CoEnumValue e;
	CoFontAttribute fa;

	int counter = 0;
	
	// BOLD
	fa = CoStyleConstants.getWeight( attr );
	if
		( fa == CoStyleConstants.AS_IS_FONT_ATTRIBUTE_VALUE )
	{
		((CoTriStateCheckBoxMenuItem)m_styleMenu.getItem(counter++)).setAsIs();
	} else {
		b = ( CoStyleConstants.enum2Boolean( fa, CoFontAttribute.BOLD ) );
  	((CoTriStateCheckBoxMenuItem)m_styleMenu.getItem(counter++)).setTriState(b);
	}
 	
	// ITALIC
	fa = CoStyleConstants.getStyle( attr );
	if
		( fa == CoStyleConstants.AS_IS_FONT_ATTRIBUTE_VALUE )
	{
		((CoTriStateCheckBoxMenuItem)m_styleMenu.getItem(counter++)).setAsIs();
	} else {
		b = ( CoStyleConstants.enum2Boolean( fa, CoFontAttribute.ITALIC ) );
  	((CoTriStateCheckBoxMenuItem)m_styleMenu.getItem(counter++)).setTriState(b);
 	}

  	// UNDERLINE
	e = CoStyleConstants.getUnderline(attr);
	if
		( e == CoStyleConstants.AS_IS_ENUM_VALUE )
	{
		m_underlineGroup.setSelected(null, true);
	} else {
		m_underlineGroup.setSelected( ( e == null ) ? CoTextConstants.NO_VALUE.toString() : e.toString() );
	}
	counter++;


	
	// STRIKE_THRU
	b = CoStyleConstants.getStrikeThru(attr);
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		((CoTriStateCheckBoxMenuItem)m_styleMenu.getItem(counter++)).setAsIs();
	} else {
  	((CoTriStateCheckBoxMenuItem)m_styleMenu.getItem(counter++)).setTriState(b);
	}
 	/*
	// OUTLINE
	b = CoStyleConstants.getOutline(attr);
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		((CoTriStateCheckBoxMenuItem)m_styleMenu.getItem(counter++)).setAsIs();
	} else {
  	((CoTriStateCheckBoxMenuItem)m_styleMenu.getItem(counter++)).setTriState(b);
	}
  */
  	
	// SHADOW
	b = CoStyleConstants.getShadow(attr);
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		((CoTriStateCheckBoxMenuItem)m_styleMenu.getItem(counter++)).setAsIs();
	} else {
  	((CoTriStateCheckBoxMenuItem)m_styleMenu.getItem(counter++)).setTriState(b);
	}
  	
	// ALL CAPS
	b = CoStyleConstants.getAllCaps(attr);
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		((CoTriStateCheckBoxMenuItem)m_styleMenu.getItem(counter++)).setAsIs();
	} else {
  	((CoTriStateCheckBoxMenuItem)m_styleMenu.getItem(counter++)).setTriState(b);
	}

	/*	
	// SMALL CAPS
	b = CoStyleConstants.enum2Boolean( CoStyleConstants.getVariant( attr ), CoStyleConstants.SMALL_CAPS );
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		((CoTriStateCheckBoxMenuItem)m_styleMenu.getItem(counter++)).setAsIs();
	} else {
  	((CoTriStateCheckBoxMenuItem)m_styleMenu.getItem(counter++)).setTriState(b);
	}
*/

  	// VERTICAL POSITION
	e = CoStyleConstants.getVerticalPosition(attr);
	if
		( e == CoStyleConstants.AS_IS_ENUM_VALUE )
	{
		m_verticalPositionGroup.setSelected(null, true);
	} else {
		m_verticalPositionGroup.setSelected( ( e == null ) ? CoTextConstants.NO_VALUE.toString() : e.toString() );
	}
	counter++;
   	
	// SUPERIOR
	b = CoStyleConstants.getSuperior(attr);
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		((CoTriStateCheckBoxMenuItem)m_styleMenu.getItem(counter++)).setAsIs();
	} else {
  	((CoTriStateCheckBoxMenuItem)m_styleMenu.getItem(counter++)).setTriState(b);
	}
}
private void updateTagChainMenuItems( CoStyledDocumentIF doc )
{
	CoTagChainIF chain = ( doc == null ) ? null : doc.getActiveChain();
	
	if
		( chain == null )
	{
		m_tagChainGroup.setSelected( CoStyledDocument.DEFAULT_TAG_NAME );
	} else {
		m_tagChainGroup.setSelected( null, true );
		m_tagChainGroup.setSelected( chain.getName() );
	}
}




public CoStyledTextMenuImplementation( CoMenuBuilder b )
{
	m_builder = b;
}

protected abstract void add( CoSubMenu menu );

protected abstract void add( CoMenuItem menuItem );

protected abstract void addSeparator();



private CoCheckboxMenuItem createCheckboxMenuItem( String label )
{
	CoCheckboxMenuItem m = new CoCheckboxMenuItem( label );
	m_builder.prepareMenuItem( m );
	return m;
}

private void createClearStyleMenuItems()
{
	m_clearCharacterStylesMenuItem = createMenuItem(getResourceString("CLEAR_CHARACTER_STYLES"));
	m_clearCharacterStylesMenuItem.addActionListener(getAction(CoStyledEditorKit.clearCharacterAttributesAction));
	add(m_clearCharacterStylesMenuItem);
	
	m_clearParagraphStylesMenuItem = createMenuItem(getResourceString("CLEAR_PARAGRAPH_STYLES"));
	m_clearParagraphStylesMenuItem.addActionListener(getAction(CoStyledEditorKit.clearParagraphAttributesAction));
	add(m_clearParagraphStylesMenuItem);
}

private void createInsertMenuItems( Collection macros )
{
	if
		( m_insertMenu == null )
	{
		m_insertMenu = createMenu( getResourceString( "INSERT" ) );
		add( m_insertMenu );
	} else {
		m_insertMenu.removeAll();
	}

	CoMenuItem mi = null;

	
	if
		( m_insertCharacterMenu == null )
	{
		ActionListener listener =
			new ActionListener()
			{
				public void actionPerformed( ActionEvent ev )
				{
					m_editor.replaceSelection( ev.getActionCommand() );
				}
			};
		
		m_insertCharacterMenu = createMenu( getResourceString( CHARACTER ) );
		for
			( int i = 0; i < CoAbstractTextEditor.getInsertedStringCount(); i++ )
		{
			String str = CoAbstractTextEditor.getInsertedString( i );
			
			String name = CoAbstractTextEditor.getInsertedStringName( i );
			KeyStroke keyStroke = CoAbstractTextEditor.getInsertedStringKeyStroke( i );

			if ( name == null ) name = str;

			if
				( str == null )
			{
				m_builder.addSeparator( m_insertCharacterMenu );
			} else {
				mi = createMenuItem( getResourceString( name ) );
				mi.setAccelerator( keyStroke );
				m_insertCharacterMenu.add( mi );
				mi.setActionCommand( str );
				mi.addActionListener( listener );
			}
		}
	}

	m_insertMenu.add( m_insertCharacterMenu );

	

	mi = createMenuItem( getResourceString( CoTextConstants.COMMENT ) );
	m_insertMenu.add( mi );
	mi.setAction( getAction( CoStyledEditorKit.commentAction ) );

	/*
	if
		( m_editor == null )
	{
		m_insertMenu.setEnabled( false );
		return;
	} else {
		m_insertMenu.setEnabled( true );
	}
*/

	if
		( macros != null )
	{
		m_insertMenu.addSeparator();
		
		Action a = getAction( CoStyledEditorKit.macroAction );

		Iterator i = macros.iterator();
		while
			( i.hasNext() )
		{
			String name = (String) i.next();
			// resource name should be used in later version
			mi = createMenuItem( name );
			mi.setAction( a );
			m_insertMenu.add( mi );
			mi.setActionCommand( name );
		};
	}
}

private CoSubMenu createMenu( String label )
{
	CoSubMenu m = new CoSubMenu( label );
	m_builder.prepareMenu( m );
	return m;
}

private CoMenuItem createMenuItem( String label )
{
	CoMenuItem m = new CoMenuItem( label );
	m_builder.prepareMenuItem( m );
	return m;
}

private CoTriStateCheckBoxMenuItem createTriStateCheckBoxMenuItem( String label )
{
	CoTriStateCheckBoxMenuItem m = new CoTriStateCheckBoxMenuItem( label );
	m_builder.prepareMenuItem( m );
	return m;
}

public char getResourceChar(String nm)
{
	String str;
	try
	{
		str = CoTextStringResources.getName(nm);
	}
	catch (MissingResourceException mre)
	{
		str = null;
	}

	return str.charAt(0);
}

private void setUndoHandler( CoUndoHandler undoHandler )
{
	if
		( m_undoHandler != null )
	{
		m_undoHandler.removeUndoMenuItem( m_undoMenuItem );
		m_undoHandler.removeRedoMenuItem( m_redoMenuItem );
		m_undoHandler.discardAllEdits();
	}

	m_undoHandler = undoHandler;

	if
		( m_undoHandler != null )
	{
		m_undoHandler.addUndoMenuItem( m_undoMenuItem );
		m_undoHandler.addRedoMenuItem( m_redoMenuItem );
	}
}

public void update( AttributeSet paraAttr, AttributeSet charAttr, int startSelection, int endSelection )
{
	updateCharacterTagMenuItems(charAttr);
	updateParagraphTagMenuItems(paraAttr);
	updateTagChainMenuItems( ( m_editor == null ) ? null : m_editor.getCoStyledDocument() );
	
	updateClearCharacterStyles(charAttr);
	updateEditMenuItems( startSelection != endSelection );

	updateParagraphTagMenuItems(paraAttr);
	updateAlignmentMenuItems(paraAttr);

	updateCharacterTagMenuItems(charAttr);
	updateColorMenuItems(charAttr);
	updateFontMenuItems(charAttr);
	updateFontSizeMenuItems(charAttr);
	updateShadeMenuItems(charAttr);
	updateStyleMenuItems(charAttr);

	boolean canUndo = m_undoMenuItem.isEnabled();
	boolean canRedo = m_redoMenuItem.isEnabled();
	
	setAllEnabled( ( m_editor == null ) ? false : m_editor.isEditable() );

	m_undoMenuItem.setEnabled( canUndo );
	m_redoMenuItem.setEnabled( canRedo );
}

private void updateClearStyles( AttributeSet attr )
{
	m_clearCharacterStylesMenuItem.setEnabled( true );
	m_clearParagraphStylesMenuItem.setEnabled( true );
}

	public static final String CHARACTER = "CoStyledTextMenuImplementation.CHARACTER";
	private CoCharacterTagUI m_characterTag;
	private CoMenuItem m_characterTagsMenuItem;
	private CoSubMenu m_insertCharacterMenu;
	private CoParagraphTagUI m_paragraphTag;
	private CoMenuItem m_paragraphTagsMenuItem;

public void create( Action[] actions,
	                  CoAbstractTextEditor editor,
										CoCharacterStyleActionUI charStyle,
										CoParagraphStyleActionUI paraStyle,
										CoCharacterTagUI charTags,
										CoParagraphTagUI paraTags,
										CoTextMeasurementPrefsUI measurementPrefs )
{
	if ( actions == null ) return;
	
	m_actions = CoActionUtilities.actionsToHashtable( actions );

	createUndoMenuItems();
	addSeparator();

	createParagraphTagMenuItems( null );
	createCharacterTagMenuItems( null );

	createTagChainMenuItems( null );
	addSeparator();

	createInsertMenuItems( null );
	addSeparator();

	createFileMenuItems();
	addSeparator();
	
	createEditMenuItems();
	addSeparator();

	createClearStyleMenuItems();
	addSeparator();
	
	createFontMenuItems(null);

	createFontSizeMenuItems();

	createStyleMenuItems();

	createColorMenuItems( null );

	createShadeMenuItems();
	addSeparator();

	createAlignmentMenuItems();
	addSeparator();

	createDialogsMenuItems();
	addSeparator();

	createSpellCheckingMenuItems();

	

	setEditor( editor );
	setCoCharacterStyleUI( charStyle );
	setCoParagraphStyleUI( paraStyle );
	setCharacterTagUI( charTags );
	setParagraphTagUI( paraTags );
	setTextMeasurementPrefsUI( measurementPrefs );
}

public void setCharacterTagUI( CoCharacterTagUI ui )
{
	if ( m_characterTag == ui ) return;

	m_characterTag = ui;
	if ( m_editor != null && m_characterTag != null ) m_characterTag.setEditor( m_editor );

	m_characterTagsMenuItem.setEnabled( m_characterTag != null );
}

public void setParagraphTagUI(CoParagraphTagUI ui)
{
	if ( m_paragraphTag == ui ) return;

	m_paragraphTag = ui;
	if (m_editor != null && m_paragraphTag != null) m_paragraphTag.setEditor(m_editor);

	m_paragraphTagsMenuItem.setEnabled( m_paragraphTag != null );
}
}