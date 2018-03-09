package com.bluebrim.text.impl.shared.xtg;

import java.io.*;

/**
 * Xtg root parse tree node
 * 
 * @author: Dennis
 */
 
public class CoXtgRootParseNode extends CoXtgParseNode
{
	private CoXtgMetadataParseNode m_metadataNode;
	private CoXtgDefsParseNode m_defsNode;
	private CoXtgParagraphsParseNode m_paragraphsNode;
/**
 * CoXtgRootParseNode constructor comment.
 */
public CoXtgRootParseNode() {
	super();
}
public void createXtg( PrintStream s )
{
	m_metadataNode.createXtg( s );
	m_defsNode.createXtg( s );
	m_paragraphsNode.createXtg( s );
	s.println();
}
public void dump( String indent )
{
	System.err.println( indent + this );
	if ( m_metadataNode != null ) m_metadataNode.dump( indent + "  " );
	if ( m_defsNode != null ) m_defsNode.dump( indent + "  " );
	if ( m_paragraphsNode != null ) m_paragraphsNode.dump( indent + "  " );
}
public void extract( com.bluebrim.text.shared.CoTypographyRuleIF r, CoXtgLogger l )
{
	m_defsNode.extract( r, l );
}
public void extract( com.bluebrim.text.shared.CoStyledDocumentIF d, CoXtgLogger l )
{
//	r.removeRules( com.bluebrim.publication.impl.shared.CoPublicationConstants.PARAGRAPH_STYLE_RULE );
//	r.removeRules( com.bluebrim.publication.impl.shared.CoPublicationConstants.CHARACTER_STYLE_RULE );

	d.clear();
	
	m_paragraphsNode.extract( d, l );
}
boolean parse( CoXtgParser p, CoXtgLogger l ) throws CoXtgParseException
{
	m_metadataNode = new CoXtgMetadataParseNode();
	m_metadataNode.parse( p, l );

	if
		( m_metadataNode.isEmpty() )
	{
		throw new CoXtgParseException( "expected xtg meta data" );
	}

	
	m_defsNode = new CoXtgDefsParseNode();
	m_defsNode.parse( p, l );
	
	m_paragraphsNode = new CoXtgParagraphsParseNode();
	m_paragraphsNode.parse( p, l );

	return true;
}
}
