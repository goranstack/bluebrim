package com.bluebrim.text.impl.shared;

import java.util.*;

import javax.swing.text.*;

import com.bluebrim.text.shared.*;

/**
 * A class that collects a distributed document.
 * See com.bluebrim.text.shared.CoDocumentDistributer for information on document distribution
 *
 * @author: Dennis Malmström
 */
 
public class CoDocumentCollector
{
	// holds sequence of paragaph that are to be appended together
	private static class ParagraphSequence
	{
		public int m_order; // paragaph order tag (see com.bluebrim.text.shared.CoDocumentDistributer)
		public List m_paragraphs = new ArrayList();

		public ParagraphSequence( int order )
		{
			m_order = order;
		}
	}
public CoDocumentCollector()
{
	super();
}


private void insertParagraphs( ParagraphSequence p, com.bluebrim.text.shared.CoStyledDocumentIF destination )
{
	List v = p.m_paragraphs;
	
	int I = v.size();
	for
		( int i = 0; i < I; i++ )
	{
		Element paragraph = (Element) v.get( i );

		int start = paragraph.getStartOffset();
		int end = paragraph.getEndOffset();
	
		int pos = destination.getLength();

		try
		{
			destination.insertString( pos, paragraph.getDocument().getText( start, end - start ), null );
		}
		catch ( BadLocationException e )
		{
		}

		MutableAttributeSet attr = new com.bluebrim.text.shared.CoSimpleAttributeSet( paragraph.getAttributes() );

		String tag = (String) paragraph.getAttributes().getAttribute( CoTextConstants.PARAGRAPH_TAG );
		if ( tag == null ) tag = CoTextConstants.DEFAULT_TAG_NAME;

		attr.removeAttribute( StyleConstants.ResolveAttribute );
		attr.removeAttribute( StyleConstants.NameAttribute );
		attr.removeAttribute( com.bluebrim.text.shared.CoDocumentDistributer.PARAGRAPH_ORDER );
		
		destination.setParagraphAttributes( pos, 1, attr, false );
		destination.setParagraphTag( pos, 1, tag );

		int runCount = paragraph.getElementCount();
		for
		  ( int n = 0; n < runCount; n++ )
		{
		  Element run = paragraph.getElement( n );
		  AttributeSet runAtts = run.getAttributes();

		  int p0 = run.getStartOffset();
			int p1 = run.getEndOffset();
			
		  tag = (String) runAtts.getAttribute( CoTextConstants.CHARACTER_TAG );
		  if
		  	( tag != null )
		  {
		  	destination.setCharacterTag( pos + p0 - start, p1 - p0, tag );
		  }

		  attr.removeAttributes( attr );
		  attr.addAttributes( runAtts );
			attr.removeAttribute( StyleConstants.ResolveAttribute );
			attr.removeAttribute( StyleConstants.NameAttribute );
			destination.setCharacterAttributes( pos + p0 - start, p1 - p0, attr, false );
		}

	}
}

public void collect( CoFormattedText destination, Collection sources )
{
	if
		( ( sources == null ) || ( destination == null ) )
	{
		return;
	}

	com.bluebrim.text.shared.CoStyledDocumentIF tmp = destination.createMutableStyledDocument();

	List paragraphSequences = new ArrayList();

	// collect paragraph sequences
	Iterator iter = sources.iterator ();
	while
		( iter.hasNext () )
	{
		collectDocument( ( (CoFormattedText) iter.next() ).createStyledDocument(), paragraphSequences );
	}

	// insert paragraph sequences in ascending order
	while
		( paragraphSequences.size() > 0 )
	{
		ParagraphSequence p = null;

		int I = paragraphSequences.size();
		for
			( int i = 0; i < I; i++ )
		{
			ParagraphSequence p2 = (ParagraphSequence) paragraphSequences.get( i );
			if
				( ( p == null ) || ( p2.m_order < p.m_order ) )
			{
				p = p2;
			}
		}
		
		paragraphSequences.remove( p );

		insertParagraphs( p, tmp );
	}


	int l = tmp.getLength();
	if
		( l > 0 )
	{
		try
		{
			tmp.remove( l - 1, 1 );
		}
		catch ( BadLocationException e )
		{
		}
	}

	destination.from( tmp );
}

private void collectDocument( com.bluebrim.text.shared.CoImmutableStyledDocumentIF doc, List paragraphSequences )
{
	Element section = doc.getRootElements()[ 0 ];
	if ( section.getEndOffset() == 1 ) return;

	int paragraphCount = section.getElementCount();

	int from = 0; // start of sequence
	int to = 0;   // end of sequence

	while
		( from < paragraphCount )
	{
		int order = -1;
		
		// find paragraph sequences
		while
			( to < paragraphCount )
		{		
			Element paragraph = section.getElement( to );
			Integer tmp = (Integer) paragraph.getAttributes().getAttribute( com.bluebrim.text.shared.CoDocumentDistributer.PARAGRAPH_ORDER );

			if
				( tmp == null )
			{
				to++;
				continue;
			}

			if
				( order == -1 )
			{
				order = tmp.intValue();
				to++;
				continue;
			}

			if
				( order == tmp.intValue() )
			{
				to++;
				continue;
			}

			break;
		}

		// store sequence
		ParagraphSequence p = new ParagraphSequence( order );
		paragraphSequences.add( p );
		for
			( int i = from; i < to; i++ )
		{
			p.m_paragraphs.add( section.getElement( i ) );
		}
		
		from = to;
	}

}
}