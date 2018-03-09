package com.bluebrim.layout.impl.client.tools;

import java.awt.event.*;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Tool for rubberbanding selection
 * 
 * @author: Dennis Malmström
 */
 
public class CoRubberbandSelectTool extends CoRubberbandTool
{
public CoRubberbandSelectTool( CoLayoutEditor pageItemEditor, CoSelectionTool selectionTool )
{
	super( selectionTool, pageItemEditor );
}
protected CoTool doIt( MouseEvent ev, CoShapeIF area )
{
	area.normalize();

	double dx = area.getWidth();
	double dy = area.getHeight();
	
	if
		( ( dx * dx < m_moveTreshold ) && ( dy * dy < m_moveTreshold ) )
	{
		// filter out mouse trembling, interpret as click
		selectClickedView( getViewAt( getLocation( ev ), false ), ev );
	} else {
		
		m_viewPanel.getSelectionManager().beginSelectionTransaction();
		if
			( ( ev.getModifiers() & ev.SHIFT_MASK ) == 0 )
		{
			// clear current selection
			m_viewPanel.getSelectionManager().unselectAllViews();
		}

		// get views in selection area
		List V = m_viewPanel.getRootView().findViewsIn( (CoRectangleShapeIF) area, ( ev.getModifiers() & ev.CTRL_MASK ) != 0, false );

		// select views in selection area
		Iterator e = V.iterator();
		while
			( e.hasNext() )
		{
			CoShapePageItemView v = (CoShapePageItemView) e.next();
			if ( isSelectable( v ) ) m_viewPanel.getSelectionManager().select( v );
		}
		m_viewPanel.getSelectionManager().endSelectionTransaction();

		xorDraw();
//		m_viewPanel.repaint();
	}

	return m_previousTool;
}
}