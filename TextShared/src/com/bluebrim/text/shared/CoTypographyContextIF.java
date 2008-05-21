package com.bluebrim.text.shared;
import java.util.*;

/**
 * Interface for holding context needed by ui's that control text character attribute values
 *
 * @author: Dennis Malmström
 */

public interface CoTypographyContextIF
{
public List getFontFamilyNames(); // [ String ], available font family names
public com.bluebrim.text.shared.CoHyphenationIF getHyphenation( String name ); // lookup hyphenation by name
public List getHyphenations(); // [ com.bluebrim.text.shared.CoHyphenationIF ], available hyphenations


List getTagChains(); // [ com.bluebrim.text.shared.CoTagChainIF ]

public com.bluebrim.paint.shared.CoColorIF getColor( String name ); // lookup color by name

public List getColors(); // [ com.bluebrim.paint.shared.CoColorIF ], available colors
}