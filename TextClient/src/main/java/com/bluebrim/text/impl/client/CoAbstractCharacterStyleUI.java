package com.bluebrim.text.impl.client;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.paint.client.*;
import com.bluebrim.paint.shared.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.shared.*;

/**
 * UI för teckenutformning.
 * Denna klass har inga värdemodeller, tanken är att den vid behov skapas i subklasser.
 * Den har heller inga "lyssnare" på "plain"-knappen, även här är tanken att dessa
 * skapas i subklasser.
 */

public class CoAbstractCharacterStyleUI extends CoAbstractTextStyleUI
{
	private CoNumericComponentBuilder m_numericComponentBuilder;




	private class OptionMenuTextColorRenderer extends CoOptionMenuColorRenderer
	{
		public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
		{
			value = ( value == null || getContext() == null ) ? null : getContext().getColor( (String) value );
			return super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
		}	
	};

public CoAbstractCharacterStyleUI()
{
	super();
}
CoAbstractCharacterStyleUI( CoNumericComponentBuilder ncb )
{
	super();

	m_numericComponentBuilder = ncb;
}
/**
 */
protected void buildColorBox( CoOptionMenu box, Collection colors )
{
	box.setQuiet( true );

	Object tmp = box.getSelectedItem();
	
	if ( box.getItemCount() > 0 ) box.removeAllItems();
	
	box.addNullItem( CoTextConstants.NO_VALUE );

	int n = 1;
	if
		( colors != null )
	{
		Iterator i = colors.iterator();
		while
			( i.hasNext() )
		{
			CoColorIF c =  (CoColorIF) i.next();
			box.addItem( c.getName() );
			n++;
		}
	}
//	box.setMaximumRowCount( n );

	box.setSelectedItem( tmp );
	
	box.setQuiet( false );
}
/**
 */
protected void buildFontBox(CoOptionMenu fontBox, java.util.List fonts)
{
	fontBox.setQuiet( true );

	Object tmp = fontBox.getSelectedItem();
	
	fontBox.removeAllItems();
	
	fontBox.addNullItem( CoTextStringResources.getName( "UNKNOWN" ) );

	if 
		( fonts != null)
	{
		Iterator iter = fonts.iterator();
		while 
			( iter.hasNext() )
		{
			fontBox.addItem( (String) iter.next() );
		}
//		fontBox.setMaximumRowCount( fonts.size() + 1 );
	} else {
//		fontBox.setMaximumRowCount( 1 );
	}

	if ( tmp != null ) fontBox.setSelectedItem( tmp );
	
	fontBox.setQuiet( false );
}

/**
 */
protected void buildSizeBox( CoComboBox sizeBox )
{
	sizeBox.setQuiet( true );
	
	sizeBox.addNullItem( CoTextStringResources.getName( "UNKNOWN" ) );

	String[] sizes = CoTextStringResources.getNames( "FONT_SIZE_OPTIONS" );

	if ( sizes == null ) return;
	
	for
		( int i = 0; i < sizes.length; i++ )
	{
		sizeBox.addItem( sizes[ i ] );
	}
	sizeBox.setMaximumRowCount( sizes.length + 1 );

	sizeBox.setQuiet( false );
}





/**
 */
protected void createWidgets(CoPanel aPanel, CoUserInterfaceBuilder builder)
{
	super.createWidgets( aPanel, builder );
	
	aPanel.add( createStyleAttributesPanel( builder ) );
}

public void postContextChange( CoTypographyContextIF context )
{
	if
		( context != null )
	{
		buildColorBox( m_colorOptionMenu, context.getColors() );
		buildColorBox( m_shadowColorOptionMenu, context.getColors() );
		buildFontBox( m_familyOptionMenu, context.getFontFamilyNames() );
	} else {
		buildColorBox( m_colorOptionMenu, null );
		buildColorBox( m_shadowColorOptionMenu, null );
		buildFontBox( m_familyOptionMenu, null );
	}

}


/**
 */
protected CoPanel createFontPanel( CoUserInterfaceBuilder builder )
{
	CoPanel fp = builder.createPanel( new CoFormLayout(), true, null );
	
	{
		fp.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.UNDERLINE ) ) );
		m_underlineOptionMenu = builder.createOptionMenu( CoTextConstants.UNDERLINE );
		m_underlineOptionMenu.setRenderer( new CoOptionMenu.TranslatingRenderer( CoTextStringResources.getBundle() ) );
		m_underlineOptionMenu.addNullItem( CoTextStringResources.getName( "UNKNOWN" ) );
		m_underlineOptionMenu.addItem( CoTextConstants.UNDERLINE_NONE );
		m_underlineOptionMenu.addItem( CoTextConstants.UNDERLINE_NORMAL );
		m_underlineOptionMenu.addItem( CoTextConstants.UNDERLINE_WORD );
		fp.add( m_underlineOptionMenu );
	}
	
	{
		fp.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.VERTICAL_POSITION ) ) );
		m_verticalPositionOptionMenu = builder.createOptionMenu( CoTextConstants.VERTICAL_POSITION );
		m_verticalPositionOptionMenu.setRenderer( new CoOptionMenu.TranslatingRenderer( CoTextStringResources.getBundle() ) );
		m_verticalPositionOptionMenu.addNullItem( CoTextStringResources.getName( "UNKNOWN" ) );
		m_verticalPositionOptionMenu.addItem( CoTextConstants.VERTICAL_POSITION_NONE );
		m_verticalPositionOptionMenu.addItem( CoTextConstants.VERTICAL_POSITION_SUPERSCRIPT );
		m_verticalPositionOptionMenu.addItem( CoTextConstants.VERTICAL_POSITION_SUBSCRIPT );
		fp.add( m_verticalPositionOptionMenu );
	}

	
	{
		fp.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.TRACK_AMOUNT ) ) );
		
		m_trackAmountTextField = createLengthTextField( builder, CoTextConstants.TRACK_AMOUNT, null );
		m_trackAmountTextField.setActivateWhenLosingFocus( true );
		m_trackAmountTextField.setSelectWhenGainingFocus( true );
		fp.add( new CoTextfieldCounterPanel( m_trackAmountTextField ) );
	}

	{
		fp.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.BASELINE_OFFSET ) ) );
		
		m_baselineOffsetTextField = createLengthTextField( builder, CoTextConstants.BASELINE_OFFSET, null );
		m_baselineOffsetTextField.setActivateWhenLosingFocus( true );
		m_baselineOffsetTextField.setSelectWhenGainingFocus( true );
		fp.add( new CoTextfieldCounterPanel( m_baselineOffsetTextField ) );
	}
		
	fp.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.HORIZONTAL_SCALE ) ) );
	{
		m_horizontalScaleTextField = builder.createSlimTextField( SwingConstants.RIGHT, 5, CoTextConstants.HORIZONTAL_SCALE );
		m_horizontalScaleTextField.setActivateWhenLosingFocus( true );
		m_horizontalScaleTextField.setSelectWhenGainingFocus( true );
		fp.add( new CoTextfieldCounterPanel( m_horizontalScaleTextField, null, 0, Float.POSITIVE_INFINITY, 10, 100 ) );
	}
		
	fp.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.VERTICAL_SCALE ) ) );
	{
		m_verticalScaleTextField = builder.createSlimTextField( SwingConstants.RIGHT, 5, CoTextConstants.VERTICAL_SCALE );
		m_verticalScaleTextField.setActivateWhenLosingFocus( true );
		m_verticalScaleTextField.setSelectWhenGainingFocus( true );
		fp.add( new CoTextfieldCounterPanel( m_verticalScaleTextField, null, 0, Float.POSITIVE_INFINITY, 10, 100 ) );
	}


	return fp;
}


/**
 */
protected CoPanel createStyleAttributesPanel( CoUserInterfaceBuilder builder )
{
	CoPanel p = builder.createPanel( new CoAttachmentLayout() );

	CoLabel fontLabel = builder.createLabel( CoTextStringResources.getName( CoTextConstants.FONT_FAMILY ) );
	m_familyOptionMenu = builder.createOptionMenu( CoTextConstants.FONT_FAMILY );
	buildFontBox( m_familyOptionMenu, null);

	CoLabel sizeLabel = builder.createLabel( CoTextStringResources.getName( CoTextConstants.FONT_SIZE ) );
	m_sizeComboBox = builder.createSlimComboBox( CoTextConstants.FONT_SIZE );
	( (CoComboBox.Editor) m_sizeComboBox.getEditor() ).setColumns( 3 );
	( (CoComboBox.Editor) m_sizeComboBox.getEditor() ).setHorizontalAlignment( JTextField.RIGHT );
	buildSizeBox( m_sizeComboBox );
	m_sizeComboBox.setEditable(true);


	
	CoPanel p1 = createFontPanel( builder );
	CoPanel p2 = createStylePanel( builder );


	CoColorPanel colorPanel = new CoColorPanel( builder );
	m_colorOptionMenu = colorPanel.getColorOptionMenu();
	m_shadeTextField = colorPanel.getShadeTextField();
	builder.addNamedWidget( CoTextConstants.FOREGROUND_COLOR, m_colorOptionMenu );
	builder.addNamedWidget( CoTextConstants.FOREGROUND_SHADE, m_shadeTextField );
	CoLabel colorLabel = builder.createLabel( CoTextStringResources.getName( CoTextConstants.FOREGROUND_COLOR ) );
	m_colorOptionMenu.setRenderer( new OptionMenuTextColorRenderer() );
	buildColorBox( m_colorOptionMenu, null );


	CoPanel shadowPanel = createShadowPanel( builder );

	
	p.add( fontLabel,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, 0, m_familyOptionMenu ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_BOTTOM, 0, m_familyOptionMenu ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	p.add( sizeLabel,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, 0, m_sizeComboBox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_BOTTOM, 0, m_sizeComboBox ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 10, m_familyOptionMenu ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	p.add( colorLabel,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, 0, colorPanel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_BOTTOM, 0, colorPanel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	p.add( colorPanel,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 5, fontLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 5, colorLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	p.add( p2,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_TOP, 0, p1 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_TOP, -5, shadowPanel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 5, p1 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	p.add( p1,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 5, colorLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_TOP, -5, shadowPanel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	p.add( m_familyOptionMenu,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 5, fontLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	p.add( m_sizeComboBox,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 5, sizeLabel ),
	                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	p.add( shadowPanel,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_NO ),
	                                        	 new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_CONTAINER, 0 ),
	                                         	 new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
	                                         	 new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	return p;
}


/**
 */
protected CoPanel createStylePanel( CoUserInterfaceBuilder builder )
{
	CoPanel p = builder.createPanel( new CoColumnLayout(), true, null );
	
	p.add( m_boldCheckbox = builder.createSlimTriStateCheckBox( CoTextStringResources.getName( CoTextConstants.WEIGHT ), CoTextConstants.WEIGHT ) );
	p.add( m_italicCheckbox = builder.createSlimTriStateCheckBox(CoTextStringResources.getName( CoTextConstants.STYLE ), CoTextConstants.STYLE ) );
	p.add( m_strikeThruCheckbox = builder.createSlimTriStateCheckBox(CoTextStringResources.getName( CoTextConstants.STRIKE_THRU ), CoTextConstants.STRIKE_THRU ) );
	p.add( m_superiorCheckbox = builder.createSlimTriStateCheckBox(CoTextStringResources.getName( CoTextConstants.SUPERIOR ), CoTextConstants.SUPERIOR ) );
	p.add( m_shadowCheckbox = builder.createSlimTriStateCheckBox(CoTextStringResources.getName( CoTextConstants.SHADOW ), CoTextConstants.SHADOW ) );
	p.add( m_allCapsCheckbox = builder.createSlimTriStateCheckBox(CoTextStringResources.getName( CoTextConstants.ALL_CAPS ), CoTextConstants.ALL_CAPS ) );

	return p;
}


	protected CoTriStateCheckBox m_allCapsCheckbox;
	protected CoTextField m_baselineOffsetTextField;
	protected CoTriStateCheckBox m_boldCheckbox;
	protected CoOptionMenu m_colorOptionMenu;
	protected CoOptionMenu m_familyOptionMenu;
	protected CoTextField m_horizontalScaleTextField;
	protected CoTriStateCheckBox m_italicCheckbox;
	protected CoTextField m_shadeTextField;
	protected CoTextField m_shadowAngleTextField;
	protected CoTriStateCheckBox m_shadowCheckbox;
	protected CoOptionMenu m_shadowColorOptionMenu;
	protected CoTextField m_shadowOffsetTextField;
	protected CoTextField m_shadowShadeTextField;
	protected CoComboBox m_sizeComboBox;
	protected CoTriStateCheckBox m_strikeThruCheckbox;
	protected CoTriStateCheckBox m_superiorCheckbox;
	protected CoTextField m_trackAmountTextField;
	protected CoOptionMenu m_underlineOptionMenu;
	protected CoOptionMenu m_verticalPositionOptionMenu;
	protected CoTextField m_verticalScaleTextField;

public CoTextField createLengthTextField( CoUserInterfaceBuilder b, String name, CoLengthUnitSet us )
{
	if
		( m_numericComponentBuilder != null )
	{
		return m_numericComponentBuilder.createLengthTextField( b, name, us );
	} else {
		return b.createSlimTextField( CoTextField.RIGHT, name );
	}
}

protected void createListeners()
{
	super.createListeners();

	m_shadowCheckbox.addItemListener(
		new ItemListener()
		{
			public void itemStateChanged( ItemEvent e )
			{
				enableShadowPanel();
			}
		}
	);
}

/**
 */
protected CoPanel createShadowPanel( CoUserInterfaceBuilder builder )
{
	CoPanel fp = builder.createPanel( new CoFormLayout(), true, null );
	fp.setBorder( BorderFactory.createTitledBorder( CoTextStringResources.getName( CoTextConstants.SHADOW ) ) );
	
	{
		fp.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.SHADOW_OFFSET ) ) );
		
		m_shadowOffsetTextField = createLengthTextField( builder, CoTextConstants.SHADOW_OFFSET, null );
		m_shadowOffsetTextField.setActivateWhenLosingFocus( true );
		m_shadowOffsetTextField.setSelectWhenGainingFocus( true );
		fp.add( new CoTextfieldCounterPanel( m_shadowOffsetTextField, null, 0, Integer.MAX_VALUE, 10, 10 ) );
	}

	{
		fp.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.SHADOW_ANGLE ) ) );
		
		m_shadowAngleTextField = createLengthTextField( builder, CoTextConstants.SHADOW_ANGLE, null );
		m_shadowAngleTextField.setActivateWhenLosingFocus( true );
		m_shadowAngleTextField.setSelectWhenGainingFocus( true );
		fp.add( new CoTextfieldCounterPanel( m_shadowAngleTextField, null, 0, 360, 15, 45 ) );
	}


	CoColorPanel colorPanel = new CoColorPanel( builder );
	m_shadowColorOptionMenu = colorPanel.getColorOptionMenu();
	m_shadowShadeTextField = colorPanel.getShadeTextField();
	builder.addNamedWidget( CoTextConstants.SHADOW_COLOR, m_shadowColorOptionMenu );
	builder.addNamedWidget( CoTextConstants.SHADOW_SHADE, m_shadowShadeTextField );
	m_shadowColorOptionMenu.setRenderer( new OptionMenuTextColorRenderer() );
	buildColorBox( m_shadowColorOptionMenu, null );

	fp.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.SHADOW_COLOR ) ) );
	fp.add( colorPanel );

	return fp;
}

protected void doAfterCreateUserInterface()
{
	super.doAfterCreateUserInterface();

	enableShadowPanel();
}

protected void enableShadowPanel()
{
	/*
	boolean b = Boolean.TRUE.equals( m_shadowCheckbox.getTriState() );
	m_shadowOffsetTextField.setEnabled( b );
	m_shadowAngleTextField.setEnabled( b );
	m_shadowColorOptionMenu.setEnabled( b );
	m_shadowShadeTextField.setEnabled( b );
	*/
}
}