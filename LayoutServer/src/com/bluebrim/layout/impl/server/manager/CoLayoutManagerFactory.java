package com.bluebrim.layout.impl.server.manager;

import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.layoutmanager.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.layoutmanager.*;

public class CoLayoutManagerFactory implements CoLayoutManagerFactoryIF
{
	private CoLayoutManagerIF m_noLayoutManager = new CoNoLayoutManager();

	private CoLayoutManagerIF m_rowLayoutManager = new CoRowLayoutManager();
	private CoLayoutManagerIF m_columnLayoutManager = new CoColumnLayoutManager();
	private CoLayoutManagerIF m_reversedColumnLayoutManager = new CoReversedColumnLayoutManager();
	private CoLayoutManagerIF m_reversedRowLayoutManager = new CoReversedRowLayoutManager();

	private CoLayoutManagerIF m_exjobbLayoutManager = new CoExjobbLayoutManager();
	private CoLayoutManagerIF m_adPlacementLayoutManager = new CoAdPlacementLayoutManager();

	private CoLayoutManagerIF m_rectangleLayoutManager=new CoRectangleLayoutManager();
			
	private Map m_keyToInstanceMap = new HashMap();
	{
		m_keyToInstanceMap.put( m_noLayoutManager.getFactoryKey(), m_noLayoutManager );
		m_keyToInstanceMap.put( m_columnLayoutManager.getFactoryKey(), m_columnLayoutManager );
		m_keyToInstanceMap.put( m_reversedColumnLayoutManager.getFactoryKey(), m_reversedColumnLayoutManager );
		m_keyToInstanceMap.put( m_reversedRowLayoutManager.getFactoryKey(), m_reversedRowLayoutManager );

		m_keyToInstanceMap.put( m_exjobbLayoutManager.getFactoryKey(), m_exjobbLayoutManager );
		m_keyToInstanceMap.put( m_adPlacementLayoutManager.getFactoryKey(), m_adPlacementLayoutManager );
	
	}

	private Map m_keyToPrototypeMap = new HashMap();
	{
		m_keyToPrototypeMap.put( m_rowLayoutManager.getFactoryKey(), m_rowLayoutManager );
		m_keyToPrototypeMap.put( m_rectangleLayoutManager.getFactoryKey(), m_rectangleLayoutManager);
	}

	private List m_keys = new ArrayList();
	{
		m_keys.addAll( m_keyToInstanceMap.keySet() );
		m_keys.addAll( m_keyToPrototypeMap.keySet() );
		m_keys.remove( m_noLayoutManager.getFactoryKey() );
	}

public CoFactoryElementIF createObject()
{
	return null;
}
public java.util.List getKeys()
{
	return m_keys;
}
public CoLayoutManagerIF getLayoutManager( String key )
{
	CoLayoutManagerIF m = (CoLayoutManagerIF) m_keyToInstanceMap.get( key );

	if ( m != null ) return m;

	m = (CoLayoutManagerIF) m_keyToPrototypeMap.get( key );

	if ( m != null ) return (CoLayoutManagerIF) m.deepClone();

	return null;
}
public String getLayoutManagerPanelClassName( String key )
{
	CoLayoutManagerIF lm = (CoLayoutManagerIF) m_keyToPrototypeMap.get( key );

	if
		( lm == null )
	{
		return null;
	} else {
		return lm.getPanelClassName();
	}
	
}
public String getLocalizedName( String key )
{
	CoLayoutManagerIF lm = (CoLayoutManagerIF) m_keyToPrototypeMap.get( key );

	if
		( lm == null )
	{
		lm = (CoLayoutManagerIF) m_keyToInstanceMap.get( key );
	}
	
	if
		( lm == null )
	{
		return null;
	} else {
		return lm.getLocalizedName();
	}
	
}
public CoLayoutManagerIF getNoLayoutManager()
{
	return m_noLayoutManager;
}
}