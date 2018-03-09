package com.bluebrim.base.shared.geom;
import java.awt.*;
import java.awt.geom.*;
import java.lang.ref.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;

/**
 * Superclass for all geometrical shapes
 * 
 * @author: Dennis Malmström
 */
 
public abstract class CoShape extends CoObject implements CoShapeIF, Cloneable
{
/*
 * Used at XML import
 * Helena Rankegård 2001-10-29
 */
 
public void xmlAddSubModel(String name, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) 
{ 
}
/*
 * Used at XML import
 * Helena Rankegård 2001-10-29
 */
 
public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context)
{ 
}

	protected transient boolean m_keepAspectRatio = false;
	protected transient boolean m_editMode = false;

	// handle cache
	protected transient CoReshapeHandleIF[] m_reshapeHandles;
	protected transient CoHandleIF m_moveHandle;

	// float equality tolerance
	private static double CLOSE_ENOUGH  = 0.01;

	
	// Mutable proxy
	protected class MutableProxy implements CoRemoteShapeIF
	{
		private CoShapeIF.Owner m_owner;

		public void setOwner( CoShapeIF.Owner owner )
		{
			m_owner = owner;
		}
		
		public CoShapeIF.Owner getOwner()
		{
			return m_owner;
		}

		// non mutating methods, delegate to outer instance
		public void addPropertyChangeListener( CoPropertyChangeListener l ) { CoShape.this.addPropertyChangeListener( l ); }
	 	public void removePropertyChangeListener( CoPropertyChangeListener l ) {CoShape.this.removePropertyChangeListener( l ); }
		public boolean hit( Graphics2D g, Rectangle r )  { return CoShape.this.hit( g, r ); }
		public CoShapeIF createExpandedInstance( double delta ) { return CoShape.this.createExpandedInstance( delta ); }
		public CoShapeIF copyTranslatedBy( double x, double y ) { return CoShape.this.copyTranslatedBy( x, y ); }
	  	public CoShapeIF createNewInstance() { return CoShape.this.createNewInstance(); }
	  	public CoShapeIF createNewInstanceFrom( CoImmutableShapeIF s ) { return CoShape.this.createNewInstanceFrom( s ); }
	  	public CoShapeIF deepClone() { return CoShape.this.deepClone(); }
	  	public String getFactoryKey() { return CoShape.this.getFactoryKey(); }
	  	public double getHeight() { return CoShape.this.getHeight(); }
	  	public CoHandleIF getMoveHandle() { return CoShape.this.getMoveHandle(); }
	  	public CoReshapeHandleIF[] getReshapeHandles() { return CoShape.this.getReshapeHandles(); }
	  	public Shape getShape() { return CoShape.this.getShape(); }
	  	public double getWidth() { return CoShape.this.getWidth(); }
	  	public double getX() { return CoShape.this.getX(); }
	  	public double getY() { return CoShape.this.getY(); }
	  	public double calculateArea() { return CoShape.this.calculateArea(); }
		public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) { CoShape.this.xmlVisit(visitor); }	
		public void xmlAddSubModel(String name, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) { CoShape.this.xmlAddSubModel(name, subModel, context); }
		public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) { CoShape.this.xmlImportFinished(node, context); }
	  	public CoShapeIF getMutableProxy( CoShapeIF.Owner owner ) { return CoShape.this.getMutableProxy( owner ); }
	  	public void setKeepAspectRatio( boolean b ) { CoShape.this.setKeepAspectRatio( b ); }
	  	public boolean isClosedShape() { return CoShape.this.isClosedShape(); }
	  	public boolean isInside( Point2D p, double strokeWidth, double outsideStrokeWidth ) { return CoShape.this.isInside( p, strokeWidth, outsideStrokeWidth ); }
	  	public boolean isOutside( Point2D p, double strokeWidth, double outsideStrokeWidth ) { return CoShape.this.isOutside( p, strokeWidth, outsideStrokeWidth ); }
 	
		// methods that will fail because of non-serializable return types.
	  	public Rectangle2D getBounds2D() { illegalCall(); return CoShape.this.getBounds2D(); }
	  	public PathIterator getPathIterator( AffineTransform t ) { illegalCall(); return CoShape.this.getPathIterator( t ); }
	  	public PathIterator getPathIterator( AffineTransform t, double f ) { illegalCall(); return CoShape.this.getPathIterator( t, f ); }

	  	protected void illegalCall()
	  	{
		  	throw new RuntimeException( "Illegal call" );
	  	}
	  	
			// mutating methods, delegate to outer instance and nitofy owner
	  	public void normalize()
	  	{
		  	int p = 0;
		  	if ( CoShape.this.getX() != 0 ) p |= CoShapeIF.Owner.X;
		  	if ( CoShape.this.getY() != 0 ) p |= CoShapeIF.Owner.Y;
		  	
				CoShape.this.normalize();
				notifyOwner( p );
	  	}

		 	public void setEditMode( boolean b )
		 	{
		  	if ( b == CoShape.this.m_editMode ) return;
				CoShape.this.setEditMode( b );
	  	}

		 	public void setHeight( double h )
		 	{
		  	if ( h == CoShape.this.getHeight() ) return;
				CoShape.this.setHeight( h );
				notifyOwner( CoShapeIF.Owner.HEIGHT );
	  	}

	  	public void setTranslation( double x, double y )
	  	{
		  	if ( ( x == CoShape.this.getX() ) && ( y == CoShape.this.getY() ) ) return;
	 
		  	int p = 0;
		  	if ( CoShape.this.getX() != x ) p |= CoShapeIF.Owner.X;
		  	if ( CoShape.this.getY() != y ) p |= CoShapeIF.Owner.Y;
		  	
	  	 	CoShape.this.setTranslation( x, y );
				notifyOwner( p );
	  	}

	  	public void setWidth( double w )
	  	{
		  	if ( w == CoShape.this.getWidth() ) return;
	   	 	CoShape.this.setWidth( w );
				notifyOwner( CoShapeIF.Owner.WIDTH );
	  	}

	  	public void setSize( double w, double h )
	  	{
		  	if ( ( w == CoShape.this.getWidth() ) && ( h == CoShape.this.getHeight() ) ) return;

		  	int p = 0;
		  	if ( CoShape.this.getWidth() != w ) p |= CoShapeIF.Owner.WIDTH;
		  	if ( CoShape.this.getHeight() != h ) p |= CoShapeIF.Owner.HEIGHT;
		  	
				CoShape.this.setWidth( w );
				CoShape.this.setHeight( h );
				notifyOwner( p );
	  	}

	  	public void setX( double x )
	  	{
		  	if ( x == CoShape.this.getX() ) return;
				CoShape.this.setX( x );
				notifyOwner( CoShapeIF.Owner.X );
	  	}

	  	public void setY( double y )
	  	{
		  	if ( y == CoShape.this.getY() ) return;
				CoShape.this.setY( y );
				notifyOwner( CoShapeIF.Owner.Y );
	  	}

	  	public void translateBy( double dx, double dy )
	  	{
		  	if ( ( dx == 0 ) && ( dy == 0 ) ) return;
	 
		  	int p = 0;
		  	if ( 0 != dx ) p |= CoShapeIF.Owner.X;
		  	if ( 0 != dy ) p |= CoShapeIF.Owner.Y;
		  	
				CoShape.this.translateBy( dx, dy );
				notifyOwner( p );
	  	}

	  	// owner notification
	  	protected void notifyOwner( int properties )
	  	{
				m_owner.shapeChanged( properties );
	  	}
	};












	private transient SoftReference m_expandedInstancesCache;


	// double -> CoShapeIF hash table, most of it was stolen from java.util.HashMap
	private static class Double2ShapeMap implements java.io.Serializable
	{
		// entry = two characters and a tracking value
		private static class Entry implements java.io.Serializable
		{
			int m_hash;
			double m_key;
			CoShapeIF m_value;
			Entry m_next;

			Entry( int hash, double key, CoShapeIF value, Entry next )
			{
		    m_hash = hash;
				m_key = key;
				m_value = value;
		    m_next = next;
			}
		}

		private Entry m_table[] = new Entry[ 10 ];
		private float m_loadFactor = 0.75f;
		private int m_count;
		private int m_threshold = (int) ( m_table.length * m_loadFactor );
		
		public void put( double key, CoShapeIF value )
		{
			// Makes sure the key is not already in the HashMap.
			Entry tab[] = m_table;

			int hash = hash( key );
			int index = (hash & 0x7FFFFFFF) % tab.length;
			for
				( Entry e = tab[ index ] ; e != null ; e = e.m_next )
			{
				if
					( ( e.m_hash == hash ) && ( key == e.m_key ) )
				{
					e.m_value = value;
					return;
				}
			}

			if
				( m_count >= m_threshold )
			{
		    // Rehash the table if the threshold is exceeded
		    rehash();

				tab = m_table;
				index = (hash & 0x7FFFFFFF) % tab.length;
			}

			// Creates the new entry.
			Entry e = new Entry( hash, key, value, tab[ index ] );
			tab[ index ] = e;
			m_count++;
		}

		public CoShapeIF get( double key )
		{
			Entry tab[] = m_table;
			int hash = hash( key );
			int index = (hash & 0x7FFFFFFF) % tab.length;
			for
				( Entry e = tab[index]; e != null; e = e.m_next )
			{
				if ( ( e.m_hash == hash ) && ( key == e.m_key ) ) return e.m_value;
			}
			return null;
		}
		
		public void clear()
		{
			Entry tab[] = m_table;
			for (int index = tab.length; --index >= 0;) tab[index] = null;
			m_count = 0;
		}

		private int hash( double key )
		{
			long bits = Double.doubleToLongBits( key );
			return (int)(bits ^ (bits >> 32));
		}

		private void rehash()
		{
			int oldCapacity = m_table.length;
			Entry oldMap[] = m_table;
			int newCapacity = oldCapacity * 2 + 1;
			Entry newMap[] = new Entry[newCapacity];
			m_threshold = (int) (newCapacity * m_loadFactor);
			m_table = newMap;
			for
				(int i = oldCapacity; i-- > 0;)
			{
				for
					(Entry old = oldMap[i]; old != null;)
				{
					Entry e = old;
					old = old.m_next;
					int index = (e.m_hash & 0x7FFFFFFF) % newCapacity;
					e.m_next = newMap[index];
					newMap[index] = e;
				}
			}
		}
	};

	public static final String XML_SHAPE = "shape";
	// xml tag constants
	public static final String XML_TAG = "shape";

public double calculateArea()
{
	Rectangle2D b = getBounds2D();
	
	return b.getWidth() * b.getHeight();
}


public static boolean closeEnough( double d1, double d2 )
{
	return Math.abs( d1 - d2 ) < CLOSE_ENOUGH;
}


public static boolean closeEnough( double x1, double y1, double x2, double y2 )
{
	return closeEnough( x1, x2 ) && closeEnough( y1, y2 );
}


protected abstract CoShape copyForTranslation();


public CoShapeIF copyTranslatedBy( double x, double y )
{
	CoShapeIF copy = copyForTranslation();
	
	copy.translateBy( x, y );
	
	return copy;
}


public CoShapeIF createExpandedInstance( double delta )
{
	if
		(( m_expandedInstancesCache == null ) || (m_expandedInstancesCache.get() == null))
	{
		m_expandedInstancesCache = new SoftReference( new Double2ShapeMap() );
	}

	Double2ShapeMap map = (Double2ShapeMap) m_expandedInstancesCache.get();

	CoShapeIF expanded = map.get( delta );

	if
		( expanded == null )
	{
		expanded = doCreateExpandedInstance( delta );
		map.put( delta, expanded );
	}

	return expanded;
}


protected CoHandleIF createMoveHandle()
{
	return new CoHandleIF()
	{
		public final void move( double dx, double dy )
		{
			translateBy( dx, dy ); // apply handle translation to shape
		}
	};
}


protected CoShape.MutableProxy createMutableProxy()
{
	return new MutableProxy();	
}


public CoShapeIF createNewInstance()
{
	try
	{
		return (CoShapeIF) getClass().newInstance();
	}
	catch ( Exception e )
	{
		return null;
	}
}


protected abstract CoReshapeHandleIF[] createReshapeHandles();


/**
 */
public CoShapeIF deepClone()
{
	CoShape s	= null;
	
	try
	{
		s	= (CoShape) clone();
	}
	catch ( CloneNotSupportedException ex )
	{
		throw new RuntimeException( getClass() + ".clone() failed" );
	}

	// handles can't be shared
	s.m_reshapeHandles = s.createReshapeHandles();
	s.m_moveHandle = s.createMoveHandle();
	
	return s;
}


protected abstract CoShapeIF doCreateExpandedInstance( double delta );


public Rectangle2D getBounds2D()
{
	return getShape().getBounds2D();
}


public final CoHandleIF getMoveHandle()
{
	if
		( m_moveHandle == null )
	{
		m_moveHandle = createMoveHandle();
	}
	
	return m_moveHandle;
}


public final CoShapeIF getMutableProxy( CoShapeIF.Owner owner )
{
	MutableProxy mp = createMutableProxy();
	mp.setOwner( owner );
	return mp;
	
	/*
	if
		( m_mutableProxy == null )
	{
		m_mutableProxy = createMutableProxy();
	}
	m_mutableProxy.setOwner(owner);
	return m_mutableProxy;
	*/
}


public PathIterator getPathIterator( AffineTransform at )
{
	return getShape().getPathIterator( at );
}


public PathIterator getPathIterator(AffineTransform at, double flatness)
{
	return getShape().getPathIterator(at, flatness);
}


public CoReshapeHandleIF[] getReshapeHandles()
{
	if
		( m_reshapeHandles == null )
	{
		m_reshapeHandles = createReshapeHandles();
	}
	
	return m_reshapeHandles;
}


public String getXmlTag()
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( false, "illegal call" );
	return null;
}


// hit test using Graphics2D

public boolean hit( Graphics2D g, Rectangle r )
{
	return
		g.hit( r, getShape(), false ) // in shape ?
	||
		g.hit( r, getShape(), true ); // on stroke ?
}


protected final boolean intersects( double x, double y, double w, double h )
{
	return getShape().intersects( x, y, w, h );
}


protected void invalidate()
{
	m_expandedInstancesCache = null;
}


public boolean isInside( Point2D p, double strokeWidth, double outsideStrokeWidth )
{
	return false;
}


// outside bounds -> outside shape

public boolean isOutside( Point2D p, double strokeWidth, double outsideStrokeWidth )
{
	double x = p.getX();
	double y = p.getY();

	if ( x < getX() - outsideStrokeWidth ) return true;
	if ( x > getX() + getWidth() + outsideStrokeWidth ) return true;
	if ( y < getY() - outsideStrokeWidth ) return true;
	if ( y > getY() + getHeight() + outsideStrokeWidth ) return true;

	return false;
}


public void setEditMode( boolean b )
{
	m_editMode = b;
}


public void setKeepAspectRatio( boolean b )
{
	m_keepAspectRatio = b;
}


public void setSize( double w, double h )
{
	setWidth( w );
	setHeight( h );
}


public void setTranslation( double x, double y )
{
	setX( x );
	setY( y );
}


public void translateBy( double x, double y )
{
	setX( getX() + x );
	setY( getY() + y );
}


/*
 * Used at XML export
 * Helena Rankegård 2001-10-29
 */
 
public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor)
{
	//visitor.exportAttribute( XML_SHAPE, getXmlTag() );
}
}