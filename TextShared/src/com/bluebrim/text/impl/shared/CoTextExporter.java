package com.bluebrim.text.impl.shared;

import java.io.*;
import java.util.*;

import javax.swing.text.*;

import com.bluebrim.text.shared.*;

/**
 * Class for exporting a com.bluebrim.text.shared.CoStyledDocumentIF in the BlueBrim native format.
 * PENDING: do we realy need this ???
 *
 * @author Dennis Malmström
 *
 * @deprecated Use {@link com.bluebrim.report.impl.server.CoXmlTextExporter} instead.
 */

public class CoTextExporter extends CoAbstractTextExporter
{
	private PrintWriter m_printWriter; // destination of export
/**
 * @deprecated Use {@link com.bluebrim.report.impl.server.CoXmlTextExporter} instead.
 */
public CoTextExporter( OutputStream stream )
{
	this( new PrintWriter( stream, true ) );
}
/**
 * @deprecated Use {@link com.bluebrim.report.impl.server.CoXmlTextExporter} instead.
 */
public CoTextExporter( PrintWriter writer )
{
	m_printWriter = writer;
}
/**
 * @deprecated Use {@link com.bluebrim.report.impl.server.CoXmlTextExporter} instead.
 */
public void doExport( com.bluebrim.text.shared.CoStyledDocumentIF doc )
{
	export( doc, m_printWriter );
}
/**
 * @deprecated Use {@link com.bluebrim.report.impl.server.CoXmlTextExporter} instead.
 */
private void export( com.bluebrim.text.shared.CoStyledDocumentIF doc, PrintWriter out )
{
	// traverse paragraph elements
  Element section = doc.getRootElements()[ 0 ];
  int paragraphCount = section.getElementCount();
  for
  	( int i = 0; i < paragraphCount; i++ )
  {
		Element paragraph = section.getElement( i );
		AttributeSet paragraphAtts = paragraph.getAttributes();

		// paragraph tag
		String paragraphTag = (String) paragraphAtts.getAttribute( CoTextConstants.PARAGRAPH_TAG );
		if
		  ( paragraphTag != null )
		{
	  	out.print( "<" + paragraphTag + ">" );
		}

		// paragraph attributes
		Enumeration attrs = paragraphAtts.getAttributeNames();
		while
		  ( attrs.hasMoreElements() )
		{
		  Object attr = attrs.nextElement();
		  CoAttributeTranslator translator = CoAttributeTranslator.getTranslator( attr );
		  if ( translator == null ) continue;
	  	out.print( "<" + translator.getTag() + "=" + translator.value2string( paragraphAtts.getAttribute( attr ) ) + ">" );
		}

		  
		Map previous = new HashMap();
		String previousCharacterTag = null;
		Element run = null;
		 
		int runCount = paragraph.getElementCount();
		for
		  ( int n = 0; n < runCount; n++ )
		{
		  run = paragraph.getElement( n );

		  if
		  	( run.getName().equals( CoTextConstants.CommentElementName ) )
		  {
			  out.print( "<" + CoAttributeTranslator.COMMENT_TAG + "=" + run.getAttributes().getAttribute( CoTextConstants.COMMENT ) + "><\\" + CoAttributeTranslator.COMMENT_TAG + ">" );
			  continue;
		  }
		  
		  AttributeSet runAtts = run.getAttributes();


		  // collect attributes of current run
		  Map current = new HashMap();
		  String currentCharacterTag = (String) runAtts.getAttribute( CoTextConstants.CHARACTER_TAG );

		  attrs = runAtts.getAttributeNames();
		  while
		    ( attrs.hasMoreElements() )
		  {
	  	  Object attr = attrs.nextElement();
		    CoAttributeTranslator translator = CoAttributeTranslator.getTranslator( attr );
		    if ( translator == null ) continue;
				current.put( translator.getTag(), runAtts.getAttribute( attr ) );
		  }
		  

		  // print end tags for attributes that have changed or disappeared since previous run
		  if
		    ( previousCharacterTag != null )
		  {
				if
				  ( ( currentCharacterTag == null ) ||
					( ! currentCharacterTag.equals( previousCharacterTag ) ) )
				{
			      out.print( "<\\" + previousCharacterTag + ">" );
				}
		  }

		  Iterator e = previous.keySet().iterator();
		  while
		  	( e.hasNext() )
		  {
			  Object key = e.next();
			  Object previousValue = previous.get( key );
			  Object currentValue = current.get( key );
			  if
			    ( ( currentValue == null ) || ( ! currentValue.equals( previousValue ) ) )
			  {
			    out.print( "<\\" + key + ">" );
			  }
		  }

		  
		  
		  
		  // print tags for attributes that have changed or appeared since previous run
		  if
		    ( currentCharacterTag != null )
		  {
			if
			  ( ( previousCharacterTag == null ) ||
				( ! currentCharacterTag.equals( previousCharacterTag ) ) )
			{
		      out.print( "<" + currentCharacterTag + ">" );
			}
		  }

		  e = current.keySet().iterator();
		  while
		  	( e.hasNext() )
		  {
			  String tag = (String) e.next();
			  Object currentValue = current.get( tag );
			  Object previousValue = previous.get( tag );
			  if
			    ( ( previousValue == null ) || ( ! previousValue.equals( currentValue ) ) ) 
			  {
		      CoAttributeTranslator translator = CoAttributeTranslator.getTranslator( tag );
			    out.print( "<" + tag + "=" + translator.value2string( currentValue ) + ">" );
			  }
		  }



		  // print text
		  try
		  {
			  if
			  	(n + 1 == runCount)
			 	{
				  if
				  	(run.getStartOffset() + 1 != run.getEndOffset())
				  {
					  // do not write return or eof character, only text before
	 					String text = doc.getText( run.getStartOffset(), run.getEndOffset() - run.getStartOffset() - 1 );
		   			out.print( text );
				  }
			  } else {
	 				String text = doc.getText( run.getStartOffset(), run.getEndOffset() - run.getStartOffset() );
		   		out.print( text );
			  }
		  }
		  catch ( Throwable ex )
		  {
	//		ex.printStackTrace(System.out);  
	//	    out.print( "ERROR" );
		  }

		  // save attributes of current run
		  previous = current;
		  previousCharacterTag = currentCharacterTag;
		}

		  
		
		
		// print end tags for all attributes of previous run
		if
		  ( previousCharacterTag != null )
		{
		  out.print( "<\\" + previousCharacterTag + ">" );
		}

		Iterator e = previous.keySet().iterator();
		while
		  ( e.hasNext() )
		{
		  Object key = e.next();
		  out.print( "<\\" + key + ">" );
		}
	  
	  if (run != null) {
		  try
		  {
			  // write return or eof character
				String text = doc.getText( run.getEndOffset() - 1, 1);
		   		out.print( text );
		  }
		  catch ( Throwable ex )
		  {
//			ex.printStackTrace(System.out);  
//		    out.print( "ERROR" );
		  }
	  }
  }

  out.flush();
}
}