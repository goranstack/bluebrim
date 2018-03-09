package com.bluebrim.text.shared;
import java.util.*;

import javax.swing.text.*;

/**
 * Interface for mutable formatted text.
 *
 * See com.bluebrim.text.shared.CoStyledDocument for method comments
 *
 * @author: Dennis Malmström
 */

public interface CoStyledDocumentIF extends com.bluebrim.text.shared.CoImmutableStyledDocumentIF
{
MutableAttributeSet addCharacterTag( String name );
MutableAttributeSet addParagraphTag( String name );
void changeCharacterAttributes( int offset, int length, com.bluebrim.text.shared.CoAttributeSetOperationIF op );
 void changeParagraphAttributes( int offset, int length, com.bluebrim.text.shared.CoAttributeSetOperationIF op );  
void clear();
void clearAtomAttributes( MutableAttributeSet as );

void insertMacro( int offset, int length, String macro );
void setActiveChain( String chainName, int pos  );
void setActiveChain( com.bluebrim.text.shared.CoTagChainIF chain, int pos  );
void setChains( List chains );
void setCharacterTag( int offset, int length, String tagName );
public void setFontFamilies( List fonts );
void setFontManager( com.bluebrim.text.shared.CoImmutableStyledDocumentIF.FontManager fm );
public void setNameToColorMap( Map cc );
public void setNameToHyphenationMap( Map cc );
void setParagraphTag( int offset, int length, String tagName );

public void unsetCharacterAttributes( int offset, int length, AttributeSet attributes );
public void unsetParagraphAttributes( int offset, int length, AttributeSet attributes );

void expandMacros();

float getKernAboveSize();

boolean getUseQxpJustification();

void setKernAboveSize( float kas );

void setMacros( Map macros );

void setUseQxpJustification( boolean b );
}