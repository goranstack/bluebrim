package com.bluebrim.text.impl.client;

import java.awt.*;
import java.util.*;
import java.util.List;

import com.bluebrim.base.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.transact.shared.*;

// Author: Dennis

public class CoTextMeasurementPrefsUI extends CoDomainUserInterface
{
	private static final String TAGS = "TAGS";

	public static final String MEASUREMENT = "CoTextMeasurementPrefsUI.MEASUREMENT";
	public static final String AVAILABLE_TAGS = "CoTextMeasurementPrefsUI.AVAILABLE_TAGS";
	public static final String MEASURED_TAGS = "CoTextMeasurementPrefsUI.MEASURED_TAGS";
	public static final String COLUMN_WIDTH = "CoTextMeasurementPrefsUI.COLUMN_WIDTH";


	private List m_availableTags = new ArrayList();
	private List m_measuredTags = new ArrayList();
public CoTextMeasurementPrefsUI()
{
	setContext( null );
}
private void addTags( final Object tags[] )
{
	for
		( int i = 0; i < tags.length; i++ )
	{
		m_measuredTags.add( tags[ i ] );
		m_availableTags.remove( tags[ i ] );
	}
	
	CoCommand c = 
		new CoCommand( "ADD_MEASURED_TAGS" )
		{
			public boolean doExecute()
			{
				( (com.bluebrim.text.shared.CoTextMeasurementPrefsIF) getDomain() ).setMeasurementTags( m_measuredTags );
				return true;
			}
		};

	CoTransactionUtilities.execute( c, getDomain() );
}
protected void createListeners()
{
	super.createListeners();
	
	( (CoChooserPanel) getNamedWidget( TAGS ) ).addChooserEventListener(
		new CoChooserEventListener()
		{
			public void handleChooserEvent( CoChooserEvent e )
			{
				if
					( e.isAddEvent() )
				{
					addTags( e.getElements() );
				} else {
					removeTags( e.getElements() );
				}
			}
		}
	);
}
protected void createValueModels(CoUserInterfaceBuilder b)
{
	super.createValueModels(b);
	
	b.createListBoxAdaptor(
		b.addListValueModel(
			new CoListValueModel.Mutable( AVAILABLE_TAGS, m_availableTags )
		),
		(CoListBox) ( (CoChooserPanel) getNamedWidget( TAGS ) ).getSourceBox()
	);
	
	b.createListBoxAdaptor(
		b.addListAspectAdaptor(
			new CoAbstractListAspectAdaptor.Default( this, MEASURED_TAGS )
			{
				public Object get(CoObjectIF subject)
				{
					return ( (com.bluebrim.text.shared.CoTextMeasurementPrefsIF) subject ).getMeasurementTags();
				}
			}
		),
		(CoListBox) ( (CoChooserPanel) getNamedWidget( TAGS ) ).getDestinationBox() );



	CoAspectAdaptor ad = new CoGsAspectAdaptor( this, COLUMN_WIDTH )
	{
		public Object get( CoObjectIF subject ) 
		{
			return new Float( ( (com.bluebrim.text.shared.CoTextMeasurementPrefsIF) subject ).getMeasurementColumnWidth() );
		}
		public void set( CoObjectIF subject, Object value )
		{
			( (com.bluebrim.text.shared.CoTextMeasurementPrefsIF) subject ).setMeasurementColumnWidth( ( (Float) value ).floatValue() );
		}
	};
	ad = b.addAspectAdaptor( ad );
	b.createTextFieldAdaptor( new CoFloatConverter( ad, CoLengthUnit.LENGTH_UNITS ), (CoTextField) getNamedWidget( COLUMN_WIDTH ) ); 

}
protected void createWidgets( CoPanel P, CoUserInterfaceBuilder b )
{
	super.createWidgets( P, b );

	CoTabbedPane tp = b.createTabbedPane();
	P.add( tp );
	
	{
		CoPanel measurementPanel = b.createPanel( new BorderLayout( 0, 10 ) );
		tp.addTab( CoTextStringResources.getName( MEASUREMENT ), measurementPanel );
		
		CoChooserPanel cp = b.createChooserPanel( CoTextStringResources.getName( AVAILABLE_TAGS ),
			                                        CoTextStringResources.getName( MEASURED_TAGS ),
			                                        CoChooserPanel.SOURCE_TO_THE_LEFT,
			                                        CoChooserPanel.REMOVE_FROM_SOURCE,
			                                        TAGS );

		measurementPanel.add( cp, BorderLayout.CENTER );

		
		CoPanel p = b.createPanel( new CoFormLayout() );
		measurementPanel.add( p, BorderLayout.SOUTH );

		p.add( b.createLabel( CoTextStringResources.getName( COLUMN_WIDTH ) ) );
		p.add( b.createTextField( CoTextField.RIGHT, 6, COLUMN_WIDTH ) );
	}
	
}
public void open()
{
	Window w = CoWindowList.findWindowFor( this );
	
	if
		( w == null )
	{
		w = new CoFrame( "Inställningar, texteditor", this );

		( (CoFrame) w ).setDefaultCloseOperation( CoFrame.HIDE_ON_CLOSE );
	}
	
	w.show();
}
private void removeTags( final Object tags[] )
{
	for
		( int i = 0; i < tags.length; i++ )
	{
		m_availableTags.add( tags[ i ] );
		m_measuredTags.remove( tags[ i ] );
	}
	
	CoCommand c = 
		new CoCommand( "REMOVE_MEASRUED_TAGS" )
		{
			public boolean doExecute()
			{
				( (com.bluebrim.text.shared.CoTextMeasurementPrefsIF) getDomain() ).setMeasurementTags( m_measuredTags );
				return true;
			}
		};

	CoTransactionUtilities.execute( c, getDomain() );
}
public void setContext( CoTextEditorContextIF context )
{
	m_availableTags.clear();

	if
		( context != null )
	{
		m_availableTags.addAll( context.getParagraphTagNames() );

		if
			( getDomain() != null )
		{
			m_availableTags.removeAll( ( (com.bluebrim.text.shared.CoTextMeasurementPrefsIF) getDomain() ).getMeasurementTags() );
		}
	}
}
}
