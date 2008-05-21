package com.bluebrim.layout.impl.client.editor;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;


public class CoLengthUnitSetUI extends CoDomainUserInterface
{
	public static final String UNIT = "UNIT";
	public static final String VIEW_DECIMALS = "VIEW_DECIMALS";
	public static final String SNAP_DECIMALS = "SNAP_DECIMALS";

	CoPropertyChangeListener m_listener = 
		new CoPropertyChangeListener()
		{
			public void propertyChange( CoPropertyChangeEvent ev )
			{
				valueHasChanged();
			}
		};
		
		
public CoLengthUnitSetUI()
{
	super();
}
protected void createValueModels( CoUserInterfaceBuilder b )
{
	super.createValueModels( b );

	b.createComboBoxAdaptor(
		b.addAspectAdaptor( 
			new CoAspectAdaptor( this, UNIT )
			{
				public Object get( CoObjectIF subject )
				{
					return ( (CoLengthUnitSet) subject ).getCurrentUnit();
				}
				
				public void set( CoObjectIF subject, Object value )
				{
					( (CoLengthUnitSet) subject ).setCurrentUnit( (CoLengthUnit) value );
				}
					
			}
		),
		(CoComboBox) getNamedWidget( UNIT )
	);




	CoValueModel vm = b.addAspectAdaptor( new CoAspectAdaptor( this, VIEW_DECIMALS )
	{
		public Object get( CoObjectIF subject )
		{
			return new Integer( ( (CoLengthUnitSet) subject ).getViewDecimalCount() );
		}

		public void set( CoObjectIF subject, Object value )
		{
			( (CoLengthUnitSet) subject ).setViewDecimalCount( ( (Integer) value ).intValue() );
		}
	} );
	
	b.createTextFieldAdaptor( new CoIntegerConverter( vm ), (CoTextField) getNamedWidget( VIEW_DECIMALS ) );




	vm = b.addAspectAdaptor( new CoAspectAdaptor( this, SNAP_DECIMALS )
	{
		public Object get( CoObjectIF subject )
		{
			return new Integer( ( (CoLengthUnitSet) subject ).getSnapDecimalCount() );
		}

		public void set( CoObjectIF subject, Object value )
		{
			( (CoLengthUnitSet) subject ).setSnapDecimalCount( ( (Integer) value ).intValue() );
		}
	} );
	
	b.createTextFieldAdaptor( new CoIntegerConverter( vm ), (CoTextField) getNamedWidget( SNAP_DECIMALS ) );


}
protected void createWidgets( CoPanel p, CoUserInterfaceBuilder b )
{
	super.createWidgets( p, b );

	p.setLayout( new CoFormLayout( 2, 2, true ) );

	p.add( b.createLabel( CoUnitUIStringResources.getName( UNIT ), null, CoLabel.RIGHT ) );
	p.add( b.createComboBox( 
		new DefaultListCellRenderer()
		{
			public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
			{
				if
					( value != null )
				{
					value = ( (CoLengthUnit) value ).getName();
					if ( value.toString().length() == 0 ) value = " ";
				}
				return super.getListCellRendererComponent( list,
					                                         value,
					                                         index,
					                                         isSelected,
					                                         cellHasFocus );
			}
		},
		UNIT ) );
	
	p.add( b.createLabel( CoUnitUIStringResources.getName( VIEW_DECIMALS ), null, CoLabel.RIGHT ) );
	CoTextField tf = b.createTextField( CoTextField.RIGHT, VIEW_DECIMALS );
	tf.setEditable( false );
	p.add( new CoTextfieldCounterPanel( tf ) );
	
	p.add( b.createLabel( CoUnitUIStringResources.getName( SNAP_DECIMALS ), null, CoLabel.RIGHT ) );
	tf = b.createTextField( CoTextField.RIGHT, SNAP_DECIMALS );
	tf.setEditable( false );
	p.add( new CoTextfieldCounterPanel( tf ) );
}
protected void doAfterCreateUserInterface()
{
	super.doAfterCreateUserInterface();
	
	update();
}
public static void main(String[] args)
{
	CoLengthUnitSetUI ui = new CoLengthUnitSetUI();

	ui.setDomain( CoLengthUnit.LENGTH_UNITS );
	
	ui.openInWindow();
}
protected void postDomainChange( CoObjectIF domain )
{
	super.postDomainChange( domain );

	if
		( domain != null )
	{
		domain.addPropertyChangeListener( m_listener );
	}
	
	update();
}
protected void preDomainChange( CoObjectIF domain )
{
	super.preDomainChange( domain );

	if
		( domain != null )
	{
		domain.removePropertyChangeListener( m_listener );
	}
}
private void update()
{
	CoComboBox cb = (CoComboBox) getNamedWidget( UNIT );

	if ( cb == null ) return;
	
	cb.removeAllItems( true );
	
	CoLengthUnitSet u = (CoLengthUnitSet) getDomain();
	if
		( u != null )
	{
		Iterator i = u.getUnitMap().values().iterator();
		while
			( i.hasNext() )
		{
			cb.addItem( i.next(), true );
		}
	}

	valueHasChanged();
}
}
