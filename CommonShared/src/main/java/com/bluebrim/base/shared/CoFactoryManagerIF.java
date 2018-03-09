package com.bluebrim.base.shared;

import java.util.*;

import com.bluebrim.collection.shared.*;

public interface CoFactoryManagerIF {
	public void add(String key, CoFactoryIF factory);

	public List getFactories(CoCollections.Selector selector);

	public CoFactoryIF getFactoryFor(String key);

	public Object getKeyFor(CoFactoryIF factory);


}