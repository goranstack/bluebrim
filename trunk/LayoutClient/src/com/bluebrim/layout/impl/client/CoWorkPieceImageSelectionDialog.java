package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.image.shared.*;

/**
 * Dialog for displaying a list of CoImageIFs for selection.
 *
 * @author: Dennis
 */
 
class CoWorkPieceImageSelectionDialog extends CoWorkPieceContentIndexSelectionDialog
{
public CoWorkPieceImageSelectionDialog( CoUserInterfaceBuilder b )
{
	super( b );
}
protected ListCellRenderer createRenderer()
{
	class Renderer extends JComponent implements ListCellRenderer
	{
		BufferedImage m_image;
		double m_scale;
		
		public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
		{
			m_image = (BufferedImage) value;
			m_scale = getImageHeight() / m_image.getHeight();
			setEnabled( list.isEnabled() );

			return this;
		}

		protected void paintComponent( Graphics g )
		{
			super.paintComponent( g );

			CoScreenPaintable p = CoScreenPaintable.wrap( g );
			p.drawBufferedImage(m_image, m_scale, m_scale);
			p.releaseDelegate();
		}

		public Dimension getPreferredSize()
		{
			return new Dimension( (int) ( m_image.getWidth() * m_scale ), getImageHeight() );
		}
		
	};

	return new Renderer();

}
private int getImageHeight()
{
	return 100;
}
protected int getMaxVisibleRowCount()
{
	return 5;
}
protected Object prepareElement( Object e )
{
	CoImageContentIF t = (CoImageContentIF) e;
	return t.getImage();
}
}