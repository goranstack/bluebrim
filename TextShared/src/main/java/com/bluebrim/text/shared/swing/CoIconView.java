package com.bluebrim.text.shared.swing;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.text.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-04-30 14:50:24)
 * @author: Dennis
 */

public class CoIconView extends View implements CoPaintableView, CoJustifiableView, com.bluebrim.text.shared.swing.CoHighlightableView, CoBreakableView, com.bluebrim.text.shared.swing.CoReuseableView
{
	private Element m_element;

	private Icon m_icon;

	private static boolean TRACE_POOL = false;
	private static boolean TRACE_ROW_POOL = false;
	
 	private static List m_pool;
public CoIconView( Element elem )
{
	super( null );
	
	set( elem );
}

/**
 * clearSpacePadding method comment.
 */
public void clearSpacePadding() {}
public static CoIconView create( Element elem )
{
	CoIconView v = null;
	if
		( getPool().isEmpty() )
	{
		v = new CoIconView( elem );
		if ( TRACE_POOL ) System.err.println( "NEW " + v );
	} else {
		v = (CoIconView) getPool().remove( getPool().size() - 1 );
		v.reset();
		v.set( elem );
		if ( TRACE_POOL ) System.err.println( "REUSE " + v );
	}

	return v;
}
	/**
	 * Creates a new icon view that represents an element.
	 *
	 * @param elem the element to create a view for
	 */
	 /*
	public IconView(Element elem) {
	super(elem);
	AttributeSet attr = elem.getAttributes();
	c = StyleConstants.getIcon(attr);
	}
	*/
	/**
	 * Determines the desired alignment for this view along an
	 * axis.  This is implemented to give the alignment to the
	 * bottom of the icon along the y axis, and the default
	 * along the x axis.
	 *
	 * @param axis may be either View.X_AXIS or View.Y_AXIS
	 * @returns the desired alignment >= 0.0f && <= 1.0f.  This should be
	 *   a value between 0.0 and 1.0 where 0 indicates alignment at the
	 *   origin and 1.0 indicates alignment to the full span
	 *   away from the origin.  An alignment of 0.5 would be the
	 *   center of the view.
	 */
	public float getAlignment(int axis) {
	switch (axis) {
	case View.Y_AXIS:
	    return 1;
	default:
	    return super.getAlignment(axis);
	}
	}
public float getAscent()
{
	return getPreferredSpan( Y_AXIS );
}
public float getDescent()
{
	return 0;
}
public Element getElement()
{
	return m_element;
}
public float getFontSize()
{
	return com.bluebrim.text.impl.shared.CoViewStyleConstants.getDefaultFontSize();
}
public float getHyphenWidth()
{
	return 0;
}
/**
 * getMinimalPartialSpan method comment.
 */
public float getMinimalPartialSpan(float x0, int p0, int p1, float minimumRelativeSpaceWidth) {
	return getPreferredSpan( X_AXIS );
}
/**
 * getMinimalSpan method comment.
 */
public float getMinimalSpan(float x0, float minimumRelativeSpaceWidth) {
	return getPreferredSpan( X_AXIS );
}
private static List getPool()
{
	if ( m_pool == null ) m_pool = new ArrayList();
	return m_pool;
}
	/**
	 * Determines the preferred span for this view along an
	 * axis.
	 *
	 * @param axis may be either View.X_AXIS or View.Y_AXIS
	 * @returns  the span the view would like to be rendered into.
	 *           Typically the view is told to render into the span
	 *           that is returned, although there is no guarantee.
	 *           The parent may choose to resize or break the view.
	 * @exception IllegalArgumentException for an invalid axis
	 */
	public float getPreferredSpan(int axis) {
	switch (axis) {
	case View.X_AXIS:
	    return m_icon.getIconWidth();
	case View.Y_AXIS:
	    return m_icon.getIconHeight();
	default:
	    throw new IllegalArgumentException("Invalid axis: " + axis);
	}
	}
public float getSpacePadding()
{
	return 0;
}
	/**
	 * Provides a mapping from the document model coordinate space
	 * to the coordinate space of the view mapped to it.
	 *
	 * @param pos the position to convert >= 0
	 * @param a the allocated region to render into
	 * @return the bounding box of the given position
	 * @exception BadLocationException  if the given position does not
	 *   represent a valid location in the associated document
	 * @see View#modelToView
	 */
	public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
	int p0 = getStartOffset();
	int p1 = getEndOffset();
	if ((pos >= p0) && (pos <= p1)) {
	    Rectangle r = a.getBounds();
	    if (pos == p1) {
		r.x += r.width;
	    }
	    r.width = 0;
	    return r;
	}
	throw new BadLocationException(pos + " not in range " + p0 + "," + p1, pos);
	}
	// --- View methods ---------------------------------------------

	/**
	 * Paints the icon.
	 * The real paint behavior occurs naturally from the association
	 * that the icon has with its parent container (the same
	 * container hosting this view), so this simply allows us to
	 * position the icon properly relative to the view.  Since
	 * the coordinate system for the view is simply the parent
	 * containers, positioning the child icon is easy.
	 *
	 * @param g the rendering surface to use
	 * @param a the allocated region to render into
	 * @see View#paint
	 */
	public void paint(Graphics g, Shape a) {
	Rectangle alloc = a.getBounds();
	m_icon.paintIcon(getContainer(), g, alloc.x, alloc.y);
	}
/**
 * paint method comment.
 */
public void paint(java.awt.Graphics2D g, java.awt.Shape alloc, boolean broken)
{
	paint( g, alloc );
}
/**
 * paint method comment.
 */
public void paint(com.bluebrim.base.shared.CoPaintable g, java.awt.Shape alloc)
{
	paint( g, alloc, false );
}
/**
 * paint method comment.
 */
public void paint(com.bluebrim.base.shared.CoPaintable g, java.awt.Shape alloc, boolean broken)
{
	if ( g.getGraphics2D() != null ) paint( g.getGraphics2D(), alloc, broken );
}
/**
 * paintSelectionShadow method comment.
 */
public void paintSelectionShadow(java.awt.Graphics2D g, int from, int to, java.awt.Shape allocation) {}
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
	
}
private void reset()
{
	setParent( null );
	
	m_element = null;
	
	m_icon = null;
}
private void set( Element elem )
{
	m_element = elem;

	AttributeSet attr = m_element.getAttributes();
	
	m_icon = StyleConstants.getIcon( attr );
}
	/**
	 * Sets the size of the view.  Since Icon doesn't
	 * support this functionality, there is nothing
	 * we can do.
	 *
	 * @param width the width
	 * @param height the height
	 */
	public void setSize(float width, float height) {
	}
/**
 * setSpacePadding method comment.
 */
public float setSpacePadding(float p, float minimumRelativeSpaceWidth) throws com.bluebrim.text.shared.swing.CoJustifiableView.ToTight {
	return 0;
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
	public int viewToModel(float x, float y, Shape a, Position.Bias[] bias) {
	Rectangle alloc = (Rectangle) a;
	if (x < alloc.x + (alloc.width / 2)) {
	    bias[0] = Position.Bias.Forward;
	    return getStartOffset();
	}
	bias[0] = Position.Bias.Backward;
	return getEndOffset();
	}
}
