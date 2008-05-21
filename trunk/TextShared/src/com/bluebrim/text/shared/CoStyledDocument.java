package com.bluebrim.text.shared;
import java.util.*;

import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;

import org.apache.crimson.tree.*;
import org.w3c.dom.*;

import com.bluebrim.base.shared.debug.*;
import com.bluebrim.font.shared.*;
import com.bluebrim.paint.shared.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.impl.shared.xml.*;
import com.bluebrim.xml.shared.*;

/**
 * Implementation of formatted text.
 *
 * @author: Dennis Malmström
 */

public class CoStyledDocument extends com.bluebrim.text.shared.swing.DefaultStyledDocument implements CoStyledDocumentIF, CoTextConstants, CoXmlEnabledIF {
	private static final String MASTER_TAG_PREFIX = "Master"; // prefix for master styles

	private List m_fontFamilies; // [ String ], available font families
	private Map m_nameToColorMap; // [ String -> CoColorIF ], available colors
	private Map m_nameToHyphenationMap; // [ String -> CoHyphenationIF ], available colors
	private List m_tagChains; // [ CoTagChainIF ], available tag chains

	private CoTagChainIF m_activeTagChain; // active tag chain
	private List m_chain; // [ String ], = m_tagChain.getChain()

	private List m_colors; // values of m_nameToColorMap
	private List m_hyphenations; // values of m_nameToHyphenationMap

	private int m_wordCount = -1;

	private transient CoImmutableStyledDocumentIF.FontManager m_fontManager; // overrides   font name -> Font   mapping

	private static Segment m_searchSegment = new Segment();
	public CoStyledDocument() {
		super();

		// create the "no tag" style.
		StyleContext c = (StyleContext) getAttributeContext();
		MutableAttributeSet as = c.addStyle(DEFAULT_TAG_NAME, c.getStyle(StyleContext.DEFAULT_STYLE));
	}
	// create and add a character tag

	public MutableAttributeSet addCharacterTag(String name) {
		String masterStyleName = MASTER_TAG_PREFIX + name;

		Style masterStyle = getStyle(masterStyleName);
		if (masterStyle == null) {
			masterStyle = addStyle(masterStyleName, getStyle(StyleContext.DEFAULT_STYLE));
			masterStyle.addAttribute(CHARACTER_TAG, name);
			addStyle(name, masterStyle);
		} else {
			Object tmp1 = masterStyle.getAttribute(CHARACTER_TAG);
			Object tmp2 = masterStyle.getAttribute(StyleConstants.ResolveAttribute);
			Object tmp3 = masterStyle.getAttribute(StyleConstants.NameAttribute);
			masterStyle.removeAttributes(masterStyle);
			masterStyle.addAttribute(StyleConstants.NameAttribute, tmp3);
			masterStyle.addAttribute(StyleConstants.ResolveAttribute, tmp2);
			masterStyle.addAttribute(CHARACTER_TAG, tmp1);
		}

		return masterStyle;
	}
	public void addFontFamily(String fontName) {
		m_fontFamilies.add(fontName);
	}
	// create and add a paragraph tag

	public MutableAttributeSet addParagraphTag(String name) {
		String masterStyleName = MASTER_TAG_PREFIX + name;

		Style masterStyle = getStyle(masterStyleName);
		if (masterStyle == null) {
			masterStyle = addStyle(masterStyleName, getStyle(StyleContext.DEFAULT_STYLE));
			masterStyle.addAttribute(PARAGRAPH_TAG, name);
			addStyle(name, masterStyle);
		} else {
			Object tmp1 = masterStyle.getAttribute(PARAGRAPH_TAG);
			Object tmp2 = masterStyle.getAttribute(StyleConstants.ResolveAttribute);
			Object tmp3 = masterStyle.getAttribute(StyleConstants.NameAttribute);
			masterStyle.removeAttributes(masterStyle);
			masterStyle.addAttribute(StyleConstants.NameAttribute, tmp3);
			masterStyle.addAttribute(StyleConstants.ResolveAttribute, tmp2);
			masterStyle.addAttribute(PARAGRAPH_TAG, tmp1);
		}

		return masterStyle;
	}
	// apply a character attribute set operation to a piece of text

	public void changeCharacterAttributes(int offset, int length, CoAttributeSetOperationIF op) {
		try {
			writeLock();
			DefaultDocumentEvent changes = new DefaultDocumentEvent(offset, length, DocumentEvent.EventType.CHANGE);

			// split elements that need it
			buffer.change(offset, length, changes);

			int lastEnd = Integer.MAX_VALUE;
			for (int pos = offset; pos < (offset + length); pos = lastEnd) {
				Element run = getCharacterElement(pos);
				lastEnd = run.getEndOffset();
				MutableAttributeSet attr = (MutableAttributeSet) run.getAttributes();
				op.apply(attr);
			}
			changes.end();
			fireChangedUpdate(changes);
			fireUndoableEditUpdate(new UndoableEditEvent(this, changes));
		} finally {
			writeUnlock();
		}
	}
	// apply a paragaph attribute set operation to a piece of text

	public void changeParagraphAttributes(int offset, int length, CoAttributeSetOperationIF op) {
		Element section = getDefaultRootElement();
		int index0 = section.getElementIndex(offset);
		int index1 = section.getElementIndex(offset + length);

		offset = section.getElement(index0).getStartOffset();
		length = section.getElement(index1).getEndOffset() - offset;

		try {
			writeLock();
			DefaultDocumentEvent changes = new DefaultDocumentEvent(offset, length, DocumentEvent.EventType.CHANGE);

			for (int i = index0; i <= index1; i++) {
				Element paragraph = section.getElement(i);
				MutableAttributeSet attr = (MutableAttributeSet) paragraph.getAttributes();
				op.apply(attr);
			}
			changes.end();
			fireChangedUpdate(changes);
			fireUndoableEditUpdate(new UndoableEditEvent(this, changes));
		} finally {
			writeUnlock();
		}
	}
	// clear all text

	public void clear() {
		try {
			remove(0, getLength());
			setParagraphAttributes(0, 0, new CoSimpleAttributeSet(), true);
		} catch (Exception e) {
		}
	}
	// make sure that as is not considered atomic

	public void clearAtomAttributes(MutableAttributeSet as) {
		clearMacroAttributesFrom(as);
	}
	private static void clearMacroAttributesFrom(MutableAttributeSet as) {
		if (as.isDefined(CoTextConstants.MACRO)) {
			as.removeAttribute(CoTextConstants.MACRO);
		}
	}
	private void countWords() {
		int charCount = getLength();

		m_wordCount = 0;

		if (charCount > 0) {
			Segment s = CoTextUtilities.TMP_Segment;
			try {
				getText(0, charCount - 1, s);
			} catch (BadLocationException ex) {
				CoAssertion.assertTrue(false, "BadLocationException");
			}

			for (int i = 1; i < s.count; i++) {
				if ((Character.isWhitespace(s.array[s.offset + i])) && (!Character.isWhitespace(s.array[s.offset + i - 1]))) {
					m_wordCount++;
				}
			}

			if ((s.count > 0) && !Character.isWhitespace(s.array[s.offset + s.count - 1]))
				m_wordCount++;
		}

	}
	// for debugging

	public void dump() {
		Element section = getRootElements()[0];
		int paragraphCount = section.getElementCount();
		for (int i = 0; i < paragraphCount; i++) {

			Element paragraph = section.getElement(i);
			AttributeSet paragraphAtts = paragraph.getAttributes();

			System.err.println("paragraph # " + i + " (" + paragraphAtts.hashCode() + ")");

			Enumeration names = paragraphAtts.getAttributeNames();
			while (names.hasMoreElements()) {
				Object o = names.nextElement();
				System.err.println("  " + o + " = " + paragraphAtts.getAttribute(o));
			}

			int runCount = paragraph.getElementCount();
			for (int n = 0; n < runCount; n++) {
				Element run = paragraph.getElement(n);
				AttributeSet runAtts = run.getAttributes();

				String text = "ERROR";
				try {
					text = getText(run.getStartOffset(), run.getEndOffset() - run.getStartOffset());
				} catch (Throwable ex) {
				}
				System.err.println(
					"  run # "
						+ n
						+ " ("
						+ runAtts.hashCode()
						+ ")"
						+ " [ "
						+ run.getStartOffset()
						+ "->"
						+ run.getEndOffset()
						+ " ] "
						+ " \""
						+ text
						+ "\"");

				Enumeration names2 = runAtts.getAttributeNames();
				while (names2.hasMoreElements()) {
					Object o = names2.nextElement();
					System.err.println("    " + o + " = " + runAtts.getAttribute(o));
				}
			}
		}
	}

	public CoTagChainIF getActiveChain() {
		return m_activeTagChain;
	}
	// Get values of a given set of attributes for a piece of the text.
	// If more than one value occurs for an attribute, return the value CoStyleConstants.AS_IS_VALUE for that attribute

	public AttributeSet getAttributes(int offset, int length, Object[] attributes) {
		MutableAttributeSet as = new CoSimpleAttributeSet();

		int p = offset + length;
		Element elem = getCharacterElement(p);

		if ((offset + length > 0) && (offset + length == elem.getStartOffset())) {
			elem = getCharacterElement(offset + length - 1);
		}

		AttributeSet as2 = elem.getAttributes();

		int I = attributes.length;
		for (int i = 0; i < I; i++) {
			Object a = attributes[i];
			Object v = as2.getAttribute(a);
			if (v != null)
				as.addAttribute(a, v);
		}

		p = elem.getStartOffset() - 1;
		while (p >= offset) {
			elem = getCharacterElement(p);
			if (elem.getName().equals(CoTextConstants.CommentElementName))
				continue;
			p = elem.getStartOffset() - 1;

			as2 = elem.getAttributes();

			for (int i = 0; i < I; i++) {
				Object a = attributes[i];
				Object v = as.getAttribute(a);
				Object v2 = as2.getAttribute(a);

				if (v == null) {
					if (v2 != null) {
						as.addAttribute(a, CoStyleConstants.AS_IS_VALUE);
					}
				} else {
					if ((v2 == null) || (!v2.equals(v))) {
						as.addAttribute(a, CoStyleConstants.AS_IS_VALUE);
					}
				}
			}
		}

		elem = getParagraphElement(offset + length);
		as2 = elem.getAttributes();

		I = attributes.length;
		for (int i = 0; i < I; i++) {
			Object a = attributes[i];
			Object v = as2.getAttribute(a);
			if (v != null)
				as.addAttribute(a, v);
		}

		p = elem.getStartOffset() - 1;
		while (p >= offset) {
			elem = getParagraphElement(p);
			p = elem.getStartOffset() - 1;

			as2 = elem.getAttributes();

			for (int i = 0; i < I; i++) {
				Object a = attributes[i];
				Object v = as.getAttribute(a);
				Object v2 = as2.getAttribute(a);

				if (v == null) {
					if (v2 != null) {
						as.addAttribute(a, CoStyleConstants.AS_IS_VALUE);
					}
				} else {
					if ((v2 == null) || (!v2.equals(v))) {
						as.addAttribute(a, CoStyleConstants.AS_IS_VALUE);
					}
				}
			}
		}

		//	System.err.println( "         " + as );

		as.removeAttribute(StyleConstants.ResolveAttribute);

		return as;
	}
	// Get the value of a given character attribute for a piece of the text.
	// If more than one value occurs, return null

	public Object getCharacterAttribute(int offset, int length, Object attribute) {
		Object value = null;

		Element elem = getCharacterElement(offset + length - 1);
		AttributeSet attr = elem.getAttributes();
		if (attr.isDefined(attribute)) {
			value = attr.getAttribute(attribute);
			int p = elem.getStartOffset() - 1;
			while (p >= offset) {
				elem = getCharacterElement(p);
				p = elem.getStartOffset() - 1;
				AttributeSet attr2 = elem.getAttributes();
				Object value2 = null;
				if (attr2.isDefined(attribute))
					value2 = attr2.getAttribute(attribute);
				if ((value2 != null) && (value2.equals(value)))
					continue;
				value = null;
				break;
			}
		}

		return value;
	}
	// Get the character attribute values for a piece of the text.
	// If more than one value occurs for an attribute, return the value CoStyleConstants.AS_IS_VALUE for that attribute

	public AttributeSet getCharacterAttributes(int offset, int length) {
		Element elem = getCharacterElement(offset + length);

		if ((offset + length > 0) && (offset + length == elem.getStartOffset())) {
			elem = getCharacterElement(offset + length - 1);
		}

		MutableAttributeSet as = new CoSimpleAttributeSet(elem.getAttributes());

		Object tag = as.getAttribute(CHARACTER_TAG);

		as.removeAttribute(StyleConstants.ResolveAttribute);

		int p = elem.getStartOffset() - 1;
		while (p >= offset) {
			elem = getCharacterElement(p);
			p = elem.getStartOffset() - 1;

			if (elem.getName().equals(CoTextConstants.CommentElementName))
				continue;

			AttributeSet as2 = elem.getAttributes();

			Object tag2 = as2.getAttribute(CHARACTER_TAG);
			if ((tag == null) && (tag2 != null)) {
				tag = CoStyleConstants.AS_IS_VALUE;
			} else if ((tag2 == null) && (tag != null)) {
				tag = CoStyleConstants.AS_IS_VALUE;
			} else if ((tag2 == null) && (tag == null)) {
			} else if (!tag.equals(tag2)) {
				tag = CoStyleConstants.AS_IS_VALUE;
			}

			Enumeration e = as.getAttributeNames();
			while (e.hasMoreElements()) {
				Object attribute = e.nextElement();
				if (as2.isDefined(attribute)) {
					if (as2.getAttribute(attribute).equals(as.getAttribute(attribute))) {
						continue;
					}
				}
				as.addAttribute(attribute, CoStyleConstants.AS_IS_VALUE);
			}

			e = as2.getAttributeNames();
			while (e.hasMoreElements()) {
				Object attribute = e.nextElement();
				if (as.isDefined(attribute)) {
					if (as.getAttribute(attribute).equals(as2.getAttribute(attribute))) {
						continue;
					}
				}
				as.addAttribute(attribute, CoStyleConstants.AS_IS_VALUE);
			}
		}

		if (tag != null)
			as.addAttribute(CHARACTER_TAG, tag);
		/*
			if
				( length > 0 )
			{
				Object tracking = getCharacterAttribute( offset, length - 1, CoTextConstants.TRACK_AMOUNT );
				if ( tracking != null ) as.addAttribute( CoTextConstants.TRACK_AMOUNT, tracking );
			}
		*/

		return as;
	}
	// return the names and keystrokes of all character tags

	public List getCharacterTagNameAndKeyStrokes() // [ Object[] ( String, KeyStroke ) ]
	{
		List v = new ArrayList();

		Enumeration styleNames = getStyleContext().getStyleNames();
		while (styleNames.hasMoreElements()) {
			String styleName = (String) styleNames.nextElement();

			// endast "master styles" är intressanta
			if (styleName.startsWith(MASTER_TAG_PREFIX)) {
				AttributeSet as = getStyle(styleName);
				if ((as.isDefined(CHARACTER_TAG)) && (!as.isDefined(IS_DELETED))) {
					Object keyStroke = CoStyleConstants.getKeyStroke(as); //as.getAttribute( KEY_STROKE );

					v.add(new Object[] { styleName.substring(MASTER_TAG_PREFIX.length()), keyStroke });
				}
			}
		}

		Collections.sort(v, m_nameAndKeyStrokeOrder);

		return v;
	}
	// return the names of all character tags

	public List getCharacterTagNames() // [ String ]
	{
		List v = new ArrayList();

		Enumeration styleNames = getStyleContext().getStyleNames();
		while (styleNames.hasMoreElements()) {
			String styleName = (String) styleNames.nextElement();

			// endast "master styles" är intressanta
			if (styleName.startsWith(MASTER_TAG_PREFIX)) {
				AttributeSet as = getStyle(styleName);
				if ((as.isDefined(CHARACTER_TAG)) && (!as.isDefined(IS_DELETED))) {
					v.add(styleName.substring(MASTER_TAG_PREFIX.length()));
				}
			}
		}

		Collections.sort(v, String.CASE_INSENSITIVE_ORDER);

		return v;
	}
	// extract a Font from an attribute set

	public CoFont getCoFont(AttributeSet attr) {
		if (m_fontManager != null) {
			// use font manager if present
			return m_fontManager.getCoFont(attr);
		} else {
			return CoViewStyleConstants.getCoFont(attr);
		}
	}
	// create a copy of the document

	public CoStyledDocumentIF getCopy() {
		CoStyledDocument doc = new CoStyledDocument();

		// copy styles
		Style defaultStyle = doc.getStyle(StyleContext.DEFAULT_STYLE);

		Enumeration styleNames = getStyleContext().getStyleNames();
		while (styleNames.hasMoreElements()) {
			String styleName = (String) styleNames.nextElement();
			if (!styleName.startsWith(CoStyledDocument.MASTER_TAG_PREFIX))
				continue;

			Style style = getStyle(styleName);

			Style s = doc.addStyle(styleName, defaultStyle);
			s.addAttributes(style);

			styleName = styleName.substring(CoStyledDocument.MASTER_TAG_PREFIX.length());
			doc.addStyle(styleName, s);
		}

		// copy text
		try {
			doc.insertString(0, getText(0, getLength()), null);
		} catch (Exception t) {
		}

		// copy format
		Element section = getRootElements()[0];

		int paragraphCount = section.getElementCount();
		for (int i = 0; i < paragraphCount; i++) {
			Element paragraph = section.getElement(i);
			AttributeSet paragraphAtts = paragraph.getAttributes();
			int offset = paragraph.getStartOffset();
			int length = paragraph.getEndOffset() - offset;

			doc.setParagraphAttributes(offset, length, paragraphAtts, true);

			String paragraphTag = (String) paragraphAtts.getAttribute(PARAGRAPH_TAG);
			if (paragraphTag == null)
				paragraphTag = DEFAULT_TAG_NAME;
			doc.setParagraphTag(offset, length, paragraphTag);

			int runCount = paragraph.getElementCount();
			for (int n = 0; n < runCount; n++) {
				Element run = paragraph.getElement(n);
				AttributeSet runAtts = run.getAttributes();
				offset = run.getStartOffset();
				length = run.getEndOffset() - offset;

				String characterTag = (String) runAtts.getAttribute(CHARACTER_TAG);
				if (characterTag == null)
					characterTag = DEFAULT_TAG_NAME;

				if ((i == paragraphCount - 1) && (n == runCount - 1)) {
					continue; // dont mess with the trailing element
				}

				doc.setCharacterAttributes(offset, length, runAtts, true);
				doc.setCharacterTag(offset, length, characterTag);
			}
		}

		// PENDING!! Kan vi verkligen vara säkra på att documentProperties bara är att klona!
		doc.setDocumentProperties((Dictionary) ((Hashtable) getDocumentProperties()).clone());

		doc.m_acceptedTags.addAll(m_acceptedTags);

		// copy colors
		doc.m_nameToColorMap = getNameToColorMap();
		doc.m_colors = getColors();
		doc.m_nameToHyphenationMap = getNameToHyphenationMap();
		doc.m_hyphenations = getHyphenations();

		// copy font families
		if (getFontFamilyNames() != null)
			doc.m_fontFamilies = new ArrayList(getFontFamilyNames());

		doc.m_kernAboveSize = m_kernAboveSize;
		doc.m_useQxpJustification = m_useQxpJustification;

		// copy chain
		doc.m_tagChains = getTagChains();
		doc.m_activeTagChain = getActiveChain();

		// copy macros
		doc.m_macros.clear();
		doc.m_macros.putAll(m_macros);

		// word count
		doc.m_wordCount = getWordCount();

		return doc;
	}
	// extract a Font from an attribute set

	public java.awt.Font getFont(AttributeSet attr) {
		if (m_fontManager != null) {
			// use font manager if present
			return m_fontManager.getFont(attr);
		} else {
			return CoViewStyleConstants.getFont(attr);
		}
	}
	public String getFontFamily(int index) {
		return (String) m_fontFamilies.get(index);
	}
	public List getFontFamilyNames() // [ String ]
	{
		return m_fontFamilies;
	}
	public CoImmutableStyledDocumentIF.FontManager getFontManager() {
		return m_fontManager;
	}
	public java.awt.Color getForeground(AttributeSet attr) {
		return (java.awt.Color) CoViewStyleConstants.getForegroundColor(attr, this);
	}

	public CoHyphenationIF getHyphenation(String name) {
		if (m_nameToHyphenationMap != null) 
			return (CoHyphenationIF) m_nameToHyphenationMap.get(name);
		 else 
			return null;
	}

	public List getHyphenations() // [ CoHyphenationIF ]
	{
		if (m_hyphenations == null) {
			m_hyphenations = new ArrayList();
			if (m_nameToHyphenationMap != null)
				m_hyphenations.addAll(m_nameToHyphenationMap.values());
		}

		return m_hyphenations;
	}

	public Map getNameToColorMap() {
		return m_nameToColorMap;
	}
	public Map getNameToHyphenationMap() {
		return m_nameToHyphenationMap;
	}
	// Get the value of a given paragraph attribute for a piece of the text.
	// If more than one value occurs, return null

	public Object getParagraphAttribute(int offset, int length, Object attribute) {
		Object value = null;

		Element elem = getParagraphElement(offset + length - 1);
		AttributeSet attr = elem.getAttributes();
		if (attr.isDefined(attribute)) {
			value = attr.getAttribute(attribute);
			int p = elem.getStartOffset() - 1;
			while (p >= offset) {
				elem = getParagraphElement(p);
				p = elem.getStartOffset() - 1;
				AttributeSet attr2 = elem.getAttributes();
				Object value2 = null;
				if (attr2.isDefined(attribute))
					value2 = attr2.getAttribute(attribute);
				if ((value2 != null) && (value2.equals(value)))
					continue;
				value = null;
				break;
			}
		}

		return value;
	}
	// Get the paragraph attribute values for a piece of the text.
	// If more than one value occurs for an attribute, return the value CoStyleConstants.AS_IS_VALUE for that attribute

	public AttributeSet getParagraphAttributes(int offset, int length) {
		Element elem = getParagraphElement(offset + length);
		MutableAttributeSet as = new CoSimpleAttributeSet(elem.getAttributes());

		Object tag = as.getAttribute(PARAGRAPH_TAG);

		as.removeAttribute(StyleConstants.ResolveAttribute);

		int p = elem.getStartOffset() - 1;
		while (p >= offset) {
			elem = getParagraphElement(p);
			p = elem.getStartOffset() - 1;

			AttributeSet as2 = elem.getAttributes();

			Object tag2 = as2.getAttribute(PARAGRAPH_TAG);
			if ((tag == null) && (tag2 != null)) {
				tag = CoStyleConstants.AS_IS_VALUE;
			} else if ((tag2 == null) && (tag != null)) {
				tag = CoStyleConstants.AS_IS_VALUE;
			} else if ((tag2 == null) && (tag == null)) {
			} else if (!tag.equals(tag2)) {
				tag = CoStyleConstants.AS_IS_VALUE;
			}

			Enumeration e = as.getAttributeNames();
			while (e.hasMoreElements()) {
				Object attribute = e.nextElement();
				if (attribute == StyleConstants.ResolveAttribute)
					continue;
				if (attribute.equals(StyleConstants.ResolveAttribute))
					System.err.println("gädda: missed comparison of resolve (in getParagraphAttributes)");
				if (attribute.equals(StyleConstants.ResolveAttribute))
					continue;

				if (as2.isDefined(attribute)) {
					if (as2.getAttribute(attribute).equals(as.getAttribute(attribute))) {
						continue;
					}
				}
				as.addAttribute(attribute, CoStyleConstants.AS_IS_VALUE);
			}

			e = as2.getAttributeNames();
			while (e.hasMoreElements()) {
				Object attribute = e.nextElement();
				if (attribute == StyleConstants.ResolveAttribute)
					continue;
				if (attribute.equals(StyleConstants.ResolveAttribute))
					System.err.println("gädda: missed comparison of resolve (in getParagraphAttributes)");
				if (attribute.equals(StyleConstants.ResolveAttribute))
					continue;
				if (as.isDefined(attribute)) {
					if (as.getAttribute(attribute).equals(as2.getAttribute(attribute))) {
						continue;
					}
				}
				as.addAttribute(attribute, CoStyleConstants.AS_IS_VALUE);
			}
		}

		if (tag != null)
			as.addAttribute(PARAGRAPH_TAG, tag);

		return as;

	}
	// return the names and keystrokes of all paragraph tags

	public List getParagraphTagNameAndKeyStrokes() // [ Object[] ( String, KeyStroke ) ]
	{
		List v = new ArrayList();

		Enumeration styleNames = getStyleContext().getStyleNames();
		while (styleNames.hasMoreElements()) {
			String styleName = (String) styleNames.nextElement();

			// only "master styles"
			if (styleName.startsWith(MASTER_TAG_PREFIX)) {
				AttributeSet as = getStyle(styleName);
				if ((as.isDefined(PARAGRAPH_TAG)) && (!as.isDefined(IS_DELETED))) {
					Object keyStroke = CoStyleConstants.getKeyStroke(as); //as.getAttribute( KEY_STROKE );
					v.add(new Object[] { styleName.substring(MASTER_TAG_PREFIX.length()), keyStroke });
				}
			}
		}

		Collections.sort(v, m_nameAndKeyStrokeOrder);

		return v;
	}
	// return the names of all paragraph tags

	public List getParagraphTagNames() // [ String ]
	{
		List v = new ArrayList();

		Enumeration styleNames = getStyleContext().getStyleNames();
		while (styleNames.hasMoreElements()) {
			String styleName = (String) styleNames.nextElement();

			// endast "master styles" är intressanta
			if (styleName.startsWith(MASTER_TAG_PREFIX)) {
				AttributeSet as = getStyle(styleName);
				if ((as.isDefined(PARAGRAPH_TAG)) && (!as.isDefined(IS_DELETED))) {
					v.add(styleName.substring(MASTER_TAG_PREFIX.length()));
				}
			}
		}

		Collections.sort(v, String.CASE_INSENSITIVE_ORDER);

		return v;
	}

	public StyleContext getStyleContext() {
		return (StyleContext) getAttributeContext();
	}
	// get the attributes if a tag

	public MutableAttributeSet getTag(String tagName) {
		return getStyleContext().getStyle(MASTER_TAG_PREFIX + tagName);
	}
	public List getTagChains() {
		return m_tagChains;
	}
	public List getTagNames() // [ String ]
	{
		List v = new ArrayList();

		Enumeration styleNames = getStyleContext().getStyleNames();
		while (styleNames.hasMoreElements()) {
			String styleName = (String) styleNames.nextElement();

			// only "master styles"
			if (styleName.startsWith(MASTER_TAG_PREFIX)) {
				v.add(styleName.substring(MASTER_TAG_PREFIX.length()));
			}
		}

		return v;
	}

	// extract all paragraph tags that are actualy used in the text

	public List getUsedParagraphTags() // [ String ]
	{
		List v = new ArrayList();

		Element section = getRootElements()[0];
		int I = section.getElementCount();
		for (int i = 0; i < I; i++) {
			Object tag = section.getElement(i).getAttributes().getAttribute(CoTextConstants.PARAGRAPH_TAG);

			if ((tag != null) && (!v.contains(tag))) {
				v.add(tag);
			}
		}

		return v;
	}
	public int getWordCount() {
		if (m_wordCount == -1) {
			countWords();
		}

		return m_wordCount;
	}
	// insert a text macro

	public void insertMacro(int offset, int length, String macro) {
		// remove selection
		if (length > 0) {
			try {
				remove(offset, length);
			} catch (Throwable t) {
				return;
			}
		}

		// insert text and tag it as a macro
		try {
			String text = getMacroDefinition(m_macros, macro);

			MutableAttributeSet as = new CoSimpleAttributeSet();
			as.addAttribute(CoTextConstants.MACRO, macro);

			insertString(offset, text, as);
		} catch (Throwable t) {
		}
	}
	// insert text

	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		if (str.equals(TAG_CHAIN_IGNORING_RETURN)) {
			super.insertString(offset, "\n", a);
		} else {
			super.insertString(offset, str, a);

			m_wordCount = -1; // invalidate word count cache

			if ((str == null) || (str.length() != 1) || (str.charAt(0) != '\n')) {
				return;
			}

			// "\n" inserted -> get next paragraph tag from the tag chain and apply it to new paragraph
			AttributeSet paragraphAtts = getParagraphElement(offset).getAttributes();
			if (paragraphAtts != null) {
				String tag = (String) paragraphAtts.getAttribute(CoTextConstants.PARAGRAPH_TAG);
				setParagraphTag(offset + 1, 0, nextTag(tag));
			}
		}
	}
	// macros are atomic

	public boolean isAtomic(Element e) {
		return isMacro(e);
	}
	private static boolean isMacro(Element e) {
		return (e.getAttributes().isDefined(CoTextConstants.MACRO));
	}
	private String nextTag(String currentTag) {
		// no tags in chain
		if ((m_chain == null) || (m_chain.size() == 0)) {
			if (currentTag == null) {
				return CoTextConstants.DEFAULT_TAG_NAME;
			} else {
				return currentTag;
			}
		}

		// if no current tag exists return the first tag in chain
		if ((currentTag == null) || (currentTag.equals(CoTextConstants.DEFAULT_TAG_NAME))) {
			return (String) m_chain.get(0);
		}

		// see if current tag exists in chain
		int i = 0;
		while (i < m_chain.size()) {
			if (m_chain.get(i++).equals(currentTag)) {
				if (i < m_chain.size()) {
					// return the tag following current tag in chain
					return (String) m_chain.get(i);
				} else {
					// repeat the last tag in chain
					return (String) m_chain.get(i - 1);
				}
			}
		}

		return currentTag;
	}
	// force document listener notification

	public void notifyChange() {
		stylesChanged();
	}
	public void remove(int offs, int len) throws BadLocationException {
		super.remove(offs, len);

		m_wordCount = -1; // invalidate word count cache
	}
	public int search(String key, int startAt, boolean forward, boolean caseSensitive, boolean word) {
		int N = key.length();

		try {
			getText(0, getLength(), m_searchSegment);
		} catch (BadLocationException ex) {
			return -1;
		}

		Segment segment = m_searchSegment;

		int i = forward ? startAt : startAt - N;

		outer : while (true) {
			if (i < 0)
				return -1;
			if (i >= segment.count)
				return -1;

			if (word) {
				if (((i > 0) && !Character.isWhitespace(segment.array[segment.offset + i - 1]))
					|| ((i + N < segment.count) && !Character.isWhitespace(segment.array[segment.offset + i + N]))) {
					i += forward ? 1 : -1;
					continue outer;
				}

			}

			for (int n = 0; n < N; n++) {
				char c0 = key.charAt(n);
				char c1 = segment.array[segment.offset + i + n];

				if ((caseSensitive && (c1 != c0)) || (Character.toLowerCase(c1) != Character.toLowerCase(c0))) {
					i += forward ? 1 : -1;
					continue outer;
				}
			}

			return i;
		}
	}
	public void setActiveChain(String name, int pos) {
		if (name != null) {
			Iterator i = m_tagChains.iterator();
			while (i.hasNext()) {
				CoTagChainIF c = (CoTagChainIF) i.next();
				if (c.getName().equals(name)) {
					setActiveChain(c, pos);
					return;
				}
			}
		}

		setActiveChain((CoTagChainIF) null, pos);
	}
	public void setActiveChain(CoTagChainIF chain, int pos) {
		m_activeTagChain = chain;
		m_chain = (m_activeTagChain == null) ? null : m_activeTagChain.getChain();

		if ((m_chain != null) && (pos >= 0)) {
			Element e = getParagraphElement(pos);
			if (e.getEndOffset() == e.getStartOffset() + 1) {
				setParagraphTag(pos, 0, (String) m_chain.get(0));
				return;
			}
		}

		// this change doesn't effect the text views but the ui components displaying the active chain must be notified to
		DefaultDocumentEvent changes = new DefaultDocumentEvent(0, 0, DocumentEvent.EventType.CHANGE);
		fireChangedUpdate(changes);
	}
	public void setChains(List chains) {
		m_tagChains = chains;
	}
	public void setCharacterTag(int offset, int length, String tagName) {
		AttributeSet as = getStyle(tagName);
		if (as == null) {
			addCharacterTag(tagName);
			as = getStyle(tagName);
		}

		setCharacterAttributes(offset, length, as, false);
	}
	public void setFontFamilies(List fonts) {
		m_fontFamilies = fonts;

		DefaultDocumentEvent changes = new DefaultDocumentEvent(0, getLength() - 1, DocumentEvent.EventType.CHANGE);
		fireChangedUpdate(changes);
	}
	public void setFontManager(CoImmutableStyledDocumentIF.FontManager fm) {
		m_fontManager = fm;
	}
	public void setNameToColorMap(Map cc) {
		m_nameToColorMap = cc;
		m_colors = null;

		DefaultDocumentEvent changes = new DefaultDocumentEvent(0, getLength() - 1, DocumentEvent.EventType.CHANGE);
		fireChangedUpdate(changes);
	}
	public void setNameToHyphenationMap(Map m) {
		m_nameToHyphenationMap = m;
		m_hyphenations = null;

		DefaultDocumentEvent changes = new DefaultDocumentEvent(0, getLength() - 1, DocumentEvent.EventType.CHANGE);
		fireChangedUpdate(changes);
	}
	public void setParagraphCharacterAttributes(int offset, int length, AttributeSet s, boolean replace) {
		Element section = getDefaultRootElement();

		int index0 = section.getElementIndex(offset);
		int index1 = section.getElementIndex(offset + ((length > 0) ? length - 1 : 0));

		offset = section.getElement(index0).getStartOffset();
		length = section.getElement(index1).getEndOffset() - offset;

		setParagraphAttributes(offset, length, s, replace);
	}
	public void setParagraphTag(int offset, int length, String tagName) {
		Element section = getDefaultRootElement();

		int index0 = section.getElementIndex(offset);
		int index1 = section.getElementIndex(offset + ((length > 0) ? length - 1 : 0));

		offset = section.getElement(index0).getStartOffset();
		length = section.getElement(index1).getEndOffset() - offset;

		AttributeSet as = getStyle(tagName);
		if (as == null) {
			addParagraphTag(tagName);
			as = getStyle(tagName);
		}

		setParagraphAttributes(offset, length, as, false);
	}

	// Overridden from DefaultStyledDocument
	// I really don't remember why, but there is probably a good reason

	protected void styleChanged(Style style) {
	}
	public void stylesChanged() {
		super.styleChanged(null);
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append(super.toString() + "\n");

		Element section = getRootElements()[0];
		int paragraphCount = section.getElementCount();
		for (int i = 0; i < paragraphCount; i++) {

			Element paragraph = section.getElement(i);
			AttributeSet paragraphAtts = paragraph.getAttributes();

			sb.append("paragraph # " + i + " (" + paragraphAtts.hashCode() + ")\n");
			/*
					Enumeration names = paragraphAtts.getAttributeNames();
					while
					  ( names.hasMoreElements() )
					{
					  Object o = names.nextElement();
					  sb.append( "  " + o + " = " + paragraphAtts.getAttribute( o ) + "\n" );
					}
			*/

			int runCount = paragraph.getElementCount();
			for (int n = 0; n < runCount; n++) {
				Element run = paragraph.getElement(n);
				AttributeSet runAtts = run.getAttributes();

				String text = "ERROR";
				try {
					text = getText(run.getStartOffset(), run.getEndOffset() - run.getStartOffset());
				} catch (Throwable ex) {
				}
				sb.append(
					"  run # "
						+ n
						+ " ("
						+ runAtts.hashCode()
						+ ")"
						+ " [ "
						+ run.getStartOffset()
						+ "->"
						+ run.getEndOffset()
						+ " ] "
						+ " \""
						+ text
						+ "\"\n");
				/*
						  Enumeration names2 = runAtts.getAttributeNames();
						  while
						    ( names2.hasMoreElements() )
						  {
						    Object o = names2.nextElement();
						    sb.append( "    " + o + " = " + runAtts.getAttribute( o ) + "\n");
						  }
						  */
			}
		}

		return sb.toString();
	}
	// clear character attributes for a piece of text

	public void unsetCharacterAttributes(int offset, int length, AttributeSet s) {
		try {
			writeLock();
			DefaultDocumentEvent changes = new DefaultDocumentEvent(offset, length, DocumentEvent.EventType.CHANGE);

			// split elements that need it
			buffer.change(offset, length, changes);
			AttributeSet sCopy = s.copyAttributes();

			// PENDING(prinz) - this isn't a very efficient way to iterate
			int lastEnd = Integer.MAX_VALUE;
			for (int pos = offset; pos < (offset + length); pos = lastEnd) {
				Element run = getCharacterElement(pos);
				lastEnd = run.getEndOffset();
				MutableAttributeSet attr = (MutableAttributeSet) run.getAttributes();

				changes.addEdit(new AttributeUndoableEdit(run, sCopy, false));
				attr.removeAttributes(s.getAttributeNames());
			}
			changes.end();
			fireChangedUpdate(changes);
			fireUndoableEditUpdate(new UndoableEditEvent(this, changes));
		} finally {
			writeUnlock();
		}
	}
	// clear paragraph attributes for a piece of text

	public void unsetParagraphAttributes(int offset, int length, AttributeSet s) {
		Element section = getDefaultRootElement();
		int index0 = section.getElementIndex(offset);
		int index1 = section.getElementIndex(offset + length);

		offset = section.getElement(index0).getStartOffset();
		length = section.getElement(index1).getEndOffset() - offset;

		try {
			writeLock();
			DefaultDocumentEvent changes = new DefaultDocumentEvent(offset, length, DocumentEvent.EventType.CHANGE);
			AttributeSet sCopy = s.copyAttributes();

			// PENDING(prinz) - this assumes a particular element structure
			//		Element section = getDefaultRootElement();
			//		int index0 = section.getElementIndex(offset);
			//		int index1 = section.getElementIndex(offset + ((length > 0) ? length - 1 : 0));
			for (int i = index0; i <= index1; i++) {
				Element paragraph = section.getElement(i);
				MutableAttributeSet attr = (MutableAttributeSet) paragraph.getAttributes();
				changes.addEdit(new AttributeUndoableEdit(paragraph, sCopy, false));

				attr.removeAttributes(s.getAttributeNames());
			}
			changes.end();
			fireChangedUpdate(changes);
			fireUndoableEditUpdate(new UndoableEditEvent(this, changes));
		} finally {
			writeUnlock();
		}
	}

	private boolean areEqual(AttributeSet as0, AttributeSet as1) {
		MutableAttributeSet AS0 = new CoSimpleAttributeSet(as0);
		MutableAttributeSet AS1 = new CoSimpleAttributeSet(as1);

		AS0.removeAttribute(StyleConstants.ResolveAttribute);
		AS1.removeAttribute(StyleConstants.ResolveAttribute);

		return AS0.isEqual(AS1);
	}

	public boolean isEqual(CoImmutableStyledDocumentIF d) {
		// identity
		if (d == this)
			return true;

		// size
		if (getLength() != d.getLength())
			return false;

		if (!(d instanceof CoStyledDocument))
			return false;
		CoStyledDocument doc = (CoStyledDocument) d;

		// structure
		Element section0 = getRootElements()[0];
		Element section1 = doc.getRootElements()[0];
		int I = section0.getElementCount();
		if (I != section1.getElementCount())
			return false;
		for (int i = 0; i < I; i++) {
			Element p0 = section0.getElement(i);
			Element p1 = section1.getElement(i);
			if (p0.getEndOffset() != p1.getEndOffset())
				return false;
			if (!areEqual(p0.getAttributes(), p1.getAttributes()))
				return false;

			int J = p0.getElementCount();
			if (J != p1.getElementCount())
				return false;
			for (int j = 0; j < J; j++) {
				Element r0 = p0.getElement(j);
				Element r1 = p1.getElement(j);
				if (r0.getEndOffset() != r1.getEndOffset())
					return false;
				if (!areEqual(r0.getAttributes(), r1.getAttributes()))
					return false;
			}
		}

		// text
		try {
			if (!getText(0, getLength()).equals(d.getText(0, d.getLength()))) {
				return false;
			}
		} catch (BadLocationException ex) {
			return false;
		}

		return true;
	}

	public void convertFromRTF() {
		Element section = getRootElements()[0];
		int paragraphCount = section.getElementCount();

		for (int i = 0; i < paragraphCount; i++) {
			Element paragraph = section.getElement(i);
			AttributeSet a = paragraph.getAttributes();
			String tagName = (String) a.getAttribute(Style.NameAttribute);

			if (tagName != null) {
				StringBuffer s = new StringBuffer(tagName.toLowerCase());
				s.setCharAt(0, Character.toUpperCase(s.charAt(0)));

				setParagraphTag(paragraph.getStartOffset(), 0, s.toString());
			}

			int runCount = paragraph.getElementCount();
			for (int n = 0; n < runCount; n++) {
				Element run = paragraph.getElement(n);
				setCharacterTag(run.getStartOffset(), run.getEndOffset() - run.getStartOffset(), CoTextConstants.DEFAULT_TAG_NAME);
			}
		}
	}

	private float m_kernAboveSize = 0;
	private static final Comparator m_nameAndKeyStrokeOrder = new Comparator() {
		public int compare(Object o1, Object o2) {
			return String.CASE_INSENSITIVE_ORDER.compare(((String[]) o1)[0], ((String[]) o2)[0]);
		}
	};
	private boolean m_useQxpJustification = false;
	public static final String XML_TAG = "text";

	public float getKernAboveSize() {
		return m_kernAboveSize;
	}

	public boolean getUseQxpJustification() {
		return m_useQxpJustification;
	}

	public void setKernAboveSize(float kas) {
		if (m_kernAboveSize == kas)
			return;

		m_kernAboveSize = kas;

		DefaultDocumentEvent changes = new DefaultDocumentEvent(0, getLength() - 1, DocumentEvent.EventType.CHANGE);
		fireChangedUpdate(changes);
	}

	public void setUseQxpJustification(boolean b) {
		if (m_useQxpJustification == b)
			return;

		m_useQxpJustification = b;

		DefaultDocumentEvent changes = new DefaultDocumentEvent(0, getLength() - 1, DocumentEvent.EventType.CHANGE);
		fireChangedUpdate(changes);
	}

	public void xmlAddSubModel(String parameter, Object subModel, CoXmlContext context) throws CoXmlReadException {
		// We are not interested in any sub models, so this method has been intentionally left blank
	}

	public void xmlVisit(CoXmlVisitorIF visitor) {
		// Build some dummy variables to be able to call CoXmlTextExporter.doExport().

		// FIXME: That class should be rewritten so that it doesn't need dummy
		// parameters to function.
		ElementNode styledTextNode = new ElementNode(XML_TAG);
		styledTextNode.setAttribute("xml:space", "preserve");

		XmlDocument dummyXmlDocument = new XmlDocument();
		dummyXmlDocument.appendChild(styledTextNode);

		// Perform the export
		try {
			new CoXmlTextExporter(dummyXmlDocument, styledTextNode).doExport(this);
		} catch (CoXmlWriteException e) {
			CoAssertion.assertTrue(false, "Internal error: " + getClass() + ".xmlVisit() is broken");
		}

		visitor.export(styledTextNode);
	}

	/**
	 * Create a new styled text document from an ElementNode
	 * <p>
	 * Creation date: (2001-06-15 12:58:14)
	 * 
	 * @author Johan Walles
	 */
	public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) throws CoXmlReadException {
		return new CoXmlTextImporter().doImport(node, null);
	}

	public void xmlImportFinished(Node node, CoXmlContext context) {
		// We don't need to finalize the import, so this method has been intentionally left blank
	}

	private final Map m_macros = new HashMap();
	private static final String ZERO_WIDTH_SPACE_STRING = "" + CoUnicode.ZERO_WIDTH_SPACE;

	// expand macros

	public void expandMacros() {
		expandMacros(m_macros);
	}

	// expand macros

	private void expandMacros(Map macros) {
		Element section = getRootElements()[0];

		// traverse paragraphs in reversed order
		int paragraphCount = section.getElementCount();
		for (int i = paragraphCount - 1; i >= 0; i--) {
			Element paragraph = section.getElement(i);

			// traverse runs in reversed order
			int runCount = paragraph.getElementCount();
			for (int n = runCount - 1; n >= 0; n--) {
				Element run = paragraph.getElement(n);

				// get macro reference
				AttributeSet as = run.getAttributes();
				String macro = (String) as.getAttribute(CoTextConstants.MACRO);
				debug(as);
				if (macro != null) {
					// insert definition
					int start = run.getStartOffset();
					int length = run.getEndOffset() - start;

					try {
						remove(start, length);
						insertString(start, getMacroDefinition(macros, macro), as);
					} catch (Throwable t) {
					}
				}
			}
		}
	}

	private void debug(AttributeSet as) {
		Enumeration enumer = as.getAttributeNames();
		while (enumer.hasMoreElements()) {
			System.out.println((String) enumer.nextElement());
		}
	}

	protected static String getMacroDefinition(Map macros, String macro) {
		String value = (String) ((macros == null) ? null : macros.get(macro));
		if (value == null)
			value = "$" + macro;

		if (value.length() == 0)
			value = ZERO_WIDTH_SPACE_STRING;

		return value;
	}

	public Map getMacros() {
		return m_macros;
	}

	public void setMacros(Map macros) {
		m_macros.clear();
		if (macros != null)
			m_macros.putAll(macros);
	}

	private List m_acceptedTags = new ArrayList();
	public static final String TAG_CHAIN_IGNORING_RETURN = "\0\0TAG_CHAIN_IGNORING_RETURN\0\0";

	public List getAcceptedTags() {
		return m_acceptedTags;
	}

	public CoColorIF getColor(String name) {
		if (m_nameToColorMap != null) {
			return (CoColorIF) m_nameToColorMap.get(name);
		} else {
			return null;
		}
	}

	public List getColors() // [ CoColorIF ]
	{
		if (m_colors == null) {
			m_colors = new ArrayList();
			if (m_nameToColorMap != null)
				m_colors.addAll(m_nameToColorMap.values());
		}

		return m_colors;
	}

	public void setAcceptedTags(List l) {
		m_acceptedTags.clear();
		m_acceptedTags.addAll(l);
	}
}