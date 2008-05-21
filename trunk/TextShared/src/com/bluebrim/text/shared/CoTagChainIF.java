package com.bluebrim.text.shared;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.browser.shared.*;

/**
 * Interface defining the protocol of a paragraph tag chain.
 * A paragaph tag chain is a sequence of paragraph tag names.
 * 
 * @author: Dennis Malmström
 */
 
public interface
	CoTagChainIF
extends
	CoObjectIF,
	CoCatalogElementIF,
	java.rmi.Remote,
	com.bluebrim.xml.shared.CoXmlEnabledIF
{
	String FACTORY_KEY = "TAG_CHAIN";
List getChain(); // [ String ], paragraph tag names
String getName();
void setChain( List tagNames /* [ String ] */ );
void setName( String name );
}