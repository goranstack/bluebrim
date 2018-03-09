package com.bluebrim.font.impl.client;

import java.util.*;

import com.bluebrim.gui.client.*;


/**
 * 
 * 
 * Creation date: (2001-01-19 16:48:37)
 * @author: Dennis
 */
 
public class CoDefaultFontMapPane extends CoFontMapPane
{

public CoDefaultFontMapPane( CoUserInterfaceBuilder builder, List faces )
{
	super( builder, faces );

	m_weightOptionMenu.addItem( com.bluebrim.font.shared.CoFontAttribute.DEFAULT, true );
	m_styleOptionMenu.addItem( com.bluebrim.font.shared.CoFontAttribute.DEFAULT, true );
	m_variantOptionMenu.addItem( com.bluebrim.font.shared.CoFontAttribute.DEFAULT, true );
	m_stretchOptionMenu.addItem( com.bluebrim.font.shared.CoFontAttribute.DEFAULT, true );

	m_weightOptionMenu.setSelectedItem( com.bluebrim.font.shared.CoFontAttribute.DEFAULT );
	m_styleOptionMenu.setSelectedItem( com.bluebrim.font.shared.CoFontAttribute.DEFAULT );
	m_variantOptionMenu.setSelectedItem( com.bluebrim.font.shared.CoFontAttribute.DEFAULT );
	m_stretchOptionMenu.setSelectedItem( com.bluebrim.font.shared.CoFontAttribute.DEFAULT );

	m_weightOptionMenu.setEnabled( false );
	m_styleOptionMenu.setEnabled( false );
	m_variantOptionMenu.setEnabled( false );
	m_stretchOptionMenu.setEnabled( false );

	remove( getComponentCount() - 1 );
}
com.bluebrim.font.shared.CoFontFaceSpec createSpec( String family )
{
	return new com.bluebrim.font.shared.CoFontFaceSpec( family, com.bluebrim.font.shared.CoFontConstants.DEFAULT, com.bluebrim.font.shared.CoFontConstants.DEFAULT, com.bluebrim.font.shared.CoFontConstants.DEFAULT, com.bluebrim.font.shared.CoFontConstants.DEFAULT );
}
protected void set( com.bluebrim.font.shared.CoFontFaceSpec spec )
{

}
}