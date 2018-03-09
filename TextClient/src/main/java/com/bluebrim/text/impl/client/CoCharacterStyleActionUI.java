package com.bluebrim.text.impl.client;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.text.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.client.actions.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;



/**
 * Dialog för att sätta teckenutformning.
 * Dialogen saknar 'valuemodel', istället är Action-objekt kopplade till kontrollerna.
 * Det normala är att dessa actions direktmanipulerar den selekterade texten i en CoAbstractTextPane.
 */
public class CoCharacterStyleActionUI extends CoAbstractCharacterStyleUI implements CoAttributeListenerIF
{
	private CoAbstractTextEditor m_editor;
	private boolean m_isApplying;
	private Map m_actions;

public CoCharacterStyleActionUI() {
	super();
}
/**
 * com.bluebrim.publication.impl.client.CoEditionUI constructor comment.
 * @param aDomainObject java.lang.Object
 */
public CoCharacterStyleActionUI( Action[] actions, CoAbstractTextEditor editor )
{
	super();

	m_actions = CoActionUtilities.actionsToHashtable(actions);

	buildForComponent();
	
	setEditor( editor );
}
public void attributesChanged( AttributeSet as )
{
 	com.bluebrim.font.shared.CoFontAttribute fa;
	Boolean b;
	CoEnumValue e;
	String s;
	Float f;

	// bold
	fa = CoStyleConstants.getWeight( as );
	if
		( fa == CoStyleConstants.AS_IS_FONT_ATTRIBUTE_VALUE )
	{
		m_boldCheckbox.setAsIs();
	} else {
		m_boldCheckbox.setTriState( CoStyleConstants.enum2Boolean( fa, com.bluebrim.font.shared.CoFontAttribute.BOLD ) );
	}

	// italic
	fa = CoStyleConstants.getStyle( as );
	if
		( fa == CoStyleConstants.AS_IS_FONT_ATTRIBUTE_VALUE )
	{
		m_italicCheckbox.setAsIs();
	} else {
		m_italicCheckbox.setTriState( CoStyleConstants.enum2Boolean( fa, com.bluebrim.font.shared.CoFontAttribute.ITALIC ) );
	}
  
 
	// strike thru
	b = CoStyleConstants.getStrikeThru( as );
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		m_strikeThruCheckbox.setAsIs();
	} else {
	  m_strikeThruCheckbox.setTriState( b );
	}

	/*
	// outline
	b = CoStyleConstants.getOutline( as );
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		m_outlineCheckbox.setAsIs();
	} else {
	  m_outlineCheckbox.setTriState( b );
	}
	*/

	// shadow
	b = CoStyleConstants.getShadow( as );
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		m_shadowCheckbox.setAsIs();
	} else {
	  m_shadowCheckbox.setTriState( b );
	}

	// all caps
	b = CoStyleConstants.getAllCaps( as );
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		m_allCapsCheckbox.setAsIs();
	} else {
	  m_allCapsCheckbox.setTriState( b );
	}

	/*	
	// small caps
	fa = CoStyleConstants.getVariant( as );
	if
		( fa == CoStyleConstants.AS_IS_FONT_ATTRIBUTE_VALUE )
	{
		m_smallCapsCheckbox.setAsIs();
	} else {
		m_smallCapsCheckbox.setTriState( CoStyleConstants.enum2Boolean( fa, CoStyleConstants.SMALL_CAPS ) );
	}
	*/
  
	// superior
	b = CoStyleConstants.getSuperior( as );
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		m_superiorCheckbox.setAsIs();
	} else {
	  m_superiorCheckbox.setTriState( b );
	}

	// font
	s = CoStyleConstants.getFontFamily( as );
	if
		( s == CoStyleConstants.AS_IS_STRING_VALUE )
	{
		m_familyOptionMenu.setAsIs();
	} else {
  	m_familyOptionMenu.setSelectedItem( s, true );
	}

	// font size
	f = CoStyleConstants.getFontSize( as );
  if
  	( f == null )
  {
	  m_sizeComboBox.setSelectedItem( null, true );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
	  m_sizeComboBox.setAsIs();
  } else {
	  m_sizeComboBox.setSelectedItem( "" + f, true );
  }

	// underline
	e = CoStyleConstants.getUnderline( as );
	if
		( e == CoStyleConstants.AS_IS_ENUM_VALUE )
	{
		m_underlineOptionMenu.setAsIs();
	} else {
		m_underlineOptionMenu.setSelectedItem( e, true );
	}

	// vertical position
	e = CoStyleConstants.getVerticalPosition( as );
	if
		( e == CoStyleConstants.AS_IS_ENUM_VALUE )
	{
		m_verticalPositionOptionMenu.setAsIs();
	} else {
		m_verticalPositionOptionMenu.setSelectedItem( e, true );
	}

	// color
	s = CoStyleConstants.getForegroundColor( as );
	if
		( s == CoStyleConstants.AS_IS_STRING_VALUE )
	{
		m_colorOptionMenu.setAsIs();
	} else {
  	m_colorOptionMenu.setSelectedItem( s, true );
	}

	// shade  
  f = CoStyleConstants.getForegroundShade( as );
  if
  	( f == null )
  {
	  m_shadeTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
	  m_shadeTextField.setAsIs();
  } else {
	  m_shadeTextField.setText( CoLengthUnitSet.format( f.floatValue() ) );
  }
  
	// tracking  
  f = CoStyleConstants.getTrackAmount( as );
  if
  	( f == null )
  {
	  m_trackAmountTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
	  m_trackAmountTextField.setAsIs();
  } else {
	  m_trackAmountTextField.setText( "" + f );
  }

  // baseline offset
  f = CoStyleConstants.getBaselineOffset( as );
  if
  	( f == null )
  {
	  m_baselineOffsetTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
	  m_baselineOffsetTextField.setAsIs();
  } else {
	  m_baselineOffsetTextField.setText( "" + f );//CoLengthUnitSet.format( f.floatValue(), CoLengthUnit.POINTS, CoLengthUnit.LENGTH_UNITS.getViewDecimalCount() ) );
  }
 
  // horizontal scale
  f = CoStyleConstants.getHorizontalScale( as );
  if
  	( f == null )
  {
	  m_horizontalScaleTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_horizontalScaleTextField.setAsIs();
  } else {
	  m_horizontalScaleTextField.setText( f.toString() );
  }
  
  // vertical scale
  f = CoStyleConstants.getVerticalScale( as );
  if
  	( f == null )
  {
	  m_verticalScaleTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_verticalScaleTextField.setAsIs();
  } else {
	  m_verticalScaleTextField.setText( f.toString() );
  }
  
  // shadow offset
  f = CoStyleConstants.getShadowOffset( as );
  if
  	( f == null )
  {
	  m_shadowOffsetTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_shadowOffsetTextField.setAsIs();
  } else {
	  m_shadowOffsetTextField.setText( "" + f );
  }
  
  // shadow angle
  f = CoStyleConstants.getShadowAngle( as );
  if
  	( f == null )
  {
	  m_shadowAngleTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_shadowAngleTextField.setAsIs();
  } else {
	  m_shadowAngleTextField.setText( f.toString() );
  }

	// shadow color
	s = CoStyleConstants.getShadowColor( as );
	if
		( s == CoStyleConstants.AS_IS_STRING_VALUE )
	{
		m_shadowColorOptionMenu.setAsIs();
	} else {
  	m_shadowColorOptionMenu.setSelectedItem( s, true );
	}

	// shadow shade  
  f = CoStyleConstants.getShadowShade( as );
  if
  	( f == null )
  {
	  m_shadowShadeTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
	  m_shadowShadeTextField.setAsIs();
  } else {
	  m_shadowShadeTextField.setText( CoLengthUnitSet.format( f.floatValue() ) );
  }

 
	getPanel().repaint();

}
public void attributesChanged( CoAttributeEvent e )
{
	if
		( e.didEditableChange() )
	{
		setAllEnabled( m_editor.isEditable() );
		return;
	}
	
	if ( m_isApplying ) return;

	attributesChanged( e.getCharacterAttributes() );
}

protected void doAfterCreateUserInterface( ) 
{
	super.doAfterCreateUserInterface();
	
	class _ implements ActionListener
	{
		private ActionListener m_delegate;
		public _( ActionListener l ) { m_delegate = l; }
		public void actionPerformed( ActionEvent e )
		{
			m_isApplying = true;
			m_delegate.actionPerformed( e );
			m_isApplying = false;
		}
	};

	m_boldCheckbox.addActionListener( new _( getAction( CoStyledEditorKit.boldCharacterAction ) ) );
	m_italicCheckbox.addActionListener( new _( getAction( CoStyledEditorKit.italicCharacterAction ) ) );
	m_strikeThruCheckbox.addActionListener( new _( getAction( CoStyledEditorKit.strikeThruCharacterAction ) ) );
	m_shadowCheckbox.addActionListener( new _( getAction( CoStyledEditorKit.shadowCharacterAction ) ) );
	m_allCapsCheckbox.addActionListener( new _( getAction( CoStyledEditorKit.allCapsCharacterAction ) ) );
	m_superiorCheckbox.addActionListener( new _( getAction( CoStyledEditorKit.superiorCharacterAction ) )) ;
	m_underlineOptionMenu.addActionListener( new _( getAction( CoStyledEditorKit.underlineCharacterAction ) ) );
	m_verticalPositionOptionMenu.addActionListener( new _( getAction( CoStyledEditorKit.verticalPositionCharacterAction ) ) );

	m_familyOptionMenu.addActionListener( new _( getAction( CoStyledEditorKit.fontFamilyCharacterAction ) ) );
	m_sizeComboBox.addActionListener( new _( getAction( CoStyledEditorKit.fontSizeCharacterAction ) ) );
	m_colorOptionMenu.addActionListener( new _( getAction( CoStyledEditorKit.foregroundColorCharacterAction ) ) );
	m_shadeTextField.addActionListener( new _( getAction( CoStyledEditorKit.foregroundShadeCharacterAction ) ) );
	m_trackAmountTextField.addActionListener( new _( getAction( CoStyledEditorKit.trackAmountCharacterAction ) ) );
	m_baselineOffsetTextField.addActionListener( new _( getAction( CoStyledEditorKit.baselineOffsetCharacterAction ) ) );
	m_horizontalScaleTextField.addActionListener( new _( getAction( CoStyledEditorKit.horizontalScaleCharacterAction ) ) );
	m_verticalScaleTextField.addActionListener( new _( getAction( CoStyledEditorKit.verticalScaleCharacterAction ) ) );

	m_shadowOffsetTextField.addActionListener( new _( getAction( CoStyledEditorKit.shadowOffsetCharacterAction ) ) );
	m_shadowAngleTextField.addActionListener( new _( getAction( CoStyledEditorKit.shadowAngleCharacterAction ) ) );
	m_shadowColorOptionMenu.addActionListener( new _( getAction( CoStyledEditorKit.shadowColorCharacterAction ) ) );
	m_shadowShadeTextField.addActionListener( new _( getAction( CoStyledEditorKit.shadowShadeCharacterAction ) ) );

}
private Action getAction( String cmd )
{
	return (Action) m_actions.get (cmd );
}
public void open()
{
	Window d = CoWindowList.findWindowFor( this );
	if
		( d != null )
	{
		( (CoDialog) d ).setLocationRelativeTo( m_editor );
	} else {
		Container w = m_editor.getTopLevelAncestor();
		if
			( w instanceof Frame )
		{
			d = new CoDialog( (Frame) w, CoTextStringResources.getName( CoTextConstants.CHARACTER_STYLE ), false, this );
		} else if
			( w instanceof Dialog )
		{
			d = new CoDialog( (Dialog) w, CoTextStringResources.getName( CoTextConstants.CHARACTER_STYLE ), false, this );
		} else {
			return;
		}
	}
	
	
	d.show();
}
protected void setAllEnabled( boolean b )
{
	m_boldCheckbox.setEnabled( b );
	m_italicCheckbox.setEnabled( b );
	m_strikeThruCheckbox.setEnabled( b );
//	m_outlineCheckbox.setEnabled( b );
	m_shadowCheckbox.setEnabled( b );
	m_allCapsCheckbox.setEnabled( b );
//	m_smallCapsCheckbox.setEnabled( b );
	m_superiorCheckbox.setEnabled( b );
	m_familyOptionMenu.setEnabled( b );
	m_sizeComboBox.setEnabled( b );
	m_underlineOptionMenu.setEnabled( b );
	m_verticalPositionOptionMenu.setEnabled( b );
	m_colorOptionMenu.setEnabled( b );
	m_shadeTextField.setEnabled( b );
	m_trackAmountTextField.setEnabled( b );
	m_baselineOffsetTextField.setEnabled( b );
	m_horizontalScaleTextField.setEnabled( b );
	m_verticalScaleTextField.setEnabled( b );
}
public void setContext( CoTextEditorContextIF context )
{
	super.setContext( context );

	if ( m_editor != null ) attributesChanged( m_editor.getSelectedCharacterAttributes() );
}
public void setEditor( CoAbstractTextEditor editor )
{
	if ( m_editor == editor ) return;
	
	if
		( m_editor != null )
	{
	  m_editor.removeAttributeListener( this );
	}
	
	m_editor = editor;
	
	if
		( m_editor != null )
	{
	  m_editor.addAttributeListener( this );
	}

	setAllEnabled( m_editor != null );
}

public CoTextField createLengthTextField( CoUserInterfaceBuilder b, String name, CoLengthUnitSet us )
{
	CoTextField tf = new CoNumericTextField( us );//CoLengthUnit.LENGTH_UNITS );
	b.prepareTextField( tf );
	tf.setHorizontalAlignment( CoTextField.RIGHT );
	tf.setBorder( BorderFactory.createEtchedBorder() );
	if ( name != null ) b.addNamedWidget( name, tf );
	return tf;
}
}