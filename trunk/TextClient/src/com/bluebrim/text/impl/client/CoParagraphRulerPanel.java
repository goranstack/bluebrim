package com.bluebrim.text.impl.client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

//

public class CoParagraphRulerPanel extends CoPanel
{
	public final static String ACTIVE = "CoParagraphRulerPanel.ACTIVE";

	public final static String POSITION = "CoParagraphRulerPanel.POSITION";
	public final static String THICKNESS = "CoParagraphRulerPanel.THICKNESS";
	public final static String SPAN = "CoParagraphRulerPanel.SPAN";

	public final static String FIXED_SPAN_ALIGNMENT = "CoParagraphRulerPanel.FIXED_SPAN_ALIGNMENT";
	public final static String FIXED_SPAN_WIDTH = "CoParagraphRulerPanel.FIXED_SPAN_WIDTH";

	public final static String LEFT_INSET = "CoParagraphRulerPanel.LEFT_INSET";
	public final static String RIGHT_INSET = "CoParagraphRulerPanel.RIGHT_INSET";

	public CoTextField m_positionTextField;
	public CoTextField m_thicknessTextField;
	public CoOptionMenu m_spanOptionMenu;

	public CoOptionMenu m_fixedSpanAlignmentOptionMenu;
	public CoTextField m_fixedSpanWidthTextField;
	
	public CoTextField m_leftInsetTextField;
	public CoTextField m_rightInsetTextField;
	
	private CoTextField m_leftInsetTextField2;
	private CoTextField m_rightInsetTextField2;
	
	private String m_asIsSpan = CoViewStyleConstants.getDefaultRulerSpan().toString();
public CoParagraphRulerPanel( String name, CoUserInterfaceBuilder b, CoNumericComponentBuilder ncb )
{
	super( new CoRowLayout( 5 ) );
	
	b.preparePanel( this );
	b.addNamedWidget( name, this );

	CoPanel p0 = b.createPanel( new CoFormLayout() );
	add( p0 );
	
	p0.add( b.createLabel( CoTextStringResources.getName( THICKNESS ) ) );
	p0.add( m_thicknessTextField = ncb.createLengthTextField( b, name + THICKNESS, CoLengthUnit.LENGTH_UNITS ) );

	p0.add( b.createLabel( CoTextStringResources.getName( POSITION ) ) );
	p0.add( m_positionTextField = b.createSlimTextField( name + POSITION ) );
	m_positionTextField.setHorizontalAlignment( SwingConstants.RIGHT );

	p0.add( b.createLabel( CoTextStringResources.getName( SPAN ) ) );
	p0.add( m_spanOptionMenu = b.createOptionMenu( name + SPAN ) );
	m_spanOptionMenu.addNullItem( CoTextStringResources.getName( "UNKNOWN" ) );
	m_spanOptionMenu.addItem( CoTextConstants.RULER_SPAN_TEXT );
	m_spanOptionMenu.addItem( CoTextConstants.RULER_SPAN_FIXED );
	m_spanOptionMenu.addItem( CoTextConstants.RULER_SPAN_COLUMN );
	m_spanOptionMenu.setRenderer( new CoOptionMenu.TranslatingRenderer( CoTextStringResources.getBundle() ) );
	
	final CardLayout cardLayout = new CardLayout();
	final CoPanel p1 = b.createPanel( cardLayout );
	add( p1 );

	{
		CoPanel p = b.createPanel( new CoFormLayout() );
		p1.add( p, CoTextConstants.RULER_SPAN_TEXT.toString() );
		p.add( b.createLabel( CoTextStringResources.getName( LEFT_INSET ) ) );
		p.add( m_leftInsetTextField = ncb.createLengthTextField( b, name + LEFT_INSET, CoLengthUnit.LENGTH_UNITS ) );
		p.add( b.createLabel( CoTextStringResources.getName( RIGHT_INSET ) ) );
		p.add( m_rightInsetTextField = ncb.createLengthTextField( b, name + RIGHT_INSET, CoLengthUnit.LENGTH_UNITS ) );
	}

	{
		CoPanel p = b.createPanel( new CoFormLayout() );
		p1.add( p, CoTextConstants.RULER_SPAN_FIXED.toString() );
		p.add( b.createLabel( CoTextStringResources.getName( FIXED_SPAN_ALIGNMENT ) ) );
		p.add( m_fixedSpanAlignmentOptionMenu = b.createOptionMenu( name + FIXED_SPAN_ALIGNMENT ) );
		m_fixedSpanAlignmentOptionMenu.addNullItem( CoTextStringResources.getName( "UNKNOWN" ) );
		m_fixedSpanAlignmentOptionMenu.addItem( CoTextConstants.ALIGN_LEFT );
		m_fixedSpanAlignmentOptionMenu.addItem( CoTextConstants.ALIGN_CENTER );
		m_fixedSpanAlignmentOptionMenu.addItem( CoTextConstants.ALIGN_RIGHT );
		m_fixedSpanAlignmentOptionMenu.setRenderer( new CoOptionMenu.TranslatingRenderer( CoTextStringResources.getBundle() ) );

		p.add( b.createLabel( CoTextStringResources.getName( FIXED_SPAN_WIDTH ) ) );
		p.add( m_fixedSpanWidthTextField = ncb.createLengthTextField( b, name + FIXED_SPAN_WIDTH, CoLengthUnit.LENGTH_UNITS ) );
	}

	{
		CoPanel p = b.createPanel( new CoFormLayout() );
		p1.add( p, CoTextConstants.RULER_SPAN_COLUMN.toString() );

		p.add( b.createLabel( CoTextStringResources.getName( LEFT_INSET ) ) );
		m_leftInsetTextField2 = ncb.createLengthTextField( b, null, CoLengthUnit.LENGTH_UNITS );
		m_leftInsetTextField2.setDocument( m_leftInsetTextField.getDocument() );
		m_leftInsetTextField2.addActionListener(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent ev ) { m_leftInsetTextField.postActionEvent(); }
			}
		);
		p.add( m_leftInsetTextField2 );
		
		p.add( b.createLabel( CoTextStringResources.getName( RIGHT_INSET ) ) );
		m_rightInsetTextField2= ncb.createLengthTextField( b, null, CoLengthUnit.LENGTH_UNITS );
		m_rightInsetTextField2.setDocument( m_rightInsetTextField.getDocument() );
		m_rightInsetTextField2.addActionListener(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent ev ) { m_rightInsetTextField.postActionEvent(); }
			}
		);
		p.add( m_rightInsetTextField2 );
	}
	
	m_spanOptionMenu.addItemListener(
		new ItemListener()
		{
			public void itemStateChanged( ItemEvent ev )
			{
				if ( ev.getStateChange() == ItemEvent.DESELECTED ) return;
				Object i = ev.getItem();
				if
					( i != null )
				{
					cardLayout.show( p1, i.toString() );
				} else {
					cardLayout.show( p1, m_asIsSpan );
				}
			}
		}
	);

}
void setAsIsSpan( String s )
{
	m_asIsSpan = s;
}
public void setEnabled( boolean b )
{
	super.setEnabled( b );
	
	m_positionTextField.setEnabled( b );
	m_thicknessTextField.setEnabled( b );
	m_spanOptionMenu.setEnabled( b );

	m_fixedSpanAlignmentOptionMenu.setEnabled( b );
	m_fixedSpanWidthTextField.setEnabled( b );
	
	m_leftInsetTextField.setEnabled( b );
	m_rightInsetTextField.setEnabled( b );
	
	m_leftInsetTextField2.setEnabled( b );
	m_rightInsetTextField2.setEnabled( b );
}
}