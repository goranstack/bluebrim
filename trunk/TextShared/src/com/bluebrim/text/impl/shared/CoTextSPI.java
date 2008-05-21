package com.bluebrim.text.impl.shared;

import com.bluebrim.text.shared.CoFormattedText;
import com.bluebrim.text.shared.CoHyphenationPatternCollectionIF;
import com.bluebrim.text.shared.CoStyledTextPreferencesIF;
import com.bluebrim.text.shared.CoTextContentIF;
import com.bluebrim.text.shared.CoTextMeasurementPrefsIF;

/**
 * Service Provider Interface for Text domain implementors.
 * 
 * @author Göran Stäck 2002-04-25
 */

public interface CoTextSPI {

	public CoTextContentIF createTextContent( CoFormattedText text);

	public CoTextContentIF createTextContent( String name, String writer, String taggedText, int id, String idAlgorithm);

	public CoFormattedText createFormattedText(String taggedText);
	
	/**
	 * Return text measurement prefs for the current user
	 */
	public CoTextMeasurementPrefsIF getTextMeasurementPrefs();

	public CoHyphenationPatternCollectionIF getHyphenationPatterns();
	
	public CoStyledTextPreferencesIF getDefaultStyledTextPreferences();
	
	public void setDefaultStyledTextPreferences(CoStyledTextPreferencesIF styledTextPreferences);
}
