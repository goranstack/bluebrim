package com.bluebrim.font.impl.client;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.*;

import com.bluebrim.font.impl.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;


/**
 * 
 * 
 * Creation date: (2001-01-19 16:48:37)
 * @author: Dennis
 */
 
public class CoFontMapPane extends CoPanel
{
	protected List m_faces;
	
	protected CoOptionMenu m_weightOptionMenu;
	protected CoOptionMenu m_styleOptionMenu;
	protected CoOptionMenu m_variantOptionMenu;
	protected CoOptionMenu m_stretchOptionMenu;
	protected CoOptionMenu m_faceOptionMenu;

	protected final class FaceMenuModel extends AbstractListModel implements ComboBoxModel
	{
		private Object m_selection;
		
 		public Object getSelectedItem()
 		{
	 		return m_selection;
 		}
  		
  	public void setSelectedItem( Object anItem )
  	{
	  	m_selection = anItem;
			fireContentsChanged( this, -1, -1 );
  	}
	
 		public Object getElementAt( int i )
 		{
	 		return m_faces.get( i );
 		}
  		
 		public int getSize()
 		{
	 		return m_faces.size();
 		}
	};


public CoFontMapPane( CoUserInterfaceBuilder builder, List faces )
{
	super( new CoRowLayout( true ) );

	builder.preparePanel( this );

	m_faces = faces;

	{
		CoPanel p = builder.createPanel( new GridLayout( 1, 4 ) );
		add( p );
		p.add( m_weightOptionMenu = builder.createOptionMenu() );
		p.add( m_styleOptionMenu = builder.createOptionMenu() );
		p.add( m_variantOptionMenu = builder.createOptionMenu() );
		p.add( m_stretchOptionMenu = builder.createOptionMenu() );
	}

	add( m_faceOptionMenu = builder.createOptionMenu( new FaceMenuModel() ) );

	m_weightOptionMenu.addItem( com.bluebrim.font.shared.CoFontAttribute.NORMAL_WEIGHT, true );
	m_weightOptionMenu.addItem( com.bluebrim.font.shared.CoFontAttribute.BOLD, true );

	m_styleOptionMenu.addItem( com.bluebrim.font.shared.CoFontAttribute.NORMAL_STYLE, true );
	m_styleOptionMenu.addItem( com.bluebrim.font.shared.CoFontAttribute.ITALIC, true );

	m_variantOptionMenu.addItem( com.bluebrim.font.shared.CoFontAttribute.NORMAL_VARIANT, true );
	m_variantOptionMenu.addItem( com.bluebrim.font.shared.CoFontAttribute.SMALL_CAPS, true );

	m_stretchOptionMenu.addItem( com.bluebrim.font.shared.CoFontAttribute.NORMAL_STRETCH, true );

	CoButton b = builder.createButton( CoFontResources.getName( "DELETE" ), null );
	add( b );
	b.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				JComponent p = (JComponent) getParent().getParent();
				getParent().remove( CoFontMapPane.this );
				p.revalidate();
			}
		}
	);
}
com.bluebrim.font.shared.CoFontFaceSpec createSpec( String family )
{
	com.bluebrim.font.shared.CoFontAttribute weight = (com.bluebrim.font.shared.CoFontAttribute) m_weightOptionMenu.getSelectedItem();
	com.bluebrim.font.shared.CoFontAttribute style = (com.bluebrim.font.shared.CoFontAttribute) m_styleOptionMenu.getSelectedItem();
	com.bluebrim.font.shared.CoFontAttribute variant = (com.bluebrim.font.shared.CoFontAttribute) m_variantOptionMenu.getSelectedItem();
	com.bluebrim.font.shared.CoFontAttribute stretch = (com.bluebrim.font.shared.CoFontAttribute) m_stretchOptionMenu.getSelectedItem();
	
	return new com.bluebrim.font.shared.CoFontFaceSpec( family, weight, style, variant, stretch );
}
com.bluebrim.font.shared.CoFontFace getFace()
{
	Object i = m_faceOptionMenu.getSelectedItem();
	return (com.bluebrim.font.shared.CoFontFace) i;
}
protected void set( com.bluebrim.font.shared.CoFontFace face )
{
	m_faceOptionMenu.setSelectedItem( face );
}
protected void set( com.bluebrim.font.shared.CoFontFaceSpec spec )
{
	m_weightOptionMenu.setSelectedItem( spec.getWeightAttribute() );
	m_styleOptionMenu.setSelectedItem( spec.getStyleAttribute() );
	m_variantOptionMenu.setSelectedItem( spec.getVariantAttribute() );
	m_stretchOptionMenu.setSelectedItem( spec.getStretchAttribute() );

}
public void set( com.bluebrim.font.shared.CoFontFaceSpec spec, com.bluebrim.font.shared.CoFontFace face )
{
	set( spec );
	set( face );
}
}