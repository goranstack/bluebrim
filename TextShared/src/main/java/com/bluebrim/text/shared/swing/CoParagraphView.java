package com.bluebrim.text.shared.swing;

// Calvin imports
import java.awt.*;
import java.awt.geom.*;
import java.lang.ref.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Text view used to display a paragraph.
 * 
 * @author: Dennis Malmström
 */


public class CoParagraphView extends CoCompositeView implements CoTabExpander, CoPaintableView, com.bluebrim.text.shared.swing.CoHighlightableView, CoReuseableView
{
	private static boolean TRACE_POOL = false;
	private static boolean TRACE_ROW_POOL = false;
	
	private Element m_element;
	
	private int [] m_breakpoints;
	private int m_breakpointCount;
	private int m_nextBreakpoint;

	
	static int m_ID = 0;
	int m_id = m_ID++;

	private CoColumnGeometryIF.CoColumnIF m_column;
	private float m_y;
	private double m_deltaYrow;

	protected float m_width;
	protected float m_height;

	protected boolean m_xRequestValid;
	protected boolean m_yRequestValid;

	protected float m_firstBaseLine;
	protected float m_baseLineSpacing;
		
	protected CoSizeRequirements m_xRequest;
	protected CoSizeRequirements m_yRequest;
	
	protected float[] m_xPos;
	protected float[] m_xSpan;
	protected float[] m_yPos;
	protected float[] m_ySpan;
	protected boolean m_allocValid;
	protected int m_visibleRowCount;

//	private float m_rowHeight;
	private float m_fallbackRowHeight;

	private int m_dropCapsViewWidth;
	
	// grannar som uppstått vid delning
	 CoParagraphView m_head;
	protected CoParagraphView m_tail;

	// modellgränser som uppstått vid delning (-1 = ogiltig)
	protected int m_offset = -1;
	protected int m_length = -1;
	
	protected CoLeading m_leading;




	
	/*
	protected boolean m_isLeadingAbsolute;
	protected boolean m_isLeadingOffset;
	protected boolean m_isLeadingRelative;
	protected boolean m_autoLeading;
	*/
	
	protected CoEnumValue m_justification;
	protected int m_firstLineIndent;
	protected int m_trailingLinesIndent;
	protected boolean m_dropCaps;
	protected int m_dropCapsLineCount;
	protected int m_dropCapsCharacterCount;
	protected boolean m_keepLinesTogether;
	protected boolean m_topOfColumn;
	protected boolean m_lastInColumn;

	protected CoLineBreakerIF m_lineBreaker;
	protected CoEnumValue m_hyphenationFallbackBehavior;
	protected CoTabSetIF m_tabSet;
	protected boolean m_adjustToBaseLineGrid;

	private CoTabStopIF m_currentTabStop;
	private CoTabStopIF m_fallbackTabStop = new CoTabStop();

	
	private static class HorizontalLineStroke implements Stroke
	{
		private double m_thickness = 0;
		private Rectangle2D m_strokeShape = new Rectangle2D.Double();

		public double getThickness()
		{
			return m_thickness;
		}

		public void setThickness( double t )
		{
			m_thickness = t;
		}
		
		public Shape createStrokedShape( Shape p )
		{
			Line2D l = (Line2D) p;
			m_strokeShape.setRect( l.getX1(), l.getY1() - m_thickness / 2, l.getX2() - l.getX1(), m_thickness );
			return m_strokeShape;
		}
	};

	protected Line2D m_topRulerShape = new Line2D.Double();
	protected HorizontalLineStroke m_topRulerStroke = new HorizontalLineStroke();

	protected float m_topRulerPosition;
	protected CoEnumValue m_topRulerJustification;
	protected float m_topRulerFixedWidth;
	protected float m_topRulerLeftIndent;
	protected float m_topRulerRightIndent;
	protected boolean m_topRulerIsColumn;
	
	
	protected Line2D m_bottomRulerShape = new Line2D.Double();
	protected HorizontalLineStroke m_bottomRulerStroke = new HorizontalLineStroke();

	protected float m_bottomRulerPosition;
	protected CoEnumValue m_bottomRulerJustification;
	protected float m_bottomRulerFixedWidth;
	protected float m_bottomRulerLeftIndent;
	protected float m_bottomRulerRightIndent;
	protected boolean m_bottomRulerIsColumn;

	
	// hjälpstrukturer som används vid anrop till View.replace
	private static final View[] ZERO_VIEWS = new View[0];

	// flaggor som styr debug- och testbeteenden
	public static Color VIEW_BORDER_COLOR = null;
	
	/**
	 * Used by the TabExpander functionality to determine
	 * where to base the tab calculations.  This is basically
	 * the location of the left side of the paragraph.
	 */
	private float m_tabBase;

	/**
	 * These are the views that represent the child elements
	 * of the element this view represents.  These are not
	 * directly children of this view.  These are either 
	 * placed into the rows directly or used for the purpose
	 * of breaking into smaller chunks.
	 */
	private List m_layoutPool = new ArrayList();

	/** Used for searching for a tab. */
	public static char[] m_tabChars = new char [] { '\t' };
	
	/** Used for searching for a tab or decimal character. */
	public static char[] m_tabDecimalChars = new char [] { '\t', ( new java.text.DecimalFormatSymbols( java.util.Locale.getDefault() ) ).getDecimalSeparator() };//'.', ',' };




 	private static SoftReference m_pool;
 	private transient SoftReference m_rowPool;
	class Row extends CoRowView implements CoReuseableView
	{
		Row( Element elem )
		{
			super( elem );
//			m_allRows.add( this );
		}


		public void release()
		{
			List rowPool = getRowPool();
			
			if
				( ! rowPool.contains( this ) )
			{	
				if ( TRACE_ROW_POOL ) System.err.println( "RELEASE " + this );
				rowPool.add( this );
			}
			
			int I = getViewCount();
			for
				( int i = 0; i < I; i++ )
			{
				View v = getView( i );
				if ( v instanceof CoLabelView.Fragment ) ( (CoReuseableView) getView( i ) ).release();
			}

			removeAll();
		}

		boolean isJustified()
		{
			return
				(
					(
						( m_justification == CoStyleConstants.ALIGN_JUSTIFIED )
		  		&&
						( this.getEndOffset() != this.getElement().getEndOffset() )
		  		)
				||
		 				( m_justification == CoStyleConstants.ALIGN_FORCED )
				);
		}

		public float getAlignment( int axis )
		{
			if
				( axis == View.X_AXIS )
			{
				if      ( m_justification == CoStyleConstants.ALIGN_LEFT )      return 0.0f;
				else if ( m_justification == CoStyleConstants.ALIGN_JUSTIFIED ) return 0.0f;
				else if ( m_justification == CoStyleConstants.ALIGN_FORCED )    return 0.0f;
				else if ( m_justification == CoStyleConstants.ALIGN_RIGHT )     return 1.0f;
				else if ( m_justification == CoStyleConstants.ALIGN_CENTER )    return 0.5f;
			}
			
			return super.getAlignment( axis );
		}

	}
private CoParagraphView(Element elem)
{
	super( null );

	set( elem );
}
private CoParagraphView( Element elem, CoParagraphView head )
{
	super( null );

	set( elem, head );
}
private void adjustRow( Row row, float desiredSpan, boolean forceConsume )
{
	boolean isJustified = isJustified();

	
	assureBreakPoints();
	if ( m_breakpoints == null ) return;

	int n0 = m_nextBreakpoint;
	int n = n0;
	int p0 = row.getStartOffset();

	int bpOffset = getElement().getStartOffset(); // m_breakpoints[ n ] is view relative
	
	// iterate over breakpoints until desired span is consumed
	// PENDING: start by interpolating n
	while
		( n < m_breakpointCount )
	{
		float w = row.getMinimalPartialSpan( m_breakpoints[ n ] + bpOffset, m_optimumSpaceWidth );
		if
			( w > desiredSpan  )
				// IDEA FOR OPT: cache <subrange,subspan> values in CoRow (recalcs in this loop reduced)
		{
			if ( ! isJustified ) n--;
			break;
		}
		n++;
	}

	float minimumRelativeSpaceWidth = isJustified ? m_minimumSpaceWidth : m_optimumSpaceWidth;


	// go back until row (including hyphen) fits
	while
		( n >= n0 )
	{
		int bp = m_breakpoints[ n ] + bpOffset;
		if
			( ( p0 < bp ) && adjustToBreakpoint( row, bp, desiredSpan, minimumRelativeSpaceWidth ) )
		{
			m_nextBreakpoint = n + 1;
			return;
		}
		n--;
	}

	// fallback behavior: force empty line
	if
		( ! forceConsume && m_hyphenationFallbackBehavior == CoStyleConstants.HYPHENATION_FALLBACK_BEHAVIOR_NO_LINE )
	{
		adjustToBreakpoint( row, p0, Float.MAX_VALUE, minimumRelativeSpaceWidth );
		return;
	}

	
	// fallback behavior: force first breakpoint
	if
		( forceConsume || m_hyphenationFallbackBehavior == CoStyleConstants.HYPHENATION_FALLBACK_BEHAVIOR_FIRST_BREAKPOINT )
	{
		// no breakpoint found, layout entire row
		while ( p0 >= m_breakpoints[ n0 ] + bpOffset && n0 < m_breakpointCount - 1 ) n0++;
		adjustToBreakpoint( row, m_breakpoints[ n0 ] + bpOffset, Float.MAX_VALUE, minimumRelativeSpaceWidth );
		m_nextBreakpoint = n0 + 1;
//		System.err.println( "LINEBREAK FAILED::::::::::::::::::::: " + row );
		return;
	}




	
	// row wont fit, try fallback breakpoints (anywhere

// EXACT: int end = m_breakpoints[ m_breakpointCount - 1 ] + bpOffset;

	// interpolate to breakpoint (might yield a line that is to short)
//	int p0 = row.getStartOffset();
	int p1 = row.getEndOffset();
	int end = p0 + (int) ( ( p1 - p0 ) * desiredSpan / row.getMinimalPartialSpan( p1, minimumRelativeSpaceWidth ) );

	System.err.println( "FALLBACK::::::::::::::::::::::::::: " + row.getStartOffset() + " -> " + end/*row.getEndOffset()*/ + ", " + desiredSpan);

	

	if
		( m_column.isRectangular() )
	{
		// no hope of column widening, break anywhere
		/*
		if
			( true )
		{
			// fast method = move n chars back (should make room for hyphen)   only "works" if end is calculated exactly
			adjustToBreakpoint( row, end - 1, Float.MAX_VALUE );
			return;
		}
		*/
		
		while
			( end > row.getStartOffset() + 1 )
		{

			System.err.println( end );
			if
				( adjustToBreakpoint( row, end, desiredSpan, minimumRelativeSpaceWidth ) )
			{
			System.err.println( "---> " + end );
				return;
			}
			end--;
		}

		// no breakpoint found, check for one character row
		if
			( end == row.getStartOffset() + 1 )
		{
			adjustToBreakpoint( row, end, Float.MAX_VALUE, minimumRelativeSpaceWidth );
			return;
		}
	} else {
		adjustToBreakpoint( row, row.getStartOffset(), Float.MAX_VALUE, minimumRelativeSpaceWidth );
	}

	// no breakpoint found, layout entire row
	System.err.println( "FALLBACK FAILED:::::::::::::::::::::" );
	adjustToBreakpoint( row, row.getEndOffset(), Float.MAX_VALUE, minimumRelativeSpaceWidth );
	
	//PENDING: handle failure
}

void applyRowSpacing()
{
	int I = getViewCount();
	for
		( int i = 0; i < I; i++ )
	{
		m_yPos[ i ] += m_deltaYrow * i;
	}
}
private void assureBreakPoints()
{
	if
		( m_breakpoints == null )
	{
		int p0 = getElement().getStartOffset();
		int p1 = getElement().getEndOffset();

		Segment s = CoTextUtilities.TMP_Segment;
		try
		{
			getDocument().getText( p0, p1 - p0, s );
		}
		catch ( BadLocationException ex )
		{
			return;
		}

		m_breakpoints = new int [ p1 - p0 + 2 ];
		m_breakpointCount = 0;
		
		CoLineBreakerIF.BreakPointIteratorIF bps = m_lineBreaker.getBreakPoints( s );
		while
			( bps.hasNext() )
		{
			int p = bps.next();
			m_breakpoints[ m_breakpointCount++ ] = p;
		}

		m_breakpoints[ m_breakpointCount ] = p1 - 1 - p0;
		
		CoParagraphView v = this;
		while ( v.m_head != null ) v = v.m_head;
		while
			( v != null )
		{
			v.m_breakpoints = m_breakpoints;
			v.m_breakpointCount = m_breakpointCount;
			v = v.m_tail;
		}

	}
}
/**
 * Breaks this view on the given axis at the given length.<p>
 * ParagraphView instances are breakable along the Y_AXIS only, and only if
 * <code>len</code> is after the first line.
 *
 * @param axis may be either View.X_AXIS or View.Y_AXIS
 * @param len specifies where a potential break is desired
 *  along the given axis >= 0
 * @param a the current allocation of the view
 * @return the fragment of the view that represents the
 *  given span, if the view can be broken.  If the view
 *  doesn't support breaking behavior, the view itself is
 *  returned.
 * @see View#breakView
 */
public View breakView(int axis, float len, Shape a)
{
	if (axis == View.Y_AXIS)
	{
		if (a != null)
		{
			Rectangle2D alloc = a.getBounds2D();
			setSize((float) alloc.getWidth(), (float) alloc.getHeight());
		}
		// Determine what row to break on.

		// PENDING(prinz) add break support
		return this;
	}
	return this;
}
private void buildDropCapsRow( Row row, float span, int p0, int p1, boolean forceConsume )
{
	View v = createView( p0 );

	v = ( (CoLabelView) v ).createDropCapsFragment( p0,
												                          Math.min( p1 - 1, p0 + m_dropCapsCharacterCount ),
												                          m_dropCapsLineCount,
												                          0 );
	row.append( v );
	m_dropCapsViewWidth = (int) row.getPreferredSpan( View.X_AXIS );

	if
		( m_dropCapsViewWidth > span )
	{
		// drop cap row does not fit, return an empty one
		row.removeAndReleaseAll();
	}
}
private void buildNormalRow( Row row, float allocatedSpan, int p0, boolean forceConsume )
{
	if ( allocatedSpan <= 0 ) allocatedSpan = 1;
	
	row.clearSpacePadding();

	float availableSpan = allocatedSpan;

	int end = getElement().getEndOffset();

	// add label views until row is full
	while
		( ( p0 < end ) )
	{
		View v = createView( p0 );
		( (CoJustifiableView) v ).clearSpacePadding();

		// handle linefeed
		Segment text = CoTextUtilities.TMP_Segment;
		try
		{
			getDocument().getText( p0, v.getEndOffset() - p0, text );
		}
		catch ( BadLocationException bl )
		{
			System.err.println( p0 + "  " + ( v.getEndOffset() - p0 ) + "       " + getDocument().getLength() );
			throw new CoStateInvariantError("LabelView: Stale view: " + bl);
		}

		for
			( int i = text.offset, I = i + text.count; i < I; i++ )
		{
			if
				( text.array[ i ] == '\r' )
			{
				v = v.createFragment( p0, p0 + i + 1 - text.offset );
				end = v.getEndOffset();
				break;
			}
		}

		row.append( v );
		p0 = v.getEndOffset();

//WHY DO THIS ???		float w = row.getMinimalSpan( (float) m_column.getBounds().getX() );


		
		// stop if w > availableSpan (not counting hyphen)
//		System.err.print( w + " # " );
//		if ( row.isBroken() ) w += row.getHyphenWidth();
//		System.err.println( w + " # " + availableSpan );

	}

	float w = row.getPreferredSpan( X_AXIS );
	float spanLeft = availableSpan - w;

	
//	if ( row.isBroken() ) spanLeft -= row.getHyphenWidth();	  handled in CoRowView.calculateXAxisRequirements instead

	float minimumRelativeSpaceWidth = isJustified() ? m_minimumSpaceWidth : m_optimumSpaceWidth;
	
	if
		( row.getViewCount() == 0 )
	{
		// Imposible spec... put in whatever is left.
		View v = createView( p0 );
		row.append( v );
	} else {
		
		if
			( spanLeft < 0 )
		{
			// try squeezing it in
			boolean canShrink = row.setSpacePadding( spanLeft, minimumRelativeSpaceWidth );
			if
				( ! canShrink )
			{
				// This row is too long and needs to be adjusted.
				adjustRow( row, availableSpan, forceConsume );
				if ( row.getEndOffset() == row.getStartOffset() ) return;
				
				spanLeft = availableSpan - row.getPreferredSpan( X_AXIS );
//				if ( row.isBroken() ) spanLeft -= row.getHyphenWidth();	  handled in CoRowView.calculateXAxisRequirements instead

				if
					( spanLeft < 0 )
				{
					canShrink = row.setSpacePadding( spanLeft, minimumRelativeSpaceWidth );
					if
						( ! canShrink )
					{
						// line adjustment failed

						String str = null;
						try
						{
							str = getDocument().getText( row.getStartOffset(), row.getEndOffset() - row.getStartOffset() );
						}
						catch ( Exception ex )
						{
						}
						System.err.println( "KNAS KNAS KNAS " + row.getStartOffset() + " -> " + row.getEndOffset() + " in " + availableSpan + " leaves " + spanLeft + " : #" + str + "#" );
					}
					double qqq = row.getPreferredSpan( X_AXIS );
				} else {
					row.clearSpacePadding();
				}
			}
		}

		if
			( row.isJustified() )
		{
			if ( spanLeft > 0 ) row.setSpacePadding( spanLeft, minimumRelativeSpaceWidth );
		} else if
			( spanLeft < 0 )
		{
		} else {
			row.clearSpacePadding();
		}
	}
		
		// Adjust for line spacing
//	row.setLeading( (short) calcLeadingOffset( row.getPreferredSpan( Y_AXIS ), row.getFontSize() ) );
	row.setBroken();
}
private Row buildRow( Row row, int rowCount, float span, int p0, int p1, boolean forceConsume )
{
	// create or reuse row
	if
		( row == null )
	{
		row = createRow( getElement() );
	} else {
		row.removeAndReleaseAll();
	}

	if
		( span <= 1 )
	{
		return row;
	}
		
	// set first line indent
	if
		( ( rowCount == 0 ) && ( m_head == null ) )
	{
		// Give it at least 5 pixels.
		row.setFirstLineIndent( (short) Math.min( span - 5, m_firstLineIndent ) );
	} else if
		( ( rowCount > 0 ) && ( m_head == null ) )
	{
		row.setFirstLineIndent( (short) Math.min( span - 5, m_trailingLinesIndent ) );
	}

	// layout the row to the current span
	if
		( hasDropCaps() && ( rowCount == 0 ) )
	{
		buildDropCapsRow( row, span, p0, p1, forceConsume );
	} else {
		buildNormalRow( row, span, p0, forceConsume );
	}

	return row;
}


protected void calculateXRequirements()
{
	if
		(m_xRequest == null)
	{
		m_xRequest = new CoSizeRequirements();
	}
	
	float pref = 0;
	int I = m_layoutPool.size();
	for
		(int i = 0; i < I; i++)
	{
		View v = (View) m_layoutPool.get(i);
		pref += v.getPreferredSpan(X_AXIS);
	}
	
	m_xRequest.m_minimum = 0;
	m_xRequest.m_preferred = pref;
	m_xRequest.m_maximum = Integer.MAX_VALUE;
	m_xRequest.m_alignment = 0.5f;
}
protected void calculateYRequirements()
{
	float min = 0;
	float pref = 0;
	float max = 0;
	
	int I = getViewCount();
	for
		( int i = 0; i < I; i++ )
	{
		CoRowView v = (CoRowView) getView(i);
		min = Math.max( min, v.getY() + v.getMinimumSpan( Y_AXIS ) );
		pref = Math.max( pref, v.getY() + v.getPreferredSpan( Y_AXIS ) );
		max = Math.max( max, v.getY() + v.getMaximumSpan( Y_AXIS ) );
	}

	if
		( I > 0 )
	{
		min -= getY0();
		pref -= getY0();
		max -= getY0();
	}
/*
	// calculate tiled request
	float min = m_extraYspace;
	float pref = m_extraYspace;
	float max = m_extraYspace;
		

	if
		( hasDropCaps() )
	{
		int I = Math.min( getViewCount(), m_dropCapsLineCount + 1 );
		View v;
		int i = 1;
		for
			( ; i < I; i++ )
		{
			v = getView( i );
			min += v.getMinimumSpan( Y_AXIS );
			pref += v.getPreferredSpan( Y_AXIS );
			max += v.getMaximumSpan( Y_AXIS );
		}

		if
			( I > 0 )
		{
			v = getView( 0 );
			if
				( pref < v.getPreferredSpan( Y_AXIS ) )
			{
				min = v.getMinimumSpan( Y_AXIS );
				pref = v.getPreferredSpan( Y_AXIS );
				max = v.getMaximumSpan( Y_AXIS );
			}
		}

		I = getViewCount();
		for
			( ; i < I; i++ )
		{
			v = getView( i );
			min += v.getMinimumSpan( Y_AXIS );
			pref += v.getPreferredSpan( Y_AXIS );
			max += v.getMaximumSpan( Y_AXIS );
		}
	} else {
		int I = getViewCount();
		for
			( int i = 0; i < I; i++ )
		{
			View v = getView(i);
			min += v.getMinimumSpan( Y_AXIS );
			pref += v.getPreferredSpan( Y_AXIS );
			max += v.getMaximumSpan( Y_AXIS );
		}
	}
	*/
	if
		( m_yRequest == null )
	{
		m_yRequest = new CoSizeRequirements();
	}
	m_yRequest.m_alignment = 0.5f;
	m_yRequest.m_minimum = min;
	m_yRequest.m_preferred = pref;
	m_yRequest.m_maximum = max;	
}
/**
 * Gives notification from the document that attributes were changed
 * in a location that this view is responsible for.
 *
 * @param changes the change information from the associated document
 * @param a the current allocation of the view
 * @param f the factory to use to rebuild if the view has children
 * @see View#changedUpdate
 */
public void changedUpdate(DocumentEvent changes, Shape a, ViewFactory f)
{
	
	final boolean DO_TRACE = false;
	
	if ( m_head != null ) return;
	
	if ( DO_TRACE ) System.err.println( "#### changedUpdate " + this );
//	if ( DO_TRACE ) dumpLayoutPool();
	
	// update any property settings stored
	CoParagraphView v = this;
	while ( v.m_head != null ) v = v.m_head;

	while
		( v != null )
	{
		v.setPropertiesFromAttributes( true );
		v = v.m_tail;
	}
	
	// update the pool of logical children
	Element elem = getElement();
	DocumentEvent.ElementChange ec = changes.getChange(elem);
	
	if
		( ec != null )
	{
		// the structure of this element changed.
		updateLogicalChildren( ec, f );
	}
//	if ( DO_TRACE ) dumpLayoutPool();

	// forward to the logical children
	int p0 = changes.getOffset();
	int p1 = p0 + changes.getLength();
	int index0 = elem.getElementIndex(p0);
	int index1 = elem.getElementIndex(p1 - 1);
	
	// Check for case where p0 == p1 and they fall on a boundry.
	if
		( ( p0 == p1 ) && ( index1 < index0 ) && ( index0 > 0 ) )
	{
		index0--;
		index1 = index0 + 1;
	}
	
	for
		( int i = index0; i <= index1; i++ )
	{
		View v2 = (View) m_layoutPool.get( i );
		v2.changedUpdate( changes, null, f );
	}

	// force layout, should do something more intelligent about
	// incurring damage and triggering a new layout.

	// VOJNE: experimental optimizatin
//	preferenceChanged( null, true, true );

	if
		( a != null )
	{
	// VOJNE: experimental optimizatin
		if
			( updateSubParagraphs( changes ) )
		{
			preferenceChanged( null, true, true );
		}

		CoParagraphView tmp = this;
		while
			( tmp != null )
		{
			tmp.layout();
			tmp = tmp.m_tail;
		}
		
		Component host = getContainer();
		Rectangle2D alloc = getInsideAllocation( a );
		host.repaint( (int) alloc.getX(), (int) alloc.getY(), (int) alloc.getWidth(), (int) alloc.getHeight() );
	}
	
	if ( DO_TRACE ) System.err.println( "#### changedUpdate <<" );

}
void checkRequests()
{
	if
		( ! m_xRequestValid )
	{
		calculateXRequirements();
		m_xRequestValid = true;
	}
	
	if
		( ! m_yRequestValid )
	{
		calculateYRequirements();
		m_yRequestValid = true;
	}
	
}
protected void childAllocation( int index, Rectangle2D alloc )
{
	alloc.setRect( alloc.getX() + m_xPos[ index ],
		             alloc.getY() + m_yPos[ index ],
		             m_xSpan[ index ],
	               m_ySpan[ index ] );
	/*
	alloc.x += (int) m_xPos[ index ];
	alloc.y += (int) m_yPos[ index ];
	alloc.width = (int) m_xSpan[ index ];
	alloc.height = (int) m_ySpan[ index ];
	*/
}
public static CoParagraphView create( Element elem )
{
	CoParagraphView v = null;
	if
		( getPool().isEmpty() )
	{
		v = new CoParagraphView( elem );
//		m_all.add( v );
		if ( TRACE_POOL ) System.err.println( "NEW " + v );
	} else {
		v = (CoParagraphView) getPool().remove( getPool().size() - 1 );
		v.reset();
		v.set( elem );
		if ( TRACE_POOL ) System.err.println( "REUSE " + v );
	}

	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( v.m_layoutPool.isEmpty(), "Non empty layout pool" );

	return v;
}
public static CoParagraphView create( Element elem, CoParagraphView head )
{
	CoParagraphView v = null;
	
	if
		( getPool().isEmpty() )
	{
		v = new CoParagraphView( elem, head );
//		m_all.add( v );
		if ( TRACE_POOL ) System.err.println( "NEW " + v );
	} else {
		v = (CoParagraphView) getPool().remove( getPool().size() - 1 );
		v.reset();
		v.set( elem, head );
		if ( TRACE_POOL ) System.err.println( "REUSE " + v );
	}

	return v;
}
private Row createRow( Element e )
{
	List rowPool = getRowPool();
	
	Row r = null;
	if
		( rowPool.isEmpty() )
	{
		r = new Row( e );
		if ( TRACE_ROW_POOL ) System.err.println( "NEW " + r );
	} else {
		r = (Row) rowPool.remove( rowPool.size() - 1 );
		r.reset();
		r.set( e );
		if ( TRACE_ROW_POOL ) System.err.println( "REUSE " + r );
	}

	return r;
}
/**
 * Creates a unidirectional view that can be used to represent the
 * current chunk.  This can be either an entire view from the
 * layout pool, or a fragment there of.
 */
View createView( int startOffset )
{
	// Get the child view that contains the given starting position
	int childIndex = getElement().getElementIndex(startOffset);
	View v = (View) m_layoutPool.get(childIndex);
	int endOffset = v.getEndOffset();

	// REMIND (bcb) handle case of not an abstract document.
	//	###2###
	Document doc = getDocument();
	DefaultStyledDocument d = (DefaultStyledDocument) doc;
	if
		( d.getProperty( DefaultStyledDocument.I18NProperty ).equals( Boolean.TRUE ) )
	{
		//	###2###
		Element bidiRoot = d.getBidiRootElement();
		if
			(bidiRoot.getElementCount() > 1)
		{
			int bidiIndex = bidiRoot.getElementIndex(startOffset);
			Element bidiElem = bidiRoot.getElement(bidiIndex);
			endOffset = Math.min(bidiElem.getEndOffset(), endOffset);
		}
	}
	if
		(startOffset == v.getStartOffset() && endOffset == v.getEndOffset())
	{
		// return the entire view
		return v;
	}

	// return a unidirectional fragment.

	v = v.createFragment( startOffset, endOffset );
		
	return v;
}

public void dump()
{
	System.err.println( "  paragraph " + getStartOffset() + " -> " + getEndOffset() );
	int I = getViewCount();
	for
		( int i = 0; i < I; i++ )
	{
		( (Row) getView( i ) ).dump();
	}
}
public void dumpLayoutPool()
{
	System.err.println( "  paragraph " + getStartOffset() + " -> " + getEndOffset() );

	System.err.println( "    layout pool" );
	int I = m_layoutPool.size();
	for
		( int i = 0; i < I; i++ )
	{
		( (CoLabelView) m_layoutPool.get( i ) ).dump();
	}
}
public static void dumpPools()
{
	/*
	Iterator i = m_all.iterator();
	while
		( i.hasNext() )
	{
		m_allRows.removeAll( ( (CoParagraphView) i.next() ).m_rowPool );
	}

	m_all.removeAll( m_pool );

	System.err.println( "Leaked " + m_all.size() + " paragraph views" );
	if ( ! m_all.isEmpty() ) System.err.println( m_all );
	
	System.err.println( "Leaked " + m_allRows.size() + " row views" );
	if ( ! m_allRows.isEmpty() ) System.err.println( m_allRows );
*/
}
	/**
	 * Finds the next character in the document with a character in
	 * <code>string</code>, starting at offset <code>start</code>. If
	 * there are no characters found, -1 will be returned.
	 *
	 * @param string the string of characters
	 * @param start where to start in the model >= 0
	 * @return the document offset or -1
	 */
	protected int findOffsetToCharactersInString(char[] string,
												 int start) {
		int stringLength = string.length;
		int end = getEndOffset();
		Segment seg = CoTextUtilities.TMP_Segment;//new Segment();
		try {
			getDocument().getText(start, end - start, seg);
		} catch (BadLocationException ble) {
			return -1;
		}
		for(int counter = seg.offset, maxCounter = seg.offset + seg.count;
			counter < maxCounter; counter++) {
			char currentChar = seg.array[counter];
			for(int subCounter = 0; subCounter < stringLength;
				subCounter++) {
				if(currentChar == string[subCounter])
					return counter - seg.offset + start;
			}
		}
		// No match.
		return -1;
	}
protected boolean flipEastAndWestAtEnds(int position, Position.Bias bias)
{
	Document doc = getDocument();
	if
		(
			( doc instanceof DefaultStyledDocument )
		&&
			! ( (DefaultStyledDocument) doc).isLeftToRight( getStartOffset(), 
				                                                getStartOffset() + 1 )
		)
	{
		return true;
	}
	return false;
}
/**
 * Determines the desired alignment for this view along an
 * axis.  This is implemented to give the alignment to the
 * center of the first row along the y axis, and the default
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
	switch
		(axis)
	{
		case Y_AXIS :
			float a = 0.5f;
			if
				(getViewCount() != 0)
			{
				float paragraphSpan = getPreferredSpan(Y_AXIS);
				View v = getView(0);
				float rowSpan = v.getPreferredSpan(Y_AXIS);
				a = ( paragraphSpan != 0 ) ? ( rowSpan / 2 ) / paragraphSpan : 0;
			}
			return a;
			
		case X_AXIS :
			return 0.5f;
			
		default :
			throw new IllegalArgumentException("Invalid axis: " + axis);
	}
}
public AttributeSet getAttributes()
{
	return m_element.getAttributes();
}
/**
 * Gets the break weight for a given location.
 * ParagraphView instances are breakable along the Y_AXIS only, and 
 * only if <code>len</code> is after the first row.  If the length
 * is less than one row, a value of BadBreakWeight is returned.
 *
 * @param axis may be either View.X_AXIS or View.Y_AXIS
 * @param len specifies where a potential break is desired >= 0
 * @return a value indicating the attractiveness of breaking here
 * @see View#getBreakWeight
 */
public int getBreakWeight(int axis, float len)
{
	return BadBreakWeight;
}
protected CoSizeRequirements[] getChildRequests(int axis)
{
	// PENDING(prinz) This generates too much garbage.
	int n = getViewCount();
	CoSizeRequirements[] reqs = new CoSizeRequirements[n];
	for
		(int i = 0; i < n; i++)
	{
		View v = getView(i);
		float min = v.getMinimumSpan(axis);
		float pref = v.getPreferredSpan(axis);
		float max = v.getMaximumSpan(axis);
		float a = v.getAlignment(axis);
		reqs[i] = new CoSizeRequirements(min, pref, max, a);
	}
	return reqs;
}
/**
 * Returns the closest model position to <code>x</code>.
 * <code>rowIndex</code> gives the index of the view that corresponds
 * that should be looked in.
 */
// NOTE: This will not properly work if ParagraphView contains
// other ParagraphViews. It won't raise, but this does not message
// the children views with getNextVisualPositionFrom.
protected int getClosestPositionTo(int pos, Position.Bias b, Shape a, int direction, Position.Bias[] biasRet, int rowIndex, float x) throws BadLocationException
{
	if ( getViewCount() == 0 ) return pos; // outside column geometry
	
	JTextComponent text = (JTextComponent) getContainer();
	Document doc = getDocument();
	DefaultStyledDocument aDoc = (doc instanceof DefaultStyledDocument) ? (DefaultStyledDocument) doc : null;
	View row = getView(rowIndex);
	int lastPos = -1;
	// This could be made better to check backward positions too.
	biasRet[0] = Position.Bias.Forward;
	for (int vc = 0, numViews = row.getViewCount(); vc < numViews; vc++)
	{
		View v = row.getView(vc);
		int start = v.getStartOffset();
		boolean ltr = (aDoc != null) ? aDoc.isLeftToRight(start, start + 1) : true;
		if (ltr)
		{
			lastPos = start;
			for (int end = v.getEndOffset(); lastPos < end; lastPos++)
			{
				if (text.modelToView(lastPos).getBounds().x >= x)
				{
//					if ( lastPos == 0 ) return lastPos;
//					return v.getNextVisualPositionFrom( lastPos - 1, b, a, EAST, biasRet );
					return lastPos;
				}
			}
			lastPos--;
		}
		else
		{
			for (lastPos = v.getEndOffset() - 1; lastPos >= start; lastPos--)
			{
				if (text.modelToView(lastPos).getBounds().x >= x)
				{
					return lastPos;
				}
			}
			lastPos++;
		}
	}
	if (lastPos == -1)
	{
		return getStartOffset();
	}
	return lastPos;
}
public CoColumnGeometryIF.CoColumnIF getColumn()
{
	return m_column;
}
public Document getDocument()
{
	return m_element.getDocument();
}
public Element getElement()
{
	return m_element;
}
  /**
   * Överriden metod från javax.swing.text.View.
   * Gör att en delad instans returnerar rätt värde.
   * @see javax.swing.text.View.getEndOffset.
   */
public int getEndOffset()
{
  if
		( m_offset != -1 )
  {
		return Math.min( getElement().getEndOffset(), getElement().getStartOffset() + m_offset + m_length );
  } else {
		return getElement().getEndOffset();
  }
}
protected Rectangle2D getInsideAllocation( Shape a )
{
	if
		( a != null )
	{
		Rectangle2D alloc = a.getBounds();
		alloc.setRect( alloc.getX() + getLeftInset(),
			             alloc.getY() + getTopInset(),
			             alloc.getWidth() - getLeftInset() - getRightInset(),
			             alloc.getHeight() - getTopInset() - getBottomInset() );
		/*
		alloc.x += getLeftInset();
		alloc.y += getTopInset();
		alloc.width -= getLeftInset() + getRightInset();
		alloc.height -= getTopInset() + getBottomInset();
		*/
		return alloc;
	}
	return null;
}
/**
 * Overriden from CompositeView.
 */
protected int getNextNorthSouthVisualPositionFrom(int pos, Position.Bias b, Shape a, int direction, Position.Bias[] biasRet) throws BadLocationException
{
	int vIndex;
	if
		(pos == -1)
	{
		vIndex = (direction == SwingConstants.NORTH) ? getViewCount() - 1 : 0;
	} else {
	
		if
			(b == Position.Bias.Backward && pos > 0)
		{
			vIndex = getViewIndexAtPosition(pos - 1);
		} else {
			vIndex = getViewIndexAtPosition(pos);
		}

		if
			(direction == NORTH)
		{
			if
				(vIndex == 0)
			{
				return -1;
			}
			vIndex--;
		} else {
			if
				( vIndex == getViewCount() - 1 )
			{
				return -1;
			}
			vIndex++;
		}
	}

	if ( vIndex < 0 ) return -1;

	
	// vIndex gives index of row to look in.
	JTextComponent text = (JTextComponent) getContainer();
	Caret c = text.getCaret();
	Point magicPoint;
	magicPoint = (c != null) ? c.getMagicCaretPosition() : null;
	int x;
	
	if
		(magicPoint == null)
	{
		Shape posBounds = text.getUI().modelToView(text, pos, b);
		if
			(posBounds == null)
		{
			x = 0;
		} else {
			x = posBounds.getBounds().x;
		}
	} else {
		x = magicPoint.x;
	}
	
	return getClosestPositionTo(pos, b, a, direction, biasRet, vIndex, x);
}
	/**
	 * Returns the size used by the views between <code>startOffset</code>
	 * and <code>endOffset</code>. This uses getPartialView to calculate the
	 * size if the child view implements the TabableView interface. If a 
	 * size is needed and a View does not implement the TabableView
	 * interface, the preferredSpan will be used.
	 *
	 * @param startOffset the starting document offset >= 0
	 * @param endOffset the ending document offset >= startOffset
	 * @return the size >= 0
	 */
	protected float getPartialSize(int startOffset, int endOffset) {
		float size = 0.0f;
		int viewIndex;
		int numViews = getViewCount();
		View view;
		int viewEnd;
		int tempEnd;

		// Have to search layoutPool!
		// PENDING: when ParagraphView supports breaking location
		// into layoutPool will have to change!
		viewIndex = getElement().getElementIndex(startOffset);
		numViews = m_layoutPool.size();
		while(startOffset < endOffset && viewIndex < numViews) {
			view = (View) m_layoutPool.get(viewIndex++);
			viewEnd = view.getEndOffset();
			tempEnd = Math.min(endOffset, viewEnd);
			if(view instanceof TabableView)
				size += ((TabableView)view).getPartialSpan(startOffset, tempEnd);
			else if(startOffset == view.getStartOffset() &&
					tempEnd == view.getEndOffset())
				size += view.getPreferredSpan(View.X_AXIS);
			else
				// PENDING: should we handle this better?
				return 0.0f;
			startOffset = viewEnd;
		}
		return size;
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
	checkRequests();

	if
		( axis == Y_AXIS )
	{
		return m_yRequest.m_preferred + getTopInset() + getBottomInset();
	} else {
		return m_xRequest.m_preferred + getLeftInset() + getRightInset();
	}
}
// --- public methods ---

/**
   * Överriden metod från javax.swing.text.View.
   * Gör att en delad instans returnerar rätt värde.
   * @see javax.swing.text.View.getStartOffset.
   */
public int getStartOffset()
{
	if
		(m_offset != -1)
	{
		return getElement().getStartOffset() + m_offset;
	} else {
		return getElement().getStartOffset();
	}
}
/**
 * @return where tabs are calculated from.
 */
protected float getTabBase()
{
	return (float) m_tabBase;
}
protected CoTabSetIF getTabSet()
{
	
	return m_tabSet;
}
protected View getViewAtPoint(int x, int y, Rectangle2D alloc)
{
	double X = alloc.getX();
	double Y = alloc.getY();
	
	if
		(hasDropCaps())
	{
		if
			(x < (X + m_xPos[0] + m_xSpan[0]))
		{
			if
				(y < (Y + m_yPos[0] + m_ySpan[0]))
			{
				childAllocation(0, alloc);
				return getView(0);
			}
		}
	}

	int n = getViewCount();
	if
		(y < (Y + m_yPos[0]))
	{
		childAllocation(0, alloc);
		return getView(0);
	}
	for
		(int i = 0; i < n; i++)
	{
		if
			(y < (Y + m_yPos[i]))
		{
			childAllocation(i - 1, alloc);
			return getView(i - 1);
		}
	}
	childAllocation(n - 1, alloc);
	return getView(n - 1);
}
/**
 * Fetches the child view that represents the given position in
 * the model.  This is implemented to walk through the children
 * looking for a range that contains the given position.  In this
 * view the children do not have a one to one mapping with the
 * child elements (i.e. the children are actually rows that
 * represent a portion of the element this view represents).
 *
 * @param pos  the search position >= 0
 * @param a  the allocation to the box on entry, and the
 *   allocation of the view containing the position on exit
 * @returns  the view representing the given position, or 
 *   null if there isn't one
 */
protected View getViewAtPosition(int pos, Rectangle2D a)
{
	int n = getViewCount();
	for (int i = 0; i < n; i++)
	{
		View v = getView(i);
		int p0 = v.getStartOffset();
		int p1 = v.getEndOffset();
		if ((pos >= p0) && (pos < p1))
		{
			// it's in this view.
			if (a != null)
			{
				childAllocation(i, a);
			}
			return v;
		}
	}
	if (pos == getEndOffset())
	{
		// PENDING(bcb): This will probably want to choose the first
		// if right to left.
		View v = getView(n - 1);
		if (a != null)
		{
			this.childAllocation(n - 1, a);
		}
		return v;
	}
	return null;
}
	/**
	 * Fetches the child view index representing the given position in
	 * the model.
	 *
	 * @param pos the position >= 0
	 * @returns  index of the view representing the given position, or 
	 *   -1 if no view represents that position
	 */
	protected int getViewIndexAtPosition(int pos) {
	// This is expensive, but are views are not necessarily layed
	// out in model order.
	if(pos < getStartOffset() || pos >= getEndOffset())
	    return -1;
	for(int counter = getViewCount() - 1; counter >= 0; counter--) {
	    View v = getView(counter);
	    int p0 = v.getStartOffset();
	    int p1 = v.getEndOffset();
	    if ( p0 == p1 ) continue;
	    if ( ( pos >= p0 ) && ( pos < p1 ) )
	    {
				return counter;
	    }
	}
	return -1;
	}
private float getY0()
{
	return m_y + ( ( m_head == null ) ? getTopInset() : 0 );
}
private boolean hasDropCaps()
{
	return m_dropCaps && ( m_head == null ) && ( getElement().getEndOffset() > getElement().getStartOffset() + 1 );
}
/**
 * Gives notification that something was inserted into the document
 * in a location that this view is responsible for.
 *
 * @param changes the change information from the associated document
 * @param a the current allocation of the view
 * @param f the factory to use to rebuild if the view has children
 * @see View#insertUpdate
 */
public void insertUpdate(DocumentEvent changes, Shape a, ViewFactory f)
{
	final boolean DO_TRACE = false;
	final boolean DO_TIME = false;

	if ( DO_TRACE ) System.err.println( "paragraph.insertUpdate " + this );
	long t = System.currentTimeMillis();

	invalidateBreakPoints();
	invalidateRulerData();

	// update the pool of logical children
	Element elem = getElement();
	DocumentEvent.ElementChange ec = changes.getChange(elem);
	if
		( ec != null )
	{
		// the structure of this element changed.
		updateLogicalChildren( ec, f );
	}

	if ( DO_TIME ) System.err.println( System.currentTimeMillis() - t );
//	t = CoTimeStamp.nowMillis();

	// find and forward if there is anything there to forward to.
	// If children were removed then there was
	// a replacement of the removal range and there is no need to forward.
	if
		(ec != null && ec.getChildrenAdded().length > 0)
	{
		int index = ec.getIndex();
		int pos = changes.getOffset();
		if
			(index > 0)
		{
			Element child = elem.getElement(index - 1);
			if (child.getEndOffset() >= pos)
			{
				View v = (View) m_layoutPool.get(index - 1);
				v.insertUpdate(changes, null, f);
			}
		}
		int endIndex = index + ec.getChildrenAdded().length;
		if
			(endIndex < m_layoutPool.size())
		{
			Element child = elem.getElement(endIndex);
			int start = child.getStartOffset();
			if (start >= pos && start <= (pos + changes.getLength()))
			{
				View v = (View) m_layoutPool.get(endIndex);
				v.insertUpdate(changes, null, f);
			}
		}
	} else {
	//REMIND(bcb) It is possible for an event have no added children,
	//a removed child and a change to an existing child.  To see, this
	//do the following.  Bring up Stylepad. Empty its contents. Select
	//a different font.  Type a line until it wraps then hit return.
	//Someone should code review this change.
		int pos = changes.getOffset();
		int index = elem.getElementIndex(pos);
		View v = (View) m_layoutPool.get(index);
		v.insertUpdate(changes, null, f);
		if (index > 0 && v.getStartOffset() == pos)
		{
			v = (View) m_layoutPool.get(index - 1);
			v.insertUpdate(changes, null, f);
		}
	}


	
	if ( DO_TIME ) System.err.println( System.currentTimeMillis() - t );


	
	
	Rectangle2D alloc = getInsideAllocation(a);
	if
		( alloc != null )
	{
		if
			( updateSubParagraphs( changes ) )
		{
			preferenceChanged( null, true, true );
		}
		
		layout();
		Component host = getContainer();
		host.repaint( (int) alloc.getX(), (int) alloc.getY(), (int) alloc.getWidth(), (int) alloc.getHeight());
	}
	
	if ( DO_TIME ) System.err.println( System.currentTimeMillis() - t );

	if ( DO_TRACE ) System.err.println( "paragraph.insertUpdate <<" );
}
private void invalidateAlloc()
{
	m_allocValid = false;
}
private void invalidateBreakPoints()
{
	CoParagraphView v = this;
	while ( v.m_head != null ) v = v.m_head;
	while
		( v != null )
	{
		v.m_breakpoints = null;
		v = v.m_tail;
	}
}
protected void invalidateRulerData()
{
	m_topRulerShape.setLine( -42, -42, -42, -42 );
	m_bottomRulerShape.setLine( -42, -42, -42, -42 );
}
protected boolean isAfter( int x, int y, Rectangle2D innerAlloc )
{
	return y > ( innerAlloc.getHeight() + innerAlloc.getY() );
}
protected boolean isBefore(int x, int y, Rectangle2D innerAlloc)
{
	return y < innerAlloc.getY();
}
/**
 * Lays out the children.  If the layout span has changed,
 * the rows are rebuilt.  The superclass functionality
 * is called after checking and possibly rebuilding the
 * rows.  If the height has changed, the 
 * <code>preferenceChanged</code> method is called
 * on the parent since the vertical preference is 
 * rigid.
 *
 * @param width  the width to lay out against >= 0.  This is the width inside of the inset area.
 * @param height the height to lay out against >= 0 (not used by paragraph, but used by the superclass).
 *        This is the height inside of the inset area.
 */
protected void layout()
{
	checkRequests();

	// rebuild the allocation arrays if they've been removed due to a change in child count.
	if
		( m_xSpan == null )
	{
		int n = getViewCount();
		m_xSpan = new float[ n ];
		m_ySpan = new float[ n ];
		m_xPos = new float[ n ];
		m_yPos = new float[ n ];
	}
	
	if
		( ! m_allocValid )
	{
		layoutBothAxis();
		m_allocValid = true;
	}

	// flush changes to the children
	int n = getViewCount();
	for
		( int i = 0; i < n; i++ )
	{
		View v = getView( i );
		v.setSize( m_xSpan[ i ], m_ySpan[ i ] );
	}
}
protected void layoutBothAxis()
{
	m_column.setMargins( getLeftInset(), getRightInset() );

	boolean hasDropCaps = hasDropCaps();
	int skippedDropCapRows = 0;
	float range[] = new float[ 2 ];
	
	int I = getViewCount();

	// can happen if updateRows( boolean ) is told to stop at end of column
	if ( I == 0 ) return;
	
	for
		( int i = 0; i < I; i++ )
	{
		// fetch and measure row
		Row row = (Row) getView( i );
		float requiredWidth = row.getMaximumSpan( X_AXIS );
		float y = row.getY();
		
		// calculate available space
		m_column.getRange( y, 0, range );
//		m_column.getMinimalRange( y, y + calcHeight( row.getPreferredSpan( Y_AXIS ), row.getFontSize() ), range );
		m_column.getMinimalRange( y, y + row.getPreferredSpan( Y_AXIS ), range );
		float availableWidth = range[ CoColumnGeometryIF.CoColumnIF.WIDTH ];

		// layout row
		m_yPos[i] = ( row.getY() - m_y ) - getTopInset();
		m_ySpan[i] = row.getPreferredSpan( Y_AXIS );
		
		float x = range[ CoColumnGeometryIF.CoColumnIF.X ];
		
		if
			( requiredWidth <= availableWidth )
		{
			// can't make the child this wide, align it
			float align = row.getAlignment(X_AXIS);
			m_xPos[i] = ( ( availableWidth - requiredWidth ) * align ) + x - getLeftInset();
			
			if
				( ! ( hasDropCaps && ( i == 0 ) ) && row.isJustified() )
			{
				// force child width
				requiredWidth = availableWidth;
			}
			
			m_xSpan[i] = requiredWidth;
			
		} else {
			// make it the target width, or as small as it can get.
			m_xPos[i] = x - getLeftInset();
			m_xSpan[i] = Math.max( row.getMinimumSpan(X_AXIS), availableWidth);
		}

		// adjust margins to account for drop caps
		if
			( hasDropCaps )
		{
			if
				( i == 0 )
			{
				m_column.setMargins( range[ 0 ] + m_dropCapsViewWidth, getRightInset() );
			} else if
				( i + skippedDropCapRows == m_dropCapsLineCount )
			{
				m_column.setMargins( getLeftInset(), getRightInset() );
			}
		}
	}

	// redo layout of drop caps rows
	if
		( hasDropCaps )
	{
		if
			( ( skippedDropCapRows < m_dropCapsLineCount ) && ( I > 1 ) )
		{
			float x = m_xPos[ 1 ];
			for
				( int i = 2; ( i <= m_dropCapsLineCount - skippedDropCapRows ) && ( i < I ); i++ )
			{
				if ( i >= m_xPos.length ) break;
				if ( m_xPos[ i ] < x ) x = m_xPos[ i ];
			}
			m_xPos[ 0 ] = x - m_xSpan[ 0 ];
			m_xPos[ 1 ] = x;
		}
	}	
}
/**
 * Loads all of the children to initialize the view.
 * This is called by the <code>setParent</code> method.
 * This is reimplemented to not load any children directly
 * (as they are created in the process of formatting).
 * This does create views to represent the child elements,
 * but they are placed into a pool that is used in the 
 * process of formatting.
 *
 * @param f the view factory
 */
protected void loadChildren(ViewFactory f)
{
	// if view is a tail then its layout pool is already up to date.
	if ( m_head != null ) return;

	int I = m_layoutPool.size();
	for ( int i = 0; i < I; i++ ) ( (CoLabelView) m_layoutPool.get( i ) ).release();
	m_layoutPool.clear();
	
	Element e = getElement();
	I = e.getElementCount();
	
	for
		( int i = 0; i < I; i++ )
	{
		View v = f.create( e.getElement( i ) );
		v.setParent( this );
		m_layoutPool.add( v );
	}
}
/**
   * Sätt ihop denna instans med dess svans om den har någon.
   * @return sanningsvärde som talar om ifall objektet har förändrats eller ej.
   */
int mergeTail()
{
	// kolla om svans finns
	if ( m_tail == null ) return 0;

	int i = m_tail.mergeTail();
	m_tail.release();
	m_tail = null;
	m_length = m_offset = -1;
	return i + 1;
}
// --- TabExpander methods ------------------------------------------

/**
 * Returns the next tab stop position given a reference position.
 * This view implements the tab coordinate system, and calls
 * <code>getTabbedSpan</code> on the logical children in the process 
 * of layout to determine the desired span of the children.  The
 * logical children can delegate their tab expansion upward to
 * the paragraph which knows how to expand tabs. 
 * <code>LabelView</code> is an example of a view that delegates
 * its tab expansion needs upward to the paragraph.
 * <p>
 * This is implemented to try and locate a <code>TabSet</code>
 * in the paragraph element's attribute set.  If one can be
 * found, its settings will be used, otherwise a default expansion
 * will be provided.  The base location for for tab expansion
 * is the left inset from the paragraphs most recent allocation
 * (which is what the layout of the children is based upon).
 *
 * @param x the X reference position
 * @param tabOffset the position within the text stream
 *   that the tab occurred at >= 0.
 * @return the trailing end of the tab expansion >= 0
 * @see TabSet
 * @see TabStop
 * @see LabelView
 */
public float nextTabStop(float x, int tabOffset)
{
	// If the text isn't left justified, offset by 10 pixels!
	if (!m_justification.equals(CoTextConstants.ALIGN_LEFT)) return x + 10.0f;

//	m_tabBase = 0;
//	x -= m_tabBase;

	m_tabBase = (int) m_column.getBounds().getX();
	
	CoTabSetIF tabs = getTabSet();
	if 
		(tabs == null)
	{
		// a tab every 72 pixels.
		m_currentTabStop = m_fallbackTabStop;
		int p = (int) m_currentTabStop.getPosition();
		return (float) (m_tabBase + (((int) x / p + 1) * p));
	}
	
	m_currentTabStop = tabs.getTabAfter(x + .01f);
	if
		(m_currentTabStop == null)
	{
		// no tab, do a default of 5 pixels.
		// Should this cause a wrapping of the line?
		return m_tabBase + x + 5.0f;
	}
	int alignment = m_currentTabStop.getAlignment();
	int offset;
	switch
		(alignment)
	{
		default :
		case CoTabStopIF.ALIGN_LEFT : // Simple case, left tab.
			return m_tabBase + m_currentTabStop.getPosition();
			//		case CoTabStopIF.ALIGN_BAR:
			// PENDING: what does this mean?
			//			return m_tabBase + tab.getPosition();
		case CoTabStopIF.ALIGN_RIGHT :
		case CoTabStopIF.ALIGN_CENTER :
			offset = findOffsetToCharactersInString(m_tabChars, tabOffset + 1);
			break;
		case CoTabStopIF.ALIGN_DECIMAL :
			offset = findOffsetToCharactersInString(m_tabDecimalChars, tabOffset + 1);
			break;
		case CoTabStopIF.ALIGN_ON :
			offset = findOffsetToCharactersInString(m_currentTabStop.alignOn(), tabOffset + 1);
			break;
	}
	if
		(offset == -1)
	{
		offset = getEndOffset();
	}
	float charsSize = getPartialSize(tabOffset + 1, offset);
	switch
		(alignment)
	{
		case CoTabStopIF.ALIGN_RIGHT :
		case CoTabStopIF.ALIGN_ON :
		case CoTabStopIF.ALIGN_DECIMAL : // right and decimal are treated the same way, the new position will be the location of the tab less the partialSize.
			return m_tabBase + Math.max(x, m_currentTabStop.getPosition() - charsSize);
			
		case CoTabStopIF.ALIGN_CENTER : // Similar to right, but half the partialSize.
			return m_tabBase + Math.max(x, m_currentTabStop.getPosition() - charsSize / 2.0f);
	}
	// will never get here!
	return x;
}
private float nextY( float y )
{
	if ( y < m_firstBaseLine ) return m_firstBaseLine;

	int n = 1 + (int) ( ( y + 1 - m_firstBaseLine ) / m_baseLineSpacing );

	return n * m_baseLineSpacing + m_firstBaseLine;
}

/**
 * Implementation av javax.swing.text.View.
 * @see javax.swing.text.View.paint.
 */
public void paintSelectionShadow( Graphics2D g, int from, int to, Shape allocation )
{
  Rectangle2D alloc = allocation.getBounds2D();

  double x = alloc.getX() + getLeftInset();
  double y = alloc.getY() + getTopInset();

  from = Math.max( from, getStartOffset() );
  to = Math.min( to, getEndOffset() );
  
  int i0 = getViewIndexAtPosition( from );
	if ( i0 == -1 ) return;
	
	int i1 = getViewIndexAtPosition( to - 1 );
	if ( i1 == -1 ) return;
	
  // sub-View'er
  for
		( int i = i0; i <= i1; i++ )
  {
	  alloc.setRect( x + m_xPos[i],
		               y + m_yPos[i],
		               m_xSpan[i],
		               m_ySpan[i] );
	  /*
		alloc.x = x + (int) ( 0.5f + m_xPos[i] );
		alloc.y = y + (int) ( 0.5f + m_yPos[i] );
		alloc.width = (int) ( 0.5f + m_xSpan[i] );
		alloc.height = (int) ( 0.5f + m_ySpan[i] );
		*/
		CoHighlightableView v = (CoHighlightableView) getView( i );
		v.paintSelectionShadow( g, from, to, alloc );
  }
}
private void prepareBottomRuleShape( double w, double h, double li, double ri, double ti, double bi )
{
	double x0;
	double x1;
	double y0 = h - bi + bi * m_bottomRulerPosition / 100;
	
	if
		( m_bottomRulerFixedWidth > 0 )
	{
		// fixed width
		if
			( m_bottomRulerJustification.equals( CoTextConstants.ALIGN_LEFT ) )
		{
			x0 = li;
		} else if
			( m_bottomRulerJustification.equals( CoTextConstants.ALIGN_CENTER ) )
		{
			x0 = ( w - li - ri - m_bottomRulerFixedWidth ) / 2 + li;
		} else {
			x0 = w - li - ri - m_bottomRulerFixedWidth;
		}
		x1 = x0 + m_bottomRulerFixedWidth;
	} else if
		( m_bottomRulerIsColumn )
	{
		// column span
		x0 = li + m_bottomRulerLeftIndent;
		x1 = ( w - ri ) - m_bottomRulerRightIndent;
	} else {
		// text span
		int i = m_xPos.length - 1;
		x0 = m_xPos[ i ] + li + m_firstLineIndent + m_bottomRulerLeftIndent;
		x1 = m_xPos[ i ] + li + m_xSpan[ i ] - m_bottomRulerRightIndent;
	}
	
	m_bottomRulerShape.setLine( x0, y0, x1, y0 );
}
private void prepareTopRuleShape( double w, double h, double li, double ri, double ti, double bi )
{
	double x0;
	double x1;
	double y0 = ti - ti * m_topRulerPosition / 100;
	
	if
		( m_topRulerFixedWidth > 0 )
	{
		// fixed width
		if
			( m_topRulerJustification.equals( CoTextConstants.ALIGN_LEFT ) )
		{
			x0 = li;
		} else if
			( m_topRulerJustification.equals( CoTextConstants.ALIGN_CENTER ) )
		{
			x0 = ( w - li - ri - m_topRulerFixedWidth ) / 2 + li;
		} else {
			x0 = w - li - ri - m_topRulerFixedWidth;
		}
		x1 = x0 + m_topRulerFixedWidth;
	} else if
		( m_topRulerIsColumn )
	{
		// column span
		x0 = li + m_topRulerLeftIndent;
		x1 = ( w - ri ) - m_topRulerRightIndent;
	} else {
		// text span
		x0 = m_xPos[ 0 ] + li + m_firstLineIndent + m_topRulerLeftIndent;
		x1 = m_xPos[ 0 ] + li + m_xSpan[ 0 ] - m_topRulerRightIndent;
	}
	
	m_topRulerShape.setLine( x0, y0, x1, y0 );
}
void rebuildRows()
{
//	System.err.println( "rebuildRows " + this );
	updateRows( false );
}
public void release()
{
	if
		( ! getPool().contains( this ) )
	{	
		if ( TRACE_POOL ) System.err.println( "RELEASE " + this );
		getPool().add( this );
	} else {
		com.bluebrim.base.shared.debug.CoAssertion.assertTrue( false, "" );
	}
	
	int I = getViewCount();
	for
		( int i = 0; i < I; i++ )
	{
		( (CoReuseableView) getView( i ) ).release();
	}

	if
		( m_head == null ) // head owns layout pool
	{
		I = m_layoutPool.size();
		for
			( int i = 0; i < I; i++ )
		{
			( (CoReuseableView) m_layoutPool.get( i ) ).release();
		}
		m_layoutPool.clear();
	}

	removeAll();
}
/**
 * Gives notification that something was removed from the document
 * in a location that this view is responsible for.
 *
 * @param changes the change information from the associated document
 * @param a the current allocation of the view
 * @param f the factory to use to rebuild if the view has children
 * @see View#removeUpdate
 */
public void removeUpdate(DocumentEvent changes, Shape a, ViewFactory f)
{
	final boolean DO_TRACE = false;
	final boolean DO_TIME = false;
	if ( DO_TRACE ) System.err.println( "paragraph.removeUpdate " + this );
	long t = System.currentTimeMillis();

	invalidateBreakPoints();
	invalidateRulerData();
	
	// update the pool of logical children
	Element elem = getElement();
	DocumentEvent.ElementChange ec = changes.getChange(elem);
	if
		(ec != null)
	{
		// the structure of this element changed.
		updateLogicalChildren(ec, f);
	}

	if ( DO_TIME ) System.err.println( System.currentTimeMillis() - t );

	// find and forward if there is anything there to 
	// forward to.  If children were added then there was
	// a replacement of the removal range and there is no
	// need to forward.
	if
		(ec == null || (ec.getChildrenAdded().length == 0))
	{
		int pos = changes.getOffset();
		int index = elem.getElementIndex(pos);
		View v = (View) m_layoutPool.get(index);
		v.removeUpdate(changes, null, f);
	}

	if ( DO_TIME ) System.err.println( System.currentTimeMillis() - t );

	if
		(a != null)
	{
		Rectangle2D alloc = getInsideAllocation(a).getBounds2D();

		if
			( ( ec != null ) || updateSubParagraphs( changes ) )
		{
			preferenceChanged( null, true, true );
		}

		layout();
		Component host = getContainer();
		host.repaint( (int) alloc.getX(), (int) alloc.getY(), (int) alloc.getWidth(), (int) alloc.getHeight());
	}

	if ( DO_TIME ) System.err.println( System.currentTimeMillis() - t );
	if ( DO_TRACE ) System.err.println( "paragraph.removeUpdate <<" );
}
//	###1###
public void replace(int offset, int length, View[] elems)
{
	super.replace(offset, length, elems);

	// invalidate cache 
	m_xPos = null;
	m_xSpan = null;
	m_xRequestValid = false;
	m_yPos = null;
	m_ySpan = null;
	m_yRequestValid = false;
	invalidateAlloc();
}
private void reset()
{
//	removeAll();
	
	setInsets( (short) 0, (short) 0, (short) 0, (short) 0 );
	
	setParent( null );
	
	m_element = null;
	
	m_breakpoints = null;
	m_breakpointCount = 0;
	m_nextBreakpoint = 0;

	m_column = null;
	m_y = 0;
	m_deltaYrow = 0;

	m_width = 0;
	m_height = 0;

	m_xRequestValid = false;
	m_yRequestValid = false;

	m_firstBaseLine = 0;
	m_baseLineSpacing = 0;
		
	m_xRequest = null;
	m_yRequest = null;
	
	m_xPos = null;
	m_xSpan = null;
	m_yPos = null;
	m_ySpan = null;
	m_allocValid = false;
	m_visibleRowCount = 0;

	m_fallbackRowHeight = 0;

	m_dropCapsViewWidth = 0;

	if
		( m_head != null )
	{
		m_layoutPool = null;
	}
	
	m_head = null;
	m_tail = null;

	m_offset = -1;
	m_length = -1;
	
	m_leading = null;
	/*
	m_isLeadingAbsolute = false;
	m_isLeadingOffset = false;
	m_isLeadingRelative = false;
	m_autoLeading = false;
	*/
	
	m_justification = null;
	m_firstLineIndent = 0;
	m_trailingLinesIndent = 0;
	m_dropCaps = false;
	m_dropCapsLineCount = 0;
	m_dropCapsCharacterCount = 0;
	m_keepLinesTogether = false;
	m_topOfColumn = false;
	m_lastInColumn = false;
	m_lineBreaker = null;
	m_tabSet = null;
	m_adjustToBaseLineGrid = false;

	m_currentTabStop = null;
//	m_fallbackTabStop = new CoTabStop();

	
//	m_topRulerShape = new Line2D.Double();
//	m_topRulerStroke = new HorizontalLineStroke();

	m_topRulerPosition = 0;
	m_topRulerJustification = null;
	m_topRulerFixedWidth = 0;
	m_topRulerLeftIndent = 0;
	m_topRulerRightIndent = 0;
	m_topRulerIsColumn = false;
	
	
//	m_bottomRulerShape = new Line2D.Double();
//	m_bottomRulerStroke = new HorizontalLineStroke();

	m_bottomRulerPosition = 0;
	m_bottomRulerJustification = null;
	m_bottomRulerFixedWidth = 0;
	m_bottomRulerLeftIndent = 0;
	m_bottomRulerRightIndent = 0;
	m_bottomRulerIsColumn = false;
	
	m_tabBase = 0;

	/*
	int I = m_layoutPool.size();
	for ( int i = 0; i < I; i++ ) ( (CoLabelView) m_layoutPool.get( i ) ).release();
	m_layoutPool.clear();
	*/

//	m_text = new Segment();
}
private void set( Element elem )
{
	m_element = elem;

	if
		( m_layoutPool == null )
	{
		m_layoutPool = new ArrayList();
	}
	
	setPropertiesFromAttributes( false );
}
private void set( Element elem, CoParagraphView head )
{
	m_element = elem;
	
	m_head = head;

	setInsets( (short) 0, m_head.getLeftInset(), m_head.getBottomInset(), m_head.getRightInset() );
		
	m_justification = m_head.m_justification;
	m_leading = m_head.m_leading;
	m_firstLineIndent = m_head.m_firstLineIndent;
	m_trailingLinesIndent = m_head.m_trailingLinesIndent;
//	m_autoLeading = m_head.m_autoLeading;
	m_dropCaps = m_head.m_dropCaps;
	m_dropCapsLineCount = m_head.m_dropCapsLineCount;
	m_dropCapsCharacterCount = m_head.m_dropCapsCharacterCount;
	m_keepLinesTogether = m_head.m_keepLinesTogether;
	m_topOfColumn = false;
	m_lastInColumn = m_head.m_lastInColumn;
//	m_isLeadingAbsolute = m_head.m_isLeadingAbsolute;
//	m_isLeadingRelative = m_head.m_isLeadingRelative;
//	m_isLeadingOffset = m_head.m_isLeadingOffset;
	m_fallbackRowHeight = m_head.m_fallbackRowHeight;
	m_lineBreaker = m_head.m_lineBreaker;
	m_adjustToBaseLineGrid = m_head.m_adjustToBaseLineGrid;

	m_layoutPool = m_head.m_layoutPool;

	m_breakpointCount = m_head.m_breakpointCount;
	m_breakpoints = m_head.m_breakpoints;

	m_fallbackTabStop = m_head.m_fallbackTabStop;

	m_bottomRulerShape = m_head.m_bottomRulerShape;
	m_bottomRulerStroke = m_head.m_bottomRulerStroke;
	m_bottomRulerPosition = m_head.m_bottomRulerPosition;
	m_bottomRulerJustification = m_head.m_bottomRulerJustification;
	m_bottomRulerFixedWidth = m_head.m_bottomRulerFixedWidth;
	m_bottomRulerLeftIndent = m_head.m_bottomRulerLeftIndent;
	m_bottomRulerRightIndent = m_head.m_bottomRulerRightIndent;
	m_bottomRulerIsColumn = m_head.m_bottomRulerIsColumn;
}
void setBaseLineGrid( float y0, float dy )
{
	m_firstBaseLine = y0;
	m_baseLineSpacing = dy;
}
void setColumn( CoColumnGeometryIF.CoColumnIF column, float y )
{
	m_column = column;
	m_y = y;
}
protected void setPropertiesFromAttributes( boolean notifyParent )
{
	AttributeSet attr = getAttributes();
	if
		( attr != null )
	{
		boolean doRebuildAll = false;

		int oldSpaceAbove = getTopInset();
		int oldSpaceBelow = getBottomInset();

		int spaceAbove = 0;
		if ( m_head == null ) spaceAbove = (int) CoViewStyleConstants.getSpaceAbove( attr );	
		int spaceBelow = (int) CoViewStyleConstants.getSpaceBelow( attr );
		int indentLeft = (int) CoViewStyleConstants.getLeftIndent( attr );
		int indentRight = (int) CoViewStyleConstants.getRightIndent( attr );
		setInsets( (short) spaceAbove, (short) indentLeft, (short) spaceBelow, (short) indentRight );
		
		if ( oldSpaceAbove != spaceAbove ) doRebuildAll = true;
		if ( oldSpaceBelow != spaceBelow ) doRebuildAll = true;
		
		m_fallbackRowHeight = CoViewStyleConstants.getCoFont( attr ).getLineMetrics().getHeight();
		/*
		m_isLeadingAbsolute = false;
		m_isLeadingRelative = false;
		m_isLeadingOffset = false;
*/
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

		m_justification = CoViewStyleConstants.getAlignment( attr );
		if      ( m_justification.equals( CoStyleConstants.ALIGN_LEFT ) )      m_justification = CoStyleConstants.ALIGN_LEFT;
		else if ( m_justification.equals( CoStyleConstants.ALIGN_JUSTIFIED ) ) m_justification = CoStyleConstants.ALIGN_JUSTIFIED;
		else if ( m_justification.equals( CoStyleConstants.ALIGN_FORCED ) )    m_justification = CoStyleConstants.ALIGN_FORCED;
		else if ( m_justification.equals( CoStyleConstants.ALIGN_RIGHT ) )     m_justification = CoStyleConstants.ALIGN_RIGHT;
		else if ( m_justification.equals( CoStyleConstants.ALIGN_CENTER ) )    m_justification = CoStyleConstants.ALIGN_CENTER;

		
		{
			boolean old = m_useQxpJustification;
			m_useQxpJustification = ( (CoStyledDocumentIF) getDocument() ).getUseQxpJustification();
			if ( old != m_useQxpJustification ) doRebuildAll = true;
		}

		{
			float old = m_optimumSpaceWidth;
			m_optimumSpaceWidth = 1 - CoViewStyleConstants.getOptimumSpaceWidth( attr ) / 100;
			if ( old != m_optimumSpaceWidth ) doRebuildAll = true;
		}

		{
			float old = m_minimumSpaceWidth;
			m_minimumSpaceWidth = 1 - CoViewStyleConstants.getMinimumSpaceWidth( attr ) / 100;
			if ( old != m_minimumSpaceWidth ) doRebuildAll = true;
		}

		m_firstLineIndent = (int) CoViewStyleConstants.getFirstLineIndent(attr);
		m_trailingLinesIndent = (int) CoViewStyleConstants.getTrailingLinesIndent(attr);
		m_dropCaps = CoViewStyleConstants.hasDropCaps(attr);
		if
			( m_dropCaps )
		{
			m_dropCapsLineCount = CoViewStyleConstants.getDropCapsLineCount(attr);
			m_dropCapsCharacterCount = CoViewStyleConstants.getDropCapsCharacterCount(attr);
		}


		{
			boolean old = m_keepLinesTogether;
			m_keepLinesTogether = CoViewStyleConstants.hasKeepLinesTogether(attr);
			if ( old != m_keepLinesTogether ) doRebuildAll = true;

			old = m_topOfColumn;
			m_topOfColumn = CoViewStyleConstants.isTopOfColumn(attr);
			if ( old != m_topOfColumn ) doRebuildAll = true;

			old = m_lastInColumn;
			m_lastInColumn = CoViewStyleConstants.isLastInColumn(attr);
			if ( old != m_lastInColumn ) doRebuildAll = true;
		}

		{
			CoLineBreakerIF old = m_lineBreaker;
			m_lineBreaker = CoViewStyleConstants.getLineBreaker( attr, (CoStyledDocumentIF) getDocument() );
			if
				(
					( ( old == null ) && ( m_lineBreaker != null ) )
				||
					( ( old != null ) && ( m_lineBreaker == null ) )
				||
					( ( old != null ) && ( m_lineBreaker != null ) && ! old.equals( m_lineBreaker ) )
				)
			{
				invalidateBreakPoints();	
				doRebuildAll = true;
			}
		}

		{
			CoEnumValue old = m_hyphenationFallbackBehavior;
			m_hyphenationFallbackBehavior = CoViewStyleConstants.getHyphenationFallbackBehavior( attr );
			if
				( m_hyphenationFallbackBehavior == null )
			{
				CoHyphenationIF h = ( (CoStyledDocument) getDocument() ).getHyphenation( CoViewStyleConstants.getHyphenation( attr ) );
				if ( h != null ) m_hyphenationFallbackBehavior =  h.getFallbackBehavior();
			}
			
			if      ( m_hyphenationFallbackBehavior == null ) m_hyphenationFallbackBehavior = CoStyleConstants.HYPHENATION_FALLBACK_BEHAVIOR_NO_LINE;
			else if ( m_hyphenationFallbackBehavior.equals( CoStyleConstants.HYPHENATION_FALLBACK_BEHAVIOR_FIRST_BREAKPOINT ) )  m_hyphenationFallbackBehavior = CoStyleConstants.HYPHENATION_FALLBACK_BEHAVIOR_FIRST_BREAKPOINT;
			else if ( m_hyphenationFallbackBehavior.equals( CoStyleConstants.HYPHENATION_FALLBACK_BEHAVIOR_NO_LINE ) )           m_hyphenationFallbackBehavior = CoStyleConstants.HYPHENATION_FALLBACK_BEHAVIOR_NO_LINE;
			else if ( m_hyphenationFallbackBehavior.equals( CoStyleConstants.HYPHENATION_FALLBACK_BEHAVIOR_NON_BREAKPOINT ) )    m_hyphenationFallbackBehavior = CoStyleConstants.HYPHENATION_FALLBACK_BEHAVIOR_NON_BREAKPOINT;
			if
				( old != m_hyphenationFallbackBehavior )
			{
				doRebuildAll = true;
			}
		}

		{
			boolean old = m_adjustToBaseLineGrid;
			m_adjustToBaseLineGrid = CoViewStyleConstants.getAdjustToBaseLineGrid( attr );
			if ( old != m_adjustToBaseLineGrid ) doRebuildAll = true;
		}

		m_tabSet = CoViewStyleConstants.getTabSet( attr );

		m_fallbackTabStop.setAlignment( CoTabStopIF.ALIGN_LEFT ); // PENDING: from attr ?
		m_fallbackTabStop.setPosition( CoViewStyleConstants.getRegularTabStopInterval( attr ) );
		m_fallbackTabStop.setLeader( CoTabStopIF.LEADER_NONE ); // PENDING: from attr ?

		float t = CoViewStyleConstants.getTopRulerThickness( attr );
		m_topRulerStroke.setThickness( t );
		if
			( t > 0 )
		{
			m_topRulerPosition = CoViewStyleConstants.getTopRulerPosition( attr );
			CoEnumValue span = CoViewStyleConstants.getTopRulerSpan( attr );
			if
				( span.equals( CoTextConstants.RULER_SPAN_FIXED ) )
			{
				m_topRulerFixedWidth = CoViewStyleConstants.getTopRulerFixedWidth( attr );
				m_topRulerJustification = CoViewStyleConstants.getTopRulerAlignment( attr );
			} else {
				m_topRulerFixedWidth = -1;
				m_topRulerLeftIndent = CoViewStyleConstants.getTopRulerleftInset( attr );
				m_topRulerRightIndent = CoViewStyleConstants.getTopRulerRightInset( attr );
				m_topRulerIsColumn = span.equals( CoTextConstants.RULER_SPAN_COLUMN );
			}
		}
		
		t = CoViewStyleConstants.getBottomRulerThickness( attr );
		m_bottomRulerStroke.setThickness( t );
		if
			( t > 0 )
		{
			m_bottomRulerPosition = CoViewStyleConstants.getBottomRulerPosition( attr );
			CoEnumValue span = CoViewStyleConstants.getBottomRulerSpan( attr );
			if
				( span.equals( CoTextConstants.RULER_SPAN_FIXED ) )
			{
				m_bottomRulerFixedWidth = CoViewStyleConstants.getBottomRulerFixedWidth( attr );
				m_bottomRulerJustification = CoViewStyleConstants.getBottomRulerAlignment( attr );
			} else {
				m_bottomRulerFixedWidth = -1;
				m_bottomRulerLeftIndent = CoViewStyleConstants.getBottomRulerleftInset( attr );
				m_bottomRulerRightIndent = CoViewStyleConstants.getBottomRulerRightInset( attr );
				m_bottomRulerIsColumn = span.equals( CoTextConstants.RULER_SPAN_COLUMN );
			}
		}
		invalidateRulerData();

		
		if
			( notifyParent && doRebuildAll )
		{
			preferenceChanged( null, true, true );
		}

		m_tagLabel = null;
		
	}
}
void setRowSpacing( double spacing )
{
	m_deltaYrow = spacing;
}
public void setSize( float width, float height )
{
	if
		( width != m_width )
	{
		invalidateAlloc();
		invalidateRulerData();
	}
	
	if
		( height != m_height )
	{
		invalidateAlloc();
		invalidateRulerData();
	}
	
	if
		( ! m_allocValid )
	{
		m_width = width;
		m_height = height;
		layout();
	}
}
/**
   * Dela denna vy i en del som är mindre än en given höjd och en del som utgör resten av den ursprungliga vyn.
   * @param height den tillgängliga höjden.
   * @return den nya vyn som utgör "svans".
   */
CoParagraphView splitAt( float height )
{
	float usedHeight = getTopInset();// + m_extraYspace;

	// traversera raderna
	int I = getViewCount();
	for
		( int i = 0; i < I; i++ )
	{
		CoRowView v = (CoRowView) getView( i );
		/*
		if
			(
				( i == 0 )
			||
				( ! hasDropCaps() )
			||
				( m_dropCapsLineCount < i )
			)
		{
			usedHeight += v.getPreferredSpan( Y_AXIS );
		}
*/
		usedHeight = Math.max( usedHeight, v.getY() - m_y + v.getPreferredSpan( Y_AXIS ) );

		if
			( usedHeight > height )
		{
			// tillåten höjd är överskriden, kapa vyn

			if ( i == 0 ) return null; // första raden får inte plats, delning omöjlig
  
			// skapa svansobjektet
			m_tail = create( getElement(), this );

			// flytta rader, som inte får plats, från detta objekt till svansen
			View tmp[] = new View[ I - i ];
			for
				( int j = i; j < I; j++ )
			{
				Row row = (Row) getView( j );
				tmp[ j - i ] = row;
			}
			m_tail.replace( 0, m_tail.getViewCount(), tmp );
			replace( i, I - i, ZERO_VIEWS );

			// beräkna modellgränser

			if
				( m_head == null )
			{
				m_offset = 0;
			}
			m_length = getView( i - 1 ).getEndOffset() - getView( 0 ).getStartOffset();

			m_tail.m_offset = m_offset + m_length;
			m_tail.m_length = getElement().getEndOffset() - m_tail.m_offset;
			// returnera svansen
			return m_tail;
		}
	}

	// alla rader fick plats, delning onödig
	return this;
}
public String toString()
{
	String id;
	if
		( m_head == null )
	{
		id = "PV # " + m_id;
	} else {
		int i = 1;
		CoParagraphView v = m_head;
		while ( v.m_head != null ) { v = v.m_head; i++; }
		id = "PV # " + v.m_id + "." + i;
	}

	if
		( m_element == null )
	{
		return "CoParagraphView " + " [ " + id + " ]";
	} else {
		return "CoParagraphView " + getStartOffset() + " -> " + getEndOffset() + " [ " + id + " ]";
	}
}
/**
 * Update the logical children to reflect changes made 
 * to the element this view is responsible.  This updates
 * the pool of views used for layout (ie. the views 
 * representing the child elements of the element this
 * view is responsible for).  This is called by the 
 * <code>insertUpdate, removeUpdate, and changeUpdate</code>
 * methods.
 */
void updateLogicalChildren(DocumentEvent.ElementChange ec, ViewFactory f)
{
	int index = ec.getIndex();
	Element[] removedElems = ec.getChildrenRemoved();
	for (int i = 0; i < removedElems.length; i++)
	{
		View v = (View) m_layoutPool.get(index);
		v.setParent(null);
		m_layoutPool.remove(index);
		( (CoReuseableView) v ).release();
	}
	
	Element[] addedElems = ec.getChildrenAdded();
	for (int i = 0; i < addedElems.length; i++)
	{
		View v = f.create(addedElems[i]);
		v.setParent(this);
		m_layoutPool.add(index + i, v );
	}
}
private void updateRows()
{
	updateRows( true );
	
	int i = getViewCount();
	if
		( i == 0 )
	{
		m_length = 0;
	} else {
		m_length = getView( i - 1 ).getEndOffset() - getElement().getStartOffset() - m_offset;
	}
}
private void updateRows( boolean stopAtEndOfColumn )
{
	m_nextBreakpoint = 0;

	if
		( m_adjustToBaseLineGrid && ( m_baseLineSpacing > 0 ) )
	{
		updateRowsWithBaseLineGrid( stopAtEndOfColumn );
	} else {
		updateRowsWithoutBaseLineGrid( stopAtEndOfColumn );
	}
}
private void updateRowsWithBaseLineGrid( boolean stopAtEndOfColumn )
{
	// remove old rows
	removeAndReleaseAll();

	// reparent all layoutpool views (their parents were just discarded)
	int I = m_layoutPool.size();
	for
		( int i = 0; i < I; i++ )
	{
		View v = (View) m_layoutPool.get( i );
		v.setParent( this );
	}


	
	float dy = (float) getColumn().getBounds().getY();
	float y = getY0();
	int rowCount = 0;
	int skippedRowCount = 0;
	m_visibleRowCount = Integer.MAX_VALUE;
	float range[] = new float[ 2 ];
	
	m_column.setMargins( getLeftInset(), getRightInset() );

	// get submodel boundaries
	int p0 		= getStartOffset();
	int p1 		= getElement().getEndOffset();
	boolean forceConsume = m_column.isRectangular() && m_column.getBounds().height == Integer.MAX_VALUE;

	// iterate until submodel is consumed

//	if ( y > 0 ) y -= m_baseLineSpacing;
	
	while
		( p0 < p1 )
	{
		float y0 = 0;
		float y1 = nextY( y + dy ) - dy;
		float y2 = 0;
		float span = 0;
		Row row = null;

		while
			( true )
		{
			// calculate available space
			m_column.getRange( y1, 0, range );
			span = range[ CoColumnGeometryIF.CoColumnIF.WIDTH ];

			// build a row
			int tmp = m_nextBreakpoint;
			row = buildRow( null, rowCount, span, p0, p1, forceConsume );

			// Is m_fallbackRowHeight ever used??? If so, what can be used instead ?
			float rowHeight = 0;
			if
				( p0 >= row.getEndOffset() )
			{
				rowHeight = m_fallbackRowHeight;
			} else {
				rowHeight = row.getPreferredSpan( Y_AXIS );
			}
			
			y0 = y1 - row.getBaseLine();
			y2 = y0 + rowHeight;// + calcLeadingOffset( rowHeight, row.getFontSize() );

			if
				( y0 < y )
			{
				y1 = nextY( y1 + dy ) - dy;
				m_nextBreakpoint = tmp;
				continue;
			}

			// check if bottom of row will fit the available space
			if
				( m_column.isNarrowing( y ) )
			{
				m_column.getRange( y0, 0, range );
				m_column.getMinimalRange( y0, y2, range );
				float span2 = range[ CoColumnGeometryIF.CoColumnIF.WIDTH ];
				if
					( span2 < span )
				{
					// the row does not fit the available space, build a new (shorter) one
					span = span2;
					m_nextBreakpoint = tmp;
					row = buildRow( row, rowCount, span, p0, p1, forceConsume );
				}			
			}
			break;
		}

		
		// stop at end of column
		if
			( stopAtEndOfColumn && ( y2 > m_column.getBounds().height ) )
		{
			break;
		}

		// don't paint rows outside column
		if
			( ( m_visibleRowCount == Integer.MAX_VALUE ) && ( y2 > m_column.getBounds().height ) )
		{
			m_visibleRowCount = getViewCount();
		}

		// save old start of sub model
		int old = p0;
		if ( row.getViewCount() != 0 ) p0 = row.getEndOffset();

		// check if anything was consumed
		if
			( p0 <= old )
		{
			// the row contains no characters
			if
				( m_column.isRectangular() )
			{
				break; // if it doesn't fit here it won't fit anywhere
			} else {
				y = y2; // calculate next y
			}

			// adjust margins to account for drop caps
			if
				( hasDropCaps() )
			{
				if
					( rowCount + skippedRowCount == m_dropCapsLineCount )
				{
					m_column.setMargins( getLeftInset(), getRightInset() );
				}
				if ( rowCount == 1 ) skippedRowCount++;
			}
		} else {
			// the row is not empty
			
			// append the row to the paragraph
			row.setY( y1 - row.getBaseLine() );
			append( row );

			// calculate next y
			if ( ! hasDropCaps() || ( rowCount > 0 ) ) 		y = y2;

			// adjust margins to account for drop caps
			if
				( hasDropCaps() )
			{
				if
					( rowCount == 0 )
				{
					m_column.setMargins( range[ 0 ] + m_dropCapsViewWidth, getRightInset() );

				} else if
					( rowCount + skippedRowCount == m_dropCapsLineCount )
				{
					m_column.setMargins( getLeftInset(), getRightInset() );
				}
			}

			rowCount++;
		}
	}
}
private void updateRowsWithoutBaseLineGrid( boolean stopAtEndOfColumn )
{
	// remove old rows
	removeAndReleaseAll();

	// reparent all layoutpool views (their parents were just discarded)
	int I = m_layoutPool.size();
	for
		( int i = 0; i < I; i++ )
	{
		View v = (View) m_layoutPool.get( i );
		v.setParent( this );
	}


	
	float y = getY0();
	int rowCount = 0;
	int skippedRowCount = 0;

	m_visibleRowCount = Integer.MAX_VALUE;
	float range[] = new float[ 2 ];
	
	m_column.setMargins( getLeftInset(), getRightInset() );

	// get submodel boundaries
	int p0 = getStartOffset();
	int p1 = getElement().getEndOffset();
	boolean forceConsume = m_column.isRectangular() && m_column.getBounds().height == Integer.MAX_VALUE;

	// iterate until submodel is consumed
	while
		( p0 < p1 )
	{
		// calculate available space
		m_column.getRange( y, 0, range );
		float span = range[ CoColumnGeometryIF.CoColumnIF.WIDTH ];

		// build a row
		int tmp = m_nextBreakpoint;
		Row row = buildRow( null, rowCount, span, p0, p1, forceConsume );

		// Is m_fallbackRowHeight ever used??? If so, what can be used instead ?
		float rowHeight = 0;
		if
			( p0 >= row.getEndOffset() )
		{
			rowHeight = m_fallbackRowHeight;
		} else {
			rowHeight = row.getPreferredSpan( Y_AXIS );//calcRowHeight( row.getPreferredSpan( Y_AXIS ) );
		}

		// check if bottom of row will fit the available space
		if
			( m_column.isNarrowing( y ) )
		{
//			m_column.getMinimalRange( y, y + rowHeight - ( Float.isNaN( m_leading ) ? 0 : m_leading ), range );
			m_column.getMinimalRange( y, y + m_leading.calcHeight( rowHeight ), range );
			
			float span2 = range[ CoColumnGeometryIF.CoColumnIF.WIDTH ];
			if
				( span2 < span )
			{
				// the row does not fit the available space, build a new (shorter) one
				span = span2;
				m_nextBreakpoint = tmp;
				row = buildRow( row, rowCount, span, p0, p1, forceConsume );
				if
					( p0 >= row.getEndOffset() )
				{
					rowHeight = m_fallbackRowHeight;
				} else {
					rowHeight = row.getPreferredSpan( Y_AXIS );//calcRowHeight( row.getPreferredSpan( Y_AXIS ) );
				}
			}			
		}

		// stop at end of column
		if
			( stopAtEndOfColumn && ( y + rowHeight > m_column.getBounds().height ) )
		{
			break;
		}

		// don't paint rows outside column
//System.err.println( this + " y = " + y + "    h = " + rowHeight + "    H = " + m_column.getBounds().height );
		if
			( ( m_visibleRowCount == Integer.MAX_VALUE ) && ( y + rowHeight > m_column.getBounds().height ) )
		{
			m_visibleRowCount = getViewCount();
		}

		// save old start of sub model
		int old = p0;
		if ( row.getViewCount() != 0 ) p0 = row.getEndOffset();

		// check if anything was consumed
		if
			( p0 <= old )
		{
			// the row contains no characters
			if
				( m_column.isRectangular() )
			{
				break; // if it doesn't fit here it won't fit anywhere
			} else {
				y += rowHeight; // calculate next y
			}

			// adjust margins to account for drop caps
			if
				( hasDropCaps() )
			{
				if
					( rowCount + skippedRowCount == m_dropCapsLineCount )
				{
					m_column.setMargins( getLeftInset(), getRightInset() );
				}
				if ( rowCount == 1 ) skippedRowCount++;
			}
		} else {
			// the row is not empty

			// append the row to the paragraph
			row.setY( y );
			append( row );

//			if ( m_isOneLiner ) break;

			// calculate next y
			if ( ! hasDropCaps() || ( rowCount > 0 ) ) y += rowHeight;

			// adjust margins to account for drop caps
			if
				( hasDropCaps() )
			{
				if
					( rowCount == 0 )
				{
					m_column.setMargins( range[ 0 ] + m_dropCapsViewWidth, getRightInset() );

				} else if
					( rowCount + skippedRowCount == m_dropCapsLineCount )
				{
					m_column.setMargins( getLeftInset(), getRightInset() );
				}
			}

			rowCount++;
		}
	}
}
private boolean updateSubParagraphs( DocumentEvent changes )
{
	boolean sizeChanged = false;
	int len = changes.getLength();

	if
		( ( len > 1 ) && ( changes.getType() != DocumentEvent.EventType.CHANGE ) )
	{
		sizeChanged = true;
	} else if
		( ( m_head == null ) && ( m_tail == null ) )
	{
		float old = getPreferredSpan( Y_AXIS );
		rebuildRows();
		sizeChanged = ( old != getPreferredSpan( Y_AXIS ) );

	} else {
		CoParagraphView v = this;
		while ( v.m_head != null ) v = v.m_head;

		int deltaOffset = 0;
		while
			( v.m_tail != null )
		{
			int old = v.m_length;
			v.m_offset += deltaOffset;
			v.updateRows();
			deltaOffset += ( v.m_length - old );
			v = v.m_tail;
		}

		v.m_offset += deltaOffset;
		float old = v.getPreferredSpan( Y_AXIS );
		v.updateRows();
		
		if
			(
				( old != v.getPreferredSpan( Y_AXIS ) )
			||
				( v.getEndOffset() != getElement().getEndOffset() )
			)
		{
			sizeChanged = true;
		}
	}

	return sizeChanged;
}

public void fit()
{
	float fontSize = calculateFittingFontSize();
	
	if ( fontSize == 0 ) return;

	MutableAttributeSet a = new CoSimpleAttributeSet();
	CoStyleConstants.setFontSize( a, new Float( (int) fontSize ) );
	
	( (CoStyledDocument) getDocument() ).setParagraphAttributes( getStartOffset(), 0, a, false );
}

	protected float m_minimumSpaceWidth;
	protected float m_optimumSpaceWidth;
	private String m_tagLabel;
	private int m_tagLabelWidth;
	private int m_tagLabelY;
	protected boolean m_useQxpJustification;

private boolean adjustToBreakpoint( Row row, int p, float desiredSpan, float minimumRelativeSpaceWidth )
{
	int p0 = row.getStartOffset();
	int P = p;

	if
		( ! m_useQxpJustification )
	{
		try
		{
			String s = getDocument().getText( p0, p - p0 );

			while
				( ( p > p0 + 1 ) && Character.isWhitespace( s.charAt( p - p0 - 1 ) ) )
			{
				p--;
			}
		}
		catch ( BadLocationException ex )
		{
		}
	}

	
	float span = 0;

	int N = row.getViewCount();
	int n = 0;
	for
		( ; n < N; n++ )
	{
		View v = row.getView( n );
		if 
			( v.getEndOffset() > p ) // 2000-10-09, Dennis: fix for bug # 610, used to be ( v.getEndOffset() >= p )
		{
			span += ( (CoBreakableView) v ).getMinimalPartialSpan( span, v.getStartOffset(), p, minimumRelativeSpaceWidth );
			n++;
			break;
		}
		span += ( (CoBreakableView) v ).getMinimalSpan( span, minimumRelativeSpaceWidth );
	}

	n--;
	p = P;
	
	span += row.getFirstLineIndent();
	
	if ( row.isBrokenAt( p ) ) span += row.getHyphenWidthAt( ( p == 0 ) ? 0 : ( p - 1 ) );
	
	if
		( span <= desiredSpan )
	{
		View v = row.getView( n );
		CoLabelView.Fragment V = (CoLabelView.Fragment) ( (CoBreakableView) v ).createFragment( v.getStartOffset(), p );
		
		View[] va = new View[1];
		va[0] = V;
		row.replace( n, N - n, va );
row.getPreferredSpan( X_AXIS );

		// The views removed from the row now live in the layout pool with a null parent.  These must be reparented.  Note: we could remember 
		// what is being replaced and then reparent exactly those, but its probably faster to just search the layout pool.
		int I = m_layoutPool.size();
		for
			( int i = 0; i < I; i++ )
		{
			v = (View) m_layoutPool.get( i );
			if ( v.getParent() == null ) v.setParent( this );
		}
		return true;
	}

	return false;
}

public float calculateFittingFontSize()
{
	float w = 0;
	int I = m_layoutPool.size();
	for
		( int i = 0; i < I; i++ )
	{
		CoBreakableView r = (CoBreakableView) m_layoutPool.get( i );
		w += r.getMinimalSpan( w, m_optimumSpaceWidth );
	}
	
	if ( w == 0 ) return 0;

	float width = (float) m_column.getBounds().getWidth() - getLeftInset() - getRightInset() - m_firstLineIndent;
	
	float k = width / w;

	float fontSize = CoViewStyleConstants.getFontSize( getElement().getAttributes() );

	fontSize = fontSize * k;

	return fontSize;
}

public static void clearPools()
{
	m_pool = null;
}

public void drawTab( com.bluebrim.base.shared.CoPaintable g, CoGlyphVector gv, float y, float x0, float x1, float tracking )
{
	if ( m_currentTabStop != null ) m_currentTabStop.getRenderer().paint( g, gv, y, x0, x1, tracking );
}

private List getRowPool()
{
	if
		( ( m_rowPool == null ) || ( m_rowPool.get() == null ) )
	{
		m_rowPool = new SoftReference( new ArrayList() );
	}

	return (List) m_rowPool.get();
}

private boolean isJustified()
{
	return ( m_justification == CoTextConstants.ALIGN_JUSTIFIED ) || ( m_justification == CoTextConstants.ALIGN_FORCED );
}

public void paint( com.bluebrim.base.shared.CoPaintable p, Shape a )
{
	Rectangle2D alloc = a.getBounds2D();
	
	if
		( VIEW_BORDER_COLOR != null )
	{
		Color c = p.getColor();
		p.setColor( VIEW_BORDER_COLOR );
		p.draw( alloc );
		p.setColor(c);
	}

	setSize( (float) alloc.getWidth(), (float) alloc.getHeight() );

	double li = getLeftInset();
	double ri = getRightInset();
	double ti = getTopInset();
	double bi = getBottomInset();
	double x = alloc.getX();
	double y = alloc.getY();
	double w = alloc.getWidth();
	double h = alloc.getHeight();

	// paint rows
//	int I = Math.min( getViewCount(), m_visibleRowCount );  // truncate last paragraph
	int I = getViewCount();  // clip last paragraph


	
	for
		( int i = 0; i < I; i++ )
	{
		alloc.setRect( x + li + m_xPos[ i ],
		               y + ti + m_yPos[ i ],
		               m_xSpan[ i ],
		               m_ySpan[ i ] );

		getPaintableView( i ).paint( p, alloc );
	}

	// paint rulers
	if
		( ( m_head == null ) && m_topRulerStroke.getThickness() > 0 )
	{
		if
			( m_topRulerShape.getY1() == -42 )
		{
			prepareTopRuleShape( w, h, li, ri, ti, bi );
		}

		Color c = p.getColor();
		p.setColor( Color.black );
		Stroke s = p.getStroke();
		p.setStroke( m_topRulerStroke );
		p.translate( x, y );
		p.draw( m_topRulerShape );
		p.translate( -x, -y );
		p.setStroke( s );
		p.setColor(c);
	}

	if
		( ( m_tail == null ) && m_bottomRulerStroke.getThickness() > 0 )
	{
		if
			( m_bottomRulerShape.getY1() == -42 )
		{
			prepareBottomRuleShape( w, h, li, ri, ti, bi );
		}

		Color c = p.getColor();
		p.setColor( Color.black );
		Stroke s = p.getStroke();
		p.setStroke( m_bottomRulerStroke );
		p.translate( x, y );
		p.draw( m_bottomRulerShape );
		p.translate( -x, -y );
		p.setStroke( s );
		p.setColor(c);
	}




	// paint paragraph tags in margin
	Graphics2D g = p.getGraphics2D();

	if
		( g != null )
	{
		CoSectionView parent = (CoSectionView) getParent();
		if
			( ( m_head == null ) && parent.showParagraphTags() )
		{
			AttributeSet attr = getElement().getAttributes();

			Font oldFont = g.getFont();
			Font f = ( CoSectionView.TAG_FONT == null ) ? getContainer().getFont() : CoSectionView.TAG_FONT;
			g.setFont( f );
			
			if
				( m_tagLabel == null )
			{
				
				Object tag = attr.getAttribute( CoTextConstants.PARAGRAPH_TAG );
				if ( tag == null ) tag = "";
				m_tagLabel = "<" + tag + ">";
				m_tagLabelWidth = 10 + (int) g.getFontMetrics().getStringBounds( m_tagLabel, g ).getWidth();
			}

			double dy = CoViewStyleConstants.getCoFont( attr ).getLineMetrics().getAscent() + CoViewStyleConstants.getSpaceAbove( attr );

			g.setColor( CoSectionView.TAG_COLOR );

			double s = CoBaseUtilities.getXScale( g.getTransform() );
			Rectangle2D r = a.getBounds();
			x = r.getX();
			y = r.getY() + dy;
			g.translate( x, y );
			g.scale( 1 / s, 1 / s );
			
			g.drawString( m_tagLabel, - m_tagLabelWidth, 0 );
			g.scale( s, s );
			g.translate( -x, -y );
			
			g.setFont( oldFont );
		}
	}

	
}

public void collectRowHeights( List l )
{
	if
		( m_keepLinesTogether )
	{
		l.add( new CoLayoutableTextPortion( m_height, m_topOfColumn, m_lastInColumn ) );
	} else if
		( m_dropCaps )
	{
	} else {
		int I = getViewCount();
		for
			( int i = 0; i < I; i++ )
		{
			CoRowView v = (CoRowView) getView( i );
			l.add(
				new CoLayoutableTextPortion(
					v.m_height + ( ( i == 0 ) ? getTopInset() : 0 ) + ( ( i == I - 1 ) ? getBottomInset() : 0 ),
					( i == 0 ) && m_topOfColumn,
					( i == I - 1 ) && m_lastInColumn
				)
			);
			
		}
	}
}
}