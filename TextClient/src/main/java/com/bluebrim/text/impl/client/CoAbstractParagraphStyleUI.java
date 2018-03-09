package com.bluebrim.text.impl.client;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 * UI för styckesutformning.
 * Denna klass har inga värdemodeller, tanken är att den vid behov skapas i subklasser.
 * Den har heller inga "lyssnare" på "plain"-knappen, även här är tanken att dessa
 * skapas i subklasser.
 */

public abstract class CoAbstractParagraphStyleUI extends CoAbstractTextStyleUI
{
	protected final static String DROP_CAPS_PANEL = "CoAbstractParagraphStyleUI.DROP_CAPS_PANEL";
	public final static String TOP_RULER = "CoAbstractParagraphStyleUI.TOP_RULER";
	public final static String BOTTOM_RULER = "CoAbstractParagraphStyleUI.BOTTOM_RULER";



	protected CoTabSetPanel m_tabSetPanel;

/**
 * com.bluebrim.publication.impl.client.CoEditionUI constructor comment.
 */
public CoAbstractParagraphStyleUI() {
	super();
}
/**
 */
protected void buildAlignmentBox(CoOptionMenu alignmentBox)
{
	alignmentBox.addNullItem( CoTextStringResources.getName( "UNKNOWN" ) );
	alignmentBox.addItem( CoTextConstants.ALIGN_LEFT );
	alignmentBox.addItem( CoTextConstants.ALIGN_CENTER );
	alignmentBox.addItem( CoTextConstants.ALIGN_RIGHT );
	alignmentBox.addItem( CoTextConstants.ALIGN_JUSTIFIED );
	alignmentBox.addItem( CoTextConstants.ALIGN_FORCED );
//	alignmentBox.setMaximumRowCount( 6 );
}
/**
 */
protected void buildHyphenationBox(CoOptionMenu box, Collection hyphenations )
{
	box.setQuiet( true );

	Object tmp = box.getSelectedItem();
	
	if ( box.getItemCount() > 0 ) box.removeAllItems();
	
	box.addNullItem( CoTextConstants.NO_VALUE );
	
	int i = 1;
	if
		( hyphenations != null )
	{
		Iterator e = hyphenations.iterator();
		while
			( e.hasNext() )
		{
			com.bluebrim.text.shared.CoHyphenationIF hs = (com.bluebrim.text.shared.CoHyphenationIF) e.next();
			box.addItem( hs.getName() );
			i++;
		}
	}
//	box.setMaximumRowCount( i );

	box.setSelectedItem( tmp );
	
	box.setQuiet( false );
}
/**
 */
protected void buildHyphenationFallbackBehaviorBox(CoOptionMenu alignmentBox)
{
	alignmentBox.addNullItem( CoTextStringResources.getName( "UNKNOWN" ) );
	alignmentBox.addItem( CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR_FIRST_BREAKPOINT );
	alignmentBox.addItem( CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR_NO_LINE );
	alignmentBox.addItem( CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR_NON_BREAKPOINT );
}
protected CoPanel createAdditionalPanel()
{
	CoUserInterfaceBuilder builder = getUIBuilder();
	
	CoPanel panel = builder.createPanel( new CoColumnLayout(), true, new Insets( 2, 2, 2, 2 ) );
	
	{
		CoPanel p = builder.createPanel( new CoColumnLayout(), true, new Insets( 2, 2, 2, 2 ) );

		CoTriStateCheckBox cb = builder.createSlimTriStateCheckBox( CoTextStringResources.getName( CoTextConstants.DROP_CAPS ), CoTextConstants.DROP_CAPS );
		cb.setAlignmentX( Component.LEFT_ALIGNMENT );
		p.add( cb );

		{
			CoPanel p2 = builder.createPanel( new CoFormLayout( 5, 0 ), true, new Insets( 2, 2, 2, 2 ), DROP_CAPS_PANEL );
			p.add( p2 );

			p2.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.DROP_CAPS_COUNT ) ) );
			CoTextField tf = builder.createSlimTextField( SwingConstants.RIGHT, 5, CoTextConstants.DROP_CAPS_COUNT );
			tf.setActivateWhenLosingFocus( true );
			CoPanel tmp = new CoTextfieldCounterPanel( tf, null, 1.0f, Float.POSITIVE_INFINITY, 1.0f );
			p2.add( tmp );


			p2.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.DROP_CAPS_HEIGHT ) ) );
			tf = builder.createSlimTextField( SwingConstants.RIGHT, 5, CoTextConstants.DROP_CAPS_HEIGHT );
			tf.setActivateWhenLosingFocus( true );
			tmp = new CoTextfieldCounterPanel( tf, null, 2.0f, Float.POSITIVE_INFINITY, 1.0f );
			p2.add( tmp );

			p2.setAlignmentX( Component.LEFT_ALIGNMENT );
		}
		p.setAlignmentX( Component.LEFT_ALIGNMENT );
		p.setBorder( new CoTitledBorder( null, TitledBorder.BELOW_TOP ) );
		panel.add( p );
	}
	
	{
		CoTriStateCheckBox cb = builder.createSlimTriStateCheckBox( CoTextStringResources.getName( CoTextConstants.LINES_TOGETHER ), CoTextConstants.LINES_TOGETHER );
		cb.setAlignmentX( Component.LEFT_ALIGNMENT );
		panel.add( cb );
	}
	
	{
		CoTriStateCheckBox cb = builder.createSlimTriStateCheckBox( CoTextStringResources.getName( CoTextConstants.TOP_OF_COLUMN ), CoTextConstants.TOP_OF_COLUMN );
		cb.setAlignmentX( Component.LEFT_ALIGNMENT );
		panel.add( cb );
	}
	
	{
		CoTriStateCheckBox cb = builder.createSlimTriStateCheckBox( CoTextStringResources.getName( CoTextConstants.LAST_IN_COLUMN ), CoTextConstants.LAST_IN_COLUMN );
		cb.setAlignmentX( Component.LEFT_ALIGNMENT );
		panel.add( cb );
	}
	
	{
		CoTriStateCheckBox cb = builder.createSlimTriStateCheckBox( CoTextStringResources.getName( CoTextConstants.ADJUST_TO_BASE_LINE_GRID ), CoTextConstants.ADJUST_TO_BASE_LINE_GRID );
		cb.setAlignmentX( Component.LEFT_ALIGNMENT );
		panel.add( cb );
	}
	
	return panel;
}
/**
 */
protected CoPanel createCharacterAttributesPanel()
{
	CoSubcanvas c = getUIBuilder().createSubcanvas( getCharacterAttributesPanel(), CoTextConstants.CHARACTER_STYLE );

	return c;
}

/**
 */
protected CoPanel createParagraphAttributesPanel()
{
	CoPanel p = getUIBuilder().createBoxPanel( BoxLayout.X_AXIS, true, null );

	( (JComponent) p.add( createParagraphPanel() ) ).setAlignmentY( Component.TOP_ALIGNMENT );
	( (JComponent) p.add( createAdditionalPanel() ) ).setAlignmentY( Component.TOP_ALIGNMENT );

	p.add( Box.createHorizontalGlue() );
	
	return p;
}
/**
 */
protected CoPanel createParagraphPanel()
{
	CoUserInterfaceBuilder builder = getUIBuilder();
	
	CoPanel p = builder.createRigidPanel( new CoFormLayout( 5, 0 ), true, new Insets(2, 2, 2, 2) );
	
	CoTextField tf;
		
	p.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.FIRST_LINE_INDENT ) ) );
	tf = createLengthTextField( builder, CoTextConstants.FIRST_LINE_INDENT, CoLengthUnit.LENGTH_UNITS );
	tf.setActivateWhenLosingFocus( true );
	p.add( new CoTextfieldCounterPanel( tf, CoLengthUnit.LENGTH_UNITS, 1.0f ) );
	
	p.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.TRAILING_LINES_INDENT ) ) );
	tf = createLengthTextField( builder, CoTextConstants.TRAILING_LINES_INDENT, CoLengthUnit.LENGTH_UNITS );
	tf.setActivateWhenLosingFocus( true );
	p.add( new CoTextfieldCounterPanel( tf, CoLengthUnit.LENGTH_UNITS, 1.0f ) );
	
	p.add(builder.createLabel( CoTextStringResources.getName( CoTextConstants.LEFT_INDENT ) ) );
	tf = createLengthTextField( builder, CoTextConstants.LEFT_INDENT, CoLengthUnit.LENGTH_UNITS );
	tf.setActivateWhenLosingFocus( true );
	p.add( new CoTextfieldCounterPanel( tf, CoLengthUnit.LENGTH_UNITS, 1.0f ) );
	
	p.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.RIGHT_INDENT ) ) );
	tf = createLengthTextField( builder, CoTextConstants.RIGHT_INDENT, CoLengthUnit.LENGTH_UNITS );
	tf.setActivateWhenLosingFocus( true );
	p.add( new CoTextfieldCounterPanel( tf, CoLengthUnit.LENGTH_UNITS, 1.0f ) );
	
	p.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.SPACE_ABOVE ) ) );
	tf = createLengthTextField( builder, CoTextConstants.SPACE_ABOVE, CoLengthUnit.LENGTH_UNITS );
	tf.setActivateWhenLosingFocus( true );
	p.add( new CoTextfieldCounterPanel( tf, CoLengthUnit.LENGTH_UNITS, 1.0f ) );
	
	p.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.SPACE_BELOW ) ) );
	tf = createLengthTextField( builder, CoTextConstants.SPACE_BELOW, CoLengthUnit.LENGTH_UNITS );
	tf.setActivateWhenLosingFocus( true );
	p.add( new CoTextfieldCounterPanel( tf, CoLengthUnit.LENGTH_UNITS, 1.0f ) );
		
	p.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.LEADING ) ) );
	tf = builder.createSlimTextField( SwingConstants.RIGHT, 5, CoTextConstants.LEADING );
	tf.setActivateWhenLosingFocus( true );
	p.add( new CoLeadingTextfieldCounterPanel( tf ) );
	
	p.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.MINIMUM_SPACE_WIDTH ) ) );
	tf = createLengthTextField( builder, CoTextConstants.MINIMUM_SPACE_WIDTH, null );
	tf.setActivateWhenLosingFocus( true );
	p.add( new CoTextfieldCounterPanel( tf, null, 0, Float.MAX_VALUE, 10 ) );
	
	p.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.OPTIMUM_SPACE_WIDTH ) ) );
	tf = createLengthTextField( builder, CoTextConstants.OPTIMUM_SPACE_WIDTH, null );
	tf.setActivateWhenLosingFocus( true );
	p.add( new CoTextfieldCounterPanel( tf, null, 0, Float.MAX_VALUE, 10 ) );
	
	{
		p.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.ALIGNMENT ) ) );
		CoOptionMenu cb = builder.createOptionMenu( CoTextConstants.ALIGNMENT );
		cb.setRenderer( new CoOptionMenu.TranslatingRenderer( CoTextStringResources.getBundle() ) );
		buildAlignmentBox( cb );
		p.add( cb );
	}

	{
		p.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.HYPHENATION ) ) );
		CoOptionMenu cb = builder.createOptionMenu( CoTextConstants.HYPHENATION );
		buildHyphenationBox( cb, null );
		p.add( cb );
	}
	
	{
		p.add( builder.createLabel( CoTextStringResources.getName( CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR ) ) );
		CoOptionMenu cb = builder.createOptionMenu( CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR );
		cb.setRenderer( new CoOptionMenu.TranslatingRenderer( CoTextStringResources.getBundle() ) );
		buildHyphenationFallbackBehaviorBox( cb );
		p.add( cb );
	}

	return p;
}

/**
 */
protected void createTabs( JTabbedPane tp, CoUserInterfaceBuilder builder )
{
	tp.add( createParagraphAttributesPanel(), CoTextStringResources.getName( "PARAGRAPH_STYLE_RULE" ) );
	tp.add( createCharacterAttributesPanel(), CoTextStringResources.getName( "CHARACTER_STYLE_RULE" ) );
	tp.add( createParagraphRulersPanel( builder ), CoTextStringResources.getName( "PARAGRAPH_RULERS" ) );
	tp.add( createTabStopsPanel( builder ), CoTextStringResources.getName( "TAB_STOPS" ) );
}
protected abstract CoTabSetPanel.TabSetEditor createTabSetEditor();
protected CoPanel createTabStopsPanel( CoUserInterfaceBuilder b )
{
	CoPanel p = b.createPanel( new CoAttachmentLayout() );

	CoLabel l = b.createLabel( CoTextStringResources.getName( CoTextConstants.REGULAR_TAB_STOP_INTERVAL ) );
	CoTextField tf = createLengthTextField( b, CoTextConstants.REGULAR_TAB_STOP_INTERVAL, CoLengthUnit.LENGTH_UNITS );
	tf.setColumns( 10 );
	
	m_tabSetPanel = new CoTabSetPanel( b, getMenuBuilder(), createTabSetEditor() );
	m_tabSetPanel.setTabSet( new CoTabSet() );

	JComponent c = createTabStopsPanelWorkspace( b );
	
	p.add( l,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
		                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_COMPONENT_BOTTOM, 0, tf ),
		                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
		                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	p.add( tf,
	       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_CONTAINER, 0 ),
		                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
		                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_COMPONENT_RIGHT, 0, l ),
		                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_NO ) ) );
	if
		( c == null )
	{
		p.add( m_tabSetPanel,
		       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, l ),
			                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_CONTAINER, 0 ),
			                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
			                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_CONTAINER, 0 ) ) );
	} else {
		p.add( c,
		       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, l ),
			                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_NO ),
			                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
			                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_CONTAINER, 0 ) ) );
		p.add( m_tabSetPanel,
		       new CoAttachmentLayout.Attachments( new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.TOP_COMPONENT_BOTTOM, 0, c ),
			                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.BOTTOM_CONTAINER, 0 ),
			                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.LEFT_CONTAINER, 0 ),
			                                         new CoAttachmentLayout.AttachmentSpec( CoAttachmentLayout.RIGHT_CONTAINER, 0 ) ) );
	}

	return p;
}
protected JComponent createTabStopsPanelWorkspace( CoUserInterfaceBuilder b )
{
	return null;
}
/**
 */
protected void createWidgets( CoPanel p, CoUserInterfaceBuilder builder )
{
	super.createWidgets( p, builder );
	
	JTabbedPane tp = builder.createTabbedPane();//new JTabbedPane();
	createTabs( tp, builder );

	p.add( tp, BorderLayout.NORTH );
}
/**
 */
protected abstract CoUserInterface getCharacterAttributesPanel();

protected void postContextChange( com.bluebrim.text.shared.CoTypographyContextIF context )
{
	CoOptionMenu box = (CoOptionMenu) getNamedWidget( CoTextConstants.HYPHENATION );
	
	if
		( context != null )
	{
		buildHyphenationBox( box, context.getHyphenations() );
	} else {
		buildHyphenationBox( box, null );
	}
}


protected CoPanel createParagraphRulersPanel( CoUserInterfaceBuilder b )
{
	CoPanel p = getUIBuilder().createPanel( new CoColumnLayout() );

	CoParagraphRulerPanel topRulerPanel = new CoParagraphRulerPanel( TOP_RULER, getUIBuilder(), this );
		
	p.add( topRulerPanel );
	TitledBorder border = BorderFactory.createTitledBorder( CoTextStringResources.getName( TOP_RULER ) );
	border.setTitleFont( b.getFont( CoUserInterfaceBuilder.LABEL_FONT ) );
	border.setTitleColor( b.getColor( CoUserInterfaceBuilder.LABEL_FOREGROUND ) );
	topRulerPanel.setBorder( border );
	
	CoParagraphRulerPanel bottomRulerPanel = new CoParagraphRulerPanel( BOTTOM_RULER, getUIBuilder(), this );
		
	p.add( bottomRulerPanel );
	border = BorderFactory.createTitledBorder( CoTextStringResources.getName( BOTTOM_RULER ) );
	border.setTitleFont( b.getFont( CoUserInterfaceBuilder.LABEL_FONT ) );
	border.setTitleColor( b.getColor( CoUserInterfaceBuilder.LABEL_FOREGROUND ) );
	bottomRulerPanel.setBorder( border );
	
	return p;
}

public abstract CoTextField createLengthTextField( CoUserInterfaceBuilder b, String name, CoLengthUnitSet us );
}