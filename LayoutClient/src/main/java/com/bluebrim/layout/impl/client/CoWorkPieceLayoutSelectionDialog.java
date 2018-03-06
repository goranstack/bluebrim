package com.bluebrim.layout.impl.client;

import java.awt.*;

import javax.swing.*;

import com.bluebrim.gui.client.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;

/**
 * Dialog for displaying a list of CoLayoutContentIF's for selection.
 *
 * @author: Dennis
 */
 
class CoWorkPieceLayoutSelectionDialog extends CoWorkPieceContentIndexSelectionDialog
{
	protected ListCellRenderer createRenderer()
	{
		class Renderer extends CoShapePageItemViewPane implements ListCellRenderer
		{
			public Renderer()
			{
				super( null, new Dimension( 100, 100 ), null, true, true );
			}
			
			public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
			{
				setView( (CoPageItemView) value );
				setEnabled( list.isEnabled() );
	
				return this;
			}
		};
	
		return new Renderer();
	
	}
	
	public CoWorkPieceLayoutSelectionDialog( CoUserInterfaceBuilder b )
	{
		super( b );
	}

	protected int getMaxVisibleRowCount()
	{
		return 5;
	}

	protected Object prepareElement( Object e )
	{
		CoLayoutContentIF layoutContent = (CoLayoutContentIF) e;
		return layoutContent.getLayout().getView();
	}
}