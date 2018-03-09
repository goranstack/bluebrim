package com.bluebrim.text.impl.client;

import com.bluebrim.base.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-05-16 13:57:50)
 * @author: Dennis
 */

public class CoCharacterPropertiesUI extends CoDomainUserInterface
{
	public static final String KERN_ABOVE_SIZE = "CoCharacterPropertiesUI.KERN_ABOVE_SIZE";
	public static final String SIMULATE_QXP_JUSTIFICATION_BUG = "CoCharacterPropertiesUI.SIMULATE_QXP_JUSTIFICATION_BUG";

	private CoTextField m_kernAboveTextField;
	private CoLabel m_kernAboveLabel;

	private CoTriStateCheckBox m_simulateQxpJustificationBugCheckBox;

public CoCharacterPropertiesUI() {
	super();
}

protected CoUserInterfaceBuilder createUserInterfaceBuilder()
{
    return new CoNumberUserInterfaceBuilder(this);
}

protected void createValueModels( CoUserInterfaceBuilder b )
{
	super.createValueModels( b );
	CoNumberUserInterfaceBuilder numberBuilder = (CoNumberUserInterfaceBuilder)b;


	numberBuilder.createNumberFieldAdaptor( 
		b.addAspectAdaptor(
			new CoGsAspectAdaptor( this, KERN_ABOVE_SIZE )
			{
				protected Object get( CoObjectIF subject )
				{
					float kas = ( (CoTextParameters) subject ).getKernAboveSize( true );
					return Float.isNaN( kas ) ? null : new Float( kas );
				}
				
				public void set(CoObjectIF subject, Object newValue)
				{
					CoStyledTextPreferencesIF pip = (CoStyledTextPreferencesIF) subject;
					pip.setKernAboveSize( (newValue != null) ? ((Number)newValue).floatValue() : Float.NaN );
				}
			}
		),
		m_kernAboveTextField,
		CoNumberConverter.FLOAT,
		null
	);


	b.createLabelAdaptor( 
		b.addAspectAdaptor(
			new CoReadOnlyAspectAdaptor( this, "EFFECTIVE_" + KERN_ABOVE_SIZE )
			{
				protected Object get( CoObjectIF subject )
				{
					float local = ( (CoTextParameters) subject ).getKernAboveSize( true );
					return ! Float.isNaN( local ) ? " " : " (" + Float.toString( ( (CoTextParameters) subject ).getKernAboveSize( false ) ) + ")";
				}
			}
		),
		m_kernAboveLabel
	);





	b.createTriStateCheckBoxAdaptor(
		b.addAspectAdaptor(
			new CoGsAspectAdaptor( this, SIMULATE_QXP_JUSTIFICATION_BUG )
			{
				protected Object get( CoObjectIF subject )
				{
					return ( (CoStyledTextPreferencesIF) subject ).getUseQxpJustification();
				}
				
				public void set(CoObjectIF subject, Object newValue)
				{
					CoStyledTextPreferencesIF pip = (CoStyledTextPreferencesIF) subject;
					pip.setUseQxpJustification( (Boolean) newValue );
				}
			}
		),
		m_simulateQxpJustificationBugCheckBox
	);

	
}
protected void createWidgets( CoPanel p, CoUserInterfaceBuilder b )
{
	super.createWidgets( p, b );

	p.setLayout( new CoColumnLayout( 5 ) );
	
	CoPanel P = b.createPanel( new CoRowLayout( 5 ) );

	p.add( P );

	P.add( b.createLabel( CoTextStringResources.getName( KERN_ABOVE_SIZE ) ) );

	m_kernAboveTextField = b.createSlimTextField( CoTextField.RIGHT, 2 );
	P.add( m_kernAboveTextField );

	m_kernAboveLabel = b.createLabel( " " );
	P.add( m_kernAboveLabel );


	m_simulateQxpJustificationBugCheckBox = b.createTriStateCheckBox( CoTextStringResources.getName( SIMULATE_QXP_JUSTIFICATION_BUG ) );
	p.add( m_simulateQxpJustificationBugCheckBox );
}
}
