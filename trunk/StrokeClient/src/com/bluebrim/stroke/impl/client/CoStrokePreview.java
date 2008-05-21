package com.bluebrim.stroke.impl.client;
import java.awt.*;

import javax.swing.*;

import com.bluebrim.base.shared.*;
import com.bluebrim.stroke.shared.*;

public abstract class CoStrokePreview extends JComponent
{
	CoShapeIF m_shape;
	CoStrokeRenderer m_renderer = new CoStrokeRenderer();
public CoStrokePreview()
{
	this( new Dimension( 200, 150 ) );
	
	m_shape = new com.bluebrim.base.shared.geom.CoComponentShape( this, 20 );
}
private CoStrokePreview( Dimension size )
{
	setMinimumSize( size );
	setPreferredSize( size );
}
public CoStrokePreview( CoShapeIF shape, Dimension size )
{
	this( size );
	
	m_shape = shape;
}
public abstract com.bluebrim.stroke.shared.CoStrokePropertiesIF getStrokeProperties();
protected void paintComponent( Graphics g )
{
	super.paintComponent( g );

	Graphics2D g2d = (Graphics2D) g.create();

	m_renderer.setStrokeProperties( getStrokeProperties() );
	m_renderer.setShape( m_shape );

	CoScreenPaintable p = CoScreenPaintable.wrap( g2d );
	m_renderer.paint( p );
	p.releaseDelegate();

	g2d.dispose();
}
}