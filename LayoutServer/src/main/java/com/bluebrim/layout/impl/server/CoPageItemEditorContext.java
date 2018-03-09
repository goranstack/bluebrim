package com.bluebrim.layout.impl.server;

import java.util.List;

import com.bluebrim.base.shared.debug.CoAssertion;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.CoPageItemEditorContextIF;
import com.bluebrim.layout.impl.shared.CoPageItemPreferencesIF;
import com.bluebrim.layout.impl.shared.CoPageItemPrototypeCollectionIF;
import com.bluebrim.layout.impl.shared.CoPageItemPrototypeTreeRootIF;
import com.bluebrim.layout.shared.CoLayoutParameters;

/**
 * Layout editor configuration data that can be changed during the lifespan of a layout editor.
 * 
 * @author: Dennis
 */

public class CoPageItemEditorContext implements CoPageItemEditorContextIF
{
	protected CoPageItemPreferencesIF m_preferences;
	protected CoPageItemPrototypeTreeRootIF m_prototypes; // [ CoPageItemPrototypeTreeRootIF ]
	protected List m_contentRecievers; // [ CoContentRecievers ]
	protected CoPageItemPrototypeCollectionIF m_tools;

	public CoLayoutParameters getLayoutParameters() {
		return m_preferences;
	}
	
	public CoPageItemPreferencesIF getPreferences()
	{
		return m_preferences;
	}
	public CoPageItemPrototypeTreeRootIF getPrototypes() // [ CoPageItemPrototypeTreeRootIF ]
	{
		return m_prototypes;
	}
	
	
	public CoPageItemEditorContext( CoPageItemPreferencesIF preferences,
									CoPageItemPrototypeCollectionIF tools,
		                              List contentRecievers, // [ CoContentRecievers ]
		                              CoPageItemPrototypeTreeRootIF prototypes )
	{
	//	CoAssertion.assert( pageSizes != null, "Attempt to create CoPageItemEditorContext with pageSizes == null" );
		CoAssertion.assertTrue( preferences != null, "Attempt to create CoPageItemEditorContext with preferences == null" );
	//	CoAssertion.assert( storage != null, "Attempt to create CoPageItemEditorContext with storage == null" );
	//	CoAssertion.assert( prototypes != null, "Attempt to create CoPageItemEditorContext with prototypes == null" );
		
		m_preferences	= preferences;
		m_contentRecievers = contentRecievers;
		m_prototypes = prototypes;
		m_tools = tools;
	}
	
	public List getContentRecievers() // [ CoContentReciever ]
	{
		return m_contentRecievers;
	}
	
	public CoPageItemPrototypeCollectionIF getTools()
	{
		return m_tools;
	}
}