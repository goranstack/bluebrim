package com.bluebrim.text.shared;
import java.util.*;

import com.bluebrim.paint.shared.*;
import com.bluebrim.text.impl.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-02-05 15:17:18)
 * @author: Dennis
 */
 
public interface CoStyledTextPreferencesIF extends
	// parts
	CoColorCollectionIF,
	CoHyphenationCollectionIF,
	CoFontFamilyCollectionIF,
	CoTagGroupCollectionIF,
	CoTagChainCollectionIF//,

	// roles
	//com.bluebrim.text.shared.CoTypographyContextIF
{
	CoTextStyleApplier getTextStyleApplier();

	List getParagraphTagNames(); // [ String ]

	CoTypographyRuleIF getTypographyRule();
			
	CoTypographyContextIF getTypographyContext();
	
	Boolean getUseQxpJustification();
	
	void setKernAboveSize( float kas );
	
	void setUseQxpJustification( Boolean b );
	
	public List getFontFamilyNames(); // [ String ], available font family names
	
	public CoHyphenationIF getHyphenation( String name ); // lookup hyphenation by name
	
	public List getHyphenations(); // [ CoHyphenationIF ], available hyphenations
	
	List getTagChains(); // [ CoTagChainIF ]


}