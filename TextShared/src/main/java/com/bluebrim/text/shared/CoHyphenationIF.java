package com.bluebrim.text.shared;
import java.rmi.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.browser.shared.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.transact.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * RMI-enabling interface for CoHyphenation
 * 
 * @author: Dennis Malmström
 */
public interface CoHyphenationIF extends CoObjectIF, Cloneable, CoCatalogElementIF, Remote, CoXmlEnabledIF {
	String FACTORY_KEY = "CoHyphenationIF";
	CoEnumValue getFallbackBehavior();
	CoLineBreakerIF getLineBreaker();
	String getLineBreakerKey();
	CoMutableLineBreakerIF getMutableLineBreaker();
	String getName();
	void setFallbackBehavior(CoEnumValue align);
	void setLineBreaker(String lineBreakerKey);
	void setLineBreaker(CoLineBreakerIF lb);
	void setName(String name);
	CoRef getId();
}