package com.bluebrim.text.impl.client;

// Calvin imports
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
 * Dialog för att sätta styckesutformning.
 * Dialogen saknar 'valuemodel', istället är Action-objekt kopplade till kontrollerna.
 * Det normala är att dessa actions direktmanipulerar den selekterade texten i en CoAbstractTextPane.
 */
 
public class CoParagraphStyleActionUI extends CoAbstractParagraphStyleUI implements CoAttributeListenerIF
{
	private CoAbstractTextEditor m_editor;


	private CoTriStateCheckBox m_adjustToBaseLineGridCheckbox;

	private CoTriStateCheckBox m_lastInColumnCheckbox;
	private CoTriStateCheckBox m_topOfColumnCheckbox;
	private CoTriStateCheckBox m_linesTogetherCheckbox;
	private CoTriStateCheckBox m_dropCapsCheckbox;
	private CoTextField m_leftIndentTextField;
	private CoTextField m_rightIndentTextField;
	private CoTextField m_firstLineIndentTextField;
	private CoTextField m_trailingLinesIndentTextField;
	private CoTextField m_spaceAboveTextField;
	private CoTextField m_spaceBelowTextField;
	private CoTextField m_leadingTextField;
	private CoTextField m_dropCapsCharacterCountTextField;
	private CoTextField m_dropCapsLineCountTextField;
	private CoOptionMenu m_alignmentOptionMenu;
	private CoOptionMenu m_hyphenationOptionMenu;
	private CoOptionMenu m_hyphenationFallbackBehaviorOptionMenu;
	private CoPanel m_dropCapsPanel;
	private CoParagraphRulerPanel m_topRulerPanel;
	private CoParagraphRulerPanel m_bottomRulerPanel;

	private CoAbstractCharacterStyleUI m_characterStyleUI;
	
	private CoTriStateCheckBox m_boldCheckbox;
	private CoTriStateCheckBox m_italicCheckbox;
	private CoTriStateCheckBox m_strikeThruCheckbox;

	private CoTriStateCheckBox m_shadowCheckbox;
	private CoTriStateCheckBox m_allCapsCheckbox;

	private CoTriStateCheckBox m_superiorCheckbox;
	private CoOptionMenu m_familyOptionMenu;
	private CoComboBox m_sizeComboBox;
	private CoOptionMenu m_underlineOptionMenu;
	private CoOptionMenu m_verticalPositionOptionMenu;
	private CoOptionMenu m_colorOptionMenu;
	private CoTextField m_shadeTextField;
	private CoTextField m_trackAmountTextField;
	private CoTextField m_baselineOffsetTextField;
	private CoTextField m_horizontalScaleTextField;
	private CoTextField m_verticalScaleTextField;

	private CoTextField m_regularTabStopIntervalTextField;

	private boolean m_isApplying;
	private Map m_actions;

	private CoTextField m_minimumSpaceWidthTextField;
	private CoTextField m_optimumSpaceWidthTextField;

	private CoTextField m_shadowAngleTextField;
	private CoOptionMenu m_shadowColorOptionMenu;
	private CoTextField m_shadowOffsetTextField;
	private CoTextField m_shadowShadeTextField;

public CoParagraphStyleActionUI()
{
	super();
}

/**
 * com.bluebrim.publication.impl.client.CoEditionUI constructor comment.
 */
public CoParagraphStyleActionUI( Action[] actions )
{
	this( actions, null );
}
public CoParagraphStyleActionUI( Action[] actions, CoAbstractTextEditor editor )
{
	super();

	m_actions = CoActionUtilities.actionsToHashtable(actions);

	buildForComponent();
	
	setEditor( editor );
}
public void attributesChanged( AttributeSet as )
{
	updateCharacterAttributes( as );
	updateParagraphAttriburtes( as );
	updateTopRulerPanel( as );
	updateBottomRulerPanel( as );
 
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
	if ( m_isApplying || ! e.didParagraphChange() ) return;

	attributesChanged( e.getParagraphAttributes() );
}

protected  CoTabSetPanel.TabSetEditor createTabSetEditor()
{
	return
		new CoTabSetPanel.TextEditorTabSetEditor()
		{
			protected CoAbstractTextEditor getTextEditor()
			{
				return m_editor;
			}
		};
}
protected void doAfterCreateUserInterface( ) 
{
	m_adjustToBaseLineGridCheckbox = (CoTriStateCheckBox) getNamedWidget( CoTextConstants.ADJUST_TO_BASE_LINE_GRID );
	m_lastInColumnCheckbox = (CoTriStateCheckBox) getNamedWidget( CoTextConstants.LAST_IN_COLUMN );
	m_topOfColumnCheckbox = (CoTriStateCheckBox) getNamedWidget( CoTextConstants.TOP_OF_COLUMN );
	m_linesTogetherCheckbox = (CoTriStateCheckBox) getNamedWidget( CoTextConstants.LINES_TOGETHER );
	m_dropCapsCheckbox = (CoTriStateCheckBox) getNamedWidget( CoTextConstants.DROP_CAPS );
	m_leftIndentTextField = (CoTextField) getNamedWidget( CoTextConstants.LEFT_INDENT );
	m_rightIndentTextField = (CoTextField) getNamedWidget( CoTextConstants.RIGHT_INDENT );
	m_firstLineIndentTextField = (CoTextField) getNamedWidget( CoTextConstants.FIRST_LINE_INDENT );
	m_trailingLinesIndentTextField = (CoTextField) getNamedWidget( CoTextConstants.TRAILING_LINES_INDENT );
	m_spaceAboveTextField = (CoTextField) getNamedWidget( CoTextConstants.SPACE_ABOVE );
	m_spaceBelowTextField = (CoTextField) getNamedWidget( CoTextConstants.SPACE_BELOW );
	m_leadingTextField = (CoTextField) getNamedWidget( CoTextConstants.LEADING );
	m_optimumSpaceWidthTextField = (CoTextField) getNamedWidget( CoTextConstants.OPTIMUM_SPACE_WIDTH );
	m_minimumSpaceWidthTextField = (CoTextField) getNamedWidget( CoTextConstants.MINIMUM_SPACE_WIDTH );
	m_dropCapsCharacterCountTextField = (CoTextField) getNamedWidget( CoTextConstants.DROP_CAPS_COUNT );
	m_dropCapsLineCountTextField = (CoTextField) getNamedWidget( CoTextConstants.DROP_CAPS_HEIGHT );
	m_alignmentOptionMenu = (CoOptionMenu) getNamedWidget( CoTextConstants.ALIGNMENT );
	m_hyphenationOptionMenu = (CoOptionMenu) getNamedWidget( CoTextConstants.HYPHENATION );
	m_hyphenationFallbackBehaviorOptionMenu = (CoOptionMenu) getNamedWidget( CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR );
	m_dropCapsPanel = (CoPanel) getNamedWidget( DROP_CAPS_PANEL );
	m_topRulerPanel = (CoParagraphRulerPanel) getNamedWidget( TOP_RULER );
	m_bottomRulerPanel = (CoParagraphRulerPanel) getNamedWidget( BOTTOM_RULER );

	CoUserInterface ui = ( (CoSubcanvas) getNamedWidget( CoTextConstants.CHARACTER_STYLE ) ).getUserInterface();
	
	m_boldCheckbox = (CoTriStateCheckBox) ui.getNamedWidget( CoTextConstants.WEIGHT );
	m_italicCheckbox = (CoTriStateCheckBox) ui.getNamedWidget( CoTextConstants.STYLE );
	m_strikeThruCheckbox = (CoTriStateCheckBox) ui.getNamedWidget( CoTextConstants.STRIKE_THRU );
	m_shadowCheckbox = (CoTriStateCheckBox) ui.getNamedWidget( CoTextConstants.SHADOW );
	m_allCapsCheckbox = (CoTriStateCheckBox) ui.getNamedWidget( CoTextConstants.ALL_CAPS );
	m_superiorCheckbox = (CoTriStateCheckBox) ui.getNamedWidget( CoTextConstants.SUPERIOR );
	m_underlineOptionMenu = (CoOptionMenu) ui.getNamedWidget( CoTextConstants.UNDERLINE );
	m_verticalPositionOptionMenu = (CoOptionMenu) ui.getNamedWidget( CoTextConstants.VERTICAL_POSITION );

	m_familyOptionMenu = (CoOptionMenu) ui.getNamedWidget( CoTextConstants.FONT_FAMILY );
	m_sizeComboBox = (CoComboBox) ui.getNamedWidget( CoTextConstants.FONT_SIZE );
	m_colorOptionMenu = (CoOptionMenu) ui.getNamedWidget( CoTextConstants.FOREGROUND_COLOR );
	m_shadeTextField = (CoTextField) ui.getNamedWidget( CoTextConstants.FOREGROUND_SHADE );
	m_trackAmountTextField = (CoTextField) ui.getNamedWidget( CoTextConstants.TRACK_AMOUNT );
	m_baselineOffsetTextField = (CoTextField) ui.getNamedWidget( CoTextConstants.BASELINE_OFFSET );
	m_horizontalScaleTextField = (CoTextField) ui.getNamedWidget( CoTextConstants.HORIZONTAL_SCALE );
	m_verticalScaleTextField = (CoTextField) ui.getNamedWidget( CoTextConstants.VERTICAL_SCALE );
	m_shadowOffsetTextField = (CoTextField) ui.getNamedWidget( CoTextConstants.SHADOW_OFFSET );
	m_shadowAngleTextField = (CoTextField) ui.getNamedWidget( CoTextConstants.SHADOW_ANGLE );
	m_shadowColorOptionMenu = (CoOptionMenu) ui.getNamedWidget( CoTextConstants.SHADOW_COLOR );
	m_shadowShadeTextField = (CoTextField) ui.getNamedWidget( CoTextConstants.SHADOW_SHADE );

	m_regularTabStopIntervalTextField = (CoTextField) getNamedWidget( CoTextConstants.REGULAR_TAB_STOP_INTERVAL );

//	m_plainButton = (CoButton) ui.getNamedWidget( CoTextConstants.PLAIN );
//	m_plainButton.addActionListener( getAction( CoStyledEditorKit.plainParagraphAction ) );
	
	super.doAfterCreateUserInterface();
	

	
	ActionListener l = new ActionListener() { public void actionPerformed( ActionEvent e ) {  } };

	
	class _ implements ActionListener
	{
		private ActionListener m_delegate;
		public _( ActionListener listener ) { m_delegate = listener; }
		public void actionPerformed( ActionEvent e )
		{
			m_isApplying = true;
			m_delegate.actionPerformed( e );
			m_isApplying = false;
		}
	};
			
	m_boldCheckbox.addActionListener( new _( getAction( CoStyledEditorKit.boldParagraphAction ) ) );
	m_italicCheckbox.addActionListener( new _( getAction( CoStyledEditorKit.italicParagraphAction ) ) );
	m_strikeThruCheckbox.addActionListener( new _( getAction( CoStyledEditorKit.strikeThruParagraphAction ) ) );
//	m_outlineCheckbox.addActionListener( new _( getAction( CoStyledEditorKit.outlineParagraphAction ) ) );
	m_shadowCheckbox.addActionListener( new _( getAction( CoStyledEditorKit.shadowParagraphAction ) ) );
	m_allCapsCheckbox.addActionListener( new _( getAction( CoStyledEditorKit.allCapsParagraphAction ) ) );
//	m_smallCapsCheckbox.addActionListener( new _( getAction( CoStyledEditorKit.smallCapsParagraphAction ) ) );
	m_superiorCheckbox.addActionListener( new _( getAction( CoStyledEditorKit.superiorParagraphAction ) )) ;
	m_underlineOptionMenu.addActionListener( new _( getAction( CoStyledEditorKit.underlineParagraphAction ) ) );
	m_verticalPositionOptionMenu.addActionListener( new _( getAction( CoStyledEditorKit.verticalPositionParagraphAction ) ) );

	m_familyOptionMenu.addActionListener( new _( getAction( CoStyledEditorKit.fontFamilyParagraphAction ) ) );
	m_sizeComboBox.addActionListener( new _( getAction( CoStyledEditorKit.fontSizeParagraphAction ) ) );
	m_colorOptionMenu.addActionListener( new _( getAction( CoStyledEditorKit.foregroundColorParagraphAction ) ) );
	m_shadeTextField.addActionListener( new _( getAction( CoStyledEditorKit.foregroundShadeParagraphAction ) ) );
	m_trackAmountTextField.addActionListener( new _( getAction( CoStyledEditorKit.trackAmountParagraphAction ) ) );
	m_baselineOffsetTextField.addActionListener( new _( getAction( CoStyledEditorKit.baselineOffsetParagraphAction ) ) );
	m_horizontalScaleTextField.addActionListener( new _( getAction( CoStyledEditorKit.horizontalScaleParagraphAction ) ) );
	m_verticalScaleTextField.addActionListener( new _( getAction( CoStyledEditorKit.verticalScaleParagraphAction ) ) );
	m_shadowOffsetTextField.addActionListener( new _( getAction( CoStyledEditorKit.shadowOffsetParagraphAction ) ) );
	m_shadowAngleTextField.addActionListener( new _( getAction( CoStyledEditorKit.shadowAngleParagraphAction ) ) );
	m_shadowColorOptionMenu.addActionListener( new _( getAction( CoStyledEditorKit.shadowColorParagraphAction ) ) );
	m_shadowShadeTextField.addActionListener( new _( getAction( CoStyledEditorKit.shadowShadeParagraphAction ) ) );

	m_adjustToBaseLineGridCheckbox.addActionListener( new _( getAction( CoStyledEditorKit.adjustToBaseLineGridParagraphAction ) ) );
	m_lastInColumnCheckbox.addActionListener( new _( getAction( CoStyledEditorKit.lastInColumnParagraphAction ) ) );
	m_topOfColumnCheckbox.addActionListener( new _( getAction( CoStyledEditorKit.topOfColumnParagraphAction ) ) );
	m_linesTogetherCheckbox.addActionListener( new _( getAction( CoStyledEditorKit.linesTogetherParagraphAction ) ) );
	m_dropCapsCheckbox.addActionListener( new _( getAction( CoStyledEditorKit.dropCapsParagraphAction ) ) );
	m_leftIndentTextField.addActionListener( new _( getAction( CoStyledEditorKit.leftIndentParagraphAction ) ) );
	m_rightIndentTextField.addActionListener( new _( getAction( CoStyledEditorKit.rightIndentParagraphAction ) ) );
	m_firstLineIndentTextField.addActionListener( new _( getAction( CoStyledEditorKit.firstLineIndentParagraphAction ) ) );
	m_trailingLinesIndentTextField.addActionListener( new _( getAction( CoStyledEditorKit.trailingLinesIndentParagraphAction ) ) );
	m_spaceAboveTextField.addActionListener( new _( getAction( CoStyledEditorKit.spaceAboveParagraphAction ) ) );
	m_spaceBelowTextField.addActionListener( new _( getAction( CoStyledEditorKit.spaceBelowParagraphAction ) ) );
	m_leadingTextField.addActionListener( new _( getAction( CoStyledEditorKit.leadingParagraphAction ) ) );
	m_optimumSpaceWidthTextField.addActionListener( new _( getAction( CoStyledEditorKit.optimumSpaceWidthParagraphAction ) ) );
	m_minimumSpaceWidthTextField.addActionListener( new _( getAction( CoStyledEditorKit.minimumSpaceWidthParagraphAction ) ) );
	m_dropCapsCharacterCountTextField.addActionListener( new _( getAction( CoStyledEditorKit.dropCapsWidthParagraphAction ) ) );
	m_dropCapsLineCountTextField.addActionListener( new _( getAction( CoStyledEditorKit.dropCapsHeightParagraphAction ) ) );
	m_alignmentOptionMenu.addActionListener( new _( getAction( CoStyledEditorKit.alignmentParagraphAction ) ) );
	m_hyphenationOptionMenu.addActionListener( new _( getAction( CoStyledEditorKit.hyphenationParagraphAction ) ) );
	m_hyphenationFallbackBehaviorOptionMenu.addActionListener( new _( getAction( CoStyledEditorKit.hyphenationFallbackBehaviorParagraphAction ) ) );
	
	m_topRulerPanel.m_positionTextField.addActionListener( new _( getAction( CoStyledEditorKit.topRulerPositionParagraphAction ) ) );
	m_topRulerPanel.m_thicknessTextField.addActionListener( new _( getAction( CoStyledEditorKit.topRulerThicknessParagraphAction ) ) );
	m_topRulerPanel.m_spanOptionMenu.addActionListener( new _( getAction( CoStyledEditorKit.topRulerSpanParagraphAction ) ) );
	m_topRulerPanel.m_fixedSpanAlignmentOptionMenu.addActionListener( new _( getAction( CoStyledEditorKit.topRulerAlignmentParagraphAction ) ) );
	m_topRulerPanel.m_fixedSpanWidthTextField.addActionListener( new _( getAction( CoStyledEditorKit.topRulerFixedWidthParagraphAction ) ) );
	m_topRulerPanel.m_leftInsetTextField.addActionListener( new _( getAction( CoStyledEditorKit.topRulerLeftIndentParagraphAction ) ) );
	m_topRulerPanel.m_rightInsetTextField.addActionListener( new _( getAction( CoStyledEditorKit.topRulerRightIndentParagraphAction ) ) );
	
	m_bottomRulerPanel.m_positionTextField.addActionListener( new _( getAction( CoStyledEditorKit.bottomRulerPositionParagraphAction ) ) );
	m_bottomRulerPanel.m_thicknessTextField.addActionListener( new _( getAction( CoStyledEditorKit.bottomRulerThicknessParagraphAction ) ) );
	m_bottomRulerPanel.m_spanOptionMenu.addActionListener( new _( getAction( CoStyledEditorKit.bottomRulerSpanParagraphAction ) ) );
	m_bottomRulerPanel.m_fixedSpanAlignmentOptionMenu.addActionListener( new _( getAction( CoStyledEditorKit.bottomRulerAlignmentParagraphAction ) ) );
	m_bottomRulerPanel.m_fixedSpanWidthTextField.addActionListener( new _( getAction( CoStyledEditorKit.bottomRulerFixedWidthParagraphAction ) ) );
	m_bottomRulerPanel.m_leftInsetTextField.addActionListener( new _( getAction( CoStyledEditorKit.bottomRulerLeftIndentParagraphAction ) ) );
	m_bottomRulerPanel.m_rightInsetTextField.addActionListener( new _( getAction( CoStyledEditorKit.bottomRulerRightIndentParagraphAction ) ) );

	m_regularTabStopIntervalTextField.addActionListener( new _( getAction( CoStyledEditorKit.regularTabStopIntervalParagraphAction ) ) );
}
private Action getAction( String cmd )
{
	return (Action) m_actions.get (cmd );
}
protected CoUserInterface getCharacterAttributesPanel()
{
	return m_characterStyleUI = new CoAbstractCharacterStyleUI( this );
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
			d = new CoDialog( (Frame) w, CoTextStringResources.getName( CoTextConstants.PARAGRAPH_STYLE ), false, this );
		} else if
			( w instanceof Dialog )
		{
			d = new CoDialog( (Dialog) w, CoTextStringResources.getName( CoTextConstants.PARAGRAPH_STYLE ), false, this );
		} else {
			return;
		}
	}
	
	
	d.show();
}
protected void setAllEnabled( boolean b )
{
	m_adjustToBaseLineGridCheckbox.setEnabled( b );
	m_lastInColumnCheckbox.setEnabled( b );
	m_topOfColumnCheckbox.setEnabled( b );
	m_linesTogetherCheckbox.setEnabled( b );
	m_dropCapsCheckbox.setEnabled( b );
	m_leftIndentTextField.setEnabled( b );
	m_rightIndentTextField.setEnabled( b );
	m_firstLineIndentTextField.setEnabled( b );
	m_trailingLinesIndentTextField.setEnabled( b );
	m_spaceAboveTextField.setEnabled( b );
	m_spaceBelowTextField.setEnabled( b );
	m_leadingTextField.setEnabled( b );
	m_optimumSpaceWidthTextField.setEnabled( b );
	m_minimumSpaceWidthTextField.setEnabled( b );
	m_dropCapsCharacterCountTextField.setEnabled( b );
	m_dropCapsLineCountTextField.setEnabled( b );
	m_alignmentOptionMenu.setEnabled( b );
	m_hyphenationOptionMenu.setEnabled( b );
	m_hyphenationFallbackBehaviorOptionMenu.setEnabled( b );
	m_dropCapsPanel.setEnabled( b );
	
	m_topRulerPanel.setEnabled( b );
	m_bottomRulerPanel.setEnabled( b );
	
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
	m_shadowOffsetTextField.setEnabled( b );
	m_shadowAngleTextField.setEnabled( b );
	m_shadowColorOptionMenu.setEnabled( b );
	m_shadowShadeTextField.setEnabled( b );

	m_regularTabStopIntervalTextField.setEnabled( b );
}
public void setContext( CoTextEditorContextIF context )
{
	super.setContext( context );

	if ( m_editor != null ) attributesChanged( m_editor.getSelectedParagraphAttributes() );

	if ( m_characterStyleUI != null ) m_characterStyleUI.setContext( context );
}
public void setEditor( CoAbstractTextEditor editor )
{
	if (m_editor == editor) return;
	
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
private void updateBottomRulerPanel( AttributeSet as )
{
	CoEnumValue e;
	Float f;

	// top ruler
	f = CoStyleConstants.getBottomRulerPosition( as );
  if
  	( f == null )
  {
	  m_bottomRulerPanel.m_positionTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_bottomRulerPanel.m_positionTextField.setAsIs();
  } else {
	  m_bottomRulerPanel.m_positionTextField.setText( f + " %" );
  }

	f = CoStyleConstants.getBottomRulerThickness( as );
  if
  	( f == null )
  {
	  m_bottomRulerPanel.m_thicknessTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_bottomRulerPanel.m_thicknessTextField.setAsIs();
  } else {
	  m_bottomRulerPanel.m_thicknessTextField.setText( "" + f );
  }

//	m_bottomRulerPanel.setAsIsSpan( CoViewStyleConstants.getBottomRulerSpan( as ).toString() );

	e = CoStyleConstants.getBottomRulerSpan( as );
	if
		( e == CoStyleConstants.AS_IS_ENUM_VALUE )
	{
		m_bottomRulerPanel.m_spanOptionMenu.setAsIs();
	} else {
  	m_bottomRulerPanel.m_spanOptionMenu.setSelectedItem( e, true );
	}

	e = CoStyleConstants.getBottomRulerAlignment( as );
	if
		( e == CoStyleConstants.AS_IS_ENUM_VALUE )
	{
		m_bottomRulerPanel.m_fixedSpanAlignmentOptionMenu.setAsIs();
	} else {
  	m_bottomRulerPanel.m_fixedSpanAlignmentOptionMenu.setSelectedItem( e, true );
	}

  f = CoStyleConstants.getBottomRulerFixedWidth( as );
  if
  	( f == null )
  {
	  m_bottomRulerPanel.m_fixedSpanWidthTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_bottomRulerPanel.m_fixedSpanWidthTextField.setAsIs();
  } else {
	  m_bottomRulerPanel.m_fixedSpanWidthTextField.setText( "" + f );
  }

	f = CoStyleConstants.getBottomRulerLeftInset( as );
  if
  	( f == null )
  {
	  m_bottomRulerPanel.m_leftInsetTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_bottomRulerPanel.m_leftInsetTextField.setAsIs();
  } else {
	  m_bottomRulerPanel.m_leftInsetTextField.setText( "" + f );
	}
 
  f = CoStyleConstants.getBottomRulerRightInset( as );
  if
  	( f == null )
  {
	  m_bottomRulerPanel.m_rightInsetTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_bottomRulerPanel.m_rightInsetTextField.setAsIs();
  } else {
	  m_bottomRulerPanel.m_rightInsetTextField.setText( "" + f );
  }
 
}
private void updateCharacterAttributes( AttributeSet as )
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
	  m_shadeTextField.setText( CoLengthUnitSet.format( f.floatValue() ) + " %" );
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
	  m_baselineOffsetTextField.setText( "" + f );
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

  
}           
private void updateParagraphAttriburtes( AttributeSet as )
{
 	com.bluebrim.font.shared.CoFontAttribute fa;
	Boolean b;
	CoEnumValue e;
	Float f;
	Integer i;
	String s;
	
	f = CoStyleConstants.getRegularTabStopInterval( as );
  if
  	( f == null )
  {
	  m_regularTabStopIntervalTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
	  m_regularTabStopIntervalTextField.setAsIs();
  } else {
	  m_regularTabStopIntervalTextField.setText( "" + f );
  }

	CoLeading l = CoStyleConstants.getLeading( as );
  if
  	( l == null )
  {
	  m_leadingTextField.setText( "" );
  } else if
  	( l == CoStyleConstants.AS_IS_LEADING_VALUE )
  {
		m_leadingTextField.setAsIs();
  } else {
	  m_leadingTextField.setText( CoLeading.format( l ) );
  }

	f = CoStyleConstants.getMinimumSpaceWidth( as );
  if
  	( f == null )
  {
	  m_minimumSpaceWidthTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_minimumSpaceWidthTextField.setAsIs();
  } else {
	  m_minimumSpaceWidthTextField.setText( f.toString() );
  }

  
	f = CoStyleConstants.getOptimumSpaceWidth( as );
  if
  	( f == null )
  {
	  m_optimumSpaceWidthTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_optimumSpaceWidthTextField.setAsIs();
  } else {
	  m_optimumSpaceWidthTextField.setText( f.toString() );
  }


  
	i = CoStyleConstants.getDropCapsLineCount( as );
  if
  	( i == null )
  {
	  m_dropCapsLineCountTextField.setText( "" );
  } else if
  	( i == CoStyleConstants.AS_IS_INTEGER_VALUE )
  {
	  m_dropCapsLineCountTextField.setAsIs();
  } else {
	  m_dropCapsLineCountTextField.setText( i.toString() );
  }

   
	i = CoStyleConstants.getDropCapsCharacterCount( as );
  if
  	( i == null )
  {
	  m_dropCapsCharacterCountTextField.setText( "" );
  } else if
  	( i == CoStyleConstants.AS_IS_INTEGER_VALUE )
  {
	  m_dropCapsCharacterCountTextField.setAsIs();
  } else {
	  m_dropCapsCharacterCountTextField.setText( i.toString() );
  }
	
  f = CoStyleConstants.getSpaceBelow( as );
  if
  	( f == null )
  {
	  m_spaceBelowTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_spaceBelowTextField.setAsIs();
  } else {
	  m_spaceBelowTextField.setText( "" + f );
  }
  
  f = CoStyleConstants.getSpaceAbove( as );
  if
  	( f == null )
  {
	  m_spaceAboveTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_spaceAboveTextField.setAsIs();
  } else {
	  m_spaceAboveTextField.setText( "" + f );
  }
  
  f = CoStyleConstants.getFirstLineIndent( as );
  if
  	( f == null )
  {
	  m_firstLineIndentTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_firstLineIndentTextField.setAsIs();
  } else {
	  m_firstLineIndentTextField.setText( "" + f );
  }
  
  f = CoStyleConstants.getTrailingLinesIndent( as );
  if
  	( f == null )
  {
	  m_trailingLinesIndentTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_trailingLinesIndentTextField.setAsIs();
  } else {
	  m_trailingLinesIndentTextField.setText( "" + f );
  }
  
  f = CoStyleConstants.getRightIndent( as );
  if
  	( f == null )
  {
	  m_rightIndentTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_rightIndentTextField.setAsIs();
  } else {
	  m_rightIndentTextField.setText( "" + f );
  }
	
  f = CoStyleConstants.getLeftIndent( as );
  if
  	( f == null )
  {
	  m_leftIndentTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_leftIndentTextField.setAsIs();
  } else {
	  m_leftIndentTextField.setText( "" + f );
  }





	b = CoStyleConstants.getKeepLinesTogether( as );
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		m_linesTogetherCheckbox.setAsIs();
	} else {
		m_linesTogetherCheckbox.setTriState( b );
	}

	b = CoStyleConstants.getTopOfColumn( as );
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		m_topOfColumnCheckbox.setAsIs();
	} else {
		m_topOfColumnCheckbox.setTriState( b );
	}

	b = CoStyleConstants.getLastInColumn( as );
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		m_lastInColumnCheckbox.setAsIs();
	} else {
		m_lastInColumnCheckbox.setTriState( b );
	}

	b = CoStyleConstants.getAdjustToBaseLineGrid( as );
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		m_adjustToBaseLineGridCheckbox.setAsIs();
	} else {
		m_adjustToBaseLineGridCheckbox.setTriState( b );
	}

	b = CoStyleConstants.getDropCaps( as );
	if
		( b == CoStyleConstants.AS_IS_BOOLEAN_VALUE )
	{
		m_dropCapsCheckbox.setAsIs();
	} else {
		m_dropCapsCheckbox.setTriState( b );
	}

	e = CoStyleConstants.getAlignment( as );
	if
		( e == CoStyleConstants.AS_IS_ENUM_VALUE )
	{
		m_alignmentOptionMenu.setAsIs();
	} else {
		m_alignmentOptionMenu.setSelectedItem( e, true );
	}

	s = CoStyleConstants.getHyphenation( as );
	if
		( s == CoStyleConstants.AS_IS_STRING_VALUE )
	{
		m_hyphenationOptionMenu.setAsIs();
	} else {
		m_hyphenationOptionMenu.setSelectedItem( s, true );
	}
		

	e = CoStyleConstants.getHyphenationFallbackBehavior( as );
	if
		( e == CoStyleConstants.AS_IS_ENUM_VALUE )
	{
		m_hyphenationFallbackBehaviorOptionMenu.setAsIs();
	} else {
		m_hyphenationFallbackBehaviorOptionMenu.setSelectedItem( e, true );
	}

	
	CoTabSetIF ts = (CoTabSetIF) CoStyleConstants.getTabSet( as );
	if
		( ts == CoStyleConstants.AS_IS_TAB_SET_VALUE )
	{
		m_tabSetPanel.setTabSet( null );
	} else {
		m_tabSetPanel.setTabSet( ts );
	}

	
}
private void updateTopRulerPanel( AttributeSet as )
{
	CoEnumValue e;
	Float f;

	// top ruler
	f = CoStyleConstants.getTopRulerPosition( as );
  if
  	( f == null )
  {
	  m_topRulerPanel.m_positionTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_topRulerPanel.m_positionTextField.setAsIs();
  } else {
	  m_topRulerPanel.m_positionTextField.setText( f + " %" );
  }

	f = CoStyleConstants.getTopRulerThickness( as );
  if
  	( f == null )
  {
	  m_topRulerPanel.m_thicknessTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_topRulerPanel.m_thicknessTextField.setAsIs();
  } else {
	  m_topRulerPanel.m_thicknessTextField.setText( "" + f );
  }

//	m_topRulerPanel.setAsIsSpan( CoViewStyleConstants.getTopRulerSpan( as ).toString() );

	e = CoStyleConstants.getTopRulerSpan( as );
	if
		( e == CoStyleConstants.AS_IS_ENUM_VALUE )
	{
		m_topRulerPanel.m_spanOptionMenu.setAsIs();
	} else {
  	m_topRulerPanel.m_spanOptionMenu.setSelectedItem( e, true );
	}

	e = CoStyleConstants.getTopRulerAlignment( as );
	if
		( e == CoStyleConstants.AS_IS_ENUM_VALUE )
	{
		m_topRulerPanel.m_fixedSpanAlignmentOptionMenu.setAsIs();
	} else {
  	m_topRulerPanel.m_fixedSpanAlignmentOptionMenu.setSelectedItem( e, true );
	}

  f = CoStyleConstants.getTopRulerFixedWidth( as );
  if
  	( f == null )
  {
	  m_topRulerPanel.m_fixedSpanWidthTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_topRulerPanel.m_fixedSpanWidthTextField.setAsIs();
  } else {
	  m_topRulerPanel.m_fixedSpanWidthTextField.setText( "" + f );
  }

	f = CoStyleConstants.getTopRulerLeftInset( as );
  if
  	( f == null )
  {
	  m_topRulerPanel.m_leftInsetTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_topRulerPanel.m_leftInsetTextField.setAsIs();
  } else {
	  m_topRulerPanel.m_leftInsetTextField.setText( "" + f );
	}
 
  f = CoStyleConstants.getTopRulerRightInset( as );
  if
  	( f == null )
  {
	  m_topRulerPanel.m_rightInsetTextField.setText( "" );
  } else if
  	( f == CoStyleConstants.AS_IS_FLOAT_VALUE )
  {
		m_topRulerPanel.m_rightInsetTextField.setAsIs();
  } else {
	  m_topRulerPanel.m_rightInsetTextField.setText( "" + f );
  }
 
}




public CoTextField createLengthTextField( CoUserInterfaceBuilder b, String name, CoLengthUnitSet us )
{
	CoTextField tf = new CoNumericTextField( us );//CoLengthUnit.LENGTH_UNITS );
	b.prepareTextField( tf );
	tf.setHorizontalAlignment( SwingConstants.RIGHT );
	tf.setBorder( BorderFactory.createEtchedBorder() );
	if ( name != null ) b.addNamedWidget( name, tf );
	return tf;
}
}