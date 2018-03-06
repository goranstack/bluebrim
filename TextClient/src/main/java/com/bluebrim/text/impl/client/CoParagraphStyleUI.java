package com.bluebrim.text.impl.client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.observable.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.transact.shared.*;




/**
 * Subklass till CoComponentParagraphStyleUI vars uppgift är att agera UI åt
 * en CoParagraphStyleIF-instans.
 * Värdemodeller för att åtstadkomma detta skapas.
 */

public abstract class CoParagraphStyleUI extends CoAbstractParagraphStyleUI
{
	private class ContextListener implements CoChangedObjectListener
	{
		public void serverObjectChanged( CoChangedObjectEvent e )
		{
			contextChanged();
		}
	};
	private CoChangedObjectListener m_contextListener = new ContextListener();

	private CoTabRulerPane m_tabRuler;

/**
 * CoEditionUI constructor comment.
 */
public CoParagraphStyleUI()
{
	super();

	CoDefaultServerObjectListener l = new CoDefaultServerObjectListener( this );
	l.initialize();
}

protected CoUserInterfaceBuilder createUserInterfaceBuilder()
{
    return new CoNumberUserInterfaceBuilder(this);
}

private void contextChanged()
{
	postContextChange( m_context );

	CoSubcanvas c = (CoSubcanvas) getUIBuilder().getNamedWidget( CoTextConstants.CHARACTER_STYLE );
	CoAbstractCharacterStyleUI ui = (CoAbstractCharacterStyleUI) c.getUserInterface();
	ui.postContextChange( m_context );

}

protected CoTabSetPanel.TabSetEditor createTabSetEditor()
{
	class TabSetEditor implements CoTabSetPanel.TabSetEditor
	{
		public void setTabSet( CoTabSetIF s, boolean isAdjusting )
		{
			if ( ! isAdjusting ) CoParagraphStyleUI.this.setTabSet( s );
		}
		
		public void unsetTabSet()
		{
			CoParagraphStyleUI.this.setTabSet( null );
		}
		
		public void setRegularTabStopInterval( float i, boolean isAdjusting )
		{
			if ( ! isAdjusting ) CoParagraphStyleUI.this.setRegularTabStopInterval( i );
		}
		
		public void unsetRegularTabStopInterval()
		{
			CoParagraphStyleUI.this.setRegularTabStopInterval( Float.NaN );
		}
	};

	
	return new TabSetEditor();
}
protected JComponent createTabStopsPanelWorkspace( CoUserInterfaceBuilder b )
{
	CoPanel p = b.createPanel( new BorderLayout() );
	p.setBorder( BorderFactory.createLoweredBevelBorder() );
	
	m_tabRuler =
		new CoTabRulerPane( createTabSetEditor() )
		{
			public void reshape( int x, int y, int w, int h )
			{
				super.reshape( x, y, w, h );
				setRange( getX0(), getX0() + w );
			}

		};

	m_tabRuler.setBackground( Color.white );
	m_tabRuler.setPaintTickValues( true );
	m_tabRuler.setTrackValue( true );
	m_tabRuler.setFont( p.getFont().deriveFont( 9f ) );

	final JViewport vp = new JViewport()
	{
			public Dimension getPreferredSize()
			{
				Dimension d = super.getPreferredSize();
				d.height = 20;
				return d;
			}
	};	
	vp.setView( m_tabRuler );
	
	p.add( vp, BorderLayout.CENTER );

	
	CoArrowButton leftButton = new CoArrowButton( CoArrowButton.WEST );
	p.add( leftButton, BorderLayout.WEST );
	leftButton.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				Point point = vp.getViewPosition();
				int w = vp.getWidth();
				point.x -= w / 4;
				if ( point.x < 0 ) point.x = 0;
				vp.setViewPosition( point );
				m_tabRuler.setSize( point.x + w, m_tabRuler.getHeight() );
				m_tabRuler.setRange( 0, point.x + w );
			}
		}
	);

	
	CoArrowButton rightButton = new CoArrowButton( CoArrowButton.EAST );
	p.add( rightButton, BorderLayout.EAST );
	rightButton.addActionListener(
		new ActionListener()
		{
			public void actionPerformed( ActionEvent ev )
			{
				Point point = vp.getViewPosition();
				int w = vp.getWidth();
				point.x += w / 4;
				vp.setViewPosition( point );
				m_tabRuler.setSize( point.x + w, m_tabRuler.getHeight() );
				m_tabRuler.setRange( 0, point.x + w );
			}
		}
	);
	
	return p;
}
/**
 */
protected void createValueModels(CoUserInterfaceBuilder b )
{

	CoAspectAdaptor ad;
	CoNumberUserInterfaceBuilder builder = (CoNumberUserInterfaceBuilder)b;


	CoParagraphRulerPanel tprp = (CoParagraphRulerPanel) getNamedWidget( TOP_RULER );
	CoParagraphRulerPanel bprp = (CoParagraphRulerPanel) getNamedWidget( BOTTOM_RULER );

	ad = new CoGsAspectAdaptor( this, CoTextConstants.TOP_RULER_LEFT_INSET )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getTopRulerLeftInset(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setTopRulerLeftInset( (Float) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createNumberFieldAdaptor(	ad, tprp.m_leftInsetTextField, CoNumberConverter.FLOAT, CoLengthUnit.LENGTH_UNITS);

	ad = new CoGsAspectAdaptor( this, CoTextConstants.BOTTOM_RULER_LEFT_INSET )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getBottomRulerLeftInset(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setBottomRulerLeftInset( (Float) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createNumberFieldAdaptor(	ad, bprp.m_leftInsetTextField, CoNumberConverter.FLOAT, CoLengthUnit.LENGTH_UNITS);
	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.TOP_RULER_RIGHT_INSET )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getTopRulerRightInset(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setTopRulerRightInset( (Float) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createNumberFieldAdaptor(	ad, tprp.m_rightInsetTextField, CoNumberConverter.FLOAT, CoLengthUnit.LENGTH_UNITS);
	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.BOTTOM_RULER_RIGHT_INSET )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getBottomRulerRightInset(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setBottomRulerRightInset( (Float) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createNumberFieldAdaptor(	ad, bprp.m_rightInsetTextField, CoNumberConverter.FLOAT, CoLengthUnit.LENGTH_UNITS);
	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.TOP_RULER_FIXED_WIDTH )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getTopRulerFixedWidth(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setTopRulerFixedWidth( (Float) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createNumberFieldAdaptor(	ad, tprp.m_fixedSpanWidthTextField, CoNumberConverter.FLOAT, CoLengthUnit.LENGTH_UNITS);
	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.BOTTOM_RULER_FIXED_WIDTH )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getBottomRulerFixedWidth(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setBottomRulerFixedWidth( (Float) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createNumberFieldAdaptor(	ad, bprp.m_fixedSpanWidthTextField, CoNumberConverter.FLOAT, CoLengthUnit.LENGTH_UNITS);
	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.TOP_RULER_SPAN )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getTopRulerSpan(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setTopRulerSpan( (CoEnumValue) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createOptionMenuAdaptor( ad, tprp.m_spanOptionMenu );
	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.BOTTOM_RULER_SPAN )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getBottomRulerSpan(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setBottomRulerSpan( (CoEnumValue) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createOptionMenuAdaptor( ad, bprp.m_spanOptionMenu );
	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.TOP_RULER_ALIGNMENT )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getTopRulerAlignment(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setTopRulerAlignment( (CoEnumValue) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createOptionMenuAdaptor( ad, tprp.m_fixedSpanAlignmentOptionMenu );
	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.BOTTOM_RULER_ALIGNMENT )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getBottomRulerAlignment(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setBottomRulerAlignment( (CoEnumValue) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createOptionMenuAdaptor( ad, bprp.m_fixedSpanAlignmentOptionMenu );
	
	class PositionConverter extends CoFloatConverter
	{
		public PositionConverter( CoValueModel vm ) { super( vm ); }

		public String valueToString( Object value ) 
		{
			String s = super.valueToString( value );
			if ( s.length()  > 0) s += " %";
			return s;
		}
	};

	ad = new CoGsAspectAdaptor( this, CoTextConstants.TOP_RULER_POSITION )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getTopRulerPosition(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setTopRulerPosition( (Float) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
//	builder.createComboBoxAdaptor( new PositionConverter( ad ), tprp.m_positionComboBox ); 
	builder.createNumberFieldAdaptor(	ad, tprp.m_positionTextField, CoNumberConverter.FLOAT );
	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.BOTTOM_RULER_POSITION )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getBottomRulerPosition(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setBottomRulerPosition( (Float) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
//	builder.createComboBoxAdaptor( new PositionConverter( ad ), bprp.m_positionComboBox ); 
	builder.createNumberFieldAdaptor(	ad, bprp.m_positionTextField, CoNumberConverter.FLOAT );
	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.TOP_RULER_THICKNESS )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getTopRulerThickness(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setTopRulerThickness( (Float) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
//	builder.createComboBoxAdaptor( new CoFloatConverter( ad, CoLengthUnit.LENGTH_UNITS ), tprp.m_thicknessComboBox ); 
	builder.createNumberFieldAdaptor(	ad, tprp.m_thicknessTextField, CoNumberConverter.FLOAT, CoLengthUnit.LENGTH_UNITS );
	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.BOTTOM_RULER_THICKNESS )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getBottomRulerThickness(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setBottomRulerThickness( (Float) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
//	builder.createComboBoxAdaptor( new CoFloatConverter( ad, CoLengthUnit.LENGTH_UNITS ), bprp.m_thicknessComboBox ); 
	builder.createNumberFieldAdaptor(	ad, bprp.m_thicknessTextField, CoNumberConverter.FLOAT, CoLengthUnit.LENGTH_UNITS );




	

	

	ad = new CoGsAspectAdaptor( this, CoTextConstants.REGULAR_TAB_STOP_INTERVAL )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getRegularTabStopInterval(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setRegularTabStopInterval( (Float) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createNumberFieldAdaptor(	ad,  (CoTextField) getNamedWidget(CoTextConstants.REGULAR_TAB_STOP_INTERVAL), CoNumberConverter.FLOAT, CoLengthUnit.LENGTH_UNITS);

	
	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.LEFT_INDENT )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getLeftIndent(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setLeftIndent( (Float) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createNumberFieldAdaptor(	ad,  (CoTextField) getNamedWidget(CoTextConstants.LEFT_INDENT), CoNumberConverter.FLOAT, CoLengthUnit.LENGTH_UNITS);
	

	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.FIRST_LINE_INDENT )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getFirstIndent(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setFirstIndent( (Float) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createNumberFieldAdaptor(	ad, (CoTextField) getNamedWidget(CoTextConstants.FIRST_LINE_INDENT), CoNumberConverter.FLOAT, CoLengthUnit.LENGTH_UNITS);

	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.TRAILING_LINES_INDENT )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getTrailingLinesIndent(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setTrailingLinesIndent( (Float) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createNumberFieldAdaptor(	ad, (CoTextField) getNamedWidget(CoTextConstants.TRAILING_LINES_INDENT), CoNumberConverter.FLOAT, CoLengthUnit.LENGTH_UNITS);


	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.RIGHT_INDENT )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getRightIndent(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setRightIndent( (Float) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createNumberFieldAdaptor(	ad, (CoTextField) getNamedWidget(CoTextConstants.RIGHT_INDENT), CoNumberConverter.FLOAT, CoLengthUnit.LENGTH_UNITS);



	ad = new CoGsAspectAdaptor( this, CoTextConstants.SPACE_ABOVE )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getSpaceBefore(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setSpaceBefore( (Float) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createNumberFieldAdaptor(	ad, (CoTextField) getNamedWidget(CoTextConstants.SPACE_ABOVE), CoNumberConverter.FLOAT, CoLengthUnit.LENGTH_UNITS);


	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.SPACE_BELOW )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getSpaceAfter(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setSpaceAfter( (Float) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createNumberFieldAdaptor(	ad, (CoTextField) getNamedWidget(CoTextConstants.SPACE_BELOW), CoNumberConverter.FLOAT, CoLengthUnit.LENGTH_UNITS);

	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.MINIMUM_SPACE_WIDTH )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getMinimumSpaceWidth(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setMinimumSpaceWidth( (Float) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createNumberFieldAdaptor(	ad, (CoTextField) getNamedWidget(CoTextConstants.MINIMUM_SPACE_WIDTH), CoNumberConverter.FLOAT, null );


	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.OPTIMUM_SPACE_WIDTH )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getOptimumSpaceWidth(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setOptimumSpaceWidth( (Float) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createNumberFieldAdaptor(	ad, (CoTextField) getNamedWidget(CoTextConstants.OPTIMUM_SPACE_WIDTH), CoNumberConverter.FLOAT, null );

	
	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.ALIGNMENT )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getAlignment(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setAlignment( (CoEnumValue) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createOptionMenuAdaptor( ad, (CoOptionMenu) getNamedWidget( CoTextConstants.ALIGNMENT ) );

	
	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.HYPHENATION )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getHyphenation(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setHyphenation( (String) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createOptionMenuAdaptor( ad, (CoOptionMenu) getNamedWidget( CoTextConstants.HYPHENATION ) );

	
	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR )
	{
		public Object get( CoObjectIF subject ) { return ( (CoParagraphStyleIF) subject ).getHyphenationFallbackBehavior(); }
		public void set( CoObjectIF subject, Object value ) { ( (CoParagraphStyleIF) subject ).setHyphenationFallbackBehavior( (CoEnumValue) value ); }
	};
	ad = builder.addAspectAdaptor( ad );
	builder.createOptionMenuAdaptor( ad, (CoOptionMenu) getNamedWidget( CoTextConstants.HYPHENATION_FALLBACK_BEHAVIOR ) );










	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.LEADING )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getLeading(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setLeading( (CoLeading) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createTextFieldAdaptor( new CoLeadingConverter( ad ), (CoTextField) getNamedWidget( CoTextConstants.LEADING) );


	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.DROP_CAPS )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getDropCaps(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setDropCaps( (Boolean) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createTriStateCheckBoxAdaptor( ad, (CoTriStateCheckBox) getNamedWidget(CoTextConstants.DROP_CAPS));


	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.DROP_CAPS_COUNT )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getDropCapsCharacterCount(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setDropCapsCharacterCount( (Integer) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createNumberFieldAdaptor( ad, (CoTextField) getNamedWidget(CoTextConstants.DROP_CAPS_COUNT), CoNumberConverter.INTEGER);


	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.DROP_CAPS_HEIGHT )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getDropCapsLineCount(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setDropCapsLineCount( (Integer) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createNumberFieldAdaptor(	ad, (CoTextField) getNamedWidget(CoTextConstants.DROP_CAPS_HEIGHT), CoNumberConverter.INTEGER);
	
	
	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.LINES_TOGETHER )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getKeepLinesTogether(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setKeepLinesTogether( (Boolean) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createTriStateCheckBoxAdaptor( ad, (CoTriStateCheckBox) getNamedWidget(CoTextConstants.LINES_TOGETHER));
	
	
	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.TOP_OF_COLUMN )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getTopOfColumn(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setTopOfColumn( (Boolean) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createTriStateCheckBoxAdaptor( ad, (CoTriStateCheckBox) getNamedWidget(CoTextConstants.TOP_OF_COLUMN));
	
	
	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.LAST_IN_COLUMN )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getLastInColumn(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setLastInColumn( (Boolean) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createTriStateCheckBoxAdaptor( ad, (CoTriStateCheckBox) getNamedWidget(CoTextConstants.LAST_IN_COLUMN));
	
	
	
	ad = new CoGsAspectAdaptor( this, CoTextConstants.ADJUST_TO_BASE_LINE_GRID )
	{
		public Object get( CoObjectIF subject ) 
			{ return ( (CoParagraphStyleIF) subject ).getAdjustToBaseLineGrid(); }
		public void set( CoObjectIF subject, Object value )
			{ ( (CoParagraphStyleIF) subject ).setAdjustToBaseLineGrid( (Boolean) value ); }
	};
	builder.addAspectAdaptor( ad );
	builder.createTriStateCheckBoxAdaptor( ad, (CoTriStateCheckBox) getNamedWidget(CoTextConstants.ADJUST_TO_BASE_LINE_GRID));

	



	builder.createSubcanvasAdaptor(
		builder.addAspectAdaptor(
			new CoReadOnlyAspectAdaptor( this, CoTextConstants.CHARACTER_STYLE )
			{
				public Object get( CoObjectIF subject )
				{
					return subject;
				}
			}
		),
		(CoSubcanvas) getNamedWidget( CoTextConstants.CHARACTER_STYLE )
	);

}
protected void doAfterCreateUserInterface()
{
	super.doAfterCreateUserInterface();
	
	setContext( getInitialContext() );
}
protected CoUserInterface getCharacterAttributesPanel()
{
	CoCharacterStyleUI ui =
		new CoCharacterStyleUI()
		{
			protected CoTypographyContextIF getInitialContext()
			{
				return CoParagraphStyleUI.this.getInitialContext();
			}
		};

	ui.setDomain( (CoCharacterStyleIF) getDomain() );

	return ui;
}
protected abstract CoTypographyContextIF getInitialContext();
protected void postDomainChange( CoObjectIF d ) 
{
	super.postDomainChange( d );

	update();
}
public void setContext( CoTypographyContextIF context )
{
	if ( m_context == context ) return;
	
	if
		( m_context != null )
	{
		CoObservable.removeChangedObjectListener( m_contextListener, getContext() );
	}
	
	super.setContext( context );

	if
		( m_context != null )
	{
		CoObservable.addChangedObjectListener( m_contextListener, getContext() );
	}

	CoSubcanvas c = (CoSubcanvas) getUIBuilder().getNamedWidget( CoTextConstants.CHARACTER_STYLE );
	CoAbstractCharacterStyleUI ui = (CoAbstractCharacterStyleUI) c.getUserInterface();
	ui.setContext( context );
}
private void setRegularTabStopInterval( final float i )
{
	CoCommand c = new CoCommand( "SET REGULAR TAB STOP INTERVAL" )
	{
		public boolean doExecute()
		{
			if
				( Float.isNaN( i ) )
			{
				( (CoParagraphStyleIF) getDomain() ).setRegularTabStopInterval( null );
			} else {
				( (CoParagraphStyleIF) getDomain() ).setRegularTabStopInterval( new Float( i ) );
			}
			return true;
		}
	};

	CoTransactionUtilities.execute( c, getDomain() );
}
private void setTabSet( final CoTabSetIF s )
{
	CoCommand c = new CoCommand( "SET TAB SET" )
	{
		public boolean doExecute()
		{
			( (CoParagraphStyleIF) getDomain() ).setTabSet( s );
			return true;
		}
	};

	CoTransactionUtilities.execute( c, getDomain() );
}
private void update() 
{
	CoObjectIF d = getDomain();

	if
		( d == null )
	{
		m_tabSetPanel.setTabSet( null );
		m_tabRuler.setTabSet( null );
		m_tabRuler.setRegularTabStopInterval( Float.NaN );
	} else {
		m_tabSetPanel.setTabSet( ( (CoParagraphStyleIF) d ).getTabSet() );
		m_tabRuler.setTabSet( ( (CoParagraphStyleIF) d ).getTabSet() );
		Float x = ( (CoParagraphStyleIF) d ).getRegularTabStopInterval();
		m_tabRuler.setRegularTabStopInterval( ( x == null ) ? Float.NaN : x.doubleValue() );
	}
}
public void valueHasChanged()
{
	super.valueHasChanged();

	update();
}

public CoTextField createLengthTextField( CoUserInterfaceBuilder b, String name, CoLengthUnitSet us )
{
	if
		( name == null )
	{
		return b.createSlimTextField( SwingConstants.RIGHT, us );
	} else {
		return b.createSlimTextField( SwingConstants.RIGHT, name );
	}
}
}