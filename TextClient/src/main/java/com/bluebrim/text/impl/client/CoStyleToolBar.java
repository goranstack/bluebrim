package com.bluebrim.text.impl.client;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;


public class CoStyleToolBar extends CoAbstractToolBar implements CoAttributeListenerIF
{
	private Border m_buttonBorder = BorderFactory.createEmptyBorder( 0, 0, 0, 0 );//2, 2, 2, 2 );
	
	private CoTriStateToggleButton m_boldButton = new CoTriStateToggleButton( m_boldAsIs, m_boldTrue, m_boldFalse );
	private CoTriStateToggleButton m_italicButton = new CoTriStateToggleButton( m_italicAsIs, m_italicTrue, m_italicFalse );
	private CoTriStateToggleButton m_strikeThruButton = new CoTriStateToggleButton( m_strikeThruAsIs, m_strikeThruTrue, m_strikeThruFalse );

//	private CoTriStateToggleButton m_outlineButton = new CoTriStateToggleButton( m_outlineAsIs, m_outlineTrue, m_outlineFalse );
	private CoTriStateToggleButton m_shadowButton = new CoTriStateToggleButton( m_shadowAsIs, m_shadowTrue, m_shadowFalse );
	private CoTriStateToggleButton m_allCapsButton = new CoTriStateToggleButton( m_allCapsAsIs, m_allCapsTrue, m_allCapsFalse );

//	private CoTriStateToggleButton m_smallCapsButton = new CoTriStateToggleButton( m_smallCapsAsIs, m_smallCapsTrue, m_smallCapsFalse );
	private CoTriStateToggleButton m_superiorButton = new CoTriStateToggleButton( m_superiorAsIs, m_superiorTrue, m_superiorFalse );
		
	private Co3DToggleButton m_normalUnderlineButton = new Co3DToggleButton( m_underlineNormal );
	private Co3DToggleButton m_wordUnderlineButton = new Co3DToggleButton( m_underlineWord );
	private Co3DToggleButton m_noUnderlineButton = new Co3DToggleButton( m_underlineNone );
	private Co3DToggleButton.ButtonGroup m_underlineButtonGroup = new Co3DToggleButton.ButtonGroup();

	private Co3DToggleButton m_leftButton = new Co3DToggleButton( m_alignmentLeft );
	private Co3DToggleButton m_centerButton = new Co3DToggleButton( m_alignmentCenter );
	private Co3DToggleButton m_rightButton = new Co3DToggleButton( m_alignmentRight );
	private Co3DToggleButton m_justifiedButton = new Co3DToggleButton( m_alignmentJustified );
	private Co3DToggleButton m_forcedButton = new Co3DToggleButton( m_alignmentForced );
	private Co3DToggleButton.ButtonGroup m_aligmentButtonGroup = new Co3DToggleButton.ButtonGroup();


	private static Icon m_boldAsIs = getIcon( CoStyleToolBar.class, "BOLD_IMAGE" );
	private static Icon m_boldTrue = getIcon( CoStyleToolBar.class, "BOLD_TRUE_IMAGE" );
	private static Icon m_boldFalse = getIcon( CoStyleToolBar.class, "BOLD_FALSE_IMAGE" );
	private static Icon m_italicAsIs = getIcon( CoStyleToolBar.class, "ITALIC_IMAGE" );
	private static Icon m_italicTrue = getIcon( CoStyleToolBar.class, "ITALIC_TRUE_IMAGE" );
	private static Icon m_italicFalse = getIcon( CoStyleToolBar.class, "ITALIC_FALSE_IMAGE" );
	private static Icon m_strikeThruAsIs = getIcon( CoStyleToolBar.class, "STRIKE_THRU_IMAGE" );
	private static Icon m_strikeThruTrue = getIcon( CoStyleToolBar.class, "STRIKE_THRU_TRUE_IMAGE" );
	private static Icon m_strikeThruFalse = getIcon( CoStyleToolBar.class, "STRIKE_THRU_FALSE_IMAGE" );



//	private static Icon m_outlineAsIs = getIcon( CoStyleToolBar.class, "OUTLINE_IMAGE" );
//	private static Icon m_outlineTrue = getIcon( CoStyleToolBar.class, "OUTLINE_TRUE_IMAGE" );
//	private static Icon m_outlineFalse = getIcon( CoStyleToolBar.class, "OUTLINE_FALSE_IMAGE" );
	private static Icon m_shadowAsIs = getIcon( CoStyleToolBar.class, "SHADOW_IMAGE" );
	private static Icon m_shadowTrue = getIcon( CoStyleToolBar.class, "SHADOW_TRUE_IMAGE" );
	private static Icon m_shadowFalse = getIcon( CoStyleToolBar.class, "SHADOW_FALSE_IMAGE" );
	private static Icon m_allCapsAsIs = getIcon( CoStyleToolBar.class, "ALL_CAPS_IMAGE" );
	private static Icon m_allCapsTrue = getIcon( CoStyleToolBar.class, "ALL_CAPS_TRUE_IMAGE" );
	private static Icon m_allCapsFalse = getIcon( CoStyleToolBar.class, "ALL_CAPS_FALSE_IMAGE" );



//	private static Icon m_smallCapsAsIs = getIcon( CoStyleToolBar.class, "SMALL_CAPS_IMAGE" );
//	private static Icon m_smallCapsTrue = getIcon( CoStyleToolBar.class, "SMALL_CAPS_TRUE_IMAGE" );
//	private static Icon m_smallCapsFalse = getIcon( CoStyleToolBar.class, "SMALL_CAPS_FALSE_IMAGE" );
	private static Icon m_superiorAsIs = getIcon( CoStyleToolBar.class, "SUPERIOR_IMAGE" );
	private static Icon m_superiorTrue = getIcon( CoStyleToolBar.class, "SUPERIOR_TRUE_IMAGE" );
	private static Icon m_superiorFalse = getIcon( CoStyleToolBar.class, "SUPERIOR_FALSE_IMAGE" );
	private static Icon m_underlineNormal = getIcon( CoStyleToolBar.class, "NORMAL_UNDERLINE_IMAGE" );
	private static Icon m_underlineWord = getIcon( CoStyleToolBar.class, "WORD_UNDERLINE_IMAGE" );
	private static Icon m_underlineNone = getIcon( CoStyleToolBar.class, "NO_UNDERLINE_IMAGE" );
	private static Icon m_alignmentLeft = getIcon( CoStyleToolBar.class, "LEFT_IMAGE" );
	private static Icon m_alignmentCenter = getIcon( CoStyleToolBar.class, "CENTER_IMAGE" );
	private static Icon m_alignmentRight = getIcon( CoStyleToolBar.class, "RIGHT_IMAGE" );
	private static Icon m_alignmentJustified = getIcon( CoStyleToolBar.class, "JUSTIFIED_IMAGE" );
	private static Icon m_alignmentForced = getIcon( CoStyleToolBar.class, "FORCED_IMAGE" );
	private CoTextField m_trackingTextField = new CoNumericTextField( null, 3 );
	{
		m_trackingTextField.setActivateWhenLosingFocus( true );
		m_trackingTextField.setSelectWhenGainingFocus( true );
		m_trackingTextField.setMargin( new Insets( 0, 0, 0, 1 ) );
		m_trackingTextField.setBorder( BorderFactory.createLineBorder( Color.black ) );
		m_trackingTextField.setFont( m_trackingTextField.getFont().deriveFont( m_trackingTextField.getFont().getSize2D() - 2 ) );
	}

public CoStyleToolBar(Action[] actions)
{
	this( actions, null );
}
public CoStyleToolBar( Action[] actions, CoAbstractTextEditor editor )
{
	super( actions );
	
	createToolbar();
	setEditor( editor );
}
public void attributesChanged(CoAttributeEvent e)
{
	AttributeSet as;

	if
		( e.didEditableChange() )
	{
		setAllEnabled( m_editor.isEditable() );
		return;
	}
	
	if
		(e.didParagraphChange())
	{
		as = e.getParagraphAttributes();
		updateParagraphStyleButtons(as);
	}
	
	as = e.getCharacterAttributes();
	updateCharacterStyleButtons(as);

	repaint();
}
public void createCharacterStyleButtons()
{
	m_boldButton.setBorder( m_buttonBorder );
	m_italicButton.setBorder(m_buttonBorder );
	m_strikeThruButton.setBorder( m_buttonBorder );
//	m_outlineButton.setBorder( m_buttonBorder );
	m_shadowButton.setBorder( m_buttonBorder );
	m_allCapsButton.setBorder( m_buttonBorder );
//	m_smallCapsButton.setBorder( m_buttonBorder );
	m_superiorButton.setBorder( m_buttonBorder );
	m_normalUnderlineButton.setBorder( m_buttonBorder );
	m_wordUnderlineButton.setBorder( m_buttonBorder );
	m_noUnderlineButton.setBorder( m_buttonBorder );

	
	add( m_boldButton );
	add( m_italicButton );
	add( m_strikeThruButton );
//	add( m_outlineButton );
	add( m_shadowButton );
	add( m_allCapsButton );
//	add( m_smallCapsButton );
	add( m_superiorButton );
	add( Box.createHorizontalStrut( 5 ) );
	add( Box.createVerticalStrut( 5 ) );
	add( m_normalUnderlineButton );
	add( m_wordUnderlineButton );
	add( m_noUnderlineButton );
	
	m_normalUnderlineButton.setModel( new CoToggleButton.ToggleButtonModel() );
	m_wordUnderlineButton.setModel( new CoToggleButton.ToggleButtonModel() );
	m_noUnderlineButton.setModel( new CoToggleButton.ToggleButtonModel() );
	
	m_underlineButtonGroup.add( m_normalUnderlineButton );
	m_underlineButtonGroup.add( m_wordUnderlineButton );
	m_underlineButtonGroup.add( m_noUnderlineButton );

/*
	m_boldButton.setAlignmentY( Component.TOP_ALIGNMENT );
	m_italicButton.setAlignmentY( Component.TOP_ALIGNMENT );
	m_strikeThruButton.setAlignmentY( Component.TOP_ALIGNMENT );
	m_outlineButton.setAlignmentY( Component.TOP_ALIGNMENT );
	m_shadowButton.setAlignmentY( Component.TOP_ALIGNMENT );
	m_allCapsButton.setAlignmentY( Component.TOP_ALIGNMENT );
	m_smallCapsButton.setAlignmentY( Component.TOP_ALIGNMENT );
	m_superiorButton.setAlignmentY( Component.TOP_ALIGNMENT );
*/
	m_boldButton.addActionListener( getAction( CoStyledEditorKit.boldCharacterAction ) );
	m_italicButton.addActionListener( getAction( CoStyledEditorKit.italicCharacterAction ) );
	m_strikeThruButton.addActionListener( getAction( CoStyledEditorKit.strikeThruCharacterAction ) );
//	m_outlineButton.addActionListener( getAction( CoStyledEditorKit.outlineCharacterAction ) );
	m_shadowButton.addActionListener( getAction( CoStyledEditorKit.shadowCharacterAction ) );
	m_allCapsButton.addActionListener( getAction( CoStyledEditorKit.allCapsCharacterAction ) );
//	m_smallCapsButton.addActionListener( getAction( CoStyledEditorKit.smallCapsCharacterAction ) );
	m_superiorButton.addActionListener( getAction( CoStyledEditorKit.superiorCharacterAction ) );
	
	m_normalUnderlineButton.setActionCommand( CoTextConstants.UNDERLINE_NORMAL.toString() );
	m_wordUnderlineButton.setActionCommand( CoTextConstants.UNDERLINE_WORD.toString() );
	m_noUnderlineButton.setActionCommand( CoTextConstants.UNDERLINE_NONE.toString() );

	m_normalUnderlineButton.addActionListener( getAction( CoStyledEditorKit.underlineCharacterAction ) );
	m_wordUnderlineButton.addActionListener( getAction( CoStyledEditorKit.underlineCharacterAction ) );
	m_noUnderlineButton.addActionListener( getAction( CoStyledEditorKit.underlineCharacterAction ) );

	m_boldButton.setToolTipText( getResourceString( "BOLD_TOOL_TIP" ) );
	m_italicButton.setToolTipText( getResourceString( "ITALIC_TOOL_TIP" ) );
	m_strikeThruButton.setToolTipText( getResourceString( "STRIKE_THRU_TOOL_TIP" ) );
//	m_outlineButton.setToolTipText( getResourceString( "OUTLINE_TOOL_TIP" ) );
	m_shadowButton.setToolTipText( getResourceString( "SHADOW_TOOL_TIP" ) );
	m_allCapsButton.setToolTipText( getResourceString( "ALL_CAPS_TOOL_TIP" ) );
//	m_smallCapsButton.setToolTipText( getResourceString( "SMALL_CAPS_TOOL_TIP" ) );
	m_superiorButton.setToolTipText( getResourceString( "SUPERIOR_TOOL_TIP" ) );

	m_normalUnderlineButton.setToolTipText( getResourceString( "UNDERLINE_NORMAL_TOOL_TIP" ) );
	m_wordUnderlineButton.setToolTipText( getResourceString( "UNDERLINE_WORD_TOOL_TIP" ) );
	m_noUnderlineButton.setToolTipText( getResourceString( "UNDERLINE_NONE_TOOL_TIP" ) );

	m_boldButton.setRequestFocusEnabled(false);
	m_italicButton.setRequestFocusEnabled(false);
	m_strikeThruButton.setRequestFocusEnabled(false);
//	m_outlineButton.setRequestFocusEnabled(false);
	m_shadowButton.setRequestFocusEnabled(false);
	m_allCapsButton.setRequestFocusEnabled(false);
//	m_smallCapsButton.setRequestFocusEnabled(false);
	m_superiorButton.setRequestFocusEnabled(false);
	m_normalUnderlineButton.setRequestFocusEnabled(false);
	m_wordUnderlineButton.setRequestFocusEnabled(false);
	m_noUnderlineButton.setRequestFocusEnabled(false);
}
public void createParagraphStyleButtons()
{
	add( m_leftButton );
	add( m_centerButton );
	add( m_rightButton );
	add( m_justifiedButton );
	add( m_forcedButton );

	m_leftButton.setModel( new CoToggleButton.ToggleButtonModel() );
	m_centerButton.setModel( new CoToggleButton.ToggleButtonModel() );
	m_rightButton.setModel( new CoToggleButton.ToggleButtonModel() );
	m_justifiedButton.setModel( new CoToggleButton.ToggleButtonModel() );
	m_forcedButton.setModel( new CoToggleButton.ToggleButtonModel() );
	
	m_aligmentButtonGroup.add( m_leftButton );
	m_aligmentButtonGroup.add( m_centerButton );
	m_aligmentButtonGroup.add( m_rightButton );
	m_aligmentButtonGroup.add( m_justifiedButton );
	m_aligmentButtonGroup.add( m_forcedButton );
	
	m_leftButton.setBorder( m_buttonBorder );
	m_centerButton.setBorder( m_buttonBorder );
	m_rightButton.setBorder( m_buttonBorder );
	m_justifiedButton.setBorder( m_buttonBorder );
	m_forcedButton.setBorder( m_buttonBorder );

	m_leftButton.setActionCommand( CoTextConstants.ALIGN_LEFT.toString() );
	m_centerButton.setActionCommand( CoTextConstants.ALIGN_CENTER.toString() );
	m_rightButton.setActionCommand( CoTextConstants.ALIGN_RIGHT.toString() );
	m_justifiedButton.setActionCommand( CoTextConstants.ALIGN_JUSTIFIED.toString() );
	m_forcedButton.setActionCommand( CoTextConstants.ALIGN_FORCED.toString() );

	m_leftButton.addActionListener( getAction( CoStyledEditorKit.alignmentParagraphAction ) );
	m_centerButton.addActionListener( getAction( CoStyledEditorKit.alignmentParagraphAction ) );
	m_rightButton.addActionListener( getAction( CoStyledEditorKit.alignmentParagraphAction ) );
	m_justifiedButton.addActionListener( getAction( CoStyledEditorKit.alignmentParagraphAction ) );
	m_forcedButton.addActionListener( getAction( CoStyledEditorKit.alignmentParagraphAction ) );

	m_leftButton.setRequestFocusEnabled(false);
	m_centerButton.setRequestFocusEnabled(false);
	m_rightButton.setRequestFocusEnabled(false);
	m_justifiedButton.setRequestFocusEnabled(false);
	m_forcedButton.setRequestFocusEnabled(false);

	m_leftButton.setToolTipText( getResourceString( "ALIGN_LEFT_TOOL_TIP" ) );
	m_centerButton.setToolTipText( getResourceString( "ALIGN_CENTER_TOOL_TIP" ) );
	m_rightButton.setToolTipText( getResourceString( "ALIGN_RIGHT_TOOL_TIP" ) );
	m_justifiedButton.setToolTipText( getResourceString( "ALIGN_JUSTIFIED_TOOL_TIP" ) );
	m_forcedButton.setToolTipText( getResourceString( "ALIGN_FORCED_TOOL_TIP" ) );
}
/**
 * This method was created in VisualAge.
 */
public void createToolbar ()
{
	add( new CoTextfieldCounterPanel( m_trackingTextField ) );
	m_trackingTextField.addActionListener( getAction( CoStyledEditorKit.trackAmountCharacterAction ) );

	add( Box.createHorizontalStrut( 5 ) );
	add( Box.createVerticalStrut( 5 ) );
	
	createCharacterStyleButtons();

	add( Box.createHorizontalStrut( 5 ) );
	add( Box.createVerticalStrut( 5 ) );

	// create paragarph style buttons
	createParagraphStyleButtons();

	
	add( Box.createHorizontalGlue() );
}
protected void setAllEnabled( boolean b )
{
	if
		( m_boldButton != null )
	{
		m_trackingTextField.setEnabled( b );
		m_boldButton.setEnabled( b );
		m_italicButton.setEnabled( b );
		m_strikeThruButton.setEnabled( b );
//		m_outlineButton.setEnabled( b );
		m_shadowButton.setEnabled( b );
		m_allCapsButton.setEnabled( b );
//		m_smallCapsButton.setEnabled( b );
		m_superiorButton.setEnabled( b );
		m_normalUnderlineButton.setEnabled( b );
		m_wordUnderlineButton.setEnabled( b );
		m_noUnderlineButton.setEnabled( b );
		m_leftButton.setEnabled( b );
		m_centerButton.setEnabled( b );
		m_rightButton.setEnabled( b );
		m_justifiedButton.setEnabled( b );
		m_forcedButton.setEnabled( b );
	}
}
public void setEditor(CoAbstractTextEditor editor)
{
	super.setEditor( editor );

	setAllEnabled( m_editor != null );
}
public void updateCharacterStyleButtons (AttributeSet as)
{
	com.bluebrim.font.shared.CoFontAttribute fa;
	Boolean b;
	Float f;

	
	// tracking  
  f = CoStyleConstants.getTrackAmount( as );
  if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
	  m_trackingTextField.setAsIs();
  } else {
	  String tmp = ( f == null ) ? "" : f.toString();
 	 if ( m_trackingTextField.isAsIs() || ! tmp.equals( m_trackingTextField.getText() ) ) m_trackingTextField.setText( tmp );
  }
	
	// bold
	fa = CoStyleConstants.getWeight( as );
	if
		( fa == CoStyleConstants.AS_IS_FONT_ATTRIBUTE_VALUE )
	{
		m_boldButton.setAsIs();
	} else {
		m_boldButton.setTriState( CoStyleConstants.enum2Boolean( fa, com.bluebrim.font.shared.CoFontAttribute.BOLD ) );
	}

	// italic
	fa = CoStyleConstants.getStyle( as );
	if
		( fa == CoStyleConstants.AS_IS_FONT_ATTRIBUTE_VALUE )
	{
		m_italicButton.setAsIs();
	} else {
		m_italicButton.setTriState( CoStyleConstants.enum2Boolean( fa, com.bluebrim.font.shared.CoFontAttribute.ITALIC ) );
	}

	// strike thru
	b = CoStyleConstants.getStrikeThru( as );
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		m_strikeThruButton.setAsIs();
	} else {
	  m_strikeThruButton.setTriState( b );
	}
/*
	// outline
	b = CoStyleConstants.getOutline( as );
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		m_outlineButton.setAsIs();
	} else {
	  m_outlineButton.setTriState( b );
	}
*/
	// shadow
	b = CoStyleConstants.getShadow( as );
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		m_shadowButton.setAsIs();
	} else {
	  m_shadowButton.setTriState( b );
	}

	// all caps
	b = CoStyleConstants.getAllCaps( as );
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		m_allCapsButton.setAsIs();
	} else {
	  m_allCapsButton.setTriState( b );
	}
/*
	// small caps
	fa = CoStyleConstants.getVariant( as );
	if
		( fa == CoStyleConstants.AS_IS_FONT_ATTRIBUTE_VALUE )
	{
		m_smallCapsButton.setAsIs();
	} else {
		m_smallCapsButton.setTriState( CoStyleConstants.enum2Boolean( fa, CoStyleConstants.SMALL_CAPS ) );
	}
*/
	// superior
	b = CoStyleConstants.getSuperior( as );
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		m_superiorButton.setAsIs();
	} else {
	  m_superiorButton.setTriState( b );
	}

	// underline
	CoEnumValue e = CoStyleConstants.getUnderline( as );
	if
		( e == null )
	{
		m_underlineButtonGroup.deselect();
	} else if
		( e == CoStyleConstants.AS_IS_ENUM_VALUE )
	{
		m_underlineButtonGroup.deselect();
		m_underlineButtonGroup.setAsIs();
	} else {
		if      ( e.equals( CoTextConstants.UNDERLINE_NORMAL ) ) m_normalUnderlineButton.setSelected( true );
		else if ( e.equals( CoTextConstants.UNDERLINE_WORD ) )   m_wordUnderlineButton.setSelected( true );
		else if ( e.equals( CoTextConstants.UNDERLINE_NONE ) )   m_noUnderlineButton.setSelected( true );
	}
}
public void updateParagraphStyleButtons(AttributeSet as)
{
	CoEnumValue e = CoStyleConstants.getAlignment( as );

	if
		( e == null )
	{
		m_aligmentButtonGroup.deselect();
	} else if
		( e == CoStyleConstants.AS_IS_ENUM_VALUE )
	{
		m_aligmentButtonGroup.deselect();
		m_aligmentButtonGroup.setAsIs();
	} else {
		if      ( e.equals( CoTextConstants.ALIGN_LEFT ) )      m_leftButton.setSelected( true );
		else if ( e.equals( CoTextConstants.ALIGN_CENTER ) )    m_centerButton.setSelected( true );
		else if ( e.equals( CoTextConstants.ALIGN_RIGHT ) )     m_rightButton.setSelected( true );
		else if ( e.equals( CoTextConstants.ALIGN_JUSTIFIED ) ) m_justifiedButton.setSelected( true );
		else if ( e.equals( CoTextConstants.ALIGN_FORCED ) )    m_forcedButton.setSelected( true );
	}
}
}