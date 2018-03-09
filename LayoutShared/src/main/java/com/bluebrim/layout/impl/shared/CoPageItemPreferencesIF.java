package com.bluebrim.layout.impl.shared;

import java.util.*;

import com.bluebrim.layout.shared.*;
import com.bluebrim.page.shared.*;
import com.bluebrim.paint.shared.*;
import com.bluebrim.stroke.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-02-05 16:19:52)
 * @author: Dennis
 */

public interface
	CoPageItemPreferencesIF
extends
	CoStyledTextPreferencesIF, // parent

	CoLayoutParameters,
	
	// roles
	CoTypographyContextIF,

	// parts
	CoStrokeCollectionIF,
	CoFormattedTextHolderIF.Context,

	CoXmlEnabledIF
{
	CoDesktopLayoutAreaIF getDesktopLayoutArea();
		
	void buildTextStyleApplier( CoTextStyleApplier a );
	
	boolean doUseQxpJustification();
	
	CoContentWrapperPageItemIF getCaptionboxPrototype();
	
	CoContentWrapperPageItemIF getImageboxPrototype();
	
	float getKernAboveSize( boolean local );
	
	CoLayoutAreaIF getLayoutAreaPrototype();
	
	CoContentWrapperPageItemIF getTextboxPrototype();
			
	public CoColorIF getColor( String name ); // lookup color by name
	
	public List getColors(); // [ CoColorIF ], available colors
			
	CoPageSizeCollectionIF getPageSizeCollection();
}