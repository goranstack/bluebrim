package com.bluebrim.text.shared.swing;
// JDK imports
import java.awt.*;
import java.awt.geom.*;
import java.lang.ref.*;
import java.util.*;
import java.util.List;

import javax.swing.event.*;
import javax.swing.text.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Text view used to display a run of text.
 * This view can be broken into instances of the inner class CoLabelView.Fragment
 * 
 * @author: Dennis Malmström
 */

public class CoLabelView extends View implements TabableView, CoPaintableView, com.bluebrim.text.shared.swing.CoHighlightableView, CoJustifiableView, CoBreakableView, CoReuseableView
{
	private static boolean TRACE_POOL = false;
	private static boolean TRACE_FRAGMENT_POOL = false;
	
	private Element m_element;
	
	protected CoEnumValue 	m_underline;
	protected boolean m_strikethru;
	protected boolean m_allCaps;
//	protected boolean m_smallCaps;
	protected boolean m_superior;
	protected boolean m_superscript;
	protected boolean m_subscript;

//	protected boolean m_outline;
	protected boolean m_shadow;
	protected float m_tracking;
	protected float m_baselineOffset;

	protected com.bluebrim.font.shared.CoFont m_font;
	protected com.bluebrim.font.shared.metrics.CoLineMetrics m_lineMetrics; // changed from LineMetrics by Magnus Ihse (magnus.ihse@appeal.se) (2001-08-28 16:29:45)
	protected Paint m_fg;

	protected CoTabExpander m_expander;
	protected float m_x = 42;
	protected Segment m_text;
	protected final CoGlyphVector m_glyphVector = new CoGlyphVector();

	protected boolean m_rightToLeft;

	protected int m_spaceCount;
	protected float m_spacePadding;

	
	
  	// flaggor som styr debug- och testbeteenden
 	public static Color VIEW_BORDER_COLOR = null;


 	private static SoftReference m_pool;
 	private transient SoftReference m_fragmentPool;




 	
	class Fragment extends View implements TabableView, CoPaintableView, CoHighlightableView, CoJustifiableView, CoBreakableView, CoReuseableView
	{
		private Element m_element;
	
		int m_spaceCount;
		float m_fspacePadding;

		protected short m_offset;
		protected short m_length;
		protected float m_x;
		protected boolean m_rightToLeft;

		public float getSpacePadding()
		{
			return m_fspacePadding;
		}

		protected void reset()
		{
			m_spaceCount = 0;
			m_fspacePadding = 0;

			m_offset = 0;
			m_length = 0;
			m_x = 0;
			m_rightToLeft = false;
		}

		protected void set( Element elem, int p0, int p1 )
		{
			m_element = elem;
			
	    m_offset = (short) ( p0 - elem.getStartOffset() );
	    m_length = (short) ( p1 - p0 );
			Document d = getDocument();
			if
				( d instanceof DefaultStyledDocument )
			{
				m_rightToLeft = ! ( (DefaultStyledDocument) d ).isLeftToRight( p0, p1 );
			}
		}

		
		public AttributeSet getAttributes()
		{
			return m_element.getAttributes();
		}
		
		public Document getDocument()
		{
			return m_element.getDocument();
		}
		
		public Element getElement()
		{
			return m_element;
		}



		public void release()
		{
			List fragmentPool = getFragmentPool();
			
			if
				( ! fragmentPool.contains( this ) )
			{	
				if ( TRACE_FRAGMENT_POOL ) System.err.println( "RELEASE " + this );
				fragmentPool.add( this );
			} else {
				com.bluebrim.base.shared.debug.CoAssertion.assertTrue( false, "" );
			}
		}

		

		public Fragment( Element elem, int p0, int p1 )
		{
	    super( null );
			set( elem, p0, p1 );
		}

		
		public String toString()
		{
			return "Fragment " + getStartOffset() + " -> " + getEndOffset();
		}


		

		public View createFragment( int p0, int p1 )
		{
			return CoLabelView.this.createFragment( p0, p1 );
		}
		
		public float getHyphenWidth()
		{
			return CoLabelView.this.getHyphenWidth();
		}

		public float getFontSize()
		{
			return CoLabelView.this.getFontSize();
		}

		public float getAscent()
		{
			return CoLabelView.this.getAscent();
		}

		public float getDescent()
		{
			return CoLabelView.this.getDescent();
		}
		
		public int getNextVisualPositionFrom( int pos, Position.Bias b, Shape a,  int direction, Position.Bias[] biasRet )  throws BadLocationException
		{
			return CoLabelView.this.getNextVisualPositionFrom( pos, b, a, direction, biasRet, m_rightToLeft, this.getStartOffset(), this.getEndOffset() );
		}

		public void changedUpdate(DocumentEvent e, Shape a, ViewFactory f)
		{
			CoLabelView.this.changedUpdate(e, a, f);
		}
		
		public float getAlignment(int axis)
		{
			return CoLabelView.this.getAlignment(axis);
		}



		
		
		public float setSpacePadding( float p, float minimumRelativeSpaceWidth ) throws CoJustifiableView.ToTight
		{
			sync();

			float f = p / m_glyphVector.getSpaceWidth();
			if
				( f < - minimumRelativeSpaceWidth )
			{
				throw CoJustifiableView.m_toTight;
			}
			
			m_fspacePadding = p;
			return m_fspacePadding * m_spaceCount;
		}


		
		public float getTabbedSpan( float x, TabExpander e )
		{
			CoLabelView.this.m_expander = (CoTabExpander) e;
			m_x = x;
			int p0 = this.getStartOffset();
			int p1 = this.getEndOffset();
			float s = doGetPreferredSpan( X_AXIS, p0, p1, m_x, m_fspacePadding, p0 - CoLabelView.this.getStartOffset(), p1 - p0 );
			return s;
		}
		
		public float getPartialSpan( int p0, int p1 )
		{
			return CoLabelView.this.doGetPartialSpan( m_x, p0, p1, m_fspacePadding, p0 - CoLabelView.this.getStartOffset(), p1 - p0 );
		}
		
		public float getMinimalPartialSpan( float x0, int p0, int p1, float minimumRelativeSpaceWidth )
		{
			sync();
			float w =  CoLabelView.this.doGetPartialSpan( x0, p0, p1, ( m_glyphVector.getSpaceWidth() * ( - minimumRelativeSpaceWidth ) ), p0 - CoLabelView.this.getStartOffset(), p1 - p0 );// / m_spaceCount );
			return w;
		}

		
			
		float getPreferredSpan(int axis, int p0, int p1, float x)
		{
			return doGetPreferredSpan( axis, p0, p1,  x, m_fspacePadding, p0 - CoLabelView.this.getStartOffset(), p1 - p0 );
		}
		
	  public float getPreferredSpan( int axis )
		{
			float span = getPreferredSpan(axis, this.getStartOffset(), this.getEndOffset(), m_x );

			if ( axis == Y_AXIS ) return span;

			View p = getParent();
if ( p == null ) System.err.println( "CHECK IF TRAILING WHEN PARENT = null " + this );
			boolean isTrailingView = ( p != null ) && ( this == p.getView( p.getViewCount() - 1 ) );
	  	
			if
				( isTrailingView )
			{
				float tracking = m_font.calculateTracking( m_tracking );
				
		 		int p0 = this.getStartOffset();
		  	int p1 = this.getEndOffset();
			  sync();
			  loadText( p0, p1, com.bluebrim.text.shared.CoTextUtilities.TMP_Segment );

			  int tc = com.bluebrim.text.shared.CoTextUtilities.TMP_Segment.count;
			  while
					( ( tc > 0 ) && ( Character.isWhitespace( com.bluebrim.text.shared.CoTextUtilities.TMP_Segment.array[ com.bluebrim.text.shared.CoTextUtilities.TMP_Segment.offset + tc - 1 ] ) ) )
				{
					if
						( com.bluebrim.text.shared.CoTextUtilities.TMP_Segment.array[ com.bluebrim.text.shared.CoTextUtilities.TMP_Segment.offset + tc - 1 ] != '\n' )
					{
						int d = p0 - CoLabelView.this.getStartOffset();
						span -= m_glyphVector.getAdvance( d + tc - 1 ) + tracking;
						
					}
					if
						( com.bluebrim.text.shared.CoTextUtilities.TMP_Segment.array[ com.bluebrim.text.shared.CoTextUtilities.TMP_Segment.offset + tc - 1 ] == ' ' )
					{
						span -= m_fspacePadding;
					}
					tc -= 1;
				}
				if ( tracking > 0 ) span -= tracking;
			}
			return span;
		}


		
		Shape modelToView( int pos, Shape a, int p0, int p1, Position.Bias bias, boolean rightToLeft ) throws BadLocationException
		{
			return doModelToView( pos, a, p0, p1, bias, rightToLeft, m_fspacePadding );
		}

		int viewToModel( float x, float y, Shape a, Position.Bias[] biasReturn, int p0, int p1, boolean rightToLeft )
		{
			return doViewToModel( x, y, a, biasReturn, p0, p1, rightToLeft, m_fspacePadding, p0 - CoLabelView.this.getStartOffset(), p1 - p0 );
		}


		
		public void paint( CoPaintable g, Shape a, boolean broken )
		{
			int p0 = this.getStartOffset();
			int p1 = this.getEndOffset();
			doPaint( g, a.getBounds2D(), p0, p1, broken, m_fspacePadding, p0 - CoLabelView.this.getStartOffset(), p1 - p0 );
		}
		
		public void paint( CoPaintable g, Shape a )
		{
			paint( g, a, false );
		}
		
		public void paint(Graphics2D g, Shape a, boolean broken )
		{
			CoPaintable p = (CoPaintable) g.getRenderingHint( CoScreenPaintable.PAINTABLE_KEY );
			paint( p, a, false );
		}
		
		public void paint( Graphics2D g, Shape a )
	  {
			this.paint( g, a, false );
	  }
	  
	  public void paintSelectionShadow( Graphics2D g, int from, int to, Shape a )
		{
			if ( from > getEndOffset() ) return;
			if ( to < getStartOffset() ) return;
				
			sync();
		  from = Math.max( from, this.getStartOffset() );
		  to = Math.min( to, this.getEndOffset() );

		  Rectangle2D alloc = a.getBounds2D();
		  try
		  {
			  Rectangle2D r0 = modelToView( from, alloc, Position.Bias.Forward ).getBounds2D();
			  Rectangle2D r1 = modelToView( to, alloc, Position.Bias.Forward ).getBounds2D();
				
			  double x = r0.getX();
			  double y = alloc.getY();
			  double w = r1.getX() - r0.getX() + r1.getWidth();
				double h = m_lineMetrics.getHeight();;
				m_rect.setRect( x, y, w, h );
				g.fill( m_rect );
		  }
		  catch ( BadLocationException e )
		  {
		  }
		}
		

		public int getStartOffset()
		{
	    return this.getElement().getStartOffset() + m_offset;
		}

		public int getEndOffset()
		{
	    return this.getElement().getStartOffset() + m_offset + m_length;
		}

		void dump()
		{
			System.err.println( "      fragment " + this.getStartOffset() + " -> " + this.getEndOffset() );
		}
		
		public void clearSpacePadding()
		{
			m_fspacePadding = 0;
		}
		
		public float getMinimalSpan( float x0, float minimumRelativeSpaceWidth )
		{
			return getMinimalPartialSpan( x0, getStartOffset(), getEndOffset(), minimumRelativeSpaceWidth );
		}
		
		public void paint( Graphics g, Shape a )
	  {
			this.paint( (Graphics2D) g, a );
	  }

		public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException
		{
			return modelToView( pos, a, this.getStartOffset(), this.getEndOffset(), b, m_rightToLeft );
		}

		public int viewToModel(float x, float y, Shape a, Position.Bias[] biasReturn)
		{
			biasReturn[0] = Position.Bias.Forward; // why
			return viewToModel(x, y, a, biasReturn, this.getStartOffset(), this.getEndOffset(), m_rightToLeft );
		}
		
		public int getBreakWeight(int axis, float x, float len )
		{
			throw new RuntimeException( "KNAS: CoLabelView.Fragment.getBreakWeight(int axis, float x, float len )" );
//			return getBreakWeight(axis, x, len, true );
		}
		
		public View breakView(int axis, int offset, float pos, float len )
		{
			throw new RuntimeException( "KNAS: CoLabelView.Fragment.breakView(int axis, int offset, float pos, float len )" );
//			return CoLabelView.this.breakView(axis, offset, pos, len, true );
		}

	}




	
	// -----------------------------------------------------------------------


	

	private DropCapsFragment m_dropCapsFragment = null;

	
	class DropCapsFragment extends Fragment
	{
		private int m_lines;
		private float m_xScale = 1.0f;
		private com.bluebrim.font.shared.CoFont m_dropCapsFont;
		private float m_leading;
		private CoGlyphVector m_glyphVector = new CoGlyphVector();

		protected void reset()
		{
			super.reset();
			
			m_lines = 0;
			m_xScale = 1.0f;
			m_dropCapsFont = null;
			m_leading = 0;
			m_glyphVector.reset();
		}
		
		protected void set( int p0, int p1, int lines, float leading )
		{
			super.set( CoLabelView.this.getElement(), p0, p1 );
			m_lines = lines;
			m_leading = leading;
			m_dropCapsFont	= calculateFont( (int) getPreferredSpan( Y_AXIS ) );
		}


		public void release()
		{
			this.setParent( null );
		}
		
		public DropCapsFragment( int p0, int p1, int lines, float leading, int xxx )
		{
			super( CoLabelView.this.getElement(), p0, p1 );

			m_lines = lines;
			m_leading = leading;
			m_dropCapsFont	= calculateFont( (int) getPreferredSpan( Y_AXIS ) );
		}


		
		public String toString()
		{
			return "CoDropCapsLabelFragment " + this.getStartOffset() + " -> " + this.getEndOffset();
		}

		
		public void paint( Graphics2D g, Shape a )
  	{
	  	paint( g, a, false );
  	}
		
		public void paint( CoPaintable g, Shape a, boolean broken )
  	{
			Rectangle2D alloc = a.getBounds2D();
			double	y = alloc.getY() + alloc.getHeight() - ( m_lineMetrics.getDescent() + (int) getDeltaY() ) * m_lines; //local

			int p0 = this.getStartOffset();
			int p1 = this.getEndOffset();

			doPaint( g,
	           alloc.getBounds2D(),
	           p0,
	           p1,
	           broken,
	           m_fspacePadding,
	           y,
	           m_dropCapsFont,
	           p0 - CoLabelView.this.getStartOffset(),
	           p1 - p0 );
  	}


  	
  	private com.bluebrim.font.shared.CoFont calculateFont( int maxHeight )
  	{
	  	if ( m_lines == 1 ) return getFont();

			com.bluebrim.font.shared.CoFont font = getFont();
			float s = font.getFontSize() * m_lines;
			float s0 = s;
			com.bluebrim.font.shared.metrics.CoLineMetrics m = font.getLineMetrics();
			while
				( maxHeight > m.getAscent() + m.getDescent() / 2 )
			{
				s	+= 1;
				font = font.getFace().getFont( s );
				m = font.getLineMetrics();
			}
			m_xScale = m_lines * ( s - 1 ) / s0;
			return font;
	 	}


  	
	  public float getPreferredSpan( int axis )
		{
			if ( axis == Y_AXIS ) return m_lines * ( m_leading + super.getPreferredSpan( axis ) );

			float span = 0.0f;
			int p0 = this.getStartOffset();
			int p1 = this.getEndOffset();
	  	sync();
			loadText( p0, p1, com.bluebrim.text.shared.CoTextUtilities.TMP_Segment );
			m_glyphVector.setSegment( com.bluebrim.text.shared.CoTextUtilities.TMP_Segment );
			m_glyphVector.setFont( m_dropCapsFont );
			for
				( int i = 0; i < m_glyphVector.getCharCount(); i++ )
			{
				span += m_glyphVector.getAdvance( i );
			}

			return span;
		}		


		
		public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException
		{
			Rectangle2D r = super.modelToView( pos, a, b ).getBounds2D();
			if
				( pos < this.getEndOffset() )
			{
				double x0 = a.getBounds2D().getX();
				r.setRect( x0 + ( m_xScale * ( r.getX() - x0 ) ),
					         r.getY(),
					         r.getWidth(),
					         ( r.getHeight() + m_leading ) * m_lines );
			}
			return r;
		}


		
		public int viewToModel( float x, float y, Shape a, Position.Bias[] b )
		{
			float x0 = (float) a.getBounds2D().getX();
			return super.viewToModel( x0 + ( x - x0 ) / m_xScale, y, a, b );
		}
	}


 	private static Rectangle2D m_rect = new Rectangle2D.Float();

	private int m_endOffsetOfSegment = -1;



//	protected boolean m_autoLeading;
//	protected boolean m_isLeadingAbsolute;
//	protected boolean m_isLeadingOffset;
//	protected boolean m_isLeadingRelative;
	protected CoLeading m_leading;
 	private int m_startOffsetOfSegment = -1;

	protected Paint m_shadowColor;
	protected double m_shadowOffsetX; // [ emspace / 200 ]
	protected double m_shadowOffsetY; // [ emspace / 200 ]




private CoLabelView(Element elem)
{
	super( null );
	
//	m_all.add( this );

	set( elem );
}


public View breakView(int axis, int p0, float pos, float len )
{
	throw new RuntimeException( "KNAS: CoLabelView.breakView( int axis, int p0, float pos, float len )" );
//	return breakView( axis, p0, pos, len, true );
}


float calcHeight()
{
	float h = m_lineMetrics.getAscent() + m_lineMetrics.getDescent();

	if ( m_leading != null ) return m_leading.calcHeight( h );

	return h;
}


/**
 * Gives notification from the document that attributes were changed
 * in a location that this view is responsible for.
 *
 * @param e the change information from the associated document
 * @param a the current allocation of the view
 * @param f the factory to use to rebuild if the view has children
 * @see View#changedUpdate
 */
public void changedUpdate(DocumentEvent e, Shape a, ViewFactory f)
{
	m_font = null;
}


public static void clearPools()
{
	m_pool = null;
}


public void clearSpacePadding()
{
	m_spacePadding = 0;
}


public static CoLabelView create( Element elem )
{
	CoLabelView v = null;

	if
		( getPool().isEmpty() )
	{
		v = new CoLabelView( elem );
		if ( TRACE_POOL ) System.err.println( "NEW " + v );
	} else {
		v = (CoLabelView) getPool().remove( getPool().size() - 1 );
		v.reset();
		v.set( elem );
		if ( TRACE_POOL ) System.err.println( "REUSING " + v );
	}
/*
	Exception ex = new Exception();

	java.io.ByteArrayOutputStream x = new java.io.ByteArrayOutputStream();
	java.io.PrintStream xx = new java.io.PrintStream( x );
	ex.printStackTrace( xx );
	v.m_stack = new String( x.toByteArray() );
*/
	return v;
}


public DropCapsFragment createDropCapsFragment( int p0, int p1, int lines, float leading )
{
	if ( p1 > getEndOffset() ) p1 = getEndOffset();
	
	if
		( m_dropCapsFragment == null )
	{
		m_dropCapsFragment = new DropCapsFragment( p0, p1, lines, lines, 0 );
	}

	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( m_dropCapsFragment.getParent() == null, "Illegal reuse of DropCapsFragment" );
	
	m_dropCapsFragment.reset();
	m_dropCapsFragment.set( p0, p1, lines, lines * leading );
	
	return m_dropCapsFragment;
}


public View createFragment( int p0, int p1 )
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( p0 >= getStartOffset(), "Label fragment create request out of range" );
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( p1 <= getEndOffset(), "Label fragment create request out of range" );

	Fragment f = null;

	List fragmentPool = getFragmentPool();
	
	if
		( fragmentPool.isEmpty() )
//		( true|| fragmentPool.isEmpty() )
	{
		f = new Fragment( getElement(), p0, p1 );
		if ( TRACE_FRAGMENT_POOL ) System.err.println( "NEW " + f );
	} else {
		f = (Fragment) fragmentPool.remove( fragmentPool.size() - 1 );
		f.reset();
		f.set( getElement(), p0, p1 );
		if ( TRACE_FRAGMENT_POOL ) System.err.println( "REUSING " + f );
	}

		return f;
}


float doGetPartialSpan( float x0, int p0, int p1, float spacePadding, int offset, int count )
{
	loadText( "doGetPartialSpan" );
	sync();
	float width = m_glyphVector.getTabbedTextWidth( m_x = x0, m_expander, p0, m_tracking, spacePadding, offset, count );
	return width;
}


private float doGetPreferredSpan(int axis, int p0, int p1, float x, float spacePadding, int offset, int count )
{
	sync();
	loadText( "doGetPreferredSpan" );

	
	switch
		(axis)
	{
		case View.X_AXIS :
			float width = m_glyphVector.getTabbedTextWidth( x, m_expander, p0, m_tracking, spacePadding, offset, count );
			return width;
			
		case View.Y_AXIS :
			return calcHeight();
			
		default :
			throw new IllegalArgumentException("Invalid axis: " + axis);
	}
}


Shape doModelToView( int pos,
	                 Shape a,
	                 int p0,
	                 int p1,
	                 Position.Bias bias,
	                 boolean rightToLeft,
	                 float spacePadding ) throws BadLocationException
{
	Rectangle alloc = a.getBounds();

	if
		(pos == p1)
	{
		sync();
//sutare		loadText( "doModelToView " +pos);
		if (!rightToLeft)
		{
			// The caller of this is left to right and borders a right to
			// left view, return our end location.
			return new Rectangle(alloc.x + alloc.width, alloc.y, 0, (int) m_lineMetrics.getHeight());
		}
		// The caller of this is right to left and borders a left to
		// right view.
		return new Rectangle(alloc.x, alloc.y, 0, (int) m_lineMetrics.getHeight());
	}
	
	if
		((pos >= p0) && (pos <= p1))
	{
		// determine range to the left of the position
		sync();
//SUTARE		loadText();
		int width = (int) m_glyphVector.getTabbedTextWidth( alloc.x, m_expander, p0, m_tracking, spacePadding, p0 - getStartOffset(), pos - p0 );
		
		if (rightToLeft)
		{
			// PENDING(Java2D) use real rendering with shaping
			return new Rectangle(alloc.x + alloc.width - width, alloc.y, 0, (int) m_lineMetrics.getHeight());
		}
		return new Rectangle(alloc.x + width, alloc.y, 0, (int) m_lineMetrics.getHeight());
	}
	throw new BadLocationException("modelToView - can't convert", p1);
}


private void doPaint( CoPaintable g,
	                  Rectangle2D alloc,
	                  int p0,
	                  int p1,
	                  boolean broken,
	                  float spacePadding,
	                  double y,
	                  com.bluebrim.font.shared.CoFont f,
	                  int offset,
	                  int count )
{
	/*
	if 
		( f.getSize2D() * com.bluebrim.base.shared.CoBaseUtilities.getYScale( g.getTransform() ) < 3 )
	{
		
		g.drawLine( (int) alloc.getX(), (int) y, (int) ( alloc.getX() + getMinimalSpan() ), (int) y );
	} else {
	*/
		sync();
//sutare		loadText();

		paintBorder( g, alloc );
		
		g.setFont( f );//.getAwtFont() );
		m_glyphVector.setFont( f );
		
		paintText( g, alloc.getX(), y, p0, broken, spacePadding, offset, count );

//	}
	
}


private void doPaint( CoPaintable g,
	                  Rectangle2D a,
	                  int p0,
	                  int p1,
	                  boolean broken,
	                  float spacePadding,
	                  int offset,
	                  int count )
{
//	double y = a.getY() + a.getHeight() - m_lineMetrics.getDescent() + getDeltaY();
	double y = a.getY() + m_lineMetrics.getAscent() + getDeltaY();
	doPaint( g, a, p0, p1, broken, spacePadding, y, m_font, offset, count );
}


private void doPaint(CoPaintable g, Shape a, boolean broken, int offset, int count )
{
	int p0 = getStartOffset();
	doPaint( g, a.getBounds2D(), p0, getEndOffset(), broken, m_spacePadding, offset, count );
}


/**
 * Provides a mapping from the view coordinate space to the logical
 * coordinate space of the model.
 *
 * @param x the X coordinate
 * @param y the Y coordinate
 * @param a the allocated region to render into
 * @param rightToLeft true if the text is rendered right to left
 * @return the location within the model that best represents the
 *  given point of view
 * @see View#viewToModel
 */
int doViewToModel( float x,
	               float y,
	               Shape a,
	               Position.Bias[] biasReturn,
	               int p0,
	               int p1,
	               boolean rightToLeft,
	               float spacePadding,
	               int offset,
	               int count )
{
	Rectangle alloc = a.getBounds();
	sync();
	loadText( "doViewToModel" );
	int offs = m_glyphVector.getTabbedTextOffset( alloc.x,
		                                          x,
		                                          m_expander,
		                                          p0,
		                                          true,
		                                          m_tracking,
		                                          spacePadding,
		                                          offset,
		                                          count );
	int retValue;
	
	if
		(rightToLeft)
	{
		// PENDING(Java2D) use real rendering with shaping
		retValue = p1 - offs;
	} else {
		retValue = p0 + offs;
	}
	
	if
		(retValue == p1)
	{
		biasReturn[0] = Position.Bias.Backward;
	} else {
		biasReturn[0] = Position.Bias.Forward;
	}
	
	return retValue;
}


void dump()
{
	System.err.println( "       label   " + getStartOffset() + " -> " + getEndOffset() );
}


public static void dumpPools()
{
	/*
	Iterator i = m_all.iterator();
	while
		( i.hasNext() )
	{
		m_allFragments.removeAll( ( (CoLabelView) i.next() ).m_fragmentPool );
	}

	m_all.removeAll( m_pool );

	System.err.println( "Leaked " + m_all.size() + " label views" );
	if ( ! m_all.isEmpty() ) System.err.println( m_all );
	
	System.err.println( "Leaked " + m_allFragments.size() + " label fragments views" );
	if ( ! m_allFragments.isEmpty() ) System.err.println( m_allFragments );
	*/
}


/**
 * Determines the desired alignment for this view along an
 * axis.  For the label, the alignment is along the font
 * baseline for the y axis, and the superclasses alignment
 * along the x axis.
 *
 * @param axis may be either View.X_AXIS or View.Y_AXIS
 * @returns the desired alignment.  This should be a value
 *   between 0.0 and 1.0 inclusive, where 0 indicates alignment at the
 *   origin and 1.0 indicates alignment to the full span
 *   away from the origin.  An alignment of 0.5 would be the
 *   center of the view.
 */
public float getAlignment(int axis)
{
	if
		(axis == View.Y_AXIS)
	{
		// does it ever land here ????
		float h = m_lineMetrics.getHeight();
		float d = m_lineMetrics.getDescent();
		float align = (h - d) / h;
		return align;
	}
	return super.getAlignment(axis);
}


public float getAscent()
{
	sync();
	return m_lineMetrics.getAscent();
}


public AttributeSet getAttributes()
{
	return m_element.getAttributes();
}


public int getBreakWeight(int axis, float pos, float len)
{
	throw new RuntimeException( "KNAS: CoLabelView.getBreakWeight( int axis, float pos, float len )" );
}


private float getDeltaY()
{
	int dy = 0;
	if
		( m_superscript )
	{
		if
			( m_superior )
		{
			dy -= m_lineMetrics.getAscent() * 1.5;
		} else {
			dy -= m_lineMetrics.getAscent() / 2;
		}
	} else if
		( m_subscript )
	{
		if
			( m_superior )
		{
			dy += m_lineMetrics.getDescent() / 2;
		} else {
			dy += m_lineMetrics.getDescent();
		}
	} else {
		if
			( m_superior )
		{
			dy -= m_lineMetrics.getAscent();
		}
	}

	return dy;
}


public float getDescent()
{
	sync();
	return m_lineMetrics.getDescent();
}


public Document getDocument()
{
	return m_element.getDocument();
}


public Element getElement()
{
	return m_element;
}


public int getEndOffset()
{
	return m_element.getEndOffset();
}


/**
 * Fetch the Font used for this view.
 */
protected com.bluebrim.font.shared.CoFont getFont()
{
	sync();
	return m_font;
}


public float getFontSize()
{
	sync();
	return m_font.getAwtFont().getSize2D();
}


private List getFragmentPool()
{
	if
		( ( m_fragmentPool == null ) || ( m_fragmentPool.get() == null ) )
	{
		m_fragmentPool = new SoftReference( new ArrayList() );
	}

	return (List) m_fragmentPool.get();
}


public float getHyphenWidth()
{
	sync();
	
	return m_glyphVector.getHyphenWidth();
}


public float getMinimalPartialSpan( float x0, int p0, int p1, float minimumRelativeSpaceWidth )
{
	sync();
//	float w = doGetPartialSpan( x0, p0, p1, ( m_glyphVector.getSpaceWidth() * m_minimumSpaceStretch ), 0, p1 - p0 );/// m_spaceCount );
	float w = doGetPartialSpan( x0, p0, p1, ( m_glyphVector.getSpaceWidth() * ( - minimumRelativeSpaceWidth ) ), 0, p1 - p0 );/// m_spaceCount );
	return w;
}


public float getMinimalSpan( float x0, float minimumRelativeSpaceWidth )
{
	return getMinimalPartialSpan( x0, getStartOffset(), getEndOffset(), minimumRelativeSpaceWidth );
}


/**
 * Provides a way to determine the next visually represented model
 * location that one might place a caret.  Some views may not be
 * visible, they might not be in the same order found in the model, or
 * they just might not allow access to some of the locations in the
 * model.
 *
 * @param pos the position to convert >= 0
 * @param a the allocated region to render into
 * @param direction the direction from the current position that can
 *  be thought of as the arrow keys typically found on a keyboard.
 *  This may be SwingConstants.WEST, SwingConstants.EAST, 
 *  SwingConstants.NORTH, or SwingConstants.SOUTH.  
 * @return the location within the model that best represents the next
 *  location visual position.
 * @exception BadLocationException
 * @exception IllegalArgumentException for an invalid direction
 */
public int getNextVisualPositionFrom(int pos, Position.Bias b, Shape a, int direction, Position.Bias[] biasRet) throws BadLocationException
{
	return getNextVisualPositionFrom(pos, b, a, direction, biasRet, m_rightToLeft, getStartOffset(), getEndOffset());
}


/**
 * Provides a way to determine the next visually represented model
 * location that one might place a caret.  Some views may not be
 * visible, they might not be in the same order found in the model, or
 * they just might not allow access to some of the locations in the
 * model.
 *
 * @param pos the position to convert >= 0
 * @param a the allocated region to render into
 * @param direction the direction from the current position that can
 *  be thought of as the arrow keys typically found on a keyboard.
 *  This may be SwingConstants.WEST, SwingConstants.EAST, 
 *  SwingConstants.NORTH, or SwingConstants.SOUTH.  
 * @param rightToLeft true if the view is rendered right to left
 * @param startOffset starting offset into the model the view
 *        represents
 * @param endOffset starting offset into the model the view
 *        represents
 * @return the location within the model that best represents the next
 *  location visual position.
 * @exception BadLocationException
 * @exception IllegalArgumentException for an invalid direction
 */
int getNextVisualPositionFrom( int pos, Position.Bias b, Shape a, int direction, Position.Bias[] biasRet, boolean rightToLeft, int startOffset, int endOffset) throws BadLocationException
{
	switch
		( direction )
	{
		case NORTH :
			break;
			
		case SOUTH :
			break;
			
		case EAST :
			if 
				(startOffset == getDocument().getLength())
			{
				if
					(pos == -1)
				{
					biasRet[0] = Position.Bias.Forward;
					return startOffset;
				}
				// End case for bidi text where newline is at beginning of line.
				return -1;
			}
			
			if
				(rightToLeft)
			{
				if
					(pos == -1)
				{
					loadText(endOffset - 1, endOffset, com.bluebrim.text.shared.CoTextUtilities.TMP_Segment);
					if
						(com.bluebrim.text.shared.CoTextUtilities.TMP_Segment.array[com.bluebrim.text.shared.CoTextUtilities.TMP_Segment.offset] == '\n')
					{
						biasRet[0] = Position.Bias.Forward;
						return endOffset - 1;
					}
					biasRet[0] = Position.Bias.Backward;
					return endOffset;
				}
				
				if
					(pos == startOffset)
				{
					return -1;
				}
				biasRet[0] = Position.Bias.Forward;
				return (pos - 1);
			}
			
			if
				(pos == -1)
			{
				biasRet[0] = Position.Bias.Forward;
				return startOffset;
			}
			
			if
				(pos == endOffset)
			{
				return -1;
			}
			
			if
				(++pos == endOffset)
			{
				loadText(endOffset - 1, endOffset, com.bluebrim.text.shared.CoTextUtilities.TMP_Segment);
				if
					(com.bluebrim.text.shared.CoTextUtilities.TMP_Segment.array[com.bluebrim.text.shared.CoTextUtilities.TMP_Segment.offset] == '\n')
				{
					return -1;
				}
				biasRet[0] = Position.Bias.Backward;
			} else {
				biasRet[0] = Position.Bias.Forward;
			}
			return pos;
			
		case WEST :
			if
				(startOffset == getDocument().getLength())
			{
				if
					(pos == -1)
				{
					biasRet[0] = Position.Bias.Forward;
					return startOffset;
				}
				// End case for bidi text where newline is at beginning
				// of line.
				return -1;
			}
			
			if
				(rightToLeft)
			{
				if
					(pos == -1)
				{
					biasRet[0] = Position.Bias.Forward;
					return startOffset;
				}
				
				if
					(pos == endOffset)
				{
					return -1;
				}
				
				if
					(++pos == endOffset)
				{
					loadText(endOffset - 1, endOffset,com.bluebrim.text.shared.CoTextUtilities.TMP_Segment);
					if
						(com.bluebrim.text.shared.CoTextUtilities.TMP_Segment.array[com.bluebrim.text.shared.CoTextUtilities.TMP_Segment.offset] == '\n')
					{
						return -1;
					}
					biasRet[0] = Position.Bias.Backward;
				} else {
					biasRet[0] = Position.Bias.Forward;
				}
				return pos;
			}
			
			if
				(pos == -1)
			{
				loadText(endOffset - 1, endOffset, com.bluebrim.text.shared.CoTextUtilities.TMP_Segment);
				if
					(com.bluebrim.text.shared.CoTextUtilities.TMP_Segment.array[com.bluebrim.text.shared.CoTextUtilities.TMP_Segment.offset] == '\n')
				{
					biasRet[0] = Position.Bias.Forward;
					return endOffset - 1;
				}
				biasRet[0] = Position.Bias.Backward;
				return endOffset;
			}
			
			if
				(pos == startOffset)
			{
				return -1;
			}
			biasRet[0] = Position.Bias.Forward;
			return (pos - 1);
			
		default :
			throw new IllegalArgumentException("Bad direction: " + direction);
	}
	
	return pos;
}


public float getPartialSpan( float x0, int p0, int p1 )
{
	return doGetPartialSpan( x0, p0, p1, m_spacePadding, 0, p1 - p0 );
}


public float getPartialSpan( int p0, int p1 )
{
	return doGetPartialSpan( m_x, p0, p1, m_spacePadding, p0 - getStartOffset(), p1 - p0 );
}


private static List getPool()
{
	if
		( ( m_pool == null ) || ( m_pool.get() == null ) )
	{
		m_pool = new SoftReference( new ArrayList() );
	}

	return (List) m_pool.get();
}


public float getPreferredSpan(int axis)
{
	float span = getPreferredSpan(axis, getStartOffset(), getEndOffset(), m_x);
	
	if ( axis == Y_AXIS ) return span;

	View p = getParent();
	boolean isTrailingView = ( p == null ) ? false : ( p.getViewCount() > 0 ) ? ( this == p.getView( p.getViewCount() - 1 ) ) : false;

	if
		( isTrailingView )
	{
		if ( span == 0 ) return span;

		float tracking = m_font.calculateTracking( m_tracking );
		
		int tc = m_glyphVector.getCharCount();
		char ch = m_glyphVector.getChar( tc - 1 );
	  while
			( ( tc > 0 ) && ( Character.isWhitespace( ch ) ) )
		{
			if
				( ( ch != '\n' ) && ( ch != '\r' ) )
			{
				span -= m_glyphVector.getAdvance( tc - 1 ) + tracking;
			}
			if
				( ch == ' ' )
			{
				span -= m_spacePadding;
			}
			tc -= 1;
			if ( tc > 0 ) ch = m_glyphVector.getChar( tc - 1 );
		}
		if ( tracking > 0 ) span -= tracking;
	}

	return span;
}


private float getPreferredSpan(int axis, int p0, int p1, float x)
{
	return doGetPreferredSpan( axis, p0, p1,  x, m_spacePadding, 0, p1 - p0 );
}


public float getSpacePadding()
{
	return m_spacePadding;
}


public int getStartOffset()
{
	return m_element.getStartOffset();
}


// --- TabableView methods --------------------------------------

/**
 * Determines the desired span when using the given 
 * tab expansion implementation.  
 *
 * @param x the position the view would be located
 *  at for the purpose of tab expansion >= 0.
 * @param e how to expand the tabs when encountered.
 * @return the desired span >= 0
 * @see TabableView#getTabbedSpan
 */
public float getTabbedSpan( float x, TabExpander e )
{
	m_expander = (CoTabExpander) e;
	m_x = x;
	return getPreferredSpan( X_AXIS, getStartOffset(), getEndOffset(), m_x );
}


public void insertUpdate(DocumentEvent e, Shape a, ViewFactory f)
{
	loadText( "insertUpdate" );
	
	super.insertUpdate(e, a, f);
	int p0 = getStartOffset();
	int p1 = getEndOffset();
	Document d = getDocument();

	if
		(d instanceof DefaultStyledDocument)
	{
		m_rightToLeft = !((DefaultStyledDocument) d).isLeftToRight(p0, p1);
	}
}


/**
 * Load the text buffer with the given range
 * of text.  This is used by the fragments 
 * broken off of this view as well as this 
 * view itself.
 */
final void loadText(int p0, int p1, Segment s )
{
	try
	{
		getDocument().getText( p0, p1 - p0, s );

		if
			( m_allCaps )
		{
			char [] tmp = new char [ s.count ]; // why not reuse s.array ???
			for
				( int i = 0; i < s.count; i++ )
			{
				tmp[ i ] = Character.toUpperCase( s.array[ s.offset + i ] );			
			}
			s.array = tmp;
			s.offset = 0;
		}

//		m_glyphVector.checkSegment( m_text );
	}
	catch ( BadLocationException bl )
	{
		throw new CoStateInvariantError("LabelView: Stale view: " + bl);
	}
}


final void loadText( String caller )
{
	int so = getStartOffset();
	int eo = getEndOffset();

	if
		(
			( m_startOffsetOfSegment == so )
		&&
			( m_endOffsetOfSegment == eo )
		)
	{
		return;
	}
	
	boolean x = m_startOffsetOfSegment == -1;

	m_startOffsetOfSegment = so;
	m_endOffsetOfSegment = eo;

	int offset = m_text.offset;
	int count = m_text.count;
	char [] array = m_text.array;
	
	loadText( so, eo, m_text );

	/*
	är denna koll tillräklig, kan innehållet i array ha ändrats fast allt annat är lika ???
	Hittills har ingen exempel på det synts.
	*/
	
	x = 
		(
			x
		||
			( offset != m_text.offset )
		||
			( count != m_text.count )
		||
			( array != m_text.array )
		);
		
	/*
	System.err.println(
		(
			caller
		+ "  " +
			( offset != m_text.offset )
		+ "  " +
			( count != m_text.count )
		+ "  " +
			( array != m_text.array )
		+ " -------------> " +
			x
		)
	);
	*/

	if
		( x )
	{
		m_glyphVector.checkSegment( m_text );
		m_glyphVector.setKernAboveSize( ( (com.bluebrim.text.shared.CoStyledDocumentIF) getDocument() ).getKernAboveSize() );
	}
}


Shape modelToView( int pos, Shape a, int p0, int p1, Position.Bias bias, boolean rightToLeft) throws BadLocationException
{
	return doModelToView( pos, a, p0, p1, bias, rightToLeft, m_spacePadding );
}


/**
 * Provides a mapping from the document model coordinate space
 * to the coordinate space of the view mapped to it.
 *
 * @param pos the position to convert >= 0
 * @param a the allocated region to render into
 * @return the bounding box of the given position
 * @exception BadLocationException  if the given position does not represent a
 *   valid location in the associated document
 * @see View#modelToView
 */
public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException
{
	return modelToView(pos, a, getStartOffset(), getEndOffset(), b, m_rightToLeft);
}


public void paint( Graphics g, Shape a )
{
	paint( (Graphics2D) g, a );
}


public void paint(Graphics2D g, Shape a )
{
	CoPaintable p = (CoPaintable) g.getRenderingHint( CoScreenPaintable.PAINTABLE_KEY );
	
	paint( p, a, false );
}


public void paint( CoPaintable g, Shape a )
{
	paint( g, a, false );
}


public void paint( CoPaintable g, Shape a, boolean broken )
{
	doPaint( g, a, broken, 0, getEndOffset() - getStartOffset() );
}


private static void paintBorder( CoPaintable g, Rectangle2D r )
{
  if
		( VIEW_BORDER_COLOR != null )
  {
		g.setColor( VIEW_BORDER_COLOR );
		g.draw( r );
//		g.drawString( "" + r.getHeight(), (float) r.getX(), (float) ( r.getY() + r.getHeight() ) );
  }
}


public void paintSelectionShadow( Graphics2D g, int from, int to, Shape a )
{
	if ( from > getEndOffset() ) return;
	if ( to < getStartOffset() ) return;

	sync();
	from = Math.max( from, getStartOffset() );
  to = Math.min( to, getEndOffset() );

  Rectangle2D alloc = a.getBounds2D();
  try
  {
	  Rectangle2D r0 = modelToView( from, alloc, Position.Bias.Forward ).getBounds2D();
	  Rectangle2D r1 = modelToView( to, alloc, Position.Bias.Forward ).getBounds2D();
	  
	  double x = r0.getX();
	  double y = alloc.getY();
	  double w = r1.getX() - r0.getX() + r1.getWidth();
		double h = m_lineMetrics.getHeight();
		m_rect.setRect( x, y, w, h );
		g.fill( m_rect );
  }
  catch ( BadLocationException e )
  {
  }
}


private void paintText( CoPaintable g,
	                      double x,
	                      double y,
	                      int startOffset,
	                      boolean broken,
	                      float spacePadding,
	                      int offset,
	                      int count )
{
	y += m_baselineOffset;
	
	if
		( m_shadow )
	{
		/*
		double dx = 1;
		double dy = 1;

		if
			( Double.isNaN( m_shadowOffsetX ) )
		{
			// PENDING: get from font
			double d = 1;
			double fs = m_font.getFontSize();
			
			if ( ( m_font.getAwtFont().getStyle() & Font.BOLD ) != 0 ) d++;
			if ( fs > 30 ) d++;
			if ( fs > 40 ) d++;
			if ( fs > 50 ) d++;
			dx = dy = d;
		} else {
			dx = m_shadowOffsetX / 200.0 * m_font.getEmSpace();
			dy = m_shadowOffsetY / 200.0 * m_font.getEmSpace();
		}
		*/
		g.setPaint( m_shadowColor );
		m_glyphVector.drawTabbedText( (float) ( x + m_shadowOffsetX ),
			                          (float) ( y + m_shadowOffsetY ),
			                          g,
			                          m_expander,
			                          startOffset,
			                          broken,
			                          m_tracking,
			                          spacePadding,
			                          offset,
			                          count,
			                          m_strikethru,
			                          m_underline.equals(CoTextConstants.UNDERLINE_NORMAL ),
			                          m_underline.equals(CoTextConstants.UNDERLINE_WORD ) );
	}
	
	g.setPaint(m_fg);
	m_glyphVector.drawTabbedText( (float) x,
		                          (float) y,
		                          g,
		                          m_expander,
			                        startOffset,
		                          broken,
		                          m_tracking,
		                          spacePadding,
			                        offset,
			                        count,
			                        m_strikethru,
			                        m_underline.equals(CoTextConstants.UNDERLINE_NORMAL ),
			                          m_underline.equals(CoTextConstants.UNDERLINE_WORD ) );

}


public void release()
{
	if
		( ! getPool().contains( this ) )
	{	
		if ( TRACE_POOL ) System.err.println( "RELEASE " + this );
		getPool().add( this );
	} else {
		com.bluebrim.base.shared.debug.CoAssertion.assertTrue( false, "CoLabelView released twice" );
	}
}


public void removeUpdate(DocumentEvent e, Shape a, ViewFactory f)
{
	loadText( "removeUpdate" );
	
	super.removeUpdate(e, a, f);
}


private void reset()
{
	setParent( null );
	
	m_element = null;
	
	m_underline = null;
	m_strikethru = false;
	m_allCaps = false;
//	m_smallCaps = false;
	m_superior = false;
	m_superscript = false;
	m_subscript = false;
//	m_outline = false;
	m_shadow = false;
	m_tracking = 0;
	m_baselineOffset = 0;

	m_font = null;
	m_lineMetrics = null;
	m_fg = null;

	m_expander = null;
	m_x = 42;
	m_text = null;
	m_startOffsetOfSegment = m_endOffsetOfSegment = -1;
	
	m_glyphVector.reset();

	m_rightToLeft = false;
	m_leading = null;

	m_spaceCount = 0;
	m_spacePadding = 0;
}


private void set( Element elem )
{
	m_element = elem;
	
	m_text = new Segment();
	int p0 = elem.getStartOffset();
	int p1 = elem.getEndOffset();
	Document d = getDocument();

	if
		(d instanceof DefaultStyledDocument)
	{
		m_rightToLeft = !((DefaultStyledDocument) d).isLeftToRight(p0, p1);
	}

//	sync();
	loadText( "set" );

	View p = getParent();
	if
		( p != null )
	{
		com.bluebrim.base.shared.debug.CoAssertion.assertTrue( elem.getStartOffset() >= p.getStartOffset(), "CoLabelView is out of bounds" );
		com.bluebrim.base.shared.debug.CoAssertion.assertTrue( elem.getEndOffset() <= p.getEndOffset(), "CoLabelView is out of bounds" );
	}
}


public void setParent( View parent )
{
	super.setParent( parent );
	
	if
		( parent instanceof CoTabExpander )
	{
		m_expander = (CoTabExpander) parent;
	}

	if
		( parent != null )
	{
		com.bluebrim.base.shared.debug.CoAssertion.assertTrue( m_element.getStartOffset() >= parent.getElement().getStartOffset(), "CoLabelView is out of bounds" );
		com.bluebrim.base.shared.debug.CoAssertion.assertTrue( m_element.getEndOffset() <= parent.getElement().getEndOffset(), "CoLabelView is out of bounds" );
	}

	if ( TRACE_POOL ) System.err.println( "SET PARENT " + this + " TO " + parent );
}


/**
 * Set the cached properties from the attributes.
 */
protected void setPropertiesFromAttributes()
{
	Element e = getElement();
	Document d = getDocument();
	
	if
		(d instanceof com.bluebrim.text.shared.CoStyledDocumentIF )
	{
		AttributeSet attr = e.getAttributes();
		m_font = CoViewStyleConstants.getCoFont( attr );

		loadText( "setPropertiesFromAttributes" );

		m_glyphVector.setFont( m_font );
		m_lineMetrics = m_font.getLineMetrics();
		
		m_fg = CoViewStyleConstants.getForegroundColor( attr, (com.bluebrim.text.shared.CoStyledDocumentIF) d );
		m_underline = CoViewStyleConstants.getUnderline( attr );
		m_strikethru = CoViewStyleConstants.isStrikeThru( attr );
		m_allCaps = CoViewStyleConstants.isAllCaps( attr );
		m_startOffsetOfSegment = m_endOffsetOfSegment = -1;
//		m_smallCaps = CoViewStyleConstants.isSmallCaps( attr );
		m_superior = CoViewStyleConstants.isSuperior( attr );
//		m_outline = CoViewStyleConstants.isOutline( attr );
		m_shadow = CoViewStyleConstants.isShadow( attr );
		m_tracking = CoViewStyleConstants.getTrackAmount( attr );
		m_baselineOffset = CoViewStyleConstants.getBaselineOffset( attr );

		if
			( m_shadow )
		{
			float offset = CoViewStyleConstants.getShadowOffset( attr );
			if
				( Float.isNaN( offset ) )
			{
				offset = 10; // PENDING: get from font
			}

			float angle = (float) Math.toRadians( CoViewStyleConstants.getShadowAngle( attr ) );
			m_shadowOffsetX = offset * Math.cos( angle ) / 200.0 * m_font.getEmSpace();
			m_shadowOffsetY = offset * Math.sin( angle ) / 200.0 * m_font.getEmSpace();
			m_shadowColor = CoViewStyleConstants.getShadowColor( attr, (com.bluebrim.text.shared.CoStyledDocumentIF) d );
		}

		CoEnumValue vpos 	= CoViewStyleConstants.getVerticalPosition( attr );
		m_superscript 		=  vpos.equals(CoTextConstants.VERTICAL_POSITION_SUPERSCRIPT );
		m_subscript 		=  vpos.equals(CoTextConstants.VERTICAL_POSITION_SUBSCRIPT );
		
//		m_isLeadingAbsolute = false;
//		m_isLeadingRelative = false;
//		m_isLeadingOffset = false;

		m_leading = CoViewStyleConstants.getLeading(attr);
/*
		m_autoLeading = m_leading == CoTextConstants.AUTO_LEADING_VALUE;
		if
			( m_autoLeading )
		{
			m_leading = 0;
		} else {
			m_isLeadingOffset = m_leading > CoTextConstants.ABSOLUTE_LEADING_MAX;
			if
				( m_isLeadingOffset )
			{
				m_leading -= CoTextConstants.OFFSET_LEADING_ZERO;
			} else {
				m_isLeadingRelative = m_leading < CoTextConstants.ABSOLUTE_LEADING_MIN;
				if
					( m_isLeadingRelative )
				{
					m_leading -= CoTextConstants.RELATIVE_LEADING_ZERO;
					m_leading /= 100;
				} else {
					m_isLeadingAbsolute = true;
				}
			}
		}
*/
	} else {
		throw new CoStateInvariantError("LabelView needs StyledDocument");
	}
}


public float setSpacePadding( float p, float minimumRelativeSpaceWidth ) throws CoJustifiableView.ToTight
{
	sync();

	if
		( p / m_glyphVector.getSpaceWidth() < - minimumRelativeSpaceWidth )//m_minimumSpaceStretch )
	{
		throw CoJustifiableView.m_toTight;
	}
	
	m_spacePadding = p;
	return m_spacePadding * m_spaceCount;
}


/**
 * Synchronize the view's cached values with the model.
 * This causes the font, metrics, color, etc to be 
 * recached if the cache has been invalidated.
 */
final void sync()
{
	if
		(m_font == null)
	{
		setPropertiesFromAttributes();
	}
}


public String toString()
{
	return "CoLabelView " + getStartOffset() + " -> " + getEndOffset();
}


/**
 * Provides a mapping from the view coordinate space to the logical
 * coordinate space of the model.
 *
 * @param x the X coordinate >= 0
 * @param y the Y coordinate >= 0
 * @param a the allocated region to render into
 * @return the location within the model that best represents the
 *  given point of view >= 0
 * @see View#viewToModel
 */
public int viewToModel(float x, float y, Shape a, Position.Bias[] biasReturn)
{
	// PENDING(prinz) fix this to do the right thing.
	biasReturn[0] = Position.Bias.Forward;
	return viewToModel(x, y, a, biasReturn, getStartOffset(), getEndOffset(), m_rightToLeft);
}


/**
 * Provides a mapping from the view coordinate space to the logical
 * coordinate space of the model.
 *
 * @param x the X coordinate
 * @param y the Y coordinate
 * @param a the allocated region to render into
 * @param rightToLeft true if the text is rendered right to left
 * @return the location within the model that best represents the
 *  given point of view
 * @see View#viewToModel
 */
int viewToModel( float x,
	               float y,
	               Shape a,
	               Position.Bias[] biasReturn,
	               int p0,
	               int p1,
	               boolean rightToLeft )
{
	return doViewToModel( x, y, a, biasReturn, p0, p1, rightToLeft, m_spacePadding, 0, p1 - p0 );
}
}