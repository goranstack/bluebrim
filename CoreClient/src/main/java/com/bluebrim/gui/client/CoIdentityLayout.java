package com.bluebrim.gui.client;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;

//

public class CoIdentityLayout implements LayoutManager2
{
	private Component m_component;
public CoIdentityLayout()
{
	super();
}
public void addLayoutComponent( Component comp, Object constraints )
{
	com.bluebrim.base.shared.debug.CoAssertion.assertTrue( m_component == null, getClass() + " can only handle one component" );
	m_component = comp;
}
public void addLayoutComponent( String name, Component comp )
{
	addLayoutComponent( comp, name );
}
public float getLayoutAlignmentX( Container target )
{
	return m_component == null ? 0.5f : m_component.getAlignmentX();
}
public float getLayoutAlignmentY( Container target )
{
	return m_component == null ? 0.5f : m_component.getAlignmentY();
}
public void invalidateLayout( Container target )
{
}
public void layoutContainer( Container parent )
{
	if ( m_component != null ) m_component.setBounds( 0, 0, parent.getWidth(), parent.getHeight() );
}
public Dimension maximumLayoutSize( Container target )
{
	return m_component == null ? new Dimension( Short.MAX_VALUE, Short.MAX_VALUE ) : m_component.getMaximumSize();
}
public Dimension minimumLayoutSize( Container target )
{
	return m_component == null ? new Dimension( 0, 0 ) : m_component.getMinimumSize();
}
public Dimension preferredLayoutSize( Container target )
{
	return m_component == null ? new Dimension( 0, 0 ) : m_component.getPreferredSize();
}
public void removeLayoutComponent( Component comp )
{
	m_component = null;
}
}
