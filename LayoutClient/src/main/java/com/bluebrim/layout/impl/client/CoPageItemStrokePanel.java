package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.paint.client.*;
import com.bluebrim.paint.impl.shared.*;
import com.bluebrim.paint.shared.*;
import com.bluebrim.stroke.shared.*;
import com.bluebrim.swing.client.*;

/**
 * Page item stroke property panel.
 *
 * @author: Dennis
 */

public class CoPageItemStrokePanel extends CoPageItemPropertyPanel
{
	public final static String STYLE 		= "CoPageItemStrokePanel.STYLE";
	public final static String THICKNESS 	= "CoPageItemStrokePanel.THICKNESS";
	public final static String ALIGNMENT 	= "CoPageItemStrokePanel.ALIGNMENT";
	public final static String ALIGN_INSIDE = "CoPageItemStrokePanel.ALIGN_INSIDE";
	public final static String ALIGN_CENTER = "CoPageItemStrokePanel.ALIGN_CENTER";
	public final static String ALIGN_OUTSIDE = "CoPageItemStrokePanel.ALIGN_OUTSIDE";
	public final static String FOREGROUND 	= "CoPageItemStrokePanel.FOREGROUND";
	public final static String BACKGROUND	= "CoPageItemStrokePanel.BACKGROUND";
	public final static String STROKE_EFFECTIVE_SHAPE	= "CoPageItemStrokePanel.STROKE_EFFECTIVE_SHAPE";


	public CoOptionMenu m_styleOptionMenu;
	public CoTextField m_widthTextField;
	public CoOptionMenu m_aligmentOptionMenu;
	public CoOptionMenu m_foregroundColorOptionMenu;
	public CoTextField m_foregroundShadeTextField;
	public CoOptionMenu m_backgroundColorOptionMenu;
	public CoTextField m_backgroundShadeTextField;
	public CoCheckBox m_strokeEffectiveShapeCheckBox;


	public CoOptionMenu m_symmetryOptionMenu;
	public final static String SYMMETRY = "CoPageItemStrokePanel.SYMMETRY";


public void doUpdate()
{
	CoImmutableStrokePropertiesIF str = m_domain.getStrokeProperties();
	
	m_styleOptionMenu.setSelectedItem( str.getStroke() );
	m_widthTextField.setText( CoLengthUnitSet.format( str.getWidth(), CoLengthUnit.LENGTH_UNITS ) );
	m_aligmentOptionMenu.setSelectedItem( new Integer( str.getAlignment() ) );
	m_foregroundColorOptionMenu.setSelectedItem(str.getForegroundColor());
	m_foregroundShadeTextField.setText( Float.toString( str.getForegroundShade() ) );//+ " %" );
	m_backgroundColorOptionMenu.setSelectedItem( str.getBackgroundColor() );
	m_backgroundShadeTextField.setText( Float.toString( str.getBackgroundShade() ) );//+ " %" );
	m_strokeEffectiveShapeCheckBox.setSelected( m_domain.getStrokeEffectiveShape() );
	m_symmetryOptionMenu.setSelectedItem( str.getSymmetry() );
}
public void setContext( CoPageItemEditorContextIF context )
{
	// colors
	{
		m_foregroundColorOptionMenu.setQuiet( true );
		m_backgroundColorOptionMenu.setQuiet( true );
	
		if ( m_foregroundColorOptionMenu.getItemCount() > 0 ) m_foregroundColorOptionMenu.removeAllItems();
		if ( m_backgroundColorOptionMenu.getItemCount() > 0 ) m_backgroundColorOptionMenu.removeAllItems();

		CoColorIF c = (CoColorIF) CoFactoryManager.createObject( CoNoColorIF.NO_COLOR );
		m_foregroundColorOptionMenu.addItem( c );
		m_backgroundColorOptionMenu.addItem( c );

		int I = 1;
		CoColorCollectionIF colors = ( context == null ) ? null : context.getPreferences();
		
		if
			( colors != null )
		{
			Iterator i = colors.getColors().iterator();
			while
				( i.hasNext() )
			{
				c = (CoColorIF) i.next();
				if
					( ! c.getFactoryKey().equals( CoNoColorIF.NO_COLOR ) )
				{
					m_foregroundColorOptionMenu.addItem( c );
					m_backgroundColorOptionMenu.addItem( c );
					I++;
				}
			}
		}

//		m_foregroundColorOptionMenu.setMaximumRowCount( I );
//		m_backgroundColorOptionMenu.setMaximumRowCount( I );
		

		m_foregroundColorOptionMenu.setQuiet( false );
		m_backgroundColorOptionMenu.setQuiet( false );
	}


	// strokes
	{
		m_styleOptionMenu.setQuiet( true );

		if ( m_styleOptionMenu.getItemCount() > 0 ) m_styleOptionMenu.removeAllItems();

		CoStrokeCollectionIF ss = ( context == null ) ? null : context.getPreferences();

		if 
			( ss != null)
		{
			Iterator i = ss.getStrokes().iterator();
			while
				( i.hasNext() )
			{
				m_styleOptionMenu.addItem( new CoStrokeComboBoxItem( (CoStrokeIF) i.next() ) );
			}
		}

//		m_styleOptionMenu.setMaximumRowCount( m_styleOptionMenu.getItemCount() );

		m_styleOptionMenu.setQuiet( false );
	}
}

public CoPageItemStrokePanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, new CoColumnLayout(), commandExecutor );
}

protected void create( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	m_strokeEffectiveShapeCheckBox = b.createCheckBox( CoPageItemUIStringResources.getName( STROKE_EFFECTIVE_SHAPE ), null );
	add( m_strokeEffectiveShapeCheckBox );

	CoPanel p0 = b.createPanel( createFormLayout() );
	p0.setBorder( BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );
	
	add( p0 );
	{
		p0.add( b.createLabel( CoPageItemUIStringResources.getName( STYLE ) ) );
		p0.add( m_styleOptionMenu = b.createOptionMenu() );
		m_styleOptionMenu.setRenderer( new CoStrokeComboBoxItem.OptionMenuRenderer() );
		m_styleOptionMenu.setModel( new CoStrokeComboBoxItem.ComboBoxModel() );
		
		p0.add( b.createLabel( CoPageItemUIStringResources.getName( THICKNESS ) ) );
		p0.add( m_widthTextField = b.createSlimTextField() );
		m_widthTextField.setHorizontalAlignment( CoTextField.RIGHT );

		
		p0.add( b.createLabel( CoPageItemUIStringResources.getName( ALIGNMENT ) ) );
		p0.add( m_aligmentOptionMenu = b.createOptionMenu() );
		m_aligmentOptionMenu.addItem( new Integer( CoStrokePropertiesIF.ALIGN_INSIDE ) );
		m_aligmentOptionMenu.addItem( new Integer( CoStrokePropertiesIF.ALIGN_CENTER ) );
		m_aligmentOptionMenu.addItem( new Integer( CoStrokePropertiesIF.ALIGN_OUTSIDE ) );
		m_aligmentOptionMenu.setRenderer(
			new CoOptionMenu.DefaultRenderer()
			{
				public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
				{
					if
						( ( value != null ) && ( value instanceof Integer ) )
					{
						int i = ( (Integer) value ).intValue();
						switch
							( i )
						{
							case CoStrokePropertiesIF.ALIGN_INSIDE :
								value = CoPageItemUIStringResources.getName( ALIGN_INSIDE );
								break;
								
							case CoStrokePropertiesIF.ALIGN_OUTSIDE :
								value = CoPageItemUIStringResources.getName( ALIGN_OUTSIDE );
								break;
								
							case CoStrokePropertiesIF.ALIGN_CENTER :
								value = CoPageItemUIStringResources.getName( ALIGN_CENTER );
								break;
						}
					}

					return super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
				}
			}
		);



		p0.add( b.createLabel( CoPageItemUIStringResources.getName( SYMMETRY ) ) );
		p0.add( m_symmetryOptionMenu = b.createOptionMenu() );
		m_symmetryOptionMenu.addItem( CoStrokePropertiesIF.NON_SYMMETRIC );
		m_symmetryOptionMenu.addItem( CoStrokePropertiesIF.SYMMETRIC_BY_STRETCHING_CORNERS );
		m_symmetryOptionMenu.addItem( CoStrokePropertiesIF.SYMMETRIC_BY_STRETCHING_DASH);
		m_symmetryOptionMenu.addItem( CoStrokePropertiesIF.SYMEETRIC_BY_STRETCHING_DASH_GP);
		m_symmetryOptionMenu.setRenderer( new CoOptionMenu.TranslatingRenderer( CoPageItemUIStringResources.getBundle() ) );

	}

	CoColorPanel pcp = new CoColorPanel( b );
	m_foregroundColorOptionMenu = pcp.getColorOptionMenu();
	m_foregroundShadeTextField = pcp.getShadeTextField();
	p0.add( b.createLabel( CoPageItemUIStringResources.getName( FOREGROUND ) ) );
	p0.add( pcp );
	
	pcp = new CoColorPanel( b );
	m_backgroundColorOptionMenu = pcp.getColorOptionMenu();
	m_backgroundShadeTextField = pcp.getShadeTextField();
	p0.add( b.createLabel( CoPageItemUIStringResources.getName( BACKGROUND ) ) );
	p0.add( pcp );



	

	m_styleOptionMenu.addActionListener(
		new OptionMenuCommandAdapter( commandExecutor, CoPageItemCommands.SET_STROKE, null )
		{
			protected void prepare()
			{
				( (CoShapePageItemSetObjectCommand) m_command ).prepare( m_domain, ( (CoStrokeComboBoxItem) m_value ).getStroke() );
			}
		}
	);

	m_symmetryOptionMenu.addActionListener( new OptionMenuCommandAdapter( commandExecutor, CoPageItemCommands.SET_STROKE_SYMMETRY, null ) );
	m_widthTextField.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_STROKE_WIDTH ) );
	m_aligmentOptionMenu.addActionListener( new OptionMenuCommandAdapter( commandExecutor, CoPageItemCommands.SET_STROKE_ALIGNMENT, null )	);
	m_foregroundColorOptionMenu.addActionListener( new OptionMenuCommandAdapter( commandExecutor, CoPageItemCommands.SET_STROKE_FOREGROUND_COLOR, null ) );
	m_foregroundShadeTextField.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_STROKE_FOREGROUND_SHADE, false, 1, 0 ) );
	m_backgroundColorOptionMenu.addActionListener( new OptionMenuCommandAdapter( commandExecutor, CoPageItemCommands.SET_STROKE_BACKGROUND_COLOR, null ) );
	m_backgroundShadeTextField.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_STROKE_BACKGROUND_SHADE, false, 1, 0 ) );
	m_strokeEffectiveShapeCheckBox.addActionListener( new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_STROKE_EFFECTIVE_SHAPE ) );

}
}