package com.bluebrim.text.shared.swing;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.event.*;
import javax.swing.text.*;

public abstract class CoRowView extends CoCompositeView implements CoPaintableView, com.bluebrim.text.shared.swing.CoHighlightableView
{
	private Element m_element;
	
	protected float m_y;
	protected float m_width;
	protected float m_height;
	/*
	 * Request cache
	 */
	protected boolean m_xValid;
	protected boolean m_yValid;
	protected CoSizeRequirements m_xRequest;
	protected CoSizeRequirements m_yRequest;

	/*
	 * Allocation cache
	 */
	protected boolean m_xAllocValid;
	protected float[] m_xOffsets;
	protected float[] m_xSpans;
	protected boolean m_yAllocValid;
	protected float[] m_yOffsets;
	protected float[] m_ySpans;

	private boolean m_broken;
	
	// debugging flags
	public static Color ROW_VIEW_BORDER_COLOR = null;

/**
 * Constructs a BoxView.
 *
 * @param elem the element this view is responsible for
 * @param axis either View.X_AXIS or View.Y_AXIS
 */
public CoRowView( Element elem )
{
	super( null );

	set( elem );
}
protected CoSizeRequirements calculateXAxisRequirements( CoSizeRequirements r)
{
	// calculate tiled request
	float min = 0;
	float pref = 0;
	float max = 0;
	int n = getViewCount();
	for
		(int i = 0; i < n; i++)
	{
		View v = getView(i);
		min += v.getMinimumSpan(X_AXIS);
		pref += v.getPreferredSpan(X_AXIS);
		max += v.getMaximumSpan(X_AXIS);
	}

	if
		( isBroken() )
	{
		float d = ( (CoBreakableView) getView( getViewCount() - 1 ) ).getHyphenWidth();
		min += d;
		pref += d;
		max += d;
	}
	
	if
		(r == null)
	{
		r = new CoSizeRequirements();
	}
	r.m_alignment = 0.5f;
	r.m_minimum = min;
	r.m_preferred = pref;
	r.m_maximum = max;

	return r;
}
protected CoSizeRequirements calculateYAxisRequirements( CoSizeRequirements r)
{
	float min = 0;
	float pref = 0;
	float max = Integer.MAX_VALUE;
	int n = getViewCount();
	for
		(int i = 0; i < n; i++)
	{
		View v = getView(i);
		min = Math.max( v.getMinimumSpan(Y_AXIS), min);
		pref = Math.max( v.getPreferredSpan(Y_AXIS), pref);
		max = Math.max( v.getMaximumSpan(Y_AXIS), max);
	}
	if
		(r == null)
	{
		r = new CoSizeRequirements();
		r.m_alignment = 0.5f;
	}

	r.m_preferred = pref;
	r.m_minimum = min;
	r.m_maximum = max;

	if
		(pref <= 0 && getViewCount() > 0)
	{
		r.m_alignment = getView(0).getAlignment(Y_AXIS);
	}

	return r;
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
	Element elem = getElement();

	// forward
	Rectangle2D alloc = ((a != null) && isAllocationValid()) ? getInsideAllocation(a) : null;
	double x = 0;
	double y = 0;
	double width = 0;
	double height = 0;
	
	if
		(alloc != null)
	{
		x = alloc.getX();
		y = alloc.getY();
		width = alloc.getWidth();
		height = alloc.getHeight();
	}
	
	int index0 = elem.getElementIndex(e.getOffset());
	int index1 = elem.getElementIndex(e.getOffset() + Math.max(e.getLength() - 1, 0));

	for
		(int i = index0; i <= index1; i++)
	{
		View v = getView(i);
		if (alloc != null)
		{
			alloc.setRect( x + m_xOffsets[ i ],
				             y + m_yOffsets[ i ],
				             m_xSpans[ i ],
				             m_ySpans[ i ] );
			/*
			alloc.x = x + (int) m_xOffsets[i];
			alloc.y = y + (int) m_yOffsets[i];
			alloc.width = (int) m_xSpans[i];
			alloc.height = (int) m_ySpans[i];
			*/
		}
		v.changedUpdate(e, alloc, f);
	}

	// replace children if necessary.
	DocumentEvent.ElementChange ec = e.getChange(elem);
	if
		(ec != null)
	{
		Element[] removedElems = ec.getChildrenRemoved();
		Element[] addedElems = ec.getChildrenAdded();
		View[] added = new View[addedElems.length];
		for
			(int i = 0; i < addedElems.length; i++)
		{
			added[i] = f.create(addedElems[i]);
		}
		replace(ec.getIndex(), removedElems.length, added);
	}
	
	if
		((a != null) && !isAllocationValid())
	{
		// size changed
		Component c = getContainer();
		c.repaint( (int) x, (int) y, (int) width, (int) height);
	}
}
/**
 * Checks the request cache and update if needed.
 */
void checkRequests()
{
	if
		(!m_xValid)
	{
		m_xRequest = calculateXAxisRequirements( m_xRequest );
	}
	
	if
		(!m_yValid)
	{
		m_yRequest = calculateYAxisRequirements( m_yRequest );
	}
	
	m_yValid = true;
	m_xValid = true;
}
/**
 * Allocates a region for a child view.  
 *
 * @param index the index of the child view to
 *   allocate, >= 0 && < getViewCount()
 * @param alloc the allocated region
 */
protected void childAllocation(int index, Rectangle2D alloc)
{
	alloc.setRect( alloc.getX() + m_xOffsets[ index ],
		             alloc.getY() + m_yOffsets[ index ],
		             m_xSpans[ index ],
		             m_ySpans[ index ] );
	/*
	alloc.x += (int) m_xOffsets[index];
	alloc.y += (int) m_yOffsets[index];
	alloc.width = (int) m_xSpans[index];
	alloc.height = (int) m_ySpans[index];
	*/
}
void clearSpacePadding()
{
	int I = getViewCount();
	for
		( int i = 0; i < I; i++ )
	{
		( (CoJustifiableView) getView( i ) ).clearSpacePadding();
	}
	m_xValid = false;
}
public void dump()
{
	System.err.println( "    row " + getStartOffset() + " -> " + getEndOffset() );
	int I = getViewCount();
	for
		( int i = 0; i < I; i++ )
	{
		View v = getView( i );
		if
			( v instanceof CoLabelView )
		{
			( (CoLabelView) v ).dump();
		} else if
			( v instanceof CoLabelView.Fragment )
		{
			( (CoLabelView.Fragment) v ).dump();
		}
	}
}
protected boolean flipEastAndWestAtEnds(int position, Position.Bias bias)
{
	return false;
}
/**
 * Determines the desired alignment for this view along an
 * axis.  This is implemented to give the total alignment
 * needed to position the children with the alignment points
 * lined up along the axis orthoginal to the axis that is
 * being tiled.  The axis being tiled will request to be
 * centered (i.e. 0.5f).
 *
 * @param axis may be either View.X_AXIS or View.Y_AXIS
 * @returns the desired alignment >= 0.0f && <= 1.0f.  This should
 *   be a value between 0.0 and 1.0 where 0 indicates alignment at the
 *   origin and 1.0 indicates alignment to the full span
 *   away from the origin.  An alignment of 0.5 would be the
 *   center of the view.
 * @exception IllegalArgumentException for an invalid axis
 */
public float getAlignment(int axis)
{
	checkRequests();
	switch
		(axis)
	{
		case View.X_AXIS : return m_xRequest.m_alignment;
		case View.Y_AXIS : return m_yRequest.m_alignment;
		default :
			throw new IllegalArgumentException("Invalid axis: " + axis);
	}
}
public AttributeSet getAttributes()
{
	return m_element.getAttributes();
}
public float getBaseLine()
{
	float bl = 0;
	int I = getViewCount();
	for
		( int i = 0; i < I; i++ )
	{
		bl = Math.max( bl, ( (CoBreakableView) getView( i ) ).getAscent() );
	}

	return bl;
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
	int offs = 0;
	int I = getViewCount();
	for 
		(int i = 0; i < I; i++)
	{
		offs = Math.max( offs, getView( i ).getEndOffset() );
	}
	return offs;
}
public short getFirstLineIndent()
{
	return getLeftInset();
}
public float getFontSize()
{
	float fs = 0;
	int I = getViewCount();
	for
		( int i = 0; i < I; i++ )
	{
		fs = Math.max( fs, ( (CoBreakableView) getView( i ) ).getFontSize() );
	}

	return fs;
}
public float getHyphenWidth()
{
	int I = getViewCount();
	if ( I == 0 )	return 0;
	return ( (CoBreakableView) getView( I - 1 ) ).getHyphenWidth();
}
public float getHyphenWidthAt( int i )
{
	i = getViewIndexAtPosition( i );
	if ( i < 0 )	return 0;
	return ( (CoBreakableView) getView( i ) ).getHyphenWidth();
}
/**
 * Determines the maximum span for this view along an
 * axis.
 *
 * @param axis may be either View.X_AXIS or View.Y_AXIS
 * @returns  the span the view would like to be rendered into >= 0.
 *           Typically the view is told to render into the span
 *           that is returned, although there is no guarantee.  
 *           The parent may choose to resize or break the view.
 * @exception IllegalArgumentException for an invalid axis type
 */
public float getMaximumSpan(int axis)
{
	checkRequests();
	switch
		(axis)
	{
		case View.X_AXIS : return ((float) m_xRequest.m_maximum) + getLeftInset() + getRightInset();
		case View.Y_AXIS : return ((float) m_yRequest.m_maximum) + getTopInset() + getBottomInset();
		default : throw new IllegalArgumentException("Invalid axis: " + axis);
	}
}


/**
 * Determines the minimum span for this view along an
 * axis.
 *
 * @param axis may be either View.X_AXIS or View.Y_AXIS
 * @returns  the span the view would like to be rendered into >= 0.
 *           Typically the view is told to render into the span
 *           that is returned, although there is no guarantee.  
 *           The parent may choose to resize or break the view.
 * @exception IllegalArgumentException for an invalid axis type
 */
public float getMinimumSpan(int axis)
{
	checkRequests();
	switch
		(axis)
	{
		case View.X_AXIS : return ((float) m_xRequest.m_minimum) + getLeftInset() + getRightInset();
		case View.Y_AXIS : return ((float) m_yRequest.m_minimum) + getTopInset() + getBottomInset();
		default :
			throw new IllegalArgumentException("Invalid axis: " + axis);
	}
}
/**
 * Determines the preferred span for this view along an
 * axis.
 *
 * @param axis may be either View.X_AXIS or View.Y_AXIS
 * @returns  the span the view would like to be rendered into >= 0.
 *           Typically the view is told to render into the span
 *           that is returned, although there is no guarantee.  
 *           The parent may choose to resize or break the view.
 * @exception IllegalArgumentException for an invalid axis type
 */
public float getPreferredSpan(int axis)
{
	checkRequests();
	switch	
		(axis)
	{
		case View.X_AXIS : return ((float) m_xRequest.m_preferred) + getLeftInset() + getRightInset();
		case View.Y_AXIS : return ((float) m_yRequest.m_preferred) + getTopInset() + getBottomInset();
		default :
			throw new IllegalArgumentException("Invalid axis: " + axis);
	}
}
/**
 * Gets the resize weight.  A value of 0 or less is not resizable.
 *
 * @param axis may be either View.X_AXIS or View.Y_AXIS
 * @return the weight
 * @exception IllegalArgumentException for an invalid axis
 */
public int getResizeWeight(int axis)
{
	checkRequests();
	switch
		(axis)
	{
		case View.X_AXIS :
			if
				((m_xRequest.m_preferred != m_xRequest.m_minimum) && (m_xRequest.m_preferred != m_xRequest.m_maximum))
			{
				return 1;
			}
			return 0;
			
		case View.Y_AXIS :
			if
				((m_yRequest.m_preferred != m_yRequest.m_minimum) && (m_yRequest.m_preferred != m_yRequest.m_maximum))
			{
				return 1;
			}
			return 0;
			
		default :
			throw new IllegalArgumentException("Invalid axis: " + axis);
	}
}
public int getStartOffset()
{
	int offs = Integer.MAX_VALUE;
	int I = getViewCount();
	for
		( int i = 0; i < I; i++ )
	{
		offs = Math.min( offs, getView( i ).getStartOffset() );
	}
	return offs;
}
/**
 * Fetches the child view at the given point.
 *
 * @param x the X coordinate >= 0
 * @param y the Y coordinate >= 0
 * @param alloc the parents inner allocation on entry, which should
 *   be changed to the childs allocation on exit.
 * @return the view
 */
protected View getViewAtPoint(int x, int y, Rectangle2D alloc)
{
	double X = alloc.getX();
	
	int n = getViewCount();

	if (x < (X + m_xOffsets[0]))
	{
		childAllocation(0, alloc);
		return getView(0);
	}
	for (int i = 0; i < n; i++)
	{
		if (x < (X + m_xOffsets[i]))
		{
			childAllocation(i - 1, alloc);
			return getView(i - 1);
		}
	}
	childAllocation(n - 1, alloc);
	return getView(n - 1);
}
protected View getViewAtPosition(int pos, Rectangle2D a)
{
	int n = getViewCount();
	for
		(int i = 0; i < n; i++)
	{
		View v = getView(i);
		int p0 = v.getStartOffset();
		int p1 = v.getEndOffset();
		if
			((pos >= p0) && (pos < p1))
		{
			// it's in this view.
			if
				(a != null)
			{
				childAllocation(i, a);
			}
			return v;
		}
	}
	if
		(pos == getEndOffset())
	{
		// PENDING(bcb): This will probably want to choose the first if right to left.
		View v = getView(n - 1);
		if
			(a != null)
		{
			childAllocation(n - 1, a);
		}
		return v;
	}
	return null;
}
protected int getViewIndexAtPosition(int pos)
{
	// This is expensive, but are views are not necessarily layed out in model order.
	if ( ( pos < getStartOffset() ) || ( pos >= getEndOffset() ) ) return -1;
	
	for
		( int i = getViewCount() - 1; i >= 0; i-- )
	{
		View v = getView( i );
		if
			( ( pos >= v.getStartOffset() ) && ( pos < v.getEndOffset() ) )
		{
			return i;
		}
	}
	return -1;
}
float getY()
{
	return m_y;
}
	/**
	 * Gives notification that something was inserted into the document
	 * in a location that this view is responsible for.
	 *
	 * @param e the change information from the associated document
	 * @param a the current allocation of the view
	 * @param f the factory to use to rebuild if the view has children
	 * @see View#insertUpdate
	 */
	public void insertUpdate(DocumentEvent e, Shape a, ViewFactory f) {
	Element elem = getElement();
	DocumentEvent.ElementChange ec = e.getChange(elem);
	boolean shouldForward = true;
	if (ec != null) {
	    // the structure of this element changed.
	    Element[] removedElems = ec.getChildrenRemoved();
	    Element[] addedElems = ec.getChildrenAdded();
	    View[] added = new View[addedElems.length];
	    for (int i = 0; i < addedElems.length; i++) {
		added[i] = f.create(addedElems[i]);
	    }
	    int index = ec.getIndex();
	    replace(index, removedElems.length, added);
	    if (added.length > 0) {
		int pos = e.getOffset();
		// Check if need to forward to left child
		// NOTE: If we do forward we use null as the shape as we are
		// going to invoke preferenceChanged, which will make our
		// size invalid.
		if (index > 0) {
		    Element child = elem.getElement(index - 1);
		    if (child.getEndOffset() >= pos) {
			View v = getViewAtPosition(child.getStartOffset(),
						   null);
			if (v != null) {
			    v.insertUpdate(e, null, f);
			}
		    }
		}
		// Check if need to forward to right child.
		if ((index + added.length) < getViewCount()) {
		    Element child = elem.getElement(index + added.length);
		    int start = child.getStartOffset();
		    if (start >= pos && start <= (pos + e.getLength())) {
			View v = getViewAtPosition(child.getStartOffset(),
						   null);
			if (v != null) {
			    v.insertUpdate(e, null, f);
			}
		    }
		}
		// No need to forward, we have already done it.
		shouldForward = false;
	    }
	    // should damge a little more intelligently.
	    if (a != null) {
		preferenceChanged(null, true, true);
		getContainer().repaint();
	    }
	}

	// find and forward if there is anything there to 
	// forward to.  If children were removed then there was
	// a replacement of the removal range and there is no
	// need to forward.

	// PENDING(prinz) fixup DocumentEvent to provide more
	// info so forwarding can be properly done.
	if (shouldForward) {
	    Rectangle2D alloc = ((a != null) && isAllocationValid()) ? 
		getInsideAllocation(a) : null;
	    int pos = e.getOffset();
	    View v = getViewAtPosition(pos, alloc);
	    if (v != null) {
		if ((v.getStartOffset() == pos) && (pos > 0)) {
		    // If v is at a boundry, forward the event to the previous
		    // view too.
		    Rectangle2D allocCopy = ((a != null) &&
					   isAllocationValid()) ? 
			getInsideAllocation(a) : null;
		    View previousView = getViewAtPosition(pos - 1, allocCopy);
		    if(previousView != v && previousView != null) {
			previousView.insertUpdate(e, allocCopy, f);
		    }
		}
		v.insertUpdate(e, alloc, f);
	    }
	}
	}
/**
 * Determines if a point falls after an allocated region.
 *
 * @param x the X coordinate >= 0
 * @param y the Y coordinate >= 0
 * @param innerAlloc the allocated region.  This is the area
 *   inside of the insets.
 * @return true if the point lies after the region else false
 */
protected boolean isAfter(int x, int y, Rectangle2D innerAlloc)
{
	return (x > (innerAlloc.getWidth() + innerAlloc.getX()));
}
// --- local methods ----------------------------------------------------

/**
 * Are the allocations for the children still
 * valid?
 *
 * @return true if allocations still valid
 */
protected boolean isAllocationValid()
{
	return (m_xAllocValid && m_yAllocValid);
}
/**
 * Determines if a point falls before an allocated region.
 *
 * @param x the X coordinate >= 0
 * @param y the Y coordinate >= 0
 * @param innerAlloc the allocated region.  This is the area
 *   inside of the insets.
 * @return true if the point lies before the region else false
 */
protected boolean isBefore(int x, int y, Rectangle2D innerAlloc)
{
	return (x < innerAlloc.getX());
}
public boolean isBroken()
{
	return isBrokenAt( getEndOffset() - 1 + 1 );
}
public boolean isBrokenAt( int i )
{
	try
	{
		String s = getDocument().getText( i - 1, 2 );

		char c0 = s.charAt( 0 );

		if ( c0 == '-' ) return false; // PENDING: what other characters behave this way ???
		
		char c1 = s.charAt( 1 );
		
		if
			( ( ! Character.isWhitespace( c0 ) ) && ( ! Character.isWhitespace( c1 ) ) )
		{
			return true;
		}
	}
	catch ( BadLocationException ex )
	{
	}

	return false;
}
/**
 * Performs layout of the children.  The size is the
 * area inside of the insets.  This method calls
 * the methods 
 * <a href="#layoutMajorAxis">layoutMajorAxis</a> and
 * <a href="#layoutMinorAxis">layoutMinorAxis</a> as
 * needed.  To change how layout is done those methods
 * should be reimplemented.
 *
 * @param width the width >= 0
 * @param height the height >= 0
 */
protected void layout( float width, float height)
{
	Document doc = getDocument();
	if
		( doc.getProperty( DefaultStyledDocument.I18NProperty ).equals( Boolean.TRUE ) )
	{
		int n = getViewCount();
		if
			( n > 1 )
		{
			// REMIND (bcb) handle case of not an abstract document.
			DefaultStyledDocument d = (DefaultStyledDocument) getDocument();
			Element bidiRoot = ( (DefaultStyledDocument) getElement().getDocument() ).getBidiRootElement();
			byte[] levels = new byte[ n ];
			View[] reorder = new View[ n ];
			for
				( int i = 0; i < n; i++ )
			{
				View v = getView( i );
				int bidiIndex = bidiRoot.getElementIndex( v.getStartOffset() );
				Element bidiElem = bidiRoot.getElement( bidiIndex );
				levels[ i ] = (byte) StyleConstants.getBidiLevel( bidiElem.getAttributes() );
				reorder[ i ] = v;
			}

			//	    Bidi.reorderVisually( levels, reorder );
			replace( 0, n, reorder );
		}
	}

			
	checkRequests();

	// rebuild the allocation arrays if they've been removed
	// due to a change in child count.
	if
		(m_xSpans == null)
	{
		int n = getViewCount();
		m_xSpans = new float[n];
		m_ySpans = new float[n];
		m_xOffsets = new float[n];
		m_yOffsets = new float[n];
	}

	if
		(!m_xAllocValid)
	{
		layoutXAxis( width );
	}
	if
		(!m_yAllocValid)
	{
		layoutYAxis( height );
	}

	m_xAllocValid = true;
	m_yAllocValid = true;

	// flush changes to the children
	int n = getViewCount();
	for
		(int i = 0; i < n; i++)
	{
		View v = getView(i);
		v.setSize( m_xSpans[i], m_ySpans[i] );
	}
}
/**
 * Perform layout for the major axis of the box (i.e. the
 * axis that it represents).  The results of the layout should
 * be placed in the given arrays which represent the allocations
 * to the children along the major axis.
 *
 * @param targetSpan the total span given to the view, which
 *  whould be used to layout the children.
 * @param axis the axis being layed out.
 * @param offsets the offsets from the origin of the view for
 *  each of the child views.  This is a return value and is
 *  filled in by the implementation of this method.
 * @param spans the span of each child view.  This is a return
 *  value and is filled in by the implementation of this method.
 * @returns the offset and span for each child view in the
 *  offsets and spans parameters.
 */
protected void layoutXAxis( float targetSpan )
{
	float x = 0;
	
	int I = m_xSpans.length;
	for
		( int i = 0; i < I; i++ )
	{
		m_xOffsets[ i ] = x;
		m_xSpans[ i ] = getView( i ).getPreferredSpan( X_AXIS );
		x += m_xSpans[ i ];
	}

	if(!true) return;
	
	int n = getViewCount();
	CoSizeRequirements[] reqs = new CoSizeRequirements[n];
	for
		(int i = 0; i < n; i++)
	{
		View v = getView(i);
		float min = v.getMinimumSpan(X_AXIS);
		float pref = v.getPreferredSpan(X_AXIS);
		float max = v.getMaximumSpan(X_AXIS);
		float a = v.getAlignment(X_AXIS);
		reqs[i] = new CoSizeRequirements(min, pref, max, a);
	}

	float min = 0;
	float pref = 0;
	float max = 0;
	for
		( int i = 0; i < reqs.length; i++ )
	{
	    min += reqs[i].m_minimum;
	    pref += reqs[i].m_preferred;
	    max += reqs[i].m_maximum;
	}
	if
		( targetSpan >= pref )
	{
		// ---- determine what we have to work with ----
		float totalPlay = Math.min( targetSpan - pref, max - pref );
		float factor = ( max - pref == 0 ) ? 0.0f : totalPlay / ( max - pref );

		// ---- make the adjustments ----
		float totalOffset = 0;
		for
			(int i = 0; i < m_xSpans.length; i++)
		{
			m_xOffsets[i] = totalOffset;
			CoSizeRequirements req = reqs[i];
			float play = factor * ( req.m_maximum - req.m_preferred );
			m_xSpans[i] = Math.min( req.m_preferred + play, Integer.MAX_VALUE );
			totalOffset = Math.min( totalOffset + m_xSpans[ i ], Integer.MAX_VALUE );
		}
	} else {
		// ---- determine what we have to work with ----
		float totalPlay = Math.min(pref - targetSpan, pref - min);
		float factor = (pref - min == 0) ? 0.0f : totalPlay / (pref - min);

		// ---- make the adjustments ----
		float totalOffset = 0;
		for
			(int i = 0; i < m_xSpans.length; i++)
		{
			m_xOffsets[i] = totalOffset;
			CoSizeRequirements req = reqs[i];
			float play = factor * ( req.m_preferred - req.m_minimum );
			m_xSpans[i] = req.m_preferred - play;
			totalOffset = Math.min( totalOffset + m_xSpans[i], Integer.MAX_VALUE);
		}
	}
}
/**
 * Perform layout for the minor axis of the box (i.e. the
 * axis orthoginal to the axis that it represents).  The results 
 * of the layout should be placed in the given arrays which represent 
 * the allocations to the children along the minor axis.
 *
 * @param targetSpan the total span given to the view, which
 *  whould be used to layout the children.
 * @param axis the axis being layed out.
 * @param offsets the offsets from the origin of the view for
 *  each of the child views.  This is a return value and is
 *  filled in by the implementation of this method.
 * @param spans the span of each child view.  This is a return
 *  value and is filled in by the implementation of this method.
 * @returns the offset and span for each child view in the
 *  offsets and spans parameters.
 */
protected void layoutYAxis( float targetSpan )
{
	int n = getViewCount();
	for
		(int i = 0; i < n; i++)
	{
		View v = getView(i);
		float min = v.getMinimumSpan(Y_AXIS);
		float max = v.getMaximumSpan(Y_AXIS);
		if
			( max < targetSpan )
		{
			// can't make the child this wide, align it
			float align = v.getAlignment(Y_AXIS);
			m_yOffsets[i] = ( targetSpan - max ) * align;
			m_ySpans[i] = max;
		} else {
			// make it the target width, or as small as it can get.
			m_yOffsets[i] = 0;
			m_ySpans[i] = Math.max(min, targetSpan);
		}
	}
}
protected void loadChildren(ViewFactory f)
{
}
/**
 * Provides a mapping from the document model coordinate space
 * to the coordinate space of the view mapped to it.  This makes
 * sure the allocation is valid before letting the superclass
 * do its thing.
 *
 * @param pos the position to convert >= 0
 * @param a the allocated region to render into
 * @return the bounding box of the given position
 * @exception BadLocationException  if the given position does
 *  not represent a valid location in the associated document
 * @see View#modelToView
 */
public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException
{
	Rectangle2D r = a.getBounds2D();
	View v = getViewAtPosition( pos, r );
	
	if
		( ( v != null ) && ( ! v.getElement().isLeaf() ) )
	{
		// Don't adjust the height if the view represents a branch.
		if
			( ! isAllocationValid() )
		{
			r = a.getBounds();
			setSize( (float) r.getWidth(), (float) r.getHeight() );
		}
		return super.modelToView( pos, a, b );
	}
	
	r = a.getBounds();
	double height = r.getHeight();
	double y = r.getY();

	if
		( ! isAllocationValid() )
	{
		setSize( (float) r.getWidth(), (float) r.getHeight() );
	}
	Shape loc = super.modelToView( pos, a, b );
	r = loc.getBounds2D();
	r.setRect( r.getX(),
						 y,
						 r.getWidth(),
						 height );
	/*
	r.height = height;
	r.y = y;
	*/
	return r;
}

public void paintSelectionShadow( Graphics2D g, int from, int to, Shape allocation )
{
	Rectangle2D alloc = allocation.getBounds2D();

	double x = alloc.getX() + getLeftInset();
	double y = alloc.getY() + getTopInset();
	
	int I = getViewCount();
	for
	  ( int i = 0; i < I; i++ )
	{
		alloc.setRect( x + m_xOffsets[i],
			             y + m_yOffsets[i],
			             m_xSpans[i],
			             m_ySpans[i] );
		/*
	  alloc.x = x + (int) ( 0.5f + m_xOffsets[i] );
	  alloc.y = y + (int) ( 0.5f + m_yOffsets[i] );
	  alloc.width = (int) ( 0.5f + m_xSpans[i] );
	  alloc.height = (int) ( 0.5f + m_ySpans[i] );
	  */
	  com.bluebrim.text.shared.swing.CoHighlightableView v = (com.bluebrim.text.shared.swing.CoHighlightableView) getView( i );
	  v.paintSelectionShadow( g, from, to, alloc );
	}
}
// --- View methods ---------------------------------------------

/**
 * This is called by a child to indicated its 
 * preferred span has changed.  This is implemented to
 * throw away cached layout information so that new
 * calculations will be done the next time the children
 * need an allocation.
 *
 * @param child the child view
 * @param width true if the width preference should change
 * @param height true if the height preference should change
 */
public void preferenceChanged(View child, boolean width, boolean height)
{
	if (width)
	{
		m_xValid = false;
		m_xAllocValid = false;
	}
	if (height)
	{
		m_yValid = false;
		m_yAllocValid = false;
	}
	super.preferenceChanged(child, width, height);
}
protected void removeAndReleaseAll()
{
	// label views are owned by the paragraph view -> can't be released by a row view
	int I = getViewCount();
	for
		( int i = 0; i < I; i++ )
	{
		View v = getView( i );
		if ( v instanceof CoLabelView.Fragment ) ( (CoReuseableView) v ).release();
	}
	
	removeAll();
}
	/**
	 * Gives notification that something was removed from the document
	 * in a location that this view is responsible for.
	 *
	 * @param e the change information from the associated document
	 * @param a the current allocation of the view
	 * @param f the factory to use to rebuild if the view has children
	 * @see View#removeUpdate
	 */
	public void removeUpdate(DocumentEvent e, Shape a, ViewFactory f) {
	Element elem = getElement();
	DocumentEvent.ElementChange ec = e.getChange(elem);
	boolean shouldForward = true;
	if (ec != null) {
	    Element[] removedElems = ec.getChildrenRemoved();
	    Element[] addedElems = ec.getChildrenAdded();
	    View[] added = new View[addedElems.length];
	    for (int i = 0; i < addedElems.length; i++) {
		added[i] = f.create(addedElems[i]);
	    }
	    replace(ec.getIndex(), removedElems.length, added);
	    if (added.length != 0) {
		shouldForward = false;
	    }

	    // should damge a little more intelligently.
	    if (a != null) {
		preferenceChanged(null, true, true);
		getContainer().repaint();
	    }
	}

	// find and forward if there is anything there to 
	// forward to.  If children were added then there was
	// a replacement of the removal range and there is no
	// need to forward.
	if (shouldForward) {
	    Rectangle2D alloc = ((a != null) && isAllocationValid()) ? 
		getInsideAllocation(a) : null;
	    int pos = e.getOffset();
	    View v = getViewAtPosition(pos, alloc);
	    if (v != null) {
		if ((v.getStartOffset() == pos) && (pos > 0)) {
		    // If v is at a boundry, forward the event to the previous
		    // view too.
		    Rectangle2D allocCopy = ((a != null) &&
					   isAllocationValid()) ? 
			                    getInsideAllocation(a) : null;
		    View previousView = getViewAtPosition(pos - 1, allocCopy);
		    if(previousView != v && previousView != null) {
			previousView.removeUpdate(e, allocCopy, f);
		    }
		}
		v.removeUpdate(e, alloc, f);
	    }
	}
	}
/**
 * Invalidates the layout and resizes the cache of requests/allocations.
 *
 * @param offset the starting offset into the child views >= 0
 * @param length the number of existing views to replace >= 0
 * @param elems the child views to insert
 */
public void replace(int offset, int length, View[] elems)
{
	super.replace(offset, length, elems);

	// invalidate cache 
	m_xOffsets = null;
	m_xSpans = null;
	m_xValid = false;
	m_xAllocValid = false;
	m_yOffsets = null;
	m_ySpans = null;
	m_yValid = false;
	m_yAllocValid = false;
}
void reset()
{
//	removeAll();
	setInsets( (short) 0, (short) 0, (short) 0, (short) 0 );
	
	setParent( null );
	
	m_element = null;
	
	m_y = 0;
	m_width = 0;
	m_height = 0;

	m_xValid = false;
	m_yValid = false;
	m_xRequest = null;
	m_yRequest = null;

	m_xAllocValid = false;
	m_xOffsets = null;
	m_xSpans = null;
	m_yAllocValid = false;
	m_yOffsets = null;
	m_ySpans = null;

	m_broken = false;
}
void set( Element elem )
{
	m_element = elem;
}
public void setBroken()
{
	m_broken = isBroken();
}
public void setFirstLineIndent( short fli )
{
	setInsets( (short) 0, fli, (short) 0, (short) 0 );
}
public void setLeading( short l )
{
	setInsets( getTopInset(), getLeftInset(), l, getRightInset() );
}
/**
 * Sets the size of the view.  If the size has changed, layout
 * is redone.  The size is the full size of the view including
 * the inset areas.
 *
 * @param width the width >= 0
 * @param height the height >= 0
 */
public void setSize( float width, float height)
{
	if ( width != m_width )
	{
		m_xAllocValid = false;
	}
	if ( height != m_height )
	{
		m_yAllocValid = false;
	}
	if ((!m_xAllocValid) || (!m_yAllocValid))
	{
		m_width = width;
		m_height = height;
		layout( m_width - getLeftInset() - getRightInset(), m_height - getTopInset() - getBottomInset() );
	}
}

void setY( float y )
{
	m_y = y;
}
public String toString()
{
	return "CoRowView " + getStartOffset() + " -> " + getEndOffset();
}
/**
 * Provides a mapping from the view coordinate space to the logical
 * coordinate space of the model.
 *
 * @param x   x coordinate of the view location to convert >= 0
 * @param y   y coordinate of the view location to convert >= 0
 * @param a the allocated region to render into
 * @return the location within the model that best represents the
 *  given point in the view >= 0
 * @see View#viewToModel
 */
public int viewToModel(float x, float y, Shape a, Position.Bias[] bias)
{
	if
		( ! isAllocationValid() )
	{
		Rectangle2D alloc = a.getBounds();
		setSize( (float) alloc.getWidth(), (float) alloc.getHeight() );
	}
	return super.viewToModel( x, y, a, bias );
}

public float getMinimalPartialSpan( int end, float minimumRelativeSpaceWidth )
{
	float w = 0;
	
	int I = getViewCount();
	CoBreakableView v = null;
	for
		( int i = 0; i < I; i++ )
	{
		v = (CoBreakableView) getView( i );
		if
			( ( (View) v ).getEndOffset() >= end )
		{
			w += v.getMinimalPartialSpan( w, ( (View) v ).getStartOffset(), end, minimumRelativeSpaceWidth );
			break;
		}
		w += v.getMinimalSpan( w, minimumRelativeSpaceWidth );
	}

//	if ( ( ! ignoreHyphen ) && ( v != null ) && isBroken() ) w += v.getHyphenWidth();
  w += getLeftInset() + getRightInset();
  
	return w;
}

public float getMinimalSpan( float x0, float minimumRelativeSpaceWidth )
{
	float w = x0;
	
	int I = getViewCount();
	CoBreakableView v = null;
	for
		( int i = 0; i < I; i++ )
	{
		v = (CoBreakableView) getView( i );
		w += v.getMinimalSpan( w, minimumRelativeSpaceWidth );
	}

//	if ( ( ! ignoreHyphen ) && ( v != null ) && isBroken() ) w += v.getHyphenWidth();
  w += getLeftInset() + getRightInset();
  
	return w - x0;
}

public void paint( com.bluebrim.base.shared.CoPaintable g, Shape allocation )
{
	Rectangle2D alloc = allocation.getBounds2D();
	setSize( (float) alloc.getWidth(), (float) alloc.getHeight() );

	
  	if
		( ROW_VIEW_BORDER_COLOR != null )
  {
		Color c = g.getColor();
		g.setColor( ROW_VIEW_BORDER_COLOR );

		g.draw( alloc );
		
		g.draw( new Line2D.Double( alloc.getX(), alloc.getY(), alloc.getX() + alloc.getWidth(), alloc.getY() + alloc.getHeight() ) );
			          
		g.setColor( c );
  }

	double x = alloc.getX() + getLeftInset();
	double y = alloc.getY() + getTopInset();
	
	int I = getViewCount();
	for
	  ( int i = 0; i < I; i++ )
	{
		alloc.setRect( x + m_xOffsets[i],
			             y + m_yOffsets[i],
	                 m_xSpans[i],
	                 m_ySpans[i] );
		/*
	  alloc.x = x + (int) m_xOffsets[i];
	  alloc.y = y + (int) m_yOffsets[i];
	  alloc.width = (int) m_xSpans[i];
	  alloc.height = (int) m_ySpans[i];
	  */

//		System.err.println( CoParagraphView.this + " row paint " + alloc );
		CoBreakableView v = (CoBreakableView) getView( i );
	  v.paint( g, alloc, m_broken && ( i == I - 1 ) );
	}

//	g.setColor( Color.red );
//	g.drawString( "" + ( (CoJustifiableView) getView(I-1) ).getSpacePadding(), (float)x, (float)y );
}

boolean setSpacePadding( float p, float minimumRelativeSpaceWidth )
{
	int spaceCount = 0;
	int n = 0;
	try
	{
		Segment s = com.bluebrim.text.shared.CoTextUtilities.TMP_Segment;//new Segment();
		getDocument().getText( getStartOffset(), getEndOffset() - getStartOffset(), s );

		n = s.offset + s.count - 1;
		for
			( ; n >= s.offset; n-- )
		{
			if ( ! Character.isWhitespace( s.array[ n ] ) ) break;
		}

		for
			( ; n >= s.offset; n-- )
		{
			if ( s.array[ n ] == '\t' ) break;
			if ( s.array[ n ] == ' ' ) spaceCount++;
		}

		n -= s.offset;
		n += getStartOffset();
		n = ( n < getStartOffset() ) ?  getStartOffset() : n;
	}
	catch ( BadLocationException ex )
	{
	}

	
	if ( spaceCount == 0 ) return false;
	
	float d = p / spaceCount;
	int I = getViewCount();

	for
		( int i = getViewIndexAtPosition( n ); i < I; i++ )
	{
		try
		{
			( (CoJustifiableView) getView( i ) ).setSpacePadding( d, minimumRelativeSpaceWidth );
/*
			float x = ( (CoJustifiableView) getView( i ) ).setSpacePadding( d );
			p -= x;
			*/
		}
		catch ( CoJustifiableView.ToTight e )
		{
			for
				( i--; i >= 0; i-- )
			{
				( (CoJustifiableView) getView( i ) ).clearSpacePadding();
			}
			return false;
		}
	}
	m_xValid = false;
	return true;
}
}