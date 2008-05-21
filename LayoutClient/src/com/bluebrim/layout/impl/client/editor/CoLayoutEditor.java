package com.bluebrim.layout.impl.client.editor;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.print.*;
import java.io.*;
import java.net.*;
import java.rmi.*;
import java.text.*;
import java.util.*;
import java.util.List;

import javax.print.attribute.*;
import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.gemstone.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.client.*;
import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.client.tools.*;
import com.bluebrim.layout.impl.client.transfer.*;
import com.bluebrim.layout.impl.client.view.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.menus.client.*;
import com.bluebrim.observable.*;
import com.bluebrim.resource.shared.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.client.*;
import com.bluebrim.text.impl.client.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * The layout editor
 *
 * @author Dennis
 * @version 1.0 [2001-09-20] 
 */

public class CoLayoutEditor extends CoUserInterface implements CoLayoutEditorClientConstants
{
	// --- UI model constants ---¨
	public static final String ZOOM = "CoLayoutEditor.ZOOM";
	public static final String APPLICATION_PREFS = "CoLayoutEditor.APPLICATION_PREFS";
	public static final String CREATE_TEMPLATE = "CoLayoutEditor.CREATE_TEMPLATE";

	private String m_name;
	
	// --- Layout editor context ---
	protected CoPageItemEditorContextIF m_context;
	private CoChangedObjectListener m_contextListener;

	
	// --- Layout editor models ---
	protected List m_models; // [ CoLayoutEditorModel ]
	protected Stack m_modelStack = new Stack(); // [ Collection[ CoLayoutEditorModel ] ], used to keep previous model when the model is changed to a a subset of itself
	
	// --- UI components ---
	protected CoPageItemEditorPanel m_workspace;
	protected CoPageItemToolbar m_pageItemToolbar;
	protected CoPageItemTreePathPane m_treePathPane;
	protected CoPageItemPane m_modifyPane;



//	protected CoPageItemTrappingPanel m_trappingPanel;


	// --- Dialogs and UI's
	protected CoDialog m_modifyDialog;
	protected CoLayerDialog m_layerDialog;

//	protected CoDialog m_trappingDialog;
	protected CoDialog m_workspaceScrollBoardDialog;
	protected List m_externalUIAction = new ArrayList(); // [ CoExternalUIAction ]



	
	// custom operations stuff
	private class PageItemOperationAction extends AbstractAction
	{
		private CoPageItemOperationIF m_op;
		
		public PageItemOperationAction( CoPageItemOperationIF op )
		{
			super( op.getName() );
			m_op = op;
		}

		public CoPageItemOperationIF getOperation()
		{
			return m_op;
		}

		public void actionPerformed( ActionEvent ev )
		{
			doExecute( m_op );
		}
	};
		

	// --- Text editor stuff ---
	protected CoStyledTextEditor m_styledTextEditor;
	protected CoStyledDocumentIF m_originalDocument; // when text editor isn't active
		
	protected CoTextRulerPane m_textRuler;
	protected CoStyledTextPopupMenu m_textEditorPopupMenu;
	protected CoTextStyleToolbarSet m_textStyleToolbars;
	protected CoTextStyleMenu m_textStyleMenu;
	protected CoCharacterStyleActionUI m_characterStyleUI;
	protected CoParagraphStyleActionUI m_paragraphStyleUI;
	
	private MouseEvent m_textEditorInitialEvent; // injected when text editor is made visible in order to postion text cursor
	protected CoPageItemAbstractTextContentView m_activeTextContentView;
	protected CoTextMeasurementPrefsUI m_measurementPrefs; // PENDING: user prefs ???
	protected CoCharacterTagUI m_characterTagUI;
	protected CoParagraphTagUI m_paragraphTagUI;


	// --- Tool stuff ---
	private static class ToolToggleButton extends CoToggleButton
	{
		private CoTool m_tool;

		public ToolToggleButton( CoTool t )
		{
			m_tool = t;
		}

		public CoTool getTool()
		{
			return m_tool;
		}
		
		public String getToolTipText() {
			return m_tool.getToolTipText();
		}
		
	}
	private CoEditTextTool [] m_editTextTools;
	private CoContentTool m_contentTool;

	protected CoToolDispatcher m_toolDispatcher = new CoToolDispatcher();
	protected CoButtonGroup m_toolButtonGroup = new CoButtonGroup();
	protected CoToolbar m_creationToolbar;
	private CoMenu m_contentMenu;

	
	// --- View popup menu stuff ---
	public static final String POPUP_SHAPE_PAGE_ITEM_MODIFY = "POPUP.SHAPE_PAGE_ITEM.MODIFY";
	public static final String POPUP_SHAPE_PAGE_ITEM_SPAWN = "POPUP.SHAPE_PAGE_ITEM.SPAWN";
	public static final String POPUP_SHAPE_PAGE_ITEM_SPAWN_SPAWN_IN_THIS_WINDOW = "POPUP.SHAPE_PAGE_ITEM.SPAWN.SPAWN_IN_THIS_WINDOW";
	public static final String POPUP_SHAPE_PAGE_ITEM_SPAWN_SPAWN_IN_NEW_WINDOW = "POPUP.SHAPE_PAGE_ITEM.SPAWN.SPAWN_IN_NEW_WINDOW";
	public static final String POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_WITH_TO_FIT_HEIGHT_TO_IMAGE = "POPUP.ABSTRACT_IMAGE.ADJUST_WITH_TO_FIT_HEIGHT_TO_IMAGE";
	public static final String POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_FIT_KEEP_ASPECT_RATIO = "POPUP.ABSTRACT_IMAGE.ADJUST_TO_FIT_KEEP_ASPECT_RATIO";
	public static final String POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_FIT = "POPUP.ABSTRACT_IMAGE.ADJUST_TO_FIT";
	public static final String POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_SCALED_IMAGE_SIZE = "POPUP.ABSTRACT_IMAGE.ADJUST_TO_SCALED_IMAGE_SIZE";
	public static final String POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_IMAGE_SIZE = "POPUP.ABSTRACT_IMAGE.ADJUST_TO_IMAGE_SIZE";
	public static final String POPUP_IMAGE_CONTENT_GET_PICTURE = "POPUP_IMAGE_CONTENT_GET_PICTURE";
	public static final String POPUP_TEXT_CONTENT_ADJUST_HEIGHT_TO_TEXT = "POPUP.TEXT_CONTENT.ADJUST_HEIGHT_TO_TEXT";
	public static final String POPUP_TEXT_CONTENT_EDIT = "POPUP.TEXT_CONTENT.EDIT";
	public static final String POPUP_ABSTRACT_LAYOUT_CONTENT_EDIT = "POPUP.ABSTRACT_LAYOUT_CONTENT.EDIT";
	public static final String POPUP_ABSTRACT_LAYOUT_CONTENT_ADJUST_WITH_TO_FIT_HEIGHT_TO_LAYOUT = "POPUP.ABSTRACT_LAYOUT.ADJUST_WITH_TO_FIT_HEIGHT_TO_LAYOUT";
	public static final String POPUP_ABSTRACT_LAYOUT_CONTENT_ADJUST_TO_FIT_KEEP_ASPECT_RATIO = "POPUP.ABSTRACT_LAYOUT.ADJUST_TO_FIT_KEEP_ASPECT_RATIO";
	public static final String POPUP_ABSTRACT_LAYOUT_CONTENT_ADJUST_TO_FIT = "POPUP.ABSTRACT_LAYOUT.ADJUST_TO_FIT";
	public static final String POPUP_ABSTRACT_LAYOUT_CONTENT_ADJUST_TO_SCALED_LAYOUT_SIZE = "POPUP.ABSTRACT_LAYOUT.ADJUST_TO_SCALED_LAYOUT_SIZE";
	public static final String POPUP_ABSTRACT_LAYOUT_CONTENT_ADJUST_TO_LAYOUT_SIZE = "POPUP.ABSTRACT_LAYOUT.ADJUST_TO_LAYOUT_SIZE";
	public static final String POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_EMBEDDED_PATH = "POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_EMBEDDED_PATH";

	private CoSplitPane m_splitPane;
	private int m_splitPaneDividerSize;

	private List m_popupMenuPrepareables = new ArrayList(); // [ CoPrepareable ]
	private CoPageItemPopupMenuModel m_popupMenuModel;

	private CoPageItemView m_popupMenuOwner;
	private Hashtable m_popupMenus = new Hashtable(); // [ Object (popup menu key) -> CoPopupMenu ]


	
	// --- help stuff ---
	public static final String KEYBOARD_SHORTCUTS_HELP_URL = "CoLayoutEditor.KEYBOARD_SHORTCUTS_HELP_URL";
	private static JFrame m_kbShortcutsWindow;
	
	// --- quick zoom stuff ---
	private static float QUICK_ZOOM_STATES [] = new float [] { 1.0f, 2.0f, 0.0f };
	private int m_quickZoomState = 0;
		

	// --- View properties ---
	protected boolean m_snapToGrid = true;
	protected Map m_renderingHints = new HashMap();
	{
		m_renderingHints.put( CoPageItemViewRenderer.PAINT_CUSTOM_GRID, CoPageItemViewRenderer.PAINT_CUSTOM_GRID_ON );
		m_renderingHints.put( CoPageItemViewRenderer.PAINT_COLUMN_GRID, CoPageItemViewRenderer.PAINT_COLUMN_GRID_ON );
		m_renderingHints.put( CoPageItemViewRenderer.PAINT_OUTLINE, CoPageItemViewRenderer.PAINT_OUTLINE_ON );
		m_renderingHints.put( CoPageItemViewRenderer.PAINT_MODEL_OUTLINE, CoPageItemViewRenderer.PAINT_MODEL_OUTLINE_ON);
		m_renderingHints.put( CoTextRenderingHints.PAINT_DUMMY_TEXT, CoTextRenderingHints.PAINT_DUMMY_TEXT_ON);
		m_renderingHints.put( CoTextEditorIF.PAINT_OVERFLOW_INDICATOR, CoTextEditorIF.PAINT_OVERFLOW_INDICATOR_ON );
		m_renderingHints.put( CoPageItemViewRenderer.PAINT_IMAGE_PLACEHOLDERS, CoPageItemViewRenderer.PAINT_IMAGE_PLACEHOLDERS_OFF );
	}



	private CoButtonGroup m_viewRendererFactoryButtonGroup = new CoButtonGroup();

	// --- Cut/paste clipboard ---
	private static ClipboardOwner m_defaultClipboardOwner = new ClipboardOwner()
	{
		public void lostOwnership( Clipboard clipboard, Transferable contents )
		{
		}
	};
	


	// tool editing stuff
	MouseAdapter m_toolEditingManager = new MouseAdapter()
	{
		private CoPopupMenu m_menu = getMenuBuilder().createPopupMenu();
		private JMenuItem m_deleteMenuItem;
		
		{
			ActionListener l =
				new ActionListener()
				{
					public void actionPerformed( ActionEvent ev )
				  {
						m_workspace.getSelectionManager().unselectAllViews();
						modifyTool( m_tool );
					}
				};
			m_menu.add( CoPageItemViewResources.getName( "POPUP.TOOL.MODIFY" ) ).addActionListener( l );
			
			m_menu.addSeparator();

			l =
				new ActionListener()
				{
					public void actionPerformed( ActionEvent ev )
				  {
					  doCopyTool( m_tool );
					}
				};				
			m_menu.add( CoPageItemViewResources.getName( "POPUP.TOOL.COPY" ) ).addActionListener( l );

			l =
				new ActionListener()
				{
					public void actionPerformed( ActionEvent ev )
				  {
					  doDeleteTool( m_tool );
					}
				};				
			m_deleteMenuItem = m_menu.add( CoPageItemViewResources.getName( "POPUP.TOOL.DELETE" ) );
			m_deleteMenuItem.addActionListener( l );

			m_menu.addSeparator();

			l =
				new ActionListener()
				{
					public void actionPerformed( ActionEvent ev )
				  {
					  doMoveToolUp( m_tool );
					}
				};				
			m_menu.add( CoPageItemViewResources.getName( "POPUP.TOOL.MOVE_UP" ) ).addActionListener( l );

			l =
				new ActionListener()
				{
					public void actionPerformed( ActionEvent ev )
				  {
					  doMoveToolDown( m_tool );
					}
				};				
			m_menu.add( CoPageItemViewResources.getName( "POPUP.TOOL.MOVE_DOWN" ) ).addActionListener( l );

		}

		CoAbstractCreateTool m_tool;

		
		public void mouseClicked( MouseEvent ev ) { postMenu( ev ); }
		public void mousePressed( MouseEvent ev ) { postMenu( ev ); }
		public void mouseReleased( MouseEvent ev ) { postMenu( ev ); }

		private void postMenu( MouseEvent ev )
		{
			if ( ! ev.isPopupTrigger() ) return;
			
			ToolToggleButton b = (ToolToggleButton) ev.getSource();
			m_tool = (CoAbstractCreateTool) b.getTool();
			m_deleteMenuItem.setEnabled( m_tool.getPrototype().isDeleteable() );
			m_menu.show( b, ev.getX(), ev.getY() );
		}
	};



	// --- Enable/disable handling ---
	/*
	 Each CoEnableDisableManager represents a layout editor state.
	 Any Action, Component or CoValueable can be added to a CoEnableDisableManager.
	*/
	
	protected final CoEnableDisableManager m_modelsNotEmpty = new CoEnableDisableManager()
	{
		public boolean isEnabled() { return ( m_models != null ) && ( ! m_models.isEmpty() ); }
	};

	protected final CoEnableDisableManager m_textEditorActive = new CoEnableDisableManager()
	{
		public boolean isEnabled() { return isTextEditorActive(); }
	};

	protected final CoEnableDisableManager m_atLeastOneSelected = new CoEnableDisableManager()
	{
		public boolean isEnabled() { return getWorkspace().getSelectionManager().getSelectedViewCount() > 0; }
	};

	protected final CoEnableDisableManager m_exactlyOneSelected = new CoEnableDisableManager()
	{
		public boolean isEnabled() { return getWorkspace().getSelectionManager().getSelectedViewCount() == 1; }
	};

	protected final CoEnableDisableManager m_exactlyOneTextSelected = new CoEnableDisableManager()
	{
		public boolean isEnabled()
		{
			CoPageItemView cw = getWorkspace().getSelectionManager().getSelectedView();
			if ( cw == null ) return false;
			if ( ! ( cw instanceof CoContentWrapperPageItemView ) ) return false;
			cw = ( (CoContentWrapperPageItemView) cw ).getContentView();
			return ( cw instanceof CoPageItemAbstractTextContentView );
		}
	};

	protected final CoEnableDisableManager m_exactlyOneImageSelected = new CoEnableDisableManager()
	{
		public boolean isEnabled()
		{
			CoPageItemView cw = getWorkspace().getSelectionManager().getSelectedView();
			if ( cw == null ) return false;
			if ( ! ( cw instanceof CoContentWrapperPageItemView ) ) return false;
			cw = ( (CoContentWrapperPageItemView) cw ).getContentView();
			return ( cw instanceof CoPageItemImageContentView );
		}
	};
	
	protected final CoEnableDisableManager m_exactlyOneNonImageContentSelected = new CoEnableDisableManager()
	{
		public boolean isEnabled()
		{
			CoPageItemView cw = getWorkspace().getSelectionManager().getSelectedView();
			if ( cw == null ) return false;
			if ( ! ( cw instanceof CoContentWrapperPageItemView ) ) return false;
			cw = ( (CoContentWrapperPageItemView) cw ).getContentView();
			return ! ( cw instanceof CoPageItemImageContentView );
		}
	};
	
	protected final CoEnableDisableManager m_exactlyOneNonTextContentSelected = new CoEnableDisableManager()
	{
		public boolean isEnabled()
		{
			CoPageItemView cw = getWorkspace().getSelectionManager().getSelectedView();
			if ( cw == null ) return false;
			if ( ! ( cw instanceof CoContentWrapperPageItemView ) ) return false;
			cw = ( (CoContentWrapperPageItemView) cw ).getContentView();
			return ! ( cw instanceof CoPageItemAbstractTextContentView );
		}
	};
	
	protected final CoEnableDisableManager m_exactlyOneNonNoContentSelected = new CoEnableDisableManager()
	{
		public boolean isEnabled()
		{
			CoPageItemView cw = getWorkspace().getSelectionManager().getSelectedView();
			if ( cw == null ) return false;
			if ( ! ( cw instanceof CoContentWrapperPageItemView ) ) return false;
			cw = ( (CoContentWrapperPageItemView) cw ).getContentView();
			return ! ( cw instanceof CoPageItemNoContentView );
		}
	};
	
	protected final CoEnableDisableManager m_alwaysDisabled = new CoEnableDisableManager()
	{
		public boolean isEnabled()
		{
			return false;
		}
	};
	
	protected final CoEnableDisableManager m_alwaysEnabled = new CoEnableDisableManager()
	{
		public boolean isEnabled()
		{
			return true;
		}
	};
	

	
	/*
	protected final CoEnableDisableManager m_undoStackNotEmptyEnabled = new CoEnableDisableManager()
	{
		public boolean isEnabled()
		{
			return m_commandManager.getTopUndo() != null;
		}
	};
	*/
	
	protected final CoEnableDisableManager m_pageItemOperationEnabled = new CoEnableDisableManager()
	{
		public boolean isEnabled()
		{
			return true; // not used
		}
			
		public void update()
		{
			int c = 0;
			List l = new ArrayList();

			Iterator i = m_workspace.getSelectionManager().getSelectedViews();
			while
				( i.hasNext() )
			{
				l.add( i.next() );
				c++;
			}
			
			i = m_actions.iterator();
			while
				( i.hasNext() )
			{
				PageItemOperationAction a = (PageItemOperationAction) i.next();
				a.setEnabled( a.getOperation().isValidOperand( c ) && a.getOperation().isValidOperand( l ) );
			}
		}
	};
	
	protected final CoEnableDisableManager m_modelStackNotEmpty = new CoEnableDisableManager()
	{
		public boolean isEnabled()
		{
			return ! m_modelStack.isEmpty();
		}
	};

	// These CoEnableDisableManager are updated by calling the method updateEnableDisableState.
	// Note: they are updated in order so be carefull when inserting new ones (general states before specific ones).
	protected CoEnableDisableManager[] m_enableDisableManagers =
		new CoEnableDisableManager[]
		{
			m_modelsNotEmpty,
			m_atLeastOneSelected,
			m_exactlyOneSelected,
			m_exactlyOneNonImageContentSelected,
			m_exactlyOneNonTextContentSelected,
			m_exactlyOneNonNoContentSelected,
			m_exactlyOneTextSelected,
			m_exactlyOneImageSelected,
			m_pageItemOperationEnabled,
//			m_undoStackNotEmptyEnabled,
			m_textEditorActive,
			m_alwaysDisabled,
			m_alwaysEnabled,
			m_modelStackNotEmpty,
		};








	// custom grid line stuff
	protected CoTextField m_customGridlineTextField;
	protected double m_xCustomGridline;
	protected double m_yCustomGridline;
	protected CoDialog m_treeViewDialog;
	protected CoPopupMenu m_customGridlinePopup;

	private CoChangedObjectListener m_toolListener;
	protected CoCustomGridIF m_customGrid;


	protected CoDesktopLayoutAreaIF m_desktop;

	// --- Undo stuff ---
	private CoUndoableCommandExecutor m_commandExecutor = new CoUndoableCommandExecutor();
	protected CoPageItemTreePane m_treeViewPane;
	private CoUndoHandler m_undoHandler;

	protected CoTextField m_statusText;

	
	/* 	Added by Magnus Ihse (magnus.ihse@appeal.se) (2001-08-20 15:00:01) */
	// --- Printer/postscript stuff ---
	private PageFormat m_pageFormat;
	private PrintRequestAttributeSet m_printRequestAttributeSet = new HashPrintRequestAttributeSet();


	// --- Layout editor configuration ---
	private CoLayoutEditorConfiguration m_configuration;
	private CoMenuItem m_createTemplateMenuItem;



	private CoSubMenu m_itemMenu;
	protected CoDomainUserInterface m_prototypeTreeUI;
	private CoMenu m_viewRendererMenu;
	public static final String TEMPLATES = "CoLayoutEditor.TEMPLATES";

	protected CoZoomPanel m_zoomPanel;
public void activateContentTool()
{
	m_toolDispatcher.setActiveTool( m_contentTool );
}
public CoTool activateTextEditTool( MouseEvent e )
{
	CoTool t = getEditTextTool( null, e );
	m_toolDispatcher.setActiveTool( t );
	return t;
}
public CoTool activateTextEditTool( CoPageItemAbstractTextContentView v )
{
	CoTool t = getEditTextTool( v, null );
	m_toolDispatcher.setActiveTool( t );
	return t;
}
protected void addPopupMenu( CoPopupMenu m, Object key )
{
	m_popupMenus.put( key, m );

	CoComponentVisitor cv =
		new CoComponentVisitor()
		{
			public void doit( Component c )
			{
				if
					( c instanceof CoPrepareable )
				{
					m_popupMenuPrepareables.add( c );
				} else if
					( c instanceof CoMenuItem )
				{
					Action a = ( (CoMenuItem) c ).getAction();
					if
						( a instanceof CoPrepareable )
					{
						m_popupMenuPrepareables.add( a );
					}
				}
			}
		};

	cv.visit( m );
}
protected CoValueable addValueModel( CoUserInterfaceBuilder b, CoValueable vm, CoEnableDisableManager edm )
{
	b.addNamedValueModel( vm.getKey(), vm );
	if ( edm != null ) edm.add( vm );
	return vm;
}
public void closing()
{
	super.closing();

	// stop text editing
	if ( m_activeTextContentView != null ) stopTextEditing( m_activeTextContentView, m_styledTextEditor );
	
	// hide all subwindows
	if ( m_modifyDialog != null ) m_modifyDialog.setVisible( false );
	if ( m_treeViewDialog != null ) m_treeViewDialog.setVisible( false );
	if ( m_layerDialog != null ) m_layerDialog.setVisible( false );
//	if ( m_trappingDialog != null ) m_trappingDialog.setVisible( false );
	if ( m_workspaceScrollBoardDialog != null ) m_workspaceScrollBoardDialog.setVisible( false );

	Iterator i = m_externalUIAction.iterator();
	while
		( i.hasNext() )
	{
		( (CoExternalUIAction) i.next() ).close();
	}
	
	boolean dispose = false;
	Window w = getWindow();
	if
		( w instanceof JDialog )
	{
		dispose = ( (JDialog) w ).getDefaultCloseOperation() == WindowConstants.DISPOSE_ON_CLOSE;
	} else {
		dispose = ( (JFrame) w ).getDefaultCloseOperation() == WindowConstants.DISPOSE_ON_CLOSE;
	}
	
	if ( dispose ) dispose();
}
private void copyPageItems()
{
	if ( m_workspace.getSelectionManager().getSelectedViewCount() == 0 ) return;
	
	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

	CoPageItemTransferable contents = new CoPageItemTransferable( m_workspace.getSelectionManager().getSelectedViews() );
	clipboard.setContents( contents, m_defaultClipboardOwner );
}


private CoCheckboxMenuItem createCheckboxMenuItem( CoMenuBuilder b, CoMenu m, CoLayoutEditorClientConstants.MenuItem resource, CoEnableDisableManager edm )
{
	return createCheckboxMenuItem(b, m, resource.text(), resource.mnemonic(), resource.accelerator(), edm);
}

private CoCheckboxMenuItem createCheckboxMenuItem( CoMenuBuilder b, CoMenu m, String name, char mnemonic, KeyStroke accelerator, CoEnableDisableManager edm )
{
	CoCheckboxMenuItem i = b.addCheckBoxMenuItem( m, name );
	i.setMnemonic( mnemonic );
	if (accelerator != null)
		i.setAccelerator(accelerator);	
	if ( edm != null ) 
		edm.add( i );
	return i;
}


protected void createCreationTool( CoPageItemPrototypeIF p )
{
	CoCreateToolProxy t = new CoCreateToolProxy( p, this );
	m_toolDispatcher.manageTool( t );

	CoToggleButton b = createToolButton( null, t, t);
	b.addMouseListener( m_toolEditingManager );
	
	m_creationToolbar.add( b );
}

protected void createCreationTools( CoToolbarDockingBay db )
{
	m_creationToolbar = new CoToolbar();
	db.addToolbar( m_creationToolbar, 0 );

	updateCreationTools();
}

protected Component createEastWidget( CoUserInterfaceBuilder builder )
{
	return null;
}

private void createEditMenu( CoMenuBar mb, CoMenuBuilder b )
{
	CoSubMenu m = mb.addSubMenu(MENU_EDIT);

	CoMenuItem undoMenuItem = new CoMenuItem();
	m.add(undoMenuItem);
	undoMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, Event.SHIFT_MASK ) );
	
	CoMenuItem redoMenuItem = new CoMenuItem();
	m.add(redoMenuItem);
	redoMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, Event.SHIFT_MASK | Event.CTRL_MASK ) );

	m_undoHandler = new CoUndoHandler();
	m_undoHandler.addUndoMenuItem( undoMenuItem );
	m_undoHandler.addRedoMenuItem( redoMenuItem );
	m_commandExecutor.addUndoableEditListener( m_undoHandler );

	b.addSeparator( m );

	createMenuItem( m, MENU_EDIT_CUT,        m_atLeastOneSelected ).addActionListener( new ActionListener() { public void actionPerformed( ActionEvent ev ) { cutPageItems(); } } );
	createMenuItem( m, MENU_EDIT_COPY,       m_atLeastOneSelected ).addActionListener( new ActionListener() { public void actionPerformed( ActionEvent ev ) { copyPageItems(); } } );
	createMenuItem( m, MENU_EDIT_PASTE,      null ).addActionListener(                 new ActionListener() { public void actionPerformed( ActionEvent ev ) { pastePageItems(); } } );
	createMenuItem( m, MENU_EDIT_DELETE,     m_atLeastOneSelected ).addActionListener( new ActionListener() { public void actionPerformed( ActionEvent ev ) { deletePageItems(); } } );
	createMenuItem( m, MENU_EDIT_SELECT_ALL, null ).addActionListener(                 new ActionListener() { public void actionPerformed( ActionEvent ev ) { selectAllPageItems(); } } );

	m.addSeparator();

	createMenuItem( m, MENU_EDIT_FIND_REPLACE, null ).addActionListener( new CoFindReplace( this ) );

	m.addSeparator();

	{
		CoMenu m2 = m.addSubMenu(MENU_EDIT_PREFERENCES );
		createMenuItem( m2, MENU_EDIT_PREFERENCES_APPLICATION, null ).addActionListener( new CoEditPrefs( this ) );
	}

	createMenuItem( m, MENU_EDIT_HYPHENATIONS, null ).addActionListener( new CoEditHyphenations( this ) );
	createMenuItem( m, MENU_EDIT_COLORS, null ).addActionListener( new CoEditColors( this ) );
	createMenuItem( m, MENU_EDIT_DASHES_STRIPES, null ).addActionListener( new CoEditStrokes( this ) );
	
}
	/**
	 * Skapa verktygspalettens UI
	 *
	 * Dessa verktyg är temporärt bortkommenterade för att
	 * de ännu ej fungerar och tar upp onödig plats i layouteditorns
	 * verktygsrad.
	 */
protected void createEditTools( CoToolbarDockingBay db )
{
	CoToolbar toolbar = new CoToolbar();
	db.addToolbar( toolbar, 0 );

	
	Icon icon = getIcon( "rotation.gif" );
	CoTool t = new CoRotationTool( this );
	toolbar.add( createToolButton( null, icon, t ));

	m_toolDispatcher.manageTool( t );

/**
	
	name = "chain";
	icon = getIcon( "name + ".gif" );
	toolbar.add( createToolButton( null, icon, null, CoPageItemUIStringResources.getName("CHAIN_TOOL") ));

	
	name = "unchain";
	icon = getIcon( "name + ".gif" );
	toolbar.add( createToolButton( null, icon, null, CoPageItemUIStringResources.getName("UNCHAIN_TOOL") ));
*/
}



/**
Builds the File Menu.
*/
private void createFileMenu( CoMenuBar mb, CoMenuBuilder b )
{
	CoMenu m = mb.addSubMenu(MENU_FILE);
	
	{
		CoMenu m2 = m.addSubMenu( MENU_FILE_SPAWN );
		createMenuItem( m2, MENU_FILE_SPAWN_IN_THIS_WINDOW, m_atLeastOneSelected ).addActionListener( getSubsetModelAction() );
		createMenuItem( m2, MENU_FILE_SPAWN_IN_NEW_WINDOW, m_atLeastOneSelected ).addActionListener( new CoSpawnSelected( CoLayoutEditor.this ) );
	}

	createMenuItem( m, MENU_FILE_PREVIOUS_MODEL, m_modelStackNotEmpty ).addActionListener( getUnsubsetModelAction() );

	m.addSeparator();

	createMenuItem( m, MENU_FILE_PRINT_SETTINGS, null ).addActionListener( new CoChangePrintSettings( this ) );
	createMenuItem( m, MENU_FILE_PRINT, null ).addActionListener( new CoLayoutEditorPrint( this ) );

	b.addSeparator( m );

	CoMenu m3 = m.addSubMenu( MENU_FILE_EXPORT );

	createMenuItem( m3, MENU_FILE_EXPORT_EPS, null ).addActionListener( new CoExportAsEPS( this ) );
	createMenuItem( m3, MENU_FILE_EXPORT_POSTSCRIPT, null ).addActionListener( new CoExportAsPostscript( this ) );
	
//	CoMenu m4 = b.addSubMenu( m, CoLayouteditorUIStringResources.getName( MENU_FILE_PRINT.MENU" ) );
//	m4.setMnemonic( CoLayouteditorUIStringResources.getChar( MENU_FILE_PRINT.MENU_MNEMONIC" ) );

//	createMenuItem( b, m4, MENU_FILE_PRINT.PRINTER", null ).addActionListener( new CoPrintPostscriptToPrinter( this ) );
//	createMenuItem( b, m4, MENU_FILE_PRINT.FILE", null ).addActionListener( new CoExportPostscriptToFile( this ) );
//	createMenuItem( b, m4, MENU_FILE_PRINT.TYPESETTER", null ).addActionListener( new CoPrintPostscriptToRIP( this ) );
}

private void createHelpMenu( CoMenuBar mb, CoMenuBuilder b )
{
	CoMenu m = mb.addSubMenu(MENU_HELP);

	CoMenuItem i = createMenuItem( m, MENU_HELP_KB_SHORTCUTS, null );
	i.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent ev ) { showKbShortcuts(); } } );

}

private void createItemMenu( CoMenuBar mb, CoMenuBuilder b )
{
	m_itemMenu = mb.addSubMenu( MENU_ITEM );

	CoMenuItem i = createMenuItem( m_itemMenu, MENU_ITEM_MODIFY, m_exactlyOneSelected );
	i.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent ev ) { modifySelectedView(); } } );

	m_itemMenu.addSeparator();

	i = createMenuItem( m_itemMenu, MENU_ITEM_LAYERS, null );
	i.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent ev ) { openLayerDialog(); } } );

	b.addSeparator( m_itemMenu );

	createMenuItem( m_itemMenu, MENU_ITEM_DUPLICATE, m_atLeastOneSelected ).addActionListener( new CoDuplicate( CoLayoutEditor.this ) );
	createMenuItem( m_itemMenu, MENU_ITEM_STEP_AND_REPEAT, m_exactlyOneSelected ).addActionListener( new CoDuplicateAndRepeat( CoLayoutEditor.this ) );

	i = createMenuItem( m_itemMenu, MENU_ITEM_DELETE, m_atLeastOneSelected );
	i.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent ev ) { deletePageItems(); } } );

	b.addSeparator( m_itemMenu );

	i = createMenuItem( m_itemMenu, MENU_ITEM_SEND_BACKWARD, m_atLeastOneSelected );
	i.addActionListener( new CoSendBackwards( CoLayoutEditor.this ) );

	i = createMenuItem( m_itemMenu, MENU_ITEM_SEND_TO_BACK, m_atLeastOneSelected );
	i.addActionListener( new CoSendToBack( CoLayoutEditor.this ) );

	i = createMenuItem( m_itemMenu, MENU_ITEM_BRING_FORWARD, m_atLeastOneSelected );
	i.addActionListener( new CoBringForward( CoLayoutEditor.this ) );

	i = createMenuItem( m_itemMenu, MENU_ITEM_BRING_TO_FRONT, m_atLeastOneSelected );
	i.addActionListener( new CoBringToFront( CoLayoutEditor.this ) );

	b.addSeparator( m_itemMenu );

	
	CoCheckboxMenuItem cbi = createCheckboxMenuItem( b, m_itemMenu, MENU_ITEM_RESHAPE_POLYGONS, null );
	new CoCheckBoxMenuItemAdaptor(
		new CoValueModelWrapper( CoCurveValueModels.POLYGON_HANDLE_MODE )
		{
			public void valueChange( CoValueChangeEvent ev )
			{
				super.valueChange( ev );
				getWorkspace().getSelectionManager().repaint();
			}
		},
		cbi );

	cbi = createCheckboxMenuItem( b, m_itemMenu, MENU_ITEM_STICKY_CURVE_POINTS, null );
	new CoCheckBoxMenuItemAdaptor(
		new CoValueModelWrapper( CoCurveValueModels.BEZIER_CURVE_STICKY_POINTS_MODE )
		{
			public void valueChange( CoValueChangeEvent ev )
			{
				super.valueChange( ev );
//				getWorkspace().repaint();
			}
		},
		cbi );

	{
		CoMenu m2 = m_itemMenu.addSubMenu( MENU_ITEM_CURVE_CONTINUITY );

		CoButtonGroup bg = new CoButtonGroup();

		cbi = createCheckboxMenuItem( b, m2, MENU_ITEM_CURVE_CONTINUITY_0, null );
		bg.add( cbi );
		new CoCheckBoxMenuItemAdaptor(
			new CoValueModelWrapper( CoCurveValueModels.BEZIER_CURVE_CONTINUITY_0 )
			{
				public void valueChange( CoValueChangeEvent ev )
				{
					super.valueChange( ev );
//					getWorkspace().repaint();
				}
			},
			cbi );

		cbi = createCheckboxMenuItem( b, m2, MENU_ITEM_CURVE_CONTINUITY_1, null );
		bg.add( cbi );
		new CoCheckBoxMenuItemAdaptor(
			new CoValueModelWrapper( CoCurveValueModels.BEZIER_CURVE_CONTINUITY_1 )
			{
				public void valueChange( CoValueChangeEvent ev )
				{
					super.valueChange( ev );
//					getWorkspace().repaint();
				}
			},
			cbi );

		cbi = createCheckboxMenuItem( b, m2, MENU_ITEM_CURVE_CONTINUITY_2, null );
		bg.add( cbi );
		new CoCheckBoxMenuItemAdaptor(
			new CoValueModelWrapper( CoCurveValueModels.BEZIER_CURVE_CONTINUITY_2 )
			{
				public void valueChange( CoValueChangeEvent ev )
				{
					super.valueChange( ev );
//					getWorkspace().repaint();
				}
			},
			cbi );
	}

	m_itemMenu.addSeparator();
	updateCustomOperationMenu();
	
}
protected void createKeyboardBinding( ActionListener l, int keyCode, int modifiers )
{
	createKeyboardBinding( getPanel(), l, keyCode, modifiers );
	createKeyboardBinding( m_workspace, l, keyCode, modifiers );
	createKeyboardBinding( m_modifyPane, l, keyCode, modifiers );
}
protected static void createKeyboardBinding( JComponent c, ActionListener l, int keyCode, int modifiers )
{
	CoGUI.createKeyboardBinding( c, l, KeyStroke.getKeyStroke( keyCode, modifiers ), ! true );
}

protected void createListeners()
{
	super.createListeners();



	
	m_toolDispatcher.addListener(
		new CoToolDispatcher.Listener()
		{
			public void activeToolChanged( CoTool oldTool, CoTool newTool )
			{
				Enumeration e = m_toolButtonGroup.getElements();
				while
					( e.hasMoreElements() )
				{
					ToolToggleButton b = (ToolToggleButton) e.nextElement();

					if
						( b.getTool() == newTool )
					{
						m_toolButtonGroup.setSelected( b.getModel(), true );		
						b.repaint();
						break;
					}
				}
			}
		}
	);
	
	
	m_workspace.addMouseListener( m_toolDispatcher );
	m_workspace.addMouseMotionListener( m_toolDispatcher );
	m_workspace.addKeyListener( m_toolDispatcher );

	
	// object menu handler
	m_workspace.addMouseListener( 
		new MouseAdapter()
		{
			public void mouseClicked( MouseEvent e ) { handlePopupMenu( e ); }
			public void mousePressed( MouseEvent e ) { handlePopupMenu( e ); }
			public void mouseReleased( MouseEvent e ) { handlePopupMenu( e ); }
		}
	);


	// selection listener
	m_workspace.getSelectionManager().addSelectionListener( 
		new CoViewSelectionManager.SelectionListener()
		{
			public void selectionChanged( CoViewSelectionManager.SelectionChangedEvent e )
			{
				CoViewSelectionManager p = (CoViewSelectionManager) e.getSource();
				CoShapePageItemView v = p.getSelectedView();
				setModifyPaneModel( v );
				updateEnableDisableState();
			}
		}
	);


	createKeyboardBinding( 
		new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				deletePageItems();
			}
		},
		KeyEvent.VK_DELETE,
		KeyEvent.VK_UNDEFINED );

	
	createKeyboardBinding(
		new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				deletePageItems();
			}
		},
		KeyEvent.VK_BACK_SPACE,
		KeyEvent.VK_UNDEFINED );



	
	createKeyboardBinding(
		new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				m_toolDispatcher.setActiveTool( null );
			}
		},
		KeyEvent.VK_F1,
		KeyEvent.VK_UNDEFINED );
	
	createKeyboardBinding(
		new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				m_toolDispatcher.setActiveTool( m_contentTool );
			}
		},
		KeyEvent.VK_F1,
		Event.SHIFT_MASK );


	
	createKeyboardBinding(
		new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				m_toolDispatcher.nextTool();
			}
		},
		KeyEvent.VK_F2,
		KeyEvent.VK_UNDEFINED );
	
	createKeyboardBinding(
		new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				m_toolDispatcher.previousTool();
			}
		},
		KeyEvent.VK_F2,
		Event.SHIFT_MASK );

	createKeyboardBinding(
		new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				m_pageItemToolbar.getXTextField().requestFocus();
			}
		},
		KeyEvent.VK_F3,
		KeyEvent.VK_UNDEFINED );

	createKeyboardBinding(
		new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				m_pageItemToolbar.getDerivedCheckBox().requestFocus();
			}
		},
		KeyEvent.VK_F3,
		Event.SHIFT_MASK );

	createKeyboardBinding(
		new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				m_pageItemToolbar.getXScaleTextField().requestFocus();
			}
		},
		KeyEvent.VK_F3,
		Event.CTRL_MASK );

	createKeyboardBinding(
		new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				m_paragraphTagUI.open();
			}
		},
		KeyEvent.VK_F11,
		KeyEvent.VK_UNDEFINED );



	
	class _ implements ActionListener
	{
		private String [] m_tabNames;
		
		public _( String tabName )
		{
			m_tabNames = new String [] { tabName };
		}
		
		public _( String [] tabNames )
		{
			m_tabNames = tabNames;
		}
		
		public void actionPerformed( ActionEvent e ) 
		{
			for ( int i = 0; i < m_tabNames.length; i++ ) openModifyDialog( m_tabNames[ i ] );
		}
	};


	createKeyboardBinding( new _( CoPageItemPane.GEOMETRY_TAB ), KeyEvent.VK_G, Event.CTRL_MASK | Event.ALT_MASK );
	createKeyboardBinding( new _( CoPageItemPane.FILL_TAB ), KeyEvent.VK_F, Event.CTRL_MASK | Event.ALT_MASK );
	createKeyboardBinding( new _( CoPageItemPane.STROKE_TAB ), KeyEvent.VK_I, Event.CTRL_MASK | Event.ALT_MASK );
	createKeyboardBinding( new _( CoPageItemPane.LAYOUT_TAB ), KeyEvent.VK_L, Event.CTRL_MASK | Event.ALT_MASK );
	createKeyboardBinding( new _( CoPageItemPane.GRID_TAB ), KeyEvent.VK_K, Event.CTRL_MASK | Event.ALT_MASK );
	createKeyboardBinding( new _( CoPageItemPane.RUN_AROUND_SPEC_TAB ), KeyEvent.VK_R, Event.CTRL_MASK | Event.ALT_MASK );
	createKeyboardBinding( new _( CoPageItemPane.CHILD_LAYOUT_ORDER_TAB ), KeyEvent.VK_O, Event.CTRL_MASK | Event.ALT_MASK );
	createKeyboardBinding( new _( CoPageItemPane.CHILD_Z_ORDER_TAB ), KeyEvent.VK_Z, Event.CTRL_MASK | Event.ALT_MASK );
	createKeyboardBinding( new _( CoPageItemPane.WORKPIECE_TEXT_TAB ), KeyEvent.VK_A, Event.CTRL_MASK | Event.ALT_MASK );
	createKeyboardBinding( new _( new String [] { CoPageItemPane.IMAGE_TAB,
		                                            CoPageItemPane.TEXT_TAB,
		                                            CoPageItemPane.LAYOUT_AREA_TAB } ),
	                       KeyEvent.VK_C,
	                       Event.CTRL_MASK | Event.ALT_MASK );

	createKeyboardBinding( new ActionListener()
		                     {
														public void actionPerformed( ActionEvent e ) 
														{
															m_modifyDialog.setVisible( false );
														}
		                     },
		                     KeyEvent.VK_Q,
		                     Event.CTRL_MASK | Event.ALT_MASK );



	// forced refresh for testing purposes
	createKeyboardBinding(
		new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if
					( m_workspace.hasRootView() )
				{	
					m_workspace.getRootView().refresh();
					m_workspace.repaint();
				}
			}
		},
		KeyEvent.VK_R,
		KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK | KeyEvent.ALT_MASK );



	createKeyboardBinding(
		getSubsetModelAction(),
		KeyEvent.VK_Q,
		Event.CTRL_MASK | Event.ALT_MASK );

	createKeyboardBinding(
		getUnsubsetModelAction(),
		KeyEvent.VK_A,
		Event.CTRL_MASK | Event.ALT_MASK );

}

public CoMenuBar createMenuBar()
{
	CoMenuBuilder b = getMenuBuilder();


	CoMenuBar mb = b.createMenuBar();

	createFileMenu( mb, b );
	createEditMenu( mb, b );
	createStyleMenu( mb, b );
	createItemMenu( mb, b );
	createViewMenu( mb, b );
	createContentMenu( mb, b );
	createUtilitiesMenu( mb, b );
	createHelpMenu( mb, b );
	
	CoMenuExtender extender = m_configuration.getMenuExtender();
	if (extender != null)
		extender.extendMenu(mb, b);
	
	m_modelsNotEmpty.add( mb );

	updateEnableDisableState();

	return mb;
}
private static CoMenuItem createMenuItem( CoMenu menu, CoLayoutEditorClientConstants.MenuItem resources, CoEnableDisableManager edm )
{
	CoMenuItem menuItem = menu.add(resources);
//	CoMenuItem menuItem = b.addMenuItem( menu, CoLayouteditorUIStringResources.getName( name ) );
//	menuItem.setMnemonic( CoLayouteditorUIStringResources.getChar( name + "_MNEMONIC" ) );

//	KeyStroke keyStroke = CoLayouteditorUIStringResources.getKeyStroke(name + "_KEYSTROKE");
//	if (keyStroke != null) {
//		menuItem.setAccelerator(keyStroke);
//	}

	if ( edm != null ) edm.add( menuItem );
	return menuItem;
}

protected Component createNorthWidget( CoUserInterfaceBuilder builder )
{
	return null;
}

private CoPageItemPopupMenuModel createPopupMenuModel()
{
	CoPageItemPopupMenuModel top = new CoPageItemPopupMenuModel();

	CoPageItemPopupMenuModel defaultModel = new CoPageItemPopupMenuModel();
	
	top.addSubModel( defaultModel );



	
 	 // shape page item
	CoPageItemPopupMenuModel shapePageItem =
		new CoPageItemPopupMenuModel()
		{
			Action m_modify =
				new AbstractAction( CoLayouteditorUIStringResources.getName( POPUP_SHAPE_PAGE_ITEM_MODIFY ) )
				{
					public void actionPerformed( ActionEvent ev ) { modifySelectedView(); }
				};

			Action m_spawnInNewWindow = new CoSpawnAction( CoLayouteditorUIStringResources.getName( POPUP_SHAPE_PAGE_ITEM_SPAWN_SPAWN_IN_NEW_WINDOW ), CoLayoutEditor.this );
			Action m_spawnInThisWindow = getSubsetModelAction();
				
			public void create( CoPopupMenu menu, CoPageItemView v )
			{
				menu.addAction( m_modify );
				CoSubMenu subMenu = new CoSubMenu( CoLayouteditorUIStringResources.getName( POPUP_SHAPE_PAGE_ITEM_SPAWN ) );
				menu.add( subMenu );
				subMenu.add( m_spawnInThisWindow );
				subMenu.add( m_spawnInNewWindow );
			}
		};

	defaultModel.addSubModel( CoShapePageItemView.class, shapePageItem );



	
		// text content
	CoPageItemPopupMenuModel textContent =
		new CoPageItemPopupMenuModel()
		{
			Action m_adjustToTextHeight = new CoAdjustHeightToText( CoLayouteditorUIStringResources.getName( POPUP_TEXT_CONTENT_ADJUST_HEIGHT_TO_TEXT ), CoLayoutEditor.this );

			Action m_editText =
				new CoEditText( CoLayouteditorUIStringResources.getName( POPUP_TEXT_CONTENT_EDIT ), CoLayoutEditor.this )
				{
					public void prepare( CoShapePageItemView v )
					{
						if
						 	( check( v, CoContentWrapperPageItemView.class ) )
						{
							CoPageItemContentView cv = (CoPageItemContentView) ( (CoContentWrapperPageItemView) v ).getContentView();
							if
								( check( cv, CoPageItemAbstractTextContentView.class ) )
							{
								setEnabled( ! ( (CoPageItemAbstractTextContentView) cv ).isTextLocked() );
							}
						}
					}
				};
				
			public void create( CoPopupMenu m, CoPageItemView v )
			{
				m.addAction( m_adjustToTextHeight );
				m.addAction( m_editText );
			}
		};

	defaultModel.addSubModel( CoPageItemAbstractTextContentView.class, textContent );
	



	
	 // abstract image content
	CoPageItemPopupMenuModel abstractImageContent =
		new CoPageItemPopupMenuModel()
		{
			Action m_adjustImage0 = new CoAdjustContentWidthToShapeThenHeightToScaledContent( CoLayouteditorUIStringResources.getName( POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_WITH_TO_FIT_HEIGHT_TO_IMAGE ), CoLayoutEditor.this );
			Action m_adjustImage1 = new CoAdjustContentToShapeKeepAspectRatio( CoLayouteditorUIStringResources.getName( POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_FIT_KEEP_ASPECT_RATIO ), CoLayoutEditor.this );
			Action m_adjustImage2 = new CoAdjustContentToShape( CoLayouteditorUIStringResources.getName( POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_FIT ), CoLayoutEditor.this );
			Action m_adjustImage3 = new CoAdjustToScaledContent( CoLayouteditorUIStringResources.getName( POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_SCALED_IMAGE_SIZE ), CoLayoutEditor.this );
			Action m_adjustImage4 = new CoAdjustToContent( CoLayouteditorUIStringResources.getName( POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_IMAGE_SIZE ), CoLayoutEditor.this );
			Action m_adjustImage5 = new CoAdjustToEmbeddedPath( CoLayouteditorUIStringResources.getName( POPUP_ABSTRACT_IMAGE_CONTENT_ADJUST_TO_EMBEDDED_PATH ), CoLayoutEditor.this );
			Action m_importImage = new CoImportImage( CoLayouteditorUIStringResources.getName( POPUP_IMAGE_CONTENT_GET_PICTURE ), CoLayoutEditor.this );
				
			public void create( CoPopupMenu m, CoPageItemView v )
			{
				m.addAction( m_adjustImage0 );
				m.addAction( m_adjustImage1 );
				m.addAction( m_adjustImage2 );
				m.addAction( m_adjustImage3 );
				m.addAction( m_adjustImage4 );
				m.addAction( m_adjustImage5 );
				m.addAction( m_importImage );
			}
		};

	defaultModel.addSubModel( CoPageItemImageContentView.class, abstractImageContent );


	// layout content
	CoPageItemPopupMenuModel layoutContent =
		new CoPageItemPopupMenuModel()
		{
			Action m_editLayout =
				new CoEditLayout( CoLayouteditorUIStringResources.getName( POPUP_ABSTRACT_LAYOUT_CONTENT_EDIT ), CoLayoutEditor.this )
				{
				};

				Action m_adjustLayout0 = new CoAdjustContentWidthToShapeThenHeightToScaledContent( CoLayouteditorUIStringResources.getName( POPUP_ABSTRACT_LAYOUT_CONTENT_ADJUST_WITH_TO_FIT_HEIGHT_TO_LAYOUT ), CoLayoutEditor.this );
				Action m_adjustLayout1 = new CoAdjustContentToShapeKeepAspectRatio( CoLayouteditorUIStringResources.getName( POPUP_ABSTRACT_LAYOUT_CONTENT_ADJUST_TO_FIT_KEEP_ASPECT_RATIO ), CoLayoutEditor.this );
				Action m_adjustLayout2 = new CoAdjustContentToShape( CoLayouteditorUIStringResources.getName( POPUP_ABSTRACT_LAYOUT_CONTENT_ADJUST_TO_FIT ), CoLayoutEditor.this );
				Action m_adjustLayout3 = new CoAdjustToScaledContent( CoLayouteditorUIStringResources.getName( POPUP_ABSTRACT_LAYOUT_CONTENT_ADJUST_TO_SCALED_LAYOUT_SIZE ), CoLayoutEditor.this );
				Action m_adjustLayout4 = new CoAdjustToContent( CoLayouteditorUIStringResources.getName( POPUP_ABSTRACT_LAYOUT_CONTENT_ADJUST_TO_LAYOUT_SIZE ), CoLayoutEditor.this );
				
			public void create( CoPopupMenu m, CoPageItemView v )
			{
				m.addAction( m_adjustLayout0 );
				m.addAction( m_adjustLayout1 );
				m.addAction( m_adjustLayout2 );
				m.addAction( m_adjustLayout3 );
				m.addAction( m_adjustLayout4 );
				m.addAction( m_editLayout );
			}
		};

	defaultModel.addSubModel( CoPageItemLayoutContentView.class, layoutContent );

	return top;
}
protected CoRootView createRootView( CoDesktopLayoutAreaIF desktop ) 
{
	CoRootView v = new CoRootView( desktop );

	return v;
}
protected void createSelectionTools( CoToolbarDockingBay db )
{
	CoToolbar toolbar = new CoToolbar();
	db.add( toolbar );

	
	Icon icon = getIcon( "selection.gif" );
	CoTool tool = new CoSelectionTool( this );
	toolbar.add( createToolButton( null, icon, tool ) );

	m_toolDispatcher.manageTool( tool );
	m_toolDispatcher.setDefaultTool( tool );
	m_toolDispatcher.setActiveTool( null );

	
	icon = getIcon( "content.gif" );
	m_contentTool = new CoContentTool( this );
	toolbar.add( createToolButton( null, icon, m_contentTool ) );

	m_toolDispatcher.manageTool( m_contentTool );
}
protected Component createSouthWidget( CoUserInterfaceBuilder b )
{
	CoPanel p = b.createPanel( new CoAttachmentLayout() );

	// docking bay
	CoHorizontalToolbarDockingBay bay = new CoHorizontalToolbarDockingBay();
	m_pageItemToolbar = new CoPageItemToolbar( b, getCommandExecutor() );
	CoToolbar tb = new CoToolbar();
	tb.add( m_pageItemToolbar );
	bay.add( tb, 0 );
	bay.addToolbar( m_textStyleToolbars.m_tagToolBar, 0 );
	bay.addToolbar( m_textStyleToolbars.m_fontToolBar, 0 );
	bay.addToolbar( m_textStyleToolbars.m_styleToolBar, 0 );

		
	// status row
	CoPanel statusPanel = b.createPanel( new CoRowLayout( true ) );

	m_zoomPanel = new CoZoomPanel( b, CoLayouteditorUIStringResources.getName( ZOOM ), 1, null, null );
	statusPanel.add( m_zoomPanel );

	m_statusText = b.createSlimTextField();
	m_statusText.setBorder( BorderFactory.createLoweredBevelBorder() );
	m_statusText.setEditable( false );
	statusPanel.add( m_statusText, CoRowLayout.FILL );


	
	p.add( bay,
			   new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_NO ),
								                             new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_TOP, 0, statusPanel ),
								                             new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_CONTAINER ),
								                             new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER ) ) );

	p.add( statusPanel,
			   new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_NO ),
								                             new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_CONTAINER ),
								                             new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_CONTAINER ),
								                             new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER ) ) );

	
	m_zoomPanel.setZoomable(
		new CoZoomPanel.Zoomable()
		{
			public void setScale( double scale )
			{
				setZoomScale( scale / 100.0 );
			}
			
			public double getScale()
			{
				return m_workspace.getUserScale() * 100;
			}
		}
	);

	return p;
}
protected void createStyledTextEditor() 
{
	{
		CoTabSetPanel.TabSetEditor e =
			new CoTabSetPanel.TextEditorTabSetEditor()
			{
				protected CoAbstractTextEditor getTextEditor()
				{
					return m_styledTextEditor;
				}
			};
		
		m_textRuler = new CoTextRulerPane( e )
		{
			public void paint( Graphics g )
			{
				Graphics2D g2d = (Graphics2D) g;

				AffineTransform t = g2d.getTransform();
				m_workspace.applyTransform( g2d );
				
				super.paint( g );

				g2d.setTransform( t );
			}
		};
	}
	
	m_textRuler.setBackground( Color.white );
	m_textRuler.setForeground( Color.black );
	m_workspace.add( m_textRuler );

	
	m_styledTextEditor = new CoStyledTextEditor( null )
	{
		public void paint( Graphics g )
		{
			Graphics2D g2d = (Graphics2D) g;

			AffineTransform t = g2d.getTransform();
			m_workspace.applyTransform( g2d );
			
			super.paint( g );

			g2d.setTransform( t );
		}

		// The following method is declared to do nothing here in order
		// to disable autoscroll of text inside the layout editor. (DM/GF)
		public void scrollRectToVisible( Rectangle r ) {}

		public void setVisible( boolean v )
		{
			super.setVisible( v );
			if
				( v )
			{
				m_textRuler.setBounds( getX(), getY() - 20 - 1, getWidth(), 20 );
			}

			m_textRuler.setVisible( v );
		}
	};
	
	m_styledTextEditor.setOpaque( false );
	m_styledTextEditor.setVisible( false );

	m_originalDocument = m_styledTextEditor.getCoStyledDocument();
	

	m_textStyleToolbars = new CoTextStyleToolbarSet( getUIBuilder(), m_styledTextEditor.getActions() );
	m_characterStyleUI = new CoCharacterStyleActionUI( m_styledTextEditor.getActions(), null );
	m_paragraphStyleUI = new CoParagraphStyleActionUI( m_styledTextEditor.getActions(), null );
	m_characterTagUI = new CoCharacterTagUI( null );
	m_paragraphTagUI = new CoParagraphTagUI( null );
	
	m_measurementPrefs = new CoTextMeasurementPrefsUI();
	try
    {
        m_measurementPrefs.setDomain( CoTextClient.getTextServer().getTextMeasurementPrefs() );
    } catch (RemoteException e)
    {
        throw new RuntimeException(e);
    }

	m_textEditorPopupMenu = 
		new CoStyledTextPopupMenu( 
			m_styledTextEditor.getActions(),
			getMenuBuilder(),
			m_styledTextEditor,
			m_characterStyleUI,
			m_paragraphStyleUI,
			m_characterTagUI,
			m_paragraphTagUI,
			m_measurementPrefs
		);

	m_styledTextEditor.setPopupMenu( m_textEditorPopupMenu );

	m_workspace.add( m_styledTextEditor );

  m_styledTextEditor.addComponentListener(
	  new ComponentAdapter()
	  {
		  public void componentShown( ComponentEvent ev )
		  {
				if ( m_textEditorInitialEvent != null ) m_styledTextEditor.injectMouseEvent( m_textEditorInitialEvent );	  
			}
	  }
	);



}
protected void createStyleMenu( CoMenuBar mb, CoMenuBuilder b )
{
	m_textStyleMenu = new CoTextStyleMenu( m_styledTextEditor.getActions(), 
		                                     b,
	                                       null,
	                                       m_characterStyleUI,
	                                       m_paragraphStyleUI,
	                                       m_characterTagUI,
	                                       m_paragraphTagUI );

	m_textStyleMenu.setText( CoLayoutEditorClientConstants.MENU_STYLE.text() );
	m_textStyleMenu.setMnemonic(CoLayoutEditorClientConstants.MENU_STYLE.mnemonic());
	m_textEditorActive.add( m_textStyleMenu );
	b.addMenu( m_textStyleMenu );


	m_styledTextEditor.addAttributeListener( m_textStyleMenu );

}
protected CoToggleButton createToolButton( String label, Icon icon, CoTool tool )
{
	ToolToggleButton button = new ToolToggleButton( tool );
	getUIBuilder().prepareToggleButton( button );
	button.setText( label );
	button.setIcon( icon );
	button.setButtonGroup( m_toolButtonGroup );

	m_modelsNotEmpty.add( button );
	if ( tool == null ) m_alwaysDisabled.add( button );
	
	button.setFocusPainted( false );
	button.setRequestFocusEnabled( false );
	button.setFocusTraversable( false );
	button.setPreferredSize( new Dimension( 26, 26 ) );
	button.setMinimumSize( new Dimension( 26, 26 ) );
	ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
	toolTipManager.registerComponent(button);

	button.addItemListener(
		new ItemListener()
		{
			public void itemStateChanged( ItemEvent ev )
			{
				if
					( ev.getStateChange() == ItemEvent.SELECTED )
				{
					m_toolDispatcher.setActiveTool( ( (ToolToggleButton) ev.getSource() ).getTool() );
				}
			}
		}
	);

	return button;
}
protected CoVerticalToolbarDockingBay createToolsToolbar()
{
	CoVerticalToolbarDockingBay db = new CoVerticalToolbarDockingBay();

	createSelectionTools( db );
	createViewTools( db );
	createCreationTools( db );
	createEditTools( db );

	return db;
}
protected CoUserInterfaceBuilder createUserInterfaceBuilder()
{ 
	return 
		new CoUserInterfaceBuilder( this )
		{
			public void prepareTextField( JTextField t )
			{
				super.prepareTextField( t );
				CoTextField T = (CoTextField) t;
				T.setActivateWhenLosingFocus( true );
				T.setSelectWhenGainingFocus( true );
			}
			/*
			public CoComboBox createSlimComboBox()
			{
				CoComboBox cb = new CoSlimComboBox();
				prepareComboBox( cb );
				cb.setOpaque( false );
				return cb;
			}
*/
			private Insets m_insets = new Insets( 0, 0, 0, 0 );
			
			protected void prepareAbstractButton( AbstractButton b )
			{
				super.prepareAbstractButton( b );
				b.setMargin( m_insets );
			}

            /**
             */
            public CoTextFieldAdaptor createNumberFieldAdaptor(CoValueModel aValueModel, JTextField aTextField)
            {
            	return createNumberFieldAdaptor(aValueModel, aTextField, CoNumberConverter.INTEGER);
            }

            /**
             */
            public CoTextFieldAdaptor createNumberFieldAdaptor(CoValueModel aValueModel, JTextField aTextField, int formatPatternIndex)
            {
            	aTextField.setHorizontalAlignment(JTextField.RIGHT);
            	return doCreateTextFieldAdaptor(CoNumberConverter.newNumberConverter(aValueModel, formatPatternIndex), aTextField);
            }

            /**
             */
            public CoTextFieldAdaptor createNumberFieldAdaptor(CoValueModel aValueModel, JTextField aTextField, int formatPatternIndex, CoConvertibleUnitSet us)
            {
            	aTextField.setHorizontalAlignment(JTextField.RIGHT);
            	return doCreateTextFieldAdaptor(CoNumberConverter.newNumberConverter(aValueModel, us, formatPatternIndex), aTextField);
            }
		};
}
private void createUtilitiesMenu( CoMenuBar mb, CoMenuBuilder b )
{
	CoSubMenu menu = mb.addSubMenu(MENU_UTILITIES);
//	CoMenu m = b.addMenu( CoLayouteditorUIStringResources.getName( MENU_UTILITIES ) );
//	m.setMnemonic( CoLayouteditorUIStringResources.getChar( MENU_UTILITIES_MNEMONIC ) );

	createMenuItem( menu, MENU_UTILITIES_CHECK_SPELLING, null ).addActionListener( new CoCheckSpelling( CoLayoutEditor.this ) );

	createMenuItem( menu, MENU_UTILITIES_SPELL_CHECKING_OPTIONS, null ).addActionListener( new CoEditSpellingPrefs( CoLayoutEditor.this ) );

	menu.addSeparator();
	
	createMenuItem( menu, MENU_UTILITIES_SUGGESTED_HYPHENATION, null ).addActionListener( new CoEditCustumHyphenationPatterns( CoLayoutEditor.this ) );



	// FIXME: Temporarily removed since font repository manager is not yet usable...
	// Magnus Ihse <magnus.ihse@appeal.se> (2001-05-09 10:54:48)
	/*
	JMenuItem i = m.add( "fontserver" );
	b.prepareMenuItem( i );
	i.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				final CoFontManagerPanel x = new CoFontManagerPanel( getUIBuilder(), getMenuBuilder() );
				JFrame f = new JFrame();
				f.addWindowListener(
					new WindowAdapter()
					{
						public void windowClosing( WindowEvent evt ) { x.stop(); }
					}
				);
				f.getContentPane().add( x );
				f.pack();

				x.start();
				f.show();
			}
		}
	);
	*/
}
protected Component createWestWidget( CoUserInterfaceBuilder builder )
{
	Component c = createToolsToolbar();

	JPanel p = new JPanel( new CoAttachmentLayout() );

	m_treePathPane = new CoPageItemTreePathPane( m_workspace );//.getRootView() );
	m_treePathPane.setBorder( BorderFactory.createBevelBorder( javax.swing.border.BevelBorder.LOWERED ) );

	p.add( c,
		     new CoAttachmentLayout.Attachments(
			     new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER ),
			     new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER ),
			     new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_TOP, 0, m_treePathPane )
		     )
		   );

	p.add( m_treePathPane,
		     new CoAttachmentLayout.Attachments(
			     new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_CONTAINER ),
			     new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER )
		     )
		   );


	return p;

}
protected void createWidgets( CoPanel p, CoUserInterfaceBuilder builder )
{
	super.createWidgets( p, builder );

	createWorkspace( p );

	Component c;
	
	c = createNorthWidget( builder );
	if ( c != null ) p.add( c, BorderLayout.NORTH );
	
	c = createSouthWidget( builder );
	if ( c != null ) p.add( c, BorderLayout.SOUTH );
	
	c = createWestWidget( builder );
	if ( c != null ) p.add( c, BorderLayout.WEST );
	
	c = createEastWidget( builder );
	if ( c != null ) p.add( c, BorderLayout.EAST );
	
	
	m_modifyPane = new CoPageItemPane( builder, getCommandExecutor() );
//	m_trappingPanel = new CoPageItemTrappingPanel(new CoUserInterfaceBuilder(null));
	
	m_zoomPanel.update();
}

private void createViewMenu( CoMenuBar mb, CoMenuBuilder b)
{
	CoSubMenu m = mb.addSubMenu(MENU_VIEW);
	
	CoMenuItem i = createMenuItem( m, MENU_VIEW_OPEN_TREE_VIEW, null );
	b.prepareMenuItem( i );
	i.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				if
					( m_treeViewDialog == null )
				{
					Window w = getWindow();
					if
						( w instanceof Frame )
					{
						Frame f = (Frame) w;
						m_treeViewDialog = new CoDialog( f, "", false );
					} else {
						Dialog d = (Dialog) w;
						m_treeViewDialog = new CoDialog( d, "", false );
					}
					
					m_treeViewPane = new CoLayoutEditorPageItemTreePane( CoLayoutEditor.this );
					m_treeViewPane.setRoot( getWorkspace().getRootView() );
					m_treeViewDialog.getContentPane().add( new JScrollPane( m_treeViewPane ) );
					m_treeViewDialog.pack();
					m_treeViewDialog.setSize( m_treeViewDialog.getWidth() * 2, m_treeViewDialog.getHeight() * 2 );
				}
				
				m_treeViewDialog.show();
			}
		}
	);

	m.addSeparator();
	
	i = createMenuItem( m, MENU_VIEW_FIT_IN_WINDOW, null );
	i.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent ev ) { fitInWindow(); } } );

	i = createMenuItem( m, MENU_VIEW_FIT_SELECTION_IN_WINDOW, null );
	i.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent ev ) { fitSelectionInWindow(); } } );

	i = createMenuItem( m, MENU_VIEW_50_PROC, null );
	i.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent ev ) { setZoomScale( 0.5 ); } } );

	i = createMenuItem( m, MENU_VIEW_75_PROC, null );
	i.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent ev ) { setZoomScale( 0.75 ); } } );

	i = createMenuItem( m, MENU_VIEW_ACTUAL_SIZE, null );
	i.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent ev ) { setZoomScale( 1 ); } } );

	i = createMenuItem( m, MENU_VIEW_200_PROC, null );
	i.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent ev ) { setZoomScale( 2 ); } } );

	m.addSeparator();


	class RenderingHintValueModel extends CoValueModel
	{
		private Object m_key;
		private Object m_trueValue;
		private Object m_falseValue;

		public RenderingHintValueModel( Object key, Object trueValue, Object falseValue )
		{
			m_key = key;
			m_trueValue = trueValue;
			m_falseValue = falseValue;
		}
		
		public void setValue( Object o )
		{
			m_renderingHints.put( m_key, ( (Boolean) o ).booleanValue() ? m_trueValue : m_falseValue );
			getWorkspace().repaint();
		}
		public Object getValue()
		{
			return ( m_renderingHints.get( m_key ) == m_trueValue ) ? Boolean.TRUE : Boolean.FALSE;
		}
	};

	
	CoCheckboxMenuItem cbi = createCheckboxMenuItem( b, m, MENU_VIEW_SHOW_GUIDES, null );
	new CoCheckBoxMenuItemAdaptor( new RenderingHintValueModel( CoPageItemViewRenderer.PAINT_COLUMN_GRID, CoPageItemViewRenderer.PAINT_COLUMN_GRID_ON, CoPageItemViewRenderer.PAINT_COLUMN_GRID_OFF ), cbi );

	cbi = createCheckboxMenuItem( b, m, MENU_VIEW_SHOW_OUTLINES, null );
	new CoCheckBoxMenuItemAdaptor( new RenderingHintValueModel( CoPageItemViewRenderer.PAINT_OUTLINE, CoPageItemViewRenderer.PAINT_OUTLINE_ON, CoPageItemViewRenderer.PAINT_OUTLINE_OFF ), cbi );

	cbi = createCheckboxMenuItem( b, m, MENU_VIEW_SHOW_PAGE_OUTLINES, null );
	new CoCheckBoxMenuItemAdaptor( new RenderingHintValueModel( CoPageItemViewRenderer.PAINT_MODEL_OUTLINE, CoPageItemViewRenderer.PAINT_MODEL_OUTLINE_ON, CoPageItemViewRenderer.PAINT_MODEL_OUTLINE_OFF ), cbi );

	cbi = createCheckboxMenuItem( b, m, MENU_VIEW_SHOW_BASELINE_GRID, null );
	new CoCheckBoxMenuItemAdaptor( new RenderingHintValueModel( CoPageItemViewRenderer.PAINT_BASE_LINE_GRID, CoPageItemViewRenderer.PAINT_BASE_LINE_GRID_ON, CoPageItemViewRenderer.PAINT_BASE_LINE_GRID_OFF ), cbi );

	cbi = createCheckboxMenuItem( b, m, MENU_VIEW_SHOW_INVISIBLES, null );
	new CoCheckBoxMenuItemAdaptor( new RenderingHintValueModel( CoTextRenderingHints.PAINT_FORMAT_CHARACTERS, CoTextRenderingHints.PAINT_FORMAT_CHARACTERS_ON, CoTextRenderingHints.PAINT_FORMAT_CHARACTERS_OFF ), cbi );

	cbi = createCheckboxMenuItem( b, m, MENU_VIEW_SHOW_DUMMY_TEXT, null );
	new CoCheckBoxMenuItemAdaptor( new RenderingHintValueModel( CoTextRenderingHints.PAINT_DUMMY_TEXT, CoTextRenderingHints.PAINT_DUMMY_TEXT_ON, CoTextRenderingHints.PAINT_DUMMY_TEXT_OFF ), cbi );

	cbi = createCheckboxMenuItem( b, m, MENU_VIEW_SHOW_TEXT_OVERFLOW_INDICATOR, null );
	new CoCheckBoxMenuItemAdaptor( new RenderingHintValueModel( CoTextEditorIF.PAINT_OVERFLOW_INDICATOR, CoTextEditorIF.PAINT_OVERFLOW_INDICATOR_ON, CoTextEditorIF.PAINT_OVERFLOW_INDICATOR_OFF ), cbi );

	cbi = createCheckboxMenuItem( b, m, MENU_VIEW_PAINT_IMAGES, null );
	new CoCheckBoxMenuItemAdaptor( new RenderingHintValueModel( CoPageItemViewRenderer.PAINT_IMAGE_PLACEHOLDERS, CoPageItemViewRenderer.PAINT_IMAGE_PLACEHOLDERS_OFF, CoPageItemViewRenderer.PAINT_IMAGE_PLACEHOLDERS_ON ), cbi );

	cbi = createCheckboxMenuItem( b, m, MENU_VIEW_SHOW_IMAGE_CLIP_INDICATOR, null );
	new CoCheckBoxMenuItemAdaptor( new RenderingHintValueModel( CoPageItemViewRenderer.PAINT_IMAGE_CLIP_INDICATOR, CoPageItemViewRenderer.PAINT_IMAGE_CLIP_INDICATOR_ON, CoPageItemViewRenderer.PAINT_IMAGE_CLIP_INDICATOR_OFF ), cbi );

	cbi = createCheckboxMenuItem( b, m, MENU_VIEW_SHOW_TOPICS, null );
	new CoCheckBoxMenuItemAdaptor( new RenderingHintValueModel( CoPageItemViewRenderer.PAINT_TOPICS, CoPageItemViewRenderer.PAINT_TOPICS_ON, CoPageItemViewRenderer.PAINT_TOPICS_OFF ), cbi );

	b.addSeparator( m );

	cbi = createCheckboxMenuItem( b, m, MENU_VIEW_TEXT_ANTIALIASING, null );
	new CoCheckBoxMenuItemAdaptor( new RenderingHintValueModel( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF ), cbi );

	cbi = createCheckboxMenuItem( b, m, MENU_VIEW_TEXT_FRACTIONALMETRICS, null );
	new CoCheckBoxMenuItemAdaptor( new RenderingHintValueModel(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON, RenderingHints.VALUE_FRACTIONALMETRICS_OFF), cbi );

	/*
	cbi = createCheckboxMenuItem( b, m, MENU_VIEW_SHOW_CHILD_LOCK_INDICATOR", null );
	new CoCheckBoxMenuItemAdaptor( new RenderingHintValueModel( CoPageItemViewRenderer.PAINT_CHILD_LOCK_INDICATOR, CoPageItemViewRenderer.PAINT_CHILD_LOCK_INDICATOR_ON, CoPageItemViewRenderer.PAINT_CHILD_LOCK_INDICATOR_OFF ), cbi );
	*/

	b.addSeparator( m );

	cbi = createCheckboxMenuItem( b, m, MENU_VIEW_SNAP_TO_GUIDES, null );
	new CoCheckBoxMenuItemAdaptor(
		new CoValueModel( MENU_VIEW_SNAP_TO_GUIDES )
		{
			public void setValue( Object o )
			{
				m_snapToGrid = ( (Boolean) o ).booleanValue();
			}
			public Object getValue()
			{
				return m_snapToGrid ? Boolean.TRUE : Boolean.FALSE;
			}
		},
		cbi );

	b.addSeparator( m );

	cbi = createCheckboxMenuItem( b, m, MENU_VIEW_SHOW_ENTIRE_DESKTOP, null );
	new CoCheckBoxMenuItemAdaptor(
		new CoValueModel( MENU_VIEW_SHOW_ENTIRE_DESKTOP )
		{
			public void setValue( Object o )
			{
				getWorkspace().getRootView().setDoShowEntireDesktop( ( (Boolean) o ).booleanValue() );
			}
			public Object getValue()
			{
				if ( ! getWorkspace().hasRootView() ) return Boolean.FALSE;
				return getWorkspace().getRootView().getDoShowEntireDesktop() ? Boolean.TRUE : Boolean.FALSE;
			}
		},
		cbi );

	b.addSeparator( m );

	{
		m_viewRendererMenu = m.addSubMenu( MENU_VIEW_LOOK );
		updateViewRendererMenu();
	}
	
}
	/**
	 * Skapa verktygspalettens UI
	 */
protected void createViewTools( CoToolbarDockingBay db )
{
	CoToolbar toolbar = new CoToolbar();
	db.addToolbar( toolbar, 0 );

	
	Icon icon = getIcon( "zoom.gif" );
	CoTool t = new CoZoomTool( this );
	toolbar.add( createToolButton( null, icon, t ) );

	m_toolDispatcher.manageTool( t );

	
	icon = getIcon( "scrollhand.gif" );
	t = new CoScrollTool( this );
	toolbar.add( createToolButton( null, icon, t ) );

	m_toolDispatcher.manageTool( t );
}


protected void createWorkspace(CoPanel panel) {
	m_workspace = new CoPageItemEditorPanel() {
		public void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.addRenderingHints(m_renderingHints);
			super.paintComponent(g);
		}

		protected void screenScaleChanged() {
			super.screenScaleChanged();
			m_zoomPanel.update();
		}
	};

	getUIBuilder().preparePanel(m_workspace);

	m_workspace.setOpaque(true);
	m_workspace.setBackground(Color.white);

	JScrollPane sp =
		new JScrollPane(m_workspace, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS) {
		public Dimension getPreferredSize() {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			double height = screenSize.getHeight() - 30;
			double width = 500;
			return new Dimension((int)width, (int)height);
		}
	};

	sp.getViewport().putClientProperty("EnableWindowBlit", Boolean.TRUE);
	//	sp.getViewport().setBackingStoreEnabled(true);
	//	sp.setDoubleBuffered(true);

	sp.setCorner(JScrollPane.LOWER_RIGHT_CORNER, createScrollboardButton());

	m_splitPane = getUIBuilder().createSplitPane(false);
	m_splitPane.setOneTouchExpandable(true);
	m_splitPane.setLeftComponent(sp);
	m_splitPaneDividerSize = m_splitPane.getDividerSize();
	if (m_eastWidget == null) {
		m_splitPane.setDividerSize(0);
	} else {
		m_splitPane.setRightComponent(m_eastWidget);
	}

	panel.add(m_splitPane, BorderLayout.CENTER);

	m_workspace.addMouseListener(new MouseAdapter() {
		public void mouseEntered(MouseEvent e) {
			handleMouseEnteredWorkspace();
		}
	});

	createStyledTextEditor();

	m_modelsNotEmpty.add(m_workspace);

	createCustomGridUI();
}

private void cutPageItems()
{
	copyPageItems();
	deletePageItems();
}
private void deletePageItems()
{
	int I = m_workspace.getSelectionManager().getSelectedViewCount();
	if ( I == 0 ) return;

	boolean atLeastOne = false;
	CoShapePageItemIF [] pi = new CoShapePageItemIF [ I ];
	for
		( int i = 0; i < I; i++ )
	{
		CoShapePageItemView v = m_workspace.getSelectionManager().getSelectedView( i ).getView();
		if
			( ! getWorkspace().getRootView().isModelView( v ) )
		{
			pi[ i ] = v.getShapePageItem();
			atLeastOne = true;
		}
	}

	if ( ! atLeastOne ) return;
	
	CoPageItemCommands.REPARENT_PAGE_ITEMS.prepare( "DELETE PAGE ITEMS", pi, pi[ 0 ].getParent(), null, null, null );
	getCommandExecutor().doit( CoPageItemCommands.REPARENT_PAGE_ITEMS, null );
}
public void dispose()
{
	// clear all page item listeners
	setModels( (List) null, null );

	getWorkspace().setRootView( null );
	/*
	if
		( getWorkspace().hasRootView() )
	{
		CoPageItemView root = getWorkspace().getRootView();
		CoUtilities.removePageItemListeners( root );
		root.dispose();
	}
*/
	m_styledTextEditor.dispose();
}
protected void doAfterCreateUserInterface( ) 
{
	super.doAfterCreateUserInterface();

	CoLengthUnit.LENGTH_UNITS.addPropertyChangeListener(
		new CoPropertyChangeListener()
		{
			public void propertyChange( CoPropertyChangeEvent ev )
			{
				m_modifyPane.valueHasChanged();
//				m_trappingPanel.valueHasChanged();
				m_pageItemToolbar.valueHasChanged();
			}
		}
	);

	updateConfiguration();
	updateContext();
	updateModels();
}
private void doCopyTool( final CoAbstractCreateTool tool )
{
	CoCommand c = new CoCommand( "ADD_PAGE_ITEM_TOOL" )
	{
		public boolean doExecute()
		{
			CoPageItemPrototypeIF p = tool.getPrototype();
			p = m_context.getTools().copyPageItemPrototype( p, null );
			p.setDeleteable( true );
			
			return true;
		}
	};

	getCommandExecutor().doit( c, m_context.getTools() );
	
}

private void doDeleteTool( final CoAbstractCreateTool tool )
{
	CoCommand c = new CoCommand( "DELETE_PAGE_ITEM_TOOL" )
	{
		public boolean doExecute()
		{
			m_context.getTools().removePageItemPrototype( tool.getPrototype() );
			return true;
		}
	};


	getCommandExecutor().doit( c, m_context.getTools() );
	
}
protected void doExecute( final CoPageItemOperationIF op )
{
	final List views = new ArrayList();

	Iterator i = m_workspace.getSelectionManager().getSelectedViews();
	while
		( i.hasNext() )
	{
		views.add( i.next() );
	}

	CoPageItemCommands.PERFORM_CUSTOM_OPERATION.prepare( op, CoLayoutEditor.this, views );
	getCommandExecutor().doit( CoPageItemCommands.PERFORM_CUSTOM_OPERATION, null );
	/*
	CoCommand c = new CoCommand( op.getName() )
	{
		public boolean doExecute()
		{
			return op.execute( CoLayoutEditor.this, views );
		}
	};

	getCommandExecutor().doit( c, null );
	*/
}
private void doMoveToolDown( final CoAbstractCreateTool tool )
{
	/*
	CoCommand c = new CoCommand( "DELETE_PAGE_ITEM_TOOL" )
	{
		public boolean doExecute()
		{
			m_application.getTools().removePageItemPrototype( tool.getPrototype() );
			return true;
		}
	};


	getCommandManager().doit( c, m_application.getTools() );
	*/
}
private void doMoveToolUp( final CoAbstractCreateTool tool )
{
	/*
	CoCommand c = new CoCommand( "DELETE_PAGE_ITEM_TOOL" )
	{
		public boolean doExecute()
		{
			m_application.getTools().removePageItemPrototype( tool.getPrototype() );
			return true;
		}
	};


	getCommandManager().doit( c, m_application.getTools() );
	*/
}

public void doubleClicked()
{
	modifySelectedView();
}


public void fitInWindow()
{
	m_workspace.setUserScale( 1.0 );
	
	Dimension d1 = m_workspace.getModelSize();
	if ( d1 == null ) return;

	Dimension d2 = m_workspace.getParent().getSize();
	if ( ( d2.width == 0 ) || ( d2.height == 0 ) ) return;
	
	double s1 = (double) ( d2.width - 20 ) / (double)( d1.width );
	double s2 = (double) ( d2.height - 20 ) / (double) ( d1.height );

	double s = Math.min( s1, s2 );
	
	setZoomScale( s );

	// center scrollbars
	JScrollBar sb = ( (JScrollPane) m_workspace.getParent().getParent() ).getHorizontalScrollBar();
	sb.setValue( ( sb.getMaximum() - sb.getMinimum() - sb.getVisibleAmount() ) / 2 );
	
	sb = ( (JScrollPane) m_workspace.getParent().getParent() ).getVerticalScrollBar();
	sb.setValue( ( sb.getMaximum() - sb.getMinimum() - sb.getVisibleAmount() ) / 2 );
}
public void fitSelectionInWindow()
{
	double x0 = Double.MAX_VALUE;
	double y0 = Double.MAX_VALUE;
	double x1 = Double.MIN_VALUE;
	double y1 = Double.MIN_VALUE;
	Point2D p1 = new Point2D.Double();
	Point2D p2 = new Point2D.Double();
	
	Iterator i = m_workspace.getSelectionManager().getSelectedViews();
	while
		( i.hasNext() )
	{
		CoShapePageItemView v = (CoShapePageItemView) i.next();
		p1.setLocation( 0, 0 );
		p2.setLocation( v.getWidth(), v.getHeight() );
		v.transformToGlobal( p1 );
		v.transformToGlobal( p2 );
		x0 = Math.min( x0, p1.getX() );
		y0 = Math.min( y0, p1.getY() );
		x1 = Math.max( x1, p2.getX() );
		y1 = Math.max( y1, p2.getY() );
	}

	if
		( x0 != Double.MAX_VALUE )
	{
		zoomTo( x0, y0, x1 - x0, y1 - y0, 20 );
	}
}
public CoPageItemAbstractTextContentView getActiveTextContentView()
{
	return m_activeTextContentView;
}
protected CoPageItemView getAndClearPopupMenuOwner()
{
	CoPageItemView tmp = m_popupMenuOwner;
	m_popupMenuOwner = null;
	
	return tmp;
}


public CoPageItemEditorContextIF getContext()
{
	return m_context;
}
protected CoPageItemBoundedContentView getCurrentBoundedContentView()
{
	return (CoPageItemBoundedContentView) getCurrentPageItemContentView( CoPageItemBoundedContentView.class );
}

protected CoCompositePageItemView getCurrentCompositePageItemView()
{
	return (CoCompositePageItemView) getCurrentPageItemView( CoCompositePageItemView.class );
}
protected CoContentWrapperPageItemView getCurrentContentWrapperView()
{
	return (CoContentWrapperPageItemView) getCurrentPageItemView( CoContentWrapperPageItemView.class );
}
protected CoPageItemImageContentView getCurrentImageContentView()
{
	return (CoPageItemImageContentView) getCurrentPageItemContentView( CoPageItemImageContentView.class );
}
protected CoPageItemLayoutContentView getCurrentLayoutContentView()
{
	return (CoPageItemLayoutContentView) getCurrentPageItemContentView( CoPageItemLayoutContentView.class );
}
protected CoPageItemContentView getCurrentPageItemContentView( Class c )
{
	CoContentWrapperPageItemView cw = getCurrentContentWrapperView();

	if ( cw == null ) return null;

	CoPageItemContentView v = cw.getContentView();

	return c.isInstance( v ) ? v : null;
}
protected CoPageItemView getCurrentPageItemView( Class c )
{
	CoPageItemView v = getAndClearPopupMenuOwner();

	if
		( v == null )
	{
		v = m_workspace.getSelectionManager().getSelectedView();
	}

	return c.isInstance( v ) ? v : null;
}

protected CoPageItemAbstractTextContentView getCurrentTextContentView()
{
	return (CoPageItemAbstractTextContentView) getCurrentPageItemContentView( CoPageItemAbstractTextContentView.class );
}
protected Insets getDefaultPanelInsets()
{
	return null;
}
/**
 * getWindow method comment.
 */
public String getDefaultWindowTitle()
{
	if
		( m_name != null )
	{
		return CoLayouteditorUIStringResources.getName( "LAYOUTEDITOR" ) + " - " + m_name;
	} else {
		return CoLayouteditorUIStringResources.getName("LAYOUTEDITOR");
	}
}
private CoEditTextTool getEditTextTool( CoPageItemAbstractTextContentView v, MouseEvent initialEvent )
{
	if
		( m_editTextTools == null )
	{
		m_editTextTools = new CoEditTextTool [ 2 ];
		m_editTextTools[ 0 ] = new CoEditTextTool( m_contentTool, this );
		m_editTextTools[ 1 ] = new CoEditTextTool( m_contentTool, this );
	}

	// make sure a different tool than the active one is used
	CoEditTextTool t = m_editTextTools[ 0 ];
	if ( m_toolDispatcher.getActiveTool() == t ) t = m_editTextTools[ 1 ];
	
	t.setView( v );
	t.setInitialEvent( initialEvent );

	return t;
}

protected Icon getIcon(String fileName)
{
	return CoResourceLoader.loadIcon( CoLayoutClient.class, fileName);
}
protected CoLayerDialog getLayerDialog()
{
	if
		( m_layerDialog == null )
	{
		Window w = getWindow();
		if
			( w instanceof Frame )
		{
			Frame f = (Frame) w;
			m_layerDialog = new CoLayerDialog( f, this );
		} else {
			Dialog d = (Dialog) w;
			m_layerDialog = new CoLayerDialog( d, this );
		}

		m_layerDialog.pack();
	}
	
	return m_layerDialog;
}
/**
 * Return a list of <code>CoLayoutEditorModel</code>
 */
public List getModels() // [ CoLayoutEditorModel ]
{
	return m_models;
}


/**
 * Return a list of <code>CoNamedViewable</code>. One for each page. <br>
 * Previously the getModel method was used to access views that could be
 * use for PostScript generation. But the views from this method has a
 * state that is affected by their usage in the present layout editor and
 * that lead to unwanted result in the PostScript generation. <br>
 * Instead of trying to manipulate and restore the state in these views
 * we created this method that instantiate new views. This will cost since
 * the views is created at the server side and we should therefor find a way to use
 * existing views as we did before or redesign how printing is done to awoid 
 * unneccesary instantiation and transportation of views. / Göran S 2002-10-17 
 */
public List getNamedViewables () {
	List namedViewables = new ArrayList(m_models.size());
	Iterator iter = m_models.iterator();
	while (iter.hasNext()) {
		final CoLayoutEditorModel layoutEditorModel = (CoLayoutEditorModel)iter.next();
		namedViewables.add(new CoNamedViewable() {
			public String getName() {
				return layoutEditorModel.getName();
			}
			
			public CoView getView() {
				return layoutEditorModel.createView();
			}
			
		});
	}
	return namedViewables; 
}


protected CoDialog getModifyDialog()
{
	if
		( m_modifyDialog == null )
	{
		Window w = getWindow();
		if
			( w instanceof Frame )
		{
			Frame f = (Frame) w;
			m_modifyDialog = new CoDialog( f, "", false );
		} else {
			Dialog d = (Dialog) w;
			m_modifyDialog = new CoDialog( d, "", false );
		}
		
		m_modifyDialog.getContentPane().add( m_modifyPane );
		m_modifyDialog.pack();
//		m_modifyPane.selectTab()
	}

	return m_modifyDialog;
}
public boolean getSnapToGrid()
{
	return m_snapToGrid;
}

private CoSubsetModel getSubsetModelAction()
{
	Iterator i = m_externalUIAction.iterator();
	while
		( i.hasNext() )
	{
		Object o = i.next();
		if
			( o instanceof CoSubsetModel )
		{
			return (CoSubsetModel) o;
		}
	}

	return new CoSubsetModel( CoLayouteditorUIStringResources.getName( POPUP_SHAPE_PAGE_ITEM_SPAWN_SPAWN_IN_THIS_WINDOW ), this );
}
public CoStyledTextEditor getTextEditor()
{
	return m_styledTextEditor;
}

private CoUnsubsetModel getUnsubsetModelAction()
{
	Iterator i = m_externalUIAction.iterator();
	while
		( i.hasNext() )
	{
		Object o = i.next();
		if
			( o instanceof CoUnsubsetModel )
		{
			return (CoUnsubsetModel) o;
		}
	}

	return new CoUnsubsetModel( this );
}
public CoPageItemEditorPanel getWorkspace()
{
	return m_workspace;
}
/**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
private void handleException(Throwable exception) {

	/* Uncomment the following lines to print uncaught exceptions to stderr */
 	System.err.println("--------- UNCAUGHT EXCEPTION ---------");
 	exception.printStackTrace(System.err);
}

protected void handleMouseEnteredWorkspace()
{
// Is this realy neccesary? It leads to a very strange behavior. /Göran S.
//	if ( ! m_styledTextEditor.hasFocus() ) 
//		m_workspace.requestFocus();
}

private void handlePopupMenu( MouseEvent e )
{
	if ( ! m_workspace.isEnabled() ) return;
	m_workspace.requestFocusInWindow();
	if ( ! e.isPopupTrigger() ) return;

	if
		( ( e.getModifiers() & InputEvent.CTRL_MASK ) != 0 )
	{
		performQuickZoom( e );
		return;
	}

	// find target view
	CoRootView rv = m_workspace.getRootView();
	Point2D p = CoAbstractTool.getLocation( e );
	
	CoShapePageItemView v = rv.findTopMostViewContaining( p, rv.getChildren(), true, false, 1 );
	if ( v == null ) v = rv;
	CoImmutableCustomGridIF g = v.getCustomGrid();

	if
		( g != null )
	{
		m_xCustomGridline = rv.findClosestXCustomGridLine( v, e.getPoint(), CoSelectedView.HANDLE_RANGE );
		m_yCustomGridline = rv.findClosestYCustomGridLine( v, e.getPoint(), CoSelectedView.HANDLE_RANGE );
		
		if
			( ! Double.isNaN( m_xCustomGridline ) || ! Double.isNaN( m_yCustomGridline ) )
		{
			// popup custom gridline textfield
			if
				( ! Double.isNaN( m_xCustomGridline ) )
			{
				m_customGridlineTextField.setText( CoLengthUnitSet.format( m_xCustomGridline, CoLengthUnit.LENGTH_UNITS ) );
			} else {
				m_customGridlineTextField.setText( CoLengthUnitSet.format( m_yCustomGridline, CoLengthUnit.LENGTH_UNITS ) );
			}

			m_customGrid = v.getMutableCustomGrid();
			m_customGridlinePopup.show( m_workspace, e.getX(), e.getY() );
			m_customGridlineTextField.requestFocus();
			return;
		}
	}
	
	v = rv.findTopMostViewContaining( p, null, false, false, -1 );
	CoShapePageItemView v2 = v;
	while
		( v2 != null )
	{
		if
			( getWorkspace().getSelectionManager().isSelected( v2 ) )
		{
			// found selected parent -> parent is target
			v = v2;
			break;
		}
		v2 = v2.getParent();
	}

	
	if ( v == getWorkspace().getRootView() ) return; // root view has no popup menu

	postPopupMenu( e, m_workspace, v );
}

public final boolean isTextEditorActive()
{
	return m_styledTextEditor.isVisible();
}
private void modifySelectedView()
{
	CoShapePageItemView v = getCurrentShapePageItemView();

	if
		( v == null )
	{
		setModifyPaneModel( null );
	} else {
		getWorkspace().getSelectionManager().select( v, true );
	}
	
//	getWorkspace().repaint();
	
	openModifyDialog( null );
}
private void modifyTool( CoAbstractCreateTool t )
{
	if ( ( m_modifyPane.getDomain() != null ) && ( m_modifyPane.getDomain().getPageItemId().equals( t.getPrototype().getPageItemId() ) ) ) return;

	CoShapePageItemView v = t.getView();
	
	m_modifyPane.setPrototype( t.getPrototype(), v );

	openModifyDialog( null );
}
private void openLayerDialog()
{
	getLayerDialog().show();
}
private void openModifyDialog( String tabName )
{
	if
		( tabName != null )
	{
		m_modifyPane.selectTab( tabName );
	}
	getModifyDialog().show();
}
private void openWorkspaceScrollBoardDialog() 
{
	if
		( m_workspaceScrollBoardDialog == null )
	{
		JScrollPane sp = (JScrollPane) m_workspace.getParent().getParent();

		CoScrollBoardPanel sbp =
			new CoScrollBoardPanel( true, sp )
			{
				protected void paintModelDetails( Graphics g )
				{
					super.paintModelDetails( g );
					
					g.setColor( Color.lightGray );
					double s = getWorkspace().getScreenScale() * m_scale;
					Iterator i = getWorkspace().getRootView().getModelViews().iterator();
					while
						( i.hasNext() )
					{
						CoShapePageItemView v = (CoShapePageItemView) i.next();
						CoFixedPositionAdapter a = (CoFixedPositionAdapter) v.getAdapter();
						g.fillRect( (int) Math.round( a.getChildX() * s ),
							          (int) Math.round( a.getChildY() * s ),
							          (int) Math.round( v.getWidth() * s ),
							          (int) Math.round( v.getHeight() * s ) );
					}
				}

				protected void zoom( double scale )
				{
					setZoomScale( scale * m_workspace.getUserScale() );
				}
			};


		if
			( m_workspace.getTopLevelAncestor() instanceof Dialog )
		{
			m_workspaceScrollBoardDialog = new CoDialog( (Dialog) m_workspace.getTopLevelAncestor(), "" );
		} else {
			m_workspaceScrollBoardDialog = new CoDialog( (Frame) m_workspace.getTopLevelAncestor(), "" );
		}
			
		m_workspaceScrollBoardDialog.getContentPane().add( sbp );

		int w = 200;
		Dimension2D d = getWorkspace().getRootView().getPreferredSize();
		m_workspaceScrollBoardDialog.setSize( w, (int) ( w * ( d.getHeight() / d.getWidth() ) ) );
		m_workspaceScrollBoardDialog.setLocation( m_workspace.getParent().getParent().getLocationOnScreen() );
	}

	m_workspaceScrollBoardDialog.setVisible( true );

}
private void pastePageItems()
{
	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	Transferable content = clipboard.getContents( this );

	List pageItems = null;
	Rectangle2D bounds = null;
	
	if
		( content != null )
	{
		try
		{
			CoPageItemTransferable pit = (CoPageItemTransferable) content.getTransferData( CoPageItemTransferable.FLAVOR );
			pageItems = pit.getPageItems();
			bounds = pit.getBounds();
		}
		catch( Exception e )
		{
			m_workspace.getToolkit().beep();
			return;
		}
	}

	
	
	CoRootView rv = m_workspace.getRootView();
	Point2D p = m_toolDispatcher.getMousePosition();
	
	CoCompositePageItemView v = (CoCompositePageItemView) rv.findTopMostViewContaining( p, null, true, false, -1 );

	if ( v == null ) v = rv;

	
	CoCompositePageItemIF newParent = v.getCompositePageItem();
	
	int I = pageItems.size();
	CoShapePageItemIF [] pi = new CoShapePageItemIF [ I ];

	if ( I == 0 ) return;

	m_workspace.untransform( p );
	v.transformFromGlobal( p );
	double dx = p.getX() - bounds.getX();
	double dy = p.getY() - bounds.getY();
	
	for
		( int i = 0; i < I; i++ )
	{
		pi[ i ] = (CoShapePageItemIF) ( (CoShapePageItemIF) pageItems.get( i ) ).deepClone();
		m_workspace.getRootView().addRecentlyCreatedPageItem( pi[ i ] );
		pi[ i ].translate( dx, dy );
	}
	
	CoPageItemCommands.REPARENT_PAGE_ITEMS.prepare( "PASTE PAGE ITEM", pi, null, null, newParent, null );

	m_workspace.getSelectionManager().unselectAllViews();

	getCommandExecutor().doit( CoPageItemCommands.REPARENT_PAGE_ITEMS, null );
}
private void performQuickZoom( MouseEvent e )
{
	if
		( ( e.getModifiers() & InputEvent.SHIFT_MASK ) != 0 )
	{
		setZoomScale( m_workspace.getUserScale() + 0.25 );
	} else if
		( ( e.getModifiers() & InputEvent.ALT_MASK ) != 0 )
	{
		setZoomScale( m_workspace.getUserScale() - 0.25 );
	} else {
		Point2D p = CoAbstractTool.getLocation( e );
		m_workspace.untransform( p );
		m_workspace.getRootView().transformFromGlobal( p );

		double s = m_workspace.getUserScale();
		for
			( int i = 0; i < QUICK_ZOOM_STATES.length; i++ )
		{
			if
				( s == QUICK_ZOOM_STATES[ i ] )
			{
				m_quickZoomState = i;
				break;
			}
		}
		
		m_quickZoomState += 1;
		if ( m_quickZoomState == QUICK_ZOOM_STATES.length ) m_quickZoomState = 0;
		setZoomScale( QUICK_ZOOM_STATES[ m_quickZoomState ] );

		m_workspace.getRootView().transformToGlobal( p );
		m_workspace.transform( p );
		
		JScrollBar hsb = ( (JScrollPane) m_workspace.getParent().getParent() ).getHorizontalScrollBar();
		JScrollBar vsb = ( (JScrollPane) m_workspace.getParent().getParent() ).getVerticalScrollBar();
		
		hsb.setValue( (int) p.getX() - hsb.getVisibleAmount() / 2 );
		vsb.setValue( (int) p.getY() - vsb.getVisibleAmount() / 2 );
	}
}
void postPopupMenu( MouseEvent e, Component owner, CoShapePageItemView v )
{
	// get menu
	CoPopupMenu m = (CoPopupMenu) m_popupMenus.get( v.getPopupMenuKey() );

	// no such menu, create it
	if
		( m == null )
	{
		m = new CoPopupMenu();
		CoLabel title = new CoLabel( "" );
		title.setAlignmentX( 0.5f );
		m.add( title );
		m.addSeparator();
		
		m_popupMenuModel.create( m, v );
		m.addSeparator();
		if
			( m.getComponentCount() > 2 )
		{
			addPopupMenu( m, v.getPopupMenuKey() );
		} else {
			m = null;
		}
	}

	// prepare menu
	if
		( m != null )
	{
		CoLabel title = (CoLabel) m.getComponent( 0 );
		title.setText( " " + v/*.getShapePageItem()*/.getName() + " " );
		
		setPopupMenuOwner( v );

		if
			( m_createTemplateMenuItem != null )
		{	
			m.add( m_createTemplateMenuItem );
			m_createTemplateMenuItem.setEnabled( !(v.isSlave()  || v instanceof CoPageLayoutAreaView) );
		}
			
		Iterator i = m_popupMenuPrepareables.iterator();
		while
			( i.hasNext() )
		{
			( (CoPrepareable) i.next() ).prepare( v );
		}

		// post menu
		m.show( owner, e.getX(), e.getY() );
	}

}

public void prepareFrame(CoFrame frame ) 
{
	super.prepareFrame(frame);
	((JPanel)frame.getContentPane()).setDoubleBuffered(false);
}
void register( CoExternalUIAction a )
{
	m_externalUIAction.add( a );
}
private void selectAllPageItems()
{
	CoViewSelectionManager sm = m_workspace.getSelectionManager();
	
	sm.beginSelectionTransaction();

	sm.unselectAllViews();
	
	Iterator i = m_workspace.getRootView().getChildren().iterator();
	while
		( i.hasNext() )
	{
		sm.select( (CoShapePageItemView) i.next() );
	}

	sm.endSelectionTransaction();
	
	m_workspace.repaint();
}
public void setContext( CoPageItemEditorContextIF context )
{
	if ( m_context == context ) return;
	
	if
		( m_contextListener == null )
	{
		m_contextListener =
			new CoChangedObjectListener()
			{
				public void serverObjectChanged( CoChangedObjectEvent e )
				{
					updateContext();
					m_workspace.repaint();
				}
			};
	}
	
	if
		( m_toolListener == null )
	{
		m_toolListener =
			new CoChangedObjectListener()
			{
				public void serverObjectChanged( CoChangedObjectEvent e )
				{
					updateCreationTools();
				}
			};
	}
	
	if
		( m_context != null )
	{
		if ( m_context.getPreferences() != null ) CoObservable.removeChangedObjectListener( m_contextListener, m_context.getPreferences() );
		if ( m_context.getPreferences().getPageSizeCollection() != null ) CoObservable.removeChangedObjectListener( m_contextListener, m_context.getPreferences().getPageSizeCollection() );
		if ( m_context.getTools() != null ) CoObservable.removeChangedObjectListener( m_toolListener, m_context.getTools() );
	}


	m_context = context;

	
	updateContext();
	updateCreationTools();

	if
		( m_context != null )
	{
		if ( m_context.getPreferences() != null ) CoObservable.addChangedObjectListener( m_contextListener, m_context.getPreferences() );
		if ( m_context.getPreferences().getPageSizeCollection() != null ) CoObservable.addChangedObjectListener( m_contextListener, m_context.getPreferences().getPageSizeCollection() );
		if ( m_context.getTools() != null ) CoObservable.addChangedObjectListener( m_toolListener, m_context.getTools() );
	}
}



private void setModifyPaneModel( CoShapePageItemView v )
{
	m_treePathPane.setView( v );

	if ( v == m_modifyPane.getDomain() ) return;

	CoDialog d = getModifyDialog();
	m_modifyPane.setDomain( v );
	d.pack();
	d.setSize( d.getWidth(), d.getHeight() + 50 ); // compensate for bug in JTabbedPane
	d.validate();

	m_pageItemToolbar.setDomain( v );
/*
	if
		( ! m_workspace.getRootView().isModelView( v ) )
	{
		m_pageItemToolbar.setDomain( v );
	} else {
		m_pageItemToolbar.setDomain( null );
	}
	*/
}
protected void setPopupMenuOwner( CoPageItemView o )
{
	m_popupMenuOwner = o;
}

public void setZoomScale( double scale )
{
	if
		( scale == 0 )
	{
		fitInWindow();
	} else {
		JScrollPane sp = (JScrollPane) m_workspace.getParent().getParent();
		JScrollBar hsb = sp.getHorizontalScrollBar();
		JScrollBar vsb = sp.getVerticalScrollBar();

		Point2D p = new Point2D.Float( hsb.getValue() + hsb.getVisibleAmount() / 2, vsb.getValue() + vsb.getVisibleAmount() / 2 );
		m_workspace.setSupressPainting(true);
		m_workspace.untransform( p );
		
		m_workspace.setUserScale( scale );

		m_workspace.transform( p );
		
		hsb.setValue( (int) p.getX() - hsb.getVisibleAmount() / 2 );
		vsb.setValue( (int) p.getY() - vsb.getVisibleAmount() / 2 );
		m_workspace.setSupressPainting(false);
	}
}
private void showKbShortcuts()
{
	Rectangle bounds = new Rectangle(0,0,400, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
	URL url = CoResourceLoader.getURL(CoLayoutEditor.class, CoLayouteditorUIStringResources.getName(KEYBOARD_SHORTCUTS_HELP_URL));
	showHTMLwindow(url, m_kbShortcutsWindow, bounds);
}

public static void showHTMLwindow(URL url, JFrame frame, Rectangle bounds) {
	if (frame == null) {
		frame = new JFrame();

		try {
			JEditorPane p = new JEditorPane(url);
			p.setEditable(false);
			frame.getContentPane().add(new JScrollPane(p));
			frame.setBounds(bounds);
		} catch (IOException ex) {
			CoAssertion.assertTrue(false, ex.toString());
		}

	}
	
	frame.show();
	frame.setState(JFrame.NORMAL);
}


/*
 * Anropas när användaren klickat med content-verktyget i ett textblock
 */
boolean startTextEditing( final CoPageItemAbstractTextContentView v, MouseEvent initialEvent )
{
	boolean b = startTextEditing( v, m_styledTextEditor, true );

	if
		( b )
	{
		m_activeTextContentView = v;
		
		m_textStyleMenu.setEditor( m_styledTextEditor );
		m_textStyleToolbars.setEditor( m_styledTextEditor );
		m_characterStyleUI.setEditor( m_styledTextEditor );
		m_paragraphStyleUI.setEditor( m_styledTextEditor );
		m_characterTagUI.setEditor( m_styledTextEditor );
		m_paragraphTagUI.setEditor( m_styledTextEditor );
		m_textRuler.setEditor( m_styledTextEditor );

		CoImmutableStyledDocumentIF context = m_styledTextEditor.getCoStyledDocument();//v.getDocument();
	
	  m_textStyleToolbars.m_tagToolBar.setContext( context );
	  m_textStyleToolbars.m_fontToolBar.setContext( context );
	  m_textStyleMenu.setContext( context );
	  m_textEditorPopupMenu.setContext( context );
	  m_characterStyleUI.setContext( context );
	  m_paragraphStyleUI.setContext( context );
	  m_characterTagUI.setContext( context );
	  m_paragraphTagUI.setContext( context );

	  m_textEditorInitialEvent = initialEvent;
	}
		
	return b;
}
protected boolean startTextEditing( CoPageItemAbstractTextContentView v, final CoAbstractTextEditor textEditor, final boolean isOnWorkspace )
{
	if (CoAssertion.TRACE) CoAssertion.trace("startTextEditing: "+"                               "+m_activeTextContentView);

	if
		( m_activeTextContentView != null )
	{
		stopTextEditing( m_activeTextContentView );
	}
	
	
	final CoPageItemAbstractTextContentView V = v;
	
	class StartTextEditSession extends CoCommand
	{
		public StartTextEditSession()
		{
			super( "START TEXT EDITING" );
		}
		
		public boolean m_success;
		
		public boolean doExecute()
		{
			m_success = V.startEdit( (CoTextEditorIF) textEditor, isOnWorkspace );
			return true;
		}
	};
	

	boolean success = true;
	
	try
	{
		StartTextEditSession t = new StartTextEditSession();
		CoCmdKit.doInTransaction( t);
		CoObservable.objectChanged(V);
		success = t.m_success;
	}
	catch ( CoTransactionException ex )
	{
		CoAssertion.assertTrue( false, "Start text editing failed:\n" + ex.toString() );
		success = false;
	}

	if
		( success )
	{
		textEditor.setVisible( true );
		textEditor.requestFocus();

		updateEnableDisableState();
		
		return true;
	} else {
		if (CoAssertion.TRACE) CoAssertion.trace( "startTextEditing failed ");
		return false;
	}
}
/*
 * Anropas när användaren klickat avselekterat ett textblock
 */
public void stopTextEditing( CoPageItemAbstractTextContentView v )
{
	stopTextEditing( v, m_styledTextEditor );
}
/*
 * Anropas när användaren klickat avselekterat ett textblock
 */
public void stopTextEditing( CoPageItemAbstractTextContentView v, final CoAbstractTextEditor tp )
{
	if ( CoAssertion.TRACE ) CoAssertion.trace( "stopTextEditing: " );

	m_activeTextContentView = null;
	
	final CoPageItemAbstractTextContentView V = v;

	CoCommand c = new CoCommand( "STOP TEXT EDITING" )
	{
		public boolean doExecute()
		{
			V.stopEdit( (CoTextEditorIF) tp );
			return true;
		}
	};

	try
	{
		CoCmdKit.doInTransaction( c );
		CoObservable.objectChanged(V);
	}
	catch ( CoTransactionException ex )
	{
		CoAssertion.assertTrue( false, "Stop text editing failed:\n" + ex.toString() );
	}

	tp.setCoStyledDocument( m_originalDocument );

	if ( m_textEditorPopupMenu.isVisible() ) m_textEditorPopupMenu.setVisible( false );
	if ( m_textStyleMenu.isVisible() ) m_textStyleMenu.setPopupMenuVisible( false );
	
	m_textStyleMenu.setEditor( null );
	m_textStyleToolbars.setEditor( null );
	m_characterStyleUI.setEditor( null );
	m_paragraphStyleUI.setEditor( null );
	m_characterTagUI.setEditor( null );
	m_paragraphTagUI.setEditor( null );
	m_textRuler.setEditor( null );

	m_textStyleToolbars.m_tagToolBar.setContext( null );
	m_textStyleToolbars.m_fontToolBar.setContext( null );
	m_textStyleMenu.setContext( null );
 	m_textEditorPopupMenu.setContext( null );
 	m_characterStyleUI.setContext( null );
 	m_paragraphStyleUI.setContext( null );
 	m_characterTagUI.setContext( null );
 	m_paragraphTagUI.setContext( null );

	m_workspace.requestFocus();
	
	updateEnableDisableState();
}
protected final void updateContext()
{
	if ( m_workspace == null ) return;


	// protect domain object by temporary disconnecting it
	CoShapePageItemView tmp = m_modifyPane.getDomain();
	m_modifyPane.setDomain( null );
	m_pageItemToolbar.setDomain( null );

	// update ui components
	CoPageItemEditorContextIF context = getContext();
	
	Iterator i = m_externalUIAction.iterator();
	while
		( i.hasNext() )
	{
		( (CoExternalUIAction) i.next() ).setContext( context );
	}

	m_modifyPane.setContext( context );

	// reconnect
	m_modifyPane.setDomain( tmp );
	m_pageItemToolbar.setDomain( tmp );


	
	if
		( m_prototypeTreeUI != null )
	{
		m_prototypeTreeUI.setDomain( ( m_context == null ) ? null : m_context.getPrototypes() );
	}


	updateEnableDisableState();
	
	if
		( m_workspace.hasRootView() )
	{	
		m_workspace.getRootView().refresh();
	}
}
protected void updateCreationTools()
{
	if ( m_creationToolbar == null ) return;
	
	Iterator i = Arrays.asList( m_creationToolbar.getComponents() ).iterator();
	while
		( i.hasNext() )
	{
		ToolToggleButton b = (ToolToggleButton) i.next();
		m_toolDispatcher.unmanageTool( b.getTool() );
	}
	
	m_creationToolbar.removeAll();
	
	i = m_context.getTools().getPageItemPrototypes().iterator();
	while
		( i.hasNext() )
	{
		createCreationTool( (CoPageItemPrototypeIF) i.next() );
	}

	getPanel().revalidate();
}
protected void updateEnableDisableState()
{
	if ( getWorkspace() == null ) return;
	
	int I = m_enableDisableManagers.length;

	for
		( int i = 0; i < I; i++ )
	{
		m_enableDisableManagers[ i ].update();
	}
}
/**
 * Updates the on-screen representation of the layout editor contents.
 */
protected final void updateModels()
{
	if ( m_workspace == null ) return;
	if ( m_context == null ) return;
	if ( m_desktop == null ) return;


	if
		( getWorkspace().hasRootView() )
	{
		if
			( ! getWorkspace().getRootView().getPageItemId().equals( m_desktop.getId() ) )
		{
			// different desktop -> create new root view
			CoPageItemViewRendererFactory f = getWorkspace().getRootView().getRendererFactory();
			CoRootView rv = createRootView( m_desktop );
			m_workspace.setRootView( rv );
			rv.setRenderer( f );
		}
	} else {
		// no root view -> create one
		CoRootView rv = createRootView( m_desktop );
		m_workspace.setRootView( rv );
		rv.setRenderer( (CoPageItemViewRendererFactory) m_configuration.getRendererFactories().get( 0 ) );
	}
		
	
	m_workspace.getRootView().setModels( m_models );

	if ( m_layerDialog != null ) m_layerDialog.refresh();
	
	updateEnableDisableState();

	m_workspace.getRootView().refresh();

	if ( m_treeViewPane != null ) m_treeViewPane.setRoot( m_workspace.getRootView() );

	fitInWindow();
}
private void updateUndoMenuItem()
{
	/*
	CoUndoCommand c = m_commandManager.getTopUndo();

	String s = m_undoMenuItem.getText();
	int i = s.indexOf( " - " );
	if ( i >= 0 ) s = s.substring( 0, i );
	
	if
		( c != null )
	{
		s += " - " + c.getName();
	}
		
	m_undoMenuItem.setText( s );
	*/
}

public void zoomTo( double x, double y, double w, double h, int margin )
{
	m_workspace.setSupressPainting(true);
	JScrollPane sp = (JScrollPane) m_workspace.getParent().getParent();
	JScrollBar hsb = sp.getHorizontalScrollBar();
	JScrollBar vsb = sp.getVerticalScrollBar();
	
	Dimension d = m_workspace.getParent().getSize();

	double s = Math.min( ( d.width - margin ) / w, ( d.height - margin ) / h );

	Point2D p = new Point2D.Double( x + w / 2, y + h / 2 );

	m_workspace.setScreenScale( s );

	if
		( m_workspace.getParent().getParent() instanceof JScrollPane )
	{
		m_workspace.transform( p );

		hsb.setValue( (int) p.getX() - hsb.getVisibleAmount() / 2 );
		vsb.setValue( (int) p.getY() - vsb.getVisibleAmount() / 2 );
	}
	m_workspace.setSupressPainting(false);
	// m_workspace.repaint();
}



private void createCustomGridUI() 
{
	m_customGridlineTextField = new CoTextField( 15 );
	m_customGridlineTextField.setActivateWhenLosingFocus( false );
	m_customGridlineTextField.setSelectWhenGainingFocus( true );
	m_customGridlineTextField.setSize( m_customGridlineTextField.getPreferredSize() );

	m_customGridlinePopup = new CoPopupMenu();
	m_customGridlinePopup.add( m_customGridlineTextField );

	m_customGridlineTextField.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				try
				{
					final double p = CoLengthUnitSet.parse( m_customGridlineTextField.getText(), CoLengthUnit.LENGTH_UNITS );

					CoMoveCustomGridLineCommand c = null;
					
					if
						( ! Double.isNaN( m_xCustomGridline ) )
					{
						if
							( p != m_xCustomGridline )
						{
							CoPageItemCommands.MOVE_CUSTOM_GRID_LINE.prepare( m_customGrid, m_xCustomGridline, p, Double.NaN, Double.NaN );
							c = CoPageItemCommands.MOVE_CUSTOM_GRID_LINE;

						}
					} else {
						if
							( p != m_yCustomGridline )
						{
							CoPageItemCommands.MOVE_CUSTOM_GRID_LINE.prepare( m_customGrid, Double.NaN, Double.NaN, m_yCustomGridline, p );
							c = CoPageItemCommands.MOVE_CUSTOM_GRID_LINE;
						}
					}
					if ( c != null ) getCommandExecutor().doit( c, null );
					
				}
				catch ( ParseException ex )
				{
				}

				m_xCustomGridline = Double.NaN;
				m_yCustomGridline = Double.NaN;
				
				m_customGridlinePopup.setVisible( false );
				m_workspace.repaint();
			}
		}
	);

}

private CoButton createScrollboardButton() 
{
	CoButton b =
		new CoButton()
		{
			protected void paintComponent( Graphics g )
			{
				super.paintComponent( g );

				Insets i = getInsets();
				int w = getWidth() - i.left - i.right - 1;
				int h = getHeight() - i.top - i.bottom - 1;
				g.setColor( Color.white );
				g.fillRect( i.left + 1, i.top + 1, w, h );
				g.setColor( Color.black );
				g.drawRect( i.left + 1 + w / 4, i.top + 1 + h / 4, w / 2, h / 2 );
			}
		};
	b.setBorder( BorderFactory.createRaisedBevelBorder() );
	b.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				openWorkspaceScrollBoardDialog();
			}
		}
	);
	
	return b;
}

protected CoShapePageItemView getCurrentShapePageItemView()
{
	return (CoShapePageItemView) getCurrentPageItemView( CoShapePageItemView.class );
}




public void checkDesktop()
{
	if
		( m_workspace.hasRootView() )
	{
		m_workspace.getRootView().checkDesktop( m_desktop );
	}
}

public CoDesktopLayoutAreaIF getDesktop()
{
	return m_desktop;
}

public void setModel( CoShapePageItemIF pi, CoDesktopLayoutAreaIF desktop )
{
	List tmp = new ArrayList();
	
	if
		( pi != null )
	{
		tmp.add( new CoLayoutEditorModel( pi, pi.getName() ) );
	}
	
	setModels( tmp, desktop );
}

public void setModel( CoLayoutEditorModel model, CoDesktopLayoutAreaIF desktop )
{
	List tmp = new ArrayList();
	
	if
		( model != null )
	{
		tmp.add( model );
	}
	
	setModels( tmp, desktop );
}

public void setModels( List models, CoDesktopLayoutAreaIF desktop ) // [ CoLayoutEditorModel ]
{
	m_models = models;
	m_desktop = desktop;

	updateModels();
}





public CoUndoableCommandExecutor getCommandExecutor()
{
	return m_commandExecutor;
}



public void setStatusText( String str )
{
	m_statusText.setText( str );
}


public PageFormat getPageFormat() {
	if (m_pageFormat == null) {
		m_pageFormat = new PageFormat();
	}

	return m_pageFormat;
}


public void setPageFormat(PageFormat pageFormat) {
	m_pageFormat = pageFormat;
}



public PrintRequestAttributeSet getPrintRequestAttributeSet() {
	return m_printRequestAttributeSet;
}


public void setPrintRequestAttributeSet(PrintRequestAttributeSet printRequestAttributeSet) {
	m_printRequestAttributeSet = printRequestAttributeSet;
}


public CoLayoutEditor( CoLayoutEditorConfiguration config, String name )
{
	super();

	m_name = name;

	setConfiguration( config );
}





private void createTemplateMenuItem()
{
	if ( m_contentMenu == null ) return;
	if ( getContext() == null ) return;
	if ( getContext().getPrototypes() == null ) return;

	CoPageItemPrototypeTreeNodeRIF ps = getContext().getPrototypes();
	if
		( ps != null )
	{
		final String title = CoLayouteditorUIStringResources.getName( TEMPLATES );

		CoLayoutEditorDialog d = CoLayoutEditor.createLayoutEditorDialog( m_configuration );

		CoPageItemFactoryIF f = (CoPageItemFactoryIF) CoFactoryManager.getFactory( CoPageItemFactoryIF.PAGE_ITEM_FACTORY );
		List l = new ArrayList();
		l.add( f.createTextBox() );
		l.add( f.createNoContentBox() );
		l.add( f.createWorkPieceTextBox() );
		l.add( f.createImageBox() );		
		l.add( f.createLayoutArea() );

		CoUIContext uic =
			new CoUIContext()
			{
				public Object getStateFor( String key )
				{
					if
						( key.equals( CoPageItemEditorContextIF.KEY ) )
					{
						return m_context;
					} else if
						( key.equals( CoDesktopLayoutAreaIF.DESKTOP_LAYOUT ) )
					{
						return m_context.getPreferences().getDesktopLayoutArea();
					} else if
						( key.equals( CoLayoutEditorConfiguration.LAYOUT_EDITOR_CONFIGURATION ) )
					{
						return m_configuration;
					} else {
						return null;
					}
				}
			};
		
		m_prototypeTreeUI =
			new CoPageItemPrototypeTreeUI( l, d, uic )
			{
				public String getDefaultWindowTitle()
				{
					return title;
				}
			};

		( (CoPageItemPrototypeTreeUI) m_prototypeTreeUI ).setDomain( ps );

		createExternalUIMenuItem( m_contentMenu, m_prototypeTreeUI );


		CoCommand action = new CoCommand( "CREATE_PAGE_ITEM_TEMPLATE" )
		{
			public void actionPerformed( ActionEvent e )
			{
				getCommandExecutor().doit( this, getContext().getPrototypes() );
			}

			public boolean doExecute()
			{
				CoShapePageItemIF pi = (CoShapePageItemIF) m_popupMenuOwner.getPageItem().deepClone();
				pi.clearWorkpieceProjections();
				CoPageItemPrototypeTreeRootIF collection = getContext().getPrototypes();
				collection.addTo( collection, pi.getName(), "", pi );
				return true;
			}
		};
		
		m_createTemplateMenuItem = new CoMenuItem( CoLayouteditorUIStringResources.getName( CREATE_TEMPLATE ), action );
	}
}

private void doCreatePageItemTemplate()
{
	CoCommand c = new CoCommand( "CREATE_PAGE_ITEM_TEMPLATE" )
	{
		public boolean doExecute()
		{
			CoShapePageItemIF pi = (CoShapePageItemIF) m_popupMenuOwner.getPageItem().deepClone();
			pi.clearWorkpieceProjections();
			CoPageItemPrototypeTreeRootIF collection = getContext().getPrototypes();
			collection.addTo( collection, pi.getName(), "", pi );
			return true;
		}
	};

	getCommandExecutor().doit( c, getContext().getPrototypes() );
}

public CoLayoutEditorConfiguration getConfiguration()
{
	return m_configuration;
}

public void setConfiguration( CoLayoutEditorConfiguration config )
{
	CoAssertion.assertTrue( config != null, "Layout editor config must not be null" );

	m_configuration = config;

	updateConfiguration();
}

private void updateCustomOperationMenu()
{
	if ( m_itemMenu == null ) return;
	
	int I = m_itemMenu.getItemCount() - 1;
	while
		( ( I >= 0 ) && ( m_itemMenu.getItem( I ) != null ) )
	{
		m_itemMenu.remove( I );
		I--;
	}

	CoMenuBuilder b = getMenuBuilder();
	
	// custom operations
	int N = m_configuration.getOperations().size();
	
	for
		( int n = 0; n < N; n++ )
	{
		CoPageItemOperationIF op = (CoPageItemOperationIF) m_configuration.getOperations().get( n );
		PageItemOperationAction a = new PageItemOperationAction( op );
		CoMenuItem i = b.addMenuItem( m_itemMenu, a );
		KeyStroke ks = op.getShortcut();
		if ( ks != null ) i.setAccelerator( ks );
		m_pageItemOperationEnabled.add( a );
	}

}

private void updateExternalUIsMenu()
{
	if ( m_contentMenu == null ) return;
	
	List uis = m_configuration.getExternalUIs();

	if ( uis == null ) return;
	if ( uis.isEmpty() ) return;

	getMenuBuilder().addSeparator( m_contentMenu );

	Iterator it = uis.iterator();
	while
		( it.hasNext () )
	{
		CoUserInterface ui = (CoUserInterface) it.next();
		createExternalUIMenuItem( m_contentMenu, ui );
	}

}

private void updatePopupMenuModel()
{
	m_popupMenuModel = createPopupMenuModel();

	List m = m_configuration.getPopupMenuModels();
	if
		( m != null )
	{
		m_popupMenuModel.addSubModels( m );
	}

	m_popupMenus.clear();
}



private void updateConfiguration()
{
	updatePopupMenuModel();
	updateCustomOperationMenu();
	updateExternalUIsMenu();
	updateViewRendererMenu();
	updateEnableDisableState();
}

private void updateViewRendererMenu()
{
	if ( m_viewRendererMenu == null ) return;
	
	class X implements ItemListener
	{
		CoPageItemViewRendererFactory m_f;

		public X( CoPageItemViewRendererFactory f )
		{
			m_f = f;
		}

		public void itemStateChanged( ItemEvent ev )
		{
			if ( ev.getStateChange() == ItemEvent.DESELECTED ) return;
			if
				( getWorkspace().hasRootView() )
			{	
				getWorkspace().getRootView().setRenderer( m_f );
			}
		}
	};

	CoPageItemViewRendererFactory old = getWorkspace().getRootView().getRendererFactory();
	
	m_viewRendererMenu.removeAll();
	
	CoMenuBuilder b = getMenuBuilder();

	String selected = null;

	int N = m_configuration.getRendererFactories().size();
	for
		( int n = 0; n < N; n++ )
	{
		CoPageItemViewRendererFactory f = (CoPageItemViewRendererFactory) m_configuration.getRendererFactories().get( n );
		
		CoCheckboxMenuItem cbi = createCheckboxMenuItem(b, m_viewRendererMenu, f.getName(), ' ', null, null );
		m_viewRendererFactoryButtonGroup.add( cbi );
		cbi.addItemListener( new X( f ) );
		if ( ( n == 0 ) || ( old == f ) ) selected = cbi.getActionCommand();
	}

	m_viewRendererFactoryButtonGroup.setSelected( selected );
}

public void prepareDragAndDrop()
{
	// normal DnD
	new DropTarget( getWorkspace(), new CoLayoutEditorDropTargetListener( this ) );

	// page item DnD
	new CoLayoutEditorPageItemDropTarget( this );
}

public void opened()
{
	super.opened();
	fitInWindow();
}

	protected Component m_eastWidget;

public void setEastWidget( Component c )
{
	m_eastWidget = c;

	if
		( isBuilt() )
	{
		m_splitPane.setRightComponent( m_eastWidget );
		m_splitPane.setDividerSize( m_eastWidget == null ? 0 : m_splitPaneDividerSize );
	}
}


private CoMenuItem createExternalUIMenuItem( CoMenu m, CoUserInterface ui )
{
	String name = ui.getDefaultWindowTitle();
	
	if ( ui == null ) return null;

	
	class OpenUIAction extends AbstractAction
	{
		private CoDialog m_dialog;
		private CoUserInterface m_ui;

		public OpenUIAction( CoUserInterface _ui )
		{
			m_ui = _ui;
		}
		
		public void actionPerformed( ActionEvent e )
		{
			if
				( m_dialog == null )
			{
				m_dialog = m_ui.openInDialog(CoGUI.getFrameFor(m_workspace), false);
//				m_dialog = m_ui.openInWindow();
				m_dialog.setDefaultCloseOperation( WindowConstants.HIDE_ON_CLOSE );
			} else {
//				m_dialog.setState( Frame.NORMAL );
				m_dialog.show();
			}
		}
	};
	

	return getMenuBuilder().addMenuItem( m, name + " ...", new OpenUIAction( ui ) );
}

public static CoLayoutEditorDialog createLayoutEditorDialog( final CoLayoutEditorConfiguration config )
{
	CoAssertion.assertTrue( config != null, "Layout editor config must not be null" );

	return
		new CoLayoutEditorDialog()
		{
			public CoLayoutEditor createEditor()
			{
				return new CoLayoutEditor( config, "" );
			}
				
		};
}

/**
Builds the Content Menu.
*/
private void createContentMenu( CoMenuBar mb, CoMenuBuilder b )
{
	if
		( m_contentMenu == null )
	{
		m_contentMenu = mb.addSubMenu(CoLayoutEditorClientConstants.MENU_CONTENT);
	}

	m_contentMenu.removeAll();

	createTemplateMenuItem();

	updateExternalUIsMenu();
}
}