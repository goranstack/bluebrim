package com.bluebrim.text.impl.client;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.font.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.observable.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;


/**
 * Subklass till CoComponentCharacterStyleUI vars uppgift är att agera UI åt
 * en CoCharacterStyleIF-instans.
 * Värdemodeller för att åtstadkomma detta skapas.
 * Dessutom skapas en "lyssnare" på "plain"-knappen som verkar på värdemodellerna.
 */
 

public abstract class CoCharacterStyleUI extends CoAbstractCharacterStyleUI
{
	private class ContextListener implements CoChangedObjectListener
	{
		public void serverObjectChanged( CoChangedObjectEvent e )
		{
			contextChanged();
		}
	};
	
	private CoChangedObjectListener m_contextListener = new ContextListener();

/**
 * CoEditionUI constructor comment.
 */
public CoCharacterStyleUI()
{
	super();
}
private void contextChanged()
{
	postContextChange( getContext() );
}


/**
 */
protected void createValueModels(CoUserInterfaceBuilder builder )
{
	CoAspectAdaptor ad;


	ad = new CoGsAspectAdaptor( this, CoTextConstants.FONT_FAMILY )
	{
		public Object get( CoObjectIF subject ) 
		{
			return ( (CoCharacterStyleIF) subject ).getFontFamily();
		}
		public void set( CoObjectIF subject, Object value )
		{
			( (CoCharacterStyleIF) subject ).setFontFamily( (String) value );
		}
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createOptionMenuAdaptor( ad, m_familyOptionMenu );

	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.FOREGROUND_COLOR )
	{
		public Object get( CoObjectIF subject ) 
		{
			return ( (CoCharacterStyleIF) subject ).getForegroundColor();
		}
			
		public void set( CoObjectIF subject, Object value )
		{
			( (CoCharacterStyleIF) subject ).setForegroundColor( (String) value );
		}
	};
	builder.addAspectAdaptor( ad );
	builder.createOptionMenuAdaptor( ad, m_colorOptionMenu ); 

	

	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.UNDERLINE )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoCharacterStyleIF) subject ).getUnderline(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoCharacterStyleIF) subject ).setUnderline( (CoEnumValue) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createOptionMenuAdaptor( ad, m_underlineOptionMenu );


	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.VERTICAL_POSITION )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoCharacterStyleIF) subject ).getVerticalPosition(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoCharacterStyleIF) subject ).setVerticalPosition( (CoEnumValue) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createOptionMenuAdaptor( ad, m_verticalPositionOptionMenu );



	ad = new CoGsAspectAdaptor( this, CoTextConstants.FOREGROUND_SHADE )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoCharacterStyleIF) subject ).getForegroundShade(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoCharacterStyleIF) subject ).setForegroundShade( (Float) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createTextFieldAdaptor( new CoFloatConverter( ad ), m_shadeTextField ); 





	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.FONT_SIZE )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoCharacterStyleIF) subject ).getFontSize(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoCharacterStyleIF) subject ).setFontSize( (Float) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createComboBoxAdaptor( new CoFloatConverter( ad ), m_sizeComboBox ); 


	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.TRACK_AMOUNT )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoCharacterStyleIF) subject ).getTrackAmount(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoCharacterStyleIF) subject ).setTrackAmount( (Float) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createTextFieldAdaptor( new CoFloatConverter( ad ), m_trackAmountTextField ); 


	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.BASELINE_OFFSET )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoCharacterStyleIF) subject ).getBaselineOffset(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoCharacterStyleIF) subject ).setBaselineOffset( (Float) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createTextFieldAdaptor( new CoFloatConverter( ad ), m_baselineOffsetTextField ); 

		
	ad = new CoGsAspectAdaptor( this, CoTextConstants.HORIZONTAL_SCALE )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoCharacterStyleIF) subject ).getHorizontalScale(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoCharacterStyleIF) subject ).setHorizontalScale( (Float) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createTextFieldAdaptor( new CoFloatConverter( ad ), m_horizontalScaleTextField ); 

	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.VERTICAL_SCALE )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoCharacterStyleIF) subject ).getVerticalScale(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoCharacterStyleIF) subject ).setVerticalScale( (Float) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createTextFieldAdaptor( new CoFloatConverter( ad ), m_verticalScaleTextField ); 


	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.WEIGHT )
	{
		public Object get( CoObjectIF subject ) 
			{ return CoStyleConstants.enum2Boolean( ( (CoCharacterStyleIF) subject ).getWeight(), CoFontAttribute.BOLD ); }
		public void set( CoObjectIF subject, Object value )
			{
				if ( value == null ) ( (CoCharacterStyleIF) subject ).setWeight( null );
				else                 ( (CoCharacterStyleIF) subject ).setWeight( ( (Boolean) value ).booleanValue() ? CoFontAttribute.BOLD : CoFontAttribute.NORMAL_WEIGHT );
			}
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createTriStateCheckBoxAdaptor( ad, m_boldCheckbox );


	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.STYLE )
	{
		public Object get( CoObjectIF subject ) 
			{ return CoStyleConstants.enum2Boolean( ( (CoCharacterStyleIF) subject ).getStyle(), CoFontAttribute.ITALIC ); }
		public void set( CoObjectIF subject, Object value )
		{
				if ( value == null ) ( (CoCharacterStyleIF) subject ).setStyle( null );
				else                 ( (CoCharacterStyleIF) subject ).setStyle( ( (Boolean) value ).booleanValue() ? CoFontAttribute.ITALIC : CoFontAttribute.NORMAL_STYLE );
		}
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createTriStateCheckBoxAdaptor( ad, m_italicCheckbox );


	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.STRIKE_THRU )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoCharacterStyleIF) subject ).getStrikeThru(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoCharacterStyleIF) subject ).setStrikeThru( (Boolean) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createTriStateCheckBoxAdaptor( ad, m_strikeThruCheckbox );

	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.SHADOW )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoCharacterStyleIF) subject ).getShadow(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoCharacterStyleIF) subject ).setShadow( (Boolean) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createTriStateCheckBoxAdaptor( ad, m_shadowCheckbox );


	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.ALL_CAPS )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoCharacterStyleIF) subject ).getAllCaps(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoCharacterStyleIF) subject ).setAllCaps( (Boolean) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createTriStateCheckBoxAdaptor( ad, m_allCapsCheckbox );


	/*
	ad = new CoGsAspectAdaptor( this, CoTextConstants.VARIANT )
	{
		public Object get( CoObjectIF subject ) 
			{ return CoStyleConstants.enum2Boolean( ( (CoCharacterStyleIF) subject ).getVariant(), CoStyleConstants.SMALL_CAPS ); }
		public void set( CoObjectIF subject, Object value )
		{
				if ( value == null ) ( (CoCharacterStyleIF) subject ).setVariant( null );
				else                 ( (CoCharacterStyleIF) subject ).setVariant( ( (Boolean) value ).booleanValue() ? CoStyleConstants.SMALL_CAPS : CoStyleConstants.NORMAL_VARIANT );
		}
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createTriStateCheckBoxAdaptor( ad, (CoTriStateCheckBox) getNamedWidget( CoTextConstants.VARIANT ) );
*/

	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.SUPERIOR )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoCharacterStyleIF) subject ).getSuperior(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoCharacterStyleIF) subject ).setSuperior( (Boolean) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createTriStateCheckBoxAdaptor( ad, m_superiorCheckbox );






	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.SHADOW_OFFSET )
	{
		public Object get( CoObjectIF subject ) 
		{
			return ( (CoCharacterStyleIF) subject ).getShadowOffset();
		}
		
		public void set( CoObjectIF subject, Object value )
		{
			( (CoCharacterStyleIF) subject ).setShadowOffset( (Float) value );
		}
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createTextFieldAdaptor( new CoFloatConverter( ad ), m_shadowOffsetTextField ); 

	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.SHADOW_ANGLE )
	{
		public Object get( CoObjectIF subject ) 
		{
			return ( (CoCharacterStyleIF) subject ).getShadowAngle();
		}
		
		public void set( CoObjectIF subject, Object value )
		{
			( (CoCharacterStyleIF) subject ).setShadowAngle( (Float) value );
		}
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createTextFieldAdaptor( new CoFloatConverter( ad ), m_shadowAngleTextField ); 

	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.SHADOW_COLOR )
	{
		public Object get( CoObjectIF subject ) 
		{
			return ( (CoCharacterStyleIF) subject ).getShadowColor();
		}
			
		public void set( CoObjectIF subject, Object value )
		{
			( (CoCharacterStyleIF) subject ).setShadowColor( (String) value );
		}
	};
	builder.addAspectAdaptor( ad );
	builder.createOptionMenuAdaptor( ad, m_shadowColorOptionMenu ); 

	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.SHADOW_SHADE )
	{
		public Object get( CoObjectIF subject ) 
		{
			return ( (CoCharacterStyleIF) subject ).getShadowShade();
		}
		
		public void set( CoObjectIF subject, Object value )
		{
			( (CoCharacterStyleIF) subject ).setShadowShade( (Float) value );
		}
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createTextFieldAdaptor( new CoFloatConverter( ad ), m_shadowShadeTextField ); 
	
}
protected void doAfterCreateUserInterface()
{
	super.doAfterCreateUserInterface();
	
	setContext( getInitialContext() );
}


protected abstract CoTypographyContextIF getInitialContext();
public void setContext( CoTypographyContextIF context )
{
	if ( m_context == context ) return;
	
	if
		( m_context != null )
	{
		CoObservable.removeChangedObjectListener( m_contextListener, getContext() );
	}
	
	super.setContext( context );

	if
		( m_context != null )
	{
		CoObservable.addChangedObjectListener( m_contextListener, getContext() );
	}
}




public CoTextField createLengthTextField( CoUserInterfaceBuilder b, String name, CoLengthUnitSet us )
{
	return b.createSlimTextField( SwingConstants.RIGHT, name );
}
}