package com.bluebrim.layout.impl.client.editor;

import java.awt.*;
import java.awt.event.*;

import com.bluebrim.gemstone.client.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * Layout editor operation: Open a new layout editor with the layout of the current layout content as the model.
 * 
 * @author: Dennis
 */
 
public class CoEditLayout extends CoSpawnAction
{

	private static class TouchWhenClosing extends WindowAdapter
		{
			private CoLayoutContentIF m_layoutContent;
	
			public TouchWhenClosing( CoLayoutContentIF layoutContent )
			{
				m_layoutContent = layoutContent;
			}
			
			public void windowClosing( WindowEvent ev )
			{
				Window w = (Window) ev.getSource();
				w.removeWindowListener( this );
	
				CoCommand c =
					new CoCommand( "TOUCH LAYOUT" )
					{
						public boolean doExecute()
						{
							m_layoutContent.layoutsChanged();
							return true;
						}
					};
				
				CoTransactionUtilities.execute( c, null );
			}	
		}
	
	public CoEditLayout( String name, CoLayoutEditor e )
	{
		super( name, e );
	}

	public CoEditLayout( CoLayoutEditor e )
	{
		super( e );
	}

	public void actionPerformed(java.awt.event.ActionEvent arg1)
	{
		CoContentWrapperPageItemView v = m_editor.getCurrentContentWrapperView();
	
		if
			( v != null )
		{
			CoLayoutContentIF layoutContent = ( (CoPageItemLayoutContentIF) v.getContentView().getPageItem() ).getLayoutContent();
			CoLayoutEditor layoutEditor = doSpawn( (CoShapePageItemIF)layoutContent.getLayout(), m_editor.getDesktop(), null, null ); // shouldn't layout content have its own desktop ???
			Window window = layoutEditor.getWindow();
			
			window.addWindowListener( new TouchWhenClosing( layoutContent ) );
		}
	}
	
	public void prepare( CoShapePageItemView v )
	{
		super.prepare( v );
	
		if
		 	( check( v, CoContentWrapperPageItemView.class ) )
		{
			CoPageItemContentView cv = ( (CoContentWrapperPageItemView) v ).getContentView();
			if
				( check( cv, CoPageItemLayoutContentView.class ) )
			{
				CoPageItemLayoutContentView lcv = (CoPageItemLayoutContentView) cv;
				setEnabled( lcv.hasContent() );
			}
		}
	
	}
}