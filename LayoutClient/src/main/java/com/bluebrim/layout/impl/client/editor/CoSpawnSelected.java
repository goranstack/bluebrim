package com.bluebrim.layout.impl.client.editor;

//import se.corren.calvin.editorial.interfaces.*;
import java.util.*;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;

/**
 * Layout editor operation: Open a new layout editor with the selected page items as the model.
 * 
 * @author: Dennis
 */
 
public class CoSpawnSelected extends CoSpawnAction
{
public CoSpawnSelected( String name, CoLayoutEditor e )
{
	super( name, e );
}


public CoSpawnSelected( CoLayoutEditor e )
{
	super( e );
}


public void actionPerformed(java.awt.event.ActionEvent arg1)
{
	List models = new ArrayList();
	String name = null;
	CoLayoutEditorConfiguration app = null;
	
	Iterator i = m_editor.getWorkspace().getSelectionManager().getSelectedViews();
	while
		( i.hasNext() )
	{
		Object o = i.next();
		if
			( o instanceof CoCompositePageItemView )
		{
			CoCompositePageItemView v = (CoCompositePageItemView) o;
			CoCompositePageItemIF model = v.getCompositePageItem();
			if
				( app == null )
			{
				name = v.getName();
				app = m_editor.getConfiguration().getSpawnedConfiguration( model );
				models.add( new CoLayoutEditorModel( model, name ) );
			} else if
				( app == m_editor.getConfiguration().getSpawnedConfiguration( model ) )
			{
				name += ", " + v.getName();
				models.add( new CoLayoutEditorModel( model, name ) );
			}
		}
	}

	if
		( app != null )
	{
		doSpawn( models, m_editor.getDesktop(), name, app );
	}
}
}