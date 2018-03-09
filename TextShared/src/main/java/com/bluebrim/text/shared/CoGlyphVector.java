package com.bluebrim.text.shared;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.text.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.font.shared.*;
import com.bluebrim.text.impl.shared.*;

/**
 * Wraps and replaces java.awt.font.GlyphVector.
 *  Features:
 *  - rendering and measuring using tracking, pair kerning and tracking curves.
 *  - caching of glyph geometry for improved performance.
 *  - rendering of format characters (tabs, end of paragraph, breakpoints, ...)
 *
 * @author: Dennis Malmström
 */

public class CoGlyphVector implements CoHyphenationConstants
{
	public static final char HYPHEN_CHARACTER = CoUnicode.HYPHEN_MINUS;
	public static final String HYPHEN_STRING = "" + HYPHEN_CHARACTER;

	public static final char END_OF_PARAGRAPH_CHARACTER = CoUnicode.PILCROW_SIGN;
	public static final String END_OF_PARAGRAPH_STRING = "" + END_OF_PARAGRAPH_CHARACTER;

	private Segment m_segment = new Segment();

	private char m_prefix; // character before text
	private char m_suffix; // character after text

	private float m_advances []; // glyph advance cache

	private CoFont m_font;

	private float m_spaceWidth = Float.NaN;
	private float m_hyphenWidth = Float.NaN;

	private Map m_tabStringWidths = new HashMap();


  private static Shape m_breakPointShape;
  private static Color m_breakPointColor = Color.green.darker();
  private static Color m_noBreakPointColor = Color.red;
  private static Shape m_tabShape;
  private static Stroke m_tabStroke = new BasicStroke( 1 );
  static
  {
	  GeneralPath gp = new GeneralPath();
	  gp.moveTo( -1, 0 );
	  gp.lineTo( -1, 4 );
	  gp.lineTo(  3, 4 );
	  gp.closePath();
	  m_breakPointShape = gp;

	  gp = new GeneralPath();
	  gp.moveTo( 0, -1 );
	  gp.lineTo( 0, 2 );
	  gp.moveTo( -2, 2 );
	  gp.lineTo( 2, 2 );
  m_tabShape = gp;
  }

  private static Rectangle2D m_lineShape = new Rectangle2D.Double();;

	private float m_kernAboveSize = 0;

;

public CoGlyphVector()
{
}

private void calculateWidths()
{
 m_spaceWidth = m_font.getAdvance( ' ' );
 m_hyphenWidth = m_font.getAdvance( HYPHEN_CHARACTER );
}
// update glyph vector if nesseccary

public void checkSegment(Segment s)
{
	boolean diff = ( m_segment == null ) || ( m_segment.array == null ) || ( s.count != m_segment.count );

	if
		( ! diff )
	{
		for
			( int i = 0; i < s.count; i++ )
		{
			if
				( s.array[ s.offset + i ] != m_segment.array[ m_segment.offset + i ] )
			{
				diff = true;
				break;
			}
		}
	}
	
	if
		( diff )
	{
		setSegment( s );
	} else {
//		System.err.println( "check -> segment not set" );
	}
}


public float getAdvance(int i)
{
	if
		( m_advances[i] == 0 )
	{
		char c = getChar( i );

		if
			( ( c == HYPHENATION_POINT_CHARACTER ) || ( c == ANTI_HYPHENATION_POINT_CHARACTER ) || ( c == '\t' ) )
		{
			m_advances[ i ] = 0;
		} else if
			( c == ' ' )
		{
			m_advances[ i ] = getSpaceWidth();
		} else {
			m_advances[ i ] = m_font.getAdvance( c );
			
			if ( m_font.getFontSize() >= m_kernAboveSize ) m_advances[ i ] += m_font.pairKern( c, ( i + 1 < getCharCount() ) ? getChar( i + 1 ) : m_suffix );
		}
	}

	return m_advances[ i ];
}
public char getChar( int i )
{
	return m_segment.array[ m_segment.offset + i ];
}
public int getCharCount()
{
 return m_segment.count;
}
public CoFont getFont()
{
 return m_font;
}
public float getHyphenWidth()
{
 if
  ( Float.isNaN( m_spaceWidth ) )
 {
  calculateWidths();
 }

 return m_hyphenWidth;
}
public float getSpaceWidth()
{
 if
  ( Float.isNaN( m_spaceWidth ) )
 {
  calculateWidths();
 }

 return m_spaceWidth;
}
public final int getTabbedTextOffset( float x0,
																			float x1,
																			CoTabExpander e,
																			int startOffset,
																			boolean round,
																			float tracking,
																			float spacePadding,
																			int offset,
																			int count )
{
	float currX = x0;
	float nextX = currX;

	tracking = m_font.calculateTracking( tracking );

	int i0 = offset;
	int I = offset + count;

	for
		( int i = i0; i < I; i++ )
	{
		char c = getChar(i);
		if
			( c == '\t' )
		{
			if
				( e != null )
			{
				nextX = e.nextTabStop( nextX, startOffset + i - i0 );
			} else {
				nextX += getSpaceWidth();
			}
		} else {
			nextX += getAdvance( i );

			if
				( getChar( i ) == ' ' )
			{	
				nextX += spacePadding + tracking;
			} else {
				nextX += tracking;
			}
		}

		if
			( ( x1 >= currX ) && ( x1 < nextX ) )
		{
			// found the hit position... return the appropriate side
			if
				( ( ! round ) || ( ( x1 - currX ) < ( nextX - x1 ) ) )
			{
				i = i - offset;
			} else {
				i = i - offset + 1;
			}

			return i;
		}
		currX = nextX;
	}

	// didn't find, return end offset
	return count;
}
public final float getTabbedTextWidth(  float x0,
																				CoTabExpander e,
																				int startOffset,
																				float tracking,
																				float spacePadding,
																				int offset,
																				int count )
{
	if ( count == 0 ) return 0;

//	System.err.print( offset + " " + count + " " + tracking + " " + spacePadding );
//	int u = 0;
	
	tracking = m_font.calculateTracking( tracking );

	float x = x0;

	int i0 = offset;
	int I = offset + count;

	for
		( int i = i0; i < I; i++ ) 
	{
		char c = getChar(i);
		if
			( c == '\t' )
		{
			if
				( e != null )
			{
				x = e.nextTabStop( x, startOffset + i - i0 );
			} else {
				x += getSpaceWidth();
			}
		} else if
			( c != '\n' )
		{
			x += getAdvance( i );

			if
				( c == ' ' )
			{
				x += spacePadding + tracking;
//				u++;
			} else {
				x += tracking;
			}
		}
	}
//	System.err.println( " -> " + ( x - x0 ) + "   " + u );

	return x - x0;
}

protected void invalidate()
{
 m_spaceWidth = Float.NaN;
 m_hyphenWidth = Float.NaN;
 if ( m_advances != null ) java.util.Arrays.fill( m_advances, 0 );
}
public void reset()
{
	m_font = null;

	m_segment.array = null;
	m_prefix = '\0';
	m_suffix = '\0';
	m_advances = null;
	
	m_spaceWidth = Float.NaN;
	m_hyphenWidth = Float.NaN;

	m_tabStringWidths.clear();
}
public void setFont( CoFont font )
{
 boolean isSameFont = true;

 Font f = font.getAwtFont();

 if
  ( ( m_font == null ) || ! f.equals( m_font.getAwtFont() ) )
 {
  isSameFont = false;
 } else {
  AffineTransform t0 = f.getTransform();
  AffineTransform t1 = m_font.getAwtFont().getTransform();
  if ( ( t0 == null ) && ( t1 != null ) ) isSameFont = false;
  else if ( ( t0 != null ) && ( t1 == null ) ) isSameFont = false;
  else if ( ! t0.equals( t1 ) ) isSameFont = false;
 }

 if ( isSameFont ) return;

 m_font = font;
 m_spaceWidth = Float.NaN;
 m_hyphenWidth = Float.NaN;

 m_tabStringWidths.clear();

 invalidate();
}
// set new text

public void setSegment( Segment s )
{
	m_segment.count = s.count;
	m_segment.offset = 0;//s.offset;
	m_segment.array = new char [ m_segment.count ];
	System.arraycopy( s.array, s.offset, m_segment.array, 0, s.count );

	if
		( m_segment.offset == 0 )
	{
		m_prefix = 'a'; // any non whitespace
	} else {
		m_prefix = m_segment.array[ m_segment.offset - 1 ];
	}

	if
		( m_segment.offset + m_segment.count < m_segment.array.length )
	{
		m_suffix = m_segment.array[ m_segment.offset + m_segment.count ];
	} else {
		m_suffix = '\0';
	}

	if ( ( m_advances == null ) || ( m_advances.length < m_segment.count ) ) m_advances = new float[ m_segment.count ];

	invalidate();
}
public String toString()
{
 return "GV: " + m_segment;
}









public void setKernAboveSize( float kas )
{
	if ( kas == m_kernAboveSize ) return;
	m_kernAboveSize = kas;
	invalidate();
}

public void draw( CoPaintable g, float x, float y, int offset )
{
	char c = getChar( offset );
	if ( c != CoUnicode.ZERO_WIDTH_SPACE ) g.drawChar( c, x, y );
}

public final float drawTabbedText(	float X,
																		float Y,
																		CoPaintable g,
																		CoTabExpander e,
																		int startOffset,
																		boolean broken,
																		float tracking,
																		float spacePadding,
																		int offset,
																		int count,
																		boolean strikeThrough,
																		boolean underline,
																		boolean wordUnderline)
{
//	System.err.println( m_font.getAwtFont() );
//	System.err.println( g.getGraphics2D().getFont() );
//	System.err.println();
//System.err.println( X + " " + Y + " " + strikeThrough );
	boolean doPaintFormatCharacters = ( g.getRenderingHint(CoTextRenderingHints.PAINT_FORMAT_CHARACTERS) == CoTextRenderingHints.PAINT_FORMAT_CHARACTERS_ON );

	tracking = m_font.calculateTracking( tracking );

	float x = X;
	float y = Y;

	float wx0 = Character.isWhitespace( m_prefix ) ? Float.NaN : x;
	float ulH = m_font.getLineMetrics().getUnderlineThickness();
	float ulDy = - m_font.getLineMetrics().getUnderlineOffset() - ulH / 2;
	

	float nextX = x;

	int i0 = offset;
	int I = offset + count;

	int flushIndex = i0;

	char c = '\0';
	for
		( int i = i0; i < I; i++ )
	{
		c = getChar(i);

		if
			( wordUnderline )
		{
			if
				( Character.isWhitespace( c ) )
			{
				m_lineShape.setRect( wx0, y + ulDy, x - wx0, ulH );
				g.fill( m_lineShape );
				wx0 = Float.NaN;
			} else {
				if ( Float.isNaN( wx0 ) ) wx0 = x;
			}
		}

		if
			( c == '\t' )
		{
			// tab			
			flushIndex = i + 1;
			if
				( e != null )
			{
				nextX = e.nextTabStop(nextX, startOffset + i - i0);
				e.drawTab(g, this, y, x, nextX, tracking);
			} else {
				nextX += getSpaceWidth();
			}

			if
				( doPaintFormatCharacters )
			{
				Stroke s = g.getStroke();
				g.setStroke(m_tabStroke);
				g.translate(x, y);
				g.draw(m_tabShape);
				g.translate(-x, -y);
				g.setStroke(s);
			}

			x = nextX;

		} else if
			( ( c == '\n' ) || ( c == '\r' ) )
		{
			// new line
			flushIndex = i + 1;
			x = nextX;

			if
				( c == '\n' && doPaintFormatCharacters )
			{
				g.drawChar( END_OF_PARAGRAPH_CHARACTER, x + 2, y );
			}

		} else if
			( c == HYPHENATION_POINT_CHARACTER )
		{
			// breakpoint
			flushIndex = i + 1;
			if
				( doPaintFormatCharacters )
			{
				Paint p = g.getPaint();
				g.setColor(m_breakPointColor);
				g.translate(x, y);
				g.fill(m_breakPointShape);
				g.translate(-x, -y);
				g.setPaint(p);
			}

		} else if
			(c == ANTI_HYPHENATION_POINT_CHARACTER)
		{
			// negative breakpoint
			flushIndex = i + 1;
			if
				(doPaintFormatCharacters)
			{
				Paint p = g.getPaint();
				g.setColor(m_noBreakPointColor);
				g.translate(x, y);
				g.fill(m_breakPointShape);
				g.translate(-x, -y);
				g.setPaint(p);
			}
		} else if
			( c == ' ' )
		{
			flushIndex = i + 1;

			nextX += getSpaceWidth() + tracking + spacePadding;
			/*
m_lineShape.setRect( x, y -2, nextX-x, 2);
g.draw(m_lineShape);
m_lineShape.setRect( x, y , nextX-x - spacePadding, 2);
g.draw(m_lineShape);
*/
			x = nextX;
		} else {
			nextX += getAdvance(i) + tracking;

			draw(g, x, y, flushIndex);
			
			flushIndex = i + 1;
			x = nextX;
		}
	}


	
	if
		(wordUnderline && !Float.isNaN(wx0))
	{
		m_lineShape.setRect(wx0, y + ulDy, x - wx0, ulH);
		g.fill(m_lineShape);
	}

	if
		( broken )
	{
		g.drawChar(HYPHEN_CHARACTER, (nextX - tracking), y);
		nextX += getHyphenWidth();
	}

	if
		(strikeThrough)
	{
		float sth = m_font.getLineMetrics().getStrikethroughThickness();
		m_lineShape.setRect(
			X,
			Y - m_font.getLineMetrics().getStrikethroughOffset() - sth / 2,
			nextX - X,
			sth
		);
		g.fill(m_lineShape);
	}

	if
		(underline)
	{
		m_lineShape.setRect(X, Y + ulDy, nextX - X, ulH);
		g.fill(m_lineShape);
	}

	if
		(m_font.getFace().isRemoved())
	{
		Color color = g.getColor();
		g.setColor(new Color(1, 0, 0, 0.5f));
		m_lineShape.setRect(X, Y - m_font.getLineMetrics().getAscent(), nextX - X, m_font.getLineMetrics().getHeight());
		g.fill(m_lineShape);
		g.setColor(color);
	}

	if
		(m_font.getFace().hasNewerVersion())
	{
		Color color = g.getColor();
		g.setColor(new Color(0, 1, 0, 0.5f));
		m_lineShape.setRect(X, Y - m_font.getLineMetrics().getAscent(), nextX - X, m_font.getLineMetrics().getHeight());
		g.fill(m_lineShape);
		g.setColor(color);
	}

	/* debugging stuff
	 g.setColor( Color.blue );
	 LineMetrics lm = m_font.getLineMetrics();
	 float X0 = X;
	 float X1 = ( X + nextX ) / 2;
	 g.draw( new Line2D.Float( X0, Y, X1, Y ) );
	 g.draw( new Line2D.Float( X0, Y + lm.getDescent(), X1, Y + lm.getDescent() ) );
	 g.draw( new Line2D.Float( X0, Y - lm.getAscent(), X1, Y - lm.getAscent() ) );
	 g.draw( new Line2D.Float( X0, Y - lm.getAscent(), X0, Y - lm.getAscent() + lm.getHeight() ) );
	*/
	return nextX;
}

public float getTabStringWidth( char c )
{
 return m_font.getAdvance( c );
}
}