package com.bluebrim.text.shared;

import java.io.*;
import java.util.*;

import javax.swing.text.*;

import com.bluebrim.xml.shared.*;


/**
 * A class for handling serialization and persistance of formatted text.
 * It has no protocoll for editing the text. Text editing must be performed on a com.bluebrim.text.shared.CoStyledDocument.
 * This class has methods for conversion to and from com.bluebrim.text.shared.CoStyledDocument.
 * 
 * Creation date: (2001-09-13 14:09:44)
 * @author: Dennis
 */
 
public class CoFormattedText implements java.io.Serializable, com.bluebrim.xml.shared.CoXmlEnabledIF
{
	public static final String XML_TAG = "formatted-text";
	// text
	private String m_text;

	// formatting
	private List m_paragraphs; // [ Paragraph ]

	// other persistant state
 	private List m_acceptedTags = new ArrayList();
  private long m_timeStamp = 0;

  // cached state derived from document
  private int m_wordCount;


  
	// styles
	private transient Map m_characterStyles; // [ String -> MutableAttributeSet ]
	private transient Map m_paragraphStyles; // [ String -> MutableAttributeSet ]

	// context
	private transient Map m_colors; // [ String -> com.bluebrim.paint.shared.CoColorIF ]
	private transient Map m_hyphenations; // [ String -> com.bluebrim.text.shared.CoHyphenationIF ]
	private transient List m_fontFamilies; // [ String ]
	private transient List m_tagChains; // [ com.bluebrim.text.shared.CoTagChainIF ]
	private transient Map m_macros; // [ String -> String ]
	private transient float m_kernAboveSize;
	private transient boolean m_useQxpJustification;



	private static class Run implements java.io.Serializable
	{
		public Run( Run r )
		{
			m_start = r.m_start;
			m_end = r.m_end;
			m_attributes = new com.bluebrim.text.shared.CoSimpleAttributeSet( r.m_attributes );
			m_tag = r.m_tag;
		}

		public Run( Element e )
		{
			m_start = e.getStartOffset();
			m_end = e.getEndOffset();
			AttributeSet as = e.getAttributes();
			m_attributes = new com.bluebrim.text.shared.CoSimpleAttributeSet( as );
			m_attributes.removeAttribute( StyleConstants.ResolveAttribute );
			m_attributes.removeAttribute( StyleConstants.NameAttribute );
			m_attributes.removeAttribute( StyleConstants.ModelAttribute );
			m_attributes.removeAttribute( StyleConstants.ComposedTextAttribute );
			m_tag = getTag( as );
		}

		protected String getTag( AttributeSet as )
		{
			return (String) as.getAttribute( CoTextConstants.CHARACTER_TAG );
		}

		int m_start;
		int m_end;
		String m_tag;
		com.bluebrim.text.shared.CoSimpleAttributeSet m_attributes;
	}



	private static class Paragraph extends Run
	{
		public Paragraph( Paragraph p )
		{
			super( p );

			Iterator i = p.m_runs.iterator();
			while
				( i.hasNext() )
			{
				m_runs.add( new Run( (Run) i.next() ) );
			}
		}

		public Paragraph( Element e )
		{
			super( e );

			StyledDocument doc = (StyledDocument) e.getDocument();
			int pos = m_start;
			
			while
				( pos < m_end )
			{
				Run r = new Run( doc.getCharacterElement( pos ) );
				m_runs.add( r );
				pos = r.m_end;
			}
		}

		protected String getTag( AttributeSet as )
		{
			return (String) as.getAttribute( CoTextConstants.PARAGRAPH_TAG );
		}
		
		List m_runs = new LinkedList(); // [ Run ]
	}


	
/**
 * CoFormatedText constructor comment.
 */
public CoFormattedText() {
	super();
}
public CoFormattedText( com.bluebrim.text.shared.CoImmutableStyledDocumentIF doc )
{
	from( doc );
}
public void clear()
{
	m_text = null;
	m_paragraphs = null;
}
/**
 * Try to fix non functional macro expansion by excluding the
 * lines dealing with that from the <code>m_characterStyles != null</code> true block.
 * Must admit I don't understand why some other lines is doing in true block. Perhaps
 * they should be excluded as well.
 * Göran Stäck 2002-09-02
 */
public com.bluebrim.text.shared.CoStyledDocumentIF createMutableStyledDocument()
{
	com.bluebrim.text.shared.CoStyledDocument doc = new com.bluebrim.text.shared.CoStyledDocument();

	if
		( m_characterStyles != null )
	{
	  // add character styles
	  Iterator i = m_characterStyles.entrySet().iterator();
	  while
	  	( i.hasNext() )
	  {
		  Map.Entry e = (Map.Entry) i.next();
			String name = (String) e.getKey();
			doc.addCharacterTag( name ).addAttributes( (AttributeSet) e.getValue() );
	  }

	  // add missing paragraph tags
	  i = m_paragraphStyles.entrySet().iterator();
	  while
	  	( i.hasNext() )
	  {
		  Map.Entry e = (Map.Entry) i.next();
			String name = (String) e.getKey();
			doc.addParagraphTag( name ).addAttributes( (AttributeSet) e.getValue() );
	  }

		// Update colors
		doc.setNameToColorMap( m_colors );

		// Update hyphenations
		doc.setNameToHyphenationMap( m_hyphenations );

		// Update fonts
		doc.setFontFamilies(m_fontFamilies);

		// Update tag chains
		doc.setChains( m_tagChains );

		doc.setKernAboveSize( m_kernAboveSize );
		doc.setUseQxpJustification( m_useQxpJustification );
	}

		doc.setMacros( m_macros );
		doc.expandMacros();

	doc.setAcceptedTags( m_acceptedTags );


	if
		( m_text != null )
	{
		try
		{
			doc.insertString( 0, m_text, null );
		}
		catch ( BadLocationException ex )
		{
		}

		Iterator paragraphs = m_paragraphs.iterator();
		while
			( paragraphs.hasNext() )
		{
			Paragraph p = (Paragraph) paragraphs.next();
			doc.setParagraphAttributes( p.m_start, 0,p.m_attributes, true );
			if ( p.m_tag != null ) doc.setParagraphTag( p.m_start, 0, p.m_tag );

			Iterator runs = p.m_runs.iterator();
			while
				( runs.hasNext() )
			{
				Run r = (Run) runs.next();
				if
					( runs.hasNext() || paragraphs.hasNext() )
				{
					doc.setCharacterAttributes( r.m_start, r.m_end - r.m_start, r.m_attributes, true );
					if ( r.m_tag != null ) doc.setCharacterTag( r.m_start, r.m_end - r.m_start, r.m_tag );
				}
			}
		}
	}


	return doc;
}
public com.bluebrim.text.shared.CoImmutableStyledDocumentIF createStyledDocument()
{
	return createMutableStyledDocument();
}
public CoFormattedText deepClone()
{
	CoFormattedText clone = new CoFormattedText();

	if
		( m_text != null )
	{
		clone.m_text = new String( m_text );

		clone.m_paragraphs = new ArrayList();
		Iterator i = m_paragraphs.iterator();
		while
			( i.hasNext() )
		{
			clone.m_paragraphs.add( new Paragraph( (Paragraph) i.next() ) );
		}
	}	

	clone.m_acceptedTags.addAll( m_acceptedTags );
	clone.m_timeStamp = m_timeStamp;

	clone.m_wordCount = m_wordCount;

	if ( m_characterStyles != null ) clone.m_characterStyles = new HashMap( m_characterStyles );
	if ( m_paragraphStyles != null ) clone.m_paragraphStyles = new HashMap( m_paragraphStyles );
	if ( m_colors != null ) clone.m_colors = new HashMap( m_colors );
	if ( m_hyphenations != null ) clone.m_hyphenations = new HashMap( m_hyphenations );
	if ( m_macros != null ) clone.m_macros = new HashMap( m_macros );
	if ( m_fontFamilies != null ) clone.m_fontFamilies = new ArrayList( m_fontFamilies );
	if ( m_tagChains != null ) clone.m_tagChains = new ArrayList( m_tagChains );
	clone.m_useQxpJustification = m_useQxpJustification;
	clone.m_kernAboveSize = m_kernAboveSize;

	return clone;
}
// extract formatted text information from a com.bluebrim.text.shared.CoImmutableStyledDocumentIF

public void from( com.bluebrim.text.shared.CoImmutableStyledDocumentIF doc )
{
	int length = doc.getLength();
	
	try
	{
		m_text = doc.getText( 0, length );
	}
	catch ( BadLocationException ex )
	{
	}

	m_paragraphs = new LinkedList();

	int pos = 0;

	while
		( pos <= length )
	{
		Paragraph p = new Paragraph( doc.getParagraphElement( pos ) );
		m_paragraphs.add( p );
		pos = p.m_end;
	}

	m_acceptedTags = new ArrayList( doc.getAcceptedTags() );

	m_wordCount = doc.getWordCount();
}


public void from( String text )
{

	CoStyledDocument doc = new CoStyledDocument();
	try
	{
		doc.insertString( 0, text, null );
	}
	catch ( BadLocationException ex )
	{
	}
	from( doc );
}


public List getAcceptedTags()
{
	return m_acceptedTags;
}
public int getLength()
{
	return ( m_text == null ) ? 0 : m_text.length();
}
public String getText()
{
	return ( m_text == null ) ? "" : m_text;
}
public String getText( int start, int length )
{
	return ( m_text == null ) ? "" : m_text.substring( start, start + length );
}
public long getTimeStamp()
{
	return m_timeStamp;
}
public List getUsedParagraphTags()
{
	List l = new ArrayList();

	Iterator i = m_paragraphs.iterator();
	while
		( i.hasNext() )
	{
		Paragraph p = (Paragraph) i.next();
		if ( ( p.m_tag != null ) && ! l.contains( p.m_tag ) ) l.add( p.m_tag );
	}

	return l;
}
public int getWordCount()
{
	return m_wordCount;
}
private void readObject( ObjectInputStream s ) throws IOException, ClassNotFoundException
{
	s.defaultReadObject();
//System.err.println( "m_characterStyles" );
	m_characterStyles = (Map) s.readObject();
//System.err.println( "m_paragraphStyles" );
	m_paragraphStyles = (Map) s.readObject();
//System.err.println( "m_colors" );
	m_colors = (Map) s.readObject();
//System.err.println( "m_hyphenations" );
	m_hyphenations = (Map) s.readObject();
//System.err.println( "m_fontFamilies" );
	m_fontFamilies = (List) s.readObject();
//System.err.println( "m_tagChains" );
	m_tagChains = (List) s.readObject();
//System.err.println( "m_macros" );
	m_macros = (Map) s.readObject();
//System.err.println( "m_useQxpJustification" );
	m_useQxpJustification = s.readBoolean();
//System.err.println( "m_kernAboveSize" );
	m_kernAboveSize = s.readFloat();
//System.err.println( "DONE" );
}
public void setAcceptedTags( List l )
{
	m_acceptedTags.clear();
	m_acceptedTags.addAll( l );
}
public void setChains( List chains )
{
	m_tagChains = chains;
}
public void setCharacterStyles( Map s )
{
	m_characterStyles = s;
}
public void setColors( Map cc )
{
	m_colors = cc;
}
public void setFontFamilies(List fonts)
{
	m_fontFamilies = fonts;
}
public void setHyphenations( Map m )
{
	m_hyphenations = m;
}
public void setKernAboveSize( float kas )
{
	m_kernAboveSize = kas;
}
public void setMacros( Map macros )
{
	m_macros = macros;
}
public void setParagraphStyles( Map s )
{
	m_paragraphStyles = s;
}
public void setTimeStamp( long t )
{
	m_timeStamp = t;
}
public void setUseQxpJustification( boolean b )
{
	m_useQxpJustification = b;
}
private void writeObject( ObjectOutputStream s ) throws IOException
{
	s.defaultWriteObject();

	s.writeObject( m_characterStyles );
	s.writeObject( m_paragraphStyles );
	s.writeObject( m_colors );
	s.writeObject( m_hyphenations );
	s.writeObject( m_fontFamilies );
	s.writeObject( m_tagChains );
	s.writeObject( m_macros );
	s.writeBoolean( m_useQxpJustification );
	s.writeFloat( m_kernAboveSize );
}
/**
 * When the parser has created a sub model to this node, this method will
 * be called.  If the sub model has an identifier, it is passed to this
 * method.
 *
 * @param parameter A sub model identifier (can be <code>null</code>)
 *
 * @param subMmodel The sub model.  The <code>subModel</code> may be a
 * {@link java.io.InputStream InputStream}.  In that case, the actual model is a
 * binary chunk which can be read from that <code>InputStream</code>.
 * Another special case is when you have XML:ed a <code>Collection</code>.  In
 * that case the <code>subModel</code> will be an {@link java.util.Iterator Iterator}
 * from which the original <code>Collection</code> can be reconstructed.
 *
 * @see com.bluebrim.xml.shared.CoXmlVisitorIF#export(CoXmlWrapperFlavor, String, com.bluebrim.xml.shared.CoXmlExportEnabledIF)
 * @see com.bluebrim.xml.shared.CoXmlVisitorIF#export(String, Collection)
 * @see java.io.InputStream
 */
public void xmlAddSubModel(java.lang.String parameter, java.lang.Object subModel, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {}
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel( Object superModel, org.w3c.dom.Node node, com.bluebrim.xml.shared.CoXmlContext context ) throws com.bluebrim.xml.shared.CoXmlReadException 
{
	return new CoFormattedText( (com.bluebrim.text.shared.CoImmutableStyledDocumentIF) com.bluebrim.text.shared.CoStyledDocument.xmlCreateModel( superModel, node, context ) );
}
/**
 * This method is called after an object and all its sub-objects have
 * been read from an XML file.
 * <p>
 * Creation date: (2001-09-24 13:34:18)
 */
public void xmlImportFinished(org.w3c.dom.Node node, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {}
public void xmlVisit( com.bluebrim.xml.shared.CoXmlVisitorIF visitor ) throws com.bluebrim.xml.shared.CoXmlWriteException
{
	createStyledDocument().xmlVisit( visitor );
}

	private final static long serialVersionUID = 7083286764384940576L;

public Map getCharacterStyles()
{
	return m_characterStyles;
}

public Map getParagraphStyles()
{
	return m_paragraphStyles;
}
}