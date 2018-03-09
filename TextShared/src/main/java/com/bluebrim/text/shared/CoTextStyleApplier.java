package com.bluebrim.text.shared;
import java.io.*;
import java.util.*;

import javax.swing.text.*;

import com.bluebrim.paint.shared.*;
import com.bluebrim.text.impl.shared.*;

/**
 * Class for collecting text style data and applying it to documents
 * 
 * @author: Dennis Malmström
 */

public class CoTextStyleApplier implements CoTextConstants, Serializable
{
	private Map m_characterStyles = new HashMap(); // [ String -> MutableAttributeSet ]
	private Map m_paragraphStyles = new HashMap(); // [ String -> MutableAttributeSet ]
	
	private Map m_colors = new HashMap(); // [ String -> CoColorIF ]
	
	private Map m_hyphenations = new HashMap(); // [ String -> CoHyphenationIF ]
	
	private List m_fontFamilies = new ArrayList(); // [ String ]
	
	private List m_tagChains = new ArrayList(); // [ CoTagChainIF ]
	
	private Map m_macros = new HashMap(); // [ String -> String ]

	private float m_kernAboveSize;
	private boolean m_useQxpJustification;

public CoTextStyleApplier()
{
}



public void clear()
{
	m_characterStyles.clear();
	m_paragraphStyles.clear();
	m_colors.clear();
	m_hyphenations.clear();
	m_fontFamilies.clear();
	m_tagChains.clear();
	m_macros.clear();
}
protected static void clear( MutableAttributeSet s )
{
  Enumeration keys = s.getAttributeNames();
  while
		( keys.hasMoreElements() )
  {
		Object key = keys.nextElement();
		
		if ( key.equals( StyleConstants.NameAttribute ) ) continue;
		if ( key.equals( StyleConstants.ResolveAttribute ) ) continue;
		if ( key.equals( PARAGRAPH_TAG ) ) continue;
		if ( key.equals( CHARACTER_TAG ) ) continue;
		
		s.removeAttribute( key );
  }
}
/**
 * Lasse 990630. Ett första optimeringsförsök. 
 * 40% av tiden när en sidöversikt skapas tillbringas i denna metod!
 */
protected static void copy( MutableAttributeSet to, AttributeSet from )
{
	Object name = to.getAttribute( StyleConstants.NameAttribute );
	Object resolver = to.getAttribute( StyleConstants.ResolveAttribute );
	to.removeAttributes( to );
	to.addAttributes( from );

	if
		( name != null )
	{
		to.addAttribute( StyleConstants.NameAttribute, name );
	}

	if
		( resolver != null )
	{
		to.addAttribute( StyleConstants.ResolveAttribute, resolver );
	}
}
public boolean equals( Object o )
{
	if
		( o instanceof CoTextStyleApplier )
	{
		return equals( (CoTextStyleApplier) o );
	} else {
		return super.equals( o );
	}
}
public boolean equals( CoTextStyleApplier a )
{
	if ( ! m_characterStyles.equals( a.m_characterStyles ) ) return false;
	if ( ! m_paragraphStyles.equals( a.m_paragraphStyles ) ) return false;
	if ( ! m_colors.equals( a.m_colors ) ) return false;
	if ( ! m_hyphenations.equals( a.m_hyphenations ) ) return false;
	if ( ! m_fontFamilies.equals( a.m_fontFamilies ) ) return false;
	if ( ! m_tagChains.equals( a.m_tagChains ) ) return false;
	return true;
}
public void extract( CoTextParameters p )
{
	extractColors( p.getTypographyContext() );
	extractHyphenations( p.getTypographyContext() );
	extractFontFamilies( p.getTypographyContext() );
	extractTagChains( p.getTypographyContext() );
	m_kernAboveSize = p.getKernAboveSize( false );
	m_useQxpJustification = p.doUseQxpJustification();
}

private void extractColors( CoTypographyContextIF colors )
{
	Iterator i = colors.getColors().iterator();
	while
		( i.hasNext() )
	{
		CoColorIF c = (CoColorIF) i.next();
		m_colors.put( c.getName(), c );
	}
}
private void extractFontFamilies( CoTypographyContextIF provider )
{
	Iterator i = ( (CoFontFamilyCollectionIF) provider ).getFontFamilyNames().iterator();
	while
		( i.hasNext() )
	{		
		String f = (String) i.next();
		m_fontFamilies.add(f);
	}
}
private void extractHyphenations( CoTypographyContextIF cp )
{
	Iterator i = cp.getHyphenations().iterator();
	while
		( i.hasNext() )
	{
		CoHyphenationIF c = (CoHyphenationIF) i.next();
		m_hyphenations.put( c.getName(), c );
	}
}

private void extractStyles( CoTypographyRuleIF tr )
{
	Iterator i = tr.getCharacterStyles().iterator();
	while
		( i.hasNext() )
	{
		extractStyle( (CoCharacterStyleIF) i.next(), m_characterStyles );
	}
	
	i = tr.getParagraphStyles().iterator();
	while
		( i.hasNext() )
	{
		extractStyle( (CoCharacterStyleIF) i.next(), m_paragraphStyles );
	}
}
private void extractTagChains( CoTypographyContextIF p )
{
	m_tagChains.clear();
	m_tagChains.addAll( p.getTagChains() );
}


public void setMacroMap(Map macroMap) {
	m_macros = macroMap;
}

//WARNING: Should only be used by XML-import

public void setState( Map characterStyles,
	                    Map paragraphStyles,
						          Map colors,
						          Map hyphenations,
						          List fontFamilies){
							
	m_characterStyles = characterStyles;
	m_paragraphStyles = paragraphStyles;
	m_colors = colors;
	m_hyphenations = hyphenations;
	if ( fontFamilies != null ) m_fontFamilies = fontFamilies;	
}


private void extractStyle( CoCharacterStyleIF p, Map target )
{
	String name = p.getName();
		
	if
		( p.isDeleted() )
	{
		target.remove( name );
	} else {
		MutableAttributeSet a = (MutableAttributeSet) target.get( name );
		
		if
			( a == null )
		{
			a = new CoSimpleAttributeSet();
			target.put( name, a );
		} else if
			( ! p.getInherit() )
		{
			a.removeAttributes( a );
		}
		
		a.addAttributes( p.getEffectiveAttributes() );
	}
}


public void extractStyles( CoTextParameters p )
{
	extractStyles( p.getTypographyRule() );
}

public boolean apply( CoFormattedText text )
{
  if ( text == null ) return false;

  text.setCharacterStyles( m_characterStyles );
  text.setParagraphStyles( m_paragraphStyles );
	text.setColors( m_colors );
	text.setHyphenations( m_hyphenations );
	text.setFontFamilies( m_fontFamilies );
	text.setChains( m_tagChains );
	text.setKernAboveSize( m_kernAboveSize );
	text.setUseQxpJustification( m_useQxpJustification );
	text.setMacros( m_macros );
  
  return true;
}

public static CoTextStyleApplier createNullApplier()
{
	CoTextStyleApplier nullApplier = new CoTextStyleApplier();
	nullApplier.m_macros = null;

	return nullApplier;
}

public Map getParagraphStyles()
{
	return m_paragraphStyles;
}
}