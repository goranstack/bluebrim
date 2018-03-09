package com.bluebrim.layout.impl.client.editor;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.bluebrim.base.client.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.menus.client.*;
import com.bluebrim.observable.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.transact.shared.*;

/**
 * An UI that displays CoLayoutHolders as a palette of page item view icons.
 * It has support for selection and it also acts as a page item drag source (cloning on drop).
 * The domain object is a com.bluebrim.layout.shared.CoPageItemHolderCollectionIF.
 *
 * @Author: Dennis
 */

public class CoPageItemHolderPaletteUI extends CoDomainUserInterface implements CoContextAcceptingUI
{

	protected Insets m_insets = new Insets( 5, 5, 5, 5 );
	protected static final Point2D m_pos = new Point2D.Double( 0, 0 );

	private String m_title;

	// selection state
	protected List m_selectedHolders = new ArrayList(); // [ CoLayoutHolder ]
	protected List m_selectionListeners;
	private static Color m_selectionColor;

	protected CoPanel m_palettePanel;
	
	private MouseListener m_mouseListener;
	
	public interface SelectionListener
	{
		void selectionChanged( List selected ); // [ CoLayoutHolder ]
	};


	// see CoShapePageItemViewPane for details on decorations
	private CoViewPanel.Decoration m_viewPanelDecoration =
		new CoViewPanel.LabelDecoration()
		{
			public String getLabel( CoViewPanel p )
			{
				CoPageItemHolderPaletteViewPane pvp = (CoPageItemHolderPaletteViewPane) p;
				return pvp.getLabel();
			}
		};


	private boolean m_copyOnDrop = true;


	private static final String CANCEL_RENAME = "\0CANCEL";
	// page item view icon geometry
	public static final IconSizeProducer DEFAULT_ICON_SIZE_PRODUCER = new ConstantIconSizeProducer( new CoDimension2D( 50, 50 ) );
	private CoTransActionCommand [] m_addActions;
	private Action m_editAction;
	private List m_editedHolders = new ArrayList();
	private CoLayoutEditorDialog m_editorDialog;
	protected IconSizeProducer m_iconSizeProducer = DEFAULT_ICON_SIZE_PRODUCER;
	private boolean m_isNormalDragSource;
	private boolean m_isPageItemDragSource;
	protected CoTextField m_nameTextField;
	protected JWindow m_nameWindow;
	CoChangedObjectListener m_pageItemHolderListener =
		new CoAbstractChangedObjectListener()
		{
			public void changedServerObject( CoChangedObjectEvent e )
			{
				pageItemHolderChanged( e );
			}
		};
	private CoPopupMenu m_popupMenu;
	private Action m_postRenameWindowAction;
	private CoTransActionCommand m_removeAction;
	private Renamer m_renamer;
	private CoTextField m_renameTextField;
	private CoCommand m_touchPageItemHolderCommand;
	private CoUIContext m_uiContext;
	private int m_viewDetailMode = CoPageItemView.DETAILS_EVERYTHING;
	private WindowListener m_windowListener =
		new WindowAdapter()
		{
			public void windowClosing( WindowEvent e )
			{
				stoppedEditing( m_editorDialog.getEditor() );
			}	
		};

	public static class ConstantIconSizeProducer implements IconSizeProducer
	{
		private final Dimension2D m_iconSize;

		public ConstantIconSizeProducer( Dimension2D iconSize )
		{
			m_iconSize = iconSize;
		}
		
		public Dimension2D getIconSizeFor( CoLayoutHolder pih )
		{
			return m_iconSize;
		}
	}

	public interface IconSizeProducer
	{
		Dimension2D getIconSizeFor( CoLayoutHolder pih );
	}

	public abstract static class Renamer
	{
		protected abstract void rename( CoRenameable holder, String name );
	}
public CoPageItemHolderPaletteUI( CoObjectIF domain, CoUIContext uiContext )
{
	this( null, uiContext, CoPageItemView.DETAILS_EVERYTHING, false, false, "", null, null );

	setDomain( domain );
}
public CoPageItemHolderPaletteUI( 
	CoLayoutEditorDialog ed,
	CoUIContext uiContext,
	int viewDetailMode,
	boolean isPageItemDragSource,
	boolean isNormalDragSource,
	String title, 
	IconSizeProducer isp, 
	Insets insets
)
{
	super();

	m_viewDetailMode = viewDetailMode;
	m_isPageItemDragSource = isPageItemDragSource;
	m_isNormalDragSource = isNormalDragSource;
	
	m_title = title;
	if ( isp != null ) m_iconSizeProducer = isp;
	if ( insets != null ) m_insets = insets;

	setUIContext( uiContext );
	m_editorDialog = ( ed != null ) ? ed : createLayoutEditorDialogFromUiContext();

	m_editAction =
		new AbstractAction( "Redigera ..." )
		{
			public void actionPerformed( ActionEvent ev )
			{
				edit();
			}
		};

	m_postRenameWindowAction =
		new AbstractAction( "Namn ..." )
		{
			public void actionPerformed( ActionEvent ev )
			{
				rename();
			}
		};

}
public void addSelectionListener( SelectionListener l )
{
	if ( m_selectionListeners == null ) m_selectionListeners = new ArrayList();
	if ( ! m_selectionListeners.contains( l ) ) m_selectionListeners.add( l );
}
protected CoPanel createDefaultPanel( )
{
	CoPanel p = super.createDefaultPanel();
	p.setOpaque( false );
	return p;
}
private CoLayoutEditorDialog createLayoutEditorDialogFromUiContext()
{
	CoLayoutEditorConfiguration config = getLayoutEditorConfiguration();

	if ( config == null ) return null;
	
	return CoLayoutEditor.createLayoutEditorDialog( config );
}

private void createMouseListeners() {
	if (m_mouseListener == null) {
		m_mouseListener = new CoPopupGestureListener(m_popupMenu) {
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
					holderSelected(e);
				}
			}
			
			protected void showPopup(MouseEvent e) {
				preparePopupMenu(e);
				super.showPopup(e);
			}

		};
	}

}

/**
 * Used to be able to change the behaviour of the palette view pane
 * from being the content pane of a CoScrolledHorizontalFlowPanel to
 * be a ordinary panel in a srollpane.
 * This method should set m_palettePanel and return the CoPanel holding m_palettePanel
 * Creation date: (2001-10-30 10:52:12)
 * @return com.bluebrim.swing.client.CoPanel
 */
public JComponent createPalettePanel() {	
	CoScrolledHorizontalFlowPanel sp = new CoScrolledHorizontalFlowPanel();
	m_palettePanel = sp.getContentPane();
	return sp;
}
protected void createWidgets( CoPanel p, CoUserInterfaceBuilder b )
{
	super.createWidgets( p, b );

	//CoScrolledHorizontalFlowPanel sp = new CoScrolledHorizontalFlowPanel();
	/*m_palettePanel = new CoPanel();
	m_palettePanel.setLayout(new FlowLayout());
	//sp.setViewportView(m_palettePanel);
	JScrollPane sp= new JScrollPane(m_palettePanel,
		ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	sp.getViewport().putClientProperty( "EnableWindowBlit", Boolean.TRUE );
	*/
	JComponent sp=createPalettePanel();
	p.add( sp, BorderLayout.CENTER );
	//p.add( sp, BorderLayout.CENTER );
	
	
	m_palettePanel.setOpaque( false );
	sp.setOpaque( false );

	createPopMenu();

	createMouseListeners();
	m_palettePanel.addMouseListener( m_mouseListener );

	update();

	m_selectionColor = b.getColor( LIST_SELECTION_BACKGROUND );

}

private void createPopMenu() {
	CoMenuBuilder mb = getMenuBuilder();
	
	m_popupMenu = mb.createPopupMenu();
	if ( m_editAction != null ) mb.addPopupMenuItem( m_popupMenu, m_editAction );
	if
		( m_addActions != null )
	{
		if
			( m_addActions.length == 1 )
		{
			mb.addPopupMenuItem( m_popupMenu, m_addActions[ 0 ] );
		} else {
			CoMenu m = mb.addPopupSubMenu( m_popupMenu, "Ny" );
			for ( int i = 0; i < m_addActions.length; i++ ) mb.addMenuItem( m, m_addActions[ i ] );
		}
	}
	if ( m_removeAction != null ) mb.addPopupMenuItem( m_popupMenu, m_removeAction );
	if ( m_renamer != null ) mb.addPopupMenuItem( m_popupMenu, m_postRenameWindowAction );
}

protected final CoPageItemHolderPaletteViewPane createViewPane( CoLayoutHolder h )
{
	CoPageItemHolderPaletteViewPane vp = new CoPageItemHolderPaletteViewPane( m_pos, m_iconSizeProducer.getIconSizeFor( h ), m_insets, m_selectionColor, m_isPageItemDragSource, m_isNormalDragSource );
	
	vp.setDecoration( m_viewPanelDecoration );
	vp.setHolder( h );

	return vp;
}
protected List createViews( CoLayoutHolder h )
{
	List views = new ArrayList();
	
	Iterator i = h.getLayouts().iterator();
	while
		( i.hasNext() )
	{
		views.add( CoPageItemView.create( (CoShapePageItemIF) i.next(), null, m_viewDetailMode ) );
	}
	
	return views;
}
protected void disposeViews( CoPageItemHolderPaletteViewPane vp )
{
	Iterator pivi = vp.getViews().iterator();
	while
		( pivi.hasNext() )
	{
		CoShapePageItemView v = (CoShapePageItemView) pivi.next();
		v.dispose();
	}
}
private void edit()
{
	if
		( m_editorDialog.isVisible() )
	{
		stoppedEditing( m_editorDialog.getEditor() );
	}

	m_editorDialog.getEditor().setConfiguration( getLayoutEditorConfiguration() );
	m_editorDialog.getEditor().setContext( getPageItemEditorContext() );

	StringBuffer title = new StringBuffer();
	List models = new ArrayList( m_selectedHolders );
	m_editedHolders.clear();
	
	int I = models.size();
	for
		( int i = 0; i < I; i++ )
	{
		CoLayoutHolder h = (CoLayoutHolder) models.get( i );
		m_editedHolders.add( h );
		h = getPageItemHolderForEditing( h );
		String name = h.getName();
		models.set( i, new CoLayoutEditorModel( h.getLayouts(), name ) );
		title.append( name );
		if ( i < I - 1 ) title.append( ", " );
	}

	m_editorDialog.getEditor().setModels( models, getDesktop() );

	prepareEditor( m_editorDialog.getEditor() );
	
	m_editorDialog.open( getPanel(), title.toString() );
	m_editorDialog.getCoDialog().addWindowListener( m_windowListener );
}
protected void fireSelectionChanged()
{
	if ( m_selectionListeners == null ) return;
	Iterator i = m_selectionListeners.iterator();
	while
		( i.hasNext() )
	{
		( (SelectionListener) i.next() ).selectionChanged( m_selectedHolders );
	}
}
public CoGenericUIContext getCopyOfCurrentRequiredUIContext()
{
	return null;
}
protected Insets getDefaultPanelInsets( )
{
	return null;
}
public String getDefaultWindowTitle()
{
	return ( m_title != null ) ? m_title : super.getDefaultWindowTitle();
}
protected CoDesktopLayoutAreaIF getDesktop()
{
	if ( m_uiContext == null ) return null;

	return (CoDesktopLayoutAreaIF) m_uiContext.getStateFor( CoDesktopLayoutAreaIF.DESKTOP_LAYOUT );
}
private CoLayoutHolderCollection getHolders()
{
	return (CoLayoutHolderCollection) getDomain();
}
protected CoLayoutEditorConfiguration getLayoutEditorConfiguration()
{
	if ( m_uiContext == null ) return null;

	Object x = m_uiContext.getStateFor( CoLayoutEditorConstants.LAYOUT_EDITOR_CONFIGURATION );
	if ( x == null ) return null;

	if
		( x instanceof CoLayoutEditorConfiguration )
	{
		return (CoLayoutEditorConfiguration) x;
	} else {
		return CoLayoutEditorConfiguration.lookup( (String) x );
	}
}
protected CoPageItemEditorContextIF getPageItemEditorContext()
{
	if ( m_uiContext == null ) return null;

	return (CoPageItemEditorContextIF) m_uiContext.getStateFor( CoPageItemEditorContextIF.KEY );
}
protected CoLayoutHolder getPageItemHolderForEditing( CoLayoutHolder domain )
{
	return domain;
}
protected CoPageItemHolderPaletteViewPane getPaneFor( CoLayoutHolder h )
{
	int I = m_palettePanel.getComponentCount();
	for
		( int i = 0; i < I; i++ )
	{
		CoPageItemHolderPaletteViewPane p = (CoPageItemHolderPaletteViewPane) m_palettePanel.getComponent( i );
		if ( p.getHolder() == h ) return p;
	}

	return null;
}
public List getSelectedHolders() // [ CoLayoutHolder ]
{
	return m_selectedHolders;
}
private void holderSelected( MouseEvent e )
{
	if(!(e.getSource() instanceof CoPageItemHolderPaletteViewPane))
		return;
	CoPageItemHolderPaletteViewPane vp = (CoPageItemHolderPaletteViewPane) e.getSource();

	if
		( ( e.getModifiers() & InputEvent.CTRL_MASK ) != 0 )
	{
		if
			( vp.isSelected() )
		{
			if ( unselect( vp ) ) fireSelectionChanged();
		} else {
			if ( select( vp ) ) fireSelectionChanged();
		}
	} else {
		boolean doFire = false;
		int I = m_palettePanel.getComponentCount();
		for
			( int i = 0; i < I; i++ )
		{
			 if ( unselect( (CoPageItemHolderPaletteViewPane) m_palettePanel.getComponent( i ) ) ) doFire = true;
		}
		if ( select( vp ) ) doFire = true;
		if ( doFire ) fireSelectionChanged();
	}

	getPanel().repaint();
}
private void pageItemHolderChanged( CoChangedObjectEvent e )
{
	CoLayoutHolder pih = (CoLayoutHolder) e.getChangedObject();

	int i = getHolders().indexOf( pih );

	CoPageItemHolderPaletteViewPane vp = (CoPageItemHolderPaletteViewPane) m_palettePanel.getComponent( i );
	disposeViews( vp );

	vp.setViews( createViews( pih ) );
	
	m_palettePanel.repaint();
}
protected void postDomainChange( CoObjectIF d )
{
	super.postDomainChange( d );
	
	update();
}
private void preparePopupMenu(MouseEvent e) {
	int selectedCount = m_selectedHolders.size();
	if (m_editAction != null)
		m_editAction.setEnabled(selectedCount > 0);
	if (m_removeAction != null)
		m_removeAction.setEnabled(selectedCount > 0);
	if (m_postRenameWindowAction != null)
		m_postRenameWindowAction.setEnabled((selectedCount == 1) && (m_selectedHolders.get(0) instanceof CoRenameable));

}
private void postUpdate()
{
	getPanel().revalidate();
}
protected void prepareEditor( CoLayoutEditor editor )
{
}
public void removeSelectionListener( SelectionListener l )
{
	if ( m_selectionListeners == null ) return;
	m_selectionListeners.remove( l );
	if ( m_selectionListeners.isEmpty() ) m_selectionListeners = null;
}
private void rename()
{
	if
		( m_nameWindow == null )
	{
		m_nameTextField = new CoTextField( 20 );
		m_nameWindow = new JWindow( getWindow() );
		m_nameWindow.getContentPane().add( m_nameTextField );

		CoCommand a =
			new CoCommand( "SET NAME" )
			{
				String m_name;
				
				public final void actionPerformed( ActionEvent ev )
				{
					m_nameWindow.setVisible( false );
					if
						( ! ev.getActionCommand().equals( CANCEL_RENAME ) )
					{
						m_name = ev.getActionCommand();
						CoTransactionUtilities.execute( this );
					}
				}
				
				public boolean doExecute()
				{
					m_renamer.rename( (CoRenameable) m_selectedHolders.get( 0 ), m_name );
					return true;
				}
			};
			
		m_nameTextField.addActionListener( a );
		m_nameTextField.registerKeyboardAction( a, CANCEL_RENAME, KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
	}
	
	CoLayoutHolder h = (CoLayoutHolder) m_selectedHolders.get( 0 );

	m_nameTextField.setText( h.getName() );
	m_nameTextField.selectAll();

	m_nameWindow.pack();

	JComponent c = getPaneFor( h );

	Point p = c.getLocationOnScreen();
	int x = p.x;
	int y = p.y + c.getHeight() - m_nameWindow.getHeight();
	m_nameWindow.setLocation( x, y );
	m_nameWindow.setVisible( true );
	m_nameTextField.requestFocus();
}
protected boolean select( CoPageItemHolderPaletteViewPane p )
{
	if ( p.isSelected() ) return false;
	
	p.setSelected( true );
	m_selectedHolders.add( p.getHolder() );
	return true;
}
public void setActions( CoTransActionCommand [] addActions, CoTransActionCommand removeAction, Renamer renamer )
{
	m_addActions = addActions;
	m_removeAction = removeAction;
	m_renamer = renamer;
}
public void setCopyOnDrop( boolean cod )
{
	m_copyOnDrop = cod;
}
public void setDecoration( CoViewPanel.Decoration r )
{
	m_viewPanelDecoration = r;
}
public void setUIContext( CoUIContext context )
{
	m_uiContext = context;
}
protected void stoppedEditing( CoLayoutEditor editor )
{
	m_editorDialog.getEditor().setModels( null, null );
	m_editorDialog.getCoDialog().removeWindowListener( m_windowListener );

	if ( getDomain() == null ) return;
	
	if
		( m_touchPageItemHolderCommand == null )
	{
		m_touchPageItemHolderCommand =
			new CoCommand( "TOUCH PAGE ITEM HOLDER" )
			{
				public boolean doExecute()
				{
					Iterator i = m_editedHolders.iterator();
					while
						( i.hasNext() )
					{
						( (CoLayoutHolder) i.next() ).layoutsChanged();
					}
					return true;
				}
			};
	}
		
	CoTransactionUtilities.execute( m_touchPageItemHolderCommand, null );
}
protected boolean unselect( CoPageItemHolderPaletteViewPane p )
{
	if ( ! p.isSelected() ) return false;
	
	p.setSelected( false );
	m_selectedHolders.remove( p.getHolder() );
	return true;
}
// PENDING: this algorithm (rebuild everything) isn't very effective, try to reuse unchanged view panes

protected void update()
{
	if ( m_palettePanel == null ) return;

	int I = m_palettePanel.getComponentCount();
	for
		( int i = 0; i < I; i++ )
	{
		CoPageItemHolderPaletteViewPane vp = (CoPageItemHolderPaletteViewPane) m_palettePanel.getComponent( i );
		disposeViews( vp );
		CoObservable.removeChangedObjectListener( m_pageItemHolderListener, vp.getHolder() );
	}

	m_palettePanel.removeAll();

	if
		( getHolders() != null )
	{
		List tmpSelected = m_selectedHolders;
		m_selectedHolders = new ArrayList();
	
		createMouseListeners();
	
		CoUserInterfaceBuilder b = getUIBuilder();
		
		Iterator i = getHolders().getLayoutHolders().iterator();
		while
			( i.hasNext() )
		{
			CoLayoutHolder h = (CoLayoutHolder) i.next();

			final CoPageItemHolderPaletteViewPane vp = createViewPane( h );
			if
				( tmpSelected.contains( h ) )
			{	
				vp.setSelected( true );
				m_selectedHolders.add( h );
			}

			vp.setViews( createViews( h ) );
			CoObservable.addChangedObjectListener( m_pageItemHolderListener, h );
			vp.addMouseListener( m_mouseListener );

			m_palettePanel.add( vp );
		}

		if
			( tmpSelected.size() == m_selectedHolders.size() )
		{
			fireSelectionChanged();
		}
		
		postUpdate();
	}

}
public void valueHasChanged()
{
	super.valueHasChanged();
	
	update();

}
}
