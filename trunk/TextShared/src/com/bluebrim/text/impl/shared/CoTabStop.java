package com.bluebrim.text.impl.shared;


/**
 * Implementation of tab stop
 * 
 * @author: Dennis Malmström
 */

public class CoTabStop implements CoTabStopIF
{
	protected int m_alignment = ALIGN_LEFT;
	protected float m_position = 0;
	protected int m_leader = LEADER_NONE;
	protected char m_alignOn [] = { }; // valid if m_alignment = ALIGN_ON

	// renders
	private static CoTabStopIF.Renderer[] m_renderers;

public char [] alignOn()
{
	return m_alignOn;
}
public CoTabStopIF copy()
{
	CoTabStop t = new CoTabStop();
	copyInto( t );
	return t;
}
protected void copyInto( CoTabStop t )
{
	t.m_alignment = m_alignment;
	t.m_position = m_position;
	t.m_leader = m_leader;
	t.m_alignOn = m_alignOn;
}
public boolean equals( Object o )
{
	if
		( o instanceof CoTabStop )
	{
		CoTabStop t = (CoTabStop) o;
		return
			(
				m_alignment == t.m_alignment
			&&
				m_position == t.m_position
			&&
				m_leader == t.m_leader
			);
	} else {
		return super.equals( o );
	}
}
public int getAlignment()
{
	return m_alignment;
}
public int getLeader()
{
	return m_leader;
}
public float getPosition()
{
	return m_position;
}
public CoTabStopIF.Renderer getRenderer()
{
	if
		( m_renderers == null )
	{
		m_renderers = new CoTabStopIF.Renderer[ 6 ];
		m_renderers[ LEADER_DOTS ] = new CoTabStringLeaderRenderer( '.' );
		m_renderers[ LEADER_EQUALS ] = new CoTabStringLeaderRenderer( '=' );
		m_renderers[ LEADER_HYPHENS ] = new CoTabStringLeaderRenderer( '-' );
		m_renderers[ LEADER_NONE ] = new CoTabNoLeaderRenderer();
		m_renderers[ LEADER_THICKLINE ] = new CoTabLineLeaderRenderer( 2 );
		m_renderers[ LEADER_UNDERLINE ] = new CoTabLineLeaderRenderer( 1 );
	}
	
	return m_renderers[ m_leader ];
}
public void setAlignment( int a )
{
	m_alignment = a;
}
public void setAlignOn( String s )
{
	m_alignOn = s.toCharArray();
}
public void setLeader( int l )
{
	m_leader = l;
}
public void setPosition( float pos )
{
	m_position = pos;
}
}