package com.bluebrim.text.shared;
import java.rmi.*;
import java.util.*;

import com.bluebrim.base.shared.*;

/**
 * Protocol of a collection of custom hyphenation patterns.
 * 
 * @author: Dennis Malmström
 */

public interface CoHyphenationPatternCollectionIF extends CoObjectIF, Remote
{
com.bluebrim.text.shared.CoHyphenationPatternIF addPattern( String p );
com.bluebrim.text.shared.CoHyphenationPatternIF getPattern( int i );
int getPatternCount();
Collection getPatterns(); // [ String ]
int indexOfPattern( String w );
int indexOfPattern( com.bluebrim.text.shared.CoHyphenationPatternIF p );
void removePattern( int i );
void removePattern( String word );
void removePattern( com.bluebrim.text.shared.CoHyphenationPatternIF p );

void removeAllPatterns();
}