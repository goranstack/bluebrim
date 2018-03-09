package com.bluebrim.layout.impl.client;

import java.awt.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.swing.client.*;

/**
 * Page item boxed line shape property panel.
 *
 * @author: Dennis
 */
 
public class CoPageItemBoxedLineShapePanel extends CoPageItemRectangularShapePanel
{
	public static final String MARGIN = "CoPageItemBoxedLineShapePanel.MARGIN";
	public static final String ORIENTATION = "CoPageItemBoxedLineShapePanel.ORIENTATION";

	private static final String HORIZONTAL = "HORIZONTAL";
	private static final String VERTICAL = "VERTICAL";
	
	protected CoTextField m_marginTextfield;
	protected CoToggleButton m_horizontalToggleButton;
	protected CoToggleButton m_verticalToggleButton;
	protected CoButtonGroup m_orientationButtonGroup;

	protected static final int m_iconSize = 14;
	
	protected static Icon m_horizontalIcon =
		new Icon()
		{
			public int getIconHeight() { return m_iconSize; }
			public int getIconWidth() { return m_iconSize; }
			public void paintIcon( Component c, Graphics g, int x, int y )
			{
				g.translate( x, y );
				g.setColor( c.getForeground() );
				g.drawLine( 0, m_iconSize / 2, m_iconSize, m_iconSize / 2 );
				g.translate( -x, -y );
			}
		};
		
	protected static Icon m_verticalIcon =
		new Icon()
		{
			public int getIconHeight() { return m_iconSize; }
			public int getIconWidth() { return m_iconSize; }
			public void paintIcon( Component c, Graphics g, int x, int y )
			{
				g.translate( x, y );
				g.setColor( c.getForeground() );
				g.drawLine( m_iconSize / 2, 0, m_iconSize / 2, m_iconSize );
				g.translate( -x, -y );
			}
		};





public void doUpdate()
{
	super.doUpdate();
	
	if ( m_domain == null ) return;
	if ( ! isVisible() ) return;
	
	CoImmutableShapeIF s = m_domain.getCoShape();

	if ( ! ( s instanceof CoImmutableBoxedLineIF ) ) return;

	CoImmutableBoxedLineIF l = (CoImmutableBoxedLineIF) s;
	
	m_marginTextfield.setText( CoLengthUnitSet.format( l.getMargin() ) );

	m_orientationButtonGroup.setSelected( l.isHorizontal() ? HORIZONTAL : VERTICAL );
}
public void postSetDomain()
{
	super.postSetDomain();
	
	if ( m_domain == null ) return;
	
	boolean isSlave = m_domain.isSlave();

	m_marginTextfield.setEnabled( ! isSlave );
	m_horizontalToggleButton.setEnabled( ! isSlave );
	m_verticalToggleButton.setEnabled( ! isSlave );
}

public CoPageItemBoxedLineShapePanel( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super( b, commandExecutor );
}

protected void create( CoUserInterfaceBuilder b, CoUndoableCommandExecutor commandExecutor )
{
	super.create( b, commandExecutor );

	add( b.createLabel( CoPageItemUIStringResources.getName( MARGIN ) ) );
	add( m_marginTextfield = b.createSlimTextField( CoTextField.RIGHT, 6 ) );

	add( b.createLabel( CoPageItemUIStringResources.getName( ORIENTATION ) ) );
	{
		CoPanel p = b.createPanel( new CoRowLayout() );
		add( p );

		m_orientationButtonGroup = b.createButtonGroup();
		
		p.add( m_horizontalToggleButton = b.createToggleButton( null, m_horizontalIcon, m_orientationButtonGroup, HORIZONTAL ) );
		p.add( m_verticalToggleButton = b.createToggleButton( null, m_verticalIcon, m_orientationButtonGroup, VERTICAL ) );
	}



	m_marginTextfield.addActionListener( new DoubleTextFieldCommandAdapter( commandExecutor, CoPageItemCommands.SET_BOXED_LINE_MARGIN ) );
	m_horizontalToggleButton.addActionListener( new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_BOXED_LINE_HORIZONTAL ) );
	
	m_verticalToggleButton.addActionListener(
		new BooleanButtonCommandAdapter( commandExecutor, CoPageItemCommands.SET_BOXED_LINE_HORIZONTAL )
		{
			protected boolean getCurrentValue()
			{
				return ! super.getCurrentValue();
			}
			
			protected void prepare()
			{
				( (CoShapePageItemSetBooleanCommand) m_command ).prepare( m_domain, ! m_value );
			}
		}
	);

}
}