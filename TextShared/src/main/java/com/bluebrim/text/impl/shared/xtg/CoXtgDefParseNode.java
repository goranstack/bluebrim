package com.bluebrim.text.impl.shared.xtg;

import java.io.*;

import javax.swing.text.*;

/**
 * Xtg definition tree node
 * 
 * @author: Dennis
 */
 
public class CoXtgDefParseNode extends CoXtgParseNode
{
	private CoXtgNameParseNode m_nameNode;
	private CoXtgDefspecParseNode m_defspecNode;
	private CoXtgAttributelistParseNode m_attributelistNode;
/**
 * CoXtgRootParseNode constructor comment.
 */
public CoXtgDefParseNode() {
	super();
}
protected static void copy( MutableAttributeSet to, AttributeSet from )
{
	Object name = to.getAttribute( StyleConstants.NameAttribute );
	Object resolver = to.getAttribute( StyleConstants.ResolveAttribute );
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
public void createXtg( PrintStream s )
{
	s.print( m_nameNode.getName() + "=" );

	if ( m_defspecNode != null ) m_defspecNode.createXtg( s );

	m_attributelistNode.createXtg( s );
}
public void dump( String indent )
{
	System.err.println( indent + this );
	if ( m_defspecNode != null ) m_defspecNode.dump( indent + "  " );
	if ( m_attributelistNode != null ) m_attributelistNode.dump( indent + "  " );
}
public void extract( com.bluebrim.text.shared.CoTypographyRuleIF r, CoXtgLogger l )
{
	boolean isCharacterStyle = false;
	if ( m_defspecNode == null ) isCharacterStyle = true;
	else if ( m_defspecNode.getString( 3 ) != null ) isCharacterStyle = true;

	if
		( isCharacterStyle )
	{
		extractCharacterTypographyRule( r, l );
	} else {
		extractParagraphTypographyRule( r, l );
	}
}
public void extract( com.bluebrim.text.shared.CoStyledDocumentIF d, CoXtgLogger logger )
{
}
private void extractCharacterTypographyRule( com.bluebrim.text.shared.CoTypographyRuleIF r, CoXtgLogger l )
{
	String name = m_nameNode.getName().substring( 1 );
	com.bluebrim.text.shared.CoCharacterStyleIF cs = r.getCharacterStyle( name );
	if
		( cs == null )
	{
		cs = r.addCharacterStyle( name );
	} else {
		cs.clear();
	}
	
	if
		( m_defspecNode != null )
	{
		String basedOn = m_defspecNode.getString( 3 ).getQstring();
		if
			( basedOn.length() > 0 )
		{
			com.bluebrim.text.shared.CoCharacterStyleIF tmp = r.getCharacterStyle( basedOn );
			if
				( tmp != null )
			{
				copy( (MutableAttributeSet) cs.getAttributes(), tmp.getAttributes() );
			} else {
				l.log( "Generator warning: can't find character style (" + basedOn + ") on which character style " + name + " is based." );
			}
		}
	}

	m_attributelistNode.extract( cs, l );
}
private void extractParagraphTypographyRule( com.bluebrim.text.shared.CoTypographyRuleIF r, CoXtgLogger l )
{
	String name = m_nameNode.getName().substring( 1 );
	com.bluebrim.text.shared.CoCharacterStyleIF cs = r.getParagraphStyle( name );
	if
		( cs == null )
	{
		cs = r.addParagraphStyle( name );
	} else {
		cs.clear();
	}
	
	CoXtgQstringParseNode n = m_defspecNode.getString( 0 );
	if
		( n != null )
	{
		String basedOn = n.getQstring();
		if
			( basedOn.length() > 0 )
		{
			com.bluebrim.text.shared.CoCharacterStyleIF tmp = r.getParagraphStyle( basedOn );
			if
				( tmp != null )
			{
				copy( (MutableAttributeSet) cs.getAttributes(), tmp.getAttributes() );
			} else {
				l.log( "Generator warning: can't find paragraph style (" + basedOn + ") on which paragraph style " + name + " is based." );
			}
		}
	}
	
	n = m_defspecNode.getString( 2 );
	if
		( n != null )
	{
		String basedOn = n.getQstring();
		if
			( basedOn.length() > 0 )
		{
			com.bluebrim.text.shared.CoCharacterStyleIF tmp = r.getCharacterStyle( basedOn );
			if
				( tmp != null )
			{
				copy( (MutableAttributeSet) cs.getAttributes(), tmp.getAttributes() );
			} else {
				l.log( "Generator warning: can't find character style (" + basedOn + ") on which paragraph style " + name + " is based." );
			}
		}
	}

	m_attributelistNode.extract( cs, l );
}
boolean parse( CoXtgParser p, CoXtgLogger l ) throws CoXtgParseException
{
	CoXtgToken t = p.getToken();

	if
		( ! ( t instanceof CoXtgNameToken ) )
	{
		return false;
	}

	if
		( ! ( p.getNextToken() instanceof CoXtgEqualsToken ) )
	{
		return false;
	}
	
	m_nameNode = new CoXtgNameParseNode( ( (CoXtgNameToken) t ).getString() );
	t = p.nextToken();
	t = p.nextToken();
	if
		( t instanceof CoXtgLeftBracketToken )
	{
		p.nextToken();
		CoXtgDefspecParseNode tmp = new CoXtgDefspecParseNode();
		tmp.parse( p, l );
		t = p.getToken();
		checkToken( t, CoXtgRightBracketToken.class );
		p.nextToken();
		m_defspecNode = tmp;
	}

	CoXtgAttributelistParseNode tmp = new CoXtgAttributelistParseNode();
	if
		( tmp.parse( p, l ) )
	{
		m_attributelistNode = tmp;
		return true;
	} else {
		return false;
	}
}
}