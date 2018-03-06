package com.bluebrim.stroke.impl.client;
import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.paint.client.*;
import com.bluebrim.swing.client.*;

public class CoStrokeLayerUI extends CoDomainUserInterface
{
	public static final String DASH_COLOR = "dash_color";
	public static final String DASH_FOREGROUND_COLOR = "dash_foreground_color";
	public static final String DASH_BACKGROUND_COLOR = "dash_background_color";
	public static final String DASH_NO_COLOR = "dash_no_color";
	public static final String DASH_COLOR_SHADE = "dash_color_shade";
	public static final String DASH_PARAMETERS = "dash_parameters";
	public static final String DASH_WIDTH_PROPORTION = "dash_width_proportion";
	public static final String DASH = "dash";

	private CoAbstractListAspectAdaptor m_colorListAdaptor;
	
	protected com.bluebrim.stroke.impl.client.CoDashUI m_dashUI;

	protected com.bluebrim.paint.shared.CoColorCollectionIF m_colorCollection;


	private class ComboBoxRenderer extends com.bluebrim.paint.impl.client.CoColorListCellRenderer
	{
		public Component getListCellRendererComponent( JList list, 
					                                         Object value, 
					                                         int index, 
					                                         boolean isSelected, 
					                                         boolean cellHasFocus ) 
		{
			setIcon( null );
			if
				( value instanceof com.bluebrim.stroke.shared.CoDashColorIF )
			{
				super.getListCellRendererComponent( list, null, index, isSelected, cellHasFocus );
				if      ( value.equals( com.bluebrim.stroke.impl.shared.CoForegroundDashColor.getInstance() ) ) setText( com.bluebrim.stroke.impl.client.CoStrokeUIResources.getName( DASH_FOREGROUND_COLOR ) );
				else if ( value.equals( com.bluebrim.stroke.impl.shared.CoBackgroundDashColor.getInstance() ) ) setText( com.bluebrim.stroke.impl.client.CoStrokeUIResources.getName( DASH_BACKGROUND_COLOR ) );
				else if ( value.equals( com.bluebrim.stroke.impl.shared.CoNoDashColor.getInstance() ) )         setText( com.bluebrim.stroke.impl.client.CoStrokeUIResources.getName( DASH_NO_COLOR ) );
				else                                                            setText( value.toString() );
			} else {
				super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
			}
			return this;
		}
	};


public CoStrokeLayerUI()
{
	super();
}
public CoStrokeLayerUI(com.bluebrim.stroke.shared.CoStrokeLayerIF aDomainObject)
{
	super(aDomainObject);
}

protected CoUserInterfaceBuilder createUserInterfaceBuilder()
{
    return new CoNumberUserInterfaceBuilder(this);
}

protected void createListeners()
{
	super.createListeners();
	
	getNamedValueModel( DASH_COLOR ).addValueListener( new CoValueListener()
		{
			public void valueChange( CoValueChangeEvent ev )
			{
				getNamedWidget( DASH_COLOR_SHADE ).setEnabled( ev.getNewValue() instanceof com.bluebrim.paint.shared.CoColorIF );
			}
		} );
		
}
protected void createValueModels(CoUserInterfaceBuilder b) {
	
	super.createValueModels( b );
	CoNumberUserInterfaceBuilder numberBuilder = (CoNumberUserInterfaceBuilder)b;

	CoValueModel x = new CoReadOnlyAspectAdaptor( this, "" )
	{
		public Object get( CoObjectIF domain )
		{
			if ( domain == null ) return null;
			return ( (com.bluebrim.stroke.shared.CoStrokeLayerIF) domain ).getDash();
		}
	};

	b.createSubcanvasAdaptor( x, (CoSubcanvas) b.getNamedWidget( DASH ) );
	



	

	numberBuilder.createNumberFieldAdaptor(
		b.addAspectAdaptor(
			new CoGsAspectAdaptor( this, DASH_WIDTH_PROPORTION )
			{
				protected Object get(CoObjectIF subject) 
				{
					return new Float(((com.bluebrim.stroke.shared.CoStrokeLayerIF) subject).getWidthProportion());
				}
				public void set(CoObjectIF subject, Object newValue) 
				{
					((com.bluebrim.stroke.shared.CoStrokeLayerIF) subject).setWidthProportion((newValue != null) ? ((Number)newValue).floatValue() : 0f);
				}
			}
		),
		(CoTextField )getNamedWidget(DASH_WIDTH_PROPORTION),
		CoNumberConverter.FLOAT
	);	




	m_colorListAdaptor = b.addListAspectAdaptor(new CoAbstractListAspectAdaptor.Default(this, "COLORS") {
		public Object get(CoObjectIF subject)
		{
			return getColorsFor(subject);
		}
	});

	b.createOptionMenuAdaptor(
		m_colorListAdaptor,
		b.addAspectAdaptor(
			new CoGsAspectAdaptor(this, DASH_COLOR ) 
			{
				public Object get(CoObjectIF subject)
				{
					return getDashColorComboBoxItem( ( (com.bluebrim.stroke.shared.CoStrokeLayerIF) subject ).getColor() );
				}
				public void set(CoObjectIF subject, Object value)
				{
					setDashColor( (com.bluebrim.stroke.shared.CoStrokeLayerIF) subject, value );
				}
			}
		),
		(CoOptionMenu )getNamedWidget(DASH_COLOR)
	);







	

	numberBuilder.createNumberFieldAdaptor(
		b.addAspectAdaptor(
			new CoGsAspectAdaptor( this, DASH_COLOR_SHADE )
			{
				protected Object get(CoObjectIF subject) 
				{
					com.bluebrim.stroke.shared.CoDashColorIF cs = ( (com.bluebrim.stroke.shared.CoStrokeLayerIF) subject ).getColor();
					if
						( cs instanceof com.bluebrim.stroke.shared.CoAbsoluteDashColorIF )
					{
						return new Float( ( (com.bluebrim.stroke.shared.CoAbsoluteDashColorIF) cs ).getShade() );
					} else {
						return null;
					}
				}
				public void set(CoObjectIF subject, Object newValue) 
				{
					com.bluebrim.stroke.shared.CoDashColorIF cs = ( (com.bluebrim.stroke.shared.CoStrokeLayerIF) subject ).getColor();
					if
						( cs instanceof com.bluebrim.stroke.shared.CoAbsoluteDashColorIF )
					{
						( (com.bluebrim.stroke.shared.CoAbsoluteDashColorIF) cs ).setShade( (newValue != null) ? ((Number)newValue).floatValue() : 0f );
					}
					
				}
			}
		),
		(CoTextField )getNamedWidget(DASH_COLOR_SHADE),
		CoNumberConverter.FLOAT
	);	

}
protected void createWidgets( CoPanel p, CoUserInterfaceBuilder b )
{
	super.createWidgets( p, b );

	p.setLayout( new CoColumnLayout( true ) );

	// width and color
	{
		CoPanel tmp = b.createPanel( new CoFormLayout() );
		tmp.setBorder( new TitledBorder( com.bluebrim.stroke.impl.client.CoStrokeUIResources.getName( DASH_PARAMETERS ) ) );
		
		tmp.add( b.createLabel( com.bluebrim.stroke.impl.client.CoStrokeUIResources.getName( DASH_WIDTH_PROPORTION ) ) );
		tmp.add( b.createTextField( SwingConstants.RIGHT, 5, DASH_WIDTH_PROPORTION ) );
		
		tmp.add( b.createLabel( com.bluebrim.stroke.impl.client.CoStrokeUIResources.getName( DASH_COLOR ) ) );

		CoColorPanel cp = new CoColorPanel( b );
		tmp.add( cp );
		b.addNamedWidget( DASH_COLOR, cp.getColorOptionMenu() );
		b.addNamedWidget( DASH_COLOR_SHADE, cp.getShadeTextField() );
		cp.getColorOptionMenu().setRenderer( new ComboBoxRenderer() );
		updateColors();

		p.add( tmp );
	}

	// dash
	{
		m_dashUI = new com.bluebrim.stroke.impl.client.CoDashUI();
		CoSubcanvas sc = b.createSubcanvas( m_dashUI, DASH );
		p.add( sc );
	}

}
public List getColorsFor(CoObjectIF object)
{
	List colors = new ArrayList();

	if
		( object != null )
	{
		colors.add( com.bluebrim.stroke.impl.shared.CoForegroundDashColor.getInstance() );
		colors.add( com.bluebrim.stroke.impl.shared.CoBackgroundDashColor.getInstance() );
		colors.add( com.bluebrim.stroke.impl.shared.CoNoDashColor.getInstance() );

		if
			( m_colorCollection != null )
		{
			java.util.Iterator i = m_colorCollection.getColors().iterator();

			while
				( i.hasNext() )
			{
				colors.add( i.next() );
			}
		}
	}
	return colors;
}
private Object getDashColorComboBoxItem( com.bluebrim.stroke.shared.CoDashColorIF cs )
{
	if
		(
			( cs.equals( com.bluebrim.stroke.impl.shared.CoForegroundDashColor.getInstance() ) )
		||
			( cs.equals( com.bluebrim.stroke.impl.shared.CoBackgroundDashColor.getInstance() ) )
		||
			( cs.equals( com.bluebrim.stroke.impl.shared.CoNoDashColor.getInstance() ) )
		)
	{
		return cs;
	} else {
		return ( (com.bluebrim.stroke.shared.CoAbsoluteDashColorIF) cs ).getColor();
	}
}
protected Insets getDefaultPanelInsets()
{
	return null;
}

private void setDashColor( com.bluebrim.stroke.shared.CoStrokeLayerIF subject, Object value )
{
	if
		( value instanceof com.bluebrim.stroke.shared.CoDashColorIF )
	{
		subject.setColor( (com.bluebrim.stroke.shared.CoDashColorIF) value );
	} else {
		
		com.bluebrim.stroke.shared.CoAbsoluteDashColorIF acs = subject.setAbsoluteColor();
		acs.setColor( (com.bluebrim.paint.shared.CoColorIF) value );
	}

}
public void setEnabled(boolean enable)
{
	super.setEnabled( enable );

	boolean e = enable && canBeEnabled();

	getNamedValueModel( DASH_WIDTH_PROPORTION ).setEnabled( e );
	getNamedValueModel( DASH_COLOR ).setEnabled( e );		
	getNamedValueModel( DASH_COLOR_SHADE ).setEnabled( e );		
}
public void updateColors()
{
	CoOptionMenu cb = (CoOptionMenu) getNamedWidget( DASH_COLOR );

	if
		( cb != null )
	{
		cb.removeAllItems();

		cb.addItem( com.bluebrim.stroke.impl.shared.CoForegroundDashColor.getInstance() );
		cb.addItem( com.bluebrim.stroke.impl.shared.CoBackgroundDashColor.getInstance() );
		cb.addItem( com.bluebrim.stroke.impl.shared.CoNoDashColor.getInstance() );

		if
			( m_colorCollection != null )
		{
			Iterator i = m_colorCollection.getColors().iterator();

			while
				( i.hasNext() )
			{
				cb.addItem( i.next() );
			}
		}

		cb.revalidate();
	}
}
public void valueHasChanged()
{
	super.valueHasChanged();

	m_dashUI.valueHasChanged();
}

public void setColorCollection( com.bluebrim.paint.shared.CoColorCollectionIF colorCollection )
{
	m_colorCollection = colorCollection;
	if ( m_colorListAdaptor != null ) m_colorListAdaptor.listHasChanged(this);
}
}