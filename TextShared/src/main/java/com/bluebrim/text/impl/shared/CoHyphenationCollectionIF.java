package com.bluebrim.text.impl.shared;

import java.util.*;

import com.bluebrim.base.shared.*;

/**
 * Interface defining the protocol of a collection of com.bluebrim.text.shared.CoHyphenationIF.
 * 
 * @author: Dennis Malmström
 */

public interface CoHyphenationCollectionIF extends CoObjectIF, java.rmi.Remote
{
void addHyphenation( com.bluebrim.text.shared.CoHyphenationIF hs );
public com.bluebrim.text.shared.CoHyphenationIF createHyphenation();
com.bluebrim.text.shared.CoHyphenationIF getHyphenation( String name );
List getHyphenations(); // [ com.bluebrim.text.shared.CoHyphenationIF ]
void removeHyphenation( com.bluebrim.text.shared.CoHyphenationIF hs );

int getImmutableHyphenationCount();
}