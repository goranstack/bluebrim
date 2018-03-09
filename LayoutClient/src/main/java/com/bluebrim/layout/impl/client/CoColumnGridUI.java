package com.bluebrim.layout.impl.client;

import java.awt.event.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.swing.client.*;

/**
 * UI for CoColumnGridIF.
 *
 * @author: Dennis
 */

public class CoColumnGridUI extends CoInsetlessUI
{
	public static final String COLUMN_GRID = "COLUMN_GRID";
	public static final String MARGIN_GRID = "MARGIN_GRID";
		
	public static final String LEFT_MARGIN = "GRID_LEFT_MARGIN";
	public static final String RIGHT_MARGIN = "GRID_RIGHT_MARGIN";
	public static final String TOP_MARGIN = "GRID_TOP_MARGIN";
	public static final String BOTTOM_MARGIN = "GRID_BOTTOM_MARGIN";
	public static final String COLUMN_COUNT = "GRID_COLUMN_COUNT";
	public static final String SPACING = "GRID_SPACING";
public CoColumnGridUI()
{
	super();
}
protected void createValueModels(CoUserInterfaceBuilder builder)
{
	super.createValueModels( builder );

		
	CoValueModel vm = new CoGsAspectAdaptor( this, LEFT_OUTSIDE_SENSITIVE )
	{
		public Object get( CoObjectIF domain )
		{
			try
			{
				boolean s = ( (CoImmutableColumnGridIF) domain ).isLeftOutsideSensitive();
				return s ? Boolean.TRUE : Boolean.FALSE;
			}
			catch ( ClassCastException ex )
			{
				return null;
			}
		}

		public void set( CoObjectIF domain, Object value )
		{
			try
			{
				( (CoColumnGridIF) domain ).setLeftOutsideSensitive( ( (Boolean) value ).booleanValue() );
			}
			catch ( ClassCastException ex )
			{
			}
		}
	};
	
	builder.createToggleButtonAdaptor( new CoDoubleConverter( vm ), (CoCheckBox) getNamedWidget( LEFT_OUTSIDE_SENSITIVE ) );

	
		
	vm = new CoGsAspectAdaptor( this, RIGHT_MARGIN )
	{
		public Object get( CoObjectIF domain )
		{
			try
			{
				double d = ( (CoImmutableColumnGridIF) domain ).getRightMargin();
				return new Double( d );
			}
			catch ( ClassCastException ex )
			{
				return null;
			}
		}

		public void set( CoObjectIF domain, Object value )
		{
			try
			{
				( (CoColumnGridIF) domain ).setRightMargin( ( (Double) value ).doubleValue() );
			}
			catch ( ClassCastException ex )
			{
			}
		}
	};
	
	builder.createTextFieldAdaptor( new CoDoubleConverter( vm ), (CoTextField) getNamedWidget( RIGHT_MARGIN ) );

	
		
	vm = new CoGsAspectAdaptor( this, TOP_MARGIN )
	{
		public Object get( CoObjectIF domain )
		{
			try
			{
				double d = ( (CoImmutableColumnGridIF) domain ).getTopMargin();
				return new Double( d );
			}
			catch ( ClassCastException ex )
			{
				return null;
			}
		}

		public void set( CoObjectIF domain, Object value )
		{
			try
			{
				( (CoColumnGridIF) domain ).setTopMargin( ( (Double) value ).doubleValue() );
			}
			catch ( ClassCastException ex )
			{
			}
		}
	};
	
	builder.createTextFieldAdaptor( new CoDoubleConverter( vm ), (CoTextField) getNamedWidget( TOP_MARGIN ) );


	
	vm = new CoGsAspectAdaptor( this, BOTTOM_MARGIN )
	{
		public Object get( CoObjectIF domain )
		{
			try
			{
				double d = ( (CoImmutableColumnGridIF) domain ).getBottomMargin();
				return new Double( d );
			}
			catch ( ClassCastException ex )
			{
				return null;
			}
		}

		public void set( CoObjectIF domain, Object value )
		{
			try
			{
				( (CoColumnGridIF) domain ).setBottomMargin( ( (Double) value ).doubleValue() );
			}
			catch ( ClassCastException ex )
			{
			}
		}
	};
	
	builder.createTextFieldAdaptor( new CoDoubleConverter( vm ), (CoTextField) getNamedWidget( BOTTOM_MARGIN ) );


	
	vm = new CoGsAspectAdaptor( this, SPACING )
	{
		public Object get( CoObjectIF domain )
		{
			try
			{
				double d = ( (CoImmutableColumnGridIF) domain ).getColumnSpacing();
				return new Double( d );
			}
			catch ( ClassCastException ex )
			{
				return null;
			}
		}

		public void set( CoObjectIF domain, Object value )
		{
			try
			{
				( (CoColumnGridIF) domain ).setColumnSpacing( ( (Double) value ).doubleValue() );
			}
			catch ( ClassCastException ex )
			{
			}
		}
	};
	
	builder.createTextFieldAdaptor( new CoDoubleConverter( vm ), (CoTextField) getNamedWidget( SPACING ) );


	
	vm = new CoGsAspectAdaptor( this, COLUMN_COUNT )
	{
		protected Object validate( CoObjectIF subject, Object newValue ) throws java.beans.PropertyVetoException
		{
			if ( ( (Integer) newValue ).intValue() > 100 ) throw new java.beans.PropertyVetoException( "To many Columns ( >100 )", null );
			return newValue;
		}
		
		public Object get( CoObjectIF domain )
		{
			try
			{
				int i = ( (CoImmutableColumnGridIF) domain ).getColumnCount();
				return new Integer( i );
			}
			catch ( ClassCastException ex )
			{
				return null;
			}
		}

		public void set( CoObjectIF domain, Object value )
		{
			try
			{
				( (CoColumnGridIF) domain ).setColumnCount( ( (Integer) value ).intValue() );
			}
			catch ( ClassCastException ex )
			{
			}
		}
	};
	
	builder.createTextFieldAdaptor( new CoIntegerConverter( vm ), (CoTextField) getNamedWidget( COLUMN_COUNT ) );

}
protected void createWidgets( CoPanel p, CoUserInterfaceBuilder builder )
{
	super. createWidgets( p, builder );
	p.setLayout(new CoColumnLayout());
	

	// COLUMN GRID
	CoPanel marginPanel = builder.createPanel(new CoFormLayout(10,2),COLUMN_GRID);
	marginPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),CoPageItemUIStringResources.getName(COLUMN_GRID)));
	
	marginPanel.add( builder.createLabel( CoPageItemUIStringResources.getName( COLUMN_COUNT ) ) );
	marginPanel.add( builder.createTextField( CoTextField.RIGHT, 5, COLUMN_COUNT ) );

	marginPanel.add( builder.createLabel( CoPageItemUIStringResources.getName( SPACING ) ) );
	marginPanel.add( builder.createTextField( CoTextField.RIGHT, 5, SPACING ) );

	// MARGIN GRID
	CoPanel columnPanel = builder.createPanel( new CoFormLayout(10,2), MARGIN_GRID);
	columnPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),CoPageItemUIStringResources.getName(MARGIN_GRID)));

	final CoLabel leftMarginLabel = builder.createLabel( CoPageItemUIStringResources.getName( LEFT_MARGIN ) );
	columnPanel.add( leftMarginLabel );
	columnPanel.add( builder.createTextField( CoTextField.RIGHT, 5, LEFT_MARGIN ) );

	final CoLabel rightMarginLabel = builder.createLabel( CoPageItemUIStringResources.getName( RIGHT_MARGIN ) );
	columnPanel.add( rightMarginLabel );
	columnPanel.add( builder.createTextField( CoTextField.RIGHT, 5, RIGHT_MARGIN ) );

	columnPanel.add( builder.createLabel( CoPageItemUIStringResources.getName( TOP_MARGIN ) ) );
	columnPanel.add( builder.createTextField( CoTextField.RIGHT, 5, TOP_MARGIN ) );

	columnPanel.add( builder.createLabel( CoPageItemUIStringResources.getName( BOTTOM_MARGIN ) ) );
	columnPanel.add( builder.createTextField( CoTextField.RIGHT, 5, BOTTOM_MARGIN ) );

	columnPanel.add( builder.createLabel( CoPageItemUIStringResources.getName( LEFT_OUTSIDE_SENSITIVE ) ) );
	CoCheckBox cb = builder.createCheckBox( "", LEFT_OUTSIDE_SENSITIVE );
	columnPanel.add( cb );

	p.add(marginPanel);
	p.add(columnPanel);



	cb.addItemListener(
		new ItemListener()
		{
			public void itemStateChanged( ItemEvent ev )
			{
				boolean selected = ev.getStateChange() == ItemEvent.SELECTED;
				leftMarginLabel.setText( CoPageItemUIStringResources.getName( selected ? OUTSIDE_MARGIN : LEFT_MARGIN ) );
				rightMarginLabel.setText( CoPageItemUIStringResources.getName( selected ? OUTSIDE_MARGIN : RIGHT_MARGIN ) );
			}
		}
	);
}

/**
 * Insert the method's description here.
 * Creation date: (1999-09-17 14:10:22)
 * @param isDerived boolean
 */
public void setDerived(boolean isDerived)
{
	if (isDerived) setDomain(null);

	CoPanel panel = (CoPanel)getNamedWidget(COLUMN_GRID);
	panel.setEnabled(!isDerived);

	panel = (CoPanel)getNamedWidget(MARGIN_GRID);
	panel.setEnabled(!isDerived);
}

	public static final String INSIDE_MARGIN = "GRID_INSIDE_MARGIN";
	public static final String LEFT_OUTSIDE_SENSITIVE = "GRID_LEFT_OUTSIDE_SENSITIVE";
	public static final String OUTSIDE_MARGIN = "GRID_OUTSIDE_MARGIN";
}