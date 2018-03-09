package com.bluebrim.base.shared.geom;
import java.awt.*;
import java.awt.geom.*;

import org.w3c.dom.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Boxed line shape
 * A boxed line is an invisible rectangle with a visible line running through the middle
 * 
 * @author: Dennis Malmström
 */
 
public class CoBoxedLineShape extends CoRectangleShape implements CoBoxedLineShapeIF
{
/*
 * Used at XML import
 * Helena Rankegård 2001-10-29
 */
 
public static CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, CoXmlContext context ) 
{
	return new CoBoxedLineShape( node, context );
}
	// geometry
	private boolean m_isHorizontal = true;
	private double m_margin = 0.0;

	// Mutable proxy
	protected class MutableProxy extends CoShape.MutableProxy implements CoRemoteBoxedLineShapeIF
	{
		// non mutating methods, delegate to outer instance
		public double getMargin() { return CoBoxedLineShape.this.getMargin(); }
		public boolean isHorizontal() { return CoBoxedLineShape.this.isHorizontal(); }
		public boolean isVertical() { return CoBoxedLineShape.this.isVertical(); }

		// mutating methods, delegate to outer instance and nitofy owner
		public void setMargin(double r)
		{
	  	if ( r == CoBoxedLineShape.this.getMargin() ) return;
			CoBoxedLineShape.this.setMargin(r); 
			notifyOwner( 0 );
		}

		public void setHorizontal()
		{
	  	if ( CoBoxedLineShape.this.isHorizontal() ) return;
			CoBoxedLineShape.this.setHorizontal(); 
			notifyOwner( 0 );
		}

		public void setHorizontal( boolean b )
		{
	  	if ( CoBoxedLineShape.this.isHorizontal() == b ) return;
			CoBoxedLineShape.this.setHorizontal( b ); 
			notifyOwner( 0 );
		}

		public void setVertical()
		{
	  	if ( CoBoxedLineShape.this.isVertical() ) return;
			CoBoxedLineShape.this.setVertical(); 
			notifyOwner( 0 );
		}

		public void setVertical( boolean b )
		{
	  	if ( CoBoxedLineShape.this.isVertical() == b ) return;
			CoBoxedLineShape.this.setVertical( b ); 
			notifyOwner( 0 );
		}
	}
	public static final String XML_IS_HORIZONTAL = "isHorizontal";
	public static final String XML_MARGIN = "margin";
	// xml tag constants
	public static final String XML_TAG = "boxedLine";

public CoBoxedLineShape()
{
	super();
}


public CoBoxedLineShape(double x, double y, double width, double height)
{
	super( x, y, width, height );
}


public CoBoxedLineShape( RectangularShape rectangularShape )
{
	super( rectangularShape );
}


/*
 * Contructor used for XML import.
 * Helena Rankegård 2001-10-29
 */
 
protected CoBoxedLineShape( Node node, CoXmlContext context ) 
{
	super();

	// xml init
	NamedNodeMap map = node.getAttributes();
	
	m_margin = CoModelBuilder.getDoubleAttrVal( map, XML_MARGIN, m_margin );
	m_isHorizontal = CoModelBuilder.getBoolAttrVal( map, XML_IS_HORIZONTAL, m_isHorizontal );
}


protected CoShape.MutableProxy createMutableProxy()
{
	return new MutableProxy();	
}


public CoShapeIF createNewInstanceFrom( CoImmutableShapeIF s )
{
	CoBoxedLineShape S = (CoBoxedLineShape) super.createNewInstanceFrom( s );
	S.setHorizontal( S.getWidth() > S.getHeight() ); // alignment = dimension with greatest span
	return S;
}


// boxed lines have different snaping behavior (edge masks) than other bounding shapes

protected CoReshapeHandleIF[] createReshapeHandles()
{
	return new CoReshapeHandleIF[]
	{
		new CoReshapeHandleIF() // upper left
		{
			public final double getX() { return CoBoxedLineShape.this.getX(); }
			public final double getY() { return CoBoxedLineShape.this.getY(); }
			public final int getEdgeMask() { return HORIZONTAL_EDGE_MASK | VERTICAL_EDGE_MASK; }
			public final void move( double dx, double dy )
			{
				double w = getWidth() - dx;
				double h = getHeight() - dy;
				if
					( m_keepAspectRatio )
				{
					double sw = w / getWidth();
					double sh = h / getHeight();
					if
						( ( ! Double.isInfinite( sw ) ) && ( ! Double.isInfinite( sh ) ) )
					{
						if
							( sw < sh )
						{
							w = getWidth() * sh;
						} else {
							h = getHeight() * sw;
						}
					}
				}

				dx = getWidth() - w;
				dy = getHeight() - h;

				setX( CoBoxedLineShape.this.getX() + dx );
				setWidth( w );
				setY( CoBoxedLineShape.this.getY() + dy );
				setHeight( h );
			}
		},
		new CoReshapeHandleIF() // upper
		{
			public final double getX() { return CoBoxedLineShape.this.getX() + getWidth() / 2; }
			public final double getY() { return CoBoxedLineShape.this.getY(); }
			public final int getEdgeMask() { return HORIZONTAL_EDGE_MASK; }
			public final void move( double dx, double dy )
			{
				double w = getWidth();
				double h = getHeight();
				if
					( m_keepAspectRatio && ( w != 0 ) && ( h != 0 ) )
				{
					double dw = ( w * ( ( h - dy ) / h ) ) - w;
					setX( CoBoxedLineShape.this.getX() - dw / 2 );
					setWidth( w + dw );
				}
				
				setY( CoBoxedLineShape.this.getY() + dy );
				setHeight( h - dy );
			}
		},
		new CoReshapeHandleIF() // upper right
		{
			public final double getX() { return CoBoxedLineShape.this.getX() + getWidth(); }
			public final double getY() { return CoBoxedLineShape.this.getY(); }
			public final int getEdgeMask() { return HORIZONTAL_EDGE_MASK | VERTICAL_EDGE_MASK; }
			public final void move( double dx, double dy )
			{
				double w = getWidth() + dx;
				double h = getHeight() - dy;
				if
					( m_keepAspectRatio )
				{
					double sw = w / getWidth();
					double sh = h / getHeight();
					if
						( ( ! Double.isInfinite( sw ) ) && ( ! Double.isInfinite( sh ) ) )
					{
						if
							( sw < sh )
						{
							w = getWidth() * sh;
						} else {
							h = getHeight() * sw;
						}
					}
				}

//				dx = w - getWidth();
				dy = getHeight() - h;

				setWidth( w );
				setY( CoBoxedLineShape.this.getY() + dy );
				setHeight( h );
			}
		},
		new CoReshapeHandleIF() // right
		{
			public final double getX() { return CoBoxedLineShape.this.getX() + getWidth(); }
			public final double getY() { return CoBoxedLineShape.this.getY() + getHeight() / 2; }
			public final int getEdgeMask() { return VERTICAL_EDGE_MASK; }
			public final void move( double dx, double dy )
			{
				double w = getWidth();
				double h = getHeight();
				if
					( m_keepAspectRatio && ( w != 0 ) && ( h != 0 ) )
				{
					double dh = ( h * ( ( w + dx ) / w ) ) - h;
					setY( CoBoxedLineShape.this.getY() - dh / 2 );
					setHeight( h + dh );
				}
				
				setWidth( w + dx );
			}
		},
		new CoReshapeHandleIF() // lower right
		{
			public final double getX() { return CoBoxedLineShape.this.getX() + getWidth(); }
			public final double getY() { return CoBoxedLineShape.this.getY() + getHeight(); }
			public final int getEdgeMask() { return HORIZONTAL_EDGE_MASK | VERTICAL_EDGE_MASK; }
			public final void move( double dx, double dy )
			{
				double w = getWidth() + dx;
				double h = getHeight() + dy;
				if
					( m_keepAspectRatio )
				{
					double sw = w / getWidth();
					double sh = h / getHeight();
					if
						( ( ! Double.isInfinite( sw ) ) && ( ! Double.isInfinite( sh ) ) )
					{
						if
							( sw < sh )
						{
							w = getWidth() * sh;
						} else {
							h = getHeight() * sw;
						}
					}
				}

				setWidth( w );
				setHeight( h );
			}
		},
		new CoReshapeHandleIF() // lower
		{
			public final double getX() { return CoBoxedLineShape.this.getX() + getWidth() / 2; }
			public final double getY() { return CoBoxedLineShape.this.getY() + getHeight(); }
			public final int getEdgeMask() { return HORIZONTAL_EDGE_MASK; }
			public final void move( double dx, double dy )
			{
				double w = getWidth();
				double h = getHeight();
				if
					( m_keepAspectRatio && ( w != 0 ) && ( h != 0 ) )
				{
					double dw = ( w * ( ( h + dy ) / h ) ) - w;
					setX( CoBoxedLineShape.this.getX() - dw / 2 );
					setWidth( w + dw );
				}
				
				setHeight( h + dy );
			}
		},
		new CoReshapeHandleIF() // lower left
		{
			public final double getX() { return CoBoxedLineShape.this.getX(); }
			public final double getY() { return CoBoxedLineShape.this.getY() + getHeight(); }
			public final int getEdgeMask() { return HORIZONTAL_EDGE_MASK | VERTICAL_EDGE_MASK; }
			public final void move( double dx, double dy )
			{
				double w = getWidth() - dx;
				double h = getHeight() + dy;
				if
					( m_keepAspectRatio )
				{
					double sw = w / getWidth();
					double sh = h / getHeight();
					if
						( ( ! Double.isInfinite( sw ) ) && ( ! Double.isInfinite( sh ) ) )
					{
						if
							( sw < sh )
						{
							w = getWidth() * sh;
						} else {
							h = getHeight() * sw;
						}
					}
				}

				dx = getWidth() - w;

				setX( CoBoxedLineShape.this.getX() + dx );
				setWidth( w );
				setHeight( h );
			}
		},
		new CoReshapeHandleIF() // left
		{
			public final double getX() { return CoBoxedLineShape.this.getX(); }
			public final double getY() { return CoBoxedLineShape.this.getY() + getHeight() / 2; }
			public final int getEdgeMask() { return VERTICAL_EDGE_MASK; }
			public final void move( double dx, double dy )
			{
				double w = getWidth();
				double h = getHeight();
				if
					( m_keepAspectRatio && ( w != 0 ) && ( h != 0 ) )
				{
					double dh = ( h * ( ( w - dx ) / w ) ) - h;
					setY( CoBoxedLineShape.this.getY() - dh / 2 );
					setHeight( h + dh );
				}
				
				setX( CoBoxedLineShape.this.getX() + dx );
				setWidth( w - dx );
			}
		},
	};

}


public CoShapeIF deepClone()
{
	CoBoxedLineShape s = (CoBoxedLineShape) super.deepClone();
	return s;
}


// boxed line is its own expansion

protected CoShapeIF doCreateExpandedInstance( double delta )
{
	return this;
}


public boolean equals( Object s )
{
	if ( s == this ) return true;

	if ( ! ( s instanceof CoBoxedLineShape ) ) return false;
	
	CoBoxedLineShape r = (CoBoxedLineShape) s;
	return
		(
			( m_isHorizontal == r.m_isHorizontal )
		&&
			( m_margin == r.m_margin )
		&&
			super.equals( s )
		);
}


public String getFactoryKey ()
{
	return CoBoxedLineShapeIF.BOXED_LINE;
}


public double getMargin()
{
	return m_margin;
}


public Shape getShape()
{
	// awt representation is a line
	if
		( m_shape == null )
	{
		if
			( m_isHorizontal )
		{
			double x0 = m_x + m_margin;
			double x1 = m_x + m_width - m_margin;
			double y = m_y + m_height / 2.0;
			m_shape = new Line2D.Double( Math.min( x0, x1 ),
				                           y,
				                           Math.max( x0, x1 ),
				                           y );
		} else {
			double y0 = m_y + m_margin;
			double y1 = m_y + m_height - m_margin;
			double x = m_x + m_width / 2.0;
			m_shape = new Line2D.Double( x,
																	 Math.min( y0, y1 ),
				                           x,
				                           Math.max( y0, y1 ) );
		}

		if
			( m_editMode )
		{
			GeneralPath gp = new GeneralPath();
			gp.moveTo( (float) ( m_x ), (float) ( m_y + m_height / 4 ) );
			gp.lineTo( (float) ( m_x ), (float) ( m_y ) );
			gp.lineTo( (float) ( m_x + m_width / 4 ), (float) ( m_y ) );

			gp.moveTo( (float) ( m_x + 3 * m_width / 4 ), (float) ( m_y ) );
			gp.lineTo( (float) ( m_x + m_width ), (float) ( m_y ) );
			gp.lineTo( (float) ( m_x + m_width ), (float) ( m_y + m_height / 4 ) );

			gp.moveTo( (float) ( m_x + m_width ), (float) ( m_y + 3 * m_height / 4 ) );
			gp.lineTo( (float) ( m_x + m_width ), (float) ( m_y + m_height ) );
			gp.lineTo( (float) ( m_x + 3 * m_width / 4 ), (float) ( m_y + m_height ) );

			gp.moveTo( (float) ( m_x + m_width / 4 ), (float) ( m_y + m_height ) );
			gp.lineTo( (float) ( m_x ), (float) ( m_y + m_height ) );
			gp.lineTo( (float) ( m_x ), (float) ( m_y + 3 * m_height / 4 ) );

			gp.append( m_shape, false );
			
			m_shape = gp;
		}

	}	
	
	return m_shape;
}


public String getXmlTag()
{
	return XML_TAG;
}


public boolean isHorizontal()
{
	return m_isHorizontal;
}


public boolean isVertical()
{
	return ! m_isHorizontal;
}


public void setHorizontal()
{
	m_isHorizontal = true;

	invalidate();
}


public void setHorizontal( boolean b )
{
	m_isHorizontal = b;

	invalidate();
}


public void setMargin( double m )
{
	m_margin = Math.min( Math.max( 0, m ), m_width / 2 );

	invalidate();
}


public void setVertical()
{
	m_isHorizontal = false;

	invalidate();
}


public void setVertical( boolean b )
{
	m_isHorizontal = ! b;

	invalidate();
}


public String toString()
{
	return "CoBoxedLineShape: " + m_x + " " + m_y + "   " + m_width + " " + m_height + " " + m_margin + " " + m_isHorizontal;
}


/*
 * Used at XML export
 * Helena Rankegård 2001-10-29
 */
 
public void xmlVisit( CoXmlVisitorIF visitor )
{
	super.xmlVisit( visitor );
	
	visitor.exportAttribute( XML_IS_HORIZONTAL, ( m_isHorizontal ? Boolean.TRUE : Boolean.FALSE ).toString() );
	visitor.exportAttribute( XML_MARGIN, Double.toString( m_margin ) );
}
}