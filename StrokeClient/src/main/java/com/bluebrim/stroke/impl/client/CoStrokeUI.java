package com.bluebrim.stroke.impl.client;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.menus.client.*;
import com.bluebrim.observable.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.transact.shared.*;

public class CoStrokeUI extends CoDomainUserInterface
{
	public final static String LAYER_LIST = "CoStrokeUI.LAYER_LIST";
	public final static String ADD_LAYER = "CoStrokeUI.ADD_LAYER";
	public final static String DELETE_LAYER = "CoStrokeUI.DELETE_LAYER";
	public final static String STROKE = "CoStrokeUI.STROKE";
	public final static String NAME = "CoStrokeUI.NAME";

	protected com.bluebrim.stroke.impl.client.CoStrokeLayerUI m_strokeLayerUI;

	protected com.bluebrim.paint.shared.CoColorCollectionIF m_colorCollection;

	protected JComponent m_preview;
	protected com.bluebrim.stroke.shared.CoStrokePropertiesIF m_previewModel;

	// Popupmenu
	private CoPopupMenu m_popupMenu;
	
	// Actions
	private AbstractAction m_removeAction;
	private AbstractAction m_addAction;
public CoStrokeUI()
{
	super();
	initialize();
}
public CoStrokeUI(com.bluebrim.stroke.shared.CoStrokeIF aDomainObject)
{
	super(aDomainObject);
	initialize();
}

protected void createListeners() {
	super.createListeners();

	getStrokeLayerList().addListSelectionListener(new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent ev) {
			if (ev.getValueIsAdjusting())
				return;

			updateStrokeLayerUI();
			enableDisableMenus(true);
		}
	});

	getStrokeLayerList().addMouseListener(new CoPopupGestureListener(m_popupMenu) {
		protected void showPopup(MouseEvent e) {
			JList l = (JList) e.getSource();
			if (l.isEnabled())
				super.showPopup(e);
		}

	});

}

protected void createPopupMenus () 
{	
	super.createPopupMenus();
	
	CoMenuBuilder b = getMenuBuilder();
	m_popupMenu = b.createPopupMenu( getStrokeLayerList() );
	b.addPopupMenuItem( m_popupMenu, m_addAction );
	b.addPopupMenuItem( m_popupMenu, m_removeAction );

	enableDisableMenus( true );
}

protected void createValueModels(CoUserInterfaceBuilder b) {
	
	super.createValueModels( b );

	b.createTextFieldAdaptor(
		b.addAspectAdaptor(
			new CoGsAspectAdaptor(this, NAME)
			{
				protected Object get(CoObjectIF subject)
				{
					return ( (com.bluebrim.stroke.shared.CoStrokeIF) subject ).getName();
				}
				public void set(CoObjectIF subject, Object newValue) 
				{
					( (com.bluebrim.stroke.shared.CoStrokeIF) subject ).setName( (String) newValue );
				}
			}
		),
		(CoTextField )getNamedWidget(NAME)
	);	

		
	CoAbstractListAspectAdaptor vm = new CoAbstractListAspectAdaptor( this, LAYER_LIST )
	{
		public CoAbstractListModel getDefaultListModel()
		{
			return new CoAbstractListModel()
			{
				public Object getElementAt( int i )
				{
					com.bluebrim.stroke.shared.CoStrokeIF mss = (com.bluebrim.stroke.shared.CoStrokeIF) getDomain();
					if ( mss == null ) return null;
					return mss.get( mss.getCount() - 1 - i );
				}

				public int getSize()
				{
					com.bluebrim.stroke.shared.CoStrokeIF mss = (com.bluebrim.stroke.shared.CoStrokeIF) getDomain();
					if ( mss == null ) return 0;
					return mss.getCount();
				}

				public int indexOf( Object element )
				{
					com.bluebrim.stroke.shared.CoStrokeIF mss = (com.bluebrim.stroke.shared.CoStrokeIF) getDomain();
					if ( mss == null ) return -1;
					return mss.getCount() - 1 - mss.indexOf( (com.bluebrim.stroke.shared.CoStrokeLayerIF) element );
				}

				public void sort( java.util.Comparator c ) {}
			};
		}
		
		protected Object get( CoObjectIF subject )
		{
			return subject;
		}
	};

	b.createListBoxAdaptor( vm, (CoListBox) b.getNamedWidget( LAYER_LIST ) );
	b.addAspectAdaptor( vm );

}
protected void createWidgets( CoPanel p, CoUserInterfaceBuilder b )
{
	super.createWidgets( p, b );

	p.setLayout( new CoRowLayout( 5, true ) );

	{
		CoPanel tmp = b.createPanel( new CoColumnLayout( 5, true ) );
		p.add( tmp );

		// name ...
		{
			CoPanel tmp2 = b.createPanel( new CoFormLayout() );
			tmp.add( tmp2 );

			tmp2.add( b.createLabel( com.bluebrim.stroke.impl.client.CoStrokeUIResources.getName( NAME ) ) );
			tmp2.add( b.createTextField( CoTextField.LEFT, 20, NAME ) );
		}

		// list
		{
			CoListBox lb = b.createListBox( LAYER_LIST );
			tmp.add( lb, CoRowLayout.FILL );

			lb.setCellRenderer(
				new CoListCellRenderer()
				{
					public Component getListCellRendererComponent( JList list,
						                                             Object value,
						                                             int index,
						                                             boolean isSelected,
						                                             boolean cellHasFocus )
					{
						super.getListCellRendererComponent( list,
							                                  value,
							                                  index,
							                                  isSelected,
							                                  cellHasFocus );
						
						if
							( value instanceof com.bluebrim.stroke.shared.CoStrokeLayerIF )
						{
							setIcon( new com.bluebrim.stroke.impl.client.CoStrokeLayerIcon( (com.bluebrim.stroke.shared.CoStrokeLayerIF) value,
								                              100,//list.getWidth(),
								                              list.getFixedCellHeight() - 2,
								                              m_previewModel ) );
							setText( Integer.toString( index + 1 ) );
							setHorizontalTextPosition( LEFT );
							setIconTextGap( 5 );
						} else {
							setIcon( null );
							setText( value.toString() );
						}
						return this;
					}
				}
			);

			JList l = lb.getList();
			l.setPrototypeCellValue( " " );
			l.setVisibleRowCount( 5 );
		}

		// preview
		{
			m_previewModel = new com.bluebrim.stroke.impl.shared.CoStrokeProperties();
			m_previewModel.setWidth( 10 );
			
			m_preview = new com.bluebrim.stroke.impl.client.CoStrokePreview()
			{
				public com.bluebrim.stroke.shared.CoStrokePropertiesIF getStrokeProperties()
				{
					return m_previewModel;
				}
			};
			
			tmp.add( m_preview );
		}
	}

	// stroke
	{
		m_strokeLayerUI = new com.bluebrim.stroke.impl.client.CoStrokeLayerUI();
		p.add( b.createSubcanvas( m_strokeLayerUI, STROKE ) );
	}

	updateColors();
}
protected void doAddStrokeLayer()
{
	CoCommand c = new CoCommand( "ADD_STROKE_LAYER" )
	{
		public boolean doExecute()
		{
			CoList l = getStrokeLayerList();
			
			int i[] = l.getSelectedIndices();
			l.clearSelection();

			if
				( i.length == 1 )
			{
				( (com.bluebrim.stroke.shared.CoStrokeIF) getDomain() ).insert( i[ 0 ] );
				l.setSelectedIndex( i[ 0 ] );
			} else {
				( (com.bluebrim.stroke.shared.CoStrokeIF) getDomain() ).add();
				l.setSelectedIndex( l.getModel().getSize() - 1 );
			}
			
			( (com.bluebrim.stroke.shared.CoStrokeIF) getDomain() ).normalize();
			return true;
		}
	};

	executeCommand( c );
}
protected void doDeleteStrokeLayers()
{
	CoCommand c = new CoCommand( "DELETE_STROKE_LAYER" )
	{
		public boolean doExecute()
		{
			Object o[] = getStrokeLayerList().getSelectedValues();
			getStrokeLayerList().clearSelection();

			for
				( int i = 0; i < o.length; i++ )
			{
				( (com.bluebrim.stroke.shared.CoStrokeIF) getDomain() ).remove( (com.bluebrim.stroke.shared.CoStrokeLayerIF) o[ i ] );
			}
			( (com.bluebrim.stroke.shared.CoStrokeIF) getDomain() ).normalize();
			return true;
		}
	};

	executeCommand( c );
}
private void enableDisableMenus(boolean state)
{
	boolean tState = state && isEnabled();
	m_addAction.setEnabled(true);
	int tSelectionCount = getStrokeLayerList().getSelectedValues().length;
	m_removeAction.setEnabled(tState && tSelectionCount > 0);
}
private void executeCommand( CoCommand c )
{
	try
	{
		CoCmdKit.doInTransaction( c );
		CoObservable.objectChanged(getDomain());
	}
	catch ( com.bluebrim.gemstone.shared.CoTransactionException ex )
	{
		handleTransactionException( c, ex );
	}
}
protected Insets getDefaultPanelInsets()
{
	return null;
}
private CoList getStrokeLayerList()
{
	return ( (CoListBox) getNamedWidget( LAYER_LIST ) ).getList();
}
private void handleTransactionException( CoCommand c, com.bluebrim.gemstone.shared.CoTransactionException ex )
{
	com.bluebrim.base.shared.debug.CoAssertion.trace( "CoCommand failed: " + c + "\n" + ex );
}
private void initialize()
{

	//-------------------------------------------------------------------------------------------
	m_addAction = new AbstractAction(com.bluebrim.stroke.impl.client.CoStrokeUIResources.getName(ADD_LAYER)) {
		
		public void actionPerformed(ActionEvent e)
		{
			doAddStrokeLayer();
		}
	};
	m_addAction.setEnabled(false);

	//-------------------------------------------------------------------------------------------
	m_removeAction = new AbstractAction(com.bluebrim.stroke.impl.client.CoStrokeUIResources.getName(DELETE_LAYER)) {
		
		public void actionPerformed(ActionEvent e)
		{
			doDeleteStrokeLayers();
		}
	};
	m_removeAction.setEnabled(false);

}
protected void postDomainChange( CoObjectIF aDomain )
{
	super.postDomainChange( aDomain );
	updateStrokeLayerUI();
	valueHasChanged();
}
protected void preDomainChange( CoObjectIF aDomain )
{
	super.preDomainChange( aDomain );
	getStrokeLayerList().clearSelection();
	
}

public void setEnabled(boolean enable)
{
	super.setEnabled( enable );
	
	boolean e = enable && canBeEnabled();

	getNamedValueModel( LAYER_LIST ).setEnabled( e );
}
private void updateColors()
{
	if ( m_strokeLayerUI != null ) m_strokeLayerUI.setColorCollection( m_colorCollection );
}
private void updateStrokeLayerUI()
{
	Object[] o = getStrokeLayerList().getSelectedValues();
	if
		( o.length != 1 )
	{
		m_strokeLayerUI.setDomain( null );
	} else {
		m_strokeLayerUI.setDomain( (com.bluebrim.stroke.shared.CoStrokeLayerIF) o[ 0 ] );
	}
	
}
public void valueHasChanged()
{
	super.valueHasChanged();
	
	m_strokeLayerUI.valueHasChanged();

	m_previewModel.setStroke( (com.bluebrim.stroke.shared.CoStrokeIF) getDomain() );
	m_preview.repaint();

	getNamedWidget( LAYER_LIST ).repaint();
}

public void setColorCollection( com.bluebrim.paint.shared.CoColorCollectionIF colorCollection )
{
	m_colorCollection = colorCollection;
	updateColors();
}
}