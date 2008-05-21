package com.bluebrim.stroke.impl.client;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;

import com.bluebrim.base.shared.CoObjectIF;
import com.bluebrim.gemstone.client.CoGemStoneUIConstants;
import com.bluebrim.gemstone.client.CoGsUIStringResources;
import com.bluebrim.gemstone.client.command.CoBasicTransaction;
import com.bluebrim.gui.client.CoAbstractListAspectAdaptor;
import com.bluebrim.gui.client.CoAbstractListModel;
import com.bluebrim.gui.client.CoCollectionListModel;
import com.bluebrim.gui.client.CoDomainUserInterface;
import com.bluebrim.gui.client.CoEnableDisableEvent;
import com.bluebrim.gui.client.CoEnableDisableListener;
import com.bluebrim.gui.client.CoPopupGestureListener;
import com.bluebrim.gui.client.CoSelectionEvent;
import com.bluebrim.gui.client.CoSelectionListener;
import com.bluebrim.gui.client.CoSubcanvas;
import com.bluebrim.gui.client.CoUserInterfaceBuilder;
import com.bluebrim.menus.client.CoMenuBuilder;
import com.bluebrim.menus.client.CoPopupMenu;
import com.bluebrim.paint.shared.CoColorCollectionIF;
import com.bluebrim.swing.client.CoListBox;
import com.bluebrim.swing.client.CoPanel;

public class CoStrokeCollectionUI extends CoDomainUserInterface 
{
	// Popupmenu
	private CoPopupMenu m_popupMenu;
	
	// Actions
	private AbstractAction m_removeAction;
	private AbstractAction m_addAction;

	// UI Constants
	public static final String ADD 						= "add";
	public static final String REMOVE 					= "remove";
	public static final String STROKES 	= "strokes";
	public static final String STROKE 	= "stroke";

	// sub-UI's
	private com.bluebrim.stroke.impl.client.CoStrokeUI m_strokeUI;
	private CoColorCollectionIF m_colorCollection;
	//------ inner classes -----------------------------------------------------------------

	private class AddTransaction extends CoBasicTransaction
	{
		private com.bluebrim.stroke.shared.CoStrokeIF m_stroke;
		
		public AddTransaction( String name , 
													 CoObjectIF target , 
													 CoStrokeCollectionUI source , 
													 com.bluebrim.stroke.shared.CoStrokeIF stroke ) 
		{
			super( name, target, source );
			m_stroke = stroke;
		}
		
		protected String createErrorMessage(Exception e) 
		{
			MessageFormat tFormat = new MessageFormat(CoGsUIStringResources.getName(CoGemStoneUIConstants.ADD_LIST_ELEMENT_ERROR));
			return tFormat.format(new String[] {e.getMessage() != null ? e.getMessage() : new String()});
		}
		
		protected void doTransaction() 
		{
			addStroke( m_stroke );
		}
	};

	private class RemoveTransaction extends CoBasicTransaction 
	{
		public RemoveTransaction( String name,
			                        CoObjectIF target,
			                        CoStrokeCollectionUI source ) 
		{
			super(name, target, source);
		}
		
		protected String createErrorMessage(Exception e) 
		{
			MessageFormat tFormat = new MessageFormat(CoGsUIStringResources.getName(CoGemStoneUIConstants.REMOVE_LIST_ELEMENTS_ERROR));
			return tFormat.format(new String[] {e.getMessage() != null ? e.getMessage() : new String()});
		}
		
		protected void doTransaction() 
		{
			removeStroke();
		}	
	};
public CoStrokeCollectionUI() 
{	
	super();
	initialize();
}

private void addStroke( com.bluebrim.stroke.shared.CoStrokeIF ss ) 
{
	getStrokeCollection().addStroke( ss );
	getStrokeList().setSelectedValue( ss , true );
	getNamedValueModel( STROKES ).valueHasChanged();
}
protected void createListeners() {

	super.createListeners();

	getStrokeList().addMouseListener(new CoPopupGestureListener(m_popupMenu) {
		protected void showPopup(MouseEvent e) {
			JList l = (JList) e.getSource();
			if (l.isEnabled())
				super.showPopup(e);
		}

	});
		
	//-----------------------------------------------------------------------------------------------------------		
	( (CoAbstractListAspectAdaptor) getNamedValueModel( STROKES ) ).addSelectionListener(
		new CoSelectionListener() 
		{
			public void selectionChange( CoSelectionEvent e ) 
			{
				if ( e.getValueIsAdjusting() ) return;
				
				if
					( singleSelection() ) 
				{
					m_strokeUI.setDomain( (CoObjectIF) getStrokeList().getSelectedValue() );
					m_strokeUI.setEnabled( getStrokeList().getSelectedIndex() >= m_startOfMutableStrokes );
				} else {
					m_strokeUI.setDomain( null );
				}

				enableDisableMenus( true );
			}
		}
	);
	
	//-----------------------------------------------------------------------------------------------------------
	getStrokeList().registerKeyboardAction(new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			JList tList = getStrokeList();
			if( !tList.isSelectionEmpty() )
				new RemoveTransaction
				(		
					com.bluebrim.stroke.impl.client.CoStrokeUIResources.getName(REMOVE),
					getDomain(),
					CoStrokeCollectionUI.this
				).execute();
		}
	}, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, KeyEvent.VK_UNDEFINED), JComponent.WHEN_FOCUSED);

	//-----------------------------------------------------------------------------------------------------------
	getStrokeList().registerKeyboardAction(new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			if ( getDomain() != null )
				new AddTransaction
					(		
						com.bluebrim.stroke.impl.client.CoStrokeUIResources.getName(ADD),
						getDomain(),
						CoStrokeCollectionUI.this,
						getStrokeCollection().createStroke()
					).execute();
		}
	}, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, KeyEvent.VK_UNDEFINED), JComponent.WHEN_FOCUSED);

	//-----------------------------------------------------------------------------------------------------------
	getStrokeList().registerKeyboardAction(new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			selectDeselectLead(true);
		}
	}, KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, ActionEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);

	//-----------------------------------------------------------------------------------------------------------
	getStrokeList().registerKeyboardAction(new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			selectDeselectLead(false);
		}
	}, KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.VK_UNDEFINED), JComponent.WHEN_FOCUSED);

	//-----------------------------------------------------------------------------------------------------------
	getStrokeList().registerKeyboardAction(new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			moveLead(-1);
		}
	}, KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);

	//-----------------------------------------------------------------------------------------------------------
	getStrokeList().registerKeyboardAction(new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			moveLead(+1);
		}
	}, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ActionEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);	

	//-----------------------------------------------------------------------------------------------------------	
	getUIBuilder().addEnableDisableListener(new CoEnableDisableListener()
	{
		public void enableDisable(CoEnableDisableEvent e)
		{
			enableDisableMenus(e.enable());		
		}
	});

}
protected void createPopupMenus () {	
	super.createPopupMenus();
	
	CoMenuBuilder b = getMenuBuilder();
	m_popupMenu = b.createPopupMenu( getStrokeList() );
	b.addPopupMenuItem( m_popupMenu, m_addAction );
	b.addPopupMenuItem( m_popupMenu, m_removeAction );
	b.addSeparator( m_popupMenu );
	b.addPopupMenuItem( m_popupMenu, m_addDefaultStrokesAction );
/*
	b.addPopupMenuItem(m_popupMenu, new AbstractAction(com.bluebrim.mediaproduction.impl.client.CoMediaProductionUIStringResources.getName("SAVE_AS_XML")){
			public void actionPerformed(ActionEvent e){
				com.bluebrim.xml.shared.CoListWrapper strokes = new com.bluebrim.xml.shared.CoListWrapper(getStrokeCollection().getStrokes());
				if(strokes != null)
					doSaveAsXML(strokes);				
			}
	});
	
	b.addPopupMenuItem(m_popupMenu, new AbstractAction(com.bluebrim.mediaproduction.impl.client.CoMediaProductionUIStringResources.getName("LOAD_FROM_XML")){
			public void actionPerformed(ActionEvent e){
				doLoadXml();				
			}
	});
	*/
	
	enableDisableMenus( true );
}
public void createValueModels(CoUserInterfaceBuilder aBuilder) {

	super.createValueModels(aBuilder);

	// --------------------------------------------------------------------------------
	aBuilder.createListBoxAdaptor( aBuilder.addListAspectAdaptor( new CoAbstractListAspectAdaptor( this , STROKES ) 
	{
		protected Object get(CoObjectIF subject) 
		{
			return ( (com.bluebrim.stroke.shared.CoStrokeCollectionIF) subject ).getStrokes();
		}

		public CoAbstractListModel getDefaultListModel() 
		{
			return new CoCollectionListModel.Default(this);
		}
			
	}) , (CoListBox )getNamedWidget( STROKES ) );
		
}
protected void createWidgets( CoPanel p, CoUserInterfaceBuilder b ) 
{	
	super.createWidgets( p, b );

	// HeadLine
	CoPanel boxPanel = b.createBoxPanel( BoxLayout.Y_AXIS , false , null );
	boxPanel.add( b.createHeadlineLabel( com.bluebrim.stroke.impl.client.CoStrokeUIResources.getName( STROKES ) ) );

	// ListBox
	CoPanel p2 = b.createBoxPanel( BoxLayout.Y_AXIS , false , null );
	CoListBox lb = b.createListBox( STROKES );
	JList l = getStrokeList();
//	l.setPrototypeCellValue( "-------------------------------" );
//	l.setVisibleRowCount(6);
	l.setCellRenderer(
		new com.bluebrim.stroke.impl.client.CoStrokeListRenderer()
		{
			protected boolean isMutable( com.bluebrim.stroke.shared.CoStrokeIF color, int index )
			{
				return index >= m_startOfMutableStrokes;
			}
		}
	);
	p2.add( lb );
	
	// Stroe spec subcanvas
	m_strokeUI = new com.bluebrim.stroke.impl.client.CoStrokeUI();
	CoSubcanvas sb = b.createSubcanvas( m_strokeUI , STROKE );
	( new com.bluebrim.observable.CoServerObjectListener( m_strokeUI )
	{
		public void changedServerObject( com.bluebrim.observable.CoChangedObjectEvent e )
		{
			getUserInterface().valueHasChanged();
		}
	} ).initialize();

	
	// Split Pane
	JSplitPane split = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, p2, sb );
	split.setOneTouchExpandable( true );
	boxPanel.add( split );
	
	p.add( boxPanel , BorderLayout.CENTER );

	updateColors();
}


private void enableDisableMenus(boolean state)
{
	boolean e = state && isEnabled();
	m_addAction.setEnabled( true );

	int [] sel = getStrokeList().getSelectedIndices();
	
	for
		( int i = 0; i < sel.length; i++ )
	{
		e &= sel[ i ] >= m_startOfMutableStrokes;
	}
	
	m_removeAction.setEnabled( e );
}
private com.bluebrim.stroke.shared.CoStrokeCollectionIF getStrokeCollection()
{
	return (com.bluebrim.stroke.shared.CoStrokeCollectionIF) getDomain();
}
private JList getStrokeList()
{
	return ((CoListBox) getNamedWidget(STROKES)).getList();
}
	
private void initialize()
{

	//-------------------------------------------------------------------------------------------
	m_addAction = new AbstractAction(com.bluebrim.stroke.impl.client.CoStrokeUIResources.getName(ADD)) {
		
		public void actionPerformed(ActionEvent e)
		{
			new AddTransaction
				(		
					com.bluebrim.stroke.impl.client.CoStrokeUIResources.getName(ADD),
					getDomain(),
					CoStrokeCollectionUI.this,
					getStrokeCollection().createStroke()
				).execute();
		}
	};
	m_addAction.setEnabled(false);

	//-------------------------------------------------------------------------------------------
	m_removeAction = new AbstractAction(com.bluebrim.stroke.impl.client.CoStrokeUIResources.getName(REMOVE)) {
		
		public void actionPerformed(ActionEvent e)
		{
			new RemoveTransaction
				(		
					com.bluebrim.stroke.impl.client.CoStrokeUIResources.getName(REMOVE),
					getDomain(),
					CoStrokeCollectionUI.this
				).execute();	
		}
	};
	m_removeAction.setEnabled(false);

	//-------------------------------------------------------------------------------------------
	m_addDefaultStrokesAction = new AbstractAction(com.bluebrim.stroke.impl.client.CoStrokeUIResources.getName(ADD_DEFAULT_STROKES)) {
		
		public void actionPerformed(ActionEvent e)
		{
			new AddDefaultStrokesTransaction
				(		
					com.bluebrim.stroke.impl.client.CoStrokeUIResources.getName(ADD_DEFAULT_STROKES),
					getDomain(),
					CoStrokeCollectionUI.this
				).execute();	
		}
	};

}
private boolean isIn(int index, int[] indices) {

	for(int i = 0; i < indices.length; i++)
	{
		if(indices[i] == index)
			return true;
	}
	return false;
	
}
private boolean isValidIndex(int index) 
{
	return ( getStrokeCollection() != null ) && 
			 ( getStrokeCollection().getStrokes().size() > index && 0 <= index );
}
private void moveLead( int step ) 
{
	ListSelectionModel m = getStrokeList().getSelectionModel();
	int newLead = m.getLeadSelectionIndex() + step;

	if( !isValidIndex( newLead ) ) return;
		
	boolean flag = m.isSelectedIndex( newLead );
	
	m.removeSelectionInterval( newLead , newLead );

	// Ugly...
	if
		(flag)
	{
		m.addSelectionInterval( newLead , newLead );
	}

	getStrokeList().ensureIndexIsVisible( newLead );
	
}
private void removeStroke() 
{
	List strokes = getStrokeCollection().getStrokes();	
	JList l = getStrokeList();
	ListSelectionModel sm = l.getSelectionModel();

	Object[] selected = l.getSelectedValues();
	l.clearSelection();
	
	getStrokeCollection().removeStroke( selected );

	getNamedValueModel( STROKES ).valueHasChanged();	
}
private void selectDeselectLead( boolean ctrlIsDown ) 
{	
	JList l = getStrokeList();
	int leadIndex = l.getLeadSelectionIndex();

	if( !isValidIndex( leadIndex ) )
		return;
	
	if( l.isSelectedIndex( leadIndex ) && ctrlIsDown )
	{
		l.removeSelectionInterval( leadIndex , leadIndex );
		return;
	}
	
	l.addSelectionInterval( leadIndex , leadIndex );
}
public void setColorCollection( CoColorCollectionIF colorCollection )
{
	m_colorCollection = colorCollection;
	updateColors();
}
private boolean singleSelection()
{
	JList l = getStrokeList();
	return (l.getMinSelectionIndex() == l.getMaxSelectionIndex()) && (l.getMinSelectionIndex() != -1);
}
private void updateColors()
{
	if ( m_strokeUI != null ) m_strokeUI.setColorCollection( m_colorCollection );
}
public void valueHasChanged()
{
	super.valueHasChanged();
	
	m_startOfMutableStrokes = getStrokeCollection().getImmutableStrokeCount();

	m_strokeUI.valueHasChanged();
}

	private int m_startOfMutableStrokes;

public void postDomainChange( CoObjectIF d )
{
	super.postDomainChange( d );

	com.bluebrim.stroke.shared.CoStrokeCollectionIF c = getStrokeCollection();
	m_startOfMutableStrokes = ( c == null ) ? 0 : c.getImmutableStrokeCount();
}

	public static final String ADD_DEFAULT_STROKES 						= "add_default_strokes";
	private AbstractAction m_addDefaultStrokesAction;

	private class AddDefaultStrokesTransaction extends CoBasicTransaction 
	{
		public AddDefaultStrokesTransaction( String name,
			                                   CoObjectIF target,
			                                   CoStrokeCollectionUI source ) 
		{
			super(name, target, source);
		}
		
		protected String createErrorMessage(Exception e) 
		{
			MessageFormat tFormat = new MessageFormat(CoGsUIStringResources.getName(CoGemStoneUIConstants.ADD_LIST_ELEMENT_ERROR));
			return tFormat.format(new String[] {e.getMessage() != null ? e.getMessage() : new String()});
		}
		
		protected void doTransaction() 
		{
			addDefaultStrokes();
		}	
	}

private void addDefaultStrokes() 
{
	getStrokeCollection().addDefaultStrokes();
	getNamedValueModel( STROKES ).valueHasChanged();
}
}