package com.bluebrim.text.shared;
import java.util.*;

import javax.swing.text.*;

/**
 * Interface for immutable formatted text.
 * Extends javax.swing.text.StyledDocument and adds the following features:
 *	- Paragraph and character tags
 *  - Paragraph tag chains 
 *  - Atomic texts (substrings that act like a single character)
 *  - Macro expansion
 *  - Embedded comments
 *  - Latest change timestamping
 *  - Convenience methods for handling paragraph and character attributes
 *
 * See com.bluebrim.text.shared.CoStyledDocument for method comments
 *
 * @author: Dennis Malmström
 */

public interface CoImmutableStyledDocumentIF extends StyledDocument, CoTextEditorContextIF, com.bluebrim.xml.shared.CoXmlExportEnabledIF
{
	// interface for extracting a font object from an attribute set.
	public interface FontManager
	{
		java.awt.Font getFont( AttributeSet a );
		com.bluebrim.font.shared.CoFont getCoFont( AttributeSet a );
	};
com.bluebrim.text.shared.CoTagChainIF getActiveChain();
AttributeSet getAttributes( int offset, int length, Object [] attributes );
Object getCharacterAttribute( int offset, int length, Object attribute );
AttributeSet getCharacterAttributes( int offset, int length );
com.bluebrim.text.shared.CoStyledDocumentIF getCopy();
public Dictionary getDocumentProperties();
FontManager getFontManager();
public Map getNameToColorMap();
public Map getNameToHyphenationMap();
Object getParagraphAttribute( int offset, int length, Object attribute );
AttributeSet getParagraphAttributes( int offset, int length );
StyleContext getStyleContext();

List getUsedParagraphTags();
int getWordCount();
boolean isAtomic( Element e );
int search( String key, int startAt, boolean forward, boolean caseSensitive, boolean word );

boolean isEqual( CoImmutableStyledDocumentIF d );

List getAcceptedTags();
}