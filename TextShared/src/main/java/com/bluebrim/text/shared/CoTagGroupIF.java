package com.bluebrim.text.shared;
import java.util.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.browser.shared.*;

/**
 * Interface defining the protocol of a paragraph tag group.
 * A paragaph tag group is a set of paragraph tag names.
 * 
 * @author: Dennis Malmström
 */
 
public interface CoTagGroupIF extends CoObjectIF, CoCatalogElementIF, java.rmi.Remote, com.bluebrim.xml.shared.CoXmlEnabledIF
{
	String FACTORY_KEY = "TAG_GROUP";
String getName();
List getTags(); // [ String ], paragraph tag names
void setName( String name );
void setTags( List tagNames /* [ String ] */ );
}