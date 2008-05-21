package com.bluebrim.text.impl.client;

// JFC imports
import java.util.*;

import javax.swing.text.*;


/**
 * Event-klass som beskriver en förändring i den selekterade textens typografi.
 */
public class CoAttributeEvent extends EventObject
{
	private boolean m_didEditableChange;
	
	private int m_p0;
	private int m_p1;
	private boolean m_isStraddlingParagraphs;
	private boolean m_didParagraphChange;
	
	private AttributeSet m_paragraphAttributes;
	private AttributeSet m_characterAttributes;
public CoAttributeEvent( CoAbstractTextEditor source )
{
	super( source );

	set( null, null, -1, -1, false, false );
}
public boolean didEditableChange()
{
	return m_didEditableChange;
}
public boolean didParagraphChange()
{
	return m_didParagraphChange;
}
public AttributeSet getCharacterAttributes()
{
	return m_characterAttributes;
}
public int getP0()
{
	return m_p0;
}
public int getP1()
{
	return m_p1;
}
public AttributeSet getParagraphAttributes()
{
	return m_paragraphAttributes;
}
public boolean isStraddlingParagraphs()
{
	return m_isStraddlingParagraphs;
}
public void set( AttributeSet paragraphAttributes,
	               AttributeSet characterAttributes,
	               int p0,
	               int p1,
	               boolean isStraddlingParagraphs,
	               boolean didParagraphChange )
{
	m_paragraphAttributes = paragraphAttributes;
	m_characterAttributes = characterAttributes;
	m_p0 = p0;
	m_p1 = p1;
	m_isStraddlingParagraphs = isStraddlingParagraphs;
	m_didParagraphChange = didParagraphChange;

	m_didEditableChange = false;
}
public void set( boolean didEditableChange )
{
	m_paragraphAttributes = null;
	m_characterAttributes = null;
	m_p0 = -1;
	m_p1 = -1;
	m_isStraddlingParagraphs = false;
	m_didParagraphChange = false;

	m_didEditableChange = didEditableChange;
}
}
