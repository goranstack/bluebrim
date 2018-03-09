package com.bluebrim.text.shared;
import java.util.*;

import javax.swing.text.*;


/**
 * A formatted text (source) can be distributed among a set of formatted texts (destinations).
 * The text is distributed paragaph by paragaph. Which destination receives a paragraph
 * is decided by comparing the paragraph tag to the destinations set of accepted tags.
 * Paragraph for which no destination can be found are distributed to a "leftovers" destination.
 * The distributed paragraphs are marked with an integer defining the order in which they were distributed.
 * This order marked is used when then distributed text is collected back into one document.
 *
 * Internally this class works with com.bluebrim.text.shared.CoStyledDocumentIF, not CoFormattedText.
 *
 * Example:
 *  source
 *   <ptag1>Slim Harpo
 *   <ptag2>Pat Hare
 *   <ptag3>Willie Johnsson
 *   <ptag4>Etta James
 *   John Brim
 *   <ptag1>Elmore James
 *
 * -->
 *
 *  destination1 (accepted tags = ptag1, ptag3)
 *   <ptag1>Slim Harpo
 *   <ptag3>Willie Johnsson
 *   <ptag1>Elmore James
 * 
 *  destination2 (accepted tags = ptag2)
 *   <ptag2>Pat Hare
 * 
 *  "leftovers" destination
 *   <ptag4>Etta James
 *   John Brim
 * 
 * @author: Dennis Malmström
 */
 
public abstract class CoDocumentDistributer
{

	public static final String PARAGRAPH_ORDER = "paragraph-order";

	





private CoDocumentDistributer()
{
	super();
}









protected static int addParagraphTo( Element paragraph, String tag, com.bluebrim.text.shared.CoStyledDocumentIF destination, int paragraphOrderCounter )
{
	int start = paragraph.getStartOffset();
	int end = paragraph.getEndOffset();
	
	int pos = destination.getLength();

	// append text
	try
	{
		destination.insertString( pos, paragraph.getDocument().getText( start, end - start ), null );
	}
	catch ( BadLocationException e )
	{
	}

	// set paragraph attributes
	MutableAttributeSet attr = new com.bluebrim.text.shared.CoSimpleAttributeSet( paragraph.getAttributes() );

	attr.removeAttribute( StyleConstants.ResolveAttribute );
	attr.removeAttribute( StyleConstants.NameAttribute );
	attr.addAttribute( PARAGRAPH_ORDER, new Integer( paragraphOrderCounter++ ) );
	
	destination.setParagraphAttributes( pos, 1, attr, false );
	destination.setParagraphTag( pos, 1, tag );

	// set character attributes
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

	return paragraphOrderCounter;
}

public static CoFormattedText distribute( CoFormattedText source, Collection destinations /* [ CoFormattedText ] */ )
{
	if
		( ( source == null ) || ( destinations == null ) )
	{
		return null;
	}



	// prepare
	Map tag2docMap = new HashMap();
	List destinationDocuments = new ArrayList();
	com.bluebrim.text.shared.CoStyledDocument leftovers = new com.bluebrim.text.shared.CoStyledDocument();
	int paragraphOrderCounter = 0;

	{
		Iterator i = destinations.iterator();
		while
			( i.hasNext () )
		{
			CoFormattedText ft = (CoFormattedText) i.next();
			com.bluebrim.text.shared.CoStyledDocumentIF doc = ft.createMutableStyledDocument();
			destinationDocuments.add( doc );

			List tags = ft.getAcceptedTags();
			if
				( tags == null )
			{
			} else {
				Iterator i2 = tags.iterator();
				while
					( i2.hasNext() )
				{
					String tag = (String) i2.next();
					if
						( tag2docMap.get( tag ) == null )
					{
						tag2docMap.put( tag, doc );
					}
				}
			}
		}
	}


	

	// traverse paragraphs
	Element section = source.createStyledDocument().getRootElements()[ 0 ];
	int paragraphCount = section.getElementCount();
	for
		( int i = 0; i < paragraphCount; i++ )
	{
		Element paragraph = section.getElement( i );

		// get paragraph tag
		String tag = (String) paragraph.getAttributes().getAttribute( CoTextConstants.PARAGRAPH_TAG );
		if ( tag == null ) tag = CoTextConstants.DEFAULT_TAG_NAME;

		// append paragraph
		paragraphOrderCounter = addParagraphTo( paragraph, tag, getDestinationDocument( tag, tag2docMap, leftovers ), paragraphOrderCounter );
	}

	// remove last character from each destination
	Iterator docs = destinationDocuments.iterator();
	Iterator iter = destinations.iterator();
	CoFormattedText destination;
	com.bluebrim.text.shared.CoStyledDocumentIF destinationDocument;
	while
		( iter.hasNext ( ) )
	{
		destination = (CoFormattedText) iter.next();
		destinationDocument = (com.bluebrim.text.shared.CoStyledDocumentIF) docs.next();
		
		int l = destinationDocument.getLength();
		if
			( l > 0 )
		{
			try
			{
				destinationDocument.remove( l - 1, 1 );
			}
			catch ( BadLocationException e )
			{
			}
		}

		destination.from( destinationDocument );
	}

	// return fallback destination
	return new CoFormattedText( leftovers );
}

protected static com.bluebrim.text.shared.CoStyledDocumentIF getDestinationDocument( String tag, Map tag2docMap, com.bluebrim.text.shared.CoStyledDocumentIF fallback )
{
	com.bluebrim.text.shared.CoStyledDocumentIF destination = null;
	
	destination = (com.bluebrim.text.shared.CoStyledDocumentIF) tag2docMap.get( tag );
	if
		( destination == null )
	{
		destination = fallback;
	}
	
	return destination;
}
}