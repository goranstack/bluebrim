package com.bluebrim.text.impl.shared;

import java.util.*;

/**
 * Interface defining the protocol of a collection of paragraph tag groups.
 * 
 * @author: Dennis Malmström
 */
 
public interface CoTagGroupCollectionIF extends com.bluebrim.base.shared.CoObjectIF, java.rmi.Remote
{
void addTagGroup( com.bluebrim.text.shared.CoTagGroupIF g );
com.bluebrim.text.shared.CoTagGroupIF createTagGroup( String name );
List getTagGroups(); // [ com.bluebrim.text.shared.CoTagGroupIF ]
void removeTagGroup( com.bluebrim.text.shared.CoTagGroupIF g );

int getImmutableGroupCount();
}