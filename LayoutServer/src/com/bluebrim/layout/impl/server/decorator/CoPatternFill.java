package com.bluebrim.layout.impl.server.decorator;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

import org.w3c.dom.*;

import com.bluebrim.layout.impl.shared.*;

/**
 * Implementation of pattern fill style
 * 
 * @author: Dennis Malmström
 */

public class CoPatternFill extends CoFillStyle implements CoPatternFillStyleIF, Cloneable
{
/*
 * Used at XML import
 * Helena Rankegård 2001-10-26
 */
 
public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel ( Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context ) 
{
	return new CoPatternFill();
}
	protected class MutableProxy extends CoFillStyle.MutableProxy implements CoRemotePatternFillStyleIF
	{
		public String getPattern() { return CoPatternFill.this.getPattern(); }


		public void setPattern( String pattern )
		{
			CoPatternFill.this.setPattern( pattern );
			m_owner.notifyOwner();
		}

	};

	private String m_patternKey = CHESSBOARD;
	private static Paint m_patternPaint;
	public static final String XML_PATTERN = "pattern";
	// xlm tags
	public static final String XML_TAG = "pattern-fill";

public CoPatternFill( ) {

	super();
}


protected CoFillStyle.MutableProxy createMutableProxy()
{
	return new CoPatternFill.MutableProxy();
}


public CoFillStyleIF deepClone()
{
	CoPatternFill c = null;

	try
	{
		c = (CoPatternFill) clone();
	}
	catch ( CloneNotSupportedException ex )
	{
		throw new RuntimeException( getClass() + ".clone() failed" );
	}
	
	return c;
}


public boolean equals( Object s )
{
	if ( s == this ) return true;

	if ( ! ( s instanceof CoGradientFill ) ) return false;

	return true;
}


public String getFactoryKey() 
{
	return PATTERN_FILL;
}


public Paint getPaint( Rectangle2D bounds )
{
	if
		( m_patternPaint == null )
	{
		int s = 40;
		
		if      ( m_patternKey.equals( CHESSBOARD ) ) ;
		else if ( m_patternKey.equals( SCALES ) ) s = 20;
		else if ( m_patternKey.equals( MESH ) ) s = 20;
		else if ( m_patternKey.equals( DOTS ) ) ;
		
		BufferedImage i = new BufferedImage( s, s, BufferedImage.TYPE_INT_RGB );
		Graphics2D g = (Graphics2D) i.getGraphics();

		g.setColor( Color.white );
		g.fillRect( 0, 0, s, s );
		
		if
			( m_patternKey.equals( CHESSBOARD ) )
		{
			g.setColor( Color.black );
			g.fillRect( 0, 0, s / 2, s / 2 );
			g.fillRect( s / 2, s / 2, s / 2, s / 2 );
		} else if
			( m_patternKey.equals( SCALES ) )
		{
			g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
			g.setStroke( new BasicStroke( 2 ) );
			g.setColor( Color.black );
			g.drawArc( 1 + s / 2, 0, s, s, 90, 180 );
			g.drawArc( 1, - s / 2, s, s, -90, -90 );
			g.drawArc( 1, s / 2, s, s, 90, 90 );
		} else if
			( m_patternKey.equals( MESH ) )
		{
			g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
			g.setStroke( new BasicStroke( 1 ) );
			g.setColor( Color.black );
			g.drawLine( s / 2, 0, 0, s / 2 );
			g.drawLine( s, s / 2, s / 2, s );
			g.drawLine( s / 2, 0, s, s / 2 );
			g.drawLine( 0, s / 2, s / 2, s );
		} else if
			( m_patternKey.equals( DOTS ) )
		{
			g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
			g.setColor( Color.black );
			g.fillOval( s / 4, s / 4, s / 2, s / 2 );
		}

		
		m_patternPaint = new TexturePaint( i, new Rectangle2D.Double( 0, 0, s, s ) );
	}

	return m_patternPaint;
}


public String getPattern()
{
	return m_patternKey;
}


public void setPattern( String pattern )
{
	m_patternKey = pattern;
	m_patternPaint = null;
}
}