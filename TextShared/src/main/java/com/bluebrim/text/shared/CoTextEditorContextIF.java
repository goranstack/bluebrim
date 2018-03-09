package com.bluebrim.text.shared;

import java.util.*;

import javax.swing.text.*;

/**
 * Interface for holding context needed by text editors
 *
 * @author: Dennis Malmström
 */

public interface CoTextEditorContextIF extends com.bluebrim.text.shared.CoTypographyContextIF
{
public List getCharacterTagNameAndKeyStrokes(); // [ Object[]( String, KeyStroke ) ], the names and keystrokes of all available character tags
public List getCharacterTagNames(); // [ String ], the names of all available character tags
public List getParagraphTagNameAndKeyStrokes(); // [ Object[]( String, KeyStroke ) ], the names and keystrokes of all available paragraph tags
public List getParagraphTagNames(); // [ String ], the names of all available paragraph tags
public MutableAttributeSet getTag( String tagName ); // get the attributes of a tag
List getTagChains(); // [ CoTagChinaIF ], available tag chains
public List getTagNames(); // [ String ], the names of all available tags
void notifyChange(); // call after object has been changed

Map getMacros();
}