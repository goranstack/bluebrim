package com.bluebrim.stroke.impl.client;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.bluebrim.base.shared.geom.CoLine;
import com.bluebrim.gui.client.CoRowLayout;

//

public class CoStrokeListRenderer extends JPanel implements ListCellRenderer
{
	private com.bluebrim.stroke.impl.shared.CoStrokeProperties m_strokeProperties = new com.bluebrim.stroke.impl.shared.CoStrokeProperties();
	{
		m_strokeProperties.setWidth( 10 );
	}
	
	private com.bluebrim.stroke.impl.client.CoStrokePreview m_strokePreview = new com.bluebrim.stroke.impl.client.CoStrokePreview( new CoLine( 10, 10, 60, 10 ), new Dimension( 70, 20 ) )
	{
		public com.bluebrim.stroke.shared.CoStrokePropertiesIF getStrokeProperties()
		{
			return m_strokeProperties;
		}
	};
	private JLabel m_label;

	protected static Border noFocusBorder = new EmptyBorder(1,1,1,1);
public CoStrokeListRenderer()
{
	super();
	setLayout( new CoRowLayout( true ) );
	add(m_label = new JLabel());
	add(m_strokePreview);

//	m_label.setVerticalAlignment( m_label.CENTER );
	
}
public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
{
	if
		( isSelected )
	{
	    setBackground( list.getSelectionBackground() );
	    setForeground( list.getSelectionForeground() );
	} else {
	    setBackground( list.getBackground() );
	    setForeground( list.getForeground() );
	}

	boolean isStroke = value != null &&  value instanceof com.bluebrim.stroke.shared.CoStrokeIF ;
	
	com.bluebrim.stroke.shared.CoStrokeIF stroke = isStroke ? (com.bluebrim.stroke.shared.CoStrokeIF) value : null;
	
	m_label.setText( isStroke ? stroke.getName() : null );

	if
		( ! isMutable( stroke, index ) )
	{
		if
			( ( m_font == null ) || ! list.getFont().equals( m_font ) )
		{
			m_font = list.getFont();
			m_immutableFont = m_font.deriveFont( Font.ITALIC );
		}
		
		m_label.setFont( m_immutableFont );
	} else {
		m_label.setFont( list.getFont() );
	}

		
	m_strokeProperties.setStroke( stroke );

	validate();	
	setEnabled(list.isEnabled());
//	setFont(list.getFont());
	setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);

	return this;
}

	private Font m_font;
	private Font m_immutableFont;

protected boolean isMutable( com.bluebrim.stroke.shared.CoStrokeIF color, int index )
{
	return true;
}
}