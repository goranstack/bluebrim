package com.bluebrim.layout.impl.client;

import java.awt.event.*;

import javax.swing.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.swing.client.*;

/**
 * Page item layout order property panel.
 *
 * @author: Dennis
 */

public class CoPageItemLayoutOrderPanel extends CoPageItemPropertyPanel
{



	public static final String TOP = "CoPageItemLayoutOrderPanel.TOP";
	public static final String UP = "CoPageItemLayoutOrderPanel.UP";
	public static final String DOWN = "CoPageItemLayoutOrderPanel.DOWN";
	public static final String BOTTOM = "CoPageItemLayoutOrderPanel.BOTTOM";
	public static final String SIZE_ORDER = "CoPageItemLayoutOrderPanel.SIZE_ORDER";
	
	private class LayoutOrderListModel extends AbstractListModel
	{
		public void refresh()
		{
			fireContentsChanged( this, 0, getSize() - 1 );
		}
		
		public Object getElementAt( int i )
		{
			if ( m_domain == null ) return null;
			return ( (CoCompositePageItemView) m_domain ).getLayoutChildAt( i );
		}

		public int getSize()
		{
			if ( m_domain == null ) return 0;
			return ( (CoCompositePageItemView) m_domain ).getLayoutChildCount();
		}
	};

	public CoList m_orderList;
	public LayoutOrderListModel m_orderListModel;



public void doUpdate()
{
	m_orderListModel.refresh();

	if
		( ( m_selectedBeforeChange != null ) && ( m_domain != null ) && ( ( (CoCompositePageItemView) m_domain ).getChildren().contains( m_selectedBeforeChange ) ) )
	{
		m_orderList.setSelectedValue( m_selectedBeforeChange, true );
	} else {
		m_orderList.clearSelection();
		updateButtons();
	}
}

	private CoButton m_bottomButton;
	private CoButton m_downButton;
	private CoShapePageItemView m_selectedBeforeChange;
	private CoButton m_topButton;
	private CoButton m_upButton;



public void setDomain( CoShapePageItemView domain )
{
	if
		( domain instanceof CoCompositePageItemView )
	{
		super.setDomain( domain );
	} else {
		super.setDomain( null );
	}
}

private void updateButtons()
{
	int i = m_orderList.getSelectedIndex();

	if
		( i == -1 )
	{
		m_topButton.setEnabled( false );
		m_upButton.setEnabled( false );
		m_downButton.setEnabled( false );
		m_bottomButton.setEnabled( false );
	} else {
		m_topButton.setEnabled( i > 0 );
		m_upButton.setEnabled( i > 0 );

		int I = m_orderListModel.getSize() - 1;
		m_downButton.setEnabled( i < I );
		m_bottomButton.setEnabled( i < I );
	}
}

public CoPageItemLayoutOrderPanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, new CoAttachmentLayout(), commandExecutor );
}

protected void create( CoUserInterfaceBuilder b, final CoUndoableCommandExecutor commandExecutor )
{
	CoListBox list = b.createListBox();

	m_topButton = b.createButton( CoPageItemUIStringResources.getName( TOP ), null );
	m_upButton = b.createButton( CoPageItemUIStringResources.getName( UP ), null );
	m_downButton = b.createButton( CoPageItemUIStringResources.getName( DOWN ), null );
	m_bottomButton = b.createButton( CoPageItemUIStringResources.getName( BOTTOM ), null );
	CoButton sortBySizeButton = b.createButton( CoPageItemUIStringResources.getName( SIZE_ORDER ), null );

	CoPanel buttons = b.createPanel( new CoColumnLayout( true ) );
	buttons.add( m_topButton );
	buttons.add( m_upButton );
	buttons.add( m_downButton );
	buttons.add( m_bottomButton );
	buttons.add( sortBySizeButton );

	add( list,
	     new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_COMPONENT_LEFT, 0, buttons ) ) );
	add( buttons,
	     new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_CONTAINER, 0 ) ) );


	m_orderList = list.getList();
	m_orderList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
	m_orderList.setCellRenderer(
		new CoListCellRenderer()
		{
			protected void setValue( Object value, int index, boolean isSelected, boolean cellHasFocus )
			{			
				{
					setText( ( (CoShapePageItemView) value ).getName() );
				}
			}		
		}
	);

	m_orderListModel = new LayoutOrderListModel();
	m_orderList.setModel( m_orderListModel );
	
	m_orderList.addListSelectionListener(
		new javax.swing.event.ListSelectionListener()
		{
			public void valueChanged( javax.swing.event.ListSelectionEvent e )
			{
				if ( e.getValueIsAdjusting() ) return;
				updateButtons();
			}
		}
	);

	


	
	m_topButton.addActionListener( 
		new CoCommandAdapter( commandExecutor, CoPageItemCommands.MOVE_TO_FIRST_LAYOUT_POSITION )
		{
			protected void prepare()
			{
				m_selectedBeforeChange = (CoShapePageItemView) m_orderList.getSelectedValue();
				( (CoShapePageItemLayoutOrderCommand) m_command ).prepare( m_selectedBeforeChange );
			}
		}
	);

	m_upButton.addActionListener( 
		new CoCommandAdapter( commandExecutor, CoPageItemCommands.MOVE_TOWARDS_FIRST_LAYOUT_POSITION )
		{
			protected void prepare()
			{
				m_selectedBeforeChange = (CoShapePageItemView) m_orderList.getSelectedValue();
				( (CoShapePageItemLayoutOrderCommand) m_command ).prepare( m_selectedBeforeChange );
			}
		}
	);

	m_downButton.addActionListener( 
		new CoCommandAdapter( commandExecutor, CoPageItemCommands.MOVE_TOWARDS_LAST_LAYOUT_POSITION )
		{
			protected void prepare()
			{
				m_selectedBeforeChange = (CoShapePageItemView) m_orderList.getSelectedValue();
				( (CoShapePageItemLayoutOrderCommand) m_command ).prepare( m_selectedBeforeChange );
			}
		}
	);

	m_bottomButton.addActionListener( 
		new CoCommandAdapter( commandExecutor, CoPageItemCommands.MOVE_TO_LAST_LAYOUT_POSITION )
		{
			protected void prepare()
			{
				m_selectedBeforeChange = (CoShapePageItemView) m_orderList.getSelectedValue();
				( (CoShapePageItemLayoutOrderCommand) m_command ).prepare( m_selectedBeforeChange );
			}
		}
	);

	

	sortBySizeButton.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				m_selectedBeforeChange = (CoShapePageItemView) m_orderList.getSelectedValue();
				CoPageItemCommands.SET_LAYOUT_ORDER.prepare( m_domain, new CoCompositePageItemIF.AreaComparator() );
				commandExecutor.doit( CoPageItemCommands.SET_LAYOUT_ORDER, null );
			}
		}
	);
}

public void postSetDomain()
{
	m_orderList.clearSelection();
	m_selectedBeforeChange = null;
	m_orderListModel.refresh();
}
}