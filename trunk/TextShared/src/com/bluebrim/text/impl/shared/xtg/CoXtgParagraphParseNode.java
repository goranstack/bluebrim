package com.bluebrim.text.impl.shared.xtg;

import java.io.*;
import java.util.*;

import javax.swing.text.*;

import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Xtg paragraph parse tree node
 * 
 * @author: Dennis
 */

 public class CoXtgParagraphParseNode extends CoXtgParseNode
{
	private CoXtgNameParseNode m_nameNode;
	private CoXtgTextParseNode m_textNode;
/**
 * CoXtgRootParseNode constructor comment.
 */
public CoXtgParagraphParseNode() {
	super();
}
public void createXtg( PrintStream s )
{
	if
		( m_nameNode != null )
	{
		s.print( m_nameNode.getName() + ":" );
	}

	s.print( m_textNode.getText() );
}
public void dump( String indent )
{
	System.err.println( indent + this );
	if ( m_nameNode != null ) System.err.println( m_nameNode.getName() + " : " );
	System.err.println( m_textNode.getText() );
}
public void extract( com.bluebrim.text.shared.CoTypographyRuleIF r, CoXtgLogger l )
{
}
public void extract( com.bluebrim.text.shared.CoStyledDocumentIF d, CoXtgLogger logger )
{
	if ( m_textNode == null ) return;

	int I = d.getLength();
	Map tagmap = new HashMap();

	// scan text string
	CoXtgScanner sc = new CoXtgTextScanner( new java.io.StringReader( m_textNode.getText() ), logger );
	CoXtgToken t = sc.scan();
	while
		( t != null )
	{
		String text = null;
		// analyze token
		if
			( t instanceof CoXtgTextToken )
		{
			// text
			text = ( (CoXtgTextToken) t ).getString();
			
		} else if
		// special character
			( ( (CoXtgNameToken) t ).getString().charAt( 1 ) == '\\' )
		{
			text = translate( ( (CoXtgNameToken) t ).getString() );
			
		} else {
			// tags
			CoXtgAttributelistParser p = new CoXtgAttributelistParser( new java.io.StringReader( ( (CoXtgNameToken) t ).getString() ) );
			p.parse();
			CoXtgAttributelistParseNode tt = (CoXtgAttributelistParseNode) p.getRoot();
			Integer n = new Integer( d.getLength() );
			Iterator i = tt.getTags( null ).iterator();
			while
				( i.hasNext() )
			{
				CoXtgAttributeParseNode o = (CoXtgAttributeParseNode) i.next();
				String tagname = (String) o.getName();
				if ( tagname.charAt( 0 ) == '@' ) tagname = "@";
				List l = (List) tagmap.get( tagname );
				if
					( l == null )
				{
					l = new ArrayList();
					tagmap.put( tagname, l );
				}
				
				l.add( l = new ArrayList() );
				l.add( o );
				l.add( n );
			}
		}

		// insert text
		if
			( text != null )
		{
			try
			{
				d.insertString( d.getLength(), text, null );
			}
			catch ( javax.swing.text.BadLocationException ex )
			{
				logger.log( "Generator exception: text insertion failed\n" + text + "\n" + ex );
			}
		}
		
		t = sc.scan();
	}

	// add newline
	try
	{
		d.insertString( d.getLength(), "\n", null );
	}
	catch ( javax.swing.text.BadLocationException ex )
	{
		logger.log( "Generator exception: newline insertion failed\n" + ex );
	}


	
	MutableAttributeSet paragarphAttributes = new com.bluebrim.text.shared.CoSimpleAttributeSet();

	// set paragraph tag
	String ptag = null;
	if
		( m_nameNode != null )
	{	
		ptag = m_nameNode.getName();
		if ( ptag.equals( "" ) ) ptag = null;
		else if ( ptag.equals( "$" ) ) ptag = null;
	} else {
		if 
			( I > 0 )
		{	
			// no tag, use previous paragraph tag
			ptag = (String) CoStyleConstants.getParagraphTag( d.getParagraphAttributes( I - 1, 0 ) );
			// save previous paragraph attributes
			paragarphAttributes.addAttributes( d.getParagraphAttributes( I - 1, 0 ) );
			paragarphAttributes.removeAttribute( CoTextConstants.PARAGRAPH_TAG );
		}

	}

	if ( ptag != null ) d.setParagraphTag( I, 0, ptag );

	// apply tags
	MutableAttributeSet a = new com.bluebrim.text.shared.CoSimpleAttributeSet();
	Iterator i = tagmap.keySet().iterator();
	while
		( i.hasNext() )
	{
		String tagname = (String) i.next();
		List occurences = (List) tagmap.get( tagname );
		boolean applyToParagraph = false;
		if
			( occurences.size() == 1 )
		{
			if
				( ( (Integer) ( (List) occurences.get( 0 ) ).get( 1 ) ).intValue() == I )
			{
				applyToParagraph = true;
			}
		}

		if
			( applyToParagraph )
		{
			// paragraph attribute
			( (CoXtgAttributeParseNode) ( (List) occurences.get( 0 ) ).get( 0 ) ).extract( paragarphAttributes, logger );
			
		} else if
			( tagname.equals( "@" ) )
		{
			// character tag
			int N = occurences.size();
			for
				( int n = 0; n < N; n++ )
			{
				List occurence = (List) occurences.get( n );
				String ctag = ( (CoXtgAttributeParseNode) occurence.get( 0 ) ).getName().substring( 1 );
				if
					( ! ctag.equals( "$" ) )
				{
					int p0 = ( (Integer) occurence.get( 1 ) ).intValue();
					int p1 = ( n == N - 1 ) ? d.getLength() : ( (Integer) ( (List) occurences.get( n + 1 ) ).get( 1 ) ).intValue();
					d.setCharacterTag( p0, p1 - p0, ctag );
				}
			}
		} else {

			// character attribute
			a.removeAttributes( a );
			int N = occurences.size();
			for
				( int n = 0; n < N; n++ )
			{
				List occurence = (List) occurences.get( n );
				( (CoXtgAttributeParseNode) occurence.get( 0 ) ).extract( a, logger );
				if
					( a.getAttributeCount() > 0 )
				{
					int p0 = ( (Integer) occurence.get( 1 ) ).intValue();
					int p1 = ( n == N - 1 ) ? d.getLength() : ( (Integer) ( (List) occurences.get( n + 1 ) ).get( 1 ) ).intValue();
					d.setCharacterAttributes( p0, p1 - p0, a, false );
				}
			}
		}
	}

	d.setParagraphAttributes( I, 0, paragarphAttributes, false );

}
boolean parse( CoXtgParser p, CoXtgLogger l ) throws CoXtgParseException
{
	CoXtgToken t = p.getToken();

	if ( t == null ) return false;
	
	if
		( t instanceof CoXtgNameToken )
	{
		m_nameNode = new CoXtgNameParseNode( ( (CoXtgNameToken) t ).getString().substring( 1 ) );
		t = p.nextToken();
		checkToken( t, CoXtgColonToken.class );
		t = p.nextToken();
	}

	CoXtgAttributelistParseNode taglist0Node = new CoXtgAttributelistParseNode();
	CoXtgAttributelistParseNode taglist1Node = null;
	if
		( taglist0Node.parse( p, l ) )
	{
		taglist1Node = new CoXtgAttributelistParseNode();
		if
			( taglist1Node.parse( p, l ) )
		{
		} else {
			taglist1Node = null;
		}
	} else {
		taglist0Node = null;
	}
	
	t = p.getToken();
	checkToken( t, CoXtgTextToken.class );

	StringBuffer text = new StringBuffer();
	if ( taglist0Node != null ) taglist0Node.collect( text );
	if ( taglist1Node != null ) taglist1Node.collect( text );
	text.append( ( (CoXtgTextToken) t ).getString() );

	m_textNode = new CoXtgTextParseNode( text.toString() );
	p.nextToken();
/*
	CoXtgScanner sc = new CoXtgTextScanner( new java.io.StringReader( text.toString() ) );
	System.err.println( "##############################################" );
	t = sc.scan();
	while
		( t != null )
	{
		System.err.println( t );
		t = sc.scan();
	}
	System.err.println( "##############################################" );
	*/
	return true;
}
private String translate( String str )
{
	switch
		( str.charAt( 2 ) )
	{
		case 'n' : return "\r";
		case 'd' : return "\r";
		case 'm' : return "--";
		case '-' : return "-";
//					case 'i' : return "???";
		case 't' : return "\t";
		case 's' : return " ";
//					case 'f' : return "???";
//					case 'p' : return "???";
//					case 'q' : return "???";
		case 'h' : return CoHyphenationConstants.HYPHENATION_POINT_STRING;
//					case '2' : return "???";
//					case '3' : return "???";
//					case '4' : return "???";
//					case 'c' : return "???";
//					case 'b' : return "???";
		case '!' :
			{
				switch
					( str.charAt( 3 ) )
				{
					case '-' : return CoHyphenationConstants.ANTI_HYPHENATION_POINT_STRING;
					case 's' : return CoTextConstants.NO_BREAK_SPACE_STRING;
//					case 'f' : return "???";
//					case 'p' : return "???";
//					case 'q' : return "???";
				}
				break;
			}
		
	}

	return str;
}
}