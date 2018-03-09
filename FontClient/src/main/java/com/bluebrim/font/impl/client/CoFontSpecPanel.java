package com.bluebrim.font.impl.client;

import com.bluebrim.font.impl.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;


/**
 * 
 * 
 * @author: Dennis
 */
 
public class CoFontSpecPanel extends CoPanel
{
	protected CoTextField m_familyNameTextField;
	protected CoOptionMenu m_stretchOptionMenu;
	protected CoOptionMenu m_styleOptionMenu;
	protected CoOptionMenu m_variantOptionMenu;
	protected CoOptionMenu m_weightOptionMenu;

public CoFontSpecPanel( CoUserInterfaceBuilder builder )
{
	super( new CoFormLayout() );

	builder.preparePanel( this );

	add( builder.createLabel( CoFontResources.getName( "FAMILY" ) ) );
	add( m_familyNameTextField = builder.createTextField() );
	m_familyNameTextField.setColumns( 15 );
	
	add( builder.createLabel( CoFontResources.getName( "WEIGHT" ) ) );
	add( m_weightOptionMenu = builder.createOptionMenu() );
	
	add( builder.createLabel( CoFontResources.getName( "STYLE" ) ) );
	add( m_styleOptionMenu = builder.createOptionMenu() );
	
	add( builder.createLabel( CoFontResources.getName( "VARIANT" ) ) );
	add( m_variantOptionMenu = builder.createOptionMenu() );
	
	add( builder.createLabel( CoFontResources.getName( "STRETCH" ) ) );
	add( m_stretchOptionMenu = builder.createOptionMenu() );

	
	m_weightOptionMenu.addItem( com.bluebrim.font.shared.CoFontAttribute.NORMAL_WEIGHT, true );
	m_weightOptionMenu.addItem( com.bluebrim.font.shared.CoFontAttribute.BOLD, true );

	m_styleOptionMenu.addItem( com.bluebrim.font.shared.CoFontAttribute.NORMAL_STYLE, true );
	m_styleOptionMenu.addItem( com.bluebrim.font.shared.CoFontAttribute.ITALIC, true );

	m_variantOptionMenu.addItem( com.bluebrim.font.shared.CoFontAttribute.NORMAL_VARIANT, true );
	m_variantOptionMenu.addItem( com.bluebrim.font.shared.CoFontAttribute.SMALL_CAPS, true );

	m_stretchOptionMenu.addItem( com.bluebrim.font.shared.CoFontAttribute.NORMAL_STRETCH, true );
}
public com.bluebrim.font.shared.CoFontFaceSpec get()
{
	return
		new com.bluebrim.font.shared.CoFontFaceSpec(
			m_familyNameTextField.getText(),
			(com.bluebrim.font.shared.CoFontAttribute) m_weightOptionMenu.getSelectedItem(),
			(com.bluebrim.font.shared.CoFontAttribute) m_styleOptionMenu.getSelectedItem(),
			(com.bluebrim.font.shared.CoFontAttribute) m_variantOptionMenu.getSelectedItem(),
			(com.bluebrim.font.shared.CoFontAttribute) m_stretchOptionMenu.getSelectedItem()
		);
}
public void set( com.bluebrim.font.shared.CoFontFaceSpec spec )
{
	m_familyNameTextField.setText( spec.getFamilyName() );
	m_weightOptionMenu.setSelectedItem( spec.getWeightAttribute() );
	m_styleOptionMenu.setSelectedItem( spec.getStyleAttribute() );
	m_variantOptionMenu.setSelectedItem( spec.getVariantAttribute() );
	m_stretchOptionMenu.setSelectedItem( spec.getStretchAttribute() );
}
}