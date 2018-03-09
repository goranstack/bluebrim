package com.bluebrim.text.impl.shared;

import java.io.*;
import java.util.*;

import javax.swing.text.*;

import com.bluebrim.text.shared.*;

/**
 * Class for importing text in the BlueBrim native format.
 * PENDING: do we really need this ???
 *
 * @author: Dennis Malmström
 *
 * @deprecated in favor of {@link CoXmlTextImporter}
 */
 
public class CoTextImporter extends CoAbstractTextImporter
{
/**
 * @deprecated in favor of {@link CoXmlTextImporter}
 */
public CoTextImporter()
{
}
/**
 * @deprecated in favor of {@link CoXmlTextImporter}
 */
private static void convertToStyledText (String text, CoStyledDocumentIF doc) {
	if (text == null || text.length() == 0)
		return;

	try {
		// clear document
		if (doc.getLength() > 0)
			doc.remove(0, doc.getLength());
			
		int offset = 0;
		int i = 0;
		int j;
		int n = text.length();
		while (i < n && (j = (text.substring(i).indexOf('\n'))) != -1) {
			j += i + 1;
			offset = convertToStyledTextPart(doc, text.substring(i, j), offset);
			i = j;
		}
		// convert the last part
		if (i < n)
		{
			convertToStyledTextPart(doc, text.substring(i), offset);
		}
		
	} catch (BadLocationException ble) {
	}
}
/**
 * @deprecated in favor of {@link CoXmlTextImporter}
 */
private static int convertToStyledTextPart (CoStyledDocumentIF doc, String text, int offset) {
 	Style documentDefaultStyle = doc.getStyle( StyleContext.DEFAULT_STYLE );

 	int off = offset;
 	boolean textStart = true;
	int i = 0;
	int j;
	Vector v = new Vector();
	
	MutableAttributeSet pAttrs = new CoSimpleAttributeSet();
	String paragraphTagName = null;

	try {
		// while > are found
		while ((j = (text.substring(i).indexOf(">"))) != -1) {
			// find corresponding tag start <
			int k = text.substring(i, j + i).lastIndexOf("<");
			// need to know if is still in the beginning of the text part
			textStart = textStart && k == 0;
			// write text before possible tag
			if (k > 0) {
				doc.insertString(offset, text.substring(i, k + i), documentDefaultStyle);
				offset += k;
			} else if (k == -1) {
				doc.insertString(offset, text.substring(i, j + i + 1), documentDefaultStyle);
				offset += j + 1;
			}
			j += i;
			if (k != -1) {
				k += i;
				if (isTagStart(text.substring(k , j + 1))) {
					// tag name
					String tagname = text.substring(k + 1, j);
					if (hasTagEndSymbol(text.substring(j + 1), tagname)) {
						textStart = false;
						// is an character tag
						if (tagname.indexOf("=") == -1) {
							// a character tag (etikett)
							v.addElement(new Integer (1));
						} else {
							// a character style tag
							v.addElement(new Integer(2));
						}
						v.addElement(tagname);
						v.addElement(new Integer(offset));
					} else if (textStart) {
						// is a paragraph tag
						int p;
						if ((p = tagname.indexOf("=")) == -1) {
							// a paragraph tag
							if ( doc.getStyle( tagname ) == null ) doc.addParagraphTag(tagname);
							paragraphTagName = new String( tagname );
//							doc.setParagraphTag(off, offset- off, tagname);
						} else {
							// a paragraph style tag
							// use translator to set the correct style
							CoAttributeTranslator translator = CoAttributeTranslator.getTranslator(tagname.substring(0,p));
//							MutableAttributeSet sattr = new CoSimpleAttributeSet();
							pAttrs.addAttribute(translator.getAttribute(), translator.string2value(tagname.substring(p+1)));
//							doc.setParagraphAttributes(off, offset- off, sattr, false);
						}
					} else {
						// no tag, just insert it
						doc.insertString(offset, text.substring(k, j + 1), documentDefaultStyle);
						offset += j + 1 - k;
					}
				} else if (isTagEnd(text.substring(k , j + 1))) {
					// is tag end symbol
					// tag name
					String tagname = text.substring(k + 2, j);
					boolean found = false;
					//int o = 1;
					int o = v.size() - 2;
					while (!found && o > 0) { // && v.size() > o){
						String str = (String)v.elementAt(o);
						found = str.startsWith(tagname);
						if (!found)
						o += 3;
					}
					if (((Integer)v.elementAt(o - 1)).intValue() == 1) {
						// a character tag (etikett)
						o = ((Integer)v.elementAt(o + 1)).intValue();
						if ( doc.getStyle( tagname ) == null ) doc.addCharacterTag(tagname);
						doc.setCharacterTag(o, offset - o, tagname);
					} else {
						// a character style tag
						// use translator to set the correct style
						tagname = (String)v.elementAt(o);
						int p = tagname.indexOf("=");
						o = ((Integer)v.elementAt(o + 1)).intValue();

						if
							( tagname.substring( 0, p ).equals( CoAttributeTranslator.COMMENT_TAG ) )
						{
							doc.insertString( offset, " ", documentDefaultStyle );
							offset += 1;
							MutableAttributeSet sattr = new CoSimpleAttributeSet();
							CoStyleConstants.setComment( sattr, tagname.substring( p + 1 ) );
							doc.setCharacterAttributes( offset - 1, 1, sattr, false );
						} else {
							CoAttributeTranslator translator = CoAttributeTranslator.getTranslator(tagname.substring(0,p));
							MutableAttributeSet sattr = new CoSimpleAttributeSet();
							sattr.addAttribute(translator.getAttribute(), translator.string2value(tagname.substring(p+1)));
							doc.setCharacterAttributes(o, offset - o, sattr, false);
						}
					}
				} else {
					// no tag, just insert it
					doc.insertString(offset, text.substring(k, j + 1), documentDefaultStyle);
					offset += j + 1 - k;
				}
			}
			i = j + 1;
		}
		// insert the last text part
		if (i < text.length()) {
			doc.insertString(offset, text.substring(i), documentDefaultStyle);
			offset +=  text.substring(i).length();
		}
	} catch (BadLocationException ble) {
	}

	if
		( offset > off )
	{
		if ( paragraphTagName != null ) doc.setParagraphTag(off, 0, paragraphTagName);
		doc.setParagraphAttributes(off, 0, pAttrs, false);
	}

	return offset;
}
/**
 * @deprecated in favor of {@link CoXmlTextImporter}
 */
public CoStyledDocument doImport( Reader r, CoStyledDocument doc ) throws Exception
{
	// Create a new document to import text to.
	if ( doc == null ) doc = new CoStyledDocument();
	
	convertToStyledText( readText( r ), doc );

	return doc;
}
/**
 * @deprecated in favor of {@link CoXmlTextImporter}
 */
private static boolean hasTagEndSymbol (String text, String tagname) {
	String s;
	int i;
	if ((i = tagname.indexOf('=')) != -1)
		s = "<\\" + tagname.substring(0, i) + ">";
	else
		s = "<\\" + tagname + ">";

	return text.indexOf(s) != -1;
}
/**
 * @deprecated in favor of {@link CoXmlTextImporter}
 */
private static boolean isTagEnd(String text) {
	// text look like this: "<\xxxxx>"
	return text.startsWith("<\\") && text.length() > 3;
}
/**
 * @deprecated in favor of {@link CoXmlTextImporter}
 */
private static boolean isTagStart(String text) {
	// text look like this: "<xxxxxx>"
	// not allowed: "<\xxxx>" 
	return text.indexOf("<\\") == -1 && text.length() > 2;
}
}