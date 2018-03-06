package com.bluebrim.text.shared.swing;
import java.awt.*;
import java.awt.geom.*;
import java.lang.ref.*;
import java.util.*;
import java.util.List;

import javax.swing.event.*;
import javax.swing.text.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.base.shared.debug.*;
import com.bluebrim.text.shared.*;

/**
 * Text view used to display a document.
 * 
 * @author: Dennis Malmström
 */

public class CoSectionView extends CoCompositeView implements CoHighlightableView, CoReuseableView
{
	private static boolean TRACE_POOL = false;



	
	private boolean m_isDirty;
	private Element m_element;

	// yttre dimensioner
  private float m_width;
  private float m_height;

  // kolumngeometridefinition (hämtas från JColumnTextPane)
  private CoColumnGeometryIF m_columnGeometry = null;
  
  private CoBaseLineGeometryIF m_baseLineGeometry = null;
  
	private String m_firstBaselineType; // not used
	private float m_firstBaselineOffset = Float.NaN;
	private String m_verticalAlignmentType;
	private float m_verticalAlignmentMaxInter = Float.NaN;

	private String m_dummyText;

	float m_tabBase;
  // övriga utseende-attribut (hämtas från JColumnTextPane)
  protected Color m_columnBorderColor = null;
  private float m_scale = 1.0f;
  private boolean m_doPaintDummyText;

  // kolumngeometricache (härleds ur kolumngeometridefinitionen)
  protected boolean m_allColumnsOfSameWidth;

  // flagga som indikerar ifall texten får plats eller ej
  protected boolean m_overflow = false;
  protected int m_overflowPosition;
  
  // flagga som styr ifall kolumnhöjden ska ökas när texten ej får plats.
//  protected boolean m_expandOnOverflow = false;
  
  // geometri för sub-View-objekten
  protected float[] m_childLocationX;
  protected float[] m_childLocationY;
  protected float[] m_childWidth;
  protected float[] m_childHeight;
  protected boolean m_childGeometryValid;

  // hjälpstrukturer som används vid anrop till View.replace
  private static final View[] ZERO_VIEWS = new View[ 0 ];
  private static final View[] ONE_VIEW = new View[ 1 ];

  public static final Color DUMMY_TEXT_COLOR = Color.gray;
  public static final Font DUMMY_TEXT_FONT = new Font( "Dialog", Font.PLAIN, 8 );



 	private static SoftReference m_pool;
//private static List m_all = new ArrayList();


protected CoSectionView( Element elem)
{
	super( null );

	set( elem );
}
  /**
   * Överriden metod från javax.swing.text.View.
   * Anropas då textattribut ändras.
   * Denna kod är till stor del kopierad from BoxView varför de ursprungliga kommentarerna på engelska lämnats kvar.
   * @see javax.swing.text.View.changedUpdate.
   * @see BoxView.changedUpdate.
   */
public void changedUpdate( DocumentEvent e, Shape a, ViewFactory f )
{
	m_isDirty = true;
	
  Element elem = getElement();

  // forward
  Rectangle2D alloc = ( ( a != null ) && m_childGeometryValid ) ? getInsideAllocation(a) : null;
  double x = 0;
  double y = 0;
  double width = 0;
  double height = 0;
  if
		( alloc != null )
  {
		x = alloc.getX();
		y = alloc.getY();
		width = alloc.getWidth();
		height = alloc.getHeight();
  }
  int index0 = elem.getElementIndex( e.getOffset() );
  int index1 = elem.getElementIndex( e.getOffset() + Math.max( e.getLength() - 1, 0 ) );

  // kompensera för delade sub-view'er
  for
		( int i = 0; i < index0; i++ )
  {
		if ( ( (CoParagraphView) getView( i ) ).m_tail != null ) index0++;
  }
  for
		( int i = 0; i <= index1; i++ )
  {
		if ( ( (CoParagraphView) getView( i ) ).m_tail != null ) index1++;
  }
	
  for
		( int i = index0; i <= index1; i++ )
  {
		View v = getView( i );
		if
		  ( alloc != null )
		{
			alloc.setRect( x + m_childLocationX[ i ],
				             y + m_childLocationY[ i ],
				             m_childWidth[ i ],
				             m_childHeight[ i ] );
			/*
		  alloc.x = x + (int) ( 0.5f + m_childLocationX[ i ] );
		  alloc.y = y + (int) ( 0.5f + m_childLocationY[ i ] );
		  alloc.width = (int) ( 0.5f + m_childWidth[ i ] );
		  alloc.height = (int) ( 0.5f + m_childHeight[ i ] );
		  */
		}
		v.changedUpdate( e, alloc, f );
  }

  // replace children if necessary.
  DocumentEvent.ElementChange ec = e.getChange( elem );
  if
		( ec != null )
  {
		Element[] removedElems = ec.getChildrenRemoved();
		Element[] addedElems = ec.getChildrenAdded();
		View[] added = new View[ addedElems.length ];
		for
		  ( int i = 0; i < addedElems.length; i++ )
		{
		  added[ i ] = f.create( addedElems[ i ] );
		}
		replace( ec.getIndex(), removedElems.length, added );
  }

  if
		( ( a != null ) && m_childGeometryValid )
  {
		// size changed
		Component c = getContainer();
		c.repaint( (int) x, (int) y, (int) width, (int) height );
  }
}
/**
   * Implementation av javax.swing.text.CompositeView.
   * @see javax.swing.text.CompositeView.childAllocation.
   */
protected void childAllocation( int index, Rectangle2D alloc )
{
	alloc.setRect( alloc.getX() + m_childLocationX[ index ],
		             alloc.getY() + m_childLocationY[ index ],
		             m_childWidth[ index ],
		             m_childHeight[ index ] );
		
	/*
	alloc.x += (int) (0.5f + m_childLocationX[index]);
	alloc.y += (int) (0.5f + m_childLocationY[index]);
	alloc.width = (int) (0.5f + m_childWidth[index]);
	alloc.height = (int) (0.5f + m_childHeight[index]);
	*/
}
public static CoSectionView create( Element elem )
{
	List pool = getPool();
	
	CoSectionView v = null;
	if
		( pool.isEmpty() )
	{
		v = new CoSectionView( elem );
//		m_all.add( v );
		if ( TRACE_POOL ) System.err.println( "NEW " + v );
	} else {
		v = (CoSectionView) pool.remove( pool.size() - 1 );
		v.reset();
		v.set( elem );
		if ( TRACE_POOL ) System.err.println( "REUSE " + v );
	}
	
	return v;
}
public boolean doesOverflow()
{
	return m_overflow;
}

public void dump()
{
	System.err.println( "-------------\nsection " + getStartOffset() + " -> " + getEndOffset() );
	int I = getViewCount();
	for ( int i = 0; i < I; i++ ) ( (CoParagraphView) getView( i ) ).dump();
}
public void dumpLayoutPools()
{
	System.err.println( "-------------\nsection " + getStartOffset() + " -> " + getEndOffset() );
	int I = getViewCount();
	for ( int i = 0; i < I; i++ ) ( (CoParagraphView) getView( i ) ).dumpLayoutPool();
}
public static void dumpPools()
{
	/*
	m_all.removeAll( m_pool );

	System.err.println( "Leaked " + m_all.size() + " section views" );
	if ( ! m_all.isEmpty() ) System.err.println( m_all );
	*/
}
private void fixVerticalAlignment()
{
  if ( ! m_columnGeometry.isRectangular() ) return;
  if ( m_verticalAlignmentType == null ) return;

  if
  	( m_verticalAlignmentType.equals( CoTextGeometryIF.ALIGN_TOP ) )
  {
  } else if
  	( m_verticalAlignmentType.equals( CoTextGeometryIF.ALIGN_BOTTOM ) )
  {
	  fixVerticalBottomAlignment();
  } else if
		( m_verticalAlignmentType.equals( CoTextGeometryIF.ALIGN_CENTERED ) )
	{
	  fixVerticalCenteredAlignment();
	} else if
		( m_verticalAlignmentType.equals( CoTextGeometryIF.ALIGN_JUSTIFIED ) )
	{
	  fixVerticalJustifiedAlignment();
	}

}
private void fixVerticalBottomAlignment()
{
	int I = m_columnGeometry.getColumnCount() - ( m_overflow ? 1 : 0 );
	int N = getViewCount();
	int i = 0;
	int n = 0;
	int n0 = n;
	for
		( ; ( i < I ) && ( n < N ); i++ )
	{
		CoColumnGeometryIF.CoColumnIF c = m_columnGeometry.getColumn( i );
		CoParagraphView v = null;
		while
			( n < N )
		{
			v = (CoParagraphView) getView( n );
			if ( v.getColumn() != c ) break;
			n++;
		}
		double H = c.getBounds().getY() + c.getBounds().getHeight();
		
		double dY = ( H - ( m_childLocationY[ n - 1 ] + m_childHeight[ n - 1 ] ) );
		
	  for
	  	( int m = n - 1; m >= n0; m-- )
	  {
		  m_childLocationY[ m ] += dY;
	  }

		n0 = n;
	}
}
private void fixVerticalCenteredAlignment()
{
	int I = m_columnGeometry.getColumnCount() - ( m_overflow ? 1 : 0 );
	int N = getViewCount();
	int i = 0;
	int n = 0;
	int n0 = n;
	for
		( ; ( i < I ) && ( n < N ); i++ )
	{
		CoColumnGeometryIF.CoColumnIF c = m_columnGeometry.getColumn( i );
		CoParagraphView v = null;
		while
			( n < N )
		{
			v = (CoParagraphView) getView( n );
			if ( v.getColumn() != c ) break;
			n++;
		}
		double H = c.getBounds().getY() + c.getBounds().getHeight();
		
		double dY = ( H - ( m_childLocationY[ n - 1 ] + m_childHeight[ n - 1 ] ) ) / 2.0;
		
	  for
	  	( int m = n - 1; m >= n0; m-- )
	  {
		  m_childLocationY[ m ] += dY;
	  }

		n0 = n;
	}
}
private void fixVerticalJustifiedAlignment()
{
  float dhpMax = Float.MAX_VALUE;
	if ( ! Float.isNaN( m_verticalAlignmentMaxInter ) ) dhpMax = m_verticalAlignmentMaxInter;

	int I = m_columnGeometry.getColumnCount() - ( m_overflow ? 1 : 0 );
	int N = getViewCount();
	int i = 0;
	int n = 0;
	int n0 = n;
	for
		( ; ( i < I ) && ( n < N ); i++ )
	{
		CoColumnGeometryIF.CoColumnIF c = m_columnGeometry.getColumn( i );
		CoParagraphView v = null;
		int rowCount = 0;
		
		while
			( n < N )
		{
			v = (CoParagraphView) getView( n );
			if ( v.getColumn() != c ) break;
			rowCount += v.getViewCount();
			n++;
		}
		double H = c.getBounds().getY() + c.getBounds().getHeight();
		double h = m_childLocationY[ n - 1 ] + m_childHeight[ n - 1 ];
		double dh = H - h;
		double dhp = Math.min( dh / ( n - 1 - n0 ), dhpMax );
		double dhr = ( dh - dhp * ( n - 1 - n0 ) ) / ( rowCount - 1 );

		double dy = 0;
	  for
	  	( int m = n0; m <= n - 1; m++ )
	  {
			v = (CoParagraphView) getView( m );
		  m_childLocationY[ m ] += dy;
		  dh = dhr * v.getViewCount();
		  m_childHeight[ m ] += dh;
		  v.setRowSpacing( dhr );
		  dy += dh + dhp;
	  }

		n0 = n;
	}
}
public AttributeSet getAttributes()
{
	return m_element.getAttributes();
}
protected Color getBackgroundColor()
{
	return getContainer().getBackground();
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
 * Hämta kolumngeometri mm från JColumnTextPane.
 */
private void getGeometry()
{
	Container c = getContainer();

  if
  	( ( c != null ) && ( c instanceof CoViewGeometryProviderIF ) )
  {
	  CoViewGeometryProviderIF gp = (CoViewGeometryProviderIF) c;
		boolean dirty = gp.isColumnGeometryDirty();
//		System.err.println( "getGeometry: dirty = " + dirty + ", valid = " + m_childGeometryValid + " invalidated" );
//		System.err.println( gp.isColumnGeometryDirty() );
		if
			( dirty || ( m_columnGeometry == null ) )
		{
			setColumnGeometry( gp.getColumnGeometry() );
			setBaselineGeometry( gp.getBaseLineGeometry() );
			setTextGeometry( gp.getTextGeometry() );
			
			m_columnBorderColor = gp.getColumnBorderColor();
			m_doPaintDummyText = ! gp.doesHaveFocus();

			setDummyText( gp.getDummyText() );
			
			resetChildGeometry();
		}
  } else {
		m_doPaintDummyText = true;
	}
}
public int getOverflowPosition()
{
	return m_overflowPosition;
}
public float getPreferredHeight()
{
  if
		( ! m_childGeometryValid )
  {
		layout( m_width, m_height );
	}

  float h = 0;
	
	if
		( m_childLocationY != null )
	{
		for
			( int i = 0; i < m_childLocationY.length; i++ )
		{
			h = Math.max( h, m_childLocationY[ i ] + m_childHeight[ i ] );
		}
	}
	
	return h;	
}
  /**
   * Implementation av javax.swing.text.View.
   * @see javax.swing.text.View.getPreferredSpan.
   */
public float getPreferredSpan( int axis )
{
  switch
		( axis )
  {
		case View.X_AXIS:
		  getGeometry();
			float w = 0;
			int I = m_columnGeometry.getColumnCount();
			for
			  ( int i = 0; i < I; i++ )
			{
				CoRectangle2DFloat r = m_columnGeometry.getColumn( i ).getBounds();
			  w = Math.max( w, r.x + r.width );
			}
			return w;

		case View.Y_AXIS:
		  getGeometry();
			float h = 0;
			I = m_columnGeometry.getColumnCount();
			for
			  ( int i = 0; i < I; i++ )
			{
				CoRectangle2DFloat r = m_columnGeometry.getColumn( i ).getBounds();
			  h = Math.max( h, r.y + r.height );
			}
			return h;

		default:
		  throw new IllegalArgumentException( "Invalid axis: " + axis );
  }
}
public int getStartOffset()
{
	return m_element.getStartOffset();
}
  /**
   * Implementation av javax.swing.text.CompositeView.
   * @see javax.swing.text.CompositeView.getViewAtPoint.
   */
protected View getViewAtPoint( int x, int y, Rectangle2D alloc )
{
	double X = alloc.getX();
	double Y = alloc.getY();
	double W = alloc.getWidth();
	double H = alloc.getHeight();
	
  int I = getViewCount();
  for
		( int i = 0; i < I; i++ )
  {
		if ( x < ( X + m_childLocationX[ i ] ) ) continue;
		if ( y < ( Y + m_childLocationY[ i ] ) ) continue;
		if ( y > ( Y + m_childLocationY[ i ] + m_childHeight[ i ] ) ) continue;
		if ( x > ( X + m_childLocationX[ i ] + m_childWidth[ i ] ) ) continue;
		childAllocation( i, alloc );
		return getView( i );
  }

  int best = -1;
  double D = Double.MAX_VALUE;
  
  for
		( int i = 0; i < I; i++ )
  {
		if ( x < ( X + m_childLocationX[ i ] ) ) continue;
		if ( x > ( X + m_childLocationX[ i ] + m_childWidth[ i ] ) ) continue;
		double d;
		if
			( y < ( Y + m_childLocationY[ i ] ) )
		{
			d = Y + m_childLocationY[ i ] - y;
		} else {
			d = y - ( Y + m_childLocationY[ i ] + m_childHeight[ i ] );
		}

		if
			( d < D )
		{
			D = d;
			best = i;
		}
  }

  if
  	( best != -1 )
  {
		childAllocation( best, alloc );
		return getView( best );
  }
  
  for
		( int i = 0; i < I; i++ )
  {
		if ( y < ( Y + m_childLocationY[ i ] ) ) continue;
		if ( y > ( Y + m_childLocationY[ i ] + m_childHeight[ i ] ) ) continue;
		double d;
		if
			( x < ( X + m_childLocationX[ i ] ) )
		{
			d = X + m_childLocationX[ i ] - x;
		} else {
			d = x - ( X + m_childLocationX[ i ] + m_childWidth[ i ] );
		}

		if
			( d < D )
		{
			D = d;
			best = i;
		}
  }

  if
  	( best != -1 )
  {
		childAllocation( best, alloc );
		return getView( best );
  }
  
  return null;
  /*
	float d = Float.MAX_VALUE;


  
  if
		( y < ( alloc.y + m_childLocationY[ 0 ] ) )
  {
		childAllocation( 0, alloc );
		return getView( 0 );
  }


  childAllocation( I - 1, alloc );
  return getView( I - 1 );
  */
}
/**
   * Implementation av javax.swing.text.CompositeView.
   * @see javax.swing.text.CompositeView.getViewAtPosition.
   */
protected View getViewAtPosition(int pos, Rectangle2D a)
{
	int I = getViewCount();
	for
		(int i = 0; i < I; i++)
	{
		View v = getView(i);
		int p0 = v.getStartOffset();
		int p1 = v.getEndOffset();
		if
			((pos >= p0) && (pos < p1))
		{
			if (a != null) childAllocation(i, a);
			return v;
		}
	}
	return null;
}
protected int getViewIndexAtPosition(int pos)
{
	// This is expensive, but are views are not necessarily layed
	// out in model order.
	if (pos < getStartOffset() || pos >= getEndOffset())
		return -1;
	for (int counter = getViewCount() - 1; counter >= 0; counter--)
	{
		View v = getView(counter);
		if (pos >= v.getStartOffset() && pos < v.getEndOffset())
		{
			return counter;
		}
	}
	return -1;
}
  /**
   * Används av se.corren.calvin.swingextras.ColumnTextPaneCaret.DefaultHighlightPainter.
   * @see se.corren.calvin.swingextras.ColumnTextPaneCaret.DefaultHighlightPainter.paint.
   * @param pos textindex.
   * @return index för den sub-View som innehåller ett givet textindex.
   */
  public int getViewIndexOfPos( int pos )
	{
	  int I = getViewCount();
	  for
		( int i = 0; i < I; i++ )
	  {
		View v = getView( i );
		int p0 = v.getStartOffset();
		int p1 = v.getEndOffset();
		if
		  ( ( pos >= p0 ) && ( pos < p1 ) )
		{
		  return i;
		}
	  }
	  return -1;
	}
/**
   * Överriden metod från javax.swing.text.View.
   * Anropas då ny text läggs in.
   * Denna kod är till stor del kopierad from BoxView varför de ursprungliga kommentarerna på engelska lämnats kvar.
   * @see javax.swing.text.View.insertUpdate.
   * @see BoxView.insertUpdate.
   */
public void insertUpdate(DocumentEvent e, Shape a, ViewFactory f)
{
	m_isDirty = true;
	
	Element elem = getElement();
	DocumentEvent.ElementChange ec = e.getChange(elem);
	boolean shouldForward = true;
	
	if
		(ec != null)
	{
		// the structure of this element changed.
		Element[] removedElems = ec.getChildrenRemoved();
		Element[] addedElems = ec.getChildrenAdded();
		View[] added = new View[addedElems.length];
		
		for
			(int i = 0; i < addedElems.length; i++)
		{
			added[i] = f.create(addedElems[i]);
		}
		int index = ec.getIndex();
		int I = ec.getIndex();
		int L = removedElems.length;

		// kompensera för delade sub-view'er
		for
			(int i = 0; i < I; i++)
		{
			if (((CoParagraphView) getView(i)).m_tail != null)
				I++;
		}
		for
			(int i = I; i < I + L; i++)
		{
			if (((CoParagraphView) getView(i)).m_tail != null)
				L++;
		}
		replace(I, L, added);
		
		if
			(added.length > 0)
		{
			int pos = e.getOffset();
			// Check if need to forward to left child
			// NOTE: If we do forward we use null as the shape as we are
			// going to invoke preferenceChanged, which will make our
			// size invalid.
			if
				(index > 0)
			{
				Element child = elem.getElement(index - 1);
				if
					(child.getEndOffset() >= pos)
				{
					View v = getViewAtPosition(child.getStartOffset(), null);
					if
						(v != null)
					{
						v.insertUpdate(e, null, f);
					}
				}
			}
			// Check if need to forward to right child.
			if
				((index + added.length) < getViewCount())
			{
				Element child = elem.getElement(index + added.length);

				if
					( child != null )
				{
					int start = child.getStartOffset();
					if
						(start >= pos && start <= (pos + e.getLength()))
					{
						View v = getViewAtPosition(child.getStartOffset(), null);
						if
							(v != null)
						{
							v.insertUpdate(e, null, f);
						}
					}
				}
			}
			// No need to forward, we have already done it.
			shouldForward = false;
		}
		// should damge a little more intelligently.
		if
			(a != null)
		{
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
	if
		(shouldForward)
	{
//		Rectangle2D alloc = getInsideAllocation(a);
		Rectangle2D alloc = ( ( a != null ) && m_childGeometryValid ) ? getInsideAllocation( a ) : null;
		int pos = e.getOffset();
		View v = getViewAtPosition(pos, alloc);
		if
			(v != null)
		{
			int tmp = v.getStartOffset();
			if
				((tmp == pos) && (pos > 0))
			{
				// If v is at a boundry, forward the event to the previous view too.
		//		Rectangle2D allocCopy =  getInsideAllocation(a);
				Rectangle2D allocCopy = ( ( a != null ) && m_childGeometryValid ) ? getInsideAllocation( a ) : null;
				View previousView = getViewAtPosition(pos - 1, allocCopy);
				if
					(previousView != v && previousView != null)
				{
					previousView.insertUpdate(e, allocCopy, f);
				}
			}
			v.insertUpdate(e, alloc, f);
		}
	}

	getContainer().repaint();  //vojnevojne
}
private void invalidateGeometry()
{
	m_childGeometryValid = false;
}
/**
   * Implementation av javax.swing.text.CompositeView.
   * @see javax.swing.text.CompositeView.isAfter.
   */
protected boolean isAfter(int x, int y, Rectangle2D innerAlloc)
{
	if (x > innerAlloc.getX() + innerAlloc.getWidth())
		return true;
	if (x < innerAlloc.getX())
		return false;
	return (y > (innerAlloc.getHeight() + innerAlloc.getY()));
}
/**
   * Implementation av javax.swing.text.CompositeView.
   * @see javax.swing.text.CompositeView.isBefore.
   */
protected boolean isBefore(int x, int y, Rectangle2D innerAlloc)
{
	if (x < innerAlloc.getX())
		return true;
	if (x > innerAlloc.getX() + innerAlloc.getWidth())
		return false;
	return (y < innerAlloc.getY());
}
public boolean isDirty()
{
	return m_isDirty;
}
/**
 * Gör ny sub-View-layout.
 * @param width  tillgänglig bredd.
 * @param height tillgänglig höjd.
 */
protected void layout( double width, double height )
{
	final boolean DO_TRACE = false;

	if ( DO_TRACE ) System.err.println( "section layout" );


	
  // Beräkna kolumngeometri och placera sub-View'erna
  updateChildGeometry();
  m_childGeometryValid = true;

  // Gör ny layout i sub-View'erna
  int I = getViewCount();
  for
		( int i = 0; i < I; i++ )
  {
	  CoParagraphView v = (CoParagraphView) getView( i );
	  
		v.setSize( (float) m_childWidth[ i ], (float) m_childHeight[ i ] );

		if ( ( m_verticalAlignmentType != null ) && m_verticalAlignmentType.equals( CoTextGeometryIF.ALIGN_JUSTIFIED ) ) v.applyRowSpacing();
	}
  
	if ( DO_TRACE ) System.err.println( "section layout <<" );
}
/**
 * Gör ny sub-View-layout.
 * @param width  tillgänglig bredd.
 * @param height tillgänglig höjd.
 */
protected void layout( int width, int height )
{
	layout( (double) width, (double) height );
}
/**
   * Implementation av javax.swing.text.View.
   * @see javax.swing.text.View.modelToView.
   */
public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException
{
	// uppdatera sub-View-geometrin
	if
		(!m_childGeometryValid)
	{
		Rectangle2D alloc = a.getBounds2D();
		setSize( (float) alloc.getWidth(), (float) alloc.getHeight());
	}

	// anropa superklassen
	Shape s = super.modelToView(pos, a, b);

	// skala resultatet
	if
		( m_scale == 1.0f )
	{
		return s;
	} else {
		Rectangle2D r = s.getBounds2D();

		r.setRect( r.getX() * m_scale,
			         r.getY() * m_scale,
			         r.getWidth() * m_scale,
			         r.getHeight() * m_scale );
		/*
		r.x = (int) (r.x * m_scale + 0.5f);
		r.y = (int) (r.y * m_scale + 0.5f);
		r.width = (int) (r.width * m_scale + 0.5f);
		r.height = (int) (r.height * m_scale + 0.5f);
		*/
		
		return r;
	}
}
public final void paint( Graphics2D g, Shape allocation )
{
	CoPaintable p = (CoPaintable) g.getRenderingHint( CoScreenPaintable.PAINTABLE_KEY );

	if
		( p != null )
	{
		paint( p, allocation );
	} else {
		CoScreenPaintable sp = CoScreenPaintable.wrap( g );
		paint( sp, allocation );
		sp.releaseDelegate();
	}
}
/**
 * Implementation av javax.swing.text.View.
 * @see javax.swing.text.View.paint.
 */
public void paintSelectionShadow( Graphics2D g, int from, int to, Shape allocation )
{
  Rectangle2D alloc = new Rectangle2D.Double();
  alloc.setRect( allocation.getBounds2D() );

  double x = alloc.getX();
  double y = alloc.getY();

  from = Math.max( from, getStartOffset() );
  to = Math.min( to, getEndOffset() - 1 );
  
  int i0 = getViewIndexAtPosition( from );
	if ( i0 == -1 ) return;
	
	int i1 = getViewIndexAtPosition( to );
	if ( i1 == -1 ) return;

  // sub-View'er
  for
		( int i = i0; i <= i1; i++ )
  {
		if ( ( m_childLocationX[ i ] == m_width ) && ( m_childLocationY[ i ] == m_height ) ) continue; // overflow

		alloc.setRect( x + m_childLocationX[ i ],
			             y + m_childLocationY[ i ],
			             m_childWidth[ i ],
			             m_childHeight[ i ] );
		/*
		alloc.x = x + (int) ( 0.5f + m_childLocationX[ i ] );
		alloc.y = y + (int) ( 0.5f + m_childLocationY[ i ] );
		alloc.width = (int) ( 0.5f + m_childWidth[ i ] );
		alloc.height = (int) ( 0.5f + m_childHeight[ i ] );
		*/
		CoHighlightableView v = (CoHighlightableView) getView( i );
		v.paintSelectionShadow( g, from, to, alloc );
  }
}
  /**
   * Fånga anropen till javax.swing.text.View.preferenceChanged.
   * Kontrollera sub-View-geometrins giltighet och fortsätt sedan med det ursprungliga anropet.
   * @see javax.swing.text.View.preferenceChanged.
   */
  public void preferenceChanged( View sourceChild, boolean width, boolean height )   // overridden from View
	{
	  if
	  	( width || height )
	  {
		  invalidateGeometry();
		}
	  super.preferenceChanged( sourceChild, width, height );
	}
/**
 * used by CoStyledTextMeasurer
 */
 
void prepareForMeasurement(ViewFactory f)
{
	loadChildren(f);
}
public void release()
{
	List pool = getPool();

	if
		( ! pool.contains( this ) )
	{	
		if ( TRACE_POOL ) System.err.println( "RELEASE " + this );
		pool.add( this );
	} else {
		CoAssertion.assertTrue( false, "" );
	}
	

	removeAll();
}
  /**
   * Överriden metod från javax.swing.text.View.
   * Anropas då text tas bort.
   * Denna kod är till stor del kopierad from BoxView varför de ursprungliga kommentarerna på engelska lämnats kvar.
   * @see javax.swing.text.View.removeUpdate.
   * @see BoxView.removeUpdate.
   */
 public void removeUpdate( DocumentEvent e, Shape a, ViewFactory f )
{
	m_isDirty = true;

  Element elem = getElement();
  DocumentEvent.ElementChange ec = e.getChange( elem );
  boolean shouldForward = true;

  if
		( ec != null )
  {
		Element[] removedElems = ec.getChildrenRemoved();
		Element[] addedElems = ec.getChildrenAdded();
		View[] added = new View[ addedElems.length ];
		for
		  ( int i = 0; i < addedElems.length; i++ )
		{
		  added[ i ] = f.create( addedElems[ i ] );
		}

		int I = ec.getIndex();
		int L = removedElems.length;

		// kompensera för delade sub-view'er
		for
		  ( int i = 0; i < I; i++ )
		{
		  if ( ( (CoParagraphView) getView( i ) ).m_tail != null ) I++;
		}
		for
		  ( int i = I; i < I + L; i++ )
		{
		  if ( ( (CoParagraphView) getView( i ) ).m_tail != null ) L++;
		}

		replace( I, L, added );
		if
		  ( added.length != 0 )
		{
		  shouldForward = false;
		}

	  if
			( a != null )
	  {
			preferenceChanged( null, true, true );
			getContainer().repaint();
	  }
  }

  // find and forward if there is anything there to forward to.  
  // If children were added then there was a replacement of the removal range and there is no need to forward.
  if
		( shouldForward )
  {
		Rectangle2D alloc = ( ( a != null ) && m_childGeometryValid ) ? getInsideAllocation( a ) : null;
		int pos = e.getOffset();
		View v = getViewAtPosition( pos, alloc );
		if
		  ( v != null )
		{
		  v.removeUpdate( e, alloc, f );
		}
  }

	getContainer().repaint();  //vojnevojne
}
  /**
   * Överriden metod från javax.swing.text.CompositeView.
   * Ser till att sub-View-geometrin blir ogiltig då sub-View-strukturen ändras.
   * @see javax.swing.text.CompositeView.
   */
public void replace( int offset, int length, View[] elems )
{
	for
		( int i = offset; i < offset + length; i++ )
	{
		( (CoReuseableView) getView( i ) ).release();
	}

  super.replace( offset, length, elems );
	resetChildGeometry();

}
protected void reset()
{
	m_element = null;

  m_width = 0;
  m_height = 0;

  m_columnGeometry = null;
  
  m_baseLineGeometry = null;
  
	m_firstBaselineType = null;
	m_firstBaselineOffset = Float.NaN;
	m_verticalAlignmentType = null;
	m_verticalAlignmentMaxInter = Float.NaN;

	m_dummyText = null;

	m_tabBase = 0;
  m_columnBorderColor = null;
  m_scale = 1.0f;
  m_doPaintDummyText = false;

  m_allColumnsOfSameWidth = false;

  m_overflow = false;
  m_overflowPosition = 0;
  
  m_childLocationX = null;
  m_childLocationY = null;
  m_childWidth = null;
  m_childHeight = null;
  m_childGeometryValid = false;
}
  /**
   */
  protected void resetChildGeometry()
	{
	  m_childLocationX = m_childLocationY = m_childWidth = m_childHeight = null;
	  invalidateGeometry();
	}
protected void set( Element elem )
{
	m_element = elem;
}
void setBaselineGeometry( CoBaseLineGeometryIF blg )
{
	m_baseLineGeometry = blg;
}
private void setBaseLineGrid( CoParagraphView v, float y )
{
	if
		( m_baseLineGeometry != null )
	{
		/*
		float y0 = m_baseLineGeometry.getY0();
		float dy = m_baseLineGeometry.getDeltaY();
		int n = 1 + (int) ( ( y - y0 ) / dy );
		if ( n < 0 ) n = 0;
		v.setBaseLineGrid( y0 - y + n * dy, dy );*/
		v.setBaseLineGrid( m_baseLineGeometry.getY0(), m_baseLineGeometry.getDeltaY() );
	} else {
		v.setBaseLineGrid( 0, 0 );
	}
}
void setColumnGeometry( CoColumnGeometryIF cg )
{
	m_columnGeometry = cg;

	m_tabBase = (float) m_columnGeometry.getColumn( 0 ).getBounds().getX();
	
	m_allColumnsOfSameWidth = true;

	double W = -1;

	int I = m_columnGeometry.getColumnCount();
	for
		( int i = 0; i < I; i++ )
	{
		CoColumnGeometryIF.CoColumnIF c = m_columnGeometry.getColumn( i );
		if
			( ! c.isRectangular() )
		{
			m_allColumnsOfSameWidth = false;
			return;
		}
		
		double w = c.getBounds().getWidth();
		if
			( W == -1 )
		{
			W = w;
		} else {
			if
				( W != w )
			{
				m_allColumnsOfSameWidth = false;
				return;
			}
		}
	}
}
void setDummyText( String t )
{
	m_dummyText = t;
}
public void setParent( View parent )
{
	super.setParent( parent );

	if
		( parent == null )
	{
		release();
	}
}
/**
 * Överriden metod från javax.swing.text.View.
 * Gör ny sub-View-layout om så behövs.
 * @see javax.swing.text.View.setSize.
 */
public void setSize( float width, float height )
{
  // uppdatera sub-View-geometrins giltighet
  if
  	( width != m_width || height != m_height )
  {
	  invalidateGeometry();
  }

  // hämta kolumngeometri
  getGeometry();

  // vid behov gör om layout'en
  if
		( ! m_childGeometryValid )
  {
		if ( width >= 0 ) m_width = width;
		if ( height >= 0 ) m_height = height;
		if ( height >= 0 ) layout( m_width, m_height );
	}

}
void setTextGeometry( CoTextGeometryIF tg )
{
	if
		( tg == null )
	{
		m_firstBaselineOffset = Float.NaN;
		m_firstBaselineType = null;
		m_verticalAlignmentType = null;
		m_verticalAlignmentMaxInter = Float.NaN;
	} else {
		m_firstBaselineOffset = tg.getFirstBaselineOffset();
		m_firstBaselineType = tg.getFirstBaselineType();
		m_verticalAlignmentType = tg.getVerticalAlignmentType();
		m_verticalAlignmentMaxInter = tg.getVerticalAlignmentMaxInter();
	}
}
public void unsetDirty()
{
	m_isDirty = false;
}
/**
 * Placera ut sub-View'erna enligt den befinntliga kolumngeometri-cachen.
 */
private void updateChildGeometry()
{
//	System.err.println( "updateChildGeometry" );
	
  if ( m_childGeometryValid ) return;
  
  // vid behov allokera ny sub-View-geometri
  if
		( m_childWidth == null || true )
  {
		int I = getViewCount();

		// gör plats för delning av sub-View'er
	  I += m_columnGeometry.getColumnCount();

		// allokera
		if
			( m_childWidth == null || m_childWidth.length != I )
		{
			m_childWidth = new float [ I ];
			m_childHeight = new float [ I ];
			m_childLocationX = new float [ I ];
			m_childLocationY = new float [ I ];
		}
  }

  
  int columnIndex = 0;  // kolumn
  boolean isLastColumn = columnIndex == m_columnGeometry.getColumnCount() - 1;
	CoRectangle2DFloat column = m_columnGeometry.getColumn( columnIndex ).getBounds();

  float w = column.width;   // sub-View-dimension
  float h = column.height;  // sub-View-dimension
  float x = 0;  // sub-View-position
  float y = Float.isNaN( m_firstBaselineOffset ) ? 0 : m_firstBaselineOffset;  // sub-View-position

  m_overflow = false;
  m_overflowPosition = 0;
	CoParagraphView v = null;
	boolean wasMerged = false;
	

  
  // traversera sub-View'erna
	int I = getViewCount();
  for
		( int i = 0; i < I; i++ )
  {
	  // check if previous view was last in its column
		if
			(
				( i > 0 )
			&&
				( y > 0 )
			&&
				( (CoParagraphView) getView( i - 1 ) ).m_lastInColumn
			)
		{
			if 
				( isLastColumn )
			{
				m_overflow = true;
				m_overflowPosition = getView( i - 1 ).getEndOffset();
			} else {
				columnIndex++;
				isLastColumn = columnIndex == m_columnGeometry.getColumnCount() - 1;
				column = m_columnGeometry.getColumn( columnIndex ).getBounds();
				w = column.width;
				h = column.height;
				x = 0;
				y = Float.isNaN( m_firstBaselineOffset ) ? 0 : m_firstBaselineOffset;
			}
		}
	  

		v = (CoParagraphView) getView( i );


		if
		  ( m_overflow )
		{
		  // denna sub-View får inte plats, placera den utanför yttre gränserna
		  v.setColumn( m_columnGeometry.getColumn( columnIndex ), m_columnGeometry.getColumn( columnIndex ).getBounds().height );
		  m_childLocationX[ i ] = m_width;
		  m_childLocationY[ i ] = m_height;
		  m_childWidth[ i ] = w;
		  m_childHeight[ i ] = v.getPreferredSpan( Y_AXIS );
		  continue;
		}


		
		// top of column
		if
			( ( y > 0 ) && v.m_topOfColumn )
		{
			if 
				( isLastColumn )
			{
				m_overflow = true;
				m_overflowPosition = v.getStartOffset();
			} else {
				columnIndex++;
				isLastColumn = columnIndex == m_columnGeometry.getColumnCount() - 1;
				column = m_columnGeometry.getColumn( columnIndex ).getBounds();
				w = column.width;
				h = column.height;
				x = 0;
				y = Float.isNaN( m_firstBaselineOffset ) ? 0 : m_firstBaselineOffset;
			}
		  i--;
		  continue;
		}


		
		// sätt ihop eventuellt delade sub-View'er
		int tailCount = v.mergeTail();
		if
		  ( tailCount > 0 )
		{
	 		v.setColumn( m_columnGeometry.getColumn( columnIndex ), y );
			setBaseLineGrid( v, y + column.y );
			v.rebuildRows();
		  super.replace( i + 1, tailCount, ZERO_VIEWS );
		  I -= tailCount;
		  i--;
		  wasMerged = true;
		  continue;
		}

		// om kolumnerna kan ha olika bredd så måste sub-View'ns rader byggas om
		// detta är dyrt men tyvärr nödvändigt
		if
			( ! wasMerged )
		{
			v.setColumn( m_columnGeometry.getColumn( columnIndex ), y );
			setBaseLineGrid( v, y + column.y );
			v.rebuildRows();
//			if
//				( ! m_allColumnsOfSameWidth )
//			{
//				v.rebuildRows();
//			}
		}
		wasMerged = false;

		
		// får sub-View'n plats ?
		float span = v.getPreferredSpan( Y_AXIS );

		boolean doesFit = true;
		if
			( y + span > h )
		{
			doesFit = false;
		}
		
		if
			( ( ! doesFit ) && isLastColumn )
		{
			m_overflow = true;
			m_overflowPosition = v.getEndOffset();
		}


		
		// sub-View'n får plats, placera ut den
		if
//		  ( doesFit )
		  ( doesFit || isLastColumn ) // clip last line, don't trunscate it
		{
		  m_childLocationX[ i ] = x + column.x;
		  m_childLocationY[ i ] = y + column.y;
		  m_childWidth[ i ] = w;
		  m_childHeight[ i ] = span;
		  y += span;
		  continue;
		}


		
		// sub-View'n får inte plats
		boolean tryNextcolumn = false;
		float availableHeight = h - y;
		CoParagraphView tail = null;

		if
			( ( (CoParagraphView) v ).m_keepLinesTogether )
		{
		  if
				( ! m_overflow )
		  {
				// stycket är odelbart, prova nästa kolumn
			  tryNextcolumn = true;
		  }
		} else {
		  // dela sub-View'n
			tail = v.splitAt( availableHeight );
		
			if
			  ( tail == null )
			{
			  if
					( ! m_overflow )
			  {
					// första raden får inte plats, prova nästa kolumn
				  tryNextcolumn = true;
			  }
			}
		}

		if
		  ( tryNextcolumn )
	  {
			columnIndex++;
			isLastColumn = columnIndex == m_columnGeometry.getColumnCount() - 1;
			column = m_columnGeometry.getColumn( columnIndex ).getBounds();
			w = column.width;
			h = column.height;
			x = 0;
			y = Float.isNaN( m_firstBaselineOffset ) ? 0 : m_firstBaselineOffset;
		  i--;
		  continue;
		}

		// placera ut första delen
		m_childLocationX[ i ] = x + column.x;
		m_childLocationY[ i ] = y + column.y;
		m_childWidth[ i ] = w;
		m_childHeight[ i ] = v.getPreferredSpan( Y_AXIS );//availableHeight;
		m_overflowPosition = v.getEndOffset();

		// stega fram kolumn
		if
		  ( ! m_overflow )
		{
		  columnIndex++;
		  isLastColumn = columnIndex == m_columnGeometry.getColumnCount() - 1;
			column = m_columnGeometry.getColumn( columnIndex ).getBounds();
		  w = column.width;
		  h = column.height;
		  x = 0;
		  y = Float.isNaN( m_firstBaselineOffset ) ? 0 : m_firstBaselineOffset;
		}

		
		if
			( ( tail != null ) && ( tail != v ) )
		{
			// stoppa in andra delen
			I++;
			ONE_VIEW[ 0 ] = tail;
			super.replace( i + 1, 0, ONE_VIEW );
		}
	
		continue;
  }

  fixVerticalAlignment();
  
}
/**
   * Implementation av javax.swing.text.View.
   * @see javax.swing.text.View.viewToModel.
   */
public int viewToModel(float x, float y, Shape a, Position.Bias[] bias)
{
	// skala parametrarna
	if
		(m_scale != 1.0f)
	{
		x /= m_scale;
		y /= m_scale;
		
		Rectangle2D r = a.getBounds2D();

		r.setRect( r.getX() / m_scale,
			         r.getY() / m_scale,
			         r.getWidth() / m_scale,
			         r.getHeight() / m_scale );
		/*
		r.x = (int) (r.x / m_scale + 0.5f);
		r.y = (int) (r.y / m_scale + 0.5f);
		r.width = (int) (r.width / m_scale + 0.5f);
		r.height = (int) (r.height / m_scale + 0.5f);
		*/
		a = r;
	}

	// uppdatera sub-View-geometrin
	if
		(!m_childGeometryValid)
	{
		Rectangle2D alloc = a.getBounds2D();
		setSize((float) alloc.getWidth(), (float) alloc.getHeight());
	}

	// anropa superklassen
	int pos = super.viewToModel(x, y, a, bias);
	return pos;
}

	private static Line2D m_line = new Line2D.Double();
	private static Rectangle2D m_rectangle = new Rectangle2D.Double();
	public static Color TAG_COLOR = Color.gray;
//	public static boolean SHOW_TAGS = true;
	public static Font TAG_FONT = new Font( "Dialog", Font.PLAIN, 10 );

public static void clearPools()
{
	m_pool = null;
}

public void collectRowHeights( List l )
{
	int I = getViewCount();
	for
		( int i = 0; i < I; i++ )
	{
		CoParagraphView v = (CoParagraphView) getView( i );
		v.collectRowHeights( l );
	}
}

/**
 * Draws the given text, expanding any tabs that are contained
 * using the given tab expansion technique.  This particular
 * implementation renders in a 1.1 style coordinate system
 * where ints are used and 72dpi is assumed.
 * 
 * @param s  the source of the text
 * @param x  the X origin >= 0
 * @param y  the Y origin >= 0
 * @param g  the graphics context
 * @param e  how to expand the tabs.  If this value is null, 
 *   tabs will be expanded as a space character.
 * @param startOffset starting offset of the text in the document >= 0
 * @returns  the X location at the end of the rendered text
 */
public static final void drawOverflowMarker( CoPaintable g,
											                       double x,
											                       double y,
	                                           float delta,
	                                           Color bg )
{
	int x0 = (int) ( x - delta ) - 1;
	int y0 = (int) ( y - delta ) - 1;
	int x1 = (int) ( x ) - 2;
	int y1 = (int) ( y ) - 2;
	int d = (int) ( delta );

	if
		( bg != null )
	{
		g.setColor( bg );
		m_rectangle.setRect( x0, y0, d + 1, d + 1 );
		g.fill( m_rectangle );
	}
	
	g.setColor( CoTextEditorIF.PAINT_OVERFLOW_INDICATOR_COLOR );
	
	m_rectangle.setRect( x0, y0, d - 1, d - 1 );
	g.draw( m_rectangle );

	m_line.setLine( x0, y0, x1, y1 );
	g.draw( m_line );

	m_line.setLine( x0, y1, x1, y0 );
	g.draw( m_line );

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

public final void paint(Graphics g, Shape allocation)
{
	paint( (Graphics2D) g, allocation );
}

/**
 * Implementation av javax.swing.text.View.
 * @see javax.swing.text.View.paint.
 */
public void paint( CoPaintable g, Shape allocation )
{
	m_scale = (float) g.getScale();

  Rectangle2D alloc = new Rectangle2D.Double();
  alloc.setRect( allocation.getBounds2D() );
  
  setSize( (float) alloc.getWidth(), (float) alloc.getHeight() );

  double x0 = 0;//alloc.getX();
  double y0 = 0;//alloc.getY();

  // kolumngränser
  if
		( m_columnBorderColor != null )
  {
		Shape clip = g.getClip();

		g.setClip( new Rectangle2D.Double( 0, 0, 9999, 9999 ) );

		g.setColor( m_columnBorderColor );
	
		int I = m_columnGeometry.getColumnCount();
		for
		  ( int i = 0; i < I; i++ )
		{
			CoRectangle2DFloat r = m_columnGeometry.getColumn( i ).getBounds();
			g.setStroke( new BasicStroke( 0.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, new float[] { 5, 5 }, 0.0f ) );
			m_rectangle.setRect( r.x + x0, r.y + y0, r.width, r.height );
		  g.draw( m_rectangle );
			g.setStroke( new BasicStroke( 0 ) );

		  CoColumnGeometryIF.CoColumnIF c = m_columnGeometry.getColumn( i );
		  c.setMargins( 0, 0 );
		  float[] r0 = new float [ 2 ];
		  float[] r1 = new float [ 2 ];
		  int res = 1;
		  for
		  	( int Y = 0; Y < r.height - res; Y += res )
		  {
			  c.getRange( Y, 0, r0 );
			  c.getRange( Y + res, 0, r1 );
				m_line.setLine( r.x + r0[ 0 ] + x0, r.y + Y + y0, r.x + r1[ 0 ], r.y + Y + res );
		  	g.draw( m_line );
				m_line.setLine( r.x + r0[ 0 ] + r0[ 1 ] + x0, r.y + Y + y0, r.x + r1[ 0 ] + r1[ 1 ], r.y + Y + res );
		  	g.draw( m_line );
		  }
		}
		
	  g.setClip( clip );
  }

  // dummy text
  int L = getDocument().getLength();
  if
  	(
	  	( L == 0 )
	  &&
	  	( g.getRenderingHint( CoTextRenderingHints.PAINT_DUMMY_TEXT ) == CoTextRenderingHints.PAINT_DUMMY_TEXT_ON )
	  &&
	  	( m_doPaintDummyText )
	  )
  {
		g.setStroke( new BasicStroke( 0 ) );
	  g.setColor( DUMMY_TEXT_COLOR );
	
		float DY = DUMMY_TEXT_FONT.getSize2D();
		DY = Math.max( 4, DY * m_scale ) / m_scale;
	
		List v = ( (CoImmutableStyledDocumentIF) getDocument() ).getAcceptedTags();
		Iterator acceptedTags = ( v == null ) ? null : v.iterator();
		String dummyText = m_dummyText;
		
	  int I = m_columnGeometry.getColumnCount();
	  for
	  	( int i = 0; i < I; i++ )
	  {
			CoRectangle2DFloat r = m_columnGeometry.getColumn( i ).getBounds();
			float X = r.x;
			float Y = r.y;
			float W = r.width;
			float H = r.height;

			int dy = Math.max( 2, (int) DY );
			
			float range[] = new float[ 2 ];
		  CoColumnGeometryIF.CoColumnIF c = m_columnGeometry.getColumn( i );
		  c.setMargins( 0, 0 );

			float y = 0;
			while
				( y + dy < H )
			{
		  	c.getRange( y, 0, range );
		  	c.getMinimalRange( y, y + dy, range );
				if
					( range[ 1 ] > 8 )
				{
					m_line.setLine( ( X + range[ 0 ] + 2 ) + 2, Y + y + dy, ( X + range[ 0 ] + range[ 1 ] - 2 ) - 2, Y + y + dy );
			  	g.draw( m_line );
					if
						( dummyText != null )
					{
						g.drawDecorationString( dummyText, (int) ( X + range[ 0 ] + 2 ) + 2, (int) y + dy, DUMMY_TEXT_FONT );
						dummyText = null;
					} else if
						( acceptedTags != null )
					{
						if
							( acceptedTags.hasNext() )
						{
							String t = (String) acceptedTags.next();
							g.drawDecorationString( t, (int) ( X + range[ 0 ] + 2 ) + 2, (int) y + dy, DUMMY_TEXT_FONT );
						} else {
							acceptedTags = null;
						}
					}
				}
				
				y += dy;
			}
	  }
	  return;
  }


  
  // sub-View'er
  int I = getViewCount();
  for
		( int i = 0; i < I; i++ )
  {
		if ( ( m_childLocationX[ i ] == m_width ) && ( m_childLocationY[ i ] == m_height ) ) continue;
		alloc.setRect( x0 + m_childLocationX[ i ],
				   y0 + m_childLocationY[ i ],
				   m_childWidth[ i ],
				   m_childHeight[ i ] );
		CoPaintableView v = getPaintableView( i );
		v.paint( g, alloc );
  }

  // overflow-markering
  if
		( m_overflow && g.getRenderingHint( CoTextEditorIF.PAINT_OVERFLOW_INDICATOR ) == CoTextEditorIF.PAINT_OVERFLOW_INDICATOR_ON )
  {
		int i = m_columnGeometry.getColumnCount() - 1;

		CoRectangle2DFloat r = m_columnGeometry.getColumn( i ).getBounds();
	  drawOverflowMarker( g, r.x + r.width, r.y + r.height, 10.0f, Color.white );
	}
}

public boolean showParagraphTags()
{
	return false;
}
}