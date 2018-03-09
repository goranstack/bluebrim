package com.bluebrim.layout.shared;

import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.layoutmanager.*;

/*
 */
public interface CoLayoutManagerFactoryIF extends CoFactoryIF {
	List getKeys();
	CoLayoutManagerIF getLayoutManager(String key);
	String getLayoutManagerPanelClassName(String key);
	String getLocalizedName(String key);
	CoLayoutManagerIF getNoLayoutManager();
}
