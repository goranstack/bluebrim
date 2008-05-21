package com.bluebrim.text.impl.server;

import java.io.*;
import java.util.*;

import javax.swing.text.*;

import com.bluebrim.font.shared.*;
import com.bluebrim.system.shared.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 * @author Göran Stäck 2002-04-25
 */
public class CoTextServerImpl implements CoTextServer 
{
	private CoStyledTextPreferencesIF m_defaultStyledTextPreferences;
	private CoTextMeasurementPrefsIF m_textMeasurementPrefs;
	private CoHyphenationPatternCollectionIF m_hyphenationPatterns = new CoHyphenationPatternCollection();

	
	public void setDefaultStyledTextPreferences(CoStyledTextPreferencesIF styledTextPreferences)
    {
        m_defaultStyledTextPreferences = styledTextPreferences;
		createHyphenationRules(m_defaultStyledTextPreferences);
		createStyleSheets(m_defaultStyledTextPreferences.getTypographyRule());
		createTagChains(m_defaultStyledTextPreferences);
   }
	
	public CoTextContentIF createTextContent( CoFormattedText text) {
		return new CoTextContent( text);
	}

	public CoTextContentIF createTextContent( String name, String writer, String taggedText, int id, String idAlgorithm) {

		CoTextContentIF text = new CoTextContent( new CoGOI(new CoSpecificContext(idAlgorithm, "anySystem", "com.bluebrim.text.impl.server.CoTextContent"), id));
		text.setFormattedText(createFormattedText(taggedText));
		text.setName(name);
		text.setWriter(writer);
		return text;
	}

	/**
	 * Will replace the deprecated <code>CoTextImporter</code> as soon I have
	 * the opportunity to migrate current home brewed textformat to XML.
	 * PENDING: Throw a specific exception instead of returning null when something goes wrong.
	 */
	public CoFormattedText createFormattedText( String taggedText) {
		CoTextImporter importer = new CoTextImporter();
		try {
			return new CoFormattedText(importer.doImport(new StringReader(taggedText), null));
		} catch (Exception e) {
			return null;
		}

	}
	
	/**
	 * Return text measurement prefs for the current user.
	 * PENDING: When the user domain is redesigned and have a lightweight user interface
	 * we will return a <code>CoTextMeasurementPrefsIF</code> that associated with
	 * the current user.
	 */
	public CoTextMeasurementPrefsIF getTextMeasurementPrefs() {
		if (m_textMeasurementPrefs == null) 
			m_textMeasurementPrefs = new CoTextMeasurementPrefs();
		return m_textMeasurementPrefs;
	}

	public CoHyphenationPatternCollectionIF getHyphenationPatterns() {
		return m_hyphenationPatterns;
	}


	public CoStyledTextPreferencesIF getDefaultStyledTextPreferences() {
		return m_defaultStyledTextPreferences;
	}
	
	private final void createHyphenationRules(CoStyledTextPreferencesIF textPrefs) {
		CoHyphenation hyphenation = new CoHyphenation("Liang 2 2 4");
		CoLiangLineBreaker liangBreaker = CoLiangLineBreaker.create();
		liangBreaker.setMinimumPrefixLength(2);
		liangBreaker.setMinimumSuffixLength(2);
		liangBreaker.setMinimumWordLength(4);
		hyphenation.setLineBreaker(liangBreaker);
		textPrefs.addHyphenation(hyphenation);
	}

	private final void createTagChains(CoStyledTextPreferencesIF textPrefs) {

		List tagList = new ArrayList();
		tagList.add("Fotobyline");
		tagList.add("Bildtext");
		CoTagChainIF chain = textPrefs.createChain("Bildtext");
		chain.setChain(tagList);
		textPrefs.addChain(chain);

		tagList = new ArrayList();
		tagList.add("Etikett");
		tagList.add("Rubrik");
		tagList.add("Dateringstext");
		tagList.add("Ingresstext");
		tagList.add("Ingressindrag");
		tagList.add("Text");
		tagList.add("Mellanrubrik");
		tagList.add("Text");
		chain = textPrefs.createChain("Artikel");
		chain.setChain(tagList);
		textPrefs.addChain(chain);

		tagList = new ArrayList();
		tagList.add("Fotobyline");
		tagList.add("Bildtext");
		CoTagGroupIF group = textPrefs.createTagGroup("Bildtext");
		group.setTags(tagList);
		//	p.addTagGroup( g );

		tagList = new ArrayList();
		tagList.add("Etikett");
		tagList.add("Rubrik");
		group = textPrefs.createTagGroup("Rubrik");
		group.setTags(tagList);
		textPrefs.addTagGroup(group);

		tagList = new ArrayList();
		tagList.add("Dateringstext");
		tagList.add("Ingresstext");
		tagList.add("Ingressindrag");
		tagList.add("Text");
		tagList.add("Mellanrubrik");
		tagList.add("Signatur");
		group = textPrefs.createTagGroup("Artikel");
		group.setTags(tagList);
		textPrefs.addTagGroup(group);

		/*
			as = createParagraphTypographyRule( "Bildtext", styleRule );
			as = createParagraphTypographyRule( "Text", styleRule );	
			as = createParagraphTypographyRule( "Dateringstext", styleRule );
			as = createParagraphTypographyRule( "Fotobyline", styleRule );
			as = createParagraphTypographyRule( "Etikett", styleRule );
			as = createParagraphTypographyRule( "Ingresstext", styleRule );	// OK
			as = createParagraphTypographyRule( "Ingressindrag", styleRule );	// OK
			as = createParagraphTypographyRule( "Kortisrubrik", styleRule );	
			as = createParagraphTypographyRule( "Kortistext", styleRule );
			as = createParagraphTypographyRule( "Mellanrubrik", styleRule );
			as = createParagraphTypographyRule( "Rubrik", styleRule );
			as = createParagraphTypographyRule( "Signatur", styleRule );	
			as = createParagraphTypographyRule( "Faktarubrik", styleRule );
			as = createParagraphTypographyRule( "Faktatext", styleRule );
			as = createParagraphTypographyRule( "Idagrubrik", styleRule );
		*/
	}

	
	private final CoTypographyRuleIF createStyleSheets(CoTypographyRuleIF styleRule) {
		if (styleRule == null)
			styleRule = new CoTypographyRule();

		styleRule.clear();

		MutableAttributeSet as = null;

		as = createParagraphTypographyRule("Bildtext", styleRule);
		CoStyleConstants.setFontFamily(as, "Futura Book");
		CoStyleConstants.setFontSize(as, new Float(8));
		CoStyleConstants.setWeight(as, CoFontAttribute.BOLD);
		CoStyleConstants.setLeading(as, new CoLeading(CoLeading.OFFSET, 1));

		as = createParagraphTypographyRule("Text", styleRule);
		CoStyleConstants.setFontFamily(as, "Concorde BE");
		CoStyleConstants.setFontSize(as, new Float(8.5));
		CoStyleConstants.setFirstLineIndent(as, new Float(3 / 25.4 * 72));
		CoStyleConstants.setLeading(as, new CoLeading(CoLeading.OFFSET, 1));
		CoStyleConstants.setAlignment(as, CoTextConstants.ALIGN_JUSTIFIED);
		CoStyleConstants.setAdjustToBaseLineGrid(as, Boolean.TRUE);
		CoStyleConstants.setHyphenation(as, "Liang 2 2 4");

		as = createParagraphTypographyRule("Dateringstext", styleRule);
		CoStyleConstants.setFontFamily(as, "Futura Book");
		CoStyleConstants.setFontSize(as, new Float(8));
		CoStyleConstants.setWeight(as, CoFontAttribute.BOLD);
		CoStyleConstants.setLeading(as, new CoLeading(CoLeading.OFFSET, 1));
		CoStyleConstants.setAllCaps(as, Boolean.TRUE);
		CoStyleConstants.setAdjustToBaseLineGrid(as, Boolean.TRUE);

		as = createParagraphTypographyRule("Fotobyline", styleRule);
		CoStyleConstants.setFontFamily(as, "Futura Medium");
		CoStyleConstants.setFontSize(as, new Float(7));
		CoStyleConstants.setLeading(as, new CoLeading(CoLeading.OFFSET, 2));
		CoStyleConstants.setAlignment(as, CoTextConstants.ALIGN_RIGHT);

		as = createParagraphTypographyRule("Etikett", styleRule);
		CoStyleConstants.setFontFamily(as, "Futura Book");
		CoStyleConstants.setFontSize(as, new Float(18));
		CoStyleConstants.setLeading(as, new CoLeading(CoLeading.OFFSET, 1));
		CoStyleConstants.setSpaceBelow(as, new Float(1.5 / 25.4 * 72));

		as = createParagraphTypographyRule("Ingresstext", styleRule); // OK
		CoStyleConstants.setFontFamily(as, "Futura Book");
		CoStyleConstants.setFontSize(as, new Float(9.5));
		CoStyleConstants.setWeight(as, CoFontAttribute.BOLD);
		CoStyleConstants.setLeading(as, new CoLeading(CoLeading.OFFSET, 1));
		CoStyleConstants.setSpaceBelow(as, new Float(1 / 25.4 * 72));
		CoStyleConstants.setHyphenation(as, "Liang 2 2 4");

		as = createParagraphTypographyRule("Ingressindrag", styleRule); // OK
		CoStyleConstants.setFontFamily(as, "Futura Book");
		CoStyleConstants.setFontSize(as, new Float(9.5));
		CoStyleConstants.setWeight(as, CoFontAttribute.BOLD);
		CoStyleConstants.setLeading(as, new CoLeading(CoLeading.OFFSET, 1));
		CoStyleConstants.setSpaceBelow(as, new Float(1 / 25.4 * 72));
		CoStyleConstants.setFirstLineIndent(as, new Float(9));
		CoStyleConstants.setHyphenation(as, "Liang 2 2 4");

		as = createParagraphTypographyRule("Kortisrubrik", styleRule);
		CoStyleConstants.setFontFamily(as, "Futura Book");
		CoStyleConstants.setFontSize(as, new Float(9.5));
		CoStyleConstants.setWeight(as, CoFontAttribute.BOLD);
		CoStyleConstants.setLeading(as, new CoLeading(CoLeading.OFFSET, 1));

		as = createParagraphTypographyRule("Kortistext", styleRule);
		CoStyleConstants.setFontFamily(as, "Concorde BE");
		CoStyleConstants.setFontSize(as, new Float(8.5));
		CoStyleConstants.setFirstLineIndent(as, new Float(3 / 25.4 * 72));
		CoStyleConstants.setLeading(as, new CoLeading(CoLeading.OFFSET, 1));
		CoStyleConstants.setHyphenation(as, "Liang 2 2 4");

		as = createParagraphTypographyRule("Mellanrubrik", styleRule);
		CoStyleConstants.setFontFamily(as, "Futura Book");
		CoStyleConstants.setFontSize(as, new Float(9.5));
		CoStyleConstants.setWeight(as, CoFontAttribute.BOLD);
		CoStyleConstants.setLeading(as, new CoLeading(CoLeading.OFFSET, 1));
		CoStyleConstants.setSpaceAbove(as, new Float(2 / 25.4 * 72));
		CoStyleConstants.setSpaceBelow(as, new Float(0.5 / 25.4 * 72));

		as = createParagraphTypographyRule("Rubrik", styleRule);
		CoStyleConstants.setFontFamily(as, "Concorde BE");
		CoStyleConstants.setFontSize(as, new Float(48));
		CoStyleConstants.setLeading(as, new CoLeading(CoLeading.OFFSET, 1));

		as = createParagraphTypographyRule("Signatur", styleRule);
		CoStyleConstants.setFontFamily(as, "Futura Book");
		CoStyleConstants.setFontSize(as, new Float(8));
		CoStyleConstants.setWeight(as, CoFontAttribute.BOLD);
		CoStyleConstants.setLeading(as, new CoLeading(CoLeading.OFFSET, 1));
		CoStyleConstants.setAllCaps(as, Boolean.TRUE);
		CoStyleConstants.setAdjustToBaseLineGrid(as, Boolean.TRUE);
		CoStyleConstants.setAlignment(as, CoTextConstants.ALIGN_RIGHT);

		as = createParagraphTypographyRule("Faktarubrik", styleRule);
		CoStyleConstants.setFontFamily(as, "ITC Franklin Gothic");
		CoStyleConstants.setFontSize(as, new Float(12));
		CoStyleConstants.setWeight(as, CoFontAttribute.BOLD);
		CoStyleConstants.setLeading(as, new CoLeading(CoLeading.OFFSET, 1));

		as = createParagraphTypographyRule("Faktatext", styleRule);
		CoStyleConstants.setFontFamily(as, "ITC Franklin Gothic");
		CoStyleConstants.setFontSize(as, new Float(8));
		CoStyleConstants.setLeading(as, new CoLeading(CoLeading.OFFSET, 1));
		CoStyleConstants.setHyphenation(as, "Liang 2 2 4");

		as = createParagraphTypographyRule("Idagrubrik", styleRule);
		CoStyleConstants.setFontFamily(as, "ITC Franklin Gothic");
		CoStyleConstants.setWeight(as, CoFontAttribute.BOLD);
		CoStyleConstants.setFontSize(as, new Float(8));
		CoStyleConstants.setLeading(as, new CoLeading(CoLeading.OFFSET, 1));
		CoStyleConstants.setSpaceAbove(as, new Float(1 / 25.4 * 72));

		return styleRule;
	}

	private final MutableAttributeSet createParagraphTypographyRule(String name, CoTypographyRuleIF owner) {
		MutableAttributeSet as = (MutableAttributeSet) owner.addParagraphStyle(name).getAttributes();
		as.addAttribute(CoTextConstants.PARAGRAPH_TAG, name);
		return as;
	}
			
}
