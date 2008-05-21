package com.bluebrim.base.shared.geom;

import java.awt.*;
import java.awt.geom.*;

import com.bluebrim.base.shared.*;

/**
 * Eftersom man bara kan stoppa in och inte få ut rotation ur
 * AffineTransform så kan vi inte använda en AffineTransform för att 
 * hålla i transformationsuppgifterna.
 */
public class CoTransform extends CoObject implements CoTransformIF
{
	protected double m_rotation;		// Rotationsvinkel i radianer
	protected double m_rotationPointX;
	protected double m_rotationPointY;

	protected transient AffineTransform m_transform;

/**
 * CoTransform constructor comment.
 */
public CoTransform() {
	super();
}
private void applyInverseOn(AffineTransform transform)
{
	if ( m_rotation != 0 ) transform.rotate( - m_rotation, m_rotationPointX, m_rotationPointY );
}
public void applyOn(AffineTransform transform)
{
	if ( m_rotation != 0 ) transform.rotate( m_rotation, m_rotationPointX, m_rotationPointY );
}
public void applyOn(Graphics2D g2d)
{
	if ( m_rotation != 0 ) g2d.rotate( m_rotation, m_rotationPointX, m_rotationPointY );
}

/*
 * Answer an Affinetransform initialized with values from the reciever
 */
public AffineTransform createAffineTransform()
{
	AffineTransform t = new AffineTransform();
	applyOn( t );
	return t;
}
/*
 * Answer an Affinetransform initialized with values from the reciever
 */
public AffineTransform createInverseAffineTransform()
{
	AffineTransform t = new AffineTransform();
	applyInverseOn( t );
	return t;
}
public CoTransformIF deepClone()
{
	try
	{
		CoTransform t = (CoTransform) clone();
		t.invalidate();
		return t;
	}
	catch ( CloneNotSupportedException ex )
	{
		com.bluebrim.base.shared.debug.CoAssertion.assertTrue( false, "Failure in CoTransform.deepClone()" );
	}

	return null;
}
public boolean equals( Object o )
{
	if ( this == o ) return true;
	if
		( o instanceof CoImmutableTransformIF )
	{
		CoImmutableTransformIF t = (CoImmutableTransformIF) o;
		
		return 
			(
				( m_rotation == t.getRotation() )
			&&
				( m_rotationPointX == t.getRotationPointX() )
			&&
				( m_rotationPointY == t.getRotationPointY() )
			);
	} else {
		return super.equals( o );
	}
}
public String getFactoryKey (){
	return CoTransformIF.TRANSFORM;
}
public double getRotation() {
	return m_rotation;
}
public double getRotationPointX()
{
	return m_rotationPointX;
}
public double getRotationPointY()
{
	return m_rotationPointY;
}
public int hashCode()
{
	long bits = Double.doubleToLongBits( m_rotation );
	return (int) ( bits ^ ( bits >> 32 ) );
}
private void invalidate()
{
	m_transform = null;
}
public boolean isIdentity()
{
	return ( m_rotation == 0 );
}
public void setRotation(double rotation)
{
	m_rotation = rotation;
	invalidate();
}
public void setRotationPoint( double x, double y)
{
	m_rotationPointX = x;
	m_rotationPointY = y;
	invalidate();
}
public String toString()
{
	return "CoTransform: " + m_rotation + " (" + m_rotationPointX + ", " + m_rotationPointY + ")";
}
public void transform( Point2D p )
{
	if ( isIdentity() ) return;
	
	if
		( m_transform == null )
	{
		m_transform = createAffineTransform();
	}

	m_transform.transform( p, p );
}
public void unapplyOn(Graphics2D g2d)
{
	if ( m_rotation != 0 ) g2d.rotate( - m_rotation, m_rotationPointX, m_rotationPointY );
}

public void untransform( Point2D p )
{
	if ( isIdentity() ) return;
	
	if
		( m_transform == null )
	{
		m_transform = createAffineTransform();
	}
	try
	{
		m_transform.inverseTransform( p, p );
	}
	catch ( NoninvertibleTransformException ex )
	{
		throw new RuntimeException( "NoninvertibleTransformException:\n" + ex );
	}

}
	public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) {
		visitor.exportAttribute("rotation", new Double(getRotation()).toString());
	}

public void applyOn( CoPaintable g2d )
{
	if ( m_rotation != 0 ) g2d.rotate( m_rotation, m_rotationPointX, m_rotationPointY );
}

public void unapplyOn(CoPaintable g2d)
{
	if ( m_rotation != 0 ) g2d.unrotate( m_rotation, m_rotationPointX, m_rotationPointY );
}
}